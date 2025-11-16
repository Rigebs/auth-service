package com.rige.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
public class TokenJpaEntity {

    @Id
    private String refreshToken;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "expires_at")
    private Instant expiresAt;
}
