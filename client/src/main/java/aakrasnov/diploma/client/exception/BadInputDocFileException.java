package aakrasnov.diploma.client.exception;

public class BadInputDocFileException extends RuntimeException {
    public BadInputDocFileException(String msg) {
        super(msg);
    }

    public BadInputDocFileException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
