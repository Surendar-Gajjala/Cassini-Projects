package com.cassinisys.plm.model.mes.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smukka on 12-05-2022.
 */
@Data
public class OperationPartDto {
    List<BOPOperationPartDto> consumedParts = new ArrayList<>();
    List<BOPOperationPartDto> producedParts = new ArrayList<>();
}

