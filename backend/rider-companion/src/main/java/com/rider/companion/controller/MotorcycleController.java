package com.rider.companion.controller;

import com.rider.companion.dto.MotorcycleRequest;
import com.rider.companion.dto.MileageUpdateRequest;
import com.rider.companion.dto.PrimaryMotorcycleUpdateRequest;
import com.rider.companion.entity.MotocycleEntity;
import com.rider.companion.service.MotorcycleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/motorcycles")
@Tag(name = "Motorcycles", description = "Operations for the rider's virtual garage")
public class MotorcycleController {
  private final MotorcycleService motorcycleService;

  public MotorcycleController(MotorcycleService motorcycleService) {
    this.motorcycleService = motorcycleService;
  }

  @GetMapping
  @Operation(
      summary = "List motorcycles",
      description = "Returns every motorcycle in the virtual garage.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Motorcycles returned")})
  public List<MotocycleEntity> getAllMotorcycles() {
    return motorcycleService.getAllMotorcycles();
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get a motorcycle",
      description = "Returns one motorcycle by its database identifier.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Motorcycle returned"),
    @ApiResponse(responseCode = "404", description = "Motorcycle not found")
  })
  public MotocycleEntity getMotorcycleById(
      @Parameter(description = "Motorcycle identifier", example = "1") @PathVariable Long id) {
    return motorcycleService.getMotorcycleById(id);
  }

  @GetMapping("/user/{userId}")
  @Operation(summary = "List motorcycles by user ID")
  public List<MotocycleEntity> getMotorcyclesByUserId(@PathVariable Long userId) {
    return motorcycleService.getMotorcyclesByUserId(userId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a motorcycle")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Motorcycle created"),
    @ApiResponse(responseCode = "400", description = "Invalid request body")
  })
  public MotocycleEntity createMotorcycle(@Valid @RequestBody MotorcycleRequest motorcycle) {
    return motorcycleService.createMotorcycle(motorcycle);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update a motorcycle",
      description = "Replaces the editable fields of an existing motorcycle.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Motorcycle updated"),
    @ApiResponse(responseCode = "400", description = "Invalid request body"),
    @ApiResponse(responseCode = "404", description = "Motorcycle not found")
  })
  public MotocycleEntity updateMotorcycle(
      @PathVariable Long id, @Valid @RequestBody MotorcycleRequest motorcycle) {
    return motorcycleService.updateMotorcycle(id, motorcycle);
  }

  @PatchMapping("/{id}/mileage")
  @Operation(summary = "Update motorcycle mileage")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Mileage updated"),
    @ApiResponse(responseCode = "400", description = "Invalid mileage"),
    @ApiResponse(responseCode = "404", description = "Motorcycle not found")
  })
  public MotocycleEntity updateMileage(
      @PathVariable Long id, @Valid @RequestBody MileageUpdateRequest request) {
    return motorcycleService.updateMileage(id, request.currentMileage());
  }

  @PatchMapping("/{id}/primary")
  @Operation(summary = "Update primary motorcycle status")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Primary status updated"),
    @ApiResponse(responseCode = "400", description = "Invalid primary status"),
    @ApiResponse(responseCode = "404", description = "Motorcycle not found")
  })
  public MotocycleEntity updatePrimaryMotorcycle(
      @PathVariable Long id, @Valid @RequestBody PrimaryMotorcycleUpdateRequest request) {
    return motorcycleService.updatePrimaryMotorcycle(id, request.primaryMotorcycle());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete a motorcycle")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Motorcycle deleted"),
    @ApiResponse(responseCode = "404", description = "Motorcycle not found")
  })
  public void deleteMotorcycle(@PathVariable Long id) {
    motorcycleService.deleteMotorcycle(id);
  }
}
