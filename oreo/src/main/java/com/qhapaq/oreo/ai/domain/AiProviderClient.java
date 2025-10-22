package com.qhapaq.oreo.ai.domain;


/**
 * Interfaz genérica para cualquier proveedor de LLM (GitHub Models, OpenAI, etc.).
 */
public interface AiProviderClient {
    String generateSummary(String prompt);
}
