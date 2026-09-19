package com.rider.companion.service;

import com.rider.companion.dto.DashboardResponse;
import com.rider.companion.entity.MaintenanceRecordEntity;
import com.rider.companion.entity.MotocycleEntity;
import com.rider.companion.entity.RideEntity;
import com.rider.companion.entity.RiderEntity;
import com.rider.companion.entity.UserEntity;
import com.rider.companion.exception.UserNotFoundException;
import com.rider.companion.repository.MaintenanceRecordRepository;
import com.rider.companion.repository.MotorcycleRepository;
import com.rider.companion.repository.RideRepository;
import com.rider.companion.repository.RiderRepository;
import com.rider.companion.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

  private final UserRepository userRepository;
  private final RiderRepository riderRepository;
  private final MotorcycleRepository motorcycleRepository;
  private final MaintenanceRecordRepository maintenanceRecordRepository;
  private final RideRepository rideRepository;

  public DashboardService(
      UserRepository userRepository,
      RiderRepository riderRepository,
      MotorcycleRepository motorcycleRepository,
      MaintenanceRecordRepository maintenanceRecordRepository,
      RideRepository rideRepository) {
    this.userRepository = userRepository;
    this.riderRepository = riderRepository;
    this.motorcycleRepository = motorcycleRepository;
    this.maintenanceRecordRepository = maintenanceRecordRepository;
    this.rideRepository = rideRepository;
  }

  public DashboardResponse getDashboard(Long userId) {
    UserEntity user = userId == null ? null : userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    RiderEntity rider = userId == null ? null : riderRepository.findByUserId(userId).orElse(null);

    List<MotocycleEntity> motorcycles = userId == null
        ? motorcycleRepository.findAll()
        : motorcycleRepository.findByUserId(userId);
    List<MaintenanceRecordEntity> maintenance = userId == null
        ? maintenanceRecordRepository.findAll()
        : maintenanceRecordRepository.findByMotorcycleUserId(userId);
    List<RideEntity> rides = userId == null
        ? rideRepository.findAll()
        : rideRepository.findByMotorcycleUserId(userId);

    return new DashboardResponse(
        user == null ? null : new DashboardResponse.UserSummary(
            user.getFirstName(), rider == null ? null : rider.getExperienceLevel()),
        motorcycles.size(),
        motorcycles.stream().filter(motorcycle -> Boolean.TRUE.equals(motorcycle.getPrimaryMotorcycle()))
            .findFirst().map(this::toPrimaryMotorcycle).orElse(null),
        toMaintenanceSummary(maintenance),
        toStatistics(maintenance, rides));
  }

  private DashboardResponse.PrimaryMotorcycle toPrimaryMotorcycle(MotocycleEntity motorcycle) {
    return new DashboardResponse.PrimaryMotorcycle(
        motorcycle.getId(), motorcycle.getBrand(), motorcycle.getModel(), motorcycle.getYear(),
        motorcycle.getCurrentMileage(), motorcycle.getImageUrl());
  }

  private DashboardResponse.MaintenanceSummary toMaintenanceSummary(
      List<MaintenanceRecordEntity> maintenance) {
    LocalDate today = LocalDate.now();
    DashboardResponse.LastMaintenance lastMaintenance = maintenance.stream()
        .filter(record -> record.getCompletionDate() != null)
        .max(Comparator.comparing(MaintenanceRecordEntity::getCompletionDate))
        .map(record -> new DashboardResponse.LastMaintenance(
            record.getMaintenanceType(), record.getCompletionDate(), record.getMileage()))
        .orElse(null);
    long upcomingCount = maintenance.stream()
        .filter(record -> isOpen(record) && record.getPlannedDate() != null
            && !record.getPlannedDate().isBefore(today))
        .count();
    long overdueCount = maintenance.stream()
        .filter(record -> isOpen(record) && record.getPlannedDate() != null
            && record.getPlannedDate().isBefore(today))
        .count();
    return new DashboardResponse.MaintenanceSummary(lastMaintenance, upcomingCount, overdueCount);
  }

  private DashboardResponse.Statistics toStatistics(
      List<MaintenanceRecordEntity> maintenance, List<RideEntity> rides) {
    BigDecimal totalCost = maintenance.stream()
        .map(MaintenanceRecordEntity::getCost)
        .filter(cost -> cost != null)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    long estimatedDistance = rides.stream()
        .map(RideEntity::getEstimatedDistance)
        .filter(distance -> distance != null)
        .mapToLong(Integer::longValue)
        .sum();
    long plannedRides = rides.stream().filter(ride -> "PLANNED".equals(ride.getStatus())).count();
    return new DashboardResponse.Statistics(totalCost, maintenance.size(), plannedRides, estimatedDistance);
  }

  private boolean isOpen(MaintenanceRecordEntity record) {
    return !"COMPLETED".equals(record.getStatus());
  }
}
