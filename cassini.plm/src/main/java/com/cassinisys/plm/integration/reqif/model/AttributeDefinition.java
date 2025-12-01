package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public abstract class AttributeDefinition extends BaseObject {
    private boolean isEditable;
    private DatatypeDefinition type;

    public AttributeDefinition(DatatypeDefinition type) {
        this.type = type;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public DatatypeDefinition getType() {
        return type;
    }

    public void setType(DatatypeDefinition type) {
        this.type = type;
    }

    public abstract Element createRefElement();

    public abstract Element toXml();
}
