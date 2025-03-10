package dimstyl.orm.processors;

import dimstyl.orm.annotations.Database;
import dimstyl.orm.configurations.DatabaseConfigurationFactory;
import dimstyl.orm.enums.DatabaseType;
import dimstyl.orm.exceptions.DatabaseConnectionException;
import dimstyl.orm.exceptions.MissingDatabaseAnnotationException;
import dimstyl.orm.exceptions.MissingTableAnnotationException;
import dimstyl.orm.exceptions.UnsupportedFieldTypeException;
import dimstyl.orm.metadata.DatabaseMetadata;
import dimstyl.orm.resolvers.ColumnTypeResolver;
import dimstyl.orm.resolvers.ColumnTypeResolverFactory;
import dimstyl.orm.utils.PrintUtils;
import dimstyl.orm.utils.StringUtils;

import java.util.ArrayList;
import java.util.stream.Stream;

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
        final String databaseName = database.name().isBlank() ? StringUtils.getDefaultName(databaseClassName) : database.name();
        final DatabaseType databaseType = database.type();
        final ColumnTypeResolver columnTypeResolver = ColumnTypeResolverFactory.getResolver(databaseType);

        // Get database configuration instance
        try (final var databaseConfiguration = DatabaseConfigurationFactory.getConfiguration(databaseType)) {
            // Connect to the database (automatically closed after try block)
            databaseConfiguration.connect(databaseName);

            final var databaseMetadata = new DatabaseMetadata(databaseName, databaseType, new ArrayList<>());

            // Process tables
            Stream.of(database.tables()).forEach(entityClass -> {
                PrintUtils.print("\n🔄️ Processing table '%s'...\n", entityClass.getSimpleName());
                final var tableMetadata = TableAnnotationProcessor.process(entityClass, columnTypeResolver);
                databaseMetadata.addTableMetadata(tableMetadata);
            });

            return databaseMetadata;
        }
    }

}