package com.cassinisys.platform.service.config;

import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

/**
 * Created by reddy on 10/17/15.
 */
public class Config {
    private String tenant;
    private FileSystemService fileSystemService;
    private JsonNode rootNode;

//    @Transactional
    public Config(String tenant,FileSystemService fileSystemService) {
        this.tenant = tenant;
        this.fileSystemService = fileSystemService;

        init();
    }

    @Transactional
    private void init() {
        try {
            File configFile = fileSystemService.getTenantFile(tenant, "config.json");
            rootNode  = new ObjectMapper().readTree(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public File getTasksTemplateFile() throws ConfigServiceException {
        String printTask = rootNode.path("printTemplates").path("printPersonTask").textValue();
        return fileSystemService.getCurrentTenantFile(printTask);
    }
}
