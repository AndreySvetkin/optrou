package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.FactRefuellingPlan;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FactRefuellingPlanRepository extends JmixDataRepository<FactRefuellingPlan, UUID> {
}