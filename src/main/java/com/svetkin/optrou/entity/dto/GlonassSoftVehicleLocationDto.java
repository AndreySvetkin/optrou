package com.svetkin.optrou.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.time.LocalDateTime;

@JmixEntity(name = "optrou_GlonassSoftRefuellingDto")
public class GlonassSoftVehicleLocationDto {

    @JsonProperty("deviceTime")
    private LocalDateTime deviceDateTime;

    private Double longitude;

    private Double latitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getDeviceDateTime() {
        return deviceDateTime;
    }

    public void setDeviceDateTime(LocalDateTime deviceDateTime) {
        this.deviceDateTime = deviceDateTime;
    }

}