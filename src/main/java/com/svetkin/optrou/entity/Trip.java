package com.svetkin.optrou.entity;

import com.svetkin.optrou.entity.type.TripStatus;
import io.jmix.core.DeletePolicy;
import io.jmix.core.MetadataTools;
import io.jmix.core.annotation.DeletedBy;
import io.jmix.core.annotation.DeletedDate;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDelete;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.datatype.DatatypeFormatter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@JmixEntity
@Table(name = "OPTROU_TRIP", indexes = {
        @Index(name = "IDX_OPTROU_TRIP_ROUTE", columnList = "ROUTE_ID"),
        @Index(name = "IDX_OPTROU_TRIP_LOGIST", columnList = "LOGIST_ID")
})
@Entity(name = "optrou_Trip")
public class Trip {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private OffsetDateTime createdDate;

    @DeletedBy
    @Column(name = "DELETED_BY")
    private String deletedBy;

    @DeletedDate
    @Column(name = "DELETED_DATE")
    private OffsetDateTime deletedDate;

    @Column(name = "NUMBER_", nullable = false)
    @NotNull
    private Long number;

    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @Column(name = "STATUS", nullable = false)
    @NotNull
    private String status = TripStatus.NEW.getId();

    @NotNull
    @Column(name = "PLANNING_DATE_START", nullable = false)
    private LocalDateTime planningDateStart;

    @NotNull
    @Column(name = "PLANNING_DATE_END", nullable = false)
    private LocalDateTime planningDateEnd;

    @Column(name = "FACT_DATE_START")
    private LocalDateTime factDateStart;

    @Column(name = "FACT_DATE_END")
    private LocalDateTime factDateEnd;

    @JoinColumn(name = "ROUTE_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Route route;

    @NotNull
    @Column(name = "DISTANCE", nullable = false)
    private Double length;

    @Column(name = "LINE", nullable = false)
    @NotNull
    private LineString line;

    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    @OneToMany(mappedBy = "trip")
    private List<TripPoint> controlPoints = new ArrayList<>();

    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    @OneToMany(mappedBy = "trip")
    private List<TripFuelStation> fuelStations = new ArrayList<>();

    @JoinColumn(name = "DRIVER_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Driver driver;

    @JoinColumn(name = "LOGIST_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User logist;

    @JoinColumn(name = "VEHICLE_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Vehicle vehicle;

    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    @OneToMany(mappedBy = "trip")
    private List<RefuellingPlan> refuellingPlans = new ArrayList<>();

    public TripStatus getStatus() {
        return status == null ? null : TripStatus.fromId(status);
    }

    public void setStatus(TripStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public void setPlanningDateStart(LocalDateTime planningDateStart) {
        this.planningDateStart = planningDateStart;
    }

    public LocalDateTime getPlanningDateStart() {
        return planningDateStart;
    }

    public void setPlanningDateEnd(LocalDateTime planningDateEnd) {
        this.planningDateEnd = planningDateEnd;
    }

    public LocalDateTime getPlanningDateEnd() {
        return planningDateEnd;
    }

    public void setFactDateEnd(LocalDateTime factDateEnd) {
        this.factDateEnd = factDateEnd;
    }

    public LocalDateTime getFactDateEnd() {
        return factDateEnd;
    }

    public LocalDateTime getFactDateStart() {
        return factDateStart;
    }

    public void setFactDateStart(LocalDateTime factDateStart) {
        this.factDateStart = factDateStart;
    }

    public List<TripFuelStation> getFuelStations() {
        fuelStations.sort(Comparator.comparing(TripFuelStation::getDistance));
        return fuelStations;
    }

    public void setFuelStations(List<TripFuelStation> fuelStations) {
        this.fuelStations = fuelStations;
    }

    public List<TripPoint> getControlPoints() {
        controlPoints.sort(Comparator.comparing(TripPoint::getOrder));
        return controlPoints;
    }

    public void setControlPoints(List<TripPoint> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public LineString getLine() {
        return line;
    }

    public void setLine(LineString line) {
        this.line = line;
    }

    public User getLogist() {
        return logist;
    }

    public void setLogist(User logist) {
        this.logist = logist;
    }

    public List<RefuellingPlan> getRefuellingPlans() {
        return refuellingPlans;
    }

    public void setRefuellingPlans(List<RefuellingPlan> refuellingPlans) {
        this.refuellingPlans = refuellingPlans;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OffsetDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(OffsetDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @InstanceName
    @DependsOnProperties({"number", "name"})
    public String getInstanceName(MetadataTools metadataTools, DatatypeFormatter datatypeFormatter) {
        return String.format("%s %s",
                datatypeFormatter.formatLong(number),
                metadataTools.format(name));
    }

    public static Function<Object, String> getPointTooltipTextProviderFunction() {
        return object -> {
            TripPoint point = (TripPoint) object;
            if (point.getName() == null || point.getLocation() == null) {
                return "";
            }

            return point.getName();
        };
    }

    public static Function<Object, String> getFuelStationTooltipTextProviderFunction() {
        return object -> {
            TripFuelStation tripFuelStation = (TripFuelStation)  object;
            if (tripFuelStation.getFuelStation() == null) {
                return "";
            }

            FuelStation fuelStation = tripFuelStation.getFuelStation();

            if (fuelStation.getName() == null || fuelStation.getBrand() == null || fuelStation.getLocation() == null) {
                return "";
            }

            return fuelStation.getName() + " " + fuelStation.getBrand().getName() + " " + tripFuelStation.getDistance();
        };
    }

    public static Function<Object, String> getLineTooltipTextProviderFunction() {
        return object -> {
            Trip trip = (Trip) object;
            if (trip.getName() == null || trip.getLine() == null) {
                return "";
            }

            return trip.getName() + " " + trip.getNumber() + " Длина: " + trip.getLength();
        };
    }

    public static Function<Object, String> getVehiclePointTooltipTextProviderFunction() {
        return object -> {
            Trip trip = (Trip) object;
            Vehicle vehicle = trip.getVehicle();
            if (vehicle.getModel() == null || vehicle.getLicensePlate() == null) {
                return "";
            }

            return vehicle.getModel() + " " + vehicle.getLicensePlate();
        };
    }
}