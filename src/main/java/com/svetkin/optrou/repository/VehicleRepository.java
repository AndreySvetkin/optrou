package com.svetkin.optrou.repository;

import com.svetkin.optrou.entity.Vehicle;
import io.jmix.core.repository.JmixDataRepository;
import io.jmix.core.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JmixDataRepository<Vehicle, UUID> {

    @Query("""
            select distinct v
            from optrou_Vehicle v
                left join optrou_Trip t
                    on v = t.vehicle
            where not exists (
                    select 1
                    from optrou_Trip t
                    where t.vehicle = v
                        and t.status not like 'IN_PROGRESS'
                )""")
    List<Vehicle> findByTripStatusNotInProgress();
}