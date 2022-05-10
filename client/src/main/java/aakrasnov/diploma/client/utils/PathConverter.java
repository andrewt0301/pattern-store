package aakrasnov.diploma.client.utils;

import aakrasnov.diploma.common.DocDto;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

public final class PathConverter {
    private final Path path;

    public PathConverter(final Path path) {
        this.path = path;
    }

    public JsonObject toJsonObj() throws IOException {
        try (FileInputStream input = new FileInputStream(path.toFile())) {
            return JsonParser.parseReader(
                new InputStreamReader(
                    new BufferedInputStream(input)
                )
            ).getAsJsonObject();
        }
    }

    public DocDto toDocDto() throws IOException {
        return new Gson().fromJson(
            toJsonObj(),
            DocDto.class
        );
    }
}
