package com.cassinisys.plm.model.mes.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smukka on 27-07-2022.
 */
@Data
public class OperationResourceDto {
    private String resourceType;
    private Integer quantity = 0;
    private Integer consumedQty = 0;
    private List<BOPOperationResourceDto> resources = new ArrayList<>();
}
