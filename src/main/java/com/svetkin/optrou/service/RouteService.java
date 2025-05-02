package com.svetkin.optrou.service;

import com.mapbox.geojson.utils.PolylineUtils;
import com.svetkin.optrou.controller.FuelStationController;
import com.svetkin.optrou.controller.OsrmRouteController;
import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.FuelStationPrice;
import com.svetkin.optrou.entity.RoutePoint;
import com.svetkin.optrou.entity.dto.FuelStationDto;
import com.svetkin.optrou.entity.dto.FuelStationPriceDto;
import com.svetkin.optrou.entity.dto.RouteDto;
import com.svetkin.optrou.entity.dto.RoutePointDto;
import com.svetkin.optrou.repository.FuelStationPriceRepository;
import com.svetkin.optrou.repository.FuelStationRepository;
import com.svetkin.optrou.service.mapper.FuelStationMapper;
import com.svetkin.optrou.service.mapper.FuelStationPriceMapper;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import io.jmix.maps.converter.wkt.LineStringWKTConverter;
import io.jmix.maps.utils.GeometryUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.util.GeometryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.StreamSupport;

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
                .map(routeDto -> PolylineUtils.decode(routeDto.getGeometry(), 5))
                .map(points -> points.stream()
                        .map(point -> new Coordinate(point.longitude(), point.latitude()))
                        .toList())
                .map(coords -> GeometryUtils.createLineString(coords.toArray(Coordinate[]::new)))
                .toList();
    }
}