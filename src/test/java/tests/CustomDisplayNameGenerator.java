package tests;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.DisplayNameGenerator.Simple;

/**
 * A custom display name generator for JUnit tests.
 * <p>
 * This generator enhances test method names to improve readability by:
 * <ul>
 *   <li>Replacing underscores (_) with " - ".</li>
 *   <li>Converting camel case method names into space-separated words, ensuring all words are transformed to lowercase.</li>
 * </ul>
 * </p>
 * <p>
 * Example transformations:
 * <ul>
 *   <li>{@code getAllCustomersH2Test} → {@code get all customers h2 test}</li>
 *   <li>{@code delete_Customer_By_Id_H2_Test} → {@code delete - customer - by - id - h2 - test}</li>
 * </ul>
 * </p>
 */
class CustomDisplayNameGenerator {

    /**
     * Custom JUnit display name generator that replaces camel case with spaces and underscores with " - ".
     * <p>
     * All words in the generated display name are converted to lowercase.
     * </p>
     */
    static class ReplaceCamelCase extends Simple {

        @Override
        public String generateDisplayNameForClass(Class<?> testClass) {
            return replaceUnderscore(replaceCamelCase(super.generateDisplayNameForClass(testClass)));
        }

        @Override
        public String generateDisplayNameForNestedClass(List<Class<?>> enclosingInstanceTypes, Class<?> nestedClass) {
            return replaceUnderscore(replaceCamelCase(super.generateDisplayNameForNestedClass(enclosingInstanceTypes, nestedClass)));
        }

        @Override
        public String generateDisplayNameForMethod(List<Class<?>> enclosingInstanceTypes, Class<?> testClass, Method testMethod) {
            return replaceUnderscore(replaceCamelCase(super.generateDisplayNameForMethod(enclosingInstanceTypes, testClass, testMethod)));
        }

        /**
         * Replaces underscores with " - " for better readability.
         *
         * @param input The input string.
         * @return The modified string with underscores replaced and worded in lowercase.
         */
        private String replaceUnderscore(String input) {
            return input.replace("_", " -");
        }

        /**
         * Converts camel case into a space-separated string.
         * <p>
         * Additionally, all words are converted to lowercase.
         * </p>
         *
         * @param camelCase The input camel case string.
         * @return The formatted string with spaces between words and all characters in lowercase.
         */
        private String replaceCamelCase(String camelCase) {
            StringBuilder result = new StringBuilder();
            result.append(camelCase.charAt(0));
            for (int i = 1; i < camelCase.length(); i++) {
                if (Character.isUpperCase(camelCase.charAt(i))) {
                    result.append(' ');
                    result.append(Character.toLowerCase(camelCase.charAt(i)));
                } else {
                    result.append(camelCase.charAt(i));
                }
            }
            return result.toString();
        }

    }

}
