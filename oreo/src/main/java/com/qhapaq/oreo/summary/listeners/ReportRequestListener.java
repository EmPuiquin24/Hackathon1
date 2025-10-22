package com.qhapaq.oreo.summary.listeners;

import com.qhapaq.oreo.ai.domain.AiService;
import com.qhapaq.oreo.mail.domain.MailService;
import com.qhapaq.oreo.summary.dto.LlmSummaryDto;
import com.qhapaq.oreo.summary.events.ReportRequestedEvent;
import com.qhapaq.oreo.summary.infrastructure.ReportRequestRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class ReportRequestListener {

    private final ReportRequestRepository reportRequestRepository;
    private final SalesAggregationService salesAggregationService;
    private final AiService aiService;
    private final MailService mailService;

    public ReportRequestListener(ReportRequestRepository reportRequestRepository,
                                 SalesAggregationService salesAggregationService,
                                 AiService aiService,
                                 MailService mailService) {
        this.reportRequestRepository = reportRequestRepository;
        this.salesAggregationService = salesAggregationService;
        this.aiService = aiService;
        this.mailService = mailService;
    }

    @Async
    @EventListener
    public void handleReportRequest(ReportRequestedEvent event) {
        try {
            // 1. Convertir LocalDate a LocalDateTime para queries
            LocalDateTime from = event.getFromDate().atStartOfDay();
            LocalDateTime to = event.getToDate().atTime(LocalTime.MAX);

            // 2. Calcular agregados
            LlmSummaryDto summary = salesAggregationService.calculateAggregates(from, to, event.getBranch());

            // 3. Generar resumen con LLM
            String summaryText = aiService.generateSummary(summary);
            summary.setSummaryText(summaryText);

            // 4. Enviar email
            mailService.sendWeeklySummary(event.getEmailTo(), event.getFromDate(), event.getToDate(), summary);

            // 5. Actualizar status a COMPLETED
            ReportRequest reportRequest = reportRequestRepository.findById(event.getRequestId()).orElse(null);
            if (reportRequest != null) {
                reportRequest.setStatus(ReportStatus.COMPLETED);
                reportRequestRepository.save(reportRequest);
            }

        } catch (Exception e) {
            // 6. Si falla, actualizar status a FAILED
            ReportRequest reportRequest = reportRequestRepository.findById(event.getRequestId()).orElse(null);
            if (reportRequest != null) {
                reportRequest.setStatus(ReportStatus.FAILED);
                reportRequestRepository.save(reportRequest);
            }
        }
    }
}