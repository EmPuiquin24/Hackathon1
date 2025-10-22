package com.qhapaq.oreo.auth.dto;

import com.qhapaq.oreo.user.domain.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BranchDtoValidator implements ConstraintValidator<BranchDtoConstraint, JwtRegisterRequestDto> {

    @Override
    public boolean isValid(JwtRegisterRequestDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        Role role = dto.getRole();
        String branch = dto.getBranch();

        if (role == null) {
            return true; // let @NotNull on role handle absence
        }

        if (role == Role.BRANCH) {
            return branch != null && !branch.trim().isEmpty();
        } else if (role == Role.CENTRAL) {
            return branch == null;
        }

        return true;
    }
}
