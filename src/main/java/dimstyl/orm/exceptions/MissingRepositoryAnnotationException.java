package dimstyl.orm.exceptions;

/**
 * Exception thrown when a repository class is missing the required {@code @Repository} annotation.
 * <p>
 * This exception indicates that a class intended to act as a repository for database operations
 * does not have the necessary {@code @Repository} annotation, preventing it from being recognized
 * by the ORM framework.
 * </p>
 */
public class MissingRepositoryAnnotationException extends RuntimeException {

    /**
     * Constructs a new {@code MissingRepositoryAnnotationException} with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public MissingRepositoryAnnotationException(String message) {
        super(message);
    }

}
