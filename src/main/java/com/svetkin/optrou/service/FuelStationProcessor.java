package com.svetkin.optrou.service;

import com.svetkin.optrou.controller.FuelStationBenzuberController;
import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.dto.FuelStationDto;
import com.svetkin.optrou.repository.FuelStationRepository;
import com.svetkin.optrou.service.mapper.FuelStationMapper;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component(FuelStationProcessor.NAME)
public class FuelStationProcessor {

    public static final String NAME = "optrou_FuelStationProcessor";

    private static final Logger log = LoggerFactory.getLogger(FuelStationProcessor.class);
    private final FuelStationBenzuberController fuelStationBenzuberController;
    private final FuelStationRepository fuelStationRepository;
    private final FuelStationMapper fuelStationMapper;
    private final DataManager dataManager;

    public FuelStationProcessor(FuelStationBenzuberController fuelStationBenzuberController,
                                FuelStationRepository fuelStationRepository,
                                FuelStationMapper fuelStationMapper,
                                DataManager dataManager) {
        this.fuelStationBenzuberController = fuelStationBenzuberController;
        this.fuelStationRepository = fuelStationRepository;
        this.fuelStationMapper = fuelStationMapper;
        this.dataManager = dataManager;
    }

    public void processFuelStations() {
        log.debug("Begin process fuel stations");
        List<FuelStationDto> fuelStationDtos = fuelStationBenzuberController.getAllFuelStations();

        List<FuelStation> fuelStations = StreamSupport
                .stream(fuelStationRepository.findAll().spliterator(), false)
                .toList();
        List<String> fuelStationStationIds = fuelStations.stream()
                .map(FuelStation::getStationId)
                .toList();

        SaveContext saveContext = new SaveContext();
        for (FuelStationDto fuelStationDto : fuelStationDtos) {
            int lastIndexOf = fuelStationStationIds.lastIndexOf(fuelStationDto.getStationId());

            FuelStation fuelStation = lastIndexOf != -1
                    ? fuelStations.get(lastIndexOf)
                    : fuelStationRepository.create();

            fuelStation = fuelStationMapper.mapDtoToEntity(fuelStation, fuelStationDto);

            saveContext.saving(fuelStation);
        }

        dataManager.save(saveContext);
        log.debug("End process fuel stations");
    }
}