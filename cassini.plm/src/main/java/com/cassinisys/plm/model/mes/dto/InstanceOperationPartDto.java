package com.cassinisys.plm.model.mes.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smukka on 12-05-2022.
 */
@Data
public class InstanceOperationPartDto {
    List<BOPInstanceOperationPartDto> consumedParts = new ArrayList<>();
    List<BOPInstanceOperationPartDto> producedParts = new ArrayList<>();
}

