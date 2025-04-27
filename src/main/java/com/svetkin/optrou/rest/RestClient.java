package com.svetkin.optrou.rest;

import com.svetkin.optrou.controller.FuelStationIntegrationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component("optrou_RestClient")
public class RestClient {
    private static final Logger log = LoggerFactory.getLogger(FuelStationIntegrationController.class);

    public <RES> ResponseEntity<RES> get(String path, Class<RES> responseType, Map<String, Object> uriVariables) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RES> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(path, responseType, uriVariables);

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                log.debug("Received non 200 http response {}", responseEntity);
            }

            log.debug("Fuel stations successfully loaded from benzuber");
            return responseEntity;
        } catch (Exception e) {
            log.debug("Error for load fuel stations from benzuber", e);
        }

        return null;
    }
}