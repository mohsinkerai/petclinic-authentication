package com.system.demo.volunteer;

/**
 * Created by Zeeshan Damani
 */

import com.system.demo.volunteer.Council.Local;
import com.system.demo.volunteer.Council.Regional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "volunteers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "volunteer_form_no")
    String volunteerFormNo;

    @NotNull
    @Column(name = "volunteer_name")
    String volunteerName;

    @Column(name = "volunteer_registration_date")
    String registrationDate;

    @NotNull
    @Column(name = "volunteer_cnic")
    String volunteerCnic;

    @NotNull
    @Column(name = "volunteer_residential_address")
    String residentialAddress;

    @Column(name = "volunteer_email")
    String emailAdddress;

    @Column(name = "volunteer_home_phone")
    String homePhone;

    @Column(name = "volunteer_cell_phone")
    String cellPhone;

    @Column(name = "volunteer_category")
    VolunteerCategory category;

    @NotNull
    @Column(name = "regional_council")
    String regionalCouncil;

    @NotNull
    @Column(name = "local_council")
    String localCouncil;

    @Column(name = "volunteer_jamatkhanna")
    String jamatKhanna;

    @Column(name = "volunteer_institution")
    String institution;

    @Column(name = "volunteer_age")
    String age;

    @Column(name = "volunteer_duty_day")
    String dutyDay;

    @Column(name = "volunteer_duty_shift")
    String dutyShift;

    @Column(name = "volunteer_duty_zone")
    String dutyZone;

    @Column(name = "volunteer_image")
    String volunteerImage;

    @Column(name = "volunteer_memberof")
    String volunteerMemberOf;

    @Column(name = "volunteer_jurisdiction")
    String volunteerJurisdction;

    @Column(name = "volunteer_site")
    String volunteerSite;

    @Column(name = "volunteer_committee")
    String volunteerCommittee;

    @Column(name="volunteer_isprinted")
    boolean volunteerIsPrinted = false;
    private boolean isEnabled;

    public boolean validateCnic() {
        if (this.volunteerCnic.matches("(\\d){5}-(\\d){7}-(\\d){1}") && !this.volunteerCnic
            .equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateEmail() {
        if (this.emailAdddress.matches("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$")
            || this.emailAdddress.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateMobile() {
        if (this.cellPhone.matches("^(\\d){4}-(\\d){7}$") || this.cellPhone.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean valiateAge() {
        if (this.age.matches("^(\\d)+$") || !this.age.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean valiateResidentialAddress() {
        if (!this.residentialAddress.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean valiateHomePhone() {
        if (this.homePhone.equals("") || this.homePhone.matches("^(\\d){3}-(\\d){8}$")) {
            return true;
        } else {
            return false;
        }
    }
}
