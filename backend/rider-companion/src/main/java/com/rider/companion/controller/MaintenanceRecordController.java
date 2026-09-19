package com.rider.companion.controller;

import com.rider.companion.dto.MaintenanceRecordRequest;
import com.rider.companion.entity.MaintenanceRecordEntity;
import com.rider.companion.service.MaintenanceRecordService;
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
@RequestMapping("/api/maintenance-records")
@Tag(name = "Maintenance Records", description = "Operations for motorcycle maintenance records")
public class MaintenanceRecordController {

  private final MaintenanceRecordService maintenanceRecordService;

  public MaintenanceRecordController(MaintenanceRecordService maintenanceRecordService) {

    this.maintenanceRecordService = maintenanceRecordService;
  }

  @GetMapping
  @Operation(summary = "List maintenance records", description = "Returns all maintenance records.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Maintenance records returned")})
  public List<MaintenanceRecordEntity> getAllMaintenanceRecords() {
    return maintenanceRecordService.getAllMaintenanceRecords();
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get a maintenance record",
      description = "Returns one maintenance record by its identifier.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Maintenance record returned"),
    @ApiResponse(responseCode = "404", description = "Maintenance record not found")
  })
  public MaintenanceRecordEntity getMaintenanceRecordById(
      @Parameter(description = "Maintenance record identifier", example = "1") @PathVariable
          Long id) {
    return maintenanceRecordService.getMaintenanceRecordById(id);
  }

  @GetMapping("/motorcycle/{motorcycleId}")
  @Operation(summary = "List maintenance records by motorcycle ID")
  public List<MaintenanceRecordEntity> getMaintenanceRecordsByMotorcycleId(
      @PathVariable Long motorcycleId) {
    return maintenanceRecordService.getMaintenanceRecordsByMotorcycleId(motorcycleId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a maintenance record")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Maintenance record created"),
    @ApiResponse(responseCode = "400", description = "Invalid request body")
  })
  public MaintenanceRecordEntity createMaintenanceRecord(
      @Valid @RequestBody MaintenanceRecordRequest maintenanceRecord) {

    return maintenanceRecordService.createMaintenanceRecord(maintenanceRecord);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update a maintenance record",
      description = "Updates an existing maintenance record.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Maintenance record updated"),
    @ApiResponse(responseCode = "400", description = "Invalid request body"),
    @ApiResponse(responseCode = "404", description = "Maintenance record not found")
  })
  public MaintenanceRecordEntity updateMaintenanceRecord(
      @PathVariable Long id, @Valid @RequestBody MaintenanceRecordRequest maintenanceRecord) {

    return maintenanceRecordService.updateMaintenanceRecord(id, maintenanceRecord);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete a maintenance record")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Maintenance record deleted"),
    @ApiResponse(responseCode = "404", description = "Maintenance record not found")
  })
  public void deleteMaintenanceRecord(@PathVariable Long id) {

    maintenanceRecordService.deleteMaintenanceRecord(id);
  }
}
