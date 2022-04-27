package aakrasnov.diploma.client.exception;

public class BadInputStatisticFileException extends RuntimeException {
    public BadInputStatisticFileException(String msg) {
        super(msg);
    }

    public BadInputStatisticFileException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
