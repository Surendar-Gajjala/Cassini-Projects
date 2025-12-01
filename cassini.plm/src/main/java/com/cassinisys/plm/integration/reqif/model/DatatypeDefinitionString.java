package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.util.Date;

public class DatatypeDefinitionString extends DatatypeDefinition {
    private int maxLength;

    public DatatypeDefinitionString() {
        super();
        setLongName("Text");
        setDescription("Text data type");
        setLastChange(new Date());
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public Element toXml() {
        Element elem = createXmlElement("DATATYPE-DEFINITION-STRING");
        return elem;
    }
}
