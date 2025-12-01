package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.util.Date;

public class DatatypeDefinitionDate extends DatatypeDefinition {

    public DatatypeDefinitionDate() {
        super();
        setLongName("Date");
        setDescription("Date data type");
        setLastChange(new Date());
    }

    @Override
    public Element toXml() {
        Element elem = createXmlElement("DATATYPE-DEFINITION-DATE");
        return elem;
    }
}
