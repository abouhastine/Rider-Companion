package com.rider.companion.dto;

public record SignInRequest(String firstName, String lastName, String email, String passwordHash) {}
