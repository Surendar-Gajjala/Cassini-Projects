package com.cassinisys.drdo.service.transactions;

import com.cassinisys.drdo.filtering.RequestCriteria;
import com.cassinisys.drdo.filtering.RequestPredicateBuilder;
import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.model.dto.*;
import com.cassinisys.drdo.model.inventory.ItemAllocation;
import com.cassinisys.drdo.model.inventory.Storage;
import com.cassinisys.drdo.model.inventory.StorageItem;
import com.cassinisys.drdo.model.transactions.*;
import com.cassinisys.drdo.repo.bom.*;
import com.cassinisys.drdo.repo.inventory.ItemAllocationRepository;
import com.cassinisys.drdo.repo.inventory.StorageItemRepository;
import com.cassinisys.drdo.repo.inventory.StorageRepository;
import com.cassinisys.drdo.repo.transactions.*;
import com.cassinisys.drdo.service.DRDOUpdatesService;
import com.cassinisys.drdo.service.bom.ItemTypeService;
import com.cassinisys.platform.exceptions.CassiniException;
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

import java.util.*;

/**
 * Created by subra on 17-10-2018.
 */
@Service
public class RequestService implements CrudService<Request, Integer> {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestItemRepository requestItemRepository;

    @Autowired
    private RequestStatusHistoryRepository requestStatusHistoryRepository;

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private BomRepository bomRepository;

    @Autowired
    private BomItemRepository bomItemRepository;

    @Autowired
    private BomGroupRepository bomGroupRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private RequestPredicateBuilder requestPredicateBuilder;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueItemRepository issueItemRepository;

    @Autowired
    private IssueHistoryRepository issueHistoryRepository;

    @Autowired
    private ItemInstanceRepository itemInstanceRepository;

    @Autowired
    private BomInstanceItemRepository bomInstanceItemRepository;

    @Autowired
    private BomItemInstanceRepository bomItemInstanceRepository;

    @Autowired
    private ItemAllocationRepository itemAllocationRepository;

    @Autowired
    private StorageItemRepository storageItemRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private BomInstanceRepository bomInstanceRepository;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private LotInstanceRepository lotInstanceRepository;

    @Autowired
    private DRDOUpdatesService drdoUpdatesService;

    @Autowired
    private BDLReceiveRepository bdlReceiveRepository;

    @Autowired
    private BDLReceiveItemRepository bdlReceiveItemRepository;

    @Override
    @Transactional(readOnly = false)
    public Request create(Request request) {

        AutoNumber autoNumber = autoNumberRepository.findByName("Default Requisition Number Source");

        String number = autoNumber.next();

        autoNumber = autoNumberRepository.save(autoNumber);

        Bom bom = bomRepository.findOne(request.getBomInstance().getBom());
        String section = "";
        String subsystem = "";
        String unit = "";

        if (request.getSection() != null) {
            BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(request.getSection());
            if (bomInstanceItem != null) {
                if (request.getVersity()) {
                    section = bomInstanceItem.getTypeRef().getName() + " ( VSPL )_";
                } else {
                    section = bomInstanceItem.getTypeRef().getName() + "_";
                }
            }
        }

        if (request.getSubsystem() != null) {
            BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(request.getSubsystem());
            if (bomInstanceItem != null) {
                subsystem = bomInstanceItem.getTypeRef().getName() + "_";
            }
        }

        /*if (request.getUnit() != null) {
            BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(request.getUnit());
            if (bomInstanceItem != null) {
                unit = bomInstanceItem.getTypeRef().getName() + "_";
            }
        }*/

        if (request.getUnitIds().size() > 0) {
            if (request.getUnitIds().size() == 1) {
                BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(request.getUnitIds().get(0));
                if (bomInstanceItem != null) {
                    unit = bomInstanceItem.getTypeRef().getName() + "_";
                }
            } else {
                unit = "General_";
            }
        }

        request.setReqNumber(bom.getItem().getItemMaster().getItemCode() + "_" + request.getBomInstance().getItem().getInstanceName() + "_" + section + subsystem + unit + number);
        request.setRequestedBy(sessionWrapper.getSession().getLogin().getPerson());
        request.setRequestedDate(new Date());
        /*if (request.getVersity()) {
            request.setStatus(RequestStatus.VERSITY_MANAGER);
        } else {
            request.setStatus(RequestStatus.BDL_MANAGER);
        }*/
        request.setStatus(RequestStatus.REQUESTED);
        request = requestRepository.save(request);

        return request;
    }

