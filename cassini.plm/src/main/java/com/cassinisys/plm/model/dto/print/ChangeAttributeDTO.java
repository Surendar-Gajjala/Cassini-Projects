package com.cassinisys.plm.model.dto.print;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.plm.model.cm.PLMChangeAttribute;
import com.cassinisys.plm.model.cm.PLMChangeTypeAttribute;
import com.cassinisys.plm.model.cm.PLMECO;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.repo.cm.ChangeAttributeRepository;
import com.cassinisys.plm.repo.cm.ChangeTypeAttributeRepository;
import com.cassinisys.plm.repo.cm.ECORepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pm.ProjectRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
public class ChangeAttributeDTO {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ChangeTypeAttributeRepository changeTypeAttributeRepository;
    @Autowired
    private ChangeAttributeRepository changeAttributeRepository;
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
    public ChangeAttributeDTO getChangeAttributes(PLMChangeTypeAttribute typeAttribute, PLMChangeAttribute attribute) {
        PLMChangeTypeAttribute itemTypeAttribute = typeAttribute;
        ChangeAttributeDTO dto = new ChangeAttributeDTO();

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


    public ChangeAttributeDTO getIntegerValues(PLMChangeTypeAttribute typeAttribute, PLMChangeAttribute attribute) {
        ChangeAttributeDTO dto = new ChangeAttributeDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getIntegerValue().toString());
        return dto;
    }


    public ChangeAttributeDTO getDoubleValues(PLMChangeTypeAttribute typeAttribute, PLMChangeAttribute attribute) {
        ChangeAttributeDTO dto = new ChangeAttributeDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDoubleValue().toString());
        return dto;
    }

    public ChangeAttributeDTO getDateValues(PLMChangeTypeAttribute typeAttribute, PLMChangeAttribute attribute) {
        ChangeAttributeDTO dto = new ChangeAttributeDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getDateValue().toString());
        return dto;
    }

    public ChangeAttributeDTO getTimeValues(PLMChangeTypeAttribute typeAttribute, PLMChangeAttribute attribute) {
        ChangeAttributeDTO dto = new ChangeAttributeDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimeValue().toString());
        return dto;
    }

    public ChangeAttributeDTO getTimeStampValues(PLMChangeTypeAttribute typeAttribute, PLMChangeAttribute attribute) {
        ChangeAttributeDTO dto = new ChangeAttributeDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getTimestampValue().toString());

        return dto;
    }

    public ChangeAttributeDTO getBooleanValues(PLMChangeTypeAttribute typeAttribute, PLMChangeAttribute attribute) {
        ChangeAttributeDTO dto = new ChangeAttributeDTO();
        String fBool = String.valueOf(attribute.getBooleanValue());
        dto.setName(typeAttribute.getName());
        dto.setValue(fBool);
        return dto;
    }


    public ChangeAttributeDTO getCurrencyValues(PLMChangeTypeAttribute typeAttribute, PLMChangeAttribute attribute) {
        ChangeAttributeDTO dto = new ChangeAttributeDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(attribute.getCurrencyValue().toString());
        return dto;
    }

    public ChangeAttributeDTO getTextValues(PLMChangeTypeAttribute typeAttribute, PLMChangeAttribute fromItem) {
        ChangeAttributeDTO dto = new ChangeAttributeDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getStringValue());
        return dto;
    }

    public ChangeAttributeDTO getLongTextValues(PLMChangeTypeAttribute typeAttribute, PLMChangeAttribute fromItem) {
        ChangeAttributeDTO dto = new ChangeAttributeDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getLongTextValue());
        return dto;
    }


    public ChangeAttributeDTO getRichTextValues(PLMChangeTypeAttribute typeAttribute, PLMChangeAttribute fromItem) {
        ChangeAttributeDTO dto = new ChangeAttributeDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getRichTextValue());
        return dto;
    }

    public ChangeAttributeDTO getHyperLinkValues(PLMChangeTypeAttribute typeAttribute, PLMChangeAttribute fromItem) {
        ChangeAttributeDTO dto = new ChangeAttributeDTO();
        dto.setName(typeAttribute.getName());
        dto.setValue(fromItem.getHyperLinkValue());
        return dto;
    }

}