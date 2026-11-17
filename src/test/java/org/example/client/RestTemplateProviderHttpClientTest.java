package org.example.client;

import org.example.model.ProviderDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;

class RestTemplateProviderHttpClientTest {

    private MockRestServiceServer mockServer;
    private ProviderHttpClient providerHttpClient;
    private ProviderDto provider;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();

        mockServer = MockRestServiceServer
                .bindTo(restTemplate)
                .build();

        providerHttpClient = new RestTemplateProviderHttpClient(restTemplate, "X-API-Key");

        provider = new ProviderDto("provider-1", "https://api-one.com", "key-111");
    }

    @Test
    void shouldReturnTrueWhenStatusIsOk() {
        mockServer.expect(requestTo("https://api-one.com/subscribers/79991234567"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-API-Key", "key-111"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .body("{\"status\":\"ok\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                );

        boolean result = providerHttpClient.isAvailable(provider, "79991234567");

        assertTrue(result);

        mockServer.verify();
    }

    @Test
    void shouldReturnFalseWhenStatusIsNotFound() {
        mockServer.expect(requestTo("https://api-one.com/subscribers/79991234567"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-API-Key", "key-111"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));

        boolean result = providerHttpClient.isAvailable(provider, "79991234567");

        assertFalse(result);

        mockServer.verify();
    }
}