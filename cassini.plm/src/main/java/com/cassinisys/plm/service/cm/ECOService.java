package com.cassinisys.plm.service.cm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.*;
import com.cassinisys.plm.filtering.*;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.cm.dto.AffectedItemDto;
import com.cassinisys.plm.model.cm.dto.ChangeObjectsFilterDto;
import com.cassinisys.plm.model.cm.dto.VarianceRelatedItemDto;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.mobile.ECODetails;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.rm.RecentlyVisited;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.dto.DefinitionEventDto;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.rm.RecentlyVisitedRepository;
import com.cassinisys.plm.repo.wf.*;
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
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.TypedQuery;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by lakshmi on 1/3/2016.
 */
@Service
@Transactional
public class ECOService implements CrudService<PLMECO, Integer>,
        PageableService<PLMECO, Integer> {
    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private ECOAdvancedCriteria ecoAdvancedCriteria;
    @Autowired
    private ECOPredicateBuilder predicateBuilder;
    @Autowired
    private ChangePredicateBuilder changePredicateBuilder;
    @Autowired
    private DCOPredicateBuilder dcoPredicateBuilder;
    @Autowired
    private DCRPredicateBuilder dcrPredicateBuilder;
    @Autowired
    private ECRPredicateBuilder ecrPredicateBuilder;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private AffectedItemRepository affectedItemRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ChangeAttributeRepository changeAttributeRepository;
    @Autowired
    private ChangeTypeRepository changeTypeRepository;
    @Autowired
    private ChangeTypeAttributeRepository changeTypeAttributeRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private RecentlyVisitedRepository recentlyVisitedRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private ChangeFileRepository changeFileRepository;
    @Autowired
    private PLMWorkflowRepository workflowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private PLMWorkflowDefinitionStatusRepository plmWorkflowDefinitionStatusRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private ChangeRelatedItemRepository changeRelatedItemRepository;
    @Autowired
    private BomItemFilterPredicateBuilder bomItemFilterPredicateBuilder;
    @Autowired
    private DCRRepository dcrRepository;
    @Autowired
    private DCORepository dcoRepository;
    @Autowired
    private MCORepository mcoRepository;
    @Autowired
    private ECRAffectedItemRepository ecrAffectedItemRepository;
    @Autowired
    private ECRRepository ecrRepository;
    @Autowired
    private QCRProblemItemRepository qcrProblemItemRepository;
    @Autowired
    private QCRRepository qcrRepository;
    @Autowired
    private PRProblemItemRepository prProblemItemRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private QCRAggregatePRRepository qcrAggregatePRRepository;
    @Autowired
    private ECOECRRepository ecoecrRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ChangeRepository changeRepository;
    @Autowired
    private ItemRevisionStatusHistoryRepository revisionStatusHistoryRepository;
    @Autowired
    private BomService bomService;
    @Autowired
    private MessageSource messageSources;
    @Autowired
    private AffectedItemRepository ecoAffectedItemRepository;
    @Autowired
    private WorkflowDefinitionMasterRepository workflowDefinitionMasterRepository;
    @Autowired
    private VarianceRepository varianceRepository;
    @Autowired
    private ECOTypeRepository ecoTypeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private RelatedItemRepository relatedItemRepository;
    @Autowired
    private ECRPRRepository ecrprRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private DCRService dcrService;
    @Autowired
    private ECRService ecrService;
    @Autowired
    private MCOService mcoService;
    @Autowired
    private WorkflowEventService workflowEventService;
    @Autowired
    private PLMWorkflowEventRepository plmWorkflowEventRepository;
    @Autowired
    private PLMWorkflowDefinitionEventRepository plmWorkflowDefinitionEventRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#eco,'create')")
    public PLMECO create(PLMECO eco) {
        checkNotNull(eco);
        eco.setId(null);

        Integer workflowId = eco.getWorkflow();
        Integer workflowStatusId = eco.getWorkflowStatus();
        eco.setWorkflowStatus(null);
        eco.setWorkflow(null);
        autoNumberService.saveNextNumber(eco.getChangeClass().getAutoNumberSource().getId(), eco.getEcoNumber());
        eco = ecoRepository.save(eco);

        attachWorkflow(eco.getId(), workflowId, workflowStatusId);
        applicationEventPublisher.publishEvent(new ECOEvents.ECOCreatedEvent(eco));
        return eco;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#eco.id,'edit')")
    public PLMECO update(PLMECO eco) {
        checkNotNull(eco);
        PLMECO oldEco = JsonUtils.cloneEntity(ecoRepository.findOne(eco.getId()), PLMECO.class);
        if (eco.getStatusType() == WorkflowStatusType.RELEASED) {
            eco.setReleased(true);
            eco.setReleasedDate(new Date());
        } else if (eco.getStatusType() == WorkflowStatusType.REJECTED) {
            eco.setCancelled(true);
            eco.setCancelledDate(new Date());
        }
        eco = ecoRepository.save(eco);
        if (eco.getReleased().equals(true) || eco.getCancelled()) {
            List<PLMAffectedItem> affectedItems = affectedItemRepository.findByChange(eco.getId());
            if (affectedItems.size() != 0) {
                for (PLMAffectedItem affectedItem : affectedItems) {
                    PLMItemRevision itemRevision = null;
                    if (affectedItem.getToItem() != null) {
                        itemRevision = itemRevisionRepository.findOne(affectedItem.getToItem());
                    } else {
                        itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                    }
                    PLMItem item1 = itemRepository.findOne(itemRevision.getItemMaster());
                    if (!itemRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED) && !itemRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.CANCELLED)) {
                        if (eco.getStatusType().equals(WorkflowStatusType.RELEASED)) {
                            itemRevision.setReleased(true);
                            if (affectedItem.getToItem() != null && !affectedItem.getItem().equals(affectedItem.getToItem())) {
                                PLMItemRevision oldRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                                oldRevision.setEffectiveTo(eco.getReleasedDate());
                                oldRevision = itemRevisionRepository.save(oldRevision);
                            }
                            Integer notReleasedDocumentCount = objectDocumentRepository.getNotReleasedDocumentsCount(itemRevision.getId());
                            if (notReleasedDocumentCount > 0) {
                                String message = messageSource.getMessage("affected_item_has_unreleased_documents", null, "[{0}] afftected item has some unreleased documents", LocaleContextHolder.getLocale());
                                String result = MessageFormat.format(message + ".", item1.getItemNumber());
                                throw new CassiniException(result);
                            }
                        } else if (eco.getStatusType().equals(WorkflowStatusType.REJECTED)) {
                            itemRevision.setRejected(true);
                        }
                        itemRevision.setReleasedDate(new Date());
                        if (affectedItem.getEffectiveDate() != null) {
                            itemRevision.setEffectiveFrom(affectedItem.getEffectiveDate());
                        } else {
                            itemRevision.setEffectiveFrom(eco.getReleasedDate());
                        }
                        itemRevision.setEffectiveTo(null);
                        itemRevision.setChangeOrder(eco.getId());
                        itemRevision = itemRevisionRepository.save(itemRevision);

                    /* App Events */
                        applicationEventPublisher.publishEvent(new ItemEvents.ItemReleasedEvent(item1, itemRevision, eco));

                        List<PLMItemRevision> instances = itemRevisionRepository.findByInstanceOrderByCreatedDateDesc(itemRevision.getId());
                        for (PLMItemRevision instance : instances) {
                            instance.setEffectiveFrom(itemRevision.getEffectiveFrom());
                            instance.setEffectiveTo(itemRevision.getEffectiveTo());
                            instance = itemRevisionRepository.save(instance);
                        }
                    }
                    Integer[] ecrs = affectedItem.getChangeRequests();
                    if (ecrs.length > 0) {
                        for (int i = 0; i < ecrs.length; i++) {
                            Integer dcrId = ecrs[i];
                            Boolean notImplemented = false;
                            List<PLMECRAffectedItem> plmecrAffectedItems = ecrAffectedItemRepository.findByEcr(dcrId);
                            for (PLMECRAffectedItem plmecrAffectedItem : plmecrAffectedItems) {
                                PLMItemRevision revision = itemRevisionRepository.findOne(plmecrAffectedItem.getItem());
                                if (revision.getItemMaster().equals(itemRevision.getItemMaster())) {
                                    plmecrAffectedItem.setIsImplemented(true);
                                    plmecrAffectedItem.setImplementedDate(eco.getReleasedDate());
                                    plmecrAffectedItem = ecrAffectedItemRepository.save(plmecrAffectedItem);
                                }

                                if (!plmecrAffectedItem.getIsImplemented()) {
                                    notImplemented = true;
                                }
                            }

                            if (!notImplemented) {
                                PLMECR plmecr = ecrRepository.findOne(dcrId);
                                plmecr.setIsImplemented(true);
                                plmecr.setImplementedDate(eco.getReleasedDate());

                                plmecr = ecrRepository.save(plmecr);
                                List<PLMECRPR> plmecrprs = ecrprRepository.findByEcr(plmecr.getId());

                                if (plmecr.getQcr() != null) {
                                    PQMQCR pqmqcr = qcrRepository.findOne(plmecr.getQcr().getId());

                                    Boolean notImplementedQcrItem = false;

                                    List<PQMQCRProblemItem> pqmqcrProblemItems = qcrProblemItemRepository.findByQcr(plmecr.getQcr().getId());
                                    for (PLMECRAffectedItem savedAffectedItem : plmecrAffectedItems) {
                                        for (PQMQCRProblemItem qcrProblemItem : pqmqcrProblemItems) {
                                            if (savedAffectedItem.getItem().equals(qcrProblemItem.getItem())) {
                                                qcrProblemItem.setIsImplemented(true);
                                                qcrProblemItem.setImplementedDate(eco.getReleasedDate());
                                                qcrProblemItem = qcrProblemItemRepository.save(qcrProblemItem);
                                            }

                                            if (!qcrProblemItem.getIsImplemented()) {
                                                notImplementedQcrItem = true;
                                            }
                                        }
                                    }

                                    if (!notImplementedQcrItem) {
                                        pqmqcr.setIsImplemented(true);
                                        pqmqcr.setImplementedDate(new Date());
                                        pqmqcr = qcrRepository.save(pqmqcr);
                                        Boolean notImplementedPrItem = false;
                                        List<PQMQCRAggregatePR> aggregatePRList = qcrAggregatePRRepository.findByQcr(pqmqcr.getId());
                                        for (PQMQCRAggregatePR pqmqcrAggregatePR : aggregatePRList) {
                                            List<PQMPRProblemItem> problemItems = prProblemItemRepository.findByProblemReport(pqmqcrAggregatePR.getPr().getId());
                                            for (PQMQCRProblemItem qcrProblemItem : pqmqcrProblemItems) {
                                                for (PQMPRProblemItem prProblemItem : problemItems) {
                                                    if (prProblemItem.getItem().equals(qcrProblemItem.getItem())) {
                                                        prProblemItem.setIsImplemented(true);
                                                        prProblemItem.setImplementedDate(eco.getReleasedDate());
                                                        prProblemItem = prProblemItemRepository.save(prProblemItem);
                                                    }

                                                    if (!prProblemItem.getIsImplemented()) {
                                                        notImplementedPrItem = true;
                                                    }
                                                }
                                            }

                                            if (!notImplementedPrItem) {
                                                PQMProblemReport problemReport = problemReportRepository.findOne(pqmqcrAggregatePR.getPr().getId());
                                                problemReport.setIsImplemented(true);
                                                problemReport.setImplementedDate(eco.getReleasedDate());
                                                problemReport = problemReportRepository.save(problemReport);
                                                applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportImplementedEvent(problemReport));
                                            }
                                        }
                                    }
                                }

                                if (plmecrprs.size() > 0) {
                                    for (PLMECRPR plmecrpr : plmecrprs) {
                                        Boolean notImplementedPrItem = false;

                                        List<PQMPRProblemItem> pqmprProblemItems = prProblemItemRepository.findByProblemReport(plmecrpr.getProblemReport());
                                        for (PLMECRAffectedItem savedAffectedItem : plmecrAffectedItems) {
                                            for (PQMPRProblemItem pqmprProblemItem : pqmprProblemItems) {
                                                if (savedAffectedItem.getItem().equals(pqmprProblemItem.getItem())) {
                                                    pqmprProblemItem.setIsImplemented(true);
                                                    pqmprProblemItem.setImplementedDate(eco.getReleasedDate());
                                                    pqmprProblemItem = prProblemItemRepository.save(pqmprProblemItem);
                                                }

                                                if (!pqmprProblemItem.getIsImplemented()) {
                                                    notImplementedPrItem = true;
                                                }
                                            }
                                        }

                                        if (!notImplementedPrItem) {
                                            PQMProblemReport problemReport = problemReportRepository.findOne(plmecrpr.getProblemReport());
                                            problemReport.setIsImplemented(true);
                                            problemReport.setImplementedDate(eco.getReleasedDate());
                                            problemReport = problemReportRepository.save(problemReport);
                                            applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportImplementedEvent(problemReport));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    String message = null;
                    Person person = sessionWrapper.getSession().getLogin().getPerson();
                    message = item1.getItemType().getName() + " - " + item1.getItemName() + " - " + item1.getItemNumber() + " : " + " Rev " + itemRevision.getRevision()
                            + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Item released from ' " + eco.getEcoNumber() + " - " + eco.getTitle()
                            + " ' ECO by " + person.getFullName();
                    String mailSubject = item1.getItemNumber() + " : " + " Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Notification";
                    itemService.sendItemSubscribeNotification(item1, message, mailSubject);
                }
            }
        }
        applicationEventPublisher.publishEvent(new ECOEvents.ECOBasicInfoUpdatedEvent(oldEco, eco));
        return eco;
    }

    public List<PLMAffectedItem> getAffectedItems(Integer ecoID) {

        List<PLMAffectedItem> affectedItems = affectedItemRepository.findByChange(ecoID);
        for (PLMAffectedItem affectedItem : affectedItems) {
            if (affectedItem.getChangeRequests().length > 0) {
                List<Integer> ecrIds = new ArrayList<>();
                for (int i = 0; i < affectedItem.getChangeRequests().length; i++) {
                    ecrIds.add(affectedItem.getChangeRequests()[i]);
                }
                affectedItem.getEcrList().addAll(ecrRepository.findByIdIn(ecrIds));
            }
        }

        return affectedItems;
    }

    @Transactional
    public PLMAffectedItem createEcoAffectedItem(PLMAffectedItem ecoAffectedItem) {
        ecoAffectedItem.setId(null);
        PLMECO ecoObj = null;
        PLMItem exist = checkItemHasPendingEco(ecoAffectedItem.getItem());
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(ecoAffectedItem.getItem());
        PLMItem item1 = itemRepository.findOne(itemRevision.getItemMaster());
        itemRevision = itemRevisionRepository.findOne(item1.getLatestRevision());
        PLMItemRevision revision = itemRevisionRepository.getByItemMasterAndRevision(itemRevision.getItemMaster(),
                ecoAffectedItem.getToRevision());

        PLMItemRevision configurablePart = null;
        PLMItemRevision revisedPart = null;
        if (revision == null) {
            if (!hasAnyEcos(item1.getId())) {
                itemRevision.setRevision(ecoAffectedItem.getToRevision());
                itemRevision = itemRevisionRepository.save(itemRevision);
                ecoAffectedItem.setItem(itemRevision.getId());
                List<PLMItem> items = itemRepository.findByInstanceOrderByCreatedDateDesc(item1.getId());
                if (items.size() > 0) {
                    for (PLMItem item : items) {
                        PLMItemRevision itemRevision1 = itemRevisionRepository.findOne(item.getLatestRevision());
                        itemRevision1.setRevision(ecoAffectedItem.getToRevision());
                        itemRevision1 = itemRevisionRepository.save(itemRevision1);
                    }
                }
                String messsage = null;
                PLMECO eco = ecoRepository.findOne(ecoAffectedItem.getChange());
                Person person = sessionWrapper.getSession().getLogin().getPerson();
                messsage = item1.getItemType().getName() + " - " + item1.getItemName() + " - " + item1.getItemNumber() + " Item added to ' " + eco.getEcoNumber() + " - " + eco.getTitle()
                        + " ' ECO Affected Items by " + person.getFullName() + " from Rev " + ecoAffectedItem.getFromRevision() + " to Rev " + ecoAffectedItem.getToRevision();
                String mailSubject = item1.getItemNumber() + " : " + " Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Notification";
                itemService.sendItemSubscribeNotification(item1, messsage, mailSubject);

            } else {
                PLMItemRevision existRevisionItem = itemRevisionRepository.getByItemMasterAndRevision(itemRevision.getItemMaster(), ecoAffectedItem.getToRevision());
                if (existRevisionItem == null) {
                    revision = itemService.reviseRevisionItem(itemRevision, ecoAffectedItem.getToRevision());
                    revision.setFromRevision(itemRevision.getRevision());
                    ecoAffectedItem.setToItem(revision.getId());
                } else {
                    ecoAffectedItem.setToItem(existRevisionItem.getId());
                }
                if (item1.getConfigurable()) {
                    configurablePart = itemRevision;
                    revisedPart = revision;
                }
                String messsage = null;
                PLMECO eco = ecoRepository.findOne(ecoAffectedItem.getChange());
                ecoObj = eco;
                Person person = sessionWrapper.getSession().getLogin().getPerson();
                messsage = item1.getItemType().getName() + " - " + item1.getItemName() + " - " + item1.getItemNumber() + " Item added to ' " + eco.getEcoNumber() + " - " + eco.getTitle()
                        + " ' ECO Affected Items and Revised" + " from Rev " + ecoAffectedItem.getFromRevision()
                        + " to Rev " + ecoAffectedItem.getToRevision() + " : Preliminary by " + person.getFullName();
                String mailSubject = item1.getItemNumber() + " : " + " Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Notification";
                itemService.sendItemSubscribeNotification(item1, messsage, mailSubject);
            }
        }

        ecoAffectedItem.setFromRevision(itemRevision.getRevision());
        ecoAffectedItem.setToRevision(ecoAffectedItem.getToRevision());
        ecoAffectedItem = affectedItemRepository.save(ecoAffectedItem);
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(ecoAffectedItem.getItem());
        PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
        if (plmItem != null) {
            ecoAffectedItem.setItemObject(plmItem);
        }
        if (configurablePart != null && configurablePart.getInclusionRules() != null && !configurablePart.getInclusionRules().equals("")) {
            ecoAffectedItem.setConfigurableItem(configurablePart);
            ecoAffectedItem.setRevisedItem(revisedPart);
        }
        return ecoAffectedItem;
    }

    @Transactional
    public PLMAffectedItem createAffectedItem(PLMAffectedItem ecoAffectedItem) {
        ecoAffectedItem.setId(null);
        PLMItem item = checkItemHasPendingEco(ecoAffectedItem.getItem());
        ecoAffectedItem = affectedItemRepository.save(ecoAffectedItem);
        PLMECO eco = ecoRepository.findOne(ecoAffectedItem.getChange());
        if (eco != null && eco.getRevisionsCreated()) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(ecoAffectedItem.getItem());
            PLMItemRevision existRevisionItem = itemRevisionRepository.getByItemMasterAndRevision(itemRevision.getItemMaster(), ecoAffectedItem.getToRevision());
            PLMItemRevision revision = null;
            if (existRevisionItem == null) {
                revision = itemService.reviseRevisionItem(itemRevision, ecoAffectedItem.getToRevision());
                revision.setFromRevision(itemRevision.getRevision());
                revision = itemRevisionRepository.save(revision);
                ecoAffectedItem.setToItem(revision.getId());
            } else {
                ecoAffectedItem.setToItem(existRevisionItem.getId());
            }
            ecoAffectedItem = affectedItemRepository.save(ecoAffectedItem);
        }
        List<PLMAffectedItem> affectedItems = new ArrayList<>();
        affectedItems.add(ecoAffectedItem);
        addAffectedItemToWorkflowEventData(eco, ecoAffectedItem);
        applicationEventPublisher.publishEvent(new ECOEvents.ECOAffectedItemAddedEvent(eco, affectedItems));
        return ecoAffectedItem;
    }

    @Transactional
    public List<PLMAffectedItem> createAffectedItems(Integer ecoId, List<PLMAffectedItem> ecoAffectedItems) {
        PLMECO eco = ecoRepository.findOne(ecoId);
        ecoAffectedItems.forEach(affectedItem -> {
            PLMItem item = checkItemHasPendingEco(affectedItem.getItem());
        });
        ecoAffectedItems = affectedItemRepository.save(ecoAffectedItems);
        if (eco != null && eco.getRevisionsCreated()) {
            for (PLMAffectedItem ecoAffectedItem : ecoAffectedItems) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(ecoAffectedItem.getItem());
                PLMItemRevision existRevisionItem = itemRevisionRepository.getByItemMasterAndRevision(itemRevision.getItemMaster(), ecoAffectedItem.getToRevision());
                PLMItemRevision revision = null;
                if (existRevisionItem == null) {
                    revision = itemService.reviseRevisionItem(itemRevision, ecoAffectedItem.getToRevision());
                    revision.setFromRevision(itemRevision.getRevision());
                    revision = itemRevisionRepository.save(revision);
                    ecoAffectedItem.setToItem(revision.getId());
                } else {
                    ecoAffectedItem.setToItem(existRevisionItem.getId());
                }
                ecoAffectedItem = affectedItemRepository.save(ecoAffectedItem);
            }
        }
        if (eco != null && eco.getWorkflow() != null) {
            for (PLMAffectedItem ecoAffectedItem : ecoAffectedItems) {
                addAffectedItemToWorkflowEventData(eco, ecoAffectedItem);
            }
        }
        applicationEventPublisher.publishEvent(new ECOEvents.ECOAffectedItemAddedEvent(eco, ecoAffectedItems));
        return ecoAffectedItems;
    }

    @Transactional
    public void addAffectedItemToWorkflowEventData(PLMECO eco, PLMAffectedItem ecoAffectedItem) {
        PLMWorkflow workflow = workflowRepository.findOne(eco.getWorkflow());
        PLMWorkflowDefinition workflowDefinition = workFlowDefinitionRepository.findOne(workflow.getWorkflowRevision());
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowOrderByIdAsc(eco.getWorkflow());
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

    @Transactional
    public PLMItem checkItemHasPendingEco(Integer revisionId) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(revisionId);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        String toRevision = itemService.getNextRevisionSequence(item);
        List<PLMAffectedItem> affectedItems = affectedItemRepository.findByItemAndToRevision(item.getLatestRevision(), toRevision);
        if (item.getLatestReleasedRevision() != null) {
            List<PLMAffectedItem> releasedAffectedItems = affectedItemRepository.findByItemAndToRevision(item.getLatestReleasedRevision(), toRevision);
            if (releasedAffectedItems.size() > 0) {
                releasedAffectedItems.forEach(releasedAffectedItem -> {
                    PLMECO plmeco = ecoRepository.findOne(releasedAffectedItem.getChange());
                    if (plmeco != null) {
                        if (!plmeco.getReleased()) {
                            item.setPendingEco(true);
                            item.setEcoNumber(plmeco.getEcoNumber());
                            item.setChangeId(plmeco.getId());
                        }
                    }
                });
            }
        }
        if (affectedItems.size() > 0) {
            affectedItems.forEach(affectedItem -> {
                PLMECO plmeco = ecoRepository.findOne(affectedItem.getChange());
                if (plmeco != null) {
                    if (!plmeco.getReleased()) {
                        item.setPendingEco(true);
                        item.setEcoNumber(plmeco.getEcoNumber());
                    }
                }
            });
        }
        PLMItemRevision oldRevision = itemRevisionRepository.getByItemMasterAndRevision(item.getId(), "-");
        if (oldRevision != null && item.getLatestReleasedRevision() == null && itemRevision.getRejected()) {
            List<PLMAffectedItem> plmAffectedItems = affectedItemRepository.findByItemAndToRevision(oldRevision.getId(), toRevision);
            if (plmAffectedItems.size() > 0) {
                plmAffectedItems.forEach(plmAffectedItem -> {
                    PLMECO plmeco = ecoRepository.findOne(plmAffectedItem.getChange());
                    if (plmeco != null) {
                        if (!plmeco.getReleased()) {
                            item.setPendingEco(true);
                            item.setEcoNumber(plmeco.getEcoNumber());
                        }
                    }
                });
            }
        }

        if (item.getPendingEco()) {
            String message = messageSource.getMessage("item_has_pending_eco", null, "{0} item has pending ECO {1}", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", item.getItemNumber(), item.getEcoNumber());
            throw new CassiniException(result);
        }

        return item;
    }

    @Transactional(readOnly = true)
    public List<PLMAffectedItem> getConfigurableAffectedItems(Integer ecoId) {
        List<PLMAffectedItem> configurableAffectedItems = new ArrayList<>();
        List<PLMAffectedItem> affectedItems = affectedItemRepository.findByChange(ecoId);
        affectedItems.forEach(affectedItem -> {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            if (item.getConfigurable() && affectedItem.getToItem() != null) {
                PLMItemRevision revisedPart = itemRevisionRepository.findOne(affectedItem.getToItem());
                affectedItem.setRevisedItem(revisedPart);
                affectedItem.setConfigurableItem(itemRevision);

                configurableAffectedItems.add(affectedItem);
            }
        });

        return configurableAffectedItems;
    }

    @Transactional
    public PLMECO createRevisions(Integer ecoId, Boolean start, Boolean finish, Integer statusId) {
        PLMECO plmeco = ecoRepository.findOne(ecoId);
        if (plmeco.getRevisionCreationType() != null && !plmeco.getRevisionsCreated()) {
            if (start) {
                if (plmeco.getRevisionCreationType().equals(RevisionCreationType.WORKFLOW_START)) {
                    plmeco = updateAffectedItemRevisions(plmeco);
                }
            } else if (finish) {
                plmeco = updateAffectedItemRevisions(plmeco);
            } else {
                if (statusId.equals(plmeco.getWorkflowStatus())) {
                    plmeco = updateAffectedItemRevisions(plmeco);
                }
            }
        }

        return plmeco;
    }

    private PLMECO updateAffectedItemRevisions(PLMECO plmeco) {
        List<PLMAffectedItem> affectedItems = affectedItemRepository.findByChange(plmeco.getId());
        affectedItems.forEach(affectedItem -> {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
            PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());
            //this.itemService.checkGitReleasedTag(plmItem, itemRevision);
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
        });
        plmeco.setRevisionsCreated(true);
        plmeco = ecoRepository.save(plmeco);
        return plmeco;
    }

    public PLMAffectedItem updateEcoAffectedItem(PLMAffectedItem ecoAffectedItem) {
        PLMAffectedItem oldAffectedItem = JsonUtils.cloneEntity(ecoAffectedItemRepository.findOne(ecoAffectedItem.getId()), PLMAffectedItem.class);
        checkNotNull(ecoAffectedItem);
        ecoAffectedItem = affectedItemRepository.save(ecoAffectedItem);
        PLMECO eco = ecoRepository.findOne(ecoAffectedItem.getChange());
        applicationEventPublisher.publishEvent(new ECOEvents.ECOAffectedItemUpdatedEvent(eco, oldAffectedItem, ecoAffectedItem));
        return ecoAffectedItem;
    }


    private Boolean hasAnyEcos(Integer itemId) {
        List<PLMItemRevision> revs = itemService.getItemRevisions(itemId);
        List<Integer> itemIds = new ArrayList<>();
        revs.forEach(rev -> itemIds.add(rev.getId()));
        return affectedItemRepository.findByItemIn(itemIds).size() > 0;
    }

    private Boolean hasAnyPendingEcos(Integer itemId) {
        List<PLMECO> ecos = ecoRepository.findByStatusTypeNot(WorkflowStatusType.RELEASED);
        List<Integer> ids = new ArrayList<>();
        ecos.forEach(eco -> {
            ids.add(eco.getId());
        });
        List<PLMAffectedItem> items = affectedItemRepository.findByChangeInAndItem(ids, 43);
        return items.size() > 0;
    }

    @Override
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        checkNotNull(id);
        PLMECO eco = ecoRepository.findOne(id);
        List<RecentlyVisited> recentlyVisiteds = recentlyVisitedRepository.findByObjectId(id);
        for (RecentlyVisited recentlyVisited : recentlyVisiteds) {
            recentlyVisitedRepository.delete(recentlyVisited.getId());
        }
        if (eco == null) {
            throw new ResourceNotFoundException();
        }
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
        ecoRepository.delete(id);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject,'view')")
    public PLMECO get(Integer id) {
        checkNotNull(id);
        PLMECO plmeco = ecoRepository.findOne(id);
        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto = workflowService.setWorkflowStatusSettings(plmeco.getId());
        plmeco.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        plmeco.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        plmeco.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        return plmeco;
    }


    @Transactional(readOnly = true)
    public ECODetails getEcoDetails(Integer id) {
        ECODetails ecoDetails = new ECODetails();
        PLMECO plmeco = ecoRepository.findOne(id);
        ecoDetails.setId(plmeco.getId());
        ecoDetails.setTitle(plmeco.getTitle());
        ecoDetails.setDescription(plmeco.getDescription());
        ecoDetails.setEcoNumber(plmeco.getEcoNumber());
        ecoDetails.setReasonForChange(plmeco.getReasonForChange());
        ecoDetails.setStatus(plmeco.getStatus());
        ecoDetails.setEcoType(ecoTypeRepository.findOne(plmeco.getEcoType()).getName());
        ecoDetails.setChangeAnalyst(personRepository.findOne(plmeco.getEcoOwner()).getFullName());
        ecoDetails.setCreatedDate(plmeco.getCreatedDate());
        ecoDetails.setModifiedDate(plmeco.getModifiedDate());
        ecoDetails.setCreatedBy(personRepository.findOne(plmeco.getCreatedBy()).getFullName());
        ecoDetails.setModifiedBy(personRepository.findOne(plmeco.getModifiedBy()).getFullName());
        ecoDetails.setWorkflow(plmeco.getWorkflow());

        return ecoDetails;
    }

    public Integer getByNumber(String id) {
        checkNotNull(id);
        List<PLMECO> plmecos = ecoRepository.findByEcoNumber(id);
        return plmecos.size();
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMECO> getAll() {
        return ecoRepository.findAll();
    }

    public List<Person> getChangeAnalysts() {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = ecoRepository.getChangeAnalystIds();
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMECO> getAllItemECOs(Pageable pageable, ECOCriteria ecoCriteria) {
        Predicate predicate = predicateBuilder.build(ecoCriteria, QPLMECO.pLMECO);
        Page<PLMECO> ecos = ecoRepository.findAll(predicate, pageable);
        ecos.getContent().forEach(eco -> {
            WorkflowStatusDTO workFlowStatusDto = workflowService.setWorkflowStatusSettings(eco.getId());
            eco.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            eco.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            eco.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
            eco.setOnHold(workFlowStatusDto.getOnHold());
            eco.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            eco.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());
        });
        return ecos;
    }

    public List<String> getStatus() {
        return ecoRepository.getStatusIds();
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMECO> findAll(Pageable pageable) {
        Page<PLMECO> plmecos = ecoRepository.findAll(pageable);
        plmecos.getContent().forEach(plmeco -> {
            if (plmeco.getWorkflow() != null) {
                PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(plmeco.getWorkflow());
                if (plmWorkflow != null) {
                    plmeco.setOnHold(plmWorkflow.getOnhold());
                }
            }
        });
        return plmecos;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMECO> findAll(ECOCriteria criteria, Pageable pageable) {
        Predicate predicate = predicateBuilder.build(criteria,
                QPLMECO.pLMECO);
        Page<PLMECO> plmecos = ecoRepository.findAll(predicate, pageable);
        plmecos.getContent().forEach(plmeco -> {
            if (plmeco.getWorkflow() != null) {
                PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(plmeco.getWorkflow());
                if (plmWorkflow != null) {
                    plmeco.setOnHold(plmWorkflow.getOnhold());
                }
            }
        });
        return plmecos;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMECO> findMultiple(List<Integer> ids) {
        return ecoRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<PLMChangeType> getMultipleChangeTypes(List<Integer> ids) {
        return changeTypeRepository.findByIdIn(ids);
    }

    public void deleteEcoAffectedItem(Integer affactedItemId) {
        PLMAffectedItem affectedItem = affectedItemRepository.findOne(affactedItemId);
        String messsage = null;
        PLMECO eco = ecoRepository.findOne(affectedItem.getChange());
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
        PLMItem item1 = itemRepository.findOne(itemRevision.getItemMaster());
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        messsage = item1.getItemType().getName() + " - " + item1.getItemName() + " - " + item1.getItemNumber() + " : " + " Rev " + itemRevision.getRevision()
                + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Item deleted from ' " + eco.getEcoNumber() + " - " + eco.getTitle()
                + " ' ECO Affected Items by " + person.getFullName();
        String mailSubject = item1.getItemNumber() + " : " + " Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Notification";
        itemService.sendItemSubscribeNotification(item1, messsage, mailSubject);
        applicationEventPublisher.publishEvent(new ECOEvents.ECOAffectedItemDeletedEvent(eco, affectedItem));
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowOrderByIdAsc(eco.getWorkflow());
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
        affectedItemRepository.delete(affactedItemId);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMECO> findByAffectedItem(Integer itemId) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        List<Integer> revisionIds = itemRevisionRepository.getRevisionIdsByItemId(itemRevision.getItemMaster());
        List<PLMECO> ecos = new LinkedList<>();
        List<PLMAffectedItem> affectedItems = new ArrayList<>();
        for (Integer revisionId : revisionIds) {
            affectedItems.addAll(affectedItemRepository.findByItem(revisionId));
            affectedItems.addAll(affectedItemRepository.findByToItem(revisionId));
        }
        for (PLMAffectedItem item : affectedItems) {
            PLMECO plmeco = ecoRepository.findOne(item.getChange());
            if (plmeco != null) {
                plmeco.setFromRev(item.getFromRevision());
                plmeco.setToRev(item.getToRevision());
                if (ecos.indexOf(plmeco) == -1) {
                    ecos.add(plmeco);
                }
            }
        }
        return ecos;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMECO> find(ECOCriteria criteria, Pageable pageable) {
        Predicate predicate = predicateBuilder.build(criteria, QPLMECO.pLMECO);
        Page<PLMECO> plmecos = ecoRepository.findAll(predicate, pageable);
        plmecos.getContent().forEach(plmeco -> {
            if (plmeco.getWorkflow() != null) {
                PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(plmeco.getWorkflow());
                if (plmWorkflow != null) {
                    plmeco.setOnHold(plmWorkflow.getOnhold());
                }
            }
        });
        return plmecos;
    }

    @Transactional(readOnly = true)
    // @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMECO> advancedSearchECR(ParameterCriteria[] parameterCriterias, Pageable pageable) {
        TypedQuery<PLMECO> typedQuery = ecoAdvancedCriteria.getECOTypeQuery(parameterCriterias);
        typedQuery.setFirstResult(pageable.getPageNumber());
        typedQuery.setMaxResults(pageable.getPageSize());
        Page<PLMECO> resultlist1 = new PageImpl(typedQuery.getResultList());
        resultlist1.getContent().forEach(plmeco -> {
            if (plmeco.getWorkflow() != null) {
                PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(plmeco.getWorkflow());
                if (plmWorkflow != null) {
                    plmeco.setOnHold(plmWorkflow.getOnhold());
                }
            }
        });
        return resultlist1;
    }

    public PLMWorkflow attachWorkflow(Integer ecoId, Integer wfDefId, Integer workflowStatusId) {
        PLMWorkflow workflow = null;
        PLMECO eco = ecoRepository.findOne(ecoId);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (eco != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.CHANGE, ecoId, wfDef);
            eco.setWorkflow(workflow.getId());
            if (workflowStatusId != null && eco.getRevisionCreationType().equals(RevisionCreationType.ACTIVITY_COMPLETION)) {
                PLMWorkflowDefinitionStatus workflowDefinitionStatus = plmWorkflowDefinitionStatusRepository.findOne(workflowStatusId);
                PLMWorkflowStatus workflowStatus = workflowStatusRepository.findByWorkflowAndNameAndType(workflow.getId(), workflowDefinitionStatus.getName(), workflowDefinitionStatus.getType());
                eco.setWorkflowStatus(workflowStatus.getId());
            }
            ecoRepository.save(eco);
        }
        return workflow;
    }

    @Transactional
    public PLMWorkflow attachNewWorkflow(Integer ecoId, Integer wfDefId, RevisionCreationType revisionCreationType, Integer workflowStatusId) {
        PLMWorkflow workflow = null;
        PLMECO eco = ecoRepository.findOne(ecoId);
        eco.setRevisionCreationType(revisionCreationType);
        eco = ecoRepository.save(eco);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (eco != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.CHANGE, ecoId, wfDef);
            eco.setWorkflow(workflow.getId());
            if (workflowStatusId != null && eco.getRevisionCreationType().equals(RevisionCreationType.ACTIVITY_COMPLETION)) {
                PLMWorkflowDefinitionStatus workflowDefinitionStatus = plmWorkflowDefinitionStatusRepository.findOne(workflowStatusId);
                PLMWorkflowStatus workflowStatus = workflowStatusRepository.findByWorkflowAndNameAndType(workflow.getId(), workflowDefinitionStatus.getName(), workflowDefinitionStatus.getType());
                eco.setWorkflowStatus(workflowStatus.getId());
            }
            eco = ecoRepository.save(eco);
            applicationEventPublisher.publishEvent(new ECOEvents.ECOWorkflowChangeEvent(eco, null, workflow));
        }
        return workflow;
    }

    @Transactional(readOnly = true)
    public PLMWorkflow getAttachedWorkflow(Integer ecoId) {
        PLMWorkflow workflow = workflowService.getAttachedWorkflow(ecoId);
        return workflowService.get(workflow.getId());
    }

    @Transactional(readOnly = true)
    public List<PLMChangeTypeAttribute> getEcoTypeAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            return changeTypeAttributeRepository.findByChangeTypeOrderBySeq(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    public void saveEcoAttributes(List<PLMChangeAttribute> attributes) {
        for (PLMChangeAttribute attribute : attributes) {
            PLMChangeTypeAttribute changeTypeAttribute = changeTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || changeTypeAttribute.getDataType().toString().equals("FORMULA") ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {
                changeAttributeRepository.save(attribute);
            }
        }
    }

    private List<PLMChangeTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<PLMChangeTypeAttribute> collector = new ArrayList<>();
        List<PLMChangeTypeAttribute> atts = changeTypeAttributeRepository.findByChangeTypeOrderBySeq(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<PLMChangeTypeAttribute> collector, Integer typeId) {
        PLMChangeType changeType = changeTypeRepository.findOne(typeId);
        if (changeType != null) {
            Integer parentType = changeType.getParentType();
            if (parentType != null) {
                List<PLMChangeTypeAttribute> atts = changeTypeAttributeRepository.findByChangeTypeOrderByName(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    public PLMChangeAttribute createEcoAttribute(PLMChangeAttribute attribute) {
        return changeAttributeRepository.save(attribute);
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public PLMChangeAttribute updateEcoAttribute(PLMChangeAttribute attribute) {
        PLMChangeAttribute oldValue = changeAttributeRepository.findByChangeAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PLMChangeAttribute.class);
        attribute = changeAttributeRepository.save(attribute);
        PLMChange change = changeRepository.findOne(attribute.getId().getObjectId());
        if (change != null) {
            if (change.getChangeType().equals(ChangeType.ECO)) {
                PLMECO plmeco = ecoRepository.findOne(attribute.getId().getObjectId());
                applicationEventPublisher.publishEvent(new ECOEvents.ECOAttributesUpdatedEvent(plmeco, oldValue, attribute));
            } else if (change.getChangeType().equals(ChangeType.ECR)) {
                PLMECR plmecr = ecrRepository.findOne(attribute.getId().getObjectId());
                applicationEventPublisher.publishEvent(new ECREvents.ECRAttributesUpdatedEvent(plmecr, oldValue, attribute));
            } else if (change.getChangeType().equals(ChangeType.DCR)) {
                PLMDCR plmdcr = dcrRepository.findOne(attribute.getId().getObjectId());
                applicationEventPublisher.publishEvent(new DCREvents.DCRAttributesUpdatedEvent(plmdcr, oldValue, attribute));
            } else if (change.getChangeType().equals(ChangeType.DCO)) {
                PLMDCO plmdco = dcoRepository.findOne(attribute.getId().getObjectId());
                applicationEventPublisher.publishEvent(new DCOEvents.DCOAttributesUpdatedEvent(plmdco, oldValue, attribute));
            } else if (change.getChangeType().equals(ChangeType.DEVIATION)) {
                PLMVariance plmVariance = varianceRepository.findOne(attribute.getId().getObjectId());
                applicationEventPublisher.publishEvent(new VarianceEvents.VarianceAttributesUpdatedEvent(plmVariance, oldValue, attribute));
            } else if (change.getChangeType().equals(ChangeType.WAIVER)) {
                PLMVariance plmVariance = varianceRepository.findOne(attribute.getId().getObjectId());
                applicationEventPublisher.publishEvent(new VarianceEvents.VarianceAttributesUpdatedEvent(plmVariance, oldValue, attribute));
            } else if (change.getChangeType().equals(ChangeType.MCO)) {
                PLMMCO plmmco = mcoRepository.findOne(attribute.getId().getObjectId());
                applicationEventPublisher.publishEvent(new MCOEvents.MCOAttributesUpdatedEvent(plmmco, oldValue, attribute));
            }
        }

        return attribute;

    }

    @Transactional(readOnly = true)
    public List<PLMChangeAttribute> getEcoAttributes(Integer ecoId) {
        return changeAttributeRepository.findByChangeIdIn(ecoId);
    }

    @Transactional(readOnly = true)
    public Page<PLMItemRevision> getLatestReleasedRevisions(Pageable pageable) {
        Page<PLMItemRevision> itemRevisions = itemRevisionRepository.findByReleasedIsTrueOrderByReleasedDateDesc(pageable);
        return itemRevisions;
    }

    @Transactional(readOnly = true)
    public Page<PLMECO> findByReleasedFalse(Pageable pageable) {
        Page<PLMECO> plmecos = ecoRepository.findByReleasedFalse(pageable);
        List<PLMECO> ecos = new LinkedList<>();
        plmecos.getContent().forEach(plmeco -> {
            if (!plmeco.getStatusType().equals(WorkflowStatusType.REJECTED)) {
                ecos.add(plmeco);
            }
        });
        return new PageImpl<PLMECO>(ecos, pageable, plmecos.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<PLMECO> getECOsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        PLMChangeType type = changeTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        Page<PLMECO> plmecos = ecoRepository.getByEcoTypeIds(ids, pageable);
        return plmecos;
    }

    @Transactional(readOnly = true)
    public Integer getObjectsByType(Integer typeId) {
        PLMChangeType change = changeTypeRepository.findOne(typeId);
        Integer count = 0;
        if (change.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ECOTYPE.toString()))) {
            count = ecoRepository.findByEcoType(typeId).size();
        } else if (change.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ECRTYPE.toString()))) {
            count = ecrRepository.findByCrType(typeId).size();
        } else if (change.getObjectType().equals(ObjectType.valueOf(PLMObjectType.DCOTYPE.toString()))) {
            count = dcoRepository.findByDcoType(typeId).size();
        } else if (change.getObjectType().equals(ObjectType.valueOf(PLMObjectType.DCRTYPE.toString()))) {
            count = dcrRepository.findByCrType(typeId).size();
        } else if (change.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MCOTYPE.toString()))) {
            count = mcoRepository.findByMcoType(typeId).size();
        } else if (change.getObjectType().equals(ObjectType.valueOf(PLMObjectType.DEVIATIONTYPE.toString()))) {
            count = changeRepository.findByChangeClass(typeId).size();
        } else if (change.getObjectType().equals(ObjectType.valueOf(PLMObjectType.WAIVERTYPE.toString()))) {
            count = changeRepository.findByChangeClass(typeId).size();
        }

        return count;
    }

    private void collectHierarchyTypeIds(List<Integer> collector, PLMChangeType type) {
        if (type != null) {
            collector.add(type.getId());
            List<PLMChangeType> children = changeTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (PLMChangeType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<PLMChangeAttribute> getEcoUsedAttributes(Integer attributeId) {
        List<PLMChangeAttribute> changeAttributes = changeAttributeRepository.findByAttributeId(attributeId);
        return changeAttributes;
    }

    public PLMECO saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        PLMECO plmeco = ecoRepository.findOne(objectId);
        if (plmeco != null) {
            PLMChangeAttribute changeAttribute = new PLMChangeAttribute();
            changeAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    changeAttribute.setImageValue(file.getBytes());
                    changeAttribute = changeAttributeRepository.save(changeAttribute);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return plmeco;
    }

    @Transactional(readOnly = true)
    public DetailsCount getECODetails(Integer ecoId) {
        DetailsCount detailsCount = new DetailsCount();
        List<PLMChangeFile> changeFiles = changeFileRepository.findByChangeAndFileTypeAndLatestTrueOrderByModifiedDateDesc(ecoId, "FILE");
        List<PLMAffectedItem> affectedItems = affectedItemRepository.findByChange(ecoId);
        detailsCount.setFiles(changeFiles.size());
        detailsCount.setFiles(detailsCount.getFiles() + objectDocumentRepository.getDocumentsCountByObjectId(ecoId));
        detailsCount.setAffectedItems(affectedItems.size());
        detailsCount.setChangeRequests(ecoecrRepository.findByEco(ecoId).size());
        return detailsCount;
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getHierarchyWorkflows(Integer typeId, String type) {
        List<PLMWorkflowDefinition> workflowDefinitions = new ArrayList<>();
        PLMChangeType changeType = changeTypeRepository.findOne(typeId);
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
        List<PLMWorkflowDefinition> workflowDefinition1 = workFlowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
        if (workflowDefinition1.size() > 0) {
            workflowDefinition1.forEach(workflowDefinition -> {
                PLMWorkflowDefinitionMaster definitionMaster = workflowDefinitionMasterRepository.findOne(workflowDefinition.getMaster().getId());
                if (definitionMaster.getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                    workflowDefinitions.add(workflowDefinition);
                }
            });
        }
        if (changeType.getParentType() != null) {
            getWorkflowsFromHierarchy(workflowDefinitions, changeType.getParentType(), type);
        }
        return workflowDefinitions;
    }


    private void getWorkflowsFromHierarchy(List<PLMWorkflowDefinition> definitions, Integer typeId, String type) {
        PLMChangeType changeType = changeTypeRepository.findOne(typeId);
        if (changeType != null) {
            PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
            if (workflowType != null) {
                List<PLMWorkflowDefinition> workflowDefinition2 = workFlowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
                if (workflowDefinition2.size() > 0) {
                    workflowDefinition2.forEach(workflowDefinition -> {
                        PLMWorkflowDefinitionMaster definitionMaster = workflowDefinitionMasterRepository.findOne(workflowDefinition.getMaster().getId());
                        if (definitionMaster.getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                            definitions.add(workflowDefinition);
                        }
                    });
                }
            }
            if (changeType.getParentType() != null) {
                getWorkflowsFromHierarchy(definitions, changeType.getParentType(), type);
            }
        }
    }


    @Transactional(readOnly = true)
    public Page<PLMItem> getFilterRelatedItems(Pageable pageable, BomItemFilterCriteria bomItemFilterCriteria) {
        Predicate predicate = bomItemFilterPredicateBuilder.build(bomItemFilterCriteria, QPLMItem.pLMItem);
        Page<PLMItem> plmItem = itemRepository.findAll(predicate, pageable);
        return plmItem;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','relateditem')")
    public PLMChangeRelatedItem createChangeRelatedItem(PLMChangeRelatedItem plmChangeRelatedItem) {
        return changeRelatedItemRepository.save(plmChangeRelatedItem);
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','relateditem')")
    public void deleteChangeRelatedItem(Integer id) {
        changeRelatedItemRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<VarianceRelatedItemDto> getAllChangeRelatedItems(Integer id) {
        List<PLMChangeRelatedItem> plmChangeRelatedItems = changeRelatedItemRepository.findByChange(id);
        List<VarianceRelatedItemDto> varianceRelatedItemDtos = new ArrayList<>();
        plmChangeRelatedItems.forEach(plmChangeRelatedItem -> {
            VarianceRelatedItemDto relatedItemDto = new VarianceRelatedItemDto();
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmChangeRelatedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            relatedItemDto.setItem(item);
            relatedItemDto.setId(plmChangeRelatedItem.getId());
            relatedItemDto.setItemRevision(itemRevision);
            varianceRelatedItemDtos.add(relatedItemDto);
        });
        return varianceRelatedItemDtos;
    }


    private void releaseOrRejectECO(PLMECO eco, PLMWorkflow workflow) {
        List<PLMAffectedItem> items = ecoAffectedItemRepository.findByChange(workflow.getAttachedTo());
        items.forEach(item -> {
            PLMItemRevision rev = itemService.getRevision(item.getItem());
            PLMItem itemMaster = itemService.get(rev.getItemMaster());
            PLMItemRevision toRevision = itemService.getByItemMasterAndRevision(itemMaster.getId(), item.getToRevision());
            PLMLifeCyclePhase oldStatus = toRevision.getLifeCyclePhase();
            if (!toRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED) && !toRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.CANCELLED)) {
                List<PLMItemRevisionStatusHistory> itemRevisionStatusHistories = revisionStatusHistoryRepository.findByItemOrderByTimestampDesc(toRevision.getId());
                if (itemRevisionStatusHistories.size() > 0) {
                    oldStatus = itemRevisionStatusHistories.get(0).getNewStatus();
                }
                if (eco.getStatusType().equals(WorkflowStatusType.REJECTED)) {
                    toRevision.setRejected(true);
                } else {
                    toRevision.setReleased(Boolean.TRUE);
                }
                toRevision.setChangeOrder(eco.getId());
                toRevision.setReleasedDate(new Date());
                toRevision = itemRevisionRepository.save(toRevision);
                if (eco.getStatusType().equals(WorkflowStatusType.RELEASED)) {
                    itemMaster.setLatestReleasedRevision(toRevision.getId());
                }
                itemService.updateItemFromEco(itemMaster);
                updateItemInstanceHistory(itemMaster.getLatestRevision(), item);
            }
        });
        items.forEach(item -> {
            //First make sure you can release the item!!!
            PLMItemRevision rev = itemService.getRevision(item.getItem());
            PLMItem itemMaster = itemService.get(rev.getItemMaster());
            PLMItemRevision toRevision = itemService.getByItemMasterAndRevision(itemMaster.getId(), item.getToRevision());
            validateRelease(toRevision, eco.getId());
            updateBomWithAsReleased(toRevision);
            checkItemObjectAttribute(itemMaster);
        });
        eco.setReleasedDate(new Date());
        update(eco);
    }

    private void checkItemObjectAttribute(PLMItem item) {
        List<PLMItemTypeAttribute> itemRefTypeAttributes = itemTypeAttributeRepository.findByItemTypeAndDataTypeAndRefType(item.getItemType().getId(), DataType.OBJECT, ObjectType.valueOf(PLMObjectType.ITEM.toString()));
        List<ObjectTypeAttribute> itemReferenceAttributes = objectTypeAttributeRepository.findByDataTypeAndRefTypeAndObjectType(DataType.OBJECT, ObjectType.valueOf(PLMObjectType.ITEM.toString()), ObjectType.valueOf(PLMObjectType.ITEM.toString()));
        List<ObjectTypeAttribute> itemRevisionReferenceAttributes = objectTypeAttributeRepository.findByDataTypeAndRefTypeAndObjectType(DataType.OBJECT, ObjectType.valueOf(PLMObjectType.ITEMREVISION.toString()), ObjectType.valueOf(PLMObjectType.ITEMREVISION.toString()));
        itemRefTypeAttributes.forEach(plmItemTypeAttribute -> {
            if (plmItemTypeAttribute.getRevisionSpecific()) {
                ObjectAttribute objectAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(item.getLatestRevision(), plmItemTypeAttribute.getId());
                if (objectAttribute != null && objectAttribute.getRefValue() != null) {
                    PLMItem refItem = itemRepository.findOne(objectAttribute.getRefValue());
                    if (refItem != null) {
                        PLMItemRevision refRevision = itemRevisionRepository.findOne(refItem.getLatestRevision());
                        if (!refRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                            String message = messageSources.getMessage("some_of_the_reference_items_un_released", null, "{0} : affected item have un-released reference items. This ECO cannot be released.", LocaleContextHolder.getLocale());
                            String result = MessageFormat.format(message + ".", item.getItemNumber());
                            throw new CassiniException(result);
                        }
                    }
                }
            } else {
                ObjectAttribute objectAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(item.getId(), plmItemTypeAttribute.getId());
                if (objectAttribute != null && objectAttribute.getRefValue() != null) {
                    PLMItem refItem = itemRepository.findOne(objectAttribute.getRefValue());
                    if (refItem != null) {
                        PLMItemRevision refRevision = itemRevisionRepository.findOne(refItem.getLatestRevision());
                        if (!refRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                            String message = messageSources.getMessage("some_of_the_reference_items_un_released", null, "{0} : affected item have un-released reference items. This ECO cannot be released.", LocaleContextHolder.getLocale());
                            String result = MessageFormat.format(message + ".", item.getItemNumber());
                            throw new CassiniException(result);
                        }
                    }
                }
            }
        });

        itemReferenceAttributes.forEach(objectTypeAttribute -> {
            ObjectAttribute objectAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(item.getId(), objectTypeAttribute.getId());
            if (objectAttribute != null && objectAttribute.getRefValue() != null) {
                PLMItem refItem = itemRepository.findOne(objectAttribute.getRefValue());
                if (refItem != null) {
                    PLMItemRevision refRevision = itemRevisionRepository.findOne(refItem.getLatestRevision());
                    if (!refRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                        String message = messageSources.getMessage("some_of_the_reference_items_un_released", null, "{0} : affected item have un-released reference items. This ECO cannot be released.", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", item.getItemNumber());
                        throw new CassiniException(result);
                    }
                }
            }
        });
        itemRevisionReferenceAttributes.forEach(objectTypeAttribute -> {
            ObjectAttribute objectAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(item.getLatestRevision(), objectTypeAttribute.getId());
            if (objectAttribute != null && objectAttribute.getRefValue() != null) {
                PLMItemRevision refItemRevision = itemRevisionRepository.findOne(objectAttribute.getRefValue());
                if (refItemRevision != null) {
                    if (!refItemRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                        String message = messageSources.getMessage("some_of_the_reference_items_un_released", null, "{0} : affected item have un-released reference items. This ECO cannot be released.", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", item.getItemNumber());
                        throw new CassiniException(result);
                    }
                }
            }
        });

    }

    private void checkRelatedItems(PLMItem item) {
        List<PLMRelatedItem> relatedItems = relatedItemRepository.findByFromItem(item.getLatestRevision());
        relatedItems.forEach(relatedItem -> {
            if (!relatedItem.getToItem().getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                String message = messageSources.getMessage("some_of_the_related_items_un_released", null, "{0} : affected item have un-released related items. This ECO cannot be released.", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", item.getItemNumber());
                throw new CassiniException(result);
            }
        });
    }

    private void validateRelease(PLMItemRevision itemRevision, Integer ecoId) {
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        List<PLMBom> bomItems = bomService.getBom(itemRevision, false);
        bomItems.forEach(bomItem -> {
            PLMItem itemMaster = bomItem.getItem();
            PLMItemRevision itemMasterRevision = itemRevisionRepository.findOne(itemMaster.getLatestRevision());
            PLMAffectedItem affectedItem = ecoAffectedItemRepository.findByChangeAndToItem(ecoId, itemMasterRevision.getId());
            if (itemMaster.getLatestReleasedRevision() == null && affectedItem == null) {
                String message = messageSources.getMessage("some_of_the_affected_items_un_released", null, "{0} : affected item have un-released BOM items. This ECO cannot be released", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", item.getItemNumber());
                throw new CassiniException(result);
            } else if (itemMasterRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.OBSOLETE)) {
                String message = messageSources.getMessage("some_of_the_affected_items_obsolete", null, "{0} : affected item have absolete BOM items. This {1} cannot be released", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", item.getItemNumber(), "ECO");
                throw new CassiniException(result);
            } else if (!itemMasterRevision.getReleased() && affectedItem == null) {
                String message = messageSources.getMessage("some_of_the_affected_items_un_released", null, "{0} : affected item have un-released BOM items. This ECO cannot be released", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", item.getItemNumber());
                throw new CassiniException(result);
            }
        });
    }


    private void updateItemInstanceHistory(Integer id, PLMAffectedItem item1) {
        List<PLMItemRevision> revisions = itemRevisionRepository.findByInstanceOrderByCreatedDateDesc(id);
        if (revisions.size() > 0) {
            for (PLMItemRevision itemRevision : revisions) {
                PLMItem itemMaster = itemService.get(itemRevision.getItemMaster());
                PLMItemRevision toRevision = itemService.getByItemMasterAndRevision(itemMaster.getId(), item1.getToRevision());
                PLMLifeCyclePhase oldStatus = toRevision.getLifeCyclePhase();
                toRevision.setReleased(Boolean.TRUE);
                toRevision.setReleasedDate(new Date());
                toRevision = itemRevisionRepository.save(toRevision);

                itemMaster.setLatestReleasedRevision(toRevision.getId());
                itemService.update(itemMaster);
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

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'ECO'")
    public void ecoWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PLMECO eco = (PLMECO) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus workflowStatus = workflowStatusRepository.findOne(plmWorkflow.getCurrentStatus());
        createRevisions(eco.getId(), true, false, null);
        applicationEventPublisher.publishEvent(new ECOEvents.ECOWorkflowStartedEvent(eco, plmWorkflow));
        workflowEventService.workflowStart("ECO", plmWorkflow);
        workflowEventService.workflowActivityStart(eco.getChangeType().toString(), plmWorkflow, workflowStatus);
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'ECO'")
    public void ecoWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PLMECO eco = (PLMECO) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();

        eco = createRevisions(eco.getId(), false, false, fromStatus.getId());
        eco.setStatus(fromStatus.getName());
        eco.setStatusType(fromStatus.getType());
        applicationEventPublisher.publishEvent(new ECOEvents.ECOWorkflowPromotedEvent(eco, plmWorkflow, fromStatus, toStatus));
        if (fromStatus.getType() == WorkflowStatusType.RELEASED || fromStatus.getType() == WorkflowStatusType.REJECTED) {
            releaseOrRejectECO(eco, plmWorkflow);
        }
        update(eco);
        workflowEventService.workflowActivityFinish("ECO", plmWorkflow, fromStatus);
        if (toStatus != null) {
            workflowEventService.workflowActivityStart("ECO", plmWorkflow, toStatus);
        }
        if (fromStatus.getType() == WorkflowStatusType.RELEASED) {
            workflowEventService.workflowFinish("ECO", plmWorkflow);
        }
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'ECO'")
    public void ecoWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMECO eco = (PLMECO) event.getAttachedToObject();
        PLMWorkflowStatus toStatus = event.getToStatus();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        eco.setStatus(toStatus.getName());
        eco.setStatusType(toStatus.getType());
        ecoRepository.save(eco);
        applicationEventPublisher.publishEvent(new ECOEvents.ECOWorkflowDemotedEvent(eco, plmWorkflow, fromStatus, toStatus));
        workflowEventService.workflowActivityFinishDemote("ECO", plmWorkflow, toStatus);
        workflowEventService.workflowActivityStartDemote("ECO", plmWorkflow, fromStatus);
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'ECO'")
    public void ecoWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PLMECO eco = (PLMECO) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();

        eco = createRevisions(eco.getId(), false, true, fromStatus.getId());
        eco.setStatus(fromStatus.getName());
        eco.setStatusType(fromStatus.getType());
        applicationEventPublisher.publishEvent(new ECOEvents.ECOWorkflowFinishedEvent(eco, plmWorkflow));
        if (fromStatus.getType() == WorkflowStatusType.RELEASED || fromStatus.getType() == WorkflowStatusType.REJECTED) {
            releaseOrRejectECO(eco, plmWorkflow);
        }
        workflowEventService.workflowActivityFinish("ECO", plmWorkflow, fromStatus);
        workflowEventService.workflowFinish("ECO", plmWorkflow);
        update(eco);
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'ECO'")
    public void ecoWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PLMECO eco = (PLMECO) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ECOEvents.ECOWorkflowHoldEvent(eco, plmWorkflow, fromStatus));
        workflowEventService.workflowHold("ECO", plmWorkflow);
    }

    @EventListener(condition = "#event.attachedToType.name() == 'CHANGE' && #event.attachedToObject.changeType.name() == 'ECO'")
    public void ecoWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PLMECO eco = (PLMECO) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ECOEvents.ECOWorkflowUnholdEvent(eco, plmWorkflow, fromStatus));
        workflowEventService.workflowUnhold("ECO", plmWorkflow);
    }


    @Transactional(readOnly = true)
    public List<PLMAffectedItem> getEcoAffectedItems(Integer ecoId) {
        List<PLMAffectedItem> affectedElements = new ArrayList<>();
        List<PLMAffectedItem> affectedItems = ecoAffectedItemRepository.findByChange(ecoId);
        for (PLMAffectedItem affectedItem : affectedItems) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            affectedItem.setItemObject(item);
            affectedItem.setRevisedItem(itemRevision);
            if (affectedItem.getChangeRequests().length > 0) {
                List<Integer> ecrIds = new ArrayList<>();
                for (int i = 0; i < affectedItem.getChangeRequests().length; i++) {
                    ecrIds.add(affectedItem.getChangeRequests()[i]);
                }
                affectedItem.getEcrList().addAll(ecrRepository.findByIdIn(ecrIds));
            }

            affectedElements.add(affectedItem);
        }

        return affectedElements;
    }

    @Transactional(readOnly = true)
    public Long getEcoCount() {
        return ecoRepository.count();
    }


    @Transactional(readOnly = true)
    public Page<PLMChange> getChangeObjects(ECOCriteria criteria, Pageable pageable) {
        Predicate predicate = changePredicateBuilder.getDefaultPredicates(criteria, QPLMChange.pLMChange);
        return changeRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public PLMChange getChangeObject(Integer id) {
        return changeRepository.findOne(id);
    }


    @Transactional(readOnly = true)
    public List<AffectedItemDto> getChangeAffectedItems(Integer id) {
        List<AffectedItemDto> dtoList = new LinkedList<>();
        List<PLMAffectedItem> affectedItems = affectedItemRepository.findByChange(id);
        affectedItems.forEach(affectedItem -> {
            AffectedItemDto affectedItemDto = new AffectedItemDto();
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());

            affectedItemDto.setId(affectedItem.getId());
            affectedItemDto.setItem(affectedItem.getItem());
            affectedItemDto.setItemName(item.getItemName());
            affectedItemDto.setItemNumber(item.getItemNumber());
            affectedItemDto.setDescription(item.getDescription());
            affectedItemDto.setFromRevision(affectedItem.getFromRevision());
            affectedItemDto.setToRevision(affectedItem.getToRevision());
            item.getItemType().getLifecycle().getPhases().forEach(plmLifeCyclePhase -> {
                if (plmLifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                    affectedItemDto.getReleasedLifecyclePhases().add(plmLifeCyclePhase);
                } else if (plmLifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.CANCELLED)) {
                    affectedItemDto.getRejectedLifecyclePhases().add(plmLifeCyclePhase);
                } else if (!plmLifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.OBSOLETE)) {
                    affectedItemDto.getNormalLifecyclePhases().add(plmLifeCyclePhase);
                }
            });
            affectedItemDto.setToItem(affectedItem.getToItem());
            affectedItemDto.setItemTypeName(item.getItemType().getName());
            affectedItemDto.setFromLifecyclePhase(itemRevision.getLifeCyclePhase());
            dtoList.add(affectedItemDto);
        });
        return dtoList;
    }



    @Transactional(readOnly = true)
    public ChangeObjectsFilterDto getChangeTypeFilterObjects(String type) {
        ChangeObjectsFilterDto filterTypes = new ChangeObjectsFilterDto();
        if (type.equals("ECR")) {
            filterTypes.setChangeAnalysts(ecrService.getChangeAnalysts());
            filterTypes.setChangeReasonTypes(ecrService.getChangeReasonType());
            filterTypes.setOriginators(ecrService.getOriginators());
            filterTypes.setUrgencies(ecrService.getUrgency());
            filterTypes.setRequestedBys(ecrService.getRequesters());
        } else {
            filterTypes.setChangeAnalysts(dcrService.getChangeAnalysts());
            filterTypes.setChangeReasonTypes(dcrService.getChangeReasonType());
            filterTypes.setOriginators(dcrService.getOriginators());
            filterTypes.setUrgencies(dcrService.getUrgency());
            filterTypes.setRequestedBys(dcrService.getRequestedBy());
        }

        return filterTypes;
    }
}
