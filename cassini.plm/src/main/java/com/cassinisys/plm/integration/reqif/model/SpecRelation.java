package com.cassinisys.plm.integration.reqif.model;

import java.util.List;

public class SpecRelation extends BaseObject {
    private SpecRelationType type;
    private SpecObject source;
    private SpecObject target;
    private List<AttributeValue> values;

    public SpecRelationType getType() {
        return type;
    }

    public void setType(SpecRelationType type) {
        this.type = type;
    }

    public SpecObject getSource() {
        return source;
    }

    public void setSource(SpecObject source) {
        this.source = source;
    }

    public SpecObject getTarget() {
        return target;
    }

    public void setTarget(SpecObject target) {
        this.target = target;
    }

    public List<AttributeValue> getValues() {
        return values;
    }

    public void setValues(List<AttributeValue> values) {
        this.values = values;
    }
}
