package com.rider.companion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MobileRegisterRequest(
    @NotBlank @Size(max = 255) String firstName,
    @NotBlank @Size(max = 255) String lastName,
    MobileAuthRequest credentials) {}
