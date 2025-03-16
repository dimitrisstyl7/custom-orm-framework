package dimstyl.orm.internal.sql.generator;

import dimstyl.orm.annotations.UniqueConstraint;
import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.exceptions.InvalidColumnNameException;
import dimstyl.orm.internal.utils.ConsoleUtils;
import dimstyl.orm.internal.utils.FileUtils;
import dimstyl.orm.metadata.ColumnMetadata;
import dimstyl.orm.metadata.DatabaseMetadata;
import dimstyl.orm.metadata.TableMetadata;

import java.io.IOException;
import java.util.*;

/**
 * Singleton-based SQL query generator responsible for creating database schema scripts.
 * <p>
 * This generator produces `CREATE TABLE` SQL queries based on metadata and supports different
 * database engines such as H2, SQLite, and Derby.
 * </p>
 */
public enum DatabaseSchemaGenerator implements SqlQueryGenerator<List<String>, DatabaseMetadata> {

    /**
     * The singleton instance of the database schema generator.
     */
    INSTANCE;

    /**
     * Generates SQL `CREATE TABLE` queries based on {@link DatabaseMetadata}.
     * <p>
     * The generated queries are also saved as SQL script files based on the selected database engine.
     * </p>
     *
     * @param databaseMetadata Metadata describing the database schema.
     * @return A list of SQL `CREATE TABLE` statements as strings.
     * @throws InvalidColumnNameException If any column in constraints does not exist in the table definition.
     */
    @Override
    public List<String> generate(final DatabaseMetadata databaseMetadata) throws InvalidColumnNameException {
        final StringBuilder createTableQueriesBuilder = new StringBuilder();
        final DatabaseEngine databaseEngine = databaseMetadata.databaseEngine();

        ConsoleUtils.printFormatted("\n🔄️ Generating SQL 'CREATE TABLE' queries...\n");
        databaseMetadata.tableMetadataList().forEach(tableMetadata -> {
            final String createTableQuery = generateCreateTableQuery(tableMetadata, databaseEngine);
            ConsoleUtils.printFormatted("\t✅ Created query for table '%s'\n", tableMetadata.tableName());
            createTableQueriesBuilder.append(createTableQuery).append("\n\n");
        });

        final String createTableQueries = createTableQueriesBuilder.toString();

        // Save the generated SQL "CREATE TABLE" queries to a file specific to the database engine
        try {
            final String fileNamePlaceholder = switch (databaseEngine) {
                case H2 -> "db/h2/%s";
                case SQLITE -> "db/sqlite/%s";
                case DERBY -> "db/derby/%s";
            };

            final String fileName = String.format(fileNamePlaceholder, "create_tables.sql");
            final String absolutePath = FileUtils.writeToFileAndGetAbsolutePath(fileName, createTableQueries);
            ConsoleUtils.printFormatted("\n💾 SQL script saved to '%s'\n", absolutePath);
        } catch (IOException e) {
            ConsoleUtils.printFormatted("\n⚠️ Failed to write SQL 'CREATE TABLE' queries to a file\n\tERROR: %s\n", e.getMessage());
        }

        return Arrays.stream(createTableQueries.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    /**
     * Generates an SQL `CREATE TABLE` query based on {@link TableMetadata} and {@link DatabaseEngine}.
     *
     * @param tableMetadata  The metadata for the table.
     * @param databaseEngine The database engine for which the query is generated.
     * @return The SQL `CREATE TABLE` query as a string.
     * @throws InvalidColumnNameException If an invalid column name is referenced.
     */
    private String generateCreateTableQuery(final TableMetadata tableMetadata, final DatabaseEngine databaseEngine)
            throws InvalidColumnNameException {
        final String tableName = tableMetadata.tableName();
        final StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(databaseEngine != DatabaseEngine.DERBY ?
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

    /**
     * Appends column definitions to the SQL query based on the provided list of {@link ColumnMetadata}.
     *
     * @param sqlBuilder         The {@link StringBuilder} for the SQL query.
     * @param columnMetadataList The list of column metadata.
     * @return A set of column names used in the table.
     */
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
            tableColumnNames.add(column.columnName());
            if (column.primaryKey()) primaryKeys.add(column.columnName());
        }

        // Join column definitions
        sqlBuilder.append(String.join(",\n", columnDefinitions));

        // Add PRIMARY KEY constraint
        addPrimaryKeyConstraint(sqlBuilder, primaryKeys);

        return tableColumnNames;
    }

    /**
     * Adds a `PRIMARY KEY` constraint to the SQL query if necessary.
     *
     * @param sqlBuilder  The {@link StringBuilder} for the SQL query.
     * @param primaryKeys A list of primary key column names.
     */
    private void addPrimaryKeyConstraint(final StringBuilder sqlBuilder, final List<String> primaryKeys) {
        if (!primaryKeys.isEmpty()) {
            sqlBuilder.append(",\n\tPRIMARY KEY (")
                    .append(String.join(", ", primaryKeys))
                    .append(")");
        }
    }

    /**
     * Adds `UNIQUE` constraints to the table definition based on {@link UniqueConstraint}.
     *
     * @param sqlBuilder        The {@link StringBuilder} for the SQL query.
     * @param uniqueConstraints The array of unique constraints.
     * @param tableColumnNames  The set of column names in the table.
     * @param tableName         The name of the table.
     * @throws InvalidColumnNameException If an invalid column name is referenced in the constraint.
     */
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

    /**
     * Validates that all columns referenced in a {@link UniqueConstraint} exist in the table.
     *
     * @param tableColumnNames The set of column names in the table.
     * @param tableName        The name of the table.
     * @param uniqueConstraint The unique constraint being validated.
     * @return A list of valid column names for the unique constraint.
     * @throws InvalidColumnNameException If a referenced column does not exist in the table.
     */
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
