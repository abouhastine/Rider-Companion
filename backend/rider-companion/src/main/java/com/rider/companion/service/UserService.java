package com.rider.companion.service;

import com.rider.companion.entity.UserEntity;
import com.rider.companion.exception.UserNotFoundException;
import com.rider.companion.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

  private final UserRepository repository;

  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  public List<UserEntity> getAllUsers() {
    return repository.findAll();
  }

  public UserEntity getUserById(Long id) {
    return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  public UserEntity createUser(UserEntity user) {
    user.setId(null);
    user.setCreatedAt(LocalDate.now());
    user.setUpdatedAt(null);
    return repository.save(user);
  }

  public UserEntity updateUser(Long id, UserEntity changes) {

    UserEntity user = getUserById(id);

    user.setFirstName(changes.getFirstName());
    user.setLastName(changes.getLastName());
    user.setEmail(changes.getEmail());
    user.setPasswordHash(changes.getPasswordHash());
    user.setCreatedAt(user.getCreatedAt());
    user.setUpdatedAt(LocalDate.now());

    return repository.save(user);
  }

  public void deleteUser(Long id) {

    if (!repository.existsById(id)) {
      throw new UserNotFoundException(id);
    }

    repository.deleteById(id);
  }
}
