package com.system.demo.bulkprogress.jobdata;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created by Zeeshan Damani
 */

@Component
@AllArgsConstructor
public class UserJobService {

    private final UserJobRepository userJobRepository;



    public UserJobData findOne(long key) {
        return userJobRepository.findOne(key);
    }

    public UserJobData save(UserJobData userJobData) {
        return userJobRepository.save(userJobData);
    }

    public UserJobData getUserJobDataByJobId(Long id){ return userJobRepository.getUserJobDataByJobId(id); }
}
