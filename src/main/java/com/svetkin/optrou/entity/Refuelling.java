package com.svetkin.optrou.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.UUID;

@JmixEntity
@Table(name = "OPTROU_REFUELLING", indexes = {
        @Index(name = "IDX_OPTROU_REFUELLING_REFUELLING_PLAN", columnList = "REFUELLING_PLAN_ID"),
        @Index(name = "IDX_OPTROU_REFUELLING_FUEL_STATION_PRICE", columnList = "FUEL_STATION_PRICE_ID")
})
@Entity(name = "optrou_Refuelling")
public class Refuelling {
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

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "REFUELLING_PLAN_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private RefuellingPlan refuellingPlan;

    @JoinColumn(name = "FUEL_STATION_PRICE_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private FuelStationPrice fuelStationPrice;

    @Column(name = "VOLUME", nullable = false)
    @NotNull
    private Double volume;

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public FuelStationPrice getFuelStationPrice() {
        return fuelStationPrice;
    }

    public void setFuelStationPrice(FuelStationPrice fuelStationPrice) {
        this.fuelStationPrice = fuelStationPrice;
    }

    public RefuellingPlan getRefuellingPlan() {
        return refuellingPlan;
    }

    public void setRefuellingPlan(RefuellingPlan refuellingPlan) {
        this.refuellingPlan = refuellingPlan;
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