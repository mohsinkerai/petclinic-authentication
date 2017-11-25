package com.system.demo.bulk.volunteer.job.elements.listener;

import com.system.demo.bulk.UploadTypes;
import com.system.demo.bulkprogress.jobdata.UserJobData;
import com.system.demo.bulkprogress.jobdata.UserJobService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class VolunteerJobNotificationListener extends JobExecutionListenerSupport {
    @Autowired
    UserJobService userJobService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void afterJob(JobExecution jobExecution) {
       try {
           Integer totalWriteCount = 0;
           Integer totalProcessItems = 0;
           Integer totalSkipProcessCount = 0;
           Integer totalWriteSkipCount = 0;
           UserJobData userJobData = userJobService.getUserJobDataByJobId(jobExecution.getId());
           for(StepExecution stepExecution : jobExecution.getStepExecutions())
           {
               totalProcessItems += stepExecution.getReadCount();
               totalWriteCount += stepExecution.getWriteCount();
               totalSkipProcessCount += stepExecution.getProcessSkipCount();
               totalWriteSkipCount += stepExecution.getWriteSkipCount();
           }
           String path = jobExecution.getJobParameters().getString("sourceFilePath");
           if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
               userJobData.setJobStatus(BatchStatus.COMPLETED.toString());
                userJobService.save(userJobData);
               log.info("CSV File Upload Finished follwing Data are inserted of file " + resourceLoader.getResource(path).getFilename());
               log.info("Total data read from file  : " + totalProcessItems);
               log.info("Succefully Updated data : " + totalWriteCount);
               log.info("Number of Items Skip on read from file  : " + totalSkipProcessCount);
               log.info("Number of Items Skip on write from file  : " + totalWriteSkipCount);

               userJobData.setJobTotalItems(totalProcessItems);
               userJobData.setJobFailureItems(totalProcessItems - totalWriteCount);
               userJobData.setJobSucessItems(totalWriteCount);
               userJobService.save(userJobData);

           } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
               log.info("CSV file Uploading Failed of job  " + jobExecution.getJobId());

               userJobData.setJobTotalItems(totalProcessItems);
               userJobData.setJobFailureItems(totalSkipProcessCount);
               userJobData.setJobSucessItems(totalWriteCount);
               userJobService.save(userJobData);
               userJobData.setJobStatus(BatchStatus.FAILED.toString());
           }
           else {
               userJobData.setJobStatus(BatchStatus.FAILED.toString());
               userJobService.save(userJobData);
           }
       }catch (Exception e){
           log.info(e.toString());
       }
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Starting Upload");
        Long userId = jobExecution.getJobParameters().getLong("userId");
        UserJobData userJobData = UserJobData.builder()
            .jobId(jobExecution.getId())
            .jobName(jobExecution.getJobParameters().getString("sourceFilePath"))
            .jobType(UploadTypes.VolunteerUpload.toString())
            .jobStatus(jobExecution.getStatus().toString())
            .userId(userId)
            .build();

       UserJobData createdUserJobData = userJobService.save(userJobData);
       jobExecution.getExecutionContext().put("userJobData",createdUserJobData);
    }
}
