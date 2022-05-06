package aakrasnov.diploma.client.http;

public interface CheckedRunnable<E extends Exception> {
    void run() throws E;
}
