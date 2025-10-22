package com.qhapaq.oreo.summary.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;

    @Column
    private String branch;

    @Column(nullable = false)
    private String emailTo;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    public ReportRequest(LocalDate fromDate, LocalDate toDate, String branch, String emailTo) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.branch = branch;
        this.emailTo = emailTo;
        this.status = ReportStatus.PROCESSING;
        this.requestedAt = LocalDateTime.now();
    }
}
