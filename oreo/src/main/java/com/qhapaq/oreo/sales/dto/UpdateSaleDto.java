package com.qhapaq.oreo.sales.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSaleDto {

    @Size(min = 3, max = 50)
    private String sku;

    @Min(value = 1)
    private Integer units;

    @DecimalMin(value = "0.01")
    private BigDecimal price;

    @Size(min = 2, max = 100)
    private String branch;

    private Instant soldAt;
}
