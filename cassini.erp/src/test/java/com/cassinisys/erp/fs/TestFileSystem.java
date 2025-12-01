package com.cassinisys.erp.fs;

import com.cassinisys.erp.BaseTest;
import com.cassinisys.erp.service.filesystem.FileSystemService;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * Created by reddy on 10/17/15.
 */
public class TestFileSystem extends BaseTest{
    @Autowired
    private FileSystemService fileSystemService;

    @Test
    public void testFileSystem() throws Exception {
        File root = fileSystemService.getCurrentTenantRoot();
        System.out.println(root);
    }

    @Test
    public void testConfig() throws Exception {
        File configFile = fileSystemService.getCurrentTenantFile("config.json");
        Object document = Configuration.defaultConfiguration().jsonProvider().
                parse(FileUtils.readFileToString(configFile));
        String printOrder = JsonPath.read(document, "$.printTemplates.printOrder");
        File printOrderFile = fileSystemService.getCurrentTenantFile(printOrder);
        System.out.println(printOrderFile);
    }
}
