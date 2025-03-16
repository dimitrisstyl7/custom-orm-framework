package dimstyl.orm.exceptions;

/**
 * Exception thrown when an invalid column name is detected.
 * <p>
 * This exception is used to indicate that an entity contains a column name
 * that is either empty, incorrectly formatted, or does not match the expected database schema.
 * </p>
 */
public class InvalidColumnNameException extends RuntimeException {

    /**
     * Constructs a new {@code InvalidColumnNameException} with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public InvalidColumnNameException(String message) {
        super(message);
    }

}
