package com.qhapaq.oreo.auth.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = BranchDtoValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface BranchDtoConstraint {
    String message() default "Invalid branch/role combination";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

