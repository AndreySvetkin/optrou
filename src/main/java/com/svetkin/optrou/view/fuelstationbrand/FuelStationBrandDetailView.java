package com.svetkin.optrou.view.fuelstationbrand;

import com.svetkin.optrou.entity.FuelStationBrand;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "fuelStationBrands/:id", layout = MainView.class)
@ViewController(id = "optrou_FuelStationBrand.detail")
@ViewDescriptor(path = "fuel-station-brand-detail-view.xml")
@EditedEntityContainer("fuelStationBrandDc")
public class FuelStationBrandDetailView extends StandardDetailView<FuelStationBrand> {
}