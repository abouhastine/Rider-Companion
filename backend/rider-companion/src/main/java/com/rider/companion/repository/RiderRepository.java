package com.rider.companion.repository;

import com.rider.companion.entity.RiderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RiderRepository extends JpaRepository<RiderEntity, Long> {
  Optional<RiderEntity> findByUserId(Long userId);
}
