package dimstyl.orm.processors;

import dimstyl.orm.annotations.Database;
import dimstyl.orm.configurations.H2Config;
import dimstyl.orm.exceptions.DatabaseConnectionException;
import dimstyl.orm.exceptions.MissingDatabaseAnnotationException;
import dimstyl.orm.exceptions.MissingTableAnnotationException;
import dimstyl.orm.exceptions.UnsupportedFieldTypeException;
import dimstyl.orm.metadata.DatabaseMetadata;
import dimstyl.orm.resolvers.ColumnTypeResolver;
import dimstyl.orm.resolvers.ColumnTypeResolverFactory;

import java.util.ArrayList;
import java.util.stream.Stream;

import static dimstyl.orm.enums.DatabaseType.*;
import static dimstyl.orm.utils.StringUtils.getDefaultName;

public final class DatabaseAnnotationProcessor {

    private DatabaseAnnotationProcessor() {
    }

    public static DatabaseMetadata process(final Class<?> databaseClass)
            throws MissingDatabaseAnnotationException, DatabaseConnectionException, MissingTableAnnotationException, UnsupportedFieldTypeException {
        final String databaseClassName = databaseClass.getName();

        // If the @Database annotation does not exist, throw MissingDatabaseAnnotationException
        if (!databaseClass.isAnnotationPresent(Database.class)) {
            final String message = String.format("Missing @Database annotation in class '%s'", databaseClassName);
            throw new MissingDatabaseAnnotationException(message);
        }

        final Database database = databaseClass.getAnnotation(Database.class);
        final String databaseName = database.name().isBlank() ? getDefaultName(databaseClassName) : database.name();
        ColumnTypeResolver columnTypeResolver;

        switch (database.type()) {
            case H2 -> {
                columnTypeResolver = ColumnTypeResolverFactory.getResolver(H2);
                H2Config.connect(databaseName);
            }
            case SQLITE -> {
                columnTypeResolver = ColumnTypeResolverFactory.getResolver(SQLITE);
                /* TODO: */
            }
            case DERBY -> {
                columnTypeResolver = ColumnTypeResolverFactory.getResolver(DERBY);
                /* TODO: */
            }
            default -> columnTypeResolver = ColumnTypeResolverFactory.getResolver(UNKNOWN);
        }

        final var databaseMetadata = new DatabaseMetadata(databaseName, database.type(), new ArrayList<>());

        // Tables metadata
        Stream.of(database.tables()).forEach(entityClass -> {
            final var tableMetadata = TableAnnotationProcessor.process(entityClass, columnTypeResolver);
            databaseMetadata.addTableMetadata(tableMetadata);
        });

        return databaseMetadata;
    }

}
