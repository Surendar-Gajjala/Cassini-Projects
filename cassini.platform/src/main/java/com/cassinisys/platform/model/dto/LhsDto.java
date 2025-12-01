package com.cassinisys.platform.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;


@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class LhsDto {
    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;
    @JsonProperty("date")
    private Date date;
    @JsonProperty("objectType")
    private String objectType;
    @JsonProperty("dataType")
    private String dataType;
    @JsonProperty("object")
    private LhsDto object;
}
