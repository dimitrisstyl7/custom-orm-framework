package dimstyl.orm.internal.utils;

/**
 * Utility class for console-based formatted output.
 *
 * <p>This class provides a convenient method for printing formatted messages
 * to the standard output using {@code System.out.printf()}.</p>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>
 *     ConsoleUtils.printFormatted("Hello, %s!", "World");
 *     // Output: Hello, World!
 * </pre>
 *
 * <p><strong>Design Notes:</strong></p>
 * <ul>
 *     <li>This class is {@code final} to prevent inheritance.</li>
 *     <li>The constructor is private to enforce a static utility pattern.</li>
 * </ul>
 */
public final class ConsoleUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private ConsoleUtils() {
    }

    /**
     * Prints a formatted message to the console.
     *
     * @param message The format string, following {@link java.util.Formatter} syntax.
     * @param args    The arguments referenced by the format specifiers in the message.
     */
    public static void printFormatted(final String message, final Object... args) {
        System.out.printf(message, args);
    }

}
