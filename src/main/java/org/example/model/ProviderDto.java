package org.example.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record ProviderDto(
        @NotBlank(message = "Не переданное значение идентификатор")
        String id,
        @NotBlank(message = "Не переданное значение ссылка провайдера")
        @URL(message = "Пожалуйста, введите корректный URL (например, https://example.com)")
        String baseUrl,
        @NotBlank(message = "Не переданное значение ключа")
        String apiKey
) {
}
