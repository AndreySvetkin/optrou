package com.svetkin.optrou.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.time.LocalDateTime;

@JmixEntity(name = "optrou_GlonassSoftRefuellingDto")
public class GlonassSoftRefuellingDto {

    @JsonProperty("event")
    private Integer eventType;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @JsonProperty("valueFuel")
    private Double volume;

    @JsonProperty("startFuel")
    private Double beforeLevelFuel;

    @JsonProperty("endFuel")
    private Double afterLevelFuel;

    public Double getAfterLevelFuel() {
        return afterLevelFuel;
    }

    public void setAfterLevelFuel(Double afterLevelFuel) {
        this.afterLevelFuel = afterLevelFuel;
    }

    public Double getBeforeLevelFuel() {
        return beforeLevelFuel;
    }

    public void setBeforeLevelFuel(Double beforeLevelFuel) {
        this.beforeLevelFuel = beforeLevelFuel;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

}