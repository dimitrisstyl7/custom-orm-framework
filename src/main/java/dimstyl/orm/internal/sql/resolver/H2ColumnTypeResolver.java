package dimstyl.orm.internal.sql.resolver;

import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.exceptions.UnsupportedFieldTypeException;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Singleton enum implementation of {@link ColumnTypeResolver} for the H2 database.
 *
 * <p>This resolver maps Java data types to their corresponding H2 SQL data types
 * based on the official H2 database documentation.</p>
 *
 * <p>It supports the following Java-to-SQL type mappings:</p>
 * <ul>
 *     <li>{@code String} → {@code VARCHAR(255)}</li>
 *     <li>{@code boolean}, {@code Boolean} → {@code BOOLEAN}</li>
 *     <li>{@code short}, {@code Short} → {@code SMALLINT}</li>
 *     <li>{@code int}, {@code Integer} → {@code INTEGER}</li>
 *     <li>{@code long}, {@code Long} → {@code BIGINT}</li>
 *     <li>{@code float}, {@code Float} → {@code REAL}</li>
 *     <li>{@code double}, {@code Double} → {@code DOUBLE}</li>
 * </ul>
 *
 * @see <a href="https://h2database.com/html/datatypes.html">H2 Data Types Reference</a>
 */
enum H2ColumnTypeResolver implements ColumnTypeResolver {

    /**
     * The singleton instance of the H2 column type resolver.
     */
    INSTANCE;

    /**
     * Maps Java types to their corresponding H2 SQL column types.
     */
    private static final Map<Class<?>, String> TYPE_MAP = Map.ofEntries(
            Map.entry(String.class, "VARCHAR(255)"),

            Map.entry(boolean.class, "BOOLEAN"),
            Map.entry(Boolean.class, "BOOLEAN"),

            Map.entry(short.class, "SMALLINT"),
            Map.entry(Short.class, "SMALLINT"),
            Map.entry(int.class, "INTEGER"),
            Map.entry(Integer.class, "INTEGER"),
            Map.entry(long.class, "BIGINT"),
            Map.entry(Long.class, "BIGINT"),
            Map.entry(float.class, "REAL"),
            Map.entry(Float.class, "REAL"),
            Map.entry(double.class, "DOUBLE"),
            Map.entry(Double.class, "DOUBLE")
    );

    /**
     * The database engine type for this resolver.
     */
    private static final DatabaseEngine DATABASE_ENGINE = DatabaseEngine.H2;

    /**
     * Resolves the SQL column type for the given Java field.
     *
     * @param field The Java field to resolve.
     * @return The corresponding SQL column type for H2.
     * @throws UnsupportedFieldTypeException If the field type is not supported.
     */
    @Override
    public String resolve(final Field field) throws UnsupportedFieldTypeException {
        return resolve(field, DATABASE_ENGINE.toString(), TYPE_MAP);
    }

}
