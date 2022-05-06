package aakrasnov.diploma.client.exception;

public class InputFileNotFoundException extends RuntimeException {
    public InputFileNotFoundException(String msg) {
        super(msg);
    }

    public InputFileNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
