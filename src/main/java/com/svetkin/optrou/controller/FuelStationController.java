package com.svetkin.optrou.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svetkin.optrou.entity.dto.FuelStationDto;
import com.svetkin.optrou.entity.dto.FuelStationPriceDto;
import com.svetkin.optrou.rest.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component(FuelStationController.NAME)
public class FuelStationController {

    public static final String NAME = "optrou_FuelStationController";

    private final ObjectMapper objectMapper;

    @Value("${optrou.benzuber.base-url}")
    private String baseUrl;

    @Value("${optrou.benzuber.apikey}")
    private String apikey;

    private final RestClient restClient;

    public FuelStationController(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public List<FuelStationDto> getAllFuelStations() {
        String path = baseUrl + "fuelstations";
        List<?> fuelStations = restClient.get(path, List.class, Map.of()).getBody();
        return fuelStations.stream()
                .map(fuelStation -> objectMapper.convertValue(fuelStation, FuelStationDto.class))
                .toList();
    }

    public List<FuelStationPriceDto> getAllFuelStationPrices() {
        String path = baseUrl + "price";
        List<?> fuelStationPrices = restClient.get(path, List.class, Map.of()).getBody();
        return fuelStationPrices.stream()
                .map(fuelStationPrice -> objectMapper.convertValue(fuelStationPrice, FuelStationPriceDto.class))
                .toList();
    }

    public List<FuelStationPriceDto> getFuelStationPrices(String stationId) {
        String path = baseUrl + "price";
        List<?> fuelStationPrices = restClient.get(path, List.class, Map.of("stationId", stationId)).getBody();
        return fuelStationPrices.stream()
                .map(fuelStationPrice -> objectMapper.convertValue(fuelStationPrice, FuelStationPriceDto.class))
                .toList();
    }
}