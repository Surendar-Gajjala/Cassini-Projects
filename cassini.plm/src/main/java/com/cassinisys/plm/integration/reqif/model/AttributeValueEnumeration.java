package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class AttributeValueEnumeration extends AttributeValue {
    private List<EnumValue> values = new ArrayList<>();

    public AttributeValueEnumeration(AttributeDefinition def) {
        super(def);
    }

    public List<EnumValue> getValues() {
        return values;
    }

    public void setValues(List<EnumValue> values) {
        this.values = values;
    }

    public Element toXml() {
        Element elem = createXmlElement("ATTRIBUTE-VALUE-ENUMERATION");
        Element valuesElem = new Element("VALUES");
        for (EnumValue value : values) {
            valuesElem.addContent(value.toXml());
        }
        elem.addContent(valuesElem);
        return elem;
    }
}
