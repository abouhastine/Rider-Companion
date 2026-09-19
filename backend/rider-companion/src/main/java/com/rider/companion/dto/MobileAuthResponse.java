package com.rider.companion.dto;

import java.time.Instant;

public record MobileAuthResponse(
    String accessToken,
    Instant accessTokenExpiresAt,
    String refreshToken,
    Instant refreshTokenExpiresAt,
    AuthResponse.UserSummary user) {}
