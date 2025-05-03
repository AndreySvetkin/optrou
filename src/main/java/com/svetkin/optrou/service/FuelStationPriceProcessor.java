package com.svetkin.optrou.service;

import com.svetkin.optrou.controller.FuelStationBenzuberController;
import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.FuelStationPrice;
import com.svetkin.optrou.entity.dto.FuelStationPriceDto;
import com.svetkin.optrou.repository.FuelStationPriceRepository;
import com.svetkin.optrou.repository.FuelStationRepository;
import com.svetkin.optrou.service.mapper.FuelStationPriceMapper;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component(FuelStationPriceProcessor.NAME)
public class FuelStationPriceProcessor {

    public static final String NAME = "optrou_FuelStationPriceProcessor";

    private static final Logger log = LoggerFactory.getLogger(FuelStationPriceProcessor.class);
    private final FuelStationBenzuberController fuelStationBenzuberController;
    private final FuelStationRepository fuelStationRepository;
    private final FuelStationPriceRepository fuelStationPriceRepository;
    private final FuelStationPriceMapper fuelStationPriceMapper;
    private final DataManager dataManager;

    public FuelStationPriceProcessor(FuelStationBenzuberController fuelStationBenzuberController,
                                     FuelStationRepository fuelStationRepository,
                                     FuelStationPriceRepository fuelStationPriceRepository,
                                     FuelStationPriceMapper fuelStationPriceMapper,
                                     DataManager dataManager) {
        this.fuelStationBenzuberController = fuelStationBenzuberController;
        this.fuelStationRepository = fuelStationRepository;
        this.fuelStationPriceRepository = fuelStationPriceRepository;
        this.fuelStationPriceMapper = fuelStationPriceMapper;
        this.dataManager = dataManager;
    }

    public void processFuelStationPrices() {
        log.debug("Begin process fuel station prices");
        List<FuelStationPriceDto> fuelStationPriceDtos = fuelStationBenzuberController.getAllFuelStationPrices();

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

        SaveContext saveContext = new SaveContext();
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

            saveContext.saving(fuelStationPrice);
        }

        dataManager.save(saveContext);
        log.debug("End process fuel station prices");
    }

    public void processFuelStationPricesByFuelStation(FuelStation fuelStation) {
        log.debug("Begin process fuel station prices. Fuel station {}", fuelStation);
        String stationId = fuelStation.getStationId();

        List<FuelStationPriceDto> fuelStationPriceDtos = fuelStationBenzuberController.getFuelStationPrices(stationId);

        List<FuelStationPrice> fuelStationPrices = fuelStationPriceRepository.findByFuelStation(fuelStation);

        SaveContext saveContext = new SaveContext();
        for (FuelStationPriceDto fuelStationPriceDto : fuelStationPriceDtos) {
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

            saveContext.saving(fuelStationPrice);
        }

        dataManager.save(saveContext);
        log.debug("End process fuel station prices. Fuel station {}", fuelStation);
    }
}