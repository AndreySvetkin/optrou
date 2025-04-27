package com.svetkin.optrou.entity.type;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum FuelType implements EnumClass<Integer> {

    A80(10, "a80"),
    AI92(20, "a92"),
    AI92_PREMIUM(30, "a92_premium"),
    AI92_OPTI(40, "a92_opti"),
    AI95(50, "a95"),
    AI95_PREMIUM(60, "a95_premium"),
    AI95_OPTI(70, "a95_opti"),
    AI98(80, "a98"),
    AI98_PREMIUM(90, "a98_premium"),
    AI100(100, "a100"),
    AI100_PREMIUM(110, "a100_premium"),
    DIESEL(120, "diesel"),
    DIESEL_OPTI(130, "diesel_opti"),
    DIESEL_PREMIUM(140, "diesel_premium"),
    DIESEL_WINTER(150, "diesel_winter"),
    DIESEL_DEMISEASON(160, "diesel_demiseason"),
    METAN(170, "metan"),
    PROPANE(180, "propane");

    private final Integer id;
    private final String benzuberId;

    FuelType(Integer id, String benzuberId) {
        this.id = id;
        this.benzuberId = benzuberId;
    }

    public Integer getId() {
        return id;
    }

    public String getBenzuberId() {
        return benzuberId;
    }

    @Nullable
    public static FuelType fromId(Integer id) {
        for (FuelType at : FuelType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

    @Nullable
    public static FuelType fromBenzuberId(String benzuberId) {
        for (FuelType at : FuelType.values()) {
            if (at.getBenzuberId().equals(benzuberId)) {
                return at;
            }
        }
        return null;
    }
}