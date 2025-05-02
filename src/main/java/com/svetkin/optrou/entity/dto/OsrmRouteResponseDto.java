package com.svetkin.optrou.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.List;

@JmixEntity(name = "optrou_OsrmRouteResponseDto")
public class OsrmRouteResponseDto {

    private String code;

    @JsonProperty("waypoints")
    private List<RoutePointDto> points;

    private List<RouteDto> routes;

    public List<RouteDto> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteDto> routes) {
        this.routes = routes;
    }

    public List<RoutePointDto> getPoints() {
        return points;
    }

    public void setPoints(List<RoutePointDto> points) {
        this.points = points;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}