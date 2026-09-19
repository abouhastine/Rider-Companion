package com.rider.companion.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record RideResponse(
    Long id, MotorcycleSummary motorcycle, String title, LocalDate plannedDate,
    LocalTime departureTime, String departureLocation, String destination,
    Integer estimatedDistance, Integer actualDistance, Integer estimatedDuration,
    Integer actualDuration, String rideType, Boolean useHighway, Boolean useTolls,
    Integer plannedBreaks, BigDecimal fuelCost, String status, Integer rating, String notes,
    List<ChecklistItem> checklistItems) {
  public record MotorcycleSummary(Long id, String brand, String model) {}
  public record ChecklistItem(Long id, String label, Boolean checked) {}
}
