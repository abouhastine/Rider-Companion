# Spring REST CRUD — classic architecture

This example shows a complete REST CRUD API for the `Motorcycle` resource using the usual Spring layers:

```text
HTTP client
    |
    v
Controller  -> receives HTTP requests and returns HTTP responses
    |
    v
Service     -> contains use cases, validation, and business rules
    |
    v
Repository  -> reads and writes data through Spring Data JPA
    |
    v
Entity      -> Java object mapped to a database table
```

For example, a `POST /api/motorcycles` request flows as follows:

```text
Client JSON -> MotorcycleController.create()
            -> MotorcycleService.create()
            -> MotorcycleRepository.save()
            -> database table motorcycles
            -> 201 Created + saved Motorcycle JSON
```

## REST contract

| Operation | HTTP method and URL | Success response |
| --- | --- | --- |
| Create | `POST /api/motorcycles` | `201 Created` |
| Read all | `GET /api/motorcycles` | `200 OK` |
| Read one | `GET /api/motorcycles/{id}` | `200 OK` or `404 Not Found` |
| Update | `PUT /api/motorcycles/{id}` | `200 OK` or `404 Not Found` |
| Delete | `DELETE /api/motorcycles/{id}` | `204 No Content` or `404 Not Found` |

Example request body for create and update:

```json
{
  "brand": "Yamaha",
  "model": "MT-07",
  "year": 2024
}
```

## 1. Entity: database mapping

`entity/Motorcycle.java` represents one row in the `motorcycles` table. JPA generates the `id` when the entity is saved.

```java
package com.rider.companion.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "motorcycles")
public class Motorcycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private Integer year;

    public Motorcycle() { }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
}
```

## 2. Repository: persistence access

`JpaRepository` already provides `findAll`, `findById`, `save`, `existsById`, and `deleteById`; no SQL is needed for basic CRUD.

```java
package com.rider.companion.repository;

import com.rider.companion.entity.Motorcycle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotorcycleRepository extends JpaRepository<Motorcycle, Long> {
}
```

## 3. Service: use cases and error handling

The service is the only layer that talks to the repository. It centralizes the “not found” rule and controls what fields may be changed.

```java
package com.rider.companion.service;

import com.rider.companion.entity.Motorcycle;
import com.rider.companion.repository.MotorcycleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotorcycleService {

    private final MotorcycleRepository repository;

    public MotorcycleService(MotorcycleRepository repository) {
        this.repository = repository;
    }

    public Motorcycle create(Motorcycle motorcycle) {
        motorcycle.setId(null); // a new resource must not choose its database id
        return repository.save(motorcycle);
    }

    public List<Motorcycle> findAll() {
        return repository.findAll();
    }

    public Motorcycle findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new MotorcycleNotFoundException(id));
    }

    public Motorcycle update(Long id, Motorcycle changes) {
        Motorcycle motorcycle = findById(id);
        motorcycle.setBrand(changes.getBrand());
        motorcycle.setModel(changes.getModel());
        motorcycle.setYear(changes.getYear());
        return repository.save(motorcycle);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new MotorcycleNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
```

```java
package com.rider.companion.service;

public class MotorcycleNotFoundException extends RuntimeException {
    public MotorcycleNotFoundException(Long id) {
        super("Motorcycle " + id + " was not found");
    }
}
```

## 4. Controller: REST endpoints

The controller converts HTTP requests into service calls. `@RequestBody` converts JSON to Java; `@PathVariable` reads the `{id}` from the URL.

```java
package com.rider.companion.controller;

import com.rider.companion.entity.Motorcycle;
import com.rider.companion.service.MotorcycleService;
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
@RequestMapping("/api/motorcycles")
public class MotorcycleController {

    private final MotorcycleService service;

    public MotorcycleController(MotorcycleService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Motorcycle create(@RequestBody Motorcycle motorcycle) {
        return service.create(motorcycle);
    }

    @GetMapping
    public List<Motorcycle> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Motorcycle findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Motorcycle update(@PathVariable Long id, @RequestBody Motorcycle motorcycle) {
        return service.update(id, motorcycle);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
```

## 5. Exception handler: return a useful 404

Without a handler, an exception becomes a generic server error. `@RestControllerAdvice` maps this domain exception to `404 Not Found`.

```java
package com.rider.companion.controller;

import com.rider.companion.service.MotorcycleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MotorcycleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(MotorcycleNotFoundException exception) {
        return Map.of("message", exception.getMessage());
    }
}
```

## Try the complete API

```bash
# Create
curl -i -X POST http://localhost:8080/api/motorcycles \
  -H 'Content-Type: application/json' \
  -d '{"brand":"Yamaha","model":"MT-07","year":2024}'

# Read all / read one
curl -i http://localhost:8080/api/motorcycles
curl -i http://localhost:8080/api/motorcycles/1

# Update
curl -i -X PUT http://localhost:8080/api/motorcycles/1 \
  -H 'Content-Type: application/json' \
  -d '{"brand":"Yamaha","model":"MT-07","year":2025}'

# Delete
curl -i -X DELETE http://localhost:8080/api/motorcycles/1
```

## Project note

The current project uses `MotorcycleRepo`; the guide calls it `MotorcycleRepository` for clarity. Either name works, as long as the service imports the same interface. The entity maps to the `motorcycles` table; its Java `year` field maps to the `manufacture_year` column to avoid SQL reserved-word conflicts.

For a production API, prefer request/response DTOs plus Bean Validation (`@Valid`, `@NotBlank`, `@Min`) instead of exposing the JPA entity directly. This guide deliberately uses the entity in controller requests to keep the first CRUD example focused on the four layers.
