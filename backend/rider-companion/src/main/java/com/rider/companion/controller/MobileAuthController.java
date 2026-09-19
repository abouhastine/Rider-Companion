package com.rider.companion.controller;

import com.rider.companion.dto.MobileAuthRequest;
import com.rider.companion.dto.MobileAuthResponse;
import com.rider.companion.dto.MobileRefreshRequest;
import com.rider.companion.dto.MobileRegisterRequest;
import com.rider.companion.service.MobileAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mobile/auth")
public class MobileAuthController {
  private final MobileAuthService service;
  public MobileAuthController(MobileAuthService service) { this.service = service; }
  @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
  public MobileAuthResponse register(@Valid @RequestBody MobileRegisterRequest request) { return service.register(request); }
  @PostMapping("/login") public MobileAuthResponse login(@Valid @RequestBody MobileAuthRequest request) { return service.login(request); }
  @PostMapping("/refresh") public MobileAuthResponse refresh(@Valid @RequestBody MobileRefreshRequest request) { return service.refresh(request.refreshToken()); }
  @PostMapping("/logout") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void logout(@Valid @RequestBody MobileRefreshRequest request) { service.logout(request.refreshToken()); }
}
