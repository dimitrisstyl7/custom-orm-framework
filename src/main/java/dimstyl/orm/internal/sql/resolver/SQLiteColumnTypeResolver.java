package dimstyl.orm.internal.sql.resolver;

import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.exceptions.UnsupportedFieldTypeException;

import java.lang.reflect.Field;
import java.util.Map;

enum SQLiteColumnTypeResolver implements ColumnTypeResolver {

    INSTANCE;

    // https://www.sqlite.org/datatype3.html#affinity_name_examples
    private final Map<Class<?>, String> TYPE_MAP = Map.ofEntries(
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

    private final DatabaseEngine DATABASE_TYPE = DatabaseEngine.SQLITE;

    @Override
    public String resolve(final Field field) throws UnsupportedFieldTypeException {
        return resolve(field, DATABASE_TYPE.toString(), TYPE_MAP);
    }

}
