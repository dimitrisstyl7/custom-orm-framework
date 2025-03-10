package dimstyl.orm.resolvers;

import dimstyl.orm.exceptions.UnsupportedFieldTypeException;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

public interface ColumnTypeResolver {

    String resolve(final Field field) throws UnsupportedFieldTypeException;

    default String resolve(final Field field, final String databaseType, final Map<Class<?>, String> typeMap)
            throws UnsupportedFieldTypeException {
        final String entityClassName = field.getDeclaringClass().getSimpleName();
        final var fieldType = field.getType();

        if (fieldType.isEnum()) return typeMap.get(Enum.class);

        return Optional
                .ofNullable(typeMap.get(fieldType))
                .orElseThrow(() -> {
                    final String message = String.format(
                            "Unsupported field type '%s' in entity class '%s'\n%s",
                            fieldType,
                            entityClassName,
                            getSupportedDataTypes(databaseType, typeMap)
                    );
                    return new UnsupportedFieldTypeException(message);
                });
    }

    default String getSupportedDataTypes(final String databaseType, final Map<Class<?>, String> typeMap) {
        StringBuilder sb = new StringBuilder();

        sb.append("\t\t📌 Supported Data Type Mappings:\n");
        sb.append(String.format("\t\t\t%-30s -> %s Type%n", "Java Type", databaseType));
        sb.append("\t\t\t--------------------------------------------------\n");

        typeMap.forEach((javaType, sqlType) ->
                sb.append(String.format("\t\t\t%-30s -> %s%n", javaType.getSimpleName(), sqlType))
        );

        return sb.toString();
    }

}
