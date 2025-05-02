package com.svetkin.optrou.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svetkin.optrou.entity.RoutePoint;
import com.svetkin.optrou.entity.dto.OsrmRouteResponseDto;
import com.svetkin.optrou.entity.dto.RouteDto;
import com.svetkin.optrou.entity.dto.RoutePointDto;
import com.svetkin.optrou.rest.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component(OsrmRouteController.NAME)
public class OsrmRouteController {

    public static final String NAME = "optrou_OsrmRouteController";

    private static final String OK_CODE = "Ok";
    private static final Logger log = LoggerFactory.getLogger(OsrmRouteController.class);

    private final ObjectMapper objectMapper;

    @Value("${optrou.osrm.base-url}")
    private String baseUrl;

    private final RestClient restClient;

    public OsrmRouteController(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public List<RouteDto> getRoutesByControlPoints(List<RoutePoint> controlPoints) {
        String path = baseUrl + "route/v1/driving/" + convertRoutePointsToString(controlPoints);
        OsrmRouteResponseDto osrmRouteResponseDto = restClient
                .get(path, OsrmRouteResponseDto.class, Map.of("overview", "full", "geometries", "polyline6"))
                .getBody();
        if (!Objects.equals(OK_CODE, osrmRouteResponseDto.getCode())) {
            log.debug("Error load route from OSRM. Code {}", osrmRouteResponseDto.getCode());
        }
        return osrmRouteResponseDto.getRoutes();
    }

    private String convertRoutePointsToString(List<RoutePoint> routePoints) {
        return routePoints.stream()
                .map(routePoint -> routePoint.getLatitude() + "," + routePoint.getLongitude())
                .collect(Collectors.joining(";"));
    }
}