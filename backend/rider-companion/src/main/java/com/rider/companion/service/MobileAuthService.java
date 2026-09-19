package com.rider.companion.service;

import com.rider.companion.config.JwtService;
import com.rider.companion.dto.AuthResponse;
import com.rider.companion.dto.MobileAuthRequest;
import com.rider.companion.dto.MobileAuthResponse;
import com.rider.companion.dto.MobileRegisterRequest;
import com.rider.companion.entity.MobileSessionEntity;
import com.rider.companion.entity.RiderEntity;
import com.rider.companion.entity.UserEntity;
import com.rider.companion.repository.MobileSessionRepository;
import com.rider.companion.repository.RiderRepository;
import com.rider.companion.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MobileAuthService {
  private static final long ACCESS_TOKEN_SECONDS = 15 * 60;
  private final UserRepository users;
  private final RiderRepository riders;
  private final MobileSessionRepository sessions;
  private final JwtService jwtService;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final SecureRandom random = new SecureRandom();
  private final long refreshLifetimeSeconds;

  public MobileAuthService(UserRepository users, RiderRepository riders, MobileSessionRepository sessions,
      JwtService jwtService, @Value("${app.mobile.refresh-expiry-seconds:2592000}") long refreshLifetimeSeconds) {
    this.users = users;
    this.riders = riders;
    this.sessions = sessions;
    this.jwtService = jwtService;
    this.refreshLifetimeSeconds = refreshLifetimeSeconds;
  }

  @Transactional
  public MobileAuthResponse register(MobileRegisterRequest request) {
    MobileAuthRequest credentials = request.credentials();
    if (users.findByEmail(credentials.email()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
    }
    UserEntity user = new UserEntity();
    user.setFirstName(request.firstName()); user.setLastName(request.lastName());
    user.setEmail(credentials.email()); user.setPasswordHash(passwordEncoder.encode(credentials.password()));
    user = users.save(user);
    RiderEntity rider = new RiderEntity(); rider.setUserId(user.getId()); riders.save(rider);
    return createSession(user, credentials);
  }

  @Transactional
  public MobileAuthResponse login(MobileAuthRequest request) {
    UserEntity user = users.findByEmail(request.email())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }
    return createSession(user, request);
  }

  @Transactional(noRollbackFor = ResponseStatusException.class)
  public MobileAuthResponse refresh(String refreshToken) {
    TokenParts parts = parse(refreshToken);
    MobileSessionEntity session = sessions.findLockedById(parts.sessionId()).orElseThrow(this::invalidSession);
    if (session.getRevokedAt() != null || !session.getExpiresAt().isAfter(Instant.now())) throw invalidSession();
    if (!MessageDigest.isEqual(session.getRefreshSecretHash().getBytes(StandardCharsets.US_ASCII),
        hash(parts.secret()).getBytes(StandardCharsets.US_ASCII))) {
      session.setRevokedAt(Instant.now()); sessions.save(session); throw invalidSession();
    }
    String nextSecret = secret();
    session.setRefreshSecretHash(hash(nextSecret)); session.setLastUsedAt(Instant.now()); sessions.save(session);
    UserEntity user = users.findById(session.getUserId()).orElseThrow(this::invalidSession);
    return response(user, session, nextSecret);
  }

  @Transactional
  public void logout(String refreshToken) {
    try {
      TokenParts parts = parse(refreshToken);
      sessions.findById(parts.sessionId()).ifPresent(session -> {
        if (MessageDigest.isEqual(session.getRefreshSecretHash().getBytes(StandardCharsets.US_ASCII),
            hash(parts.secret()).getBytes(StandardCharsets.US_ASCII))) {
          session.setRevokedAt(Instant.now()); sessions.save(session);
        }
      });
    } catch (ResponseStatusException ignored) { /* client token removal is still idempotent */ }
  }

  private MobileAuthResponse createSession(UserEntity user, MobileAuthRequest metadata) {
    Instant now = Instant.now(); String secret = secret();
    MobileSessionEntity session = new MobileSessionEntity();
    session.setId(UUID.randomUUID().toString()); session.setUserId(user.getId());
    session.setRefreshSecretHash(hash(secret)); session.setIssuedAt(now); session.setLastUsedAt(now);
    session.setExpiresAt(now.plusSeconds(refreshLifetimeSeconds)); session.setDeviceName(metadata.deviceName());
    session.setPlatform(metadata.platform()); session.setAppVersion(metadata.appVersion()); sessions.save(session);
    return response(user, session, secret);
  }

  private MobileAuthResponse response(UserEntity user, MobileSessionEntity session, String secret) {
    JwtService.IssuedToken access = jwtService.issue(user.getId(), user.getEmail(), ACCESS_TOKEN_SECONDS);
    return new MobileAuthResponse(access.token(), access.expiresAt(), session.getId() + "." + secret,
        session.getExpiresAt(), new AuthResponse.UserSummary(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()));
  }

  private String secret() { byte[] value = new byte[32]; random.nextBytes(value); return Base64.getUrlEncoder().withoutPadding().encodeToString(value); }
  private String hash(String value) { try { return Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); } catch (Exception e) { throw new IllegalStateException(e); } }
  private TokenParts parse(String token) { String[] parts = token.split("\\."); if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) throw invalidSession(); return new TokenParts(parts[0], parts[1]); }
  private ResponseStatusException invalidSession() { return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Mobile session expired or invalid"); }
  private record TokenParts(String sessionId, String secret) {}
}
