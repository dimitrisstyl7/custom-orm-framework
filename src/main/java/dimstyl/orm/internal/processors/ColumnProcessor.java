package dimstyl.orm.internal.processors;

import dimstyl.orm.annotations.Column;
import dimstyl.orm.annotations.PrimaryKey;
import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.exceptions.MissingColumnAnnotationException;
import dimstyl.orm.exceptions.UnsupportedFieldTypeException;
import dimstyl.orm.internal.sql.resolver.ColumnTypeResolver;
import dimstyl.orm.internal.sql.resolver.ColumnTypeResolverFactory;
import dimstyl.orm.internal.utils.StringUtils;
import dimstyl.orm.metadata.ColumnMetadata;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Utility class responsible for processing column-related metadata in entity classes.
 * <p>
 * This class extracts metadata from fields annotated with {@link Column} and resolves
 * information such as column name, type, primary key status, and constraints.
 * </p>
 */
final class ColumnProcessor {

    /**
     * Private constructor to prevent instantiation.
     */
    private ColumnProcessor() {
    }

    /**
     * Extracts metadata from a given field and constructs a {@link ColumnMetadata} object.
     * <p>
     * If the field is not annotated with {@link Column}, an empty {@link Optional} is returned.
     * Otherwise, column properties such as name, type, primary key status, nullability,
     * and uniqueness are extracted.
     * </p>
     *
     * @param field          The field to process.
     * @param databaseEngine The {@link DatabaseEngine} for which the column type should be resolved.
     * @return An {@link Optional} containing the {@link ColumnMetadata} if the field is annotated,
     * otherwise an empty {@link Optional}.
     * @throws UnsupportedFieldTypeException If the field type is not supported by the ORM framework.
     */
    static Optional<ColumnMetadata> extractMetadata(final Field field, final DatabaseEngine databaseEngine)
            throws UnsupportedFieldTypeException {
        final Optional<Column> optionalColumn = extractColumnAnnotation(field);

        // If the @Column annotation does not exist, return Optional.empty()
        if (optionalColumn.isEmpty()) return Optional.empty();

        final Column column = optionalColumn.get();
        final String fieldName = field.getName();
        final String columnName = determineColumnName(column, fieldName);
        final ColumnTypeResolver columnTypeResolver = ColumnTypeResolverFactory.getResolver(databaseEngine);
        final String columnType = columnTypeResolver.resolve(field);
        final boolean isPrimaryKey = isPrimaryKey(field);

        final ColumnMetadata columnMetadata = ColumnMetadata.builder()
                .columnName(columnName)
                .columnType(columnType)
                .primaryKey(isPrimaryKey)
                .nullable(column.nullable())
                .unique(column.unique())
                .build();

        return Optional.of(columnMetadata);
    }

    /**
     * Checks whether the given field is annotated as a primary key.
     *
     * @param field The field to check.
     * @return {@code true} if the field is annotated with {@link PrimaryKey}, otherwise {@code false}.
     */
    static boolean isPrimaryKey(final Field field) {
        return field.isAnnotationPresent(PrimaryKey.class);
    }

    /**
     * Resolves the column name for a given field.
     * <p>
     * If the field is annotated with {@link Column}, its specified name is used. Otherwise,
     * an exception is thrown if the annotation is missing.
     * </p>
     *
     * @param field The field whose column name needs to be resolved.
     * @return The resolved column name.
     * @throws MissingColumnAnnotationException If the field is not annotated with {@link Column}.
     */
    static String resolveColumnName(final Field field) throws MissingColumnAnnotationException {
        final String fieldName = field.getName();
        final Optional<Column> optionalColumn = extractColumnAnnotation(field);
        final Column column = optionalColumn.orElseThrow(() -> {
            final String entityClassName = field.getDeclaringClass().getSimpleName();
            final String message = String.format(
                    "Field '%s' in entity class '%s' is missing the @Column annotation.",
                    fieldName,
                    entityClassName
            );
            return new MissingColumnAnnotationException(message);
        });
        return determineColumnName(column, fieldName);
    }

    /**
     * Determines the column name based on the {@link Column} annotation.
     * <p>
     * If the annotation specifies a column name, it is returned. Otherwise, a default
     * name is derived from the field name using {@link StringUtils#getDefaultName(String)}.
     * </p>
     *
     * @param column    The {@link Column} annotation instance.
     * @param fieldName The name of the field.
     * @return The resolved column name.
     */
    private static String determineColumnName(final Column column, final String fieldName) {
        return column.name().isBlank() ? StringUtils.getDefaultName(fieldName) : column.name();
    }

    /**
     * Extracts the {@link Column} annotation from the given field, if present.
     *
     * @param field The field to inspect.
     * @return An {@link Optional} containing the {@link Column} annotation if present, otherwise empty.
     */
    private static Optional<Column> extractColumnAnnotation(final Field field) {
        return Optional.ofNullable(field.getDeclaredAnnotation(Column.class));
    }

}
