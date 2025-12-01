package com.cassinisys.plm.service.integration;

import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.plm.integration.reqif.model.*;
import com.cassinisys.plm.integration.reqif.model.Specification;
import com.cassinisys.plm.integration.reqif.model.SpecificationType;
import com.cassinisys.plm.model.rm.*;
import com.cassinisys.plm.repo.rm.*;
import com.cassinisys.plm.service.rm.RequirementsService;
import com.cassinisys.plm.service.rm.SpecificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Scope("prototype")
public class Cassini2ReqIF {
    private Map<String, DatatypeDefinition> mapDatatypes = new HashMap<>();
    private Map<String, SpecType> mapSpecTypes = new HashMap<>();
    private Map<String, AttributeDefinition> mapAttributeDefinitions = new HashMap<>();
    private Map<String, SpecObject> mapSpecObjects = new HashMap<>();
    private Map<String, com.cassinisys.plm.integration.reqif.model.Specification> mapSpecifications = new HashMap<>();
    private Map<Integer, SpecObject> mapSpecElements = new HashMap<>();

    @Autowired
    private SpecElementRepository specElementRepository;
    @Autowired
    private SpecSectionRepository specSectionRepository;
    @Autowired
    private SpecificationTypeRepository specificationTypeRepository;
    @Autowired
    private RequirementTypeRepository requirementTypeRepository;
    @Autowired
    private RmObjectTypeAttributeRepository rmObjectTypeAttributeRepository;
    @Autowired
    private RmObjectAttributeRepository rmObjectAttributeRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ReqIFService reqIFService;
    @Autowired
    private RequirementsService requirementsService;
    @Autowired
    private SpecificationsService specificationsService;

    private SystemMapping systemMapping = null;

    @Transactional
    public ReqIF toReqIF(com.cassinisys.plm.model.rm.Specification specification) {
        systemMapping = reqIFService.getReqIFMapping().getSystemByName("Doors");
        AttributeValueXhtml.clearImageObjects();
        ReqIF reqIF = new ReqIF();
        reqIF.setHeader(getHeader(specification));
        reqIF.setContent(getContent(specification));
        return reqIF;
    }

    private ReqIFHeader getHeader(com.cassinisys.plm.model.rm.Specification specification) {
        ReqIFHeader header = new ReqIFHeader();
        header.setTitle(specification.getName());
        header.setCreationDate(new Date());
        header.setComment("Cassini ReqIF exportData");
        header.setReqIFToolId("Cassini.PLM ReqIF Exporter");
        header.setReqIFVersion("1.0.0");
        header.setSourceToolId("Cassini.PLM");
        return header;
    }

    private ReqIFContent getContent(com.cassinisys.plm.model.rm.Specification specification) {
        ReqIFContent content = new ReqIFContent();
        content.setDatatypeDefinitions(getDatatypeDefinitions());
        content.setSpecTypes(getSpecTypes());
        content.setSpecObjects(getSpecObjects(specification));
        content.setSpecifications(getSpecifications(specification));
        return content;
    }

    private List<DatatypeDefinition> getDatatypeDefinitions() {
        List<DatatypeDefinition> datatypes = new ArrayList<>();
        DatatypeDefinition datatypeDefinition = new DatatypeDefinitionString();
        datatypes.add(datatypeDefinition);
        mapDatatypes.put("string", datatypeDefinition);
        datatypeDefinition = new DatatypeDefinitionInteger();
        datatypes.add(datatypeDefinition);
        mapDatatypes.put("integer", datatypeDefinition);
        datatypeDefinition = new DatatypeDefinitionReal();
        datatypes.add(datatypeDefinition);
        mapDatatypes.put("real", datatypeDefinition);
        datatypeDefinition = new DatatypeDefinitionDate();
        datatypes.add(datatypeDefinition);
        mapDatatypes.put("date", datatypeDefinition);
        datatypeDefinition = new DatatypeDefinitionBoolean();
        datatypes.add(datatypeDefinition);
        mapDatatypes.put("boolean", datatypeDefinition);
        datatypeDefinition = new DatatypeDefinitionXhtml();
        datatypes.add(datatypeDefinition);
        mapDatatypes.put("xhtml", datatypeDefinition);
        return datatypes;
    }

