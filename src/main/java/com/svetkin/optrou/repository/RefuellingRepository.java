package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.Refuelling;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefuellingRepository extends JmixDataRepository<Refuelling, UUID> {
}