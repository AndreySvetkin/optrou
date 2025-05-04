package com.svetkin.optrou.entity.type;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum DriverLicenseCategory implements EnumClass<String> {

    B("B"),
    BE("BE"),
    C("C"),
    CE("CE"),
    D("D"),
    DE("DE");

    private final String id;

    DriverLicenseCategory(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static DriverLicenseCategory fromId(String id) {
        for (DriverLicenseCategory at : DriverLicenseCategory.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}