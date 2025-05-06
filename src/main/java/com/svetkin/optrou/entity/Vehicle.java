package com.svetkin.optrou.entity;

import com.svetkin.optrou.entity.type.DriverLicenseCategory;
import com.svetkin.optrou.entity.type.FuelType;
import io.jmix.core.DeletePolicy;
import io.jmix.core.MetadataTools;
import io.jmix.core.annotation.DeletedBy;
import io.jmix.core.annotation.DeletedDate;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDelete;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;
import io.jmix.maps.utils.GeometryUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "OPTROU_VEHICLE")
@Entity(name = "optrou_Vehicle")
public class Vehicle {
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

    @Column(name = "LICENSE_PLATE", nullable = false)
    @NotNull
    private String licensePlate;

    @Column(name = "MODEL", nullable = false)
    @NotNull
    private String model;

    @Column(name = "DRIVER_LICENSE_CATEGORY", nullable = false)
    @NotNull
    private String driverLicenseCategory;

    @Column(name = "FUEL_TYPE", nullable = false)
    @NotNull
    private Integer fuelType;

    @Column(name = "FUEL_CONSUMPTION")
    private Double fuelConsumption;

    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    @OneToMany(mappedBy = "vehicle")
    private List<FuelTank> tanks;

    @Column(name = "LOCATION")
    private Point location;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @DependsOnProperties({"tanks"})
    @JmixProperty
    public Double getRemainingFuel() {
        return tanks.stream()
                .mapToDouble(FuelTank::getRemainingFuel)
                .sum();
    }

    @DependsOnProperties({"tanks"})
    @JmixProperty
    public Double getCapacity() {
        return tanks.stream()
                .mapToDouble(FuelTank::getCapacity)
                .sum();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<FuelTank> getTanks() {
        return tanks;
    }

    public void setTanks(List<FuelTank> tanks) {
        this.tanks = tanks;
    }

    public Double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(Double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public FuelType getFuelType() {
        return fuelType == null ? null : FuelType.fromId(fuelType);
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType == null ? null : fuelType.getId();
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public DriverLicenseCategory getDriverLicenseCategory() {
        return driverLicenseCategory == null ? null : DriverLicenseCategory.fromId(driverLicenseCategory);
    }

    public void setDriverLicenseCategory(DriverLicenseCategory driverLicenseCategory) {
        this.driverLicenseCategory = driverLicenseCategory == null ? null : driverLicenseCategory.getId();
    }

    public void setLongitude(Double longitude) {
        if (longitude != null && this.latitude != null) {
            setLocation(GeometryUtils.createPoint(this.latitude, longitude));
        }
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        if (latitude != null && this.longitude != null) {
            setLocation(GeometryUtils.createPoint(latitude, this.longitude));
        }
        this.latitude = latitude;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
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
    @DependsOnProperties({"model", "licensePlate"})
    public String getInstanceName(MetadataTools metadataTools) {
        return String.format("%s %s",
                metadataTools.format(model),
                metadataTools.format(licensePlate));
    }
}