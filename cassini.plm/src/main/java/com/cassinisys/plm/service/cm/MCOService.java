package com.cassinisys.plm.service.cm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.MCOEvents;
import com.cassinisys.plm.filtering.*;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.cm.dto.MCOAffecteditemsDto;
import com.cassinisys.plm.model.cm.dto.MCOProductAffectedItemDto;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mes.dto.MBOMItemDto;
import com.cassinisys.plm.model.mfr.PLMItemManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartHistory;
import com.cassinisys.plm.model.mobile.MCODetails;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.BomDto;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.dto.DefinitionEventDto;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.mfr.ItemManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartHistoryRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.plm.ItemFileRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.wf.*;
import com.cassinisys.plm.service.mes.BOPService;
import com.cassinisys.plm.service.mes.MBOMFileService;
import com.cassinisys.plm.service.mes.MBOMService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

/**
 * Created by subramanyam on 14-06-2020.
 */
@Service
public class MCOService implements CrudService<PLMMCO, Integer> {

    @Autowired
    private MCORepository mcoRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private MCOPredicateBuilder mcoPredicateBuilder;
    @Autowired
    private ItemMCOPredicateBuilder itemMcoPredicateBuilder;
    @Autowired
    private ManufacturerMCOPredicateBuilder manufacturerMCOPredicateBuilder;
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
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MCOAffectedItemRepository mcoAffectedItemRepository;
    @Autowired
    private MCORelatedItemRepository mcoRelatedItemRepository;
    @Autowired
    private DCRItemsPredicateBuilder dcrItemsPredicateBuilder;
    @Autowired
    private ChangeFileRepository changeFileRepository;
    @Autowired
    private QCRProblemMaterialRepository qcrProblemMaterialRepository;
    @Autowired
    private ItemManufacturerPartRepository itemManufacturerPartRepository;
    @Autowired
    private QCRRepository qcrRepository;
    @Autowired
    private QCRAggregateNCRRepository qcrAggregateNCRRepository;
    @Autowired
    private NCRProblemItemRepository ncrProblemItemRepository;
    @Autowired
    private NCRRepository ncrRepository;
    @Autowired
    private ManufacturerPartHistoryRepository manufacturerPartHistoryRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ItemMCORepository itemMCORepository;
    @Autowired
    private ManufacturerMCORepository manufacturerMCORepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private MCOProductAffectedItemRepository mcoProductAffectedItemRepository;
    @Autowired
    private MESMBOMRevisionRepository mesMBOMRevisionRepository;
    @Autowired
    private MESMBOMRepository mesMBOMRepository;
    @Autowired
    private MBOMTypeRepository mbomTypeRepository;
    @Autowired
    private MESBOMItemRepository mesbomItemRepository;
    @Autowired
    private MBOMFileRepository mbomFileRepository;
    @Autowired
    private MBOMFileService mbomFileService;
    @Autowired
    private MBOMRevisionStatusHistoryRepository mbomRevisionStatusHistoryRepository;
    @Autowired
    private PLMWorkflowRepository workflowRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private PLMWorkflowEventRepository plmWorkflowEventRepository;
    @Autowired
    private PLMWorkflowDefinitionEventRepository plmWorkflowDefinitionEventRepository;
    @Autowired
    private PLMWorkflowDefinitionStatusRepository plmWorkflowDefinitionStatusRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private WorkflowEventService workflowEventService;
    @Autowired
    private MBOMService mbomService;
    @Autowired
    private BOPRevisionRepository bopRevisionRepository;
    @Autowired
    private BOPService bopService;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mco,'create')")
    public PLMMCO create(PLMMCO mco) {
        Integer workflowDef = null;
        workflowDef = mco.getWorkflow();
        Integer workflowStatusId = mco.getWorkflowStatus();
        mco.setWorkflow(null);
        mco.setWorkflowStatus(null);

        PLMMCO existMco = mcoRepository.findByMcoNumber(mco.getMcoNumber());
        if (existMco != null) {
            throw new CassiniException(messageSource.getMessage(mco.getMcoNumber() + " : " + "mco_number_already_exists", null, "MCO Number already exist", LocaleContextHolder.getLocale()));
        }
        autoNumberService.saveNextNumber(mco.getMcoType().getAutoNumberSource().getId(), mco.getMcoNumber());
        mco = mcoRepository.save(mco);
        if (workflowDef != null) {
            attachDCRWorkflow(mco, workflowDef, workflowStatusId);
        }

        if (mco.getQcr() != null) {
            List<PQMQCRProblemMaterial> problemMaterials = qcrProblemMaterialRepository.findByQcr(mco.getQcr());

            for (PQMQCRProblemMaterial problemMaterial : problemMaterials) {
                PLMMCOAffectedItem affectedItem = new PLMMCOAffectedItem();
                affectedItem.setMaterial(problemMaterial.getMaterial().getId());
                affectedItem.setChangeType(MCOChangeType.REPLACED);
                affectedItem.setMco(mco.getId());
                affectedItem = mcoAffectedItemRepository.save(affectedItem);
            }
        }
        applicationEventPublisher.publishEvent(new MCOEvents.MCOCreatedEvent(mco));
        return mco;
    }

    @Transactional
    @PreAuthorize("hasPermission(#mco,'create')")
    public PLMItemMCO createItemMco(PLMItemMCO mco) {
        Integer workflowDef = null;
        workflowDef = mco.getWorkflow();
        Integer workflowStatusId = mco.getWorkflowStatus();
        mco.setWorkflow(null);
        mco.setWorkflowStatus(null);

        PLMItemMCO existMco = itemMCORepository.findByMcoNumber(mco.getMcoNumber());
        if (existMco != null) {
            throw new CassiniException(messageSource.getMessage(mco.getMcoNumber() + " : " + "mco_number_already_exists", null, "MCO Number already exist", LocaleContextHolder.getLocale()));
        }
        autoNumberService.saveNextNumber(mco.getMcoType().getAutoNumberSource().getId(), mco.getMcoNumber());
        mco = itemMCORepository.save(mco);
        if (workflowDef != null) {
            attachItemMCOWorkflow(mco, workflowDef, workflowStatusId);
        }

        if (mco.getQcr() != null) {
            List<PQMQCRProblemMaterial> problemMaterials = qcrProblemMaterialRepository.findByQcr(mco.getQcr());

            for (PQMQCRProblemMaterial problemMaterial : problemMaterials) {
                PLMMCOAffectedItem affectedItem = new PLMMCOAffectedItem();
                affectedItem.setMaterial(problemMaterial.getMaterial().getId());
                affectedItem.setChangeType(MCOChangeType.REPLACED);
                affectedItem.setMco(mco.getId());
                affectedItem = mcoAffectedItemRepository.save(affectedItem);
            }
        }
        applicationEventPublisher.publishEvent(new MCOEvents.MCOCreatedEvent(mco));
        return mco;
    }

    @Transactional
    @PreAuthorize("hasPermission(#mco,'create')")
    public PLMManufacturerMCO createManufacturerMco(PLMManufacturerMCO mco) {
        Integer workflowDef = null;
        workflowDef = mco.getWorkflow();
        mco.setWorkflow(null);

        PLMManufacturerMCO existMco = manufacturerMCORepository.findByMcoNumber(mco.getMcoNumber());
        if (existMco != null) {
            throw new CassiniException(messageSource.getMessage(mco.getMcoNumber() + " : " + "mco_number_already_exists", null, "MCO Number already exist", LocaleContextHolder.getLocale()));
        }
        autoNumberService.saveNextNumber(mco.getMcoType().getAutoNumberSource().getId(), mco.getMcoNumber());
        mco = manufacturerMCORepository.save(mco);
        if (workflowDef != null) {
            attachManufacturerWorkflow(mco, workflowDef);
        }

        if (mco.getQcr() != null) {
            List<PQMQCRProblemMaterial> problemMaterials = qcrProblemMaterialRepository.findByQcr(mco.getQcr());

            for (PQMQCRProblemMaterial problemMaterial : problemMaterials) {
                PLMMCOAffectedItem affectedItem = new PLMMCOAffectedItem();
                affectedItem.setMaterial(problemMaterial.getMaterial().getId());
                affectedItem.setChangeType(MCOChangeType.REPLACED);
                affectedItem.setMco(mco.getId());
                affectedItem = mcoAffectedItemRepository.save(affectedItem);
            }
        }
        applicationEventPublisher.publishEvent(new MCOEvents.MCOCreatedEvent(mco));
        return mco;
    }


    @Transactional
    public PLMWorkflow attachDCRWorkflow(PLMMCO mco, Integer wfDefId, Integer workflowStatusId) {
        PLMWorkflow workflow = null;
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (wfDef != null) {
            workflow = workflowService.attachWorkflow(mco.getObjectType(), mco.getId(), wfDef);
            mco.setWorkflow(workflow.getId());
            if (workflowStatusId != null && mco.getRevisionCreationType().equals(RevisionCreationType.ACTIVITY_COMPLETION)) {
                PLMWorkflowDefinitionStatus workflowDefinitionStatus = plmWorkflowDefinitionStatusRepository.findOne(workflowStatusId);
                PLMWorkflowStatus workflowStatus = workflowStatusRepository.findByWorkflowAndNameAndType(workflow.getId(), workflowDefinitionStatus.getName(), workflowDefinitionStatus.getType());
                mco.setWorkflowStatus(workflowStatus.getId());
            }
            mcoRepository.save(mco);
        }
        return workflow;
    }

    @Transactional
    public PLMWorkflow attachItemMCOWorkflow(PLMItemMCO mco, Integer wfDefId, Integer workflowStatusId) {
        PLMWorkflow workflow = null;
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (wfDef != null) {
            workflow = workflowService.attachWorkflow(mco.getObjectType(), mco.getId(), wfDef);
            mco.setWorkflow(workflow.getId());
            if (workflowStatusId != null && mco.getRevisionCreationType().equals(RevisionCreationType.ACTIVITY_COMPLETION)) {
                PLMWorkflowDefinitionStatus workflowDefinitionStatus = plmWorkflowDefinitionStatusRepository.findOne(workflowStatusId);
                PLMWorkflowStatus workflowStatus = workflowStatusRepository.findByWorkflowAndNameAndType(workflow.getId(), workflowDefinitionStatus.getName(), workflowDefinitionStatus.getType());
                mco.setWorkflowStatus(workflowStatus.getId());
            }
            mco = itemMCORepository.save(mco);
        }
        return workflow;
    }

    @Transactional
    public PLMWorkflow attachManufacturerWorkflow(PLMManufacturerMCO mco, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (wfDef != null) {
            workflow = workflowService.attachWorkflow(mco.getObjectType(), mco.getId(), wfDef);
            mco.setWorkflow(workflow.getId());
            manufacturerMCORepository.save(mco);
        }
        return workflow;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mco.id,'edit')")
    public PLMMCO update(PLMMCO mco) {
        PLMMCO oldMco = JsonUtils.cloneEntity(mcoRepository.findOne(mco.getId()), PLMMCO.class);
        if (mco.getStatusType() == WorkflowStatusType.RELEASED) {
            mco.setReleased(true);
            mco.setReleasedDate(new Date());
        } else if (mco.getStatusType().equals(WorkflowStatusType.REJECTED)) {
            mco.setRejected(true);
            mco.setReleasedDate(new Date());
        }

        if (mco.getStatusType() == WorkflowStatusType.RELEASED || mco.getStatusType() == WorkflowStatusType.REJECTED) {
            if (mco.getMcoType().getMcoType().equals(MCOType.OEMPARTMCO)) {
                releaseMfrPartMcoAffectedItems(mco);
            } else {
                releaseItemMcoAffectedItems(mco);
            }
        }
        applicationEventPublisher.publishEvent(new MCOEvents.MCOBasicInfoUpdatedEvent(oldMco, mco));
        mco = mcoRepository.save(mco);
        return mco;
    }

    @Transactional
    private void checkMBOMRevisionBom(MESMBOMRevision mesmbomRevision) {
        Integer normalItemCount = mesbomItemRepository.getNormalItemsCountByMBomRevision(mesmbomRevision.getId());
        MESMBOM mesmbom = mesMBOMRepository.findOne(mesmbomRevision.getMaster());
        if (normalItemCount == 0) {
            String message = messageSource.getMessage("no_mbom_items_found", null, "MBOM {0} cannot be released as there are no item from EBOM", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", mesmbom.getNumber());
            throw new CassiniException(result);
        } else {
            Integer eBomLeafItemsCount = getEBomTotalLeafItemsCount(mesmbomRevision);
            Integer mBomRevisionLeafItemsCount = getMBomRevisionTotalLeafItemsCount(mesmbomRevision, mesmbom);
            if (eBomLeafItemsCount > 0 && !eBomLeafItemsCount.equals(mBomRevisionLeafItemsCount)) {
                String message = messageSource.getMessage("ebom_consumed_qty_not_same", null, "MBOM {0} cannot be released, until all the EBOM items consumed", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", mesmbom.getNumber());
                throw new CassiniException(result);
            }
        }
    }

    private Integer getEBomTotalLeafItemsCount(MESMBOMRevision mesmbomRevision) {
        List<BomDto> eBomItems = mbomService.getReleasedBom(mesmbomRevision.getId(), mesmbomRevision.getItemRevision(), true);
        Integer leafItemsCount = 0;

        for (BomDto eBomItem : eBomItems) {
            if (eBomItem.getChildren().size() == 0) {
                leafItemsCount = leafItemsCount + eBomItem.getQuantity();
            } else {
                leafItemsCount = visitEbomItemForQuantity(eBomItem, leafItemsCount);
            }
        }
        return leafItemsCount;
    }

    @Transactional
    public Integer getMBomRevisionTotalLeafItemsCount(MESMBOMRevision mesmbomRevision, MESMBOM mesmbom) {
        Integer leafItemsCount = 0;
        List<MBOMItemDto> mbomItems = mbomService.getMBOMItems(mesmbomRevision.getId(), true);
        for (MBOMItemDto mbomItem : mbomItems) {
            if (mbomItem.getItemRevisionHasBom() && mbomItem.getChildren().size() == 0) {
                String message = messageSource.getMessage("no_children_in_mbom_item", null, "MBOM {0} cannot be released as there are no children for item {1}", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", mesmbom.getNumber(), mbomItem.getItemNumber());
                throw new CassiniException(result);
            }
            if (mbomItem.getChildren().size() == 0 && mbomItem.getType().equals(MESBomItemType.NORMAL)) {
                leafItemsCount = leafItemsCount + mbomItem.getQuantity();
            } else {
                leafItemsCount = visitMbomItemForQuantity(mbomItem, leafItemsCount);
            }
        }
        return leafItemsCount;
    }

    private Integer visitEbomItemForQuantity(BomDto bomDto, Integer leafItemsCount) {
        for (BomDto eBomItem : bomDto.getChildren()) {
            if (eBomItem.getChildren().size() == 0) {
                leafItemsCount = leafItemsCount + eBomItem.getQuantity();
            } else {
                leafItemsCount = visitEbomItemForQuantity(eBomItem, leafItemsCount);
            }
        }
        return leafItemsCount;
    }

    private Integer visitMbomItemForQuantity(MBOMItemDto bomDto, Integer leafItemsCount) {
        for (MBOMItemDto eBomItem : bomDto.getChildren()) {
            if (eBomItem.getChildren().size() == 0) {
                leafItemsCount = leafItemsCount + eBomItem.getQuantity();
            } else {
                leafItemsCount = visitMbomItemForQuantity(eBomItem, leafItemsCount);
            }
        }
        return leafItemsCount;
    }

    @Transactional
    private void releaseItemMcoAffectedItems(PLMMCO mco) {
        List<PLMMCOProductAffectedItem> productAffectedItems = mcoProductAffectedItemRepository.findByMco(mco.getId());
        productAffectedItems.forEach(plmmcoProductAffectedItem -> {
            MESMBOMRevision mesmbomRevision = mesMBOMRevisionRepository.findOne(plmmcoProductAffectedItem.getToItem());
            MESMBOM mesmbom = mesMBOMRepository.findOne(mesmbomRevision.getMaster());

            if (mco.getStatusType().equals(WorkflowStatusType.RELEASED)) {
                checkMBOMRevisionBom(mesmbomRevision);
                mesmbomRevision.setReleased(true);
                mesmbomRevision.setReleasedDate(new Date());
                mesmbomRevision = mesMBOMRevisionRepository.save(mesmbomRevision);

                mesmbom.setLatestReleasedRevision(mesmbomRevision.getId());
                mesmbom = mesMBOMRepository.save(mesmbom);
            }

            if (mco.getStatusType().equals(WorkflowStatusType.REJECTED)) {
                mesmbomRevision.setRejected(true);
                mesmbomRevision.setReleasedDate(new Date());
                mesmbomRevision = mesMBOMRevisionRepository.save(mesmbomRevision);
            }
        });
    }

    private void releaseMfrPartMcoAffectedItems(PLMMCO mco) {
        List<PLMMCOAffectedItem> affectedItems = mcoAffectedItemRepository.findByMco(mco.getId());

        for (PLMMCOAffectedItem affectedItem : affectedItems) {
            List<Integer> itemIds = new ArrayList<>();
            PLMManufacturerPartHistory manufacturerPartHistory = manufacturerPartHistoryRepository.findByAffectedPart(affectedItem.getId());
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(affectedItem.getMaterial());
            if (affectedItem.getChangeType().equals(MCOChangeType.REMOVED)) {
                List<PLMItemManufacturerPart> itemManufacturerParts = itemManufacturerPartRepository.findByManufacturerPart(manufacturerPart);

                for (PLMItemManufacturerPart itemManufacturerPart : itemManufacturerParts) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemManufacturerPart.getItem());
                    PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());
                    if (plmItem.getLatestRevision().equals(itemRevision.getId())) {
                        itemIds.add(itemManufacturerPart.getItem());
                    }
                    itemManufacturerPartRepository.delete(itemManufacturerPart.getId());
                }
            } else {
                List<PLMItemManufacturerPart> itemManufacturerParts = itemManufacturerPartRepository.findByManufacturerPart(manufacturerPart);
                PLMManufacturerPart replacementPart = manufacturerPartRepository.findOne(affectedItem.getReplacement());
                for (PLMItemManufacturerPart itemManufacturerPart : itemManufacturerParts) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemManufacturerPart.getItem());
                    PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());
                    PLMItemManufacturerPart existManufacturerPart = itemManufacturerPartRepository.findByItemAndManufacturerPart(itemRevision.getId(), replacementPart);
                    if (existManufacturerPart == null) {
                        itemManufacturerPart.setManufacturerPart(replacementPart);
                        itemManufacturerPart = itemManufacturerPartRepository.save(itemManufacturerPart);
                    } else {
                        itemManufacturerPartRepository.delete(itemManufacturerPart.getId());
                    }
                    if (plmItem.getLatestRevision().equals(itemRevision.getId())) {
                        itemIds.add(itemManufacturerPart.getItem());
                    }
                }
            }
            if (manufacturerPartHistory != null) {
                Integer[] intArray = new Integer[itemIds.size()];
                intArray = itemIds.toArray(intArray);
                manufacturerPartHistory.setItemSources(intArray);
                manufacturerPartHistoryRepository.save(manufacturerPartHistory);
            }

            if (mco.getQcr() != null) {
                List<PQMQCRProblemMaterial> problemMaterials = qcrProblemMaterialRepository.findByQcr(mco.getQcr());
                Boolean notImplemented = false;
                for (PQMQCRProblemMaterial problemMaterial : problemMaterials) {
                    if (problemMaterial.getMaterial().getId().equals(affectedItem.getMaterial())) {
                        problemMaterial.setIsImplemented(true);
                        problemMaterial.setImplementedDate(mco.getReleasedDate());
                        problemMaterial = qcrProblemMaterialRepository.save(problemMaterial);
                    }

                    if (!problemMaterial.getIsImplemented()) {
                        notImplemented = true;
                    }
                }

                if (!notImplemented) {
                    PQMQCR pqmqcr = qcrRepository.findOne(mco.getQcr());
                    pqmqcr.setIsImplemented(true);
                    pqmqcr.setImplementedDate(mco.getReleasedDate());
                    pqmqcr = qcrRepository.save(pqmqcr);


                    List<PQMQCRProblemMaterial> pqmqcrProblemMaterials = qcrProblemMaterialRepository.findByQcr(mco.getQcr());

                    for (PQMQCRProblemMaterial pqmqcrProblemMaterial : pqmqcrProblemMaterials) {
                        Boolean notImplementedNcr = false;
                        List<PQMQCRAggregateNCR> aggregateNCRList = qcrAggregateNCRRepository.findByQcr(mco.getQcr());
                        for (PQMQCRAggregateNCR aggregateNCR : aggregateNCRList) {
                            List<PQMNCRProblemItem> problemItems = ncrProblemItemRepository.findByNcr(aggregateNCR.getNcr().getId());

                            for (PQMNCRProblemItem problemItem : problemItems) {
                                if (problemItem.getMaterial().getId().equals(pqmqcrProblemMaterial.getMaterial().getId())) {
                                    problemItem.setIsImplemented(true);
                                    problemItem.setImplementedDate(mco.getReleasedDate());
                                    problemItem = ncrProblemItemRepository.save(problemItem);
                                }

                                if (!problemItem.getIsImplemented()) {
                                    notImplementedNcr = true;
                                }
                            }

                            if (!notImplementedNcr) {
                                PQMNCR pqmncr = ncrRepository.findOne(aggregateNCR.getNcr().getId());
                                pqmncr.setIsImplemented(true);
                                pqmncr.setImplementedDate(mco.getReleasedDate());
                                pqmncr = ncrRepository.save(pqmncr);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
        mcoRepository.delete(id);
    }

    @Override
    @Transactional
    @PostAuthorize("hasPermission(returnObject,'view')")
    public PLMMCO get(Integer id) {
        PLMMCO plmmco = mcoRepository.findOne(id);
        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(plmmco.getId());
        if (workflow != null) {
            if (workflow.getStart() != null) {
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
                if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    plmmco.setStartWorkflow(true);
                }
            }
        }
        return plmmco;
    }

    @Transactional
    public MCODetails getMCODetails(Integer id) {
        PLMMCO plmmco = mcoRepository.findOne(id);
        MCODetails mcoDetails = new MCODetails();
        mcoDetails.setId(plmmco.getId());
        mcoDetails.setMcoNumber(plmmco.getMcoNumber());
        mcoDetails.setMcoType(plmmco.getMcoType().getName());
        mcoDetails.setTitle(plmmco.getTitle());
        mcoDetails.setDescription(plmmco.getDescription());
        mcoDetails.setReasonForChange(plmmco.getReasonForChange());
        mcoDetails.setStatus(plmmco.getStatus());
        mcoDetails.setChangeAnalyst(personRepository.findOne(plmmco.getChangeAnalyst()).getFullName());
        mcoDetails.setCreatedBy(personRepository.findOne(plmmco.getCreatedBy()).getFullName());
        mcoDetails.setModifiedBy(personRepository.findOne(plmmco.getModifiedBy()).getFullName());
        mcoDetails.setCreatedDate(plmmco.getCreatedDate());
        mcoDetails.setModifiedDate(plmmco.getModifiedDate());
        mcoDetails.setWorkflow(plmmco.getWorkflow());
        return mcoDetails;
    }

    @Override
    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMMCO> getAll() {
        return mcoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMMCO> findMultiple(List<Integer> ids) {
        return mcoRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMMCO> getAllMCOs(Pageable pageable, MCOCriteria mcoCriteria) {
        Predicate predicate = mcoPredicateBuilder.build(mcoCriteria, QPLMMCO.pLMMCO);
        Page<PLMMCO> mcos = mcoRepository.findAll(predicate, pageable);
        mcos.getContent().forEach(mco -> {
            if (mco.getWorkflow() != null) {
                PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(mco.getWorkflow());
                if (plmWorkflow != null) {
                    mco.setOnHold(plmWorkflow.getOnhold());
                }
            }
        });
        return mcos;
    }
    // @PostFilter("hasPermission(filterObject,'view')")
    // public Page<PLMMCO> freeTextSearch(Pageable pageable, MCOCriteria criteria) {
    //     Predicate predicate = mcoPredicateBuilder.build(criteria, QPLMMCO.pLMMCO);
    //     return mcoRepository.findAll(predicate, pageable);
    // }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMItemMCO> getAllItemMCOs(Pageable pageable, ItemMCOCriteria mcoCriteria) {
        Predicate predicate = itemMcoPredicateBuilder.build(mcoCriteria, QPLMItemMCO.pLMItemMCO);
        Page<PLMItemMCO> mcos = itemMCORepository.findAll(predicate, pageable);
        mcos.getContent().forEach(mco -> {
            WorkflowStatusDTO workFlowStatusDto = workflowService.setWorkflowStatusSettings(mco.getId());
            mco.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            mco.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            mco.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
            mco.setOnHold(workFlowStatusDto.getOnHold());
            mco.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            mco.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());
        });
        return mcos;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMManufacturerMCO> getAllManufacturerMCOs(Pageable pageable, ManufacturerMCOCriteria mcoCriteria) {
        Predicate predicate = manufacturerMCOPredicateBuilder.build(mcoCriteria, QPLMManufacturerMCO.pLMManufacturerMCO);
        Page<PLMManufacturerMCO> mcos = manufacturerMCORepository.findAll(predicate, pageable);
        mcos.getContent().forEach(mco -> {
            WorkflowStatusDTO workFlowStatusDto = workflowService.setWorkflowStatusSettings(mco.getId());
            mco.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            mco.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            mco.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
            mco.setOnHold(workFlowStatusDto.getOnHold());
            mco.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            mco.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());
        });
        return mcos;
    }

    @Transactional
    public PLMWorkflow attachDCRWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMMCO mco = mcoRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (mco != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(mco.getObjectType(), mco.getId(), wfDef);
            mco.setWorkflow(workflow.getId());
            mco = mcoRepository.save(mco);
            applicationEventPublisher.publishEvent(new MCOEvents.MCOWorkflowChangeEvent(mco, null, workflow));
        }
        return workflow;
    }


    @Transactional
    public PLMMCOAffectedItem createAffectedItem(Integer id, PLMMCOAffectedItem item) {
        PLMMCO plmmco = mcoRepository.findOne(id);
        if (item.getChangeType().equals(MCOChangeType.REMOVED)) {
            item.setReplacement(null);
        }
        item = mcoAffectedItemRepository.save(item);
        PLMManufacturerPartHistory partHistory = manufacturerPartHistoryRepository.findByAffectedPart(item.getId());
        if (partHistory == null) {
            PLMManufacturerPartHistory manufacturerPartHistory = new PLMManufacturerPartHistory();
            manufacturerPartHistory.setAffectedPart(item.getId());
            manufacturerPartHistoryRepository.save(manufacturerPartHistory);
        }
        List<PLMMCOAffectedItem> affectedItems = new ArrayList<>();
        affectedItems.add(item);
        applicationEventPublisher.publishEvent(new MCOEvents.MCOAffectedItemAddedEvent(plmmco, affectedItems));
        return item;
    }

    @Transactional
    public List<PLMMCOAffectedItem> createAffectedItems(Integer id, List<PLMMCOAffectedItem> affectedItems) {
        PLMMCO plmmco = mcoRepository.findOne(id);
        for (PLMMCOAffectedItem item : affectedItems) {
            if (item.getChangeType().equals(MCOChangeType.REMOVED)) {
                item.setReplacement(null);
            }
            item = mcoAffectedItemRepository.save(item);
            PLMManufacturerPartHistory partHistory = manufacturerPartHistoryRepository.findByAffectedPart(item.getId());
            if (partHistory == null) {
                PLMManufacturerPartHistory manufacturerPartHistory = new PLMManufacturerPartHistory();
                manufacturerPartHistory.setAffectedPart(item.getId());
                manufacturerPartHistoryRepository.save(manufacturerPartHistory);
            }
        }
        applicationEventPublisher.publishEvent(new MCOEvents.MCOAffectedItemAddedEvent(plmmco, affectedItems));
        return affectedItems;
    }

    @Transactional
    public PLMMCOAffectedItem updateAffectedItem(Integer id, PLMMCOAffectedItem item) {
        PLMMCO plmmco = mcoRepository.findOne(id);
        PLMMCOAffectedItem oldAffectedItem = JsonUtils.cloneEntity(mcoAffectedItemRepository.findOne(item.getId()), PLMMCOAffectedItem.class);
        if (item.getChangeType().equals(MCOChangeType.REMOVED)) {
            item.setReplacement(null);
        }
        item = mcoAffectedItemRepository.save(item);
        applicationEventPublisher.publishEvent(new MCOEvents.MCOAffectedItemUpdatedEvent(plmmco, oldAffectedItem, item));
        PLMManufacturerPartHistory partHistory = manufacturerPartHistoryRepository.findByAffectedPart(item.getId());
        if (partHistory == null) {
            PLMManufacturerPartHistory manufacturerPartHistory = new PLMManufacturerPartHistory();
            manufacturerPartHistory.setAffectedPart(item.getId());
            manufacturerPartHistoryRepository.save(manufacturerPartHistory);
        }
        return item;
    }

    public List<Person> getChangeAnalysts(String type) {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        if (type.equals("ITEMMCO")) {
            List<PLMItemMCO> plmItemMcos = itemMCORepository.findAll();
            plmItemMcos.forEach(plmItemMco -> {
                integers.add(plmItemMco.getChangeAnalyst());
            });

        } else {
            List<PLMManufacturerMCO> plmMfrs = manufacturerMCORepository.findAll();
            plmMfrs.forEach(plmMfr -> {
                integers.add(plmMfr.getChangeAnalyst());
            });
        }
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }

    public List<String> getStatus(String type) {
        List<String> status = new ArrayList<>();
        if (type.equals("ITEMMCO")) {
            // List<PLMItemMCO> plmItemMcos = itemMCORepository.findAll();
            List<String> statuses = itemMCORepository.getStatuses();
            statuses.forEach(status1 -> {
                status.add(status1);
            });
        } else {
            // List<PLMManufacturerMCO> plmMfrs = manufacturerMCORepository.findAll();
            List<String> statuses = manufacturerMCORepository.getStatuses();
            statuses.forEach(status1 -> {
                status.add(status1);
            });
        }
        return status;
    }


    @Transactional(readOnly = true)
    public List<MCOAffecteditemsDto> getAffectedItem(Integer mcoId) {
        List<MCOAffecteditemsDto> dtos = new ArrayList<>();
        PLMMCO plmmco = mcoRepository.findOne(mcoId);
        List<PLMMCOAffectedItem> affectedItems = mcoAffectedItemRepository.findByMco(mcoId);
        for (PLMMCOAffectedItem affectedItem : affectedItems) {
            MCOAffecteditemsDto affecteditemsDto = new MCOAffecteditemsDto();
            if (plmmco.getQcr() != null) {
                PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(affectedItem.getMaterial());
                PQMQCRProblemMaterial pqmqcrProblemMaterial = qcrProblemMaterialRepository.findByQcrAndMaterial(plmmco.getQcr(), manufacturerPart);
                if (pqmqcrProblemMaterial != null) {
                    affecteditemsDto.setQcrItem(true);
                }
            }
            affecteditemsDto.setId(affectedItem.getId());
            affecteditemsDto.setMaterial(affectedItem.getMaterial());
            affecteditemsDto.setNotes(affectedItem.getNotes());
            affecteditemsDto.setMco(affectedItem.getMco());

            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(affectedItem.getMaterial());
            if (manufacturerPart != null) {
                affecteditemsDto.setMaterialNumber(manufacturerPart.getPartNumber());
                affecteditemsDto.setMaterialType(manufacturerPart.getMfrPartType().getName());
                affecteditemsDto.setMaterialName(manufacturerPart.getPartName());
                affecteditemsDto.setMfrId(manufacturerPart.getManufacturer());
            }

            if (affectedItem.getReplacement() != null) {
                PLMManufacturerPart replacementPart = manufacturerPartRepository.findOne(affectedItem.getReplacement());
                if (replacementPart != null) {
                    affecteditemsDto.setReplacementName(replacementPart.getPartName());
                    affecteditemsDto.setReplacementNumber(replacementPart.getPartNumber());
                    affecteditemsDto.setReplacementType(replacementPart.getMfrPartType().getName());
                }
            }
            affecteditemsDto.setChangeType(affectedItem.getChangeType());
            affecteditemsDto.setType(affectedItem.getChangeType().name());
            dtos.add(affecteditemsDto);
        }

        return dtos;
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemMCO> findByMCOsByMbomRevision(Integer mbomRevisionId) {
        MESMBOMRevision mesmbomRevision = mesMBOMRevisionRepository.findOne(mbomRevisionId);
        List<Integer> revisionIds = mesMBOMRevisionRepository.getRevisionIdsByMaster(mesmbomRevision.getMaster());
        List<PLMItemMCO> mcos = new LinkedList<>();
        List<PLMMCOProductAffectedItem> affectedItems = new ArrayList<>();
        for (Integer revisionId : revisionIds) {
            affectedItems.addAll(mcoProductAffectedItemRepository.findByItem(revisionId));
            affectedItems.addAll(mcoProductAffectedItemRepository.findByToItem(revisionId));
        }
        List<Integer> mcoIds = new ArrayList<>();
        for (PLMMCOProductAffectedItem item : affectedItems) {
            PLMItemMCO mco = itemMCORepository.findOne(item.getMco());
            if (mco != null) {
                if (mcoIds.indexOf(mco.getId()) == -1) {
                    mco.setFromRev(item.getFromRevision());
                    mco.setToRev(item.getToRevision());
                    mcoIds.add(mco.getId());
                    mcos.add(mco);
                }
            }
        }
        return mcos;
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
    public PLMMCORelatedItem createMcoRelatedItem(PLMMCORelatedItem relatedItem) {
        relatedItem = mcoRelatedItemRepository.save(relatedItem);
        PLMMCO plmmco = mcoRepository.findOne(relatedItem.getMco());
        List<PLMMCORelatedItem> relatedItems = new ArrayList<>();
        relatedItems.add(relatedItem);
        applicationEventPublisher.publishEvent(new MCOEvents.MCORelatedItemAddedEvent(plmmco, relatedItems));
        return relatedItem;
    }

    @Transactional
    public PLMMCORelatedItem updateMcoRelatedItem(PLMMCORelatedItem relatedItem) {
        PLMMCORelatedItem oldRelatedItem = JsonUtils.cloneEntity(mcoRelatedItemRepository.findOne(relatedItem.getId()), PLMMCORelatedItem.class);
        relatedItem = mcoRelatedItemRepository.save(relatedItem);
        PLMMCO plmmco = mcoRepository.findOne(relatedItem.getMco());
        applicationEventPublisher.publishEvent(new MCOEvents.MCORelatedItemUpdatedEvent(plmmco, oldRelatedItem, relatedItem));
        return relatedItem;
    }

    @Transactional
    public List<PLMMCORelatedItem> createMcoRelatedItems(Integer mcoId, List<PLMMCORelatedItem> relatedItems) {
        PLMMCO plmmco = mcoRepository.findOne(mcoId);
        relatedItems = mcoRelatedItemRepository.save(relatedItems);
        applicationEventPublisher.publishEvent(new MCOEvents.MCORelatedItemAddedEvent(plmmco, relatedItems));
        return relatedItems;
    }

    @Transactional(readOnly = true)
    public List<PLMMCORelatedItem> getMcoRelatedItems(Integer mcr) {
        return mcoRelatedItemRepository.findByMco(mcr);
    }

    @Transactional
    public void deleteMcoAffectedItem(Integer id) {
        PLMMCOAffectedItem affectedItem = mcoAffectedItemRepository.findOne(id);
        PLMMCO plmmco = mcoRepository.findOne(affectedItem.getMco());
        applicationEventPublisher.publishEvent(new MCOEvents.MCOAffectedItemDeletedEvent(plmmco, affectedItem));
        mcoAffectedItemRepository.delete(id);
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','relateditem')")
    public void deleteMcoRelatedItem(Integer id) {
        PLMMCORelatedItem affectedItem = mcoRelatedItemRepository.findOne(id);
        PLMMCO plmmco = mcoRepository.findOne(affectedItem.getMco());
        applicationEventPublisher.publishEvent(new MCOEvents.MCORelatedItemDeletedEvent(plmmco, affectedItem));
        mcoRelatedItemRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getMcoDetailsCount(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        PLMMCO plmmco = mcoRepository.findOne(id);
        if (plmmco.getMcoType().getMcoType().equals(MCOType.ITEMMCO)) {
            detailsDto.setAffectedItems(mcoProductAffectedItemRepository.getMCOAffectedItemsCount(id));
        } else {
            detailsDto.setAffectedItems(mcoAffectedItemRepository.getMCOAffectedItemsCount(id));
        }
        detailsDto.setRelatedItems(mcoRelatedItemRepository.findByMco(id).size());
        detailsDto.setItemFiles(changeFileRepository.findByChangeAndFileTypeAndLatestTrueOrderByModifiedDateDesc(id, "FILE").size());
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        List<PLMMCOAffectedItem> affectedItems = mcoAffectedItemRepository.findByMco(id);
        if (affectedItems.size() > 0) {
            affectedItems.forEach(plmmcoAffectedItem -> {
                if (plmmcoAffectedItem.getChangeType().equals(MCOChangeType.REPLACED) && plmmcoAffectedItem.getReplacement() == null) {
                    detailsDto.setReplacementPartsExist(false);
                }
            });
        }
        return detailsDto;
    }

    public List<PLMMCO> findByAffectedItem(Integer mfrPartId) {
        List<PLMMCOAffectedItem> plmmcoAffectedItems = mcoAffectedItemRepository.findByReplacement(mfrPartId);
        List<PLMMCO> plmmcos = new ArrayList<>();
        for (PLMMCOAffectedItem affectedItem : plmmcoAffectedItems) {
            plmmcos.add(mcoRepository.findOne(affectedItem.getMco()));
        }
        return plmmcos;
    }

    @Transactional(readOnly = true)
    public List<MCOAffecteditemsDto> getAmlParts(Integer id) {
        List<MCOAffecteditemsDto> affecteditemsDtos = new ArrayList<>();
        List<PLMMCOAffectedItem> affectedItems = mcoAffectedItemRepository.findByMco(id);
        affectedItems.forEach(plmmcoAffectedItem -> {
            MCOAffecteditemsDto mcoAffecteditemsDto = new MCOAffecteditemsDto();
            PLMManufacturerPartHistory partHistory = manufacturerPartHistoryRepository.findByAffectedPart(plmmcoAffectedItem.getId());
            if (partHistory != null) {
                mcoAffecteditemsDto.setMco(plmmcoAffectedItem.getMco());
                mcoAffecteditemsDto.setReplacement(plmmcoAffectedItem.getReplacement());
                mcoAffecteditemsDto.setMaterial(plmmcoAffectedItem.getMaterial());
                PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(plmmcoAffectedItem.getMaterial());
                mcoAffecteditemsDto.setMfrId(manufacturerPart.getManufacturer());
                mcoAffecteditemsDto.setMaterialName(manufacturerPart.getPartName());
                mcoAffecteditemsDto.setMaterialNumber(manufacturerPart.getPartNumber());
                mcoAffecteditemsDto.setManufacturer(manufacturerRepository.findOne(manufacturerPart.getManufacturer()).getName());
                mcoAffecteditemsDto.setChangeType(plmmcoAffectedItem.getChangeType());
                if (plmmcoAffectedItem.getReplacement() != null) {
                    PLMManufacturerPart part = manufacturerPartRepository.findOne(plmmcoAffectedItem.getReplacement());
                    mcoAffecteditemsDto.setReplacementName(part.getPartName());
                    mcoAffecteditemsDto.setReplacementNumber(part.getPartNumber());
                    mcoAffecteditemsDto.setReplaceMfr(manufacturerRepository.findOne(part.getManufacturer()).getName());
                }
                for (int i = 0; i < partHistory.getItemSources().length; i++) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(partHistory.getItemSources()[i]);
                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                    item.setRev(itemRevision);
                    mcoAffecteditemsDto.getItems().add(item);
                }
                affecteditemsDtos.add(mcoAffecteditemsDto);

            }

        });
        return affecteditemsDtos;
    }

    @Transactional
    private PLMMCO createMBOMRevisions(PLMMCO mco) {
        List<PLMMCOProductAffectedItem> productAffectedItems = mcoProductAffectedItemRepository.findByMco(mco.getId());
        for (PLMMCOProductAffectedItem productAffectedItem : productAffectedItems) {
            MESMBOMRevision mesmbomRevision = mesMBOMRevisionRepository.findOne(productAffectedItem.getItem());
            MESMBOMRevision revision = reviseMBOMRevision(mesmbomRevision, productAffectedItem.getToRevision());
            productAffectedItem.setToItem(revision.getId());
        }
        if (productAffectedItems.size() > 0) {
            mcoProductAffectedItemRepository.save(productAffectedItems);
        }
        mco.setRevisionsCreated(true);
        return mcoRepository.save(mco);
    }

    @Transactional
    public MESMBOMRevision reviseMBOMRevision(MESMBOMRevision revision, String nextRev) {
        MESMBOM mesmbom = mesMBOMRepository.findOne(revision.getMaster());
        if (nextRev == null) {
            nextRev = getNextRevisionSequence(mesmbom);
        }
        if (nextRev != null) {
            MESMBOMRevision copy = createNextRev(revision, nextRev);
            mesmbom.setLatestRevision(copy.getId());
            mesMBOMRepository.save(mesmbom);
            //Copy the related
            copyBOP(revision, copy);
            copyBOM(revision, copy);
            copyFolderStructure(revision, copy);
            return copy;
        } else {
            throw new CassiniException(messageSource.getMessage("could_not_retrieve_next_revision_sequence",
                    null, "Could not retrieve next revision sequence", LocaleContextHolder.getLocale()));
        }
    }

    public void copyFolderStructure(MESMBOMRevision oldRevision, MESMBOMRevision newRevision) {
        List<MESMBOMFile> mesmbomFiles = mbomFileRepository.findByMbomRevisionAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(oldRevision.getId());
        List<PLMObjectDocument> objectDocuments = objectDocumentRepository.findByObject(oldRevision.getId());
        List<PLMObjectDocument> documents = new ArrayList<>();
        for (PLMObjectDocument plmObjectDocument : objectDocuments) {
            PLMObjectDocument objectDocument = new PLMObjectDocument();
            objectDocument.setDocument(plmObjectDocument.getDocument());
            objectDocument.setObject(newRevision.getId());
            objectDocument.setFolder(plmObjectDocument.getFolder());
            objectDocument.setDocumentType(plmObjectDocument.getDocumentType());
            documents.add(objectDocument);
        }
        if (documents.size() > 0) {
            objectDocumentRepository.save(documents);
        }
        for (MESMBOMFile mesmbomFile : mesmbomFiles) {
            mbomFileService.copyMBOMFile(oldRevision, newRevision, mesmbomFile);
        }
    }

    private void copyBOP(MESMBOMRevision oldRevision, MESMBOMRevision newRevision) {
        List<MESBOPRevision> mesbopRevisions = bopRevisionRepository.findByMbomRevisionAndReleasedTrueOrderByIdDesc(oldRevision.getId());
        if (mesbopRevisions.size() > 0) {
            bopService.reviseBOPRevision(mesbopRevisions.get(0), null, newRevision.getId());
        } else {
            mesbopRevisions = bopRevisionRepository.findByMbomRevisionAndReleasedFalseAndRejectedFalseOrderByIdDesc(oldRevision.getId());
            if (mesbopRevisions.size() > 0) {
                bopService.reviseBOPRevision(mesbopRevisions.get(0), null, newRevision.getId());
            }
        }
    }

    @PreAuthorize("hasPermission(#mbom,'create')")
    private MESMBOMRevision createNextRev(MESMBOMRevision mesmbomRevision, String nextRev) {
        Integer notReleasedDocumentCount = objectDocumentRepository.getNotReleasedDocumentsCount(mesmbomRevision.getId());
        MESMBOM mesmbom = mesMBOMRepository.findOne(mesmbomRevision.getMaster());
        if (notReleasedDocumentCount > 0) {
            String message = messageSource.getMessage("mbom_has_unreleased_documents", null, "[{0}] MBOM has some unreleased documents", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", mesmbom.getNumber());
            throw new CassiniException(result);
        }
        MESMBOMRevision copy = new MESMBOMRevision();
        MESMBOMType mesmbomType = mbomTypeRepository.findOne(mesmbom.getType().getId());
        PLMLifeCyclePhase lifeCyclePhase = mesmbomType.getLifecycle().getPhaseByType(LifeCyclePhaseType.PRELIMINARY);
        copy.setMaster(mesmbomRevision.getMaster());
        copy.setRevision(nextRev);
        copy.setLifeCyclePhase(lifeCyclePhase);
        copy.setItemRevision(mesmbomRevision.getItemRevision());
        MESMBOMRevision revision = mesMBOMRevisionRepository.save(copy);
        MESMBOMRevisionStatusHistory statusHistory = new MESMBOMRevisionStatusHistory();
        statusHistory.setMbomRevision(revision.getId());
        statusHistory.setOldStatus(revision.getLifeCyclePhase());
        statusHistory.setNewStatus(revision.getLifeCyclePhase());
        statusHistory.setUpdatedBy(mesmbomRevision.getCreatedBy());
        statusHistory = mbomRevisionStatusHistoryRepository.save(statusHistory);

        return revision;
    }


    public String getNextRevisionSequence(MESMBOM mesmbom) {
        String nextRev = null;
        List<String> revs = getRevisions(mesmbom);
        String lastRev = revs.get(revs.size() - 1);
        Lov lov = mesmbom.getType().getRevisionSequence();
        String[] values = lov.getValues();
        int lastIndex = -1;
        for (int i = 0; i < values.length; i++) {
            String rev = values[i];
            if (rev.equalsIgnoreCase(lastRev)) {
                lastIndex = i;
                break;
            }
        }
        if (lastIndex != -1 && lastIndex < values.length) {
            nextRev = values[lastIndex + 1];
        }
        return nextRev;
    }

    private List<String> getRevisions(MESMBOM item) {
        List<String> revs = new ArrayList<>();
        MESMBOMRevision revisions = mesMBOMRevisionRepository.findOne(item.getLatestRevision());
        String rev = revisions.getRevision();
        if (!revs.contains(rev)) {
            revs.add(rev);
        }
        Collections.sort(revs);
        return revs;
    }

    private void copyBOM(MESMBOMRevision revision, MESMBOMRevision copy) {
        List<MESBOMItem> mesbomItems = mesbomItemRepository.findByMbomRevisionAndParentIsNullOrderByCreatedDateAsc(revision.getId());
        for (MESBOMItem mesbomItem : mesbomItems) {
            MESBOMItem item = JsonUtils.cloneEntity(mesbomItem, MESBOMItem.class);
            item.setId(null);
            item.setMbomRevision(copy.getId());
            item = mesbomItemRepository.save(item);

            List<MESBOMItem> children = mesbomItemRepository.findByParentOrderByCreatedDateAsc(mesbomItem.getId());
            for (MESBOMItem child : children) {
                MESBOMItem bomItem = JsonUtils.cloneEntity(child, MESBOMItem.class);
                bomItem.setId(null);
                bomItem.setMbomRevision(copy.getId());
                bomItem.setParent(item.getId());
                bomItem = mesbomItemRepository.save(bomItem);
                copyBOMItemChildren(child.getId(), bomItem.getId(), copy.getId());
            }
        }
    }

    private void copyBOMItemChildren(Integer itemId, Integer bomItemId, Integer mbomRevision) {
        List<MESBOMItem> children = mesbomItemRepository.findByParentOrderByCreatedDateAsc(itemId);
        for (MESBOMItem child : children) {
            MESBOMItem bomItem = JsonUtils.cloneEntity(child, MESBOMItem.class);
            bomItem.setId(null);
            bomItem.setMbomRevision(mbomRevision);
            bomItem.setParent(bomItemId);
            bomItem = mesbomItemRepository.save(bomItem);
            copyBOMItemChildren(child.getId(), bomItem.getId(), mbomRevision);
        }
    }

    @Transactional
    public PLMMCO createRevisions(PLMMCO mco, Boolean start, Boolean finish, Integer statusId) {
        if (mco.getRevisionCreationType() != null && !mco.getRevisionsCreated()) {
            if (start) {
                if (mco.getRevisionCreationType().equals(RevisionCreationType.WORKFLOW_START)) {
                    mco = createMBOMRevisions(mco);
                }
            } else if (finish) {
                mco = createMBOMRevisions(mco);
            } else {
                if (statusId.equals(mco.getWorkflowStatus())) {
                    mco = createMBOMRevisions(mco);
                }
            }
        }

        return mco;
    }

    @EventListener(condition = "(#event.attachedToType.name() == 'OEMPARTMCO' || #event.attachedToType.name() == 'ITEMMCO') && #event.attachedToObject.changeType.name() == 'MCO'")
    public void mcoWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PLMMCO mco = (PLMMCO) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus workflowStatus = workflowStatusRepository.findOne(plmWorkflow.getCurrentStatus());
        if (mco.getMcoType().getMcoType().equals(MCOType.ITEMMCO)) {
            mco = createRevisions(mco, true, false, null);
        }
        workflowEventService.workflowStart("MCO", plmWorkflow);
        workflowEventService.workflowActivityStart(mco.getChangeType().toString(), plmWorkflow, workflowStatus);
        applicationEventPublisher.publishEvent(new MCOEvents.MCOWorkflowStartedEvent(mco, plmWorkflow));
    }

    @EventListener(condition = "(#event.attachedToType.name() == 'OEMPARTMCO' || #event.attachedToType.name() == 'ITEMMCO') && #event.attachedToObject.changeType.name() == 'MCO'")
    public void mcoWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PLMMCO mco = (PLMMCO) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        mco = createRevisions(mco, false, false, fromStatus.getId());
        mco.setStatus(fromStatus.getName());
        mco.setStatusType(fromStatus.getType());
        update(mco);
        applicationEventPublisher.publishEvent(new MCOEvents.MCOWorkflowPromotedEvent(mco, plmWorkflow, fromStatus, toStatus));
        workflowEventService.workflowActivityFinish("MCO", plmWorkflow, fromStatus);
        if (toStatus != null) {
            workflowEventService.workflowActivityStart("MCO", plmWorkflow, toStatus);
        }
        if (fromStatus.getType() == WorkflowStatusType.RELEASED) {
            workflowEventService.workflowFinish("MCO", plmWorkflow);
        }
    }

    @EventListener(condition = "(#event.attachedToType.name() == 'OEMPARTMCO' || #event.attachedToType.name() == 'ITEMMCO') && #event.attachedToObject.changeType.name() == 'MCO'")
    public void mcoWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMMCO mco = (PLMMCO) event.getAttachedToObject();
        PLMWorkflowStatus toStatus = event.getToStatus();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        mco.setStatus(toStatus.getName());
        mco.setStatusType(toStatus.getType());
        mcoRepository.save(mco);
        applicationEventPublisher.publishEvent(new MCOEvents.MCOWorkflowDemotedEvent(mco, plmWorkflow, fromStatus, toStatus));
        workflowEventService.workflowActivityFinishDemote("MCO", plmWorkflow, toStatus);
        workflowEventService.workflowActivityStartDemote("MCO", plmWorkflow, fromStatus);
    }

    @EventListener(condition = "(#event.attachedToType.name() == 'OEMPARTMCO' || #event.attachedToType.name() == 'ITEMMCO') && #event.attachedToObject.changeType.name() == 'MCO'")
    public void mcoWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PLMMCO mco = (PLMMCO) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        mco = createRevisions(mco, false, true, fromStatus.getId());
        mco.setStatus(fromStatus.getName());
        mco.setStatusType(fromStatus.getType());
        update(mco);
        workflowEventService.workflowActivityFinish("MCO", plmWorkflow, fromStatus);
        workflowEventService.workflowFinish("MCO", plmWorkflow);
        applicationEventPublisher.publishEvent(new MCOEvents.MCOWorkflowFinishedEvent(mco, plmWorkflow));
    }

    @EventListener(condition = "(#event.attachedToType.name() == 'OEMPARTMCO' || #event.attachedToType.name() == 'ITEMMCO') && #event.attachedToObject.changeType.name() == 'MCO'")
    public void dcrWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PLMMCO mco = (PLMMCO) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new MCOEvents.MCOWorkflowHoldEvent(mco, plmWorkflow, fromStatus));
        workflowEventService.workflowHold("MCO", plmWorkflow);
    }

    @EventListener(condition = "(#event.attachedToType.name() == 'OEMPARTMCO' || #event.attachedToType.name() == 'ITEMMCO') && #event.attachedToObject.changeType.name() == 'MCO'")
    public void dcrWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PLMMCO mco = (PLMMCO) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new MCOEvents.MCOWorkflowUnholdEvent(mco, plmWorkflow, fromStatus));
        workflowEventService.workflowUnhold("MCO", plmWorkflow);
    }


    @Transactional
    @PreAuthorize("hasPermission(#itemMCO.id ,'edit')")
    public PLMItemMCO updateItemMco(PLMItemMCO itemMCO) {
        PLMMCO oldMco = JsonUtils.cloneEntity(mcoRepository.findOne(itemMCO.getId()), PLMMCO.class);
        itemMCO = itemMCORepository.save(itemMCO);
        PLMMCO plmmco = mcoRepository.findOne(itemMCO.getId());
        applicationEventPublisher.publishEvent(new MCOEvents.MCOBasicInfoUpdatedEvent(oldMco, plmmco));
        return itemMCO;
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMItemMCO getItemMco(Integer id) {
        PLMItemMCO itemMCO = itemMCORepository.findOne(id);
        // PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(itemMCO.getId());

        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto = workflowService.setWorkflowStatusSettings(itemMCO.getId());
        itemMCO.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        itemMCO.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        itemMCO.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());

        // if (workflow != null) {
        //     if (workflow.getStart() != null) {
        //         PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
        //         if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
        //             itemMCO.setStartWorkflow(true);
        //         }
        //     }
        // }
        return itemMCO;
    }

    @Transactional
    @PreAuthorize("hasPermission(#manufacturerMCO.id ,'edit')")
    public PLMManufacturerMCO updateMaterialMco(PLMManufacturerMCO manufacturerMCO) {
        PLMMCO oldMco = JsonUtils.cloneEntity(mcoRepository.findOne(manufacturerMCO.getId()), PLMMCO.class);
        manufacturerMCO = manufacturerMCORepository.save(manufacturerMCO);
        PLMMCO plmmco = mcoRepository.findOne(manufacturerMCO.getId());
        applicationEventPublisher.publishEvent(new MCOEvents.MCOBasicInfoUpdatedEvent(oldMco, plmmco));
        return manufacturerMCO;
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMManufacturerMCO getMaterialMco(Integer id) {
        PLMManufacturerMCO manufacturerMCO = manufacturerMCORepository.findOne(id);
        // PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(manufacturerMCO.getId());

        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto = workflowService.setWorkflowStatusSettings(manufacturerMCO.getId());
        manufacturerMCO.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        manufacturerMCO.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        manufacturerMCO.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());

        // if (workflow != null) {
        //     if (workflow.getStart() != null) {
        //         PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
        //         if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
        //             manufacturerMCO.setStartWorkflow(true);
        //         }
        //     }
        // }
        return manufacturerMCO;
    }

    @Transactional
    public PLMMCOProductAffectedItem createProductAffectedItem(Integer id, PLMMCOProductAffectedItem productAffectedItem) {
        PLMMCO plmmco = mcoRepository.findOne(id);
        productAffectedItem = mcoProductAffectedItemRepository.save(productAffectedItem);
        addAffectedItemToWorkflowEventData(plmmco, productAffectedItem);
       List<PLMMCOProductAffectedItem> productAffectedItems = new ArrayList<>();
       productAffectedItems.add(productAffectedItem);
       applicationEventPublisher.publishEvent(new MCOEvents.MCOProductAffectedItemAddedEvent(plmmco, productAffectedItems));
        return productAffectedItem;
    }

    @Transactional
    public List<PLMMCOProductAffectedItem> createProductAffectedItems(Integer id, List<PLMMCOProductAffectedItem> productAffectedItems) {
        PLMMCO plmmco = mcoRepository.findOne(id);
        for (PLMMCOProductAffectedItem productAffectedItem : productAffectedItems) {
            productAffectedItem = mcoProductAffectedItemRepository.save(productAffectedItem);
            addAffectedItemToWorkflowEventData(plmmco, productAffectedItem);
        }
        applicationEventPublisher.publishEvent(new MCOEvents.MCOProductAffectedItemAddedEvent(plmmco, productAffectedItems));
        return productAffectedItems;
    }

    @Transactional
    public PLMMCOProductAffectedItem updateProductAffectedItem(Integer id, PLMMCOProductAffectedItem mbom) {
        PLMMCO plmmco = mcoRepository.findOne(id);
        PLMMCOProductAffectedItem oldProductAffectedItem = JsonUtils.cloneEntity(mcoProductAffectedItemRepository.findOne(mbom.getId()), PLMMCOProductAffectedItem.class);
        mbom = mcoProductAffectedItemRepository.save(mbom);
        applicationEventPublisher.publishEvent(new MCOEvents.MCOProductAffectedItemUpdatedEvent(plmmco, oldProductAffectedItem, mbom));
        return mbom;
    }

    @Transactional
    public void deleteMcoProductAffectedItem(Integer id) {
        PLMMCOProductAffectedItem productAffectedItem = mcoProductAffectedItemRepository.findOne(id);
        PLMMCO plmmco = mcoRepository.findOne(productAffectedItem.getMco());
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowOrderByIdAsc(plmmco.getWorkflow());
        for (PLMWorkflowEvent workflowEvent : workflowEvents) {
            if (workflowEvent.getActionData() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    List<MCOProductAffectedItemDto> affectedItemDto = new LinkedList<>();
                    List<MCOProductAffectedItemDto> modifiedList = new LinkedList<>();
                    if (workflowEvent.getActionData() != null) {
                        affectedItemDto = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<MCOProductAffectedItemDto>>() {
                        });
                    }
                    for (MCOProductAffectedItemDto itemDto : affectedItemDto) {
                        if (!itemDto.getId().equals(productAffectedItem.getId())) {
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
        applicationEventPublisher.publishEvent(new MCOEvents.MCOProductAffectedItemDeletedEvent(plmmco, productAffectedItem));
        mcoProductAffectedItemRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<MCOProductAffectedItemDto> getProductAffectedItem(Integer mcoId) {
        List<MCOProductAffectedItemDto> dtos = new ArrayList<>();
        List<PLMMCOProductAffectedItem> productAffectedItems = mcoProductAffectedItemRepository.findByMco(mcoId);
        for (PLMMCOProductAffectedItem productAffectedItem : productAffectedItems) {
            MCOProductAffectedItemDto affectedMbomsDto = new MCOProductAffectedItemDto();
            affectedMbomsDto.setId(productAffectedItem.getId());
            affectedMbomsDto.setMco(productAffectedItem.getMco());
            affectedMbomsDto.setItem(productAffectedItem.getItem());
            affectedMbomsDto.setToItem(productAffectedItem.getToItem());
            affectedMbomsDto.setFromRevision(productAffectedItem.getFromRevision());
            affectedMbomsDto.setToRevision(productAffectedItem.getToRevision());
            affectedMbomsDto.setNotes(productAffectedItem.getNotes());
            affectedMbomsDto.setEffectiveDate(productAffectedItem.getEffectiveDate());
            MESMBOMRevision mesmbomRevision = mesMBOMRevisionRepository.findOne(productAffectedItem.getItem());
            if (mesmbomRevision != null) {
                MESMBOM mesMBOM = mesMBOMRepository.findOne(mesmbomRevision.getMaster());
                affectedMbomsDto.setNumber(mesMBOM.getNumber());
                affectedMbomsDto.setType(mesMBOM.getType().getName());
                affectedMbomsDto.setName(mesMBOM.getName());
                affectedMbomsDto.setDescription(mesMBOM.getDescription());

                MESMBOMRevision revision = mesMBOMRevisionRepository.findOne(mesMBOM.getLatestRevision());
                PLMItem item = itemRepository.findOne(mesMBOM.getItem());
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(revision.getItemRevision());
                affectedMbomsDto.setItemName(item.getItemName());
                affectedMbomsDto.setItemRevision(itemRevision.getRevision());
                mesMBOM.getType().getLifecycle().getPhases().forEach(plmLifeCyclePhase -> {
                    if (plmLifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                        affectedMbomsDto.getReleasedLifecyclePhases().add(plmLifeCyclePhase);
                    } else if (plmLifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.CANCELLED)) {
                        affectedMbomsDto.getRejectedLifecyclePhases().add(plmLifeCyclePhase);
                    } else if (!plmLifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.OBSOLETE)) {
                        affectedMbomsDto.getNormalLifecyclePhases().add(plmLifeCyclePhase);
                    }
                });
                affectedMbomsDto.setFromLifecyclePhase(mesmbomRevision.getLifeCyclePhase());
            }
            dtos.add(affectedMbomsDto);
        }
        return dtos;
    }

    @Transactional
    public void addAffectedItemToWorkflowEventData(PLMMCO mco, PLMMCOProductAffectedItem productAffectedItem) {
        PLMWorkflow workflow = workflowRepository.findOne(mco.getWorkflow());
        PLMWorkflowDefinition workflowDefinition = workFlowDefinitionRepository.findOne(workflow.getWorkflowRevision());
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowOrderByIdAsc(mco.getWorkflow());
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
                        List<MCOProductAffectedItemDto> affectedItemDto = new LinkedList<>();
                        if (workflowEvent.getActionData() != null) {
                            affectedItemDto = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<MCOProductAffectedItemDto>>() {
                            });
                        }

                        MESMBOMRevision mesmbomRevision = mesMBOMRevisionRepository.findOne(productAffectedItem.getItem());
                        MCOProductAffectedItemDto affectedMbomsDto = new MCOProductAffectedItemDto();
                        affectedMbomsDto.setId(productAffectedItem.getId());
                        affectedMbomsDto.setMco(productAffectedItem.getMco());
                        affectedMbomsDto.setItem(productAffectedItem.getItem());
                        affectedMbomsDto.setToItem(productAffectedItem.getToItem());
                        affectedMbomsDto.setFromRevision(productAffectedItem.getFromRevision());
                        affectedMbomsDto.setToRevision(productAffectedItem.getToRevision());
                        affectedMbomsDto.setNotes(productAffectedItem.getNotes());
                        affectedMbomsDto.setEffectiveDate(productAffectedItem.getEffectiveDate());
                        MESMBOM mesMBOM = mesMBOMRepository.findOne(mesmbomRevision.getMaster());
                        affectedMbomsDto.setNumber(mesMBOM.getNumber());
                        affectedMbomsDto.setType(mesMBOM.getType().getName());
                        affectedMbomsDto.setName(mesMBOM.getName());
                        affectedMbomsDto.setDescription(mesMBOM.getDescription());

                        MESMBOMRevision revision = mesMBOMRevisionRepository.findOne(mesMBOM.getLatestRevision());
                        PLMItem item = itemRepository.findOne(mesMBOM.getItem());
                        PLMItemRevision itemRevision = itemRevisionRepository.findOne(revision.getItemRevision());
                        affectedMbomsDto.setItemName(item.getItemName());
                        affectedMbomsDto.setItemRevision(itemRevision.getRevision());
                        affectedMbomsDto.setFromLifecyclePhase(mesmbomRevision.getLifeCyclePhase());

                        affectedMbomsDto.setToLifecyclePhase(lifeCyclePhaseHashMap.get(mesMBOM.getType().getLifecycle().getId()));
                        affectedItemDto.add(affectedMbomsDto);

                        workflowEvent.setActionData(objectMapper.writeValueAsString(affectedItemDto));
                        workflowEvent = plmWorkflowEventRepository.save(workflowEvent);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
