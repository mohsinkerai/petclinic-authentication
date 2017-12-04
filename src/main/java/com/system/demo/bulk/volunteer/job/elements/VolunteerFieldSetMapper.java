package com.system.demo.bulk.volunteer.job.elements;

import com.system.demo.volunteer.Volunteer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VolunteerFieldSetMapper implements FieldSetMapper<Volunteer> {


    @Value("${header.volunteerformno}")
    String formNo;

    @Autowired
    private HeaderConvertor headerConvertor;

    public Volunteer mapFieldSet(FieldSet fieldSet) {
        Volunteer volunteer = new Volunteer();
        try {
            Map<String, String> map = (Map)(fieldSet.getProperties());

            volunteer.setVolunteerFormNo(fieldSet.readString(0));

          ///  String temp = headerConvertor.getKeyForForm(map);
          //  volunteer.setVolunteerFormNo(temp.equals("") ? fieldSet.readString(0): temp );
            //volunteer.setRegistrationDate(fieldSet.readString(1));
            volunteer.setVolunteerFormNo(fieldSet.readString(0));
            volunteer.setVolunteerName(fieldSet.readString(1));
            volunteer.setVolunteerCnic(fieldSet.readString(2));
            //contact
            //volunteer.setResidentialAddress(fieldSet.readString(4));
            //volunteer.setEmailAdddress(fieldSet.readString(5));
            //volunteer.setHomePhone(fieldSet.readString(6));
            volunteer.setCellPhone(fieldSet.readString(3));
            volunteer.setVolunteerMemberOf(fieldSet.readString(4));
            volunteer.setRegionalCouncil(fieldSet.readString(5));
            //volunteer.setVolunteerJurisdction(fieldSet.readString(10));
            volunteer.setLocalCouncil(fieldSet.readString(6));
            volunteer.setJamatKhanna(fieldSet.readString(7));
            volunteer.setInstitution(fieldSet.readString(8));
            volunteer.setDutyDay(fieldSet.readString(9) != null ? fieldSet.readString(9) : "");
            //volunteer.setAge(fieldSet.readString(15));
            volunteer.setDutyShift(fieldSet.readString(10) != null ? fieldSet.readString(10) : "");
            volunteer.setDutyZone(fieldSet.readString(11) != null ? fieldSet.readString(11): "");
            volunteer.setVolunteerSite(fieldSet.readString(12));
            volunteer.setVolunteerCommittee(fieldSet.readString(13));
            volunteer.setAge(fieldSet.readString(14));
            return volunteer;
        } catch (Exception ex) {
            volunteer.setDutyDay("");
            volunteer.setDutyShift("");
            volunteer.setDutyZone( "");
                volunteer.setVolunteerCommittee("");
            volunteer.setAge("");
            return volunteer;

        }
    }

//    private String getKey(){
//
//    }
}
