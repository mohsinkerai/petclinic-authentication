package com.system.demo.vehicle;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Entity(name = "vehicle")
@ToString
@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
public class Vehicle implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    //<editor-fold desc="Personal Information">
    @NotNull
    private String ownerName;

    @NotNull
    private String driverName;

    @NotNull
    @Column(unique = true)
    private String ownerCnic;

    @NotNull
    @Column(unique = true)
    private String driverCnic;

    @NotNull
    private String ownerMobile;

    @NotNull
    private String driverMobile;

    @NotNull
    private String ownerCurrentAddress;

    @NotNull
    private String driverCurrentAddress;

    private String ownerPermanentAddress;

    private String driverPermanentAddress;
    //</editor-fold>

    //<editor-fold desc="License Info">
    @NotNull
    private String license;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date licenseIssue;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date licenseExpiry;
    //</editor-fold>

    //<editor-fold desc="Car Information">
    @NotNull
    private String registration;
    private String chassisNumber;
    private String engineNumber;

    @NotNull
    private String make;
    private String model;
    @NotNull
    private String color;
    //</editor-fold>

    private boolean isOwnerDriver;

    private boolean enabled;

    public boolean isNew() {
        return this.id == null;
    }
}
