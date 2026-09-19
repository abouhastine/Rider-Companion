package com.rider.companion.repository;

import com.rider.companion.entity.MotorcycleImageEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotorcycleImageRepository extends JpaRepository<MotorcycleImageEntity, Long> {
  Optional<MotorcycleImageEntity> findByMotorcycleId(Long motorcycleId);

  boolean existsByMotorcycleId(Long motorcycleId);

  void deleteByMotorcycleId(Long motorcycleId);
}
