package com.svetkin.optrou.view.vehicle;

import com.svetkin.optrou.entity.Vehicle;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

@Route(value = "vehicles/:id", layout = MainView.class)
@ViewController(id = "optrou_Vehicle.detail")
@ViewDescriptor(path = "vehicle-detail-view.xml")
@EditedEntityContainer("vehicleDc")
public class VehicleDetailView extends StandardDetailView<Vehicle> {

    @ViewComponent
    private MapFragment mapFragment;
    @ViewComponent
    private InstanceContainer<Vehicle> vehicleDc;

    @Subscribe
    public void onInit(final InitEvent event) {
        mapFragment.addVectorLayerWithDataVectorSource(vehicleDc, "location");
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Point location = getEditedEntity().getLocation();

        if (location != null) {
            mapFragment.setCenter(new Coordinate(location.getCoordinate()));
            mapFragment.setZoom(10.0);
        }
    }
}