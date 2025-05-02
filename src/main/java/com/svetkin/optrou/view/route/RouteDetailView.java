package com.svetkin.optrou.view.route;

import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.RoutePoint;
import com.svetkin.optrou.service.RouteService;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import io.jmix.core.DataManager;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.maps.utils.GeometryUtils;
import io.jmix.mapsflowui.component.GeoMap;
import io.jmix.mapsflowui.component.data.ContainerDataVectorSourceItems;
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

import java.util.Set;

@com.vaadin.flow.router.Route(value = "routes/:id", layout = MainView.class)
@ViewController(id = "optrou_Route.detail")
@ViewDescriptor(path = "route-detail-view.xml")
@EditedEntityContainer("routeDc")
public class RouteDetailView extends StandardDetailView<Route> {

    @Autowired
    private DataManager dataManager;
    @Autowired
    private RouteService routeService;

    @ViewComponent
    private CollectionPropertyContainer<RoutePoint> controlPointsDc;
    @ViewComponent
    private DataGrid<RoutePoint> controlPointsDataGrid;
    @ViewComponent
    private InstanceContainer<Route> routeDc;
    @ViewComponent
    private DataContext dataContext;
    @ViewComponent
    private MapFragment mapFragment;

    private GeoMap map;

    @Subscribe
    public void onInit(final InitEvent event) {
        map = mapFragment.getMap();

        mapFragment.addVectorLayerWithDataVectorSource(routeDc, "line");
        mapFragment.addVectorLayerWithDataVectorSource(controlPointsDc, "location");

        mapFragment.setZoom(10);

        map.addSingleClickListener(this::onMapSingleClick);
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {

    }

    public void onMapSingleClick(final MapSingleClickEvent event) {
        RoutePoint selectedPoint = controlPointsDataGrid.getSingleSelectedItem();

        if (selectedPoint != null) {
            Coordinate clickCoordinate = event.getCoordinate();
            Point clickPoint = GeometryUtils.createPoint(clickCoordinate.x, clickCoordinate.y);

            selectedPoint.setRoute(getEditedEntity());
            selectedPoint.setLatitude(clickCoordinate.x);
            selectedPoint.setLongitude(clickCoordinate.y);
            selectedPoint.setLocation(clickPoint);

            dataContext.merge(selectedPoint);
        }
    }

    @Subscribe("controlPointsDataGrid.create")
    public void onControlPointsDataGridCreate(final ActionPerformedEvent event) {
        RoutePoint routePoint = dataManager.create(RoutePoint.class);
        routePoint.setName("Новая точка");
        controlPointsDc.getMutableItems().add(routePoint);
    }

    @Subscribe("createRouteAction")
    public void onCreateRouteAction(final ActionPerformedEvent event) {
        Route route = getEditedEntity();
        LineString routeLine = routeService.getLineByPoints(getEditedEntity().getControlPoints()).get(0);
        route.setLine(routeLine);

        mapFragment.setCenter(new Coordinate(routeLine.getCoordinateN(0)));
    }

    @Subscribe
    public void onBeforeSave(final BeforeSaveEvent event) {
        getEditedEntity().getControlPoints().removeIf(point -> point.getLocation() == null);
    }
}