    private List<SpecType> getSpecTypes() {
        List<SpecType> specTypes = new ArrayList<>();
        List<AttributeDefinition> attributeDefinitions = getStandardAttributes();
        SpecObjectType objectType = new SpecObjectType();
        objectType.setLongName("Requirement Section");
        objectType.setDescription("Requirement section type");
        specTypes.add(objectType);
        objectType.getSpecAttributes().addAll(attributeDefinitions);
        mapSpecTypes.put("section", objectType);
        objectType = new SpecObjectType();
        objectType.setLongName("Requirement");
        objectType.setDescription("Requirement type");
        specTypes.add(objectType);
        objectType.getSpecAttributes().addAll(attributeDefinitions);
        addReqClassificationAttributes(objectType);
        mapSpecTypes.put("requirement", objectType);
        SpecificationType specType = new SpecificationType();
        specType.setLongName("Specification");
        specType.setDescription("Specification type");
        specTypes.add(specType);
        specType.getSpecAttributes().addAll(attributeDefinitions);
        addSpecClassificationAttributes(specType);
        mapSpecTypes.put("specification", specType);
        return specTypes;
    }

    private void addReqClassificationAttributes(SpecType specType) {
        RequirementType type = requirementTypeRepository.findByName(systemMapping.getDefaultReqType());
        if (type != null) {
            List<Integer> subtypes = requirementsService.getAllSubTypes(type.getId());
            for (Integer subtype : subtypes) {
                List<RmObjectTypeAttribute> attributes = requirementsService.getAttributes(subtype, false);
                addClassificationAttributes(specType, attributes);
            }
        }
    }

    private void addSpecClassificationAttributes(SpecType specType) {
        com.cassinisys.plm.model.rm.SpecificationType type = specificationTypeRepository.findByName(systemMapping.getDefaultSpecType());
        if (type != null) {
            List<RmObjectTypeAttribute> attributes = specificationsService.getAttributes(type.getId(), true);
            addClassificationAttributes(specType, attributes);
        }
    }

    private void addClassificationAttributes(SpecType specType, List<RmObjectTypeAttribute> attributes) {
        for (RmObjectTypeAttribute attribute : attributes) {
            AttributeMapping attributeMapping = systemMapping.findByCassiniName(attribute.getName());
            //if(attributeMapping != null) {
            String mappedName = attribute.getName();
            if (attributeMapping != null && attributeMapping.getSystemName() != null &&
                    !attributeMapping.getSystemName().trim().isEmpty()) {
                mappedName = attributeMapping.getSystemName().trim();
            }
            AttributeDefinition attributeDefinition = null;
            if (attribute.getDataType() == DataType.TEXT || attribute.getDataType() == DataType.LONGTEXT) {
                attributeDefinition = new AttributeDefinitionString(mapDatatypes.get("string"));
            } else if (attribute.getDataType() == DataType.INTEGER) {
                attributeDefinition = new AttributeDefinitionInteger(mapDatatypes.get("integer"));
            } else if (attribute.getDataType() == DataType.DOUBLE) {
                attributeDefinition = new AttributeDefinitionReal(mapDatatypes.get("real"));
            } else if (attribute.getDataType() == DataType.DATE) {
                attributeDefinition = new AttributeDefinitionDate(mapDatatypes.get("date"));
            } else if (attribute.getDataType() == DataType.BOOLEAN) {
                attributeDefinition = new AttributeDefinitionBoolean(mapDatatypes.get("boolean"));
            } else if (attribute.getDataType() == DataType.RICHTEXT) {
                attributeDefinition = new AttributeDefinitionXhtml(mapDatatypes.get("xhtml"));
            } else if (attribute.getDataType() == DataType.LIST) {
                attributeDefinition = new AttributeDefinitionString(mapDatatypes.get("string"));
            } else if (attribute.getDataType() == DataType.OBJECT) {
                attributeDefinition = new AttributeDefinitionInteger(mapDatatypes.get("integer"));
            }
            if (attributeDefinition != null) {
                attributeDefinition.setLongName(mappedName);
                attributeDefinition.setDescription(attribute.getDescription());
                specType.getSpecAttributes().add(attributeDefinition);
            }
            //}
        }
    }

