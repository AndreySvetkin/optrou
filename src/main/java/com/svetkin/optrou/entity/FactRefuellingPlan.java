package com.svetkin.optrou.entity;

import com.svetkin.optrou.entity.type.FuelType;
import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;
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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "OPTROU_FACT_REFUELLING_PLAN", indexes = {
        @Index(name = "IDX_OPTROU_FACT_REFUELLING_PLAN_TRIP", columnList = "TRIP_ID")
})
@Entity(name = "optrou_FactRefuellingPlan")
public class FactRefuellingPlan {
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

    @InstanceName
    @CreatedDate
    @Column(name = "CREATED_DATE")
    private OffsetDateTime createdDate;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "TRIP_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TripReport trip;

    @Column(name = "FUEL_TYPE", nullable = false)
    @NotNull
    private Integer fuelType;

    @Composition
    @OneToMany(mappedBy = "factRefuellingPlan")
    private List<FactRefuelling> refuellings;

    public void setRefuellings(List<FactRefuelling> refuellings) {
        this.refuellings = refuellings;
    }

    public List<FactRefuelling> getRefuellings() {
        return refuellings;
    }

    public void setTrip(TripReport trip) {
        this.trip = trip;
    }

    public TripReport getTrip() {
        return trip;
    }

    public FuelType getFuelType() {
        return fuelType == null ? null : FuelType.fromId(fuelType);
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType == null ? null : fuelType.getId();
    }

    @DependsOnProperties({"refuellings"})
    @JmixProperty
    public Double getRefuellingsVolume() {
        return refuellings.stream()
                .mapToDouble(FactRefuelling::getVolume)
                .sum();
    }

    @DependsOnProperties({"refuellings"})
    @JmixProperty
    public Long getRefuellingsSize() {
        return (long) refuellings.size();
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