package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.util.Date;

public class DatatypeDefinitionReal extends DatatypeDefinition {
    private float max;
    private float min;
    private int accuracy;

    public DatatypeDefinitionReal() {
        super();
        setLongName("Real");
        setDescription("Real data type");
        setLastChange(new Date());
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public Element toXml() {
        Element elem = createXmlElement("DATATYPE-DEFINITION-REAL");
        return elem;
    }
}
