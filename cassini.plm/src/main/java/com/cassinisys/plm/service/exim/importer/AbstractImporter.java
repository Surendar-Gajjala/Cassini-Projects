package com.cassinisys.plm.service.exim.importer;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractImporter {
    private ObjectMapper objectMapper;

    public AbstractImporter() {
        objectMapper = new ObjectMapper();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    protected abstract void importData(byte[] bytes);
}
