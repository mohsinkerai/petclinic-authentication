package com.system.demo.bulk.volunteer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class StorageService {

    private final Path rootLocation;

    public StorageService(@Value("${storage.path}") String storagePath) {
        this.rootLocation = Paths.get(storagePath);
    }

    public Path store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                throw new RuntimeException(
                    "Cannot store file with relative path outside current directory "
                        + filename);
            }
            Path targetPath = this.rootLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetPath,
                StandardCopyOption.REPLACE_EXISTING);
            return targetPath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }
}