    @Override
    @Transactional(readOnly = false)
    public Request update(Request request) {
        return requestRepository.save(request);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer requestId) {
        Request request = requestRepository.findOne(requestId);
        List<Issue> issues = issueRepository.findByRequest(requestId);

        if (issues.size() > 0) {
            throw new CassiniException("This request has issues. You cannot delete this request");
        } else {
            requestRepository.delete(requestId);

            String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " deleted the request " + request.getReqNumber();
            drdoUpdatesService.updateMessage(message, DRDOObjectType.REQUEST);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public Request get(Integer requestId) {
        Request request = requestRepository.findOne(requestId);

        List<RequestItem> pendingItems = requestItemRepository.getPendingItems(requestId);
        List<RequestItem> notApprovedItems = requestItemRepository.getAcceptedItems(requestId);

        if (pendingItems.size() > 0) {
            request.setShowAcceptAll(true);
        }

        if (notApprovedItems.size() > 0) {
            request.setShowApprove(true);
        }

        request.getStatusHistories().addAll(requestStatusHistoryRepository.findByRequestOrderByTimestampDesc(request.getId()));

        return request;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> getAll() {
        return requestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Request> getAllFinishedRequests() {
        List<Request> requests = new ArrayList<>();
        List<Request> requestList = requestRepository.findAllFinishedRequests(RequestStatus.REQUESTED);

        requestList.forEach(request -> {
            List<RequestItem> requestItems = requestItemRepository.getApprovedItems(request.getId());
            Boolean requestedItemIssued = true;
            for (RequestItem requestItem : requestItems) {
                List<IssueItem> issueItems = issueItemRepository.getApprovedItemsByRequest(requestItem.getId());
                if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots() && issueItems.size() == 0) {
                    requestedItemIssued = false;
                } else {
                    if (issueItems.size() != requestItem.getQuantity()) {
                        requestedItemIssued = false;
                    }
                }
            }

            if (!requestedItemIssued) {
                requests.add(request);
            }
        });

        return requests;
    }

    @Transactional(readOnly = true)
    public Page<Request> getAllRequests(Pageable pageable, RequestCriteria requestCriteria) {
        Predicate predicate = requestPredicateBuilder.build(requestCriteria, QRequest.request);
        Page<Request> requests = null;
        if (predicate != null) {
            requests = requestRepository.findAll(predicate, pageable);

            /*if (requests.getContent().size() > 0) {
                if (requestCriteria.getBdlApprove()) {
                    requests.getContent().forEach(request -> {

                        List<RequestItem> pendingItems = requestItemRepository.getPendingItems(request.getId());

                        if (pendingItems.size() > 0) {
                            request.setNewRequest(true);
                        }
                    });
                } else if (requestCriteria.getCasApprove()) {
                    requests.getContent().forEach(request -> {
                        List<RequestItem> acceptedItems = requestItemRepository.getAcceptedItems(request.getId());

                        if (acceptedItems.size() > 0) {
                            request.setNewRequest(true);
                        }
                    });
                } else if (requestCriteria.getStoreApprove()) {
                    requests.getContent().forEach(request -> {
                        List<RequestItem> approvedItems = requestItemRepository.getApprovedItems(request.getId());

                        if (approvedItems.size() > 0) {
                            request.setNewRequest(true);
                        }
                    });
                }
            }*/
        }

        /*List<Request> requestList = requestRepository.findAll();

        requestList.forEach(request -> {
            if (request.getStatus().equals(RequestStatus.REJECTED)) {
                List<RequestItem> requestItems = requestItemRepository.findByRequest(request);
                for (RequestItem requestItem : requestItems) {
                    requestItem.setStatus(RequestItemStatus.REJECTED);

                    requestItem = requestItemRepository.save(requestItem);
                }
            } else {
                List<RequestItem> requestItems = requestItemRepository.findByRequest(request);

                requestItems.forEach(requestItem -> {
                    if (requestItem.getAccepted() && requestItem.getApproved()) {
                        requestItem.setStatus(RequestItemStatus.APPROVED);
                    } else if (requestItem.getAccepted() && !requestItem.getStatus().equals(RequestItemStatus.REJECTED)) {
                        requestItem.setStatus(RequestItemStatus.ACCEPTED);
                    }

                    requestItem = requestItemRepository.save(requestItem);
                });
            }
        });

        ItemType itemType = itemTypeRepository.findByName("System");
        List<ItemType> itemTypes = itemTypeService.getHierarchicalChildren(itemType);
        List<Bom> boms = bomRepository.findSystemTypeBoms(itemTypes);

        BomGroup bomGroup = bomGroupRepository.findByTypeAndName(BomItemType.COMMONPART, "Common Parts");
        if (bomGroup != null) {
            for (Bom bom : boms) {
                BomItem bomItem = bomItemRepository.findByBomAndTypeRef(bom.getId(), bomGroup);
                if (bomItem == null) {
                    bomItem = new BomItem();
                    bomItem.setBom(bom.getId());
                    bomItem.setTypeRef(bomGroup);
                    bomItem.setBomItemType(BomItemType.COMMONPART);
                    bomItem.setHierarchicalCode(bom.getItem().getItemMaster().getItemCode());

                    bomItem = bomItemRepository.save(bomItem);
                }
            }
        }

        List<Issue> issues = issueRepository.findAll();
        issues.forEach(issue -> {
            List<IssueItem> issueItems = issueItemRepository.findByIssue(issue);

            issueItems.forEach(issueItem -> {
                if (issueItem.getStatus() == null) {
                    issueItem.setStatus(IssueItemStatus.PENDING);

                    issueItem = issueItemRepository.save(issueItem);
                }
            });

            if (issue.getStatus().equals(IssueStatus.STORE)) {
                issue.setStatus(IssueStatus.BDL_PPC);

                issue = issueRepository.save(issue);
            }
        });*/

        return requests;
    }

    @Transactional(readOnly = false)
    public Request acceptRequest(Request request) {

        RequestStatus presentStatus = request.getStatus();
        String message = null;
        Boolean acceptedOneItem = false;
        if (request.getStatus().equals(RequestStatus.BDL_MANAGER) || request.getStatus().equals(RequestStatus.VERSITY_MANAGER)
                || request.getStatus().equals(RequestStatus.PARTIALLY_ACCEPTED) || request.getStatus().equals(RequestStatus.PARTIALLY_APPROVED)) {

            List<RequestItem> requestItems = requestItemRepository.findByRequest(request);
            Integer bomInstanceId = request.getBomInstance().getId();
            for (RequestItem requestItem : requestItems) {
                if (requestItem.getStatus().equals(RequestItemStatus.PENDING)) {
                    ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(bomInstanceId, requestItem.getItem().getId());

                    /*if (itemAllocation == null) {
                        throw new CassiniException("No Allocation Quantity available for " + requestItem.getItem().getItem().getItemMaster().getItemName());
                    } else {
                        if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                            if ((itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty()) < requestItem.getFractionalQuantity()) {
                                throw new CassiniException("Allocation Quantity is Not Enough for " + requestItem.getItem().getItem().getItemMaster().getItemName());
                            }
                        } else {
                            if ((itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty()) < requestItem.getQuantity()) {
                                throw new CassiniException("Allocation Quantity is Not Enough for " + requestItem.getItem().getItem().getItemMaster().getItemName());
                            }
                        }
                    }*/

                    if (itemAllocation != null) {
                        if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                            if ((itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty()) >= requestItem.getFractionalQuantity()) {
                                requestItem.setAccepted(true);
                                requestItem.setStatus(RequestItemStatus.ACCEPTED);
                                requestItem = requestItemRepository.save(requestItem);

                                acceptedOneItem = true;
                            }
                        } else {
                            if ((itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty()) >= requestItem.getQuantity().doubleValue()) {
                                requestItem.setAccepted(true);
                                requestItem.setStatus(RequestItemStatus.ACCEPTED);
                                requestItem = requestItemRepository.save(requestItem);

                                acceptedOneItem = true;
                            }
                        }
                    }
                }
            }

            List<RequestItem> acceptedItems = requestItemRepository.getAcceptedItems(request.getId());

            if (acceptedItems.size() == requestItems.size()) {
                acceptedOneItem = true;
                message = "Items accepted successfully";
                request.setStatus(RequestStatus.CAS_MANAGER);
            } else {
                if (acceptedOneItem) {
                    message = "Items partially accepted successfully";
                    request.setStatus(RequestStatus.PARTIALLY_ACCEPTED);
                } else {
                    throw new CassiniException("There is no allocation to accept items");
                }
            }

            request = requestRepository.save(request);

            if (acceptedOneItem) {
                RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
                requestStatusHistory.setRequest(request.getId());
                requestStatusHistory.setOldStatus(presentStatus);
                requestStatusHistory.setNewStatus(request.getStatus());
                requestStatusHistory.setTimestamp(new Date());
                requestStatusHistory.setResult(true);
                requestStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

                requestStatusHistory = requestStatusHistoryRepository.save(requestStatusHistory);
            }
        }

        request.setMessage(message);
        return request;
    }

    @Transactional(readOnly = false)
    public Request approveRequest(Request request) {

        RequestStatus presentStatus = request.getStatus();
        String message = "";
        Boolean approvedOneItem = false;
        if (request.getStatus().equals(RequestStatus.CAS_MANAGER) || request.getStatus().equals(RequestStatus.PARTIALLY_ACCEPTED) || request.getStatus().equals(RequestStatus.PARTIALLY_APPROVED)) {

            List<RequestItem> requestItems = requestItemRepository.findByRequest(request);
            Integer bomInstanceId = request.getBomInstance().getId();
            for (RequestItem requestItem : requestItems) {
                if (requestItem.getStatus().equals(RequestItemStatus.ACCEPTED)) {
                    ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(bomInstanceId, requestItem.getItem().getId());

                    /*if (itemAllocation == null) {
                        throw new CassiniException("No Allocation Quantity available for " + requestItem.getItem().getItem().getItemMaster().getItemName());
                    } else {
                        if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                            if ((itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty()) < requestItem.getFractionalQuantity()) {
                                throw new CassiniException("Allocation Quantity is Not Enough for " + requestItem.getItem().getItem().getItemMaster().getItemName());
                            }
                        } else {
                            if ((itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty()) < requestItem.getQuantity()) {
                                throw new CassiniException("Allocation Quantity is Not Enough for " + requestItem.getItem().getItem().getItemMaster().getItemName());
                            }
                        }
                    }*/

                    if (itemAllocation != null) {
                        if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                            if ((itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty()) >= requestItem.getFractionalQuantity()) {
                                requestItem.setAccepted(true);
                                requestItem.setApproved(true);
                                requestItem.setStatus(RequestItemStatus.APPROVED);
                                requestItem = requestItemRepository.save(requestItem);

                                approvedOneItem = true;
                            }
                        } else {
                            if ((itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty()) >= requestItem.getQuantity().doubleValue()) {
                                requestItem.setAccepted(true);
                                requestItem.setApproved(true);
                                requestItem.setStatus(RequestItemStatus.APPROVED);
                                requestItem = requestItemRepository.save(requestItem);

                                approvedOneItem = true;
                            }
                        }
                    }
                }
            }

            List<RequestItem> approvedItems = requestItemRepository.getApprovedItems(request.getId());

            if (approvedItems.size() == requestItems.size()) {
                approvedOneItem = true;
                message = "Items approved successfully";
                request.setStatus(RequestStatus.APPROVED);
            } else {
                if (approvedOneItem) {
                    message = "Items partially approved successfully";
                    request.setStatus(RequestStatus.PARTIALLY_APPROVED);
                } else {
                    message = "There is no allocation to approved Items";
                }
            }
            request = requestRepository.save(request);
            if (approvedOneItem) {
                RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
                requestStatusHistory.setRequest(request.getId());
                requestStatusHistory.setOldStatus(presentStatus);
                requestStatusHistory.setNewStatus(request.getStatus());
                requestStatusHistory.setTimestamp(new Date());
                requestStatusHistory.setResult(true);
                requestStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

                requestStatusHistory = requestStatusHistoryRepository.save(requestStatusHistory);
            }
        }
        request.setMessage(message);
        return request;
    }

    @Transactional(readOnly = false)
    public RequestItem acceptRequestItem(RequestItem requestItem) {

        Request request = requestRepository.findOne(requestItem.getRequest().getId());
        RequestStatus presentStatus = request.getStatus();
        Integer bomInstanceId = request.getBomInstance().getId();
        ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(bomInstanceId, requestItem.getItem().getId());

        if (itemAllocation == null) {
            throw new CassiniException("No Allocation Quantity available for " + requestItem.getItem().getItem().getItemMaster().getItemName());
        } else {
            if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                if ((itemAllocation.getAllocateQty() - (itemAllocation.getIssuedQty() + itemAllocation.getFailedQty())) < 1) {
                    throw new CassiniException("No Allocation Quantity available for " + requestItem.getItem().getItem().getItemMaster().getItemName());
                }
            } else {
                if ((itemAllocation.getAllocateQty() - (itemAllocation.getIssuedQty() + itemAllocation.getFailedQty())) < 1) {
                    throw new CassiniException("No Allocation Quantity available for " + requestItem.getItem().getItem().getItemMaster().getItemName());
                }
            }
        }

        requestItem.setAccepted(true);
        requestItem.setStatus(RequestItemStatus.ACCEPTED);
        requestItem = requestItemRepository.save(requestItem);

        List<RequestItem> pendingItems = requestItemRepository.getPendingItems(request.getId());
        if (pendingItems.size() == 0) {
            request.setStatus(RequestStatus.CAS_MANAGER);
        } else {
            request.setStatus(RequestStatus.PARTIALLY_ACCEPTED);
        }
        request = requestRepository.save(request);
        RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
        requestStatusHistory.setRequest(request.getId());
        requestStatusHistory.setOldStatus(presentStatus);
        requestStatusHistory.setNewStatus(request.getStatus());
        requestStatusHistory.setTimestamp(new Date());
        requestStatusHistory.setResult(true);
        requestStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        requestStatusHistory = requestStatusHistoryRepository.save(requestStatusHistory);

        BomInstanceItem section = getSectionName(requestItem.getItem());
        Bom bom = bomRepository.findOne(request.getBomInstance().getBom());

        String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " has Accepted the '" + requestItem.getItem().getItem().getItemMaster().getItemName()
                + "' Requested item in '" + section.getTypeRef().getName() + "' of " + bom.getItem().getItemMaster().getItemName() + "_" + request.getBomInstance().getItem().getInstanceName();
        drdoUpdatesService.updateMessage(message, DRDOObjectType.REQUEST);

        return requestItem;
    }

    @Transactional(readOnly = false)
    public RequestItem approveRequestItem(RequestItem requestItem) {

        Request request = requestRepository.findOne(requestItem.getRequest().getId());
        RequestStatus presentStatus = request.getStatus();
        Integer bomInstanceId = request.getBomInstance().getId();
        ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(bomInstanceId, requestItem.getItem().getId());

        if (itemAllocation == null) {
            throw new CassiniException("No Allocation Quantity available for " + requestItem.getItem().getItem().getItemMaster().getItemName());
        } else {
            if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                if ((itemAllocation.getAllocateQty() - (itemAllocation.getIssuedQty() + itemAllocation.getFailedQty())) < 1) {
                    throw new CassiniException("No Allocation Quantity available for " + requestItem.getItem().getItem().getItemMaster().getItemName());
                }
            } else {
                if ((itemAllocation.getAllocateQty() - (itemAllocation.getIssuedQty() + itemAllocation.getFailedQty())) < 1) {
                    throw new CassiniException("No Allocation Quantity available for " + requestItem.getItem().getItem().getItemMaster().getItemName());
                }
            }
        }

        requestItem.setAccepted(true);
        requestItem.setApproved(true);
        requestItem.setStatus(RequestItemStatus.APPROVED);
        requestItem = requestItemRepository.save(requestItem);

        List<RequestItem> acceptedItems = requestItemRepository.getAcceptedItems(request.getId());
        List<RequestItem> pendingItems = requestItemRepository.getPendingItems(request.getId());

        if (acceptedItems.size() == 0 && pendingItems.size() == 0) {
            request.setStatus(RequestStatus.APPROVED);
        } else {
            request.setStatus(RequestStatus.PARTIALLY_APPROVED);
        }

        request = requestRepository.save(request);
        RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
        requestStatusHistory.setRequest(request.getId());
        requestStatusHistory.setOldStatus(presentStatus);
        requestStatusHistory.setNewStatus(request.getStatus());
        requestStatusHistory.setTimestamp(new Date());
        requestStatusHistory.setResult(true);
        requestStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        requestStatusHistory = requestStatusHistoryRepository.save(requestStatusHistory);


        BomInstanceItem section = getSectionName(requestItem.getItem());
        Bom bom = bomRepository.findOne(request.getBomInstance().getBom());

        String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " has Approved the '" + requestItem.getItem().getItem().getItemMaster().getItemName()
                + "' Requested Item in '" + section.getTypeRef().getName() + "' of " + bom.getItem().getItemMaster().getItemName() + "_" + request.getBomInstance().getItem().getInstanceName();
        drdoUpdatesService.updateMessage(message, DRDOObjectType.REQUEST);


