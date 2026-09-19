package com.rider.companion.controller;

import com.rider.companion.dto.RideRequest;
import com.rider.companion.dto.RideStatusUpdateRequest;
import com.rider.companion.dto.ChecklistItemCheckedUpdateRequest;
import com.rider.companion.entity.RideEntity;
import com.rider.companion.service.RideService;
import com.rider.companion.service.RideChecklistItemService;
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
@RequestMapping("/api/rides")
@Tag(name = "Rides", description = "Operations for managing rides")
public class RideController {

  private final RideService rideService;
  private final RideChecklistItemService rideChecklistItemService;

  public RideController(
      RideService rideService, RideChecklistItemService rideChecklistItemService) {
    this.rideService = rideService;
    this.rideChecklistItemService = rideChecklistItemService;
  }

  @GetMapping
  @Operation(summary = "List rides", description = "Returns every ride.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Rides returned")})
  public List<RideEntity> getAllRides() {
    return rideService.getAllRides();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a ride", description = "Returns one ride by its database identifier.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Ride returned"),
    @ApiResponse(responseCode = "404", description = "Ride not found")
  })
  public RideEntity getRideById(
      @Parameter(description = "Ride identifier", example = "1") @PathVariable Long id) {

    return rideService.getRideById(id);
  }

  @GetMapping("/motorcycle/{motorcycleId}")
  @Operation(summary = "List rides by motorcycle ID with checklist items")
  public List<RideEntity> getRidesByMotorcycleId(@PathVariable Long motorcycleId) {
    return rideService.getRidesByMotorcycleId(motorcycleId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a ride")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Ride created"),
    @ApiResponse(responseCode = "400", description = "Invalid request body")
  })
  public RideEntity createRide(@Valid @RequestBody RideRequest ride) {

    return rideService.createRide(ride);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update a ride",
      description = "Replaces the editable fields of an existing ride.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Ride updated"),
    @ApiResponse(responseCode = "400", description = "Invalid request body"),
    @ApiResponse(responseCode = "404", description = "Ride not found")
  })
  public RideEntity updateRide(@PathVariable Long id, @Valid @RequestBody RideRequest ride) {

    return rideService.updateRide(id, ride);
  }

  @PatchMapping("/{id}/status")
  @Operation(summary = "Update ride status")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Ride status updated"),
    @ApiResponse(responseCode = "400", description = "Invalid ride status"),
    @ApiResponse(responseCode = "404", description = "Ride not found")
  })
  public RideEntity updateRideStatus(
      @PathVariable Long id, @Valid @RequestBody RideStatusUpdateRequest request) {
    return rideService.updateStatus(id, request.status());
  }

  @PatchMapping("/{rideId}/checklist/{itemId}")
  @Operation(summary = "Check or uncheck a ride checklist item")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Checklist item updated"),
    @ApiResponse(responseCode = "400", description = "Invalid checked value"),
    @ApiResponse(responseCode = "404", description = "Ride or checklist item not found")
  })
  public com.rider.companion.entity.RideChecklistItemEntity updateChecklistItem(
      @PathVariable Long rideId,
      @PathVariable Long itemId,
      @Valid @RequestBody ChecklistItemCheckedUpdateRequest request) {
    return rideChecklistItemService.updateChecked(rideId, itemId, request.checked());
  }


  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete a ride")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Ride deleted"),
    @ApiResponse(responseCode = "404", description = "Ride not found")
  })
  public void deleteRide(@PathVariable Long id) {
    rideService.deleteRide(id);
  }
}
