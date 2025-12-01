package com.cassinisys.plm.model.pdm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AssemblyDTO.class, name = "AssemblyDTO"),
        @JsonSubTypes.Type(value = PartDTO.class, name = "PartDTO")
})
public class ComponentDTO implements Serializable {
    private Integer id = 0;
    private String name;
    private String configuration = "Default";
    private Integer version = 1;
    private Integer quantity;
    private String revision = "A";
    private double[] transform = {};

    public ComponentDTO() {

    }
}
