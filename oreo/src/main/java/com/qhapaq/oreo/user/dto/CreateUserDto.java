package com.qhapaq.oreo.user.dto;

import com.qhapaq.oreo.user.domain.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserDto {

    @NotBlank
    @Size(min = 3, max = 30)
    @Pattern(regexp = "^[A-Za-z0-9_.]+$", message = "Username can contain letters, numbers, underscore and dot only")
    private String username;

    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull
    private Role role;

    @Size(max = 100)
    private String branch;
}
