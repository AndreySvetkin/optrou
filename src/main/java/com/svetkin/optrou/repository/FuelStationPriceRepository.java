package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.FuelStationPrice;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FuelStationPriceRepository extends JmixDataRepository<FuelStationPrice, UUID> {

    List<FuelStationPrice> findByFuelStation(FuelStation fuelStation);
}