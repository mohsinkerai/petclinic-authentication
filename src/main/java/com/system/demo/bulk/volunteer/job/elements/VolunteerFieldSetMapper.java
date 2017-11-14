package com.system.demo.bulk.volunteer.job.elements;

import com.system.demo.volunteer.Council;
import com.system.demo.volunteer.Volunteer;
import com.system.demo.volunteer.VolunteerCategory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;


public class VolunteerFieldSetMapper implements FieldSetMapper<Volunteer> {

    public Volunteer mapFieldSet(FieldSet fieldSet) {
        Volunteer volunteer = new Volunteer();
        try {
            volunteer.setRegistrationDate(fieldSet.readString(0));
            volunteer.setVolunteerName(fieldSet.readString(1));
            volunteer.setVolunteerCnic(fieldSet.readString(2));
            volunteer.setAge(fieldSet.readString(3));
            volunteer.setResidentialAddress(fieldSet.readString(4));
            volunteer.setEmailAdddress(fieldSet.readString(5));
            volunteer.setHomePhone(fieldSet.readString(6));
            volunteer.setCellPhone(fieldSet.readString(7));
            volunteer.setCategory(VolunteerCategory.valueOf(fieldSet.readString(8)));
            volunteer.setRegionalCouncil(Council.Regional.valueOf(fieldSet.readString(9)));
            volunteer.setLocalCouncil(Council.Local.valueOf(fieldSet.readString(10)));
            volunteer.setJamatKhanna(fieldSet.readString(11));
            volunteer.setInstitution(fieldSet.readString(12));
            volunteer.setDutyDay(fieldSet.readString(13) != null ? fieldSet.readString(13) : "");
            volunteer.setDutyShift(fieldSet.readString(14) != null ? fieldSet.readString(14) : "");
            volunteer.setDutyZone(fieldSet.readString(15) != null ? fieldSet.readString(15) : "");

            return volunteer;

        } catch (Exception ex) {
            volunteer.setDutyDay((""));
            volunteer.setDutyShift("");
            volunteer.setDutyZone( "");
            return volunteer;

        }
    }
}
