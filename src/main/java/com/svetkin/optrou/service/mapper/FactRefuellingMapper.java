package com.svetkin.optrou.service.mapper;

import com.svetkin.optrou.entity.FactRefuelling;
import com.svetkin.optrou.entity.FactRefuellingPlan;
import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.FuelStationBrand;
import com.svetkin.optrou.entity.FuelStationPrice;
import com.svetkin.optrou.entity.dto.FuelStationDto;
import com.svetkin.optrou.entity.dto.GlonassSoftRefuellingDto;
import com.svetkin.optrou.entity.dto.PointDto;
import com.svetkin.optrou.repository.FuelStationBrandRepository;
import io.jmix.maps.utils.GeometryUtils;
import org.springframework.stereotype.Component;

@Component(FactRefuellingMapper.NAME)
public class FactRefuellingMapper {

    public static final String NAME = "optrou_FactRefuellingMapper";

    public FactRefuelling mapDtoToEntity(FactRefuelling factRefuelling, GlonassSoftRefuellingDto refuellingDto, FactRefuellingPlan factRefuellingPlan) {
        factRefuelling.setFactRefuellingPlan(factRefuellingPlan);
        factRefuelling.setVolume(refuellingDto.getVolume());
        factRefuelling.setBeforeLevelFuel(refuellingDto.getBeforeLevelFuel());
        factRefuelling.setAfterLevelFuel(refuellingDto.getAfterLevelFuel());
        return factRefuelling;
    }
}