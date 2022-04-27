package aakrasnov.diploma.client.exception;

public class PatternsIdFileNotFoundException extends RuntimeException {
    public PatternsIdFileNotFoundException(String msg) {
        super(msg);
    }

    public PatternsIdFileNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
