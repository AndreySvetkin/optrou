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
@Table(name = "OPTROU_FACT_REFUELLING", indexes = {
        @Index(name = "IDX_OPTROU_FACT_REFUELLING_FACT_REFUELLING_PLAN", columnList = "FACT_REFUELLING_PLAN_ID")
})
@Entity(name = "optrou_FactRefuelling")
public class FactRefuelling {
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

    @Column(name = "VOLUME", nullable = false)
    @NotNull
    private Double volume;

    @Column(name = "BEFORE_LEVEL_FUEL")
    private Double beforeLevelFuel;

    @Column(name = "AFTER_LEVEL_FUEL")
    private Double afterLevelFuel;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "FACT_REFUELLING_PLAN_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private FactRefuellingPlan factRefuellingPlan;

    public Double getAfterLevelFuel() {
        return afterLevelFuel;
    }

    public void setAfterLevelFuel(Double afterLevelFuel) {
        this.afterLevelFuel = afterLevelFuel;
    }

    public Double getBeforeLevelFuel() {
        return beforeLevelFuel;
    }

    public void setBeforeLevelFuel(Double beforeLevelFuel) {
        this.beforeLevelFuel = beforeLevelFuel;
    }

    public FactRefuellingPlan getFactRefuellingPlan() {
        return factRefuellingPlan;
    }

    public void setFactRefuellingPlan(FactRefuellingPlan factRefuellingPlan) {
        this.factRefuellingPlan = factRefuellingPlan;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
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