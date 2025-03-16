package dimstyl.orm.exceptions;

public class MissingPrimaryKeyException extends RuntimeException {

    public MissingPrimaryKeyException(String message) {
        super(message);
    }

}
