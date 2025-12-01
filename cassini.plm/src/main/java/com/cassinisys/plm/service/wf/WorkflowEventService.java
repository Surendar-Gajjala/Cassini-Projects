package com.cassinisys.plm.service.wf;

import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.event.ItemEvents;
import com.cassinisys.plm.model.cm.ChangeType;
import com.cassinisys.plm.model.cm.PLMAffectedItem;
import com.cassinisys.plm.model.cm.PLMChange;
import com.cassinisys.plm.model.cm.PLMMCOProductAffectedItem;
import com.cassinisys.plm.model.cm.dto.AffectedItemDto;
import com.cassinisys.plm.model.cm.dto.MCOProductAffectedItemDto;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.LifecycleDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowEvent;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.cm.AffectedItemRepository;
import com.cassinisys.plm.repo.cm.ChangeRepository;
import com.cassinisys.plm.repo.cm.MCOProductAffectedItemRepository;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionStatusHistoryRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
public class WorkflowEventService {
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionStatusHistoryRepository revisionStatusHistoryRepository;
    @Autowired
    private BOPRevisionStatusHistoryRepository bopRevisionStatusHistoryRepository;
    @Autowired
    private ChangeRepository changeRepository;
    @Autowired
    private PLMWorkflowEventRepository plmWorkflowEventRepository;
    @Autowired
    private AffectedItemRepository affectedItemRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MCOProductAffectedItemRepository mcoProductAffectedItemRepository;
    @Autowired
    private MESMBOMRevisionRepository mesmbomRevisionRepository;
    @Autowired
    private MESMBOMRepository mesmbomRepository;
    @Autowired
    private MBOMRevisionStatusHistoryRepository mbomRevisionStatusHistoryRepository;
    @Autowired
    private BOPRevisionRepository bopRevisionRepository;
    @Autowired
    private MESBOPRepository mesbopRepository;

    @Transactional
    public void workflowStart(String objectType, PLMWorkflow plmWorkflow) {
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowAndEventTypeOrderByIdAsc(plmWorkflow.getId(), "WorkflowStart");
        if (workflowEvents.size() > 0) {
            workflowEvents.forEach(workflowEvent -> {
                if ((objectType.equals("ECO") || objectType.equals("DCO") || objectType.equals("MCO")) && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    PLMChange change = changeRepository.findOne(plmWorkflow.getAttachedTo());
                    if (change != null && change.getRevisionsCreated()) {
                        setNextLifecyclePhase(workflowEvent, change);
                    }
                } else if (objectType.equals("BOPREVISION") && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    MESBOPRevision mesbopRevision = bopRevisionRepository.findOne(plmWorkflow.getAttachedTo());
                    setNextBopLifecyclePhase(workflowEvent, mesbopRevision);
                }
            });
        }
    }

    @Transactional
    public void workflowFinish(String objectType, PLMWorkflow plmWorkflow) {
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowAndEventTypeOrderByIdAsc(plmWorkflow.getId(), "WorkflowFinish");
        if (workflowEvents.size() > 0) {
            workflowEvents.forEach(workflowEvent -> {
                if ((objectType.equals("ECO") || objectType.equals("DCO") || objectType.equals("MCO")) && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    PLMChange change = changeRepository.findOne(plmWorkflow.getAttachedTo());
                    if (change != null && change.getRevisionsCreated()) {
                        setNextLifecyclePhase(workflowEvent, change);
                    }
                } else if (objectType.equals("BOPREVISION") && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    MESBOPRevision mesbopRevision = bopRevisionRepository.findOne(plmWorkflow.getAttachedTo());
                    setNextBopLifecyclePhase(workflowEvent, mesbopRevision);
                }
            });
        }
    }

