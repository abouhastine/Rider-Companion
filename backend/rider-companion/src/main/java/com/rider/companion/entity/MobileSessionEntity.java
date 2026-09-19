package com.rider.companion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mobile_sessions")
@Getter
@Setter
@NoArgsConstructor
public class MobileSessionEntity {
  @Id
  private String id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "refresh_secret_hash", nullable = false)
  private String refreshSecretHash;

  @Column(name = "issued_at", nullable = false)
  private Instant issuedAt;

  @Column(name = "expires_at", nullable = false)
  private Instant expiresAt;

  @Column(name = "last_used_at", nullable = false)
  private Instant lastUsedAt;

  @Column(name = "revoked_at")
  private Instant revokedAt;

  @Column(name = "device_name", length = 120)
  private String deviceName;

  @Column(name = "platform", length = 30)
  private String platform;

  @Column(name = "app_version", length = 40)
  private String appVersion;
}
