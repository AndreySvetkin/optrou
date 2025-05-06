package com.svetkin.optrou.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svetkin.optrou.entity.dto.FuelStationDto;
import com.svetkin.optrou.entity.dto.FuelStationPriceDto;
import com.svetkin.optrou.entity.dto.FuelStationsResponseDto;
import com.svetkin.optrou.rest.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component(FuelStationBenzuberController.NAME)
public class FuelStationBenzuberController {

    public static final String NAME = "optrou_FuelStationBenzuberController";

    private static final String API_KEY = "apikey";

    @Value("${optrou.benzuber.base-url}")
    private String baseUrl;

    @Value("${optrou.benzuber.apikey}")
    private String apikey;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public FuelStationBenzuberController(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public List<FuelStationDto> getAllFuelStations() {
        String path = baseUrl + "stations";
        List<?> responseList = restClient.get(path, List.class, Map.of(API_KEY, apikey)).getBody();

        if (responseList == null) {
            return List.of();
        }

        return responseList.stream()
                .map(fuelStationString -> objectMapper.convertValue(fuelStationString, FuelStationDto.class))
                .toList();
    }

    public List<FuelStationPriceDto> getAllFuelStationPrices() {
        String path = baseUrl + "price";
        List<?> responseList = restClient.get(path, List.class, Map.of(API_KEY, apikey)).getBody();

        if (responseList == null) {
            return List.of();
        }

        return responseList.stream()
                .map(fuelStationPrice -> objectMapper.convertValue(fuelStationPrice, FuelStationPriceDto.class))
                .toList();
    }

    public List<FuelStationPriceDto> getFuelStationPrices(String stationId) {
        String path = baseUrl + "price";
        List<?> responseList = restClient.get(path, List.class, Map.of("stationId", stationId, API_KEY, apikey)).getBody();

        if (responseList == null) {
            return List.of();
        }

        return responseList.stream()
                .map(fuelStationPrice -> objectMapper.convertValue(fuelStationPrice, FuelStationPriceDto.class))
                .toList();
    }
}