package com.cassinisys.erp.service.config;

import com.cassinisys.erp.service.filesystem.FileSystemService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by reddy on 10/17/15.
 */
public class Config {
    private String tenant;
    private FileSystemService fileSystemService;
    private JsonNode rootNode;


    public Config(String tenant,FileSystemService fileSystemService) {
        this.tenant = tenant;
        this.fileSystemService = fileSystemService;

        init();
    }

    private void init() {
        try {
            File configFile = fileSystemService.getTenantFile(tenant, "config.json");
            rootNode  = new ObjectMapper().readTree(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public File getPrintOrderTemplateFile() throws ConfigServiceException {
        String printOrder = rootNode.path("printTemplates").path("printOrder").textValue();
        return fileSystemService.getCurrentTenantFile(printOrder);
    }

    public File getPrintInvoiceTemplateFile() throws ConfigServiceException {
        String printInvoice = rootNode.path("printTemplates").path("printInvoice").textValue();
        return fileSystemService.getCurrentTenantFile(printInvoice);
    }

    public File getShipperTemplate(String shipper) {
        Iterator<JsonNode> it = rootNode.path("shipperTemplates").elements();
        while(it.hasNext()) {
            JsonNode node = it.next();

            String name = node.path("name").textValue();
            if(name.equalsIgnoreCase(shipper)) {
                String fileName = node.path("template").textValue();
                if(fileName != null) {
                    return fileSystemService.getCurrentTenantFile(fileName);
                }
            }
        }

        return null;
    }

}
