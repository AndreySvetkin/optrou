package com.svetkin.optrou.service;

import com.svetkin.optrou.entity.RoutePoint;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripPoint;
import com.svetkin.optrou.repository.TripPointRepository;
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
        tripPoint.setOrder(routePoint.getOrder());
        tripPoint.setName(routePoint.getName());
        tripPoint.setTrip(trip);
        tripPoint.setLatitude(routePoint.getLatitude());
        tripPoint.setLongitude(routePoint.getLongitude());
        tripPoint.setLocation(routePoint.getLocation());
        return tripPoint;
    }


}