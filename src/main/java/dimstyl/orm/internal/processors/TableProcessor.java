package dimstyl.orm.internal.processors;

import dimstyl.orm.annotations.Column;
import dimstyl.orm.annotations.Table;
import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.exceptions.*;
import dimstyl.orm.internal.utils.ConsoleUtils;
import dimstyl.orm.internal.utils.StringUtils;
import dimstyl.orm.metadata.TableMetadata;
import dimstyl.orm.model.Entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class TableProcessor {

    private TableProcessor() {
    }

    static TableMetadata extractMetadata(final Class<? extends Entity> entityClass, final DatabaseEngine databaseEngine)
            throws MissingTableAnnotationException, UnsupportedFieldTypeException {
        final String entityClassName = entityClass.getSimpleName();

        final Table table = extractTableAnnotation(entityClass);
        final String tableName = determineTableName(table, entityClassName);
        final var tableMetadata = new TableMetadata(tableName, table.uniqueConstraints(), new ArrayList<>());

        // Process columns
        final Field[] fields = entityClass.getDeclaredFields();
        Stream.of(fields).forEach(field -> {
            try {
                final var optionalColumnMetadata = ColumnProcessor.extractMetadata(field, databaseEngine);
                optionalColumnMetadata.ifPresent(columnMetadata -> {
                    tableMetadata.addColumnMetadata(columnMetadata);
                    ConsoleUtils.printFormatted("\t✅ Column '%s'\n", field.getName());
                });
            } catch (UnsupportedFieldTypeException e) {
                ConsoleUtils.printFormatted("\t❌ Column '%s' ➡️ ERROR: %s\n", field.getName(), e.getMessage());
                throw e;
            }
        });

        return tableMetadata;
    }

    public static String resolvePrimaryKeyColumnName(final Class<? extends Entity> entityClass)
            throws CompositePrimaryKeyException, MissingColumnAnnotationException, MissingPrimaryKeyException {
        final String entityClassName = entityClass.getSimpleName();

        // Check for composite primary keys
        if (hasCompositePrimaryKey(entityClass)) {
            final String message = String.format(
                    "Entity class '%s' has a composite primary key," +
                    "so a single primary key column name cannot be determined.",
                    entityClassName
            );
            throw new CompositePrimaryKeyException(message);
        }

        // Find the primary key column name, else throw an exception if no primary key is found
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(ColumnProcessor::isPrimaryKey)
                .map(ColumnProcessor::resolveColumnName)
                .findFirst()
                .orElseThrow(() -> {
                    final String message = String.format("No primary key found in entity class '%s'.", entityClassName);
                    return new MissingPrimaryKeyException(message);
                });
    }

    public static Map<String, String> mapFieldsToColumns(final Class<? extends Entity> entityClass)
            throws MissingColumnAnnotationException {
        // Map<String, String>: key -> entity class field name, value -> table column name
        final Map<String, String> fieldToColumnMap = new HashMap<>();
        final Field[] fields = entityClass.getDeclaredFields();

        Stream.of(fields)
                .filter(field -> field.isAnnotationPresent(Column.class))
                .forEach(field -> {
                    final String fieldName = field.getName();
                    final String columnName = ColumnProcessor.resolveColumnName(field);
                    fieldToColumnMap.put(fieldName, columnName);
                });

        return fieldToColumnMap;
    }

    static String resolveTableName(final Class<? extends Entity> entityClass) {
        final Table table = extractTableAnnotation(entityClass);
        final String entityClassName = entityClass.getSimpleName();
        return determineTableName(table, entityClassName);
    }

    private static String determineTableName(final Table table, final String entityClassName) {
        return table.name().isBlank() ? StringUtils.getDefaultName(entityClassName) : table.name();
    }

    private static Table extractTableAnnotation(final Class<? extends Entity> entityClass)
            throws MissingTableAnnotationException {
        // If the @Table annotation does not exist, throw MissingTableAnnotationException
        if (!entityClass.isAnnotationPresent(Table.class)) {
            final String message = String.format("Missing @Table annotation in entity class '%s'", entityClass);
            throw new MissingTableAnnotationException(message);
        }
        return entityClass.getDeclaredAnnotation(Table.class);
    }

    private static boolean hasCompositePrimaryKey(final Class<? extends Entity> entityClass) {
        final Field[] fields = entityClass.getDeclaredFields();
        final var count = Arrays.stream(fields)
                .filter(ColumnProcessor::isPrimaryKey)
                .count();
        return count > 1;
    }

}
