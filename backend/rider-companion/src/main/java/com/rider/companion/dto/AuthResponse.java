package com.rider.companion.dto;

import java.time.Instant;

public record AuthResponse(String message, String token, Instant expiresAt, UserSummary user) {
  public record UserSummary(Long id, String firstName, String lastName, String email) {}
}
