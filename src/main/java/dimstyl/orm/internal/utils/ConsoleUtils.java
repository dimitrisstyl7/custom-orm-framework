package dimstyl.orm.internal.utils;

public final class ConsoleUtils {

    private ConsoleUtils() {
    }

    public static void printFormatted(final String message, final Object... args) {
        System.out.printf(message, args);
    }

}
