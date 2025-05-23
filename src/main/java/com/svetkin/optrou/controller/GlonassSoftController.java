package com.svetkin.optrou.controller;

import com.svetkin.optrou.entity.Vehicle;
import com.svetkin.optrou.entity.dto.GlonassSoftAuthenticationDto;
import com.svetkin.optrou.entity.dto.GlonassSoftAuthenticationRequestDto;
import com.svetkin.optrou.entity.dto.GlonassSoftResponseRefuellingsDto;
import com.svetkin.optrou.entity.dto.GlonassSoftResponseVehicleLocationsDto;
import com.svetkin.optrou.entity.dto.GlonassSoftVehicleInfoRequestDto;
import com.svetkin.optrou.entity.dto.GlonassSoftVehicleLocationRequestDto;
import com.svetkin.optrou.rest.RestClient;
import io.jmix.core.Metadata;
import io.jmix.core.session.SessionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component(GlonassSoftController.NAME)
public class GlonassSoftController {

    public static final String NAME = "optrou_GlonassSoftController";

    private static final String GLONASSSOFT_TOKEN_SESSION_KEY = "glonasssoft-token-key";
    private static final String GLONASSSOFT_TOKEN_HEADER_KEY = "X-Auth";
    private static final Logger log = LoggerFactory.getLogger(GlonassSoftController.class);

    @Value("${optrou.glonasssoft.base-url}")
    private String baseUrl;

    @Value("${optrou.glonasssoft.login}")
    private String login;

    @Value("${optrou.glonasssoft.password}")
    private String password;

    private final RestClient restClient;
    private final ObjectProvider<SessionData> sessionDataProvider;

    private final Metadata metadata;

    public GlonassSoftController(RestClient restClient,
                                 ObjectProvider<SessionData> sessionDataProvider,
                                 Metadata metadata) {
        this.restClient = restClient;
        this.sessionDataProvider = sessionDataProvider;
        this.metadata = metadata;
    }

    public boolean checkAuthorize() {
        String token = (String) sessionDataProvider.getObject().getAttribute(GLONASSSOFT_TOKEN_SESSION_KEY);
        if (token != null) {
            sleepOneSecond();

            String path = baseUrl + "/api/v3/auth/check";

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(GLONASSSOFT_TOKEN_HEADER_KEY, token);
            headers.add("Content-Type", "application/json");
            HttpEntity<?> request = new HttpEntity<>(headers);

            ResponseEntity<?> response = restClient
                    .getWithRequest(path, Object.class, request, Map.of());

            if (response.getStatusCode().is2xxSuccessful()) {
                return true;
            }
        }
        return false;
    }

    public void authorize() {
        sleepOneSecond();

        String path = baseUrl + "/api/v3/auth/login";
        GlonassSoftAuthenticationRequestDto requestBody = metadata.create(GlonassSoftAuthenticationRequestDto.class);
        requestBody.setLogin(login);
        requestBody.setPassword(password);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        HttpEntity request = new HttpEntity<>(requestBody,headers);

        GlonassSoftAuthenticationDto responseDto = restClient
                .post(path, GlonassSoftAuthenticationDto.class, request, Map.of())
                .getBody();
        sessionDataProvider.getObject().setAttribute(GLONASSSOFT_TOKEN_SESSION_KEY, responseDto.getToken());
    }

    public GlonassSoftResponseRefuellingsDto getFactRefuellings(Vehicle vehicle, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (!checkAuthorize()) {
            authorize();
        }

        sleepOneSecond();

        String path = baseUrl + "/api/v3/vehicles/fuelInOut";
        String token = (String) sessionDataProvider.getObject().getAttribute(GLONASSSOFT_TOKEN_SESSION_KEY);

        GlonassSoftVehicleInfoRequestDto requestBody = metadata.create(GlonassSoftVehicleInfoRequestDto.class);
        requestBody.setVehicleIds(List.of(vehicle.getVehicleId()));
        requestBody.setStartDate(startDateTime);
        requestBody.setEndDate(endDateTime);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(GLONASSSOFT_TOKEN_HEADER_KEY, token);
        headers.add("Content-Type", "application/json");
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);

        return restClient
                .post(path, GlonassSoftResponseRefuellingsDto.class, request, Map.of())
                .getBody();
    }

    public GlonassSoftResponseVehicleLocationsDto getFactLocations(Vehicle vehicle, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (!checkAuthorize()) {
            authorize();
        }

        sleepOneSecond();

        String path = baseUrl + "/api/v3/terminalMessages";
        String token = (String) sessionDataProvider.getObject().getAttribute(GLONASSSOFT_TOKEN_SESSION_KEY);

        GlonassSoftVehicleLocationRequestDto requestBody = metadata.create(GlonassSoftVehicleLocationRequestDto.class);
        requestBody.setVehicleId(vehicle.getVehicleId());
        requestBody.setStartDate(startDateTime);
        requestBody.setEndDate(endDateTime);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(GLONASSSOFT_TOKEN_HEADER_KEY, token);
        headers.add("Content-Type", "application/json");
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);

        return restClient
                .post(path, GlonassSoftResponseVehicleLocationsDto.class, request, Map.of())
                .getBody();
    }

    private void sleepOneSecond() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}