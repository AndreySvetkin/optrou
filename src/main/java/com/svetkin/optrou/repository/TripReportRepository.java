package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.TripReport;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TripReportRepository extends JmixDataRepository<TripReport, UUID> {
}