package com.qhapaq.oreo.summary.domain;

import com.qhapaq.oreo.summary.dto.WeeklySummaryRequestDto;
import com.qhapaq.oreo.summary.dto.WeeklySummaryResponseDto;
import com.qhapaq.oreo.summary.events.ReportRequestedEvent;
import com.qhapaq.oreo.summary.infrastructure.ReportRequestRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SummaryService {

    private final ReportRequestRepository reportRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    public SummaryService(ReportRequestRepository reportRequestRepository, ApplicationEventPublisher eventPublisher) {
        this.reportRequestRepository = reportRequestRepository;
        this.eventPublisher = eventPublisher;
    }

    public WeeklySummaryResponseDto requestWeeklySummary(WeeklySummaryRequestDto request, String userBranch) {
        // Si from/to son null, calcular última semana
        LocalDate from = request.getFrom() != null ? request.getFrom() : LocalDate.now().minusDays(7);
        LocalDate to = request.getTo() != null ? request.getTo() : LocalDate.now();

        // Determinar branch final
        String branch = request.getBranch();

        // Validación: si el usuario tiene branch asignado, no puede consultar otro
        if (userBranch != null) {
            if (branch != null && !branch.equals(userBranch)) {
                throw new IllegalArgumentException("No puede solicitar reportes de otra sucursal.");
            }
            // Si no envió branch, se usa el suyo
            branch = userBranch;
        }

        // Crear entidad ReportRequest
        ReportRequest reportRequest = new ReportRequest(from, to, branch, request.getEmailTo());
        reportRequest = reportRequestRepository.save(reportRequest);

        // Publicar evento asincrónico
        ReportRequestedEvent event = new ReportRequestedEvent(
                this,
                reportRequest.getId(),
                from,
                to,
                branch,
                request.getEmailTo()
        );
        eventPublisher.publishEvent(event);

        // Respuesta inmediata tipo 202 Accepted
        return new WeeklySummaryResponseDto(
                reportRequest.getId(),
                "PROCESSING",
                "Su solicitud está siendo procesada. Recibirá el resumen en " + request.getEmailTo() + " en unos momentos.",
                "30–60 segundos",
                reportRequest.getRequestedAt()
        );
    }
}
