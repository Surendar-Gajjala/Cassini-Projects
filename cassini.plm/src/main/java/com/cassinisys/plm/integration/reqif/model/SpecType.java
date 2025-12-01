package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public abstract class SpecType extends BaseObject {
    private List<AttributeDefinition> specAttributes = new ArrayList<>();

    public List<AttributeDefinition> getSpecAttributes() {
        return specAttributes;
    }

    public void setSpecAttributes(List<AttributeDefinition> specAttributes) {
        this.specAttributes = specAttributes;
    }

    protected Element toXml(String tagName) {
        Element elem = createXmlElement(tagName);
        Element elemAtts = new Element("SPEC-ATTRIBUTES");
        for (AttributeDefinition def : specAttributes) {
            elemAtts.addContent(def.toXml());
        }
        elem.addContent(elemAtts);
        return elem;
    }

    protected abstract Element toXml();

    public AttributeDefinition getSpecAttributeByName(String name) {
        for (AttributeDefinition specAttribute : specAttributes) {
            if (specAttribute.getLongName().equalsIgnoreCase(name)) {
                return specAttribute;
            }
        }
        return null;
    }
}
