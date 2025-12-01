package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public abstract class BaseObject {
    private String identifier = UUID.randomUUID().toString().toLowerCase();
    private Date lastChange = new Date();
    private String longName = "";
    private String description = "";

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Date getLastChange() {
        return lastChange;
    }

    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Element createXmlElement(String elementName) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Element elem = new Element(elementName);
        elem.setAttribute("IDENTIFIER", identifier != null ? identifier : "");
        elem.setAttribute("LONG-NAME", longName != null ? longName : "");
        elem.setAttribute("DESC", description != null ? description : "");
        elem.setAttribute("LAST-CHANGE", df.format(lastChange));
        return elem;
    }
}
