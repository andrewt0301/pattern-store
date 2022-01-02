package aakrasnov.diploma.client.filter;

/**
 * Filter for checking a value of the specified in the document.
 * TODO: extract this interface to a separate module because it will
 *  be used in client and server.
 */
public interface Filter {
    /**
     * Obtains key for check.
     * @return Key for check.
     */
    String key();

    /**
     * Obtains target value of the specified key.
     * @return Target value
     */
    String value();
}
