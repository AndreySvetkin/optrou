package com.svetkin.optrou.service.mapper;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.dto.FuelStationDto;
import com.svetkin.optrou.entity.dto.PointDto;
import com.svetkin.optrou.repository.FuelStationRepository;
import io.jmix.maps.utils.GeometryUtils;
import org.springframework.stereotype.Component;

@Component(FuelStationMapper.NAME)
public class FuelStationMapper {

    public static final String NAME = "optrou_FuelStationPriceMapper";

    public FuelStation mapDtoToEntity(FuelStation fuelStation, FuelStationDto fuelStationDto) {
        fuelStation.setStationId(fuelStationDto.getStationId());
        fuelStation.setName(fuelStationDto.getName());
        fuelStation.setBrand(fuelStationDto.getBrand());
        fuelStation.setIsEnabled(fuelStationDto.getIsGoods());
        fuelStation.setRegion(fuelStationDto.getRegion());
        fuelStation.setAddress(fuelStationDto.getAddress());
        PointDto pointDto = fuelStationDto.getLocation();
        fuelStation.setLatitude(pointDto.getLatitude());
        fuelStation.setLongitude(pointDto.getLongitude());
        fuelStation.setLocation(GeometryUtils.createPoint(pointDto.getLatitude(), pointDto.getLongitude()));
        return fuelStation;
    }
}