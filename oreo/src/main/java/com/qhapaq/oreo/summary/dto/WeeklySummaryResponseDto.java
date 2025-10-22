package com.qhapaq.oreo.summary.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class WeeklySummaryResponseDto {

    private Long requestId;
    private String status;
    private String message;
    private String estimatedTime;
    private LocalDateTime requestedAt;
}