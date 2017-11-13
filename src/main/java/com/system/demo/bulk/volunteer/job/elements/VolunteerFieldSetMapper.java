package com.system.demo.bulk.volunteer.job.elements;

import com.system.demo.volunteer.Volunteer;
import com.system.demo.volunteer.VolunteerCategory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;


public class VolunteerFieldSetMapper implements FieldSetMapper<Volunteer> {

    public Volunteer mapFieldSet(FieldSet fieldSet) {
        Volunteer volunteer = new Volunteer();
        try {
            volunteer.setFormId(fieldSet.readString(0));
            volunteer.setRegistrationDate(fieldSet.readString(1));
            volunteer.setVolunteerName(fieldSet.readString(2));
            volunteer.setVolunteerCnic(fieldSet.readString(3));
            volunteer.setAge(fieldSet.readString(4));
            volunteer.setResidentialAddress(fieldSet.readString(5));
            volunteer.setEmailAdddress(fieldSet.readString(6));
            volunteer.setHomePhone(fieldSet.readString(7));
            volunteer.setCellPhone(fieldSet.readString(8));
            volunteer.setCategory(VolunteerCategory.valueOf(fieldSet.readString(9)));
            volunteer.setRegionalCouncil(fieldSet.readString(10));
            volunteer.setLocalCouncil(fieldSet.readString(11));
            volunteer.setJamatKhanna(fieldSet.readString(12));
            volunteer.setInstitution(fieldSet.readString(13));
            volunteer.setDutyDay(fieldSet.readString(14) != null ? fieldSet.readString(14) : "");
            volunteer.setDutyShift(fieldSet.readString(15) != null ? fieldSet.readString(15) : "");
            volunteer.setDutyZone(fieldSet.readString(16) != null ? fieldSet.readString(16) : "");

            return volunteer;

        } catch (ArrayIndexOutOfBoundsException ex) {
            volunteer.setDutyDay((""));
            volunteer.setDutyShift("");
            volunteer.setDutyZone( "");
            return volunteer;

        }
    }
}
