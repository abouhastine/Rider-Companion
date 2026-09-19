package com.rider.companion.service;

import com.rider.companion.dto.RideChecklistItemRequest;
import com.rider.companion.entity.RideChecklistItemEntity;
import com.rider.companion.entity.RideEntity;
import com.rider.companion.exception.RideChecklistItemNotFoundException;
import com.rider.companion.exception.RideNotFoundException;
import com.rider.companion.repository.RideChecklistItemRepository;
import com.rider.companion.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideChecklistItemService {

  private final RideChecklistItemRepository repository;
  private final RideRepository rideRepository;

  public RideChecklistItemService(
      RideChecklistItemRepository repository, RideRepository rideRepository) {
    this.repository = repository;
    this.rideRepository = rideRepository;
  }

  public List<RideChecklistItemEntity> getAllRideChecklistItems() {
    return repository.findAll();
  }

  public RideChecklistItemEntity getRideChecklistItemById(Long id) {
    return repository.findById(id).orElseThrow(() -> new RideChecklistItemNotFoundException(id));
  }

  public RideChecklistItemEntity createRideChecklistItem(RideChecklistItemRequest request) {

    RideChecklistItemEntity item = new RideChecklistItemEntity();
    applyChanges(item, request);
    item.setId(null);
    return repository.save(item);
  }

  public RideChecklistItemEntity updateRideChecklistItem(Long id, RideChecklistItemRequest changes) {

    RideChecklistItemEntity item = getRideChecklistItemById(id);
    applyChanges(item, changes);

    return repository.save(item);
  }

  public RideChecklistItemEntity updateChecked(Long rideId, Long itemId, Boolean checked) {
    RideEntity ride = rideRepository.findById(rideId)
        .orElseThrow(() -> new RideNotFoundException(rideId));
    RideChecklistItemEntity item = getRideChecklistItemById(itemId);
    if (!item.getRide().getId().equals(ride.getId())) {
      throw new RideChecklistItemNotFoundException(itemId);
    }
    item.setChecked(checked);
    return repository.save(item);
  }


  private void applyChanges(RideChecklistItemEntity item, RideChecklistItemRequest changes) {
    RideEntity ride =
        rideRepository
            .findById(changes.ride())
            .orElseThrow(() -> new RideNotFoundException(changes.ride()));

    item.setRide(ride);
    item.setLabel(changes.label());
    item.setChecked(changes.checked());
  }

  public void deleteRideChecklistItem(Long id) {

    if (!repository.existsById(id)) {
      throw new RideChecklistItemNotFoundException(id);
    }

    repository.deleteById(id);
  }
}
