package com.rider.companion.repository;

import com.rider.companion.entity.RevokedTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokedTokenRepository extends JpaRepository<RevokedTokenEntity, String> {}
