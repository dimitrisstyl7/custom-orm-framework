package dimstyl.orm.internal.sql.resolver;

import dimstyl.orm.enums.DatabaseEngine;

public final class ColumnTypeResolverFactory {

    private ColumnTypeResolverFactory() {
    }

    public static ColumnTypeResolver getResolver(final DatabaseEngine databaseEngine) {
        return switch (databaseEngine) {
            case H2 -> H2ColumnTypeResolver.INSTANCE;
            case SQLITE -> SQLiteColumnTypeResolver.INSTANCE;
            case DERBY -> DerbyColumnTypeResolver.INSTANCE;
        };
    }

}
