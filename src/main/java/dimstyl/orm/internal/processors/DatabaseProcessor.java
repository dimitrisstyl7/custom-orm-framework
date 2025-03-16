package dimstyl.orm.internal.processors;

import dimstyl.orm.annotations.Database;
import dimstyl.orm.configuration.DatabaseConfigurationFactory;
import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.enums.SqlOperation;
import dimstyl.orm.exceptions.DatabaseConnectionException;
import dimstyl.orm.exceptions.MissingDatabaseAnnotationException;
import dimstyl.orm.exceptions.MissingTableAnnotationException;
import dimstyl.orm.exceptions.UnsupportedFieldTypeException;
import dimstyl.orm.internal.utils.ConsoleUtils;
import dimstyl.orm.internal.utils.StringUtils;
import dimstyl.orm.metadata.DatabaseMetadata;

import java.util.ArrayList;
import java.util.stream.Stream;

public final class DatabaseProcessor {

    private DatabaseProcessor() {
    }

    public static DatabaseMetadata extractMetadata(final Class<?> databaseClass)
            throws MissingDatabaseAnnotationException, DatabaseConnectionException, MissingTableAnnotationException, UnsupportedFieldTypeException {
        final String databaseClassName = databaseClass.getName();

        // If the @Database annotation does not exist, throw MissingDatabaseAnnotationException
        if (!databaseClass.isAnnotationPresent(Database.class)) {
            final String message = String.format("Missing @Database annotation in class '%s'", databaseClassName);
            throw new MissingDatabaseAnnotationException(message);
        }

        final Database database = databaseClass.getDeclaredAnnotation(Database.class);
        final String databaseName = database.name().isBlank() ? StringUtils.getDefaultName(databaseClassName) : database.name();
        final DatabaseEngine databaseEngine = database.engine();
        final SqlOperation sqlOperation = SqlOperation.CREATE_TABLE;
        final var databaseMetadata =
                new DatabaseMetadata(databaseName, databaseEngine, sqlOperation, new ArrayList<>());

        try (final var databaseConfiguration = DatabaseConfigurationFactory.getConfiguration(databaseEngine)) {
            // Connect to the database
            databaseConfiguration.connect(databaseName, sqlOperation);

            // Process tables
            Stream.of(database.tables()).forEach(entityClass -> {
                ConsoleUtils.printFormatted("\n🔄️ Processing table '%s'...\n", entityClass.getSimpleName());
                final var tableMetadata = TableProcessor.extractMetadata(entityClass, databaseEngine);
                databaseMetadata.addTableMetadata(tableMetadata);
            });

            return databaseMetadata;
        }
    }

}