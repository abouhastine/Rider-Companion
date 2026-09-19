package com.rider.companion.exception;

public class MotorcycleNotFoundException extends RuntimeException {
  public MotorcycleNotFoundException(Long id) {
    super("Motorcycle with id " + id + " was not found");
  }
}
