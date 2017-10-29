package com.system.demo.vehicle;

import java.io.Serializable;
import java.util.Date;
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
    @NotNull
    private String firstname;
    private String fathername;
    private String surname;
    private String address;

    // Needs Regex
    @NotNull
    private String cnic;

    private String homePhone;
    private String cellPhone;

    private String license;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date licenseIssue;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date licenseExpiry;

    // Enable Boolean After Thymleaf Support
//    private boolean driverDrives;
    private String driverFirstname;
    private String driverFathername;
    private String driverSurname;
    private String driverAddress;
    private String driverCnic;
    private String driverHomePhone;
    private String driverCellPhone;
    private String driverLicense;
    // Checkout DATE

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date driverLicenseIssue;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date driverLicenseExpiry;
    private String driverWithOwner;

    private String make;
    private String model;
    private String registration;
    private String color;

    @Enumerated(EnumType.STRING)
    private VehicleType category;

    private boolean enabled;

    public boolean isNew() {
        return this.id == null;
    }
}
