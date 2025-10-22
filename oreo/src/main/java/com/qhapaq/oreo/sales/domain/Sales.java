package com.qhapaq.oreo.sales.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sales", indexes = {
        @Index(name = "idx_sales_branch", columnList = "branch"),
        @Index(name = "idx_sales_sold_at", columnList = "sold_at"),
        @Index(name = "idx_sales_sku", columnList = "sku")
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 50, message = "SKU must be between 3 and 50 characters")
    @Column(nullable = false, length = 50)
    private String sku;

    @NotNull(message = "Units is required")
    @Min(value = 1, message = "Units must be at least 1")
    @Column(nullable = false)
    private Integer units;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotBlank(message = "Branch is required")
    @Size(min = 2, max = 100, message = "Branch must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String branch;

    @NotNull(message = "Sold at timestamp is required")
    @Column(name = "sold_at", nullable = false)
    private Instant soldAt;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    @Transient
    public BigDecimal getTotalRevenue() {
        return price.multiply(BigDecimal.valueOf(units));
    }
}