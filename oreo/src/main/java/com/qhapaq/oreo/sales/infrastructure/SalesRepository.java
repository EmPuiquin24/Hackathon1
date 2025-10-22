package com.qhapaq.oreo.sales.infrastructure;

import com.qhapaq.oreo.sales.domain.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface SalesRepository extends JpaRepository<Sales, UUID> {

    @Query("SELECT s FROM Sales s WHERE s.soldAt BETWEEN :from AND :to")
    List<Sales> findByDateRange(@Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT s FROM Sales s WHERE s.soldAt BETWEEN :from AND :to AND s.branch = :branch")
    List<Sales> findByDateRangeAndBranch(
            @Param("from") Instant from,
            @Param("to") Instant to,
            @Param("branch") String branch
    );

    Page<Sales> findByBranch(String branch, Pageable pageable);

    @Query("SELECT s FROM Sales s WHERE s.branch = :branch AND s.soldAt BETWEEN :from AND :to")
    Page<Sales> findByBranchAndDateRange(
            @Param("branch") String branch,
            @Param("from") Instant from,
            @Param("to") Instant to,
            Pageable pageable
    );

    @Query("SELECT s FROM Sales s WHERE s.soldAt BETWEEN :from AND :to")
    Page<Sales> findByDateRangePaginated(
            @Param("from") Instant from,
            @Param("to") Instant to,
            Pageable pageable
    );

    boolean existsByIdAndBranch(String id, String branch);
}