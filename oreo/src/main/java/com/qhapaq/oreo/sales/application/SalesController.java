package com.qhapaq.oreo.sales.application;

import com.qhapaq.oreo.sales.domain.Sales;
import com.qhapaq.oreo.sales.domain.SalesService;
import com.qhapaq.oreo.sales.dto.*;
import com.qhapaq.oreo.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CENTRAL', 'BRANCH')")
    public SaleResponseDto createSale(
            @Valid @RequestBody CreateSaleDto dto,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        Sales sale = salesService.createSale(dto, user);
        return modelMapper.map(sale, SaleResponseDto.class);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CENTRAL', 'BRANCH')")
    public SaleResponseDto getSaleById(
            @PathVariable String id,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        Sales sale = salesService.getSaleById(id, user);
        return modelMapper.map(sale, SaleResponseDto.class);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CENTRAL', 'BRANCH')")
    public SalesPageResponseDto getSales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String branch,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        Instant fromInstant = (from != null) ? from.atStartOfDay().toInstant(ZoneOffset.UTC) : null;
        Instant toInstant = (to != null) ? to.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC) : null;

        Page<Sales> salesPage = salesService.getSales(fromInstant, toInstant, branch, page, size, user);

        SalesPageResponseDto response = new SalesPageResponseDto();
        response.setSales(salesPage.getContent().stream()
                .map(sale -> modelMapper.map(sale, SaleResponseDto.class))
                .toList());
        response.setPage(salesPage.getNumber());
        response.setSize(salesPage.getSize());
        response.setTotalElements(salesPage.getTotalElements());
        response.setTotalPages(salesPage.getTotalPages());

        return response;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CENTRAL', 'BRANCH')")
    public SaleResponseDto updateSale(
            @PathVariable String id,
            @Valid @RequestBody UpdateSaleDto dto,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        Sales sale = salesService.getSaleById(id, user);
        Sales updatedSale = salesService.updateSale(sale, dto, user);
        return modelMapper.map(updatedSale, SaleResponseDto.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('CENTRAL')")
    public void deleteSale(@PathVariable String id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Sales sale = salesService.getSaleById(id, user);
        salesService.deleteSale(sale);
    }

    // ========== ENDPOINT PARA VER AGREGADOS ==========
    @GetMapping("/aggregates")
    @PreAuthorize("hasAnyRole('CENTRAL', 'BRANCH')")
    public SalesAggregatesDto getAggregates(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String branch,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        // Si es BRANCH, solo puede ver agregados de su sucursal
        String effectiveBranch = (user.getRole().name().equals("BRANCH")) ? user.getBranch() : branch;

        Instant fromInstant = (from != null) ? from.atStartOfDay().toInstant(ZoneOffset.UTC) : null;
        Instant toInstant = (to != null) ? to.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC) : null;

        return salesService.calculateAggregates(fromInstant, toInstant, effectiveBranch);
    }
}