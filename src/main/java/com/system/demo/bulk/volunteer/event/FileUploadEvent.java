package com.system.demo.bulk.volunteer.event;

import java.nio.file.Path;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class FileUploadEvent extends ApplicationEvent {

    private final Path filePath;
    private final long userId;

    public FileUploadEvent(Object source, Path filePath, long userId) {
        super(source);
        this.filePath = filePath;
        this.userId = userId;
    }
}
