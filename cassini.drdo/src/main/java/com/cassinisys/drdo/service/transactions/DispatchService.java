package com.cassinisys.drdo.service.transactions;

import com.cassinisys.drdo.filtering.DispatchCriteria;
import com.cassinisys.drdo.filtering.DispatchPredicateBuilder;
import com.cassinisys.drdo.filtering.NotificationCriteria;
import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.bom.ItemInstanceStatus;
import com.cassinisys.drdo.model.bom.ItemInstanceStatusHistory;
import com.cassinisys.drdo.model.bom.ItemType;
import com.cassinisys.drdo.model.dto.NotificationDto;
import com.cassinisys.drdo.model.inventory.Storage;
import com.cassinisys.drdo.model.transactions.*;
import com.cassinisys.drdo.repo.DRDOUpdateRepository;
import com.cassinisys.drdo.repo.bom.ItemInstanceRepository;
import com.cassinisys.drdo.repo.bom.ItemInstanceStatusHistoryRepository;
import com.cassinisys.drdo.repo.bom.ItemTypeRepository;
import com.cassinisys.drdo.repo.bom.LotInstanceRepository;
import com.cassinisys.drdo.repo.inventory.StorageRepository;
import com.cassinisys.drdo.repo.transactions.*;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by subra on 26-11-2018.
 */
@Service
public class DispatchService implements CrudService<Dispatch, Integer> {

    @Autowired
    private DispatchRepository dispatchRepository;

    @Autowired
    private DispatchItemRepository dispatchItemRepository;

    @Autowired
    private InwardRepository inwardRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private InwardItemRepository inwardItemRepository;

    @Autowired
    private InwardItemInstanceRepository inwardItemInstanceRepository;

    @Autowired
    private ItemInstanceRepository itemInstanceRepository;

    @Autowired
    private ItemInstanceStatusHistoryRepository itemInstanceStatusHistoryRepository;

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private DispatchPredicateBuilder dispatchPredicateBuilder;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private GatePassRepository gatePassRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private RequestItemRepository requestItemRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private LotInstanceRepository lotInstanceRepository;

    @Autowired
    private DRDOUpdateRepository drdoUpdateRepository;

    @Autowired
    private IssueItemRepository issueItemRepository;

    @Override
    @Transactional(readOnly = false)
    public Dispatch create(Dispatch dispatch) {

        List<ItemInstance> itemInstances = dispatch.getItemInstances();

        AutoNumber autoNumber = autoNumberRepository.findByName("Default Dispatch Number Source");
        String dispatchNumber = null;
        if (autoNumber != null) {
            dispatchNumber = autoNumber.next();
        }

        dispatch.setNumber(dispatchNumber);

        dispatch = dispatchRepository.save(dispatch);

        autoNumber = autoNumberRepository.save(autoNumber);

        for (ItemInstance itemInstance : itemInstances) {
            DispatchItem dispatchItem = new DispatchItem();
            dispatchItem.setDispatch(dispatch.getId());
            dispatchItem.setItem(itemInstance);

            dispatchItem = dispatchItemRepository.save(dispatchItem);
        }

        return dispatch;
    }

    @Override
    @Transactional(readOnly = false)
    public Dispatch update(Dispatch dispatch) {

        List<DispatchItem> dispatchItems = dispatch.getDispatchItems();

        dispatch.setStatus(DispatchStatus.DISPATCHED);
        dispatch = dispatchRepository.save(dispatch);

        for (DispatchItem dispatchItem : dispatchItems) {

            ItemInstance itemInstance = itemInstanceRepository.findOne(dispatchItem.getItem().getId());

            Storage storage = itemInstance.getStorage();

            if (storage != null) {
                if (itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                    storage.setRemainingCapacity(storage.getRemainingCapacity() + itemInstance.getLotSize());
                } else {
                    storage.setRemainingCapacity(storage.getRemainingCapacity() + 1);
                }
                storage = storageRepository.save(storage);
            }

            itemInstance.setStorage(null);
            itemInstance.setStatus(ItemInstanceStatus.DISPATCH);
            itemInstance.setPresentStatus(ItemInstanceStatus.DISPATCH.toString());
            itemInstance = itemInstanceRepository.save(itemInstance);

            ItemInstanceStatusHistory itemInstanceStatusHistory = new ItemInstanceStatusHistory();
            itemInstanceStatusHistory.setItemInstance(itemInstance);
            itemInstanceStatusHistory.setStatus(ItemInstanceStatus.DISPATCH);
            itemInstanceStatusHistory.setPresentStatus(ItemInstanceStatus.DISPATCH.toString());
            itemInstanceStatusHistory.setTimestamp(new Date());
            itemInstanceStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            itemInstanceStatusHistory = itemInstanceStatusHistoryRepository.save(itemInstanceStatusHistory);

        }


        return dispatch;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer dispatchId) {
        dispatchRepository.delete(dispatchId);
    }

