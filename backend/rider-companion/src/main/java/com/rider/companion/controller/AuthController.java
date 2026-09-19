package com.rider.companion.controller;

import com.rider.companion.dto.AuthResponse;
import com.rider.companion.dto.LoginRequest;
import com.rider.companion.dto.SignInRequest;
import com.rider.companion.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api")
@Tag(name = "Authentication", description = "Authentication operations")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/sign-in")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Register a user", description = "Creates a new user account.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "User registered"),
    @ApiResponse(responseCode = "400", description = "Invalid request body")
  })
  public AuthResponse signIn(@RequestBody SignInRequest request) {

    return authService.signIn(request);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Login", description = "Authenticates a user using email and password hash.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Login successful"),
    @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public AuthResponse login(@RequestBody LoginRequest request) {

    return authService.login(request);
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Logout", description = "Logs out a user.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Logout successful"),
    @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public AuthResponse logout(@RequestHeader("Authorization") String authorization) {
    if (!authorization.startsWith("Bearer ")) {
      throw new org.springframework.web.server.ResponseStatusException(
          HttpStatus.UNAUTHORIZED, "Missing access token");
    }
    return authService.logout(authorization.substring(7));
  }
}
