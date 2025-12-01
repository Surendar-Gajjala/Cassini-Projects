package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public class AttributeDefinitionString extends AttributeDefinition {

    public AttributeDefinitionString(DatatypeDefinition type) {
        super(type);
    }

    @Override
    public Element toXml() {
        Element elem = createXmlElement("ATTRIBUTE-DEFINITION-STRING");
        Element type = new Element("TYPE");
        Element ref = new Element("DATATYPE-DEFINITION-STRING-REF");
        ref.setText(getType().getIdentifier());
        type.addContent(ref);
        elem.addContent(type);
        return elem;
    }

    @Override
    public Element createRefElement() {
        Element elem = new Element("ATTRIBUTE-DEFINITION-STRING-REF");
        elem.setText(getIdentifier());
        return elem;
    }
}
