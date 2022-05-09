package aakrasnov.diploma.client.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Test resource.
 * @since 0.24
 */
public final class TestResource {

    /**
     * Relative to test resources folder resource path.
     */
    private final String name;

    /**
     * Ctor.
     * @param name Resource path
     */
    public TestResource(final String name) {
        this.name = name;
    }

    /**
     * Obtains resources from context loader.
     * @return File path
     */
    public Path asPath() {
        try {
            return Paths.get(
                Objects.requireNonNull(
                    Thread.currentThread().getContextClassLoader().getResource(name)
                ).toURI()
            );
        } catch (final URISyntaxException exc) {
            throw new IllegalStateException("Failed to obtain test recourse", exc);
        }
    }

    /**
     * Recourse as input stream.
     * @return Input stream
     */
    public InputStream asInputStream() {
        return Objects.requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream(this.name)
        );
    }

    /**
     * Resource as bytes.
     * @return Bytes.
     */
    public byte[] asBytes() {
        try (InputStream stream = asInputStream()) {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int count;
            final byte[] data = new byte[8 * 1024];
            while ((count = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, count);
            }
            return buffer.toByteArray();
        } catch (IOException exc) {
            throw new IllegalStateException("Failed to load test recourse", exc);
        }
    }
}
