package com.svetkin.optrou.view.route;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.RouteFuelStation;
import com.svetkin.optrou.entity.RoutePoint;
import com.svetkin.optrou.repository.RouteFuelStationRepository;
import com.svetkin.optrou.repository.RoutePointRepository;
import com.svetkin.optrou.service.FuelStationSearchService;
import com.svetkin.optrou.service.RouteService;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.component.tabs.Tab;
import io.jmix.core.DataManager;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
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

import java.util.Objects;
import java.util.Set;

@com.vaadin.flow.router.Route(value = "routes/:id", layout = MainView.class)
@ViewController(id = "optrou_Route.detail")
@ViewDescriptor(path = "route-detail-view.xml")
@EditedEntityContainer("routeDc")
public class RouteDetailView extends StandardDetailView<Route> {

    @Autowired
    private RouteService routeService;
    @Autowired
    private FuelStationSearchService fuelStationSearchService;
    @Autowired
    private RoutePointRepository routePointRepository;
    @Autowired
    private RouteFuelStationRepository routeFuelStationRepository;

    @ViewComponent
    private CollectionPropertyContainer<RoutePoint> controlPointsDc;
    @ViewComponent
    private InstanceContainer<Route> routeDc;
    @ViewComponent
    private CollectionPropertyContainer<RouteFuelStation> routeFuelStationsDc;
    @ViewComponent
    private InstanceLoader<Route> routeDl;
    @ViewComponent
    private DataGrid<RoutePoint> controlPointsDataGrid;
    @ViewComponent
    private DataContext dataContext;
    @ViewComponent
    private MapFragment mapFragment;
    @ViewComponent("tabSheet.fuelStationsTab")
    private Tab fuelStationsTab;
    @ViewComponent("tabSheet.commonTab")
    private Tab commonTab;

    private GeoMap map;
    private VectorLayer routeVectorLayer;
    private VectorLayer controlPointsVectorLayer;
    private VectorLayer fuelStationsVectorLayer;

    @Subscribe
    public void onInit(final InitEvent event) {
        map = mapFragment.getMap();

        routeVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(routeDc, "line");
        controlPointsVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(controlPointsDc, "location");
        fuelStationsVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(routeFuelStationsDc, "fuelStation.location");

        map.addSingleClickListener(this::onMapSingleClick);
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        LineString routeLine = getEditedEntity().getLine();
        if (routeLine != null) {
            setMapCenterByLine(routeLine);
        }
    }

    @Subscribe("tabSheet")
    public void onTabSheetSelectedChange(final JmixTabSheet.SelectedChangeEvent event) {
        fuelStationsVectorLayer.setVisible(fuelStationsTab.isSelected());
        routeVectorLayer.setVisible(commonTab.isSelected());
        controlPointsVectorLayer.setVisible(commonTab.isSelected());
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
        RoutePoint routePoint = routePointRepository.create();
        routePoint.setName("Новая точка");
        controlPointsDc.getMutableItems().add(routePoint);
    }

    @Subscribe("createRouteAction")
    public void onCreateRouteAction(final ActionPerformedEvent event) {
        Route route = getEditedEntity();
        LineString routeLine = routeService.getLineByPoints(getEditedEntity().getControlPoints()).get(0);
        route.setLine(routeLine);

        setMapCenterByLine(routeLine);
    }

    @Subscribe
    public void onBeforeSave(final BeforeSaveEvent event) {
        getEditedEntity().getControlPoints().removeIf(point -> point.getLocation() == null);
    }

    @Subscribe("searchFuelStationsAction")
    public void onSearchFuelStationsAction(final ActionPerformedEvent event) {
        routeFuelStationsDc.getMutableItems().clear();
        routeFuelStationsDc.getMutableItems().addAll(fuelStationSearchService.getFuelStations(getEditedEntity()));
    }

    private void setMapCenterByLine(LineString line) {
        mapFragment.setCenter(new Coordinate(line.getCoordinateN(0)));
        mapFragment.setZoom(10.0);
    }
}
