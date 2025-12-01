package com.cassinisys.platform.filtering;

import lombok.Data;

@Data
public class AutoNumberCriteria extends Criteria {
    private String name;
    private String description;
    private String prefix;
    private String suffix;
    private String searchQuery;
}
