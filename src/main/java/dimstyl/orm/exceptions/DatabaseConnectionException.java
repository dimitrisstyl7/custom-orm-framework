package dimstyl.orm.exceptions;

/**
 * Exception thrown when a database connection issue occurs.
 * <p>
 * This exception is used to indicate failures in establishing, maintaining or closing a connection.
 * </p>
 */
public class DatabaseConnectionException extends RuntimeException {

    /**
     * Constructs a new {@code DatabaseConnectionException} with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public DatabaseConnectionException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code DatabaseConnectionException} with the specified detail message and cause.
     *
     * @param message The detail message explaining the reason for the exception.
     * @param cause   The underlying cause of the exception.
     */
    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
