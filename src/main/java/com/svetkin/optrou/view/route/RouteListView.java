package com.svetkin.optrou.view.route;

import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.RoutePoint;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.repository.TripRepository;
import com.svetkin.optrou.service.RouteService;
import com.svetkin.optrou.service.TripCreateService;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.component.grid.CellFocusEvent;
import com.vaadin.flow.router.RouteParameters;
import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.Metadata;
import io.jmix.core.MetadataTools;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.model.impl.CollectionContainerImpl;
import io.jmix.flowui.model.impl.InstanceContainerImpl;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.maps.utils.GeometryUtils;
import io.jmix.mapsflowui.component.GeoMap;
import io.jmix.mapsflowui.component.data.ContainerDataVectorSourceItems;
import io.jmix.mapsflowui.component.data.DataVectorSourceItems;
import io.jmix.mapsflowui.component.event.MapSingleClickEvent;
import io.jmix.mapsflowui.component.model.feature.LineStringFeature;
import io.jmix.mapsflowui.component.model.layer.VectorLayer;
import io.jmix.mapsflowui.component.model.source.DataVectorSource;
import io.jmix.mapsflowui.component.model.source.VectorSource;
import org.apache.commons.collections4.CollectionUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Set;
import java.util.Vector;

@com.vaadin.flow.router.Route(value = "routes", layout = MainView.class)
@ViewController(id = "optrou_Route.list")
@ViewDescriptor(path = "route-list-view.xml")
@LookupComponent("routesDataGrid")
@DialogMode(width = "80em")
public class RouteListView extends StandardListView<Route> {

    @Autowired
    private Notifications notifications;
    @Autowired
    private TripCreateService tripCreateService;

    @ViewComponent
    private InstanceContainer<Route> routeDc;
    @ViewComponent
    private CollectionPropertyContainer<RoutePoint> controlPointsDc;
    @ViewComponent
    private MapFragment mapFragment;
    @ViewComponent
    private DataGrid<Route> routesDataGrid;

    private GeoMap map;

    @Subscribe
    public void onInit(final InitEvent event) {
        map = mapFragment.getMap();

        mapFragment.addVectorLayerWithDataVectorSource(routeDc, "line");
        mapFragment.addVectorLayerWithDataVectorSource(controlPointsDc, "location");
    }

    @Subscribe("routesDataGrid")
    public void onRoutesDataGridCellFocus(final CellFocusEvent<Route> event) {
        event.getItem().ifPresent(route -> {
            routeDc.setItem(route);

            if (route.getLine() != null) {
                mapFragment.setCenter(new Coordinate(route.getLine().getCoordinateN(0)));
                mapFragment.setZoom(7.0);
            }
        });
    }

    @Subscribe("createTripAction")
    public void onCreateTripAction(final ActionPerformedEvent event) {
        Set<Route> selectedRoutes = routesDataGrid.getSelectedItems();

        if (CollectionUtils.size(selectedRoutes) == 1) {
            Route selectedRoute = selectedRoutes.iterator().next();
            tripCreateService.createAndNavigateTrip(Id.of(selectedRoute));
        } else {
            notifications.create("Выберите маршрут")
                    .build()
                    .open();
        }
    }
}