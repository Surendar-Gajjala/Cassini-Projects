package com.cassinisys.drdo.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by subra on 08-11-2018.
 */
public class BomGroupCriteria extends Criteria {

    private String type;

    private String name;

    private Integer bom;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBom() {
        return bom;
    }

    public void setBom(Integer bom) {
        this.bom = bom;
    }
}
