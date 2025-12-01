package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.util.Date;
import java.util.List;

public class DatatypeDefinitionEnumeration extends DatatypeDefinition {
    private List<EnumValue> specifiedValues;

    public DatatypeDefinitionEnumeration() {
        super();
        setLongName("Enumeration");
        setDescription("Enumeration data type");
        setLastChange(new Date());
    }

    public List<EnumValue> getSpecifiedValues() {
        return specifiedValues;
    }

    public void setSpecifiedValues(List<EnumValue> specifiedValues) {
        this.specifiedValues = specifiedValues;
    }

    @Override
    public Element toXml() {
        Element elem = createXmlElement("DATATYPE-DEFINITION-ENUMERATION");
        Element elemValues = new Element("SPECIFIED-VALUES");
        for (EnumValue value : specifiedValues) {
            elemValues.addContent(value.toXml());
        }
        elem.addContent(elemValues);
        return elem;
    }
}
