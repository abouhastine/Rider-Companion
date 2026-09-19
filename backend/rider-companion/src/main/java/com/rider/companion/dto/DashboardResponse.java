package com.rider.companion.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DashboardResponse(
    UserSummary user,
    long motorcyclesCount,
    PrimaryMotorcycle primaryMotorcycle,
    MaintenanceSummary maintenance,
    Statistics statistics) {

  public record UserSummary(String firstName, String experienceLevel) {}

  public record PrimaryMotorcycle(
      Long id, String brand, String model, Integer year, Integer currentMileage, String imageUrl) {}

  public record MaintenanceSummary(
      LastMaintenance lastMaintenance, long upcomingCount, long overdueCount) {}

  public record LastMaintenance(String type, LocalDate date, Integer mileage) {}

  public record Statistics(
      BigDecimal maintenanceTotalCost,
      long maintenanceCount,
      long plannedRidesCount,
      long estimatedRideDistance) {}
}
