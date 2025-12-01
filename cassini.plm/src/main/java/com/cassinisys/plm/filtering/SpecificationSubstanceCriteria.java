package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 20-05-2020.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SpecificationSubstanceCriteria extends Criteria {
    String number;
    String name;
    String status;
    Integer type;
    String description;
    Integer specification;
}
