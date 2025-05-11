package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.FactRefuelling;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FactRefuellingRepository extends JmixDataRepository<FactRefuelling, UUID> {
}