package com.svetkin.optrou.view.fuelstation;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "fuelStations/:id", layout = MainView.class)
@ViewController(id = "optrou_FuelStation.detail")
@ViewDescriptor(path = "fuel-station-detail-view.xml")
@EditedEntityContainer("fuelStationDc")
public class FuelStationDetailView extends StandardDetailView<FuelStation> {
}