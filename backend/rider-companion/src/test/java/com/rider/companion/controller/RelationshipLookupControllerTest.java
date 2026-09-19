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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RelationshipLookupControllerTest {

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
  void returnsResourcesFilteredByTheirParentIds() throws Exception {
    UserEntity user = new UserEntity();
    user.setFirstName("Oussama");
    user.setLastName("Bouhastine");
    user.setEmail("oussama@example.com");
    user.setPasswordHash("hashed-password");
    user = userRepository.save(user);

    RiderEntity profile = new RiderEntity();
    profile.setUserId(user.getId());
    profile.setLicenseType("A");
    riderRepository.save(profile);

    MotocycleEntity motorcycle = new MotocycleEntity();
    motorcycle.setUser(user);
    motorcycle.setBrand("Yamaha");
    motorcycle.setModel("MT-07");
    motorcycle = motorcycleRepository.save(motorcycle);

    MaintenanceRecordEntity maintenance = new MaintenanceRecordEntity();
    maintenance.setMotorcycle(motorcycle);
    maintenance.setMaintenanceType("Oil Change");
    maintenanceRecordRepository.save(maintenance);

    RideEntity ride = new RideEntity();
    ride.setMotorcycle(motorcycle);
    ride.setTitle("Weekend Trip");
    ride = rideRepository.save(ride);

    RideChecklistItemEntity checklistItem = new RideChecklistItemEntity();
    checklistItem.setRide(ride);
    checklistItem.setLabel("Check tire pressure");
    checklistItem.setChecked(false);
    checklistItemRepository.save(checklistItem);

    mockMvc.perform(get("/api/riders/user/{userId}", user.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.licenseType").value("A"));

    mockMvc.perform(get("/api/motorcycles/user/{userId}", user.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].brand").value("Yamaha"));

    String authorization = "Bearer " + jwtService.issue(user.getId(), user.getEmail()).token();
    mockMvc.perform(get("/api/me/maintenance-records").header("Authorization", authorization))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].maintenanceType").value("Oil Change"));

    mockMvc.perform(get("/api/me/rides").header("Authorization", authorization))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Weekend Trip"))
        .andExpect(jsonPath("$[0].checklistItems[0].label").value("Check tire pressure"))
        .andExpect(jsonPath("$[0].checklistItems[0].checked").value(false));
  }
}
