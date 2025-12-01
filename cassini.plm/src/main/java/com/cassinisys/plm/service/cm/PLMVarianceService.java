package com.cassinisys.plm.service.cm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.common.PersonService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.plm.event.VarianceEvents;
import com.cassinisys.plm.filtering.VarianceCriteria;
import com.cassinisys.plm.filtering.VariancePredicateBuilder;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.cm.dto.VarianceAffectedItemDto;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.dto.VarianceDto;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mobile.VarianceDetails;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class PLMVarianceService implements CrudService<PLMVariance, Integer> {

    @Autowired
    private VarianceRepository varianceRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private ChangeFileRepository fileRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private VarianceAffectedItemRepository varianceAffectedItemRepository;
    @Autowired
    private VarianceAffectedObjectRepository varianceAffectedObjectRepository;
    @Autowired
    private VarianceAffectedMaterialRepository varianceAffectedMaterialRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workFlowService;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private ChangeAttributeRepository changeAttributeRepository;
    @Autowired
    private ECOService ecoService;
    @Autowired
    private VariancePredicateBuilder variancePredicateBuilder;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ChangeRelatedItemRepository changeRelatedItemRepository;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;

    @Override
    @PreAuthorize("hasPermission(#plmVariance,'create')")
    public PLMVariance create(PLMVariance plmVariance) {
        autoNumberService.saveNextNumber(plmVariance.getChangeClass().getAutoNumberSource().getId(), plmVariance.getVarianceNumber());
        PLMVariance variance = varianceRepository.save(plmVariance);
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceCreatedEvent(variance));
        return variance;
    }

    public PLMWorkflow attachWorkflow(Integer varianceId, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMVariance variance = varianceRepository.findOne(varianceId);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (variance != null && wfDef != null) {
            workflow = workFlowService.attachWorkflow(PLMObjectType.CHANGE, varianceId, wfDef);
            variance.setWorkflow(workflow.getId());
            variance = varianceRepository.save(variance);
            applicationEventPublisher.publishEvent(new VarianceEvents.VarianceWorkflowChangeEvent(variance, null, workflow));
        }
        return workflow;
    }


    @Override
    @PreAuthorize("hasPermission(#plmVariance.id,'edit')")
    public PLMVariance update(PLMVariance plmVariance) {
        checkNotNull(plmVariance);
        PLMVariance oldVariance = varianceRepository.findOne(plmVariance.getId());
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceBasicInfoUpdatedEvent(oldVariance, plmVariance));
        return varianceRepository.save(plmVariance);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
        varianceRepository.delete(id);
    }

    @Override
    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMVariance get(Integer id) {
        PLMVariance variance = varianceRepository.findOne(id);

         //Adding workflow relavent settings
         WorkflowStatusDTO workFlowStatusDto=workFlowService.setWorkflowStatusSettings(variance.getId());
         variance.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
         variance.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
         variance.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());

        return variance;
    }

    @Transactional
    public VarianceDetails getVarianceDetails(Integer id) {
        PLMVariance variance = varianceRepository.findOne(id);
        VarianceDetails varianceDetails = new VarianceDetails();
        varianceDetails.setId(variance.getId());
        varianceDetails.setTitle(variance.getTitle());
        varianceDetails.setVarianceNumber(variance.getVarianceNumber());
        varianceDetails.setVarianceFor(variance.getVarianceFor());
        varianceDetails.setDescription(variance.getDescription());
        varianceDetails.setReasonForVariance(variance.getReasonForVariance());
        varianceDetails.setCurrentRequirement(variance.getCurrentRequirement());
        varianceDetails.setRequirementDeviation(variance.getRequirementDeviation());
        varianceDetails.setStatus(variance.getStatus());
        varianceDetails.setOriginator(personRepository.findOne(variance.getOriginator()).getFullName());
        varianceDetails.setCreatedBy(personRepository.findOne(variance.getCreatedBy()).getFullName());
        varianceDetails.setModifiedBy(personRepository.findOne(variance.getModifiedBy()).getFullName());
        varianceDetails.setModifiedDate(variance.getModifiedDate());
        varianceDetails.setCreatedDate(variance.getCreatedDate());
        varianceDetails.setWorkflow(variance.getWorkflow());

        return varianceDetails;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMVariance> getAll() {
        return varianceRepository.findAll();
    }

    @Transactional
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<VarianceDto> getVarianceByType(Pageable pageable, VarianceCriteria varianceCriteria) {
        Predicate predicate = variancePredicateBuilder.build(varianceCriteria, QPLMVariance.pLMVariance);
        Page<PLMVariance> variances = varianceRepository.findAll(predicate, pageable);
        List<VarianceDto> varianceDtoList = new ArrayList<>();
        variances.forEach(variance -> {
            VarianceDto varianceDto = new VarianceDto();
            varianceDto.setId(variance.getId());
            varianceDto.setVarianceNumber(variance.getVarianceNumber());
            varianceDto.setTitle(variance.getTitle());
            varianceDto.setDescription(variance.getDescription());
            varianceDto.setEffecitivityType(variance.getEffectivityType());
            varianceDto.setReasonForVariance(variance.getReasonForVariance());
            varianceDto.setVarianceFor(variance.getVarianceFor().toString());
            varianceDto.setCurrentRequirement(variance.getCurrentRequirement());
            varianceDto.setRequirementDeviation(variance.getRequirementDeviation());
            varianceDto.setStatus(variance.getStatus());
            varianceDto.setObjectType(variance.getObjectType().name());
            varianceDto.setSubType(variance.getChangeClass().getName());
            varianceDto.setRecurring(variance.getIsRecurring());
            varianceDto.setOriginator(personService.get(variance.getOriginator()));
            varianceDto.setCreatedBy(personService.get(variance.getCreatedBy()));
            varianceDto.setCreatedDate(variance.getCreatedDate());
            varianceDto.setStatusType(variance.getStatusType().toString());

            WorkflowStatusDTO workFlowStatusDto=workFlowService.setWorkflowStatusSettings(variance.getId());
            varianceDto.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            varianceDto.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            varianceDto.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
            varianceDto.setOnHold(workFlowStatusDto.getOnHold());
            varianceDto.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            varianceDto.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());

            varianceDto.setTagsCount(variance.getTags().size());
            varianceDtoList.add(varianceDto);
        });
        return new PageImpl<VarianceDto>(varianceDtoList, pageable, variances.getTotalElements());
    }

    public Integer getByNumber(String id) {
        checkNotNull(id);
        List<PLMVariance> plmVariances = varianceRepository.findByVarianceNumber(id);
        return plmVariances.size();
    }


    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public List<PLMVariance> findMultiple(List<Integer> ids) {
        return varianceRepository.findByIdIn(ids);
    }

    @Transactional
    public List<VarianceAffectedItemDto> getAffectedItems(Integer varianceID) {
        List<VarianceAffectedItemDto> varianceAffectedItemDtoList = new ArrayList<>();
        List<PLMVarianceAffectedItem> varianceAffectedItems = varianceAffectedItemRepository.findByVarianceOrderByModifiedDateDesc(varianceID);
        varianceAffectedItems.forEach(plmVarianceAffectedItem -> {
            VarianceAffectedItemDto varianceAffectedItemDto = new VarianceAffectedItemDto();
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmVarianceAffectedItem.getItem());
            PLMItem plmItem = itemRepository.findById(itemRevision.getItemMaster());
            varianceAffectedItemDto.setId(plmVarianceAffectedItem.getId());
            varianceAffectedItemDto.setItemName(plmItem.getItemName());
            varianceAffectedItemDto.setRecurring(plmVarianceAffectedItem.getIsRecurring());
            varianceAffectedItemDto.setItemId(plmVarianceAffectedItem.getItem());
            varianceAffectedItemDto.setItemNumber(plmItem.getItemNumber());
            varianceAffectedItemDto.setItemType(plmItem.getItemType().getName());
            varianceAffectedItemDto.setNotes(plmVarianceAffectedItem.getNotes());
            varianceAffectedItemDto.setQuantity(plmVarianceAffectedItem.getQuantity());
            varianceAffectedItemDto.setSerialsOrLots(plmVarianceAffectedItem.getSerialsOrLots());
            varianceAffectedItemDtoList.add(varianceAffectedItemDto);
        });
        return varianceAffectedItemDtoList;
    }

    @Transactional
    public List<VarianceAffectedItemDto> getAffectedParts(Integer varianceID) {
        List<VarianceAffectedItemDto> varianceAffectedItemDtoList = new ArrayList<>();
        List<PLMVarianceAffectedMaterial> varianceAffectedItems = varianceAffectedMaterialRepository.findByVarianceOrderByModifiedDateDesc(varianceID);
        varianceAffectedItems.forEach(plmVarianceAffectedItem -> {
            VarianceAffectedItemDto varianceAffectedItemDto = new VarianceAffectedItemDto();
            PLMManufacturerPart plmManufacturerPart = manufacturerPartRepository.findOne(plmVarianceAffectedItem.getMaterial());
            varianceAffectedItemDto.setId(plmVarianceAffectedItem.getId());
            varianceAffectedItemDto.setItemName(plmManufacturerPart.getPartName());
            varianceAffectedItemDto.setRecurring(plmVarianceAffectedItem.getIsRecurring());
            varianceAffectedItemDto.setItemId(plmVarianceAffectedItem.getMaterial());
            varianceAffectedItemDto.setItemNumber(plmManufacturerPart.getPartNumber());
            varianceAffectedItemDto.setItemType(plmManufacturerPart.getMfrPartType().getName());
            varianceAffectedItemDto.setNotes(plmVarianceAffectedItem.getNotes());
            varianceAffectedItemDto.setQuantity(plmVarianceAffectedItem.getQuantity());
            varianceAffectedItemDto.setSerialsOrLots(plmVarianceAffectedItem.getSerialsOrLots());
            varianceAffectedItemDtoList.add(varianceAffectedItemDto);
        });
        return varianceAffectedItemDtoList;
    }

    public PLMVarianceAffectedItem createVarianceAffectedItem(Integer id, PLMVarianceAffectedItem varianceAffectedItem) {
        varianceAffectedItem.setId(null);
        List<PLMVarianceAffectedItem> varianceAffectedItems = new ArrayList<>();
        PLMVariance variance = varianceRepository.findOne(id);
        varianceAffectedItem = varianceAffectedItemRepository.save(varianceAffectedItem);
        varianceAffectedItems.add(varianceAffectedItem);
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceAffectedItemAddedEvent(variance, varianceAffectedItems));
        return varianceAffectedItem;
    }

    public PLMVarianceAffectedMaterial createVarianceAffectedPart(Integer id, PLMVarianceAffectedMaterial varianceAffectedPart) {
        varianceAffectedPart.setId(null);
        List<PLMVarianceAffectedMaterial> varianceAffectedMaterials = new ArrayList<>();
        varianceAffectedPart = varianceAffectedMaterialRepository.save(varianceAffectedPart);
        PLMVariance variance = varianceRepository.findOne(id);
        varianceAffectedMaterials.add(varianceAffectedPart);
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceAffectedPartAddedEvent(variance, varianceAffectedMaterials));
        return varianceAffectedPart;
    }

    public List<PLMVarianceAffectedItem> createAllVarianceAffectedItems(Integer id, List<PLMVarianceAffectedItem> varianceAffectedItems) {
        varianceAffectedItems = varianceAffectedItemRepository.save(varianceAffectedItems);
        PLMVariance variance = varianceRepository.findOne(id);
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceAffectedItemAddedEvent(variance, varianceAffectedItems));
        return varianceAffectedItems;
    }

    public List<PLMVarianceAffectedMaterial> createAllVarianceAffectedParts(Integer id, List<PLMVarianceAffectedMaterial> varianceAffectedMaterials) {
        varianceAffectedMaterials = varianceAffectedMaterialRepository.save(varianceAffectedMaterials);
        PLMVariance variance = varianceRepository.findOne(id);
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceAffectedPartAddedEvent(variance, varianceAffectedMaterials));
        return varianceAffectedMaterials;
    }

    public PLMVarianceAffectedItem updateVarianceAffectedItem(Integer id, PLMVarianceAffectedItem plmVarianceAffectedItem) {
        checkNotNull(plmVarianceAffectedItem);
        PLMVariance variance = varianceRepository.findOne(id);
        PLMVarianceAffectedItem oldAffectedItem = varianceAffectedItemRepository.findOne(plmVarianceAffectedItem.getId());
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceAffectedItemUpdatedEvent(variance, oldAffectedItem, plmVarianceAffectedItem));
        plmVarianceAffectedItem = varianceAffectedItemRepository.save(plmVarianceAffectedItem);
        return plmVarianceAffectedItem;
    }

    public PLMVarianceAffectedMaterial updateVarianceAffectedPart(Integer id, PLMVarianceAffectedMaterial plmVarianceAffectedMaterial) {
        checkNotNull(plmVarianceAffectedMaterial);
        PLMVariance variance = varianceRepository.findOne(id);
        PLMVarianceAffectedMaterial oldAffectedItem = varianceAffectedMaterialRepository.findOne(plmVarianceAffectedMaterial.getId());
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceAffectedPartUpdatedEvent(variance, oldAffectedItem, plmVarianceAffectedMaterial));
        plmVarianceAffectedMaterial = varianceAffectedMaterialRepository.save(plmVarianceAffectedMaterial);
        return plmVarianceAffectedMaterial;
    }

    public void deleteVarianceAffectedItem(Integer id, Integer affactedItemId) {
        PLMVarianceAffectedItem varianceAffectedItem = varianceAffectedItemRepository.findOne(affactedItemId);
        PLMVariance variance = varianceRepository.findOne(id);
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceAffectedItemDeletedEvent(variance, varianceAffectedItem));
        varianceAffectedItemRepository.delete(affactedItemId);
    }

    public void deleteVarianceAffectedPart(Integer id, Integer affactedPartId) {
        PLMVarianceAffectedMaterial varianceAffectedParts = varianceAffectedMaterialRepository.findOne(affactedPartId);
        PLMVariance variance = varianceRepository.findOne(id);
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceAffectedPartDeletedEvent(variance, varianceAffectedParts));
        varianceAffectedMaterialRepository.delete(affactedPartId);
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','relateditem')")
    public void deleteVarianceRelatedItem(Integer id, Integer item) {
        PLMVariance variance = varianceRepository.findOne(id);
        PLMChangeRelatedItem affectedItem = changeRelatedItemRepository.findOne(item);
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceChangeRelatedDeletedEvent(variance, affectedItem));
        changeRelatedItemRepository.delete(item);
    }


    @Transactional(readOnly = true)
    public List<PLMVariance> findByAffectedItem(Integer itemId) {
        List<Integer> ids = new ArrayList<>();
        List<PLMVarianceAffectedItem> affectedItems = new ArrayList<>();
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
        List<PLMItemRevision> revisions = itemRevisionRepository.getByItemMasterOrderByCreatedDateDesc(plmItem.getId());
        if (revisions.size() > 0) {
            for (PLMItemRevision itemRevision : revisions) {
                List<PLMVarianceAffectedItem> affectedItem = varianceAffectedItemRepository.findByItem(itemRevision.getId());
                affectedItems.addAll(affectedItem);
            }
        }

        for (PLMVarianceAffectedItem item : affectedItems) {
            ids.add(item.getVariance());
        }
        return findMultiple(ids);
    }

    @Transactional(readOnly = true)
    public List<PLMVariance> findByAffectedPart(Integer partId) {
        List<Integer> ids = new ArrayList<>();
        List<PLMVarianceAffectedMaterial> affectedMaterials = new ArrayList<>();
        if (partId != null) {
            List<PLMVarianceAffectedMaterial> affectedItem = varianceAffectedMaterialRepository.findByMaterial(partId);
            affectedMaterials.addAll(affectedItem);
        }

        for (PLMVarianceAffectedMaterial item : affectedMaterials) {
            ids.add(item.getVariance());
        }
        return findMultiple(ids);
    }

    public DetailsCount getVarianceDetailsCount(Integer id) {
        DetailsCount detailsCount = new DetailsCount();
        detailsCount.setFiles(fileRepository.findByChangeAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(id).size());
        detailsCount.setFiles(detailsCount.getFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        detailsCount.setAffectedItems(varianceAffectedObjectRepository.findByVarianceOrderByModifiedDateAsc(id).size());
        detailsCount.setRelatedItems(ecoService.getAllChangeRelatedItems(id).size());
        return detailsCount;
    }

    @Transactional(readOnly = true)
    public PLMWorkflow getAttachedWorkflow(Integer varianceId) {
        PLMWorkflow workflow = workFlowService.getAttachedWorkflow(varianceId);
        return workFlowService.get(workflow.getId());
    }


    public PLMVariance getWorkflow(Integer id) {
        checkNotNull(id);
        PLMVariance plmVariance = varianceRepository.findOne(id);
        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(plmVariance.getId());
        if (workflow != null) {
            PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
            if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                plmVariance.setStartWorkflow(true);
            }
        }

        return plmVariance;
    }

    public PLMVariance saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        PLMVariance plmVariance = varianceRepository.findOne(objectId);
        if (plmVariance != null) {
            PLMChangeAttribute changeAttribute = new PLMChangeAttribute();
            changeAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, changeAttribute);

        }
        return plmVariance;
    }

    @Transactional
    public void checkIsRecurring(Integer varianceId) {
        List<Integer> recurringVariances = new ArrayList<>();
        //Integer recurringValue = preferenceRepository.findByPreferenceKey("APPLICATION.RECURRING_ITEM").getIntegerValue();
        List<PLMVarianceAffectedItem> varianceAffectedItems = varianceAffectedItemRepository.findByVariance(varianceId);
        if (varianceAffectedItems != null) {
            varianceAffectedItems.forEach(plmVarianceAffectedItem -> {
                List<PLMVarianceAffectedItem> AffectedItems = varianceAffectedItemRepository.findByItem(plmVarianceAffectedItem.getItem());
                if (AffectedItems.size() > 1) {
                    AffectedItems.forEach(AffectedItem -> {
                        AffectedItem.setIsRecurring(true);
                        varianceAffectedItemRepository.save(AffectedItem);
                        PLMVariance variance = varianceRepository.findOne(varianceId);
                        variance.setIsRecurring(true);
                        varianceRepository.save(variance);
                    });
                }
            });
        }
    }

    @Transactional
    public void checkIsRecurringForParts(Integer varianceId) {
        List<Integer> recurringVariances = new ArrayList<>();
        //Integer recurringValue = preferenceRepository.findByPreferenceKey("APPLICATION.RECURRING_ITEM").getIntegerValue();
        List<PLMVarianceAffectedMaterial> varianceAffectedParts = varianceAffectedMaterialRepository.findByVariance(varianceId);
        if (varianceAffectedParts != null) {
            varianceAffectedParts.forEach(plmVarianceAffectedMaterial -> {
                List<PLMVarianceAffectedMaterial> AffectedParts = varianceAffectedMaterialRepository.findByMaterial(plmVarianceAffectedMaterial.getMaterial());
                if (AffectedParts.size() > 1) {
                    AffectedParts.forEach(AffectedItem -> {
                        AffectedItem.setIsRecurring(true);
                        varianceAffectedMaterialRepository.save(AffectedItem);
                        PLMVariance variance = varianceRepository.findOne(varianceId);
                        variance.setIsRecurring(true);
                        varianceRepository.save(variance);
                    });
                }
            });
        }
    }

    @Transactional
    public void checkIsRecurringAfterDelete(Integer varianceId, Integer itemId) {
        int count = 0;
        PLMVariance variance = varianceRepository.findOne(varianceId);
        List<PLMVarianceAffectedItem> varianceAffectedItems = varianceAffectedItemRepository.findByItem(itemId);
        List<PLMVarianceAffectedItem> varianceAffectedItems1 = varianceAffectedItemRepository.findByVariance(varianceId);
        if (varianceAffectedItems != null) {
            if (varianceAffectedItems.size() == 1) {
                varianceAffectedItems.forEach(plmVarianceAffectedItem -> {
                    plmVarianceAffectedItem.setIsRecurring(false);
                    plmVarianceAffectedItem = varianceAffectedItemRepository.save(plmVarianceAffectedItem);
                    PLMVariance variance1 = varianceRepository.findOne(plmVarianceAffectedItem.getVariance());
                    checkIfVarianceHasRecurringItems(variance1.getId());
                    checkIfVarianceHasRecurringItems(varianceId);
                });
            } else {
                if (varianceAffectedItems1.size() == 0) {
                    variance.setIsRecurring(false);
                }
                for (PLMVarianceAffectedItem varianceAffectedItem : varianceAffectedItems1) {
                    if (varianceAffectedItem.getIsRecurring() && count == 0) {
                        count++;
                        variance.setIsRecurring(true);
                    } else if (count == 0) variance.setIsRecurring(false);
                }
                varianceRepository.save(variance);
            }
        }
    }

    @Transactional
    public void checkIsRecurringAfterDeleteForParts(Integer varianceId, Integer partId) {
        int count = 0;
        PLMVariance variance = varianceRepository.findOne(varianceId);
        List<PLMVarianceAffectedMaterial> varianceAffectedParts = varianceAffectedMaterialRepository.findByMaterial(partId);
        List<PLMVarianceAffectedMaterial> varianceAffectedParts1 = varianceAffectedMaterialRepository.findByVariance(varianceId);
        if (varianceAffectedParts != null) {
            if (varianceAffectedParts.size() == 1) {
                varianceAffectedParts.forEach(plmVarianceAffectedMaterial -> {
                    plmVarianceAffectedMaterial.setIsRecurring(false);
                    plmVarianceAffectedMaterial = varianceAffectedMaterialRepository.save(plmVarianceAffectedMaterial);
                    PLMVariance variance1 = varianceRepository.findOne(plmVarianceAffectedMaterial.getVariance());
                    checkIfVarianceHasRecurringParts(variance1.getId());
                    checkIfVarianceHasRecurringParts(varianceId);
                });
            } else {
                if (varianceAffectedParts1.size() == 0) {
                    variance.setIsRecurring(false);
                }
                for (PLMVarianceAffectedMaterial varianceAffectedItem : varianceAffectedParts1) {
                    if (varianceAffectedItem.getIsRecurring() && count == 0) {
                        count++;
                        variance.setIsRecurring(true);
                    } else if (count == 0) variance.setIsRecurring(false);
                }
                varianceRepository.save(variance);
            }
        }
    }

    public void checkIfVarianceHasRecurringItems(Integer id) {
        PLMVariance variance = varianceRepository.findOne(id);
        List<PLMVarianceAffectedItem> varianceAffectedItems = varianceAffectedItemRepository.findByVarianceAndIsRecurringTrue(id);
        if (varianceAffectedItems.size() != 0 && variance.getIsRecurring()) {
            variance.setIsRecurring(true);
            varianceRepository.save(variance);
        } else {
            variance.setIsRecurring(false);
            varianceRepository.save(variance);
        }
    }

    public void checkIfVarianceHasRecurringParts(Integer id) {
        PLMVariance variance = varianceRepository.findOne(id);
        List<PLMVarianceAffectedMaterial> varianceAffectedParts = varianceAffectedMaterialRepository.findByVarianceAndIsRecurringTrue(id);
        if (varianceAffectedParts.size() != 0 && variance.getIsRecurring()) {
            variance.setIsRecurring(true);
            varianceRepository.save(variance);
        } else {
            variance.setIsRecurring(false);
            varianceRepository.save(variance);
        }
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','relateditem')")
    public List<PLMChangeRelatedItem> createVarianceRelatedItems(Integer variance, List<PLMChangeRelatedItem> changeRelatedItems) {
        PLMVariance plmVariance = varianceRepository.findOne(variance);
        for (PLMChangeRelatedItem relatedItem : changeRelatedItems) {
            changeRelatedItemRepository.save(relatedItem);
        }
        if (plmVariance != null) {
            applicationEventPublisher.publishEvent(new VarianceEvents.VarianceRelatedItemsAddedEvent(plmVariance, changeRelatedItems));
        }
        return changeRelatedItems;
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && (#event.attachedToObject.changeType.name() == 'DEVIATION' || #event.attachedToObject.changeType.name() == 'WAIVER')")
    public void varianceWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PLMVariance variance = (PLMVariance) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceWorkflowStartedEvent(variance, plmWorkflow));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && (#event.attachedToObject.changeType.name() == 'DEVIATION' || #event.attachedToObject.changeType.name() == 'WAIVER')")
    public void varianceWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PLMVariance variance = (PLMVariance) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        variance.setStatus(fromStatus.getName());
        variance.setStatusType(fromStatus.getType());
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceWorkflowPromotedEvent(variance, plmWorkflow, fromStatus, toStatus));
        if (fromStatus.getType() == WorkflowStatusType.REJECTED) {
        }
        update(variance);
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && (#event.attachedToObject.changeType.name() == 'DEVIATION' || #event.attachedToObject.changeType.name() == 'WAIVER')")
    public void varianceWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMVariance variance = (PLMVariance) event.getAttachedToObject();
        PLMWorkflowStatus toStatus = event.getToStatus();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        variance.setStatus(toStatus.getName());
        variance.setStatusType(toStatus.getType());
        varianceRepository.save(variance);
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceWorkflowDemotedEvent(variance, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && (#event.attachedToObject.changeType.name() == 'DEVIATION' || #event.attachedToObject.changeType.name() == 'WAIVER')")
    public void varianceWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PLMVariance variance = (PLMVariance) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        variance.setStatus(fromStatus.getName());
        variance.setStatusType(fromStatus.getType());
        update(variance);
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceWorkflowFinishedEvent(variance, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && (#event.attachedToObject.changeType.name() == 'DEVIATION' || #event.attachedToObject.changeType.name() == 'WAIVER')")
    public void varianceWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PLMVariance variance = (PLMVariance) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceWorkflowHoldEvent(variance, plmWorkflow, fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && (#event.attachedToObject.changeType.name() == 'DEVIATION' || #event.attachedToObject.changeType.name() == 'WAIVER')")
    public void varianceWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PLMVariance variance = (PLMVariance) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceWorkflowUnholdEvent(variance, plmWorkflow, fromStatus));
    }

    public List<PLMVariance> getVariances(Integer mfrPartId) {
        List<PLMVarianceAffectedMaterial> varianceAffectedMaterials = varianceAffectedMaterialRepository.findByMaterial(mfrPartId);
        List<PLMVariance> variances = new ArrayList<>();
        for (PLMVarianceAffectedMaterial varianceAffectedMaterial : varianceAffectedMaterials) {
            variances.add(varianceRepository.findOne(varianceAffectedMaterial.getVariance()));
        }
        return variances;
    }

    public List<Person> getOriginator(VarianceType type) {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = varianceRepository.getOriginator(type);
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }

    public List<String> getStatus(VarianceType type) {
        return varianceRepository.getStatus(type);
    }
}
