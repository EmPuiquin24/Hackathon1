package com.qhapaq.oreo.ai.domain;

import com.qhapaq.oreo.ai.dto.AiPromptRequestDto;
import com.qhapaq.oreo.ai.dto.AiSummaryResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Implementación de cliente HTTP para GitHub Models API.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GitHubModelsClient implements AiProviderClient {

    private final WebClient webClient;

    @Value("${GITHUB_MODELS_URL}")
    private String apiUrl;

    @Value("${MODEL_ID}")
    private String modelId;

    @Value("${GITHUB_TOKEN}")
    private String token;

    @Override
    public String generateSummary(String prompt) {
        log.info("Llamando a GitHub Models API...");

        AiPromptRequestDto request = new AiPromptRequestDto(
                modelId,
                new AiPromptRequestDto.Message[]{
                        new AiPromptRequestDto.Message("system", "Eres un analista que escribe resúmenes breves y claros para emails corporativos."),
                        new AiPromptRequestDto.Message("user", prompt)
                },
                200
        );

        AiSummaryResponseDto response = webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), AiPromptRequestDto.class)
                .retrieve()
                .bodyToMono(AiSummaryResponseDto.class)
                .block();

        if (response != null && !response.getChoices().isEmpty()) {
            String text = response.getChoices().get(0).getMessage().getContent();
            log.info("Resumen generado: {}", text);
            return text;
        } else {
            log.warn("Respuesta vacía del modelo.");
            return "No se pudo generar el resumen en este momento.";
        }
    }
}
