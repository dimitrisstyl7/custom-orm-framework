package dimstyl.orm.utils;

public final class PrintUtils {

    private PrintUtils() {
    }

    public static void print(final String message, final Object... args) {
        System.out.printf(message, args);
    }

}
