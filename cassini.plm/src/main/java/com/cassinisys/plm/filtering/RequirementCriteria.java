package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RequirementCriteria extends Criteria {
    Integer type;
    Integer requirementDocument;
    String number;
    String name;
    String description;
    String searchQuery;
    Integer assignedTo;
    String priority;
    String phase;
}
