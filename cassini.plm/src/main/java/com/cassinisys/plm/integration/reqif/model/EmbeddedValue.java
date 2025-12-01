package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public class EmbeddedValue {
    private int key;
    private String otherContent;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getOtherContent() {
        return otherContent;
    }

    public void setOtherContent(String otherContent) {
        this.otherContent = otherContent;
    }

    public Element toXml() {
        Element elem = new Element("EMBEDDED-VALUE");
        elem.setAttribute("KEY", "" + this.key);
        elem.setAttribute("OTHER-CONTENT", this.otherContent);
        return elem;
    }
}
