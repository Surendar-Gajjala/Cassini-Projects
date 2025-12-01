package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public class AttributeDefinitionEnumeration extends AttributeDefinition {
    private boolean multiValued = false;

    public AttributeDefinitionEnumeration(DatatypeDefinition type) {
        super(type);
    }

    public boolean isMultiValued() {
        return multiValued;
    }

    public void setMultiValued(boolean multiValued) {
        this.multiValued = multiValued;
    }

    @Override
    public Element toXml() {
        Element elem = createXmlElement("ATTRIBUTE-DEFINITION-ENUMERATION");
        elem.setAttribute("MULTI-VALUED", "" + multiValued);
        Element type = new Element("TYPE");
        Element ref = new Element("DATATYPE-DEFINITION-ENUMERATION-REF");
        ref.setText(getType().getIdentifier());
        type.addContent(ref);
        elem.addContent(type);
        return elem;
    }

    @Override
    public Element createRefElement() {
        Element elem = new Element("ATTRIBUTE-DEFINITION-ENUMERATION-REF");
        elem.setText(getIdentifier());
        return elem;
    }
}
