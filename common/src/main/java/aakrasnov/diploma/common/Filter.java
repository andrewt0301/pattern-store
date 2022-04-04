package aakrasnov.diploma.common;

/**
 * Filter for checking a value of the specified in the document.
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