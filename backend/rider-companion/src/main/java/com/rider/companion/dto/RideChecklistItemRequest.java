package com.rider.companion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload for creating or updating a ride checklist item")
public record RideChecklistItemRequest(
    @Schema(example = "1", description = "Identifier of the associated ride")
    @NotNull(message = "Ride is required") Long ride,
    String label,
    Boolean checked) {}
