package com.svetkin.optrou.service;

import com.mapbox.geojson.utils.PolylineUtils;
import com.svetkin.optrou.controller.OsrmRouteController;
import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.RoutePoint;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.dto.RouteDto;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component(TripCreateService.NAME)
public class TripCreateService {

    public static final String NAME = "optrou_TripCreateService";

    private final TripRepository tripRepository;
    private final TripPointCreateService tripPointCreateService;
    private final TripFuelStationCreateService tripFuelStationCreateService;
    private final RouteRepository routeRepository;
    private final FetchPlans fetchPlans;
    private final ViewNavigators viewNavigators;

    public TripCreateService(TripRepository tripRepository,
                             TripPointCreateService tripPointCreateService,
                             TripFuelStationCreateService tripFuelStationCreateService,
                             RouteRepository routeRepository,
                             FetchPlans fetchPlans,
                             ViewNavigators viewNavigators) {
        this.tripRepository = tripRepository;
        this.tripPointCreateService = tripPointCreateService;
        this.tripFuelStationCreateService = tripFuelStationCreateService;
        this.routeRepository = routeRepository;
        this.fetchPlans = fetchPlans;
        this.viewNavigators = viewNavigators;
    }

    public void createAndNavigateTrip(Id<Route> routeId) {
        Trip trip = createTrip(routeId);

        viewNavigators.view(UiComponentUtils.getCurrentView(), TripDetailView.class)
                .withRouteParameters(new RouteParameters("id", "new"))
                .withAfterNavigationHandler(afterViewNavigationEvent -> {
                    TripDetailView tripDetailView = afterViewNavigationEvent.getView();
                    tripDetailView.setTrip(trip);
                })
                .navigate();
    }

    public Trip createTrip(Id<Route> routeId) {
        Route route = routeRepository.getById((UUID) routeId.getValue(), getRouteFetchPlan());
        Trip trip = tripRepository.create();
        trip.setRoute(route);
        trip.setLine(route.getLine());
        trip.setLength(route.getLine().getLength() * 100);
        trip.setControlPoints(route.getControlPoints().stream()
                .map(routePoint -> tripPointCreateService.createTripPoint(trip, routePoint))
                .toList());
        trip.setFuelStations(route.getFuelStations().stream()
                .map(routeFuelStation -> tripFuelStationCreateService.createTripFuelStation(trip, routeFuelStation))
                .toList());
        return trip;
    }

    private FetchPlan getRouteFetchPlan() {
        return fetchPlans.builder(Route.class)
                .addFetchPlan(FetchPlan.BASE)
                .add("controlPoints", rpfpb -> rpfpb.addFetchPlan(FetchPlan.BASE))
                .add("fuelStations", fsfpb -> fsfpb
                        .addFetchPlan(FetchPlan.BASE)
                        .add("fuelStation"))
                .build();
    }
}