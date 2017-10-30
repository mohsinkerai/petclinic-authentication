package com.system.demo.bulk.volunteer.event;

import com.system.demo.bulk.volunteer.job.BulkJobBuilder;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileUploadListener {

    private final BulkJobBuilder bulkJobBuilder;
    private final JobRegistry jobRegistry;
    private final JobExplorer jobExplorer;
    private final JobLauncher jobLauncher;

    public FileUploadListener(BulkJobBuilder bulkJobBuilder,
        JobRegistry jobRegistry, JobExplorer jobExplorer,
        JobLauncher jobLauncher) {
        this.bulkJobBuilder = bulkJobBuilder;
        this.jobRegistry = jobRegistry;
        this.jobExplorer = jobExplorer;
        this.jobLauncher = jobLauncher;
    }

    @Async
    @EventListener
    public void onApplicationEvent(FileUploadEvent fileUploadEvent) {
        log.info("Received file event {}", fileUploadEvent.getFilePath().toString());

        String path = fileUploadEvent.getFilePath().toFile().getAbsolutePath();

        Job volunteerBulkJob = bulkJobBuilder.builBulkVolunteerJob
            ("bulkVolunteerInsert to " + path);

        Map<String, JobParameter> jobParamsMap = new HashMap<>();

        JobParameter filePathJobParam = new JobParameter(path);
        jobParamsMap.put("sourceFilePath", filePathJobParam);
        jobParamsMap.put("timestamp",
            new JobParameter(new Timestamp(System.currentTimeMillis()).getTime()));
        JobParameters bulkJobsParameters = new JobParameters(jobParamsMap);

        try {
            jobLauncher.run(volunteerBulkJob, bulkJobsParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
