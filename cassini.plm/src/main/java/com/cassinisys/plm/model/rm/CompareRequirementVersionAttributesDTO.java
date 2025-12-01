package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.model.core.ObjectAttribute;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CompareRequirementVersionAttributesDTO {
    private List<Map<Requirement, List<ObjectAttribute>>> requirementListMap;
}