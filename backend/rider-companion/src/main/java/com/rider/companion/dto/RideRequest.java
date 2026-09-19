package com.rider.companion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Payload for creating or updating a ride")
public record RideRequest(
    @Schema(example = "1", description = "Identifier of the motorcycle used for the ride")
    @NotNull(message = "Motorcycle is required") Long motorcycle,
    String title,
    LocalDate plannedDate,
    LocalTime departureTime,
    String departureLocation,
    String destination,
    Integer estimatedDistance,
    Integer actualDistance,
    Integer estimatedDuration,
    Integer actualDuration,
    String rideType,
    Boolean useHighway,
    Boolean useTolls,
    Integer plannedBreaks,
    BigDecimal fuelCost,
    String status,
    Integer rating,
    String notes) {}
