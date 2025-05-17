package com.svetkin.optrou.view.fuelstation;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.service.FuelStationPriceProcessor;
import com.svetkin.optrou.service.FuelStationProcessor;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.component.grid.CellFocusEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.mapsflowui.component.model.layer.VectorLayer;
import io.jmix.mapsflowui.component.model.source.DataVectorSource;
import io.jmix.mapsflowui.component.model.source.GeoObjectClickNotifier;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "fuelStations", layout = MainView.class)
@ViewController(id = "optrou_FuelStation.list")
@ViewDescriptor(path = "fuel-station-list-view.xml")
@LookupComponent("fuelStationsDataGrid")
@DialogMode(width = "80em")
public class FuelStationListView extends StandardListView<FuelStation> {

    @Autowired
    private FuelStationProcessor fuelStationProcessor;
    @Autowired
    private FuelStationPriceProcessor fuelStationPriceProcessor;

    @ViewComponent
    private CollectionLoader<FuelStation> fuelStationsDl;
    @ViewComponent
    private MapFragment mapFragment;
    @ViewComponent
    private CollectionContainer<FuelStation> fuelStationsDc;
    @ViewComponent
    private DataGrid<FuelStation> fuelStationsDataGrid;

    @Subscribe
    public void onInit(final InitEvent event) {
        VectorLayer fuelStationsVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(fuelStationsDc, "location");

        mapFragment.setFuelStationStyleProvider(fuelStationsVectorLayer);

        fuelStationsVectorLayer.<DataVectorSource<FuelStation>>getSource().addGeoObjectClickListener(this::onGeoObjectClick);

        mapFragment.addSelectedGeoObjectTextProvider(fuelStationsVectorLayer.getSource(), FuelStation.getPointTooltipTextProviderFunction());
    }

    private void onGeoObjectClick(GeoObjectClickNotifier.GeoObjectClickEvent<FuelStation> event) {
        fuelStationsDataGrid.select(event.getItem());
    }

    @Subscribe("fuelStationsDataGrid")
    public void onFuelStationsDataGridCellFocus(final CellFocusEvent<FuelStation> event) {
        event.getItem().ifPresent(fuelStation -> {
            Point location = fuelStation.getLocation();
            if (location != null) {
                mapFragment.setCenter(location.getCoordinate());
                mapFragment.setZoom(10.0);
            }
        });
    }

    @Subscribe("processFuelStationsAndPricesAction")
    public void onProcessFuelStationsAndPricesAction(final ActionPerformedEvent event) {
        fuelStationProcessor.processFuelStations();
        fuelStationPriceProcessor.processFuelStationPrices();
        fuelStationsDl.load();
    }

    @Subscribe("processFuelStationsAction")
    public void onProcessFuelStationsAction(final ActionPerformedEvent event) {
        fuelStationProcessor.processFuelStations();
        fuelStationsDl.load();
    }

    @Subscribe("processFuelStationsPricesAction")
    public void onProcessFuelStationPricesAction(final ActionPerformedEvent event) {
        fuelStationPriceProcessor.processFuelStationPrices();
        fuelStationsDl.load();
    }
}