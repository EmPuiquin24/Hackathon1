package com.qhapaq.oreo.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponseDto {
    private String id;
    private String sku;
    private Integer units;
    private BigDecimal price;
    private String branch;
    private Instant soldAt;
    private String createdBy;
}