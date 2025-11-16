package com.rige.application.service;

import com.rige.application.port.in.AuthenticateUserUseCase;
import com.rige.application.port.out.TokenProviderPort;
import com.rige.application.port.out.TokenStorePort;
import com.rige.application.port.out.UserRepositoryPort;
import com.rige.domain.models.Token;
import com.rige.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticateUserService implements AuthenticateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final TokenProviderPort tokenProvider;
    private final TokenStorePort tokenStore;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse authenticate(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);

        // Guardamos refresh token
        tokenStore.save(new Token(
                refreshToken,
                user.getId(),
                Instant.now().plusSeconds(60 * 60 * 24 * 7)
        ));

        return new AuthResponse(accessToken, refreshToken);
    }
}
