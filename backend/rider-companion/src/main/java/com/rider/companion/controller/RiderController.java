package com.rider.companion.controller;

import com.rider.companion.entity.RiderEntity;
import com.rider.companion.service.RiderService;
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
@RequestMapping("/api/riders")
@Tag(name = "Riders", description = "Operations for adding riders")
public class RiderController {
  private final RiderService riderService;

  public RiderController(RiderService riderService) {
    this.riderService = riderService;
  }

  @GetMapping
  @Operation(summary = "List riders", description = "Returns every rider .")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Riders returned")})
  public List<RiderEntity> getAllRiders() {
    return riderService.getAllRiders();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a rider", description = "Returns one rider by its database identifier.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Rider returned"),
    @ApiResponse(responseCode = "404", description = "Rider not found")
  })
  public RiderEntity getRidersById(
      @Parameter(description = "Motorcycle identifier", example = "1") @PathVariable Long id) {
    return riderService.getRidersById(id);
  }

  @GetMapping("/user/{userId}")
  @Operation(summary = "Get rider profile by user ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Rider profile returned"),
    @ApiResponse(responseCode = "404", description = "Rider profile not found")
  })
  public RiderEntity getRiderProfileByUserId(@PathVariable Long userId) {
    return riderService.getRiderProfileByUserId(userId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a rider")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Rider created"),
    @ApiResponse(responseCode = "400", description = "Invalid request body")
  })
  public RiderEntity createRider(@Valid @RequestBody RiderEntity rider) {
    return riderService.createRider(rider);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update a rider",
      description = "Replaces the editable fields of an existing rider.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Rider updated"),
    @ApiResponse(responseCode = "400", description = "Invalid request body"),
    @ApiResponse(responseCode = "404", description = "Rider not found")
  })
  public RiderEntity updateRider(@PathVariable Long id, @Valid @RequestBody RiderEntity rider) {
    return riderService.updateRider(id, rider);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete a rider")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Rider deleted"),
    @ApiResponse(responseCode = "404", description = "Rider not found")
  })
  public void deleteRider(@PathVariable Long id) {
    riderService.deleteRider(id);
  }
}
