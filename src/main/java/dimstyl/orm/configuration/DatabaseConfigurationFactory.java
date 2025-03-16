package dimstyl.orm.configuration;

import dimstyl.orm.enums.DatabaseEngine;

public final class DatabaseConfigurationFactory {

    private DatabaseConfigurationFactory() {
    }

    public static DatabaseConfiguration getConfiguration(final DatabaseEngine databaseEngine) {
        GenericDatabaseConfiguration.INSTANCE.setDatabaseEngine(databaseEngine);
        return GenericDatabaseConfiguration.INSTANCE;
    }

}