package dimstyl.orm.exceptions;

public class MissingDatabaseAnnotationException extends RuntimeException {

    public MissingDatabaseAnnotationException(String message) {
        super(message);
    }

}
