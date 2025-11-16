package com.rige.application.service;

import com.rige.application.port.in.GetUserProfileUseCase;
import com.rige.application.port.out.UserRepositoryPort;
import com.rige.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService implements GetUserProfileUseCase {

    private final UserRepositoryPort userRepository;

    @Override
    public User getCurrentUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
