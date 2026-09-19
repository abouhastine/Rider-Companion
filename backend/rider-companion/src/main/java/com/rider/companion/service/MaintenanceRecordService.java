package com.rider.companion.service;

import com.rider.companion.dto.MaintenanceRecordRequest;
import com.rider.companion.entity.MaintenanceRecordEntity;
import com.rider.companion.entity.MotocycleEntity;
import com.rider.companion.exception.MaintenanceRecordNotFoundException;
import com.rider.companion.exception.MotorcycleNotFoundException;
import com.rider.companion.repository.MaintenanceRecordRepository;
import com.rider.companion.repository.MotorcycleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceRecordService {

  private final MaintenanceRecordRepository repository;
  private final MotorcycleRepository motorcycleRepository;

  public MaintenanceRecordService(
      MaintenanceRecordRepository repository, MotorcycleRepository motorcycleRepository) {
    this.repository = repository;
    this.motorcycleRepository = motorcycleRepository;
  }

  public List<MaintenanceRecordEntity> getAllMaintenanceRecords() {
    return repository.findAll();
  }

  public List<MaintenanceRecordEntity> getMaintenanceRecordsByMotorcycleId(Long motorcycleId) {
    if (!motorcycleRepository.existsById(motorcycleId)) {
      throw new MotorcycleNotFoundException(motorcycleId);
    }
    return repository.findByMotorcycleId(motorcycleId);
  }


  public MaintenanceRecordEntity getMaintenanceRecordById(Long id) {
    return repository.findById(id).orElseThrow(() -> new MaintenanceRecordNotFoundException(id));
  }

  public MaintenanceRecordEntity createMaintenanceRecord(
      MaintenanceRecordRequest request) {

    MaintenanceRecordEntity maintenanceRecord = new MaintenanceRecordEntity();
    applyChanges(maintenanceRecord, request);
    maintenanceRecord.setId(null);
    maintenanceRecord.setCreatedAt(LocalDate.now());
    maintenanceRecord.setUpdatedAt(null);

    return repository.save(maintenanceRecord);
  }

  public MaintenanceRecordEntity updateMaintenanceRecord(Long id, MaintenanceRecordRequest changes) {

    MaintenanceRecordEntity maintenanceRecord = getMaintenanceRecordById(id);
    applyChanges(maintenanceRecord, changes);

    maintenanceRecord.setUpdatedAt(LocalDate.now());

    return repository.save(maintenanceRecord);
  }

  private void applyChanges(MaintenanceRecordEntity maintenanceRecord, MaintenanceRecordRequest changes) {
    MotocycleEntity motorcycle =
        motorcycleRepository
            .findById(changes.motorcycle())
            .orElseThrow(() -> new MotorcycleNotFoundException(changes.motorcycle()));

    maintenanceRecord.setMotorcycle(motorcycle);
    maintenanceRecord.setMaintenanceType(changes.maintenanceType());
    maintenanceRecord.setStatus(changes.status());
    maintenanceRecord.setCompletionDate(changes.completionDate());
    maintenanceRecord.setPlannedDate(changes.plannedDate());
    maintenanceRecord.setMileage(changes.mileage());
    maintenanceRecord.setPlannedMileage(changes.plannedMileage());
    maintenanceRecord.setCost(changes.cost());
    maintenanceRecord.setServiceProvider(changes.serviceProvider());
    maintenanceRecord.setNotes(changes.notes());
  }

  public void deleteMaintenanceRecord(Long id) {

    if (!repository.existsById(id)) {
      throw new MaintenanceRecordNotFoundException(id);
    }

    repository.deleteById(id);
  }
}
