package com.cassinisys.plm.model.dto;

import lombok.Data;

@Data
public class MapperDTO {
    private Integer id;
    private String key;
    private String value;
    private Integer cellId;
    private boolean exclude;

}