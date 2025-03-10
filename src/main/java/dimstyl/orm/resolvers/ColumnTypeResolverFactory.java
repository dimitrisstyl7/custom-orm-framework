package dimstyl.orm.resolvers;

import dimstyl.orm.enums.DatabaseType;

public final class ColumnTypeResolverFactory {

    private ColumnTypeResolverFactory() {
    }

    public static ColumnTypeResolver getResolver(final DatabaseType databaseType) {
        return switch (databaseType) {
            case H2 -> H2ColumnTypeResolver.INSTANCE;
            case SQLITE -> SQLiteColumnTypeResolver.INSTANCE;
            case DERBY -> DerbyColumnTypeResolver.INSTANCE;
        };
    }

}
