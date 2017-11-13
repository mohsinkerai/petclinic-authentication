package com.system.demo.bulk;

import com.system.demo.bulk.vehicle.VehicleItemWriterListener;
import com.system.demo.bulk.volunteer.job.elements.VolunteerBulkProcessor;
import com.system.demo.bulk.volunteer.job.elements.listener.VolunteerItemWriterListener;
import com.system.demo.bulk.volunteer.volunteerupadate.VolunteerUpdateProcessor;
import com.system.demo.vehicle.Vehicle;
import com.system.demo.volunteer.Volunteer;
import com.system.demo.bulk.volunteer.job.elements.listener.VolunteerJobNotificationListener;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Component
@EnableBatchProcessing
public class BulkJobBuilder {

    private StepBuilderFactory stepBuilderFactory;
    private JobBuilderFactory jobBuilderFactory;
    private JpaItemWriter<Volunteer> volunteerBulkWriter;
    @Qualifier("volunteerItemReader")
    private FlatFileItemReader<Volunteer> volunteerItemReader;
    private VolunteerItemWriterListener itemLoggerListener;
    private VehicleItemWriterListener vehicleItemWriterListener;
    private VolunteerBulkProcessor volunteerBulkProcessor;
    private VolunteerJobNotificationListener volunteerJobNotificationListener;
    private FlatFileItemReader<Vehicle> vehicleFlatFileItemReader;
    private JpaItemWriter<Vehicle> vehicleJpaItemWriter;
    @Qualifier("volunteerUpdateItemReader")
    private FlatFileItemReader<Volunteer>  volunteerUpdateReader;
    private VolunteerUpdateProcessor volunteerUpdateProcessor;


    public Job buildVolunteerUpload(String jobName) {

        Step step = stepBuilderFactory.get(jobName + " Step.")
            .<Volunteer, Volunteer>chunk(2)
            .reader(volunteerItemReader)
            .processor(volunteerBulkProcessor)
            .writer(volunteerBulkWriter)
            .listener(itemLoggerListener)
            .faultTolerant().skipPolicy(new DatabaseJobSkipPolicy())
            .build();

        return jobBuilderFactory.get(jobName)
            .incrementer(new RunIdIncrementer())
            .listener(volunteerJobNotificationListener)
            .flow(step)
            .end()
            .build();
    }

    public Job buildVehicleUpload(String jobName) {

        Step step = stepBuilderFactory.get(UploadTypes.VehicleUpload.toString())
            .<Vehicle, Vehicle>chunk(2)
            .reader(vehicleFlatFileItemReader)
            .writer(vehicleJpaItemWriter)
            .listener(vehicleItemWriterListener)
            .faultTolerant().skipPolicy(new DatabaseJobSkipPolicy())
            .build();

        return jobBuilderFactory.get(jobName)
            .incrementer(new RunIdIncrementer())
            .listener(volunteerJobNotificationListener)
            .flow(step)
            .end()
            .build();
    }


    public Job updateVolunteer(String jobName) {

        Step step = stepBuilderFactory.get(UploadTypes.VehicleUpload.toString())
            .<Volunteer, Volunteer>chunk(2)
            .reader(volunteerUpdateReader)
            .processor(volunteerUpdateProcessor)
            .listener(vehicleItemWriterListener)
            .faultTolerant().skipPolicy(new DatabaseJobSkipPolicy())
            .build();

        return jobBuilderFactory.get(jobName)
            .incrementer(new RunIdIncrementer())
            .listener(volunteerJobNotificationListener)
            .flow(step)
            .end()
            .build();
    }



}
