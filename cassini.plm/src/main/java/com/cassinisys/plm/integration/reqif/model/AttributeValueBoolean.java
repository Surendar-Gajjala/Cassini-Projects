package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public class AttributeValueBoolean extends AttributeValue {
    private boolean value = false;

    public AttributeValueBoolean(AttributeDefinition def) {
        super(def);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public Element toXml() {
        Element elem = createXmlElement("ATTRIBUTE-VALUE-BOOLEAN");
        elem.setAttribute("THE-VALUE", "" + this.value);
        return elem;
    }
}