    @Override
    @Transactional(readOnly = true)
    public Dispatch get(Integer dispatchId) {

        Dispatch dispatch = dispatchRepository.findOne(dispatchId);

        dispatch.getDispatchItems().addAll(dispatchItemRepository.findByDispatch(dispatchId));

        dispatch.getDispatchItems().forEach(dispatchItem -> {
            ItemType itemType = itemTypeRepository.findOne(dispatchItem.getItem().getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                dispatchItem.getItem().getItem().getItemMaster().setParentType(itemType);
            } else {
                dispatchItem.getItem().getItem().getItemMaster().setParentType(dispatchItem.getItem().getItem().getItemMaster().getItemType());
            }
        });

        return dispatch;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dispatch> getAll() {
        return dispatchRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Dispatch> getAllDispatches(Pageable pageable, DispatchCriteria criteria) {
        Predicate predicate = dispatchPredicateBuilder.build(criteria, QDispatch.dispatch);
        return dispatchRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public List<ItemInstance> getItemsToDispatchByBomId(Integer bomId, String type) {
        List<ItemInstance> itemInstances = new ArrayList<>();

        List<Inward> inwards = inwardRepository.findInwardsByBom(bomId);

        List<InwardItem> inwardItems = new ArrayList<>();

        inwards.forEach(inward -> {
            inwardItems.addAll(inwardItemRepository.findByInward(inward.getId()));
        });

        List<InwardItemInstance> inwardItemInstances = new ArrayList<>();
        if (inwardItems.size() > 0) {
            inwardItems.forEach(inwardItem -> {
                inwardItemInstances.addAll(inwardItemInstanceRepository.getItemsToDispatch(inwardItem.getId(), ItemInstanceStatus.valueOf(type)));
            });
        }

        List<Dispatch> dispatches = dispatchRepository.findByBom(bomId);

        List<DispatchItem> dispatchItems = new ArrayList<>();

        dispatches.forEach(dispatch -> {
            dispatchItems.addAll(dispatchItemRepository.findByDispatch(dispatch.getId()));
        });

        if (inwardItemInstances.size() > 0) {
            inwardItemInstances.forEach(inwardItemInstance -> {
                Boolean itemExist = false;
                for (DispatchItem dispatchItem : dispatchItems) {
                    if (inwardItemInstance.getItem().getId().equals(dispatchItem.getItem().getId())) {
                        itemExist = true;
                    }
                }

                if (!itemExist) {
                    itemInstances.add(inwardItemInstance.getItem());

                    ItemType itemType = itemTypeRepository.findOne(inwardItemInstance.getItem().getItem().getItemMaster().getItemType().getParentType());

                    if (!itemType.getParentNode()) {
                        inwardItemInstance.getItem().getItem().getItemMaster().setParentType(itemType);
                    } else {
                        inwardItemInstance.getItem().getItem().getItemMaster().setParentType(inwardItemInstance.getItem().getItem().getItemMaster().getItemType());
                    }
                }

            });
        }


        return itemInstances;
    }

    @Transactional(readOnly = true)
    public NotificationDto getAllNotifications(NotificationCriteria notificationCriteria) {
        NotificationDto notificationDto = new NotificationDto();

        if (notificationCriteria.getStoreApprove() && !notificationCriteria.getAdminPermission()) {
            if (notificationCriteria.getCasApprove()) {
                notificationDto.setInwards(inwardRepository.getAllNotFinishedInwards().size());
            } else {

                List<Inward> storeInwards = inwardRepository.getInwardsByStatus(InwardStatus.STORE);

                storeInwards.forEach(storeInward -> {
                    List<InwardItem> inwardItems = inwardItemRepository.findByInward(storeInward.getId());
                    List<InwardItemInstance> inwardItemInstances = new ArrayList<>();
                    inwardItems.forEach(inwardItem -> {
                        inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.NEW));
                        inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.STORE_SUBMITTED));
                    });

                    if (inwardItemInstances.size() > 0) {
                        notificationDto.setInwardItems(notificationDto.getInwardItems() + inwardItemInstances.size());
                    }
                });

                List<Inward> inventoryInwards = inwardRepository.getInwardsByStatus(InwardStatus.INVENTORY);

                inventoryInwards.forEach(inventoryInward -> {
                    List<InwardItem> inwardItems = inwardItemRepository.findByInward(inventoryInward.getId());
                    List<InwardItemInstance> inwardItemInstances = new ArrayList<>();
                    inwardItems.forEach(inwardItem -> {
                        inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.ACCEPT));
//                        inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.INVENTORY));
                    });

                    if (inwardItemInstances.size() > 0) {
                        notificationDto.setInwardItems(notificationDto.getInwardItems() + inwardItemInstances.size());
                    }
                });

