package com.cassinisys.plm.service.cm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.col.AttributeAttachment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.service.col.AttributeAttachmentService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.DCOEvents;
import com.cassinisys.plm.event.ItemEvents;
import com.cassinisys.plm.filtering.DCOChangeRequestCriteria;
import com.cassinisys.plm.filtering.DCOChangeRequestPredicateBuilder;
import com.cassinisys.plm.filtering.DCOCriteria;
import com.cassinisys.plm.filtering.DCOPredicateBuilder;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.cm.dto.*;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.mobile.DCODetails;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pqm.dto.QualityTypeAttributeDto;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.dto.DefinitionEventDto;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionStatusHistoryRepository;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.wf.*;
import com.cassinisys.plm.service.mfr.ManufacturerPartService;
import com.cassinisys.plm.service.plm.BomService;
import com.cassinisys.plm.service.plm.ItemService;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.cassinisys.plm.service.wf.WorkflowEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;


/**
 * Created by CassiniSystems on 09-06-2020.
 */
@Service
public class DCOService implements CrudService<PLMDCO, Integer> {

    @Autowired
    private DCORepository dcoRepository;
    @Autowired
    private DCOPredicateBuilder dcoPredicateBuilder;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;


    @Autowired
    private PLMWorkflowService workflowService;

    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;

    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;

    @Autowired
    private PLMWorkFlowStatusAssignmentRepository workFlowStatusAssignmentRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private DCOChangeRequestPredicateBuilder changeRequestPredicateBuilder;

    @Autowired
    private DCRRepository dcrRepository;

    @Autowired
    private DCRAffectedItemRepository dcrAffectedItemRepository;

    @Autowired
    private DCOAffectedItemRepository dcoAffectedItemRepository;

    @Autowired
    private DCODCRRepository dcodcrRepository;

    @Autowired
    private ItemRevisionRepository itemRevisionRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private AffectedItemRepository affectedItemRepository;

    @Autowired
    private ChangeTypeAttributeRepository changeTypeAttributeRepository;


    @Autowired
    private ChangeAttributeRepository changeAttributeRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private DCRService dcrService;

    @Autowired
    private AttributeAttachmentService attributeAttachmentService;

    @Autowired
    private ChangeFileRepository changeFileRepository;

    @Autowired
    private ChangeTypeRepository changeTypeRepository;

    @Autowired
    private ECRService ecrService;
    @Autowired
    private MCOService mcoService;
    @Autowired
    private ECOService ecoService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ChangeRelatedItemRepository changeRelatedItemRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ItemRevisionStatusHistoryRepository revisionStatusHistoryRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private BomService bomService;
    @Autowired
    private ChangeRepository changeRepository;
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private ManufacturerPartService manufacturerPartService;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private WorkflowEventService workflowEventService;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private PLMWorkflowRepository workflowRepository;
    @Autowired
    private PLMWorkflowDefinitionStatusRepository plmWorkflowDefinitionStatusRepository;
    @Autowired
    private PLMWorkflowDefinitionEventRepository plmWorkflowDefinitionEventRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository plmWorkflowDefinitionRepository;
    @Autowired
    private PLMWorkflowEventRepository plmWorkflowEventRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#dco,'create')")
    public PLMDCO create(PLMDCO dco) {
        PLMDCO existDCO = dcoRepository.findByDcoNumber(dco.getDcoNumber());
        if (existDCO != null) {
            throw new CassiniException(messageSource.getMessage(dco.getDcoNumber() + " : " + "dco_number_already_exists", null, "DCO Number already exist", LocaleContextHolder.getLocale()));
        }

        Integer workflowDef = dco.getWorkflow();
        Integer workflowStatusId = dco.getWorkflowStatus();
        dco.setWorkflowStatus(null);
        dco.setWorkflow(null);
        autoNumberService.saveNextNumber(dco.getChangeClass().getAutoNumberSource().getId(), dco.getDcoNumber());
        PLMDCO plmdco = dcoRepository.save(dco);
        PLMWorkflow workflow = attachDCOWorkflow(dco.getId(), workflowDef, workflowStatusId);
        applicationEventPublisher.publishEvent(new DCOEvents.DCOCreatedEvent(dco));
        return plmdco;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#dco.id,'edit')")
    public PLMDCO update(PLMDCO dco) {
        PLMDCO oldDco = JsonUtils.cloneEntity(dcoRepository.findOne(dco.getId()), PLMDCO.class);
        dco = dcoRepository.save(dco);
        if (dco.getIsReleased().equals(true)) {
            List<PLMDCOAffectedItem> affectedItems = dcoAffectedItemRepository.findByDco(dco.getId());
            if (affectedItems.size() != 0) {
                for (PLMDCOAffectedItem affectedItem : affectedItems) {
                    PLMItemRevision itemRevision = null;
                    if (affectedItem.getToItem() != null) {
                        itemRevision = itemRevisionRepository.findOne(affectedItem.getToItem());
                    } else {
                        itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                    }
                    PLMItem item1 = itemRepository.findOne(itemRevision.getItemMaster());
                    if (!itemRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED) && !itemRevision.getLifeCyclePhase().equals(LifeCyclePhaseType.CANCELLED)) {
                        if (dco.getStatusType().equals(WorkflowStatusType.RELEASED)) {
                            itemRevision.setReleased(true);
                            Integer notReleasedDocumentCount = objectDocumentRepository.getNotReleasedDocumentsCount(itemRevision.getId());
                            if (notReleasedDocumentCount > 0) {
                                String message = messageSource.getMessage("affected_item_has_unreleased_documents", null, "[{0}] affected item has some unreleased documents", LocaleContextHolder.getLocale());
                                String result = MessageFormat.format(message + ".", item1.getItemNumber());
                                throw new CassiniException(result);
                            }
                        } else if (dco.getStatusType().equals(WorkflowStatusType.REJECTED)) {
                            itemRevision.setRejected(true);
                        }
                        itemRevision.setReleasedDate(new Date());
                        if (affectedItem.getEffectiveDate() != null) {
                            itemRevision.setEffectiveFrom(affectedItem.getEffectiveDate());
                        } else {
                            itemRevision.setEffectiveFrom(dco.getReleasedDate());
                        }
                        itemRevision.setEffectiveTo(null);
                        itemRevision = itemRevisionRepository.save(itemRevision);

                    /* App Events */
                        applicationEventPublisher.publishEvent(new ItemEvents.ItemReleasedEvent(item1, itemRevision, dco));

                        List<PLMItemRevision> instances = itemRevisionRepository.findByInstanceOrderByCreatedDateDesc(itemRevision.getId());
                        for (PLMItemRevision instance : instances) {
                            instance.setEffectiveFrom(itemRevision.getEffectiveFrom());
                            instance.setEffectiveTo(itemRevision.getEffectiveTo());
                            instance = itemRevisionRepository.save(instance);
                        }
                    }

                    Integer[] dcrs = affectedItem.getChangeRequests();
                    if (dcrs.length > 0) {
                        for (int i = 0; i < dcrs.length; i++) {
                            Integer dcrId = dcrs[i];
                            List<PLMDCRAffectedItem> plmdcrAffectedItems = dcrAffectedItemRepository.findByDcr(dcrId);
                            for (PLMDCRAffectedItem plmdcrAffectedItem : plmdcrAffectedItems) {
                                PLMItemRevision revision = itemRevisionRepository.findOne(plmdcrAffectedItem.getItem());
                                if (revision.getItemMaster().equals(itemRevision.getItemMaster())) {
                                    plmdcrAffectedItem.setIsImplemented(true);
                                    plmdcrAffectedItem.setImplementedDate(dco.getReleasedDate());

                                    plmdcrAffectedItem = dcrAffectedItemRepository.save(plmdcrAffectedItem);
                                }
                            }

                            List<PLMDCRAffectedItem> savedAffectedItems = dcrAffectedItemRepository.findByDcr(dcrId);

                            Boolean notImplemented = false;

                            for (PLMDCRAffectedItem savedAffectedItem : savedAffectedItems) {
                                if (!savedAffectedItem.getIsImplemented()) {
                                    notImplemented = true;
                                }
                            }

                            if (!notImplemented) {
                                PLMDCR plmdcr = dcrRepository.findOne(dcrId);
                                plmdcr.setIsImplemented(true);
                                plmdcr.setImplementedDate(dco.getReleasedDate());

                                plmdcr = dcrRepository.save(plmdcr);
                            }
                        }
                    }
                }
            }
        }

        applicationEventPublisher.publishEvent(new DCOEvents.DCOBasicInfoUpdatedEvent(oldDco, dco));
        return dcoRepository.save(dco);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
        dcoRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject,'view')")
    public PLMDCO get(Integer id) {
        PLMDCO plmdco = dcoRepository.findOne(id);
        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(plmdco.getId());
        plmdco.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        plmdco.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        plmdco.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        return plmdco;
    }

