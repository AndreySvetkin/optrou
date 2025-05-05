package com.svetkin.optrou.view.trip;

import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripFuelStation;
import com.svetkin.optrou.entity.TripPoint;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.mapsflowui.component.GeoMap;
import io.jmix.mapsflowui.component.model.layer.VectorLayer;

@Route(value = "trips/:id", layout = MainView.class)
@ViewController(id = "optrou_Trip.detail")
@ViewDescriptor(path = "trip-detail-view.xml")
@EditedEntityContainer("tripDc")
public class TripDetailView extends StandardDetailView<Trip> {

    @ViewComponent
    private InstanceContainer<Trip> tripDc;
    @ViewComponent
    private CollectionPropertyContainer<TripPoint> controlPointsDc;
    @ViewComponent
    private MapFragment mapFragment;
    @ViewComponent
    private CollectionPropertyContainer<TripFuelStation> tripFuelStationsDc;
    @ViewComponent
    private InstanceLoader<Trip> tripDl;

    public void setTrip(Trip trip) {
        setupEntityToEdit(trip);
        tripDl.load();
    }

    @Subscribe
    public void onInit(final InitEvent event) {
        mapFragment.addVectorLayerWithDataVectorSource(tripDc, "line");
        mapFragment.addVectorLayerWithDataVectorSource(controlPointsDc, "location");
        mapFragment.addVectorLayerWithDataVectorSource(tripFuelStationsDc, "fuelStation.location");
    }


}