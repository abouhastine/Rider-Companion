package com.rider.companion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Payload for creating or updating a maintenance record")
public record MaintenanceRecordRequest(
    @Schema(example = "1", description = "Identifier of the associated motorcycle")
    @NotNull(message = "Motorcycle is required") Long motorcycle,
    String maintenanceType,
    String status,
    LocalDate completionDate,
    LocalDate plannedDate,
    Integer mileage,
    Integer plannedMileage,
    BigDecimal cost,
    String serviceProvider,
    String notes) {}
