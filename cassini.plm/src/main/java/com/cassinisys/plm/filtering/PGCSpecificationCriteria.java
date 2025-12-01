package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by GSR  on 27-10-2020.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PGCSpecificationCriteria extends Criteria {
    Integer type;
    String name;
    String number;
    String description;
    String searchQuery;
    Integer declaration;
    Integer item;
}