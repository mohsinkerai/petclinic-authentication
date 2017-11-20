package com.system.demo.bulk;

import com.system.demo.bulk.vehicle.VehicleItemWriterListener;
import com.system.demo.bulk.volunteer.job.elements.VolunteerBulkProcessor;
import com.system.demo.bulk.volunteer.job.elements.listener.VolunteerItemReaderListener;
import com.system.demo.bulk.volunteer.job.elements.listener.VolunteerItemWriterListener;
import com.system.demo.bulk.volunteer.volunteerupadate.VolunteerUpdateProcessor;
import com.system.demo.vehicle.Vehicle;
import com.system.demo.volunteer.Volunteer;
import com.system.demo.bulk.volunteer.job.elements.listener.VolunteerJobNotificationListener;
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

import javax.annotation.Resource;

/** Created by Zeeshan Damani */
@Slf4j
@Component
@EnableBatchProcessing
public class BulkJobBuilder {

  private final FlatFileItemReader<Volunteer> volunteerUpdateReader;


  private final FlatFileItemReader<Volunteer> volunteerItemReader;

  private final StepBuilderFactory stepBuilderFactory;
  private final JobBuilderFactory jobBuilderFactory;
  private final JpaItemWriter<Volunteer> volunteerBulkWriter;
  private final VolunteerItemWriterListener itemLoggerListener;
  private final VehicleItemWriterListener vehicleItemWriterListener;
  private final VolunteerBulkProcessor volunteerBulkProcessor;
  private final VolunteerJobNotificationListener volunteerJobNotificationListener;
  private final FlatFileItemReader<Vehicle> vehicleFlatFileItemReader;
  private final JpaItemWriter<Vehicle> vehicleJpaItemWriter;
  private final VolunteerUpdateProcessor volunteerUpdateProcessor;
  private final VolunteerItemReaderListener volunteerItemReaderListener;

  public BulkJobBuilder(
      @Qualifier("volunteerUpdateItemReader")
      FlatFileItemReader<Volunteer> volunteerUpdateReader,
      @Qualifier("volunteerItemReader")
      FlatFileItemReader<Volunteer> volunteerItemReader,
      StepBuilderFactory stepBuilderFactory,
      JobBuilderFactory jobBuilderFactory,
      JpaItemWriter<Volunteer> volunteerBulkWriter,
      VolunteerItemWriterListener itemLoggerListener,
      VehicleItemWriterListener vehicleItemWriterListener,
      VolunteerBulkProcessor volunteerBulkProcessor,
      VolunteerJobNotificationListener volunteerJobNotificationListener,
      FlatFileItemReader<Vehicle> vehicleFlatFileItemReader,
      JpaItemWriter<Vehicle> vehicleJpaItemWriter,
      VolunteerUpdateProcessor volunteerUpdateProcessor,
      VolunteerItemReaderListener volunteerItemReaderListener) {
    this.volunteerUpdateReader = volunteerUpdateReader;
    this.volunteerItemReader = volunteerItemReader;
    this.stepBuilderFactory = stepBuilderFactory;
    this.jobBuilderFactory = jobBuilderFactory;
    this.volunteerBulkWriter = volunteerBulkWriter;
    this.itemLoggerListener = itemLoggerListener;
    this.vehicleItemWriterListener = vehicleItemWriterListener;
    this.volunteerBulkProcessor = volunteerBulkProcessor;
    this.volunteerJobNotificationListener = volunteerJobNotificationListener;
    this.vehicleFlatFileItemReader = vehicleFlatFileItemReader;
    this.vehicleJpaItemWriter = vehicleJpaItemWriter;
    this.volunteerUpdateProcessor = volunteerUpdateProcessor;
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

  public Job buildVehicleUpload(String jobName) {

    Step step =
        stepBuilderFactory
            .get(UploadTypes.VehicleUpload.toString())
            .<Vehicle, Vehicle>chunk(2)
            .reader(vehicleFlatFileItemReader)
            .writer(vehicleJpaItemWriter)
            .listener(vehicleItemWriterListener)
            .faultTolerant()
            .skipPolicy(new DatabaseJobSkipPolicy())
            .build();

    return jobBuilderFactory
        .get(jobName)
        .incrementer(new RunIdIncrementer())
        .listener(volunteerJobNotificationListener)
        .flow(step)
        .end()
        .build();
  }

  public Job updateVolunteer(String jobName) {

    Step step =
        stepBuilderFactory
            .get(UploadTypes.VehicleUpload.toString())
            .<Volunteer, Volunteer>chunk(2)
            .reader(volunteerUpdateReader)
            .processor(volunteerUpdateProcessor)
            .listener(vehicleItemWriterListener)
            .faultTolerant()
            .skipPolicy(new DatabaseJobSkipPolicy())
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
