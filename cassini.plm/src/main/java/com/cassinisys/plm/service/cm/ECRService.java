package com.cassinisys.plm.service.cm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.ECOEvents;
import com.cassinisys.plm.event.ECREvents;
import com.cassinisys.plm.filtering.*;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.cm.dto.ECRAffecteditemsDto;
import com.cassinisys.plm.model.cm.dto.ECRDto;
import com.cassinisys.plm.model.cm.dto.ECRRelatedItemDto;
import com.cassinisys.plm.model.cm.dto.RequestedItemDto;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.mobile.ECRDetails;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pqm.PQMPRProblemItem;
import com.cassinisys.plm.model.pqm.PQMProblemReport;
import com.cassinisys.plm.model.pqm.PQMQCRProblemItem;
import com.cassinisys.plm.model.pqm.ReporterType;
import com.cassinisys.plm.model.pqm.dto.ProblemReportsDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.plm.ItemFileRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pqm.PQMCustomerRepository;
import com.cassinisys.plm.repo.pqm.PRProblemItemRepository;
import com.cassinisys.plm.repo.pqm.ProblemReportRepository;
import com.cassinisys.plm.repo.pqm.QCRProblemItemRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.plm.ItemService;
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

import java.util.*;

/**
 * Created by subramanyam on 14-06-2020.
 */
@Service
public class ECRService implements CrudService<PLMECR, Integer> {

    @Autowired
    private ECRRepository ecrRepository;
    @Autowired
    private ChangeTypeRepository changeTypeRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private ECRPredicateBuilder ecrPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemFileRepository itemFileRepository;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ECRAffectedItemRepository ecrAffectedItemRepository;
    @Autowired
    private DCRItemsPredicateBuilder dcrItemsPredicateBuilder;
    @Autowired
    private ChangeRelatedItemRepository changeRelatedItemRepository;
    @Autowired
    private ChangeFileRepository changeFileRepository;
    @Autowired
    private QCRProblemItemRepository qcrProblemItemRepository;
    @Autowired
    private ECOChangeRequestPredicateBuilder ecoChangeRequestPredicateBuilder;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ECOECRRepository ecoecrRepository;
    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private AffectedItemRepository affectedItemRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ECRPRRepository ecrprRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private PQMCustomerRepository pqmCustomerRepository;
    @Autowired
    private PRProblemItemRepository prProblemItemRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ECOService ecoService;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private ChangeRequestRepository changeRequestRepository;
    @Autowired
    private ChangeRepository changeRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#plmecr,'create')")
    public PLMECR create(PLMECR plmecr) {
        Integer workflowDef = null;
        workflowDef = plmecr.getWorkflow();

        PLMECR existECRNumber = ecrRepository.findByCrNumber(plmecr.getCrNumber());
        if (existECRNumber != null) {
            throw new CassiniException(messageSource.getMessage("ecr_number_already_exists", null, plmecr.getCrNumber() + " : ECR Number already exist", LocaleContextHolder.getLocale()));
        }

        plmecr.setWorkflow(null);
        plmecr.setRequestedDate(new Date());
        autoNumberService.saveNextNumber(plmecr.getChangeClass().getAutoNumberSource().getId(), plmecr.getCrNumber());
        plmecr = ecrRepository.save(plmecr);
        if (workflowDef != null) {
            attachDCRWorkflow(plmecr, workflowDef);
        }

        if (plmecr.getQcr() != null) {
            List<PQMQCRProblemItem> problemItems = qcrProblemItemRepository.findByQcr(plmecr.getQcr().getId());
            for (PQMQCRProblemItem problemItem : problemItems) {
                PLMECRAffectedItem affectedItem = new PLMECRAffectedItem();
                affectedItem.setEcr(plmecr.getId());
                affectedItem.setItem(problemItem.getItem());
                affectedItem = ecrAffectedItemRepository.save(affectedItem);
            }
        }
        applicationEventPublisher.publishEvent(new ECREvents.ECRCreatedEvent(plmecr));
        return plmecr;
    }

