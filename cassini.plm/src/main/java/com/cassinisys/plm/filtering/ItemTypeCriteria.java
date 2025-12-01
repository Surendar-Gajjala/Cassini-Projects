package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Lov;

public class ItemTypeCriteria extends Criteria {

    String name;
    Integer parentType;
    AutoNumber itemNumberSource;
    Lov revisionSequence;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentType() {
        return parentType;
    }

    public void setParentType(Integer parentType) {
        this.parentType = parentType;
    }

    public AutoNumber getItemNumberSource() {
        return itemNumberSource;
    }

    public void setItemNumberSource(AutoNumber itemNumberSource) {
        this.itemNumberSource = itemNumberSource;
    }

    public Lov getRevisionSequence() {
        return revisionSequence;
    }

    public void setRevisionSequence(Lov revisionSequence) {
        this.revisionSequence = revisionSequence;
    }


}
