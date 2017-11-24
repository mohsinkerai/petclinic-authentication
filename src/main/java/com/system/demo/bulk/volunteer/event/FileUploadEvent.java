package com.system.demo.bulk.volunteer.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.nio.file.Path;

@Data
public class FileUploadEvent extends ApplicationEvent {

    private final Path filePath;
    private final long userId;
    private final boolean pictureAvailable;

    public FileUploadEvent(Object source, Path filePath, long userId, boolean pictureAvailable) {
        super(source);
        this.filePath = filePath;
        this.userId = userId;
        this.pictureAvailable = pictureAvailable;
    }
}
