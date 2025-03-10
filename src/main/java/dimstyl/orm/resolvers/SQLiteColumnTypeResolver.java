package dimstyl.orm.resolvers;

import dimstyl.orm.exceptions.UnsupportedFieldTypeException;

import java.lang.reflect.Field;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

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
            Map.entry(Double.class, "REAL"),

            Map.entry(java.sql.Date.class, "NUMERIC"),
            Map.entry(LocalDate.class, "NUMERIC"),
            Map.entry(Time.class, "NUMERIC"),
            Map.entry(LocalTime.class, "NUMERIC"),
            Map.entry(Timestamp.class, "NUMERIC"),
            Map.entry(java.util.Date.class, "NUMERIC"),
            Map.entry(LocalDateTime.class, "NUMERIC")
    );

    @Override
    public String resolve(Field field) throws UnsupportedFieldTypeException {
        final String entityClassName = field.getDeclaringClass().getSimpleName();
        final var fieldType = field.getType();

        if (fieldType.isEnum()) return "TEXT";

        return Optional
                .ofNullable(TYPE_MAP.get(fieldType))
                .orElseThrow(() -> {
                    final String message = String.format(
                            "Unsupported field type %s in entity class %s",
                            fieldType,
                            entityClassName
                    );
                    return new UnsupportedFieldTypeException(message);
                });
    }

}
