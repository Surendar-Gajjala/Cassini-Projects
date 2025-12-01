package com.cassinisys.drdo.service.transactions;

import com.cassinisys.drdo.filtering.BomInstanceItemSearchPredicateBuilder;
import com.cassinisys.drdo.filtering.BomSearchCriteria;
import com.cassinisys.drdo.filtering.IssueCriteria;
import com.cassinisys.drdo.filtering.IssuePredicateBuilder;
import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.model.dto.*;
import com.cassinisys.drdo.model.inventory.Inventory;
import com.cassinisys.drdo.model.inventory.ItemAllocation;
import com.cassinisys.drdo.model.inventory.Storage;
import com.cassinisys.drdo.model.transactions.*;
import com.cassinisys.drdo.repo.bom.*;
import com.cassinisys.drdo.repo.inventory.InventoryRepository;
import com.cassinisys.drdo.repo.inventory.ItemAllocationRepository;
import com.cassinisys.drdo.repo.inventory.StorageRepository;
import com.cassinisys.drdo.repo.transactions.*;
import com.cassinisys.drdo.service.DRDOUpdatesService;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.model.security.GroupPermission;
import com.cassinisys.platform.repo.common.GroupMemberRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.repo.security.GroupPermissionRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by subramanyam reddy on 17-10-2018.
 */
@Service
public class IssueService implements CrudService<Issue, Integer> {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueItemRepository issueItemRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private ItemInstanceRepository itemInstanceRepository;

    @Autowired
    private BomRepository bomRepository;

    @Autowired
    private BomItemInstanceRepository bomItemInstanceRepository;

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private ItemInstanceStatusHistoryRepository itemInstanceStatusHistoryRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestItemRepository requestItemRepository;

    @Autowired
    private ItemAllocationRepository itemAllocationRepository;

    @Autowired
    private LotInstanceRepository lotInstanceRepository;

    @Autowired
    private LotInstanceHistoryRepository lotInstanceHistoryRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private IssuePredicateBuilder issuePredicateBuilder;

    @Autowired
    private BomInstanceItemRepository bomInstanceItemRepository;

    @Autowired
    private BomInstanceRepository bomInstanceRepository;

    @Autowired
    private BDLReceiveRepository bdlReceiveRepository;

    @Autowired
    private BDLReceiveItemRepository bdlReceiveItemRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ItemInstanceStatusHistoryRepository instanceStatusHistoryRepository;

    @Autowired
    private DRDOUpdatesService drdoUpdatesService;

    @Autowired
    private IssueHistoryRepository issueHistoryRepository;

    @Autowired
    private BomInstanceItemSearchPredicateBuilder bomInstanceItemSearchPredicateBuilder;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private GroupPermissionRepository groupPermissionRepository;

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    @Transactional(readOnly = false)
    public Issue create(Issue issue) {
        return issueRepository.save(issue);
    }

    @Override
    @Transactional(readOnly = false)
    public Issue update(Issue issue) {
        return issueRepository.save(issue);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer issueId) {
        issueRepository.delete(issueId);
    }

    @Override
    @Transactional(readOnly = true)
    public Issue get(Integer issueId) {
        return issueRepository.findOne(issueId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Issue> getAll() {
        return issueRepository.findAllByOrderByCreatedDateDesc();
    }

    @Transactional(readOnly = true)
    public List<Issue> getIssuesByInstance(Integer instanceId) {
        return issueRepository.findByBomInstance(instanceId);
    }

    @Transactional(readOnly = true)
    public Page<Issue> getAllIssues(Pageable pageable, IssueCriteria criteria) {
        Predicate predicate = issuePredicateBuilder.build(criteria, QIssue.issue);
        Page<Issue> issues = null;
        issues = issueRepository.findAll(predicate, pageable);

        issues.getContent().forEach(issue -> {
            List<IssueItem> issueItems = issueItemRepository.findByIssue(issue);
            Boolean provisionalApprove = false;
            for (IssueItem issueItem : issueItems) {
                ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());
                if (itemInstance != null && itemInstance.getProvisionalAccept()) {
                    provisionalApprove = true;
                }
            }

            if (provisionalApprove) {
                issue.setProvisionalApprove(true);
            }
        });

        return issues;
    }

    @Transactional(readOnly = false)
    public Issue newIssue(Issue issue) {

        List<IssueItemDto> issueItemDtos = issue.getIssueItemDtos();

        BomInstanceItem bomInstanceItem = issueItemDtos.get(0).getRequestItem().getItem();
        String sectionCode = "";
        if (bomInstanceItem.getParent() != null) {
            BomInstanceItem parent = bomInstanceItemRepository.findOne(bomInstanceItem.getParent());

            if (parent != null && parent.getBomItemType().equals(BomItemType.UNIT)) {
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());

                if (parent1 != null && parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());

                    if (parent2 != null && (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART))) {
                        sectionCode = parent2.getTypeRef().getCode();
                    }
                } else if (parent1 != null && (parent1.getBomItemType().equals(BomItemType.SECTION) || parent1.getBomItemType().equals(BomItemType.COMMONPART))) {
                    sectionCode = parent1.getTypeRef().getCode();
                }
            } else if (parent != null && parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());

