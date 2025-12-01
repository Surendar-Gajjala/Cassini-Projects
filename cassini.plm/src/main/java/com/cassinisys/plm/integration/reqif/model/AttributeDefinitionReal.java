package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public class AttributeDefinitionReal extends AttributeDefinition {

    public AttributeDefinitionReal(DatatypeDefinition type) {
        super(type);
    }

    @Override
    public Element toXml() {
        Element elem = createXmlElement("ATTRIBUTE-DEFINITION-REAL");
        Element type = new Element("TYPE");
        Element ref = new Element("DATATYPE-DEFINITION-REAL-REF");
        ref.setText(getType().getIdentifier());
        type.addContent(ref);
        elem.addContent(type);
        return elem;
    }

    @Override
    public Element createRefElement() {
        Element elem = new Element("ATTRIBUTE-DEFINITION-REAL-REF");
        elem.setText(getIdentifier());
        return elem;
    }
}
