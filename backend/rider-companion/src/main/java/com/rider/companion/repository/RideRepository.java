package com.rider.companion.repository;

import com.rider.companion.entity.RideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RideRepository extends JpaRepository<RideEntity, Long> {
  List<RideEntity> findByMotorcycleId(Long motorcycleId);
  List<RideEntity> findByMotorcycleUserId(Long userId);
}
