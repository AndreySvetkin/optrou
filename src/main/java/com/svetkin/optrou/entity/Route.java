package com.svetkin.optrou.entity;

import com.svetkin.optrou.entity.trait.HasLine;
import io.jmix.core.MetadataTools;
import io.jmix.core.annotation.DeletedBy;
import io.jmix.core.annotation.DeletedDate;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.datatype.DatatypeFormatter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@JmixEntity
@Table(name = "OPTROU_ROUTE")
@Entity(name = "optrou_Route")
public class Route implements HasLine {
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

    @DeletedBy
    @Column(name = "DELETED_BY")
    private String deletedBy;

    @DeletedDate
    @Column(name = "DELETED_DATE")
    private OffsetDateTime deletedDate;

    @Column(name = "NUMBER_", nullable = false)
    @NotNull
    private Long number;

    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @NotNull
    @Column(name = "LINE", nullable = false)
    private LineString line;

    @Column(name = "LENGTH", nullable = false)
    @NotNull
    private Double length;

    @Composition
    @OneToMany(mappedBy = "route")
    private List<RoutePoint> controlPoints = new ArrayList<>();

    @OneToMany(mappedBy = "route")
    private List<RouteFuelStation> fuelStations = new ArrayList<>();

    @OneToMany(mappedBy = "route")
    private List<Trip> trips;

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public OffsetDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(OffsetDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public void setFuelStations(List<RouteFuelStation> fuelStations) {
        this.fuelStations = fuelStations;
    }

    public List<RouteFuelStation> getFuelStations() {
        fuelStations.sort(Comparator.comparing(RouteFuelStation::getDistance));
        return fuelStations;
    }

    public List<RoutePoint> getControlPoints() {
        controlPoints.sort(Comparator.comparing(RoutePoint::getOrder));
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

    @InstanceName
    @DependsOnProperties({"number", "name"})
    public String getInstanceName(MetadataTools metadataTools, DatatypeFormatter datatypeFormatter) {
        return String.format("%s %s",
                datatypeFormatter.formatLong(number),
                metadataTools.format(name));
    }

    public static Function<Object, String> getPointTooltipTextProviderFunction() {
        return object -> {
            RoutePoint point = (RoutePoint) object;
            if (point.getName() == null || point.getLocation() == null) {
                return "";
            }

            return point.getName();
        };
    }

    public static Function<Object, String> getFuelStationTooltipTextProviderFunction() {
        return object -> {
            RouteFuelStation routeFuelStation = (RouteFuelStation)  object;
            if (routeFuelStation.getFuelStation() == null) {
                return "";
            }

            FuelStation fuelStation = routeFuelStation.getFuelStation();

            if (fuelStation.getName() == null || fuelStation.getBrand() == null || fuelStation.getLocation() == null) {
                return "";
            }

            return fuelStation.getName() + " " + fuelStation.getBrand().getName() + " " + routeFuelStation.getDistance();
        };
    }

    public static Function<Object, String> getLineTooltipTextProviderFunction() {
        return object -> {
            Route route = (Route) object;
            if (route.getName() == null || route.getLine() == null) {
                return "";
            }

            return route.getName() + " " + route.getNumber() + " Длина: " + route.getLength();
        };
    }
}