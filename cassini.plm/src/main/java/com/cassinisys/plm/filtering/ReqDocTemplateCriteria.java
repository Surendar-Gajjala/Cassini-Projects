package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Lenovo on 26-06-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReqDocTemplateCriteria extends Criteria {
    String name;
    String description;
    String searchQuery;
}
