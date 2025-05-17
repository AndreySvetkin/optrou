package com.svetkin.optrou.view.vehicle;

import com.svetkin.optrou.entity.Vehicle;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.component.grid.CellFocusEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.mapsflowui.component.GeoMap;
import io.jmix.mapsflowui.component.model.layer.VectorLayer;
import io.jmix.mapsflowui.component.model.source.DataVectorSource;
import io.jmix.mapsflowui.component.model.source.GeoObjectClickNotifier;
import org.locationtech.jts.geom.Point;


@Route(value = "vehicles", layout = MainView.class)
@ViewController(id = "optrou_Vehicle.list")
@ViewDescriptor(path = "vehicle-list-view.xml")
@LookupComponent("vehiclesDataGrid")
@DialogMode(width = "80em")
public class VehicleListView extends StandardListView<Vehicle> {

    @ViewComponent
    private MapFragment mapFragment;
    @ViewComponent
    private CollectionContainer<Vehicle> vehiclesDc;
    @ViewComponent
    private DataGrid<Vehicle> vehiclesDataGrid;

    private GeoMap map;

    @Subscribe
    public void onInit(final InitEvent event) {
        map = mapFragment.getMap();

        VectorLayer vehiclesVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(vehiclesDc, "location");

        vehiclesVectorLayer.<DataVectorSource<Vehicle>>getSource().addGeoObjectClickListener(this::onGeoObjectClick);

        mapFragment.setVehiclesStyleProvider(vehiclesVectorLayer);

        mapFragment.addSelectedGeoObjectTextProvider(vehiclesVectorLayer.getSource(), Vehicle.getPointTooltipTextProviderFunction());
    }

    private void onGeoObjectClick(GeoObjectClickNotifier.GeoObjectClickEvent<Vehicle> event) {
        vehiclesDataGrid.select(event.getItem());
    }

    @Subscribe("vehiclesDataGrid")
    public void onVehiclesDataGridCellFocus(final CellFocusEvent<Vehicle> event) {
        event.getItem().ifPresent(vehicle -> {
            Point location = vehicle.getLocation();
            if (location != null) {
                mapFragment.setCenter(location.getCoordinate());
                mapFragment.setZoom(10.0);
            }
        });
    }


}