    @Transactional
    public void workflowActivityFinishDemote(String objectType, PLMWorkflow plmWorkflow, PLMWorkflowStatus workflowStatus) {
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowAndActivityAndEventTypeOrderByIdAsc(plmWorkflow.getId(), workflowStatus, "WorkflowActivityFinish");
        if (workflowEvents.size() > 0) {
            workflowEvents.forEach(workflowEvent -> {
                if ((objectType.equals("ECO") || objectType.equals("DCO") || objectType.equals("MCO")) && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    PLMChange change = changeRepository.findOne(plmWorkflow.getAttachedTo());
                    if (change != null && change.getRevisionsCreated()) {
                        setOldLifecyclePhase(workflowEvent, change);
                    }
                } else if (objectType.equals("BOPREVISION") && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    MESBOPRevision mesbopRevision = bopRevisionRepository.findOne(plmWorkflow.getAttachedTo());
                    setOldBopLifecyclePhase(workflowEvent, mesbopRevision);
                }
            });
        }
    }

    @Transactional
    public void workflowActivityStartDemote(String objectType, PLMWorkflow plmWorkflow, PLMWorkflowStatus workflowStatus) {
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowAndActivityAndEventTypeOrderByIdAsc(plmWorkflow.getId(), workflowStatus, "WorkflowActivityStart");
        if (workflowEvents.size() > 0) {
            workflowEvents.forEach(workflowEvent -> {
                if ((objectType.equals("ECO") || objectType.equals("DCO") || objectType.equals("MCO")) && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    PLMChange change = changeRepository.findOne(plmWorkflow.getAttachedTo());
                    if (change != null && change.getRevisionsCreated()) {
                        setOldLifecyclePhase(workflowEvent, change);
                    }
                } else if (objectType.equals("BOPREVISION") && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    MESBOPRevision mesbopRevision = bopRevisionRepository.findOne(plmWorkflow.getAttachedTo());
                    setOldBopLifecyclePhase(workflowEvent, mesbopRevision);
                }
            });
        }
    }

