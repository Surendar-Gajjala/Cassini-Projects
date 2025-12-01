package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.util.Date;

public class DatatypeDefinitionInteger extends DatatypeDefinition {
    private int max;
    private int min;

    public DatatypeDefinitionInteger() {
        super();
        setLongName("Integer");
        setDescription("Integer data type");
        setLastChange(new Date());
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    @Override
    public Element toXml() {
        Element elem = createXmlElement("DATATYPE-DEFINITION-INTEGER");
        return elem;
    }
}
