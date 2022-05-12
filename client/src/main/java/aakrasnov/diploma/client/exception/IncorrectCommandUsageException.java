package aakrasnov.diploma.client.exception;

public class IncorrectCommandUsageException extends RuntimeException {
    public IncorrectCommandUsageException(String msg) {
        super(msg);
    }

    public IncorrectCommandUsageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
