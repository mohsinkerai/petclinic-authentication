package com.system.demo.bulkprogress.itemdata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.opencsv.CSVWriter;
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

    public File exportCsv(Long jobId) throws IOException {
        long currentMillis = System.currentTimeMillis();
        List<FailItems> failItems = failItemRepository.findAllByUserJobId((jobId));
        CSVWriter writer = new CSVWriter(new FileWriter(String.valueOf(currentMillis) + "-volunterFaild-export.csv"));
        writer.writeNext(headers());
        List<String[]> listOfFailItems = failItems.stream().map(this::map)
            .collect(Collectors.toList());
        writer.writeAll(listOfFailItems);
        writer.flush();
        writer.close();
        return new File(String.valueOf(currentMillis) + "-volunterFaild-export.csv");
    }

    private String[] headers() {
        return new String[]{
            "CNIC",
            "Error Message"
        };
    }

    private String[] map(FailItems failItems) {
        return new String[]{
            failItems.getFailedItemsCnic(),
            failItems.getFailureReason()
        };

    }
}
