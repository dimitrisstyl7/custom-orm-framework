package dimstyl.orm.internal.sql.resolver;

import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.exceptions.UnsupportedFieldTypeException;

import java.lang.reflect.Field;
import java.util.Map;

enum DerbyColumnTypeResolver implements ColumnTypeResolver {

    INSTANCE;

    // https://db.apache.org/derby/docs/10.2/ref/crefsqlj31068.html
    private final Map<Class<?>, String> TYPE_MAP = Map.ofEntries(
            Map.entry(String.class, "VARCHAR(255)"),

            Map.entry(boolean.class, "SMALLINT"),
            Map.entry(Boolean.class, "SMALLINT"),

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

    private final DatabaseEngine DATABASE_TYPE = DatabaseEngine.DERBY;

    @Override
    public String resolve(final Field field) throws UnsupportedFieldTypeException {
        return resolve(field, DATABASE_TYPE.toString(), TYPE_MAP);
    }

}
