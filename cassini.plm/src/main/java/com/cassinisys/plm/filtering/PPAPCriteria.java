package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PPAPCriteria extends Criteria{

    String number;
    String name;
    String description;
    String searchQuery;
}
