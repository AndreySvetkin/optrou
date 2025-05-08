package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.RefuellingPlan;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefuellingPlanRepository extends JmixDataRepository<RefuellingPlan, UUID> {
}