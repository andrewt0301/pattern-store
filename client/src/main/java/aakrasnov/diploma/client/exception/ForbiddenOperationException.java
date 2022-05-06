package aakrasnov.diploma.client.exception;

public class ForbiddenOperationException extends RuntimeException {
    public ForbiddenOperationException(String msg) {
        super(msg);
    }

    public ForbiddenOperationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
