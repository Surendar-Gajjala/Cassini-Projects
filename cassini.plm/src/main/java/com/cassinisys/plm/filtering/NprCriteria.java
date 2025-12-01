package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by GSR  on 18-12-2020.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NprCriteria extends Criteria {
    String number;
    String description;
    String reasonForRequest;
    String notes;
    String searchQuery;
}