package com.rige.infrastructure.persistence.mappers;

import com.rige.domain.models.Token;
import com.rige.infrastructure.persistence.TokenJpaEntity;

public class TokenJpaMapper {

    public static Token toDomain(TokenJpaEntity entity) {
        return new Token(
                entity.getRefreshToken(),
                entity.getUserId(),
                entity.getExpiresAt()
        );
    }

    public static TokenJpaEntity toEntity(Token domain) {
        TokenJpaEntity entity = new TokenJpaEntity();
        entity.setRefreshToken(domain.getRefreshToken());
        entity.setUserId(domain.getUserId());
        entity.setExpiresAt(domain.getExpiresAt());
        return entity;
    }
}
