package com.rige.application.port.out;

import com.rige.domain.models.Token;

import java.util.Optional;

public interface TokenStorePort {

    Token save(Token token);

    Optional<Token> findByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);

    void deleteAllTokensForUser(Long userId);
}
