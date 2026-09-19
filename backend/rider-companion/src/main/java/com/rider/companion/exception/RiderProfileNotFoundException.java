package com.rider.companion.exception;

public class RiderProfileNotFoundException extends RuntimeException {
  public RiderProfileNotFoundException(Long userId) {
    super("Rider profile for user id " + userId + " was not found");
  }
}
