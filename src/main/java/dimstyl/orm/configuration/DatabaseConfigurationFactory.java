package dimstyl.orm.configuration;

import dimstyl.orm.enums.DatabaseEngine;

/**
 * A factory class for obtaining database configuration instances.
 * <p>
 * This factory provides a method to retrieve a {@link DatabaseConfiguration} implementation
 * based on the specified {@link DatabaseEngine}. It ensures that the same configuration
 * instance is used consistently.
 * </p>
 */
public final class DatabaseConfigurationFactory {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DatabaseConfigurationFactory() {
    }

    /**
     * Retrieves a database configuration instance for the specified database engine.
     * <p>
     * This method sets the database engine for the shared {@link GenericDatabaseConfiguration} instance
     * and returns it for use.
     * </p>
     *
     * @param databaseEngine The {@link DatabaseEngine} to configure.
     * @return A {@link DatabaseConfiguration} instance configured for the specified engine.
     */
    public static DatabaseConfiguration getConfiguration(final DatabaseEngine databaseEngine) {
        GenericDatabaseConfiguration.INSTANCE.setDatabaseEngine(databaseEngine);
        return GenericDatabaseConfiguration.INSTANCE;
    }

}