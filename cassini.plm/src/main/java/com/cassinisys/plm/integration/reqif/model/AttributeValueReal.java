package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public class AttributeValueReal extends AttributeValue {
    private float value;

    public AttributeValueReal(AttributeDefinition def) {
        super(def);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Element toXml() {
        Element elem = createXmlElement("ATTRIBUTE-VALUE-REAL");
        elem.setAttribute("THE-VALUE", "" + this.value);
        return elem;
    }
}
