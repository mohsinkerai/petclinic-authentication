package com.system.demo.bulk.volunteer.event;

import com.system.demo.bulk.BulkJobBuilder;
import com.system.demo.bulk.UploadTypes;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${file.path.zip.extract}")
    private String zipExtractionFolder;

    @Value("${file.path.data}")
    private String allDataFolder;

    @Value("${file.path.xlsm}")
    private String pythonScriptPath;

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
    public void onApplicationEvent(FileUploadEvent fileUploadEvent) throws InterruptedException,IOException{

        log.info("Received file event {}", fileUploadEvent);
        String path = fileUploadEvent.getFilePath().toFile().getAbsolutePath();
        try {
            String pythonCommand =
                "python " + pythonScriptPath + " \"" + path + "\"" + " \"" + zipExtractionFolder
                    + "\"";
            log.info("Executing python command {}", pythonCommand);
            Process exec = Runtime.getRuntime().exec(pythonCommand);
            while (exec.isAlive()) {
                // Waiting for Control
                Thread.sleep(100);
            }
            int exitCode = exec.exitValue();
            log.info("Execution of Python Script Output {}", exitCode);
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            arrayOutputStream.writeTo(exec.getOutputStream());
            log.info("Output of Python Script {}", arrayOutputStream.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String rootPath= new File(".").getCanonicalPath();

        File dataHouse = new File(rootPath + "\\datahouse\\"+ System.currentTimeMillis());
        dataHouse.mkdir();
        FileUtils.copyDirectory(new File(zipExtractionFolder),dataHouse);
        long userId = fileUploadEvent.getUserId();
        // We should copy/backup file after running job - maybe associate its name with jobId?
        String csvPath = dataHouse.getAbsolutePath() + "\\data.csv";
        log.info("Csv Path is {}", csvPath);
        Job volunteerBulkJob = bulkJobBuilder.buildVolunteerUpload(
            UploadTypes.VolunteerUpload.toString() + csvPath);
        Map<String, JobParameter> jobParamsMap = new HashMap<>();
        jobParamsMap.put("sourceFilePath", new JobParameter(csvPath));
        jobParamsMap.put("timestamp",
            new JobParameter(new Timestamp(System.currentTimeMillis()).getTime()));
        // TODO: Fill this Params
        jobParamsMap.put("pictureFlag", new JobParameter("True"));
        jobParamsMap.put("imagesSource", new JobParameter(dataHouse.getAbsolutePath()));
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