        return requestItem;
    }

    private BomInstanceItem getSectionName(BomInstanceItem bomInstanceItem) {
        BomInstanceItem section = null;
        BomInstanceItem parent = bomInstanceItemRepository.findOne(bomInstanceItem.getParent());

        if (parent.getBomItemType().equals(BomItemType.UNIT)) {
            BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
            if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                if (parent2 != null) {
                    section = parent2;
                }

            } else if (parent1.getBomItemType().equals(BomItemType.SECTION) || parent1.getBomItemType().equals(BomItemType.COMMONPART)) {
                section = parent1;
            }
        } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
            BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
            if (parent1 != null) {
                section = parent1;
            }

        } else if (parent.getBomItemType().equals(BomItemType.SECTION) || parent.getBomItemType().equals(BomItemType.COMMONPART)) {
            section = parent;
        }

        return section;

    }

    @Transactional(readOnly = false)
    public Request rejectRequest(Request request) {

        RequestStatus presentStatus = request.getStatus();
        RequestStatus rejectedStatus = null;

        if (request.getStatus().equals(RequestStatus.BDL_MANAGER)) {
            rejectedStatus = RequestStatus.BDL_MANAGER;
        } else if (request.getStatus().equals(RequestStatus.CAS_MANAGER)) {
            rejectedStatus = RequestStatus.CAS_MANAGER;
        }

        List<RequestItem> requestItems = requestItemRepository.findByRequest(request);

        Boolean rejectItemsExist = false;
        for (RequestItem requestItem : requestItems) {
            if (!requestItem.getStatus().equals(RequestItemStatus.APPROVED)) {
                requestItem.setStatus(RequestItemStatus.REJECTED);
                requestItem.setReason(request.getReason());
                requestItem = requestItemRepository.save(requestItem);

                rejectItemsExist = true;
            }
        }

        List<RequestItem> rejectedItems = requestItemRepository.getRejectedItems(request.getId());
        List<RequestItem> approvedItems = requestItemRepository.getApprovedItems(request.getId());
        List<RequestItem> acceptedItems = requestItemRepository.getAcceptedItems(request.getId());

        if (rejectedItems.size() > 0 && approvedItems.size() == 0 && acceptedItems.size() == 0) {
            request.setStatus(RequestStatus.REJECTED);
            request = requestRepository.save(request);

            RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
            requestStatusHistory.setRequest(request.getId());
            requestStatusHistory.setOldStatus(presentStatus);
            requestStatusHistory.setNewStatus(RequestStatus.REJECTED);
            requestStatusHistory.setTimestamp(new Date());
            requestStatusHistory.setResult(false);
            requestStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            requestStatusHistory = requestStatusHistoryRepository.save(requestStatusHistory);
        }

        if (rejectItemsExist) {
            String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " has Rejected items from '" + request.getReqNumber()
                    + "' Request for " + request.getReason();
            drdoUpdatesService.updateMessage(message, DRDOObjectType.REQUEST);
        }

        List<RequestItem> pendingItems = requestItemRepository.getPendingItems(request.getId());

        if (pendingItems.size() == 0 && acceptedItems.size() > 0) {
            request.setStatus(RequestStatus.CAS_MANAGER);

            request = requestRepository.save(request);
            List<RequestStatusHistory> statusHistories = requestStatusHistoryRepository.findByRequestOrderByTimestampDesc(request.getId());

            RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
            requestStatusHistory.setRequest(request.getId());
            requestStatusHistory.setOldStatus(statusHistories.get(0).getNewStatus());
            requestStatusHistory.setNewStatus(presentStatus);
            requestStatusHistory.setTimestamp(new Date());
            requestStatusHistory.setResult(true);
            requestStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            requestStatusHistory = requestStatusHistoryRepository.save(requestStatusHistory);
        }

        if (pendingItems.size() == 0 && acceptedItems.size() == 0 && approvedItems.size() > 0) {
            request.setStatus(RequestStatus.APPROVED);

            request = requestRepository.save(request);
            List<RequestStatusHistory> statusHistories = requestStatusHistoryRepository.findByRequestOrderByTimestampDesc(request.getId());

            RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
            requestStatusHistory.setRequest(request.getId());
            requestStatusHistory.setOldStatus(statusHistories.get(0).getNewStatus());
            requestStatusHistory.setNewStatus(presentStatus);
            requestStatusHistory.setTimestamp(new Date());
            requestStatusHistory.setResult(true);
            requestStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            requestStatusHistory = requestStatusHistoryRepository.save(requestStatusHistory);
        }

        return request;
    }

    @Transactional(readOnly = false)
    public List<RequestItem> createRequestItems(Integer requestId, List<RequestItem> requestItems) {

        Request request = requestRepository.findOne(requestId);

        Bom bom = bomRepository.findOne(request.getBomInstance().getBom());

        RequestItem item = requestItems.get(0);

        BomInstanceItem bomInstanceItem = item.getItem();
        String sectionCode = "";
        BomInstanceItem section = null;
        if (bomInstanceItem.getParent() != null) {
            BomInstanceItem parent = bomInstanceItemRepository.findOne(bomInstanceItem.getParent());

            if (parent != null && parent.getBomItemType().equals(BomItemType.UNIT)) {
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());

                if (parent1 != null && parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());

                    if (parent2 != null) {
                        sectionCode = parent2.getTypeRef().getCode();
                        section = parent2;
                    }
                } else if (parent1 != null && (parent1.getBomItemType().equals(BomItemType.SECTION) || parent1.getBomItemType().equals(BomItemType.COMMONPART))) {
                    sectionCode = parent1.getTypeRef().getCode();
                    section = parent1;
                }
            } else if (parent != null && parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());

                if (parent1 != null) {
                    sectionCode = parent1.getTypeRef().getCode();
                    section = parent1;
                }
            } else if (parent != null && parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                sectionCode = parent.getTypeRef().getCode();
                section = parent;
            }
        }

        /*AutoNumber autoNumber = autoNumberRepository.findByName("Default Requisition Number Source");

        String number = autoNumber.current();

        String reqNumber = bom.getItem().getItemMaster().getItemCode() + "_" + request.getBomInstance().getItem().getInstanceName() + "_" + sectionCode + "_" + number;

        request.setReqNumber(reqNumber);

        request = requestRepository.save(request);*/

        for (RequestItem requestItem : requestItems) {
            if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                if (requestItem.getFractionalQuantity() > 0.0) {
                    requestItem.setRequest(request);
                    requestItem.setAccepted(true);
                    requestItem.setApproved(true);
                    requestItem.setStatus(RequestItemStatus.APPROVED);
                    requestItem = requestItemRepository.save(requestItem);
                }
            } else {
                if (requestItem.getQuantity() > 0) {
                    requestItem.setRequest(request);
                    requestItem.setAccepted(true);
                    requestItem.setApproved(true);
                    requestItem.setStatus(RequestItemStatus.APPROVED);
                    requestItem = requestItemRepository.save(requestItem);
                }
            }

        }

        if (section != null) {
            String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " has created new '" + request.getReqNumber()
                    + "' Request for '" + section.getTypeRef().getName() + "' Items in " + bom.getItem().getItemMaster().getItemName() + "_" + request.getBomInstance().getItem().getInstanceName();
            drdoUpdatesService.updateMessage(message, DRDOObjectType.REQUEST);
        }

        /*RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
        requestStatusHistory.setRequest(request.getId());
        if (request.getVersity()) {
            requestStatusHistory.setOldStatus(RequestStatus.VERSITY_EMPLOYEE);
            requestStatusHistory.setNewStatus(RequestStatus.VERSITY_EMPLOYEE);
        } else {
            requestStatusHistory.setOldStatus(RequestStatus.BDL_EMPLOYEE);
            requestStatusHistory.setNewStatus(RequestStatus.BDL_EMPLOYEE);
        }

        requestStatusHistory.setTimestamp(new Date());
        requestStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        requestStatusHistory = requestStatusHistoryRepository.save(requestStatusHistory);*/

        return requestItems;
    }

    @Transactional(readOnly = true)
    public List<RequestedItemDto> getRequestItems(Integer requestId) {

        HashMap<Integer, RequestedItemDto> sectionGroupMap = new HashMap<>();

        Request request = requestRepository.findOne(requestId);

        List<RequestItem> requestItems = requestItemRepository.findByRequest(request);
        Integer bomInstanceId = request.getBomInstance().getId();

        requestItems.forEach(requestItem -> {

            ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(bomInstanceId, requestItem.getItem().getId());
            if (itemAllocation != null) {
                requestItem.setAllocatedQuantity(itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty());
            }

            ItemType itemType = itemTypeRepository.findOne(requestItem.getItem().getItem().getItemMaster().getItemType().getParentType());
            if (!itemType.getParentNode()) {
                requestItem.getItem().getItem().getItemMaster().setParentType(itemType);
            } else {
                requestItem.getItem().getItem().getItemMaster().setParentType(requestItem.getItem().getItem().getItemMaster().getItemType());
            }

            BomInstanceItem parent = bomInstanceItemRepository.findOne(requestItem.getItem().getParent());
            BomGroup section = null;
            if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent.getParent());
                if (parent2.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomInstanceItem parent3 = bomInstanceItemRepository.findOne(parent2.getParent());
                    if (parent3 != null) {
                        section = parent3.getTypeRef();
                    }
                } else if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                    section = parent2.getTypeRef();
                }
            } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent.getParent());
                if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                    section = parent2.getTypeRef();
                }
            } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                section = parent.getTypeRef();
            }

            if (section != null) {
                RequestedItemDto requestedItemDto = sectionGroupMap.containsKey(section.getId()) ? sectionGroupMap.get(section.getId()) : new RequestedItemDto();

                requestedItemDto.setSection(section);
                requestedItemDto.getRequestItems().add(requestItem);

                sectionGroupMap.put(section.getId(), requestedItemDto);
            }


        });

        List<RequestedItemDto> requestedItemDtos = new ArrayList<>();
        for (RequestedItemDto requestedItemDto : sectionGroupMap.values()) {
            requestedItemDtos.add(requestedItemDto);
        }

        if (requestedItemDtos.size() > 0) {
            Collections.sort(requestedItemDtos, new Comparator<RequestedItemDto>() {
                public int compare(final RequestedItemDto object1, final RequestedItemDto object2) {
                    return object1.getSection().getName().compareTo(object2.getSection().getName());
                }
            });
        }

        return requestedItemDtos;
    }

    @Transactional(readOnly = false)
    public void rejectRequestItem(Integer requestId, RequestItem requestItem) {
        Request request = requestRepository.findOne(requestId);
        RequestStatus presentStatus = request.getStatus();
        requestItem.setStatus(RequestItemStatus.REJECTED);

        requestItem = requestItemRepository.save(requestItem);

        String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " rejected the " + requestItem.getItem().getItem().getItemMaster().getItemName()
                + " from " + requestItem.getRequest().getReqNumber() + " request for ' " + requestItem.getReason() + " '";
        drdoUpdatesService.updateMessage(message, DRDOObjectType.REQUEST);

        List<RequestItem> approvedItems = requestItemRepository.getApprovedItems(requestId);
        List<RequestItem> acceptedItems = requestItemRepository.getAcceptedItems(requestId);

        List<RequestItem> pendingItems = requestItemRepository.getPendingItems(request.getId());
        List<RequestItem> rejectedItems = requestItemRepository.getRejectedItems(request.getId());

        if (pendingItems.size() == 0 && acceptedItems.size() > 0) {
            request.setStatus(RequestStatus.CAS_MANAGER);

            request = requestRepository.save(request);
            List<RequestStatusHistory> statusHistories = requestStatusHistoryRepository.findByRequestOrderByTimestampDesc(request.getId());

            RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
            requestStatusHistory.setRequest(request.getId());
            requestStatusHistory.setOldStatus(statusHistories.get(0).getNewStatus());
            requestStatusHistory.setNewStatus(presentStatus);
            requestStatusHistory.setTimestamp(new Date());
            requestStatusHistory.setResult(true);
            requestStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            requestStatusHistory = requestStatusHistoryRepository.save(requestStatusHistory);
        }

        if (pendingItems.size() == 0 && acceptedItems.size() == 0 && approvedItems.size() > 0) {
            request.setStatus(RequestStatus.APPROVED);

            request = requestRepository.save(request);
            List<RequestStatusHistory> statusHistories = requestStatusHistoryRepository.findByRequestOrderByTimestampDesc(request.getId());

            RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
            requestStatusHistory.setRequest(request.getId());
            requestStatusHistory.setOldStatus(statusHistories.get(0).getNewStatus());
            requestStatusHistory.setNewStatus(presentStatus);
            requestStatusHistory.setTimestamp(new Date());
            requestStatusHistory.setResult(true);
            requestStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            requestStatusHistory = requestStatusHistoryRepository.save(requestStatusHistory);
        }
    }


    @Transactional(readOnly = true)
    public List<Request> getRequestsByInstance(Integer instanceId) {
        return requestRepository.getRequestsByInstance(instanceId);
    }

    @Transactional(readOnly = true)
    public RequestReportDto getRequestReport(Integer requestId) {

        RequestReportDto requestReportDto = new RequestReportDto();

        Request request = requestRepository.findOne(requestId);

        List<RequestItem> requestItems = requestItemRepository.findByRequest(request);
        requestReportDto.getRequestItems().addAll(requestItems);

        requestReportDto.getRequestItems().forEach(requestItem -> {
            ItemType itemType = itemTypeRepository.findOne(requestItem.getItem().getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                requestItem.getItem().getItem().getItemMaster().setParentType(itemType);
            } else {
                requestItem.getItem().getItem().getItemMaster().setParentType(requestItem.getItem().getItem().getItemMaster().getItemType());
            }
        });

        List<Issue> issues = issueRepository.findByRequest(requestId);

        List<IssueDetailsDto> issueDetailsDtos = new ArrayList<>();

        issues.forEach(issue -> {
            List<IssueItem> issueItems = issueItemRepository.findByIssue(issue);

            issueItems.forEach(issueItem -> {
                IssueDetailsDto issueDetailsDto = new IssueDetailsDto();


                issueDetailsDto.setItemInstance(itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance()));
                issueDetailsDto.setRequestItem(requestItemRepository.findOne(issueItem.getRequestItem()));

                ItemType itemType = itemTypeRepository.findOne(issueDetailsDto.getRequestItem().getItem().getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    issueDetailsDto.getRequestItem().getItem().getItem().getItemMaster().setParentType(itemType);
                } else {
                    issueDetailsDto.getRequestItem().getItem().getItem().getItemMaster().setParentType(issueDetailsDto.getRequestItem().getItem().getItem().getItemMaster().getItemType());
                }

                issueDetailsDto.setIssueItem(issueItem);

                issueDetailsDtos.add(issueDetailsDto);
            });

        });

        requestReportDto.setIssuedItems(issueDetailsDtos);
        requestReportDto.getStatusHistories().addAll(requestStatusHistoryRepository.findByRequestOrderByTimestampDesc(request.getId()));
        return requestReportDto;
    }

    @Transactional(readOnly = true)
    public List<NewIssueDto> getRequestedItems(Integer requestId) {

        List<NewIssueDto> newIssueDtos = new ArrayList<>();

        Request request = requestRepository.findOne(requestId);
//        List<RequestItem> requestItems = requestItemRepository.findByRequest(request);
        List<RequestItem> requestItems = requestItemRepository.getApprovedItems(request.getId());

        for (RequestItem requestItem : requestItems) {
            NewIssueDto newIssueDto = new NewIssueDto();

            newIssueDto.setRequestItem(requestItem);

            ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(request.getBomInstance().getId(), requestItem.getItem().getId());

            if (itemAllocation != null) {
                newIssueDto.setAllocateQty(itemAllocation.getAllocateQty());
            }

            List<IssueItemDto> issueItemDtos = new ArrayList<>();

            if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {

                List<IssueItem> issueItems = issueItemRepository.getApprovedItemsByRequest(requestItem.getId());

                if (issueItems.size() > 0) {
                    for (IssueItem issueItem : issueItems) {
                        IssueItemDto issuedItemDto = new IssueItemDto();
                        issuedItemDto.setRequestItem(requestItem);

                        BomItemInstance bomItemInstance = bomItemInstanceRepository.findOne(issueItem.getBomItemInstance().getId());

                        if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                            issuedItemDto.setStatus(IssueItemStatus.RECEIVED);
                        } else {
                            if (issueItem.getStatus().equals(IssueItemStatus.APPROVED) || issueItem.getStatus().equals(IssueItemStatus.P_APPROVED)) {
                                issuedItemDto.setStatus(issueItem.getStatus());
                            } else {
                                issuedItemDto.setStatus(issueItem.getStatus());
                            }
                            String storageLocation = "";
                            Storage storage = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance()).getStorage();

                            if (storage != null && storage.getParent() != null) {
                                Storage parentStorage = storageRepository.findOne(storage.getParent());

                                if (parentStorage != null) {
                                    storageLocation = parentStorage.getName() + "/" + storage.getName();
                                    if (parentStorage.getParent() != null) {
                                        Storage parentStorage1 = storageRepository.findOne(parentStorage.getParent());
                                        storageLocation = parentStorage1.getName() + " /" + storageLocation;

                                        if (parentStorage1.getParent() != null) {
                                            Storage parentStorage2 = storageRepository.findOne(parentStorage1.getParent());
                                            storageLocation = parentStorage2.getName() + " /" + storageLocation;

                                            if (parentStorage2.getParent() != null) {

                                                Storage parentStorage3 = storageRepository.findOne(parentStorage2.getParent());
                                                storageLocation = parentStorage3.getName() + " /" + storageLocation;

                                                if (parentStorage3.getParent() != null) {

                                                    Storage parentStorage4 = storageRepository.findOne(parentStorage3.getParent());
                                                    storageLocation = parentStorage4.getName() + " /" + storageLocation;
                                                }
                                            }
                                        }

                                    }
                                }
                            } else {
                                if (storage != null) {
                                    storageLocation = storage.getName();
                                }
                            }
                            issuedItemDto.getStorageLocations().add(storageLocation);
                        }

                        issuedItemDto.setRequestQuantity(1);
                        issuedItemDto.setIssuedItemInstance(itemInstanceRepository.findOne(bomItemInstance.getItemInstance()));
                        issuedItemDto.setLotInstance(lotInstanceRepository.findByInstanceAndIssueItem(bomItemInstance.getItemInstance(), issueItem.getId()));

                        issueItemDtos.add(issuedItemDto);
                    }
                } else {
                    IssueItemDto issueItemDto = new IssueItemDto();
                    issueItemDto.setRequestItem(requestItem);
                    issueItemDto.setRequestQuantity(1);
                    issueItemDto.setIssued(false);
                    issueItemDto.setAllocateQty(newIssueDto.getAllocateQty());


                    BomInstanceItem bomItemSection = null;
                    BomInstanceItem parent = bomInstanceItemRepository.findOne(requestItem.getItem().getParent());
                    if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                        BomInstanceItem bomItemUnit = parent;
                        BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                        if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                            if (parent2 != null) {
                                bomItemSection = parent2;
                            }
                        } else {
                            bomItemSection = parent1;
                        }
                    } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                        bomItemSection = parent1;
                    } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                        bomItemSection = parent;
                    }

                    Bom bom = bomRepository.findOne(request.getBomInstance().getBom());

                    List<ItemInstance> inventoryItemInstances = new ArrayList<>();
                    if (bomItemSection != null) {
                        inventoryItemInstances = itemInstanceRepository.getItemInstancesBySectionAndBomAndUniqueCode(requestItem.getItem().getItem().getId(), bomItemSection.getTypeRef().getId(), bom.getId(), requestItem.getItem().getUniqueCode());
                    }

                    List<ItemInstance> inventoryItemInstancesWithoutSection = itemInstanceRepository.getItemInstancesByBomAndUniqueCodeAndSectionIsNull(requestItem.getItem().getItem().getId(), bom.getId(), requestItem.getItem().getUniqueCode());

                    issueItemDto.getInventoryInstances().addAll(inventoryItemInstances);
                    issueItemDto.getInventoryInstances().addAll(inventoryItemInstancesWithoutSection);

                    /*--------------------  To pick default itemInstance to issue --------------------*/


                    /*HashMap<Integer, ItemInstance> issuedItemInstanceMap = new HashMap<>();

                    if (inventoryItemInstances.size() > 0) {
                        for (ItemInstance inventoryItemInstance : inventoryItemInstances) {
                            ItemInstance existInstance = issuedItemInstanceMap.get(inventoryItemInstance.getId());

                            if (existInstance == null && requestItem.getFractionalQuantity() <= issueItemDto.getAllocateQty()) {
                                inventoryItemInstance = getIssueUpn(request, requestItem.getItem(), inventoryItemInstance);

                                issueItemDto.setSelectInstanceToIssue(inventoryItemInstance);
                                issuedItemInstanceMap.put(inventoryItemInstance.getId(), inventoryItemInstance);
                                break;
                            }
                        }
                    } else {
                        for (ItemInstance inventoryItemInstance : inventoryItemInstancesWithoutSection) {
                            ItemInstance existInstance = issuedItemInstanceMap.get(inventoryItemInstance.getId());

                            if (existInstance == null && requestItem.getFractionalQuantity() <= issueItemDto.getAllocateQty()) {
                                inventoryItemInstance = getIssueUpn(request, requestItem.getItem(), inventoryItemInstance);

                                issueItemDto.setSelectInstanceToIssue(inventoryItemInstance);
                                issuedItemInstanceMap.put(inventoryItemInstance.getId(), inventoryItemInstance);
                                break;
                            }
                        }
                    }*/

                    BomInstance bomInstance = request.getBomInstance();

                    bom = bomRepository.findOne(bomInstance.getBom());

                    List<StorageItem> storageItems = new ArrayList<>();

                    bomItemSection = null;
                    parent = bomInstanceItemRepository.findOne(requestItem.getItem().getParent());
                    if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                        BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                        if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                            if (parent2 != null) {
                                bomItemSection = parent2;
                            }
                        } else {
                            bomItemSection = parent1;
                        }
                    } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                        bomItemSection = parent1;
                    } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                        bomItemSection = parent;
                    }

                    if (bomItemSection != null) {
                        storageItems.addAll(storageItemRepository.getItemStorageByBomAndUniqueCodeAndSection(bom.getId(), requestItem.getItem().getUniqueCode(), bomItemSection.getTypeRef().getId()));
                    }
                    storageItems.addAll(storageItemRepository.getItemStorageByBomAndUniqueCodeAndSectionIsNull(bom.getId(), requestItem.getItem().getUniqueCode()));

                    Map<String, String> locationMap = new HashMap<>();

                    for (StorageItem storageItem : storageItems) {
                        String storageLocation = "";

                        List<ItemInstance> itemInstances = itemInstanceRepository.getItemInstancesByItemAndBomAndStorage(requestItem.getItem().getItem().getId(), bom.getId(), storageItem.getStorage().getId());

                        if (itemInstances.size() > 0) {
                            if (storageItem.getStorage().getParent() != null) {
                                Storage parentStorage = storageRepository.findOne(storageItem.getStorage().getParent());

                                if (parentStorage != null) {
                                    storageLocation = parentStorage.getName() + "/" + storageItem.getStorage().getName();
                                    if (parentStorage.getParent() != null) {
                                        Storage parentStorage1 = storageRepository.findOne(parentStorage.getParent());
                                        storageLocation = parentStorage1.getName() + " /" + storageLocation;

                                        if (parentStorage1.getParent() != null) {
                                            Storage parentStorage2 = storageRepository.findOne(parentStorage1.getParent());
                                            storageLocation = parentStorage2.getName() + " /" + storageLocation;

                                            if (parentStorage2.getParent() != null) {

                                                Storage parentStorage3 = storageRepository.findOne(parentStorage2.getParent());
                                                storageLocation = parentStorage3.getName() + " /" + storageLocation;

                                                if (parentStorage3.getParent() != null) {

                                                    Storage parentStorage4 = storageRepository.findOne(parentStorage3.getParent());
                                                    storageLocation = parentStorage4.getName() + " /" + storageLocation;
                                                }
                                            }
                                        }

                                    }
                                }
                            } else {
                                storageLocation = storageItem.getStorage().getName();
                            }

                            locationMap.put(storageLocation, storageLocation);
                        }
                    }

                    locationMap.values().forEach(s -> {
                        issueItemDto.getStorageLocations().add(s);
                    });

                    Collections.sort(issueItemDto.getInventoryInstances(), new Comparator<ItemInstance>() {
                        public int compare(final ItemInstance object1, final ItemInstance object2) {
                            return object1.getOemNumber().compareTo(object2.getOemNumber());
                        }
                    });

                    issueItemDtos.add(issueItemDto);
                }

            } else {

                List<IssueItem> issueItems = issueItemRepository.getApprovedItemsByRequest(requestItem.getId());

                for (IssueItem issueItem : issueItems) {
                    IssueItemDto issuedItemDto = new IssueItemDto();
                    issuedItemDto.setRequestItem(requestItem);

                    BomItemInstance bomItemInstance = bomItemInstanceRepository.findOne(issueItem.getBomItemInstance().getId());

                    if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                        issuedItemDto.setStatus(issueItem.getStatus());
                    } else {
                        if (issueItem.getStatus().equals(IssueItemStatus.APPROVED) || issueItem.getStatus().equals(IssueItemStatus.P_APPROVED)) {
                            issuedItemDto.setStatus(issueItem.getStatus());
                        } else {
                            issuedItemDto.setStatus(issueItem.getStatus());
                        }

                        String storageLocation = "";
                        Storage storage = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance()).getStorage();

                        if (storage != null && storage.getParent() != null) {
                            Storage parentStorage = storageRepository.findOne(storage.getParent());

                            if (parentStorage != null) {
                                storageLocation = parentStorage.getName() + "/" + storage.getName();
                                if (parentStorage.getParent() != null) {
                                    Storage parentStorage1 = storageRepository.findOne(parentStorage.getParent());
                                    storageLocation = parentStorage1.getName() + " /" + storageLocation;

                                    if (parentStorage1.getParent() != null) {
                                        Storage parentStorage2 = storageRepository.findOne(parentStorage1.getParent());
                                        storageLocation = parentStorage2.getName() + " /" + storageLocation;

                                        if (parentStorage2.getParent() != null) {

                                            Storage parentStorage3 = storageRepository.findOne(parentStorage2.getParent());
                                            storageLocation = parentStorage3.getName() + " /" + storageLocation;

                                            if (parentStorage3.getParent() != null) {

                                                Storage parentStorage4 = storageRepository.findOne(parentStorage3.getParent());
                                                storageLocation = parentStorage4.getName() + " /" + storageLocation;
                                            }
                                        }
                                    }

                                }
                            }
                        } else {
                            if (storage != null) {
                                storageLocation = storage.getName();
                            }

                        }
                        issuedItemDto.getStorageLocations().add(storageLocation);
                    }


                    issuedItemDto.setRequestQuantity(1);
                    issuedItemDto.setIssuedItemInstance(itemInstanceRepository.findOne(bomItemInstance.getItemInstance()));

                    issueItemDtos.add(issuedItemDto);
                }

                Integer requestedQuantity = requestItem.getQuantity();

                requestedQuantity = requestedQuantity - issueItems.size();
                Double allocateQty = newIssueDto.getAllocateQty();

                BomInstanceItem bomItemSection = null;
                BomInstanceItem parent = bomInstanceItemRepository.findOne(requestItem.getItem().getParent());
                if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                    BomInstanceItem bomItemUnit = parent;
                    BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                    if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                        if (parent2 != null) {
                            bomItemSection = parent2;
                        }
                    } else {
                        bomItemSection = parent1;
                    }
                } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                    bomItemSection = parent1;
                } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                    bomItemSection = parent;
                }

                Bom bom = bomRepository.findOne(request.getBomInstance().getBom());

                List<ItemInstance> inventoryItemInstances = new ArrayList<>();
                if (bomItemSection != null) {
                    inventoryItemInstances = itemInstanceRepository.getItemInstancesBySectionAndBomAndUniqueCode(requestItem.getItem().getItem().getId(), bomItemSection.getTypeRef().getId(), bom.getId(), requestItem.getItem().getUniqueCode());
                }

                List<ItemInstance> inventoryItemInstancesWithoutSection = itemInstanceRepository.getItemInstancesByBomAndUniqueCodeAndSectionIsNull(requestItem.getItem().getItem().getId(), bom.getId(), requestItem.getItem().getUniqueCode());

