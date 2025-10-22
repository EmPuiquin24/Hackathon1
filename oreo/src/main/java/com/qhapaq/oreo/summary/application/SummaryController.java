package com.qhapaq.oreo.summary.application;

import com.qhapaq.oreo.summary.domain.SummaryService;
import com.qhapaq.oreo.summary.dto.WeeklySummaryRequestDto;
import com.qhapaq.oreo.summary.dto.WeeklySummaryResponseDto;
import com.qhapaq.oreo.user.domain.Role;
import com.qhapaq.oreo.user.domain.User;
import com.qhapaq.oreo.user.infrastructure.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales/summary")
public class SummaryController {

    private final SummaryService summaryService;
    private final UserRepository userRepository;

    public SummaryController(SummaryService summaryService, UserRepository userRepository) {
        this.summaryService = summaryService;
        this.userRepository = userRepository;
    }

    @PostMapping("/weekly")
    @PreAuthorize("hasAnyRole('CENTRAL', 'BRANCH')")
    public ResponseEntity<WeeklySummaryResponseDto> requestWeeklySummary(
            @Valid @RequestBody WeeklySummaryRequestDto request,
            Authentication authentication) {

        // Obtener usuario logueado
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        // Si es BRANCH, usar su branch asignado
        String userBranch = (user.getRole() == Role.BRANCH) ? user.getBranch() : null;

        WeeklySummaryResponseDto response = summaryService.requestWeeklySummary(request, userBranch);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
