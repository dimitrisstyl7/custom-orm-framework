package dimstyl.orm.internal.utils;

import java.util.Map;

/**
 * Utility class for string transformations and formatting.
 *
 * <p>This class provides helper methods to convert strings between different naming conventions and to generate
 * formatted representations of supported Java-to-SQL type mappings.</p>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>
 *     // Convert camelCase to snake_case
 *     String snakeCase = StringUtils.camelCaseToSnakeCase("myVariableName");
 *     System.out.println(snakeCase); // Output: my_variable_name
 *
 *     // Get formatted data type mappings for a database engine
 *     Map<Class<?>, String> typeMap = Map.of(
 *         String.class, "VARCHAR(255)",
 *         Integer.class, "INTEGER"
 *     );
 *     String formattedTypes = StringUtils.getSupportedDataTypes("H2", typeMap);
 *     System.out.println(formattedTypes); // Output: formatted type mappings for H2
 * </pre>
 *
 * <p><strong>Design Notes:</strong></p>
 * <ul>
 *     <li>This class is {@code final} to prevent inheritance.</li>
 *     <li>The constructor is private to enforce a static utility pattern.</li>
 * </ul>
 */
public final class StringUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private StringUtils() {
    }

    /**
     * Converts a string to a default name format.
     * <p>This method internally calls {@link #camelCaseToSnakeCase(String)}.</p>
     *
     * @param s The input string in camelCase format.
     * @return The string converted to snake_case format.
     */
    public static String getDefaultName(final String s) {
        return StringUtils.camelCaseToSnakeCase(s);
    }

    /**
     * Converts a camelCase string to snake_case.
     *
     * <p>If the input string is {@code null} or blank, it is returned as-is.</p>
     *
     * @param s The input string in camelCase format.
     * @return The string converted to snake_case format.
     */
    public static String camelCaseToSnakeCase(String s) {
        if (s == null || s.isBlank()) return s;

        final StringBuilder result = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);

            if (Character.isUpperCase(c)) {
                if (i > 0) result.append('_');
                result.append(Character.toLowerCase(c));
            } else result.append(c);
        }

        return result.toString();
    }

    /**
     * Retrieves a formatted list of supported Java-to-SQL type mappings for a given database.
     *
     * <p>This method generates a table-like representation of supported data types,
     * helping users understand which Java types are mapped to SQL types.</p>
     *
     * @param databaseEngine The target database engine (e.g., "H2", "SQLite").
     * @param typeMap        A map of Java types to SQL types.
     * @return A formatted string containing supported data type mappings.
     */
    public static String getSupportedDataTypes(final String databaseEngine, final Map<Class<?>, String> typeMap) {
        StringBuilder sb = new StringBuilder();

        sb.append("\t\t📌 Supported Data Type Mappings:\n");
        sb.append(String.format("\t\t\t%-30s -> %s Type%n", "Java Type", databaseEngine));
        sb.append("\t\t\t--------------------------------------------------\n");

        typeMap.forEach((javaType, sqlType) ->
                sb.append(String.format("\t\t\t%-30s -> %s%n", javaType.getSimpleName(), sqlType))
        );

        return sb.toString();
    }

}
