package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.RouteFuelStation;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RouteFuelStationRepository extends JmixDataRepository<RouteFuelStation, UUID> {
}