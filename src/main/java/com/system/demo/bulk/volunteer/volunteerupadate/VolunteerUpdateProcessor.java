package com.system.demo.bulk.volunteer.volunteerupadate;

import com.system.demo.bulkprogress.itemdata.FailItemService;
import com.system.demo.volunteer.Volunteer;
import com.system.demo.volunteer.VolunteerService;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Zeeshan Damani
 */

@JobScope
@Component
public class VolunteerUpdateProcessor implements ItemProcessor<Volunteer,Volunteer> {

    @Autowired
    private VolunteerService volunteerService;

    @Value("#{jobExecution}")
    private JobExecution jobExecution;

    @Autowired
    private FailItemService failItemService;

    public Volunteer process(Volunteer v) throws Exception {

        Volunteer savedVolunteer = volunteerService.findByCnic(v.getVolunteerCnic()).get(0);

        savedVolunteer.setDutyZone(v.getDutyZone());
        savedVolunteer.setDutyDay(v.getDutyDay());
        savedVolunteer.setDutyShift(v.getDutyShift());
   //     volunteerService.updateVolunteer(savedVolunteer);
        return savedVolunteer;
    }

    }
