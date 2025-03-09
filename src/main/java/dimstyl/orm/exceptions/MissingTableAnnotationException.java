package dimstyl.orm.exceptions;

public class MissingTableAnnotationException extends RuntimeException {

    public MissingTableAnnotationException(String message) {
        super(message);
    }

}
