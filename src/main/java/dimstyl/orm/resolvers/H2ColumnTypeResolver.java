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

enum H2ColumnTypeResolver implements ColumnTypeResolver {

    INSTANCE;

    // https://h2database.com/html/datatypes.html
    private final Map<Class<?>, String> TYPE_MAP = Map.ofEntries(
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
            Map.entry(Double.class, "DOUBLE"),

            Map.entry(java.sql.Date.class, "DATE"),
            Map.entry(LocalDate.class, "DATE"),
            Map.entry(Time.class, "TIME"),
            Map.entry(LocalTime.class, "TIME"),
            Map.entry(Timestamp.class, "TIMESTAMP"),
            Map.entry(java.util.Date.class, "TIMESTAMP"),
            Map.entry(LocalDateTime.class, "TIMESTAMP")
    );

    @Override
    public String resolve(Field field) throws UnsupportedFieldTypeException {
        final String entityClassName = field.getDeclaringClass().getSimpleName();
        final var fieldType = field.getType();

        if (fieldType.isEnum()) return "ENUM";

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
