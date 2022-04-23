package aakrasnov.diploma.client.http;

public final class AddSlash {
    private final String url;

    public AddSlash(final String url) {
        this.url = url;
    }

    public String addIfAbsent() {
        if (url != null && !url.endsWith("/")) {
            return String.format("%s/", url);
        }
        return url;
    }
}
