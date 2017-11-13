package com.system.demo.bulkprogress.itemdata;

import com.system.demo.bulkprogress.jobdata.UserJobData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created by Zeeshan Damani
 */
@Component
@AllArgsConstructor
public class FailItemService {

    private final FailItemRepository failItemRepository;

    public FailItems findOne(long key) {
        return failItemRepository.findOne(key);
    }

    public FailItems save(FailItems failItems) {
        return failItemRepository.save(failItems);
    }
}
