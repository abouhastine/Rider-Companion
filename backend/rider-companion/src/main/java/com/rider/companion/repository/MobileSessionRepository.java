package com.rider.companion.repository;

import com.rider.companion.entity.MobileSessionEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MobileSessionRepository extends JpaRepository<MobileSessionEntity, String> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<MobileSessionEntity> findLockedById(String id);
}
