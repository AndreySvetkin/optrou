package com.svetkin.optrou.entity.type;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum TripStatus implements EnumClass<String> {

    NEW("NEW"),
    CONFIRMATION("CONFIRMATION"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE"),
    CANCELLED("CANCELLED");

    private final String id;

    TripStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TripStatus fromId(String id) {
        for (TripStatus at : TripStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}