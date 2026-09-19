package com.rider.companion.controller;

import com.rider.companion.entity.MaintenanceRecordEntity;
import com.rider.companion.entity.MotocycleEntity;
import com.rider.companion.entity.RideChecklistItemEntity;
import com.rider.companion.entity.RideEntity;
import com.rider.companion.entity.RiderEntity;
import com.rider.companion.entity.UserEntity;
import com.rider.companion.config.JwtService;
import com.rider.companion.repository.MaintenanceRecordRepository;
import com.rider.companion.repository.MotorcycleRepository;
import com.rider.companion.repository.RideChecklistItemRepository;
import com.rider.companion.repository.RideRepository;
import com.rider.companion.repository.RiderRepository;
import com.rider.companion.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RidePatchAndDashboardControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private RiderRepository riderRepository;
  @Autowired private MotorcycleRepository motorcycleRepository;
  @Autowired private MaintenanceRecordRepository maintenanceRecordRepository;
  @Autowired private RideRepository rideRepository;
  @Autowired private RideChecklistItemRepository checklistItemRepository;
  @Autowired private JwtService jwtService;

  @BeforeEach
  void clearDatabase() {
    checklistItemRepository.deleteAll();
    rideRepository.deleteAll();
    maintenanceRecordRepository.deleteAll();
    motorcycleRepository.deleteAll();
    riderRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void updatesRideStatusAndChecklistAndReturnsUserDashboard() throws Exception {
    UserEntity user = new UserEntity();
    user.setFirstName("Aymen");
    user.setLastName("Rider");
    user.setEmail("aymen@example.com");
    user.setPasswordHash("hashed-password");
    user = userRepository.save(user);

    RiderEntity profile = new RiderEntity();
    profile.setUserId(user.getId());
    profile.setExperienceLevel("BEGINNER");
    riderRepository.save(profile);

    MotocycleEntity motorcycle = new MotocycleEntity();
    motorcycle.setUser(user);
    motorcycle.setBrand("Triumph");
    motorcycle.setModel("Tiger 900 GT Pro");
    motorcycle.setYear(2025);
    motorcycle.setCurrentMileage(3500);
    motorcycle.setPrimaryMotorcycle(true);
    motorcycle = motorcycleRepository.save(motorcycle);

    MaintenanceRecordEntity maintenance = new MaintenanceRecordEntity();
    maintenance.setMotorcycle(motorcycle);
    maintenance.setMaintenanceType("GENERAL_SERVICE");
    maintenance.setStatus("COMPLETED");
    maintenance.setCompletionDate(LocalDate.of(2026, 2, 1));
    maintenance.setMileage(1000);
    maintenance.setCost(new BigDecimal("320.50"));
    maintenanceRecordRepository.save(maintenance);

    RideEntity ride = new RideEntity();
    ride.setMotorcycle(motorcycle);
    ride.setTitle("Weekend Trip");
    ride.setEstimatedDistance(420);
    ride = rideRepository.save(ride);

    RideChecklistItemEntity checklistItem = new RideChecklistItemEntity();
    checklistItem.setRide(ride);
    checklistItem.setLabel("Check tire pressure");
    checklistItem.setChecked(false);
    checklistItem = checklistItemRepository.save(checklistItem);

    String authorization = "Bearer " + jwtService.issue(user.getId(), user.getEmail()).token();
    mockMvc.perform(patch("/api/me/rides/{id}/status", ride.getId()).header("Authorization", authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"status\":\"PLANNED\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("PLANNED"));

    mockMvc.perform(patch("/api/me/rides/{rideId}/checklist/{itemId}", ride.getId(), checklistItem.getId()).header("Authorization", authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"checked\":true}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.checked").value(true));

    mockMvc.perform(get("/api/me/dashboard").header("Authorization", authorization))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.user.firstName").value("Aymen"))
        .andExpect(jsonPath("$.user.experienceLevel").value("BEGINNER"))
        .andExpect(jsonPath("$.motorcyclesCount").value(1))
        .andExpect(jsonPath("$.primaryMotorcycle.model").value("Tiger 900 GT Pro"))
        .andExpect(jsonPath("$.maintenance.lastMaintenance.type").value("GENERAL_SERVICE"))
        .andExpect(jsonPath("$.statistics.maintenanceTotalCost").value(320.50))
        .andExpect(jsonPath("$.statistics.plannedRidesCount").value(1))
        .andExpect(jsonPath("$.statistics.estimatedRideDistance").value(420));
  }
}
