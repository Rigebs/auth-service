package com.rige.application.service;

import com.rige.application.port.in.OAuthLoginUseCase;
import com.rige.application.port.out.UserRepositoryPort;
import com.rige.domain.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService implements OAuthLoginUseCase {

    private final UserRepositoryPort userRepository;

    @Override
    public User loginWithOAuth(OAuth2UserInfo info) {

        if (!info.isEmailVerified()) {
            throw new RuntimeException("Email no verificado por Google.");
        }

        return userRepository
                .findByEmail(info.getEmail())
                .map(existing -> updateUserIfNeeded(existing, info))
                .orElseGet(() -> registerNewUser(info));
    }

    private User updateUserIfNeeded(User user, OAuth2UserInfo info) {
        user.setName(info.getName());
        user.setPictureUrl(info.getPictureUrl());
        user.setEmailVerified(info.isEmailVerified());
        user.setProvider(AuthProvider.GOOGLE);
        return userRepository.save(user);
    }

    private User registerNewUser(OAuth2UserInfo info) {
        User user = new User(
                null,
                info.getEmail(),
                null,
                info.getName(),
                info.getPictureUrl(),
                AuthProvider.GOOGLE,
                Set.of(Role.USER),
                info.isEmailVerified()
        );
        return userRepository.save(user);
    }
}

