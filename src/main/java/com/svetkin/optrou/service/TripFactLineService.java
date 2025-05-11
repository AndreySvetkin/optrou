package com.svetkin.optrou.service;

import com.svetkin.optrou.controller.GlonassSoftController;
import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.dto.GlonassSoftResponseVehicleLocationsDto;
import com.svetkin.optrou.entity.dto.GlonassSoftVehicleLocationDto;
import com.svetkin.optrou.repository.RouteRepository;
import com.svetkin.optrou.repository.TripRepository;
import com.svetkin.optrou.view.trip.TripDetailView;
import com.vaadin.flow.router.RouteParameters;
import io.jmix.core.FetchPlan;
import io.jmix.core.FetchPlans;
import io.jmix.core.Id;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.maps.utils.GeometryUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component(TripFactLineService.NAME)
public class TripFactLineService {
    public static final String NAME = "optrou_TripFactLineService";

    private final GlonassSoftController glonassSoftController;

    public TripFactLineService(GlonassSoftController glonassSoftController) {
        this.glonassSoftController = glonassSoftController;
    }

    public LineString getTripFactLine(Trip trip) {
        GlonassSoftResponseVehicleLocationsDto responseLocationsDto = glonassSoftController
                .getFactLocations(trip.getVehicle(), trip.getFactDateStart(), trip.getFactDateEnd());

        return GeometryUtils.createLineString(responseLocationsDto.getLocations().stream()
                .map(locationDto -> new Coordinate(locationDto.getLatitude(), locationDto.getLongitude()))
                .toArray(Coordinate[]::new));
    }
}