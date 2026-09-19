package com.rider.companion.dto;

import jakarta.validation.constraints.NotBlank;

public record MobileRefreshRequest(@NotBlank String refreshToken) {}
