package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.TripPoint;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TripPointRepository extends JmixDataRepository<TripPoint, UUID> {
}