package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public class AttributeDefinitionDate extends AttributeDefinition {

    public AttributeDefinitionDate(DatatypeDefinition type) {
        super(type);
    }

    @Override
    public Element toXml() {
        Element elem = createXmlElement("ATTRIBUTE-DEFINITION-DATE");
        Element type = new Element("TYPE");
        Element ref = new Element("DATATYPE-DEFINITION-DATE-REF");
        ref.setText(getType().getIdentifier());
        type.addContent(ref);
        elem.addContent(type);
        return elem;
    }

    @Override
    public Element createRefElement() {
        Element elem = new Element("ATTRIBUTE-DEFINITION-DATE-REF");
        elem.setText(getIdentifier());
        return elem;
    }
}
