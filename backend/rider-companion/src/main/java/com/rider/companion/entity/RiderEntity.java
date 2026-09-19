package com.rider.companion.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "riders")
@Schema(description = "A rider using the Rider Companion application")
@Getter
@Setter
@NoArgsConstructor
public class RiderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(example = "10", description = "Identifier of the associated user account")
    private Long userId;

    @Schema(example = "A", description = "Motorcycle driving license category")
    private String licenseType;

    @Schema(example = "2022", description = "Year the driving license was obtained")
    private Integer licenseYear;

    @Schema(example = "INTERMEDIATE", description = "Rider experience level")
    private String experienceLevel;

    @Schema(example = "COMMUTING", description = "Primary motorcycle usage")
    private String primaryUsage;

    @Schema(example = "12000", description = "Estimated annual distance in kilometers")
    private Integer estimatedAnnualDistance;

}