package com.svetkin.optrou.entity.type;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum RefuellingPlanCreateStatus implements EnumClass<String> {

    DONE("DONE"),
    NEXT_STATION_OR_END_ROUTE_FAR_AWAY("NEXT_STATION_OR_END_ROUTE_FAR_AWAY"),
    IS_EMPTY_FUEL_STATIONS_WITH_FUEL_TYPE("IS_EMPTY_FUEL_STATIONS_WITH_FUEL_TYPE");

    private final String id;

    RefuellingPlanCreateStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static RefuellingPlanCreateStatus fromId(String id) {
        for (RefuellingPlanCreateStatus at : RefuellingPlanCreateStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}