package com.qhapaq.oreo.summary.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WeeklySummaryRequestDto {

    private LocalDate from;

    private LocalDate to;

    private String branch;

    @NotBlank(message = "emailTo es requerido")
    @Email(message = "emailTo debe ser un email válido")
    private String emailTo;

}