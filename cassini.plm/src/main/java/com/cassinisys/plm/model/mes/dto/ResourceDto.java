package com.cassinisys.plm.model.mes.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smukka on 27-07-2022.
 */
@Data
public class ResourceDto {
    private String resource;
    private Integer count = 0;
    private List<OperationResourceDto> resourceTypes = new ArrayList<>();
}
