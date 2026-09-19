package com.rider.companion.service;

import com.rider.companion.dto.MotorcycleRequest;
import com.rider.companion.dto.MotorcycleResponse;
import com.rider.companion.dto.ProfileResponse;
import com.rider.companion.dto.ProfileUpdateRequest;
import com.rider.companion.dto.MaintenanceRecordRequest;
import com.rider.companion.dto.MaintenanceRecordResponse;
import com.rider.companion.dto.RideRequest;
import com.rider.companion.dto.RideResponse;
import com.rider.companion.entity.MaintenanceRecordEntity;
import com.rider.companion.entity.RideChecklistItemEntity;
import com.rider.companion.entity.RideEntity;
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
import com.rider.companion.repository.MaintenanceRecordRepository;
import com.rider.companion.repository.RideChecklistItemRepository;
import com.rider.companion.repository.RideRepository;
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
  private final MaintenanceRecordRepository maintenanceRecords;
  private final RideRepository rides;
  private final RideChecklistItemRepository checklistItems;

  public CurrentUserService(
      UserRepository users,
      RiderRepository riders,
      MotorcycleRepository motorcycles,
      MotorcycleImageRepository images,
      MotorcycleService motorcycleService,
      MaintenanceRecordRepository maintenanceRecords,
      RideRepository rides,
      RideChecklistItemRepository checklistItems) {
    this.users = users;
    this.riders = riders;
    this.motorcycles = motorcycles;
    this.images = images;
    this.motorcycleService = motorcycleService;
    this.maintenanceRecords = maintenanceRecords;
    this.rides = rides;
    this.checklistItems = checklistItems;
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

  public List<MaintenanceRecordResponse> maintenanceRecords(Long userId) {
    return maintenanceRecords.findByMotorcycleUserId(userId).stream().map(this::maintenanceResponse).toList();
  }

  public MaintenanceRecordResponse maintenanceRecord(Long userId, Long id) {
    return maintenanceResponse(ownedMaintenance(userId, id));
  }

  @Transactional
  public MaintenanceRecordResponse createMaintenanceRecord(Long userId, MaintenanceRecordRequest request) {
    MaintenanceRecordEntity record = new MaintenanceRecordEntity();
    applyMaintenance(record, userId, request, false);
    record.setCreatedAt(java.time.LocalDate.now());
    MaintenanceRecordEntity saved = maintenanceRecords.save(record);
    if ("COMPLETED".equals(request.status()) && (request.plannedDate() != null || request.plannedMileage() != null)) {
      MaintenanceRecordEntity followUp = new MaintenanceRecordEntity();
      followUp.setMotorcycle(saved.getMotorcycle());
      followUp.setMaintenanceType(request.maintenanceType());
      followUp.setStatus("PLANNED");
      followUp.setPlannedDate(request.plannedDate());
      followUp.setPlannedMileage(request.plannedMileage());
      followUp.setServiceProvider(request.serviceProvider());
      followUp.setNotes(request.notes());
      followUp.setCreatedAt(java.time.LocalDate.now());
      maintenanceRecords.save(followUp);
    }
    return maintenanceResponse(saved);
  }

  @Transactional
  public MaintenanceRecordResponse updateMaintenanceRecord(Long userId, Long id, MaintenanceRecordRequest request) {
    MaintenanceRecordEntity record = ownedMaintenance(userId, id);
    applyMaintenance(record, userId, request, true);
    record.setUpdatedAt(java.time.LocalDate.now());
    return maintenanceResponse(maintenanceRecords.save(record));
  }

  public void deleteMaintenanceRecord(Long userId, Long id) {
    maintenanceRecords.delete(ownedMaintenance(userId, id));
  }

  public List<RideResponse> rides(Long userId) {
    return rides.findByMotorcycleUserId(userId).stream().map(this::rideResponse).toList();
  }

  public RideResponse ride(Long userId, Long id) { return rideResponse(ownedRide(userId, id)); }

  @Transactional
  public RideResponse createRide(Long userId, RideRequest request) {
    RideEntity ride = new RideEntity();
    applyRide(ride, userId, request);
    ride.setCreatedAt(java.time.LocalDate.now());
    RideEntity saved = rides.save(ride);
    List<String> labels = List.of("Check tire pressure", "Check fuel level", "Check the chain", "Check lights", "Check the weather", "Take vehicle documents", "Take the phone", "Bring water", "Check riding gear");
    for (String label : labels) {
      RideChecklistItemEntity item = new RideChecklistItemEntity();
      item.setRide(saved); item.setLabel(label); item.setChecked(false); checklistItems.save(item);
    }
    return rideResponse(saved);
  }

  @Transactional
  public RideResponse updateRide(Long userId, Long id, RideRequest request) {
    RideEntity ride = ownedRide(userId, id);
    applyRide(ride, userId, request);
    ride.setUpdatedAt(java.time.LocalDate.now());
    return rideResponse(rides.save(ride));
  }

  public RideResponse updateRideStatus(Long userId, Long id, String status) {
    RideEntity ride = ownedRide(userId, id);
    ride.setStatus(status); ride.setUpdatedAt(java.time.LocalDate.now());
    return rideResponse(rides.save(ride));
  }

  public RideResponse.ChecklistItem updateChecklistItem(Long userId, Long rideId, Long itemId, boolean checked) {
    RideEntity ride = ownedRide(userId, rideId);
    RideChecklistItemEntity item = checklistItems.findById(itemId)
        .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist item not found"));
    if (!item.getRide().getId().equals(ride.getId())) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist item not found");
    item.setChecked(checked); checklistItems.save(item);
    return new RideResponse.ChecklistItem(item.getId(), item.getLabel(), item.getChecked());
  }

  @Transactional
  public void deleteRide(Long userId, Long id) {
    RideEntity ride = ownedRide(userId, id);
    checklistItems.deleteByRideId(ride.getId());
    rides.delete(ride);
  }

  private MaintenanceRecordEntity ownedMaintenance(Long userId, Long id) {
    MaintenanceRecordEntity record = maintenanceRecords.findById(id)
        .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Maintenance record not found"));
    if (!record.getMotorcycle().getUser().getId().equals(userId)) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Maintenance record not found");
    return record;
  }

  private RideEntity ownedRide(Long userId, Long id) {
    RideEntity ride = rides.findById(id)
        .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Ride not found"));
    if (!ride.getMotorcycle().getUser().getId().equals(userId)) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Ride not found");
    return ride;
  }

  private void applyMaintenance(MaintenanceRecordEntity record, Long userId, MaintenanceRecordRequest request, boolean update) {
    record.setMotorcycle(owned(userId, request.motorcycle()));
    record.setMaintenanceType(request.maintenanceType()); record.setStatus(request.status());
    record.setCompletionDate(request.completionDate()); record.setPlannedDate(update ? request.plannedDate() : null);
    record.setMileage(request.mileage()); record.setPlannedMileage(update ? request.plannedMileage() : null);
    record.setCost(request.cost()); record.setServiceProvider(request.serviceProvider()); record.setNotes(request.notes());
  }

  private void applyRide(RideEntity ride, Long userId, RideRequest request) {
    ride.setMotorcycle(owned(userId, request.motorcycle())); ride.setTitle(request.title());
    ride.setPlannedDate(request.plannedDate()); ride.setDepartureTime(request.departureTime());
    ride.setDepartureLocation(request.departureLocation()); ride.setDestination(request.destination());
    ride.setEstimatedDistance(request.estimatedDistance()); ride.setActualDistance(request.actualDistance());
    ride.setEstimatedDuration(request.estimatedDuration()); ride.setActualDuration(request.actualDuration());
    ride.setRideType(request.rideType()); ride.setUseHighway(request.useHighway()); ride.setUseTolls(request.useTolls());
    ride.setPlannedBreaks(request.plannedBreaks()); ride.setFuelCost(request.fuelCost()); ride.setStatus(request.status());
    ride.setRating(request.rating()); ride.setNotes(request.notes());
  }

  private MaintenanceRecordResponse maintenanceResponse(MaintenanceRecordEntity record) {
    MotocycleEntity bike = record.getMotorcycle();
    return new MaintenanceRecordResponse(record.getId(), new MaintenanceRecordResponse.MotorcycleSummary(bike.getId(), bike.getBrand(), bike.getModel()), record.getMaintenanceType(), record.getStatus(), record.getCompletionDate(), record.getPlannedDate(), record.getMileage(), record.getPlannedMileage(), record.getCost(), record.getServiceProvider(), record.getNotes());
  }

  private RideResponse rideResponse(RideEntity ride) {
    MotocycleEntity bike = ride.getMotorcycle();
    List<RideResponse.ChecklistItem> items = checklistItems.findAll().stream().filter(item -> item.getRide().getId().equals(ride.getId())).map(item -> new RideResponse.ChecklistItem(item.getId(), item.getLabel(), item.getChecked())).toList();
    return new RideResponse(ride.getId(), new RideResponse.MotorcycleSummary(bike.getId(), bike.getBrand(), bike.getModel()), ride.getTitle(), ride.getPlannedDate(), ride.getDepartureTime(), ride.getDepartureLocation(), ride.getDestination(), ride.getEstimatedDistance(), ride.getActualDistance(), ride.getEstimatedDuration(), ride.getActualDuration(), ride.getRideType(), ride.getUseHighway(), ride.getUseTolls(), ride.getPlannedBreaks(), ride.getFuelCost(), ride.getStatus(), ride.getRating(), ride.getNotes(), items);
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