    private List<AttributeDefinition> getStandardAttributes() {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        AttributeDefinition attributeDefinition = new AttributeDefinitionInteger(mapDatatypes.get("integer"));
        attributeDefinition.setLongName("Cassini ID");
        attributeDefinition.setDescription("Cassini ID");
        attributeDefinitions.add(attributeDefinition);
        attributeDefinition = new AttributeDefinitionXhtml(mapDatatypes.get("xhtml"));
        attributeDefinition.setLongName("CASSINI-FILE-ATTACHMENTS");
        attributeDefinition.setDescription("CASSINI-FILE-ATTACHMENTS");
        attributeDefinitions.add(attributeDefinition);
        attributeDefinition = new AttributeDefinitionString(mapDatatypes.get("string"));
        attributeDefinition.setLongName("Cassini.Name");
        attributeDefinition.setDescription("Cassini.Name");
        attributeDefinitions.add(attributeDefinition);
        attributeDefinition = new AttributeDefinitionXhtml(mapDatatypes.get("xhtml"));
        attributeDefinition.setLongName("Cassini.Description");
        attributeDefinition.setDescription("Cassini.Description");
        attributeDefinitions.add(attributeDefinition);
        attributeDefinition = new AttributeDefinitionString(mapDatatypes.get("string"));
        attributeDefinition.setLongName("Cassini.Number");
        attributeDefinition.setDescription("Cassini.Number");
        attributeDefinitions.add(attributeDefinition);
        attributeDefinition = new AttributeDefinitionString(mapDatatypes.get("string"));
        attributeDefinition.setLongName("Cassini.Status");
        attributeDefinition.setDescription("Cassini.Status");
        attributeDefinitions.add(attributeDefinition);
        attributeDefinition = new AttributeDefinitionString(mapDatatypes.get("string"));
        attributeDefinition.setLongName("Cassini.Type");
        attributeDefinition.setDescription("Cassini.Type");
        attributeDefinitions.add(attributeDefinition);
        attributeDefinition = new AttributeDefinitionInteger(mapDatatypes.get("integer"));
        attributeDefinition.setLongName("Cassini.AssignedTo");
        attributeDefinition.setDescription("Cassini.AssignedTo");
        attributeDefinitions.add(attributeDefinition);
        attributeDefinition = new AttributeDefinitionDate(mapDatatypes.get("date"));
        attributeDefinition.setLongName("Cassini.PlannedFinishDate");
        attributeDefinition.setDescription("Cassini.PlannedFinishDate");
        attributeDefinitions.add(attributeDefinition);
        attributeDefinition = new AttributeDefinitionString(mapDatatypes.get("string"));
        attributeDefinition.setLongName("ReqIF.ChapterName");
        attributeDefinition.setDescription("ReqIF.ChapterName");
        attributeDefinitions.add(attributeDefinition);
        attributeDefinition = new AttributeDefinitionString(mapDatatypes.get("string"));
        attributeDefinition.setLongName("ReqIF.Name");
        attributeDefinition.setDescription("ReqIF.Name");
        attributeDefinitions.add(attributeDefinition);
        attributeDefinition = new AttributeDefinitionXhtml(mapDatatypes.get("xhtml"));
        attributeDefinition.setLongName("ReqIF.Text");
        attributeDefinition.setDescription("ReqIF.Text");
        attributeDefinitions.add(attributeDefinition);
        return attributeDefinitions;
    }

