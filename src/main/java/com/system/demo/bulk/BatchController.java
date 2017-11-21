package com.system.demo.bulk;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.system.demo.system.config.WebSecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Zeeshan Damani
 */

@Slf4j
@RestController
@RequestMapping(value = "/upload")
public class BatchController {

    @Autowired
    BulkJobBuilder bulkJobBuilder;

    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private JobLauncher jobLauncher;

    @Value("${file.path.zip.extract}")
    private String zipExtractionFolder;

    @Value("${python.script.path}")
    String pyPath;

    @RequestMapping(value = "/volunteers", method = POST)
    public String uploadVolunteers(@RequestParam String path,
                                   @RequestParam String picFlag,
                                   @RequestParam String ImageSource) {
        try {

            Runtime.getRuntime().exec("python "+pyPath);
            if (jobExplorer.findRunningJobExecutions(UploadTypes.VolunteerUpload.toString() + path).isEmpty()) {
                Job volunteerBulkJob = bulkJobBuilder.buildVolunteerUpload(UploadTypes.VolunteerUpload.toString() + path);
                Map<String, JobParameter> jobParamsMap = new HashMap<>();
                jobParamsMap.put("sourceFilePath", new JobParameter(path));
                jobParamsMap.put("timestamp",
                    new JobParameter(new Timestamp(System.currentTimeMillis()).getTime()));
                jobParamsMap.put("pictureFlag",new JobParameter(picFlag));
                jobParamsMap.put("imagesSource", new JobParameter(ImageSource));
                JobParameters bulkJobsParameters = new JobParameters(jobParamsMap);
                JobExecution jobExecution =  jobLauncher.run(volunteerBulkJob, bulkJobsParameters);
            } else {
                return "unable to upload " + path + ", One upload is already running ";
            }
            return "Hello , I am good  :) !";
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }

    @RequestMapping(value = "/vehicle", method = POST)
    public String uploadVehicles(@RequestParam String path) {
        try {
            if (jobExplorer.findRunningJobExecutions(UploadTypes.VehicleUpload.toString() + path).isEmpty()) {
                Job vehicleUpload = bulkJobBuilder.buildVehicleUpload(UploadTypes.VehicleUpload.toString() + path);
                Map<String, JobParameter> jobParamsMap = new HashMap<>();
                jobParamsMap.put("sourceFilePath", new JobParameter(path));
                jobParamsMap.put("timestamp",
                    new JobParameter(new Timestamp(System.currentTimeMillis()).getTime()));
                JobParameters bulkJobsParameters = new JobParameters(jobParamsMap);
                JobExecution jobExecution =  jobLauncher.run(vehicleUpload, bulkJobsParameters);
            } else {
                return "unable to upload " + path + ", One upload is already running ";
            }
            return "Hello , I am good  :) !";
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }

    @RequestMapping(value = "/update/vounteer", method = POST)
    public String updateVolunteer(@RequestParam String path) {
        try {
            if (jobExplorer.findRunningJobExecutions(UploadTypes.VehicleUpload.toString() + path).isEmpty()) {
                Job vehicleUpload = bulkJobBuilder.updateVolunteer(UploadTypes.VolunteerAssignedUpload + path);
                Map<String, JobParameter> jobParamsMap = new HashMap<>();
                jobParamsMap.put("sourceFilePath", new JobParameter(path));
                jobParamsMap.put("timestamp",
                    new JobParameter(new Timestamp(System.currentTimeMillis()).getTime()));
                JobParameters bulkJobsParameters = new JobParameters(jobParamsMap);
                JobExecution jobExecution =  jobLauncher.run(vehicleUpload, bulkJobsParameters);
            } else {
                return "unable to upload " + path + ", One upload is already running ";
            }
            return "Hello , I am good  :) !";
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }
}
