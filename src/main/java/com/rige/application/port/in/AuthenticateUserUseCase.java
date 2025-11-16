package com.rige.application.port.in;

public interface AuthenticateUserUseCase {

    AuthResponse authenticate(String email, String rawPassword);

    record AuthResponse(String accessToken, String refreshToken) {}
}
