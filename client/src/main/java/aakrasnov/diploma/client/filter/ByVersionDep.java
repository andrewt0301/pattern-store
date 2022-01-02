package aakrasnov.diploma.client.filter;

/**
 * Filter by version of the artifact.
 */
class ByVersionDep implements Filter {
    /**
     * Name of the key in the document.
     */
    private final String key;

    /**
     * Target version of the artifact.
     */
    private final String vrsn;

    /**
     * Ctor.
     *
     * @param key Name of the key in the document
     * @param version Target version of the artifact
     */
    public ByVersionDep(final String key, final String version) {
        this.key = key;
        this.vrsn = version;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public String value() {
        return vrsn;
    }
}
