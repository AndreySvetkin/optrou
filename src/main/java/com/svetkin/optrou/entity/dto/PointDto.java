package com.svetkin.optrou.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity(name = "optrou_PointDto")
public class PointDto {

    @JsonProperty("Lat")
    private Double latitude;

    @JsonProperty("Lon")
    private Double longitude;

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}