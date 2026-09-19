package com.rider.companion.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import java.util.List;

@Entity
@Table(name = "rides")
@Schema(description = "A motorcycle trip planned or completed by a rider")
@Getter
@Setter
@NoArgsConstructor
public class RideEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Schema(example = "1")
  @ManyToOne
  @JoinColumn(name = "motorcycle_id")
  private MotocycleEntity motorcycle;

  @Schema(example = "Weekend Trip")
  private String title;

  @Schema(example = "2026-08-01")
  private LocalDate plannedDate;

  @Schema(example = "08:30:00")
  private LocalTime departureTime;

  @Schema(example = "Tunis")
  private String departureLocation;

  @Schema(example = "Sousse")
  private String destination;

  @Schema(example = "150")
  private Integer estimatedDistance;

  @Schema(example = "145")
  private Integer actualDistance;

  @Schema(example = "120")
  private Integer estimatedDuration;

  @Schema(example = "130")
  private Integer actualDuration;

  @Schema(example = "TOURING")
  private String rideType;

  @Schema(example = "true")
  private Boolean useHighway;

  @Schema(example = "false")
  private Boolean useTolls;

  @Schema(example = "2")
  private Integer plannedBreaks;

  @Schema(example = "45.50")
  private BigDecimal fuelCost;

  @Schema(example = "COMPLETED")
  private String status;

  @Schema(example = "5")
  private Integer rating;

  @Schema(example = "Very enjoyable ride")
  private String notes;

  @Schema(example = "2026-08-01")
  private LocalDate createdAt;

  @Schema(example = "2026-08-01")
  private LocalDate updatedAt;

  @JsonManagedReference
  @OneToMany(mappedBy = "ride")
  private List<RideChecklistItemEntity> checklistItems;
}
