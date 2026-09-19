package com.rider.companion.controller;

import com.rider.companion.entity.MotocycleEntity;
import com.rider.companion.entity.UserEntity;
import com.rider.companion.repository.MaintenanceRecordRepository;
import com.rider.companion.repository.MotorcycleRepository;
import com.rider.companion.repository.RideChecklistItemRepository;
import com.rider.companion.repository.RideRepository;
import com.rider.companion.repository.UserRepository;
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
class MotorcyclePatchControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private MotorcycleRepository motorcycleRepository;
  @Autowired private MaintenanceRecordRepository maintenanceRecordRepository;
  @Autowired private RideRepository rideRepository;
  @Autowired private RideChecklistItemRepository checklistItemRepository;

  @BeforeEach
  void clearDatabase() {
    checklistItemRepository.deleteAll();
    rideRepository.deleteAll();
    maintenanceRecordRepository.deleteAll();
    motorcycleRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void updatesMileageAndKeepsOnlyOnePrimaryMotorcyclePerUser() throws Exception {
    UserEntity user = new UserEntity();
    user.setFirstName("Oussama");
    user.setLastName("Bouhastine");
    user.setEmail("oussama@example.com");
    user.setPasswordHash("hashed-password");
    user = userRepository.save(user);

    MotocycleEntity first = motorcycle(user, "MT-07", false);
    MotocycleEntity second = motorcycle(user, "Tracer 9", true);
    first = motorcycleRepository.save(first);
    second = motorcycleRepository.save(second);

    mockMvc.perform(patch("/api/motorcycles/{id}/mileage", first.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"currentMileage\":15000}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currentMileage").value(15000));

    mockMvc.perform(patch("/api/motorcycles/{id}/primary", first.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"primaryMotorcycle\":true}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.primaryMotorcycle").value(true));

    mockMvc.perform(get("/api/motorcycles/{id}", second.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.primaryMotorcycle").value(false));
  }

  private MotocycleEntity motorcycle(UserEntity user, String model, boolean primary) {
    MotocycleEntity motorcycle = new MotocycleEntity();
    motorcycle.setUser(user);
    motorcycle.setBrand("Yamaha");
    motorcycle.setModel(model);
    motorcycle.setPrimaryMotorcycle(primary);
    return motorcycle;
  }
}
