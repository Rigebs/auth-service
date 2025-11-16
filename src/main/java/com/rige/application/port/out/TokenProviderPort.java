package com.rige.application.port.out;

import com.rige.domain.models.User;

public interface TokenProviderPort {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);
}