    public void workflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        if (event.getPlmWorkflow() != null) {

        }
    }

    @Transactional
    public void workflowHold(String objectType, PLMWorkflow plmWorkflow) {
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowAndEventTypeOrderByIdAsc(plmWorkflow.getId(), "WorkflowHold");
        if (workflowEvents.size() > 0) {
            workflowEvents.forEach(workflowEvent -> {
                if ((objectType.equals("ECO") || objectType.equals("DCO") || objectType.equals("MCO")) && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    PLMChange change = changeRepository.findOne(plmWorkflow.getAttachedTo());
                    if (change != null && change.getRevisionsCreated()) {
                        setNextLifecyclePhase(workflowEvent, change);
                    }
                } else if (objectType.equals("BOPREVISION") && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    MESBOPRevision mesbopRevision = bopRevisionRepository.findOne(plmWorkflow.getAttachedTo());
                    setNextBopLifecyclePhase(workflowEvent, mesbopRevision);
                }
            });
        }
    }

    @Transactional
    public void workflowUnhold(String objectType, PLMWorkflow plmWorkflow) {
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowAndEventTypeOrderByIdAsc(plmWorkflow.getId(), "WorkflowUnHold");
        if (workflowEvents.size() > 0) {
            workflowEvents.forEach(workflowEvent -> {
                if ((objectType.equals("ECO") || objectType.equals("DCO") || objectType.equals("MCO")) && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    PLMChange change = changeRepository.findOne(plmWorkflow.getAttachedTo());
                    if (change != null && change.getRevisionsCreated()) {
                        setNextLifecyclePhase(workflowEvent, change);
                    }
                } else if (objectType.equals("BOPREVISION") && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    MESBOPRevision mesbopRevision = bopRevisionRepository.findOne(plmWorkflow.getAttachedTo());
                    setNextBopLifecyclePhase(workflowEvent, mesbopRevision);
                }
            });
        }
    }

    @Transactional
    public void workflowActivityStart(String objectType, PLMWorkflow plmWorkflow, PLMWorkflowStatus workflowStatus) {
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowAndActivityAndEventTypeOrderByIdAsc(plmWorkflow.getId(), workflowStatus, "WorkflowActivityStart");
        if (workflowEvents.size() > 0) {
            workflowEvents.forEach(workflowEvent -> {
                if ((objectType.equals("ECO") || objectType.equals("DCO") || objectType.equals("MCO")) && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    PLMChange change = changeRepository.findOne(plmWorkflow.getAttachedTo());
                    if (change != null && change.getRevisionsCreated()) {
                        setNextLifecyclePhase(workflowEvent, change);
                    }
                } else if (objectType.equals("BOPREVISION") && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    MESBOPRevision mesbopRevision = bopRevisionRepository.findOne(plmWorkflow.getAttachedTo());
                    setNextBopLifecyclePhase(workflowEvent, mesbopRevision);
                }
            });
        }
    }

    @Transactional
    public void workflowActivityFinish(String objectType, PLMWorkflow plmWorkflow, PLMWorkflowStatus workflowStatus) {
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowAndActivityAndEventTypeOrderByIdAsc(plmWorkflow.getId(), workflowStatus, "WorkflowActivityFinish");
        if (workflowEvents.size() > 0) {
            workflowEvents.forEach(workflowEvent -> {
                if ((objectType.equals("ECO") || objectType.equals("DCO") || objectType.equals("MCO")) && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    PLMChange change = changeRepository.findOne(plmWorkflow.getAttachedTo());
                    if (change != null && change.getRevisionsCreated()) {
                        setNextLifecyclePhase(workflowEvent, change);
                    }
                } else if (objectType.equals("BOPREVISION") && workflowEvent.getActionType().equals("SetLifecyclePhase")) {
                    MESBOPRevision mesbopRevision = bopRevisionRepository.findOne(plmWorkflow.getAttachedTo());
                    setNextBopLifecyclePhase(workflowEvent, mesbopRevision);
                }
            });
        }
    }

    @Transactional
    public void setNextLifecyclePhase(PLMWorkflowEvent workflowEvent, PLMChange change) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (change.getChangeType().equals(ChangeType.ECO) || change.getChangeType().equals(ChangeType.DCO)) {
            try {
                List<AffectedItemDto> affectedItemDto = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<AffectedItemDto>>() {
                });
                if (affectedItemDto.size() > 0) {
                    for (AffectedItemDto itemDto : affectedItemDto) {
                        PLMAffectedItem affectedItem = affectedItemRepository.findByChangeAndItem(change.getId(), itemDto.getItem());
                        if (affectedItem != null && affectedItem.getToItem() != null) {
                            PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getToItem());
                            PLMLifeCyclePhase oldLifecyclePhase = itemRevision.getLifeCyclePhase();
                            if (itemDto.getToLifecyclePhase() != null && !oldLifecyclePhase.getId().equals(itemDto.getToLifecyclePhase().getId())) {
                                itemRevision.setLifeCyclePhase(itemDto.getToLifecyclePhase());
                                itemDto.setOldToLifecyclePhase(oldLifecyclePhase);
                                itemDto.setToItem(affectedItem.getToItem());
                                PLMItemRevisionStatusHistory statusHistory = new PLMItemRevisionStatusHistory();
                                statusHistory.setItem(itemRevision.getId());
                                statusHistory.setOldStatus(oldLifecyclePhase);
                                statusHistory.setNewStatus(itemDto.getToLifecyclePhase());
                                statusHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                                statusHistory.setTimestamp(new Date());
                                revisionStatusHistoryRepository.save(statusHistory);
                                itemRevision = itemRevisionRepository.save(itemRevision);
                                if (!itemRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                                    applicationEventPublisher.publishEvent(new ItemEvents.ItemStatusChangedEvent(item, itemRevision, change, itemDto.getOldToLifecyclePhase(), itemDto.getToLifecyclePhase()));
                                }

                                if (itemRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                                    List<PLMItemRevision> revisions = itemRevisionRepository.findByInstanceOrderByCreatedDateDesc(itemRevision.getId());
                                    if (revisions.size() > 0) {
                                        for (PLMItemRevision revision : revisions) {
                                            PLMItem itemMaster = itemRepository.findOne(revision.getItemMaster());
                                            PLMItemRevision toRevision = itemRevisionRepository.getByItemMasterAndRevision(itemMaster.getId(), affectedItem.getToRevision());
                                            PLMLifeCyclePhase phase = toRevision.getLifeCyclePhase();
                                            toRevision.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
                                            toRevision = itemRevisionRepository.save(toRevision);
                                            statusHistory = new PLMItemRevisionStatusHistory();
                                            statusHistory.setItem(toRevision.getId());
                                            statusHistory.setOldStatus(phase);
                                            statusHistory.setNewStatus(toRevision.getLifeCyclePhase());
                                            statusHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                                            statusHistory.setTimestamp(new Date());
                                            revisionStatusHistoryRepository.save(statusHistory);
                                            itemMaster.setLatestReleasedRevision(toRevision.getId());
                                            itemRepository.save(itemMaster);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    String actionData = objectMapper.writeValueAsString(affectedItemDto);

                    workflowEvent.setActionData(actionData);
                    workflowEvent = plmWorkflowEventRepository.save(workflowEvent);

                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else if (change.getChangeType().equals(ChangeType.MCO)) {
            try {
                List<MCOProductAffectedItemDto> productAffectedItemDtos = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<MCOProductAffectedItemDto>>() {
                });
                if (productAffectedItemDtos.size() > 0) {
                    for (MCOProductAffectedItemDto itemDto : productAffectedItemDtos) {
                        PLMMCOProductAffectedItem affectedItem = mcoProductAffectedItemRepository.findByMcoAndItem(change.getId(), itemDto.getItem());
                        if (affectedItem != null && affectedItem.getToItem() != null) {
                            MESMBOMRevision mesmbomRevision = mesmbomRevisionRepository.findOne(affectedItem.getToItem());
                            PLMLifeCyclePhase oldLifecyclePhase = mesmbomRevision.getLifeCyclePhase();
                            if (itemDto.getToLifecyclePhase() != null && !oldLifecyclePhase.getId().equals(itemDto.getToLifecyclePhase().getId())) {
                                mesmbomRevision.setLifeCyclePhase(itemDto.getToLifecyclePhase());
                                itemDto.setOldToLifecyclePhase(oldLifecyclePhase);
                                itemDto.setToItem(affectedItem.getToItem());
                                MESMBOMRevisionStatusHistory statusHistory = new MESMBOMRevisionStatusHistory();
                                statusHistory.setMbomRevision(mesmbomRevision.getId());
                                statusHistory.setOldStatus(oldLifecyclePhase);
                                statusHistory.setNewStatus(itemDto.getToLifecyclePhase());
                                statusHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                                statusHistory.setTimestamp(new Date());
                                mbomRevisionStatusHistoryRepository.save(statusHistory);
                                mesmbomRevision = mesmbomRevisionRepository.save(mesmbomRevision);
                                if (!mesmbomRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                                    MESMBOM item = mesmbomRepository.findOne(mesmbomRevision.getMaster());
//                                    applicationEventPublisher.publishEvent(new ItemEvents.ItemStatusChangedEvent(item, mesmbomRevision, change, itemDto.getOldToLifecyclePhase(), itemDto.getToLifecyclePhase()));
                                }
                            }
                        }
                    }

                    String actionData = objectMapper.writeValueAsString(productAffectedItemDtos);

                    workflowEvent.setActionData(actionData);
                    workflowEvent = plmWorkflowEventRepository.save(workflowEvent);

                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOldLifecyclePhase(PLMWorkflowEvent workflowEvent, PLMChange change) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (change.getChangeType().equals(ChangeType.DCO) || change.getChangeType().equals(ChangeType.ECO)) {
            try {
                List<AffectedItemDto> affectedItemDto = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<AffectedItemDto>>() {
                });
                if (affectedItemDto.size() > 0) {
                    for (AffectedItemDto itemDto : affectedItemDto) {
                        PLMAffectedItem affectedItem = affectedItemRepository.findByChangeAndItem(change.getId(), itemDto.getItem());
                        if (affectedItem != null && affectedItem.getToItem() != null) {
                            PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getToItem());
                            PLMLifeCyclePhase oldLifecyclePhase = itemRevision.getLifeCyclePhase();
                            if (itemDto.getToLifecyclePhase() != null && itemDto.getOldToLifecyclePhase() != null && !oldLifecyclePhase.getId().equals(itemDto.getOldToLifecyclePhase().getId())) {
                                itemRevision.setLifeCyclePhase(itemDto.getOldToLifecyclePhase());
                                itemDto.setToItem(affectedItem.getToItem());
                                PLMItemRevisionStatusHistory statusHistory = new PLMItemRevisionStatusHistory();
                                statusHistory.setItem(itemRevision.getId());
                                statusHistory.setOldStatus(oldLifecyclePhase);
                                statusHistory.setNewStatus(itemDto.getOldToLifecyclePhase());
                                statusHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                                statusHistory.setTimestamp(new Date());
                                revisionStatusHistoryRepository.save(statusHistory);
                                itemRevision = itemRevisionRepository.save(itemRevision);
                                if (!itemRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                                    applicationEventPublisher.publishEvent(new ItemEvents.ItemStatusChangedEvent(item, itemRevision, change, oldLifecyclePhase, itemRevision.getLifeCyclePhase()));
                                }
                            }
                        }
                    }

                    String actionData = objectMapper.writeValueAsString(affectedItemDto);

                    workflowEvent.setActionData(actionData);
                    workflowEvent = plmWorkflowEventRepository.save(workflowEvent);

                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                List<MCOProductAffectedItemDto> productAffectedItemDtos = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<MCOProductAffectedItemDto>>() {
                });
                if (productAffectedItemDtos.size() > 0) {
                    for (MCOProductAffectedItemDto itemDto : productAffectedItemDtos) {
                        PLMMCOProductAffectedItem affectedItem = mcoProductAffectedItemRepository.findByMcoAndItem(change.getId(), itemDto.getItem());
                        if (affectedItem != null && affectedItem.getToItem() != null) {
                            MESMBOMRevision mesmbomRevision = mesmbomRevisionRepository.findOne(affectedItem.getToItem());
                            PLMLifeCyclePhase oldLifecyclePhase = mesmbomRevision.getLifeCyclePhase();
                            if (itemDto.getToLifecyclePhase() != null && itemDto.getOldToLifecyclePhase() != null && !oldLifecyclePhase.getId().equals(itemDto.getOldToLifecyclePhase().getId())) {
                                mesmbomRevision.setLifeCyclePhase(itemDto.getOldToLifecyclePhase());
                                itemDto.setToItem(affectedItem.getToItem());
                                MESMBOMRevisionStatusHistory statusHistory = new MESMBOMRevisionStatusHistory();
                                statusHistory.setMbomRevision(mesmbomRevision.getId());
                                statusHistory.setOldStatus(oldLifecyclePhase);
                                statusHistory.setNewStatus(itemDto.getOldToLifecyclePhase());
                                statusHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                                statusHistory.setTimestamp(new Date());
                                mbomRevisionStatusHistoryRepository.save(statusHistory);
                                mesmbomRevision = mesmbomRevisionRepository.save(mesmbomRevision);
                                if (!mesmbomRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
//                                    MESMBOM mesmbom = mesmbomRepository.findOne(mesmbomRevision.getMaster());
//                                    applicationEventPublisher.publishEvent(new ItemEvents.ItemStatusChangedEvent(mesmbom, mesmbomRevision, change, oldLifecyclePhase, mesmbomRevision.getLifeCyclePhase()));
                                }
                            }
                        }
                    }

                    String actionData = objectMapper.writeValueAsString(productAffectedItemDtos);

                    workflowEvent.setActionData(actionData);
                    workflowEvent = plmWorkflowEventRepository.save(workflowEvent);

                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }


    @Transactional
    public void setNextBopLifecyclePhase(PLMWorkflowEvent workflowEvent, MESBOPRevision mesbopRevision) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<LifecycleDto> lifecycleDtos = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<LifecycleDto>>() {
            });
            if (lifecycleDtos.size() > 0) {
                PLMLifeCyclePhase oldLifecyclePhase = mesbopRevision.getLifeCyclePhase();
                mesbopRevision.setLifeCyclePhase(lifecycleDtos.get(0).getLifecyclePhase());
                if (!oldLifecyclePhase.getId().equals(mesbopRevision.getLifeCyclePhase().getId())) {
                    lifecycleDtos.get(0).setOldLifecyclePhase(oldLifecyclePhase);
                    MESBOPRevisionStatusHistory statusHistory = new MESBOPRevisionStatusHistory();
                    statusHistory.setBopRevision(mesbopRevision.getId());
                    statusHistory.setOldStatus(oldLifecyclePhase);
                    statusHistory.setNewStatus(mesbopRevision.getLifeCyclePhase());
                    statusHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    statusHistory.setTimestamp(new Date());
                    bopRevisionStatusHistoryRepository.save(statusHistory);
                }
                mesbopRevision = bopRevisionRepository.save(mesbopRevision);
                /*if (!mesbopRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                    MESBOP mesbop = mesbopRepository.findOne(mesbopRevision.getMaster());
                    applicationEventPublisher.publishEvent(new ItemEvents.ItemStatusChangedEvent(item, itemRevision, change, itemDto.getOldToLifecyclePhase(), itemDto.getToLifecyclePhase()));
                }*/
                String actionData = objectMapper.writeValueAsString(lifecycleDtos);
                workflowEvent.setActionData(actionData);
                workflowEvent = plmWorkflowEventRepository.save(workflowEvent);

            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    public void setOldBopLifecyclePhase(PLMWorkflowEvent workflowEvent, MESBOPRevision mesbopRevision) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<LifecycleDto> lifecycleDtos = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<LifecycleDto>>() {
            });
            if (lifecycleDtos.size() > 0) {
                PLMLifeCyclePhase oldLifecyclePhase = mesbopRevision.getLifeCyclePhase();
                mesbopRevision.setLifeCyclePhase(lifecycleDtos.get(0).getOldLifecyclePhase());
                if (!oldLifecyclePhase.getId().equals(mesbopRevision.getLifeCyclePhase().getId())) {
                    MESBOPRevisionStatusHistory statusHistory = new MESBOPRevisionStatusHistory();
                    statusHistory.setBopRevision(mesbopRevision.getId());
                    statusHistory.setOldStatus(oldLifecyclePhase);
                    statusHistory.setNewStatus(mesbopRevision.getLifeCyclePhase());
                    statusHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    statusHistory.setTimestamp(new Date());
                    bopRevisionStatusHistoryRepository.save(statusHistory);
                }
                mesbopRevision = bopRevisionRepository.save(mesbopRevision);
                /*if (!mesbopRevision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                    MESMBOM mesmbom = mesmbomRepository.findOne(mesbopRevision.getMaster());
                    applicationEventPublisher.publishEvent(new ItemEvents.ItemStatusChangedEvent(mesmbom, mesmbomRevision, change, oldLifecyclePhase, mesmbomRevision.getLifeCyclePhase()));
                }*/
                String actionData = objectMapper.writeValueAsString(lifecycleDtos);
                workflowEvent.setActionData(actionData);
                workflowEvent = plmWorkflowEventRepository.save(workflowEvent);

            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
