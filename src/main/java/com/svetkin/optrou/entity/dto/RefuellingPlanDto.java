package com.svetkin.optrou.entity.dto;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.RefuellingPlan;
import com.svetkin.optrou.entity.type.RefuellingPlanCreateStatus;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;
import jakarta.validation.constraints.NotNull;

@JmixEntity(name = "optrou_RefuellingPlanDto")
public class RefuellingPlanDto {

    @JmixProperty(mandatory = true)
    @NotNull
    private String status;

    @JmixProperty(mandatory = true)
    @NotNull
    private RefuellingPlan refuellingPlan;

    private FuelStation lastFuelStation;

    public void setLastFuelStation(FuelStation lastFuelStation) {
        this.lastFuelStation = lastFuelStation;
    }

    public FuelStation getLastFuelStation() {
        return lastFuelStation;
    }

    public RefuellingPlanCreateStatus getStatus() {
        return status == null ? null : RefuellingPlanCreateStatus.fromId(status);
    }

    public void setStatus(RefuellingPlanCreateStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public RefuellingPlan getRefuellingPlan() {
        return refuellingPlan;
    }

    public void setRefuellingPlan(RefuellingPlan refuellingPlan) {
        this.refuellingPlan = refuellingPlan;
    }
}