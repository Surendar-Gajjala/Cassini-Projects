package com.cassinisys.plm.integration.reqif.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemMapping implements Serializable {
    private String system;
    private String defaultReqType = "Requirement";
    private String defaultSpecType = "Specification";
    private List<AttributeMapping> attributeMapping = new ArrayList<>();

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getDefaultReqType() {
        return defaultReqType;
    }

    public void setDefaultReqType(String defaultReqType) {
        this.defaultReqType = defaultReqType;
    }

    public String getDefaultSpecType() {
        return defaultSpecType;
    }

    public void setDefaultSpecType(String defaultSpecType) {
        this.defaultSpecType = defaultSpecType;
    }

    public List<AttributeMapping> getAttributeMapping() {
        return attributeMapping;
    }

    public void setAttributeMapping(List<AttributeMapping> attributeMapping) {
        this.attributeMapping = attributeMapping;
    }

    public AttributeMapping findByCassiniName(String name) {
        for (AttributeMapping m : attributeMapping) {
            if (m.getCassiniName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    public AttributeMapping findBySystemName(String name) {
        for (AttributeMapping m : attributeMapping) {
            if (m.getSystemName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }
}
