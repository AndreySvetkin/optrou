package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.TripFuelStation;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TripFuelStationRepository extends JmixDataRepository<TripFuelStation, UUID> {
}