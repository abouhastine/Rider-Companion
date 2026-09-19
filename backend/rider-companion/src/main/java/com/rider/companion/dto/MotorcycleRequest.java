package com.rider.companion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Payload for creating or updating a motorcycle")
public record MotorcycleRequest(
    @Schema(example = "1", description = "Identifier of the motorcycle owner")
    @NotNull(message = "User is required") Long user,
    @Schema(example = "Yamaha") @NotBlank(message = "Brand is required") String brand,
    @Schema(example = "MT-07") @NotBlank(message = "Model is required") String model,
    @Schema(example = "2024") @Min(value = 1885, message = "Year must be 1885 or later") Integer year,
    Integer engineCapacity,
    Integer power,
    String fuelType,
    String registrationNumber,
    LocalDate purchaseDate,
    Integer currentMileage,
    Double averageConsumption,
    String imageUrl,
    Boolean primaryMotorcycle) {}
