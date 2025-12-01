package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public class AttributeValueString extends AttributeValue {
    private String value;

    public AttributeValueString(AttributeDefinition def) {
        super(def);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Element toXml() {
        Element elem = createXmlElement("ATTRIBUTE-VALUE-STRING");
        elem.setAttribute("THE-VALUE", "" + this.value);
        return elem;
    }
}
