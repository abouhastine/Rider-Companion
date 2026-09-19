package com.rider.companion.service;

import com.rider.companion.entity.RiderEntity;
import com.rider.companion.exception.RiderNotFoundException;
import com.rider.companion.exception.RiderProfileNotFoundException;
import com.rider.companion.repository.RiderRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RiderService {
    private final RiderRepository repository;

    public RiderService(RiderRepository repository) {
        this.repository = repository;
    }

    public List<RiderEntity> getAllRiders() {
        return repository.findAll();
    }

    public RiderEntity getRiderProfileByUserId(Long userId) {
        return repository.findByUserId(userId)
            .orElseThrow(() -> new RiderProfileNotFoundException(userId));
    }

    public RiderEntity getRidersById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RiderNotFoundException(id));
    }

    public RiderEntity createRider(RiderEntity rider) {
        rider.setId(null);
        return repository.save(rider);
    }

    public RiderEntity updateRider(Long id, RiderEntity changes) {

        RiderEntity rider = getRidersById(id);

        rider.setUserId(changes.getUserId());
        rider.setLicenseType(changes.getLicenseType());
        rider.setLicenseYear(changes.getLicenseYear());
        rider.setExperienceLevel(changes.getExperienceLevel());
        rider.setPrimaryUsage(changes.getPrimaryUsage());
        rider.setEstimatedAnnualDistance(changes.getEstimatedAnnualDistance());

        return repository.save(rider);
    }

    public void deleteRider(Long id) {
        if (!repository.existsById(id)) {
            throw new RiderNotFoundException(id);
        }
        repository.deleteById(id);
    }
}