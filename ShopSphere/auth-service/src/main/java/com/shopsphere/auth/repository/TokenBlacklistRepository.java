package com.shopsphere.auth.repository;

import com.shopsphere.auth.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    boolean existsByTokenHash(String tokenHash);
}
