package com.svetkin.optrou.rest;

import com.svetkin.optrou.controller.FuelStationBenzuberController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component(RestClient.NAME)
public class RestClient {

    public static final String NAME = "optrou_RestClient";

    private static final Logger log = LoggerFactory.getLogger(FuelStationBenzuberController.class);

    public <RES> ResponseEntity<RES> getWithRequest(String path, Class<RES> responseType, HttpEntity<?> request, Map<String, Object> uriVariables) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RES> responseEntity;
        try {
            responseEntity = restTemplate.exchange(path, HttpMethod.GET, request, responseType, uriVariables);

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                log.debug("Received non 200 custom request http GET response {}", responseEntity);
            }

            log.debug("Successfully received custom request http GET response {}", responseEntity);
            return responseEntity;
        } catch (Exception e) {
            log.debug("Not received custom request http GET response {}", e);
        }

        return null;
    }

    public <RES> ResponseEntity<RES> get(String path, Class<RES> responseType, Map<String, ?> uriVariables) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RES> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(path, responseType, uriVariables);

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                log.debug("Received non 200 http GET response {}", responseEntity);
            }

            log.debug("Successfully received http GET response {}", responseEntity);
            return responseEntity;
        } catch (Exception e) {
            log.debug("Not received http GET response {}", e);
        }

        return null;
    }

    public <RES> ResponseEntity<RES> post(String path, Class<RES> responseType, Object request, Map<String, Object> uriVariables) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RES> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(path, request, responseType, uriVariables);

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                log.debug("Received non 200 http POST response {}", responseEntity);
            }

            log.debug("Successfully received http POST response {}", responseEntity);
            return responseEntity;
        } catch (Exception e) {
            log.debug("Not received http POST response {}", e);
        }

        return null;
    }
}