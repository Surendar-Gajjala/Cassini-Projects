package com.cassinisys.plm.service.pqm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.NCREvents;
import com.cassinisys.plm.filtering.NCRCriteria;
import com.cassinisys.plm.filtering.NCRPredicateBuilder;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.NCRsDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.WorkflowStatusFlag;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.cm.ECORepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 04-06-2020.
 */
@Service
public class NCRService implements CrudService<PQMNCR, Integer> {

    @Autowired
    private NCRRepository ncrRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private NCRPredicateBuilder ncrPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private NCRFileRepository ncrFileRepository;
    @Autowired
    private NCRAttributeRepository ncrAttributeRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private NCRProblemItemRepository ncrProblemItemRepository;
    @Autowired
    private NCRRelatedItemRepository ncrRelatedItemRepository;
    @Autowired
    private QCRAggregateNCRRepository qcrAggregateNCRRepository;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#ncr,'create')")
    public PQMNCR create(PQMNCR ncr) {
        Integer workflowId = ncr.getWorkflow();
        ncr.setWorkflow(null);
        PQMNCR existPlanNumber = ncrRepository.findByNcrNumber(ncr.getNcrNumber());
        if (existPlanNumber != null) {
            throw new CassiniException(messageSource.getMessage(ncr.getNcrNumber() + " : " + "ncr_number_already_exists", null, "NCR Number already exist", LocaleContextHolder.getLocale()));
        }
        if (ncr.getReportedBy() == null) {
            ncr.setReportedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
        }

        ncr.setReportedDate(new Date());
        autoNumberService.saveNextNumber(ncr.getNcrType().getNumberSource().getId(), ncr.getNcrNumber());
        ncr = ncrRepository.save(ncr);

        if (workflowId != null) {
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(workflowId);
            if (wfDef != null) {
                PLMWorkflow workflow = workflowService.attachWorkflow(PLMObjectType.NCR, ncr.getId(), wfDef);
                ncr.setWorkflow(workflow.getId());
                ncr = ncrRepository.save(ncr);
            }
        }
        applicationEventPublisher.publishEvent(new NCREvents.NCRCreatedEvent(ncr));

        return ncr;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#ncr.id,'edit')")
    public PQMNCR update(PQMNCR ncr) {
        PQMNCR pqmncr = JsonUtils.cloneEntity(ncrRepository.findOne(ncr.getId()), PQMNCR.class);
        ncr = ncrRepository.save(ncr);
        applicationEventPublisher.publishEvent(new NCREvents.NCRBasicInfoUpdatedEvent(pqmncr, ncr));
        return ncr;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {

        PQMNCR pqmncr = ncrRepository.findOne(id);
        List<PQMQCRAggregateNCR> aggregateNCRList = qcrAggregateNCRRepository.findByNcr(id);
        if (aggregateNCRList.size() > 0) {
            throw new CassiniException(messageSource.getMessage("ncr_number_already_in_use", null, pqmncr.getNcrNumber() + " : NCR already in use", LocaleContextHolder.getLocale()));
        } else {
            applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
            ncrRepository.delete(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject,'view')")
    public PQMNCR get(Integer id) {
        PQMNCR ncr = ncrRepository.findOne(id);

        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(ncr.getId());
        ncr.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        ncr.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        ncr.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        return ncr;
    }

    @Transactional(readOnly = true)
    public NCRsDto getNCRDetails(Integer id) {
        PQMNCR pqmncr = ncrRepository.findOne(id);
        NCRsDto dto = new NCRsDto();
        dto.setId(pqmncr.getId());
        dto.setTitle(pqmncr.getTitle());
        dto.setObjectType(pqmncr.getObjectType().name());
        dto.setNcrNumber(pqmncr.getNcrNumber());
        dto.setDescription(pqmncr.getDescription());
        dto.setNcrType(pqmncr.getNcrType().getName());
        dto.setQualityAnalyst(personRepository.findOne(pqmncr.getQualityAnalyst()).getFullName());
        dto.setReportedBy(personRepository.findOne(pqmncr.getReportedBy()).getFullName());
        dto.setReportedDate(pqmncr.getReportedDate());
        dto.setFailureType(pqmncr.getFailureType());
        dto.setSeverity(pqmncr.getSeverity());
        dto.setStatus(pqmncr.getStatus());
        dto.setStatusType(pqmncr.getStatusType());
        dto.setDisposition(pqmncr.getDisposition());
        if (pqmncr.getWorkflow() != null) {
            PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(pqmncr.getWorkflow());
            dto.setOnHold(plmWorkflow.getOnhold());
        }
        dto.setCreatedBy(personRepository.findOne(pqmncr.getCreatedBy()).getFullName());
        dto.setCreatedDate(pqmncr.getCreatedDate());
        dto.setModifiedDate(pqmncr.getModifiedDate());
        dto.setModifiedBy(personRepository.findOne(pqmncr.getModifiedBy()).getFullName());
        dto.setModifiedDate(pqmncr.getModifiedDate());
        dto.setIsImplemented(pqmncr.getIsImplemented());
        dto.setReleased(pqmncr.getReleased());
        dto.setWorkflow(pqmncr.getWorkflow());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMNCR> getAll() {
        return ncrRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMNCR> findMultiple(List<Integer> ids) {
        return ncrRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<NCRsDto> getAllNcrs(Pageable pageable, NCRCriteria ncrCriteria) {
        Predicate predicate = ncrPredicateBuilder.build(ncrCriteria, QPQMNCR.pQMNCR);
        Page<PQMNCR> pqmncrs = ncrRepository.findAll(predicate, pageable);

        List<NCRsDto> plansDto = new LinkedList<>();
        pqmncrs.getContent().forEach(pqmncr -> {
            NCRsDto dto = new NCRsDto();
            dto.setId(pqmncr.getId());
            dto.setTitle(pqmncr.getTitle());
            dto.setObjectType(pqmncr.getObjectType().name());
            dto.setNcrNumber(pqmncr.getNcrNumber());
            dto.setDescription(pqmncr.getDescription());
            dto.setNcrType(pqmncr.getNcrType().getName());
            dto.setQualityAnalyst(personRepository.findOne(pqmncr.getQualityAnalyst()).getFullName());
            dto.setReportedBy(personRepository.findOne(pqmncr.getReportedBy()).getFullName());
            dto.setReportedDate(pqmncr.getReportedDate());
            dto.setFailureType(pqmncr.getFailureType());
            dto.setSeverity(pqmncr.getSeverity());
            dto.setStatus(pqmncr.getStatus());
            dto.setStatusType(pqmncr.getStatusType());
            dto.setDisposition(pqmncr.getDisposition());

            WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(pqmncr.getId());
            dto.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            dto.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            dto.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
            dto.setOnHold(workFlowStatusDto.getOnHold());
            dto.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            dto.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());

            dto.setCreatedBy(personRepository.findOne(pqmncr.getCreatedBy()).getFullName());
            dto.setCreatedDate(pqmncr.getCreatedDate());
            dto.setModifiedDate(pqmncr.getModifiedDate());
            dto.setModifiedBy(personRepository.findOne(pqmncr.getModifiedBy()).getFullName());
            dto.setModifiedDate(pqmncr.getModifiedDate());
            dto.setIsImplemented(pqmncr.getIsImplemented());
            dto.setReleased(pqmncr.getReleased());
            dto.setTagsCount(pqmncr.getTags().size());
            plansDto.add(dto);
        });

        return new PageImpl<NCRsDto>(plansDto, pageable, pqmncrs.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<PQMNCRFile> getNcrFiles(Integer id) {
        return ncrFileRepository.findByNcrAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(id);
    }

    @Transactional
    public PQMNCRAttribute createNcrAttribute(PQMNCRAttribute attribute) {
        return ncrAttributeRepository.save(attribute);
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public PQMNCRAttribute updateNcrAttribute(PQMNCRAttribute attribute) {
        PQMNCRAttribute oldValue = ncrAttributeRepository.findByNcrAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PQMNCRAttribute.class);
        attribute = ncrAttributeRepository.save(attribute);
        PQMNCR pqmncr = ncrRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new NCREvents.NCRAttributesUpdatedEvent(pqmncr, oldValue, attribute));
        return attribute;
    }

    @Transactional
    public PQMNCRProblemItem createProblemItem(Integer id, PQMNCRProblemItem problemItem) {
        problemItem = ncrProblemItemRepository.save(problemItem);
        PQMNCR ncr = ncrRepository.findOne(id);
        List<PQMNCRProblemItem> problemItems = new ArrayList<>();
        problemItems.add(problemItem);
        applicationEventPublisher.publishEvent(new NCREvents.NCRProblemItemAddedEvent(ncr, problemItems));
        return problemItem;
    }

    @Transactional
    public PQMNCRProblemItem updateProblemItem(Integer id, PQMNCRProblemItem problemItem) {
        PQMNCRProblemItem oldProblemItem = JsonUtils.cloneEntity(ncrProblemItemRepository.findOne(problemItem.getId()), PQMNCRProblemItem.class);
        problemItem = ncrProblemItemRepository.save(problemItem);
        PQMNCR ncr = ncrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new NCREvents.NCRProblemItemUpdatedEvent(ncr, oldProblemItem, problemItem));
        return problemItem;
    }

    @Transactional
    public List<PQMNCRProblemItem> createProblemItems(Integer id, List<PQMNCRProblemItem> problemItems) {
        problemItems = ncrProblemItemRepository.save(problemItems);
        PQMNCR ncr = ncrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new NCREvents.NCRProblemItemAddedEvent(ncr, problemItems));

        return problemItems;
    }

    @Transactional
    public void deleteProblemItem(Integer id, Integer problemItem) {
        PQMNCRProblemItem pqmprProblemItem = ncrProblemItemRepository.findOne(problemItem);
        PQMNCR ncr = ncrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new NCREvents.NCRProblemItemDeletedEvent(ncr, pqmprProblemItem));

        ncrProblemItemRepository.delete(problemItem);
    }

    @Transactional
    public List<PQMNCRProblemItem> getProblemItems(Integer id) {
        List<PQMNCRProblemItem> problemItems = ncrProblemItemRepository.findByNcr(id);
        return problemItems;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','relateditem')")
    public PQMNCRRelatedItem createRelatedItem(Integer id, PQMNCRRelatedItem problemItem) {
        problemItem = ncrRelatedItemRepository.save(problemItem);
        PQMNCR ncr = ncrRepository.findOne(id);
        List<PQMNCRRelatedItem> relatedItems = new ArrayList<>();
        relatedItems.add(problemItem);
        applicationEventPublisher.publishEvent(new NCREvents.NCRRelatedItemAddedEvent(ncr, relatedItems));
        return problemItem;
    }

    @Transactional
    public PQMNCRRelatedItem updateRelatedItem(Integer id, PQMNCRRelatedItem relatedItem) {
        PQMNCRRelatedItem oldRelatedItem = JsonUtils.cloneEntity(ncrRelatedItemRepository.findOne(relatedItem.getId()), PQMNCRRelatedItem.class);
        relatedItem = ncrRelatedItemRepository.save(relatedItem);
        PQMNCR ncr = ncrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new NCREvents.NCRRelatedItemUpdatedEvent(ncr, oldRelatedItem, relatedItem));
        return relatedItem;
    }

    @Transactional
    public List<PQMNCRRelatedItem> createRelatedItems(Integer id, List<PQMNCRRelatedItem> relatedItems) {
        relatedItems = ncrRelatedItemRepository.save(relatedItems);
        PQMNCR ncr = ncrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new NCREvents.NCRRelatedItemAddedEvent(ncr, relatedItems));
        return relatedItems;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','relateditem')")
    public void deleteRelatedItem(Integer id, Integer relatedItemId) {
        PQMNCRRelatedItem relatedItem = ncrRelatedItemRepository.findOne(relatedItemId);
        PQMNCR ncr = ncrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new NCREvents.NCRRelatedItemDeletedEvent(ncr, relatedItem));

        ncrRelatedItemRepository.delete(relatedItem);
    }

    @Transactional
    public List<PQMNCRRelatedItem> getRelatedItems(Integer id) {
        List<PQMNCRRelatedItem> relatedItems = ncrRelatedItemRepository.findByNcr(id);
        return relatedItems;
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getDetailsCount(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setProblemItems(ncrProblemItemRepository.findByNcr(id).size());
        detailsDto.setRelatedItems(ncrRelatedItemRepository.findByNcr(id).size());
        detailsDto.setItemFiles(ncrFileRepository.findByNcrAndFileTypeAndLatestTrueOrderByModifiedDateDesc(id, "FILE").size());
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        return detailsDto;
    }

    @Transactional(readOnly = true)
    public List<PQMNCR> findByProblemPart(Integer partId) {
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(partId);
        List<PQMNCRProblemItem> pqmncrProblemItems = ncrProblemItemRepository.findByMaterial(manufacturerPart);
        List<PQMNCR> pqmncrs = new ArrayList<>();
        for (PQMNCRProblemItem pqmncrProblemItem : pqmncrProblemItems) {
            pqmncrs.add(ncrRepository.findOne(pqmncrProblemItem.getNcr()));
        }
        return pqmncrs;
    }

    @EventListener(condition = "#event.attachedToType.name() == 'NCR'")
    public void ncrWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PQMNCR ncr = (PQMNCR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        applicationEventPublisher.publishEvent(new NCREvents.NCRWorkflowStartedEvent(ncr, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'NCR'")
    public void ncrWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PQMNCR ncr = (PQMNCR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        ncr.setStatus(fromStatus.getName());
        ncr.setStatusType(fromStatus.getType());
        update(ncr);
        applicationEventPublisher.publishEvent(new NCREvents.NCRWorkflowPromotedEvent(ncr, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'NCR'")
    public void ncrWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PQMNCR pqmncr = (PQMNCR) event.getAttachedToObject();
        PLMWorkflowStatus toStatus = event.getToStatus();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        pqmncr.setStatus(toStatus.getName());
        pqmncr.setStatusType(toStatus.getType());
        update(pqmncr);
        applicationEventPublisher.publishEvent(new NCREvents.NCRWorkflowDemotedEvent(pqmncr, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'NCR'")
    public void ncrWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PQMNCR ncr = (PQMNCR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        ncr.setStatus(fromStatus.getName());
        ncr.setStatusType(fromStatus.getType());
        ncr.setReleased(true);
        ncr.setReportedDate(new Date());
        update(ncr);
        applicationEventPublisher.publishEvent(new NCREvents.NCRWorkflowFinishedEvent(ncr, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'NCR'")
    public void ncrWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PQMNCR pqmncr = (PQMNCR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new NCREvents.NCRWorkflowHoldEvent(pqmncr, plmWorkflow, fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'NCR'")
    public void ncrWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PQMNCR pqmncr = (PQMNCR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new NCREvents.NCRWorkflowUnholdEvent(pqmncr, plmWorkflow, fromStatus));
    }
}
