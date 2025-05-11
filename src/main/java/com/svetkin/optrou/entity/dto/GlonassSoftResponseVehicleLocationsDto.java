package com.svetkin.optrou.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.List;

@JmixEntity(name = "optrou_GlonassSoftResponseVehicleLocationsDto")
public class GlonassSoftResponseVehicleLocationsDto {

    @JsonProperty("messages")
    private List<GlonassSoftVehicleLocationDto> locations;

    public void setLocations(List<GlonassSoftVehicleLocationDto> locations) {
        this.locations = locations;
    }

    public List<GlonassSoftVehicleLocationDto> getLocations() {
        return locations;
    }

}