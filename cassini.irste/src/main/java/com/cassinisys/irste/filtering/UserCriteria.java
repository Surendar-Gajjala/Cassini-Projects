package com.cassinisys.irste.filtering;

import com.cassinisys.platform.filtering.Criteria;
import lombok.Data;

/**
 * Created by Nageshreddy on 25-11-2018.
 */
@Data
public class UserCriteria extends Criteria {

    String applicationNumber;
    String presentStatus;
    String flowStep;
    String searchQuery;
    Integer person;
    String[] personTypes;
    private boolean freeTextSearch = false;

}
