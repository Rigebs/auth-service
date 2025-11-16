package com.rige.infrastructure.persistence.mappers;

import com.rige.domain.models.User;
import com.rige.infrastructure.persistence.UserJpaEntity;

public class UserJpaMapper {

    public static User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getName(),
                entity.getPictureUrl(),
                entity.getProvider(),
                entity.getRoles(),
                entity.isEmailVerified()
        );
    }

    public static UserJpaEntity toEntity(User domain) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(domain.getId());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());
        entity.setName(domain.getName());
        entity.setPictureUrl(domain.getPictureUrl());
        entity.setProvider(domain.getProvider());
        entity.setRoles(domain.getRoles());
        entity.setEmailVerified(domain.isEmailVerified());
        return entity;
    }
}
