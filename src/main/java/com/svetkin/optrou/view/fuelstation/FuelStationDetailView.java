package com.svetkin.optrou.view.fuelstation;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.service.FuelStationIntegrationProcessor;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "fuelStations/:id", layout = MainView.class)
@ViewController(id = "optrou_FuelStation.detail")
@ViewDescriptor(path = "fuel-station-detail-view.xml")
@EditedEntityContainer("fuelStationDc")
public class FuelStationDetailView extends StandardDetailView<FuelStation> {

    @Autowired
    private FuelStationIntegrationProcessor fuelStationIntegrationProcessor;

    @ViewComponent
    private InstanceLoader<FuelStation> fuelStationDl;

    @Subscribe("fuelStationPricesDataGrid.refresh")
    public void onFuelStationPricesDataGridRefresh(final ActionPerformedEvent event) {
        fuelStationIntegrationProcessor.processFuelStationPricesByFuelStation(getEditedEntity());
        fuelStationDl.load();
    }
}