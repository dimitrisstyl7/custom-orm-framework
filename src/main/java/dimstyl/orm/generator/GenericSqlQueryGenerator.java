package dimstyl.orm.generator;

import dimstyl.orm.annotations.UniqueConstraint;
import dimstyl.orm.enums.DatabaseType;
import dimstyl.orm.exceptions.InvalidColumnNameException;
import dimstyl.orm.metadata.ColumnMetadata;
import dimstyl.orm.metadata.DatabaseMetadata;
import dimstyl.orm.metadata.TableMetadata;
import dimstyl.orm.utils.ConsoleUtils;
import dimstyl.orm.utils.FileUtils;

import java.io.IOException;
import java.util.*;

public enum GenericSqlQueryGenerator implements SqlQueryGenerator {

    INSTANCE;

    @Override
    public List<String> generateDatabaseSchema(final DatabaseMetadata databaseMetadata)
            throws InvalidColumnNameException {
        final StringBuilder createTableQueriesBuilder = new StringBuilder();
        final DatabaseType databaseType = databaseMetadata.databaseType();

        ConsoleUtils.printFormatted("\n🔄️ Generating SQL create table queries...\n");
        databaseMetadata.tableMetadataList().forEach(tableMetadata -> {
            final String createTableQuery = generateCreateTableQuery(tableMetadata, databaseType);
            ConsoleUtils.printFormatted("\t✅ Created query for table '%s'\n", tableMetadata.tableName());
            createTableQueriesBuilder.append(createTableQuery).append("\n\n");
        });

        final String createTableQueries = createTableQueriesBuilder.toString();

        // Save the generated SQL "CREATE TABLE" queries to a file specific to the database type
        try {
            final String fileNamePlaceholder = switch (databaseType) {
                case H2 -> "db/h2/%s";
                case SQLITE -> "db/sqlite/%s";
                case DERBY -> "db/derby/%s";
            };

            final String fileName = String.format(fileNamePlaceholder, "create_tables.sql");
            final String fullPath = FileUtils.writeToFileAndGetPath(fileName, createTableQueries);
            ConsoleUtils.printFormatted("\n💾 SQL script saved to '%s'\n", fullPath);
        } catch (IOException e) {
            ConsoleUtils.printFormatted("\n⚠️ Failed to write SQL create table queries to a file\n\tERROR: %s\n", e.getMessage());
        }

        return Arrays.stream(createTableQueries.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private String generateCreateTableQuery(final TableMetadata tableMetadata, final DatabaseType databaseType)
            throws InvalidColumnNameException {
        final String tableName = tableMetadata.tableName();
        final StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(databaseType != DatabaseType.DERBY ?
                String.format("CREATE TABLE IF NOT EXISTS %s (\n", tableName) :
                String.format("CREATE TABLE %s (\n", tableName)
        );

        // Append column definitions
        final List<ColumnMetadata> columnMetadataList = tableMetadata.columnMetadataList();
        final Set<String> tableColumnNames = addColumnDefinitions(sqlBuilder, columnMetadataList);

        // Add table-level UNIQUE constraints
        final UniqueConstraint[] uniqueConstraints = tableMetadata.uniqueConstraints();
        addUniqueConstraints(sqlBuilder, uniqueConstraints, tableColumnNames, tableName);

        sqlBuilder.append("\n);");
        return sqlBuilder.toString();
    }

    private Set<String> addColumnDefinitions(final StringBuilder sqlBuilder,
                                             final List<ColumnMetadata> columnMetadataList) {
        final List<String> columnDefinitions = new ArrayList<>();
        final List<String> primaryKeys = new ArrayList<>();
        final Set<String> tableColumnNames = new HashSet<>();

        for (ColumnMetadata column : columnMetadataList) {
            String columnDefinition = String.format(
                    "\t%s %s%s%s",
                    column.columnName(),
                    column.columnType(),
                    column.nullable() ? "" : " NOT NULL",
                    column.unique() && !column.primaryKey() ? " UNIQUE" : ""
            );
            columnDefinitions.add(columnDefinition);

            if (column.primaryKey()) primaryKeys.add(column.columnName());

            tableColumnNames.add(column.columnName());
        }

        // Join column definitions
        sqlBuilder.append(String.join(",\n", columnDefinitions));

        // Add PRIMARY KEY constraint
        addPrimaryKeyConstraint(sqlBuilder, primaryKeys);

        return tableColumnNames;
    }

    private void addPrimaryKeyConstraint(final StringBuilder sqlBuilder, final List<String> primaryKeys) {
        if (!primaryKeys.isEmpty()) {
            sqlBuilder.append(",\n\tPRIMARY KEY (")
                    .append(String.join(", ", primaryKeys))
                    .append(")");
        }
    }

    private void addUniqueConstraints(final StringBuilder sqlBuilder,
                                      final UniqueConstraint[] uniqueConstraints,
                                      final Set<String> tableColumnNames,
                                      final String tableName) throws InvalidColumnNameException {
        for (UniqueConstraint uniqueConstraint : uniqueConstraints) {
            List<String> constraintColumns = validateConstraint(tableColumnNames, tableName, uniqueConstraint);

            sqlBuilder.append(",\n\tUNIQUE (")
                    .append(String.join(", ", constraintColumns))
                    .append(")");
        }
    }

    private List<String> validateConstraint(final Set<String> tableColumnNames,
                                            final String tableName,
                                            final UniqueConstraint uniqueConstraint) throws InvalidColumnNameException {
        List<String> constraintColumns = Arrays.asList(uniqueConstraint.columnNames());

        for (String columnName : constraintColumns) {
            if (!tableColumnNames.contains(columnName)) {
                final String message = String.format(
                        "Column '%s' does not exist in table '%s'",
                        columnName,
                        tableName
                );
                throw new InvalidColumnNameException(message);
            }
        }
        return constraintColumns;
    }

}
