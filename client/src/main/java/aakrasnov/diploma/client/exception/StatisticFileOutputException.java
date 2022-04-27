package aakrasnov.diploma.client.exception;

public class StatisticFileOutputException extends RuntimeException {
    public StatisticFileOutputException(String msg) {
        super(msg);
    }

    public StatisticFileOutputException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
