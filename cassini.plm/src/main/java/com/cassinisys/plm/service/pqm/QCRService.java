package com.cassinisys.plm.service.pqm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.QCREvents;
import com.cassinisys.plm.filtering.QCRCriteria;
import com.cassinisys.plm.filtering.QCRPredicateBuilder;
import com.cassinisys.plm.model.cm.PLMECR;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.PRItemsDto;
import com.cassinisys.plm.model.pqm.dto.ProblemReportsDto;
import com.cassinisys.plm.model.pqm.dto.QCRsDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.WorkflowStatusFlag;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.cm.ECORepository;
import com.cassinisys.plm.repo.cm.ECRRepository;
import com.cassinisys.plm.repo.cm.MCORepository;
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
public class QCRService implements CrudService<PQMQCR, Integer> {

    @Autowired
    private QCRRepository qcrRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private QCRPredicateBuilder qcrPredicateBuilder;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private QCRFileRepository qcrFileRepository;
    @Autowired
    private QCRAttributeRepository qcrAttributeRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private ECRRepository ecrRepository;
    @Autowired
    private MCORepository mcoRepository;
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
    private QCRAggregatePRRepository qcrAggregatePRRepository;
    @Autowired
    private QCRAggregateNCRRepository qcrAggregateNCRRepository;
    @Autowired
    private PRProblemItemRepository prProblemItemRepository;
    @Autowired
    private NCRProblemItemRepository ncrProblemItemRepository;
    @Autowired
    private QCRProblemItemRepository qcrProblemItemRepository;
    @Autowired
    private QCRProblemMaterialRepository qcrProblemMaterialRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private NCRRepository ncrRepository;
    @Autowired
    private QCRRelatedItemRepository qcrRelatedItemRepository;
    @Autowired
    private QCRRelatedMaterialRepository qcrRelatedMaterialRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private QCRCAPARepository qcrcapaRepository;
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
    @PreAuthorize("hasPermission(#qcr,'create')")
    public PQMQCR create(PQMQCR qcr) {
        Integer workflowId = qcr.getWorkflow();
        qcr.setStatus("NONE");

        PQMQCR existQCRNumber = qcrRepository.findByQcrNumber(qcr.getQcrNumber());
        if (existQCRNumber != null) {
            throw new CassiniException(messageSource.getMessage(qcr.getQcrNumber() + " : " + "qcr_number_already_exists", null, "QCR Number already exist", LocaleContextHolder.getLocale()));
        }

        qcr.setWorkflow(null);
        autoNumberService.saveNextNumber(qcr.getQcrType().getNumberSource().getId(), qcr.getQcrNumber());
        qcr = qcrRepository.save(qcr);

        if (workflowId != null) {
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(workflowId);
            if (wfDef != null) {
                PLMWorkflow workflow = workflowService.attachWorkflow(PLMObjectType.QCR, qcr.getId(), wfDef);
                qcr.setWorkflow(workflow.getId());
                qcr = qcrRepository.save(qcr);
            }
        }
        applicationEventPublisher.publishEvent(new QCREvents.QCRCreatedEvent(qcr));
        return qcr;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#qcr.id,'edit')")
    public PQMQCR update(PQMQCR qcr) {
        PQMQCR oldQcr = JsonUtils.cloneEntity(qcrRepository.findOne(qcr.getId()), PQMQCR.class);
        applicationEventPublisher.publishEvent(new QCREvents.QCRBasicInfoUpdatedEvent(oldQcr, qcr));
        return qcrRepository.save(qcr);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {

        PQMQCR pqmqcr = qcrRepository.findOne(id);
        List<PLMECR> plmecrs = ecrRepository.findByQcr(id);
        if (plmecrs.size() > 0) {
            throw new CassiniException(messageSource.getMessage("qcr_number_already_in_use", null, pqmqcr.getQcrNumber() + " : QCR already in use", LocaleContextHolder.getLocale()));
        } else {
            applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
            qcrRepository.delete(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject,'view')")
    public PQMQCR get(Integer id) {
        PQMQCR pqmqcr = qcrRepository.findOne(id);
        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(pqmqcr.getId());
        pqmqcr.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        pqmqcr.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        pqmqcr.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        return pqmqcr;
    }

    @Transactional(readOnly = true)
    public QCRsDto getQCRDetails(Integer id) {
        PQMQCR pqmqcr = qcrRepository.findOne(id);
        QCRsDto dto = new QCRsDto();
        dto.setId(pqmqcr.getId());
        dto.setTitle(pqmqcr.getTitle());
        dto.setQcrNumber(pqmqcr.getQcrNumber());
        dto.setQcrFor(pqmqcr.getQcrFor());
        dto.setDescription(pqmqcr.getDescription());
        dto.setQcrType(pqmqcr.getQcrType().getName());
        dto.setQualityAdministrator(personRepository.findOne(pqmqcr.getQualityAdministrator()).getFullName());
        dto.setStatus(pqmqcr.getStatus());
        dto.setObjectType(pqmqcr.getObjectType().name());
        dto.setModifiedDate(pqmqcr.getModifiedDate());
        dto.setModifiedBy(personRepository.findOne(pqmqcr.getModifiedBy()).getFullName());
        if (pqmqcr.getWorkflow() != null) {
            PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(pqmqcr.getWorkflow());
            dto.setOnHold(plmWorkflow.getOnhold());
        }
        dto.setReleased(pqmqcr.getReleased());
        dto.setStatusType(pqmqcr.getStatusType());
        dto.setModifiedBy(personRepository.findOne(pqmqcr.getModifiedBy()).getFullName());
        dto.setCreatedBy(personRepository.findOne(pqmqcr.getCreatedBy()).getFullName());
        dto.setModifiedDate(pqmqcr.getModifiedDate());
        dto.setCreatedDate(pqmqcr.getCreatedDate());
        dto.setIsImplemented(pqmqcr.getIsImplemented());
        dto.setWorkflow(pqmqcr.getWorkflow());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMQCR> getAll() {
        return qcrRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMQCR> findMultiple(List<Integer> ids) {
        return qcrRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<QCRsDto> getAllQcrs(Pageable pageable, QCRCriteria qcrCriteria) {
        Predicate predicate = qcrPredicateBuilder.build(qcrCriteria, QPQMQCR.pQMQCR);
        Page<PQMQCR> pqmqcrs = qcrRepository.findAll(predicate, pageable);

        List<QCRsDto> plansDto = new LinkedList<>();
        pqmqcrs.getContent().forEach(pqmqcr -> {
            QCRsDto dto = new QCRsDto();
            dto.setId(pqmqcr.getId());
            dto.setTitle(pqmqcr.getTitle());
            dto.setQcrNumber(pqmqcr.getQcrNumber());
            dto.setQcrFor(pqmqcr.getQcrFor());
            dto.setDescription(pqmqcr.getDescription());
            dto.setQcrType(pqmqcr.getQcrType().getName());
            dto.setQualityAdministrator(personRepository.findOne(pqmqcr.getQualityAdministrator()).getFullName());
            dto.setStatus(pqmqcr.getStatus());
            dto.setObjectType(pqmqcr.getObjectType().name());
            dto.setModifiedDate(pqmqcr.getModifiedDate());
            dto.setModifiedBy(personRepository.findOne(pqmqcr.getModifiedBy()).getFullName());

            WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(pqmqcr.getId());
            dto.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            dto.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            dto.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
            dto.setOnHold(workFlowStatusDto.getOnHold());
            dto.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            dto.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());

            dto.setReleased(pqmqcr.getReleased());
            dto.setStatusType(pqmqcr.getStatusType());
            dto.setModifiedBy(personRepository.findOne(pqmqcr.getModifiedBy()).getFullName());
            dto.setCreatedBy(personRepository.findOne(pqmqcr.getCreatedBy()).getFullName());
            dto.setModifiedDate(pqmqcr.getModifiedDate());
            dto.setCreatedDate(pqmqcr.getCreatedDate());
            dto.setIsImplemented(pqmqcr.getIsImplemented());
            dto.setTagsCount(pqmqcr.getTags().size());
            plansDto.add(dto);
        });

        return new PageImpl<QCRsDto>(plansDto, pageable, pqmqcrs.getTotalElements());
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMQCR> getReleasedByQcrFor(QCRFor qcrFor) {
        return qcrRepository.findByQcrForAndReleasedTrue(qcrFor);
    }

    @Transactional(readOnly = true)
    public List<PQMQCRFile> getQcrFiles(Integer id) {
        return qcrFileRepository.findByQcrAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(id);
    }

    @Transactional
    public PQMQCRAttribute createQcrAttribute(PQMQCRAttribute attribute) {
        return qcrAttributeRepository.save(attribute);
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public PQMQCRAttribute updateQcrAttribute(PQMQCRAttribute attribute) {
        PQMQCRAttribute oldValue = qcrAttributeRepository.findByQcrAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PQMQCRAttribute.class);
        attribute = qcrAttributeRepository.save(attribute);
        PQMQCR pqmqcr = qcrRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new QCREvents.QCRAttributesUpdatedEvent(pqmqcr, oldValue, attribute));
        return attribute;
    }

    @Transactional
    public List<PQMQCRAggregatePR> createPrProblemSources(Integer id, List<PQMQCRAggregatePR> aggregatePRs) {
        List<PQMQCRAggregatePR> aggregatePRList = new ArrayList<>();
        for (PQMQCRAggregatePR aggregatePR : aggregatePRs) {
            PQMProblemReport problemReport = problemReportRepository.findOne(aggregatePR.getPrDto().getId());
            aggregatePR.setPr(problemReport);
            aggregatePR = qcrAggregatePRRepository.save(aggregatePR);
            aggregatePRList.add(aggregatePR);
        }

        for (PQMQCRAggregatePR aggregatePR : aggregatePRList) {
            List<PQMPRProblemItem> problemItems = prProblemItemRepository.findByProblemReport(aggregatePR.getPr().getId());

            for (PQMPRProblemItem prProblemItem : problemItems) {
                PQMQCRProblemItem item = qcrProblemItemRepository.findByQcrAndItem(id, prProblemItem.getItem());
                if (item == null) {
                    PQMQCRProblemItem pqmqcrProblemItem = new PQMQCRProblemItem();
                    pqmqcrProblemItem.setQcr(id);
                    pqmqcrProblemItem.setProblemReport(prProblemItem.getProblemReport());
                    pqmqcrProblemItem.setItem(prProblemItem.getItem());
                    pqmqcrProblemItem = qcrProblemItemRepository.save(pqmqcrProblemItem);
                } else {
                    PQMQCRProblemItem problemItem = qcrProblemItemRepository.findByQcrAndItemAndProblemReport(id, prProblemItem.getItem(), prProblemItem.getProblemReport());
                    if (problemItem == null) {
                        item.setProblemReport(prProblemItem.getProblemReport());
                        item = qcrProblemItemRepository.save(item);
                    }
                }
            }
        }

        PQMQCR qcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRPrProblemSourceAddedEvent(qcr, aggregatePRList));

        return aggregatePRs;
    }

    @Transactional
    public List<PQMQCRAggregateNCR> createNcrProblemSources(Integer id, List<PQMQCRAggregateNCR> aggregateNCRs) {

        List<PQMQCRAggregateNCR> aggregateNCRList = new ArrayList<>();
        for (PQMQCRAggregateNCR aggregatePR : aggregateNCRs) {
            PQMNCR pqmncr = ncrRepository.findOne(aggregatePR.getNcrDto().getId());
            aggregatePR.setNcr(pqmncr);
            aggregatePR = qcrAggregateNCRRepository.save(aggregatePR);
            aggregateNCRList.add(aggregatePR);
        }

        for (PQMQCRAggregateNCR aggregateNCR : aggregateNCRList) {
            List<PQMNCRProblemItem> problemItems = ncrProblemItemRepository.findByNcr(aggregateNCR.getNcr().getId());

            for (PQMNCRProblemItem ncrProblemItem : problemItems) {
                PQMQCRProblemMaterial item = qcrProblemMaterialRepository.findByQcrAndMaterial(id, ncrProblemItem.getMaterial());
                if (item == null) {
                    PQMQCRProblemMaterial problemMaterial = new PQMQCRProblemMaterial();
                    problemMaterial.setQcr(id);
                    problemMaterial.setMaterial(ncrProblemItem.getMaterial());
                    problemMaterial.setNcr(ncrProblemItem.getNcr());
                    problemMaterial = qcrProblemMaterialRepository.save(problemMaterial);
                }
            }
        }
        PQMQCR qcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRNCRProblemSourceAddedEvent(qcr, aggregateNCRList));

        return aggregateNCRs;
    }

    @Transactional(readOnly = true)
    public List<PQMQCRAggregateNCR> getNcrProblemSources(Integer id) {
        return qcrAggregateNCRRepository.findByQcr(id);
    }

    @Transactional
    public void deletePrProblemSource(Integer id, Integer sourceId) {
        PQMQCRAggregatePR aggregatePR = qcrAggregatePRRepository.findOne(sourceId);
        List<PQMQCRProblemItem> problemItems = qcrProblemItemRepository.findByQcrAndProblemReport(id, aggregatePR.getPr().getId());
        for (PQMQCRProblemItem problemItem : problemItems) {
            qcrProblemItemRepository.delete(problemItem.getId());
        }
        PQMQCR qcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRPRProblemSourceDeletedEvent(qcr, aggregatePR));

        qcrAggregatePRRepository.delete(sourceId);
    }

    @Transactional
    public void deleteNcrProblemSource(Integer id, Integer sourceId) {
        PQMQCRAggregateNCR aggregateNCR = qcrAggregateNCRRepository.findOne(sourceId);
        List<PQMQCRProblemMaterial> problemItems = qcrProblemMaterialRepository.findByQcrAndNcr(id, aggregateNCR.getNcr().getId());

        for (PQMQCRProblemMaterial problemItem : problemItems) {
            qcrProblemMaterialRepository.delete(problemItem.getId());
        }
        PQMQCR qcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRNCRProblemSourceDeletedEvent(qcr, aggregateNCR));

        qcrAggregateNCRRepository.delete(sourceId);
    }

    @Transactional(readOnly = true)
    public List<PQMQCRAggregatePR> getPrProblemSources(Integer id) {
        List<PQMQCRAggregatePR> aggregatePRList = qcrAggregatePRRepository.findByQcr(id);
        aggregatePRList.forEach(pqmqcrAggregatePR -> {
            if (pqmqcrAggregatePR.getPr().getProduct() != null) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(pqmqcrAggregatePR.getPr().getProduct());
                PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                pqmqcrAggregatePR.setPrDto(new ProblemReportsDto());
                pqmqcrAggregatePR.getPrDto().setProduct(item.getItemName());
            }
        });
        return aggregatePRList;
    }

    @Transactional(readOnly = true)
    public List<PRItemsDto> getQcrProblemItems(Integer id) {
        List<PQMQCRProblemItem> problemItems = qcrProblemItemRepository.findByQcr(id);
        List<PRItemsDto> itemsDtoList = new LinkedList<>();

        problemItems.forEach(problemItem -> {
            PRItemsDto itemsDto = new PRItemsDto();
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(problemItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            itemsDto.setId(problemItem.getId());
            itemsDto.setQcr(id);
            itemsDto.setItemName(item.getItemName());
            itemsDto.setItemNumber(item.getItemNumber());
            itemsDto.setItemType(item.getItemType().getName());
            itemsDto.setDescription(item.getDescription());
            itemsDto.setRevision(itemRevision.getRevision());
            itemsDto.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
            itemsDto.setItem(problemItem.getItem());
            itemsDto.setNotes(problemItem.getNotes());
            itemsDto.setProblemReport(problemItem.getProblemReport());
            if (problemItem.getProblemReport() != null) {
                itemsDto.setPrNumber(problemReportRepository.findOne(problemItem.getProblemReport()).getPrNumber());
            }
            itemsDtoList.add(itemsDto);
        });

        return itemsDtoList;
    }

    @Transactional(readOnly = true)
    public List<PQMQCRProblemMaterial> getQcrProblemMaterials(Integer id) {
        List<PQMQCRProblemMaterial> problemMaterials = qcrProblemMaterialRepository.findByQcr(id);
        for (PQMQCRProblemMaterial problemMaterial : problemMaterials) {
            if (problemMaterial.getNcr() != null) {
                problemMaterial.setNcrNumber(ncrRepository.findOne(problemMaterial.getNcr()).getNcrNumber());
            }
        }

        return problemMaterials;
    }

    @Transactional
    public List<PQMQCRProblemItem> createQcrProblemItems(Integer id, List<PQMQCRProblemItem> problemItems) {
        problemItems = qcrProblemItemRepository.save(problemItems);
        PQMQCR pqmqcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRProblemItemAddedEvent(pqmqcr, problemItems));
        return problemItems;
    }

    @Transactional
    public PQMQCRProblemItem createQcrProblemItem(Integer id, PQMQCRProblemItem problemItem) {
        problemItem = qcrProblemItemRepository.save(problemItem);
        PQMQCR pqmqcr = qcrRepository.findOne(id);
        List<PQMQCRProblemItem> problemItems = new ArrayList<>();
        problemItems.add(problemItem);
        applicationEventPublisher.publishEvent(new QCREvents.QCRProblemItemAddedEvent(pqmqcr, problemItems));

        return problemItem;
    }

    @Transactional
    public PQMQCRProblemItem updateQcrProblemItem(Integer id, PQMQCRProblemItem problemItem) {
        PQMQCRProblemItem oldProblemItem = JsonUtils.cloneEntity(qcrProblemItemRepository.findOne(problemItem.getId()), PQMQCRProblemItem.class);
        problemItem = qcrProblemItemRepository.save(problemItem);
        PQMQCR pqmqcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRProblemItemUpdateEvent(pqmqcr, oldProblemItem, problemItem));

        return problemItem;
    }

