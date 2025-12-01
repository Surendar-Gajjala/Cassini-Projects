package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.util.Date;

public class DatatypeDefinitionBoolean extends DatatypeDefinition {

    public DatatypeDefinitionBoolean() {
        super();
        setLongName("Boolean");
        setDescription("Boolean data type");
        setLastChange(new Date());
    }

    @Override
    public Element toXml() {
        Element elem = createXmlElement("DATATYPE-DEFINITION-BOOLEAN");
        return elem;
    }
}
