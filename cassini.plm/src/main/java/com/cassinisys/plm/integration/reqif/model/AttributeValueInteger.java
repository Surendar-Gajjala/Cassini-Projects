package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public class AttributeValueInteger extends AttributeValue {
    private Integer value;

    public AttributeValueInteger(AttributeDefinition def) {
        super(def);
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Element toXml() {
        Element elem = createXmlElement("ATTRIBUTE-VALUE-INTEGER");
        elem.setAttribute("THE-VALUE", "" + this.value);
        return elem;
    }
}
