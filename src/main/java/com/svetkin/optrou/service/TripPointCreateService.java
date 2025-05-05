package com.svetkin.optrou.service;

import com.svetkin.optrou.controller.OsrmRouteController;
import com.svetkin.optrou.entity.RoutePoint;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripPoint;
import com.svetkin.optrou.repository.TripPointRepository;
import com.svetkin.optrou.repository.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(TripPointCreateService.NAME)
public class TripPointCreateService {

    public static final String NAME = "optrou_TripPointCreateService";


    private final TripPointRepository tripPointRepository;

    public TripPointCreateService(TripPointRepository tripPointRepository) {
        this.tripPointRepository = tripPointRepository;
    }

    public TripPoint createTripPoint(Trip trip, RoutePoint routePoint) {
        TripPoint tripPoint = tripPointRepository.create();
        tripPoint.setName(routePoint.getName());
        tripPoint.setTrip(trip);
        tripPoint.setLatitude(routePoint.getLatitude());
        tripPoint.setLongitude(routePoint.getLongitude());
        tripPoint.setLocation(routePoint.getLocation());
        return tripPoint;
    }


}