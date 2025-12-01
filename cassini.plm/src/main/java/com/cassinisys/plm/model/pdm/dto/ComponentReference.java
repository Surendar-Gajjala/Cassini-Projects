package com.cassinisys.plm.model.pdm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentReference implements Serializable {
    private String name;
    private int fileVersion;
    private double[] transform;
    private List<ComponentReference> children = new ArrayList<>();
}