    @Transactional(readOnly = true)
    public DCODetails getDCODetails(Integer id) {
        PLMDCO plmdco = dcoRepository.findOne(id);
        DCODetails dcoDetails = new DCODetails();
        dcoDetails.setId(plmdco.getId());
        dcoDetails.setDcoNumber(plmdco.getDcoNumber());
        dcoDetails.setDcoType(changeTypeRepository.findOne(plmdco.getDcoType()).getName());
        dcoDetails.setStatus(plmdco.getStatus());
        dcoDetails.setTitle(plmdco.getTitle());
        dcoDetails.setDescription(plmdco.getDescription());
        dcoDetails.setReasonForChange(plmdco.getReasonForChange());
        dcoDetails.setChangeAnalyst(personRepository.findOne(plmdco.getChangeAnalyst()).getFullName());
        dcoDetails.setCreatedDate(plmdco.getCreatedDate());
        dcoDetails.setModifiedDate(plmdco.getModifiedDate());
        dcoDetails.setCreatedBy(personRepository.findOne(plmdco.getCreatedBy()).getFullName());
        dcoDetails.setModifiedBy(personRepository.findOne(plmdco.getModifiedBy()).getFullName());
        dcoDetails.setWorkflow(plmdco.getWorkflow());
        return dcoDetails;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMDCO> getAll() {
        return dcoRepository.findAll();
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMDCO> findMultiple(List<Integer> ids) {
        return dcoRepository.findByIdIn(ids);
    }

    public List<PLMChangeType> getMultipleChangeTypes(List<Integer> ids) {
        return changeTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<DCODto> getAllDCOS(Pageable pageable, DCOCriteria dcoCriteria) {
        Predicate predicate = dcoPredicateBuilder.build(dcoCriteria, QPLMDCO.pLMDCO);
        Page<PLMDCO> dcos = dcoRepository.findAll(predicate, pageable);

        List<DCODto> dcoDtos = new LinkedList<>();
        dcos.getContent().forEach(dco -> {
            DCODto dto = new DCODto();
            dto.setId(dco.getId());
            dto.setObjectType(dco.getObjectType().name());
            dto.setSubType(dco.getChangeClass().getName());
            dto.setDcoNumber(dco.getDcoNumber());
            dto.setDescription(dco.getDescription());
            Person person = personRepository.findOne(dco.getChangeAnalyst());
            dto.setChangeAnalyst(person.getFullName());
            dto.setDcoType(changeTypeRepository.findOne(dco.getDcoType()).getName());
            dto.setReasonForChange(dco.getReasonForChange());
            dto.setTitle(dco.getTitle());
            dto.setStatus(dco.getStatus());
            dto.setStatusType(dco.getStatusType());
            dto.setReleasedDate(dco.getReleasedDate());
            dto.setIsReleased(dco.getIsReleased());
            dto.setModifiedBy(personRepository.findOne(dco.getModifiedBy()).getFullName());
            dto.setModifiedDate(dco.getModifiedDate());
            dto.setCreatedDate(dco.getCreatedDate());
            dto.setCreatedBy(personRepository.findOne(dco.getModifiedBy()).getFullName());
            dto.setReleasedDate(dco.getReleasedDate());

            WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(dco.getId());

            dto.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            dto.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            dto.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
            dto.setOnHold(workFlowStatusDto.getOnHold());
            dto.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            dto.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());

            dto.setTagsCount(dco.getTags().size());
            dcoDtos.add(dto);


        });

        return new PageImpl<DCODto>(dcoDtos, pageable, dcos.getTotalElements());
    }

    @Transactional
    public PLMWorkflow attachDCOWorkflow(Integer id, Integer wfDefId, Integer workflowStatusId) {
        PLMWorkflow workflow = null;
        PLMDCO dco = dcoRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (dco != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.CHANGE, dco.getId(), wfDef);
            dco.setWorkflow(workflow.getId());
            if (workflowStatusId != null && dco.getRevisionCreationType().equals(RevisionCreationType.ACTIVITY_COMPLETION)) {
                PLMWorkflowDefinitionStatus workflowDefinitionStatus = plmWorkflowDefinitionStatusRepository.findOne(workflowStatusId);
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findByWorkflowAndNameAndType(workflow.getId(), workflowDefinitionStatus.getName(), workflowDefinitionStatus.getType());
                dco.setWorkflowStatus(workflowStatus.getId());
            }
            dcoRepository.save(dco);
        }
        return workflow;
    }

    @Transactional
    public PLMWorkflow attachNewDCOWorkflow(Integer id, Integer wfDefId, RevisionCreationType revisionCreationType, Integer workflowStatusId) {
        PLMWorkflow workflow = null;
        PLMDCO dco = dcoRepository.findOne(id);
        dco.setRevisionCreationType(revisionCreationType);
        dco = dcoRepository.save(dco);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (dco != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.CHANGE, dco.getId(), wfDef);
            dco.setWorkflow(workflow.getId());
            if (workflowStatusId != null && dco.getRevisionCreationType().equals(RevisionCreationType.ACTIVITY_COMPLETION)) {
                PLMWorkflowDefinitionStatus workflowDefinitionStatus = plmWorkflowDefinitionStatusRepository.findOne(workflowStatusId);
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findByWorkflowAndNameAndType(workflow.getId(), workflowDefinitionStatus.getName(), workflowDefinitionStatus.getType());
                dco.setWorkflowStatus(workflowStatus.getId());
            }
            dco = dcoRepository.save(dco);
            applicationEventPublisher.publishEvent(new DCOEvents.DCOWorkflowChangeEvent(dco, null, workflow));
        }
        return workflow;
    }

