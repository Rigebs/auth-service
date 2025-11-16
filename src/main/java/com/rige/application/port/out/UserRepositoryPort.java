package com.rige.application.port.out;

import com.rige.domain.models.User;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    User save(User user);

    boolean existsByEmail(String email);
}
