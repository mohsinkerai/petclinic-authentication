package com.system.demo.bulk.volunteer.job.elements.listener;

import com.system.demo.volunteer.VolunteerRepository;
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
public class JobNotificationListener extends JobExecutionListenerSupport {


    private final VolunteerRepository volunteerRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void afterJob(JobExecution jobExecution) {
       try {

           String path = jobExecution.getJobParameters().getString("sourceFilePath");

           if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                Integer totalWriteCount = 0;
                Integer totalReadCount = 0;
                Integer totalReadskipCount = 0;
                Integer totalWriteSkipCount = 0;

               for(StepExecution stepExecution : jobExecution.getStepExecutions())
               {
                        totalReadCount += stepExecution.getReadCount();
                        totalWriteCount += stepExecution.getWriteCount();
                        totalReadskipCount += stepExecution.getReadSkipCount();
                        totalWriteSkipCount += stepExecution.getWriteSkipCount();

               }
               log.info("CSV File Upload Finished follwing Data are inserted of file " + resourceLoader.getResource(path).getFilename());

               log.info("Total data read from file  : " + totalReadCount);
               log.info("Succefully Updated data : " + totalWriteCount);

               log.info("Number of Items Skip on read from file  : " + totalReadskipCount);
               log.info("Number of Items Skip on write from file  : " + totalWriteSkipCount);


           } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
               log.info("CSV file Uploading Failed");
           }
       }catch (Exception e){
           log.info(e.toString());
       }
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Starting job");
    }

}
