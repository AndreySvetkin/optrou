package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.FuelStation;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FuelStationRepository extends JmixDataRepository<FuelStation, UUID> {
}