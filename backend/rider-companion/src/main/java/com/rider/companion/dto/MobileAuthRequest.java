package com.rider.companion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MobileAuthRequest(
    @Email @NotBlank String email,
    @NotBlank @Size(min = 8, max = 200) String password,
    @Size(max = 120) String deviceName,
    @Size(max = 30) String platform,
    @Size(max = 40) String appVersion) {}
