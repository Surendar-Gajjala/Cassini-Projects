package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.plm.model.cm.PLMChangeType;
import com.cassinisys.plm.model.cm.PLMChangeTypeAttribute;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartType;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartTypeAttribute;
import com.cassinisys.plm.model.mfr.PLMManufacturerType;
import com.cassinisys.plm.model.mfr.PLMManufacturerTypeAttribute;
import com.cassinisys.plm.model.plm.PLMItemType;
import com.cassinisys.plm.model.plm.PLMItemTypeAttribute;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.rm.RequirementType;
import com.cassinisys.plm.model.rm.RmObjectTypeAttribute;
import com.cassinisys.plm.model.rm.SpecificationType;
import com.cassinisys.plm.model.wf.PLMWorkflowType;
import com.cassinisys.plm.model.wf.PLMWorkflowTypeAttribute;
import com.cassinisys.plm.repo.cm.ChangeTypeAttributeRepository;
import com.cassinisys.plm.repo.cm.ChangeTypeRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartTypeAttributeRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartTypeRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerTypeAttributeRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerTypeRepository;
import com.cassinisys.plm.repo.plm.ItemTypeAttributeRepository;
import com.cassinisys.plm.repo.plm.ItemTypeRepository;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import com.cassinisys.plm.repo.rm.RequirementTypeRepository;
import com.cassinisys.plm.repo.rm.RmObjectTypeAttributeRepository;
import com.cassinisys.plm.repo.rm.SpecificationTypeRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeAttributeRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Component
@Transactional
public class ClassificationImporter extends AbstractImporter {
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private ChangeTypeRepository changeTypeRepository;
    @Autowired
    private ChangeTypeAttributeRepository changeTypeAttributeRepository;
    @Autowired
    private ManufacturerTypeRepository manufacturerTypeRepository;
    @Autowired
    private ManufacturerTypeAttributeRepository manufacturerTypeAttributeRepository;
    @Autowired
    private ManufacturerPartTypeRepository manufacturerPartTypeRepository;
    @Autowired
    private ManufacturerPartTypeAttributeRepository manufacturerPartTypeAttributeRepository;
    @Autowired
    private RequirementTypeRepository requirementTypeRepository;
    @Autowired
    private SpecificationTypeRepository specificationTypeRepository;
    @Autowired
    private RmObjectTypeAttributeRepository rmObjectTypeAttributeRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private WorkflowTypeAttributeRepository workflowTypeAttributeRepository;

