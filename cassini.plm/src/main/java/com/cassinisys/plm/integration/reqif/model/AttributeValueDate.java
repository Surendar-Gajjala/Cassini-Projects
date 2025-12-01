package com.cassinisys.plm.integration.reqif.model;

import com.cassinisys.plm.service.integration.ReqIFService;
import org.jdom2.Element;

import java.util.Date;

public class AttributeValueDate extends AttributeValue {
    private Date value;

    public AttributeValueDate(AttributeDefinition def) {
        super(def);
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    public Element toXml() {
        Element elem = createXmlElement("ATTRIBUTE-VALUE-DATE");
        elem.setAttribute("THE-VALUE", "" + ReqIFService.fromDateToString(this.value));
        return elem;
    }
}
