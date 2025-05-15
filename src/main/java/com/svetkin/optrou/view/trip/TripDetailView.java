package com.svetkin.optrou.view.trip;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.RefuellingPlan;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripFuelStation;
import com.svetkin.optrou.entity.TripPoint;
import com.svetkin.optrou.entity.dto.RefuellingPlanDto;
import com.svetkin.optrou.entity.type.RefuellingPlanCreateStatus;
import com.svetkin.optrou.entity.type.TripStatus;
import com.svetkin.optrou.service.RefuellingPlanCreateService;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.select.JmixSelect;
import io.jmix.flowui.exception.ValidationException;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.action.BaseAction;
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
import io.jmix.mapsflowui.component.model.layer.VectorLayer;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;

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
    @ViewComponent
    private JmixSelect<TripStatus> statusField;
    @ViewComponent
    private BaseAction toProgressAction;
    @ViewComponent
    private BaseAction cancelAction;
    @ViewComponent
    private BaseAction approveAction;

    public void setTrip(Trip trip) {
        statusField.setValue(TripStatus.NEW);
        statusField.setReadOnly(true);
        dataContext.clear();
        trip = dataContext.merge(trip);
        tripDc.setItem(trip);
    }

    @Subscribe
    public void onAfterSave(final AfterSaveEvent event) {
        statusField.setReadOnly(false);
    }

    @Subscribe
    public void onInit(final InitEvent event) {
        VectorLayer routeVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(tripDc, "line");
        VectorLayer controlPointsVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(controlPointsDc, "location");
        VectorLayer fuelStationsVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(tripFuelStationsDc, "fuelStation.location");
        VectorLayer vehicleVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(tripDc, "vehicle.location");

        mapFragment.setLineStringStyleProvider(routeVectorLayer);
        mapFragment.setControlPointStyleProvider(controlPointsVectorLayer);
        mapFragment.setFuelStationStyleProvider(fuelStationsVectorLayer);
        mapFragment.setVehiclesStyleProvider(vehicleVectorLayer);
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Trip editedEntity = getEditedEntity();
        onChangeStatus(editedEntity.getStatus());

        LineString routeLine = editedEntity.getLine();
        if (routeLine != null) {
            setMapCenterByLine(routeLine);
        }
    }

    private void setMapCenterByLine(LineString line) {
        mapFragment.setCenter(new Coordinate(line.getCoordinateN(0)));
        mapFragment.setZoom(10.0);
    }

    @Install(to = "planningDateTimeStartField", subject = "validator")
    private void planningDateTimeStartFieldValidator(final LocalDateTime value) {
        dateTimeValidator(value, getEditedEntity().getPlanningDateEnd());
    }

    @Install(to = "planningDateTimeEndField", subject = "validator")
    private void planningDateTimeEndFieldValidator(final LocalDateTime value) {
        dateTimeValidator(getEditedEntity().getPlanningDateStart(), value);
    }

    @Install(to = "factDateTimeStartField", subject = "validator")
    private void factDateTimeStartFieldValidator(final LocalDateTime value) {
        dateTimeValidator(value, getEditedEntity().getFactDateEnd());
    }

    @Install(to = "factDateTimeEndField", subject = "validator")
    private void factDateTimeEndFieldValidator(final LocalDateTime value) {
        dateTimeValidator(getEditedEntity().getFactDateStart(), value);
    }

    private void dateTimeValidator(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return;
        }

        if (startDateTime.isAfter(endDateTime)) {
            throw new ValidationException("Дата окончания должна быть позже даты начала");
        }
    }

    @Subscribe("toProgressAction")
    public void onToProgressAction(final ActionPerformedEvent event) {
        getEditedEntity().setStatus(TripStatus.IN_PROGRESS);
    }

    @Subscribe("approveAction")
    public void onApproveAction(final ActionPerformedEvent event) {
        getEditedEntity().setStatus(TripStatus.DONE);
    }

    @Subscribe("cancelAction")
    public void onCancelAction(final ActionPerformedEvent event) {
        getEditedEntity().setStatus(TripStatus.CANCELLED);
    }

    @Subscribe("statusField")
    public void onStatusFieldComponentValueChange(final AbstractField.ComponentValueChangeEvent<JmixSelect<TripStatus>, TripStatus> event) {
        onChangeStatus(event.getValue());
    }

    private void onChangeStatus(TripStatus status) {
        toProgressAction.setVisible(false);
        approveAction.setVisible(false);
        cancelAction.setVisible(false);

        if (status == TripStatus.NEW) {
            toProgressAction.setVisible(true);
            cancelAction.setVisible(true);
        }

        if (status == TripStatus.IN_PROGRESS) {
            approveAction.setVisible(true);
            cancelAction.setVisible(true);
        }

        if (status == TripStatus.DONE || status == TripStatus.CANCELLED) {
            setReadOnly(true);
        };
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