    private List<SpecObject> getSpecObjects(com.cassinisys.plm.model.rm.Specification specification) {
        List<SpecObject> specObjects = new ArrayList<>();
        List<SpecElement> specElements = specElementRepository.findBySpecification(specification.getId());
        for (SpecElement specElement : specElements) {
            SpecType specType = null;
            SpecObject specObject = new SpecObject();
            if (specElement.getType() == SpecElementType.SECTION) {
                specType = mapSpecTypes.get("section");
                SpecSection specSection = (SpecSection) specElement;
                specObject.setLastChange(specSection.getModifiedDate());
                specObject.setType((SpecObjectType) specType);
                specObject.setLongName(specSection.getName());
                specObject.setDescription(specSection.getDescription());
                List<AttributeValue> attributeValues = new ArrayList<>();
                specObject.setValues(attributeValues);
                addStandardAttributeValues(attributeValues, specType, specObject, specElement);
                mapSpecElements.put(specSection.getId(), specObject);
            } else if (specElement.getType() == SpecElementType.REQUIREMENT) {
                specType = mapSpecTypes.get("requirement");
                SpecRequirement specReq = (SpecRequirement) specElement;
                Requirement requirement = specReq.getRequirement();
                specObject.setLastChange(requirement.getModifiedDate());
                specObject.setType((SpecObjectType) specType);
                specObject.setLongName(requirement.getName());
                //specObject.setDescription(requirement.getDescription());
                List<AttributeValue> attributeValues = new ArrayList<>();
                specObject.setValues(attributeValues);
                addStandardAttributeValues(attributeValues, specType, specObject, specElement);
                com.cassinisys.plm.model.rm.RequirementType reqType = null;//requirementTypeRepository.findByName(systemMapping.getDefaultReqType());
                AttributeValueString tyepAttribute = (AttributeValueString) specObject.getAttributeValueByAttributeName("Cassini.Type");
                if (tyepAttribute != null) {
                    reqType = requirementTypeRepository.findByName(tyepAttribute.getValue());
                }
                if (reqType == null) {
                    reqType = requirementTypeRepository.findByName(systemMapping.getDefaultReqType());
                }
                if (reqType != null) {
                    List<RmObjectTypeAttribute> attributes = requirementsService.getAttributes(reqType.getId(), true);//rmObjectTypeAttributeRepository.findByRmObjectTypeOrderByName(reqType.getId());
                    addAttributeValues(requirement.getId(), specType, attributes, attributeValues);

                }
                mapSpecElements.put(requirement.getId(), specObject);
            }
            specObjects.add(specObject);
            mapSpecObjects.put(specObject.getIdentifier(), specObject);
        }
        return specObjects;
    }

    private void addStandardAttributeValues(List<AttributeValue> attributeValues, SpecType specType, SpecObject specObject, SpecElement specElement) {
        int id = 0;
        String name = "";
        String description = "";
        String number = "";
        String status = "";
        String type = "";
        Date finishDate = null;
        Integer assignedTo = 0;
        if (specElement.getType() == SpecElementType.SECTION) {
            SpecSection specSection = (SpecSection) specElement;
            id = specSection.getId();
            name = specSection.getName();
            description = specSection.getDescription();
            type = "Section";
        } else if (specElement.getType() == SpecElementType.REQUIREMENT) {
            SpecRequirement specReq = (SpecRequirement) specElement;
            id = specReq.getRequirement().getId();
            name = specReq.getRequirement().getName();
            description = specReq.getRequirement().getDescription();
            number = specReq.getRequirement().getObjectNumber();
            status = specReq.getRequirement().getStatus().toString();
            type = specReq.getRequirement().getType().getName();
            assignedTo = specReq.getRequirement().getAssignedTo() != null ? specReq.getRequirement().getAssignedTo().getId() : 0;
            finishDate = specReq.getRequirement().getPlannedFinishDate();
        }
        AttributeValueInteger idValue = new AttributeValueInteger(specType.getSpecAttributeByName("Cassini ID"));
        idValue.setValue(id);
        attributeValues.add(idValue);
        AttributeValueString typeAttValue = new AttributeValueString(specType.getSpecAttributeByName("Cassini.Type"));
        typeAttValue.setValue(type);
        attributeValues.add(typeAttValue);
        AttributeValueString nameAttValue = new AttributeValueString(specType.getSpecAttributeByName("Cassini.Name"));
        nameAttValue.setValue(name);
        attributeValues.add(nameAttValue);
        AttributeValueXhtml descAttValue = new AttributeValueXhtml(specType.getSpecAttributeByName("Cassini.Description"));
        descAttValue.setValue("<p>" + description + "</p>");
        attributeValues.add(descAttValue);
        AttributeValueString numberAttValue = new AttributeValueString(specType.getSpecAttributeByName("Cassini.Number"));
        numberAttValue.setValue(number);
        attributeValues.add(numberAttValue);
        AttributeValueString statusAttValue = new AttributeValueString(specType.getSpecAttributeByName("Cassini.Status"));
        statusAttValue.setValue(status);
        attributeValues.add(statusAttValue);
        if (specElement.getType() == SpecElementType.SECTION) {
            SpecSection specSection = (SpecSection) specElement;
            AttributeValueString attValue = new AttributeValueString(specType.getSpecAttributeByName("ReqIF.ChapterName"));
            attValue.setValue(specSection.getName());
            attributeValues.add(attValue);
            attValue = new AttributeValueString(specType.getSpecAttributeByName("ReqIF.Text"));
            attValue.setValue(specSection.getDescription());
            attributeValues.add(attValue);
        } else if (specElement.getType() == SpecElementType.REQUIREMENT) {
            SpecRequirement specReq = (SpecRequirement) specElement;
            AttributeValueString attValue = new AttributeValueString(specType.getSpecAttributeByName("ReqIF.Name"));
            attValue.setValue(specReq.getRequirement().getName());
            attributeValues.add(attValue);
            AttributeValueXhtml xhtmlAtt = new AttributeValueXhtml(specType.getSpecAttributeByName("ReqIF.Text"));
            xhtmlAtt.setValue(specReq.getRequirement().getDescription());
            attributeValues.add(xhtmlAtt);
            AttributeValueInteger assignToAttValue = new AttributeValueInteger(specType.getSpecAttributeByName("Cassini.AssignedTo"));
            assignToAttValue.setValue(assignedTo);
            attributeValues.add(assignToAttValue);
            AttributeValueDate finishDateValue = new AttributeValueDate(specType.getSpecAttributeByName("Cassini.PlannedFinishDate"));
            finishDateValue.setValue(finishDate);
            attributeValues.add(finishDateValue);
        }

    }

