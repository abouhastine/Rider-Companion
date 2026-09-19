package com.rider.companion.service;

import com.rider.companion.dto.AuthResponse;
import com.rider.companion.dto.LoginRequest;
import com.rider.companion.dto.LogoutRequest;
import com.rider.companion.dto.SignInRequest;
import com.rider.companion.config.JwtService;
import com.rider.companion.entity.RiderEntity;
import com.rider.companion.entity.RevokedTokenEntity;
import com.rider.companion.repository.RiderRepository;
import com.rider.companion.repository.RevokedTokenRepository;
import java.time.Instant;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.rider.companion.entity.UserEntity;
import com.rider.companion.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final RiderRepository riderRepository;
  private final RevokedTokenRepository revokedTokenRepository;
  private final JwtService jwtService;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public AuthService(
      UserRepository userRepository,
      RiderRepository riderRepository,
      RevokedTokenRepository revokedTokenRepository,
      JwtService jwtService) {
    this.userRepository = userRepository;
    this.riderRepository = riderRepository;
    this.revokedTokenRepository = revokedTokenRepository;
    this.jwtService = jwtService;
  }

  public AuthResponse signIn(SignInRequest request) {

    if (userRepository.findByEmail(request.email()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
    }

    UserEntity user = new UserEntity();

    user.setFirstName(request.firstName());
    user.setLastName(request.lastName());
    user.setEmail(request.email());
    user.setPasswordHash(passwordEncoder.encode(request.passwordHash()));

    user = userRepository.save(user);
    RiderEntity rider = new RiderEntity();
    rider.setUserId(user.getId());
    riderRepository.save(rider);

    return response("User registered successfully", user);
  }

  public AuthResponse login(LoginRequest request) {

    UserEntity user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

    if (!passwordEncoder.matches(request.passwordHash(), user.getPasswordHash())) {

      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    return response("Login successful", user);
  }

  public AuthResponse logout(String token) {
    JwtService.Claims claims = jwtService.verify(token);
    RevokedTokenEntity revokedToken = new RevokedTokenEntity();
    revokedToken.setJti(claims.jti());
    revokedToken.setExpiresAt(claims.expiresAt());
    revokedTokenRepository.save(revokedToken);
    return new AuthResponse("Logout successful", null, null, null);
  }

  private AuthResponse response(String message, UserEntity user) {
    JwtService.IssuedToken issued = jwtService.issue(user.getId(), user.getEmail());
    return new AuthResponse(
        message,
        issued.token(),
        issued.expiresAt(),
        new AuthResponse.UserSummary(
            user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()));
  }
}
