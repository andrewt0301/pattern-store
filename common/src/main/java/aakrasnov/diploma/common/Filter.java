package aakrasnov.diploma.common;

import aakrasnov.diploma.common.json.FilterDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.Serializable;

/**
 * Filter for checking a value of the specified in the document.
 */
@JsonDeserialize(using = FilterDeserializer.class)
public interface Filter {
    /**
     * Obtains key for check.
     * @return Key for check.
     */
    String key();

    /**
     * Obtains target value of the specified key.
     * @return Target value.
     */
    String value();

    class Wrap implements Filter, Serializable {
        /**
         * Name of fields are important here for {@link FilterDeserializer}
         */
        private final String key;

        private final String value;

        public Wrap(final String key, final String value) {
            this.key = key;
            this.value = value;
        }

        public String key() {
            return key;
        }

        public String value() {
            return value;
        }
    }
}