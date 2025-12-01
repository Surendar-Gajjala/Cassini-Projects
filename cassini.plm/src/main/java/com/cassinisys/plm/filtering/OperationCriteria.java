package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Lenovo on 29-10-2020.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OperationCriteria extends Criteria {

    Integer type;
    String name;
    String number;
    String description;
    String searchQuery;
    Integer bop;
    Integer bopPlan;
    Integer workCenter;
}