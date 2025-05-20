package com.svetkin.optrou.view.tripreport;

import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripReport;
import com.svetkin.optrou.entity.type.TripStatus;
import com.svetkin.optrou.repository.TripRepository;
import com.svetkin.optrou.service.TripOuterReportCreateService;
import com.svetkin.optrou.service.TripReportCreateService;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.component.grid.CellFocusEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.FetchPlan;
import io.jmix.core.FetchPlans;
import io.jmix.core.Id;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.app.inputdialog.DialogActions;
import io.jmix.flowui.app.inputdialog.DialogOutcome;
import io.jmix.flowui.app.inputdialog.InputParameter;
import io.jmix.flowui.download.Downloader;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.mapsflowui.component.GeoMap;
import io.jmix.mapsflowui.component.model.layer.VectorLayer;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;


@Route(value = "tripReports", layout = MainView.class)
@ViewController(id = "optrou_TripReport.list")
@ViewDescriptor(path = "trip-report-list-view.xml")
@LookupComponent("tripReportsDataGrid")
@DialogMode(width = "64em")
public class TripReportListView extends StandardListView<TripReport> {

    @Autowired
    private Notifications notifications;
    @Autowired
    private DialogWindows dialogWindows;
    @Autowired
    private TripOuterReportCreateService tripOuterReportCreateService;
    @Autowired
    private Downloader downloader;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private TripReportCreateService tripReportCreateService;

    @ViewComponent
    private CollectionPropertyContainer<TripReport> controlPointsDc;
    @ViewComponent
    private InstanceContainer<Trip> tripDc;
    @ViewComponent
    private MapFragment mapFragment;
    @ViewComponent
    private InstanceContainer<TripReport> tripReportDc;

    private GeoMap map;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private FetchPlans fetchPlans;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        VectorLayer routeVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(tripReportDc, "line");
        VectorLayer factRouteVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(tripReportDc, "factLine");
        VectorLayer controlPointsVector = mapFragment.addVectorLayerWithDataVectorSource(controlPointsDc, "location");

        mapFragment.setLineStringStyleProvider(routeVectorLayer);
        mapFragment.setFactLineStringStyleProvider(factRouteVectorLayer);
        mapFragment.setControlPointStyleProvider(controlPointsVector);
    }

    @Subscribe("tripReportsDataGrid")
    public void onTripReportsDataGridCellFocus(final CellFocusEvent<TripReport> event) {
        event.getItem().ifPresent(tripReport -> {
            Trip trip = tripReport.getTrip();
            tripReportDc.setItem(tripReport);
            tripDc.setItem(trip);

            LineString line = tripReport.getLine();
            if (line != null) {
                mapFragment.setCenter(new Coordinate(line.getCoordinateN(0)));
                mapFragment.setZoom(7.0);
            }
        });
    }

    @Subscribe("tripReportsDataGrid.create")
    public void onTripReportsDataGridCreate(final ActionPerformedEvent event) {
        dialogs.createInputDialog(this)
                .withHeader("Создание отчета по рейсу")
                .withParameter(InputParameter
                        .entityParameter("trip", Trip.class)
                        .withLabel("Рейс")
                        .withRequired(true))
                .withActions(DialogActions.OK_CANCEL)
                .withCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(DialogOutcome.OK)) {
                        Trip trip = closeEvent.getValue("trip");
                        if (trip.getStatus() != TripStatus.DONE) {
                            notifications.create("Выберите рейс со статусом Исполнено")
                                    .build()
                                    .open();

                            return;
                        }

                        if (trip.getVehicle().getVehicleId() == null) {
                            notifications.create("У машины нет GlonassSoft идентификатора")
                                    .build()
                                    .open();

                            return;
                        }

                        tripReportCreateService.createAndNavigateTripReport(Id.of(trip));
                    }
                })
                .open();
    }

    @Subscribe("outerReportAction")
    public void onOuterReportAction(final ActionPerformedEvent event) {
        dialogs.createInputDialog(this)
                .withHeader("Создание excel отчета по рейсам")
                .withParameters(
                        InputParameter
                                .localDateTimeParameter("startPlanningDateStart")
                                .withLabel("Планируемая дата начала")
                                .withRequired(true),
                        InputParameter
                                .localDateTimeParameter("endPlanningDateStart")
                                .withLabel("Планируемая дата конца")
                                .withRequired(true))
                .withActions(DialogActions.OK_CANCEL)
                .withCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(DialogOutcome.OK)) {
                        LocalDateTime startPlanningStartDate = closeEvent.getValue("startPlanningDateStart");
                        LocalDateTime endPlanningStartDate = closeEvent.getValue("endPlanningDateStart");

                        List<Trip> trips = tripRepository
                                .findByPlanningDateStart(startPlanningStartDate, endPlanningStartDate, TripStatus.DONE.getId(), getTripFetchPlan());

                        byte[] report = tripOuterReportCreateService.createTripsReport(trips);
                        downloader.download(report, "Отчет по рейсам.xlsx");
                    }
                })
                .open();

    }

    private FetchPlan getTripFetchPlan() {
        return fetchPlans.builder(Trip.class)
                .addFetchPlan(FetchPlan.BASE)
                .add("route", rfpb -> rfpb.addFetchPlan(FetchPlan.BASE))
                .add("logist", lfpb -> lfpb.addFetchPlan(FetchPlan.BASE))
                .add("driver", dfpb -> dfpb.addFetchPlan(FetchPlan.BASE))
                .add("vehicle", vfpb -> vfpb.addFetchPlan(FetchPlan.BASE))
                .add("fuelStations", tfsfpb -> tfsfpb.addFetchPlan(FetchPlan.BASE)
                        .add("fuelStation", fsfpb -> fsfpb.addFetchPlan(FetchPlan.BASE)))
                .add("refuellingPlans", repfpb -> repfpb.addFetchPlan(FetchPlan.BASE)
                        .add("refuellings", refpb -> refpb.addFetchPlan(FetchPlan.BASE)))
                .build();
    }
}