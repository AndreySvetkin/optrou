package com.svetkin.optrou.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.List;

@JmixEntity(name = "optrou_GlonassSoftResponseVehicleLocationsDto")
public class GlonassSoftResponseVehicleLocationsDto {

    @JsonProperty("messages")
    private List<GlonassSoftRefuellingDto> locations;

    public List<GlonassSoftRefuellingDto> getLocations() {
        return locations;
    }

    public void setLocations(List<GlonassSoftRefuellingDto> locations) {
        this.locations = locations;
    }
}