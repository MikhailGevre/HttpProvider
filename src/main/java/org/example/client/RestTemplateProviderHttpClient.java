package org.example.client;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.model.ProviderDto;
import org.example.model.SubscriberResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
public class RestTemplateProviderHttpClient implements ProviderHttpClient {
    private static final String PATH_SEGMENT_SUBSCRIBER = "subscribers";
    private static final String POSITIVE_STATUS = "ok";
    private final RestTemplate restTemplate;
    @Value("${rest.template.api-key-header}")
    private String apiKeyHeader;

    public RestTemplateProviderHttpClient(@Qualifier("providerRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean isAvailable(ProviderDto provider, String phone) {
        URI uri = UriComponentsBuilder
                .fromUriString(provider.baseUrl())
                .pathSegment(PATH_SEGMENT_SUBSCRIBER, phone)
                .build()
                .encode()
                .toUri();
        
        log.debug("Request for provider {}", uri);

        HttpHeaders headers = new HttpHeaders();
        headers.set(apiKeyHeader, provider.apiKey());

        HttpEntity<@NonNull Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<@NonNull SubscriberResponseDto> response =
                    restTemplate.exchange(uri, HttpMethod.GET, request, SubscriberResponseDto.class);

            return response.getBody() != null && POSITIVE_STATUS.equals(response.getBody().status());
        } catch (ResourceAccessException e) {
            log.error("Resource may be not available {}", e.getMessage());
            return false;
        } catch (HttpClientErrorException.NotFound e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
