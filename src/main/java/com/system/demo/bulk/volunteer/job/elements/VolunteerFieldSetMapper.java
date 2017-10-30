package com.system.demo.bulk.volunteer.job.elements;

import com.system.demo.volunteer.Volunteer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * Created by Zeeshan Damani
 */
public class VolunteerFieldSetMapper implements FieldSetMapper<Volunteer> {

    public Volunteer mapFieldSet(FieldSet fieldSet) {

        Volunteer volunteer = new Volunteer();

        volunteer.setFormId(fieldSet.readString(0));
        volunteer.setRegistrationDate(fieldSet.readString(1));
        volunteer.setVolunteerName(fieldSet.readString(2));
        volunteer.setVolunteerCnic(fieldSet.readString(3));
        volunteer.setResidentialAddress(fieldSet.readString(4));
        volunteer.setOfficeAddress(fieldSet.readString(5));
        volunteer.setEmailAdddress(fieldSet.readString(6));
        volunteer.setHomePhone(fieldSet.readString(7));
        volunteer.setCellPhone(fieldSet.readString(8));
        volunteer.setDesignation(fieldSet.readString(9));
        volunteer.setReportsTo(fieldSet.readString(10));
        volunteer.setMemberOf(fieldSet.readString(11));
        volunteer.setJurisdiction(fieldSet.readString(12));
        volunteer.setLocalCouncil(fieldSet.readString(13));
        volunteer.setJamatKhanna(fieldSet.readString(14));
        volunteer.setInstitution(fieldSet.readString(15));
        volunteer.setSeviceYears(fieldSet.readString(16));
        volunteer.setAge(fieldSet.readString(17));

        return volunteer;
    }
}