    @Transactional
    public PLMWorkflow attachDCRWorkflow(PLMECR plmecr, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.CHANGE, plmecr.getId(), wfDef);
            plmecr.setWorkflow(workflow.getId());
            ecrRepository.save(plmecr);
        }
        return workflow;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#plmecr.id,'delete')")
    public PLMECR update(PLMECR plmecr) {
        PLMECR oldEcr = JsonUtils.cloneEntity(ecrRepository.findOne(plmecr.getId()), PLMECR.class);
        applicationEventPublisher.publishEvent(new ECREvents.ECRBasicInfoUpdatedEvent(oldEcr, plmecr));
        return ecrRepository.save(plmecr);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {

        PLMECR plmecr = ecrRepository.findOne(id);
        List<PLMECOECR> ecoecrs = ecoecrRepository.findByEcr(id);
        if (ecoecrs.size() > 0) {
            throw new CassiniException(messageSource.getMessage("ecr_number_already_in_use", null, plmecr.getCrNumber() + " : ECR already in use", LocaleContextHolder.getLocale()));
        } else {
            applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
            ecrRepository.delete(id);
        }
    }

    @Override
    @Transactional
    @PostAuthorize("hasPermission(returnObject,'view')")
    public PLMECR get(Integer id) {
        PLMECR plmecr = ecrRepository.findOne(id);
        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto = workflowService.setWorkflowStatusSettings(plmecr.getId());
        plmecr.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        plmecr.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        plmecr.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        return plmecr;
    }

    @Transactional(readOnly = true)
    public ECRDetails getEcrDetails(Integer id) {
        ECRDetails ecrDetails = new ECRDetails();
        PLMECR plmecr = ecrRepository.findOne(id);
        ecrDetails.setId(plmecr.getId());
        ecrDetails.setCrNumber(plmecr.getCrNumber());
        ecrDetails.setType(changeTypeRepository.findOne(plmecr.getCrType()).getName());
        ecrDetails.setTitle(plmecr.getTitle());
        ecrDetails.setDescriptionOfChange(plmecr.getDescriptionOfChange());
        ecrDetails.setChangeReasonType(plmecr.getChangeReasonType());
        ecrDetails.setReasonForChange(plmecr.getReasonForChange());
        ecrDetails.setProposedChanges(plmecr.getProposedChanges());
        ecrDetails.setStatus(plmecr.getStatus());
        ecrDetails.setUrgency(plmecr.getUrgency());
        ecrDetails.setChangeAnalyst(personRepository.findOne(plmecr.getChangeAnalyst()).getFullName());
        ecrDetails.setOriginator(personRepository.findOne(plmecr.getOriginator()).getFullName());
        if (plmecr.getRequesterType().equals(RequesterType.INTERNAL) && plmecr.getRequestedBy() != null) {
            Person requestedBy = personRepository.findOne(plmecr.getRequestedBy());
            ecrDetails.setRequestedBy(requestedBy.getFirstName());
        } else if (plmecr.getRequesterType().equals(RequesterType.CUSTOMER) && plmecr.getRequestedBy() != null) {
            ecrDetails.setRequestedBy(pqmCustomerRepository.findOne(plmecr.getRequestedBy()).getName());
        } else {
            ecrDetails.setRequestedBy(plmecr.getOtherRequested());
        }
        ecrDetails.setModifiedBy(personRepository.findOne(plmecr.getModifiedBy()).getFullName());
        ecrDetails.setRequestedDate(plmecr.getRequestedDate());
        ecrDetails.setModifiedDate(plmecr.getModifiedDate());
        ecrDetails.setWorkflow(plmecr.getWorkflow());
        return ecrDetails;
    }

    @Override
    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMECR> getAll() {
        return ecrRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMECR> findMultiple(List<Integer> ids) {
        return ecrRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<ECRDto> getAllECRs(Pageable pageable, DCOCriteria dcoCriteria) {
        Predicate predicate = ecrPredicateBuilder.build(dcoCriteria, QPLMECR.pLMECR);
        Page<PLMECR> ecrs = ecrRepository.findAll(predicate, pageable);

        List<ECRDto> ecrDtos = new LinkedList<>();
        ecrs.getContent().forEach(ecr -> {
            ECRDto dto = new ECRDto();
            dto.setId(ecr.getId());
            dto.setCrNumber(ecr.getCrNumber());
            dto.setObjectType(ecr.getObjectType().name());
            dto.setSubType(ecr.getChangeClass().getName());
            dto.setDescriptionOfChange(ecr.getDescriptionOfChange());
            dto.setProposedChanges(ecr.getProposedChanges());
            dto.setReasonForChange(ecr.getReasonForChange());
            Person person = personRepository.findOne(ecr.getChangeAnalyst());
            dto.setChangeAnalyst(person.getFullName());
            dto.setOriginator(personRepository.findOne(ecr.getOriginator()).getFullName());
            if (ecr.getRequesterType().equals(RequesterType.INTERNAL) && ecr.getRequestedBy() != null) {
                Person requestedBy = personRepository.findOne(ecr.getRequestedBy());
                dto.setRequestedBy(requestedBy.getFullName());
            } else if (ecr.getRequesterType().equals(RequesterType.CUSTOMER) && ecr.getRequestedBy() != null) {
                dto.setRequestedBy(pqmCustomerRepository.findOne(ecr.getRequestedBy()).getName());
            } else {
                dto.setRequestedBy(ecr.getOtherRequested());
            }
            dto.setRequesterType(ecr.getRequesterType());
            dto.setType(changeTypeRepository.findOne(ecr.getCrType()).getName());
            dto.setReasonForChange(ecr.getReasonForChange());
            dto.setTitle(ecr.getTitle());
            dto.setUrgency(ecr.getUrgency());
            dto.setStatus(ecr.getStatus());
            dto.setStatusType(ecr.getStatusType());
            dto.setRequestedDate(ecr.getRequestedDate());
            dto.setModifiedBy(personRepository.findOne(ecr.getModifiedBy()).getFullName());
            dto.setCreatedBy(personRepository.findOne(ecr.getCreatedBy()).getFullName());
            dto.setModifiedDate(ecr.getModifiedDate());
            dto.setCreatedDate(ecr.getCreatedDate());
            dto.setIsImplemented(ecr.getIsImplemented());
            dto.setIsApproved(ecr.getIsApproved());
            dto.setChangeReasonType(ecr.getChangeReasonType());

            WorkflowStatusDTO workFlowStatusDto = workflowService.setWorkflowStatusSettings(ecr.getId());
            dto.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            dto.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            dto.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
            dto.setOnHold(workFlowStatusDto.getOnHold());
            dto.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            dto.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());
            dto.setTagsCount(ecr.getTags().size());
            ecrDtos.add(dto);
        });

        return new PageImpl<ECRDto>(ecrDtos, pageable, ecrs.getTotalElements());
    }

    @Transactional
    public PLMWorkflow attachDCRWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMECR ecr = ecrRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (ecr != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.CHANGE, ecr.getId(), wfDef);
            ecr.setWorkflow(workflow.getId());
            ecrRepository.save(ecr);
            applicationEventPublisher.publishEvent(new ECREvents.ECRWorkflowChangeEvent(ecr, null, workflow));
        }
        return workflow;
    }

    @Transactional
    public PLMECRAffectedItem createAffectedItem(PLMECRAffectedItem item) {
        item = ecrAffectedItemRepository.save(item);
        PLMECR plmecr = ecrRepository.findOne(item.getEcr());
        List<PLMECRAffectedItem> affectedItems = new ArrayList<>();
        affectedItems.add(item);
        applicationEventPublisher.publishEvent(new ECREvents.ECRAffectedItemAddedEvent(plmecr, affectedItems));
        return item;
    }

    @Transactional
    public PLMECRAffectedItem updateAffectedItem(PLMECRAffectedItem item) {
        item = ecrAffectedItemRepository.save(item);
        return item;
    }

    @Transactional
    public List<PLMECRAffectedItem> createAffectedItems(Integer id, List<PLMECRAffectedItem> items) {
        items = ecrAffectedItemRepository.save(items);
        PLMECR plmecr = ecrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new ECREvents.ECRAffectedItemAddedEvent(plmecr, items));
        return items;
    }

    @Transactional(readOnly = true)
    public List<ECRAffecteditemsDto> getAffectedItem(Integer ecr) {
        PLMECR plmecr = ecrRepository.findOne(ecr);
        List<ECRAffecteditemsDto> dtos = new ArrayList<>();
        List<PLMECRAffectedItem> affectedItems = ecrAffectedItemRepository.findByEcr(ecr);
        for (PLMECRAffectedItem affectedItem : affectedItems) {
            ECRAffecteditemsDto affecteditemsDto = new ECRAffecteditemsDto();

            if (plmecr.getQcr() != null) {
                PQMQCRProblemItem problemItem = qcrProblemItemRepository.findByQcrAndItem(plmecr.getQcr().getId(), affectedItem.getItem());
                if (problemItem != null) {
                    affecteditemsDto.setQcrItem(true);
                }
            }
            if (affectedItem.getProblemReports().length > 0) {
                List<Integer> prIds = new ArrayList<>();
                for (int i = 0; i < affectedItem.getProblemReports().length; i++) {
                    prIds.add(affectedItem.getProblemReports()[i]);
                }
                affecteditemsDto.getProblemReportList().addAll(problemReportRepository.findByIdIn(prIds));
            }

            affecteditemsDto.setId(affectedItem.getId());
            affecteditemsDto.setItem(affectedItem.getItem());
            affecteditemsDto.setNotes(affectedItem.getNotes());
            affecteditemsDto.setEcr(affectedItem.getEcr());
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
    public List<PLMChangeRelatedItem> createEcrRelatedItems(Integer ecrId, List<PLMChangeRelatedItem> changeRelatedItems) {
        changeRelatedItems = changeRelatedItemRepository.save(changeRelatedItems);
        PLMECR plmecr = ecrRepository.findOne(ecrId);
        applicationEventPublisher.publishEvent(new ECREvents.ECRRelatedItemsAddedEvent(plmecr, changeRelatedItems));
        return changeRelatedItems;
    }

    @Transactional(readOnly = true)
    public List<ECRRelatedItemDto> getEcrRelatedItem(Integer ecr) {
        List<ECRRelatedItemDto> dtos = new ArrayList<>();
        List<PLMChangeRelatedItem> changeRelatedItems = changeRelatedItemRepository.findByChange(ecr);
        for (PLMChangeRelatedItem changeRelatedItem : changeRelatedItems) {
            ECRRelatedItemDto relatedItemDto = new ECRRelatedItemDto();
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
    public void deleteEcrAffectedItem(Integer id) {
        PLMECRAffectedItem affectedItem = ecrAffectedItemRepository.findOne(id);
        PLMECR plmecr = ecrRepository.findOne(affectedItem.getEcr());
        applicationEventPublisher.publishEvent(new ECREvents.ECRAffectedItemDeletedEvent(plmecr, affectedItem));
        ecrAffectedItemRepository.delete(id);
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','relateditem')")
    public void deleteEcrRelatedItem(Integer id) {
        PLMChangeRelatedItem relatedItem = changeRelatedItemRepository.findOne(id);
        PLMECR plmecr = ecrRepository.findOne(relatedItem.getChange());
        applicationEventPublisher.publishEvent(new ECREvents.ECRRelatedItemsDeletedEvent(plmecr, relatedItem));

        changeRelatedItemRepository.delete(id);
    }

    public List<Person> getChangeAnalysts() {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = ecrRepository.getChangeAnalystIds();
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }

    public List<String> getStatus() {
        return ecrRepository.getStatusIds();
    }

    public List<String> getUrgency() {
        return ecrRepository.getUrgencyIds();
    }

    public List<Person> getOriginators() {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = ecrRepository.getOriginatorIds();
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }

    public List<Person> getRequesters() {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = ecrRepository.getRequesterIds();
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }

    public List<String> getChangeReasonType() {
        return ecrRepository.getChangeReasonTypeIds();
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getEcrDetailsCount(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setAffectedItems(ecrAffectedItemRepository.findByEcr(id).size());
        detailsDto.setRelatedItems(changeRelatedItemRepository.findByChange(id).size());
        detailsDto.setItemFiles(changeFileRepository.findByChangeAndFileTypeAndLatestTrueOrderByModifiedDateDesc(id, "FILE").size());
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        detailsDto.setProblemReports(ecrprRepository.findByEcr(id).size());
        return detailsDto;
    }

    @Transactional(readOnly = true)
    public Page<PLMECR> getFilteredEcrs(Pageable pageable, ECOChangeRequestCriteria criteria) {
        Predicate predicate = ecoChangeRequestPredicateBuilder.build(criteria, QPLMECR.pLMECR);
        Page<PLMECR> ecrs = ecrRepository.findAll(predicate, pageable);
        ecrs.forEach(plmecr -> {
            List<PLMECRAffectedItem> affectedItems = ecrAffectedItemRepository.findByEcr(plmecr.getId());
            affectedItems.forEach(item -> {
                RequestedItemDto requestedItemDto = new RequestedItemDto();
                requestedItemDto.setEcrAffectedItem(item);
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
                plmecr.getRequestedItemDtos().add(requestedItemDto);
            });

        });

        return ecrs;
    }


    @Transactional
    public List<PLMECR> createChangeReqItems(Integer ecoId, List<PLMECR> plmecrs) {
        PLMECO eco = ecoRepository.findOne(ecoId);
        HashMap<Integer, List<Integer>> dcrMap = new HashMap<>();
        plmecrs.forEach(plmdcr -> {
            plmdcr.getRequestedItemDtos().forEach(requestedItemDto -> {
                List<Integer> qcrIds = dcrMap.containsKey(requestedItemDto.getEcrAffectedItem().getItem()) ? dcrMap.get(requestedItemDto.getEcrAffectedItem().getItem()) : new ArrayList<Integer>();
                if (qcrIds.indexOf(requestedItemDto.getEcrAffectedItem().getItem()) == -1) {
                    qcrIds.add(plmdcr.getId());
                }
                dcrMap.put(requestedItemDto.getEcrAffectedItem().getItem(), qcrIds);
            });
        });

        HashMap<Integer, RequestedItemDto> affectedItemHashMap = new HashMap<>();

        for (PLMECR ecr : plmecrs) {
            List<RequestedItemDto> itemDtos = ecr.getRequestedItemDtos();
            for (RequestedItemDto requestedItemDto : itemDtos) {

                List<PLMAffectedItem> affectedItems = affectedItemRepository.findByChange(ecoId);
                Boolean itemAlreadyExist = false;
                PLMItemRevision ecrItemRevision = itemRevisionRepository.findOne(requestedItemDto.getEcrAffectedItem().getItem());
                for (PLMAffectedItem affectedItem : affectedItems) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                    if (ecrItemRevision.getItemMaster().equals(itemRevision.getItemMaster())) {
                        itemAlreadyExist = true;

                        Integer[] ecrIds = affectedItem.getChangeRequests();
                        List<Integer> changeRequestIds = new ArrayList<>();
                        if (ecrIds.length > 0) {
                            for (int i = 0; i < ecrIds.length; i++) {
                                changeRequestIds.add(ecrIds[i]);
                            }
                        }

                        if (changeRequestIds.indexOf(ecr.getId()) == -1) {
                            changeRequestIds.add(ecr.getId());
                        }
                        Integer[] intArray = new Integer[changeRequestIds.size()];
                        intArray = changeRequestIds.toArray(intArray);
                        affectedItem.setChangeRequests(intArray);

                        affectedItem = affectedItemRepository.save(affectedItem);
                    }
                }

                if (!itemAlreadyExist) {
                    RequestedItemDto alreadySaved = affectedItemHashMap.get(requestedItemDto.getEcrAffectedItem().getItem());
                    if (alreadySaved == null) {
                        PLMAffectedItem affectedItem = new PLMAffectedItem();
                        List<Integer> dcrs = dcrMap.get(requestedItemDto.getEcrAffectedItem().getItem());
                        Integer[] intArray = new Integer[dcrs.size()];
                        intArray = dcrs.toArray(intArray);
                        affectedItem.setChangeRequests(intArray);
                        affectedItem.setChange(ecoId);
                        affectedItem.setFromRevision(requestedItemDto.getFromRevision());
                        affectedItem.setToRevision(requestedItemDto.getToRevision());
                        affectedItem.setItem(requestedItemDto.getEcrAffectedItem().getItem());
                        affectedItem = affectedItemRepository.save(affectedItem);
                        ecoService.addAffectedItemToWorkflowEventData(eco, affectedItem);
                        if (eco != null && eco.getRevisionsCreated()) {
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
                            affectedItem = affectedItemRepository.save(affectedItem);
                        }
                        affectedItemHashMap.put(requestedItemDto.getEcrAffectedItem().getItem(), requestedItemDto);
                    }
                }

            }
            PLMECOECR plmdcodcr = new PLMECOECR();
            plmdcodcr.setEco(ecoId);
            plmdcodcr.setEcr(ecr.getId());
            ecoecrRepository.save(plmdcodcr);
        }
        applicationEventPublisher.publishEvent(new ECOEvents.ECOChangeRequestAddedEvent(eco, plmecrs));
        return plmecrs;
    }


    @Transactional(readOnly = true)
    public List<ECRDto> getChangeReqItems(Integer id) {
        List<PLMECOECR> plmecoecrs = ecoecrRepository.findByEco(id);
        List<ECRDto> ecrDtos = new LinkedList<>();
        plmecoecrs.forEach(plmecoecr -> {
            PLMECR plmecr = ecrRepository.findOne(plmecoecr.getEcr());
            ECRDto dto = new ECRDto();
            dto.setId(plmecoecr.getId());
            dto.setChangeRequest(plmecoecr.getEcr());
            if (plmecr.getCrType() != null) {
                dto.setType(changeTypeRepository.findOne(plmecr.getCrType()).getName());
            }
            dto.setCrNumber(plmecr.getCrNumber());
            dto.setStatus(plmecr.getStatus());
            dto.setStatusType(plmecr.getStatusType());
            dto.setTitle(plmecr.getTitle());
            dto.setDescriptionOfChange(plmecr.getDescriptionOfChange());
            dto.setReasonForChange(plmecr.getReasonForChange());
            dto.setProposedChanges(plmecr.getProposedChanges());
            dto.setUrgency(plmecr.getUrgency());
            dto.setUrgencyPrint(plmecr.getUrgency());
            dto.setChangeAnalyst(personRepository.findOne(plmecr.getChangeAnalyst()).getFullName());
            dto.setOriginator(personRepository.findOne(plmecr.getOriginator()).getFullName());
            if (plmecr.getRequesterType().equals(RequesterType.INTERNAL) && plmecr.getRequestedBy() != null) {
                Person requestedBy = personRepository.findOne(plmecr.getRequestedBy());
                dto.setRequestedBy(requestedBy.getFirstName());
            } else if (plmecr.getRequesterType().equals(RequesterType.CUSTOMER) && plmecr.getRequestedBy() != null) {
                dto.setRequestedBy(pqmCustomerRepository.findOne(plmecr.getRequestedBy()).getName());
            } else {
                dto.setRequestedBy(plmecr.getOtherRequested());
            }
            dto.setRequesterType(plmecr.getRequesterType());
            dto.setRequestedDate(plmecr.getRequestedDate());
            ecrDtos.add(dto);
        });

        return ecrDtos;
    }

    @Transactional(readOnly = true)
    public List<PLMECR> findByAffectedItem(Integer item) {
        List<PLMECR> plmecrs = new ArrayList<>();
        List<PLMECRAffectedItem> affectedItems = ecrAffectedItemRepository.findByItem(item);
        for (PLMECRAffectedItem affectedItem : affectedItems) {
            plmecrs.add(ecrRepository.findOne(affectedItem.getEcr()));
        }

        return plmecrs;
    }


    @Transactional
    public void deleteEcoChangeRequest(Integer ecoId, Integer changeId) {
        PLMECOECR plmecoecr = ecoecrRepository.findOne(changeId);
        List<PLMAffectedItem> affectedItems = affectedItemRepository.findByChange(ecoId);
        affectedItems.forEach(affectedItem -> {
            if (affectedItem.getChangeRequests().length > 0) {
                List<Integer> requestIds = new ArrayList<Integer>();
                for (int i = 0; i < affectedItem.getChangeRequests().length; i++) {
                    requestIds.add(affectedItem.getChangeRequests()[i]);
                }

                if (requestIds.size() == 1 && requestIds.indexOf(plmecoecr.getEcr()) != -1) {
                    affectedItemRepository.delete(affectedItem.getId());
                }
                if (requestIds.size() > 1 && requestIds.indexOf(plmecoecr.getEcr()) != -1) {
                    List<Integer> ecrIds = requestIds;
                    ecrIds.remove(ecrIds.indexOf(plmecoecr.getEcr()));
                    Integer[] intArray = new Integer[ecrIds.size()];
                    intArray = ecrIds.toArray(intArray);
                    affectedItem.setChangeRequests(intArray);
                    affectedItemRepository.save(affectedItem);
                }
            }
        });
        PLMECO plmeco = ecoRepository.findOne(ecoId);
        PLMECR plmecr = ecrRepository.findOne(plmecoecr.getEcr());
        applicationEventPublisher.publishEvent(new ECOEvents.ECOChangeRequestDeletedEvent(plmeco, plmecr));
        ecoecrRepository.delete(changeId);
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'ECR'")
    public void ecrWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PLMECR ecr = (PLMECR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        applicationEventPublisher.publishEvent(new ECREvents.ECRWorkflowStartedEvent(ecr, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'ECR'")
    public void ecrWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PLMECR ecr = (PLMECR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        ecr.setStatus(fromStatus.getName());
        ecr.setStatusType(fromStatus.getType());
        if (fromStatus.getType() == WorkflowStatusType.REJECTED) {
            ecr.setApprovedDate(new Date());
        }
        applicationEventPublisher.publishEvent(new ECREvents.ECRWorkflowPromotedEvent(ecr, plmWorkflow, fromStatus, toStatus));
        update(ecr);
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'ECR'")
    public void ecrWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMECR ecr = (PLMECR) event.getAttachedToObject();
        PLMWorkflowStatus toStatus = event.getToStatus();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        ecr.setStatus(toStatus.getName());
        ecr.setStatusType(toStatus.getType());
        ecrRepository.save(ecr);
        applicationEventPublisher.publishEvent(new ECREvents.ECRWorkflowDemotedEvent(ecr, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'ECR'")
    public void ecrWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PLMECR ecr = (PLMECR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        ecr.setStatus(fromStatus.getName());
        ecr.setStatusType(fromStatus.getType());
        ecr.setApprovedDate(new Date());
        ecr.setIsApproved(true);
        update(ecr);
        applicationEventPublisher.publishEvent(new ECREvents.ECRWorkflowFinishedEvent(ecr, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'ECR'")
    public void ecrWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PLMECR ecr = (PLMECR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ECREvents.ECRWorkflowHoldEvent(ecr, plmWorkflow, fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'ECR'")
    public void ecrWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PLMECR ecr = (PLMECR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ECREvents.ECRWorkflowUnholdEvent(ecr, plmWorkflow, fromStatus));
    }

    @Transactional
    public List<PLMECRPR> createECRProblemReports(Integer ecrId, List<PLMECRPR> ecrPrs) {
        List<PLMECRPR> ecrprList = new ArrayList<>();
        
        PLMECR plmecr = ecrRepository.findOne(ecrId);
        for (PLMECRPR ecrPr : ecrPrs) {
            PLMECRPR existEcrPr = ecrprRepository.findByEcrAndProblemReport(ecrPr.getEcr(), ecrPr.getProblemReport());
            if (existEcrPr == null) {
                ecrPr = ecrprRepository.save(ecrPr);
                addPrProblemItemsToEcrAffectedItems(ecrPr);
                ecrprList.add(ecrPr);
            }
        }

        HashMap<Integer, List<Integer>> prMap = new HashMap<>();
        ecrPrs.forEach(ecrPr -> {
            List<PQMPRProblemItem> problemItems = prProblemItemRepository.findByProblemReport(ecrPr.getProblemReport());
            problemItems.forEach(problemItem -> {
                List<Integer> prIds = prMap.containsKey(problemItem.getItem()) ? prMap.get(problemItem.getItem()) : new ArrayList<Integer>();
                if (prIds.indexOf(problemItem.getItem()) == -1) {
                    prIds.add(ecrPr.getProblemReport());
                }
                prMap.put(problemItem.getItem(), prIds);
            });
        });

        HashMap<Integer, PQMPRProblemItem> affectedItemHashMap = new HashMap<>();

        for (PLMECRPR ecrPr : ecrPrs) {
            List<PQMPRProblemItem> problemItems = prProblemItemRepository.findByProblemReport(ecrPr.getProblemReport());
            for (PQMPRProblemItem problemItem : problemItems) {

                List<PLMECRAffectedItem> affectedItems = ecrAffectedItemRepository.findByEcr(ecrPr.getEcr());
                Boolean itemAlreadyExist = false;
                PLMItemRevision ecrItemRevision = itemRevisionRepository.findOne(problemItem.getItem());
                for (PLMECRAffectedItem affectedItem : affectedItems) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                    if (ecrItemRevision.getItemMaster().equals(itemRevision.getItemMaster())) {
                        itemAlreadyExist = true;

                        Integer[] prIds = affectedItem.getProblemReports();
                        List<Integer> changeRequestIds = new ArrayList<>();
                        if (prIds.length > 0) {
                            for (int i = 0; i < prIds.length; i++) {
                                changeRequestIds.add(prIds[i]);
                            }
                        }

                        if (changeRequestIds.indexOf(ecrPr.getProblemReport()) == -1) {
                            changeRequestIds.add(ecrPr.getProblemReport());
                        }
                        Integer[] intArray = new Integer[changeRequestIds.size()];
                        intArray = changeRequestIds.toArray(intArray);
                        affectedItem.setProblemReports(intArray);

                        affectedItem = ecrAffectedItemRepository.save(affectedItem);
                    }
                }

                if (!itemAlreadyExist) {
                    PQMPRProblemItem alreadySaved = affectedItemHashMap.get(problemItem.getItem());
                    if (alreadySaved == null) {
                        PLMECRAffectedItem affectedItem = new PLMECRAffectedItem();
                        List<Integer> prs = prMap.get(problemItem.getItem());
                        Integer[] intArray = new Integer[prs.size()];
                        intArray = prs.toArray(intArray);
                        affectedItem.setProblemReports(intArray);
                        affectedItem.setEcr(ecrId);
                        affectedItem.setItem(problemItem.getItem());
                        affectedItem = ecrAffectedItemRepository.save(affectedItem);
                        affectedItemHashMap.put(problemItem.getItem(), problemItem);
                    }
                }
            }
        }
        applicationEventPublisher.publishEvent(new ECREvents.ECRProblemReportAddedEvent(plmecr, ecrprList));
        return ecrprList;
    }

    private void addPrProblemItemsToEcrAffectedItems(PLMECRPR ecrPr) {
        List<PQMPRProblemItem> problemItems = prProblemItemRepository.findByProblemReport(ecrPr.getProblemReport());

        for (PQMPRProblemItem problemItem : problemItems) {
            PLMECRAffectedItem affectedItem = ecrAffectedItemRepository.findByEcrAndItem(ecrPr.getEcr(), problemItem.getItem());
            if (affectedItem == null) {
                PLMECRAffectedItem plmecrAffectedItem = new PLMECRAffectedItem();
                plmecrAffectedItem.setEcr(ecrPr.getEcr());
                plmecrAffectedItem.setItem(problemItem.getItem());
                plmecrAffectedItem = ecrAffectedItemRepository.save(plmecrAffectedItem);
            }
        }
    }

    @Transactional
    public PLMECRPR createECRProblemReport(Integer ecrId, PLMECRPR ecrPr) {
        List<PLMECRPR> ecrprList = new ArrayList<>();

        PLMECRPR existEcrPr = ecrprRepository.findByEcrAndProblemReport(ecrPr.getEcr(), ecrPr.getProblemReport());
        if (existEcrPr == null) {
            ecrPr = ecrprRepository.save(ecrPr);
            addPrProblemItemsToEcrAffectedItems(ecrPr);
            ecrprList.add(ecrPr);
        }

        return ecrPr;
    }

    @Transactional(readOnly = true)
    public List<ProblemReportsDto> getECRProblemReports(Integer ecrId) {
        List<Integer> prIds = ecrprRepository.getPrIdsByEcr(ecrId);
        List<ProblemReportsDto> dtoList = new ArrayList<>();
        if (prIds.size() > 0) {
            List<PQMProblemReport> problemReports = problemReportRepository.findByIdIn(prIds);
            problemReports.forEach(problemReport -> {
                ProblemReportsDto dto = new ProblemReportsDto();
                dto.setId(problemReport.getId());
                dto.setProblem(problemReport.getProblem());
                dto.setPrNumber(problemReport.getPrNumber());
                dto.setDescription(problemReport.getDescription());
                if (problemReport.getProduct() != null) {
                    PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(problemReport.getProduct());
                    dto.setProduct(itemRepository.findOne(plmItemRevision.getItemMaster()).getItemName());
                    dto.setRevision(plmItemRevision.getRevision());
                }
                dto.setPrType(problemReport.getPrType().getName());
                dto.setStepsToReproduce(problemReport.getStepsToReproduce());
                dto.setQualityAnalyst(personRepository.findOne(problemReport.getQualityAnalyst()).getFullName());
                if (problemReport.getReportedBy() != null && problemReport.getReporterType().equals(ReporterType.CUSTOMER)) {
                    dto.setReportedBy(pqmCustomerRepository.findOne(problemReport.getReportedBy()).getName());
                } else if (problemReport.getReportedBy() != null && problemReport.getReporterType().equals(ReporterType.INTERNAL)) {
                    dto.setReportedBy(personRepository.findOne(problemReport.getReportedBy()).getFullName());
                } else if (problemReport.getReportedBy() != null && problemReport.getReporterType().equals(ReporterType.SUPPLIER)) {
                    dto.setReportedBy(personRepository.findOne(problemReport.getReportedBy()).getFullName());
                }
                dto.setOtherReported(problemReport.getOtherReported());
                dto.setReleased(problemReport.getReleased());
                dto.setReportedDate(problemReport.getReportedDate());
                dto.setReporterType(problemReport.getReporterType());
                dto.setFailureType(problemReport.getFailureType());
                dto.setSeverity(problemReport.getSeverity());
                dto.setStatus(problemReport.getStatus());
                dto.setStatusType(problemReport.getStatusType());
                dto.setDisposition(problemReport.getDisposition());
                dto.setIsImplemented(problemReport.getIsImplemented());
                if (problemReport.getWorkflow() != null) {
                    PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(problemReport.getWorkflow());
                    if (plmWorkflow != null) {
                        dto.setOnHold(plmWorkflow.getOnhold());
                    }
                }
                dto.setProblemReport(problemReport.getId());
                dto.setEcr(ecrId);
                dtoList.add(dto);
            });
        }

        return dtoList;
    }

    @Transactional
    public void deleteECRProblemReport(Integer ecrId, Integer prId) {
        PLMECRPR plmecrpr = ecrprRepository.findByEcrAndProblemReport(ecrId, prId);
        PLMECR ecr = ecrRepository.findOne(ecrId);        
        if (plmecrpr != null) {
            List<PLMECRAffectedItem> affectedItems = ecrAffectedItemRepository.findByEcr(ecrId);
            affectedItems.forEach(affectedItem -> {
                if (affectedItem.getProblemReports().length > 0) {
                    List<Integer> requestIds = new ArrayList<Integer>();
                    for (int i = 0; i < affectedItem.getProblemReports().length; i++) {
                        requestIds.add(affectedItem.getProblemReports()[i]);
                    }

                    if (requestIds.size() == 1 && requestIds.indexOf(plmecrpr.getProblemReport()) != -1) {
                        ecrAffectedItemRepository.delete(affectedItem.getId());
                    }
                    if (requestIds.size() > 1 && requestIds.indexOf(plmecrpr.getProblemReport()) != -1) {
                        List<Integer> prIds = requestIds;
                        prIds.remove(prIds.indexOf(plmecrpr.getProblemReport()));
                        Integer[] intArray = new Integer[prIds.size()];
                        intArray = prIds.toArray(intArray);
                        affectedItem.setProblemReports(intArray);
                        ecrAffectedItemRepository.save(affectedItem);
                    }
                }
            });
            ecrprRepository.delete(plmecrpr.getId());
            applicationEventPublisher.publishEvent(new ECREvents.ECRProblemReportDeletedEvent(ecr, plmecrpr));
        }
    }

}
