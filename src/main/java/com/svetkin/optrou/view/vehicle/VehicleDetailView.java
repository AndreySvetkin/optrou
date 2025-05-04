package com.svetkin.optrou.view.vehicle;

import com.svetkin.optrou.entity.Vehicle;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "vehicles/:id", layout = MainView.class)
@ViewController(id = "optrou_Vehicle.detail")
@ViewDescriptor(path = "vehicle-detail-view.xml")
@EditedEntityContainer("vehicleDc")
public class VehicleDetailView extends StandardDetailView<Vehicle> {
}