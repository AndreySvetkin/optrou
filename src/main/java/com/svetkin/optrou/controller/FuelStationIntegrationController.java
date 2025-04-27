package com.svetkin.optrou.controller;

import com.svetkin.optrou.entity.dto.FuelStationDto;
import com.svetkin.optrou.rest.RestClient;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component(FuelStationIntegrationController.NAME)
public class FuelStationIntegrationController {


    public static final String NAME = "optrou_FuelStationIntegrationController";

    @Value("optrou.benzuber.base-url")
    private String baseUrl;

    @Value("optrou.benzuber.apikey")
    private String apikey;

    private final RestClient restClient;

    public FuelStationIntegrationController(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<FuelStationDto> getAllFuelStations() {
        String path = baseUrl + "fuelstations";
        return restClient.get(path, List.class, Map.of()).getBody();
    }
}