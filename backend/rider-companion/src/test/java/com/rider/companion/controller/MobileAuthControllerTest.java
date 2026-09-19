package com.rider.companion.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.rider.companion.repository.MaintenanceRecordRepository;
import com.rider.companion.repository.MobileSessionRepository;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MobileAuthControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private MobileSessionRepository sessions;
  @Autowired private RideChecklistItemRepository checklistItems;
  @Autowired private RideRepository rides;
  @Autowired private MaintenanceRecordRepository maintenance;
  @Autowired private MotorcycleRepository motorcycles;
  @Autowired private RiderRepository riders;
  @Autowired private UserRepository users;

  @BeforeEach
  void clearDatabase() {
    sessions.deleteAll(); checklistItems.deleteAll(); rides.deleteAll(); maintenance.deleteAll();
    motorcycles.deleteAll(); riders.deleteAll(); users.deleteAll();
  }

  @Test
  void registersRotatesRefreshTokensAndRejectsReplay() throws Exception {
    String registration = """
      {"firstName":"Mobile","lastName":"Rider","credentials":{"email":"mobile@example.com","password":"safe-password","deviceName":"Test phone","platform":"android","appVersion":"0.1"}}
      """;
    String first = mockMvc.perform(post("/api/mobile/auth/register").contentType(MediaType.APPLICATION_JSON).content(registration))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.accessToken").exists())
        .andExpect(jsonPath("$.refreshToken").exists()).andExpect(jsonPath("$.accessTokenExpiresAt").exists())
        .andReturn().getResponse().getContentAsString();
    JsonNode response = JsonMapper.builder().build().readTree(first);
    String originalRefresh = response.get("refreshToken").asText();
    String refreshed = mockMvc.perform(post("/api/mobile/auth/refresh").contentType(MediaType.APPLICATION_JSON)
            .content("{\"refreshToken\":\"" + originalRefresh + "\"}"))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    String nextRefresh = JsonMapper.builder().build().readTree(refreshed).get("refreshToken").asText();
    org.junit.jupiter.api.Assertions.assertNotEquals(originalRefresh, nextRefresh);
    mockMvc.perform(post("/api/mobile/auth/refresh").contentType(MediaType.APPLICATION_JSON)
            .content("{\"refreshToken\":\"" + originalRefresh + "\"}"))
        .andExpect(status().isUnauthorized());
    mockMvc.perform(post("/api/mobile/auth/refresh").contentType(MediaType.APPLICATION_JSON)
            .content("{\"refreshToken\":\"" + nextRefresh + "\"}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void logsOutMobileSessionAndRejectsInvalidCredentials() throws Exception {
    String credentials = "{\"email\":\"bad@example.com\",\"password\":\"safe-password\",\"platform\":\"ios\"}";
    mockMvc.perform(post("/api/mobile/auth/login").contentType(MediaType.APPLICATION_JSON).content(credentials))
        .andExpect(status().isUnauthorized());
    String registered = mockMvc.perform(post("/api/mobile/auth/register").contentType(MediaType.APPLICATION_JSON)
            .content("{\"firstName\":\"Logout\",\"lastName\":\"Rider\",\"credentials\":{\"email\":\"logout@example.com\",\"password\":\"safe-password\"}}"))
        .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
    String refreshToken = JsonMapper.builder().build().readTree(registered).get("refreshToken").asText();
    mockMvc.perform(post("/api/mobile/auth/logout").contentType(MediaType.APPLICATION_JSON)
            .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
        .andExpect(status().isNoContent());
    mockMvc.perform(post("/api/mobile/auth/refresh").contentType(MediaType.APPLICATION_JSON)
            .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
        .andExpect(status().isUnauthorized());
  }
}
