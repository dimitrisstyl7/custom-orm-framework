package dimstyl.orm.internal.sql.resolver;

import dimstyl.orm.exceptions.UnsupportedFieldTypeException;
import dimstyl.orm.internal.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

/**
 * Interface for resolving SQL column types based on Java field types.
 *
 * <p>This interface defines methods for mapping Java data types to their corresponding SQL column types
 * based on the target database. Implementations are expected to provide type mappings for different databases.</p>
 *
 * <p>It provides two resolution methods:
 * <ul>
 *     <li>{@link #resolve(Field)} - Resolves the SQL type for a given field.</li>
 *     <li>{@link #resolve(Field, String, Map)} - Resolves the SQL type with additional database-specific type mapping.</li>
 * </ul>
 * </p>
 */
public interface ColumnTypeResolver {

    /**
     * Resolves the SQL column type for the given Java field.
     *
     * @param field The Java field for which the SQL type is to be determined.
     * @return A string representing the SQL column type.
     * @throws UnsupportedFieldTypeException If the field type is not supported.
     */
    String resolve(final Field field) throws UnsupportedFieldTypeException;

    /**
     * Resolves the SQL column type based on the provided field, database engine, and type mapping.
     *
     * <p>This method checks the provided type map for a corresponding SQL type.
     * If no mapping is found, an {@link UnsupportedFieldTypeException} is thrown.</p>
     *
     * @param field          The Java field to be resolved.
     * @param databaseEngine The target database engine (e.g., "H2", "SQLite").
     * @param typeMap        A map of Java classes to SQL column type representations.
     * @return The SQL column type as a string.
     * @throws UnsupportedFieldTypeException If the field type is not supported in the given database.
     */
    default String resolve(final Field field, final String databaseEngine, final Map<Class<?>, String> typeMap)
            throws UnsupportedFieldTypeException {
        final String entityClassName = field.getDeclaringClass().getSimpleName();
        final var fieldType = field.getType();

        return Optional
                .ofNullable(typeMap.get(fieldType))
                .orElseThrow(() -> {
                    final String message = String.format(
                            "Unsupported field type '%s' in entityClass class '%s'\n%s",
                            fieldType,
                            entityClassName,
                            StringUtils.getSupportedDataTypes(databaseEngine, typeMap)
                    );
                    return new UnsupportedFieldTypeException(message);
                });
    }

}
