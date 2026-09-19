package com.rider.companion.repository;

import com.rider.companion.entity.MotocycleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MotorcycleRepository extends JpaRepository<MotocycleEntity, Long> {
  List<MotocycleEntity> findByUserId(Long userId);
}
