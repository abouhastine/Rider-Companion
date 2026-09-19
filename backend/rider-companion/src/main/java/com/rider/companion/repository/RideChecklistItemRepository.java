package com.rider.companion.repository;

import com.rider.companion.entity.RideChecklistItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideChecklistItemRepository extends JpaRepository<RideChecklistItemEntity, Long> {}
