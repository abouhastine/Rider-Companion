package com.rider.companion.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
  private static final Base64.Decoder DECODER = Base64.getUrlDecoder();
  private final ObjectMapper objectMapper;
  private final byte[] secret;
  private final long expirySeconds;

  public JwtService(
      @Value("${app.jwt.secret:change-this-local-development-secret-to-at-least-32-characters}")
          String secret,
      @Value("${app.jwt.expiry-seconds:1800}") long expirySeconds) {
    this.objectMapper = new ObjectMapper();
    this.secret = secret.getBytes(StandardCharsets.UTF_8);
    this.expirySeconds = expirySeconds;
  }

  public IssuedToken issue(Long userId, String email) {
    return issue(userId, email, expirySeconds);
  }

  /** Issues an access token with a caller-selected, bounded lifetime. */
  public IssuedToken issue(Long userId, String email, long lifetimeSeconds) {
    Instant now = Instant.now();
    Instant expiresAt = now.plusSeconds(lifetimeSeconds);
    String jti = UUID.randomUUID().toString();
    try {
      String header = encode(Map.of("alg", "HS256", "typ", "JWT"));
      String payload =
          encode(
              Map.of(
                  "sub",
                  userId,
                  "email",
                  email,
                  "jti",
                  jti,
                  "iat",
                  now.getEpochSecond(),
                  "exp",
                  expiresAt.getEpochSecond()));
      String unsigned = header + "." + payload;
      return new IssuedToken(unsigned + "." + sign(unsigned), expiresAt, jti);
    } catch (Exception exception) {
      throw new IllegalStateException("Unable to issue access token", exception);
    }
  }

  public Claims verify(String token) {
    try {
      String[] parts = token.split("\\.");
      if (parts.length != 3
          || !MessageDigest.isEqual(
              sign(parts[0] + "." + parts[1]).getBytes(StandardCharsets.US_ASCII),
              parts[2].getBytes(StandardCharsets.US_ASCII))) {
        throw new IllegalArgumentException("Invalid token");
      }
      JsonNode payload = objectMapper.readTree(DECODER.decode(parts[1]));
      Instant expiresAt = Instant.ofEpochSecond(payload.required("exp").asLong());
      if (!expiresAt.isAfter(Instant.now())) throw new IllegalArgumentException("Token expired");
      return new Claims(
          payload.required("sub").asLong(),
          payload.required("email").asText(),
          payload.required("jti").asText(),
          expiresAt);
    } catch (Exception exception) {
      throw new IllegalArgumentException("Invalid access token", exception);
    }
  }

  private String encode(Object value) throws Exception {
    return ENCODER.encodeToString(objectMapper.writeValueAsBytes(value));
  }

  private String sign(String value) throws Exception {
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(secret, "HmacSHA256"));
    return ENCODER.encodeToString(mac.doFinal(value.getBytes(StandardCharsets.US_ASCII)));
  }

  public record IssuedToken(String token, Instant expiresAt, String jti) {}

  public record Claims(Long userId, String email, String jti, Instant expiresAt) {}
}
