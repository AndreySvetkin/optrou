package com.svetkin.optrou.entity.dto;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import org.locationtech.jts.geom.Point;

import java.util.UUID;

@JmixEntity(name = "optrou_FuelStationDto")
public class FuelStationDto {
    @JmixGeneratedValue
    @JmixId
    private UUID id;

    private String stationId;

    @InstanceName
    private String name;

    private String brand;

    private String address;

    private Boolean isGoods;

    private Point location;

    public void setIsGoods(Boolean isGoods) {
        this.isGoods = isGoods;
    }

    public Boolean getIsGoods() {
        return isGoods;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Point getLocation() {
        return location;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}