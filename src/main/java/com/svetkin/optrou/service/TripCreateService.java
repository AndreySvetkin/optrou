package com.svetkin.optrou.service;

import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.User;
import com.svetkin.optrou.repository.RouteRepository;
import com.svetkin.optrou.repository.TripRepository;
import com.svetkin.optrou.view.trip.TripDetailView;
import com.vaadin.flow.router.RouteParameters;
import io.jmix.core.FetchPlan;
import io.jmix.core.FetchPlans;
import io.jmix.core.Id;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.UiComponentUtils;
import org.springframework.stereotype.Component;

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
    private final CurrentAuthentication currentAuthentication;

    public TripCreateService(TripRepository tripRepository,
                             TripPointCreateService tripPointCreateService,
                             TripFuelStationCreateService tripFuelStationCreateService,
                             RouteRepository routeRepository,
                             FetchPlans fetchPlans,
                             ViewNavigators viewNavigators,
                             CurrentAuthentication currentAuthentication) {
        this.tripRepository = tripRepository;
        this.tripPointCreateService = tripPointCreateService;
        this.tripFuelStationCreateService = tripFuelStationCreateService;
        this.routeRepository = routeRepository;
        this.fetchPlans = fetchPlans;
        this.viewNavigators = viewNavigators;
        this.currentAuthentication = currentAuthentication;
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
        trip.setLogist((User) currentAuthentication.getUser());
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