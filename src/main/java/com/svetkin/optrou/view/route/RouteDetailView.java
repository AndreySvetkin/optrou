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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import io.jmix.core.DataManager;
import io.jmix.core.EntitySet;
import io.jmix.core.EntityStates;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.Views;
import io.jmix.flowui.action.list.RemoveAction;
import io.jmix.flowui.action.view.DetailSaveCloseAction;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.model.ViewData;
import io.jmix.flowui.sys.ViewSupport;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.flowui.xml.layout.support.ViewLoaderSupport;
import io.jmix.maps.utils.GeometryUtils;
import io.jmix.mapsflowui.component.GeoMap;
import io.jmix.mapsflowui.component.data.ContainerDataVectorSourceItems;
import io.jmix.mapsflowui.component.data.binding.ClusterDataVectorSourceBinding;
import io.jmix.mapsflowui.component.data.binding.DataVectorSourceBinding;
import io.jmix.mapsflowui.component.event.MapSingleClickEvent;
import io.jmix.mapsflowui.component.loader.MapLoaderSupport;
import io.jmix.mapsflowui.component.model.FitOptions;
import io.jmix.mapsflowui.component.model.feature.LineStringFeature;
import io.jmix.mapsflowui.component.model.layer.VectorLayer;
import io.jmix.mapsflowui.component.model.source.ClusterSource;
import io.jmix.mapsflowui.component.model.source.DataVectorSource;
import io.jmix.mapsflowui.component.model.source.VectorSource;
import org.apache.commons.collections4.CollectionUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
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
    private EntityStates entityStates;
    @Autowired
    private Notifications notifications;

    @ViewComponent
    private CollectionPropertyContainer<RoutePoint> controlPointsDc;
    @ViewComponent
    private InstanceContainer<Route> routeDc;
    @ViewComponent
    private CollectionPropertyContainer<RouteFuelStation> routeFuelStationsDc;

    @ViewComponent
    private DataGrid<RoutePoint> controlPointsDataGrid;
    @ViewComponent("routeFuelStationsDataGrid.remove")
    private RemoveAction<RouteFuelStation> routeFuelStationsDataGridRemove;
    @ViewComponent
    private DetailSaveCloseAction<Object> saveAction;
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
    private VectorLayer routeFuelStationsVectorLayer;

    @Subscribe
    public void onInit(final InitEvent event) {
        map = mapFragment.getMap();

        routeVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(routeDc, "line");
        controlPointsVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(controlPointsDc, "location");
        routeFuelStationsVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(routeFuelStationsDc, "fuelStation.location");
        routeFuelStationsVectorLayer.setVisible(false);

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
        routeFuelStationsVectorLayer.setVisible(fuelStationsTab.isSelected());
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

    @Subscribe("routeFuelStationsDataGrid.remove")
    public void onRouteFuelStationsDataGridRemove(final ActionPerformedEvent event) {
        List<RouteFuelStation> routeFuelStations = getEditedEntity().getFuelStations();

        if (CollectionUtils.isNotEmpty(routeFuelStations) && entityStates.isNew(routeFuelStations.get(0))) {
            getEditedEntity().setFuelStations(List.of());
        }

        routeFuelStationsDataGridRemove.execute();
    }

    @Subscribe("createRouteAction")
    public void onCreateRouteAction(final ActionPerformedEvent event) {
        List<RoutePoint> controlPoints = getEditedEntity().getControlPoints();

        if (CollectionUtils.size(controlPoints) < 2) {
            showEmptyControlPointsNotification();
            return;
        }

        Route route = getEditedEntity();
        LineString routeLine = routeService.getLineByPoints(controlPoints).get(0);
        route.setLine(routeLine);
        route.setLength(routeLine.getLength() * 100);

        setMapCenterByLine(routeLine);
    }

    @Subscribe("saveAction")
    public void onSaveAction(final ActionPerformedEvent event) {
        List<RoutePoint> controlPoints = getEditedEntity().getControlPoints();

        if (CollectionUtils.size(controlPoints) < 2) {
            showEmptyControlPointsNotification();
            return;
        }

        boolean hasEmptyLocation = controlPoints.stream()
                .anyMatch(controlPoint -> controlPoint.getLocation() == null);
        if (hasEmptyLocation) {
            notifications.create("Укажите локацию контрольным точкам")
                    .build()
                    .open();
            return;
        }

        closeWithSave();
    }

    private void showEmptyControlPointsNotification() {
        notifications.create("Укажите минимум две контрольные точки")
            .build()
            .open();
    }

    @Subscribe("searchFuelStationsAction")
    public void onSearchFuelStationsAction(final ActionPerformedEvent event) {
        Route editedEntity = getEditedEntity();

        if (editedEntity.getLine() == null) {
            notifications.create("Рассчитайте маршрут")
                    .build()
                    .open();
            return;
        }

        List<RouteFuelStation> routeFuelStations = fuelStationSearchService.getFuelStations(editedEntity);
        EntitySet mergedSet = dataContext.merge(routeFuelStations);
        editedEntity.setFuelStations(mergedSet.getAll(RouteFuelStation.class).stream().toList());

        notifications.create("Найдено АЗС : %s".formatted(routeFuelStations.size()))
                .build()
                .open();
    }

    private void setMapCenterByLine(LineString line) {
        mapFragment.setCenter(new Coordinate(line.getCoordinateN(0)));
        mapFragment.setZoom(10.0);
    }
}
