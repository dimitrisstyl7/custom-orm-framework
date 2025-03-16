package dimstyl.orm.exceptions;

/**
 * Exception thrown when an entity class is missing the required {@code @Database} annotation.
 * <p>
 * This exception is used to indicate that a class intended to represent a database configuration
 * does not have the necessary {@code @Database} annotation, preventing it from being properly
 * recognized by the ORM framework.
 * </p>
 */
public class MissingDatabaseAnnotationException extends RuntimeException {

    /**
     * Constructs a new {@code MissingDatabaseAnnotationException} with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public MissingDatabaseAnnotationException(String message) {
        super(message);
    }

}
