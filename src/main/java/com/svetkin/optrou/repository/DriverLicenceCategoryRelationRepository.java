package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.DriverLicenceCategoryRelation;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DriverLicenceCategoryRelationRepository extends JmixDataRepository<DriverLicenceCategoryRelation, UUID> {
}