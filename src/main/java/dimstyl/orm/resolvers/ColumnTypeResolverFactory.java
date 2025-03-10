package dimstyl.orm.resolvers;

import dimstyl.orm.enums.DatabaseType;
import dimstyl.orm.exceptions.UnknownDatabaseTypeException;

public final class ColumnTypeResolverFactory {

    private ColumnTypeResolverFactory() {
    }

    public static ColumnTypeResolver getResolver(DatabaseType databaseType) throws UnknownDatabaseTypeException {
        return switch (databaseType) {
            case H2 -> H2ColumnTypeResolver.INSTANCE;
            case SQLITE -> SQLiteColumnTypeResolver.INSTANCE;
            case DERBY -> DerbyColumnTypeResolver.INSTANCE;
            case UNKNOWN -> throw new UnknownDatabaseTypeException();
        };
    }

}