    private void addAttributeValues(Integer objectId, SpecType reqType,
                                    List<RmObjectTypeAttribute> attributes,
                                    List<AttributeValue> attributeValues) {
        Map<Integer, RmObjectAttribute> mapAtts = new HashMap<>();
        List<RmObjectAttribute> cassiniAttValues = rmObjectAttributeRepository.findByObjectId(objectId);
        for (RmObjectAttribute cassiniAttValue : cassiniAttValues) {
            mapAtts.put(cassiniAttValue.getId().getAttributeDef(), cassiniAttValue);
        }
        for (RmObjectTypeAttribute attribute : attributes) {
            AttributeMapping attributeMapping = systemMapping.findByCassiniName(attribute.getName());
            //if(attributeMapping != null) {
            String mappedName = attribute.getName();
            if (attributeMapping != null && attributeMapping.getSystemName() != null &&
                    !attributeMapping.getSystemName().trim().isEmpty()) {
                mappedName = attributeMapping.getSystemName().trim();
            }
            AttributeValue attributeValue = null;
            ObjectAttribute objectAttribute = mapAtts.get(attribute.getId());
            if (objectAttribute == null) continue;
            if (attribute.getDataType() == DataType.TEXT) {
                attributeValue = new AttributeValueString(reqType.getSpecAttributeByName(mappedName));
                ((AttributeValueString) attributeValue).setValue(objectAttribute.getStringValue());
            } else if (attribute.getDataType() == DataType.LONGTEXT) {
                attributeValue = new AttributeValueString(reqType.getSpecAttributeByName(mappedName));
                ((AttributeValueString) attributeValue).setValue(objectAttribute.getLongTextValue());
            } else if (attribute.getDataType() == DataType.INTEGER) {
                attributeValue = new AttributeValueInteger(reqType.getSpecAttributeByName(mappedName));
                ((AttributeValueInteger) attributeValue).setValue(objectAttribute.getIntegerValue());
            } else if (attribute.getDataType() == DataType.DOUBLE) {
                attributeValue = new AttributeValueReal(reqType.getSpecAttributeByName(mappedName));
                ((AttributeValueReal) attributeValue).setValue((objectAttribute.getDoubleValue().floatValue()));
            } else if (attribute.getDataType() == DataType.DATE) {
                attributeValue = new AttributeValueDate(reqType.getSpecAttributeByName(attribute.getName()));
                ((AttributeValueDate) attributeValue).setValue(objectAttribute.getDateValue());
            } else if (attribute.getDataType() == DataType.BOOLEAN) {
                attributeValue = new AttributeValueBoolean(reqType.getSpecAttributeByName(mappedName));
                ((AttributeValueBoolean) attributeValue).setValue(objectAttribute.getBooleanValue());
            } else if (attribute.getDataType() == DataType.RICHTEXT) {
                attributeValue = new AttributeValueXhtml(reqType.getSpecAttributeByName(mappedName));
                ((AttributeValueXhtml) attributeValue).setValue(objectAttribute.getRichTextValue());
            } else if (attribute.getDataType() == DataType.LIST) {
                attributeValue = new AttributeValueString(reqType.getSpecAttributeByName(mappedName));
                if (attribute.isListMultiple()) {
                    String[] arr = objectAttribute.getMListValue();
                    if (arr != null) {
                        String s = "";
                        for (int i = 0; i < arr.length; i++) {
                            s += arr[i];
                            if (i != arr.length - 1) {
                                s += ";";
                            }
                        }
                        ((AttributeValueString) attributeValue).setValue(s);
                    }
                } else {
                    ((AttributeValueString) attributeValue).setValue(objectAttribute.getListValue());
                }
            } else if (attribute.getDataType() == DataType.OBJECT) {
                attributeValue = new AttributeValueInteger(reqType.getSpecAttributeByName(mappedName));
                ((AttributeValueInteger) attributeValue).setValue(objectAttribute.getRefValue());
            }
            if (attributeValue != null) {
                attributeValues.add(attributeValue);
            }
            //}
        }
    }

