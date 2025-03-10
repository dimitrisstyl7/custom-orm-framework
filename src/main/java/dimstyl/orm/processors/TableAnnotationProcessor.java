package dimstyl.orm.processors;

import dimstyl.orm.annotations.Table;
import dimstyl.orm.exceptions.MissingTableAnnotationException;
import dimstyl.orm.exceptions.UnsupportedFieldTypeException;
import dimstyl.orm.marker.Entity;
import dimstyl.orm.metadata.ColumnMetadata;
import dimstyl.orm.metadata.TableMetadata;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import static dimstyl.orm.utils.StringUtils.getDefaultName;

final class TableAnnotationProcessor {

    private TableAnnotationProcessor() {
    }

    static TableMetadata process(Class<? extends Entity> entityClass)
            throws MissingTableAnnotationException, UnsupportedFieldTypeException { // TODO: add throws exceptions
        final String entityClassName = entityClass.getSimpleName();

        // If the @Database annotation does not exist, throw MissingTableAnnotationException
        if (!entityClass.isAnnotationPresent(Table.class)) {
            final String message = String.format("Missing @Table annotation in class '%s'", entityClass);
            throw new MissingTableAnnotationException(message);
        }

        final Table table = entityClass.getAnnotation(Table.class);
        final String tableName = table.name().isBlank() ? getDefaultName(entityClassName) : table.name();
        final TableMetadata tableMetadata = new TableMetadata(tableName, table.uniqueConstraints(), new ArrayList<>());

        // Columns metadata
        final Field[] fields = entityClass.getDeclaredFields();
        Stream.of(fields).forEach(field -> {
            final Optional<ColumnMetadata> optionalColumnMetadata = ColumnAnnotationProcessor.process(field);
            optionalColumnMetadata.ifPresent(tableMetadata::addColumnMetadata);
        });

        return tableMetadata;
    }

}
