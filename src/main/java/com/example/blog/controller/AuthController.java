package com.example.blog.controller;

import com.example.blog.dto.request.LoginRequest;
import com.example.blog.dto.request.LogoutRequest;
import com.example.blog.dto.request.RefreshRequest;
import com.example.blog.dto.response.ApiResponse;
import com.example.blog.dto.request.RegisterRequest;
import com.example.blog.dto.response.AuthResponse;
import com.example.blog.dto.response.LoginResponse;
import com.example.blog.dto.response.RegisterResponse;
import com.example.blog.service.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("auth")
public class AuthController {
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request) {
        var result = authService.register(request);

        var response = ApiResponse.<RegisterResponse>builder()
                .result(result)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {

        var result = authService.login(request);

        return ApiResponse.<LoginResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestBody LogoutRequest request,
            @AuthenticationPrincipal Jwt jwt
    )
            throws ParseException, JOSEException {
        long userId = jwt.getClaim("userId");
        authService.logout(request, userId);
        var response = ApiResponse.<Void>builder()
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {

        var result = authService.refreshToken(request);
        return ApiResponse.<AuthResponse>builder()
                .result(result)
                .build();
    }
}
