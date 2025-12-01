package com.cassinisys.plm.service.pqm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.ProblemReportEvents;
import com.cassinisys.plm.filtering.ProblemReportCriteria;
import com.cassinisys.plm.filtering.ProblemReportPredicateBuilder;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.PRItemsDto;
import com.cassinisys.plm.model.pqm.dto.ProblemReportsDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.WorkflowStatusFlag;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.mfr.SupplierRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pqm.*;
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
public class ProblemReportService implements CrudService<PQMProblemReport, Integer> {

    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private QCRAggregatePRRepository qcrAggregatePRRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private ProblemReportPredicateBuilder problemReportPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ProblemReportFileRepository problemReportFileRepository;
    @Autowired
    private ProblemReportAttributeRepository problemReportAttributeRepository;
    @Autowired
    private PQMCustomerRepository pqmCustomerRepository;
    @Autowired
    private PQMSupplierRepository pqmSupplierRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private PRProblemItemRepository prProblemItemRepository;
    @Autowired
    private PRRelatedItemRepository prRelatedItemRepository;
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
    @PreAuthorize("hasPermission(#problemReport,'create')")
    public PQMProblemReport create(PQMProblemReport problemReport) {
        Integer workflowId = problemReport.getWorkflow();
        problemReport.setWorkflow(null);
        PQMProblemReport existPlanNumber = problemReportRepository.findByPrNumber(problemReport.getPrNumber());
        if (existPlanNumber != null) {
            throw new CassiniException(messageSource.getMessage(problemReport.getPrNumber() + " : " + "pr_number_already_exists", null, "Number already exist", LocaleContextHolder.getLocale()));
        }
        problemReport.setReportedDate(new Date());
        autoNumberService.saveNextNumber(problemReport.getPrType().getNumberSource().getId(), problemReport.getPrNumber());
        problemReport = problemReportRepository.save(problemReport);

        if (workflowId != null) {
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(workflowId);
            if (wfDef != null) {
                PLMWorkflow workflow = workflowService.attachWorkflow(PLMObjectType.PROBLEMREPORT, problemReport.getId(), wfDef);
                problemReport.setWorkflow(workflow.getId());
                problemReport = problemReportRepository.save(problemReport);
            }
        }
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportCreatedEvent(problemReport));
        return problemReport;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#problemReport.id,'edit')")
    public PQMProblemReport update(PQMProblemReport problemReport) {
        PQMProblemReport pqmProblemReport = JsonUtils.cloneEntity(problemReportRepository.findOne(problemReport.getId()), PQMProblemReport.class);
        problemReport = problemReportRepository.save(problemReport);
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportBasicInfoUpdatedEvent(pqmProblemReport, problemReport));
        return problemReport;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {

        PQMProblemReport problemReport = problemReportRepository.findOne(id);
        List<PQMQCRAggregatePR> aggregatePRList = qcrAggregatePRRepository.findByPr(id);
        if (aggregatePRList.size() > 0) {
            throw new CassiniException(messageSource.getMessage("pr_number_already_in_use", null, problemReport.getPrNumber() + " : Problem Report already in use", LocaleContextHolder.getLocale()));
        } else {
            applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
            problemReportRepository.delete(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject,'view')")
    public PQMProblemReport get(Integer id) {
        PQMProblemReport problemReport = problemReportRepository.findOne(id);
         //Adding workflow relavent settings
         WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(problemReport.getId());
         problemReport.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
         problemReport.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
         problemReport.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        return problemReport;
    }

    @Transactional(readOnly = true)
    public ProblemReportsDto getProblemReportDetails(Integer id) {
        PQMProblemReport problemReport = problemReportRepository.findOne(id);
        ProblemReportsDto dto = new ProblemReportsDto();
        dto.setId(problemReport.getId());
        dto.setProblem(problemReport.getProblem());
        dto.setPrNumber(problemReport.getPrNumber());
        dto.setDescription(problemReport.getDescription());
        dto.setObjectType(problemReport.getObjectType().name());
        if (problemReport.getProduct() != null) {
            PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(problemReport.getProduct());
            dto.setProduct(itemRepository.findOne(plmItemRevision.getItemMaster()).getItemName());
            dto.setRevision(plmItemRevision.getRevision());
        }
        dto.setPrType(problemReport.getPrType().getName());
        dto.setStepsToReproduce(problemReport.getStepsToReproduce());
        dto.setQualityAnalyst(personRepository.findOne(problemReport.getQualityAnalyst()).getFullName());
        if (problemReport.getReporterType().equals(ReporterType.CUSTOMER)) {
            dto.setReportedBy(pqmCustomerRepository.findOne(problemReport.getReportedBy()).getName());
        } else if (problemReport.getReporterType().equals(ReporterType.INTERNAL)) {
            dto.setReportedBy(personRepository.findOne(problemReport.getReportedBy()).getFullName());
        } else if (problemReport.getReporterType().equals(ReporterType.SUPPLIER)) {
            dto.setReportedBy(pqmSupplierRepository.findOne(problemReport.getReportedBy()).getName());
        }
        dto.setReleased(problemReport.getReleased());
        dto.setReportedDate(problemReport.getReportedDate());
        dto.setReporterType(problemReport.getReporterType());
        dto.setFailureType(problemReport.getFailureType());
        dto.setSeverity(problemReport.getSeverity());
        dto.setStatus(problemReport.getStatus());
        dto.setStatusType(problemReport.getStatusType());
        dto.setDisposition(problemReport.getDisposition());
        dto.setCreatedBy(personRepository.findOne(problemReport.getCreatedBy()).getFullName());
        dto.setModifiedBy(personRepository.findOne(problemReport.getModifiedBy()).getFullName());
        dto.setModifiedDate(problemReport.getModifiedDate());
        dto.setCreatedDate(problemReport.getCreatedDate());
        dto.setIsImplemented(problemReport.getIsImplemented());
        dto.setWorkflow(problemReport.getWorkflow());
        if (problemReport.getWorkflow() != null) {
            PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(problemReport.getWorkflow());
            if (plmWorkflow != null) {
                dto.setOnHold(plmWorkflow.getOnhold());
            }
        }
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMProblemReport> getAll() {
        return problemReportRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMProblemReport> findMultiple(List<Integer> ids) {
        return problemReportRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<ProblemReportsDto> getAllProblemReports(Pageable pageable, ProblemReportCriteria problemReportCriteria) {
        Predicate predicate = problemReportPredicateBuilder.build(problemReportCriteria, QPQMProblemReport.pQMProblemReport);
        Page<PQMProblemReport> problemReports = problemReportRepository.findAll(predicate, pageable);

        List<ProblemReportsDto> problemReportsDtos = new LinkedList<>();
        problemReports.getContent().forEach(problemReport -> {
            ProblemReportsDto dto = new ProblemReportsDto();
            dto.setId(problemReport.getId());
            dto.setProblem(problemReport.getProblem());
            dto.setPrNumber(problemReport.getPrNumber());
            dto.setDescription(problemReport.getDescription());
            dto.setObjectType(problemReport.getObjectType().name());
            if (problemReport.getProduct() != null) {
                PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(problemReport.getProduct());
                dto.setProduct(itemRepository.findOne(plmItemRevision.getItemMaster()).getItemName());
                dto.setRevision(plmItemRevision.getRevision());
            }
            dto.setPrType(problemReport.getPrType().getName());
            dto.setStepsToReproduce(problemReport.getStepsToReproduce());
            dto.setQualityAnalyst(personRepository.findOne(problemReport.getQualityAnalyst()).getFullName());
            if (problemReport.getReporterType().equals(ReporterType.CUSTOMER) && problemReport.getReportedBy() != null) {
                dto.setReportedBy(pqmCustomerRepository.findOne(problemReport.getReportedBy()).getName());
            } else if (problemReport.getReporterType().equals(ReporterType.INTERNAL) && problemReport.getReportedBy() != null) {
                dto.setReportedBy(personRepository.findOne(problemReport.getReportedBy()).getFullName());
            } else if (problemReport.getReporterType().equals(ReporterType.SUPPLIER) && problemReport.getReportedBy() != null) {
                dto.setReportedBy(supplierRepository.findOne(problemReport.getReportedBy()).getName());
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
            dto.setCreatedBy(personRepository.findOne(problemReport.getCreatedBy()).getFullName());
            dto.setModifiedBy(personRepository.findOne(problemReport.getModifiedBy()).getFullName());
            dto.setModifiedDate(problemReport.getModifiedDate());
            dto.setCreatedDate(problemReport.getCreatedDate());
            dto.setIsImplemented(problemReport.getIsImplemented());

            WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(problemReport.getId());
            dto.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            dto.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            dto.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
            dto.setOnHold(workFlowStatusDto.getOnHold());
            dto.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            dto.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());

            dto.setTagsCount(problemReport.getTags().size());
            problemReportsDtos.add(dto);
        });

        return new PageImpl<ProblemReportsDto>(problemReportsDtos, pageable, problemReports.getTotalElements());
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMProblemReport> getReleasedProblemReports() {
        return problemReportRepository.findByReleasedTrueOrderByModifiedDateDesc();
    }


    @Transactional(readOnly = true)
    public List<PQMProblemReportFile> getPrFiles(Integer id) {
        return problemReportFileRepository.findByProblemReportAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(id);
    }

    @Transactional
    public PQMProblemReportAttribute createPrAttribute(PQMProblemReportAttribute attribute) {
        return problemReportAttributeRepository.save(attribute);
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public PQMProblemReportAttribute updatePrAttribute(PQMProblemReportAttribute attribute) {
        PQMProblemReportAttribute oldValue = problemReportAttributeRepository.findByProblemReportAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PQMProblemReportAttribute.class);
        attribute = problemReportAttributeRepository.save(attribute);
        PQMProblemReport problemReport = problemReportRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportAttributesUpdatedEvent(problemReport, oldValue, attribute));
        return attribute;
    }

    @Transactional
    public PQMPRProblemItem createProblemItem(Integer id, PQMPRProblemItem problemItem) {
        problemItem = prProblemItemRepository.save(problemItem);
        PQMProblemReport problemReport = problemReportRepository.findOne(id);
        List<PQMPRProblemItem> problemItems = new ArrayList<>();
        problemItems.add(problemItem);
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportProblemItemAddedEvent(problemReport, problemItems));
        return problemItem;
    }

    @Transactional
    public PQMPRProblemItem updateProblemItem(Integer id, PQMPRProblemItem problemItem) {
        PQMPRProblemItem oldProblemItem = JsonUtils.cloneEntity(prProblemItemRepository.findOne(problemItem.getId()), PQMPRProblemItem.class);
        problemItem = prProblemItemRepository.save(problemItem);
        PQMProblemReport problemReport = problemReportRepository.findOne(id);
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportProblemItemUpdatedEvent(problemReport, oldProblemItem, problemItem));
        return problemItem;
    }

    @Transactional
    public List<PQMPRProblemItem> createProblemItems(Integer id, List<PQMPRProblemItem> problemItems) {
        problemItems = prProblemItemRepository.save(problemItems);
        PQMProblemReport problemReport = problemReportRepository.findOne(id);
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportProblemItemAddedEvent(problemReport, problemItems));
        return problemItems;
    }

    @Transactional
    public void deleteProblemItem(Integer id, Integer problemItem) {
        PQMPRProblemItem pqmprProblemItem = prProblemItemRepository.findOne(problemItem);
        PQMProblemReport problemReport = problemReportRepository.findOne(id);
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportProblemItemDeletedEvent(problemReport, pqmprProblemItem));

        prProblemItemRepository.delete(problemItem);
    }

    @Transactional(readOnly = true)
    public List<PRItemsDto> getProblemItems(Integer id) {
        List<PQMPRProblemItem> problemItems = prProblemItemRepository.findByProblemReport(id);
        List<PRItemsDto> itemsDtos = new LinkedList<>();

        for (PQMPRProblemItem problemItem : problemItems) {
            PRItemsDto itemsDto = new PRItemsDto();
            itemsDto.setId(problemItem.getId());
            itemsDto.setProblemReport(problemItem.getProblemReport());
            itemsDto.setItem(problemItem.getItem());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(problemItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());

            itemsDto.setItemName(item.getItemName());
            itemsDto.setItemNumber(item.getItemNumber());
            itemsDto.setItemType(item.getItemType().getName());
            itemsDto.setDescription(item.getDescription());
            itemsDto.setRevision(itemRevision.getRevision());
            itemsDto.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
            itemsDto.setNotes(problemItem.getNotes());
            itemsDtos.add(itemsDto);
        }

        return itemsDtos;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','relateditem')")
    public PQMPRRelatedItem createRelatedItem(Integer id, PQMPRRelatedItem problemItem) {
        problemItem = prRelatedItemRepository.save(problemItem);
        PQMProblemReport problemReport = problemReportRepository.findOne(id);
        List<PQMPRRelatedItem> relatedItems = new ArrayList<>();
        relatedItems.add(problemItem);
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportRelatedItemAddedEvent(problemReport, relatedItems));
        return problemItem;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','relateditem')")
    public List<PQMPRRelatedItem> createRelatedItems(Integer id, List<PQMPRRelatedItem> relatedItems) {
        relatedItems = prRelatedItemRepository.save(relatedItems);
        PQMProblemReport problemReport = problemReportRepository.findOne(id);
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportRelatedItemAddedEvent(problemReport, relatedItems));
        return relatedItems;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','relateditem')")
    public void deleteRelatedItem(Integer id, Integer relatedItemId) {
        PQMPRRelatedItem relatedItem = prRelatedItemRepository.findOne(relatedItemId);
        PQMProblemReport problemReport = problemReportRepository.findOne(id);
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportRelatedItemDeletedEvent(problemReport, relatedItem));

        prRelatedItemRepository.delete(relatedItemId);
    }

    @Transactional(readOnly = true)
    public List<PRItemsDto> getRelatedItems(Integer id) {
        List<PQMPRRelatedItem> relatedItems = prRelatedItemRepository.findByProblemReport(id);

        List<PRItemsDto> itemsDtos = new LinkedList<>();

        for (PQMPRRelatedItem relatedItem : relatedItems) {
            PRItemsDto itemsDto = new PRItemsDto();
            itemsDto.setId(relatedItem.getId());
            itemsDto.setProblemReport(relatedItem.getProblemReport());
            itemsDto.setItem(relatedItem.getItem());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());

            itemsDto.setItemName(item.getItemName());
            itemsDto.setItemNumber(item.getItemNumber());
            itemsDto.setItemType(item.getItemType().getName());
            itemsDto.setDescription(item.getDescription());
            itemsDto.setRevision(itemRevision.getRevision());
            itemsDto.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
            itemsDto.setNotes(relatedItem.getNotes());
            itemsDtos.add(itemsDto);
        }

        return itemsDtos;
    }


