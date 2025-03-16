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

final class ColumnProcessor {

    private ColumnProcessor() {
    }

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

    static boolean isPrimaryKey(final Field field) {
        return field.isAnnotationPresent(PrimaryKey.class);
    }

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

    private static String determineColumnName(final Column column, final String fieldName) {
        return column.name().isBlank() ? StringUtils.getDefaultName(fieldName) : column.name();
    }

    private static Optional<Column> extractColumnAnnotation(final Field field) {
        return Optional.ofNullable(field.getDeclaredAnnotation(Column.class));
    }

}
