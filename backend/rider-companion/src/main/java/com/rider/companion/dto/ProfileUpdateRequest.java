package com.rider.companion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfileUpdateRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank String licenseType,
    @NotNull Integer licenseYear,
    @NotBlank String experienceLevel,
    @NotBlank String primaryUsage,
    @NotNull Integer estimatedAnnualDistance) {}
