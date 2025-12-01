package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public class AttributeDefinitionBoolean extends AttributeDefinition {

    public AttributeDefinitionBoolean(DatatypeDefinition type) {
        super(type);
    }

    @Override
    public Element toXml() {
        Element elem = createXmlElement("ATTRIBUTE-DEFINITION-BOOLEAN");
        Element type = new Element("TYPE");
        Element ref = new Element("DATATYPE-DEFINITION-BOOLEAN-REF");
        ref.setText(getType().getIdentifier());
        type.addContent(ref);
        elem.addContent(type);
        return elem;
    }

    @Override
    public Element createRefElement() {
        Element elem = new Element("ATTRIBUTE-DEFINITION-BOOLEAN-REF");
        elem.setText(getIdentifier());
        return elem;
    }
}
