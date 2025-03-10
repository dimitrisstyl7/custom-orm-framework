package dimstyl.orm.configurations;

import dimstyl.orm.enums.DatabaseType;

public class DatabaseConfigurationFactory {

    private DatabaseConfigurationFactory() {
    }

    public static DatabaseConfiguration getConfiguration(final DatabaseType databaseType) {
        return switch (databaseType) {
            case H2 -> H2Configuration.INSTANCE;
            case SQLITE -> SQLiteConfiguration.INSTANCE;
            case DERBY -> DerbyConfiguration.INSTANCE;
        };
    }

}
