package com.system.demo.bulk.volunteer.job.elements.listener;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ItemLoggerListener {
    // These methods gets called immediately after an error is encountered
    @OnReadError
    public void onReadError(Exception ex) {
        if (ex instanceof FlatFileParseException) {
            FlatFileParseException ffpe = (FlatFileParseException) ex;
            ffpe.getLineNumber(); //The line number error occured
            ffpe.getInput();
            log.error("Error While Reading Record from CSV file at line Number " + ffpe.getLineNumber());
            log.error("with data  : " + ffpe.getInput());
        }
    }

    @OnWriteError
    public void onWriteError(Exception ex, List<? extends Map<String, Object>> list) {
        log.error("Error While Saving Item Record to database. ", ex.getMessage());
        for(Object item : list){
            log.info(item.toString());
        }
    }
}
