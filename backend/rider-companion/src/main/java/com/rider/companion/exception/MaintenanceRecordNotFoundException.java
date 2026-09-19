package com.rider.companion.exception;

public class MaintenanceRecordNotFoundException extends RuntimeException {
  public MaintenanceRecordNotFoundException(Long id) {
    super("Maintenance record with id " + id + " not found");
  }
}
