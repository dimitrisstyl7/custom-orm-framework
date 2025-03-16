package dimstyl.orm.internal.sql.resolver;

import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.exceptions.UnsupportedFieldTypeException;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Singleton enum implementation of {@link ColumnTypeResolver} for the SQLite database.
 *
 * <p>This resolver maps Java data types to their corresponding SQLite SQL data types
 * based on the official SQLite documentation.</p>
 *
 * <p>It supports the following Java-to-SQLite type mappings:</p>
 * <ul>
 *     <li>{@code String} → {@code TEXT}</li>
 *     <li>{@code boolean}, {@code Boolean} → {@code INTEGER}</li>
 *     <li>{@code short}, {@code Short} → {@code INTEGER}</li>
 *     <li>{@code int}, {@code Integer} → {@code INTEGER}</li>
 *     <li>{@code long}, {@code Long} → {@code INTEGER}</li>
 *     <li>{@code float}, {@code Float} → {@code REAL}</li>
 *     <li>{@code double}, {@code Double} → {@code REAL}</li>
 * </ul>
 *
 * @see <a href="https://www.sqlite.org/datatype3.html#affinity_name_examples">SQLite Data Types Reference</a>
 */
enum SQLiteColumnTypeResolver implements ColumnTypeResolver {

    /**
     * The singleton instance of the SQLite column type resolver.
     */
    INSTANCE;

    /**
     * Maps Java types to their corresponding SQLite SQL column types.
     */
    private static final Map<Class<?>, String> TYPE_MAP = Map.ofEntries(
            Map.entry(String.class, "TEXT"),

            Map.entry(boolean.class, "INTEGER"),
            Map.entry(Boolean.class, "INTEGER"),

            Map.entry(short.class, "INTEGER"),
            Map.entry(Short.class, "INTEGER"),
            Map.entry(int.class, "INTEGER"),
            Map.entry(Integer.class, "INTEGER"),
            Map.entry(long.class, "INTEGER"),
            Map.entry(Long.class, "INTEGER"),
            Map.entry(float.class, "REAL"),
            Map.entry(Float.class, "REAL"),
            Map.entry(double.class, "REAL"),
            Map.entry(Double.class, "REAL")
    );

    /**
     * The database engine type for this resolver.
     */
    private static final DatabaseEngine DATABASE_ENGINE = DatabaseEngine.SQLITE;

    /**
     * Resolves the SQL column type for the given Java field.
     *
     * @param field The Java field to resolve.
     * @return The corresponding SQL column type for SQLite.
     * @throws UnsupportedFieldTypeException If the field type is not supported.
     */
    @Override
    public String resolve(final Field field) throws UnsupportedFieldTypeException {
        return resolve(field, DATABASE_ENGINE.toString(), TYPE_MAP);
    }

}
