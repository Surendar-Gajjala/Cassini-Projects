package com.cassinisys.pdm.model.onshape;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileProperty implements Serializable {
    private String name;
    private PropertyType dataType;
    private String textValue;
    private Double numberValue;
    private Boolean boolValue;
}
