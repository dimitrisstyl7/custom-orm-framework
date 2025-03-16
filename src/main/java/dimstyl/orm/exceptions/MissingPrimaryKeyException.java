package dimstyl.orm.exceptions;

/**
 * Exception thrown when an entity class does not have a primary key defined.
 * <p>
 * This exception is used to indicate that a class intended to be mapped to a database table
 * is missing a field annotated with {@code @PrimaryKey}, which is required for proper ORM functionality.
 * </p>
 */
public class MissingPrimaryKeyException extends RuntimeException {

    /**
     * Constructs a new {@code MissingPrimaryKeyException} with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public MissingPrimaryKeyException(String message) {
        super(message);
    }

}
