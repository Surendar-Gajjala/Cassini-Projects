package com.cassinisys.platform.filtering;


import lombok.Data;

/**
 * Created by subramanyam reddy on 20/12/21.
 */
@Data
public class TagCriteria extends Criteria {
    private String searchQuery;
    private Integer objectId;
    private String objectType;
}
