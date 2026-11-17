package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.ProviderHttpClient;
import org.example.exception.EntityNotFoundException;
import org.example.model.ProviderDto;
import org.example.model.SubscriberResponseDto;
import org.example.registry.ProviderRegistry;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegistryProviderService {
    private static final String POSITIVE_STATUS = "ok";
    private final ProviderRegistry providerRegistry;
    private final ProviderHttpClient providerHttpClient;

    public SubscriberResponseDto isAvailable(String providerId, String phone) {
        log.info("Verify provider providerId - {}, phone -{}", providerId, phone);

        ProviderDto provider = providerRegistry.findById(providerId)
                .orElseThrow(() -> new EntityNotFoundException("Provider not found for this id: " + providerId));

        boolean isAvailableProvider = providerHttpClient.isAvailable(provider, phone);

        if (!isAvailableProvider) {
            return new SubscriberResponseDto("not available");
        }
        return new SubscriberResponseDto(POSITIVE_STATUS);
    }
}
