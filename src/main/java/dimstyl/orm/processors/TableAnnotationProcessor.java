package dimstyl.orm.processors;

import dimstyl.orm.annotations.Table;
import dimstyl.orm.exceptions.MissingTableAnnotationException;
import dimstyl.orm.exceptions.UnsupportedFieldTypeException;
import dimstyl.orm.marker.Entity;
import dimstyl.orm.metadata.TableMetadata;
import dimstyl.orm.resolvers.ColumnTypeResolver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.stream.Stream;

import static dimstyl.orm.utils.StringUtils.getDefaultName;

final class TableAnnotationProcessor {

    private TableAnnotationProcessor() {
    }

    static TableMetadata process(final Class<? extends Entity> entityClass,
                                 final ColumnTypeResolver columnTypeResolver)
            throws MissingTableAnnotationException, UnsupportedFieldTypeException {
        final String entityClassName = entityClass.getSimpleName();

        // If the @Database annotation does not exist, throw MissingTableAnnotationException
        if (!entityClass.isAnnotationPresent(Table.class)) {
            final String message = String.format("Missing @Table annotation in class '%s'", entityClass);
            throw new MissingTableAnnotationException(message);
        }

        final Table table = entityClass.getAnnotation(Table.class);
        final String tableName = table.name().isBlank() ? getDefaultName(entityClassName) : table.name();
        final var tableMetadata = new TableMetadata(tableName, table.uniqueConstraints(), new ArrayList<>());

        // Columns metadata
        final Field[] fields = entityClass.getDeclaredFields();
        Stream.of(fields).forEach(field -> {
            final var optionalColumnMetadata = ColumnAnnotationProcessor.process(field, columnTypeResolver);
            optionalColumnMetadata.ifPresent(tableMetadata::addColumnMetadata);
        });

        return tableMetadata;
    }

}
