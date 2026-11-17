package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.config.JsonProviderJsonReader;
import org.example.model.ProviderDto;
import org.example.registry.ProviderRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JsonProviderRegistryService implements ProviderRegistry {
    private final JsonProviderJsonReader jsonProviderJsonReader;
    private final String pathProviders;
    private volatile Map<String, ProviderDto> providers = Map.of();

    public JsonProviderRegistryService(JsonProviderJsonReader jsonProviderJsonReader,
                                       @Value("${path.json-provider}") String pathProviders) {

        this.jsonProviderJsonReader = jsonProviderJsonReader;
        this.pathProviders = pathProviders;
    }

    @PostConstruct
    public void init() {
        List<ProviderDto> providers = jsonProviderJsonReader.read(pathProviders);
        replaceAll(providers);
    }

    @Override
    public Optional<ProviderDto> findById(String id) {
        return Optional.ofNullable(providers.get(id));
    }

    @Override
    public synchronized void replaceAll(List<ProviderDto> providers) {
        this.providers = providers.stream()
                .collect(Collectors.toUnmodifiableMap(ProviderDto::id, Function.identity()));
    }
}