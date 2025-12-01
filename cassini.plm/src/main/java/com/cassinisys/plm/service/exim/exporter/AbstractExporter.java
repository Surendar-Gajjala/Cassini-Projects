package com.cassinisys.plm.service.exim.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.zip.ZipOutputStream;

public abstract class AbstractExporter {
    private ObjectMapper objectMapper;

    public AbstractExporter() {
        objectMapper = new ObjectMapper();
        //Set pretty printing of json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public abstract void exportData(ZipOutputStream zout);
}
