package com.svetkin.optrou.service;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.RouteFuelStation;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.repository.FuelStationRepository;
import com.svetkin.optrou.repository.RouteFuelStationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.locationtech.jts.geom.LineString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Component(TripRouteViolationService.NAME)
public class TripRouteViolationService {

    public static final String NAME = "optrou_TripRouteViolationService";

    private static final Logger log = LoggerFactory.getLogger(FuelStationSearchService.class);
    private static final String SEARCH_ROUTE_VIOLATION_QUERY_STRING = """
            with fact_stbuffer as (
                select ST_Transform(ST_Buffer(ST_Transform(ST_SetSRID(#planningLine::geometry, 4326), 26986), #deviation, 'side=both'), 4326) buffer
            ),
            select ST_MaxDistance(ST_SetSRID(#factLine::geometry, 4326), ST_SetSRID(#planningLine::geometry, 4326))
            from fact_stbuffer buf;""";
    private final TripFactLineService tripFactLineService;

    @PersistenceContext
    private EntityManager entityManager;

    public TripRouteViolationService(TripFactLineService tripFactLineService) {
        this.tripFactLineService = tripFactLineService;
    }

    public double hasRouteViolations(Trip trip) {
        LineString factLine = tripFactLineService.getTripFactLine(trip);
        return (double) entityManager.createNativeQuery(SEARCH_ROUTE_VIOLATION_QUERY_STRING)
                .setParameter("planningLine", trip.getLine().toString())
                .setParameter("factLine", factLine.toString())
                .setParameter("deviation", 1000)
                .getSingleResult();
    }
}