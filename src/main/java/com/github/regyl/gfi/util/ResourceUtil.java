package com.github.regyl.gfi.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@UtilityClass
public class ResourceUtil {

    public String getFilePayload(String filePath) {
        try {
            var resource = ResourceUtil.class.getClassLoader().getResource(filePath);
            Objects.requireNonNull(resource, "File not found in resources: " + filePath);

            Path path = Path.of(resource.toURI());
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while loading file: " + filePath, e);
        }
    }
}
