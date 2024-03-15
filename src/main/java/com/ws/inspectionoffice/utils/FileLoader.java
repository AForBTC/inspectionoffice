package com.ws.inspectionoffice.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class FileLoader {
    private final ResourceLoader resourceLoader;

    public FileLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Resource loadFile(String filePath) {
        return resourceLoader.getResource("file:" + filePath);
    }
}
