package com.cassinisys.plm.model.mes.dto;

import com.cassinisys.plm.model.mes.MESOperationResources;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smukka on 29-07-2022.
 */
@Data
public class OperationResourceTypeDto {
    private String resource;
    private List<MESOperationResources> resourceTypes = new ArrayList<>();
}
