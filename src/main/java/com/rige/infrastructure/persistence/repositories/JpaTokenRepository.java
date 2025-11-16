package com.rige.infrastructure.persistence.repositories;

import com.rige.infrastructure.persistence.TokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTokenRepository extends JpaRepository<TokenJpaEntity, String> {

    void deleteByUserId(Long userId);
}
