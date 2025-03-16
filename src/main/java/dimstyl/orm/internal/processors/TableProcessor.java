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

/**
 * Utility class responsible for processing table metadata for ORM.
 * <p>
 * This class extracts and processes table metadata from entity classes annotated with {@link Table},
 * including table names, column mappings, and primary key resolution.
 * </p>
 */
public final class TableProcessor {

    /**
     * Private constructor to prevent instantiation.
     */
    private TableProcessor() {
    }

    /**
     * Extracts table metadata from an entity class.
     * <p>
     * This method retrieves table-related metadata such as table name, unique constraints,
     * and column metadata, using reflection to process annotated fields.
     * </p>
     *
     * @param entityClass    The entity class annotated with {@link Table}.
     * @param databaseEngine The {@link DatabaseEngine} used for column type resolution.
     * @return {@link TableMetadata} containing table details.
     * @throws MissingTableAnnotationException If the entity class lacks the {@link Table} annotation.
     * @throws UnsupportedFieldTypeException   If a field has an unsupported type.
     */
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

    /**
     * Resolves the primary key column name for a given entity class.
     * <p>
     * Ensures that a valid primary key is present and handles composite primary key cases.
     * </p>
     *
     * @param entityClass The entity class containing the primary key.
     * @return The name of the primary key column.
     * @throws CompositePrimaryKeyException     If multiple primary key annotations are detected.
     * @throws MissingColumnAnnotationException If the primary key field lacks a {@link Column} annotation.
     * @throws MissingPrimaryKeyException       If no primary key is found.
     */
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

    /**
     * Maps entity class fields to their corresponding table column names.
     * <p>
     * Uses reflection to scan fields annotated with {@link Column} and map their names
     * to column names as defined in the annotation.
     * </p>
     *
     * @param entityClass The entity class to be processed.
     * @return A map where keys are field names and values are corresponding column names.
     * @throws MissingColumnAnnotationException If a field lacks a {@link Column} annotation.
     */
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

    /**
     * Resolves the table name for a given entity class.
     * <p>
     * If the {@link Table} annotation specifies a name, it is used; otherwise,
     * a default name is derived from the entity class name.
     * </p>
     *
     * @param entityClass The entity class.
     * @return The resolved table name.
     */
    static String resolveTableName(final Class<? extends Entity> entityClass) {
        final Table table = extractTableAnnotation(entityClass);
        final String entityClassName = entityClass.getSimpleName();
        return determineTableName(table, entityClassName);
    }

    /**
     * Determines the table name based on the {@link Table} annotation or defaults to a formatted class name.
     *
     * @param table           The extracted {@link Table} annotation.
     * @param entityClassName The entity class name.
     * @return The resolved table name.
     */
    private static String determineTableName(final Table table, final String entityClassName) {
        return table.name().isBlank() ? StringUtils.getDefaultName(entityClassName) : table.name();
    }

    /**
     * Extracts the {@link Table} annotation from an entity class.
     *
     * @param entityClass The entity class to be processed.
     * @return The extracted {@link Table} annotation.
     * @throws MissingTableAnnotationException If the entity class lacks a {@link Table} annotation.
     */
    private static Table extractTableAnnotation(final Class<? extends Entity> entityClass)
            throws MissingTableAnnotationException {
        // If the @Table annotation does not exist, throw MissingTableAnnotationException
        if (!entityClass.isAnnotationPresent(Table.class)) {
            final String message = String.format("Missing @Table annotation in entity class '%s'", entityClass);
            throw new MissingTableAnnotationException(message);
        }
        return entityClass.getDeclaredAnnotation(Table.class);
    }

    /**
     * Determines if an entity class has a composite primary key (multiple {@link Column} fields marked as primary keys).
     *
     * @param entityClass The entity class to check.
     * @return {@code true} if the class has multiple primary key fields, {@code false} otherwise.
     */
    private static boolean hasCompositePrimaryKey(final Class<? extends Entity> entityClass) {
        final Field[] fields = entityClass.getDeclaredFields();
        final var count = Arrays.stream(fields)
                .filter(ColumnProcessor::isPrimaryKey)
                .count();
        return count > 1;
    }

}
