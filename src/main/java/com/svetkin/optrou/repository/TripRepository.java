package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.Trip;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TripRepository extends JmixDataRepository<Trip, UUID> {
    List<Trip> findByStatus(String status);
}