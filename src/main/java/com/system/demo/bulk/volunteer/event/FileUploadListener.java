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

    private String zipExtractionFolder;

    @Value("${file.path.data}")
    private String allDataFolder;

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
    public void onApplicationEvent(FileUploadEvent fileUploadEvent)
        throws InterruptedException, IOException {

        String rootPath = new File(".").getCanonicalPath();
//        pythonScriptPath = rootPath + "\\src\\main\\resources\\xlsmToCsv.py";
        pythonScriptPath = new StringBuilder(rootPath)
            .append(File.separator)
            .append("src")
            .append(File.separator)
            .append("main")
            .append(File.separator)
            .append("resources")
            .append(File.separator)
            .append("xlsmToCsv.py")
            .toString();
        File dataHouse = new File(
            rootPath + File.separator + "datahouse" + File.separator + System.currentTimeMillis());
        log.info("Received file event {}", fileUploadEvent);
        String path = fileUploadEvent.getFilePath().toFile().getAbsolutePath();
        dataHouse.mkdir();
        try {
            String pythonCommand =
                "python " + pythonScriptPath + " \"" + path + "\"" + " \"" + dataHouse
                    .getCanonicalPath()
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
        long userId = fileUploadEvent.getUserId();
        boolean allowNicDuplication = fileUploadEvent.isNicDuplication();
        // We should copy/backup file after running job - maybe associate its name with jobId?
        String csvPath = dataHouse.getAbsolutePath() + File.separator + "data.csv";
        log.info("Csv Path is {}", csvPath);
        Job volunteerBulkJob = bulkJobBuilder.buildVolunteerUpload(
            UploadTypes.VolunteerUpload.toString() + csvPath);
        Map<String, JobParameter> jobParamsMap = new HashMap<>();
        jobParamsMap.put("sourceFilePath", new JobParameter(csvPath));
        jobParamsMap.put("timestamp",
            new JobParameter(new Timestamp(System.currentTimeMillis()).getTime()));
        // TODO: Fill this Params
        jobParamsMap
            .put("pictureFlag", new JobParameter(fileUploadEvent.isPictureAvailable() + ""));
        jobParamsMap.put("imagesSource", new JobParameter(dataHouse.getAbsolutePath()));
        jobParamsMap.put("userId", new JobParameter(userId));
        jobParamsMap.put("allowNicDuplication" , new JobParameter(allowNicDuplication + ""));
        JobParameters bulkJobsParameters = new JobParameters(jobParamsMap);
        try {
            jobLauncher.run(volunteerBulkJob, bulkJobsParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
            JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
