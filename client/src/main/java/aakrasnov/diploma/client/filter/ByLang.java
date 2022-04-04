package aakrasnov.diploma.client.filter;

import aakrasnov.diploma.common.Filter;

/**
 * Filter by the language for which documents were created.
 */
public final class ByLang implements Filter {
    /**
     * Target language.
     */
    private final String lang;

    /**
     * Ctor.
     * @param lang Target language
     */
    public ByLang(final String lang) {
        this.lang = lang;
    }

    @Override
    public String key() {
        return "lang";
    }

    @Override
    public String value() {
        return lang;
    }
}
