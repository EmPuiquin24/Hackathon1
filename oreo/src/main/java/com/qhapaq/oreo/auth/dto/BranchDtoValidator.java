package com.qhapaq.oreo.auth.dto;

import com.qhapaq.oreo.user.domain.Role;
import com.qhapaq.oreo.user.dto.CreateUserDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BranchDtoValidator implements ConstraintValidator<BranchDtoConstraint, CreateUserDto> {

    @Override
    public boolean isValid(CreateUserDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        Role role = dto.getRole();
        String branch = dto.getBranch();

        if (role == null) {
            return true; // let @NotNull on role handle absence
        }

        if (role == Role.ROLE_BRANCH) {
            return branch != null && !branch.trim().isEmpty();
        } else if (role == Role.ROLE_CENTRAL) {
            return branch == null;
        }

        return true;
    }
}
