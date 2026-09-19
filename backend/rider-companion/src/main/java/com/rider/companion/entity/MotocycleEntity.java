package com.rider.companion.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.time.LocalDate;

@Entity
@Table(name = "motorcycles")
@Schema(description = "A motorcycle stored in the rider's virtual garage")
@Getter
@Setter
@NoArgsConstructor
public class MotocycleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Schema(example = "1")
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @Schema(example = "Yamaha")
  @NotBlank(message = "Brand is required")
  private String brand;

  @Schema(example = "MT-07")
  @NotBlank(message = "Model is required")
  private String model;

  @Schema(example = "2024")
  @Min(value = 1885, message = "Year must be 1885 or later")
  @Column(name = "manufacture_year")
  private Integer year;

  @Schema(example = "689")
  private Integer engineCapacity;

  @Schema(example = "74")
  private Integer power;

  @Schema(example = "Gasoline")
  private String fuelType;

  @Schema(example = "123 TUN 456")
  private String registrationNumber;

  @Schema(example = "2024-01-15")
  private LocalDate purchaseDate;

  @Schema(example = "15000")
  private Integer currentMileage;

  @Schema(example = "4.5")
  private Double averageConsumption;

  @Schema(example = "https://example.com/mt07.jpg")
  private String imageUrl;

  @Schema(example = "true")
  private Boolean primaryMotorcycle;

  @Schema(example = "2026-08-01")
  private LocalDate createdAt;

  @Schema(example = "2026-08-01")
  private LocalDate updatedAt;

  @JsonIgnore
  @OneToMany(mappedBy = "motorcycle")
  private List<MaintenanceRecordEntity> maintenanceRecords;

  @JsonIgnore
  @OneToMany(mappedBy = "motorcycle")
  private List<RideEntity> rides;
}
