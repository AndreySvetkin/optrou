package com.svetkin.optrou.service.mapper;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.FuelStationPrice;
import com.svetkin.optrou.entity.dto.FuelStationDto;
import com.svetkin.optrou.entity.dto.FuelStationPriceDto;
import com.svetkin.optrou.entity.type.FuelType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component(FuelStationPriceMapper.NAME)
public class FuelStationPriceMapper {

    public static final String NAME = "optrou_FuelStationMapper";

    public FuelStationPrice mapDtoToEntity(FuelStationPrice fuelStationPrice, FuelStationPriceDto fuelStationPriceDto) {
        fuelStationPrice.setFuelType(FuelType.fromBenzuberId(fuelStationPriceDto.getFuelTypeId()));
        fuelStationPrice.setValue(BigDecimal.valueOf(fuelStationPriceDto.getPrice()));
        return fuelStationPrice;
    }
}