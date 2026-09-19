package com.rider.companion.controller;

import com.rider.companion.entity.UserEntity;
import com.rider.companion.repository.MotorcycleRepository;
import com.rider.companion.repository.RideRepository;
import com.rider.companion.repository.RideChecklistItemRepository;
import com.rider.companion.repository.MaintenanceRecordRepository;
import com.rider.companion.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RideControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private RideRepository rideRepository;

  @Autowired
  private RideChecklistItemRepository checklistItemRepository;

  @Autowired
  private MaintenanceRecordRepository maintenanceRecordRepository;

  @Autowired
  private MotorcycleRepository motorcycleRepository;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void clearDatabase() {
    checklistItemRepository.deleteAll();
    rideRepository.deleteAll();
    maintenanceRecordRepository.deleteAll();
    motorcycleRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void createsAndListsRidesWithoutCircularJsonReferences() throws Exception {
    UserEntity user = new UserEntity();
    user.setFirstName("Oussama");
    user.setLastName("Bouhastine");
    user.setEmail("oussama@example.com");
    user.setPasswordHash("hashed-password");
    user = userRepository.save(user);

    String motorcycle = """
        {"user":%d,"brand":"Yamaha","model":"MT-07","year":2024}
        """.formatted(user.getId());

    String motorcycleResponse = mockMvc.perform(post("/api/motorcycles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(motorcycle))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();
    long motorcycleId = com.fasterxml.jackson.databind.json.JsonMapper.builder()
        .build().readTree(motorcycleResponse).get("id").asLong();

    String ride = """
        {"motorcycle":%d,"title":"Weekend Trip","plannedDate":"2026-08-01"}
        """.formatted(motorcycleId);

    mockMvc.perform(post("/api/rides")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ride))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("Weekend Trip"))
        .andExpect(jsonPath("$.motorcycle.rides").doesNotExist());

    mockMvc.perform(get("/api/rides"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Weekend Trip"))
        .andExpect(jsonPath("$[0].motorcycle.rides").doesNotExist());
  }
}
