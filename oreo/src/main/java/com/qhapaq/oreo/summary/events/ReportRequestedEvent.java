package com.qhapaq.oreo.summary.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

@Getter
public class ReportRequestedEvent extends ApplicationEvent {

    private final Long requestId;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final String branch;
    private final String emailTo;

    public ReportRequestedEvent(Object source, Long requestId, LocalDate fromDate, LocalDate toDate, String branch, String emailTo) {
        super(source);
        this.requestId = requestId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.branch = branch;
        this.emailTo = emailTo;
    }
}