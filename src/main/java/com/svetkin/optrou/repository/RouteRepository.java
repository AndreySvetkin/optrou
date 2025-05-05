package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.Route;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RouteRepository extends JmixDataRepository<Route, UUID> {
}