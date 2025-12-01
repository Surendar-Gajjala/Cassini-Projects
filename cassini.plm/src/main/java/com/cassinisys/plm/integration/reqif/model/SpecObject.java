package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class SpecObject extends BaseObject {
    private SpecObjectType type;
    private List<AttributeValue> values = new ArrayList<>();

    public SpecObjectType getType() {
        return type;
    }

    public void setType(SpecObjectType type) {
        this.type = type;
    }

    public List<AttributeValue> getValues() {
        return values;
    }

    public void setValues(List<AttributeValue> values) {
        this.values = values;
    }

    public Element toXml() {
        Element elem = createXmlElement("SPEC-OBJECT");
        Element elemType = new Element("TYPE");
        Element elemTypeRef = new Element("SPEC-OBJECT-TYPE-REF");
        elemTypeRef.setText(type.getIdentifier());
        elemType.addContent(elemTypeRef);
        elem.addContent(elemType);
        Element elemValues = new Element("VALUES");
        for (AttributeValue value : values) {
            if (value.getDefinition() != null) {
                elemValues.addContent(value.toXml());
            }
        }
        elem.addContent(elemValues);
        return elem;
    }

    public Integer getIntegerAttributeValue(String attName) {
        Integer value = null;
        for (AttributeValue attValue : values) {
            if (attValue.getDefinition().getLongName().equalsIgnoreCase(attName) &&
                    attValue.getDefinition() instanceof AttributeDefinitionInteger) {
                int v = ((AttributeValueInteger) attValue).getValue();
                if (v != 0) {
                    value = v;
                    break;
                }
            }
        }
        return value;
    }

    public Integer getCassiniId() {
        Integer value = null;
        for (AttributeValue attValue : values) {
            if (attValue.getDefinition().getLongName().equalsIgnoreCase("Cassini ID") &&
                    attValue.getDefinition() instanceof AttributeDefinitionInteger) {
                int v = ((AttributeValueInteger) attValue).getValue();
                if (v != 0) {
                    value = v;
                    break;
                }
            }
        }
        return value;
    }

    public AttributeValue getAttributeValueByAttributeName(String attName) {
        for (AttributeValue attValue : values) {
            if (attValue.getDefinition().getLongName().equalsIgnoreCase(attName)) {
                return attValue;
            }
        }
        return null;
    }

}
