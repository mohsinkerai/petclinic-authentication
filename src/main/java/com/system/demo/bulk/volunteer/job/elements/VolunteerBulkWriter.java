package com.system.demo.bulk.volunteer.job.elements;


import com.system.demo.volunteer.Volunteer;
import com.system.demo.volunteer.VolunteerRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * Created by Zeeshan Damani
 */

@AllArgsConstructor
@Component
public class VolunteerBulkWriter implements ItemWriter<Volunteer> {


    private final VolunteerRepository volunteerRepository;

    @Override
    public void write(List<? extends Volunteer> list) throws Exception {

        for (Volunteer volunteer : list) {
            System.out.println("Writing chunk to Database");
            System.out.println(volunteer.toString());
            volunteerRepository.save(volunteer);
        }


    }
}
