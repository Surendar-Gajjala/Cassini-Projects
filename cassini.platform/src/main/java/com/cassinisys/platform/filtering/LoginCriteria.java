package com.cassinisys.platform.filtering;

import lombok.Data;

@Data
public class LoginCriteria extends Criteria {
    private String searchQuery;
    private Boolean isActive;
    private Boolean external;
}
