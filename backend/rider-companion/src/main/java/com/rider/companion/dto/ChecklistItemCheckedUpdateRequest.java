package com.rider.companion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload for checking or unchecking a ride checklist item")
public record ChecklistItemCheckedUpdateRequest(
    @Schema(example = "true")
    @NotNull(message = "Checked value is required")
    Boolean checked) {}
