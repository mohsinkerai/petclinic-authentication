package com.system.demo.bulk.vehicle;

import com.system.demo.bulkprogress.itemdata.FailItemService;
import com.system.demo.bulkprogress.itemdata.FailItems;
import com.system.demo.bulkprogress.jobdata.UserJobData;
import com.system.demo.vehicle.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Zeeshan Damani
 */

@Slf4j
@JobScope
@Component
public class VehicleItemWriterListener implements ItemWriteListener<Vehicle> {

    @Autowired
    FailItemService failItemService;

    @Value("#{jobExecution}")
    private JobExecution jobExecution;

    @Override
    public void beforeWrite(List<? extends Vehicle> items) {
        System.out.println("ItemWriteListener - beforeWrite");
    }

    @Override
    public void afterWrite(List<? extends Vehicle> items) {
        System.out.println("ItemWriteListener - afterWrite");
    }

    @Override
    public void onWriteError(Exception ex, List<? extends Vehicle> list) {
        Vehicle ItemsFailed;
        if (list.size() == 1) {
            ItemsFailed = list.get(0);
            recordVehicleWriteError(ex.getMessage(),ItemsFailed);
            log.info(ItemsFailed.getDriverCnic());
            log.info(ex.getCause().getMessage());
        }
    }

    private void recordVehicleWriteError(String message, Vehicle vehicle){
        UserJobData userJobData = (UserJobData)jobExecution.getExecutionContext().get("userJobData");
        FailItems failItem = FailItems.builder()
            .failureReason(message)
            .failedItems(vehicle.getOwnerCnic())
            .userJobId(userJobData.getId())
            .build();
        failItemService.save(failItem);
    }

}
