package com.rider.companion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Payload for updating a ride status")
public record RideStatusUpdateRequest(
    @Schema(example = "PLANNED", allowableValues = {"DRAFT", "PLANNED", "COMPLETED", "CANCELLED"})
    @NotBlank(message = "Ride status is required")
    @Pattern(regexp = "DRAFT|PLANNED|COMPLETED|CANCELLED", message = "Ride status is invalid")
    String status) {}
