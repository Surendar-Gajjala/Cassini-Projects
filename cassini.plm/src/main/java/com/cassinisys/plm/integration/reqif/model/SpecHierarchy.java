package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class SpecHierarchy extends BaseObject {
    private SpecObject object;
    private List<SpecHierarchy> children = new ArrayList<>();
    private boolean isTableInternal;
    private boolean isEditable;
    private List<AttributeDefinition> editableAttributes = new ArrayList<>();

    public SpecObject getObject() {
        return object;
    }

    public void setObject(SpecObject object) {
        this.object = object;
        setLongName(object.getLongName());
        setDescription(object.getDescription());
    }

    public List<SpecHierarchy> getChildren() {
        return children;
    }

    public void setChildren(List<SpecHierarchy> children) {
        this.children = children;
    }

    public List<AttributeDefinition> getEditableAttributes() {
        return editableAttributes;
    }

    public void setEditableAttributes(List<AttributeDefinition> editableAttributes) {
        this.editableAttributes = editableAttributes;
    }

    public boolean isTableInternal() {
        return isTableInternal;
    }

    public void setTableInternal(boolean tableInternal) {
        isTableInternal = tableInternal;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public Element toXml() {
        Element elem = createXmlElement("SPEC-HIERARCHY");
        elem.setAttribute("IS-TABLE-INTERNAL", "" + isTableInternal);
        elem.setAttribute("IS-EDITABLE", "" + isEditable);
        Element elemAtts = new Element("EDITABLE-ATTS");
        for (AttributeDefinition att : editableAttributes) {
            elemAtts.addContent(att.createRefElement());
        }
        elem.addContent(elemAtts);
        Element elemObject = new Element("OBJECT");
        Element elemObjRef = new Element("SPEC-OBJECT-REF");
        elemObjRef.setText(object.getIdentifier());
        elemObject.addContent(elemObjRef);
        elem.addContent(elemObject);
        Element elemChildren = new Element("CHILDREN");
        for (SpecHierarchy child : children) {
            elemChildren.addContent(child.toXml());
        }
        elem.addContent(elemChildren);
        return elem;
    }
}
