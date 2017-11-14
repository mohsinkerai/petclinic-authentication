package com.system.demo.bulkprogress.itemdata;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<FailItems> findByJobId(long jobId, Pageable pageable) {
        return failItemRepository.findByUserJobId(jobId, pageable);
    }
}
