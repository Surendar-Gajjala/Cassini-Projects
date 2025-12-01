package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Suresh Cassini on 18-11-2020.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaintenancePlanCriteria extends Criteria {
    String name;
    String number;
    String description;
    String searchQuery;
}