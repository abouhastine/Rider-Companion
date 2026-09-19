package com.rider.companion.controller;

import com.rider.companion.exception.MotorcycleNotFoundException;
import com.rider.companion.exception.RiderNotFoundException;
import com.rider.companion.exception.RiderProfileNotFoundException;
import com.rider.companion.exception.MaintenanceRecordNotFoundException;
import com.rider.companion.exception.UserNotFoundException;
import com.rider.companion.exception.RideNotFoundException;
import com.rider.companion.exception.RideChecklistItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleValidation(MethodArgumentNotValidException exception) {
    String message =
        exception.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .orElse("Invalid request body");
    return Map.of("message", message);
  }

  @ExceptionHandler({
    MotorcycleNotFoundException.class,
    RiderNotFoundException.class,
    RiderProfileNotFoundException.class,
    MaintenanceRecordNotFoundException.class,
    UserNotFoundException.class,
    RideNotFoundException.class,
    RideChecklistItemNotFoundException.class
  })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String, String> handlerNotFound(RuntimeException exception) {
    return Map.of("message", exception.getMessage());
  }
}
