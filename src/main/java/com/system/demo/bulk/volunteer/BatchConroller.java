package com.system.demo.bulk.volunteer;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.system.demo.bulk.volunteer.job.BulkJobBuilder;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Zeeshan Damani
 */

@Slf4j
@RestController
@RequestMapping(value = "/upload")
public class BatchConroller {

    @Autowired
    BulkJobBuilder bulkJobBuilder;

    @Autowired
    private JobRegistry jobRegistry;
    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private JobLauncher jobLauncher;

    @RequestMapping(value = "/volunteers", method = POST)
    public String uploadVolunteers(
        @RequestParam String path,
        @RequestParam String picFlag) {

        try {
            if (jobExplorer.findRunningJobExecutions("bulkVolunteerInsert to " + path).isEmpty()) {
                Job volunteerBulkJob = bulkJobBuilder.builBulkVolunteerJob
                    ("bulkVolunteerInsert to " + path);

                Map<String, JobParameter> jobParamsMap = new HashMap<>();

                JobParameter filePathJobParam = new JobParameter("classpath:" + path);
                jobParamsMap.put("sourceFilePath", filePathJobParam);
                jobParamsMap.put("timestamp",
                    new JobParameter(new Timestamp(System.currentTimeMillis()).getTime()));
                JobParameters bulkJobsParameters = new JobParameters(jobParamsMap);

                jobLauncher.run(volunteerBulkJob, bulkJobsParameters);

            } else {
                //        JobExecution jobExecution =  jobExplorer.findRunningJobExecutions("bulkVolunteerInsert to " + path);
                return "unable to upload " + path + ", One upload is already running ";
            }

            return "Hello , I am good  :) !";

        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }
}
