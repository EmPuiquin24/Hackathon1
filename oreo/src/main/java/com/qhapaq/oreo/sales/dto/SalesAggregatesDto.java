package com.qhapaq.oreo.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesAggregatesDto {
    private Integer totalUnits;
    private BigDecimal totalRevenue;
    private String topSku;
    private String topBranch;
}