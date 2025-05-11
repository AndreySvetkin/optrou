package com.svetkin.optrou.view.trip;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.RefuellingPlan;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripFuelStation;
import com.svetkin.optrou.entity.TripPoint;
import com.svetkin.optrou.entity.dto.RefuellingPlanDto;
import com.svetkin.optrou.entity.type.RefuellingPlanCreateStatus;
import com.svetkin.optrou.service.RefuellingPlanCreateService;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.SupportsTypedValue;
import io.jmix.flowui.component.datepicker.TypedDatePicker;
import io.jmix.flowui.component.datetimepicker.TypedDateTimePicker;
import io.jmix.flowui.exception.ValidationException;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.Install;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Route(value = "trips/:id", layout = MainView.class)
@ViewController(id = "optrou_Trip.detail")
@ViewDescriptor(path = "trip-detail-view.xml")
@EditedEntityContainer("tripDc")
public class TripDetailView extends StandardDetailView<Trip> {

    @Autowired
    private Notifications notifications;
    @Autowired
    private RefuellingPlanCreateService refuellingPlanCreateService;

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
    @ViewComponent
    private CollectionPropertyContainer<RefuellingPlan> refuellingPlansDc;

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

    @Subscribe("planningDateTimeStartField")
    public void onPlanningDateTimeStartFieldTypedValueChange(final SupportsTypedValue.TypedValueChangeEvent<TypedDateTimePicker<LocalDateTime>, LocalDateTime> event) {
        dateTimeValidator(event.getValue(), getEditedEntity().getPlanningDateEnd());
    }

    @Subscribe("planningDateTimeEndField")
    public void onPlanningDateTimeEndFieldComponentValueChange(final SupportsTypedValue.TypedValueChangeEvent<TypedDateTimePicker<LocalDateTime>, LocalDateTime> event) {
        dateTimeValidator(getEditedEntity().getPlanningDateStart(), event.getValue());
    }

    @Subscribe("factDateTimeStartField")
    public void onFactDateTimeStartFieldTypedValueChange(final SupportsTypedValue.TypedValueChangeEvent<TypedDateTimePicker<LocalDateTime>, LocalDateTime> event) {
        dateTimeValidator(event.getValue(), getEditedEntity().getFactDateEnd());
    }

    @Subscribe("factDateTimeEndField")
    public void onFactDateTimeEndFieldTypedValueChange(final SupportsTypedValue.TypedValueChangeEvent<TypedDateTimePicker<LocalDateTime>, LocalDateTime> event) {
        dateTimeValidator(getEditedEntity().getFactDateStart(), event.getValue());
    }

    private void dateTimeValidator(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return;
        }

        if (startDateTime.isBefore(endDateTime)) {
            throw new ValidationException("Дата окончания должна быть позже даты начала");
        }
    }

    @Subscribe("refuellingPlansDataGrid.create")
    public void onRefuellingPlansDataGridCreate(final ActionPerformedEvent event) {
        Trip editedEntity = getEditedEntity();

        if (!validateCreatingRefuellingPlanTrip(editedEntity)) {
            return;
        }

        RefuellingPlanDto refuellingPlanDto = refuellingPlanCreateService.createRefuellingPlan(editedEntity);

        if (refuellingPlanDto.getStatus() == RefuellingPlanCreateStatus.NEXT_STATION_OR_END_ROUTE_FAR_AWAY) {
            FuelStation lastFuelStation = refuellingPlanDto.getLastFuelStation();
            String lastFuelStationString = lastFuelStation != null
                    ? lastFuelStation.getName() + lastFuelStation.getBrand()
                    : "Отсутсвует";

            notifications.create("Не удалось доехать до следующей АЗС или конца пути. Последняя АЗС %s".formatted(lastFuelStationString))
                    .withCloseable(true)
                    .build()
                    .open();

            return;
        }

        if (refuellingPlanDto.getStatus() == RefuellingPlanCreateStatus.IS_EMPTY_FUEL_STATIONS_WITH_FUEL_TYPE) {
            notifications.create("Не удалось построить план заправок. Нет АЗС с типом топлива %s".formatted(editedEntity.getVehicle().getFuelType()))
                    .withCloseable(true)
                    .build()
                    .open();

            return;
        }

        RefuellingPlan refuellingPlan = refuellingPlanDto.getRefuellingPlan();
        refuellingPlan = dataContext.merge(refuellingPlan);
        refuellingPlansDc.getMutableItems().add(refuellingPlan);

        notifications.create("План заправок построен успешно. Количество дозаправок %s".formatted(refuellingPlan.getRefuellings().size()))
                .withCloseable(true)
                .build()
                .open();
    }

    private boolean validateCreatingRefuellingPlanTrip(Trip trip) {
        if (trip.getRoute() == null) {
            notifications.create("Укажите маршрут")
                    .build()
                    .open();

            return false;
        }

        if (trip.getVehicle() == null) {

            notifications.create("Укажите автомобиль")
                    .build()
                    .open();

            return false;
        }

        return true;
    }
}