package com.rider.companion.dto;

import java.time.Instant;
import java.time.LocalDate;

public record MotorcycleResponse(
    Long id,
    String brand,
    String model,
    Integer year,
    Integer engineCapacity,
    Integer power,
    String fuelType,
    String registrationNumber,
    LocalDate purchaseDate,
    Integer currentMileage,
    Double averageConsumption,
    Boolean primaryMotorcycle,
    boolean hasImage,
    Instant imageUpdatedAt) {}
