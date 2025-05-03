package com.svetkin.optrou.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity(name = "optrou_FuelStationDto")
public class FuelStationDto {

    private String stationId;

    @InstanceName
    private String name;

    private String brand;

    private Boolean isGoods;

    @JsonProperty("city")
    private String region;

    private String address;

    private PointDto location;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setLocation(PointDto location) {
        this.location = location;
    }

    public PointDto getLocation() {
        return location;
    }

    public void setIsGoods(Boolean isGoods) {
        this.isGoods = isGoods;
    }

    public Boolean getIsGoods() {
        return isGoods;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

}