                if (parent1 != null && (parent1.getBomItemType().equals(BomItemType.SECTION) || parent1.getBomItemType().equals(BomItemType.COMMONPART))) {
                    sectionCode = parent1.getTypeRef().getCode();
                }
            } else if (parent != null && parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                sectionCode = parent.getTypeRef().getCode();
            }
        }

        AutoNumber autoNumber = autoNumberRepository.findByName("Default Issue Number Source");

        String number = autoNumber.next();

        autoNumber = autoNumberRepository.save(autoNumber);

        Bom system = bomRepository.findOne(issue.getRequest().getBomInstance().getBom());

        issue.setNumber(system.getItem().getItemMaster().getItemCode() + "_" + issue.getRequest().getBomInstance().getItem().getInstanceName() + "_" + sectionCode + "" + number);

        issue.setBomInstance(issue.getRequest().getBomInstance());
        issue.setIssuedTo(issue.getRequest().getRequestedBy());
        if (issue.getRequest().getVersity()) {
            issue.setStatus(IssueStatus.VERSITY_QC);
        } else {
            issue.setStatus(IssueStatus.BDL_QC);
        }

        issue.setVersity(issue.getRequest().getVersity());
        issue = issueRepository.save(issue);

        for (IssueItemDto issueItemDto : issueItemDtos) {

            IssueItem issueItem = new IssueItem();

            ItemInstance instance = itemInstanceRepository.findOne(issueItemDto.getItemInstance().getId());
            if (instance != null && !instance.getStatus().equals(ItemInstanceStatus.INVENTORY) && !instance.getStatus().equals(ItemInstanceStatus.APPROVED) && !instance.getStatus().equals(ItemInstanceStatus.P_APPROVED)) {
                throw new CassiniException(issueItemDto.getSerialNumber() + " already is not accepted part.");
            }

            issueItem.setIssue(issue);

            BomItemInstance bomItemInstance = new BomItemInstance();
            bomItemInstance.setBomInstanceItem(issueItemDto.getRequestItem().getItem().getId());

            if (issueItemDto.getRequestItem().getItem().getItem().getItemMaster().getItemType().getHasLots()) {

                bomItemInstance = bomItemInstanceRepository.findByItemInstanceAndBomInstanceItem(issueItemDto.getItemInstance().getId(), issueItemDto.getRequestItem().getItem().getId());

                if (bomItemInstance == null) {
                    bomItemInstance = new BomItemInstance();
                    bomItemInstance.setItemInstance(issueItemDto.getItemInstance().getId());
                    bomItemInstance.setBomInstanceItem(issueItemDto.getRequestItem().getItem().getId());
                    bomItemInstance = bomItemInstanceRepository.save(bomItemInstance);
                }

                if (issueItemDto.getRequestItem().getFractionalQuantity().equals(issueItemDto.getAllocateQty())) {
                    issueItem.setFractionalQuantity(issueItemDto.getAllocateQty());
                } else if (issueItemDto.getRequestItem().getFractionalQuantity() > issueItemDto.getAllocateQty()) {
                    issueItem.setFractionalQuantity(issueItemDto.getAllocateQty());
                } else if (issueItemDto.getRequestItem().getFractionalQuantity() < issueItemDto.getAllocateQty()) {
                    issueItem.setFractionalQuantity(issueItemDto.getRequestItem().getFractionalQuantity());
                }


                issueItem.setBomItemInstance(bomItemInstance);
                issueItem.setRequestItem(issueItemDto.getRequestItem().getId());

                issueItem = issueItemRepository.save(issueItem);
                RequestItem requestItem = requestItemRepository.findOne(issueItem.getRequestItem());
                ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceItem(requestItem.getItem().getId());

                if (itemAllocation != null) {
                    itemAllocation.setIssueProcessQty(itemAllocation.getIssueProcessQty() + issueItem.getFractionalQuantity());
                    itemAllocation = itemAllocationRepository.save(itemAllocation);
                }

            } else {
                ItemInstance itemInstance = itemInstanceRepository.save(issueItemDto.getItemInstance());

                bomItemInstance.setItemInstance(itemInstance.getId());

                issueItem.setQuantity(1);

                bomItemInstance = bomItemInstanceRepository.save(bomItemInstance);

                issueItem.setBomItemInstance(bomItemInstance);
                issueItem.setRequestItem(issueItemDto.getRequestItem().getId());

                issueItem = issueItemRepository.save(issueItem);

                RequestItem requestItem = requestItemRepository.findOne(issueItem.getRequestItem());
                ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceItem(requestItem.getItem().getId());

                if (itemAllocation != null) {
                    itemAllocation.setIssueProcessQty(itemAllocation.getIssueProcessQty() + issueItem.getQuantity());
                    itemAllocation = itemAllocationRepository.save(itemAllocation);
                }

            }
        }

        String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " has created new '" + issue.getNumber()
                + "' Issue for '" + issue.getRequest().getReqNumber() + "' Requested Items";
        drdoUpdatesService.updateMessage(message, DRDOObjectType.ISSUE);

        IssueHistory issueHistory = new IssueHistory();
        issueHistory.setIssue(issue.getId());
        issueHistory.setStatus(issue.getStatus());
        issueHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson());
        issueHistory.setUpdatedDate(new Date());

        issueHistory = issueHistoryRepository.save(issueHistory);

        return issue;
    }

    @Transactional(readOnly = false)
    public Issue createIssue(Issue issue) {

//        JSONParser jsonParser = new JSONParser();
//        String defaultAirframeType = null;
        /*try {

            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest();
            String url = request.getScheme()
                    + "://"
                    + request.getServerName()
                    + ":"
                    + request.getServerPort()
                    + "/application.json";

            InputStream is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);

            Object obj = jsonParser.parse(jsonText);

            JSONObject jsonObject = (JSONObject) obj;
            defaultAirframeType = jsonObject.get("AirframeType").toString();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        List<IssueItemDto> issueItemDtos = issue.getIssueItemDtos();

        BomInstanceItem bomInstanceItem = issueItemDtos.get(0).getRequestItem().getItem();
        String sectionCode = "";
        if (bomInstanceItem.getParent() != null) {
            BomInstanceItem parent = bomInstanceItemRepository.findOne(bomInstanceItem.getParent());

            if (parent != null && parent.getBomItemType().equals(BomItemType.UNIT)) {
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());

                if (parent1 != null && parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());

                    if (parent2 != null && (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART))) {
                        sectionCode = parent2.getTypeRef().getCode();
                    }
                } else if (parent1 != null && (parent1.getBomItemType().equals(BomItemType.SECTION) || parent1.getBomItemType().equals(BomItemType.COMMONPART))) {
                    sectionCode = parent1.getTypeRef().getCode();
                }
            } else if (parent != null && parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());

                if (parent1 != null && (parent1.getBomItemType().equals(BomItemType.SECTION) || parent1.getBomItemType().equals(BomItemType.COMMONPART))) {
                    sectionCode = parent1.getTypeRef().getCode();
                }
            } else if (parent != null && parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                sectionCode = parent.getTypeRef().getCode();
            }
        }

        AutoNumber autoNumber = autoNumberRepository.findByName("Default Issue Number Source");

        String number = autoNumber.next();

        autoNumber = autoNumberRepository.save(autoNumber);

        Bom system = bomRepository.findOne(issue.getRequest().getBomInstance().getBom());

        issue.setNumber(system.getItem().getItemMaster().getItemCode() + "_" + issue.getRequest().getBomInstance().getItem().getInstanceName() + "_" + sectionCode + "" + number);

        issue.setBomInstance(issue.getRequest().getBomInstance());
        issue.setIssuedTo(issue.getRequest().getRequestedBy());
        issue = issueRepository.save(issue);


        /*HashMap<Integer, BomInstanceItem> airframeMap = new HashMap<>();
        BomInstanceItem airframeItem = null;

        for (IssueItemDto issueItemDto : issueItemDtos) {

            ItemType airframeType = null;
            BomInstanceItem section = null;
            BomInstanceItem parent = bomInstanceItemRepository.findOne(issueItemDto.getRequestItem().getItem().getParent());
            if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent.getParent());
                if (parent2.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomInstanceItem parent3 = bomInstanceItemRepository.findOne(parent2.getParent());
                    if (parent3.getBomItemType().equals(BomItemType.SECTION)) {
                        section = parent3;
                    }
                } else if (parent2.getBomItemType().equals(BomItemType.SECTION)) {
                    section = parent2;
                }
            } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent.getParent());
                if (parent2.getBomItemType().equals(BomItemType.SECTION)) {
                    section = parent2;
                }
            }

            if (section != null) {

                airframeType = itemTypeRepository.findByName(defaultAirframeType);

                if (airframeType != null) {
                    ItemType parentType = itemTypeRepository.findOne(issueItemDto.getRequestItem().getItem().getItem().getItemMaster().getItemType().getParentType());
                    if (parentType != null && !parentType.getParentNode() && parentType.getId().equals(airframeType.getId())) {
                        airframeItem = issueItemDto.getRequestItem().getItem();
                        airframeItem.setSection(section.getTypeRef());
                        airframeMap.put(airframeItem.getId(), airframeItem);
                    }
                }
            }
        }*/


        for (IssueItemDto issueItemDto : issueItemDtos) {

            IssueItem issueItem = new IssueItem();

            issueItem.setIssue(issue);

            BomItemInstance bomItemInstance = new BomItemInstance();
            bomItemInstance.setBomInstanceItem(issueItemDto.getRequestItem().getItem().getId());

            Storage presentStorage = storageRepository.findOne(issueItemDto.getItemInstance().getStorage().getId());

            /*BomInstanceItem section = null;

            BomInstanceItem parent = bomInstanceItemRepository.findOne(issueItemDto.getRequestItem().getItem().getParent());

            if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent.getParent());
                if (parent2.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomInstanceItem parent3 = bomInstanceItemRepository.findOne(parent2.getParent());
                    if (parent3.getBomItemType().equals(BomItemType.SECTION)) {
                        section = parent3;
                    }
                } else if (parent2.getBomItemType().equals(BomItemType.SECTION)) {
                    section = parent2;
                }
            } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent.getParent());
                if (parent2.getBomItemType().equals(BomItemType.SECTION)) {
                    section = parent2;
                }
            }

            if (section != null) {
                BomInstanceItem airframe = null;

                ItemType airframeType = itemTypeRepository.findByName(defaultAirframeType);

                if (airframeType != null) {
                    List<BomInstanceItem> children = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(section.getId());

                    for (BomInstanceItem child : children) {
                        if (child.getBomItemType().equals(BomItemType.PART)) {
                            ItemType parentType = itemTypeRepository.findOne(child.getItem().getItemMaster().getItemType().getParentType());
                            if (parentType != null && !parentType.getParentNode() && parentType.getId().equals(airframeType.getId())) {
                                airframe = child;
                            }
                        } else {
                            airframe = getAirframeItem(child, airframe, airframeType);
                        }
                    }


                    if (airframe != null) {
                        BomInstanceItem issueContainsAirframeItem = airframeMap.get(airframe.getId());

                        if (issueContainsAirframeItem == null) {
                            ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceItem(airframe.getId());

                            if (itemAllocation != null) {
                                if (airframe.getItem().getItemMaster().getItemType().getHasLots()) {
                                    if (itemAllocation.getIssuedQty().equals(airframe.getFractionalQuantity())) {

                                    } else {
                                        throw new CassiniException("Airframe not issued to " + section.getTypeRef().getName());
                                    }
                                } else {
                                    if (itemAllocation.getIssuedQty().equals(airframe.getQuantity().doubleValue())) {

                                    } else {
                                        throw new CassiniException("Airframe not issued to " + section.getTypeRef().getName());
                                    }
                                }

                            } else {
                                throw new CassiniException("Airframe not issued to " + section.getTypeRef().getName());
                            }
                        }

                    } else {
                        throw new CassiniException("Airframe not issued to " + section.getTypeRef().getName());
                    }
                }
            }*/

            if (issueItemDto.getRequestItem().getItem().getItem().getItemMaster().getItemType().getHasLots()) {

                bomItemInstance.setItemInstance(issueItemDto.getItemInstance().getId());

                bomItemInstance = bomItemInstanceRepository.save(bomItemInstance);

                if (issueItemDto.getRequestItem().getFractionalQuantity().equals(issueItemDto.getAllocateQty())) {
                    issueItem.setFractionalQuantity(issueItemDto.getAllocateQty());
                } else if (issueItemDto.getRequestItem().getFractionalQuantity() > issueItemDto.getAllocateQty()) {
                    issueItem.setFractionalQuantity(issueItemDto.getAllocateQty());
                } else if (issueItemDto.getRequestItem().getFractionalQuantity() < issueItemDto.getAllocateQty()) {
                    issueItem.setFractionalQuantity(issueItemDto.getRequestItem().getFractionalQuantity());
                }


                issueItem.setBomItemInstance(bomItemInstance);
                issueItem.setRequestItem(issueItemDto.getRequestItem().getId());

                issueItem = issueItemRepository.save(issueItem);

                LotInstance lotInstance = new LotInstance();
                lotInstance.setInstance(issueItemDto.getItemInstance().getId());
                lotInstance.setUpnNumber(issueItemDto.getScanUpn());
                lotInstance.setStatus(ItemInstanceStatus.ISSUE);
                lotInstance.setPresentStatus(ItemInstanceStatus.ISSUE.toString());
                lotInstance.setLotQty(issueItem.getFractionalQuantity());
                lotInstance.setIssueItem(issueItem.getId());
                lotInstance = lotInstanceRepository.save(lotInstance);

                LotInstanceHistory lotInstanceHistory = new LotInstanceHistory();
                lotInstanceHistory.setStatus(ItemInstanceStatus.ISSUE);
                lotInstanceHistory.setLotInstance(lotInstance.getId());
                lotInstanceHistory.setTimestamp(new Date());
                lotInstanceHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

                lotInstanceHistory = lotInstanceHistoryRepository.save(lotInstanceHistory);

                presentStorage.setRemainingCapacity(presentStorage.getRemainingCapacity() + lotInstance.getLotQty());
                presentStorage = storageRepository.save(presentStorage);

                ItemInstance itemInstance = itemInstanceRepository.findOne(lotInstance.getInstance());

                itemInstance = itemInstanceRepository.save(itemInstance);

                Bom bom = bomRepository.findOne(issue.getRequest().getBomInstance().getBom());
                Inventory inventory = null;
                if (itemInstance.getSection() != null) {
                    inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(bom.getId(), issueItemDto.getRequestItem().getItem().getItem().getId(), issueItemDto.getRequestItem().getItem().getUniqueCode(), itemInstance.getSection());
                    if (inventory != null) {
                        inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() - lotInstance.getLotQty());
                        inventory.setFractionalQtyIssued(inventory.getFractionalQtyIssued() + lotInstance.getLotQty());

                        inventory = inventoryRepository.save(inventory);
                    }
                } else {
                    inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bom.getId(), issueItemDto.getRequestItem().getItem().getId(), issueItemDto.getRequestItem().getItem().getUniqueCode());
                    if (inventory != null) {
                        inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() - lotInstance.getLotQty());
                        inventory.setFractionalQtyIssued(inventory.getFractionalQtyIssued() + lotInstance.getLotQty());

                        inventory = inventoryRepository.save(inventory);
                    }
                }

                ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceItem(issueItemDto.getRequestItem().getItem().getId());

                if (itemAllocation != null) {
                    itemAllocation.setIssuedQty(itemAllocation.getIssuedQty() + lotInstance.getLotQty());
                    itemAllocation = itemAllocationRepository.save(itemAllocation);
                }

            } else {
                issueItemDto.getItemInstance().setStorage(null);
                issueItemDto.getItemInstance().setStatus(ItemInstanceStatus.ISSUE);
                issueItemDto.getItemInstance().setPresentStatus(ItemInstanceStatus.ISSUE.toString());

                ItemInstance itemInstance = itemInstanceRepository.save(issueItemDto.getItemInstance());

                bomItemInstance.setItemInstance(itemInstance.getId());

                issueItem.setQuantity(1);

                ItemInstanceStatusHistory itemInstanceStatusHistory = new ItemInstanceStatusHistory();
                itemInstanceStatusHistory.setStatus(ItemInstanceStatus.ISSUE);
                itemInstanceStatusHistory.setPresentStatus(ItemInstanceStatus.ISSUE.toString());
                itemInstanceStatusHistory.setItemInstance(itemInstance);
                itemInstanceStatusHistory.setTimestamp(new Date());
                itemInstanceStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

                itemInstanceStatusHistory = itemInstanceStatusHistoryRepository.save(itemInstanceStatusHistory);

                bomItemInstance = bomItemInstanceRepository.save(bomItemInstance);

                issueItem.setBomItemInstance(bomItemInstance);
                issueItem.setRequestItem(issueItemDto.getRequestItem().getId());

                issueItem = issueItemRepository.save(issueItem);

                Bom bom = bomRepository.findOne(issue.getRequest().getBomInstance().getBom());

                Inventory inventory = null;
                if (itemInstance.getSection() != null) {
                    inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(bom.getId(), issueItemDto.getRequestItem().getItem().getItem().getId(), issueItemDto.getRequestItem().getItem().getUniqueCode(), itemInstance.getSection());
                    if (inventory != null) {
                        inventory.setQuantity(inventory.getQuantity() - 1);
                        inventory.setQtyIssued(inventory.getQtyIssued() + 1);

                        inventory = inventoryRepository.save(inventory);
                    }
                } else {
                    inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bom.getId(), issueItemDto.getRequestItem().getItem().getItem().getId(), issueItemDto.getRequestItem().getItem().getUniqueCode());
                    if (inventory != null) {
                        inventory.setQuantity(inventory.getQuantity() - 1);
                        inventory.setQtyIssued(inventory.getQtyIssued() + 1);

                        inventory = inventoryRepository.save(inventory);
                    }
                }

                ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceItem(issueItemDto.getRequestItem().getItem().getId());

                if (itemAllocation != null) {
                    itemAllocation.setIssuedQty(itemAllocation.getIssuedQty() + 1);
                    presentStorage.setRemainingCapacity(presentStorage.getRemainingCapacity() + 1);
                    presentStorage = storageRepository.save(presentStorage);

                    itemAllocation = itemAllocationRepository.save(itemAllocation);
                }
            }
        }

        Request request = requestRepository.findOne(issue.getRequest().getId());
        List<RequestItem> requestItems = requestItemRepository.findByRequest(request);

        Boolean allIssued = true;

        for (RequestItem requestItem : requestItems) {
            List<IssueItem> issueItems = issueItemRepository.findByRequestItem(requestItem.getId());

            if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                Double issuedQuantity = 0.0;
                for (IssueItem issueItem : issueItems) {
                    issuedQuantity = issuedQuantity + issueItem.getFractionalQuantity();
                }

                if (!requestItem.getFractionalQuantity().equals(issuedQuantity)) {
                    allIssued = false;
                }
            } else {
                if (!requestItem.getQuantity().equals(issueItems.size())) {
                    allIssued = false;
                }
            }
        }

        if (allIssued) {
            request.setIssued(true);

            request = requestRepository.save(request);
        }

        return issue;
    }

    private BomInstanceItem getAirframeItem(BomInstanceItem bomInstanceItem, BomInstanceItem airframe, ItemType airframeType) {

        List<BomInstanceItem> children = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomInstanceItem.getId());

        for (BomInstanceItem child : children) {
            if (child.getBomItemType().equals(BomItemType.PART)) {
                ItemType parentType = itemTypeRepository.findOne(child.getItem().getItemMaster().getItemType().getParentType());
                if (parentType != null && !parentType.getParentNode() && parentType.getId().equals(airframeType.getId())) {
                    airframe = child;
                }
            } else {
                airframe = getAirframeItem(child, airframe, airframeType);
            }
        }

        return airframe;
    }

    @Transactional(readOnly = true)
    public IssueDto getIssueDetails(Integer issueId) {


        Issue issue = issueRepository.findOne(issueId);
        List<IssueItem> issueItems = issueItemRepository.findByIssue(issue);

        List<IssueDetailsDto> detailsDtos = new ArrayList<>();
        HashMap<Integer, IssuedItemsDto> sectionGroupMap = new HashMap<>();
        HashMap<Integer, ReceiveDto> receiveDtoHashMap = new HashMap<>();

        HashMap<Integer, Integer> multipleQuantityMap = new HashMap<>();

        for (IssueItem issueItem : issueItems) {
            IssueDetailsDto issueDetailsDto = new IssueDetailsDto();

            issueDetailsDto.setIssueItem(issueItem);
            issueDetailsDto.setRequestItem(requestItemRepository.findOne(issueItem.getRequestItem()));
            issueDetailsDto.setBomItemInstance(bomItemInstanceRepository.findOne(issueItem.getBomItemInstance().getId()));
            issueDetailsDto.setItemInstance(itemInstanceRepository.findOne(issueDetailsDto.getBomItemInstance().getItemInstance()));

            ItemType itemType = itemTypeRepository.findOne(issueDetailsDto.getItemInstance().getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                issueDetailsDto.getItemInstance().getItem().getItemMaster().setParentType(itemType);
            } else {
                issueDetailsDto.getItemInstance().getItem().getItemMaster().setParentType(issueDetailsDto.getItemInstance().getItem().getItemMaster().getItemType());
            }

            if (issueDetailsDto.getRequestItem().getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                issueDetailsDto.setLotInstance(lotInstanceRepository.findByInstanceAndIssueItem(issueItem.getBomItemInstance().getItemInstance(), issueItem.getId()));
            }

            ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

            if (certificateNumberType != null) {
                ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(issueDetailsDto.getItemInstance().getId(), certificateNumberType.getId());

                if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                    issueDetailsDto.getItemInstance().setCertificateNumber(certificateNumber.getStringValue());
                }
            }

            BomInstanceItem parent = bomInstanceItemRepository.findOne(issueDetailsDto.getRequestItem().getItem().getParent());
            BomGroup section = null;
            if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent.getParent());
                if (parent2.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomInstanceItem parent3 = bomInstanceItemRepository.findOne(parent2.getParent());
                    if (parent3.getBomItemType().equals(BomItemType.SECTION) || parent3.getBomItemType().equals(BomItemType.COMMONPART)) {
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

            if (issueItem.getStatus() != null && issueItem.getStatus().equals(IssueItemStatus.P_APPROVED)) {
                List<ItemInstanceStatusHistory> itemInstanceStatusHistories = instanceStatusHistoryRepository.getInstanceHistoryByInstanceAndStatus(issueDetailsDto.getItemInstance().getId(), ItemInstanceStatus.P_APPROVED);
                if (itemInstanceStatusHistories.size() > 0) {
                    issueDetailsDto.setReason(itemInstanceStatusHistories.get(0).getComment());
                }
            }

            if (issueItem.getStatus() != null && issueItem.getStatus().equals(IssueItemStatus.REJECTED)) {
                List<ItemInstanceStatusHistory> itemInstanceStatusHistories = instanceStatusHistoryRepository.getInstanceHistoryByInstanceAndStatus(issueDetailsDto.getItemInstance().getId(), ItemInstanceStatus.P_APPROVED);
                if (itemInstanceStatusHistories.size() > 0) {
                    issueDetailsDto.setReason(itemInstanceStatusHistories.get(0).getComment());
                }
            }

            multipleQuantityMap.put(issueItem.getRequestItem(), issueItem.getRequestItem());

            if (section != null) {
                IssuedItemsDto issuedItemsDto = sectionGroupMap.containsKey(section.getId()) ? sectionGroupMap.get(section.getId()) : new IssuedItemsDto();

                issuedItemsDto.setSection(section);
                issuedItemsDto.getIssuedItems().add(issueDetailsDto);

                sectionGroupMap.put(section.getId(), issuedItemsDto);

                BDLReceiveItem receiveItem = bdlReceiveItemRepository.findByIssueItem(issueItem.getId());
                if (receiveItem != null) {
                    BDLReceive bdlReceive = bdlReceiveRepository.findOne(receiveItem.getReceive());

                    ReceiveDto receiveDto = receiveDtoHashMap.containsKey(bdlReceive.getId()) ? receiveDtoHashMap.get(bdlReceive.getId()) : new ReceiveDto();

                    receiveDto.setReceive(bdlReceive);
                    receiveDto.getIssueItems().add(issueDetailsDto);
                    receiveDto.setReceivedBy(personRepository.findOne(bdlReceive.getCreatedBy()));

                    receiveDtoHashMap.put(bdlReceive.getId(), receiveDto);
                }
            }

//            detailsDtos.add(issueDetailsDto);
        }

        IssueDto issueDto = new IssueDto();

        List<IssuedItemsDto> issuedItemsDtoList = new ArrayList<>();
        List<ReceiveDto> receiveDtoList = new ArrayList<>();

        for (IssuedItemsDto issuedItemsDto : sectionGroupMap.values()) {
            issuedItemsDtoList.add(issuedItemsDto);
        }

        HashMap<Integer, HashMap<BomInstanceItem, ReceiveItemDto>> receiveItemDtoHashMap = new HashMap<>();

        for (Integer requestItemId : multipleQuantityMap.values()) {
            List<IssueItem> issueItems1 = issueItemRepository.findByIssueAndRequestItem(issue, requestItemId);
            RequestItem requestItem = requestItemRepository.findOne(requestItemId);
            for (IssueItem issueItem : issueItems1) {
                BDLReceiveItem receiveItem = bdlReceiveItemRepository.findByIssueItem(issueItem.getId());
                if (receiveItem != null) {
                    BDLReceive bdlReceive = bdlReceiveRepository.findOne(receiveItem.getReceive());

                    HashMap<BomInstanceItem, ReceiveItemDto> bomInstanceItemReceiveItemDtoHashMap = receiveItemDtoHashMap.containsKey(bdlReceive.getId()) ? receiveItemDtoHashMap.get(bdlReceive.getId()) : new HashMap<>();
                    ReceiveItemDto receiveItemDto = bomInstanceItemReceiveItemDtoHashMap.containsKey(requestItem.getItem()) ? bomInstanceItemReceiveItemDtoHashMap.get(requestItem.getItem()) : new ReceiveItemDto();

                    receiveItemDto.setItem(requestItem.getItem());
                    if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                        receiveItemDto.setIssuedQuantity(receiveItemDto.getIssuedQuantity() + issueItem.getFractionalQuantity());

                        LotInstance lotInstance = lotInstanceRepository.findByInstanceAndIssueItem(issueItem.getBomItemInstance().getItemInstance(), issueItem.getId());
                        if (lotInstance != null) {
                            lotInstance.setItemInstance(itemInstanceRepository.findOne(lotInstance.getInstance()));

                            ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                            if (certificateNumberType != null) {
                                ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(lotInstance.getItemInstance().getId(), certificateNumberType.getId());

                                if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                                    lotInstance.getItemInstance().setCertificateNumber(certificateNumber.getStringValue());
                                    lotInstance.setCertificateNumber(certificateNumber.getStringValue());
                                }
                            }

                            receiveItemDto.getLotInstances().add(lotInstance);
                        }
                    } else {
                        receiveItemDto.setIssuedQuantity(receiveItemDto.getIssuedQuantity() + issueItem.getQuantity().doubleValue());

                        receiveItemDto.getItemInstances().add(itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance()));
                    }

                    bomInstanceItemReceiveItemDtoHashMap.put(requestItem.getItem(), receiveItemDto);

                    receiveItemDtoHashMap.put(bdlReceive.getId(), bomInstanceItemReceiveItemDtoHashMap);
                }
            }
        }

        for (ReceiveDto receiveDto : receiveDtoHashMap.values()) {

            HashMap<BomInstanceItem, ReceiveItemDto> instanceItemReceiveItemDtoHashMap = receiveItemDtoHashMap.get(receiveDto.getReceive().getId());

            for (ReceiveItemDto receiveItemDto : instanceItemReceiveItemDtoHashMap.values()) {

                HashMap<String, String> cerificateMap = new LinkedHashMap<>();

                if (receiveItemDto.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                    receiveItemDto.getLotInstances().forEach(lotInstance -> {
                        cerificateMap.put(lotInstance.getCertificateNumber(), lotInstance.getCertificateNumber());
                    });
                } else {
                    receiveItemDto.getItemInstances().forEach(itemInstance -> {
                        cerificateMap.put(itemInstance.getCertificateNumber(), itemInstance.getCertificateNumber());
                    });
                }

                for (String s : cerificateMap.keySet()) {
                    receiveItemDto.getCertificateNumbers().add(s);
                }

                receiveDto.getReceiveItems().add(receiveItemDto);
            }
            receiveDtoList.add(receiveDto);
        }

        if (issuedItemsDtoList.size() > 0) {
            Collections.sort(issuedItemsDtoList, new Comparator<IssuedItemsDto>() {
                public int compare(final IssuedItemsDto object1, final IssuedItemsDto object2) {
                    return object1.getSection().getName().compareTo(object2.getSection().getName());
                }
            });
        }

        issueDto.setIssuedItemsDtos(issuedItemsDtoList);
        issueDto.setReceiveDtoList(receiveDtoList);
        issueDto.getIssueHistories().addAll(issueHistoryRepository.findByIssueOrderByUpdatedDateDesc(issueId));
        return issueDto;
    }

    @Transactional(readOnly = false)
    public Issue approveIssue(Integer issueId, Issue issue) {

        Issue issue1 = issueRepository.findOne(issueId);

        List<IssueItem> issueItems = issueItemRepository.findByIssue(issue1);

        issueItems.forEach(issueItem -> {
            issueItem.setApproved(true);
            issueItem.setApprovedBy(sessionWrapper.getSession().getLogin().getPerson());

            issueItem = issueItemRepository.save(issueItem);
        });

        issue1.setModifiedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
        issue1.setModifiedDate(new Date());
        issue1.setStatus(IssueStatus.BDL_QC);
        issue1 = issueRepository.save(issue1);

        return issue1;
    }


    @Transactional(readOnly = false)
    public IssueItem approveItem(Integer issueId, Integer issueItemId, IssueItem issueItem) {

        Issue issue = issueRepository.findOne(issueId);

        issueItem = issueItemRepository.findOne(issueItemId);

        if (issueItem.getStatus().equals(IssueItemStatus.P_APPROVED)) {
            issueItem.setStatus(IssueItemStatus.APPROVED);

            issueItem = issueItemRepository.save(issueItem);
        }

        issue.setModifiedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
        issue.setModifiedDate(new Date());
        issue = issueRepository.save(issue);

        ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());
        if (!itemInstance.getStatus().equals(ItemInstanceStatus.ISSUE) && !itemInstance.getStatus().equals(ItemInstanceStatus.FAILURE)) {
            itemInstance.setStatus(ItemInstanceStatus.APPROVED);
            itemInstance.setPresentStatus("APPROVED");
        }
        itemInstance.setProvisionalAccept(false);
        itemInstance = itemInstanceRepository.save(itemInstance);

        ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
        statusHistory.setItemInstance(itemInstance);
        statusHistory.setStatus(ItemInstanceStatus.APPROVED);
        statusHistory.setPresentStatus("APPROVED");
        statusHistory.setTimestamp(new Date());
        statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        statusHistory = instanceStatusHistoryRepository.save(statusHistory);

        RequestItem requestItem = requestItemRepository.findOne(issueItem.getRequestItem());
        BomInstanceItem section = getSectionName(requestItem.getItem());
        Bom bom = bomRepository.findOne(issue.getRequest().getBomInstance().getBom());

        ItemInstance instance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());
        String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " BDL QC has Approved provisionally Approved '" + instance.getItem().getItemMaster().getItemName()
                + "' Item from '" + issue.getNumber() + " Issue belongs to '" + section.getTypeRef().getName() + "' Section of " + bom.getItem().getItemMaster().getItemName() + "_"
                + issue.getRequest().getBomInstance().getItem().getInstanceName();
        drdoUpdatesService.updateMessage(message, DRDOObjectType.ISSUE);

        return issueItem;
    }


    @Transactional(readOnly = false)
    public IssueItem approveIssueItem(Integer issueId, Integer issueItemId, IssueItem issueItem) {

        Issue issue = issueRepository.findOne(issueId);

        issueItem = issueItemRepository.findOne(issueItemId);
        issueItem.setStatus(IssueItemStatus.APPROVED);
        issueItem.setApprovedBy(sessionWrapper.getSession().getLogin().getPerson());
        issueItem.setApprovedDate(new Date());
        issueItem = issueItemRepository.save(issueItem);

        issue.setModifiedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
        issue.setModifiedDate(new Date());
        issue = issueRepository.save(issue);

        ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());
        itemInstance.setStatus(ItemInstanceStatus.APPROVED);
        itemInstance.setPresentStatus("APPROVED");
        itemInstance = itemInstanceRepository.save(itemInstance);

        ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
        statusHistory.setItemInstance(itemInstance);
        statusHistory.setStatus(ItemInstanceStatus.APPROVED);
        statusHistory.setPresentStatus("APPROVED");
        statusHistory.setTimestamp(new Date());
        statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        statusHistory = instanceStatusHistoryRepository.save(statusHistory);

        List<IssueItem> issueItems = issueItemRepository.findByIssue(issue);

        List<IssueItem> approvedItems = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.APPROVED);
        List<IssueItem> pendingItems = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.PENDING);
        List<IssueItem> partiallyApprovedItems = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.P_APPROVED);
        List<IssueItem> rejectedItems = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.REJECTED);
        List<IssueItem> receivedItems = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.RECEIVED);

        if (issueItems.size() == (approvedItems.size() + partiallyApprovedItems.size() + rejectedItems.size() + receivedItems.size())) {
            if (issue.getVersity()) {
                issue.setStatus(IssueStatus.VERSITY_PPC);
            } else {
                issue.setStatus(IssueStatus.BDL_PPC);
            }
            issue = issueRepository.save(issue);

            IssueHistory issueHistory = new IssueHistory();
            issueHistory.setIssue(issue.getId());
            issueHistory.setStatus(issue.getStatus());
            issueHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson());
            issueHistory.setUpdatedDate(new Date());

            issueHistory = issueHistoryRepository.save(issueHistory);
        } else {
            if (pendingItems.size() > 0) {
                issue.setStatus(IssueStatus.PARTIALLY_APPROVED);

                issue = issueRepository.save(issue);

                IssueHistory issueHistory = new IssueHistory();
                issueHistory.setIssue(issue.getId());
                issueHistory.setStatus(IssueStatus.PARTIALLY_APPROVED);
                issueHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson());
                issueHistory.setUpdatedDate(new Date());

                issueHistory = issueHistoryRepository.save(issueHistory);
            }
        }

        RequestItem requestItem = requestItemRepository.findOne(issueItem.getRequestItem());
        BomInstanceItem section = getSectionName(requestItem.getItem());
        Bom bom = bomRepository.findOne(issue.getRequest().getBomInstance().getBom());

        ItemInstance instance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());
        String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " BDL QC has Approved '" + instance.getItem().getItemMaster().getItemName()
                + "' Item from '" + issue.getNumber() + " Issue belongs to '" + section.getTypeRef().getName() + "' Section of " + bom.getItem().getItemMaster().getItemName() + "_"
                + issue.getRequest().getBomInstance().getItem().getInstanceName();
        drdoUpdatesService.updateMessage(message, DRDOObjectType.ISSUE);

        return issueItem;
    }

    private BomInstanceItem getSectionName(BomInstanceItem bomInstanceItem) {
        BomInstanceItem section = null;
        BomInstanceItem parent = bomInstanceItemRepository.findOne(bomInstanceItem.getParent());

        if (parent.getBomItemType().equals(BomItemType.UNIT)) {
            BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
            if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                if (parent2 != null && (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART))) {
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
    public IssueDetailsDto partiallyApproveIssueItem(Integer issueId, Integer issueItemId, IssueDetailsDto issueDetailsDto) {

        Issue issue = issueRepository.findOne(issueId);

        IssueItem issueItem = issueItemRepository.findOne(issueDetailsDto.getIssueItem().getId());

        issueItem.setStatus(IssueItemStatus.P_APPROVED);
        issueItem.setApprovedBy(sessionWrapper.getSession().getLogin().getPerson());
        issueItem.setApprovedDate(new Date());
        issueItem = issueItemRepository.save(issueItem);

        ItemInstance itemInstance = issueDetailsDto.getItemInstance();

        itemInstance.setStatus(ItemInstanceStatus.P_APPROVED);
        itemInstance.setProvisionalAccept(true);
        itemInstance.setPresentStatus("PROVISIONAL APPROVED");
        itemInstance = itemInstanceRepository.save(itemInstance);

        ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
        statusHistory.setItemInstance(itemInstance);
        statusHistory.setStatus(ItemInstanceStatus.P_APPROVED);
        statusHistory.setPresentStatus("PROVISIONAL APPROVED");
        statusHistory.setTimestamp(new Date());
        statusHistory.setComment(itemInstance.getReason());
        statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        statusHistory = instanceStatusHistoryRepository.save(statusHistory);

        issue.setModifiedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
        issue.setModifiedDate(new Date());
        issue = issueRepository.save(issue);

        List<IssueItem> issueItems = issueItemRepository.findByIssue(issue);

        List<IssueItem> approvedItems = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.APPROVED);
        List<IssueItem> partiallyApprovedItems = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.P_APPROVED);
        List<IssueItem> rejectedItems = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.REJECTED);
        List<IssueItem> pendingItems = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.PENDING);
        List<IssueItem> receivedItems = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.RECEIVED);

        if (issueItems.size() == (approvedItems.size() + partiallyApprovedItems.size() + rejectedItems.size() + receivedItems.size())) {
            if (issue.getVersity()) {
                issue.setStatus(IssueStatus.VERSITY_PPC);
            } else {
                issue.setStatus(IssueStatus.BDL_PPC);
            }

            issue = issueRepository.save(issue);

            IssueHistory issueHistory = new IssueHistory();
            issueHistory.setIssue(issue.getId());
            issueHistory.setStatus(issue.getStatus());
            issueHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson());
            issueHistory.setUpdatedDate(new Date());

            issueHistory = issueHistoryRepository.save(issueHistory);
        } else {
            if (pendingItems.size() > 0) {
                issue.setStatus(IssueStatus.PARTIALLY_APPROVED);

                issue = issueRepository.save(issue);

                IssueHistory issueHistory = new IssueHistory();
                issueHistory.setIssue(issue.getId());
                issueHistory.setStatus(IssueStatus.PARTIALLY_APPROVED);
                issueHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson());
                issueHistory.setUpdatedDate(new Date());

                issueHistory = issueHistoryRepository.save(issueHistory);
            }
        }

        RequestItem requestItem = requestItemRepository.findOne(issueItem.getRequestItem());
        BomInstanceItem section = getSectionName(requestItem.getItem());
        Bom bom = bomRepository.findOne(issue.getRequest().getBomInstance().getBom());

        ItemInstance instance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());
        String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " BDL QC has Provisionally Approved '" + instance.getItem().getItemMaster().getItemName()
                + "' Item from '" + issue.getNumber() + " Issue belongs to '" + section.getTypeRef().getName() + "' Section of " + bom.getItem().getItemMaster().getItemName() + "_"
                + issue.getRequest().getBomInstance().getItem().getInstanceName();
        drdoUpdatesService.updateMessage(message, DRDOObjectType.ISSUE);

        return issueDetailsDto;
    }

    @Transactional(readOnly = false)
    public IssueDetailsDto rejectIssueItem(Integer issueId, Integer issueItemId, IssueDetailsDto issueDetailsDto) {

        Issue issue = issueRepository.findOne(issueId);

        IssueItem issueItem = issueItemRepository.findOne(issueDetailsDto.getIssueItem().getId());
        issueItem.setStatus(IssueItemStatus.REJECTED);
        issueItem.setApprovedBy(sessionWrapper.getSession().getLogin().getPerson());
        issueItem.setApprovedDate(new Date());
        issueItem = issueItemRepository.save(issueItem);

        ItemInstance itemInstance = issueDetailsDto.getItemInstance();

        itemInstance.setStatus(ItemInstanceStatus.REJECTED);
        itemInstance.setPresentStatus(ItemInstanceStatus.REJECTED.toString());
        itemInstance = itemInstanceRepository.save(itemInstance);

        ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
        statusHistory.setItemInstance(itemInstance);
        statusHistory.setStatus(ItemInstanceStatus.REJECTED);
        statusHistory.setPresentStatus("REJECTED");
        statusHistory.setTimestamp(new Date());
        statusHistory.setComment(itemInstance.getReason());
        statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        statusHistory = instanceStatusHistoryRepository.save(statusHistory);

        List<IssueItem> issueItemList = issueItemRepository.findByIssue(issue);
        List<IssueItem> rejectedIssueItemList = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.REJECTED);

        IssueHistory issueHistory = new IssueHistory();
        issueHistory.setIssue(issue.getId());
        if (issueItemList.size() == rejectedIssueItemList.size()) {
            issueHistory.setStatus(IssueStatus.REJECTED);
        } else {
            issueHistory.setStatus(IssueStatus.PARTIALLY_REJECTED);
        }
        issueHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson());
        issueHistory.setUpdatedDate(new Date());

        issueHistory = issueHistoryRepository.save(issueHistory);

        if (issueItemList.size() == rejectedIssueItemList.size()) {
            issue.setStatus(IssueStatus.REJECTED);
        }

        issue.setModifiedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
        issue.setModifiedDate(new Date());
        issue = issueRepository.save(issue);

        BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(issueItem.getBomItemInstance().getBomInstanceItem());

        ItemInstance issuedItemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());

        if (issuedItemInstance.getSection() != null) {
            Inventory inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(issue.getRequest().getBomInstance().getBom(), issuedItemInstance.getItem().getId(), bomInstanceItem.getUniqueCode(), issuedItemInstance.getSection());
            if (inventory != null) {
                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                    inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() - issueItem.getFractionalQuantity());
                    inventory = inventoryRepository.save(inventory);
                } else {
                    inventory.setQuantity(inventory.getQuantity() - issueItem.getQuantity());
                    inventory = inventoryRepository.save(inventory);
                }
            }
        } else {
            Inventory inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(issue.getRequest().getBomInstance().getBom(), issuedItemInstance.getItem().getId(), bomInstanceItem.getUniqueCode());
            if (inventory != null) {
                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                    inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() - issueItem.getFractionalQuantity());
                    inventory = inventoryRepository.save(inventory);
                } else {
                    inventory.setQuantity(inventory.getQuantity() - issueItem.getQuantity());
                    inventory = inventoryRepository.save(inventory);
                }
            }
        }

        Bom bom = bomRepository.findOne(issue.getRequest().getBomInstance().getBom());
        List<IssueItem> issueItems = issueItemRepository.findByIssue(issue);

        List<IssueItem> rejectedItems = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.REJECTED);

        if (issueItems.size() == rejectedItems.size()) {
            issue.setStatus(IssueStatus.REJECTED);

            issue = issueRepository.save(issue);
        }

        RequestItem requestItem = requestItemRepository.findOne(issueItem.getRequestItem());
        BomInstanceItem section = getSectionName(requestItem.getItem());

        ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceItem(requestItem.getItem().getId());

        if (itemAllocation != null) {
            if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                itemAllocation.setIssueProcessQty(itemAllocation.getIssueProcessQty() - issueItem.getFractionalQuantity());
            } else {
                itemAllocation.setIssueProcessQty(itemAllocation.getIssueProcessQty() - issueItem.getQuantity());
            }
            itemAllocation = itemAllocationRepository.save(itemAllocation);
        }

        ItemInstance instance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());
        String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " BDL QC has Rejected '" + instance.getItem().getItemMaster().getItemName()
                + "' Item from '" + issue.getNumber() + " Issue belongs to '" + section.getTypeRef().getName() + "' Section of " + bom.getItem().getItemMaster().getItemName() + "_"
                + issue.getRequest().getBomInstance().getItem().getInstanceName() + " due to '" + itemInstance.getReason() + "'";
        drdoUpdatesService.updateMessage(message, DRDOObjectType.ISSUE);

        return issueDetailsDto;
    }

    @Transactional(readOnly = true)
    public IssueItem checkRejectedItemAlreadyIssued(Integer issueId, Integer issueItemId, IssueDetailsDto issueDetailsDto) {

        Issue issue = issueRepository.findOne(issueId);

        IssueItem issueItem = issueItemRepository.findOne(issueDetailsDto.getIssueItem().getId());

        Integer bomQuantity = 0;
        Double fractionalBomQuantity = 0.0;

        BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(issueItem.getBomItemInstance().getBomInstanceItem());
        if (bomInstanceItem != null) {
            if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.findByBomInstanceItem(bomInstanceItem.getId());

                for (BomItemInstance bomItemInstance : bomItemInstances) {
                    List<IssueItem> issueItems = issueItemRepository.findByBomItemInstance(bomItemInstance);
                    for (IssueItem issueItem1 : issueItems) {
                        RequestItem requestItem = requestItemRepository.findOne(issueItem1.getRequestItem());
                        if (requestItem.getItem().getId().equals(bomInstanceItem.getId()) && !issueItem1.getStatus().equals(IssueItemStatus.REJECTED)) {
                            fractionalBomQuantity = fractionalBomQuantity + issueItem1.getFractionalQuantity();
                        }
                    }
                }

                if (bomInstanceItem.getFractionalQuantity().equals(fractionalBomQuantity)) {
                    return issueItem;
                } else {
                    if ((bomInstanceItem.getFractionalQuantity() - fractionalBomQuantity) < issueItem.getFractionalQuantity()) {
                        return issueItem;
                    } else {
                        return null;
                    }
                }

            } else {
                List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.findByBomInstanceItem(bomInstanceItem.getId());

                for (BomItemInstance bomItemInstance : bomItemInstances) {
                    IssueItem issueItem1 = issueItemRepository.getByBomItemInstance(bomItemInstance.getId());
                    if (!issueItem1.getStatus().equals(IssueItemStatus.REJECTED)) {
                        bomQuantity = bomQuantity + issueItem1.getQuantity();
                    }
                }

                if (bomInstanceItem.getQuantity().equals(bomQuantity)) {
                    return issueItem;
                } else {
                    if ((bomInstanceItem.getQuantity() - bomQuantity) < issueItem.getQuantity()) {
                        return issueItem;
                    } else {
                        return null;
                    }
                }
            }
        } else {
            return null;
        }
    }

    @Transactional(readOnly = false)
    public IssueDetailsDto resetIssueItem(Integer issueId, Integer issueItemId, IssueDetailsDto issueDetailsDto) {

        Issue issue = issueRepository.findOne(issueId);

        IssueItem issueItem = issueItemRepository.findOne(issueDetailsDto.getIssueItem().getId());

        issueItem.setStatus(IssueItemStatus.PENDING);
        issueItem.setApprovedBy(sessionWrapper.getSession().getLogin().getPerson());
        issueItem.setApprovedDate(new Date());
        issueItem = issueItemRepository.save(issueItem);

        ItemInstance itemInstance = issueDetailsDto.getItemInstance();

        itemInstance.setStatus(ItemInstanceStatus.INVENTORY);
        itemInstance.setPresentStatus(ItemInstanceStatus.INVENTORY.toString());
        itemInstance = itemInstanceRepository.save(itemInstance);

        ItemInstance issuedItemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());
        BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(issueItem.getBomItemInstance().getBomInstanceItem());

        if (issuedItemInstance.getSection() != null) {
            Inventory inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(issue.getRequest().getBomInstance().getBom(), issuedItemInstance.getItem().getId(), bomInstanceItem.getUniqueCode(), issuedItemInstance.getSection());
            if (inventory != null) {
                if (issuedItemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                    inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() + issueItem.getFractionalQuantity());
                    inventory = inventoryRepository.save(inventory);
                } else {
                    inventory.setQuantity(inventory.getQuantity() + issueItem.getQuantity());
                    inventory = inventoryRepository.save(inventory);
                }
            }
        } else {
            Inventory inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(issue.getRequest().getBomInstance().getBom(), issuedItemInstance.getItem().getId(), bomInstanceItem.getUniqueCode());
            if (inventory != null) {
                if (issuedItemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                    inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() + issueItem.getFractionalQuantity());
                    inventory = inventoryRepository.save(inventory);
                } else {
                    inventory.setQuantity(inventory.getQuantity() + issueItem.getQuantity());
                    inventory = inventoryRepository.save(inventory);
                }
            }
        }

        ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
        statusHistory.setItemInstance(itemInstance);
        statusHistory.setStatus(ItemInstanceStatus.RESET);
        statusHistory.setPresentStatus("RESET");
        statusHistory.setTimestamp(new Date());
        statusHistory.setComment(null);
        statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        statusHistory = instanceStatusHistoryRepository.save(statusHistory);


        IssueHistory issueHistory = new IssueHistory();
        issueHistory.setIssue(issue.getId());
        issueHistory.setStatus(IssueStatus.ITEM_RESET);
        issueHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson());
        issueHistory.setUpdatedDate(new Date());

        issueHistory = issueHistoryRepository.save(issueHistory);

        Bom bom = bomRepository.findOne(issue.getRequest().getBomInstance().getBom());
        issue.setStatus(IssueStatus.ITEM_RESET);

        issue.setModifiedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
        issue.setModifiedDate(new Date());
        issue = issueRepository.save(issue);

        RequestItem requestItem = requestItemRepository.findOne(issueItem.getRequestItem());
        BomInstanceItem section = getSectionName(requestItem.getItem());

        ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceItem(requestItem.getItem().getId());

        if (itemAllocation != null) {
            if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                itemAllocation.setIssueProcessQty(itemAllocation.getIssueProcessQty() + issueItem.getFractionalQuantity());
            } else {
                itemAllocation.setIssueProcessQty(itemAllocation.getIssueProcessQty() + issueItem.getQuantity());
            }
            itemAllocation = itemAllocationRepository.save(itemAllocation);
        }

        ItemInstance instance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());
        String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " Admin has Reset '" + instance.getItem().getItemMaster().getItemName()
                + "' Item from '" + issue.getNumber() + " Issue belongs to '" + section.getTypeRef().getName() + "' Section of " + bom.getItem().getItemMaster().getItemName() + "_"
                + issue.getRequest().getBomInstance().getItem().getInstanceName() + " Returned By BDL QC'";
        drdoUpdatesService.updateMessage(message, DRDOObjectType.ISSUE);

        return issueDetailsDto;
    }

    @Transactional(readOnly = false)
    public void addRejectedItemToInventory(Integer issueId, Integer issueItemId, IssueDetailsDto issueDetailsDto) {

        Issue issue = issueRepository.findOne(issueId);

        IssueItem issueItem = issueItemRepository.findOne(issueDetailsDto.getIssueItem().getId());

        BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(issueItem.getBomItemInstance().getBomInstanceItem());

        ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());

        itemInstance.setStatus(ItemInstanceStatus.INVENTORY);
        itemInstance.setPresentStatus(ItemInstanceStatus.INVENTORY.toString());
        itemInstance = itemInstanceRepository.save(itemInstance);

        if (itemInstance.getSection() != null) {
            Inventory inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(issue.getRequest().getBomInstance().getBom(), itemInstance.getItem().getId(), bomInstanceItem.getUniqueCode(), itemInstance.getSection());
            if (inventory != null) {
                if (itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                    inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() + issueItem.getFractionalQuantity());
                    inventory = inventoryRepository.save(inventory);
                } else {
                    inventory.setQuantity(inventory.getQuantity() + issueItem.getQuantity());
                    inventory = inventoryRepository.save(inventory);
                }
            }
        } else {
            Inventory inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(issue.getRequest().getBomInstance().getBom(), itemInstance.getItem().getId(), bomInstanceItem.getUniqueCode());
            if (inventory != null) {
                if (itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                    inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() + issueItem.getFractionalQuantity());
                    inventory = inventoryRepository.save(inventory);
                } else {
                    inventory.setQuantity(inventory.getQuantity() + issueItem.getQuantity());
                    inventory = inventoryRepository.save(inventory);
                }
            }
        }

        ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
        statusHistory.setItemInstance(itemInstance);
        statusHistory.setStatus(ItemInstanceStatus.RESET);
        statusHistory.setPresentStatus("RESET");
        statusHistory.setTimestamp(new Date());
        statusHistory.setComment(null);
        statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        statusHistory = instanceStatusHistoryRepository.save(statusHistory);


        IssueHistory issueHistory = new IssueHistory();
        issueHistory.setIssue(issue.getId());
        issueHistory.setStatus(IssueStatus.ITEM_RESET);
        issueHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson());
        issueHistory.setUpdatedDate(new Date());

        issueHistory = issueHistoryRepository.save(issueHistory);

        Integer bomItemInstanceId = issueItem.getBomItemInstance().getId();
        issueItemRepository.delete(issueItem.getId());
        bomItemInstanceRepository.delete(bomItemInstanceId);

    }

    @Transactional(readOnly = false)
    public Issue issueItems(Integer issueId) {

        Issue issue = issueRepository.findOne(issueId);

        List<IssueItem> issueItems = issueItemRepository.findByIssue(issue);

        for (IssueItem issueItem : issueItems) {
            if (issueItem.getStatus() != null && (issueItem.getStatus().equals(IssueItemStatus.P_APPROVED) || issueItem.getStatus().equals(IssueItemStatus.APPROVED))) {

                RequestItem requestItem = requestItemRepository.findOne(issueItem.getRequestItem());
                ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());
                Storage presentStorage = storageRepository.findOne(itemInstance.getStorage().getId());

                if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {

                    BomItemInstance bomItemInstance = bomItemInstanceRepository.findByItemInstanceAndBomInstanceItem(itemInstance.getId(), requestItem.getItem().getId());

                    BomInstance bomInstance = null;
                    BomInstanceItem bomItemSection = null;
                    BomInstanceItem bomItemSubSystem = null;
                    BomInstanceItem bomItemUnit = null;

                    if (requestItem.getItem() != null) {
                        BomInstanceItem parent = bomInstanceItemRepository.findOne(requestItem.getItem().getParent());
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
                    String missile = requestItem.getRequest().getBomInstance().getItem().getInstanceName();
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

                    if (requestItem.getItem() != null) {
                        part = requestItem.getItem().getItem().getItemMaster().getItemCode();
                    }

                    LotInstance lotInstance = new LotInstance();
                    lotInstance.setInstance(itemInstance.getId());
                    lotInstance.setUpnNumber(system + "" + missile + "" + section + "" + subSystem + "" + unit + "" + part);
                    lotInstance.setStatus(ItemInstanceStatus.ISSUE);
                    lotInstance.setPresentStatus(ItemInstanceStatus.ISSUE.toString());
                    lotInstance.setLotQty(issueItem.getFractionalQuantity());
                    lotInstance.setIssueItem(issueItem.getId());
                    lotInstance = lotInstanceRepository.save(lotInstance);

                    LotInstanceHistory lotInstanceHistory = new LotInstanceHistory();
                    lotInstanceHistory.setStatus(ItemInstanceStatus.ISSUE);
                    lotInstanceHistory.setLotInstance(lotInstance.getId());
                    lotInstanceHistory.setTimestamp(new Date());
                    lotInstanceHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

                    lotInstanceHistory = lotInstanceHistoryRepository.save(lotInstanceHistory);

                    presentStorage.setRemainingCapacity(presentStorage.getRemainingCapacity() + lotInstance.getLotQty());
                    presentStorage = storageRepository.save(presentStorage);

                    itemInstance = itemInstanceRepository.save(itemInstance);

                    Bom bom = bomRepository.findOne(issue.getRequest().getBomInstance().getBom());
                    Inventory inventory = null;
                    if (itemInstance.getSection() != null) {
                        inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(bom.getId(), requestItem.getItem().getItem().getId(), requestItem.getItem().getUniqueCode(), itemInstance.getSection());
                        if (inventory != null) {
                            inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() - lotInstance.getLotQty());
                            inventory.setFractionalQtyIssued(inventory.getFractionalQtyIssued() + lotInstance.getLotQty());

                            inventory = inventoryRepository.save(inventory);
                        }
                    } else {
                        inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bom.getId(), requestItem.getItem().getItem().getId(), requestItem.getItem().getUniqueCode());
                        if (inventory != null) {
                            inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() - lotInstance.getLotQty());
                            inventory.setFractionalQtyIssued(inventory.getFractionalQtyIssued() + lotInstance.getLotQty());

                            inventory = inventoryRepository.save(inventory);
                        }
                    }

                    ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceItem(requestItem.getItem().getId());

                    if (itemAllocation != null) {
                        itemAllocation.setIssuedQty(itemAllocation.getIssuedQty() + lotInstance.getLotQty());
                        itemAllocation = itemAllocationRepository.save(itemAllocation);
                    }
                } else {

                    itemInstance.setStorage(null);
                    itemInstance.setStatus(ItemInstanceStatus.ISSUE);
                    itemInstance.setPresentStatus(ItemInstanceStatus.ISSUE.toString());

                    itemInstance = itemInstanceRepository.save(itemInstance);

                    ItemInstanceStatusHistory itemInstanceStatusHistory = new ItemInstanceStatusHistory();
                    itemInstanceStatusHistory.setStatus(ItemInstanceStatus.ISSUE);
                    itemInstanceStatusHistory.setPresentStatus(ItemInstanceStatus.ISSUE.toString());
                    itemInstanceStatusHistory.setItemInstance(itemInstance);
                    itemInstanceStatusHistory.setTimestamp(new Date());
                    itemInstanceStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

                    itemInstanceStatusHistory = itemInstanceStatusHistoryRepository.save(itemInstanceStatusHistory);

                    Bom bom = bomRepository.findOne(issue.getRequest().getBomInstance().getBom());

                    Inventory inventory = null;
                    if (itemInstance.getSection() != null) {
                        inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(bom.getId(), requestItem.getItem().getItem().getId(), requestItem.getItem().getUniqueCode(), itemInstance.getSection());
                        if (inventory != null) {
                            inventory.setQuantity(inventory.getQuantity() - 1);
                            inventory.setQtyIssued(inventory.getQtyIssued() + 1);

                            inventory = inventoryRepository.save(inventory);
                        }
                    } else {
                        inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bom.getId(), requestItem.getItem().getItem().getId(), requestItem.getItem().getUniqueCode());
                        if (inventory != null) {
                            inventory.setQuantity(inventory.getQuantity() - 1);
                            inventory.setQtyIssued(inventory.getQtyIssued() + 1);

                            inventory = inventoryRepository.save(inventory);
                        }
                    }

                    ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceItem(requestItem.getItem().getId());

                    if (itemAllocation != null) {
                        itemAllocation.setIssuedQty(itemAllocation.getIssuedQty() + 1);
                        presentStorage.setRemainingCapacity(presentStorage.getRemainingCapacity() + 1);
                        presentStorage = storageRepository.save(presentStorage);

                        itemAllocation = itemAllocationRepository.save(itemAllocation);
                    }
                }
            } else {
                BomItemInstance bomItemInstance = bomItemInstanceRepository.findOne(issueItem.getBomItemInstance().getId());

                ItemInstance itemInstance = itemInstanceRepository.findOne(bomItemInstance.getItemInstance());

                itemInstance.setUpnNumber(itemInstance.getInitialUpn());

                itemInstance = itemInstanceRepository.save(itemInstance);
            }
        }

        issue.setModifiedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
        issue.setModifiedDate(new Date());
        issue.setStatus(IssueStatus.ISSUED);
        issue = issueRepository.save(issue);

        Request request = requestRepository.findOne(issue.getRequest().getId());
        List<RequestItem> requestItems = requestItemRepository.findByRequest(request);

        Boolean allIssued = true;

        for (RequestItem requestItem : requestItems) {
            issueItems = issueItemRepository.getReceivedItemsByRequest(requestItem.getId());

            if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                Double issuedQuantity = 0.0;
                for (IssueItem issueItem : issueItems) {
                    issuedQuantity = issuedQuantity + issueItem.getFractionalQuantity();
                }

                if (!requestItem.getFractionalQuantity().equals(issuedQuantity)) {
                    allIssued = false;
                }
            } else {
                if (!requestItem.getQuantity().equals(issueItems.size())) {
                    allIssued = false;
                }
            }
        }

        if (allIssued) {
            request.setIssued(true);

            request = requestRepository.save(request);
        }

        return issue;
    }


    @Transactional(readOnly = false)
    public Issue receiveItems(Integer issueId, List<IssueItem> issueItems) {

        Issue issue = issueRepository.findOne(issueId);

        List<BDLReceive> receives = bdlReceiveRepository.findByIssue(issue.getId());

        BDLReceive bdlReceive = new BDLReceive();
        bdlReceive.setIssue(issue.getId());
        bdlReceive.setReceiveSequence(receives.size() + 1);

        bdlReceive = bdlReceiveRepository.save(bdlReceive);

//        List<IssueItem> issueItems = issueItemRepository.findByIssue(issue);

        for (IssueItem item : issueItems) {

            IssueItem issueItem = issueItemRepository.findOne(item.getId());

            if (issueItem.getStatus() != null && (issueItem.getStatus().equals(IssueItemStatus.P_APPROVED) || issueItem.getStatus().equals(IssueItemStatus.APPROVED))) {

                RequestItem requestItem = requestItemRepository.findOne(issueItem.getRequestItem());
                ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());
                Storage presentStorage = storageRepository.findOne(itemInstance.getStorage().getId());

                if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {

                    BomItemInstance bomItemInstance = bomItemInstanceRepository.findByItemInstanceAndBomInstanceItem(itemInstance.getId(), requestItem.getItem().getId());

                    BomInstance bomInstance = null;
                    BomInstanceItem bomItemSection = null;
                    BomInstanceItem bomItemSubSystem = null;
                    BomInstanceItem bomItemUnit = null;

                    if (requestItem.getItem() != null) {
                        BomInstanceItem parent = bomInstanceItemRepository.findOne(requestItem.getItem().getParent());
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
                    String missile = requestItem.getRequest().getBomInstance().getItem().getInstanceName();
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

                    if (requestItem.getItem() != null) {
                        part = requestItem.getItem().getItem().getItemMaster().getItemCode();
                    }

                    List<LotInstance> lotInstances = lotInstanceRepository.findByInstance(itemInstance.getId());

                    LotInstance lotInstance = new LotInstance();
                    lotInstance.setInstance(itemInstance.getId());
                    lotInstance.setSequence(lotInstances.size() + 1);
                    if (bomItemSection != null && bomItemSection.getBomItemType().equals(BomItemType.COMMONPART)) {
                        lotInstance.setUpnNumber(system + "" + missile + "" + "0000" + "" + part);
                    } else if (bomItemSection != null && bomItemSection.getBomItemType().equals(BomItemType.SECTION)) {
                        lotInstance.setUpnNumber(system + "" + missile + "" + section + "" + subSystem + "" + unit + "" + part);
                    }
                    lotInstance.setStatus(ItemInstanceStatus.ISSUE);
                    lotInstance.setPresentStatus(ItemInstanceStatus.ISSUE.toString());
                    lotInstance.setLotQty(issueItem.getFractionalQuantity());
                    lotInstance.setIssueItem(issueItem.getId());
                    lotInstance = lotInstanceRepository.save(lotInstance);

                    LotInstanceHistory lotInstanceHistory = new LotInstanceHistory();
                    lotInstanceHistory.setStatus(ItemInstanceStatus.ISSUE);
                    lotInstanceHistory.setLotInstance(lotInstance.getId());
                    lotInstanceHistory.setTimestamp(new Date());
                    lotInstanceHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

                    lotInstanceHistory = lotInstanceHistoryRepository.save(lotInstanceHistory);

                    presentStorage.setRemainingCapacity(presentStorage.getRemainingCapacity() + lotInstance.getLotQty());
                    presentStorage = storageRepository.save(presentStorage);

                    itemInstance = itemInstanceRepository.save(itemInstance);

                    Bom bom = bomRepository.findOne(issue.getRequest().getBomInstance().getBom());
                    Inventory inventory = null;
                    if (itemInstance.getSection() != null) {
                        inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(bom.getId(), requestItem.getItem().getItem().getId(), requestItem.getItem().getUniqueCode(), itemInstance.getSection());
                        if (inventory != null) {
                            inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() - lotInstance.getLotQty());
                            inventory.setFractionalQtyIssued(inventory.getFractionalQtyIssued() + lotInstance.getLotQty());

                            inventory = inventoryRepository.save(inventory);
                        }
                    } else {
                        inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bom.getId(), requestItem.getItem().getItem().getId(), requestItem.getItem().getUniqueCode());
                        if (inventory != null) {
                            inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() - lotInstance.getLotQty());
                            inventory.setFractionalQtyIssued(inventory.getFractionalQtyIssued() + lotInstance.getLotQty());

                            inventory = inventoryRepository.save(inventory);
                        }
                    }

                    ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceItem(requestItem.getItem().getId());

                    if (itemAllocation != null) {
                        itemAllocation.setIssuedQty(itemAllocation.getIssuedQty() + lotInstance.getLotQty());
                        itemAllocation.setIssueProcessQty(itemAllocation.getIssueProcessQty() - lotInstance.getLotQty());
                        itemAllocation = itemAllocationRepository.save(itemAllocation);
                    }
                } else {

                    BomInstance bomInstance = null;
                    BomInstanceItem bomItemSection = null;
                    BomInstanceItem bomItemSubSystem = null;
                    BomInstanceItem bomItemUnit = null;

                    if (requestItem.getItem() != null) {
                        BomInstanceItem parent = bomInstanceItemRepository.findOne(requestItem.getItem().getParent());
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
                    String missile = requestItem.getRequest().getBomInstance().getItem().getInstanceName();
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

                    if (requestItem.getItem() != null) {
                        part = requestItem.getItem().getItem().getItemMaster().getItemCode();
                    }

                    if (bomItemSection != null && bomItemSection.getBomItemType().equals(BomItemType.COMMONPART)) {
                        itemInstance.setUpnNumber(system + "" + missile + "" + section + "" + subSystem + "" + unit + "" + part);
                    } else if (bomItemSection != null && bomItemSection.getBomItemType().equals(BomItemType.SECTION)) {
                        itemInstance.setUpnNumber(system + "" + missile + "" + section + "" + subSystem + "" + unit + "" + part);
                    }

                    itemInstance.setStorage(null);
                    itemInstance.setStatus(ItemInstanceStatus.ISSUE);
                    itemInstance.setPresentStatus(ItemInstanceStatus.ISSUE.toString());

                    itemInstance = itemInstanceRepository.save(itemInstance);

                    ItemInstanceStatusHistory itemInstanceStatusHistory = new ItemInstanceStatusHistory();
                    itemInstanceStatusHistory.setStatus(ItemInstanceStatus.ISSUE);
                    itemInstanceStatusHistory.setPresentStatus(ItemInstanceStatus.ISSUE.toString());
                    itemInstanceStatusHistory.setItemInstance(itemInstance);
                    itemInstanceStatusHistory.setTimestamp(new Date());
                    itemInstanceStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

                    itemInstanceStatusHistory = itemInstanceStatusHistoryRepository.save(itemInstanceStatusHistory);

                    Bom bom = bomRepository.findOne(issue.getRequest().getBomInstance().getBom());

                    Inventory inventory = null;
                    if (itemInstance.getSection() != null) {
                        inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(bom.getId(), requestItem.getItem().getItem().getId(), requestItem.getItem().getUniqueCode(), itemInstance.getSection());
                        if (inventory != null) {
                            inventory.setQuantity(inventory.getQuantity() - 1);
                            inventory.setQtyIssued(inventory.getQtyIssued() + 1);

                            inventory = inventoryRepository.save(inventory);
                        }
                    } else {
                        inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bom.getId(), requestItem.getItem().getItem().getId(), requestItem.getItem().getUniqueCode());
                        if (inventory != null) {
                            inventory.setQuantity(inventory.getQuantity() - 1);
                            inventory.setQtyIssued(inventory.getQtyIssued() + 1);

                            inventory = inventoryRepository.save(inventory);
                        }
                    }

                    ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceItem(requestItem.getItem().getId());

                    if (itemAllocation != null) {
                        itemAllocation.setIssuedQty(itemAllocation.getIssuedQty() + 1);
                        itemAllocation.setIssueProcessQty(itemAllocation.getIssueProcessQty() - 1);
                        presentStorage.setRemainingCapacity(presentStorage.getRemainingCapacity() + 1);
                        presentStorage = storageRepository.save(presentStorage);

                        itemAllocation = itemAllocationRepository.save(itemAllocation);
                    }
                }

                issueItem.setReceivedDate(new Date());
                issueItem.setStatus(IssueItemStatus.RECEIVED);
                issueItem = issueItemRepository.save(issueItem);

                BDLReceiveItem bdlReceiveItem = new BDLReceiveItem();
                bdlReceiveItem.setReceive(bdlReceive.getId());
                bdlReceiveItem.setIssueItem(issueItem.getId());

                bdlReceiveItem = bdlReceiveItemRepository.save(bdlReceiveItem);
            }
        }

        issue.setModifiedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
        issue.setModifiedDate(new Date());

        List<IssueItem> receivedItems = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.RECEIVED);
        List<IssueItem> pendingItems = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.PENDING);
        List<IssueItem> approvedList = issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.APPROVED);
        approvedList.addAll(issueItemRepository.findByIssueAndStatus(issue, IssueItemStatus.P_APPROVED));

        if (receivedItems.size() > 0 && approvedList.size() == 0 && pendingItems.size() == 0) {
            issue.setStatus(IssueStatus.RECEIVED);

            IssueHistory issueHistory = new IssueHistory();
            issueHistory.setIssue(issue.getId());
            issueHistory.setStatus(IssueStatus.RECEIVED);
            issueHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson());
            issueHistory.setUpdatedDate(new Date());

            issueHistory = issueHistoryRepository.save(issueHistory);
        } else if (receivedItems.size() > 0 && (approvedList.size() > 0 || pendingItems.size() > 0)) {
            issue.setStatus(IssueStatus.PARTIALLY_RECEIVED);

            IssueHistory issueHistory = new IssueHistory();
            issueHistory.setIssue(issue.getId());
            issueHistory.setStatus(IssueStatus.PARTIALLY_RECEIVED);
            issueHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson());
            issueHistory.setUpdatedDate(new Date());

            issueHistory = issueHistoryRepository.save(issueHistory);
        }

        issue = issueRepository.save(issue);

        Request request = requestRepository.findOne(issue.getRequest().getId());
        List<RequestItem> requestItems = requestItemRepository.findByRequest(request);

        Boolean allIssued = true;

        for (RequestItem requestItem : requestItems) {
            if (!requestItem.getStatus().equals(RequestItemStatus.REJECTED)) {
                issueItems = issueItemRepository.getReceivedItemsByRequest(requestItem.getId());

                if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                    Double issuedQuantity = 0.0;
                    for (IssueItem issueItem : issueItems) {
                        issuedQuantity = issuedQuantity + issueItem.getFractionalQuantity();
                    }

                    if (!requestItem.getFractionalQuantity().equals(issuedQuantity)) {
                        allIssued = false;
                    }
                } else {
                    if (!requestItem.getQuantity().equals(issueItems.size())) {
                        allIssued = false;
                    }
                }
            }
        }

        if (allIssued) {
            request.setIssued(true);

            request = requestRepository.save(request);
        }


        String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " BDL PPC has Received Items from '" + issue.getNumber();
        drdoUpdatesService.updateMessage(message, DRDOObjectType.ISSUE);

        return issue;
    }

    @Transactional(readOnly = false)
    public List<IssueReportDto> getIssueReportByMissile(Integer missileId, Integer sectionId, String searchText) {
        List<IssueReportDto> issueReport = new ArrayList<>();
        BomSearchCriteria bomSearchCriteria = new BomSearchCriteria();
        bomSearchCriteria.setSearchQuery(searchText);
        bomSearchCriteria.setBom(missileId);
        bomSearchCriteria.setSection(sectionId);

        Pageable pageable1 = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.ASC, "createdDate")));
        if (sectionId != 0) {
            BomInstanceItem section = bomInstanceItemRepository.findOne(sectionId);

            IssueReportDto issueReportDto = new IssueReportDto();
            issueReportDto.setSection(section);

            List<BomInstanceItem> subsystems = bomInstanceItemRepository.getParentChildrenOrderByCreatedDateAsc(section.getId());
            List<BomInstanceItem> parts = bomInstanceItemRepository.getPartsByParentOrderByCreatedDateAsc(section.getId());

            parts.forEach(bomInstanceItem -> {
                SubsystemIssueReport subsystemIssueReport = new SubsystemIssueReport();
                List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.findByBomInstanceItem(bomInstanceItem.getId());

                if (bomItemInstances.size() > 0) {
                    BomInstanceItemDto bomInstanceItemDto = new BomInstanceItemDto();

                    bomInstanceItemDto.setItemRevision(bomInstanceItem.getItem());
                    bomInstanceItemDto.setBomItemType(bomInstanceItem.getBomItemType());
                    if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                        bomInstanceItemDto.setQuantity(bomInstanceItem.getFractionalQuantity());
                    } else {
                        bomInstanceItemDto.setQuantity(bomInstanceItem.getQuantity().doubleValue());
                    }

                    bomItemInstances.forEach(bomItemInstance -> {
                        List<IssueItem> issueItems = issueItemRepository.findByBomItemInstance(bomItemInstance);
                        if (issueItems.size() > 0) {
                            issueItems.forEach(issueItem -> {
                                ItemInstance itemInstance = itemInstanceRepository.findOne(bomItemInstance.getItemInstance());
                                if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                                    if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                        bomInstanceItemDto.setIssuedQuantity(bomInstanceItemDto.getIssuedQuantity() + issueItem.getFractionalQuantity());
                                        bomInstanceItemDto.getLotInstances().addAll(lotInstanceRepository.findByIssueItemAndInstance(issueItem.getId(), bomItemInstance.getItemInstance()));

                                        bomInstanceItemDto.getLotInstances().forEach(lotInstance -> {
                                            lotInstance.setItemInstance(itemInstanceRepository.findOne(lotInstance.getInstance()));
                                        });
                                    } else {
                                        bomInstanceItemDto.setIssuedQuantity(bomInstanceItemDto.getIssuedQuantity() + 1);
                                        itemInstance.setIssuedDate(issueItem.getReceivedDate());
                                        bomInstanceItemDto.getItemInstances().add(itemInstance);
                                    }
                                } else {
                                    if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                        bomInstanceItemDto.setInProcessQty(bomInstanceItemDto.getInProcessQty() + issueItem.getFractionalQuantity());
                                        bomInstanceItemDto.getItemInstances().add(itemInstance);
                                    } else {
                                        bomInstanceItemDto.setInProcessQty(bomInstanceItemDto.getInProcessQty() + 1);
                                        bomInstanceItemDto.getItemInstances().add(itemInstance);
                                    }
                                }
                            });
                        } else {
                            bomItemInstanceRepository.delete(bomItemInstance.getId());
                        }
                    });

                    subsystemIssueReport.getIssuedParts().add(bomInstanceItemDto);
                }

                if (subsystemIssueReport.getIssuedParts().size() > 0) {
                    issueReportDto.getSubsystemReport().add(subsystemIssueReport);
                }
            });

            subsystems.forEach(subsystem -> {

                Predicate predicate = bomInstanceItemSearchPredicateBuilder.build(bomSearchCriteria, QBomInstanceItem.bomInstanceItem);

                Page<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findAll(predicate, pageable1);

                if (bomInstanceItems.getContent().size() > 0) {
                    SubsystemIssueReport subsystemIssueReport = new SubsystemIssueReport();

                    if (subsystem.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        subsystemIssueReport.setSubsystem(subsystem);
                    } else if (subsystem.getBomItemType().equals(BomItemType.UNIT)) {
                        subsystemIssueReport.setSubsystem(subsystem);
                    }

                    bomInstanceItems.getContent().forEach(bomInstanceItem -> {

                        List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.findByBomInstanceItem(bomInstanceItem.getId());

                        if (bomItemInstances.size() > 0) {
                            BomInstanceItemDto bomInstanceItemDto = new BomInstanceItemDto();

                            bomInstanceItemDto.setItemRevision(bomInstanceItem.getItem());
                            bomInstanceItemDto.setBomItemType(bomInstanceItem.getBomItemType());
                            if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                bomInstanceItemDto.setQuantity(bomInstanceItem.getFractionalQuantity());
                            } else {
                                bomInstanceItemDto.setQuantity(bomInstanceItem.getQuantity().doubleValue());
                            }

                            bomItemInstances.forEach(bomItemInstance -> {

                                List<IssueItem> issueItems = issueItemRepository.findByBomItemInstance(bomItemInstance);
                                if (issueItems.size() > 0) {
                                    issueItems.forEach(issueItem -> {
                                        ItemInstance itemInstance = itemInstanceRepository.findOne(bomItemInstance.getItemInstance());
                                        if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                                            if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                                bomInstanceItemDto.setIssuedQuantity(bomInstanceItemDto.getIssuedQuantity() + issueItem.getFractionalQuantity());
                                                bomInstanceItemDto.getLotInstances().addAll(lotInstanceRepository.findByIssueItemAndInstance(issueItem.getId(), bomItemInstance.getItemInstance()));

                                                bomInstanceItemDto.getLotInstances().forEach(lotInstance -> {
                                                    lotInstance.setItemInstance(itemInstanceRepository.findOne(lotInstance.getInstance()));
                                                });
                                            } else {
                                                bomInstanceItemDto.setIssuedQuantity(bomInstanceItemDto.getIssuedQuantity() + 1);
                                                itemInstance.setIssuedDate(issueItem.getReceivedDate());
                                                bomInstanceItemDto.getItemInstances().add(itemInstance);
                                            }
                                        } else {
                                            if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                                bomInstanceItemDto.setInProcessQty(bomInstanceItemDto.getInProcessQty() + issueItem.getFractionalQuantity());
                                                bomInstanceItemDto.getItemInstances().add(itemInstance);
                                            } else {
                                                bomInstanceItemDto.setInProcessQty(bomInstanceItemDto.getInProcessQty() + 1);
                                                bomInstanceItemDto.getItemInstances().add(itemInstance);
                                            }
                                        }
                                    });
                                } else {
                                    bomItemInstanceRepository.delete(bomItemInstance.getId());
                                }
                            });

                            subsystemIssueReport.getIssuedParts().add(bomInstanceItemDto);
                        }
                    });

                    if (subsystemIssueReport.getIssuedParts().size() > 0) {
                        issueReportDto.getSubsystemReport().add(subsystemIssueReport);
                    }
                }

                /*----------------------  Without Search Field -------------------------*/

                /*List<Integer> partIds = new ArrayList<Integer>();

                if (subsystem.getBomItemType().equals(BomItemType.PART)) {
                    List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.findByBomInstanceItem(subsystem.getId());
                    if (bomItemInstances.size() > 0) {
                        partIds.add(subsystem.getId());
                    }
                } else {
                    partIds = visitChildren(subsystem, partIds);
                }

                if (partIds.size() > 0) {
                    SubsystemIssueReport subsystemIssueReport = new SubsystemIssueReport();
                    subsystemIssueReport.setSubsystem(subsystem);

                    partIds.forEach(partId -> {

                        BomInstanceItemDto bomInstanceItemDto = new BomInstanceItemDto();

                        BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(partId);

                        bomInstanceItemDto.setItemRevision(bomInstanceItem.getItem());
                        bomInstanceItemDto.setBomItemType(bomInstanceItem.getBomItemType());
                        if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            bomInstanceItemDto.setQuantity(bomInstanceItem.getFractionalQuantity());
                        } else {
                            bomInstanceItemDto.setQuantity(bomInstanceItem.getQuantity().doubleValue());
                        }

                        List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.findByBomInstanceItem(bomInstanceItem.getId());

                        bomItemInstances.forEach(bomItemInstance -> {
                            IssueItem issueItem = issueItemRepository.getByBomItemInstance(bomItemInstance.getId());
                            ItemInstance itemInstance = itemInstanceRepository.findOne(bomItemInstance.getItemInstance());
                            if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                    bomInstanceItemDto.setIssuedQuantity(bomInstanceItemDto.getIssuedQuantity() + issueItem.getFractionalQuantity());
                                    bomInstanceItemDto.getLotInstances().addAll(lotInstanceRepository.findByInstance(bomItemInstance.getItemInstance()));

                                    bomInstanceItemDto.getLotInstances().forEach(lotInstance -> {
                                        lotInstance.setItemInstance(itemInstanceRepository.findOne(lotInstance.getInstance()));
                                    });
                                } else {
                                    bomInstanceItemDto.setIssuedQuantity(bomInstanceItemDto.getIssuedQuantity() + 1);
                                    itemInstance.setIssuedDate(issueItem.getReceivedDate());
                                    bomInstanceItemDto.getItemInstances().add(itemInstance);
                                }
                            } else {
                                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                    bomInstanceItemDto.setInProcessQty(bomInstanceItemDto.getInProcessQty() + issueItem.getFractionalQuantity());
                                    bomInstanceItemDto.getItemInstances().add(itemInstance);
                                } else {
                                    bomInstanceItemDto.setInProcessQty(bomInstanceItemDto.getInProcessQty() + 1);
                                    bomInstanceItemDto.getItemInstances().add(itemInstance);
                                }
                            }
                        });

                        subsystemIssueReport.getIssuedParts().add(bomInstanceItemDto);
                    });
                    issueReportDto.getSubsystemReport().add(subsystemIssueReport);
                }*/
            });

            if (issueReportDto.getSubsystemReport().size() > 0) {
                issueReport.add(issueReportDto);
            }
        } else {
            List<BomInstanceItem> sections = bomInstanceItemRepository.findByBomOrderByCreatedDateAsc(missileId);

            sections.forEach(section -> {

                IssueReportDto issueReportDto = new IssueReportDto();
                issueReportDto.setSection(section);

                List<BomInstanceItem> subsystems = bomInstanceItemRepository.getParentChildrenOrderByCreatedDateAsc(section.getId());
                List<BomInstanceItem> parts = bomInstanceItemRepository.getPartsByParentOrderByCreatedDateAsc(section.getId());

                parts.forEach(bomInstanceItem -> {
                    SubsystemIssueReport subsystemIssueReport = new SubsystemIssueReport();
                    List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.findByBomInstanceItem(bomInstanceItem.getId());

                    if (bomItemInstances.size() > 0) {
                        BomInstanceItemDto bomInstanceItemDto = new BomInstanceItemDto();

                        bomInstanceItemDto.setItemRevision(bomInstanceItem.getItem());
                        bomInstanceItemDto.setBomItemType(bomInstanceItem.getBomItemType());
                        if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            bomInstanceItemDto.setQuantity(bomInstanceItem.getFractionalQuantity());
                        } else {
                            bomInstanceItemDto.setQuantity(bomInstanceItem.getQuantity().doubleValue());
                        }

                        bomItemInstances.forEach(bomItemInstance -> {
                            List<IssueItem> issueItems = issueItemRepository.findByBomItemInstance(bomItemInstance);
                            if (issueItems.size() > 0) {
                                issueItems.forEach(issueItem -> {
                                    ItemInstance itemInstance = itemInstanceRepository.findOne(bomItemInstance.getItemInstance());
                                    if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                                        if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                            bomInstanceItemDto.setIssuedQuantity(bomInstanceItemDto.getIssuedQuantity() + issueItem.getFractionalQuantity());
                                            bomInstanceItemDto.getLotInstances().addAll(lotInstanceRepository.findByIssueItemAndInstance(issueItem.getId(), bomItemInstance.getItemInstance()));

                                            bomInstanceItemDto.getLotInstances().forEach(lotInstance -> {
                                                lotInstance.setItemInstance(itemInstanceRepository.findOne(lotInstance.getInstance()));
                                            });
                                        } else {
                                            bomInstanceItemDto.setIssuedQuantity(bomInstanceItemDto.getIssuedQuantity() + 1);
                                            itemInstance.setIssuedDate(issueItem.getReceivedDate());
                                            bomInstanceItemDto.getItemInstances().add(itemInstance);
                                        }
                                    } else {
                                        if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                            bomInstanceItemDto.setInProcessQty(bomInstanceItemDto.getInProcessQty() + issueItem.getFractionalQuantity());
                                            bomInstanceItemDto.getItemInstances().add(itemInstance);
                                        } else {
                                            bomInstanceItemDto.setInProcessQty(bomInstanceItemDto.getInProcessQty() + 1);
                                            bomInstanceItemDto.getItemInstances().add(itemInstance);
                                        }
                                    }
                                });
                            } else {
                                bomItemInstanceRepository.delete(bomItemInstance.getId());
                            }
                        });

                        subsystemIssueReport.getIssuedParts().add(bomInstanceItemDto);
                    }

                    if (subsystemIssueReport.getIssuedParts().size() > 0) {
                        issueReportDto.getSubsystemReport().add(subsystemIssueReport);
                    }
                });

                subsystems.forEach(subsystem -> {

                    if (subsystem.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        bomSearchCriteria.setSubsystem(subsystem.getId());
                    } else if (subsystem.getBomItemType().equals(BomItemType.UNIT)) {
                        bomSearchCriteria.setUnit(subsystem.getId());
                    }

                    Predicate predicate = bomInstanceItemSearchPredicateBuilder.build(bomSearchCriteria, QBomInstanceItem.bomInstanceItem);

                    Page<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findAll(predicate, pageable1);

                    if (bomInstanceItems.getContent().size() > 0) {
                        SubsystemIssueReport subsystemIssueReport = new SubsystemIssueReport();
                        if (subsystem.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            subsystemIssueReport.setSubsystem(subsystem);
                        } else if (subsystem.getBomItemType().equals(BomItemType.UNIT)) {
                            subsystemIssueReport.setSubsystem(subsystem);
                        }
                        bomInstanceItems.getContent().forEach(bomInstanceItem -> {

                            List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.findByBomInstanceItem(bomInstanceItem.getId());

                            if (bomItemInstances.size() > 0) {
                                BomInstanceItemDto bomInstanceItemDto = new BomInstanceItemDto();

                                bomInstanceItemDto.setItemRevision(bomInstanceItem.getItem());
                                bomInstanceItemDto.setBomItemType(bomInstanceItem.getBomItemType());
                                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                    bomInstanceItemDto.setQuantity(bomInstanceItem.getFractionalQuantity());
                                } else {
                                    bomInstanceItemDto.setQuantity(bomInstanceItem.getQuantity().doubleValue());
                                }

                                bomItemInstances.forEach(bomItemInstance -> {
                                    List<IssueItem> issueItems = issueItemRepository.findByBomItemInstance(bomItemInstance);
                                    if (issueItems.size() > 0) {
                                        issueItems.forEach(issueItem -> {
                                            ItemInstance itemInstance = itemInstanceRepository.findOne(bomItemInstance.getItemInstance());
                                            if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                                                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                                    bomInstanceItemDto.setIssuedQuantity(bomInstanceItemDto.getIssuedQuantity() + issueItem.getFractionalQuantity());
                                                    bomInstanceItemDto.getLotInstances().addAll(lotInstanceRepository.findByIssueItemAndInstance(issueItem.getId(), bomItemInstance.getItemInstance()));

                                                    bomInstanceItemDto.getLotInstances().forEach(lotInstance -> {
                                                        lotInstance.setItemInstance(itemInstanceRepository.findOne(lotInstance.getInstance()));
                                                    });
                                                } else {
                                                    bomInstanceItemDto.setIssuedQuantity(bomInstanceItemDto.getIssuedQuantity() + 1);
                                                    itemInstance.setIssuedDate(issueItem.getReceivedDate());
                                                    bomInstanceItemDto.getItemInstances().add(itemInstance);
                                                }
                                            } else {
                                                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                                    bomInstanceItemDto.setInProcessQty(bomInstanceItemDto.getInProcessQty() + issueItem.getFractionalQuantity());
                                                    bomInstanceItemDto.getItemInstances().add(itemInstance);
                                                } else {
                                                    bomInstanceItemDto.setInProcessQty(bomInstanceItemDto.getInProcessQty() + 1);
                                                    bomInstanceItemDto.getItemInstances().add(itemInstance);
                                                }
                                            }
                                        });
                                    } else {
                                        bomItemInstanceRepository.delete(bomItemInstance.getId());
                                    }
                                });

                                subsystemIssueReport.getIssuedParts().add(bomInstanceItemDto);
                            }
                        });

                        if (subsystemIssueReport.getIssuedParts().size() > 0) {
                            issueReportDto.getSubsystemReport().add(subsystemIssueReport);
                        }
                    }
                });

                if (issueReportDto.getSubsystemReport().size() > 0) {
                    issueReport.add(issueReportDto);
                }
            });
        }

        return issueReport;
    }

    private List<Integer> visitChildren(BomInstanceItem bomInstanceItem, List<Integer> partIds) {

        List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomInstanceItem.getId());

        for (BomInstanceItem instanceItem : bomInstanceItems) {
            if (instanceItem.getBomItemType().equals(BomItemType.PART)) {
                List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.findByBomInstanceItem(instanceItem.getId());
                if (bomItemInstances.size() > 0) {
                    partIds.add(instanceItem.getId());
                }
            } else {
                partIds = visitChildren(instanceItem, partIds);
            }
        }

        return partIds;
    }

    public GroupPermission getPermissionByPersonId(Integer personId) {
        GroupPermission groupPermission = null;

        List<PersonGroup> personGroups = groupMemberRepository.findGroupsByPerson(personId);

        List<Integer> groupIds = new ArrayList<>();

        personGroups.forEach(personGroup -> {
            groupIds.add(personGroup.getGroupId());
        });

        if (groupIds.size() > 0) {
            HashMap<String, GroupPermission> groupPermissionHashMap = new HashMap<>();
            List<GroupPermission> groupPermissions = groupPermissionRepository.getPermissionsByGroupIds(groupIds);
            groupPermissions.forEach(permission -> {
                groupPermissionHashMap.put(permission.getId().getPermission().getId(), permission);
            });

            GroupPermission storeApprove = null;
            GroupPermission editPermission = null;

            storeApprove = groupPermissionHashMap.get("permission.inward.storeApprove");
            editPermission = groupPermissionHashMap.get("permission.inward.edit");

            if (storeApprove != null && editPermission != null) {
                groupPermission = storeApprove;
            }

        }

        return groupPermission;
    }
}