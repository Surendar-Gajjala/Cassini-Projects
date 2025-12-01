package com.cassinisys.plm.integration.reqif.model;

import java.util.List;

public class RelationGroupType extends BaseObject {
    private List<AttributeDefinition> specAttributes;

    public List<AttributeDefinition> getSpecAttributes() {
        return specAttributes;
    }

    public void setSpecAttributes(List<AttributeDefinition> specAttributes) {
        this.specAttributes = specAttributes;
    }
}
