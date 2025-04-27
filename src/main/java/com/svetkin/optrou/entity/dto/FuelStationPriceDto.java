package com.svetkin.optrou.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity(name = "optrou_FuelStationPriceDto")
public class FuelStationPriceDto {

    private String stationId;

    @JsonProperty("productId")
    private String fuelTypeId;

    private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getFuelTypeId() {
        return fuelTypeId;
    }

    public void setFuelTypeId(String fuelTypeId) {
        this.fuelTypeId = fuelTypeId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}