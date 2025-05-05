package com.svetkin.optrou.service;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.RouteFuelStation;
import com.svetkin.optrou.entity.RoutePoint;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripPoint;
import com.svetkin.optrou.repository.RouteFuelStationRepository;
import com.svetkin.optrou.repository.TripPointRepository;
import org.springframework.stereotype.Component;

@Component(RouteFuelStationCreateService.NAME)
public class RouteFuelStationCreateService {

    public static final String NAME = "optrou_RouteFuelStationCreateService";

    private final RouteFuelStationRepository routeFuelStationRepository;

    public RouteFuelStationCreateService(RouteFuelStationRepository routeFuelStationRepository) {
        this.routeFuelStationRepository = routeFuelStationRepository;
    }

    public RouteFuelStation createRouteFuelStation(Route route, FuelStation fuelStation, Double distance) {
        RouteFuelStation routeFuelStation = routeFuelStationRepository.create();
        routeFuelStation.setRoute(route);
        routeFuelStation.setFuelStation(fuelStation);
        routeFuelStation.setDistance(distance);
        return routeFuelStation;
    }
}