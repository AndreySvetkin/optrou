package com.svetkin.optrou.service;

import com.mapbox.geojson.utils.PolylineUtils;
import com.svetkin.optrou.controller.OsrmRouteController;
import com.svetkin.optrou.entity.RoutePoint;
import com.svetkin.optrou.entity.dto.RouteDto;
import io.jmix.maps.utils.GeometryUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(RouteService.NAME)
public class RouteService {

    public static final String NAME = "optrou_RouteService";

    private static final Logger log = LoggerFactory.getLogger(RouteService.class);

    private final OsrmRouteController osrmRouteController;

    public RouteService(OsrmRouteController osrmRouteController) {
        this.osrmRouteController = osrmRouteController;
    }

    public List<LineString> getLineByPoints(List<RoutePoint> controlPoints) {
        List<RouteDto> routeDtos = osrmRouteController.getRoutesByControlPoints(controlPoints);

        return routeDtos.stream()
                .map(routeDto -> PolylineUtils.decode(routeDto.getGeometry(), 6))
                .map(points -> points.stream()
                        .map(point -> new Coordinate(point.longitude(), point.latitude()))
                        .toList())
                .map(coords -> GeometryUtils.createLineString(coords.toArray(Coordinate[]::new)))
                .toList();
    }
}