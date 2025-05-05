package com.svetkin.optrou.view.fuelstation;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.service.FuelStationPriceProcessor;
import com.svetkin.optrou.service.FuelStationProcessor;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.component.grid.CellFocusEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import io.jmix.mapsflowui.component.GeoMap;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "fuelStations", layout = MainView.class)
@ViewController(id = "optrou_FuelStation.list")
@ViewDescriptor(path = "fuel-station-list-view.xml")
@LookupComponent("fuelStationsDataGrid")
@DialogMode(width = "64em")
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

    @Subscribe
    public void onInit(final InitEvent event) {
        mapFragment.addVectorLayerWithDataVectorSource(fuelStationsDc, "location");
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