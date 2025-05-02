package com.svetkin.optrou.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@JmixEntity
@Table(name = "OPTROU_ROUTE")
@Entity(name = "optrou_Route")
public class Route {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private OffsetDateTime createdDate;

    @InstanceName
    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @Column(name = "LINE")
    private LineString line;

    @Composition
    @OneToMany(mappedBy = "route")
    private List<RoutePoint> controlPoints;

    @JoinTable(name = "OPTROU_ROUTE_FUEL_STATION_LINK",
            joinColumns = @JoinColumn(name = "ROUTE_ID"),
            inverseJoinColumns = @JoinColumn(name = "FUEL_STATION_ID"))
    @ManyToMany
    private Set<FuelStation> fuelStations;

    public Set<FuelStation> getFuelStations() {
        return fuelStations;
    }

    public void setFuelStations(Set<FuelStation> fuelStations) {
        this.fuelStations = fuelStations;
    }

    public List<RoutePoint> getControlPoints() {
        return controlPoints;
    }

    public void setControlPoints(List<RoutePoint> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LineString getLine() {
        return line;
    }

    public void setLine(LineString line) {
        this.line = line;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}