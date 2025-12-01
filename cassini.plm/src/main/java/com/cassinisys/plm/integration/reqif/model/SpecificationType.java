package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public class SpecificationType extends SpecType {

    @Override
    public Element toXml() {
        return super.toXml("SPECIFICATION-TYPE");
    }
}
