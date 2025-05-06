package com.svetkin.optrou.view.fuelstation;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.service.FuelStationPriceProcessor;
import com.svetkin.optrou.service.FuelStationProcessor;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "fuelStations/:id", layout = MainView.class)
@ViewController(id = "optrou_FuelStation.detail")
@ViewDescriptor(path = "fuel-station-detail-view.xml")
@EditedEntityContainer("fuelStationDc")
public class FuelStationDetailView extends StandardDetailView<FuelStation> {

    @Autowired
    private FuelStationPriceProcessor fuelStationPriceProcessor;

    @ViewComponent
    private InstanceLoader<FuelStation> fuelStationDl;
    @ViewComponent
    private InstanceContainer<FuelStation> fuelStationDc;
    @ViewComponent
    private MapFragment mapFragment;

    @Subscribe
    public void onInit(final InitEvent event) {
        mapFragment.addVectorLayerWithDataVectorSource(fuelStationDc, "location");
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Point location = getEditedEntity().getLocation();

        if (location != null) {
            mapFragment.setCenter(new Coordinate(location.getCoordinate()));
            mapFragment.setZoom(10.0);
        }
    }

    @Subscribe("processFuelStationPrices")
    public void onProcessFuelStationPrices(final ActionPerformedEvent event) {
        fuelStationPriceProcessor.processFuelStationPricesByFuelStation(getEditedEntity());
        fuelStationDl.load();
    }
}