package com.rige.infrastructure.controllers;

import com.rige.application.port.in.AuthenticateUserUseCase;
import com.rige.application.port.in.RefreshTokenUseCase;
import com.rige.application.port.in.AuthenticateUserUseCase.AuthResponse;
import com.rige.infrastructure.controllers.dto.LoginRequest;
import com.rige.infrastructure.controllers.dto.RefreshTokenRequest;
import com.rige.infrastructure.controllers.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateUserUseCase authUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        AuthResponse result = authUseCase.authenticate(request.email(), request.password());
        return ResponseEntity.ok(new TokenResponse(result.accessToken(), result.refreshToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request) {
        String newAccessToken = refreshTokenUseCase.refreshAccessToken(request.refreshToken());
        return ResponseEntity.ok(
                new TokenResponse(newAccessToken, request.refreshToken())
        );
    }
}
