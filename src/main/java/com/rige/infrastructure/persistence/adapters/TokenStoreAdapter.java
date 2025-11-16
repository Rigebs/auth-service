package com.rige.infrastructure.persistence.adapters;

import com.rige.application.port.out.TokenStorePort;
import com.rige.domain.models.Token;
import com.rige.infrastructure.persistence.mappers.TokenJpaMapper;
import com.rige.infrastructure.persistence.repositories.JpaTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenStoreAdapter implements TokenStorePort {

    private final JpaTokenRepository jpaRepository;

    @Override
    public Token save(Token token) {
        return TokenJpaMapper.toDomain(
                jpaRepository.save(TokenJpaMapper.toEntity(token))
        );
    }

    @Override
    public Optional<Token> findByRefreshToken(String refreshToken) {
        return jpaRepository.findById(refreshToken)
                .map(TokenJpaMapper::toDomain);
    }

    @Override
    public void deleteByRefreshToken(String refreshToken) {
        jpaRepository.deleteById(refreshToken);
    }

    @Override
    public void deleteAllTokensForUser(Long userId) {
        jpaRepository.deleteByUserId(userId);
    }
}
