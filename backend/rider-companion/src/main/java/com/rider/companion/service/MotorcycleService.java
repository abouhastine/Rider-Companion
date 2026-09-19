package com.rider.companion.service;

import com.rider.companion.dto.MotorcycleRequest;
import com.rider.companion.entity.MotocycleEntity;
import com.rider.companion.entity.UserEntity;
import com.rider.companion.exception.MotorcycleNotFoundException;
import com.rider.companion.exception.UserNotFoundException;
import com.rider.companion.repository.MotorcycleRepository;
import com.rider.companion.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MotorcycleService {

  private final MotorcycleRepository repository;
  private final UserRepository userRepository;

  public MotorcycleService(MotorcycleRepository repository, UserRepository userRepository) {
    this.repository = repository;
    this.userRepository = userRepository;
  }

  public List<MotocycleEntity> getAllMotorcycles() {
    return repository.findAll();
  }

  public List<MotocycleEntity> getMotorcyclesByUserId(Long userId) {
    return repository.findByUserId(userId);
  }

  public MotocycleEntity getMotorcycleById(Long id) {
    return repository.findById(id).orElseThrow(() -> new MotorcycleNotFoundException(id));
  }

  public MotocycleEntity createMotorcycle(MotorcycleRequest request) {
    MotocycleEntity motorcycle = new MotocycleEntity();
    applyChanges(motorcycle, request);
    motorcycle.setId(null);
    motorcycle.setCreatedAt(LocalDate.now());
    motorcycle.setUpdatedAt(null);
    return repository.save(motorcycle);
  }

  public MotocycleEntity updateMotorcycle(Long id, MotorcycleRequest changes) {

    MotocycleEntity motorcycle = getMotorcycleById(id);
    applyChanges(motorcycle, changes);
    motorcycle.setUpdatedAt(LocalDate.now());

    return repository.save(motorcycle);
  }

  @Transactional
  public MotocycleEntity updateMileage(Long id, Integer currentMileage) {
    MotocycleEntity motorcycle = getMotorcycleById(id);
    motorcycle.setCurrentMileage(currentMileage);
    motorcycle.setUpdatedAt(LocalDate.now());
    return repository.save(motorcycle);
  }

  @Transactional
  public MotocycleEntity updatePrimaryMotorcycle(Long id, Boolean primaryMotorcycle) {
    MotocycleEntity motorcycle = getMotorcycleById(id);
    if (Boolean.TRUE.equals(primaryMotorcycle)) {
      repository
          .findByUserId(motorcycle.getUser().getId())
          .forEach(
              candidate -> {
                if (!candidate.getId().equals(id)) {
                  candidate.setPrimaryMotorcycle(false);
                  candidate.setUpdatedAt(LocalDate.now());
                }
              });
    }
    motorcycle.setPrimaryMotorcycle(primaryMotorcycle);
    motorcycle.setUpdatedAt(LocalDate.now());
    return repository.save(motorcycle);
  }

  private void applyChanges(MotocycleEntity motorcycle, MotorcycleRequest changes) {
    UserEntity user =
        userRepository
            .findById(changes.user())
            .orElseThrow(() -> new UserNotFoundException(changes.user()));

    motorcycle.setUser(user);
    motorcycle.setBrand(changes.brand());
    motorcycle.setModel(changes.model());
    motorcycle.setYear(changes.year());
    motorcycle.setEngineCapacity(changes.engineCapacity());
    motorcycle.setPower(changes.power());
    motorcycle.setFuelType(changes.fuelType());
    motorcycle.setRegistrationNumber(changes.registrationNumber());
    motorcycle.setPurchaseDate(changes.purchaseDate());
    motorcycle.setCurrentMileage(changes.currentMileage());
    motorcycle.setAverageConsumption(changes.averageConsumption());
    motorcycle.setImageUrl(changes.imageUrl());
    motorcycle.setPrimaryMotorcycle(changes.primaryMotorcycle());
  }

  public void deleteMotorcycle(Long id) {
    if (!repository.existsById(id)) {
      throw new MotorcycleNotFoundException(id);
    }
    repository.deleteById(id);
  }
}
