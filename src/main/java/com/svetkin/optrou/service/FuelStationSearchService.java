package com.svetkin.optrou.service;

import com.mapbox.geojson.utils.PolylineUtils;
import com.svetkin.optrou.controller.OsrmRouteController;
import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.RouteFuelStation;
import com.svetkin.optrou.entity.RoutePoint;
import com.svetkin.optrou.entity.dto.RouteDto;
import com.svetkin.optrou.repository.FuelStationRepository;
import com.svetkin.optrou.repository.RouteFuelStationRepository;
import io.jmix.core.DataManager;
import io.jmix.maps.utils.GeometryUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component(FuelStationSearchService.NAME)
public class FuelStationSearchService {

    public static final String NAME = "optrou_FuelStationSearchService";

    private static final Logger log = LoggerFactory.getLogger(FuelStationSearchService.class);
    private static final String SEARCH_FUEL_STATION_QUERY_STRING = """
            with stbuffer as (
                select ST_Transform(ST_Buffer(ST_Transform(ST_SetSRID(#line::geometry, 4326), 26986), 300, 'side=both'), 4326) buffer
            ),
            fuel_stations as (
                select fs.id, fs.location
                from optrou_fuel_station fs
                where fs.deleted_date is null
                    and fs.location is not null
                    and fs.is_enabled
            ),
            filtered_right_side_fs as (
                select fs.id, fs.location
                from fuel_stations fs, stbuffer buf
                where ST_within(ST_SetSRID(fs.location::geometry, 4326), buf.buffer)
            )
            select fs.id,
                ST_length(ST_SetSRID(
                    ST_LineSubstring(
                        #line::geometry, 0, ST_LineLocatePoint(#line::geometry, ST_geomFromText(fs.location))
                    ), 4326)::geography
                ) as distance
            from filtered_right_side_fs fs
                order by distance;
            """;

    @PersistenceContext
    private EntityManager entityManager;

    private final FuelStationRepository fuelStationRepository;
    private final RouteFuelStationCreateService routeFuelStationCreateService;

    public FuelStationSearchService(FuelStationRepository fuelStationRepository,
                                    RouteFuelStationCreateService routeFuelStationCreateService) {
        this.fuelStationRepository = fuelStationRepository;
        this.routeFuelStationCreateService = routeFuelStationCreateService;
    }

    public List<RouteFuelStation> getFuelStations(Route route) {
        List<?> resultList = entityManager.createNativeQuery(SEARCH_FUEL_STATION_QUERY_STRING)
                .setParameter("line", route.getLine().toString())
                .getResultList();

        Map<UUID, Double> fuelStationIdsWithDistance = new HashMap<>();

        for (Object rowObject : resultList) {
            Object[] row = (Object[]) rowObject;

            UUID fuelStationId = (UUID) row[0];
            Double distance = (Double) row[1] / 1000 ;

            fuelStationIdsWithDistance.put(fuelStationId, distance);
        }

        return StreamSupport.stream(fuelStationRepository
                .findAllById(fuelStationIdsWithDistance.keySet()).spliterator(), false)
                .map(fuelStation -> routeFuelStationCreateService.createRouteFuelStation(
                        route,
                        fuelStation,
                        fuelStationIdsWithDistance.get(fuelStation.getId())))
                .toList();
    }
}