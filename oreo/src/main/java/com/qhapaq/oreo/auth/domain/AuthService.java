package com.qhapaq.oreo.auth.domain;

import com.qhapaq.oreo.auth.dto.JwtAuthLoginDto;
import com.qhapaq.oreo.auth.dto.JwtAuthResponseDto;
import com.qhapaq.oreo.jwt.JwtService;
import com.qhapaq.oreo.user.domain.User;
import com.qhapaq.oreo.user.domain.UserService;
import com.qhapaq.oreo.user.dto.CreateUserDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthResponseDto jwtLogin(JwtAuthLoginDto loginDto) {
        User user = userService.getUserByUsername(loginDto.getUsername());

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        JwtAuthResponseDto response = new JwtAuthResponseDto();
        response.setToken(jwtService.generateToken(user));
        return response;
    }

//    public JwtAuthResponseDto jwtRegister(JwtRegisterRequestDto registerDto) {
//        ModelMapper modelMapper = new ModelMapper();
//        CreateUserDto createUserDto = modelMapper.map(registerDto, CreateUserDto.class);
//
//        User user = userService.createUser(createUserDto);
//
//        JwtAuthResponseDto response = new JwtAuthResponseDto();
//        response.setToken(jwtService.generateToken(user));
//        return response;
//    }

    public JwtAuthResponseDto jwtRegister(CreateUserDto userDto) {
        User user = userService.createUser(userDto);

        JwtAuthResponseDto response = new JwtAuthResponseDto();
        response.setToken(jwtService.generateToken(user));
        return response;
    }



}
