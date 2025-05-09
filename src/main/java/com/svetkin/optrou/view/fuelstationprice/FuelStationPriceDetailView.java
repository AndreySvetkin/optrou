package com.svetkin.optrou.view.fuelstationprice;

import com.svetkin.optrou.entity.FuelStationPrice;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@ViewController(id = "optrou_FuelStationPrice.detail")
@ViewDescriptor(path = "fuel-station-price-detail-view.xml")
@EditedEntityContainer("fuelStationPriceDc")
@DialogMode(width = "30em")
public class FuelStationPriceDetailView extends StandardDetailView<FuelStationPrice> {
}