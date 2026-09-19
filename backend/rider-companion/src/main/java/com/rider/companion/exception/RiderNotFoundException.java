package com.rider.companion.exception;

public class RiderNotFoundException extends RuntimeException {
  public RiderNotFoundException(Long id) {
    super("Rider with id " + id + " not found");
  }
}
