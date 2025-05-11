package com.svetkin.optrou.service;

import com.svetkin.optrou.controller.GlonassSoftController;
import com.svetkin.optrou.entity.FactRefuellingPlan;
import com.svetkin.optrou.entity.RefuellingPlan;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.dto.GlonassSoftResponseRefuellingsDto;
import com.svetkin.optrou.repository.FactRefuellingPlanRepository;
import com.svetkin.optrou.repository.FactRefuellingRepository;
import com.svetkin.optrou.service.mapper.FactRefuellingMapper;
import io.jmix.maps.utils.GeometryUtils;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component(TripFactRefuellingPlanService.NAME)
public class TripFactRefuellingPlanService {
    public static final String NAME = "optrou_TripFactRefuellingPlanService";

    private final GlonassSoftController glonassSoftController;
    private final FactRefuellingMapper factRefuellingMapper;
    private final FactRefuellingPlanRepository factRefuellingPlanRepository;
    private final FactRefuellingRepository factRefuellingRepository;

    public TripFactRefuellingPlanService(GlonassSoftController glonassSoftController, FactRefuellingMapper factRefuellingMapper, FactRefuellingPlanRepository factRefuellingPlanRepository, FactRefuellingRepository factRefuellingRepository) {
        this.glonassSoftController = glonassSoftController;
        this.factRefuellingMapper = factRefuellingMapper;
        this.factRefuellingPlanRepository = factRefuellingPlanRepository;
        this.factRefuellingRepository = factRefuellingRepository;
    }

    public FactRefuellingPlan getTripFactRefuellingPlan(Trip trip) {
        GlonassSoftResponseRefuellingsDto responseRefuellingsDto = glonassSoftController
                .getFactRefuellings(trip.getVehicle(), trip.getFactDateStart(), trip.getFactDateEnd());

        RefuellingPlan lastRefuellingPlan = trip.getRefuellingPlans().stream()
                .min(Comparator.comparing(RefuellingPlan::getCreatedDate))
                .get();

        FactRefuellingPlan factRefuellingPlan = factRefuellingPlanRepository.create();
        factRefuellingPlan.setFuelType(lastRefuellingPlan.getFuelType());
        factRefuellingPlan.setRefuellings(responseRefuellingsDto.getRefuellings().stream()
                .filter(refuellingDto -> refuellingDto.getEventType() == 3 || refuellingDto.getEventType() == 20)
                .map(refuellingDto -> factRefuellingMapper.mapDtoToEntity(factRefuellingRepository.create(), refuellingDto, factRefuellingPlan))
                .toList());

        return factRefuellingPlan;
    }
}