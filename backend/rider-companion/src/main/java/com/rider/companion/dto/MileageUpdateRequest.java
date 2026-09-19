package com.rider.companion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload for updating a motorcycle odometer reading")
public record MileageUpdateRequest(
    @Schema(example = "15000")
    @NotNull(message = "Current mileage is required")
    @Min(value = 0, message = "Current mileage must be zero or greater")
    Integer currentMileage) {}
