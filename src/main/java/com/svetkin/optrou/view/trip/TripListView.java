package com.svetkin.optrou.view.trip;

import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.RoutePoint;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripPoint;
import com.svetkin.optrou.repository.TripRepository;
import com.svetkin.optrou.service.TripCreateService;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.component.grid.CellFocusEvent;
import com.vaadin.flow.router.RouteParameters;
import io.jmix.core.Id;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.app.inputdialog.DialogActions;
import io.jmix.flowui.app.inputdialog.DialogOutcome;
import io.jmix.flowui.app.inputdialog.InputParameter;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.sys.ViewSupport;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.flowui.view.navigation.DetailViewNavigator;
import io.jmix.flowui.view.navigation.ViewNavigationSupport;
import io.jmix.mapsflowui.component.GeoMap;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;

@com.vaadin.flow.router.Route(value = "trips", layout = MainView.class)
@ViewController(id = "optrou_Trip.list")
@ViewDescriptor(path = "trip-list-view.xml")
@LookupComponent("tripsDataGrid")
@DialogMode(width = "64em")
public class TripListView extends StandardListView<Trip> {

    @Autowired
    private TripCreateService tripCreateService;
    @Autowired
    private Dialogs dialogs;

    @ViewComponent
    private CollectionContainer<Trip> tripsDc;
    @ViewComponent
    private CollectionPropertyContainer<TripPoint> controlPointsDc;
    @ViewComponent
    private MapFragment mapFragment;

    private GeoMap map;
    @ViewComponent
    private InstanceContainer<Trip> tripDc;

    @Subscribe
    public void onInit(final InitEvent event) {
        map = mapFragment.getMap();

        mapFragment.addVectorLayerWithDataVectorSource(tripDc, "line");
        mapFragment.addVectorLayerWithDataVectorSource(controlPointsDc, "location");
    }

    @Subscribe("tripsDataGrid")
    public void onTripsDataGridCellFocus(final CellFocusEvent<Trip> event) {
        event.getItem().ifPresent(trip -> {
            tripDc.setItem(trip);

            LineString line = trip.getLine();
            if (line != null) {
                mapFragment.setCenter(new Coordinate(line.getCoordinateN(0)));
                mapFragment.setZoom(7.0);
            }
        });
    }

    @SuppressWarnings("DataFlowIssue")
    @Subscribe("tripsDataGrid.create")
    public void onTripsDataGridCreate(final ActionPerformedEvent event) {
        dialogs.createInputDialog(this)
                .withHeader("Создание маршрута")
                .withParameter(InputParameter
                        .entityParameter("route", Route.class)
                        .withLabel("Маршрут")
                        .withRequired(true))
                .withActions(DialogActions.OK_CANCEL)
                .withCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(DialogOutcome.OK)) {
                        Route route = closeEvent.getValue("route");
                        tripCreateService.createAndNavigateTrip(Id.of(route));
                    }
                })
                .open();
    }
}