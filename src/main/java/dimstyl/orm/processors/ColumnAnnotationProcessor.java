package dimstyl.orm.processors;

import dimstyl.orm.annotations.Column;
import dimstyl.orm.annotations.PrimaryKey;
import dimstyl.orm.exceptions.UnsupportedFieldTypeException;
import dimstyl.orm.metadata.ColumnMetadata;
import dimstyl.orm.utils.H2ColumnTypeResolver;

import java.lang.reflect.Field;
import java.util.Optional;

import static dimstyl.orm.utils.StringUtils.getDefaultName;

final class ColumnAnnotationProcessor {

    private ColumnAnnotationProcessor() {
    }

    static Optional<ColumnMetadata> process(final Field field) throws UnsupportedFieldTypeException {
        // If the @Column annotation does not exist, return Optional.empty()
        if (!field.isAnnotationPresent(Column.class)) return Optional.empty();

        final Column column = field.getAnnotation(Column.class);
        final String columnName = column.name().isBlank() ? getDefaultName(field.getName()) : column.name();
        final String columnType = H2ColumnTypeResolver.getH2ColumnType(field);
        final boolean hasPrimaryKey = field.isAnnotationPresent(PrimaryKey.class);

        final ColumnMetadata columnMetadata = ColumnMetadata.builder()
                .columnName(columnName)
                .columnType(columnType)
                .primaryKey(hasPrimaryKey)
                .nullable(column.nullable())
                .unique(column.unique())
                .build();

        return Optional.of(columnMetadata);
    }

}
