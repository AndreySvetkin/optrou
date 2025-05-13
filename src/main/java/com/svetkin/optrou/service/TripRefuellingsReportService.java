package com.svetkin.optrou.service;

import com.svetkin.optrou.entity.FactRefuellingPlan;
import com.svetkin.optrou.entity.RefuellingPlan;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.dto.RefuellingsVolumeDto;
import io.jmix.core.Metadata;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component(TripRefuellingsReportService.NAME)
public class TripRefuellingsReportService {

    public static final String NAME = "optrou_TripRefuellingsReportService";

    private final TripFactRefuellingPlanService tripFactRefuellingPlanService;
    private final Metadata metadata;

    @PersistenceContext
    private EntityManager entityManager;

    public TripRefuellingsReportService(TripFactRefuellingPlanService tripFactRefuellingPlanService,
                                        Metadata metadata) {
        this.tripFactRefuellingPlanService = tripFactRefuellingPlanService;
        this.metadata = metadata;
    }

    public RefuellingsVolumeDto getRefuellingsVolume(Trip trip) {
        FactRefuellingPlan factRefuellingPlan = tripFactRefuellingPlanService.getTripFactRefuellingPlan(trip);
        RefuellingPlan lastRefuellingPlan = trip.getRefuellingPlans().stream()
                .min(Comparator.comparing(RefuellingPlan::getCreatedDate))
                .get();

        RefuellingsVolumeDto refuellingsVolumeDto = metadata.create(RefuellingsVolumeDto.class);
        refuellingsVolumeDto.setPlanningVolume(lastRefuellingPlan.getRefuellingsVolume());
        refuellingsVolumeDto.setFactVolume(factRefuellingPlan.getRefuellingsVolume());
        return refuellingsVolumeDto;
    }
}