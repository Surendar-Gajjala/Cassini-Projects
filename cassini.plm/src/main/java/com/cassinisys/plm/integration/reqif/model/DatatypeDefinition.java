package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

public abstract class DatatypeDefinition extends BaseObject {
    protected abstract Element toXml();
}
