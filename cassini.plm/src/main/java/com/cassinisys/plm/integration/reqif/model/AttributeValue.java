package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public abstract class AttributeValue {
    private AttributeDefinition definition;

    protected AttributeValue(AttributeDefinition def) {
        this.definition = def;
    }

    public AttributeDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(AttributeDefinition definition) {
        this.definition = definition;
    }

    public Element createXmlElement(String elementName) {
        Element elem = new Element(elementName);
        Element def = new Element("DEFINITION");
        def.addContent(definition.createRefElement());
        elem.addContent(def);
        return elem;
    }

    public abstract Element toXml();
}
