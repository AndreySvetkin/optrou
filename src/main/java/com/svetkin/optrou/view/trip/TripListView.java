package com.svetkin.optrou.view.trip;

import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.RoutePoint;
import com.svetkin.optrou.entity.Trip;
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
import org.springframework.beans.factory.annotation.Autowired;

@com.vaadin.flow.router.Route(value = "trips", layout = MainView.class)
@ViewController(id = "optrou_Trip.list")
@ViewDescriptor(path = "trip-list-view.xml")
@LookupComponent("tripsDataGrid")
@DialogMode(width = "64em")
public class TripListView extends StandardListView<Trip> {

    @Autowired
    private Dialogs dialogs;
    @Autowired
    private ViewNavigators viewNavigators;

    @ViewComponent
    private InstanceLoader<Route> routeDl;
    @ViewComponent
    private InstanceContainer<Route> routeDc;
    @ViewComponent
    private CollectionPropertyContainer<RoutePoint> controlPointsDc;
    @ViewComponent
    private MapFragment mapFragment;

    private GeoMap map;
    @Autowired
    private TripCreateService tripCreateService;

    @Subscribe
    public void onInit(final InitEvent event) {
        map = mapFragment.getMap();
        mapFragment.addVectorLayerWithDataVectorSource(routeDc, "line");
        mapFragment.addVectorLayerWithDataVectorSource(controlPointsDc, "location");
    }

    @Subscribe("tripsDataGrid")
    public void onTripsDataGridCellFocus(final CellFocusEvent<Trip> event) {
        event.getItem().ifPresent(trip -> {
            routeDl.setEntityId(trip.getRoute().getId());
            routeDl.load();
            Route loadedRoute = routeDc.getItem();

            if (loadedRoute.getLine() != null) {
                map.setCenter(new Coordinate(loadedRoute.getLine().getCoordinateN(0)));
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
                        Trip trip = tripCreateService.createTrip(Id.of(route));

                        viewNavigators.view(this, TripDetailView.class)
                                .withRouteParameters(new RouteParameters("id", "new"))
                                .withAfterNavigationHandler(afterViewNavigationEvent -> {
                                    TripDetailView tripDetailView = afterViewNavigationEvent.getView();
                                    tripDetailView.setTrip(trip);
                                })
                                .navigate();
                    }
                })
                .open();
    }
}