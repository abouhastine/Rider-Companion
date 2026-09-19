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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance_records")
@Schema(description = "A maintenance record associated with a motorcycle")
@Getter
@Setter
@NoArgsConstructor
public class MaintenanceRecordEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Schema(example = "1", description = "Associated motorcycle identifier")
  @ManyToOne
  @JoinColumn(name = "motorcycle_id")
  private MotocycleEntity motorcycle;

  @Schema(example = "Oil Change")
  private String maintenanceType;

  @Schema(example = "COMPLETED")
  private String status;

  @Schema(example = "2026-07-31")
  private LocalDate completionDate;

  @Schema(example = "2026-08-15")
  private LocalDate plannedDate;

  @Schema(example = "15000")
  private Integer mileage;

  @Schema(example = "18000")
  private Integer plannedMileage;

  @Schema(example = "120.50")
  private BigDecimal cost;

  @Schema(example = "Yamaha Service Center")
  private String serviceProvider;

  @Schema(example = "Changed oil and filter")
  private String notes;

  @Schema(example = "2026-07-31")
  private LocalDate createdAt;

  @Schema(example = "2026-08-01")
  private LocalDate updatedAt;
}
