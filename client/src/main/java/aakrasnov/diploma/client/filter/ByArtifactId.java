package aakrasnov.diploma.client.filter;

/**
 * Filter by artifact id.
 */
class ByArtifactId implements Filter {
    /**
     * Name of the key in the document.
     */
    private final String key;

    /**
     * Target artifact id.
     */
    private final String artifact;

    /**
     * Ctor.
     *
     * @param key Name of the key in the document
     * @param artifact Target artifact id
     */
    public ByArtifactId(final String key, final String artifact) {
        this.key = key;
        this.artifact = artifact;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public String value() {
        return artifact;
    }
}
