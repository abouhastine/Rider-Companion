package com.rider.companion.controller;

import com.rider.companion.entity.UserEntity;
import com.rider.companion.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations for managing users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @Operation(summary = "List users", description = "Returns every user.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Users returned")})
  public List<UserEntity> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a user", description = "Returns one user by its database identifier.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "User returned"),
    @ApiResponse(responseCode = "404", description = "User not found")
  })
  public UserEntity getUserById(
      @Parameter(description = "User identifier", example = "1") @PathVariable Long id) {

    return userService.getUserById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a user")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "User created"),
    @ApiResponse(responseCode = "400", description = "Invalid request body")
  })
  public UserEntity createUser(@Valid @RequestBody UserEntity user) {

    return userService.createUser(user);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update a user",
      description = "Replaces the editable fields of an existing user.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "User updated"),
    @ApiResponse(responseCode = "400", description = "Invalid request body"),
    @ApiResponse(responseCode = "404", description = "User not found")
  })
  public UserEntity updateUser(@PathVariable Long id, @Valid @RequestBody UserEntity user) {

    return userService.updateUser(id, user);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete a user")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "User deleted"),
    @ApiResponse(responseCode = "404", description = "User not found")
  })
  public void deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
  }
}
