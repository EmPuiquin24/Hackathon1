package com.qhapaq.oreo.auth.dto;

import lombok.Data;

@Data
public class JwtAuthResponseDto {

    private String token;

    private Integer ExpiresIn;

    private String Role;

    private Boolean IsBranch;
}
