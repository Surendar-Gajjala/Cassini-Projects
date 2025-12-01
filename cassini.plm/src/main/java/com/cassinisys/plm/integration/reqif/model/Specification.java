package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class Specification extends BaseObject {
    public static String specName = "";
    private SpecificationType type;
    private List<SpecHierarchy> children = new ArrayList<>();
    private List<AttributeValue> values = new ArrayList<>();

    public SpecificationType getType() {
        return type;
    }

    public void setType(SpecificationType type) {
        this.type = type;
    }

    public List<SpecHierarchy> getChildren() {
        return children;
    }

    public void setChildren(List<SpecHierarchy> children) {
        this.children = children;
    }

    public List<AttributeValue> getValues() {
        return values;
    }

    public void setValues(List<AttributeValue> values) {
        this.values = values;
    }

    public Element toXml() {
        Element elem = createXmlElement("SPECIFICATION");
        Element elemValues = new Element("VALUES");
        for (AttributeValue value : values) {
            elemValues.addContent(value.toXml());
        }
        elem.addContent(elemValues);
        Element elemType = new Element("TYPE");
        Element elemTypeRef = new Element("SPECIFICATION-TYPE-REF");
        elemTypeRef.setText(type.getIdentifier());
        elemType.addContent(elemTypeRef);
        elem.addContent(elemType);
        if (children != null) {
            Element elemChildren = new Element("CHILDREN");
            for (SpecHierarchy child : children) {
                elemChildren.addContent(child.toXml());
            }
            elem.addContent(elemChildren);
        }
        return elem;
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

    public AttributeValue getAttributeValueByAttributeName(String attName) {
        for (AttributeValue attValue : values) {
            if (attValue.getDefinition().getLongName().equalsIgnoreCase(attName)) {
                return attValue;
            }
        }
        return null;
    }
}
