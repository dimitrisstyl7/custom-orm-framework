package dimstyl.orm.exceptions;

/**
 * Exception thrown when an entity class is missing the required {@code @Table} annotation.
 * <p>
 * This exception indicates that a class intended to be mapped to a database table
 * does not have the {@code @Table} annotation, which is required for the ORM framework
 * to recognize it as a database entity.
 * </p>
 */
public class MissingTableAnnotationException extends RuntimeException {

    /**
     * Constructs a new {@code MissingTableAnnotationException} with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public MissingTableAnnotationException(String message) {
        super(message);
    }

}
