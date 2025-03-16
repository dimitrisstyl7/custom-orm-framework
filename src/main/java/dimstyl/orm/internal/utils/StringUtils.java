package dimstyl.orm.internal.utils;

public final class StringUtils {

    private StringUtils() {
    }

    public static String getDefaultName(final String s) {
        return StringUtils.camelCaseToSnakeCase(s);
    }

    public static String camelCaseToSnakeCase(String str) {
        if (str == null || str.isBlank()) return str;

        final StringBuilder result = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            final char c = str.charAt(i);

            if (Character.isUpperCase(c)) {
                if (i > 0) result.append('_');
                result.append(Character.toLowerCase(c));
            } else result.append(c);
        }

        return result.toString();
    }

}
