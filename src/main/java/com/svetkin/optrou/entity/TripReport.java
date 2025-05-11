package com.svetkin.optrou.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@JmixEntity
@Table(name = "OPTROU_TRIP_REPORT", indexes = {
        @Index(name = "IDX_OPTROU_TRIP_REPORT_ROUTE", columnList = "ROUTE_ID"),
        @Index(name = "IDX_OPTROU_TRIP_REPORT_LOGIST", columnList = "LOGIST_ID"),
        @Index(name = "IDX_OPTROU_TRIP_REPORT_FACT_REFUELLING_PLAN", columnList = "FACT_REFUELLING_PLAN_ID"),
        @Index(name = "IDX_OPTROU_TRIP_REPORT_REFUELLING_PLANS", columnList = "REFUELLING_PLANS_ID"),
        @Index(name = "IDX_OPTROU_TRIP_REPORT_TRIP", columnList = "TRIP_ID")
})
@Entity(name = "optrou_TripReport")
public class TripReport {
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

    @JoinColumn(name = "TRIP_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Trip trip;

    @InstanceName
    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

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

    @Column(name = "FACT_LENGTH")
    private Double factLength;

    @Column(name = "LINE", nullable = false)
    @NotNull
    private LineString line;

    @Column(name = "FACT_LINE")
    private String factLine;

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

    @JoinColumn(name = "REFUELLING_PLANS_ID")
    @OneToOne(fetch = FetchType.LAZY)
    @Composition
    private RefuellingPlan refuellingPlan;

    @Composition
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACT_REFUELLING_PLAN_ID")
    private FactRefuellingPlan factRefuellingPlan;

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public void setRefuellingPlan(RefuellingPlan refuellingPlan) {
        this.refuellingPlan = refuellingPlan;
    }

    public RefuellingPlan getRefuellingPlan() {
        return refuellingPlan;
    }

    public FactRefuellingPlan getFactRefuellingPlan() {
        return factRefuellingPlan;
    }

    public void setFactRefuellingPlan(FactRefuellingPlan factRefuellingPlan) {
        this.factRefuellingPlan = factRefuellingPlan;
    }

    public String getFactLine() {
        return factLine;
    }

    public void setFactLine(String factLine) {
        this.factLine = factLine;
    }

    public Double getFactLength() {
        return factLength;
    }

    public void setFactLength(Double factLength) {
        this.factLength = factLength;
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