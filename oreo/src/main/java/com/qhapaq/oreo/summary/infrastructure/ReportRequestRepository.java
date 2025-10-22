package com.qhapaq.oreo.summary.infrastructure;

import com.qhapaq.oreo.summary.domain.ReportRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRequestRepository extends JpaRepository<ReportRequest, Long> {
}