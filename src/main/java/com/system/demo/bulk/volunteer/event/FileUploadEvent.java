package com.system.demo.bulk.volunteer.event;

import java.nio.file.Path;
import lombok.Data;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

@Data
public class FileUploadEvent extends ApplicationEvent {

    private final Path filePath;

    public FileUploadEvent(Object source, Path filePath) {
        super(source);
        this.filePath = filePath;
    }
}
