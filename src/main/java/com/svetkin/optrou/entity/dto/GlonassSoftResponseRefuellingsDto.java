package com.svetkin.optrou.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.List;

@JmixEntity(name = "optrou_GlonassSoftResponseRefuellingsDto")
public class GlonassSoftResponseRefuellingsDto {

    @JsonProperty("fuels")
    private List<GlonassSoftRefuellingDto> refuellings;

    public List<GlonassSoftRefuellingDto> getRefuellings() {
        return refuellings;
    }

    public void setRefuellings(List<GlonassSoftRefuellingDto> refuellings) {
        this.refuellings = refuellings;
    }
}