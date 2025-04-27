package com.svetkin.optrou.service;

import com.svetkin.optrou.controller.FuelStationIntegrationController;
import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.dto.FuelStationDto;
import com.svetkin.optrou.repository.FuelStationRepository;
import com.svetkin.optrou.service.mapper.FuelStationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@Component(FuelStationIntegrationProcessor.NAME)
public class FuelStationIntegrationProcessor {

    public static final String NAME = "optrou_FuelStationIntegrationProcessor";

    private static final Logger log = LoggerFactory.getLogger(FuelStationIntegrationProcessor.class);
    private final FuelStationIntegrationController fuelStationIntegrationController;
    private final FuelStationRepository fuelStationRepository;
    private final FuelStationMapper fuelStationMapper;

    public FuelStationIntegrationProcessor(FuelStationIntegrationController fuelStationIntegrationController, FuelStationRepository fuelStationRepository, FuelStationMapper fuelStationMapper) {
        this.fuelStationIntegrationController = fuelStationIntegrationController;
        this.fuelStationRepository = fuelStationRepository;
        this.fuelStationMapper = fuelStationMapper;
    }

    public void processFuelStations() {
        log.debug("Begin process fuel stations");
        List<FuelStationDto> fuelStationDtos = fuelStationIntegrationController.getAllFuelStations();
        List<FuelStation> fuelStations = StreamSupport
                .stream(fuelStationRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(FuelStation::getStationId))
                .toList();
        List<String> fuelStationStationIds = fuelStations.stream()
                .map(FuelStation::getStationId)
                .sorted()
                .toList();

        for (FuelStationDto fuelStationDto : fuelStationDtos) {
            int lastIndexOf = fuelStationStationIds.lastIndexOf(fuelStationDto.getStationId());

            FuelStation fuelStation = lastIndexOf != -1
                ? fuelStations.get(lastIndexOf)
                : fuelStationRepository.create();

            fuelStation = fuelStationMapper.mapDtoToEntity(fuelStation, fuelStationDto);

            fuelStationRepository.save(fuelStation);
        }
    }


}