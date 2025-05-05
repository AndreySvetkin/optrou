package com.svetkin.optrou.service.mapper;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.FuelStationBrand;
import com.svetkin.optrou.entity.dto.FuelStationDto;
import com.svetkin.optrou.entity.dto.PointDto;
import com.svetkin.optrou.repository.FuelStationBrandRepository;
import com.svetkin.optrou.repository.FuelStationRepository;
import io.jmix.maps.utils.GeometryUtils;
import org.springframework.stereotype.Component;

@Component(FuelStationMapper.NAME)
public class FuelStationMapper {

    public static final String NAME = "optrou_FuelStationPriceMapper";
    private final FuelStationBrandRepository fuelStationBrandRepository;

    public FuelStationMapper(FuelStationBrandRepository fuelStationBrandRepository) {
        this.fuelStationBrandRepository = fuelStationBrandRepository;
    }

    public FuelStation mapDtoToEntity(FuelStation fuelStation, FuelStationDto fuelStationDto) {
        String brandString = fuelStationDto.getBrand();
        FuelStationBrand brand = fuelStationBrandRepository.findByName(brandString);
        if (brand == null) {
            brand = fuelStationBrandRepository.create();
            brand.setName(brandString);
            brand = fuelStationBrandRepository.save(brand);
        }

        fuelStation.setStationId(fuelStationDto.getStationId());
        fuelStation.setName(fuelStationDto.getName());
        fuelStation.setBrand(brand);
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