package com.qhapaq.oreo.ai.domain;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Orquesta la generación de resúmenes usando GitHub Models.
 */
@Service
@RequiredArgsConstructor
public class AiService {

    private final AiProviderClient aiProviderClient;

    /**
     * Genera el resumen semanal en lenguaje natural basado en los agregados.
     */
    public String generateWeeklySummary(SalesAggregatesDto aggregates) {
        String prompt = String.format("""
Eres un analista de datos de la empresa Oreo.
Con estos datos de ventas:
- Unidades totales vendidas: %d
- Ingreso total: $%.2f
- SKU más vendido: %s
- Sucursal con más ventas: %s

Redacta un resumen corto, profesional y en español (máximo 120 palabras)
para enviar por email corporativo. No inventes datos.
""",
                aggregates.getTotalUnits(),
                aggregates.getTotalRevenue(),
                aggregates.getTopSku(),
                aggregates.getTopBranch()
        );

        return aiProviderClient.generateSummary(prompt);
    }
}