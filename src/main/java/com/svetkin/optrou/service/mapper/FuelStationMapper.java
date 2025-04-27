package com.svetkin.optrou.service.mapper;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.dto.FuelStationDto;
import com.svetkin.optrou.repository.FuelStationRepository;
import org.springframework.stereotype.Component;

@Component(FuelStationMapper.NAME)
public class FuelStationMapper {

    public static final String NAME = "optrou_FuelStationPriceMapper";

    public FuelStation mapDtoToEntity(FuelStation fuelStation, FuelStationDto fuelStationDto) {
        fuelStation.setStationId(fuelStationDto.getStationId());
        fuelStation.setName(fuelStationDto.getName());
        fuelStation.setBrand(fuelStationDto.getBrand());
        fuelStation.setAddress(fuelStationDto.getAddress());
        fuelStation.setLocation(fuelStationDto.getLocation());
        fuelStation.setIsEnabled(fuelStationDto.getIsGoods());
        return fuelStation;
    }
}