package com.cassinisys.plm.service.cm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.DCOEvents;
import com.cassinisys.plm.event.DCREvents;
import com.cassinisys.plm.filtering.DCRCriteria;
import com.cassinisys.plm.filtering.DCRItemsCriteria;
import com.cassinisys.plm.filtering.DCRItemsPredicateBuilder;
import com.cassinisys.plm.filtering.DCRPredicateBuilder;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.cm.dto.DCRAffecteditemsDto;
import com.cassinisys.plm.model.cm.dto.DCRDto;
import com.cassinisys.plm.model.cm.dto.DCRRelatedItemDto;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.mobile.DCRDetails;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.plm.ItemFileRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkFlowStatusAssignmentRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
@Service
public class DCRService implements CrudService<PLMDCR, Integer> {

    @Autowired
    private DCRRepository dcrRepository;
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
    private DCRPredicateBuilder dcrPredicateBuilder;
    @Autowired
    private DCRAffectedItemRepository dcrAffectedItemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private DCRItemsPredicateBuilder dcrItemsPredicateBuilder;
    @Autowired
    private ItemFileRepository itemFileRepository;
    @Autowired
    private ChangeRelatedItemRepository changeRelatedItemRepository;
    @Autowired
    private DCORepository dcoRepository;
    @Autowired
    private ChangeFileRepository changeFileRepository;
    @Autowired
    private DCODCRRepository dcodcrRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ChangeTypeRepository changeTypeRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ChangeRequestRepository changeRequestRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#dcr,'create')")
    public PLMDCR create(PLMDCR dcr) {
        Integer workflowDef = null;
        if (dcr.getWorkflow() != null) {
            workflowDef = dcr.getWorkflow();
        }

        PLMDCR existDCR = dcrRepository.findByCrNumber(dcr.getCrNumber());
        if (existDCR != null) {
            String message = messageSource.getMessage("dcr_number_already_exists", null, "{0} DCR Number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existDCR.getCrNumber());
            throw new CassiniException(result);
        }

        dcr.setRequestedDate(new Date());
        dcr.setWorkflow(null);
        autoNumberService.saveNextNumber(dcr.getChangeClass().getAutoNumberSource().getId(), dcr.getCrNumber());
        PLMDCR plmdcr = dcrRepository.save(dcr);
        if (workflowDef != null) {
            attachDCRWorkflow(dcr.getId(), workflowDef);
        }
        applicationEventPublisher.publishEvent(new DCREvents.DCRCreatedEvent(dcr));
        return plmdcr;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#dcr.id,'edit')")
    public PLMDCR update(PLMDCR dcr) {
        PLMDCR oldDcr = JsonUtils.cloneEntity(dcrRepository.findOne(dcr.getId()), PLMDCR.class);
        dcr = dcrRepository.save(dcr);
        /* App events */
        applicationEventPublisher.publishEvent(new DCREvents.DCRBasicInfoUpdatedEvent(oldDcr, dcr));

        return dcr;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        PLMDCR plmecr = dcrRepository.findOne(id);
        List<PLMDCODCR> dcodcrs = dcodcrRepository.findByDcr(id);
        if (dcodcrs.size() > 0) {
            String message = messageSource.getMessage("dcr_number_already_in_use", null, "{0} DCR already in use", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", plmecr.getCrNumber());
            throw new CassiniException(result);
        } else {
            applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
            dcrRepository.delete(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject,'view')")
    public PLMDCR get(Integer id) {
        PLMDCR plmdcr = dcrRepository.findOne(id);

        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto = workflowService.setWorkflowStatusSettings(plmdcr.getId());
        plmdcr.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        plmdcr.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        plmdcr.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());

        return plmdcr;
    }

    @Transactional(readOnly = true)
    public DCRDetails getDCRDetails(Integer id) {
        PLMDCR plmdcr = dcrRepository.findOne(id);
        DCRDetails dcrDetails = new DCRDetails();
        dcrDetails.setId(plmdcr.getId());
        dcrDetails.setType(changeTypeRepository.findOne(plmdcr.getCrType()).getName());
        dcrDetails.setChangeAnalyst(personRepository.findOne(plmdcr.getCreatedBy()).getFullName());
        dcrDetails.setCrNumber(plmdcr.getCrNumber());
        dcrDetails.setChangeReasonType(plmdcr.getChangeReasonType());
        dcrDetails.setReasonForChange(plmdcr.getReasonForChange());
        dcrDetails.setDescriptionOfChange(plmdcr.getDescriptionOfChange());
        dcrDetails.setTitle(plmdcr.getTitle());
        dcrDetails.setStatus(plmdcr.getStatus());
        dcrDetails.setUrgency(plmdcr.getUrgency());
        dcrDetails.setOriginator(personRepository.findOne(plmdcr.getOriginator()).getFullName());
        dcrDetails.setRequestedBy(personRepository.findOne(plmdcr.getRequestedBy()).getFullName());
        dcrDetails.setModifiedBy(personRepository.findOne(plmdcr.getCreatedBy()).getFullName());
        dcrDetails.setRequestedDate(plmdcr.getRequestedDate());
        dcrDetails.setModifiedDate(plmdcr.getModifiedDate());
        dcrDetails.setWorkflow(plmdcr.getWorkflow());
        return dcrDetails;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMDCR> getAll() {
        return dcrRepository.findAll();
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMDCR> findMultiple(List<Integer> ids) {
        return dcrRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<DCRDto> getAllDCRS(Pageable pageable, DCRCriteria dcrCriteria) {
        Predicate predicate = dcrPredicateBuilder.build(dcrCriteria, QPLMDCR.pLMDCR);
        Page<PLMDCR> dcrs = dcrRepository.findAll(predicate, pageable);

        List<DCRDto> dcrDtos = new LinkedList<>();
        dcrs.getContent().forEach(dcr -> {
            DCRDto dto = new DCRDto();
            dto.setId(dcr.getId());
            dto.setCrType(changeTypeRepository.findOne(dcr.getCrType()).getName());
            dto.setCrNumber(dcr.getCrNumber());
            dto.setStatus(dcr.getStatus());
            dto.setObjectType(dcr.getObjectType().name());
            dto.setSubType(dcr.getChangeClass().getName());
            dto.setTitle(dcr.getTitle());
            dto.setDescriptionOfChange(dcr.getDescriptionOfChange());
            dto.setReasonForChange(dcr.getReasonForChange());
            dto.setProposedChanges(dcr.getProposedChanges());
            dto.setUrgency(dcr.getUrgency().toString());
            dto.setStatusType(dcr.getStatusType());
            dto.setApprovedDate(dcr.getApprovedDate());
            dto.setIsImplemented(dcr.getIsImplemented());
            dto.setIsApproved(dcr.getIsApproved());
            dto.setChangeReasonType(dcr.getChangeReasonType());
            dto.setChangeAnalyst(personRepository.findOne(dcr.getChangeAnalyst()).getFullName());
            dto.setOriginator(personRepository.findOne(dcr.getOriginator()).getFullName());
            dto.setRequestedBy(personRepository.findOne(dcr.getRequestedBy()).getFullName());
            dto.setRequestedDate(dcr.getRequestedDate());

            WorkflowStatusDTO workFlowStatusDto = workflowService.setWorkflowStatusSettings(dcr.getId());
            dto.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            dto.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            dto.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
            dto.setOnHold(workFlowStatusDto.getOnHold());
            dto.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            dto.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());

            dto.setTagsCount(dcr.getTags().size());
            dcrDtos.add(dto);
        });

        return new PageImpl<DCRDto>(dcrDtos, pageable, dcrs.getTotalElements());
    }


    @Transactional
    public PLMWorkflow attachDCRWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMDCR dcr = dcrRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (dcr != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.CHANGE, dcr.getId(), wfDef);
            dcr.setWorkflow(workflow.getId());
            dcrRepository.save(dcr);
        }
        return workflow;
    }

    @Transactional
    public PLMWorkflow attachNewWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMDCR dcr = dcrRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (dcr != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.CHANGE, dcr.getId(), wfDef);
            dcr.setWorkflow(workflow.getId());
            dcrRepository.save(dcr);
            applicationEventPublisher.publishEvent(new DCREvents.DCRWorkflowChangeEvent(dcr, null, workflow));
        }
        return workflow;
    }


    @Transactional
    public PLMDCRAffectedItem createAffectedItem(PLMDCRAffectedItem item) {
        PLMDCRAffectedItem affectedItem = dcrAffectedItemRepository.save(item);
        List<PLMDCRAffectedItem> affectedItems = new ArrayList<>();
        affectedItems.add(affectedItem);
        PLMDCR dcr = dcrRepository.findOne(item.getDcr());
        applicationEventPublisher.publishEvent(new DCREvents.DCRAffectedItemAddedEvent(dcr, affectedItems));
        return affectedItem;
    }

    @Transactional
    public List<PLMDCRAffectedItem> createAffectedItems(Integer id, List<PLMDCRAffectedItem> items) {
        items = dcrAffectedItemRepository.save(items);
        PLMDCR dcr = dcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new DCREvents.DCRAffectedItemAddedEvent(dcr, items));
        return items;
    }

    @Transactional(readOnly = true)
    public List<DCRAffecteditemsDto> getAffectedItem(Integer dcr) {
        List<DCRAffecteditemsDto> dtos = new ArrayList<>();
        List<PLMDCRAffectedItem> affectedItems = dcrAffectedItemRepository.findByDcr(dcr);
        for (PLMDCRAffectedItem affectedItem : affectedItems) {
            DCRAffecteditemsDto affecteditemsDto = new DCRAffecteditemsDto();
            affecteditemsDto.setId(affectedItem.getId());
            affecteditemsDto.setItem(affectedItem.getItem());
            affecteditemsDto.setNotes(affectedItem.getNotes());
            affecteditemsDto.setDcr(affectedItem.getDcr());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            affecteditemsDto.setItemNumber(item.getItemNumber());
            affecteditemsDto.setItemName(item.getItemName());
            affecteditemsDto.setItemType(item.getItemType().getName());
            affecteditemsDto.setDescription(item.getDescription());
            affecteditemsDto.setRevision(itemRevision.getRevision());
            affecteditemsDto.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
            affecteditemsDto.setPhase(itemRevision.getLifeCyclePhase().getPhase());
            dtos.add(affecteditemsDto);
        }

        return dtos;
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> getFilterBomItems(Pageable pageable, DCRItemsCriteria criteria) {
        Predicate predicate = dcrItemsPredicateBuilder.build(criteria, QPLMItem.pLMItem);
        Page<PLMItem> plmItem = itemRepository.findAll(predicate, pageable);
        for (PLMItem item : plmItem.getContent()) {
            PLMItemRevision item1 = itemRevisionRepository.findOne(item.getLatestRevision());
            List<PLMItemFile> itemFiles = itemFileRepository.findByItem(item1);
            item.setItemFiles(itemFiles);

        }
        return plmItem;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','relateditem')")
    public List<PLMChangeRelatedItem> createDcrRelatedItems(Integer dcr, List<PLMChangeRelatedItem> changeRelatedItems) {
        PLMDCR plmdcr = dcrRepository.findOne(dcr);
        PLMDCO plmdco = dcoRepository.findOne(dcr);
        for (PLMChangeRelatedItem relatedItem : changeRelatedItems) {
            changeRelatedItemRepository.save(relatedItem);
        }
        if (plmdcr != null) {
            applicationEventPublisher.publishEvent(new DCREvents.DCRRelatedItemAddedEvent(plmdcr, changeRelatedItems));
        } else if (plmdco != null) {
            applicationEventPublisher.publishEvent(new DCOEvents.DCORelatedItemAddedEvent(plmdco, changeRelatedItems));
        }
        return changeRelatedItems;
    }

    @Transactional(readOnly = true)
    public List<DCRRelatedItemDto> getDcrRelatedItem(Integer dcr) {
        List<DCRRelatedItemDto> dtos = new ArrayList<>();
        List<PLMChangeRelatedItem> changeRelatedItems = changeRelatedItemRepository.findByChange(dcr);
        for (PLMChangeRelatedItem changeRelatedItem : changeRelatedItems) {
            DCRRelatedItemDto relatedItemDto = new DCRRelatedItemDto();
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(changeRelatedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            relatedItemDto.setItem(item);
            relatedItemDto.setId(changeRelatedItem.getId());
            relatedItemDto.setItemRevision(itemRevision);
            dtos.add(relatedItemDto);
        }
        return dtos;
    }

    @Transactional
    public void deleteDcrAffectedItem(Integer dcr, Integer id) {
        List<PLMDCODCR> dcodcrs = dcodcrRepository.findByDcr(dcr);
        if (dcodcrs.size() > 0) {
            throw new CassiniException(messageSource.getMessage("affected_item_already_in_use", null, " Item already used in DCO(s)", LocaleContextHolder.getLocale()));
        } else {
            PLMDCRAffectedItem affectedItem = dcrAffectedItemRepository.findOne(id);
            PLMDCR plmdcr = dcrRepository.findOne(dcr);
            applicationEventPublisher.publishEvent(new DCREvents.DCRAffectedItemDeletedEvent(plmdcr, affectedItem));
            dcrAffectedItemRepository.delete(id);
        }
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','relateditem')")
    public void deleteDcrRelatedItem(Integer id, Integer item) {
        PLMDCR dcr = dcrRepository.findOne(id);
        PLMChangeRelatedItem affectedItem = changeRelatedItemRepository.findOne(item);
        applicationEventPublisher.publishEvent(new DCREvents.DCRRelatedItemDeletedEvent(dcr, affectedItem));
        changeRelatedItemRepository.delete(item);
    }


    private Boolean isValidMailAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    @Transactional(readOnly = true)
    public DetailsCount getDCRDetailsCount(Integer dcrId) {
        DetailsCount detailsCount = new DetailsCount();
        List<PLMChangeFile> changeFiles = changeFileRepository.findByChangeAndFileTypeAndLatestTrueOrderByModifiedDateDesc(dcrId, "FILE");
        List<PLMDCRAffectedItem> affectedItems = dcrAffectedItemRepository.findByDcr(dcrId);
        List<PLMChangeRelatedItem> relatedItems = changeRelatedItemRepository.findByChange(dcrId);
        detailsCount.setFiles(changeFiles.size());
        detailsCount.setRelatedItems(relatedItems.size());
        detailsCount.setAffectedItems(affectedItems.size());
        return detailsCount;
    }

    @Transactional(readOnly = true)
    public List<PLMDCR> findByAffectedItem(Integer item) {
        List<PLMDCR> plmdcrs = new ArrayList<>();
        List<PLMDCRAffectedItem> affectedItems = dcrAffectedItemRepository.findByItem(item);
        for (PLMDCRAffectedItem affectedItem : affectedItems) {
            plmdcrs.add(dcrRepository.findOne(affectedItem.getDcr()));
        }

        return plmdcrs;
    }


    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'DCR'")
    public void dcrWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PLMDCR dcr = (PLMDCR) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new DCREvents.DCRWorkflowStartedEvent(dcr, event.getPlmWorkflow()));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'DCR'")
    public void dcrWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PLMDCR dcr = (PLMDCR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        dcr.setStatus(fromStatus.getName());
        dcr.setStatusType(fromStatus.getType());
        if (fromStatus.getType() == WorkflowStatusType.REJECTED) {
            dcr.setApprovedDate(new Date());
        }
        dcrRepository.save(dcr);
        update(dcr);
        applicationEventPublisher.publishEvent(new DCREvents.DCRWorkflowPromotedEvent(dcr, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'DCR'")
    public void dcrWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMDCR dcr = (PLMDCR) event.getAttachedToObject();
        PLMWorkflowStatus toStatus = event.getToStatus();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();

        dcr.setStatus(toStatus.getName());
        dcr.setStatusType(toStatus.getType());
        dcrRepository.save(dcr);
        applicationEventPublisher.publishEvent(new DCREvents.DCRWorkflowDemotedEvent(dcr, plmWorkflow, event.getFromStatus(), toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'DCR'")
    public void dcrWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PLMDCR dcr = (PLMDCR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        dcr.setStatus(fromStatus.getName());
        dcr.setStatusType(fromStatus.getType());
        dcr.setApprovedDate(new Date());
        dcr.setIsApproved(true);
        dcrRepository.save(dcr);
        applicationEventPublisher.publishEvent(new DCREvents.DCRWorkflowFinishedEvent(dcr, plmWorkflow));

    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'DCR'")
    public void dcrWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PLMDCR dcr = (PLMDCR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new DCREvents.DCRWorkflowHoldEvent(dcr, plmWorkflow, fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'DCR'")
    public void dcrWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PLMDCR dcr = (PLMDCR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new DCREvents.DCRWorkflowUnholdEvent(dcr, plmWorkflow, fromStatus));
    }

    public List<String> getStatus() {
        return dcrRepository.getStatusIds();
    }

    public List<String> getChangeReasonType() {
        return dcrRepository.getChangeReasonTypeIds();
    }

    public List<String> getUrgency() {
        return dcrRepository.getUrgencyIds();
    }

    public List<Person> getChangeAnalysts() {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = dcrRepository.getChangeAnalystIds();
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }

    public List<Person> getOriginators() {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = dcrRepository.getOriginatorIds();
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }

    public List<Person> getRequestedBy() {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = dcrRepository.getRequesterIds();
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }
}
