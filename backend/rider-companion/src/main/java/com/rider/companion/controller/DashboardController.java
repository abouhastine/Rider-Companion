package com.rider.companion.controller;

import com.rider.companion.dto.DashboardResponse;
import com.rider.companion.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Aggregated rider overview")
public class DashboardController {

  private final DashboardService dashboardService;

  public DashboardController(DashboardService dashboardService) {
    this.dashboardService = dashboardService;
  }

  @GetMapping
  @Operation(summary = "Get dashboard", description = "Returns an aggregated dashboard view.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Dashboard returned"),
    @ApiResponse(responseCode = "404", description = "User not found")
  })
  public DashboardResponse getDashboard(@RequestParam(required = false) Long userId) {
    return dashboardService.getDashboard(userId);
  }
}
