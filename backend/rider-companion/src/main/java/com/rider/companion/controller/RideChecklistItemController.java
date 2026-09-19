package com.rider.companion.controller;

import com.rider.companion.dto.RideChecklistItemRequest;
import com.rider.companion.entity.RideChecklistItemEntity;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ride-checklist-items")
@Tag(name = "Ride Checklist Items", description = "Operations for ride checklist items")
public class RideChecklistItemController {
  private final RideChecklistItemService rideChecklistItemService;

  public RideChecklistItemController(RideChecklistItemService rideChecklistItemService) {

    this.rideChecklistItemService = rideChecklistItemService;
  }

  @GetMapping
  @Operation(
      summary = "List ride checklist items",
      description = "Returns every ride checklist item.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Ride checklist items returned")})
  public List<RideChecklistItemEntity> getAllRideChecklistItems() {
    return rideChecklistItemService.getAllRideChecklistItems();
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get a ride checklist item",
      description = "Returns one ride checklist item by its database identifier.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Ride checklist item returned"),
    @ApiResponse(responseCode = "404", description = "Ride checklist item not found")
  })
  public RideChecklistItemEntity getRideChecklistItemById(
      @Parameter(description = "Ride checklist item identifier", example = "1") @PathVariable
          Long id) {

    return rideChecklistItemService.getRideChecklistItemById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a ride checklist item")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Ride checklist item created"),
    @ApiResponse(responseCode = "400", description = "Invalid request body")
  })
  public RideChecklistItemEntity createRideChecklistItem(
      @Valid @RequestBody RideChecklistItemRequest rideChecklistItem) {

    return rideChecklistItemService.createRideChecklistItem(rideChecklistItem);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update a ride checklist item",
      description = "Replaces the editable fields of an existing ride checklist item.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Ride checklist item updated"),
    @ApiResponse(responseCode = "400", description = "Invalid request body"),
    @ApiResponse(responseCode = "404", description = "Ride checklist item not found")
  })
  public RideChecklistItemEntity updateRideChecklistItem(
      @PathVariable Long id, @Valid @RequestBody RideChecklistItemRequest rideChecklistItem) {

    return rideChecklistItemService.updateRideChecklistItem(id, rideChecklistItem);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete a ride checklist item")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Ride checklist item deleted"),
    @ApiResponse(responseCode = "404", description = "Ride checklist item not found")
  })
  public void deleteRideChecklistItem(@PathVariable Long id) {
    rideChecklistItemService.deleteRideChecklistItem(id);
  }
}
