package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.RoutePoint;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoutePointRepository extends JmixDataRepository<RoutePoint, UUID> {
}