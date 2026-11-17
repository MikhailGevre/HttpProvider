package org.example.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.NoSuchFileException;
import org.example.model.ProviderDto;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonProviderJsonReader {
    private final ObjectMapper objectMapper;

    public List<ProviderDto> read(String path) {
        log.info("Reading providers from {}", path);
        Path file = Paths.get(path);

        if (!Files.isRegularFile(file) || !Files.isReadable(file)) {
            throw new NoSuchFileException("Provider configuration file does not exist or is not readable: " + path);
        }

        return objectMapper.readValue(file.toFile(), new TypeReference<>() {
        });
    }
}
