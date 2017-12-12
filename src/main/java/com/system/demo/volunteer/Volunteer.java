package com.system.demo.volunteer;

/**
 * Created by Zeeshan Damani
 */

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Data
@Entity(name = "volunteers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "volunteer_form_no")
    String volunteerFormNo;

    @NotNull
    @Column(name = "volunteer_name")
    String volunteerName;

    @Column(name = "volunteer_registration_date")
    String registrationDate;

    @NotNull
    @Column(name = "volunteer_cnic", unique = true)
    String volunteerCnic;

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

    @Column(name = "regional_council")
    String regionalCouncil;

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

    @Column(name = "volunteer_isprinted")
    boolean volunteerIsPrinted = false;

    @Column(name = "volunteer_isprinteddate")
    Timestamp volunteerIsPrintedDate;

    @Transient
    private String picture;

    private boolean isPictureAvailable = false;

    private boolean isEnabled;

    public boolean isPictureAvailable() {
        return isPictureAvailable && volunteerImage != null && volunteerImage != "";
    }

    public boolean validateCnic() {
        if (StringUtils.hasLength(volunteerCnic) &&
            (this.volunteerCnic.matches("^[0-9]{5}-[0-9]{7}-[0-9]{1}$") || //NIC
             this.volunteerCnic.matches("^[0-9]{6}-[0-9]{6}-[0-9]{1}$") || //NICOP
             this.volunteerCnic.matches("^E[C,c,b,B]-[0-9]{11}$") ||
             this.volunteerCnic.matches("^E[C,c,b,B][0-9]{11}$") ||//Afghan Citizen
             this.volunteerCnic.matches("^[0-9]{5}-[0-9]{2}$") ||
             this.volunteerCnic.matches("^GL-[0-9]{12}$") ||
             this.volunteerCnic.matches("^GL[0-9]{12}$")
            )) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateName() {
        if (!this.volunteerName.equals("")) {
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
        if (this.cellPhone.matches("^(\\d){4}-(\\d){7}$") || this.cellPhone.equals("")
            || this.cellPhone.matches("^(\\d){11}$") || this.cellPhone.matches("^(\\d){10}$") ||
            this.cellPhone.matches("^(\\d){3}(\\s){1}(\\d){7}$")) {
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

    public boolean validateLocalCoucil(){
        if (StringUtils.hasLength(this.localCouncil)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateSite(){
        if (this.volunteerSite.equalsIgnoreCase("Alyabad") ||
            this.volunteerSite.equalsIgnoreCase("Garam Chashma") ||
            this.volunteerSite.equalsIgnoreCase("All Zone") ||
            this.volunteerSite.equalsIgnoreCase("Booni") ||
            this.volunteerSite.equalsIgnoreCase("Tause") ||
            this.volunteerSite.equalsIgnoreCase("Central") ||
            this.volunteerSite.equalsIgnoreCase("Karachi") ||
            this.volunteerSite.equalsIgnoreCase("Darkhana") ||
            this.volunteerSite.equalsIgnoreCase("Clifton")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateZone(){
        if (this.dutyZone.equalsIgnoreCase("Pandal") ||
            this.dutyZone.equalsIgnoreCase("Inner Cordon") ||
            this.dutyZone.equalsIgnoreCase("MHI Entourage") ||
            this.dutyZone.equalsIgnoreCase("Stage") ||
            this.dutyZone.equalsIgnoreCase("Main Gate") ||
            this.dutyZone.equalsIgnoreCase("Outer Cordon") ||
            this.dutyZone.equalsIgnoreCase("Sacrifice Duty") ||
            this.dutyZone.equalsIgnoreCase("Imamat Zone") ||
            this.dutyZone.equalsIgnoreCase("All Zone") ||
            this.dutyZone.equalsIgnoreCase("Pakistan")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateShift(){
        if (this.dutyShift.equalsIgnoreCase("All") ||
            this.dutyShift.equals("1") ||
            this.dutyShift.equals("2")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateDay(){
        if( !StringUtils.isEmpty(this.dutyDay)){
            if (this.dutyDay.equalsIgnoreCase("All") ||
                 this.dutyDay.replace(" ","").matches("[1-5]{1}") ||
                 this.dutyDay.replace(" ","").matches("^[1-6]{1},[2-6]{1},[3-6]{1},[3-6]{1}$") ||
                 this.dutyDay.replace(" ","").matches("^[1-6]{1},[2-6]{1},[3-6]{1}$") ||
                 this.dutyDay.replace(" ","").matches("^[1-6]{1},[2-6]{1}$") ||
                 this.dutyDay.replace(" ","").matches("^[1-6]{1}$") ||
                 this.dutyDay.equals("-")) {

                 return true;
            } else {
            return false;
            }
        } else {
            return true;
        }
    }

    public boolean validateCommittee() {
        if (this.volunteerCommittee.equalsIgnoreCase("Darbar") ||
            this.volunteerCommittee.equalsIgnoreCase("Security")) {
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

    public boolean isValidForPrint() {
        boolean isValid =
            this.isPictureAvailable
                && volunteerImage != null
                && !volunteerIsPrinted
                && volunteerName != null
                && validateCnic()
                && volunteerCommittee != null
                && volunteerSite != null
                && dutyZone != null
                && localCouncil != null;

        if ((volunteerSite.equalsIgnoreCase("Central") || volunteerSite
            .equalsIgnoreCase("Southern")) && !(isValid &&
            !StringUtils.isEmpty(dutyShift) && !StringUtils.isEmpty(dutyDay))) {
            isValid = false;
        }


        if (!isValid) {
            log.info("Record id {} is invalid for Print {}", id, this);
        }

        return isValid;
    }

    public boolean isNew() {
        return this.id == null;
    }
}
