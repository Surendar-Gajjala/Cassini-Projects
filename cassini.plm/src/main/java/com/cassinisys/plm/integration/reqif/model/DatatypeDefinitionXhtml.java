package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.util.Date;

public class DatatypeDefinitionXhtml extends DatatypeDefinition {

    public DatatypeDefinitionXhtml() {
        super();
        setLongName("XHTML");
        setDescription("XHTML data type");
        setLastChange(new Date());
    }

    @Override
    public Element toXml() {
        Element elem = createXmlElement("DATATYPE-DEFINITION-XHTML");
        return elem;
    }
}
