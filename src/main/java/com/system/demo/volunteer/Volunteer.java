package com.system.demo.volunteer;

/**
 * Created by Zeeshan Damani
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    long id;

    @Column(name = "volunteer_form_id")
    String formId;

    @Column(name = "volunteer_registration_date")
    String registrationDate;

    @Column(name = "volunteer_name")
    String volunteerName;

    @Column(name = "volunteer_cnic")
    String volunteerCnic;

    @Column(name = "volunteer_residential_address")
    String residentialAddress;

    @Column(name = "volunter_office_address")
    String officeAddress;

    @Column(name = "volunteer_email")
    String emailAdddress;

    @Column(name = "volunteer_home_phone")
    String homePhone;

    @Column(name = "volunteer_cell_phone")
    String cellPhone;

    @Column(name = "volunteer_designation")
    String designation;

    @Column(name = "volunteer_reports_to")
    String reportsTo;

    @Column(name = "volunteer_member_of")
    String memberOf;

    @Column(name = "volunteer_jurisdiction")
    String jurisdiction;

    @Column(name = "volunteer_local")
    String localCouncil;

    @Column(name = "volunteer_jamatkhanna   ")
    String jamatKhanna;

    @Column(name = "volunteer_institution")
    String institution;

    @Column(name = "volunteer_service")
    String seviceYears;

    @Column(name = "volunteer_age")
    String age;

    @Column(name = "volunteer_category")
    VolunteerCategory category;
}
