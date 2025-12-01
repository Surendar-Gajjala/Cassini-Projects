package com.cassinisys.plm.model.dto.print;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.plm.model.cm.PLMECO;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.MESObjectTypeAttribute;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import com.cassinisys.plm.model.mro.MROObjectTypeAttribute;
import com.cassinisys.plm.model.pgc.PGCObjectAttribute;
import com.cassinisys.plm.model.pgc.PGCObjectTypeAttribute;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.repo.cm.ECORepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.ProjectRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
public class PrintAttributesDTO {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ItemAttributeRepository itemAttributeRepository;
    @Autowired
    private ItemRevisionAttributeRepository itemRevisionAttributeRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    private String name;
    private String value;
    private String group;


    @Transactional
    public PrintAttributesDTO getMasterAttributes(PLMItemTypeAttribute typeAttribute, PLMItemAttribute attribute) {
        PLMItemTypeAttribute itemTypeAttribute = typeAttribute;
        PrintAttributesDTO dto = new PrintAttributesDTO();

        if (itemTypeAttribute.getDataType().toString().equals("INTEGER")) {
            dto = getIntegerValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("DOUBLE")) {
            dto = getDoubleValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("DATE")) {
            dto = getDateValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIME")) {
            dto = getTimeValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIMESTAMP")) {
            dto = getTimeStampValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("BOOLEAN")) {
            dto = getBooleanValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("CURRENCY")) {
            dto = getCurrencyValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("LIST")) {
            String list = "";
            if (itemTypeAttribute.isListMultiple()) {
                for (String lElement : attribute.getMListValue()) {
                    if (list == "") {
                        list = lElement;
                    } else {
                        list = list + " ," + lElement;
                    }
                }
                dto.setName(itemTypeAttribute.getName());
                dto.setValue(list);

            } else {
                if (!itemTypeAttribute.isListMultiple()) {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(attribute.getListValue().toString());
                }

            }

        }
        if (itemTypeAttribute.getDataType().toString().equals("OBJECT")) {
            if (itemTypeAttribute.getRefType().toString().equals("ITEM")) {
                if (attribute.getRefValue() != null) {
                    PLMItem item = itemRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("ITEMREVISION")) {
                if (attribute.getRefValue() != null) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(attribute.getRefValue());
                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("CHANGE")) {
                if (attribute.getRefValue() != null) {
                    PLMECO fromPlmItem = ecoRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromPlmItem.getEcoNumber().toString());
                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("WORKFLOW")) {

                if (attribute.getRefValue() != null) {
                    PLMWorkflowDefinition fromworkflownewValue = workFlowDefinitionRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromworkflownewValue.getName());
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURER")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(manufacturernewValue.getName());

                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURERPART")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(attribute.getRefValue());
                    dto.setValue(manufacturernewValue.getPartName());
                    dto.setName(itemTypeAttribute.getName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("PROJECT")) {

                if (attribute.getRefValue() != null) {
                    PLMProject personNewValue = projectRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getName());
                } else {

                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");

                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("PERSON")) {
                if (attribute.getRefValue() != null) {
                    Person personNewValue = personRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getFullName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }
            }
        }

        if (itemTypeAttribute.getDataType().toString().equals("TEXT")) {
            dto = getTextValues(itemTypeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("LONGTEXT")) {
            dto = getLongTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("RICHTEXT")) {
            dto = getRichTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("HYPERLINK")) {
            dto = getHyperLinkValues(itemTypeAttribute, attribute);
        }

        return dto;

    }


    public PrintAttributesDTO getIntegerValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getIntegerValue().toString());
        return dto;
    }


    public PrintAttributesDTO getDoubleValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDoubleValue().toString());
        return dto;
    }

    public PrintAttributesDTO getDateValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDateValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimeValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeStampValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimestampValue().toString());

        return dto;
    }

    public PrintAttributesDTO getBooleanValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        String fBool = String.valueOf(attribute.getBooleanValue());
        dto.setName(typeAttribute.getName());
        dto.setValue(fBool);
        return dto;
    }


    public PrintAttributesDTO getCurrencyValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getCurrencyValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTextValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getStringValue());
        return dto;
    }

    public PrintAttributesDTO getLongTextValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getLongTextValue());
        return dto;
    }


    public PrintAttributesDTO getRichTextValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getRichTextValue());
        return dto;
    }

    public PrintAttributesDTO getHyperLinkValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getHyperLinkValue());
        return dto;
    }


    @Transactional
    public PrintAttributesDTO getRevisionAttributes(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute attribute) {
        PLMItemTypeAttribute itemTypeAttribute = typeAttribute;
        PrintAttributesDTO dto = new PrintAttributesDTO();

        if (itemTypeAttribute.getDataType().toString().equals("INTEGER")) {
            dto = getIntegerValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("DOUBLE")) {
            dto = getDoubleValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("DATE")) {
            dto = getDateValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIME")) {
            dto = getTimeValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIMESTAMP")) {
            dto = getTimeStampValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("BOOLEAN")) {
            dto = getBooleanValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("CURRENCY")) {
            dto = getCurrencyValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("LIST")) {
            String list = "";
            if (itemTypeAttribute.isListMultiple()) {
                for (String lElement : attribute.getMListValue()) {
                    if (list == "") {
                        list = lElement;
                    } else {
                        list = list + " ," + lElement;
                    }
                }
                dto.setName(itemTypeAttribute.getName());
                dto.setValue(list);

            } else {
                if (!itemTypeAttribute.isListMultiple()) {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(attribute.getListValue().toString());
                }

            }

        }
        if (itemTypeAttribute.getDataType().toString().equals("OBJECT")) {
            if (itemTypeAttribute.getRefType().toString().equals("ITEM")) {
                if (attribute.getRefValue() != null) {
                    PLMItem item = itemRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("ITEMREVISION")) {
                if (attribute.getRefValue() != null) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(attribute.getRefValue());
                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("CHANGE")) {
                if (attribute.getRefValue() != null) {
                    PLMECO fromPlmItem = ecoRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromPlmItem.getEcoNumber().toString());
                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("WORKFLOW")) {

                if (attribute.getRefValue() != null) {
                    PLMWorkflowDefinition fromworkflownewValue = workFlowDefinitionRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromworkflownewValue.getName());
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURER")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(manufacturernewValue.getName());

                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURERPART")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(attribute.getRefValue());
                    dto.setValue(manufacturernewValue.getPartName());
                    dto.setName(itemTypeAttribute.getName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("PROJECT")) {

                if (attribute.getRefValue() != null) {
                    PLMProject personNewValue = projectRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getName());
                } else {

                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");

                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("PERSON")) {
                if (attribute.getRefValue() != null) {
                    Person personNewValue = personRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getFullName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }
            }
        }

        if (itemTypeAttribute.getDataType().toString().equals("TEXT")) {
            dto = getTextValues(itemTypeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("LONGTEXT")) {
            dto = getLongTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("RICHTEXT")) {
            dto = getRichTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("HYPERLINK")) {
            dto = getHyperLinkValues(itemTypeAttribute, attribute);
        }

        return dto;

    }


    public PrintAttributesDTO getIntegerValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getIntegerValue().toString());
        return dto;
    }


    public PrintAttributesDTO getDoubleValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDoubleValue().toString());
        return dto;
    }

    public PrintAttributesDTO getDateValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDateValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimeValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeStampValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimestampValue().toString());

        return dto;
    }

    public PrintAttributesDTO getBooleanValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        String fBool = String.valueOf(attribute.getBooleanValue());
        dto.setName(typeAttribute.getName());
        dto.setValue(fBool);
        return dto;
    }


    public PrintAttributesDTO getCurrencyValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getCurrencyValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTextValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getStringValue());
        return dto;
    }

    public PrintAttributesDTO getLongTextValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getLongTextValue());
        return dto;
    }


    public PrintAttributesDTO getRichTextValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getRichTextValue());
        return dto;
    }

    public PrintAttributesDTO getHyperLinkValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getHyperLinkValue());
        return dto;
    }


    @Transactional
    public PrintAttributesDTO getItemCustomAttributes(ObjectTypeAttribute typeAttribute, ObjectAttribute attribute) {
        ObjectTypeAttribute itemTypeAttribute = typeAttribute;
        PrintAttributesDTO dto = new PrintAttributesDTO();

        if (itemTypeAttribute.getDataType().toString().equals("INTEGER")) {
            dto = getIntegerValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("DOUBLE")) {
            dto = getDoubleValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("DATE")) {
            dto = getDateValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIME")) {
            dto = getTimeValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIMESTAMP")) {
            dto = getTimeStampValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("BOOLEAN")) {
            dto = getBooleanValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("CURRENCY")) {
            dto = getCurrencyValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("LIST")) {
            String list = "";
            if (itemTypeAttribute.isListMultiple()) {
                for (String lElement : attribute.getMListValue()) {
                    if (list == "") {
                        list = lElement;
                    } else {
                        list = list + " ," + lElement;
                    }
                }
                dto.setName(itemTypeAttribute.getName());
                dto.setValue(list);

            } else {
                if (!itemTypeAttribute.isListMultiple()) {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(attribute.getListValue().toString());
                }

            }

        }
        if (itemTypeAttribute.getDataType().toString().equals("OBJECT")) {
            if (itemTypeAttribute.getRefType().toString().equals("ITEM")) {
                if (attribute.getRefValue() != null) {
                    PLMItem item = itemRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("ITEMREVISION")) {
                if (attribute.getRefValue() != null) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(attribute.getRefValue());
                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("CHANGE")) {
                if (attribute.getRefValue() != null) {
                    PLMECO fromPlmItem = ecoRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromPlmItem.getEcoNumber().toString());
                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("WORKFLOW")) {

                if (attribute.getRefValue() != null) {
                    PLMWorkflowDefinition fromworkflownewValue = workFlowDefinitionRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromworkflownewValue.getName());
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURER")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(manufacturernewValue.getName());

                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURERPART")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(attribute.getRefValue());
                    dto.setValue(manufacturernewValue.getPartName());
                    dto.setName(itemTypeAttribute.getName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("PROJECT")) {

                if (attribute.getRefValue() != null) {
                    PLMProject personNewValue = projectRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getName());
                } else {

                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");

                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("PERSON")) {
                if (attribute.getRefValue() != null) {
                    Person personNewValue = personRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getFullName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }
            }
        }

        if (itemTypeAttribute.getDataType().toString().equals("TEXT")) {
            dto = getTextValues(itemTypeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("LONGTEXT")) {
            dto = getLongTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("RICHTEXT")) {
            dto = getRichTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("HYPERLINK")) {
            dto = getHyperLinkValues(itemTypeAttribute, attribute);
        }

        return dto;

    }


    public PrintAttributesDTO getIntegerValues(ObjectTypeAttribute typeAttribute, ObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getIntegerValue().toString());
        return dto;
    }


    public PrintAttributesDTO getDoubleValues(ObjectTypeAttribute typeAttribute, ObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDoubleValue().toString());
        return dto;
    }

    public PrintAttributesDTO getDateValues(ObjectTypeAttribute typeAttribute, ObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDateValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeValues(ObjectTypeAttribute typeAttribute, ObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimeValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeStampValues(ObjectTypeAttribute typeAttribute, ObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimestampValue().toString());

        return dto;
    }

    public PrintAttributesDTO getBooleanValues(ObjectTypeAttribute typeAttribute, ObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        String fBool = String.valueOf(attribute.getBooleanValue());
        dto.setName(typeAttribute.getName());
        dto.setValue(fBool);
        return dto;
    }


    public PrintAttributesDTO getCurrencyValues(ObjectTypeAttribute typeAttribute, ObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getCurrencyValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTextValues(ObjectTypeAttribute typeAttribute, ObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getStringValue());
        return dto;
    }

    public PrintAttributesDTO getLongTextValues(ObjectTypeAttribute typeAttribute, ObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getLongTextValue());
        return dto;
    }


    public PrintAttributesDTO getRichTextValues(ObjectTypeAttribute typeAttribute, ObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getRichTextValue());
        return dto;
    }

    public PrintAttributesDTO getHyperLinkValues(ObjectTypeAttribute typeAttribute, ObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getHyperLinkValue());
        return dto;
    }

    /*
    * Manufacturing Object Attributes For Classification
    * */

    @Transactional
    public PrintAttributesDTO getMesObjectAttributes(MESObjectTypeAttribute typeAttribute, MESObjectAttribute attribute) {
        MESObjectTypeAttribute itemTypeAttribute = typeAttribute;
        PrintAttributesDTO dto = new PrintAttributesDTO();

        if (itemTypeAttribute.getDataType().toString().equals("INTEGER")) {
            dto = getIntegerValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("DOUBLE")) {
            dto = getDoubleValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("DATE")) {
            dto = getDateValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIME")) {
            dto = getTimeValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIMESTAMP")) {
            dto = getTimeStampValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("BOOLEAN")) {
            dto = getBooleanValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("CURRENCY")) {
            dto = getCurrencyValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("LIST")) {
            String list = "";
            if (itemTypeAttribute.isListMultiple()) {
                for (String lElement : attribute.getMListValue()) {
                    if (list == "") {
                        list = lElement;
                    } else {
                        list = list + " ," + lElement;
                    }
                }
                dto.setName(itemTypeAttribute.getName());
                dto.setValue(list);

            } else {
                if (!itemTypeAttribute.isListMultiple()) {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(attribute.getListValue().toString());
                }

            }

        }
        if (itemTypeAttribute.getDataType().toString().equals("OBJECT")) {
            if (itemTypeAttribute.getRefType().toString().equals("ITEM")) {
                if (attribute.getRefValue() != null) {
                    PLMItem item = itemRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("ITEMREVISION")) {
                if (attribute.getRefValue() != null) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(attribute.getRefValue());
                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("CHANGE")) {
                if (attribute.getRefValue() != null) {
                    PLMECO fromPlmItem = ecoRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromPlmItem.getEcoNumber().toString());
                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("WORKFLOW")) {

                if (attribute.getRefValue() != null) {
                    PLMWorkflowDefinition fromworkflownewValue = workFlowDefinitionRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromworkflownewValue.getName());
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURER")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(manufacturernewValue.getName());

                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURERPART")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(attribute.getRefValue());
                    dto.setValue(manufacturernewValue.getPartName());
                    dto.setName(itemTypeAttribute.getName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("PROJECT")) {

                if (attribute.getRefValue() != null) {
                    PLMProject personNewValue = projectRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getName());
                } else {

                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");

                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("PERSON")) {
                if (attribute.getRefValue() != null) {
                    Person personNewValue = personRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getFullName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }
            }
        }

        if (itemTypeAttribute.getDataType().toString().equals("TEXT")) {
            dto = getTextValues(itemTypeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("LONGTEXT")) {
            dto = getLongTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("RICHTEXT")) {
            dto = getRichTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("HYPERLINK")) {
            dto = getHyperLinkValues(itemTypeAttribute, attribute);
        }

        return dto;

    }


    public PrintAttributesDTO getIntegerValues(MESObjectTypeAttribute typeAttribute, MESObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getIntegerValue().toString());
        return dto;
    }


    public PrintAttributesDTO getDoubleValues(MESObjectTypeAttribute typeAttribute, MESObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDoubleValue().toString());
        return dto;
    }

    public PrintAttributesDTO getDateValues(MESObjectTypeAttribute typeAttribute, MESObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDateValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeValues(MESObjectTypeAttribute typeAttribute, MESObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimeValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeStampValues(MESObjectTypeAttribute typeAttribute, MESObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimestampValue().toString());

        return dto;
    }

    public PrintAttributesDTO getBooleanValues(MESObjectTypeAttribute typeAttribute, MESObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        String fBool = String.valueOf(attribute.getBooleanValue());
        dto.setName(typeAttribute.getName());
        dto.setValue(fBool);
        return dto;
    }


    public PrintAttributesDTO getCurrencyValues(MESObjectTypeAttribute typeAttribute, MESObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getCurrencyValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTextValues(MESObjectTypeAttribute typeAttribute, MESObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getStringValue());
        return dto;
    }

    public PrintAttributesDTO getLongTextValues(MESObjectTypeAttribute typeAttribute, MESObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getLongTextValue());
        return dto;
    }


    public PrintAttributesDTO getRichTextValues(MESObjectTypeAttribute typeAttribute, MESObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getRichTextValue());
        return dto;
    }

    public PrintAttributesDTO getHyperLinkValues(MESObjectTypeAttribute typeAttribute, MESObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getHyperLinkValue());
        return dto;
    }


    /*
    * Maintenance Object Attributes For Classification
    * */

    @Transactional
    public PrintAttributesDTO getMroObjectAttributes(MROObjectTypeAttribute typeAttribute, MROObjectAttribute attribute) {
        MROObjectTypeAttribute itemTypeAttribute = typeAttribute;
        PrintAttributesDTO dto = new PrintAttributesDTO();

        if (itemTypeAttribute.getDataType().toString().equals("INTEGER")) {
            dto = getIntegerValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("DOUBLE")) {
            dto = getDoubleValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("DATE")) {
            dto = getDateValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIME")) {
            dto = getTimeValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIMESTAMP")) {
            dto = getTimeStampValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("BOOLEAN")) {
            dto = getBooleanValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("CURRENCY")) {
            dto = getCurrencyValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("LIST")) {
            String list = "";
            if (itemTypeAttribute.isListMultiple()) {
                for (String lElement : attribute.getMListValue()) {
                    if (list == "") {
                        list = lElement;
                    } else {
                        list = list + " ," + lElement;
                    }
                }
                dto.setName(itemTypeAttribute.getName());
                dto.setValue(list);

            } else {
                if (!itemTypeAttribute.isListMultiple()) {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(attribute.getListValue().toString());
                }

            }

        }
        if (itemTypeAttribute.getDataType().toString().equals("OBJECT")) {
            if (itemTypeAttribute.getRefType().toString().equals("ITEM")) {
                if (attribute.getRefValue() != null) {
                    PLMItem item = itemRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("ITEMREVISION")) {
                if (attribute.getRefValue() != null) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(attribute.getRefValue());
                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("CHANGE")) {
                if (attribute.getRefValue() != null) {
                    PLMECO fromPlmItem = ecoRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromPlmItem.getEcoNumber().toString());
                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("WORKFLOW")) {

                if (attribute.getRefValue() != null) {
                    PLMWorkflowDefinition fromworkflownewValue = workFlowDefinitionRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromworkflownewValue.getName());
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURER")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(manufacturernewValue.getName());

                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURERPART")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(attribute.getRefValue());
                    dto.setValue(manufacturernewValue.getPartName());
                    dto.setName(itemTypeAttribute.getName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("PROJECT")) {

                if (attribute.getRefValue() != null) {
                    PLMProject personNewValue = projectRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getName());
                } else {

                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");

                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("PERSON")) {
                if (attribute.getRefValue() != null) {
                    Person personNewValue = personRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getFullName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }
            }
        }

        if (itemTypeAttribute.getDataType().toString().equals("TEXT")) {
            dto = getTextValues(itemTypeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("LONGTEXT")) {
            dto = getLongTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("RICHTEXT")) {
            dto = getRichTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("HYPERLINK")) {
            dto = getHyperLinkValues(itemTypeAttribute, attribute);
        }

        return dto;

    }


    public PrintAttributesDTO getIntegerValues(MROObjectTypeAttribute typeAttribute, MROObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getIntegerValue().toString());
        return dto;
    }


    public PrintAttributesDTO getDoubleValues(MROObjectTypeAttribute typeAttribute, MROObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDoubleValue().toString());
        return dto;
    }

    public PrintAttributesDTO getDateValues(MROObjectTypeAttribute typeAttribute, MROObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDateValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeValues(MROObjectTypeAttribute typeAttribute, MROObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimeValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeStampValues(MROObjectTypeAttribute typeAttribute, MROObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimestampValue().toString());

        return dto;
    }

    public PrintAttributesDTO getBooleanValues(MROObjectTypeAttribute typeAttribute, MROObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        String fBool = String.valueOf(attribute.getBooleanValue());
        dto.setName(typeAttribute.getName());
        dto.setValue(fBool);
        return dto;
    }


    public PrintAttributesDTO getCurrencyValues(MROObjectTypeAttribute typeAttribute, MROObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getCurrencyValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTextValues(MROObjectTypeAttribute typeAttribute, MROObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getStringValue());
        return dto;
    }

    public PrintAttributesDTO getLongTextValues(MROObjectTypeAttribute typeAttribute, MROObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getLongTextValue());
        return dto;
    }


    public PrintAttributesDTO getRichTextValues(MROObjectTypeAttribute typeAttribute, MROObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getRichTextValue());
        return dto;
    }

    public PrintAttributesDTO getHyperLinkValues(MROObjectTypeAttribute typeAttribute, MROObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getHyperLinkValue());
        return dto;
    }


    /*
    * Compliance Object Attributes For Classification
    * */

    @Transactional
    public PrintAttributesDTO getPgcObjectAttributes(PGCObjectTypeAttribute typeAttribute, PGCObjectAttribute attribute) {
        PGCObjectTypeAttribute itemTypeAttribute = typeAttribute;
        PrintAttributesDTO dto = new PrintAttributesDTO();

        if (itemTypeAttribute.getDataType().toString().equals("INTEGER")) {
            dto = getIntegerValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("DOUBLE")) {
            dto = getDoubleValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("DATE")) {
            dto = getDateValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIME")) {
            dto = getTimeValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIMESTAMP")) {
            dto = getTimeStampValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("BOOLEAN")) {
            dto = getBooleanValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("CURRENCY")) {
            dto = getCurrencyValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("LIST")) {
            String list = "";
            if (itemTypeAttribute.isListMultiple()) {
                for (String lElement : attribute.getMListValue()) {
                    if (list == "") {
                        list = lElement;
                    } else {
                        list = list + " ," + lElement;
                    }
                }
                dto.setName(itemTypeAttribute.getName());
                dto.setValue(list);

            } else {
                if (!itemTypeAttribute.isListMultiple()) {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(attribute.getListValue().toString());
                }

            }

        }
        if (itemTypeAttribute.getDataType().toString().equals("OBJECT")) {
            if (itemTypeAttribute.getRefType().toString().equals("ITEM")) {
                if (attribute.getRefValue() != null) {
                    PLMItem item = itemRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("ITEMREVISION")) {
                if (attribute.getRefValue() != null) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(attribute.getRefValue());
                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("CHANGE")) {
                if (attribute.getRefValue() != null) {
                    PLMECO fromPlmItem = ecoRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromPlmItem.getEcoNumber().toString());
                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("WORKFLOW")) {

                if (attribute.getRefValue() != null) {
                    PLMWorkflowDefinition fromworkflownewValue = workFlowDefinitionRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromworkflownewValue.getName());
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURER")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(manufacturernewValue.getName());

                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURERPART")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(attribute.getRefValue());
                    dto.setValue(manufacturernewValue.getPartName());
                    dto.setName(itemTypeAttribute.getName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("PROJECT")) {

                if (attribute.getRefValue() != null) {
                    PLMProject personNewValue = projectRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getName());
                } else {

                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");

                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("PERSON")) {
                if (attribute.getRefValue() != null) {
                    Person personNewValue = personRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getFullName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }
            }
        }

        if (itemTypeAttribute.getDataType().toString().equals("TEXT")) {
            dto = getTextValues(itemTypeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("LONGTEXT")) {
            dto = getLongTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("RICHTEXT")) {
            dto = getRichTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("HYPERLINK")) {
            dto = getHyperLinkValues(itemTypeAttribute, attribute);
        }

        return dto;

    }


    public PrintAttributesDTO getIntegerValues(PGCObjectTypeAttribute typeAttribute, PGCObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getIntegerValue().toString());
        return dto;
    }


    public PrintAttributesDTO getDoubleValues(PGCObjectTypeAttribute typeAttribute, PGCObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDoubleValue().toString());
        return dto;
    }

    public PrintAttributesDTO getDateValues(PGCObjectTypeAttribute typeAttribute, PGCObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDateValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeValues(PGCObjectTypeAttribute typeAttribute, PGCObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimeValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeStampValues(PGCObjectTypeAttribute typeAttribute, PGCObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimestampValue().toString());

        return dto;
    }

    public PrintAttributesDTO getBooleanValues(PGCObjectTypeAttribute typeAttribute, PGCObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        String fBool = String.valueOf(attribute.getBooleanValue());
        dto.setName(typeAttribute.getName());
        dto.setValue(fBool);
        return dto;
    }


    public PrintAttributesDTO getCurrencyValues(PGCObjectTypeAttribute typeAttribute, PGCObjectAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getCurrencyValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTextValues(PGCObjectTypeAttribute typeAttribute, PGCObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getStringValue());
        return dto;
    }

    public PrintAttributesDTO getLongTextValues(PGCObjectTypeAttribute typeAttribute, PGCObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getLongTextValue());
        return dto;
    }


    public PrintAttributesDTO getRichTextValues(PGCObjectTypeAttribute typeAttribute, PGCObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getRichTextValue());
        return dto;
    }

    public PrintAttributesDTO getHyperLinkValues(PGCObjectTypeAttribute typeAttribute, PGCObjectAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getHyperLinkValue());
        return dto;
    }


     /*
    * Manufacturer Object Attributes For Classification
    * */

    @Transactional
    public PrintAttributesDTO getManufacturerObjectAttributes(PLMManufacturerTypeAttribute typeAttribute, PLMManufacturerAttribute attribute) {
        PLMManufacturerTypeAttribute itemTypeAttribute = typeAttribute;
        PrintAttributesDTO dto = new PrintAttributesDTO();

        if (itemTypeAttribute.getDataType().toString().equals("INTEGER")) {
            dto = getIntegerValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("DOUBLE")) {
            dto = getDoubleValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("DATE")) {
            dto = getDateValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIME")) {
            dto = getTimeValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIMESTAMP")) {
            dto = getTimeStampValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("BOOLEAN")) {
            dto = getBooleanValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("CURRENCY")) {
            dto = getCurrencyValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("LIST")) {
            String list = "";
            if (itemTypeAttribute.isListMultiple()) {
                for (String lElement : attribute.getMListValue()) {
                    if (list == "") {
                        list = lElement;
                    } else {
                        list = list + " ," + lElement;
                    }
                }
                dto.setName(itemTypeAttribute.getName());
                dto.setValue(list);

            } else {
                if (!itemTypeAttribute.isListMultiple()) {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(attribute.getListValue().toString());
                }

            }

        }
        if (itemTypeAttribute.getDataType().toString().equals("OBJECT")) {
            if (itemTypeAttribute.getRefType().toString().equals("ITEM")) {
                if (attribute.getRefValue() != null) {
                    PLMItem item = itemRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("ITEMREVISION")) {
                if (attribute.getRefValue() != null) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(attribute.getRefValue());
                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("CHANGE")) {
                if (attribute.getRefValue() != null) {
                    PLMECO fromPlmItem = ecoRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromPlmItem.getEcoNumber().toString());
                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("WORKFLOW")) {

                if (attribute.getRefValue() != null) {
                    PLMWorkflowDefinition fromworkflownewValue = workFlowDefinitionRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromworkflownewValue.getName());
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURER")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(manufacturernewValue.getName());

                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURERPART")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(attribute.getRefValue());
                    dto.setValue(manufacturernewValue.getPartName());
                    dto.setName(itemTypeAttribute.getName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("PROJECT")) {

                if (attribute.getRefValue() != null) {
                    PLMProject personNewValue = projectRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getName());
                } else {

                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");

                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("PERSON")) {
                if (attribute.getRefValue() != null) {
                    Person personNewValue = personRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getFullName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }
            }
        }

        if (itemTypeAttribute.getDataType().toString().equals("TEXT")) {
            dto = getTextValues(itemTypeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("LONGTEXT")) {
            dto = getLongTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("RICHTEXT")) {
            dto = getRichTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("HYPERLINK")) {
            dto = getHyperLinkValues(itemTypeAttribute, attribute);
        }

        return dto;

    }


    public PrintAttributesDTO getIntegerValues(PLMManufacturerTypeAttribute typeAttribute, PLMManufacturerAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getIntegerValue().toString());
        return dto;
    }


    public PrintAttributesDTO getDoubleValues(PLMManufacturerTypeAttribute typeAttribute, PLMManufacturerAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDoubleValue().toString());
        return dto;
    }

    public PrintAttributesDTO getDateValues(PLMManufacturerTypeAttribute typeAttribute, PLMManufacturerAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDateValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeValues(PLMManufacturerTypeAttribute typeAttribute, PLMManufacturerAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimeValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeStampValues(PLMManufacturerTypeAttribute typeAttribute, PLMManufacturerAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimestampValue().toString());

        return dto;
    }

    public PrintAttributesDTO getBooleanValues(PLMManufacturerTypeAttribute typeAttribute, PLMManufacturerAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        String fBool = String.valueOf(attribute.getBooleanValue());
        dto.setName(typeAttribute.getName());
        dto.setValue(fBool);
        return dto;
    }


    public PrintAttributesDTO getCurrencyValues(PLMManufacturerTypeAttribute typeAttribute, PLMManufacturerAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getCurrencyValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTextValues(PLMManufacturerTypeAttribute typeAttribute, PLMManufacturerAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getStringValue());
        return dto;
    }

    public PrintAttributesDTO getLongTextValues(PLMManufacturerTypeAttribute typeAttribute, PLMManufacturerAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getLongTextValue());
        return dto;
    }


    public PrintAttributesDTO getRichTextValues(PLMManufacturerTypeAttribute typeAttribute, PLMManufacturerAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getRichTextValue());
        return dto;
    }

    public PrintAttributesDTO getHyperLinkValues(PLMManufacturerTypeAttribute typeAttribute, PLMManufacturerAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getHyperLinkValue());
        return dto;
    }



     /*
    * ManufacturerPart Object Attributes For Classification
    * */

    @Transactional
    public PrintAttributesDTO getManufacturerPartObjectAttributes(PLMManufacturerPartTypeAttribute typeAttribute, PLMManufacturerPartAttribute attribute) {
        PLMManufacturerPartTypeAttribute itemTypeAttribute = typeAttribute;
        PrintAttributesDTO dto = new PrintAttributesDTO();

        if (itemTypeAttribute.getDataType().toString().equals("INTEGER")) {
            dto = getIntegerValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("DOUBLE")) {
            dto = getDoubleValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("DATE")) {
            dto = getDateValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIME")) {
            dto = getTimeValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIMESTAMP")) {
            dto = getTimeStampValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("BOOLEAN")) {
            dto = getBooleanValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("CURRENCY")) {
            dto = getCurrencyValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("LIST")) {
            String list = "";
            if (itemTypeAttribute.isListMultiple()) {
                for (String lElement : attribute.getMListValue()) {
                    if (list == "") {
                        list = lElement;
                    } else {
                        list = list + " ," + lElement;
                    }
                }
                dto.setName(itemTypeAttribute.getName());
                dto.setValue(list);

            } else {
                if (!itemTypeAttribute.isListMultiple()) {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(attribute.getListValue().toString());
                }

            }

        }
        if (itemTypeAttribute.getDataType().toString().equals("OBJECT")) {
            if (itemTypeAttribute.getRefType().toString().equals("ITEM")) {
                if (attribute.getRefValue() != null) {
                    PLMItem item = itemRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("ITEMREVISION")) {
                if (attribute.getRefValue() != null) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(attribute.getRefValue());
                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("CHANGE")) {
                if (attribute.getRefValue() != null) {
                    PLMECO fromPlmItem = ecoRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromPlmItem.getEcoNumber().toString());
                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("WORKFLOW")) {

                if (attribute.getRefValue() != null) {
                    PLMWorkflowDefinition fromworkflownewValue = workFlowDefinitionRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromworkflownewValue.getName());
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURER")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(manufacturernewValue.getName());

                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURERPART")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(attribute.getRefValue());
                    dto.setValue(manufacturernewValue.getPartName());
                    dto.setName(itemTypeAttribute.getName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("PROJECT")) {

                if (attribute.getRefValue() != null) {
                    PLMProject personNewValue = projectRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getName());
                } else {

                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");

                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("PERSON")) {
                if (attribute.getRefValue() != null) {
                    Person personNewValue = personRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getFullName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }
            }
        }

        if (itemTypeAttribute.getDataType().toString().equals("TEXT")) {
            dto = getTextValues(itemTypeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("LONGTEXT")) {
            dto = getLongTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("RICHTEXT")) {
            dto = getRichTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("HYPERLINK")) {
            dto = getHyperLinkValues(itemTypeAttribute, attribute);
        }

        return dto;

    }


    public PrintAttributesDTO getIntegerValues(PLMManufacturerPartTypeAttribute typeAttribute, PLMManufacturerPartAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getIntegerValue().toString());
        return dto;
    }


    public PrintAttributesDTO getDoubleValues(PLMManufacturerPartTypeAttribute typeAttribute, PLMManufacturerPartAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDoubleValue().toString());
        return dto;
    }

    public PrintAttributesDTO getDateValues(PLMManufacturerPartTypeAttribute typeAttribute, PLMManufacturerPartAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDateValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeValues(PLMManufacturerPartTypeAttribute typeAttribute, PLMManufacturerPartAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimeValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeStampValues(PLMManufacturerPartTypeAttribute typeAttribute, PLMManufacturerPartAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimestampValue().toString());

        return dto;
    }

    public PrintAttributesDTO getBooleanValues(PLMManufacturerPartTypeAttribute typeAttribute, PLMManufacturerPartAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        String fBool = String.valueOf(attribute.getBooleanValue());
        dto.setName(typeAttribute.getName());
        dto.setValue(fBool);
        return dto;
    }


    public PrintAttributesDTO getCurrencyValues(PLMManufacturerPartTypeAttribute typeAttribute, PLMManufacturerPartAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getCurrencyValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTextValues(PLMManufacturerPartTypeAttribute typeAttribute, PLMManufacturerPartAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getStringValue());
        return dto;
    }

    public PrintAttributesDTO getLongTextValues(PLMManufacturerPartTypeAttribute typeAttribute, PLMManufacturerPartAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getLongTextValue());
        return dto;
    }


    public PrintAttributesDTO getRichTextValues(PLMManufacturerPartTypeAttribute typeAttribute, PLMManufacturerPartAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getRichTextValue());
        return dto;
    }

    public PrintAttributesDTO getHyperLinkValues(PLMManufacturerPartTypeAttribute typeAttribute, PLMManufacturerPartAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getHyperLinkValue());
        return dto;
    }



      /*
    * Supplier Object Attributes For Classification
    * */

    @Transactional
    public PrintAttributesDTO getSupplierObjectAttributes(PLMSupplierTypeAttribute typeAttribute, PLMSupplierAttribute attribute) {
        PLMSupplierTypeAttribute itemTypeAttribute = typeAttribute;
        PrintAttributesDTO dto = new PrintAttributesDTO();

        if (itemTypeAttribute.getDataType().toString().equals("INTEGER")) {
            dto = getIntegerValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("DOUBLE")) {
            dto = getDoubleValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("DATE")) {
            dto = getDateValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIME")) {
            dto = getTimeValues(typeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIMESTAMP")) {
            dto = getTimeStampValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("BOOLEAN")) {
            dto = getBooleanValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("CURRENCY")) {
            dto = getCurrencyValues(typeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("LIST")) {
            String list = "";
            if (itemTypeAttribute.isListMultiple()) {
                for (String lElement : attribute.getMListValue()) {
                    if (list == "") {
                        list = lElement;
                    } else {
                        list = list + " ," + lElement;
                    }
                }
                dto.setName(itemTypeAttribute.getName());
                dto.setValue(list);

            } else {
                if (!itemTypeAttribute.isListMultiple()) {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(attribute.getListValue().toString());
                }

            }

        }
        if (itemTypeAttribute.getDataType().toString().equals("OBJECT")) {
            if (itemTypeAttribute.getRefType().toString().equals("ITEM")) {
                if (attribute.getRefValue() != null) {
                    PLMItem item = itemRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("ITEMREVISION")) {
                if (attribute.getRefValue() != null) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(attribute.getRefValue());
                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(item.getItemNumber().toString());

                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("CHANGE")) {
                if (attribute.getRefValue() != null) {
                    PLMECO fromPlmItem = ecoRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromPlmItem.getEcoNumber().toString());
                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("WORKFLOW")) {

                if (attribute.getRefValue() != null) {
                    PLMWorkflowDefinition fromworkflownewValue = workFlowDefinitionRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(fromworkflownewValue.getName());
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURER")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(manufacturernewValue.getName());

                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURERPART")) {
                if (attribute.getRefValue() != null) {
                    PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(attribute.getRefValue());
                    dto.setValue(manufacturernewValue.getPartName());
                    dto.setName(itemTypeAttribute.getName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("PROJECT")) {

                if (attribute.getRefValue() != null) {
                    PLMProject personNewValue = projectRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getName());
                } else {

                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");

                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("PERSON")) {
                if (attribute.getRefValue() != null) {
                    Person personNewValue = personRepository.findOne(attribute.getRefValue());
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue(personNewValue.getFullName());
                } else {
                    dto.setName(itemTypeAttribute.getName());
                    dto.setValue("");
                }
            }
        }

        if (itemTypeAttribute.getDataType().toString().equals("TEXT")) {
            dto = getTextValues(itemTypeAttribute, attribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("LONGTEXT")) {
            dto = getLongTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("RICHTEXT")) {
            dto = getRichTextValues(itemTypeAttribute, attribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("HYPERLINK")) {
            dto = getHyperLinkValues(itemTypeAttribute, attribute);
        }

        return dto;

    }


    public PrintAttributesDTO getIntegerValues(PLMSupplierTypeAttribute typeAttribute, PLMSupplierAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getIntegerValue().toString());
        return dto;
    }


    public PrintAttributesDTO getDoubleValues(PLMSupplierTypeAttribute typeAttribute, PLMSupplierAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDoubleValue().toString());
        return dto;
    }

    public PrintAttributesDTO getDateValues(PLMSupplierTypeAttribute typeAttribute, PLMSupplierAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDateValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeValues(PLMSupplierTypeAttribute typeAttribute, PLMSupplierAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimeValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTimeStampValues(PLMSupplierTypeAttribute typeAttribute, PLMSupplierAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimestampValue().toString());

        return dto;
    }

    public PrintAttributesDTO getBooleanValues(PLMSupplierTypeAttribute typeAttribute, PLMSupplierAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        String fBool = String.valueOf(attribute.getBooleanValue());
        dto.setName(typeAttribute.getName());
        dto.setValue(fBool);
        return dto;
    }


    public PrintAttributesDTO getCurrencyValues(PLMSupplierTypeAttribute typeAttribute, PLMSupplierAttribute attribute) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getCurrencyValue().toString());
        return dto;
    }

    public PrintAttributesDTO getTextValues(PLMSupplierTypeAttribute typeAttribute, PLMSupplierAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getStringValue());
        return dto;
    }

    public PrintAttributesDTO getLongTextValues(PLMSupplierTypeAttribute typeAttribute, PLMSupplierAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getLongTextValue());
        return dto;
    }


    public PrintAttributesDTO getRichTextValues(PLMSupplierTypeAttribute typeAttribute, PLMSupplierAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getRichTextValue());
        return dto;
    }

    public PrintAttributesDTO getHyperLinkValues(PLMSupplierTypeAttribute typeAttribute, PLMSupplierAttribute fromItem) {
        PrintAttributesDTO dto = new PrintAttributesDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getHyperLinkValue());
        return dto;
    }


}