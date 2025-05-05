package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.FuelStationBrand;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FuelStationBrandRepository extends JmixDataRepository<FuelStationBrand, UUID> {

    FuelStationBrand findByName(String name);
}