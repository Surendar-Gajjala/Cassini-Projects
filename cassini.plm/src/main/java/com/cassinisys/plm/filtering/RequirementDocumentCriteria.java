package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RequirementDocumentCriteria extends Criteria {

    Integer type;
    String number;
    String name;
    String latestRevision;
    String description;
    String searchQuery;
    Integer project;
    Integer owner;
    String phase;
}
