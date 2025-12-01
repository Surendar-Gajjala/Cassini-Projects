package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public class AttributeDefinitionInteger extends AttributeDefinition {

    public AttributeDefinitionInteger(DatatypeDefinition type) {
        super(type);
    }

    @Override
    public Element toXml() {
        Element elem = createXmlElement("ATTRIBUTE-DEFINITION-INTEGER");
        Element type = new Element("TYPE");
        Element ref = new Element("DATATYPE-DEFINITION-INTEGER-REF");
        ref.setText(getType().getIdentifier());
        type.addContent(ref);
        elem.addContent(type);
        return elem;
    }

    @Override
    public Element createRefElement() {
        Element elem = new Element("ATTRIBUTE-DEFINITION-INTEGER-REF");
        elem.setText(getIdentifier());
        return elem;
    }
}
