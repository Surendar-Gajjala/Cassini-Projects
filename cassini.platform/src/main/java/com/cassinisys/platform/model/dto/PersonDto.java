package com.cassinisys.platform.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonDto {
    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private String id;
}
