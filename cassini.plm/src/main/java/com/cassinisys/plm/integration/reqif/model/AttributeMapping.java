package com.cassinisys.plm.integration.reqif.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AttributeMapping implements Serializable {
    private String cassiniName;
    private String systemName;

    public String getCassiniName() {
        return cassiniName;
    }

    public void setCassiniName(String cassiniName) {
        this.cassiniName = cassiniName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
