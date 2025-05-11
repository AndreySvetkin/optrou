package com.svetkin.optrou.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JmixEntity(name = "optrou_GlonassSoftVehicleInfoRequestDto")
public class GlonassSoftVehicleInfoRequestDto {

    private List<String> vehicleIds;

    @JsonProperty("from")
    private LocalDateTime startDate;

    @JsonProperty("to")
    private LocalDateTime endDate;

    private String timezone;

    public void setVehicleIds(List<String> vehicleIds) {
        this.vehicleIds = vehicleIds;
    }

    public List<String> getVehicleIds() {
        return vehicleIds;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
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