package com.rige.application.service;

import com.rige.application.port.in.RefreshTokenUseCase;
import com.rige.application.port.out.TokenProviderPort;
import com.rige.application.port.out.TokenStorePort;
import com.rige.application.port.out.UserRepositoryPort;
import com.rige.domain.models.Token;
import com.rige.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

    private final TokenStorePort tokenStore;
    private final UserRepositoryPort userRepository;
    private final TokenProviderPort tokenProvider;

    @Override
    public String refreshAccessToken(String refreshToken) {

        Token token = tokenStore.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token no válido"));

        if (token.getExpiresAt().isBefore(java.time.Instant.now())) {
            throw new RuntimeException("Refresh token expirado");
        }

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        return tokenProvider.generateAccessToken(user);
    }
}
