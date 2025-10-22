package com.qhapaq.oreo.auth.application;
import com.qhapaq.oreo.auth.domain.AuthService;
import com.qhapaq.oreo.auth.dto.JwtAuthLoginDto;
import com.qhapaq.oreo.auth.dto.JwtAuthResponseDto;
import com.qhapaq.oreo.user.dto.CreateUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public JwtAuthResponseDto login(@Valid @RequestBody JwtAuthLoginDto loginDto) {
        return authService.jwtLogin(loginDto);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtAuthResponseDto register(@Valid @RequestBody CreateUserDto registerDto) {
        return authService.jwtRegister(registerDto);
    }

}

