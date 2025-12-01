package com.cassinisys.plm.model.dto.print;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.plm.model.cm.PLMECO;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pqm.PQMQualityTypeAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.repo.cm.ECORepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pm.ProjectRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
public class QualityAttributeDTO {
    @Autowired
    private ItemRepository itemRepository;
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
    public QualityAttributeDTO getQualityAttributes(PQMQualityTypeAttribute typeAttribute, ObjectAttribute attribute) {
        PQMQualityTypeAttribute itemTypeAttribute = typeAttribute;
        QualityAttributeDTO dto = new QualityAttributeDTO();

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


    public QualityAttributeDTO getIntegerValues(PQMQualityTypeAttribute typeAttribute, ObjectAttribute attribute) {
        QualityAttributeDTO dto = new QualityAttributeDTO();
        dto.setName(typeAttribute.getName());
        if (attribute != null) dto.setValue(attribute.getIntegerValue().toString());
        else dto.setValue(null);
        return dto;
    }


    public QualityAttributeDTO getDoubleValues(PQMQualityTypeAttribute typeAttribute, ObjectAttribute attribute) {
        QualityAttributeDTO dto = new QualityAttributeDTO();
        dto.setName(typeAttribute.getName());
        if (attribute != null) dto.setValue(attribute.getDoubleValue().toString());
        else dto.setValue(null);
        return dto;
    }

    public QualityAttributeDTO getDateValues(PQMQualityTypeAttribute typeAttribute, ObjectAttribute attribute) {
        QualityAttributeDTO dto = new QualityAttributeDTO();
        dto.setName(typeAttribute.getName());
        if (attribute != null) dto.setValue(attribute.getDateValue().toString());
        else dto.setValue(null);
        return dto;
    }

    public QualityAttributeDTO getTimeValues(PQMQualityTypeAttribute typeAttribute, ObjectAttribute attribute) {
        QualityAttributeDTO dto = new QualityAttributeDTO();
        dto.setName(typeAttribute.getName());
        if (attribute != null) dto.setValue(attribute.getTimeValue().toString());
        else dto.setValue(null);
        return dto;
    }

    public QualityAttributeDTO getTimeStampValues(PQMQualityTypeAttribute typeAttribute, ObjectAttribute attribute) {
        QualityAttributeDTO dto = new QualityAttributeDTO();
        dto.setName(typeAttribute.getName());
        if (attribute != null) dto.setValue(attribute.getTimestampValue().toString());
        else dto.setValue(null);
        return dto;
    }

    public QualityAttributeDTO getBooleanValues(PQMQualityTypeAttribute typeAttribute, ObjectAttribute attribute) {
        QualityAttributeDTO dto = new QualityAttributeDTO();
        String fBool = String.valueOf(attribute.getBooleanValue());
        dto.setName(typeAttribute.getName());
        if (attribute != null) dto.setValue(fBool);
        else dto.setValue(null);
        return dto;
    }


    public QualityAttributeDTO getCurrencyValues(PQMQualityTypeAttribute typeAttribute, ObjectAttribute attribute) {
        QualityAttributeDTO dto = new QualityAttributeDTO();
        dto.setName(typeAttribute.getName());
        if (attribute != null) dto.setValue(attribute.getCurrencyValue().toString());
        else dto.setValue(null);
        return dto;
    }

    public QualityAttributeDTO getTextValues(PQMQualityTypeAttribute typeAttribute, ObjectAttribute fromItem) {
        QualityAttributeDTO dto = new QualityAttributeDTO();
        dto.setName(typeAttribute.getName());
        if (fromItem != null) dto.setValue(fromItem.getStringValue());
        else dto.setValue(null);
        return dto;
    }

    public QualityAttributeDTO getLongTextValues(PQMQualityTypeAttribute typeAttribute, ObjectAttribute fromItem) {
        QualityAttributeDTO dto = new QualityAttributeDTO();
        dto.setName(typeAttribute.getName());
        if (fromItem != null) dto.setValue(fromItem.getLongTextValue());
        else dto.setValue(null);
        return dto;
    }


    public QualityAttributeDTO getRichTextValues(PQMQualityTypeAttribute typeAttribute, ObjectAttribute fromItem) {
        QualityAttributeDTO dto = new QualityAttributeDTO();
        dto.setName(typeAttribute.getName());
        if (fromItem != null) dto.setValue(fromItem.getRichTextValue());
        else dto.setValue(null);
        return dto;
    }

    public QualityAttributeDTO getHyperLinkValues(PQMQualityTypeAttribute typeAttribute, ObjectAttribute fromItem) {
        QualityAttributeDTO dto = new QualityAttributeDTO();
        dto.setName(typeAttribute.getName());
        if (fromItem != null) dto.setValue(fromItem.getHyperLinkValue());
        else dto.setValue(null);
        return dto;
    }

}