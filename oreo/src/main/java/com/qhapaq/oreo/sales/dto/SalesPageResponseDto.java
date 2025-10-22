package com.qhapaq.oreo.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesPageResponseDto {
    private List<SaleResponseDto> sales;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}