    private List<Specification> getSpecifications(com.cassinisys.plm.model.rm.Specification specification) {
        List<Specification> specifications = new ArrayList<>();
        Specification reqIfSpecification = new Specification();
        reqIfSpecification.setLastChange(specification.getModifiedDate());
        reqIfSpecification.setLongName(specification.getName());
        reqIfSpecification.setDescription(specification.getDescription());
        List<AttributeValue> attributeValues = new ArrayList<>();
        SpecType specType = mapSpecTypes.get("specification");
        reqIfSpecification.setType((SpecificationType) specType);
        AttributeValueInteger idValue = new AttributeValueInteger(specType.getSpecAttributeByName("Cassini ID"));
        idValue.setValue(specification.getId());
        attributeValues.add(idValue);
        com.cassinisys.plm.model.rm.SpecificationType specTypeObj = specificationTypeRepository.findByName(systemMapping.getDefaultSpecType());
        if (specTypeObj != null) {
            List<RmObjectTypeAttribute> attributes = specificationsService.getAttributes(specTypeObj.getId(), true);//rmObjectTypeAttributeRepository.findByRmObjectTypeOrderByName(specTypeObj.getId());
            addAttributeValues(specification.getId(), specType, attributes, attributeValues);
        }
        reqIfSpecification.setValues(attributeValues);
        List<SpecHierarchy> children = new ArrayList<>();
        List<SpecSection> specSectionList = specSectionRepository.findBySpecificationAndParentIsNullOrderByCreatedDateAsc(specification.getId());
        for (SpecSection specSection : specSectionList) {
            SpecHierarchy specHierarchy = new SpecHierarchy();
            specHierarchy.setObject(mapSpecElements.get(specSection.getId()));
            createHierarchy(specHierarchy, specSection);
            children.add(specHierarchy);
        }
        if (children.size() > 0) {
            reqIfSpecification.setChildren(children);
        }
        specifications.add(reqIfSpecification);
        return specifications;
    }

    private void createHierarchy(SpecHierarchy parent, SpecSection specSection) {
        List<SpecHierarchy> children = new ArrayList<>();
        List<SpecElement> elements = specElementRepository.findByParentOrderByCreatedDateAsc(specSection.getId());
        for (SpecElement elem : elements) {
            SpecHierarchy specHierarchy = new SpecHierarchy();
            if (elem.getType() == SpecElementType.SECTION) {
                SpecSection section = ((SpecSection) elem);
                specHierarchy.setObject(mapSpecElements.get(section.getId()));
                createHierarchy(specHierarchy, section);
            } else if (elem.getType() == SpecElementType.REQUIREMENT) {
                Requirement requirement = ((SpecRequirement) elem).getRequirement();
                specHierarchy.setObject(mapSpecElements.get(requirement.getId()));
            }
            children.add(specHierarchy);
        }
        if (children.size() > 0) {
            parent.setChildren(children);
        }
    }
}