//                HashMap<Integer, ItemInstance> issuedItemInstanceMap = new HashMap<>();

                for (int i = 0; i < requestedQuantity; i++) {
                    IssueItemDto issueItemDto = new IssueItemDto();
                    issueItemDto.setRequestItem(requestItem);
                    issueItemDto.setRequestQuantity(1);
                    issueItemDto.setIssued(false);
                    if (allocateQty >= 1.0) {
                        issueItemDto.setAllocateQty(1.0);
                        allocateQty--;
                    }

                    if (issueItemDto.getAllocateQty() > 0) {
                        inventoryItemInstances.forEach(inventoryItemInstance -> {
                            BomItemInstance bomItemInstance = bomItemInstanceRepository.findByItemInstance(inventoryItemInstance.getId());

                            if (bomItemInstance == null) {

                                Date todayDate = new Date();
                                if (inventoryItemInstance.getExpiryDate() != null) {
                                    if (todayDate.before(inventoryItemInstance.getExpiryDate()) || todayDate.equals(inventoryItemInstance.getExpiryDate())) {
                                        issueItemDto.getInventoryInstances().add(inventoryItemInstance);
                                    }
                                } else {
                                    issueItemDto.getInventoryInstances().add(inventoryItemInstance);
                                }
                            }
                        });

                        inventoryItemInstancesWithoutSection.forEach(inventoryItemInstance -> {
                            BomItemInstance bomItemInstance = bomItemInstanceRepository.findByItemInstance(inventoryItemInstance.getId());

                            if (bomItemInstance == null) {
                                Date todayDate = new Date();
                                if (inventoryItemInstance.getExpiryDate() != null) {
                                    if (todayDate.before(inventoryItemInstance.getExpiryDate()) || todayDate.equals(inventoryItemInstance.getExpiryDate())) {
                                        issueItemDto.getInventoryInstances().add(inventoryItemInstance);
                                    }
                                } else {
                                    issueItemDto.getInventoryInstances().add(inventoryItemInstance);
                                }

                            }
                        });
                    }

                    /*if (inventoryItemInstances.size() > 0) {
                        for (ItemInstance inventoryItemInstance : inventoryItemInstances) {
                            BomItemInstance bomItemInstance = bomItemInstanceRepository.findByItemInstance(inventoryItemInstance.getId());

                            if (bomItemInstance == null) {
                                ItemInstance existInstance = issuedItemInstanceMap.get(inventoryItemInstance.getId());

                                if (existInstance == null) {
                                    inventoryItemInstance = getIssueUpn(request, requestItem.getItem(), inventoryItemInstance);


                                    issueItemDto.setSelectInstanceToIssue(inventoryItemInstance);
                                    issuedItemInstanceMap.put(inventoryItemInstance.getId(), inventoryItemInstance);
                                    break;
                                }
                            }
                        }
                    } else {
                        for (ItemInstance inventoryItemInstance : inventoryItemInstancesWithoutSection) {
                            BomItemInstance bomItemInstance = bomItemInstanceRepository.findByItemInstance(inventoryItemInstance.getId());

                            if (bomItemInstance == null) {
                                ItemInstance existInstance = issuedItemInstanceMap.get(inventoryItemInstance.getId());

                                if (existInstance == null) {

                                    inventoryItemInstance = getIssueUpn(request, requestItem.getItem(), inventoryItemInstance);

                                    issueItemDto.setSelectInstanceToIssue(inventoryItemInstance);
                                    issuedItemInstanceMap.put(inventoryItemInstance.getId(), inventoryItemInstance);
                                    break;
                                }
                            }
                        }
                    }*/
                    BomInstance bomInstance = request.getBomInstance();

                    bom = bomRepository.findOne(bomInstance.getBom());

                    List<StorageItem> storageItems = new ArrayList<>();

                    bomItemSection = null;
                    parent = bomInstanceItemRepository.findOne(requestItem.getItem().getParent());
                    if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                        BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                        if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                            if (parent2 != null) {
                                bomItemSection = parent2;
                            }
                        } else {
                            bomItemSection = parent1;
                        }
                    } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                        bomItemSection = parent1;
                    } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                        bomItemSection = parent;
                    }

                    if (bomItemSection != null) {
                        storageItems.addAll(storageItemRepository.getItemStorageByBomAndUniqueCodeAndSection(bom.getId(), requestItem.getItem().getUniqueCode(), bomItemSection.getTypeRef().getId()));
                    }
                    storageItems.addAll(storageItemRepository.getItemStorageByBomAndUniqueCodeAndSectionIsNull(bom.getId(), requestItem.getItem().getUniqueCode()));

                    Map<String, String> locationMap = new HashMap<>();

                    for (StorageItem storageItem : storageItems) {
                        String storageLocation = "";

                        List<ItemInstance> itemInstances = itemInstanceRepository.getItemInstancesByItemAndBomAndStorage(requestItem.getItem().getItem().getId(), bom.getId(), storageItem.getStorage().getId());

                        if (itemInstances.size() > 0) {
                            if (storageItem.getStorage().getParent() != null) {
                                Storage parentStorage = storageRepository.findOne(storageItem.getStorage().getParent());

                                if (parentStorage != null) {
                                    storageLocation = parentStorage.getName() + "/" + storageItem.getStorage().getName();
                                    if (parentStorage.getParent() != null) {
                                        Storage parentStorage1 = storageRepository.findOne(parentStorage.getParent());
                                        storageLocation = parentStorage1.getName() + " /" + storageLocation;

                                        if (parentStorage1.getParent() != null) {
                                            Storage parentStorage2 = storageRepository.findOne(parentStorage1.getParent());
                                            storageLocation = parentStorage2.getName() + " /" + storageLocation;

                                            if (parentStorage2.getParent() != null) {

                                                Storage parentStorage3 = storageRepository.findOne(parentStorage2.getParent());
                                                storageLocation = parentStorage3.getName() + " /" + storageLocation;

                                                if (parentStorage3.getParent() != null) {

                                                    Storage parentStorage4 = storageRepository.findOne(parentStorage3.getParent());
                                                    storageLocation = parentStorage4.getName() + " /" + storageLocation;
                                                }
                                            }
                                        }

                                    }
                                }
                            } else {
                                storageLocation = storageItem.getStorage().getName();
                            }

                            locationMap.put(storageLocation, storageLocation);
                        }
                    }

                    locationMap.values().forEach(s -> {
                        issueItemDto.getStorageLocations().add(s);
                    });


                    Collections.sort(issueItemDto.getInventoryInstances(), new Comparator<ItemInstance>() {
                        public int compare(final ItemInstance object1, final ItemInstance object2) {
                            return object1.getOemNumber().compareTo(object2.getOemNumber());
                        }
                    });

                    issueItemDtos.add(issueItemDto);
                }
            }

            newIssueDto.setIssuedItemDto(issueItemDtos);

            newIssueDtos.add(newIssueDto);
        }

        return newIssueDtos;
    }

    @Transactional(readOnly = true)
    public ItemInstance validateUpnNumber(Integer requestId, Integer itemId, String oemNumber) {

        Request request = requestRepository.findOne(requestId);
        BomInstance bomInstance = request.getBomInstance();

        Bom bom = bomRepository.findOne(bomInstance.getBom());

        ItemInstance itemInstance = null;
        BomInstanceItem instanceItem = bomInstanceItemRepository.findOne(itemId);

//        itemInstance = itemInstanceRepository.findByUpnNumber(upnNumber);

        itemInstance = itemInstanceRepository.findByOemNumberAndItemAndBom(oemNumber, instanceItem.getItem(), bom.getId());

        if (itemInstance == null) {
            throw new CassiniException(oemNumber + " Serial Number does not exist");
        }

        if (itemInstance.getStatus().equals(ItemInstanceStatus.ISSUE)) {
            throw new CassiniException(oemNumber + " : Serial Number already issued. Please pick another part");
        }

        if (itemInstance.getStatus().equals(ItemInstanceStatus.FAILURE)) {
            throw new CassiniException("Please pick another part. Selected Part already Failed");
        }

        if (itemInstance.getStatus().equals(ItemInstanceStatus.RETURN)) {
            throw new CassiniException("Please pick another part. Selected Part already Returned");
        }

        if (itemInstance.getStatus().equals(ItemInstanceStatus.DISPATCH)) {
            throw new CassiniException("Please pick another part. Selected Part already Dispatched");
        }

        if (itemInstance.getStatus().equals(ItemInstanceStatus.P_APPROVED)) {
            if (!itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                throw new CassiniException(oemNumber + " : Serial Number already in issue. Please pick another part");
            }
        }

        if (itemInstance.getStatus().equals(ItemInstanceStatus.REJECTED)) {
            throw new CassiniException(oemNumber + " : Serial Number Rejected. Please pick another part");
        }

        if (itemInstance.getStatus().equals(ItemInstanceStatus.APPROVED)) {
            if (!itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                throw new CassiniException(oemNumber + " : Serial Number already in issue. Please pick another part");
            }
        }

        if (!itemInstance.getStatus().equals(ItemInstanceStatus.INVENTORY) && !itemInstance.getStatus().equals(ItemInstanceStatus.APPROVED) && !itemInstance.getStatus().equals(ItemInstanceStatus.P_APPROVED)) {
            throw new CassiniException(oemNumber + " : Serial Number is not accepted Part. Please pick another part");
        }

        BomItemInstance bomItemInstance = null;

        if (!itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
            bomItemInstance = bomItemInstanceRepository.findByItemInstance(itemInstance.getId());

            if (bomItemInstance != null) {
                throw new CassiniException(oemNumber + " : Serial Number already in issue. Please pick another part");
            }
        }

        BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(itemId);

        bom = null;

        bomInstance = null;
        BomInstanceItem bomItemSection = null;
        BomInstanceItem bomItemSubSystem = null;
        BomInstanceItem bomItemUnit = null;

        if (bomInstanceItem != null) {
            BomInstanceItem parent = bomInstanceItemRepository.findOne(bomInstanceItem.getParent());
            if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                bomItemUnit = parent;
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                    bomItemSubSystem = parent1;
                    if (parent2 != null) {
                        bomItemSection = parent2;

                        bomInstance = bomInstanceRepository.findOne(bomItemSection.getBom());
                    }
                } else {
                    bomItemSection = parent1;

                    bomInstance = bomInstanceRepository.findOne(bomItemSection.getBom());
                }
            } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                bomItemSubSystem = parent;
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                bomItemSection = parent1;

                bomInstance = bomInstanceRepository.findOne(bomItemSection.getBom());
            } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                bomItemSection = parent;

                bomInstance = bomInstanceRepository.findOne(bomItemSection.getBom());
            }

            if (bomInstance != null) {
                bom = bomRepository.findOne(bomInstance.getBom());
            }

            if (bom != null && !bom.getId().equals(itemInstance.getBom())) {

                throw new CassiniException("Selected Part is not belongs to " + bom.getItem().getItemMaster().getItemName() + " BOM");
            }

            if (!bomInstanceItem.getItem().getId().equals(itemInstance.getItem().getId())) {
                throw new CassiniException("Selected Serial Number Part not belongs to " + bomInstanceItem.getItem().getItemMaster().getItemName());
            }

            if (bomInstanceItem.getItem().getId().equals(itemInstance.getItem().getId()) && bomItemSection != null && itemInstance.getSection() != null
                    && !bomItemSection.getTypeRef().getId().equals(itemInstance.getSection())) {
                throw new CassiniException("Selected Serial Number Part not belongs to " + bomItemSection.getTypeRef().getName());
            }

        }

        String system = itemInstance.getUpnNumber().substring(0, 2);
        String missile = request.getBomInstance().getItem().getInstanceName();
        String section = "0";
        String subSystem = "0";
        String unit = "00";
        String part = "00";
        String specName = "";
        String serialNumber = "0000";

        if (bomItemSection != null) {
            section = bomItemSection.getTypeRef().getCode();
        }

        if (bomItemSubSystem != null) {
            subSystem = bomItemSubSystem.getTypeRef().getCode();
        }

        if (bomItemUnit != null) {
            unit = bomItemUnit.getTypeRef().getCode();
        }

        if (bomInstanceItem != null) {
            part = bomInstanceItem.getItem().getItemMaster().getItemCode();
        }

        /*String sn = "";
        if (itemInstance.getOemNumber().length() > 4) {
            sn = itemInstance.getOemNumber().substring(itemInstance.getOemNumber().length() - 4);
        } else {
            sn = itemInstance.getOemNumber();
        }
        String snPart = serialNumber.substring(sn.length()) + sn;
        snPart = snPart.toUpperCase();

        String lastTwo = itemInstance.getUpnNumber().substring(14, 16);*/

