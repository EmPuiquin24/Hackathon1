package com.qhapaq.oreo.common.dto;

import lombok.Data;

@Data
public class ErrorResponseDto {
    private String error;
    private String message;
    private String timestamp;
    private String path;  // Agregué este campo que pide el hackathon
}