    @Transactional(readOnly = true)
    public ItemDetailsDto getDetailsCount(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setProblemItems(prProblemItemRepository.findByProblemReport(id).size());
        detailsDto.setRelatedItems(prRelatedItemRepository.findByProblemReport(id).size());
        detailsDto.setItemFiles(problemReportFileRepository.findByProblemReportAndFileTypeAndLatestTrueOrderByModifiedDateDesc(id, "FILE").size());
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        return detailsDto;
    }

    @Transactional(readOnly = true)
    public Long getPRCount() {
        return problemReportRepository.count();
    }

    @Transactional(readOnly = true)
    public List<PQMProblemReport> findByProblemItem(Integer itemId) {
        List<PQMPRProblemItem> pqmprProblemItems = prProblemItemRepository.findByItem(itemId);
        List<PQMProblemReport> pqmProblemReports = new ArrayList<>();
        for (PQMPRProblemItem pqmprProblemItem : pqmprProblemItems) {
            pqmProblemReports.add(problemReportRepository.findOne(pqmprProblemItem.getProblemReport()));
        }
        return pqmProblemReports;
    }

    @Transactional(readOnly = true)
    public List<PQMProblemReport> getProductItemPRs(Integer itemId) {
        List<PQMProblemReport> problemReports = problemReportRepository.findByProduct(itemId);
        return problemReports;
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PROBLEMREPORT'")
    public void prWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PQMProblemReport pr = (PQMProblemReport) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportWorkflowStartedEvent(pr, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PROBLEMREPORT'")
    public void prWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PQMProblemReport pr = (PQMProblemReport) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        pr.setStatus(fromStatus.getName());
        pr.setStatusType(fromStatus.getType());
        update(pr);
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportWorkflowPromotedEvent(pr, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PROBLEMREPORT'")
    public void prWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PQMProblemReport pr = (PQMProblemReport) event.getAttachedToObject();
        PLMWorkflowStatus toStatus = event.getToStatus();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        pr.setStatus(toStatus.getName());
        pr.setStatusType(toStatus.getType());
        update(pr);
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportWorkflowDemotedEvent(pr, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PROBLEMREPORT'")
    public void prWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PQMProblemReport pr = (PQMProblemReport) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        pr.setStatus(fromStatus.getName());
        pr.setStatusType(fromStatus.getType());
        pr.setReleased(true);
        update(pr);
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportWorkflowFinishedEvent(pr, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PROBLEMREPORT'")
    public void prWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PQMProblemReport problemReport = (PQMProblemReport) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportWorkflowHoldEvent(problemReport, plmWorkflow, fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PROBLEMREPORT'")
    public void prWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PQMProblemReport problemReport = (PQMProblemReport) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportWorkflowUnholdEvent(problemReport, plmWorkflow, fromStatus));
    }
}
