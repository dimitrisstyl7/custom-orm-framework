package dimstyl.orm.exceptions;

/**
 * Exception thrown when a composite primary key is detected in an entity.
 * <p>
 * This exception is used to indicate that an entity has multiple fields marked as
 * primary keys, which is not supported by the ORM framework.
 * </p>
 */
public class CompositePrimaryKeyException extends RuntimeException {

    /**
     * Constructs a new {@code CompositePrimaryKeyException} with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public CompositePrimaryKeyException(String message) {
        super(message);
    }

}
