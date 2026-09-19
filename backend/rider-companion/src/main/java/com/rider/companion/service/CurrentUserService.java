package com.rider.companion.service;

import com.rider.companion.dto.MotorcycleRequest;
import com.rider.companion.dto.MotorcycleResponse;
import com.rider.companion.dto.ProfileResponse;
import com.rider.companion.dto.ProfileUpdateRequest;
import com.rider.companion.entity.MotorcycleImageEntity;
import com.rider.companion.entity.MotocycleEntity;
import com.rider.companion.entity.RiderEntity;
import com.rider.companion.entity.UserEntity;
import com.rider.companion.exception.MotorcycleNotFoundException;
import com.rider.companion.exception.UserNotFoundException;
import com.rider.companion.repository.MotorcycleImageRepository;
import com.rider.companion.repository.MotorcycleRepository;
import com.rider.companion.repository.RiderRepository;
import com.rider.companion.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CurrentUserService {

  private final UserRepository users;
  private final RiderRepository riders;
  private final MotorcycleRepository motorcycles;
  private final MotorcycleImageRepository images;
  private final MotorcycleService motorcycleService;

  public CurrentUserService(
      UserRepository users,
      RiderRepository riders,
      MotorcycleRepository motorcycles,
      MotorcycleImageRepository images,
      MotorcycleService motorcycleService) {
    this.users = users;
    this.riders = riders;
    this.motorcycles = motorcycles;
    this.images = images;
    this.motorcycleService = motorcycleService;
  }

  public ProfileResponse profile(Long userId) {
    UserEntity user = user(userId);
    RiderEntity rider = riders.findByUserId(userId).orElse(null);
    return new ProfileResponse(
        userId,
        rider == null ? null : rider.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail(),
        rider == null ? null : rider.getLicenseType(),
        rider == null ? null : rider.getLicenseYear(),
        rider == null ? null : rider.getExperienceLevel(),
        rider == null ? null : rider.getPrimaryUsage(),
        rider == null ? null : rider.getEstimatedAnnualDistance());
  }

  @Transactional
  public ProfileResponse updateProfile(Long userId, ProfileUpdateRequest request) {
    UserEntity user = user(userId);
    user.setFirstName(request.firstName());
    user.setLastName(request.lastName());
    RiderEntity rider =
        riders
            .findByUserId(userId)
            .orElseGet(
                () -> {
                  RiderEntity value = new RiderEntity();
                  value.setUserId(userId);
                  return value;
                });
    rider.setLicenseType(request.licenseType());
    rider.setLicenseYear(request.licenseYear());
    rider.setExperienceLevel(request.experienceLevel());
    rider.setPrimaryUsage(request.primaryUsage());
    rider.setEstimatedAnnualDistance(request.estimatedAnnualDistance());
    riders.save(rider);
    users.save(user);
    return profile(userId);
  }

  public List<MotorcycleResponse> motorcycles(Long userId) {
    return motorcycles.findByUserId(userId).stream().map(this::response).toList();
  }

  public MotorcycleResponse motorcycle(Long userId, Long id) {
    return response(owned(userId, id));
  }

  public MotorcycleResponse createMotorcycle(Long userId, MotorcycleRequest request) {
    return response(motorcycleService.createMotorcycle(withOwner(userId, request)));
  }

  public MotorcycleResponse updateMotorcycle(Long userId, Long id, MotorcycleRequest request) {
    owned(userId, id);
    return response(motorcycleService.updateMotorcycle(id, withOwner(userId, request)));
  }

  public MotorcycleResponse updatePrimary(Long userId, Long id, boolean primary) {
    owned(userId, id);
    return response(motorcycleService.updatePrimaryMotorcycle(id, primary));
  }

  public MotorcycleResponse updateMileage(Long userId, Long id, int mileage) {
    owned(userId, id);
    return response(motorcycleService.updateMileage(id, mileage));
  }

  public void deleteMotorcycle(Long userId, Long id) {
    owned(userId, id);
    motorcycleService.deleteMotorcycle(id);
  }

  public MotorcycleImageEntity image(Long userId, Long id) {
    owned(userId, id);
    return images
        .findByMotorcycleId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
  }

  @Transactional
  public void saveImage(Long userId, Long id, byte[] content, String contentType) {
    MotocycleEntity motorcycle = owned(userId, id);
    MotorcycleImageEntity image =
        images.findByMotorcycleId(id).orElseGet(MotorcycleImageEntity::new);
    image.setMotorcycle(motorcycle);
    image.setContent(content);
    image.setContentType(contentType);
    image.setByteSize((long) content.length);
    image.setUpdatedAt(Instant.now());
    images.save(image);
  }

  @Transactional
  public void deleteImage(Long userId, Long id) {
    owned(userId, id);
    images.deleteByMotorcycleId(id);
  }

  private MotocycleEntity owned(Long userId, Long id) {
    MotocycleEntity motorcycle =
        motorcycles.findById(id).orElseThrow(() -> new MotorcycleNotFoundException(id));
    if (!motorcycle.getUser().getId().equals(userId))
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN, "Motorcycle does not belong to the current user");
    return motorcycle;
  }

  private UserEntity user(Long id) {
    return users.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  private MotorcycleRequest withOwner(Long userId, MotorcycleRequest request) {
    return new MotorcycleRequest(
        userId,
        request.brand(),
        request.model(),
        request.year(),
        request.engineCapacity(),
        request.power(),
        request.fuelType(),
        request.registrationNumber(),
        request.purchaseDate(),
        request.currentMileage(),
        request.averageConsumption(),
        null,
        request.primaryMotorcycle());
  }

  private MotorcycleResponse response(MotocycleEntity motorcycle) {
    MotorcycleImageEntity image = images.findByMotorcycleId(motorcycle.getId()).orElse(null);
    return new MotorcycleResponse(
        motorcycle.getId(),
        motorcycle.getBrand(),
        motorcycle.getModel(),
        motorcycle.getYear(),
        motorcycle.getEngineCapacity(),
        motorcycle.getPower(),
        motorcycle.getFuelType(),
        motorcycle.getRegistrationNumber(),
        motorcycle.getPurchaseDate(),
        motorcycle.getCurrentMileage(),
        motorcycle.getAverageConsumption(),
        motorcycle.getPrimaryMotorcycle(),
        image != null,
        image == null ? null : image.getUpdatedAt());
  }
}
