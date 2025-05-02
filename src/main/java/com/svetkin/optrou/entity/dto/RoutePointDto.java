package com.svetkin.optrou.entity.dto;

import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.List;

@JmixEntity(name = "optrou_RoutePointDto")
public class RoutePointDto {

    private String name;

    private Double distance;

    private List<Double> location;

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public List<Double> getLocation() {
        return location;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}