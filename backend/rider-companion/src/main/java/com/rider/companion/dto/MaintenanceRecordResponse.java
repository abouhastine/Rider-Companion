package com.rider.companion.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MaintenanceRecordResponse(
    Long id, MotorcycleSummary motorcycle, String maintenanceType, String status,
    LocalDate completionDate, LocalDate plannedDate, Integer mileage, Integer plannedMileage,
    BigDecimal cost, String serviceProvider, String notes) {
  public record MotorcycleSummary(Long id, String brand, String model) {}
}
