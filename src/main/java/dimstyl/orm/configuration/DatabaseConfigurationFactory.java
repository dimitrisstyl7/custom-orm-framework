package dimstyl.orm.configuration;

import dimstyl.orm.enums.DatabaseType;

public final class DatabaseConfigurationFactory {

    private DatabaseConfigurationFactory() {
    }

    public static DatabaseConfiguration getConfiguration(final DatabaseType databaseType) {
        return new GenericDatabaseConfiguration(databaseType);
    }

}