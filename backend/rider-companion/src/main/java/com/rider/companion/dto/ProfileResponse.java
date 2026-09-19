package com.rider.companion.dto;

public record ProfileResponse(
    Long userId,
    Long riderId,
    String firstName,
    String lastName,
    String email,
    String licenseType,
    Integer licenseYear,
    String experienceLevel,
    String primaryUsage,
    Integer estimatedAnnualDistance) {}
