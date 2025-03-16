package dimstyl.orm.internal.sql.resolver;

import dimstyl.orm.enums.DatabaseEngine;

/**
 * Factory class for obtaining the appropriate {@link ColumnTypeResolver} instance
 * based on the specified {@link DatabaseEngine}.
 *
 * <p>This factory follows a singleton approach by providing pre-defined instances
 * of {@link ColumnTypeResolver} implementations for different database engines.</p>
 *
 * <p>It supports the following database engines:
 * <ul>
 *     <li>{@link DatabaseEngine#H2} - Returns {@link H2ColumnTypeResolver#INSTANCE}</li>
 *     <li>{@link DatabaseEngine#SQLITE} - Returns {@link SQLiteColumnTypeResolver#INSTANCE}</li>
 *     <li>{@link DatabaseEngine#DERBY} - Returns {@link DerbyColumnTypeResolver#INSTANCE}</li>
 * </ul>
 * </p>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>
 *     ColumnTypeResolver resolver = ColumnTypeResolverFactory.getResolver(DatabaseEngine.H2);
 *     String sqlType = resolver.resolve(myField);
 * </pre>
 */
public final class ColumnTypeResolverFactory {

    /**
     * Private constructor to prevent instantiation.
     */
    private ColumnTypeResolverFactory() {
    }

    /**
     * Returns the appropriate {@link ColumnTypeResolver} instance based on the given {@link DatabaseEngine}.
     *
     * @param databaseEngine The database engine for which a column type resolver is required.
     * @return A {@link ColumnTypeResolver} implementation corresponding to the specified database engine.
     */
    public static ColumnTypeResolver getResolver(final DatabaseEngine databaseEngine) {
        return switch (databaseEngine) {
            case H2 -> H2ColumnTypeResolver.INSTANCE;
            case SQLITE -> SQLiteColumnTypeResolver.INSTANCE;
            case DERBY -> DerbyColumnTypeResolver.INSTANCE;
        };
    }

}
