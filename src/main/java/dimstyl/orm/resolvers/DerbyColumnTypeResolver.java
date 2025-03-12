package dimstyl.orm.resolvers;

import dimstyl.orm.enums.DatabaseType;
import dimstyl.orm.exceptions.UnsupportedFieldTypeException;

import java.lang.reflect.Field;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

enum DerbyColumnTypeResolver implements ColumnTypeResolver {

    INSTANCE;

    // https://db.apache.org/derby/docs/10.2/ref/crefsqlj31068.html
    private final Map<Class<?>, String> TYPE_MAP = Map.ofEntries(
            Map.entry(String.class, "VARCHAR(255)"),

            Map.entry(Enum.class, "VARCHAR(255)"),

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
            Map.entry(Double.class, "DOUBLE"),

            Map.entry(java.sql.Date.class, "DATE"),
            Map.entry(LocalDate.class, "DATE"),
            Map.entry(Time.class, "TIME"),
            Map.entry(LocalTime.class, "TIME"),
            Map.entry(Timestamp.class, "TIMESTAMP"),
            Map.entry(java.util.Date.class, "TIMESTAMP"),
            Map.entry(LocalDateTime.class, "TIMESTAMP")
    );

    private final DatabaseType DATABASE_TYPE = DatabaseType.DERBY;

    @Override
    public String resolve(final Field field) throws UnsupportedFieldTypeException {
        return resolve(field, DATABASE_TYPE.toString(), TYPE_MAP);
    }

}