    @Transactional
    public void deleteQcrProblemItem(Integer id, Integer problemItem) {
        PQMQCRProblemItem pqmprProblemItem = qcrProblemItemRepository.findOne(problemItem);
        PQMQCR pqmqcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRProblemItemDeletedEvent(pqmqcr, pqmprProblemItem));

        qcrProblemItemRepository.delete(problemItem);
    }

    @Transactional
    public void deleteQcrProblemMaterial(Integer id, Integer problemItem) {
        PQMQCRProblemMaterial problemMaterial = qcrProblemMaterialRepository.findOne(problemItem);
        PQMQCR qcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRProblemMaterialDeletedEvent(qcr, problemMaterial));

        qcrProblemMaterialRepository.delete(problemItem);
    }

    @Transactional
    public List<PQMQCRProblemMaterial> createQcrProblemMaterials(Integer id, List<PQMQCRProblemMaterial> problemMaterials) {
        problemMaterials = qcrProblemMaterialRepository.save(problemMaterials);
        PQMQCR qcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRProblemMaterialAddedEvent(qcr, problemMaterials));
        return problemMaterials;
    }

    @Transactional
    public PQMQCRProblemMaterial createQcrProblemMaterial(Integer id, PQMQCRProblemMaterial problemMaterial) {
        problemMaterial = qcrProblemMaterialRepository.save(problemMaterial);
        PQMQCR pqmqcr = qcrRepository.findOne(id);
        List<PQMQCRProblemMaterial> problemItems = new ArrayList<>();
        problemItems.add(problemMaterial);
        applicationEventPublisher.publishEvent(new QCREvents.QCRProblemMaterialAddedEvent(pqmqcr, problemItems));

        return problemMaterial;
    }

    @Transactional
    public PQMQCRProblemMaterial updateQcrProblemMaterial(Integer id, PQMQCRProblemMaterial problemMaterial) {
        PQMQCRProblemMaterial oldProblemMaterial = JsonUtils.cloneEntity(qcrProblemMaterialRepository.findOne(problemMaterial.getId()), PQMQCRProblemMaterial.class);
        problemMaterial = qcrProblemMaterialRepository.save(problemMaterial);
        PQMQCR pqmqcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRProblemMaterialUpdateEvent(pqmqcr, oldProblemMaterial, problemMaterial));

        return problemMaterial;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','relateditem')")
    public List<PQMQCRRelatedItem> createQcrRelatedItems(Integer id, List<PQMQCRRelatedItem> relatedItems) {
        relatedItems = qcrRelatedItemRepository.save(relatedItems);
        PQMQCR pqmqcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRRelatedItemAddedEvent(pqmqcr, relatedItems));
        return relatedItems;
    }

    @Transactional
    public List<PQMQCRRelatedMaterial> createQcrRelatedMaterials(Integer id, List<PQMQCRRelatedMaterial> relatedMaterials) {
        relatedMaterials = qcrRelatedMaterialRepository.save(relatedMaterials);
        PQMQCR qcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRRelatedMaterialAddedEvent(qcr, relatedMaterials));
        return relatedMaterials;
    }

    @Transactional(readOnly = true)
    public List<PQMQCRRelatedMaterial> getQcrRelatedMaterials(Integer id) {
        return qcrRelatedMaterialRepository.findByQcr(id);
    }

    @Transactional(readOnly = true)
    public List<PRItemsDto> getQcrRelatedItems(Integer id) {
        List<PQMQCRRelatedItem> relatedItems = qcrRelatedItemRepository.findByQcr(id);
        List<PRItemsDto> itemsDtoList = new LinkedList<>();

        relatedItems.forEach(problemItem -> {
            PRItemsDto itemsDto = new PRItemsDto();
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(problemItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            itemsDto.setId(problemItem.getId());
            itemsDto.setQcr(id);
            itemsDto.setItemName(item.getItemName());
            itemsDto.setItemNumber(item.getItemNumber());
            itemsDto.setItemType(item.getItemType().getName());
            itemsDto.setRevision(itemRevision.getRevision());
            itemsDto.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
            itemsDto.setItem(problemItem.getItem());
            itemsDtoList.add(itemsDto);
        });

        return itemsDtoList;
    }

    @Transactional
    public void deleteQcrRelatedMaterial(Integer id, Integer material) {
        PQMQCRRelatedMaterial relatedItem = qcrRelatedMaterialRepository.findOne(material);
        PQMQCR qcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRRelatedMaterialDeletedEvent(qcr, relatedItem));

        qcrRelatedMaterialRepository.delete(material);
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','relateditem')")
    public void deleteQcrRelatedItem(Integer id, Integer itemId) {
        PQMQCRRelatedItem relatedItem = qcrRelatedItemRepository.findOne(itemId);
        PQMQCR pqmqcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRRelatedItemDeletedEvent(pqmqcr, relatedItem));

        qcrRelatedItemRepository.delete(itemId);
    }


    @Transactional(readOnly = true)
    public ItemDetailsDto getDetailsCount(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setProblemSources(qcrAggregateNCRRepository.findByQcr(id).size());
        detailsDto.setProblemSources(detailsDto.getProblemSources() + qcrAggregatePRRepository.findByQcr(id).size());
        detailsDto.setProblemItems(qcrProblemItemRepository.findByQcr(id).size());
        detailsDto.setProblemItems(detailsDto.getProblemItems() + qcrProblemMaterialRepository.findByQcr(id).size());
        detailsDto.setRelatedItems(qcrRelatedItemRepository.findByQcr(id).size());
        detailsDto.setRelatedItems(detailsDto.getRelatedItems() + qcrRelatedMaterialRepository.findByQcr(id).size());
        detailsDto.setItemFiles(qcrFileRepository.findByQcrAndFileTypeAndLatestTrueOrderByModifiedDateDesc(id, "FILE").size());
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        List<PQMQCRCAPA> pqmqcrcapas = qcrcapaRepository.findByQcrOrderByLatestDescModifiedDateDesc(id);
        detailsDto.setCapaCount(pqmqcrcapas.size());
        if (pqmqcrcapas.size() > 0 && pqmqcrcapas.get(0).getResult().equals(AuditResult.PASS)) {
            detailsDto.setCapaPass(true);
        }

        return detailsDto;
    }

    @Transactional
    public PQMQCRCAPA createQCRCaPa(Integer id, PQMQCRCAPA pqmqcrcapa) {

        List<PQMQCRCAPA> pqmqcrcapaList = qcrcapaRepository.findByQcrOrderByLatestDescModifiedDateDesc(id);
        if (pqmqcrcapaList.size() > 0) {
            PQMQCRCAPA capa = qcrcapaRepository.findOne(pqmqcrcapaList.get(0).getId());
            capa.setLatest(false);
            capa = qcrcapaRepository.save(capa);
        }

        pqmqcrcapa = qcrcapaRepository.save(pqmqcrcapa);

        PQMQCR pqmqcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRCAPAAddedEvent(pqmqcr, pqmqcrcapa));
        return pqmqcrcapa;
    }

    @Transactional
    public PQMQCRCAPA updateQCRCaPa(Integer id, PQMQCRCAPA pqmqcrcapa) {
        PQMQCRCAPA oldCapa = JsonUtils.cloneEntity(qcrcapaRepository.findOne(pqmqcrcapa.getId()), PQMQCRCAPA.class);
        pqmqcrcapa = qcrcapaRepository.save(pqmqcrcapa);
        PQMQCR pqmqcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRCAPAUpdatedEvent(pqmqcr, oldCapa, pqmqcrcapa));
        return pqmqcrcapa;
    }

    @Transactional
    public PQMQCRCAPA updateQCRCaPaAudit(Integer id, PQMQCRCAPA pqmqcrcapa) {
        PQMQCRCAPA oldCapa = JsonUtils.cloneEntity(qcrcapaRepository.findOne(pqmqcrcapa.getId()), PQMQCRCAPA.class);
        PQMQCR pqmqcr = qcrRepository.findOne(id);
        pqmqcrcapa.setAuditedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
        pqmqcrcapa.setAuditDate(new Date());
        pqmqcrcapa = qcrcapaRepository.save(pqmqcrcapa);
        if (!pqmqcrcapa.getResult().equals(AuditResult.NONE)) {
            applicationEventPublisher.publishEvent(new QCREvents.QCRCAPAAuditEvent(pqmqcr, pqmqcrcapa));
        } else {
            applicationEventPublisher.publishEvent(new QCREvents.QCRCAPAUpdatedEvent(pqmqcr, oldCapa, pqmqcrcapa));
        }
        return pqmqcrcapa;
    }

    @Transactional
    public void deleteQCRCaPa(Integer id, Integer capaId) {
        PQMQCRCAPA pqmqcrcapa = qcrcapaRepository.findOne(capaId);
        PQMQCR pqmqcr = qcrRepository.findOne(id);
        applicationEventPublisher.publishEvent(new QCREvents.QCRCAPADeletedEvent(pqmqcr, pqmqcrcapa));

        qcrcapaRepository.delete(capaId);
    }

    @Transactional(readOnly = true)
    public PQMQCRCAPA getQCRCaPa(Integer id, Integer capaId) {
        return qcrcapaRepository.findOne(capaId);
    }

    @Transactional(readOnly = true)
    public List<PQMQCRCAPA> getAllQCRCaPa(Integer id) {
        return qcrcapaRepository.findByQcrOrderByLatestDescModifiedDateDesc(id);
    }

    @Transactional(readOnly = true)
    public List<PQMQCR> findByProblemItem(Integer itemId) {
        List<PQMQCRProblemItem> pqmprProblemItems = qcrProblemItemRepository.findByItem(itemId);
        List<PQMQCR> pqmqcrs = new ArrayList<>();
        for (PQMQCRProblemItem pqmprProblemItem : pqmprProblemItems) {
            pqmqcrs.add(qcrRepository.findOne(pqmprProblemItem.getQcr()));
        }
        return pqmqcrs;
    }

    @Transactional(readOnly = true)
    public List<PQMQCR> findByProblemPart(Integer partId) {
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(partId);
        List<PQMQCRProblemMaterial> pqmqcrProblemMaterials = qcrProblemMaterialRepository.findByMaterial(manufacturerPart);
        List<PQMQCR> pqmqcrs = new ArrayList<>();
        for (PQMQCRProblemMaterial pqmqcrProblemMaterial : pqmqcrProblemMaterials) {
            pqmqcrs.add(qcrRepository.findOne(pqmqcrProblemMaterial.getQcr()));
        }
        return pqmqcrs;
    }

    @EventListener(condition = "#event.attachedToType.name() == 'QCR'")
    public void qcrWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PQMQCR qcr = (PQMQCR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        applicationEventPublisher.publishEvent(new QCREvents.QCRWorkflowStartedEvent(qcr, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'QCR'")
    public void qcrWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PQMQCR qcr = (PQMQCR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        qcr.setStatus(fromStatus.getName());
        qcr.setStatusType(fromStatus.getType());
        update(qcr);
        applicationEventPublisher.publishEvent(new QCREvents.QCRWorkflowPromotedEvent(qcr, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'QCR'")
    public void qcrWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PQMQCR qcr = (PQMQCR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new QCREvents.QCRWorkflowHoldEvent(qcr, plmWorkflow, fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'QCR'")
    public void qcrWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PQMQCR qcr = (PQMQCR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new QCREvents.QCRWorkflowUnHoldEvent(qcr, plmWorkflow, fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'QCR'")
    public void qcrWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PQMQCR pqmqcr = (PQMQCR) event.getAttachedToObject();
        PLMWorkflowStatus toStatus = event.getToStatus();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        pqmqcr.setStatus(toStatus.getName());
        pqmqcr.setStatusType(toStatus.getType());
        update(pqmqcr);
        applicationEventPublisher.publishEvent(new QCREvents.QCRWorkflowDemotedEvent(pqmqcr, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'QCR'")
    public void qcrWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PQMQCR qcr = (PQMQCR) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        qcr.setStatus(fromStatus.getName());
        qcr.setStatusType(fromStatus.getType());
        qcr.setReleased(true);
        update(qcr);
        applicationEventPublisher.publishEvent(new QCREvents.QCRWorkflowFinishedEvent(qcr, plmWorkflow));
    }
}
