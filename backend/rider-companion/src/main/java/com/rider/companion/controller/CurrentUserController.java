package com.rider.companion.controller;

import com.rider.companion.dto.MileageUpdateRequest;
import com.rider.companion.dto.MotorcycleRequest;
import com.rider.companion.dto.MotorcycleResponse;
import com.rider.companion.dto.PrimaryMotorcycleUpdateRequest;
import com.rider.companion.dto.ProfileResponse;
import com.rider.companion.dto.ProfileUpdateRequest;
import com.rider.companion.entity.MotorcycleImageEntity;
import com.rider.companion.service.CurrentUserService;
import com.rider.companion.service.DashboardService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/me")
public class CurrentUserController {

  private static final long MAX_IMAGE_BYTES = 5 * 1024 * 1024;
  private static final Set<String> IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
  private final CurrentUserService service;
  private final DashboardService dashboardService;

  public CurrentUserController(CurrentUserService service, DashboardService dashboardService) {
    this.service = service;
    this.dashboardService = dashboardService;
  }

  @GetMapping("/profile")
  public ProfileResponse profile(@AuthenticationPrincipal Long userId) {
    return service.profile(userId);
  }

  @PutMapping("/profile")
  public ProfileResponse updateProfile(
      @AuthenticationPrincipal Long userId, @Valid @RequestBody ProfileUpdateRequest request) {
    return service.updateProfile(userId, request);
  }

  @GetMapping("/dashboard")
  public Object dashboard(@AuthenticationPrincipal Long userId) {
    return dashboardService.getDashboard(userId);
  }

  @GetMapping("/motorcycles")
  public List<MotorcycleResponse> motorcycles(@AuthenticationPrincipal Long userId) {
    return service.motorcycles(userId);
  }

  @PostMapping("/motorcycles")
  public ResponseEntity<MotorcycleResponse> create(
      @AuthenticationPrincipal Long userId, @Valid @RequestBody MotorcycleRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.createMotorcycle(userId, request));
  }

  @GetMapping("/motorcycles/{id}")
  public MotorcycleResponse motorcycle(
      @AuthenticationPrincipal Long userId, @PathVariable Long id) {
    return service.motorcycle(userId, id);
  }

  @PutMapping("/motorcycles/{id}")
  public MotorcycleResponse update(
      @AuthenticationPrincipal Long userId,
      @PathVariable Long id,
      @Valid @RequestBody MotorcycleRequest request) {
    return service.updateMotorcycle(userId, id, request);
  }

  @PatchMapping("/motorcycles/{id}/primary")
  public MotorcycleResponse primary(
      @AuthenticationPrincipal Long userId,
      @PathVariable Long id,
      @Valid @RequestBody PrimaryMotorcycleUpdateRequest request) {
    return service.updatePrimary(userId, id, request.primaryMotorcycle());
  }

  @PatchMapping("/motorcycles/{id}/mileage")
  public MotorcycleResponse mileage(
      @AuthenticationPrincipal Long userId,
      @PathVariable Long id,
      @Valid @RequestBody MileageUpdateRequest request) {
    return service.updateMileage(userId, id, request.currentMileage());
  }

  @DeleteMapping("/motorcycles/{id}")
  public ResponseEntity<Void> delete(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
    service.deleteMotorcycle(userId, id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/motorcycles/{id}/image")
  public ResponseEntity<byte[]> image(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
    MotorcycleImageEntity image = service.image(userId, id);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(image.getContentType()))
        .header(HttpHeaders.CACHE_CONTROL, "private, max-age=3600")
        .body(image.getContent());
  }

  @PutMapping(value = "/motorcycles/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> uploadImage(
      @AuthenticationPrincipal Long userId,
      @PathVariable Long id,
      @RequestPart("file") MultipartFile file)
      throws IOException {
    validateImage(file);
    service.saveImage(userId, id, file.getBytes(), file.getContentType());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/motorcycles/{id}/image")
  public ResponseEntity<Void> deleteImage(
      @AuthenticationPrincipal Long userId, @PathVariable Long id) {
    service.deleteImage(userId, id);
    return ResponseEntity.noContent().build();
  }

  private void validateImage(MultipartFile file) {
    if (file.isEmpty()
        || file.getSize() > MAX_IMAGE_BYTES
        || !IMAGE_TYPES.contains(file.getContentType()))
      throw new org.springframework.web.server.ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Upload a JPEG, PNG, or WebP image smaller than 5 MB");
  }
}
