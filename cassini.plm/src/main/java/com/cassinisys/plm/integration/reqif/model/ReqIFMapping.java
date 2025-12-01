package com.cassinisys.plm.integration.reqif.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqIFMapping implements Serializable {
    private List<SystemMapping> systems = new ArrayList<>();

    public static ReqIFMapping parse(InputStream is) {
        ReqIFMapping mapping = null;
        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer);
            String json = writer.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            mapping = objectMapper.readValue(json, ReqIFMapping.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }

    public List<SystemMapping> getSystems() {
        return systems;
    }

    public void setSystems(List<SystemMapping> systems) {
        this.systems = systems;
    }

    public SystemMapping getSystemByName(String name) {
        for (SystemMapping system : systems) {
            if (system.getSystem().equalsIgnoreCase(name)) {
                return system;
            }
        }
        return null;
    }
}