    private void releaseAttachedObject(PLMDCO plmdco, PLMWorkflow workflow) {
        PLMChange plmChange = changeRepository.findOne(workflow.getAttachedTo());
        CassiniObject object = objectRepository.findById(plmChange.getId());
        if (object != null && object.getObjectType().toString().equalsIgnoreCase("CHANGE")) {
            PLMDCO dco = dcoRepository.findOne(workflow.getAttachedTo());
            if (dco != null) {
                List<PLMDCOAffectedItem> items = dcoAffectedItemRepository.findByDco(workflow.getAttachedTo());
                items.forEach(item -> {
                    PLMItemRevision rev = itemService.getRevision(item.getItem());
                    PLMItem itemMaster = itemService.get(rev.getItemMaster());
                    PLMItemRevision toRevision = itemService.getByItemMasterAndRevision(itemMaster.getId(), item.getToRevision());
                    if (!toRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED) && !toRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.CANCELLED)) {
                        if (dco.getStatusType().equals(WorkflowStatusType.REJECTED)) {
                            toRevision.setRejected(true);
                        } else {
                            toRevision.setReleased(Boolean.TRUE);
                        }
                        toRevision.setChangeOrder(dco.getId());
                        toRevision.setReleasedDate(new Date());
                        toRevision = itemRevisionRepository.save(toRevision);
                        if (dco.getStatusType().equals(WorkflowStatusType.RELEASED)) {
                            itemMaster.setLatestReleasedRevision(toRevision.getId());
                        }
                        itemMaster = itemRepository.save(itemMaster);
                    }
                });
                items.forEach(item -> {
                    //First make sure you can release the item!!!
                    PLMItemRevision rev = itemService.getRevision(item.getItem());
                    PLMItem itemMaster = itemService.get(rev.getItemMaster());
                    PLMItemRevision toRevision = itemService.getByItemMasterAndRevision(itemMaster.getId(), item.getToRevision());
                    validateRelease(toRevision);
                    updateBomWithAsReleased(toRevision);
                });
                dco.setIsReleased(Boolean.TRUE);
                dco.setReleasedDate(new Date());
                update(dco);
            }
        }
    }

    private void updateBomWithAsReleased(PLMItemRevision itemRevision) {
        List<PLMBom> bomItems = bomService.getBom(itemRevision, false);
        bomItems.forEach(bomItem -> {
            PLMItem itemMaster = bomItem.getItem();
            if (itemMaster.getLatestReleasedRevision() != null) {
                bomItem.setAsReleasedRevision(itemMaster.getLatestReleasedRevision());
            } else {
                bomItem.setAsReleasedRevision(itemMaster.getLatestRevision());
            }
        });
        List<PLMItemRevision> revisions = itemRevisionRepository.findByInstanceOrderByCreatedDateDesc(itemRevision.getId());
        for (PLMItemRevision revision : revisions) {
            List<PLMBom> instanceBomItems = bomService.getBom(revision, false);
            instanceBomItems.forEach(bomItem -> {
                PLMItem itemMaster = bomItem.getItem();
                if (itemMaster.getLatestReleasedRevision() != null) {
                    bomItem.setAsReleasedRevision(itemMaster.getLatestReleasedRevision());
                } else {
                    bomItem.setAsReleasedRevision(itemMaster.getLatestRevision());
                }
            });
            bomItems.addAll(instanceBomItems);
        }
        bomService.update(bomItems);
    }

    private void validateRelease(PLMItemRevision itemRevision) {
        List<PLMBom> bomItems = bomService.getBom(itemRevision, false);
        bomItems.forEach(bomItem -> {
            PLMItem itemMaster = bomItem.getItem();
            PLMItemRevision itemMasterRevision = itemRevisionRepository.findOne(itemMaster.getLatestRevision());
            if (itemMaster.getLatestReleasedRevision() == null) {
                String message = messageSource.getMessage("some_of_the_dco_affected_items_un_released", null, "{0} : affected item have un-released BOM items. This DCO cannot be released.", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", itemMaster.getItemNumber());
                throw new CassiniException(result);
            } else if (itemMasterRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.OBSOLETE)) {
                String message = messageSource.getMessage("some_of_the_affected_items_obsolete", null, "{0} : affected item have absolete BOM items. This {1} cannot be released", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", itemMaster.getItemNumber(), "DCO");
                throw new CassiniException(result);
            } else if (!itemMasterRevision.getReleased()) {
                String message = messageSource.getMessage("some_of_the_dco_affected_items_un_released", null, "{0} : affected item have un-released BOM items. This DCO cannot be released.", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", itemMaster.getItemNumber());
                throw new CassiniException(result);
            }
        });
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','relateditem')")
    public void deleteDcoRelatedItem(Integer id, Integer item) {
        PLMDCO dco = dcoRepository.findOne(id);
        PLMChangeRelatedItem affectedItem = changeRelatedItemRepository.findOne(item);
        applicationEventPublisher.publishEvent(new DCOEvents.DCORelatedItemDeletedEvent(dco, affectedItem));
        changeRelatedItemRepository.delete(item);
    }

    @Transactional(readOnly = true)
    public Page<PLMDCR> getFilteredDcrs(Pageable pageable, DCOChangeRequestCriteria criteria) {
        Predicate predicate = changeRequestPredicateBuilder.build(criteria, QPLMDCR.pLMDCR);
        Page<PLMDCR> dcrs = dcrRepository.findAll(predicate, pageable);
        dcrs.forEach(plmdcr -> {
            List<PLMDCRAffectedItem> affectedItems = dcrAffectedItemRepository.findByDcr(plmdcr.getId());
            affectedItems.forEach(item -> {
                RequestedItemDto requestedItemDto = new RequestedItemDto();
                requestedItemDto.setAffectedItem(item);
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getItem());
                PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());
                requestedItemDto.setFromRevision(itemRevision.getRevision());
                requestedItemDto.setFromLifeCycle(itemRevision.getLifeCyclePhase());
                requestedItemDto.setName(plmItem.getItemName());
                requestedItemDto.setNumber(plmItem.getItemNumber());
                String nextRev = itemService.getNextRevisionSequence(plmItem);
                List<String> notReleasedRevisions = itemRevisionRepository.getNotReleasedRevisionItems(plmItem.getId());
                requestedItemDto.getToRevisions().addAll(notReleasedRevisions);
                if (requestedItemDto.getToRevisions().indexOf(nextRev) == -1) {
                    requestedItemDto.getToRevisions().add(nextRev);
                }
                if (requestedItemDto.getToRevisions().size() == 1) {
                    requestedItemDto.setToRevision(nextRev);
                }
                plmItem.getItemType().getLifecycle().getPhases().forEach(plmLifeCyclePhase -> {
                    if (plmLifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                        requestedItemDto.getToLifeCyclePhases().add(plmLifeCyclePhase);
                    }
                });
                if (requestedItemDto.getToLifeCyclePhases().size() == 1) {
                    requestedItemDto.setToLifeCycle(requestedItemDto.getToLifeCyclePhases().get(0));
                }
                plmdcr.getRequestedItemDtos().add(requestedItemDto);
            });

        });

        return dcrs;
    }

    @Transactional
    public List<PLMDCR> createChangeReqItems(Integer dcoId, List<PLMDCR> plmdcrs) {
        PLMDCO dco = dcoRepository.findOne(dcoId);

        HashMap<Integer, List<Integer>> dcrMap = new HashMap<>();

        plmdcrs.forEach(plmdcr -> {
            plmdcr.getRequestedItemDtos().forEach(requestedItemDto -> {

                List<Integer> qcrIds = dcrMap.containsKey(requestedItemDto.getAffectedItem().getItem()) ? dcrMap.get(requestedItemDto.getAffectedItem().getItem()) : new ArrayList<Integer>();
                if (qcrIds.indexOf(requestedItemDto.getAffectedItem().getItem()) == -1) {
                    qcrIds.add(plmdcr.getId());
                }
                dcrMap.put(requestedItemDto.getAffectedItem().getItem(), qcrIds);
            });
        });

        HashMap<Integer, RequestedItemDto> affectedItemHashMap = new HashMap<>();

        for (PLMDCR dcr : plmdcrs) {
            List<RequestedItemDto> itemDtos = dcr.getRequestedItemDtos();
            for (RequestedItemDto requestedItemDto : itemDtos) {
                List<PLMDCOAffectedItem> affectedItems = dcoAffectedItemRepository.findByDco(dcoId);
                Boolean itemAlreadyExist = false;
                PLMItemRevision ecrItemRevision = itemRevisionRepository.findOne(requestedItemDto.getAffectedItem().getItem());
                for (PLMDCOAffectedItem affectedItem : affectedItems) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                    if (ecrItemRevision.getItemMaster().equals(itemRevision.getItemMaster())) {
                        itemAlreadyExist = true;

                        Integer[] dcrIds = affectedItem.getChangeRequests();
                        List<Integer> changeRequestIds = new ArrayList<>();
                        if (dcrIds.length > 0) {
                            for (int i = 0; i < dcrIds.length; i++) {
                                changeRequestIds.add(dcrIds[i]);
                            }
                        }

                        if (changeRequestIds.indexOf(dcr.getId()) == -1) {
                            changeRequestIds.add(dcr.getId());
                        }
                        Integer[] intArray = new Integer[changeRequestIds.size()];
                        intArray = changeRequestIds.toArray(intArray);
                        affectedItem.setChangeRequests(intArray);

                        affectedItem = dcoAffectedItemRepository.save(affectedItem);
                    }
                }

                if (!itemAlreadyExist) {
                    RequestedItemDto alreadySaved = affectedItemHashMap.get(requestedItemDto.getAffectedItem().getItem());
                    if (alreadySaved == null) {
                        PLMDCOAffectedItem affectedItem = new PLMDCOAffectedItem();
                        List<Integer> dcrs = dcrMap.get(requestedItemDto.getAffectedItem().getItem());
                        Integer[] intArray = new Integer[dcrs.size()];
                        intArray = dcrs.toArray(intArray);
                        affectedItem.setChangeRequests(intArray);
                        affectedItem.setDco(dcoId);
                        affectedItem.setChange(dcoId);
                        affectedItem.setFromRevision(requestedItemDto.getFromRevision());
                        affectedItem.setToRevision(requestedItemDto.getToRevision());
                        affectedItem.setItem(requestedItemDto.getAffectedItem().getItem());
                        affectedItem = dcoAffectedItemRepository.save(affectedItem);
                        addAffectedItemToWorkflowEventData(dco, affectedItem);
                        if (dco != null && dco.getRevisionsCreated()) {
                            PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                            PLMItemRevision existRevisionItem = itemRevisionRepository.getByItemMasterAndRevision(itemRevision.getItemMaster(), affectedItem.getToRevision());
                            PLMItemRevision revision = null;
                            if (existRevisionItem == null) {
                                revision = itemService.reviseRevisionItem(itemRevision, affectedItem.getToRevision());
                                revision.setFromRevision(itemRevision.getRevision());
                                revision = itemRevisionRepository.save(revision);
                                affectedItem.setToItem(revision.getId());
                            } else {
                                affectedItem.setToItem(existRevisionItem.getId());
                            }
                            affectedItem = dcoAffectedItemRepository.save(affectedItem);
                        }
                        affectedItemHashMap.put(requestedItemDto.getAffectedItem().getItem(), requestedItemDto);
                    }
                }
            }
            PLMDCODCR plmdcodcr = new PLMDCODCR();
            plmdcodcr.setDco(dcoId);
            plmdcodcr.setDcr(dcr.getId());
            dcodcrRepository.save(plmdcodcr);
        }
        applicationEventPublisher.publishEvent(new DCOEvents.DCOChangeRequestAddedEvent(dco, plmdcrs));
        return plmdcrs;
    }


    public List<Person> getChangeAnalysts() {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = dcoRepository.getChangeAnalystIds();
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }

    public List<String> getStatus(){
        return dcoRepository.getStatusIds();
    }

    @Transactional(readOnly = true)
    public List<DCRDto> getChangeReqItems(Integer id) {
        List<PLMDCODCR> plmdcodcrs = dcodcrRepository.findByDco(id);
        List<DCRDto> dcrDtos = new LinkedList<>();
        plmdcodcrs.forEach(plmdcodcr -> {
            PLMDCR dcr = dcrRepository.findOne(plmdcodcr.getDcr());
            DCRDto dto = new DCRDto();
            dto.setId(dcr.getId());
            dto.setType(dcr.getChangeType().name());
            dto.setCrNumber(dcr.getCrNumber());
            dto.setStatus(dcr.getStatus());
            dto.setTitle(dcr.getTitle());
            dto.setDescriptionOfChange(dcr.getDescriptionOfChange());
            dto.setReasonForChange(dcr.getReasonForChange());
            dto.setProposedChanges(dcr.getProposedChanges());
            dto.setUrgency(dcr.getUrgency().toString());
            dto.setStatusType(dcr.getStatusType());
            dto.setChangeAnalyst(personRepository.findOne(dcr.getChangeAnalyst()).getFullName());
            dto.setOriginator(personRepository.findOne(dcr.getOriginator()).getFullName());
            dto.setRequestedBy(personRepository.findOne(dcr.getRequestedBy()).getFullName());
            dto.setRequestedDate(dcr.getRequestedDate());
            dto.setDcodcr(plmdcodcr.getId());
            dcrDtos.add(dto);
        });

        return dcrDtos;
    }

    @Transactional
    public PLMDCOAffectedItem createAffectedItem(PLMDCOAffectedItem item) {
        PLMItem existItem = ecoService.checkItemHasPendingEco(item.getItem());
        item = dcoAffectedItemRepository.save(item);
        PLMDCO dco = dcoRepository.findOne(item.getDco());
        if (dco != null && dco.getRevisionsCreated()) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getItem());
            PLMItemRevision existRevisionItem = itemRevisionRepository.getByItemMasterAndRevision(itemRevision.getItemMaster(), item.getToRevision());
            PLMItemRevision revision = null;
            if (existRevisionItem == null) {
                revision = itemService.reviseRevisionItem(itemRevision, item.getToRevision());
                revision.setFromRevision(itemRevision.getRevision());
                revision = itemRevisionRepository.save(revision);
                item.setToItem(revision.getId());
            } else {
                item.setToItem(existRevisionItem.getId());
            }
            item = dcoAffectedItemRepository.save(item);
        }
        addAffectedItemToWorkflowEventData(dco, item);
        applicationEventPublisher.publishEvent(new DCOEvents.DCOAffectedItemAddedEvent(dco, item));
        return item;
    }

    private void addAffectedItemToWorkflowEventData(PLMDCO dco, PLMDCOAffectedItem ecoAffectedItem) {
        PLMWorkflow workflow = workflowRepository.findOne(dco.getWorkflow());
        PLMWorkflowDefinition workflowDefinition = plmWorkflowDefinitionRepository.findOne(workflow.getWorkflowRevision());
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowOrderByIdAsc(dco.getWorkflow());
        if (workflowEvents.size() > 0) {
            for (PLMWorkflowEvent workflowEvent : workflowEvents) {
                if (workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    PLMWorkflowDefinitionStatus definitionStatus = null;
                    PLMWorkflowDefinitionEvent definitionEvent = null;
                    if (workflowEvent.getActivity() != null) {
                        definitionStatus = plmWorkflowDefinitionStatusRepository.getWorkflowDefinitionStatusByWorkflowAndNameAndType(workflowDefinition.getId(), workflowEvent.getActivity().getName(), workflowEvent.getActivity().getType());
                        definitionEvent = plmWorkflowDefinitionEventRepository.findByWorkflowAndActivityAndEventTypeAndActionType(workflowDefinition.getId(), definitionStatus, workflowEvent.getEventType(), workflowEvent.getActionType());
                    } else {
                        definitionEvent = plmWorkflowDefinitionEventRepository.findByWorkflowAndEventTypeAndActionType(workflowDefinition.getId(), workflowEvent.getEventType(), workflowEvent.getActionType());
                    }

                    HashMap<Integer, PLMLifeCyclePhase> lifeCyclePhaseHashMap = new HashMap<>();
                    ObjectMapper objectMapper = new ObjectMapper();
                    if (definitionEvent != null && definitionEvent.getActionData() != null) {
                        try {
                            List<DefinitionEventDto> eventDtos = objectMapper.readValue(definitionEvent.getActionData(), new TypeReference<List<DefinitionEventDto>>() {
                            });
                            for (DefinitionEventDto eventDto : eventDtos) {
                                lifeCyclePhaseHashMap.put(eventDto.getLifecycle().getId(), eventDto.getLifecyclePhase());
                            }

                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }

                    objectMapper = new ObjectMapper();
                    try {
                        List<AffectedItemDto> affectedItemDto = new LinkedList<>();
                        if (workflowEvent.getActionData() != null) {
                            affectedItemDto = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<AffectedItemDto>>() {
                            });
                        }

                        AffectedItemDto itemDto = new AffectedItemDto();
                        PLMItemRevision itemRevision = itemRevisionRepository.findOne(ecoAffectedItem.getItem());
                        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                        itemDto.setId(ecoAffectedItem.getId());
                        itemDto.setItem(ecoAffectedItem.getItem());
                        itemDto.setToItem(ecoAffectedItem.getToItem());
                        itemDto.setDescription(item.getDescription());
                        itemDto.setItemName(item.getItemName());
                        itemDto.setItemTypeName(item.getItemType().getName());
                        itemDto.setItemNumber(item.getItemNumber());
                        itemDto.setToLifecyclePhase(lifeCyclePhaseHashMap.get(item.getItemType().getLifecycle().getId()));
                        itemDto.setFromRevision(ecoAffectedItem.getFromRevision());
                        itemDto.setToRevision(ecoAffectedItem.getToRevision());
                        itemDto.setFromLifecyclePhase(itemRevision.getLifeCyclePhase());
                        affectedItemDto.add(itemDto);

                        workflowEvent.setActionData(objectMapper.writeValueAsString(affectedItemDto));
                        workflowEvent = plmWorkflowEventRepository.save(workflowEvent);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public List<DCRAffecteditemsDto> getDcoAffectedItem(Integer dco) {
        List<DCRAffecteditemsDto> dtos = new ArrayList<>();
        List<PLMDCOAffectedItem> affectedItems = dcoAffectedItemRepository.findByDco(dco);
        for (PLMDCOAffectedItem affectedItem : affectedItems) {
            DCRAffecteditemsDto affecteditemsDto = new DCRAffecteditemsDto();
            affecteditemsDto.setId(affectedItem.getId());
            affecteditemsDto.setItem(affectedItem.getItem());
            affecteditemsDto.setToItem(affectedItem.getToItem());
            affecteditemsDto.setNotes(affectedItem.getNotes());
            affecteditemsDto.setDco(affectedItem.getDco());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            affecteditemsDto.setItemNumber(item.getItemNumber());
            affecteditemsDto.setItemName(item.getItemName());
            affecteditemsDto.setItemType(item.getItemType().getName());
            affecteditemsDto.setRevision(itemRevision.getRevision());
            affecteditemsDto.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
            affecteditemsDto.setFromRevision(affectedItem.getFromRevision());
            affecteditemsDto.setToRevision(affectedItem.getToRevision());
            List<Integer> dcrIds = new ArrayList<>();
            if (affectedItem.getChangeRequests().length > 0) {
                for (int i = 0; i < affectedItem.getChangeRequests().length; i++) {
                    dcrIds.add(affectedItem.getChangeRequests()[i]);
                }
                affecteditemsDto.getPlmdcrs().addAll(dcrRepository.findByIdIn(dcrIds));
            }

            dtos.add(affecteditemsDto);
        }

        return dtos;
    }


    @Transactional
    public void deleteDcoAffectedItem(Integer id, Integer item) {
        PLMDCO dco = dcoRepository.findOne(id);
        PLMDCOAffectedItem affectedItem = dcoAffectedItemRepository.findOne(item);
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowOrderByIdAsc(dco.getWorkflow());
        for (PLMWorkflowEvent workflowEvent : workflowEvents) {
            if (workflowEvent.getActionData() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    List<AffectedItemDto> affectedItemDto = new LinkedList<>();
                    List<AffectedItemDto> modifiedList = new LinkedList<>();
                    if (workflowEvent.getActionData() != null) {
                        affectedItemDto = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<AffectedItemDto>>() {
                        });
                    }
                    for (AffectedItemDto itemDto : affectedItemDto) {
                        if (!itemDto.getId().equals(affectedItem.getId())) {
                            modifiedList.add(itemDto);
                        }
                    }
                    workflowEvent.setActionData(objectMapper.writeValueAsString(modifiedList));
                    workflowEvent = plmWorkflowEventRepository.save(workflowEvent);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        applicationEventPublisher.publishEvent(new DCOEvents.DCOAffectedItemDeletedEvent(dco, affectedItem));
        dcoAffectedItemRepository.delete(item);
    }

    private void saveChangeAttribute(List<PLMChangeAttribute> changeAttributes, CassiniObject obj) {
        for (PLMChangeAttribute attribute : changeAttributes) {
            PLMChangeTypeAttribute changeTypeAttribute = changeTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null ||
                    attribute.getBooleanValue() || changeTypeAttribute.getDataType().toString().equals("FORMULA")) {

                attribute.setId(new ObjectAttributeId(obj.getId(), attribute.getId().getAttributeDef()));
                changeAttributeRepository.save(attribute);
            }
        }
    }

    public void saveObjectAttributes(List<ObjectAttribute> objectAttributes, CassiniObject obj) {
        for (ObjectAttribute attribute : objectAttributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(obj.getId(), attribute.getId().getAttributeDef()));
                objectAttributeRepository.save(attribute);
            }
        }
    }

    @Transactional
    public ChangeTypeAttributeDto crateChangeObject(PLMObjectType objectType, ChangeTypeAttributeDto
            changeTypeAttributeDto) {
        if (objectType.equals(PLMObjectType.DCO)) {
            PLMDCO plmdco = create(changeTypeAttributeDto.getPlmdco());
            List<PLMChangeAttribute> changeAttributes = changeTypeAttributeDto.getChangeAttributes();
            List<ObjectAttribute> objectAttributes = changeTypeAttributeDto.getObjectAttributes();
            saveChangeAttribute(changeAttributes, plmdco);
            saveObjectAttributes(objectAttributes, plmdco);
            changeTypeAttributeDto.setPlmdco(plmdco);
        } else if (objectType.equals(PLMObjectType.DCR)) {
            PLMDCR plmdcr = dcrService.create(changeTypeAttributeDto.getPlmdcr());
            List<PLMChangeAttribute> changeAttributes = changeTypeAttributeDto.getChangeAttributes();
            List<ObjectAttribute> objectAttributes = changeTypeAttributeDto.getObjectAttributes();
            saveChangeAttribute(changeAttributes, plmdcr);

            saveObjectAttributes(objectAttributes, plmdcr);
            changeTypeAttributeDto.setPlmdcr(plmdcr);
        } else if (objectType.equals(PLMObjectType.ECR)) {
            PLMECR plmecr = ecrService.create(changeTypeAttributeDto.getPlmecr());
            List<PLMChangeAttribute> changeAttributes = changeTypeAttributeDto.getChangeAttributes();
            List<ObjectAttribute> objectAttributes = changeTypeAttributeDto.getObjectAttributes();

            saveChangeAttribute(changeAttributes, plmecr);
            saveObjectAttributes(objectAttributes, plmecr);
            changeTypeAttributeDto.setPlmecr(plmecr);
        } else if (objectType.equals(PLMObjectType.MCO)) {
            if (changeTypeAttributeDto.getMco().getType().equals("ITEMMCO")) {
                PLMItemMCO mco = mcoService.createItemMco(changeTypeAttributeDto.getItemMco());
                List<PLMChangeAttribute> changeAttributes = changeTypeAttributeDto.getChangeAttributes();
                List<ObjectAttribute> objectAttributes = changeTypeAttributeDto.getObjectAttributes();
                saveChangeAttribute(changeAttributes, mco);
                saveObjectAttributes(objectAttributes, mco);
                changeTypeAttributeDto.setMco(mco);
            } else if (changeTypeAttributeDto.getMco().getType().equals("OEMPARTMCO")) {
                PLMManufacturerMCO mco = mcoService.createManufacturerMco(changeTypeAttributeDto.getManufacturerMCO());
                List<PLMChangeAttribute> changeAttributes = changeTypeAttributeDto.getChangeAttributes();
                List<ObjectAttribute> objectAttributes = changeTypeAttributeDto.getObjectAttributes();
                saveChangeAttribute(changeAttributes, mco);
                saveObjectAttributes(objectAttributes, mco);
                changeTypeAttributeDto.setMco(mco);
            }

        }

        return changeTypeAttributeDto;
    }


    @Transactional
    public QualityTypeAttributeDto saveImageAttributeValue(PLMObjectType objectType, Integer objectId, Integer
            attributeId, Map<String, MultipartFile> fileMap) {

        if (objectType.equals(PLMObjectType.DCOTYPE) || objectType.equals(PLMObjectType.DCRTYPE) || objectType.equals(PLMObjectType.ECRTYPE) || objectType.equals(PLMObjectType.MCOTYPE)) {
            PLMChangeAttribute changeAttribute = new PLMChangeAttribute();
            changeAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, changeAttribute);
        } else {
            ObjectAttribute objectAttribute = new ObjectAttribute();
            objectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, objectAttribute);
        }
        return null;
    }

    public void setImage(List<MultipartFile> files, ObjectAttribute attribute) {
        if (files.size() > 0) {
            MultipartFile file = files.get(0);
            try {
                attribute.setImageValue(file.getBytes());
                attribute = objectAttributeRepository.save(attribute);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public QualityTypeAttributeDto saveAttachmentAttributeValue(PLMObjectType objectType, Integer objectId, Integer
            attributeId, Map<String, MultipartFile> fileMap) {
        List<AttributeAttachment> attributeAttachments = null;
        List<Integer> attachmentIds = new ArrayList<>();
        try {
            List<MultipartFile> files = new ArrayList<>();
            fileMap.values().forEach(multipartFile -> {
                files.add(multipartFile);
            });
            String type = objectType.toString();
            type = "CHANGE";

            attributeAttachments = attributeAttachmentService.addAttributeMultipleAttachments(objectId, attributeId, ObjectType.valueOf(type), files);
            attributeAttachments.forEach(attributeAttachment -> {
                attachmentIds.add(attributeAttachment.getId());
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (objectType.equals(PLMObjectType.DCOTYPE) || objectType.equals(PLMObjectType.DCRTYPE) || objectType.equals(PLMObjectType.ECRTYPE) || objectType.equals(PLMObjectType.MCOTYPE)) {
            PLMChangeAttribute changeAttribute = new PLMChangeAttribute();
            changeAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            changeAttribute.setAttachmentValues(values);
            changeAttribute = changeAttributeRepository.save(changeAttribute);
        } else {
            ObjectAttribute objectAttribute = new ObjectAttribute();
            objectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            objectAttribute.setAttachmentValues(values);
            objectAttribute = objectAttributeRepository.save(objectAttribute);
        }

        return null;
    }

    @Transactional(readOnly = true)
    public List<PLMDCO> findByAffectedItem(Integer itemId) {
        List<Integer> ids = new ArrayList<>();
        List<PLMAffectedItem> affectedItems = new ArrayList<>();
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
        List<PLMItemRevision> revisions = itemRevisionRepository.getByItemMasterOrderByCreatedDateDesc(plmItem.getId());
        if (revisions.size() > 0) {
            for (PLMItemRevision itemRevision : revisions) {
                List<PLMAffectedItem> affectedItem = affectedItemRepository.findByItem(itemRevision.getId());
                affectedItem.addAll(affectedItemRepository.findByToItem(itemRevision.getId()));
                affectedItems.addAll(affectedItem);
            }
        }

        for (PLMAffectedItem item : affectedItems) {
            ids.add(item.getChange());
        }
        return findMultiple(ids);
    }

    @Transactional(readOnly = true)
    public DetailsCount getDCRDetailsCount(Integer dcoId) {
        DetailsCount detailsCount = new DetailsCount();
        List<PLMChangeFile> changeFiles = changeFileRepository.findByChangeAndFileTypeAndLatestTrueOrderByModifiedDateDesc(dcoId, "FILE");
        List<PLMDCOAffectedItem> affectedItems = dcoAffectedItemRepository.findByDco(dcoId);
        List<PLMChangeRelatedItem> relatedItems = changeRelatedItemRepository.findByChange(dcoId);
        List<PLMDCODCR> changeRequests = dcodcrRepository.findByDco(dcoId);
        detailsCount.setFiles(changeFiles.size());
        detailsCount.setFiles(detailsCount.getFiles() + objectDocumentRepository.getDocumentsCountByObjectId(dcoId));
        detailsCount.setRelatedItems(relatedItems.size());
        detailsCount.setAffectedItems(affectedItems.size());
        detailsCount.setChangeRequests(changeRequests.size());
        return detailsCount;
    }


    @Transactional
    public void deleteDcoChangeRequest(Integer dcoId, Integer changeId) {
        PLMDCODCR plmdcodcr = dcodcrRepository.findOne(changeId);
        List<PLMDCOAffectedItem> affectedItems = dcoAffectedItemRepository.findByDco(dcoId);
        affectedItems.forEach(affectedItem -> {
            if (affectedItem.getChangeRequests().length > 0) {
                List<Integer> requestIds = new ArrayList<Integer>();
                for (int i = 0; i < affectedItem.getChangeRequests().length; i++) {
                    requestIds.add(affectedItem.getChangeRequests()[i]);
                }

                if (requestIds.size() == 1 && requestIds.indexOf(plmdcodcr.getDcr()) != -1) {
                    affectedItemRepository.delete(affectedItem.getId());
                }
                if (requestIds.size() > 1 && requestIds.indexOf(plmdcodcr.getDcr()) != -1) {
                    List<Integer> dcrIds = requestIds;
                    dcrIds.remove(dcrIds.indexOf(plmdcodcr.getDcr()));
                    Integer[] intArray = new Integer[dcrIds.size()];
                    intArray = dcrIds.toArray(intArray);
                    affectedItem.setChangeRequests(intArray);
                    affectedItemRepository.save(affectedItem);
                }
            }
        });
        PLMDCO plmdco = dcoRepository.findOne(dcoId);
        PLMDCR plmdcr = dcrRepository.findOne(plmdcodcr.getDcr());
        applicationEventPublisher.publishEvent(new DCOEvents.DCOChangeRequestDeletedEvent(plmdco, plmdcr));
        dcodcrRepository.delete(changeId);
    }

    @Transactional
    public PLMDCO createRevisions(Integer dcoId, Boolean start, Boolean finish, Integer statusId) {
        PLMDCO plmdco = dcoRepository.findOne(dcoId);
        if (plmdco.getRevisionCreationType() != null && !plmdco.getRevisionsCreated()) {
            if (start) {
                if (plmdco.getRevisionCreationType().equals(RevisionCreationType.WORKFLOW_START)) {
                    plmdco = updateAffectedItemRevisions(plmdco);
                }
            } else if (finish) {
                plmdco = updateAffectedItemRevisions(plmdco);
            } else {
                if (statusId.equals(plmdco.getWorkflowStatus())) {
                    plmdco = updateAffectedItemRevisions(plmdco);
                }
            }
        }

        return plmdco;
    }

    private PLMDCO updateAffectedItemRevisions(PLMDCO plmdco) {
        List<PLMDCOAffectedItem> affectedItems = dcoAffectedItemRepository.findByDco(plmdco.getId());
        affectedItems.forEach(affectedItem -> {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
            PLMItemRevision existRevisionItem = itemRevisionRepository.getByItemMasterAndRevision(itemRevision.getItemMaster(), affectedItem.getToRevision());
            PLMItemRevision revision = null;
            if (existRevisionItem == null) {
                revision = itemService.reviseRevisionItem(itemRevision, affectedItem.getToRevision());
                revision.setFromRevision(itemRevision.getRevision());
                revision = itemRevisionRepository.save(revision);
                affectedItem.setToItem(revision.getId());
            } else {
                affectedItem.setToItem(existRevisionItem.getId());
            }
            affectedItem = dcoAffectedItemRepository.save(affectedItem);
        });
        plmdco.setRevisionsCreated(true);
        plmdco = dcoRepository.save(plmdco);
        return plmdco;
    }


    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'DCO'")
    public void dcoWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PLMDCO dco = (PLMDCO) event.getAttachedToObject();
        createRevisions(dco.getId(), true, false, null);
        PLMWorkflowStatus workflowStatus = workflowStatusRepository.findOne(event.getPlmWorkflow().getCurrentStatus());
        applicationEventPublisher.publishEvent(new DCOEvents.DCOWorkflowStartedEvent(dco, event.getPlmWorkflow()));
        workflowEventService.workflowStart("DCO", event.getPlmWorkflow());
        workflowEventService.workflowActivityStart("DCO", event.getPlmWorkflow(), workflowStatus);
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'DCO'")
    public void dcoWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PLMDCO dco = (PLMDCO) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();

        dco = createRevisions(dco.getId(), false, false, fromStatus.getId());
        dco.setStatus(fromStatus.getName());
        dco.setStatusType(fromStatus.getType());
        update(dco);
        applicationEventPublisher.publishEvent(new DCOEvents.DCOWorkflowPromotedEvent(dco, plmWorkflow, fromStatus, toStatus));
        if (fromStatus.getType() == WorkflowStatusType.RELEASED || fromStatus.getType() == WorkflowStatusType.REJECTED) {
            releaseAttachedObject(dco, plmWorkflow);
        }
        workflowEventService.workflowActivityFinish("DCO", plmWorkflow, fromStatus);
        if (toStatus != null) {
            workflowEventService.workflowActivityStart("DCO", plmWorkflow, toStatus);
        }
        if (fromStatus.getType().equals(WorkflowStatusType.RELEASED)) {
            workflowEventService.workflowFinish("DCO", plmWorkflow);
        }
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'DCO'")
    public void dcoWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMDCO dco = (PLMDCO) event.getAttachedToObject();
        PLMWorkflowStatus toStatus = event.getToStatus();

        dco.setStatus(toStatus.getName());
        dco.setStatusType(toStatus.getType());
        dcoRepository.save(dco);
        applicationEventPublisher.publishEvent(new DCOEvents.DCOWorkflowDemotedEvent(dco, event.getPlmWorkflow(), event.getFromStatus(), toStatus));
        workflowEventService.workflowActivityFinishDemote("DCO", event.getPlmWorkflow(), toStatus);
        workflowEventService.workflowActivityStartDemote("DCO", event.getPlmWorkflow(), event.getFromStatus());
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'DCO'")
    public void dcoWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PLMDCO dco = (PLMDCO) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();

        dco = createRevisions(dco.getId(), false, true, fromStatus.getId());
        dco.setStatus(fromStatus.getName());
        dco.setStatusType(fromStatus.getType());
        update(dco);
        applicationEventPublisher.publishEvent(new DCOEvents.DCOWorkflowFinishedEvent(dco, plmWorkflow));
        if (fromStatus.getType() == WorkflowStatusType.RELEASED || fromStatus.getType() == WorkflowStatusType.REJECTED) {
            releaseAttachedObject(dco, plmWorkflow);
        }
        workflowEventService.workflowActivityFinish("DCO", plmWorkflow, fromStatus);
        workflowEventService.workflowFinish("DCO", plmWorkflow);
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'DCO'")
    public void dcoWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PLMDCO dco = (PLMDCO) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new DCOEvents.DCOWorkflowHoldEvent(dco, plmWorkflow, fromStatus));
        workflowEventService.workflowHold("DCO", plmWorkflow);
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'DCO'")
    public void dcoWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PLMDCO dco = (PLMDCO) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new DCOEvents.DCOWorkflowUnholdEvent(dco, plmWorkflow, fromStatus));
        workflowEventService.workflowUnhold("DCO", plmWorkflow);
    }

}
