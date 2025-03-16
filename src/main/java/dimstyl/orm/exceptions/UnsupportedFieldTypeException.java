package dimstyl.orm.exceptions;

/**
 * Exception thrown when an unsupported field type is encountered in an entity.
 * <p>
 * This exception is raised when the ORM framework encounters a field type that it does not
 * support for database persistence, such as a complex object or an unknown data type.
 * </p>
 */
public class UnsupportedFieldTypeException extends RuntimeException {

    /**
     * Constructs a new {@code UnsupportedFieldTypeException} with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public UnsupportedFieldTypeException(String message) {
        super(message);
    }

}
