package com.rider.companion.service;

import com.rider.companion.dto.RideRequest;
import com.rider.companion.entity.MotocycleEntity;
import com.rider.companion.entity.RideEntity;
import com.rider.companion.exception.MotorcycleNotFoundException;
import com.rider.companion.exception.RideNotFoundException;
import com.rider.companion.repository.MotorcycleRepository;
import com.rider.companion.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RideService {

  private final RideRepository repository;
  private final MotorcycleRepository motorcycleRepository;

  public RideService(RideRepository repository, MotorcycleRepository motorcycleRepository) {
    this.repository = repository;
    this.motorcycleRepository = motorcycleRepository;
  }

  public List<RideEntity> getAllRides() {
    return repository.findAll();
  }

  public List<RideEntity> getRidesByMotorcycleId(Long motorcycleId) {
    if (!motorcycleRepository.existsById(motorcycleId)) {
      throw new MotorcycleNotFoundException(motorcycleId);
    }
    return repository.findByMotorcycleId(motorcycleId);
  }


  public RideEntity getRideById(Long id) {
    return repository.findById(id).orElseThrow(() -> new RideNotFoundException(id));
  }

  public RideEntity createRide(RideRequest request) {
    RideEntity ride = new RideEntity();
    applyChanges(ride, request);
    ride.setId(null);
    ride.setCreatedAt(LocalDate.now());
    ride.setUpdatedAt(null);
    return repository.save(ride);
  }

  public RideEntity updateRide(Long id, RideRequest changes) {

    RideEntity ride = getRideById(id);
    applyChanges(ride, changes);
    ride.setUpdatedAt(LocalDate.now());

    return repository.save(ride);
  }

  public RideEntity updateStatus(Long id, String status) {
    RideEntity ride = getRideById(id);
    ride.setStatus(status);
    ride.setUpdatedAt(LocalDate.now());
    return repository.save(ride);
  }


  private void applyChanges(RideEntity ride, RideRequest changes) {
    MotocycleEntity motorcycle =
        motorcycleRepository
            .findById(changes.motorcycle())
            .orElseThrow(() -> new MotorcycleNotFoundException(changes.motorcycle()));

    ride.setMotorcycle(motorcycle);
    ride.setTitle(changes.title());
    ride.setPlannedDate(changes.plannedDate());
    ride.setDepartureTime(changes.departureTime());
    ride.setDepartureLocation(changes.departureLocation());
    ride.setDestination(changes.destination());
    ride.setEstimatedDistance(changes.estimatedDistance());
    ride.setActualDistance(changes.actualDistance());
    ride.setEstimatedDuration(changes.estimatedDuration());
    ride.setActualDuration(changes.actualDuration());
    ride.setRideType(changes.rideType());
    ride.setUseHighway(changes.useHighway());
    ride.setUseTolls(changes.useTolls());
    ride.setPlannedBreaks(changes.plannedBreaks());
    ride.setFuelCost(changes.fuelCost());
    ride.setStatus(changes.status());
    ride.setRating(changes.rating());
    ride.setNotes(changes.notes());
  }

  public void deleteRide(Long id) {

    if (!repository.existsById(id)) {
      throw new RideNotFoundException(id);
    }

    repository.deleteById(id);
  }
}
