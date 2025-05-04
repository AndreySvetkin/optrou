package com.svetkin.optrou.entity;

import com.svetkin.optrou.entity.type.DriverLicenseCategory;
import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JmixEntity
@Table(name = "OPTROU_DRIVER_LICENCE_CATEGORY_RELATION", indexes = {
        @Index(name = "IDX_OPTROU_DRIVER_LICENCE_CATEGORY_RELATION_DRIVER", columnList = "DRIVER_ID")
})
@Entity(name = "optrou_DriverLicenceCategoryRelation")
public class DriverLicenceCategoryRelation {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    @JoinColumn(name = "DRIVER_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Driver driver;

    @Column(name = "LICENSE_CATEGORY", nullable = false)
    @NotNull
    private String licenseCategory;

    public DriverLicenseCategory getLicenseCategory() {
        return licenseCategory == null ? null : DriverLicenseCategory.fromId(licenseCategory);
    }

    public void setLicenseCategory(DriverLicenseCategory licenseCategory) {
        this.licenseCategory = licenseCategory == null ? null : licenseCategory.getId();
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
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