package com.cassinisys.platform.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class OperatorDto {
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
}
