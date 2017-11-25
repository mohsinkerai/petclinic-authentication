package com.system.demo.bulk.volunteer.job.elements;

import com.system.demo.bulkprogress.itemdata.FailItemService;
import com.system.demo.bulkprogress.itemdata.FailItems;
import com.system.demo.bulkprogress.jobdata.UserJobData;
import com.system.demo.exceptions.BulkErrorType;
import com.system.demo.volunteer.Volunteer;
import com.system.demo.volunteer.VolunteerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@Component
@JobScope
public class VolunteerBulkProcessor implements ItemProcessor<Volunteer,Volunteer> {

    private final VolunteerService volunteerService;

    @Value("#{jobExecution}")
    private JobExecution jobExecution;

    @Autowired
    private FailItemService failItemService;

    String pictureFlag;

    @Value("#{jobParameters['imagesSource']}")
    String ImageDirectory;

    public VolunteerBulkProcessor(VolunteerService volunteerService,
                                  @Value("#{jobParameters['imagesSource']}") String imageDirectory,
                                  @Value("#{jobParameters['pictureFlag']}") String pictureFlag) {

        this.volunteerService = volunteerService;
        this.ImageDirectory = imageDirectory;
        this.pictureFlag = pictureFlag;
    }

    public Volunteer process(Volunteer v) throws Exception {
        if (!v.validateCnic()) {
            recordError(BulkErrorType.INVALID_CNIC.toString(), v);
            return null;
        }
//         else if (!v.valiateAge()) {
//            recordError(BulkErrorType.VALIDATION_INVALID_AGE.toString(), v);
//            return null;
//        } else if (!v.valiateHomePhone()) {
//            recordError(BulkErrorType.VALIDATION_INVALID_HOMEPHONE.toString(), v);
//            return null;
//        } else if (!v.valiateResidentialAddress()) {
//            recordError(BulkErrorType.VALIADTION_INVALID_RESIDENCE.toString(), v);
//            return null;
//        } else if (!v.validateEmail()) {
//            recordError(BulkErrorType.VALIDATION_INVALID_EMAIL.toString(), v);
//            return null;
//        }
            else if (!v.validateMobile()) {
            recordError(BulkErrorType.VALIDATION_INVALID_MOBILE.toString(), v);
            return null;
        } else {
            if (jobExecution.getJobParameters().getString("pictureFlag").equalsIgnoreCase("true")) {
                String volunteerImage = getImageForVolunteer(v.getVolunteerCnic());
                if(volunteerImage.equals("")) {
                    recordError (BulkErrorType.IMAGE_NOT_FOUND.toString(), v);
                    return null;
                 }
                v.setVolunteerImage(volunteerImage);
                v.setPictureAvailable(true);
            } else {
                String volunteerImage = getImageForVolunteer(v.getVolunteerCnic());
                if(volunteerImage.equals("")) {
                    v.setPictureAvailable(false);
                } else {
                    v.setVolunteerImage(volunteerImage);
                    v.setPictureAvailable(true);
                }
            }
            List<Volunteer> vol = volunteerService.findByCnic(v.getVolunteerCnic());//v.getVolunteerCnic());
            if (vol.size() == 0) {
                v.setEnabled(true);
                return v;
            } else {
                recordError(BulkErrorType.CNIC_ALREADY_RESGISTERED.toString(), v);
                return null;
            }
        }
    }
    private String getImageForVolunteer(String volunteerCnic){

        boolean volunteerImage = false;
        String imagePath = ImageDirectory +"\\"+ volunteerCnic;
        if (new File(imagePath + ".jpg").exists()) {
            volunteerImage = true;
            // TODO: in following line, get VolunteerCnic Should be Removed.
            return imagePath +".jpg";
        } else if (new File( imagePath + ".jpeg").exists()) {
            volunteerImage = true;
            return  imagePath +".jpeg";
        } else if (new File(imagePath + ".png").exists()) {
            volunteerImage = true;
            return imagePath +".png";
        } else {
            return "";
        }
    }
    private void recordError(String message, Volunteer volunteer){
        log.error(message);
        UserJobData userJobData = (UserJobData)jobExecution.getExecutionContext().get("userJobData");
        FailItems failItem = FailItems.builder()
            .failureReason(message)
            .failedItemsCnic(volunteer.getVolunteerCnic().equals("") ? "Empty" : volunteer.getVolunteerCnic())
            .failedItemsFromNo(volunteer.getVolunteerFormNo().equals("") ? "Empty" : volunteer.getVolunteerFormNo())
            .userJobId(userJobData.getId())
            .build();
        failItemService.save(failItem);
        log.info("valiadtion Error for : "+ volunteer.toString());
    }
}
