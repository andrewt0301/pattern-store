package aakrasnov.diploma.client.exception;

public class BadInputFileException extends RuntimeException {
    public BadInputFileException(String msg) {
        super(msg);
    }

    public BadInputFileException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
