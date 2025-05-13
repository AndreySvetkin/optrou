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
import io.jmix.core.Id;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.app.inputdialog.DialogActions;
import io.jmix.flowui.app.inputdialog.DialogOutcome;
import io.jmix.flowui.app.inputdialog.InputParameter;
import io.jmix.flowui.component.combobox.EntityComboBox;
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
import io.jmix.flowui.view.builder.LookupWindowBuilder;
import io.jmix.mapsflowui.component.GeoMap;
import org.apache.poi.ss.usermodel.Workbook;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;


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

    private GeoMap map;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        mapFragment.addVectorLayerWithDataVectorSource(tripDc, "line");
        mapFragment.addVectorLayerWithDataVectorSource(controlPointsDc, "location");
    }

    @Subscribe("tripReportsDataGrid")
    public void onTripReportsDataGridCellFocus(final CellFocusEvent<TripReport> event) {
        event.getItem().ifPresent(tripReport -> {
            Trip trip = tripReport.getTrip();
            tripDc.setItem(trip);

            LineString line = trip.getLine();
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

                        tripReportCreateService.createAndNavigateTripReport(Id.of(trip));
                    }
                })
                .open();
    }

    @Subscribe("outerReportAction")
    public void onOuterReportAction(final ActionPerformedEvent event) {
        dialogWindows.lookup(this, Trip.class)
                .withSelectHandler(trips -> {
                    byte[] report = tripOuterReportCreateService.createTripsReport(trips.stream().toList());
                    downloader.download(report, "Отчет по рейсам");
                })
                .open();
    }
}