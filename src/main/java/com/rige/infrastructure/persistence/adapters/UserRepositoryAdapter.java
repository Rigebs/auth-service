package com.rige.infrastructure.persistence.adapters;

import com.rige.application.port.out.UserRepositoryPort;
import com.rige.domain.models.User;
import com.rige.infrastructure.persistence.mappers.UserJpaMapper;
import com.rige.infrastructure.persistence.repositories.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(UserJpaMapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(UserJpaMapper::toDomain);
    }

    @Override
    public User save(User user) {
        return UserJpaMapper.toDomain(
                jpaRepository.save(UserJpaMapper.toEntity(user))
        );
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
