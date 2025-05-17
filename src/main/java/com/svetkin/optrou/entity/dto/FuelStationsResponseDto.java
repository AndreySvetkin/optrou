package com.svetkin.optrou.entity.dto;

import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.List;

@JmixEntity(name = "optrou_FuelStationsResponseDto")
public class FuelStationsResponseDto {

    private List<FuelStationDto> fuelStations;

    public List<FuelStationDto> getFuelStations() {
        return fuelStations;
    }

    public void setFuelStations(List<FuelStationDto> fuelStations) {
        this.fuelStations = fuelStations;
    }
}