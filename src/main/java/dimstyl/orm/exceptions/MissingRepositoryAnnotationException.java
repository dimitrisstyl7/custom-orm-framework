package dimstyl.orm.exceptions;

public class MissingRepositoryAnnotationException extends RuntimeException {

    public MissingRepositoryAnnotationException(String message) {
        super(message);
    }

}
