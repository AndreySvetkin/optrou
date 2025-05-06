package com.svetkin.optrou.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.annotation.DeletedBy;
import io.jmix.core.annotation.DeletedDate;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDelete;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.maps.converter.wkt.PointWKTConverter;
import io.jmix.maps.utils.GeometryUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "OPTROU_FUEL_STATION")
@Entity(name = "optrou_FuelStation")
public class FuelStation {
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

    @Column(name = "STATION_ID")
    private String stationId;

    @InstanceName
    @Column(name = "NAME")
    private String name;

    @JoinColumn(name = "BRAND_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private FuelStationBrand brand;

    @Column(name = "IS_ENABLED")
    private Boolean isEnabled;

    @Column(name = "REGION")
    private String region;

    @Column(name = "ADDRESS")
    private String address;

    @Convert(converter = PointWKTConverter.class)
    @Column(name = "LOCATION")
    private Point location;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    @OneToMany(mappedBy = "fuelStation")
    private List<FuelStationPrice> prices;

    public void setBrand(FuelStationBrand brand) {
        this.brand = brand;
    }

    public FuelStationBrand getBrand() {
        return brand;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        if (longitude != null && this.latitude != null) {
            setLocation(GeometryUtils.createPoint(this.latitude, longitude));
        }
        this.longitude = longitude;
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

    public void setLocation(Point location) {
        this.location = location;
    }

    public Point getLocation() {
        return location;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public List<FuelStationPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<FuelStationPrice> prices) {
        this.prices = prices;
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
}