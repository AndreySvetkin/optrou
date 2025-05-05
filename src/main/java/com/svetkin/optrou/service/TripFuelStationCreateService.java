package com.svetkin.optrou.service;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.RouteFuelStation;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripFuelStation;
import com.svetkin.optrou.repository.RouteFuelStationRepository;
import com.svetkin.optrou.repository.TripFuelStationRepository;
import org.springframework.stereotype.Component;

@Component(TripFuelStationCreateService.NAME)
public class TripFuelStationCreateService {

    public static final String NAME = "optrou_TripFuelStationCreateService";

    private final TripFuelStationRepository tripFuelStationRepository;

    public TripFuelStationCreateService(TripFuelStationRepository tripFuelStationRepository) {
        this.tripFuelStationRepository = tripFuelStationRepository;
    }

    public TripFuelStation createTripFuelStation(Trip trip, RouteFuelStation routeFuelStation) {
        TripFuelStation tripFuelStation = tripFuelStationRepository.create();
        tripFuelStation.setTrip(trip);
        tripFuelStation.setFuelStation(routeFuelStation.getFuelStation());
        tripFuelStation.setDistance(routeFuelStation.getDistance());
        return tripFuelStation;
    }
}