                notificationDto.setInwards(storeInwards.size());

                notificationDto.setInwards(notificationDto.getInwards() + inventoryInwards.size());

                List<Inward> inwards = inwardRepository.getInwardsByStatus(InwardStatus.SSQAG);

                inwards.forEach(inward -> {

                    Boolean storeEdit = false;

                    List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward.getId());
                    List<InwardItemInstance> inwardItemInstances = new ArrayList<>();
                    List<InwardItemInstance> provisionalItemInstances = new ArrayList<>();
                    List<InwardItemInstance> storeSubmittedItemInstances = new ArrayList<>();
                    inwardItems.forEach(inwardItem -> {
                        inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.REVIEW));
//                        inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.INVENTORY));
                        inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.ACCEPT));
                        inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.REJECTED));
                        inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.P_ACCEPT));
                        storeSubmittedItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.STORE_SUBMITTED));
                        provisionalItemInstances.addAll(inwardItemInstanceRepository.getProvisionallyAcceptedInwardItemInstances(inwardItem.getId()));
                    });


                    if (inwardItemInstances.size() > 0 || provisionalItemInstances.size() > 0 || storeSubmittedItemInstances.size() > 0) {

                        notificationDto.setInwardItems(notificationDto.getInwardItems() + inwardItemInstances.size() + storeSubmittedItemInstances.size());

                        if (storeSubmittedItemInstances.size() > 0 || provisionalItemInstances.size() > 0) {
                            notificationDto.setInwards(notificationDto.getInwards() + 1);
                        }
                    }
                });

                List<Request> requests = requestRepository.getNotIssuedApproveRequestsByStatus(RequestStatus.REQUESTED);

                notificationDto.setRequests(requests.size());

                List<Issue> issues = issueRepository.findByStatus(IssueStatus.BDL_PPC);
                issues.addAll(issueRepository.findByStatus(IssueStatus.VERSITY_PPC));
                issues.addAll(issueRepository.findByStatus(IssueStatus.PARTIALLY_RECEIVED));
                issues.addAll(issueRepository.findByStatus(IssueStatus.PARTIALLY_APPROVED));
                issues.addAll(issueRepository.findByStatus(IssueStatus.ITEM_RESET));
                issues.addAll(issueRepository.findByStatus(IssueStatus.RECEIVED));
                notificationDto.setIssues(issues.size());
            }

        }

        if (notificationCriteria.getSsqagApprove() && !notificationCriteria.getAdminPermission()) {
            notificationDto.setInwards(notificationDto.getInwards() + inwardRepository.getInwardsByStatus(InwardStatus.SSQAG).size());

            List<Inward> inwards = inwardRepository.getInwardsByStatus(InwardStatus.SSQAG);

            inwards.forEach(inward -> {

                List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward.getId());
                List<InwardItemInstance> inwardItemInstances = new ArrayList<>();
                inwardItems.forEach(inwardItem -> {
                    inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.REVIEWED));
                    inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.STORE_SUBMITTED));
                });

                if (inwardItemInstances.size() > 0) {
                    notificationDto.setInwardItems(notificationDto.getInwardItems() + inwardItemInstances.size());
                }
            });
        }

        if (notificationCriteria.getAdminPermission()) {

            List<Inward> inwards = inwardRepository.getAllNotFinishedInwards();

            notificationDto.setInwards(inwards.size());

            inwards.forEach(inward -> {
                List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward.getId());
                List<InwardItemInstance> inwardItemInstances = new ArrayList<>();
                inwardItems.forEach(inwardItem -> {
                    inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.NEW));
                    inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.STORE_SUBMITTED));
                    inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.ACCEPT));
                    inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.P_ACCEPT));
