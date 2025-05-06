package com.svetkin.optrou.view.trip;

import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripFuelStation;
import com.svetkin.optrou.entity.TripPoint;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.SupportsTypedValue;
import io.jmix.flowui.component.datepicker.TypedDatePicker;
import io.jmix.flowui.exception.ValidationException;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.Install;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.mapsflowui.component.GeoMap;
import io.jmix.mapsflowui.component.model.layer.VectorLayer;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

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
    private DataContext dataContext;
    @Autowired
    private Notifications notifications;

    public void setTrip(Trip trip) {
        dataContext.clear();
        trip = dataContext.merge(trip);
        setEntityToEdit(trip);
        tripDc.setItem(trip);
    }

    @Subscribe
    public void onInit(final InitEvent event) {
        mapFragment.addVectorLayerWithDataVectorSource(tripDc, "line");
        mapFragment.addVectorLayerWithDataVectorSource(controlPointsDc, "location");
        mapFragment.addVectorLayerWithDataVectorSource(tripFuelStationsDc, "fuelStation.location");
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        LineString routeLine = getEditedEntity().getLine();
        if (routeLine != null) {
            setMapCenterByLine(routeLine);
        }
    }

    private void setMapCenterByLine(LineString line) {
        mapFragment.setCenter(new Coordinate(line.getCoordinateN(0)));
        mapFragment.setZoom(10.0);
    }

    @Install(to = "dateStartField", subject = "validator")
    private void dateStartFieldValidator(final LocalDate value) {
        LocalDate factDateEnd = getEditedEntity().getFactDateEnd();
        LocalDate planningDateEnd = getEditedEntity().getPlanningDateEnd();
        if (value != null && factDateEnd != null && value.isAfter(factDateEnd)) {
            throw new ValidationException("Дата начала не должна быть позже даты окончания");
        }
        if (value != null && planningDateEnd != null && value.isAfter(planningDateEnd)) {
            throw new ValidationException("Дата начала не должна быть позже даты окончания");
        }
    }

    @Install(to = "factDateEndField", subject = "validator")
    private void factDateEndFieldValidator(final LocalDate value) {
        endDateValidator(value);
    }

    @Install(to = "planningDateEndField", subject = "validator")
    private void planningDateEndFieldValidator(final LocalDate value) {
        endDateValidator(value);
    }

    private void endDateValidator(LocalDate value) {
        LocalDate startDate = getEditedEntity().getDateStart();
        if (value != null && startDate != null && value.isBefore(startDate)) {
            throw new ValidationException("Дата окончания должна быть позже даты начала");
        }
    }
}