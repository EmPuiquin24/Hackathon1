package com.qhapaq.oreo.sales.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSaleDto {

    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 50)
    private String sku;

    @NotNull(message = "Units is required")
    @Min(value = 1)
    private Integer units;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01")
    private BigDecimal price;

    @NotBlank(message = "Branch is required")
    @Size(min = 2, max = 100)
    private String branch;

    @NotNull(message = "Sold at timestamp is required")
    private Instant soldAt;
}
