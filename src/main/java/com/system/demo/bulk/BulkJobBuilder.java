package com.system.demo.bulk;

import com.system.demo.bulk.volunteer.job.elements.VolunteerBulkProcessor;
import com.system.demo.bulk.volunteer.job.elements.listener.VolunteerItemReaderListener;
import com.system.demo.bulk.volunteer.job.elements.listener.VolunteerItemWriterListener;
import com.system.demo.bulk.volunteer.job.elements.listener.VolunteerJobNotificationListener;
import com.system.demo.volunteer.Volunteer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by Zeeshan Damani
 */
@Slf4j
@Component
@EnableBatchProcessing
public class BulkJobBuilder {

    private final FlatFileItemReader<Volunteer> volunteerItemReader;

    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final JpaItemWriter<Volunteer> volunteerBulkWriter;
    private final VolunteerItemWriterListener itemLoggerListener;
    private final VolunteerBulkProcessor volunteerBulkProcessor;
    private final VolunteerJobNotificationListener volunteerJobNotificationListener;
    private final VolunteerItemReaderListener volunteerItemReaderListener;

    public BulkJobBuilder(
        @Qualifier("volunteerItemReader")
            FlatFileItemReader<Volunteer> volunteerItemReader,
        StepBuilderFactory stepBuilderFactory,
        JobBuilderFactory jobBuilderFactory,
        JpaItemWriter<Volunteer> volunteerBulkWriter,
        VolunteerItemWriterListener itemLoggerListener,
        VolunteerBulkProcessor volunteerBulkProcessor,
        VolunteerJobNotificationListener volunteerJobNotificationListener,
        VolunteerItemReaderListener volunteerItemReaderListener) {
        this.volunteerItemReader = volunteerItemReader;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobBuilderFactory = jobBuilderFactory;
        this.volunteerBulkWriter = volunteerBulkWriter;
        this.itemLoggerListener = itemLoggerListener;
        this.volunteerBulkProcessor = volunteerBulkProcessor;
        this.volunteerJobNotificationListener = volunteerJobNotificationListener;
        this.volunteerItemReaderListener = volunteerItemReaderListener;

    }

    public Job buildVolunteerUpload(String jobName) {

        Step step =
            stepBuilderFactory
                .get(jobName + " Step.")
                .<Volunteer, Volunteer>chunk(5)
                .reader(volunteerItemReader)
                .processor(volunteerBulkProcessor)
                .writer(volunteerBulkWriter)
                .listener(itemLoggerListener)
                .listener(volunteerItemReaderListener)
                .faultTolerant()
                .skipPolicy(new DatabaseJobSkipPolicy()).skipLimit(0)
                .build();

        return jobBuilderFactory
            .get(jobName)
            .incrementer(new RunIdIncrementer())
            .listener(volunteerJobNotificationListener)
            .flow(step)
            .end()
            .build();
    }
}
