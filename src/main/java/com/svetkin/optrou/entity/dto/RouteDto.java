package com.svetkin.optrou.entity.dto;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity(name = "optrou_RouteDto")
public class RouteDto {

    private String geometry;

    private Double distance;

    private Double duration;

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

}