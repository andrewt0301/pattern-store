package aakrasnov.diploma.client.exception;

public class BadInputIdsFileException extends RuntimeException {
    public BadInputIdsFileException(String msg) {
        super(msg);
    }

    public BadInputIdsFileException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
