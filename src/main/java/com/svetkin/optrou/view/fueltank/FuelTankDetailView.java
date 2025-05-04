package com.svetkin.optrou.view.fueltank;

import com.svetkin.optrou.entity.FuelTank;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "fuelTanks/:id", layout = MainView.class)
@ViewController(id = "optrou_FuelTank.detail")
@ViewDescriptor(path = "fuel-tank-detail-view.xml")
@EditedEntityContainer("fuelTankDc")
public class FuelTankDetailView extends StandardDetailView<FuelTank> {
}