package dimstyl.orm.exceptions;

/**
 * Exception thrown when an error occurs during SQL execution.
 * <p>
 * This exception indicates that an issue was encountered while executing an SQL statement,
 * such as a syntax error, connection issue, or constraint violation.
 * </p>
 */
public class SqlExecutionException extends RuntimeException {

    /**
     * Constructs a new {@code SqlExecutionException} with the specified detail message
     * and the underlying cause of the exception.
     *
     * @param message The detail message explaining the reason for the exception.
     * @param cause   The underlying cause of the exception.
     */
    public SqlExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}
