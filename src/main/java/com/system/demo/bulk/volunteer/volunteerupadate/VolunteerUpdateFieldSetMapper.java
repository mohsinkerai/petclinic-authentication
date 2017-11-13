package com.system.demo.bulk.volunteer.volunteerupadate;

import com.system.demo.volunteer.Volunteer;
import com.system.demo.volunteer.VolunteerCategory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * Created by Zeeshan Damani
 */
public class VolunteerUpdateFieldSetMapper implements FieldSetMapper<Volunteer> {

    public Volunteer mapFieldSet(FieldSet fieldSet) {
        Volunteer volunteer = new Volunteer();
        try {
            volunteer.setVolunteerName(fieldSet.readString(0));
            volunteer.setVolunteerCnic(fieldSet.readString(1));
            volunteer.setAge(fieldSet.readString(2));
            volunteer.setCellPhone(fieldSet.readString(3));
            volunteer.setCategory(VolunteerCategory.valueOf(fieldSet.readString(4)));
            volunteer.setInstitution(fieldSet.readString(5));
            volunteer.setDutyDay(fieldSet.readString(6) != null ? fieldSet.readString(14) : "");
            volunteer.setDutyShift(fieldSet.readString(7) != null ? fieldSet.readString(15) : "");
            volunteer.setDutyZone(fieldSet.readString(8) != null ? fieldSet.readString(16) : "");

            return volunteer;

        } catch (ArrayIndexOutOfBoundsException ex) {
            volunteer.setDutyDay((""));
            volunteer.setDutyShift("");
            volunteer.setDutyZone( "");
            return volunteer;

        }
    }
}
