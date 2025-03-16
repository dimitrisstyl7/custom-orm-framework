package dimstyl.orm.exceptions;

/**
 * Exception thrown when a required {@code @Column} annotation is missing from an entity field.
 * <p>
 * This exception is used to indicate that a field within an entity class is expected
 * to be mapped to a database column but lacks the necessary {@code @Column} annotation.
 * </p>
 */
public class MissingColumnAnnotationException extends RuntimeException {

    /**
     * Constructs a new {@code MissingColumnAnnotationException} with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public MissingColumnAnnotationException(String message) {
        super(message);
    }

}
