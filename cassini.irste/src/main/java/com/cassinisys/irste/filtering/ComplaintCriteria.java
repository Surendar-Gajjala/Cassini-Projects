package com.cassinisys.irste.filtering;

import com.cassinisys.platform.filtering.Criteria;
import lombok.Data;

/**
 * Created by Nageshreddy on 06-11-2018.
 */
@Data
public class ComplaintCriteria extends Criteria {

    private String complaintNumber;
    private String location;
    private String utility;
    private String groupUtility;
    private String details;
    private String status;
    private String person;
    private String responder;
    private String assistor;
    private String facilitator;
    private boolean freeTextSearch = false;
    private String searchQuery;

}
