package com.rider.companion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload for updating whether a motorcycle is primary")
public record PrimaryMotorcycleUpdateRequest(
    @Schema(example = "true")
    @NotNull(message = "Primary motorcycle value is required")
    Boolean primaryMotorcycle) {}
