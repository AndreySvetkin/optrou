package com.svetkin.optrou.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.time.LocalDateTime;

@JmixEntity(name = "optrou_GlonassSoftVehicleLocationRequestDto")
public class GlonassSoftVehicleLocationRequestDto {

    private String vehicleId;

    @JsonProperty("from")
    private LocalDateTime startDate;

    @JsonProperty("to")
    private LocalDateTime endDate;

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

}