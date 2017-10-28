package org.springframework.samples.petclinic.vehicle;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    // Checkout Thymeleaf Date (Java8 Probably)
//    private String licenseIssue;
//    private String licenseExpiry;

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
//    private String driverLicenseIssue;
//    private String driverLicenseExpiry;
    private String driverWithOwner;

    private String make;
    private String model;
    private String registration;
    private String color;

    // Should be Enum
    private String category;

    private boolean enabled;

    public boolean isNew() {
        return this.id == null;
    }
}
