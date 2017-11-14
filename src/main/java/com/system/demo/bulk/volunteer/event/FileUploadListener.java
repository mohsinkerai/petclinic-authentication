package com.system.demo.bulk.volunteer.event;

import com.system.demo.bulk.BulkJobBuilder;
import com.system.demo.bulk.UploadTypes;
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
        log.info("Received file event {}", fileUploadEvent);

        long userId = fileUploadEvent.getUserId();
        String path = fileUploadEvent.getFilePath().toFile().getAbsolutePath();

        // TODO: Exec Python Command Here.
        Job volunteerBulkJob = bulkJobBuilder.buildVolunteerUpload(
            UploadTypes.VolunteerUpload.toString() + fileUploadEvent.getFilePath().getFileName());

        Map<String, JobParameter> jobParamsMap = new HashMap<>();
        jobParamsMap.put("sourceFilePath", new JobParameter(path));
        jobParamsMap.put("timestamp",
            new JobParameter(new Timestamp(System.currentTimeMillis()).getTime()));
        // TODO: Fill this Params
//        jobParamsMap.put("pictureFlag", new JobParameter(picFlag));
//        jobParamsMap.put("imagesSource", new JobParameter(ImageSource));
        jobParamsMap.put("userId", new JobParameter(userId));
        JobParameters bulkJobsParameters = new JobParameters(jobParamsMap);
        try {
            jobLauncher.run(volunteerBulkJob, bulkJobsParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
            JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