    @Override
    protected void importData(byte[] bytes) {
        try {
            ClassificationDTO classification = getObjectMapper().readValue(bytes, ClassificationDTO.class);
            processItemTypes(classification);
            processChangeTypes(classification);
            processManufacturerTypes(classification);
            processManufacturerPartTypes(classification);
            processRequirementTypes(classification);
            processSpecificationTypes(classification);
            processWorkflowTypes(classification);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processItemTypes(ClassificationDTO classification) {
        List<PLMItemType> types = classification.getItemTypes();
        for (PLMItemType type : types) {
            List<PLMItemTypeAttribute> attributes = type.getAttributes();
            if (itemTypeRepository.findByName(type.getName()) == null) {
                type.setId(null);
                PLMItemType parentType = type.getParentTypeReference();
                if (parentType != null) {
                    parentType = itemTypeRepository.findByName(parentType.getName());
                    type.setParentType(parentType.getId());
                }
                AutoNumber autoNumber = type.getItemNumberSource();
                if (autoNumber != null) {
                    type.setItemNumberSource(autoNumberRepository.findByName(autoNumber.getName()));
                }
                Lov lov = type.getRevisionSequence();
                if (lov != null) {
                    type.setRevisionSequence(lovRepository.findByName(lov.getName()));
                }
                PLMLifeCycle lifeCycle = type.getLifecycle();
                if (lifeCycle != null) {
                    type.setLifecycle(lifeCycleRepository.findByName(lifeCycle.getName()));
                }
                type = itemTypeRepository.save(type);
            } else {
                type = itemTypeRepository.findByName(type.getName());
            }
            for (PLMItemTypeAttribute attribute : attributes) {
                if (itemTypeAttributeRepository.findByItemTypeAndName(type.getId(), attribute.getName()) == null) {
                    attribute.setId(null);
                    attribute.setItemType(type.getId());
                    Lov lov = attribute.getLov();
                    if (lov != null) {
                        attribute.setLov(lovRepository.findByName(lov.getName()));
                    }
                    itemTypeAttributeRepository.save(attribute);
                }
            }
        }
    }

    private void processChangeTypes(ClassificationDTO classification) {
        List<PLMChangeType> types = classification.getChangeTypes();
        for (PLMChangeType type : types) {
            List<PLMChangeTypeAttribute> attributes = type.getAttributes();
            if (changeTypeRepository.findByName(type.getName()) == null) {
                type.setId(null);
                PLMChangeType parentType = type.getParentTypeReference();
                if (parentType != null) {
                    parentType = changeTypeRepository.findByName(parentType.getName());
                    type.setParentType(parentType.getId());
                }
                AutoNumber autoNumber = type.getAutoNumberSource();
                if (autoNumber != null) {
                    type.setAutoNumberSource(autoNumberRepository.findByName(autoNumber.getName()));
                }
                type = changeTypeRepository.save(type);
            } else {
                type = changeTypeRepository.findByName(type.getName());
            }
            for (PLMChangeTypeAttribute attribute : attributes) {
                if (changeTypeAttributeRepository.findByChangeTypeAndName(type.getId(), attribute.getName()) == null) {
                    attribute.setId(null);
                    attribute.setChangeType(type.getId());
                    Lov lov = attribute.getLov();
                    if (lov != null) {
                        attribute.setLov(lovRepository.findByName(lov.getName()));
                    }
                    changeTypeAttributeRepository.save(attribute);
                }
            }
        }
    }

    private void processManufacturerTypes(ClassificationDTO classification) {
        List<PLMManufacturerType> types = classification.getManufacturerTypes();
        for (PLMManufacturerType type : types) {
            List<PLMManufacturerTypeAttribute> attributes = type.getAttributes();
            if (manufacturerTypeRepository.findByName(type.getName()) == null) {
                type.setId(null);
                PLMManufacturerType parentType = type.getParentTypeReference();
                if (parentType != null) {
                    parentType = manufacturerTypeRepository.findByName(parentType.getName());
                    type.setParentType(parentType.getId());
                }
                type = manufacturerTypeRepository.save(type);
            } else {
                type = manufacturerTypeRepository.findByName(type.getName());
            }
            for (PLMManufacturerTypeAttribute attribute : attributes) {
                if (manufacturerTypeAttributeRepository.findByMfrTypeAndName(type.getId(), attribute.getName()) == null) {
                    attribute.setId(null);
                    attribute.setMfrType(type.getId());
                    Lov lov = attribute.getLov();
                    if (lov != null) {
                        attribute.setLov(lovRepository.findByName(lov.getName()));
                    }
                    manufacturerTypeAttributeRepository.save(attribute);
                }
            }
        }
    }

    private void processManufacturerPartTypes(ClassificationDTO classification) {
        List<PLMManufacturerPartType> types = classification.getManufacturerPartTypes();
        for (PLMManufacturerPartType type : types) {
            List<PLMManufacturerPartTypeAttribute> attributes = type.getAttributes();
            if (manufacturerPartTypeRepository.findByName(type.getName()) == null) {
                type.setId(null);
                PLMManufacturerPartType parentType = type.getParentTypeReference();
                if (parentType != null) {
                    parentType = manufacturerPartTypeRepository.findByName(parentType.getName());
                    type.setParentType(parentType.getId());
                }
                type = manufacturerPartTypeRepository.save(type);
            } else {
                type = manufacturerPartTypeRepository.findByName(type.getName());
            }
            for (PLMManufacturerPartTypeAttribute attribute : attributes) {
                if (manufacturerPartTypeAttributeRepository.findByMfrPartTypeAndName(type.getId(), attribute.getName()) == null) {
                    attribute.setId(null);
                    attribute.setMfrPartType(type.getId());
                    Lov lov = attribute.getLov();
                    if (lov != null) {
                        attribute.setLov(lovRepository.findByName(lov.getName()));
                    }
                    manufacturerPartTypeAttributeRepository.save(attribute);
                }
            }
        }
    }

    private void processRequirementTypes(ClassificationDTO classification) {
        List<RequirementType> types = classification.getRequirementTypes();
        for (RequirementType type : types) {
            List<RmObjectTypeAttribute> attributes = type.getAttributes();
            if (requirementTypeRepository.findByName(type.getName()) == null) {
                type.setId(null);
                RequirementType parentType = type.getParentTypeReference();
                if (parentType != null) {
                    parentType = requirementTypeRepository.findByName(parentType.getName());
                    type.setParentType(parentType.getId());
                }
                AutoNumber autoNumber = type.getNumberSource();
                if (autoNumber != null) {
                    type.setNumberSource(autoNumberRepository.findByName(autoNumber.getName()));
                }
                Lov lov = type.getRevisionSequence();
                if (lov != null) {
                    type.setRevisionSequence(lovRepository.findByName(lov.getName()));
                }
                PLMLifeCycle lifeCycle = type.getLifecycle();
                if (lifeCycle != null) {
                    type.setLifecycle(lifeCycleRepository.findByName(lifeCycle.getName()));
                }
                type = requirementTypeRepository.save(type);
            } else {
                type = requirementTypeRepository.findByName(type.getName());
            }
            for (RmObjectTypeAttribute attribute : attributes) {
                if (rmObjectTypeAttributeRepository.findByRmObjectTypeAndName(type.getId(), attribute.getName()) == null) {
                    attribute.setId(null);
                    attribute.setRmObjectType(type.getId());
                    Lov lov = attribute.getLov();
                    if (lov != null) {
                        attribute.setLov(lovRepository.findByName(lov.getName()));
                    }
                    rmObjectTypeAttributeRepository.save(attribute);
                }
            }
        }
    }

    private void processSpecificationTypes(ClassificationDTO classification) {
        List<SpecificationType> types = classification.getSpecificationTypes();
        for (SpecificationType type : types) {
            List<RmObjectTypeAttribute> attributes = type.getAttributes();
            if (specificationTypeRepository.findByName(type.getName()) == null) {
                type.setId(null);
                SpecificationType parentType = type.getParentTypeReference();
                if (parentType != null) {
                    parentType = specificationTypeRepository.findByName(parentType.getName());
                    type.setParentType(parentType.getId());
                }
                AutoNumber autoNumber = type.getNumberSource();
                if (autoNumber != null) {
                    type.setNumberSource(autoNumberRepository.findByName(autoNumber.getName()));
                }
                Lov lov = type.getRevisionSequence();
                if (lov != null) {
                    type.setRevisionSequence(lovRepository.findByName(lov.getName()));
                }
                PLMLifeCycle lifeCycle = type.getLifecycle();
                if (lifeCycle != null) {
                    type.setLifecycle(lifeCycleRepository.findByName(lifeCycle.getName()));
                }
                type = specificationTypeRepository.save(type);
            } else {
                type = specificationTypeRepository.findByName(type.getName());
            }
            for (RmObjectTypeAttribute attribute : attributes) {
                if (rmObjectTypeAttributeRepository.findByRmObjectTypeAndName(type.getId(), attribute.getName()) == null) {
                    attribute.setId(null);
                    attribute.setRmObjectType(type.getId());
                    Lov lov = attribute.getLov();
                    if (lov != null) {
                        attribute.setLov(lovRepository.findByName(lov.getName()));
                    }
                    rmObjectTypeAttributeRepository.save(attribute);
                }
            }
        }
    }

    private void processWorkflowTypes(ClassificationDTO classification) {
        List<PLMWorkflowType> types = classification.getWorkflowTypes();
        for (PLMWorkflowType type : types) {
            List<PLMWorkflowTypeAttribute> attributes = type.getAttributes();
            if (workflowTypeRepository.findByName(type.getName()) == null) {
                type.setId(null);
                PLMWorkflowType parentType = type.getParentTypeReference();
                if (parentType != null) {
                    parentType = workflowTypeRepository.findByName(parentType.getName());
                    type.setParentType(parentType.getId());
                }
                type = workflowTypeRepository.save(type);
            } else {
                type = workflowTypeRepository.findByName(type.getName());
            }
            for (PLMWorkflowTypeAttribute attribute : attributes) {
                if (workflowTypeAttributeRepository.findByWorkflowTypeAndName(type.getId(), attribute.getName()) == null) {
                    attribute.setId(null);
                    attribute.setWorkflowType(type.getId());
                    Lov lov = attribute.getLov();
                    if (lov != null) {
                        attribute.setLov(lovRepository.findByName(lov.getName()));
                    }
                    workflowTypeAttributeRepository.save(attribute);
                }
            }
        }
    }
}
