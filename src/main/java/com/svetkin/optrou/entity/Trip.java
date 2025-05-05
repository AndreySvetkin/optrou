package com.svetkin.optrou.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.annotation.DeletedBy;
import io.jmix.core.annotation.DeletedDate;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDelete;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
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

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

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

    @InstanceName
    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @Column(name = "DATE_START", nullable = false)
    @NotNull
    private LocalDate dateStart;

    @Column(name = "PLANNING_DATE_END", nullable = false)
    @NotNull
    private LocalDate planningDateEnd;

    @Column(name = "FACT_DATE_END")
    private LocalDate factDateEnd;

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
    private List<TripPoint> controlPoints;

    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    @OneToMany(mappedBy = "trip")
    private List<TripFuelStation> fuelStations;

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
    private List<RefuellingPlan> refuellingPlans;

    public List<TripFuelStation> getFuelStations() {
        return fuelStations;
    }

    public void setFuelStations(List<TripFuelStation> fuelStations) {
        this.fuelStations = fuelStations;
    }

    public List<TripPoint> getControlPoints() {
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

    public LocalDate getFactDateEnd() {
        return factDateEnd;
    }

    public void setFactDateEnd(LocalDate factDateEnd) {
        this.factDateEnd = factDateEnd;
    }

    public LocalDate getPlanningDateEnd() {
        return planningDateEnd;
    }

    public void setPlanningDateEnd(LocalDate planningDateEnd) {
        this.planningDateEnd = planningDateEnd;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
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
}