package com.system.demo.bulk.volunteer.job;

import com.system.demo.volunteer.Volunteer;
import com.system.demo.bulk.volunteer.job.elements.VolunteerBulkWriter;
import com.system.demo.bulk.volunteer.job.elements.listener.ItemLoggerListener;
import com.system.demo.bulk.volunteer.job.elements.listener.JobNotificationListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.stereotype.Component;

/**
 * Created by Zeeshan Damani
 */
@Slf4j
@AllArgsConstructor
@Component
@EnableBatchProcessing
public class BulkJobBuilder {

    private StepBuilderFactory stepBuilderFactory;
    private JobBuilderFactory jobBuilderFactory;
    private VolunteerBulkWriter volunteerBulkWriter;
    private FlatFileItemReader<Volunteer> volunteerItemReader;
    private ItemLoggerListener itemLoggerListener;
    private JobNotificationListener jobNotificationListener;

    public Job builBulkVolunteerJob(String jobName) {

        Step step = stepBuilderFactory.get(jobName + " Step.")
            .<Volunteer, Volunteer>chunk(2)
            .reader(volunteerItemReader)
            .writer(volunteerBulkWriter)
            .listener(itemLoggerListener)
            .build();

        return jobBuilderFactory.get(jobName)
            .incrementer(new RunIdIncrementer())
            .listener(jobNotificationListener)
            .flow(step)
            .end()
            .build();
    }


}
