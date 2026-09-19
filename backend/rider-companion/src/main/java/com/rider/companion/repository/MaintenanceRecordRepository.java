package com.rider.companion.repository;

import com.rider.companion.entity.MaintenanceRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecordEntity, Long> {
  List<MaintenanceRecordEntity> findByMotorcycleId(Long motorcycleId);
  List<MaintenanceRecordEntity> findByMotorcycleUserId(Long userId);
}
