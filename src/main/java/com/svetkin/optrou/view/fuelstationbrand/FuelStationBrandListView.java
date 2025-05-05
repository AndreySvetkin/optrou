package com.svetkin.optrou.view.fuelstationbrand;

import com.svetkin.optrou.entity.FuelStationBrand;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


@Route(value = "fuelStationBrands", layout = MainView.class)
@ViewController(id = "optrou_FuelStationBrand.list")
@ViewDescriptor(path = "fuel-station-brand-list-view.xml")
@LookupComponent("fuelStationBrandsDataGrid")
@DialogMode(width = "64em")
public class FuelStationBrandListView extends StandardListView<FuelStationBrand> {
}