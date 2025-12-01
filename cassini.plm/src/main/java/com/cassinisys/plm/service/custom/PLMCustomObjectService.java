package com.cassinisys.plm.service.custom;

import com.cassinisys.platform.events.CustomObjectEvents;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.model.custom.CustomObjectType;
import com.cassinisys.platform.model.custom.CustomObjectTypeAttribute;
import com.cassinisys.platform.repo.custom.*;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.PLMWorkflowType;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import com.cassinisys.plm.service.plm.ItemTypeService;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GSR on 20-07-2021.
 */
@Service
public class PLMCustomObjectService {

    @Autowired
    private CustomObjectTypeRepository customObjectTypeRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private CustomObjectRepository customObjectRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private PLMWorkflowService plmWorkflowService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private CustomObjectBomRepository customObjectBomRepository;
    @Autowired
    private CustomObjectFileRepository customObjectFileRepository;
    @Autowired
    private CustomObjectRelatedRepository customObjectRelatedRepository;
    @Autowired
    private CustomObjectTypeAttributeRepository customObjectTypeAttributeRepository;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getCustomObjectHierarchyWorkflows(Integer typeId, String type) {
        List<PLMWorkflowDefinition> workflowDefinitions = new ArrayList<>();
        CustomObjectType customObjectType = customObjectTypeRepository.findOne(typeId);
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
        List<PLMWorkflowDefinition> workflowDefinition1 = workflowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
        if (workflowDefinition1.size() > 0) {
            workflowDefinition1.forEach(workflowDefinition -> {
                if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                    workflowDefinitions.add(workflowDefinition);
                }
            });
        }
        if (customObjectType.getParentType() != null) {
            getWorkflowsFromHierarchy(workflowDefinitions, customObjectType.getParentType(), type);
        }
        return workflowDefinitions;
    }

    private void getWorkflowsFromHierarchy(List<PLMWorkflowDefinition> definitions, Integer typeId, String type) {
        CustomObjectType customObjectType = customObjectTypeRepository.findOne(typeId);
        if (customObjectType != null) {
            PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
            if (workflowType != null) {
                List<PLMWorkflowDefinition> workflowDefinition2 = workflowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
                if (workflowDefinition2.size() > 0) {
                    workflowDefinition2.forEach(workflowDefinition -> {
                        if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                            definitions.add(workflowDefinition);
                        }
                    });
                }
            }
            if (customObjectType.getParentType() != null) {
                getWorkflowsFromHierarchy(definitions, customObjectType.getParentType(), type);
            }
        }
    }

    @Transactional
    public PLMWorkflow attachCustomObjectWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        CustomObject customObject = customObjectRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        PLMWorkflow workflow1 = plmWorkflowRepository.findByAttachedTo(id);
        if (workflow1 != null) {
            plmWorkflowService.deleteWorkflow(id);
        }
        if (customObject != null && wfDef != null) {
            workflow = plmWorkflowService.attachWorkflow(ObjectType.CUSTOMOBJECT, customObject.getId(), wfDef);
            customObject.setWorkflow(workflow.getId());
            customObject = customObjectRepository.save(customObject);
            //applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectWorkflowChangeEvent(customObject, workflow1, workflow));

        }
        return workflow;
    }


    @Transactional(readOnly = true)
    public List<CustomObjectTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        List<CustomObjectTypeAttribute> customObjectTypeAttributes = new ArrayList<>();
        if (!hierarchy) {
            customObjectTypeAttributes = customObjectTypeAttributeRepository.findByCustomObjectTypeOrderBySeqNo(typeId);
            customObjectTypeAttributes.forEach(customObjectTypeAttribute -> {
                if (customObjectTypeAttribute.getRefSubType() != null) {
                    customObjectTypeAttribute.setRefSubTypeName(itemTypeService.getRefSubType(customObjectTypeAttribute.getRefType().name(), customObjectTypeAttribute.getRefSubType()));
                }
            });
        } else {
            customObjectTypeAttributes = getAttributesFromHierarchy(typeId);
        }
        return customObjectTypeAttributes;
    }

    private List<CustomObjectTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<CustomObjectTypeAttribute> atts = customObjectTypeAttributeRepository.findByCustomObjectTypeOrderBySeqNo(typeId);
        List<CustomObjectTypeAttribute> collector = new ArrayList<>(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<CustomObjectTypeAttribute> collector, Integer typeId) {
        CustomObjectType itemType = customObjectTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<CustomObjectTypeAttribute> atts = customObjectTypeAttributeRepository.findByCustomObjectTypeOrderBySeqNo(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }


    @EventListener(condition = "#event.attachedToType.name() == 'CUSTOMOBJECT'")
    public void customObjectWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        CustomObject customObject = (CustomObject) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectWorkflowStartedEvent(customObject, plmWorkflow.getId(), plmWorkflow.getName()));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CUSTOMOBJECT'")
    public void customObjectWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        CustomObject customObject = (CustomObject) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectWorkflowPromotedEvent
                (customObject, plmWorkflow.getId(), plmWorkflow.getName(), fromStatus.getId(), fromStatus.getName(), toStatus.getId(), toStatus.getName()));

        if (fromStatus.getName().equals("RC2A Submission")) {
            applicationEventPublisher.publishEvent(new WorkflowEvents.PromotePluginEvent(fromStatus.getName(), customObject.getId(), fromStatus));
        }
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CUSTOMOBJECT'")
    public void customObjectWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        CustomObject customObject = (CustomObject) event.getAttachedToObject();
        PLMWorkflowStatus toStatus = event.getToStatus();
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectWorkflowDemotedEvent
                (customObject, plmWorkflow.getId(), plmWorkflow.getName(), fromStatus.getId(), fromStatus.getName(), toStatus.getId(), toStatus.getName()));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CUSTOMOBJECT'")
    public void customObjectWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        CustomObject customObject = (CustomObject) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectWorkflowFinishedEvent(customObject, plmWorkflow.getId(), plmWorkflow.getName()));
        applicationEventPublisher.publishEvent(new WorkflowEvents.PromotePluginEvent("finish", customObject.getId(), fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CUSTOMOBJECT'")
    public void customObjectWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        CustomObject customObject = (CustomObject) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectWorkflowHoldEvent(customObject, plmWorkflow.getId(), plmWorkflow.getName(), fromStatus.getId(), fromStatus.getName()));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CUSTOMOBJECT'")
    public void customObjectWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        CustomObject customObject = (CustomObject) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectWorkflowUnholdEvent(customObject, plmWorkflow.getId(), plmWorkflow.getName(), fromStatus.getId(), fromStatus.getName()));
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getCustomObjectTabCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setBom(customObjectBomRepository.getCustomObjectBomCountByParent(id));
        detailsDto.setItemFiles(customObjectFileRepository.getByObjectAndFileTypeAndLatestTrue(id, "FILE"));
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        detailsDto.setRelatedItems(customObjectRelatedRepository.getRelatedCustomObjectCountByParent(id));
        detailsDto.setWhereUsedItems(customObjectBomRepository.getObjectsCountByParent(id));
        return detailsDto;
    }

    @Transactional(readOnly = true)
    public CustomObject getCustomObjectWorkflowStatus(Integer id) {
        CustomObject customObject = customObjectRepository.findOne(id);
        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto = plmWorkflowService.setWorkflowStatusSettings(customObject.getId());
        customObject.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        customObject.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        customObject.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        return customObject;
    }

}