//                    inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.INVENTORY));
                    inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.REVIEW));
                    inwardItemInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.REVIEWED));
                });

                if (inwardItemInstances.size() > 0) {
                    notificationDto.setInwardItems(notificationDto.getInwardItems() + inwardItemInstances.size());
                }
            });


        }

        if (notificationCriteria.getCasApprove() && !notificationCriteria.getStoreApprove()) {

            List<Inward> inwards = inwardRepository.getAllNotFinishedInwards();

            notificationDto.setInwards(inwards.size());
        }

        if (notificationCriteria.getAdminPermission()) {
            notificationDto.setRequests(requestRepository.getNotIssuedApproveRequestsByStatus(RequestStatus.REQUESTED).size());

            notificationDto.setIssues(issueRepository.findByStatus(IssueStatus.BDL_QC).size());
            notificationDto.setIssues(notificationDto.getIssues() + issueRepository.findByStatus(IssueStatus.VERSITY_QC).size());
            notificationDto.setIssues(notificationDto.getIssues() + issueRepository.findByStatus(IssueStatus.ITEM_RESET).size());
            notificationDto.setIssues(notificationDto.getIssues() + issueRepository.findByStatus(IssueStatus.STORE).size());
            notificationDto.setIssues(notificationDto.getIssues() + issueRepository.findByStatus(IssueStatus.PARTIALLY_RECEIVED).size());
            notificationDto.setIssues(notificationDto.getIssues() + issueRepository.findByStatus(IssueStatus.PARTIALLY_APPROVED).size());
            notificationDto.setIssues(notificationDto.getIssues() + issueRepository.findByStatus(IssueStatus.BDL_PPC).size());
            notificationDto.setIssues(notificationDto.getIssues() + issueRepository.findByStatus(IssueStatus.VERSITY_PPC).size());
        } else if (notificationCriteria.getBdlApprove() || notificationCriteria.getVersityApprove()) {
            if (notificationCriteria.getBdlApprove()) {
                notificationDto.setRequests(requestRepository.getRequestsByStatusAndVersityAndIssued(RequestStatus.REQUESTED, notificationCriteria.getVersity()).size());
            } else {
                notificationDto.setRequests(requestRepository.getRequestsByStatusAndVersityAndIssued(RequestStatus.REQUESTED, notificationCriteria.getVersity()).size());
            }

            if (notificationCriteria.getBdlQcApprove()) {
                List<Issue> issues = issueRepository.findByVersity(notificationCriteria.getVersity());

                issues.forEach(issue -> {
                    if ((issue.getStatus().equals(IssueStatus.BDL_QC) || issue.getStatus().equals(IssueStatus.ITEM_RESET)) && issue.getVersity().equals(notificationCriteria.getVersity())) {
                        notificationDto.setIssues(notificationDto.getIssues() + 1);
                    } else {
                        List<IssueItem> issueItems = issueItemRepository.getIssueItemByIssuedAndVersity(issue.getId(), notificationCriteria.getVersity());
                        Boolean provisionalAcceptExist = false;
                        for (IssueItem issueItem : issueItems) {
                            ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());

                            if (itemInstance != null && itemInstance.getProvisionalAccept()) {
                                provisionalAcceptExist = true;
                            }
                        }

                        if (provisionalAcceptExist) {
                            notificationDto.setIssues(notificationDto.getIssues() + 1);
                        }
                    }
                });
            } else if (notificationCriteria.getVersityQc()) {
                List<Issue> issues = issueRepository.findByVersity(notificationCriteria.getVersity());

                issues.forEach(issue -> {
                    if ((issue.getStatus().equals(IssueStatus.VERSITY_QC) || issue.getStatus().equals(IssueStatus.ITEM_RESET)) && issue.getVersity().equals(notificationCriteria.getVersity())) {
                        notificationDto.setIssues(notificationDto.getIssues() + 1);
                    } else {
                        List<IssueItem> issueItems = issueItemRepository.getIssueItemByIssuedAndVersity(issue.getId(), notificationCriteria.getVersity());
                        Boolean provisionalAcceptExist = false;
                        for (IssueItem issueItem : issueItems) {
                            ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());

                            if (itemInstance != null && itemInstance.getProvisionalAccept()) {
                                provisionalAcceptExist = true;
                            }
                        }

                        if (provisionalAcceptExist) {
                            notificationDto.setIssues(notificationDto.getIssues() + 1);
                        }
                    }
                });
            } else {
                notificationDto.setIssues(issueRepository.findByVersity(notificationCriteria.getVersity()).size());
            }
        } else if (notificationCriteria.getCasApprove()) {

            notificationDto.setRequests(requestRepository.getNotIssuedApproveRequestsByStatus(RequestStatus.REQUESTED).size());

            notificationDto.setIssues(issueRepository.findByStatus(IssueStatus.BDL_QC).size());
            notificationDto.setIssues(issueRepository.findByStatus(IssueStatus.VERSITY_QC).size());
            notificationDto.setIssues(notificationDto.getIssues() + issueRepository.findByStatus(IssueStatus.BDL_PPC).size());
            notificationDto.setIssues(notificationDto.getIssues() + issueRepository.findByStatus(IssueStatus.VERSITY_PPC).size());
            notificationDto.setIssues(notificationDto.getIssues() + issueRepository.findByStatus(IssueStatus.PARTIALLY_APPROVED).size());
            notificationDto.setIssues(notificationDto.getIssues() + issueRepository.findByStatus(IssueStatus.PARTIALLY_RECEIVED).size());
            notificationDto.setIssues(notificationDto.getIssues() + issueRepository.findByStatus(IssueStatus.ITEM_RESET).size());
        } else if (notificationCriteria.getBdlQcApprove() | notificationCriteria.getVersityQc()) {

            notificationDto.setRequests(requestRepository.getNotIssuedApproveRequestsByStatus(RequestStatus.REQUESTED).size());

            List<Issue> issues = issueRepository.findByVersity(notificationCriteria.getVersity());

            issues.forEach(issue -> {
                if (issue.getStatus().equals(IssueStatus.BDL_QC) || issue.getStatus().equals(IssueStatus.VERSITY_QC) || issue.getStatus().equals(IssueStatus.ITEM_RESET)) {
                    notificationDto.setIssues(notificationDto.getIssues() + 1);
                } else {
                    List<IssueItem> issueItems = issueItemRepository.getIssueItemByIssuedAndVersity(issue.getId(), notificationCriteria.getVersity());
                    Boolean provisionalAcceptExist = false;
                    Boolean pendingItems = false;
                    for (IssueItem issueItem : issueItems) {
                        ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());

                        if (itemInstance != null && itemInstance.getProvisionalAccept()) {
                            provisionalAcceptExist = true;
                        }

                        if (issueItem.getStatus().equals(IssueItemStatus.PENDING)) {
                            pendingItems = true;
                        }
                    }

                    if (provisionalAcceptExist) {
                        notificationDto.setIssues(notificationDto.getIssues() + 1);
                    }

                    if (pendingItems) {
                        notificationDto.setIssues(notificationDto.getIssues() + 1);
                    }
                }
            });
        } else if (notificationCriteria.getBdlPpcReceive()) {


            List<Request> requestList = requestRepository.getRequestsByStatusAndVersityAndIssued(RequestStatus.REQUESTED, notificationCriteria.getVersity());
            requestList.addAll(requestRepository.getRequestsByStatusAndVersityAndIssued(RequestStatus.APPROVED, notificationCriteria.getVersity()));

            notificationDto.setRequests(requestList.size());

            List<Issue> issues = issueRepository.findByStatus(IssueStatus.BDL_PPC);
            issues.addAll(issueRepository.findByStatusAndVersity(IssueStatus.PARTIALLY_RECEIVED, notificationCriteria.getVersity()));
            issues.addAll(issueRepository.findByStatusAndVersity(IssueStatus.PARTIALLY_APPROVED, notificationCriteria.getVersity()));
            notificationDto.setIssues(issues.size());

            if (notificationCriteria.getNewRequest()) {
                Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
                if (personId != null) {
                    notificationDto.setRequests(requestRepository.getRequestedByRequests(personId).size());

                    List<Request> requests = requestRepository.getRequestedByRequests(personId);

                    requests.forEach(request -> {
                        notificationDto.setIssues(notificationDto.getIssues() + issueRepository.findByRequest(request.getId()).size());
                    });
                }
            }
        } else if (notificationCriteria.getVersityPpc()) {
            List<Issue> issues = issueRepository.findByStatus(IssueStatus.VERSITY_PPC);
            issues.addAll(issueRepository.findByStatusAndVersity(IssueStatus.PARTIALLY_RECEIVED, notificationCriteria.getVersity()));
            issues.addAll(issueRepository.findByStatusAndVersity(IssueStatus.PARTIALLY_APPROVED, notificationCriteria.getVersity()));
            notificationDto.setIssues(issues.size());

            List<Request> requests = requestRepository.getRequestsByStatusAndVersityAndIssued(RequestStatus.REQUESTED, notificationCriteria.getVersity());
            requests.addAll(requestRepository.getRequestsByStatusAndVersityAndIssued(RequestStatus.APPROVED, notificationCriteria.getVersity()));

            notificationDto.setRequests(requests.size());

        } else if (notificationCriteria.getNewRequest()) {
            if (sessionWrapper.getSession() != null) {
                Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
                if (personId != null) {
                    notificationDto.setRequests(requestRepository.getRequestedByRequests(personId).size());

                    List<Request> requests = requestRepository.getRequestedByRequests(personId);

                    requests.forEach(request -> {
                        notificationDto.setIssues(notificationDto.getIssues() + issueRepository.findByRequest(request.getId()).size());
                    });
                }
            }

        }

        notificationDto.setReturns(itemInstanceRepository.getInstancesByStatus(ItemInstanceStatus.REJECTED).size());

        notificationDto.setFailures(itemInstanceRepository.getInstancesByStatus(ItemInstanceStatus.FAILURE).size());
        notificationDto.setFailures(notificationDto.getFailures() + lotInstanceRepository.getInstancesByStatus(ItemInstanceStatus.FAILURE).size());
        notificationDto.setFailureProcesses(itemInstanceRepository.getInstancesByStatus(ItemInstanceStatus.FAILURE_PROCESS).size());
        notificationDto.setFailureProcesses(notificationDto.getFailureProcesses() + lotInstanceRepository.getInstancesByStatus(ItemInstanceStatus.FAILURE_PROCESS).size());
        notificationDto.setDispatches(dispatchRepository.getDispatchesByStatus(DispatchStatus.NEW).size());
        notificationDto.setGatePasses(gatePassRepository.findByFinishFalse().size());
        notificationDto.setExpiryItems(itemInstanceRepository.getExpiredInstances(new Date(), ItemInstanceStatus.NEW, ItemInstanceStatus.STORE_SUBMITTED, ItemInstanceStatus.ACCEPT,
                ItemInstanceStatus.P_ACCEPT, ItemInstanceStatus.REVIEW, ItemInstanceStatus.REVIEWED, ItemInstanceStatus.VERIFIED, ItemInstanceStatus.INVENTORY).size());

        notificationDto.setReadMessages(drdoUpdateRepository.findByPersonAndReadFalseOrderByDateDesc(sessionWrapper.getSession().getLogin().getPerson().getId()).size());

        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date futureDate = cal.getTime();


        notificationDto.setToExpire(itemInstanceRepository.getToExpiryItems(today, futureDate, ItemInstanceStatus.NEW, ItemInstanceStatus.STORE_SUBMITTED, ItemInstanceStatus.ACCEPT,
                ItemInstanceStatus.P_ACCEPT, ItemInstanceStatus.REVIEW, ItemInstanceStatus.REVIEWED, ItemInstanceStatus.VERIFIED, ItemInstanceStatus.INVENTORY).size());

        return notificationDto;
    }
}
