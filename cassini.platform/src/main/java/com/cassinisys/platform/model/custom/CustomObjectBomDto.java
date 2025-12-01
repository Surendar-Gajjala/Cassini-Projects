package com.cassinisys.platform.model.custom;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 20-07-2021.
 */
@Data
public class CustomObjectBomDto {
    private Integer id;
    private Integer sequence;
    private Integer parent;
    private Integer child;
    private String number;
    private String name;
    private String description;
    private String typeName;
    private String notes;
    private Integer quantity;
    private Boolean hasBom = Boolean.FALSE;
    private Integer count = 0;
    private List<CustomObjectBomDto> children = new LinkedList<>();
}
