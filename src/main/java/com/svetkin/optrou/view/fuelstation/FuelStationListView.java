package com.svetkin.optrou.view.fuelstation;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.service.FuelStationIntegrationProcessor;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "fuelStations", layout = MainView.class)
@ViewController(id = "optrou_FuelStation.list")
@ViewDescriptor(path = "fuel-station-list-view.xml")
@LookupComponent("fuelStationsDataGrid")
@DialogMode(width = "64em")
public class FuelStationListView extends StandardListView<FuelStation> {

    @Autowired
    private FuelStationIntegrationProcessor fuelStationIntegrationProcessor;
    @ViewComponent
    private CollectionLoader<FuelStation> fuelStationsDl;

    @Subscribe("fuelStationsDataGrid.refresh")
    public void onFuelStationsDataGridRefresh(final ActionPerformedEvent event) {
        fuelStationIntegrationProcessor.processFuelStations();
        fuelStationsDl.load();
    }


}