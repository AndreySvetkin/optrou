package com.svetkin.optrou.entity.dto;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity(name = "optrou_RefuellingsVolumeDto")
public class RefuellingsVolumeDto {

    private Double planningVolume;

    private Double factVolume;

    public void setPlanningVolume(Double planningVolume) {
        this.planningVolume = planningVolume;
    }

    public Double getPlanningVolume() {
        return planningVolume;
    }

    public void setFactVolume(Double factVolume) {
        this.factVolume = factVolume;
    }

    public Double getFactVolume() {
        return factVolume;
    }

}