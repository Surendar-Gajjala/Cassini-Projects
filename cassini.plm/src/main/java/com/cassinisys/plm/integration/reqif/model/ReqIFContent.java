package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReqIFContent {
    private List<DatatypeDefinition> datatypeDefinitions = new ArrayList<>();
    private List<SpecType> specTypes = new ArrayList<>();
    private List<SpecObject> specObjects = new ArrayList<>();
    private List<Specification> specifications = new ArrayList<>();
    private Map<String, BaseObject> mapIdentifiers = new HashMap<>();

    public List<DatatypeDefinition> getDatatypeDefinitions() {
        return datatypeDefinitions;
    }

    public void setDatatypeDefinitions(List<DatatypeDefinition> datatypeDefinitions) {
        this.datatypeDefinitions = datatypeDefinitions;
    }

    public List<SpecType> getSpecTypes() {
        return specTypes;
    }

    public void setSpecTypes(List<SpecType> specTypes) {
        this.specTypes = specTypes;
    }

    public List<SpecObject> getSpecObjects() {
        return specObjects;
    }

    public void setSpecObjects(List<SpecObject> specObjects) {
        this.specObjects = specObjects;
    }

    public List<Specification> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<Specification> specifications) {
        this.specifications = specifications;
    }

    public Element toXml() {
        Element elem = new Element("CORE-CONTENT");
        Element elemContent = new Element("REQ-IF-CONTENT");
        Element elemDataTypes = new Element("DATATYPES");
        for (DatatypeDefinition datatype : datatypeDefinitions) {
            mapIdentifiers.put(datatype.getIdentifier(), datatype);
            elemDataTypes.addContent(datatype.toXml());
        }
        elemContent.addContent(elemDataTypes);
        Element specTypesElem = new Element("SPEC-TYPES");
        for (SpecType specType : specTypes) {
            mapIdentifiers.put(specType.getIdentifier(), specType);
            specTypesElem.addContent(specType.toXml());
        }
        elemContent.addContent(specTypesElem);
        Element specObjectsElem = new Element("SPEC-OBJECTS");
        for (SpecObject specObject : specObjects) {
            mapIdentifiers.put(specObject.getIdentifier(), specObject);
            specObjectsElem.addContent(specObject.toXml());
        }
        elemContent.addContent(specObjectsElem);
        Element specificationsElem = new Element("SPECIFICATIONS");
        for (Specification specification : specifications) {
            mapIdentifiers.put(specification.getIdentifier(), specification);
            specificationsElem.addContent(specification.toXml());
        }
        elemContent.addContent(specificationsElem);
        elem.addContent(elemContent);
        return elem;
    }

    public BaseObject getObjectByIdentifier(String identifier) {
        return mapIdentifiers.get(identifier);
    }

    public SpecObject getSpecObjectByIntegerAttribute(String attName, Integer intValue) {
        SpecObject specObject = null;
        for (SpecObject so : specObjects) {
            Integer id = so.getIntegerAttributeValue(attName);
            if (id != null && id.equals(intValue)) {
                specObject = so;
                break;
            }
        }
        return specObject;
    }

    public Specification getSpecificationByIntegerAttribute(String attName, Integer intValue) {
        Specification specification = null;
        for (Specification spec : specifications) {
            Integer id = spec.getIntegerAttributeValue(attName);
            if (id != null && id.equals(intValue)) {
                specification = spec;
                break;
            }
        }
        return specification;
    }

}
