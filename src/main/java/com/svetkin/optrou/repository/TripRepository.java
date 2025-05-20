package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.Trip;
import io.jmix.core.FetchPlan;
import io.jmix.core.repository.JmixDataRepository;
import io.jmix.core.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TripRepository extends JmixDataRepository<Trip, UUID> {
    @Query("""
            select t
            from optrou_Trip t
            where t.planningDateStart >= :startPlanningDateStart
                and t.planningDateStart <= :endPlanningDateStart
                and t.status = :tripStatus""")
    List<Trip> findByPlanningDateStart(LocalDateTime startPlanningDateStart, LocalDateTime endPlanningDateStart, String tripStatus, FetchPlan fetchPlan);
}