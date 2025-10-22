package com.qhapaq.oreo.sales.domain;

import com.qhapaq.oreo.sales.dto.*;
import com.qhapaq.oreo.sales.infrastructure.SalesRepository;
import com.qhapaq.oreo.user.domain.Role;
import com.qhapaq.oreo.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SalesService {

    private final SalesRepository salesRepository;
    private final ModelMapper modelMapper;

    public Sales createSale(CreateSaleDto dto, User user) {
        // BRANCH solo puede crear ventas de su sucursal
        if (user.getRole() == Role.ROLE_BRANCH && !dto.getBranch().equals(user.getBranch())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only create sales for your assigned branch: " + user.getBranch()
            );
        }

        Sales sale = modelMapper.map(dto, Sales.class);
        sale.setCreatedBy(user.getUsername());
        return salesRepository.save(sale);
    }

    public Sales getSaleById(UUID id, User user) {
        Sales sale = salesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Sale not found with ID: " + id
                ));

        // BRANCH solo puede ver ventas de su sucursal
        if (user.getRole() == Role.ROLE_BRANCH && !sale.getBranch().equals(user.getBranch())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only view sales from your assigned branch"
            );
        }

        return sale;
    }

    public Page<Sales> getSales(Instant from, Instant to, String branch, int page, int size, User user) {
        // Si es BRANCH, forzar su sucursal
        String effectiveBranch = (user.getRole() == Role.ROLE_BRANCH) ? user.getBranch() : branch;

        Pageable pageable = PageRequest.of(page, size, Sort.by("soldAt").descending());

        if (from != null && to != null && effectiveBranch != null) {
            return salesRepository.findByBranchAndDateRange(effectiveBranch, from, to, pageable);
        } else if (from != null && to != null) {
            return salesRepository.findByDateRangePaginated(from, to, pageable);
        } else if (effectiveBranch != null) {
            return salesRepository.findByBranch(effectiveBranch, pageable);
        } else {
            return salesRepository.findAll(pageable);
        }
    }

    public Sales updateSale(Sales sale, UpdateSaleDto dto, User user) {
        // BRANCH solo puede actualizar ventas de su sucursal
        if (user.getRole() == Role.ROLE_BRANCH && !sale.getBranch().equals(user.getBranch())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only update sales from your assigned branch"
            );
        }

        if (dto.getSku() != null) sale.setSku(dto.getSku());
        if (dto.getUnits() != null) sale.setUnits(dto.getUnits());
        if (dto.getPrice() != null) sale.setPrice(dto.getPrice());
        if (dto.getSoldAt() != null) sale.setSoldAt(dto.getSoldAt());

        if (dto.getBranch() != null) {
            // BRANCH no puede cambiar a otra sucursal
            if (user.getRole() == Role.ROLE_BRANCH && !dto.getBranch().equals(user.getBranch())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "You cannot change the branch to a different one"
                );
            }
            sale.setBranch(dto.getBranch());
        }

        return salesRepository.save(sale);
    }

    public void deleteSale(Sales sale) {
        salesRepository.delete(sale);
    }

    // ========== MÉTODO PARA CALCULAR AGREGADOS ==========
    public SalesAggregatesDto calculateAggregates(Instant from, Instant to, String branch) {
        // Si no se especifican fechas, calcular última semana
        if (from == null || to == null) {
            to = Instant.now();
            from = to.minus(7, ChronoUnit.DAYS);
        }

        List<Sales> sales = (branch != null)
                ? salesRepository.findByDateRangeAndBranch(from, to, branch)
                : salesRepository.findByDateRange(from, to);

        if (sales.isEmpty()) {
            return SalesAggregatesDto.builder()
                    .totalUnits(0)
                    .totalRevenue(BigDecimal.ZERO)
                    .topSku(null)
                    .topBranch(null)
                    .build();
        }

        // Calcular totalUnits y totalRevenue
        int totalUnits = sales.stream()
                .mapToInt(Sales::getUnits)
                .sum();

        BigDecimal totalRevenue = sales.stream()
                .map(Sales::getTotalRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcular topSku (el más vendido por unidades)
        String topSku = sales.stream()
                .collect(Collectors.groupingBy(
                        Sales::getSku,
                        Collectors.summingInt(Sales::getUnits)
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Calcular topBranch (sucursal con más ventas)
        String topBranch = sales.stream()
                .collect(Collectors.groupingBy(
                        Sales::getBranch,
                        Collectors.summingInt(Sales::getUnits)
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return SalesAggregatesDto.builder()
                .totalUnits(totalUnits)
                .totalRevenue(totalRevenue)
                .topSku(topSku)
                .topBranch(topBranch)
                .build();
    }
}