package com.system.demo.bulk.volunteer.job.elements.listener;

import com.system.demo.bulkprogress.itemdata.FailItemService;
import com.system.demo.bulkprogress.itemdata.FailItems;
import com.system.demo.bulkprogress.jobdata.UserJobData;
import com.system.demo.vehicle.Vehicle;
import com.system.demo.volunteer.Volunteer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Zeeshan Damani
 */
@Slf4j
@Component
@JobScope
public class VolunteerItemWriterListener implements ItemWriteListener<Volunteer> {

    @Autowired
    FailItemService failItemService;

    @Value("#{jobExecution}")
    private JobExecution jobExecution;

    @Override
    public void beforeWrite(List<? extends Volunteer> items) {
        log.info("Inserting Some Volunteers: ");
    }

    @Override
    public void afterWrite(List<? extends Volunteer> items) {
        log.info("Inserted Some Volunteers: ");

    }

    @Override
    public void onWriteError(Exception ex, List<? extends Volunteer> list) {
        Volunteer ItemsFailed;
        if (list.size() == 1) {
            ItemsFailed = list.get(0);
            recordVolunteerWriteError(ex.getMessage(),ItemsFailed);
            log.info(ItemsFailed.getVolunteerCnic());
            log.info(ex.getCause().getMessage());
        }
    }
    private void recordVolunteerWriteError(String message, Volunteer volunteer){
        UserJobData userJobData = (UserJobData)jobExecution.getExecutionContext().get("userJobData");

        FailItems failItem = FailItems.builder()
            .failureReason(message)
            .failedItems(volunteer.getVolunteerCnic())
            .userJobId(userJobData.getId())
            .build();
        failItemService.save(failItem);
    }
}
