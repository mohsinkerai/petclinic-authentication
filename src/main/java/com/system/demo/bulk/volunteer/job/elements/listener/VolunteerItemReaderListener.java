package com.system.demo.bulk.volunteer.job.elements.listener;

import com.system.demo.bulkprogress.jobdata.UserJobData;
import com.system.demo.bulkprogress.jobdata.UserJobService;
import com.system.demo.volunteer.Volunteer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Zeeshan Damani
 */
@Component
@Slf4j
@JobScope
public class VolunteerItemReaderListener implements ItemReadListener<Volunteer> {

    @Autowired
    UserJobService userJobService;

    @Value("#{jobExecution}")
    private JobExecution jobExecution;

    @Override
    public void beforeRead() {
    }

    @Override
    public void afterRead(Volunteer item) {
    }

    @Override
    public void onReadError(Exception ex) {
        UserJobData userJobData = userJobService.getUserJobDataByJobId(jobExecution.getId());
        userJobData.setJobStatus(BatchStatus.FAILED.toString());
        userJobService.save(userJobData);
        jobExecution.setExitStatus(new ExitStatus("FAILED","Invalid Input File "+ ex.getMessage()));
        jobExecution.stop();
    }
}
