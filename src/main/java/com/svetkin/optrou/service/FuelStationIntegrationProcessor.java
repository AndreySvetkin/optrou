package com.svetkin.optrou.service;

import com.svetkin.optrou.controller.FuelStationIntegrationController;
import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.FuelStationPrice;
import com.svetkin.optrou.entity.dto.FuelStationDto;
import com.svetkin.optrou.entity.dto.FuelStationPriceDto;
import com.svetkin.optrou.repository.FuelStationPriceRepository;
import com.svetkin.optrou.repository.FuelStationRepository;
import com.svetkin.optrou.service.mapper.FuelStationMapper;
import com.svetkin.optrou.service.mapper.FuelStationPriceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component(FuelStationIntegrationProcessor.NAME)
public class FuelStationIntegrationProcessor {

    public static final String NAME = "optrou_FuelStationIntegrationProcessor";

    private static final Logger log = LoggerFactory.getLogger(FuelStationIntegrationProcessor.class);
    private final FuelStationIntegrationController fuelStationIntegrationController;
    private final FuelStationRepository fuelStationRepository;
    private final FuelStationMapper fuelStationMapper;
    private final FuelStationPriceRepository fuelStationPriceRepository;
    private final FuelStationPriceMapper fuelStationPriceMapper;

    public FuelStationIntegrationProcessor(FuelStationIntegrationController fuelStationIntegrationController, FuelStationRepository fuelStationRepository, FuelStationMapper fuelStationMapper, FuelStationPriceRepository fuelStationPriceRepository, FuelStationPriceMapper fuelStationPriceMapper) {
        this.fuelStationIntegrationController = fuelStationIntegrationController;
        this.fuelStationRepository = fuelStationRepository;
        this.fuelStationMapper = fuelStationMapper;
        this.fuelStationPriceRepository = fuelStationPriceRepository;
        this.fuelStationPriceMapper = fuelStationPriceMapper;
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

        log.debug("End process fuel stations");
    }

    public void processFuelStationPrices() {
        log.debug("Begin process fuel station prices");
        List<FuelStationPriceDto> fuelStationPriceDtos = fuelStationIntegrationController.getAllFuelStationPrices();
        List<FuelStationPrice> fuelStationPrices = StreamSupport
                .stream(fuelStationPriceRepository.findAll().spliterator(), false)
                .toList();
        List<FuelStation> fuelStations = StreamSupport
                .stream(fuelStationRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(FuelStation::getStationId))
                .toList();
        List<String> fuelStationStationIds = fuelStations.stream()
                .map(FuelStation::getStationId)
                .sorted()
                .toList();

        for (FuelStationPriceDto fuelStationPriceDto : fuelStationPriceDtos) {
            int lastIndexOf = fuelStationStationIds.lastIndexOf(fuelStationPriceDto.getStationId());
            FuelStation fuelStation = fuelStations.get(lastIndexOf);

            Optional<FuelStationPrice> fuelStationPriceOp = fuelStationPrices.stream()
                    .filter(fuelStationPrice ->
                            Objects.equals(fuelStationPrice.getFuelStation(), fuelStation)
                            && Objects.equals(fuelStationPrice.getFuelType().getBenzuberId(), fuelStationPriceDto.getFuelTypeId()))
                    .findFirst();

            FuelStationPrice fuelStationPrice;
            if (fuelStationPriceOp.isPresent()) {
                fuelStationPrice = fuelStationPriceOp.get();
            } else {
                fuelStationPrice = fuelStationPriceRepository.create();
                fuelStationPrice.setFuelStation(fuelStation);
            }

            fuelStationPrice = fuelStationPriceMapper.mapDtoToEntity(fuelStationPrice, fuelStationPriceDto);

            fuelStationPriceRepository.save(fuelStationPrice);
        }

        log.debug("End process fuel station prices");
    }
}