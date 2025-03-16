package dimstyl.orm.internal.processors;

import dimstyl.orm.annotations.Database;
import dimstyl.orm.annotations.Table;
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

/**
 * Utility class responsible for processing metadata related to databases in ORM.
 * <p>
 * This class extracts metadata from classes annotated with {@link Database}, establishes
 * database connections, and processes associated tables.
 * </p>
 */
public final class DatabaseProcessor {

    /**
     * Private constructor to prevent instantiation.
     */
    private DatabaseProcessor() {
    }

    /**
     * Extracts metadata from a given class annotated with {@link Database}.
     * <p>
     * This method validates the presence of the {@link Database} annotation, retrieves the database
     * name, determines the database engine, and processes associated tables. It also connects
     * to the database using the {@link DatabaseConfigurationFactory}.
     * </p>
     *
     * @param databaseClass The class representing the database configuration.
     * @return The extracted {@link DatabaseMetadata}, containing database name, engine, SQL operation,
     * and table metadata.
     * @throws MissingDatabaseAnnotationException If the class is not annotated with {@link Database}.
     * @throws DatabaseConnectionException        If a connection to the database cannot be established.
     * @throws MissingTableAnnotationException    If a table entity is not annotated with {@link Table}.
     * @throws UnsupportedFieldTypeException      If a table contains an unsupported field type.
     */
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