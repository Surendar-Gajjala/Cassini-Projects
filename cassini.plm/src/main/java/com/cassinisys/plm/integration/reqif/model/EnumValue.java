package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public class EnumValue extends BaseObject {
    private EmbeddedValue embeddedValue;

    public EmbeddedValue getEmbeddedValue() {
        return embeddedValue;
    }

    public void setEmbeddedValue(EmbeddedValue embeddedValue) {
        this.embeddedValue = embeddedValue;
    }

    public Element toXml() {
        Element elem = createXmlElement("ENUM-VALUE");
        Element props = new Element("PROPERTIES");
        props.addContent(embeddedValue.toXml());
        elem.addContent(props);
        return elem;
    }
}
