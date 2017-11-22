package com.system.demo.bulk.volunteer.job.elements;

import com.system.demo.volunteer.Volunteer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;


public class VolunteerFieldSetMapper implements FieldSetMapper<Volunteer> {

    public Volunteer mapFieldSet(FieldSet fieldSet) {
        Volunteer volunteer = new Volunteer();
        try {
            volunteer.setVolunteerFormNo(fieldSet.readString(0));
            //volunteer.setRegistrationDate(fieldSet.readString(1));
            volunteer.setVolunteerName(fieldSet.readString(1));
            volunteer.setVolunteerCnic(fieldSet.readString(2));
            //contact
            //volunteer.setResidentialAddress(fieldSet.readString(4));
            //volunteer.setEmailAdddress(fieldSet.readString(5));
            //volunteer.setHomePhone(fieldSet.readString(6));
            volunteer.setCellPhone(fieldSet.readString(4));
            volunteer.setVolunteerMemberOf(fieldSet.readString(5));
            volunteer.setRegionalCouncil(fieldSet.readString(6));
            //volunteer.setVolunteerJurisdction(fieldSet.readString(10));
            volunteer.setLocalCouncil(fieldSet.readString(7));
            volunteer.setJamatKhanna(fieldSet.readString(8));
            volunteer.setInstitution(fieldSet.readString(9));
            volunteer.setDutyDay(fieldSet.readString(10) != null ? fieldSet.readString(14) : "");
            //volunteer.setAge(fieldSet.readString(15));
            volunteer.setDutyShift(fieldSet.readString(11) != null ? fieldSet.readString(16) : "");
            volunteer.setDutyZone(fieldSet.readString(12) != null ? fieldSet.readString(17): "");
            volunteer.setVolunteerSite(fieldSet.readString(13));
           // volunteer.setVolunteerCommittee(fieldSet.readString(19));

            return volunteer;
        } catch (Exception ex) {
            volunteer.setDutyDay((""));
            volunteer.setDutyShift("");
            volunteer.setDutyZone( "");
            return volunteer;

        }
    }
}
