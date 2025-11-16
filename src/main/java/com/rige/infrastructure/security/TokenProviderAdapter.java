package com.rige.infrastructure.security;

import com.rige.application.port.out.TokenProviderPort;
import com.rige.domain.models.User;
import com.rige.infrastructure.config.JwtConfig;
import io.jsonwebtoken.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

public class TokenProviderAdapter implements TokenProviderPort {

    private final JwtConfig jwtConfig;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public TokenProviderAdapter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.privateKey = jwtConfig.getPrivateKey();
        this.publicKey = jwtConfig.getPublicKey();
    }


    @Override
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(jwtConfig.getAccessTokenValiditySeconds());

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .claim("email", user.getEmail())
                .claim("roles", user.getRoles().stream().map(Enum::name).collect(Collectors.toList()))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(jwtConfig.getRefreshTokenValiditySeconds());

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .claim("type", "refresh")
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    // Validación helper (puedes exponerla si la necesitas)
    public Jws<Claims> parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException ex) {
            throw new RuntimeException("Token inválido: " + ex.getMessage(), ex);
        }
    }
}
