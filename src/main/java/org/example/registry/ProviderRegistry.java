package org.example.registry;

import org.example.model.ProviderDto;

import java.util.List;
import java.util.Optional;

public interface ProviderRegistry {

    Optional<ProviderDto> findById(String id);

    void replaceAll(List<ProviderDto> providers);
}
