package org.example.client;

import org.example.model.ProviderDto;

public interface ProviderHttpClient {
    boolean isAvailable(ProviderDto provider, String phone);
}