//        itemInstance.setUpnNumber(system + "" + missile + "" + section + "" + subSystem + "" + unit + "" + part + "" + snPart + "" + lastTwo);
        itemInstance.setUpnNumber(system + "" + missile + "" + section + "" + subSystem + "" + unit + "" + part);

        return itemInstance;
    }

    private ItemInstance getIssueUpn(Request request, BomInstanceItem bomInstanceItem, ItemInstance itemInstance) {

        Bom bom = null;
        BomInstance bomInstance = null;
        BomInstanceItem bomItemSection = null;
        BomInstanceItem bomItemSubSystem = null;
        BomInstanceItem bomItemUnit = null;

        if (bomInstanceItem != null) {
            BomInstanceItem parent = bomInstanceItemRepository.findOne(bomInstanceItem.getParent());
            if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                bomItemUnit = parent;
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                    bomItemSubSystem = parent1;
                    if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                        bomItemSection = parent2;

                        bomInstance = bomInstanceRepository.findOne(bomItemSection.getBom());
                    }
                } else {
                    bomItemSection = parent1;

                    bomInstance = bomInstanceRepository.findOne(bomItemSection.getBom());
                }
            } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                bomItemSubSystem = parent;
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                bomItemSection = parent1;

                bomInstance = bomInstanceRepository.findOne(bomItemSection.getBom());
            } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                bomItemSection = parent;

                bomInstance = bomInstanceRepository.findOne(bomItemSection.getBom());
            }
        }

        String system = itemInstance.getUpnNumber().substring(0, 2);
        String missile = request.getBomInstance().getItem().getInstanceName();
        String section = "0";
        String subSystem = "0";
        String unit = "00";
        String part = "00";
        String specName = "";
        String serialNumber = "0000";

        if (bomItemSection != null) {
            section = bomItemSection.getTypeRef().getCode();
        }

        if (bomItemSubSystem != null) {
            subSystem = bomItemSubSystem.getTypeRef().getCode();
        }

        if (bomItemUnit != null) {
            unit = bomItemUnit.getTypeRef().getCode();
        }

        if (bomInstanceItem != null) {
            part = bomInstanceItem.getItem().getItemMaster().getItemCode();
        }

        if (bomItemSection != null && bomItemSection.getBomItemType().equals(BomItemType.COMMONPART)) {
            itemInstance.setUpnNumber(system + "" + missile + "" + "0000" + "" + part);
        } else if (bomItemSection != null && bomItemSection.getBomItemType().equals(BomItemType.SECTION)) {
            itemInstance.setUpnNumber(system + "" + missile + "" + section + "" + subSystem + "" + unit + "" + part);
        }

        return itemInstance;
    }

    @Transactional(readOnly = true)
    public List<RequestSummary> getRequestSummary() {
        List<RequestSummary> requestSummaries = new ArrayList<>();

        List<Request> requestList = requestRepository.findAll();

        requestList.forEach(request -> {
            RequestSummary requestSummary = new RequestSummary();
            requestSummary.setRequest(request);
            List<RequestItem> requestItems = requestItemRepository.findByRequest(request);

            requestItems.forEach(requestItem -> {
                if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                    requestSummary.setRequestedQty(requestSummary.getRequestedQty() + requestItem.getFractionalQuantity());

                    if (requestItem.getStatus().equals(RequestItemStatus.APPROVED)) {
                        requestSummary.setApprovedQty(requestSummary.getApprovedQty() + requestItem.getFractionalQuantity());
                        requestSummary.setAcceptedQty(requestSummary.getAcceptedQty() + requestItem.getFractionalQuantity());

                    } else if (requestItem.getStatus().equals(RequestItemStatus.ACCEPTED)) {
                        requestSummary.setAcceptedQty(requestSummary.getAcceptedQty() + requestItem.getFractionalQuantity());

                    } else if (requestItem.getStatus().equals(RequestItemStatus.REJECTED)) {
                        requestSummary.setRejectedQty(requestSummary.getRejectedQty() + requestItem.getFractionalQuantity());
                    }

                    List<IssueItem> issueItems = issueItemRepository.getApprovedItemsByRequest(requestItem.getId());

                    issueItems.forEach(issueItem -> {
                        if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                            requestSummary.setQcApprovedQty(requestSummary.getQcApprovedQty() + issueItem.getFractionalQuantity());
                            requestSummary.setReceivedQty(requestSummary.getReceivedQty() + issueItem.getFractionalQuantity());
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getFractionalQuantity());

                        } else if (issueItem.getStatus().equals(IssueItemStatus.APPROVED) || issueItem.getStatus().equals(IssueItemStatus.P_APPROVED)) {
                            requestSummary.setQcApprovedQty(requestSummary.getQcApprovedQty() + issueItem.getFractionalQuantity());
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getFractionalQuantity());

                        } else if (issueItem.getStatus().equals(IssueItemStatus.REJECTED)) {
                            requestSummary.setRejectedQty(requestSummary.getRejectedQty() + requestItem.getFractionalQuantity());
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getFractionalQuantity());

                        } else if (issueItem.getStatus().equals(IssueItemStatus.PENDING)) {
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getFractionalQuantity());
                        }
                    });

                } else {
                    requestSummary.setRequestedQty(requestSummary.getRequestedQty() + requestItem.getQuantity().doubleValue());

                    if (requestItem.getStatus().equals(RequestItemStatus.APPROVED)) {
                        requestSummary.setApprovedQty(requestSummary.getApprovedQty() + requestItem.getQuantity());
                        requestSummary.setAcceptedQty(requestSummary.getAcceptedQty() + requestItem.getQuantity());

                    } else if (requestItem.getStatus().equals(RequestItemStatus.ACCEPTED)) {
                        requestSummary.setAcceptedQty(requestSummary.getAcceptedQty() + requestItem.getQuantity());

                    } else if (requestItem.getStatus().equals(RequestItemStatus.REJECTED)) {
                        requestSummary.setRejectedQty(requestSummary.getRejectedQty() + requestItem.getQuantity());
                    }

                    List<IssueItem> issueItems = issueItemRepository.getApprovedItemsByRequest(requestItem.getId());

                    issueItems.forEach(issueItem -> {
                        if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                            requestSummary.setQcApprovedQty(requestSummary.getQcApprovedQty() + issueItem.getQuantity());
                            requestSummary.setReceivedQty(requestSummary.getReceivedQty() + issueItem.getQuantity());
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getQuantity());

                        } else if (issueItem.getStatus().equals(IssueItemStatus.APPROVED) || issueItem.getStatus().equals(IssueItemStatus.P_APPROVED)) {
                            requestSummary.setQcApprovedQty(requestSummary.getQcApprovedQty() + issueItem.getQuantity());
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getQuantity());

                        } else if (issueItem.getStatus().equals(IssueItemStatus.REJECTED)) {
                            requestSummary.setRejectedQty(requestSummary.getRejectedQty() + requestItem.getQuantity());
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getQuantity());

                        } else if (issueItem.getStatus().equals(IssueItemStatus.PENDING)) {
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getQuantity());
                        }
                    });
                }
            });

            requestSummaries.add(requestSummary);
        });

        return requestSummaries;
    }

    @Transactional(readOnly = true)
    public List<RequestSummary> getRequestSummaryByInstance(Integer instanceId) {
        List<RequestSummary> requestSummaries = new ArrayList<>();

        List<Request> requestList = requestRepository.getRequestsByInstance(instanceId);

        requestList.forEach(request -> {
            RequestSummary requestSummary = new RequestSummary();
            requestSummary.setRequest(request);
            List<RequestItem> requestItems = requestItemRepository.findByRequest(request);

            requestItems.forEach(requestItem -> {
                if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                    requestSummary.setRequestedQty(requestSummary.getRequestedQty() + requestItem.getFractionalQuantity());

                    if (requestItem.getStatus().equals(RequestItemStatus.APPROVED)) {
                        requestSummary.setApprovedQty(requestSummary.getApprovedQty() + requestItem.getFractionalQuantity());
                        requestSummary.setAcceptedQty(requestSummary.getAcceptedQty() + requestItem.getFractionalQuantity());

                    } else if (requestItem.getStatus().equals(RequestItemStatus.ACCEPTED)) {
                        requestSummary.setAcceptedQty(requestSummary.getAcceptedQty() + requestItem.getFractionalQuantity());

                    } else if (requestItem.getStatus().equals(RequestItemStatus.REJECTED)) {
                        requestSummary.setRejectedQty(requestSummary.getRejectedQty() + requestItem.getFractionalQuantity());
                    }

                    List<IssueItem> issueItems = issueItemRepository.getApprovedItemsByRequest(requestItem.getId());

                    issueItems.forEach(issueItem -> {
                        if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                            requestSummary.setQcApprovedQty(requestSummary.getQcApprovedQty() + issueItem.getFractionalQuantity());
                            requestSummary.setReceivedQty(requestSummary.getReceivedQty() + issueItem.getFractionalQuantity());
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getFractionalQuantity());

                        } else if (issueItem.getStatus().equals(IssueItemStatus.APPROVED) || issueItem.getStatus().equals(IssueItemStatus.P_APPROVED)) {
                            requestSummary.setQcApprovedQty(requestSummary.getQcApprovedQty() + issueItem.getFractionalQuantity());
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getFractionalQuantity());

                        } else if (issueItem.getStatus().equals(IssueItemStatus.REJECTED)) {
                            requestSummary.setRejectedQty(requestSummary.getRejectedQty() + requestItem.getFractionalQuantity());
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getFractionalQuantity());

                        } else if (issueItem.getStatus().equals(IssueItemStatus.PENDING)) {
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getFractionalQuantity());
                        }
                    });

                } else {
                    requestSummary.setRequestedQty(requestSummary.getRequestedQty() + requestItem.getQuantity().doubleValue());

                    if (requestItem.getStatus().equals(RequestItemStatus.APPROVED)) {
                        requestSummary.setApprovedQty(requestSummary.getApprovedQty() + requestItem.getQuantity());
                        requestSummary.setAcceptedQty(requestSummary.getAcceptedQty() + requestItem.getQuantity());

                    } else if (requestItem.getStatus().equals(RequestItemStatus.ACCEPTED)) {
                        requestSummary.setAcceptedQty(requestSummary.getAcceptedQty() + requestItem.getQuantity());

                    } else if (requestItem.getStatus().equals(RequestItemStatus.REJECTED)) {
                        requestSummary.setRejectedQty(requestSummary.getRejectedQty() + requestItem.getQuantity());
                    }

                    List<IssueItem> issueItems = issueItemRepository.getApprovedItemsByRequest(requestItem.getId());

                    issueItems.forEach(issueItem -> {
                        if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                            requestSummary.setQcApprovedQty(requestSummary.getQcApprovedQty() + issueItem.getQuantity());
                            requestSummary.setReceivedQty(requestSummary.getReceivedQty() + issueItem.getQuantity());
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getQuantity());

                        } else if (issueItem.getStatus().equals(IssueItemStatus.APPROVED) || issueItem.getStatus().equals(IssueItemStatus.P_APPROVED)) {
                            requestSummary.setQcApprovedQty(requestSummary.getQcApprovedQty() + issueItem.getQuantity());
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getQuantity());

                        } else if (issueItem.getStatus().equals(IssueItemStatus.REJECTED)) {
                            requestSummary.setRejectedQty(requestSummary.getRejectedQty() + requestItem.getQuantity());
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getQuantity());

                        } else if (issueItem.getStatus().equals(IssueItemStatus.PENDING)) {
                            requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getQuantity());
                        }
                    });
                }
            });

            requestSummaries.add(requestSummary);
        });

        return requestSummaries;
    }

    @Transactional(readOnly = true)
    public List<RequestSummary> getSummaryByRequest(Integer requestId) {
        List<RequestSummary> requestSummaries = new ArrayList<>();

        Request request = requestRepository.findOne(requestId);
        List<RequestItem> requestItems = requestItemRepository.findByRequest(request);

        requestItems.forEach(requestItem -> {
            RequestSummary requestSummary = new RequestSummary();
            requestSummary.setRequest(request);
            requestSummary.setItemName(requestItem.getItem().getItem().getItemMaster().getItemName());

            ItemType itemType = itemTypeRepository.findOne(requestItem.getItem().getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                requestSummary.setType(itemType.getName());
            } else {
                requestSummary.setType(requestItem.getItem().getItem().getItemMaster().getItemType().getName());
            }

            if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                requestSummary.setRequestedQty(requestSummary.getRequestedQty() + requestItem.getFractionalQuantity());

                if (requestItem.getStatus().equals(RequestItemStatus.APPROVED)) {
                    requestSummary.setAcceptedQty(requestSummary.getAcceptedQty() + requestItem.getFractionalQuantity());
                    requestSummary.setApprovedQty(requestSummary.getApprovedQty() + requestItem.getFractionalQuantity());

                } else if (requestItem.getStatus().equals(RequestItemStatus.ACCEPTED)) {
                    requestSummary.setAcceptedQty(requestSummary.getAcceptedQty() + requestItem.getFractionalQuantity());

                } else if (requestItem.getStatus().equals(RequestItemStatus.REJECTED)) {
                    requestSummary.setRejectedQty(requestSummary.getRejectedQty() + requestItem.getFractionalQuantity());
                }

                List<IssueItem> issueItems = issueItemRepository.getApprovedItemsByRequest(requestItem.getId());

                issueItems.forEach(issueItem -> {
                    if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                        requestSummary.setQcApprovedQty(requestSummary.getQcApprovedQty() + issueItem.getFractionalQuantity());
                        requestSummary.setReceivedQty(requestSummary.getReceivedQty() + issueItem.getFractionalQuantity());
                        requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getFractionalQuantity());

                    } else if (issueItem.getStatus().equals(IssueItemStatus.APPROVED) || issueItem.getStatus().equals(IssueItemStatus.P_APPROVED)) {
                        requestSummary.setQcApprovedQty(requestSummary.getQcApprovedQty() + issueItem.getFractionalQuantity());
                        requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getFractionalQuantity());

                    } else if (issueItem.getStatus().equals(IssueItemStatus.REJECTED)) {
                        requestSummary.setRejectedQty(requestSummary.getRejectedQty() + requestItem.getFractionalQuantity());
                        requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getFractionalQuantity());

                    } else if (issueItem.getStatus().equals(IssueItemStatus.PENDING)) {
                        requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getFractionalQuantity());
                    }
                });

            } else {
                requestSummary.setRequestedQty(requestSummary.getRequestedQty() + requestItem.getQuantity().doubleValue());

                if (requestItem.getStatus().equals(RequestItemStatus.APPROVED)) {
                    requestSummary.setApprovedQty(requestSummary.getApprovedQty() + requestItem.getQuantity());
                    requestSummary.setAcceptedQty(requestSummary.getAcceptedQty() + requestItem.getQuantity());

                } else if (requestItem.getStatus().equals(RequestItemStatus.ACCEPTED)) {
                    requestSummary.setAcceptedQty(requestSummary.getAcceptedQty() + requestItem.getQuantity());

                } else if (requestItem.getStatus().equals(RequestItemStatus.REJECTED)) {
                    requestSummary.setRejectedQty(requestSummary.getRejectedQty() + requestItem.getQuantity());
                }

                List<IssueItem> issueItems = issueItemRepository.getApprovedItemsByRequest(requestItem.getId());

                issueItems.forEach(issueItem -> {
                    if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                        requestSummary.setQcApprovedQty(requestSummary.getQcApprovedQty() + issueItem.getQuantity());
                        requestSummary.setReceivedQty(requestSummary.getReceivedQty() + issueItem.getQuantity());
                        requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getQuantity());

                    } else if (issueItem.getStatus().equals(IssueItemStatus.APPROVED) || issueItem.getStatus().equals(IssueItemStatus.P_APPROVED)) {
                        requestSummary.setQcApprovedQty(requestSummary.getQcApprovedQty() + issueItem.getQuantity());
                        requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getQuantity());

                    } else if (issueItem.getStatus().equals(IssueItemStatus.REJECTED)) {
                        requestSummary.setRejectedQty(requestSummary.getRejectedQty() + requestItem.getQuantity());
                        requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getQuantity());

                    } else if (issueItem.getStatus().equals(IssueItemStatus.PENDING)) {
                        requestSummary.setIssuedQty(requestSummary.getIssuedQty() + issueItem.getQuantity());
                    }
                });
            }

            requestSummaries.add(requestSummary);
        });


        return requestSummaries;
    }

    @Transactional(readOnly = false)
    public Request updateRequestByNumber(Integer requestNumber, String partName) {

        Request request = requestRepository.findOne(requestNumber);

        BomInstance requestBomInstance = request.getBomInstance();

        if (requestBomInstance.getItem().getInstanceName().equals("15")) {
            Bom bom = bomRepository.findOne(requestBomInstance.getBom());

            BomInstance bomInstance = bomInstanceRepository.getInstanceByBomAndName(bom.getId(), "11");

            if (bomInstance != null) {

                RequestItem requestItem = requestItemRepository.getRequestItemByItemName(requestNumber, partName);

                if (requestItem != null) {

                    if (requestItem.getStatus().equals(RequestItemStatus.APPROVED)) {

                        List<IssueItem> issueItems = issueItemRepository.findByRequestItem(requestItem.getId());

                        Issue issueItemsIssue = issueItems.get(0).getIssue();

                        BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findByBomItemAndStartWithIdPath(requestItem.getItem().getBomItem(), bomInstance.getId().toString());

                        List<RequestItem> requestedItems = requestItemRepository.getRequestItemByBomInstanceAndBomInstanceItem(bomInstance.getId(), bomInstanceItem.getId());

                        if (requestedItems.size() > 0) {

                        } else {
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Requisition Number Source");

                            String number = autoNumber.next();

                            autoNumber = autoNumberRepository.save(autoNumber);

                            Request newRequest = new Request();
                            newRequest.setBomInstance(bomInstance);
                            newRequest.setReqNumber(bom.getItem().getItemMaster().getItemCode() + "_" + bomInstance.getItem().getInstanceName() + "_" + number + " ( 15 --> 11 )");
                            newRequest.setRequestedBy(sessionWrapper.getSession().getLogin().getPerson());
                            newRequest.setRequestedDate(new Date());
                            newRequest.setIssued(true);
                            newRequest.setStatus(RequestStatus.APPROVED);

                            newRequest = requestRepository.save(newRequest);

                            RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
                            requestStatusHistory.setRequest(newRequest.getId());
                            requestStatusHistory.setOldStatus(RequestStatus.CORRECTION);
                            requestStatusHistory.setNewStatus(RequestStatus.CORRECTION);
                            requestStatusHistory.setTimestamp(new Date());
                            requestStatusHistory.setResult(true);
                            requestStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

                            requestStatusHistory = requestStatusHistoryRepository.save(requestStatusHistory);

                            RequestItem newRequestItem = new RequestItem();
                            newRequestItem.setRequest(newRequest);
                            newRequestItem.setItem(bomInstanceItem);
                            newRequestItem.setQuantity(requestItem.getQuantity());
                            newRequestItem.setFractionalQuantity(requestItem.getFractionalQuantity());
                            newRequestItem.setAccepted(true);
                            newRequestItem.setApproved(true);
                            newRequestItem.setStatus(RequestItemStatus.APPROVED);

                            newRequestItem = requestItemRepository.save(newRequestItem);

                            autoNumber = autoNumberRepository.findByName("Default Issue Number Source");

                            number = autoNumber.next();

                            autoNumber = autoNumberRepository.save(autoNumber);

                            Issue issue = new Issue();
                            issue.setNumber(bom.getItem().getItemMaster().getItemCode() + "_" + newRequest.getBomInstance().getItem().getInstanceName() + "_" + number + " ( 15 --> 11 )");
                            issue.setRequest(newRequest);
                            issue.setBomInstance(newRequest.getBomInstance());
                            issue.setIssuedTo(newRequest.getRequestedBy());
                            issue.setStatus(IssueStatus.RECEIVED);
                            issue = issueRepository.save(issue);

                            List<BDLReceive> receives = bdlReceiveRepository.findByIssue(issue.getId());

                            BDLReceive bdlReceive = new BDLReceive();
                            bdlReceive.setIssue(issue.getId());
                            bdlReceive.setReceiveSequence(receives.size() + 1);

                            bdlReceive = bdlReceiveRepository.save(bdlReceive);

                            List<IssueHistory> issueHistories = issueHistoryRepository.findByIssueOrderByUpdatedDateDesc(issueItems.get(0).getIssue().getId());

                            if (issueHistories.size() > 0) {
                                for (IssueHistory issueHistory : issueHistories) {
                                    IssueHistory history = new IssueHistory();
                                    history.setIssue(issue.getId());
                                    history.setStatus(issueHistory.getStatus());
                                    history.setUpdatedDate(issueHistory.getUpdatedDate());
                                    history.setUpdatedBy(issueHistory.getUpdatedBy());
                                    history = issueHistoryRepository.save(history);
                                }
                            }

                            if (newRequestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {

                                for (IssueItem issueItem : issueItems) {
                                    issueItem.setIssue(issue);
                                    issueItem.setRequestItem(newRequestItem.getId());
                                    issueItem.setStatus(IssueItemStatus.RECEIVED);

                                    ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());

                                    BomItemInstance bomItemInstance = bomItemInstanceRepository.findOne(issueItem.getBomItemInstance().getId());

                                    bomItemInstance.setBomInstanceItem(bomInstanceItem.getId());

                                    bomItemInstance = bomItemInstanceRepository.save(bomItemInstance);

                                    issueItem = issueItemRepository.save(issueItem);

                                    LotInstance lotInstance = lotInstanceRepository.findByInstanceAndIssueItem(itemInstance.getId(), issueItem.getId());

                                    BomInstanceItem bomItemSection = null;
                                    BomInstanceItem bomItemSubSystem = null;
                                    BomInstanceItem bomItemUnit = null;

                                    if (newRequestItem.getItem() != null) {
                                        BomInstanceItem parent = bomInstanceItemRepository.findOne(newRequestItem.getItem().getParent());
                                        if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                                            bomItemUnit = parent;
                                            BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                                            if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                                                BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                                                bomItemSubSystem = parent1;
                                                if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                                                    bomItemSection = parent2;
                                                }
                                            } else {
                                                bomItemSection = parent1;
                                            }
                                        } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                                            bomItemSubSystem = parent;
                                            BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                                            bomItemSection = parent1;

                                        } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                                            bomItemSection = parent;

                                        }
                                    }

                                    String system = itemInstance.getUpnNumber().substring(0, 2);
                                    String missile = newRequestItem.getRequest().getBomInstance().getItem().getInstanceName();
                                    String section = "0";
                                    String subSystem = "0";
                                    String unit = "00";
                                    String part = "00";

                                    if (bomItemSection != null) {
                                        section = bomItemSection.getTypeRef().getCode();
                                    }

                                    if (bomItemSubSystem != null) {
                                        subSystem = bomItemSubSystem.getTypeRef().getCode();
                                    }

                                    if (bomItemUnit != null) {
                                        unit = bomItemUnit.getTypeRef().getCode();
                                    }

                                    if (newRequestItem.getItem() != null) {
                                        part = newRequestItem.getItem().getItem().getItemMaster().getItemCode();
                                    }

                                    if (bomItemSection != null && bomItemSection.getBomItemType().equals(BomItemType.COMMONPART)) {
                                        lotInstance.setUpnNumber(system + "" + missile + "" + section + "" + subSystem + "" + unit + "" + part);
                                    } else if (bomItemSection != null && bomItemSection.getBomItemType().equals(BomItemType.SECTION)) {
                                        lotInstance.setUpnNumber(system + "" + missile + "" + section + "" + subSystem + "" + unit + "" + part);
                                    }

                                    lotInstance = lotInstanceRepository.save(lotInstance);

                                    ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceItem(newRequestItem.getItem().getId());

                                    if (itemAllocation != null) {
                                        itemAllocation.setIssuedQty(itemAllocation.getIssuedQty() + issueItem.getFractionalQuantity());
                                        itemAllocation.setAllocateQty(itemAllocation.getAllocateQty() + issueItem.getFractionalQuantity());
                                        itemAllocation = itemAllocationRepository.save(itemAllocation);
                                    }

                                    itemAllocation = itemAllocationRepository.findByBomInstanceItem(requestItem.getItem().getId());

                                    if (itemAllocation != null) {
                                        itemAllocation.setIssuedQty(itemAllocation.getIssuedQty() - issueItem.getFractionalQuantity());
                                        itemAllocation.setAllocateQty(itemAllocation.getAllocateQty() - issueItem.getFractionalQuantity());
                                        itemAllocation = itemAllocationRepository.save(itemAllocation);
                                    }


                                    BDLReceiveItem bdlReceiveItem = bdlReceiveItemRepository.findByIssueItem(issueItem.getId());
                                    if (bdlReceiveItem != null) {
                                        bdlReceiveItem.setReceive(bdlReceive.getId());
                                        bdlReceiveItem = bdlReceiveItemRepository.save(bdlReceiveItem);
                                    }
                                }
                            } else {

                                for (IssueItem issueItem : issueItems) {
                                    issueItem.setIssue(issue);
                                    issueItem.setRequestItem(newRequestItem.getId());
                                    issueItem.setStatus(IssueItemStatus.RECEIVED);

                                    ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());

                                    BomItemInstance bomItemInstance = bomItemInstanceRepository.findOne(issueItem.getBomItemInstance().getId());

                                    bomItemInstance.setBomInstanceItem(bomInstanceItem.getId());

                                    bomItemInstance = bomItemInstanceRepository.save(bomItemInstance);

                                    issueItem = issueItemRepository.save(issueItem);

                                    BomInstanceItem bomItemSection = null;
                                    BomInstanceItem bomItemSubSystem = null;
                                    BomInstanceItem bomItemUnit = null;

                                    if (newRequestItem.getItem() != null) {
                                        BomInstanceItem parent = bomInstanceItemRepository.findOne(newRequestItem.getItem().getParent());
                                        if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                                            bomItemUnit = parent;
                                            BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                                            if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                                                BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                                                bomItemSubSystem = parent1;
                                                if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                                                    bomItemSection = parent2;
                                                }
                                            } else {
                                                bomItemSection = parent1;
                                            }
                                        } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                                            bomItemSubSystem = parent;
                                            BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                                            bomItemSection = parent1;

                                        } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                                            bomItemSection = parent;

                                        }
                                    }

                                    String system = itemInstance.getUpnNumber().substring(0, 2);
                                    String missile = newRequestItem.getRequest().getBomInstance().getItem().getInstanceName();
                                    String section = "0";
                                    String subSystem = "0";
                                    String unit = "00";
                                    String part = "00";

                                    if (bomItemSection != null) {
                                        section = bomItemSection.getTypeRef().getCode();
                                    }

                                    if (bomItemSubSystem != null) {
                                        subSystem = bomItemSubSystem.getTypeRef().getCode();
                                    }

                                    if (bomItemUnit != null) {
                                        unit = bomItemUnit.getTypeRef().getCode();
                                    }

                                    if (newRequestItem.getItem() != null) {
                                        part = newRequestItem.getItem().getItem().getItemMaster().getItemCode();
                                    }

                                    if (bomItemSection != null && bomItemSection.getBomItemType().equals(BomItemType.COMMONPART)) {
                                        itemInstance.setUpnNumber(system + "" + missile + "" + section + "" + subSystem + "" + unit + "" + part);
                                    } else if (bomItemSection != null && bomItemSection.getBomItemType().equals(BomItemType.SECTION)) {
                                        itemInstance.setUpnNumber(system + "" + missile + "" + section + "" + subSystem + "" + unit + "" + part);
                                    }

                                    itemInstance = itemInstanceRepository.save(itemInstance);

                                    ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceItem(newRequestItem.getItem().getId());

                                    if (itemAllocation != null) {
                                        itemAllocation.setIssuedQty(itemAllocation.getIssuedQty() + 1);
                                        itemAllocation.setAllocateQty(itemAllocation.getAllocateQty() + 1);
                                        itemAllocation = itemAllocationRepository.save(itemAllocation);
                                    }

                                    itemAllocation = itemAllocationRepository.findByBomInstanceItem(requestItem.getItem().getId());

                                    if (itemAllocation != null) {
                                        itemAllocation.setIssuedQty(itemAllocation.getIssuedQty() - 1);
                                        itemAllocation.setAllocateQty(itemAllocation.getAllocateQty() - 1);
                                        itemAllocation = itemAllocationRepository.save(itemAllocation);
                                    }

                                    BDLReceiveItem bdlReceiveItem = bdlReceiveItemRepository.findByIssueItem(issueItem.getId());
                                    if (bdlReceiveItem != null) {
                                        bdlReceiveItem.setReceive(bdlReceive.getId());
                                        bdlReceiveItem = bdlReceiveItemRepository.save(bdlReceiveItem);
                                    }
                                }
                            }

                            requestItemRepository.delete(requestItem.getId());

                            List<IssueItem> issueItemsList = issueItemRepository.findByIssue(issueItemsIssue);
                            if (issueItemsList.size() == 0) {
                                issueRepository.delete(issueItemsIssue.getId());
                            }

                            List<RequestItem> requestItems = requestItemRepository.findByRequest(request);
                            if (requestItems.size() == 0) {
                                requestRepository.delete(request.getId());
                            }
                        }
                    }
                } else {
                    throw new CassiniException("Request Item not found");
                }
            }
        }

        return null;
    }
}
