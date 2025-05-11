package com.svetkin.optrou.service;

import com.svetkin.optrou.entity.RefuellingPlan;
import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripReport;
import com.svetkin.optrou.repository.RouteRepository;
import com.svetkin.optrou.repository.TripReportRepository;
import com.svetkin.optrou.repository.TripRepository;
import com.svetkin.optrou.view.trip.TripDetailView;
import com.svetkin.optrou.view.tripreport.TripReportDetailView;
import com.vaadin.flow.router.RouteParameters;
import io.jmix.core.FetchPlan;
import io.jmix.core.FetchPlans;
import io.jmix.core.Id;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.UiComponentUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.UUID;

@Component(TripReportCreateService.NAME)
public class TripReportCreateService {

    public static final String NAME = "optrou_TripReportCreateService";

    private final TripRepository tripRepository;
    private final FetchPlans fetchPlans;
    private final ViewNavigators viewNavigators;
    private final TripReportRepository tripReportRepository;
    private final TripFactLineService tripFactLineService;
    private final TripFactRefuellingPlanService tripFactRefuellingPlanService;

    public TripReportCreateService(TripRepository tripRepository,
                                   FetchPlans fetchPlans,
                                   ViewNavigators viewNavigators,
                                   TripReportRepository tripReportRepository,
                                   TripFactLineService tripFactLineService,
                                   TripFactRefuellingPlanService tripFactRefuellingPlanService) {
        this.tripRepository = tripRepository;
        this.fetchPlans = fetchPlans;
        this.viewNavigators = viewNavigators;
        this.tripReportRepository = tripReportRepository;
        this.tripFactLineService = tripFactLineService;
        this.tripFactRefuellingPlanService = tripFactRefuellingPlanService;
    }

    public void createAndNavigateTripReport(Id<Trip> tripId) {
        TripReport tripReport = createAndSaveTripReport(tripId);

        viewNavigators.detailView(UiComponentUtils.getCurrentView(), TripReport.class)
                .editEntity(tripReport)
                .navigate();
    }

    public TripReport createAndSaveTripReport(Id<Trip> tripId) {
        Trip trip = tripRepository.getById((UUID) tripId.getValue(), getTripFetchPlan());
        TripReport tripReport = tripReportRepository.create();
        tripReport.setName(trip.getName());
        tripReport.setPlanningDateStart(trip.getPlanningDateStart());
        tripReport.setPlanningDateEnd(trip.getPlanningDateEnd());
        tripReport.setFactDateStart(trip.getFactDateStart());
        tripReport.setFactDateEnd(trip.getFactDateEnd());
        tripReport.setTrip(trip);
        tripReport.setRoute(trip.getRoute());
        tripReport.setLogist(trip.getLogist());
        tripReport.setDriver(trip.getDriver());
        tripReport.setVehicle(trip.getVehicle());
        tripReport.setRefuellingPlan(trip.getRefuellingPlans().stream()
                .min(Comparator.comparing(RefuellingPlan::getCreatedDate))
                .get());
        tripReport.setLine(tripReport.getRoute().getLine());
        tripReport.setLength(tripReport.getLine().getLength() * 100);
        tripReport.setFactLine(tripFactLineService.getTripFactLine(trip));
        tripReport.setFactLength(tripReport.getFactLine().getLength() * 100);
        tripReport.setFactRefuellingPlan(tripFactRefuellingPlanService.getTripFactRefuellingPlan(trip));
        tripReport = tripReportRepository.save(tripReport);
        return tripReport;
    }

    private FetchPlan getTripFetchPlan() {
        return fetchPlans.builder(Route.class)
                .addFetchPlan(FetchPlan.BASE)
                .add("trip", tfpb -> tfpb.addFetchPlan(FetchPlan.BASE))
                .add("route", rfpb -> rfpb.addFetchPlan(FetchPlan.BASE))
                .add("logist", lfpb -> lfpb.addFetchPlan(FetchPlan.BASE))
                .add("driver", dfpb -> dfpb.addFetchPlan(FetchPlan.BASE))
                .add("vehicle", vfpb -> vfpb.addFetchPlan(FetchPlan.BASE))
                .add("refuellingPlans", repfpb -> repfpb.addFetchPlan(FetchPlan.BASE)
                        .add("refuellings", refpb -> refpb.addFetchPlan(FetchPlan.BASE)))
                .build();
    }
}