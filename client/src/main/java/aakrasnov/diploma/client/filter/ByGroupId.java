package aakrasnov.diploma.client.filter;

import aakrasnov.diploma.common.Filter;

/**
 * Filter by group id.
 */
class ByGroupId implements Filter {
    /**
     * Name of the key in the document.
     */
    private final String key;

    /**
     * Target group id.
     */
    private final String group;

    /**
     * Ctor.
     *
     * @param key Name of the key in the document
     * @param group Target group id
     */
    public ByGroupId(final String key, final String group) {
        this.key = key;
        this.group = group;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public String value() {
        return group;
    }
}
