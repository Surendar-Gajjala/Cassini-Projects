package com.cassinisys.drdo.service.transactions;

import com.cassinisys.drdo.filtering.*;
import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.model.dto.*;
import com.cassinisys.drdo.model.inventory.Inventory;
import com.cassinisys.drdo.model.inventory.Storage;
import com.cassinisys.drdo.model.inventory.StorageItem;
import com.cassinisys.drdo.model.inventory.StorageType;
import com.cassinisys.drdo.model.transactions.*;
import com.cassinisys.drdo.repo.bom.*;
import com.cassinisys.drdo.repo.inventory.InventoryRepository;
import com.cassinisys.drdo.repo.inventory.StorageItemRepository;
import com.cassinisys.drdo.repo.inventory.StorageRepository;
import com.cassinisys.drdo.repo.transactions.*;
import com.cassinisys.drdo.service.DRDOUpdatesService;
import com.cassinisys.drdo.service.bom.ItemTypeService;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.model.col.AttributeAttachment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.col.AttributeAttachmentRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.Utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.mysema.query.types.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by subra on 16-10-2018.
 */
@Service
public class InwardService implements CrudService<Inward, Integer> {

    @Autowired
    private InwardRepository inwardRepository;

    @Autowired
    private InwardItemRepository inwardItemRepository;

    @Autowired
    private InwardStatusHistoryRepository inwardStatusHistoryRepository;

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private InwardItemInstanceRepository inwardItemInstanceRepository;

    @Autowired
    private ItemInstanceRepository itemInstanceRepository;

    @Autowired
    private BomInstanceItemRepository bomInstanceItemRepository;

    @Autowired
    private BomItemInstanceRepository bomItemInstanceRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private BomRepository bomRepository;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private BomItemRepository bomItemRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private AttributeAttachmentRepository attributeAttachmentRepository;

    @Autowired
    private ItemInstanceStatusHistoryRepository instanceStatusHistoryRepository;

    @Autowired
    private InwardPredicateBuilder inwardPredicateBuilder;

    @Autowired
    private GatePassPredicateBuilder gatePassPredicateBuilder;

    @Autowired
    private InwardItemInstancePredicateBuilder inwardItemInstancePredicateBuilder;

    @Autowired
    private GatePassRepository gatePassRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private StorageItemRepository storageItemRepository;

    @Autowired
    private BomGroupRepository bomGroupRepository;

    @Autowired
    private LotInstanceRepository lotInstanceRepository;

    @Autowired
    private LotInstanceHistoryRepository lotInstanceHistoryRepository;

    @Autowired
    private IssueItemRepository issueItemRepository;

    @Autowired
    private RequestItemRepository requestItemRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ItemInstancePredicateBuilder itemInstancePredicateBuilder;

    @Autowired
    private DRDOUpdatesService drdoUpdatesService;

    @Autowired
    private InwardItemPredicateBuilder inwardItemPredicateBuilder;

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
    public Inward create(Inward inward) {

        AutoNumber autoNumber = autoNumberRepository.findByName("Default Inward Number Source");
        String number = autoNumber.next();
        autoNumber = autoNumberRepository.save(autoNumber);

        inward.setStatus(InwardStatus.STORE);
        inward.setNumber(number);
        inward = inwardRepository.save(inward);

        Person security = personRepository.findOne(inward.getGatePass().getCreatedBy());

        InwardStatusHistory inwardStatusHistory = new InwardStatusHistory();
        inwardStatusHistory.setInward(inward.getId());
        inwardStatusHistory.setOldStatus(InwardStatus.SECURITY);
        inwardStatusHistory.setNewStatus(InwardStatus.SECURITY);
        inwardStatusHistory.setUser(security);
        inwardStatusHistory.setTimestamp(inward.getGatePass().getCreatedDate());

        inwardStatusHistory = inwardStatusHistoryRepository.save(inwardStatusHistory);

        String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " has created new '" + inward.getNumber()
                + "' Inward with Gate Pass " + inward.getGatePass().getGatePass().getName();
        drdoUpdatesService.updateMessage(message, DRDOObjectType.INWARD);

        return inward;
    }

    @Override
    @Transactional(readOnly = false)
    public Inward update(Inward inward) {
        return inwardRepository.save(inward);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {

        List<InwardItem> inwardItems = inwardItemRepository.findByInward(id);
        if (inwardItems.size() > 0) {
            throw new CassiniException("Inward has items. We can't delete");
        } else {
            inwardRepository.delete(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Inward get(Integer id) {
        Inward inward = inwardRepository.findOne(id);

        inward.getStatusHistories().addAll(inwardStatusHistoryRepository.findByInwardOrderByTimestampDesc(inward.getId()));
        return inward;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inward> getAll() {
        return inwardRepository.findAllByOrderByCreatedDateDesc();
    }

    @Transactional(readOnly = true)
    public Page<Inward> getAllInwards(Pageable pageable, InwardCriteria criteria) {

        Predicate predicate = inwardPredicateBuilder.build(criteria, QInward.inward);
        Page<Inward> inwards = null;
        if (predicate != null) {
            inwards = inwardRepository.findAll(predicate, pageable);

            inwards.getContent().forEach(inward -> {

                List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward.getId());

                inwardItems.forEach(inwardItem -> {
                    List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.getProvisionallyAcceptedInwardItemInstances(inwardItem.getId());

                    List<InwardItemInstance> inwardItemInstanceList = inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.STORE_SUBMITTED);

                    if (inwardItemInstances.size() > 0) {
                        inward.setProvisionalAcceptItems(true);
                    }

                    if (criteria.getSsqagApprove() && !criteria.getAdminPermission()) {
                        if (inwardItemInstanceList.size() > 0) {
                            inward.setShowNewBadge(true);
                        }
                    }

                });

                if (inwardItems.size() > 0) {
                    inward.setItemsExist(true);
                }

            });
        }

        /*List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.findAll();
        List<LotInstance> lotInstances = lotInstanceRepository.findAll();

        for (InwardItemInstance inwardItemInstance : inwardItemInstances) {

            if (inwardItemInstance.getItem().getStatus().equals(ItemInstanceStatus.ISSUE)) {
                ItemInstance itemInstance = itemInstanceRepository.findOne(inwardItemInstance.getItem().getId());
                BomItemInstance bomItemInstance = bomItemInstanceRepository.findByItemInstance(inwardItemInstance.getItem().getId());
                BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(bomItemInstance.getBomInstanceItem());

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
                            if (parent2.getBomItemType().equals(BomItemType.SECTION)) {
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
                String missile = "00";
                String system = itemInstance.getUpnNumber().substring(0, 2);
                if (bomInstance != null) {
                    missile = bomInstance.getItem().getInstanceName();
                }

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

                if (bomInstanceItem != null) {
                    part = bomInstanceItem.getItem().getItemMaster().getItemCode();
                }


                itemInstance.setUpnNumber(system + "" + missile + "" + section + "" + subSystem + "" + unit + "" + part);

                itemInstance = itemInstanceRepository.save(itemInstance);

            }
        }*/

        /*List<Issue> issues = issueRepository.findAll();

        for (Issue issue : issues) {
            if (issue.getStatus().equals(IssueStatus.ISSUED)) {
                List<IssueItem> issueItems = issueItemRepository.findByIssue(issue);

                List<BDLReceive> receives = bdlReceiveRepository.findByIssue(issue.getId());

                BDLReceive bdlReceive = new BDLReceive();
                bdlReceive.setIssue(issue.getId());
                bdlReceive.setReceiveSequence(receives.size() + 1);

                bdlReceive = bdlReceiveRepository.save(bdlReceive);

                for (IssueItem issueItem : issueItems) {
                    BDLReceiveItem bdlReceiveItem = new BDLReceiveItem();
                    bdlReceiveItem.setReceive(bdlReceive.getId());
                    bdlReceiveItem.setIssueItem(issueItem.getId());

                    bdlReceiveItem = bdlReceiveItemRepository.save(bdlReceiveItem);

                    if (issueItem.getStatus() != null && (issueItem.getStatus().equals(IssueItemStatus.P_APPROVED) || issueItem.getStatus().equals(IssueItemStatus.RECEIVED))) {
                        ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());

                        ItemInstanceStatusHistory statusHistory = instanceStatusHistoryRepository.getHistoryByInstanceAndStatus(itemInstance.getId(), ItemInstanceStatus.P_APPROVED);
                        if (statusHistory != null) {
                            itemInstance.setProvisionalAccept(true);
                            itemInstance = itemInstanceRepository.save(itemInstance);
                        }
                    }
                }
            } else if (issue.getStatus().equals(IssueStatus.BDL_QC)) {
                List<IssueItem> issueItems = issueItemRepository.findByIssue(issue);
                for (IssueItem issueItem : issueItems) {
                    if (issueItem.getStatus() != null && issueItem.getStatus().equals(IssueItemStatus.P_APPROVED)) {
                        ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());
                        if (itemInstance != null) {
                            itemInstance.setProvisionalAccept(true);
                            itemInstance = itemInstanceRepository.save(itemInstance);
                        }
                    }
                }
            } else if (issue.getStatus().equals(IssueStatus.STORE)) {
                List<IssueItem> issueItems = issueItemRepository.findByIssue(issue);
                for (IssueItem issueItem : issueItems) {
                    if (issueItem.getStatus() != null && issueItem.getStatus().equals(IssueItemStatus.P_APPROVED)) {
                        ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());
                        if (itemInstance != null) {
                            itemInstance.setProvisionalAccept(true);
                            itemInstance = itemInstanceRepository.save(itemInstance);
                        }
                    }
                }
            }
        }

        for (Issue issue : issues) {
            if (issue.getStatus().equals(IssueStatus.ISSUED)) {
                issue.setStatus(IssueStatus.RECEIVED);

                issue = issueRepository.save(issue);
            }
        }*/

        return inwards;
    }

    @Transactional(readOnly = true)
    public Page<InwardItemInstance> getAllInwardItems(Pageable pageable, InwardCriteria criteria) {

        Predicate predicate = inwardItemInstancePredicateBuilder.build(criteria, QInwardItemInstance.inwardItemInstance);

        pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(new Sort.Order(Sort.Direction.DESC, "item.modifiedDate")));

        Page<InwardItemInstance> inwardItemInstances = new PageImpl<InwardItemInstance>(new ArrayList<>());

        if (criteria.getSsqagApprove() || criteria.getStoreApprove()) {
            if (predicate != null) {
                inwardItemInstances = inwardItemInstanceRepository.findAll(predicate, pageable);
            }
        } else {
            inwardItemInstances = inwardItemInstanceRepository.findAll(predicate, pageable);
        }


        inwardItemInstances.forEach(inwardItemInstance -> {

            InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());

            inwardItemInstance.setInward(inwardRepository.findOne(inwardItem.getInward()));

            ItemType itemType = itemTypeRepository.findOne(inwardItemInstance.getItem().getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                inwardItemInstance.getItem().getItem().getItemMaster().setParentType(itemType);
            } else {
                inwardItemInstance.getItem().getItem().getItemMaster().setParentType(inwardItemInstance.getItem().getItem().getItemMaster().getItemType());
            }

        });

        return inwardItemInstances;
    }

    @Transactional(readOnly = true)
    public InwardDto getAllInwardsByPermission(Pageable pageable, InwardCriteria criteria) {

        InwardDto inwardDto = new InwardDto();
        Page<GatePass> gatePasses = null;
        if (criteria.getGatePassView()) {

            if (criteria.getFromDate() != null && criteria.getToDate() != null) {

                GatePassCriteria passCriteria = new GatePassCriteria();
                passCriteria.setFinish(criteria.getFinish());
                passCriteria.setFromDate(criteria.getFromDate());
                passCriteria.setToDate(criteria.getToDate());
                Predicate predicate = gatePassPredicateBuilder.build(passCriteria, QGatePass.gatePass1);
                gatePasses = gatePassRepository.findAll(predicate, pageable);
            } else {
                gatePasses = gatePassRepository.getNotFinishedGatePasses(pageable);
            }

            gatePasses.getContent().forEach(gatePass -> {
                gatePass.setInwards(inwardRepository.getInwardsByGatePass(gatePass.getId()).size());
            });

            inwardDto.setGatePasses(gatePasses);
            inwardDto.setInwardsPage(false);
            inwardDto.setGatePassView(true);
            inwardDto.setGatePassLength(gatePasses.getContent().size());
        } else {
            Predicate predicate = inwardPredicateBuilder.build(criteria, QInward.inward);
            Page<Inward> inwards = null;
            if (predicate != null) {
                inwards = inwardRepository.findAll(predicate, pageable);

                inwards.getContent().forEach(inward -> {
                    List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward.getId());

                    inwardItems.forEach(inwardItem -> {
                        List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.getProvisionallyAcceptedInwardItemInstances(inwardItem.getId());
                        List<InwardItemInstance> inwardItemInstanceList = inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.STORE_SUBMITTED);
                        if (inwardItemInstances.size() > 0) {
                            inward.setProvisionalAcceptItems(true);
                        }

                        if (criteria.getSsqagApprove() && !criteria.getAdminPermission()) {
                            if (inwardItemInstanceList.size() > 0) {
                                inward.setShowNewBadge(true);
                            }
                        }
                    });

                    if (inwardItems.size() > 0) {
                        inward.setItemsExist(true);
                    }
                });
            }

            inwardDto.setInwards(inwards);
            inwardDto.setInwardsPage(true);
            inwardDto.setFinishedPage(criteria.getFinishedPage());

        }

        List<Inward> finishedInwards = inwardRepository.getInwardsByStatus(InwardStatus.FINISH);
//        List<GatePass> gatePasses = gatePassRepository.findAll();

        inwardDto.setFinishedInwards(finishedInwards.size());
        inwardDto.setGatePassLength(gatePassRepository.findByFinishFalse().size());

        /*if (criteria.getGatePassView()) {
            Page<GatePass> gatePassList = null;
            if (criteria.getFromDate() != null && criteria.getToDate() != null) {
                GatePassCriteria passCriteria = new GatePassCriteria();
                passCriteria.setFromDate(criteria.getFromDate());
                passCriteria.setToDate(criteria.getToDate());
                Predicate predicate = gatePassPredicateBuilder.build(passCriteria, QGatePass.gatePass1);
                gatePassList = gatePassRepository.findAll(predicate, pageable);
            } else {
                gatePassList = gatePassRepository.findAll(pageable);
            }
            inwardDto.setGatePasses(gatePassList);
            inwardDto.setInwardsPage(false);
            inwardDto.setGatePassView(true);
        }*/

        return inwardDto;
    }

    @Transactional(readOnly = false)
    public Inward updateInward(Inward inward) {
        return inwardRepository.save(inward);
    }

    @Transactional(readOnly = true)
    public List<Inward> getInwardsByBom(Integer bomId) {
        return inwardRepository.findInwardsByBom(bomId);
    }

    @Transactional(readOnly = true)
    public InwardReportDto getInwardReport(Integer inwardId) {
        InwardReportDto inwardReportDto = new InwardReportDto();

        /*------------------------- Inward Items  ---------------------------------*/

        List<InwardItem> inwardItems = inwardItemRepository.findByInward(inwardId);

        List<InwardItemDto> inwardItemDtos = new ArrayList<>();

        for (InwardItem inwardItem : inwardItems) {

            InwardItemDto inwardItemDto = new InwardItemDto();
            inwardItemDto.setInwardItem(inwardItem);

            /*-------------------------- ON Hold Instances  ------------------------------*/

            List<InwardItemInstance> onHoldInwardInstances = inwardItemInstanceRepository.getOnHoldInwardInstances(inwardItem.getId());
            onHoldInwardInstances.forEach(inwardItemInstance -> {
                InwardItem item = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());
                inwardItemInstance.setBomItem(item.getBomItem());
                if (inwardItemInstance.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                    inwardItemDto.setFractionalOnHold(inwardItemDto.getFractionalOnHold() + inwardItemInstance.getItem().getLotSize());
                } else {
                    inwardItemDto.setOnHold(inwardItemDto.getOnHold() + 1);
                }

                ItemType itemType = itemTypeRepository.findOne(inwardItemInstance.getItem().getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    inwardItemInstance.getItem().getItem().getItemMaster().setParentType(itemType);
                } else {
                    inwardItemInstance.getItem().getItem().getItemMaster().setParentType(inwardItemInstance.getItem().getItem().getItemMaster().getItemType());
                }
            });
            inwardReportDto.getOnHoldInstances().addAll(onHoldInwardInstances);

            /*-------------------------- Returned Instances  ------------------------------*/

            List<InwardItemInstance> returnedInwardInstances = inwardItemInstanceRepository.getReturnedInwardInstances(inwardItem.getId());
            returnedInwardInstances.forEach(returnedInwardInstance -> {
                InwardItem item = inwardItemRepository.findOne(returnedInwardInstance.getInwardItem());
                returnedInwardInstance.setBomItem(item.getBomItem());

                if (returnedInwardInstance.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                    inwardItemDto.setFractionalReturned(inwardItemDto.getFractionalReturned() + returnedInwardInstance.getItem().getLotSize());
                } else {
                    inwardItemDto.setReturned(inwardItemDto.getReturned() + 1);
                }

                ItemType itemType = itemTypeRepository.findOne(returnedInwardInstance.getItem().getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    returnedInwardInstance.getItem().getItem().getItemMaster().setParentType(itemType);
                } else {
                    returnedInwardInstance.getItem().getItem().getItemMaster().setParentType(returnedInwardInstance.getItem().getItem().getItemMaster().getItemType());
                }
            });
            inwardReportDto.getReturnInstances().addAll(returnedInwardInstances);

             /*-------------------------- Failed Instances  ------------------------------*/

            List<InwardItemInstance> failedInwardInstances = inwardItemInstanceRepository.getFailedInwardInstances(inwardItem.getId());
            failedInwardInstances.forEach(failedInwardInstance -> {
                InwardItem item = inwardItemRepository.findOne(failedInwardInstance.getInwardItem());
                failedInwardInstance.setBomItem(item.getBomItem());

                if (failedInwardInstance.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                    inwardItemDto.setFractionalFailed(inwardItemDto.getFractionalFailed() + failedInwardInstance.getItem().getLotSize());
                } else {
                    inwardItemDto.setFailed(inwardItemDto.getFailed() + 1);
                }

                ItemType itemType = itemTypeRepository.findOne(failedInwardInstance.getItem().getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    failedInwardInstance.getItem().getItem().getItemMaster().setParentType(itemType);
                } else {
                    failedInwardInstance.getItem().getItem().getItemMaster().setParentType(failedInwardInstance.getItem().getItem().getItemMaster().getItemType());
                }
            });
            inwardReportDto.getFailedInstances().addAll(failedInwardInstances);

            /*---------------------------- Accepted Instance  --------------------------------*/

            List<InwardItemInstance> acceptedInstances = inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.ACCEPT);
            acceptedInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.INVENTORY));
            acceptedInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.VERIFIED));
            acceptedInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.ISSUE));
            acceptedInstances.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.P_ACCEPT));
//            acceptedInstances.addAll(inwardItemInstanceRepository.getProvisionallyAcceptedInwardItemInstances(inwardItem.getId()));

            acceptedInstances.forEach(acceptedInstance -> {
                InwardItem item = inwardItemRepository.findOne(acceptedInstance.getInwardItem());
                acceptedInstance.setBomItem(item.getBomItem());
                if (acceptedInstance.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                    inwardItemDto.setFractionalAcceptedQty(inwardItemDto.getFractionalAcceptedQty() + acceptedInstance.getItem().getLotSize());
                } else {
                    inwardItemDto.setAcceptedQty(inwardItemDto.getAcceptedQty() + 1);
                }

                ItemType itemType = itemTypeRepository.findOne(acceptedInstance.getItem().getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    acceptedInstance.getItem().getItem().getItemMaster().setParentType(itemType);
                } else {
                    acceptedInstance.getItem().getItem().getItemMaster().setParentType(acceptedInstance.getItem().getItem().getItemMaster().getItemType());
                }
            });

            inwardReportDto.getAcceptedInstances().addAll(acceptedInstances);

            inwardItemDtos.add(inwardItemDto);
        }

        inwardReportDto.getInwardItems().addAll(inwardItemDtos);

        return inwardReportDto;
    }

    @Transactional(readOnly = true)
    public Page<BomInwardReportDto> getBomInwardReport(Integer bomId, String searchText, Pageable pageable) {
        InwardItemCriteria itemCriteria = new InwardItemCriteria();
        itemCriteria.setSearchQuery(searchText);
        itemCriteria.setBom(bomId);

        Pageable pageable1 = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.ASC, "bomItem.item.itemMaster.itemName")));

        Predicate predicate = inwardItemPredicateBuilder.build(itemCriteria, QInwardItem.inwardItem);

        Page<InwardItem> inwardItems = inwardItemRepository.findAll(predicate, pageable1);

        LinkedHashMap<Integer, BomInwardReportDto> bomInwardReportDtoMap = new LinkedHashMap<>();

        List<BomInwardReportDto> inwardReportDto = new ArrayList<>();

        if (inwardItems.getContent().size() > 0) {
            for (InwardItem inwardItem : inwardItems) {
                BomInwardReportDto reportDto = bomInwardReportDtoMap.containsKey(inwardItem.getBomItem().getItem().getId()) ? bomInwardReportDtoMap.get(inwardItem.getBomItem().getItem().getId()) : new BomInwardReportDto();

                reportDto.setItemRevision(inwardItem.getBomItem().getItem());
                if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                    reportDto.setTotalFractionalInward(reportDto.getTotalFractionalInward() + inwardItem.getFractionalQuantity());
                } else {
                    reportDto.setTotalInward(reportDto.getTotalInward() + inwardItem.getQuantity());
                }
                reportDto.setBomItem(inwardItem.getBomItem());
                bomInwardReportDtoMap.put(inwardItem.getBomItem().getItem().getId(), reportDto);
            }
        }

        for (BomInwardReportDto reportDto : bomInwardReportDtoMap.values()) {

            HashMap<Integer, GatePassDto> gatePassMap = new HashMap<>();

            List<InwardItem> inwardItemList = inwardItemRepository.findByItemRevision(reportDto.getBomItem().getItem().getId());

            inwardItemList.forEach(inwardItem -> {
                Inward inward = inwardRepository.findOne(inwardItem.getInward());

                if (inward.getBom().getId().equals(bomId)) {
                    GatePassDto gatePassDto = gatePassMap.get(inward.getGatePass().getId());

                    if (gatePassDto != null) {
                        if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                            gatePassDto.setQuantity(gatePassDto.getQuantity() + inwardItem.getFractionalQuantity());
                        } else {
                            gatePassDto.setQuantity(gatePassDto.getQuantity() + inwardItem.getQuantity().doubleValue());
                        }

                        gatePassMap.put(inward.getGatePass().getId(), gatePassDto);
                    } else {
                        gatePassDto = new GatePassDto();
                        gatePassDto.setGatePass(inward.getGatePass());
                        if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                            gatePassDto.setQuantity(gatePassDto.getQuantity() + inwardItem.getFractionalQuantity());
                        } else {
                            gatePassDto.setQuantity(gatePassDto.getQuantity() + inwardItem.getQuantity().doubleValue());
                        }

                        gatePassMap.put(inward.getGatePass().getId(), gatePassDto);
                    }
                }
            });

            reportDto.getGatePasses().addAll(gatePassMap.values());

            inwardReportDto.add(reportDto);
        }

        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > inwardReportDto.size() ? inwardReportDto.size() : (start + pageable.getPageSize());

        Page<BomInwardReportDto> bomInwardReport = new PageImpl<BomInwardReportDto>(inwardReportDto.subList(start, end), pageable, inwardReportDto.size());

        return bomInwardReport;
    }


    /*-------------------------------------  Inward Status Update Methods --------------------------------------------*/

    @Transactional(readOnly = true)
    public List<GatePassDetailsDto> getGatePassDetails(Integer gatePassId) {

        List<GatePassDetailsDto> gatePassDetailsDto = new ArrayList<>();

        List<Inward> inwards = inwardRepository.getInwardsByGatePass(gatePassId);
        HashMap<Integer, GatePassDetailsDto> inwardItemHashMap = new HashMap<>();

        inwards.forEach(inward -> {
            List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward.getId());

            inwardItems.forEach(inwardItem -> {
                GatePassDetailsDto detailsDto = inwardItemHashMap.containsKey(inwardItem.getBomItem().getId()) ? inwardItemHashMap.get(inwardItem.getBomItem().getId()) : new GatePassDetailsDto();

                if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                    detailsDto.setFractionalQuantity(detailsDto.getFractionalQuantity() + inwardItem.getFractionalQuantity());
                } else {
                    detailsDto.setQuantity(detailsDto.getQuantity() + inwardItem.getQuantity());
                }

                detailsDto.setBomItem(inwardItem.getBomItem());
                detailsDto.getInwardItemInstances().addAll(inwardItemInstanceRepository.findByInwardItemAndLatestTrue(inwardItem.getId()));

                inwardItemHashMap.put(inwardItem.getBomItem().getId(), detailsDto);
            });
        });

        for (GatePassDetailsDto detailsDto : inwardItemHashMap.values()) {
            gatePassDetailsDto.add(detailsDto);
        }

        return gatePassDetailsDto;
    }

    /*-------------------------------------  Inward Items Methods ----------------------------------------------------*/

    @Transactional(readOnly = false)
    public Inward updateInwardStatus(Integer inwardId, Inward inward) {

        Inward presentInward = inwardRepository.findOne(inwardId);

        if (presentInward.getStatus().equals(InwardStatus.SSQAG)) {

            List<InwardItem> inwardItems = inwardItemRepository.findByInward(inwardId);
            List<InwardItemInstance> reviewItems = new ArrayList<>();

            inwardItems.forEach(inwardItem -> {
                reviewItems.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.REVIEW));
            });

            if (reviewItems.size() > 0) {
                throw new CassiniException("Some of the parts in review state. Please accept the parts");
            }
        }

        InwardStatus presentStatus = presentInward.getStatus();
        InwardStatus previousStatus = null;
        if (presentInward.getStatus().equals(InwardStatus.STORE)) {
            previousStatus = InwardStatus.SECURITY;
            inward.setStatus(InwardStatus.SSQAG);
        } else if (presentInward.getStatus().equals(InwardStatus.SSQAG)) {
            previousStatus = InwardStatus.STORE;
            inward.setStatus(InwardStatus.INVENTORY);
        } else if (presentInward.getStatus().equals(InwardStatus.INVENTORY)) {
            previousStatus = InwardStatus.SSQAG;
            inward.setStatus(InwardStatus.FINISH);
        }

        inward = inwardRepository.save(inward);

        InwardStatusHistory inwardStatusHistory = new InwardStatusHistory();
        inwardStatusHistory.setInward(inward.getId());
        inwardStatusHistory.setOldStatus(previousStatus);
        inwardStatusHistory.setNewStatus(presentStatus);
        inwardStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());
        inwardStatusHistory.setTimestamp(new Date());

        inwardStatusHistoryRepository.save(inwardStatusHistory);

        inward.setStatusHistories(inwardStatusHistoryRepository.findByInwardOrderByTimestampDesc(inwardId));

        return inward;
    }

    @Transactional(readOnly = false)
    public List<InwardItem> createInwardItems(Integer inwardId, List<InwardItem> inwardItems) {

        Inward inward = inwardRepository.findOne(inwardId);
        inward.setStatus(InwardStatus.STORE);
        inward = inwardRepository.save(inward);

        inwardItems = inwardItemRepository.save(inwardItems);

        InwardStatusHistory inwardStatusHistory = new InwardStatusHistory();
        inwardStatusHistory.setInward(inward.getId());
        inwardStatusHistory.setOldStatus(InwardStatus.SECURITY);
        inwardStatusHistory.setNewStatus(InwardStatus.STORE);
        inwardStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());
        inwardStatusHistory.setTimestamp(new Date());

        inwardStatusHistoryRepository.save(inwardStatusHistory);

        List<Storage> onHoldStorages = storageRepository.findByBomAndOnHoldTrueOrderByCreatedDateAsc(inward.getBom());

        if (onHoldStorages.size() == 0) {
            throw new CassiniException("No On Hold storage available to " + inward.getBom().getItem().getItemMaster().getItemName() + " BOM");
        }

        for (InwardItem item : inwardItems) {
            List<InwardItemInstance> currentInstances = inwardItemInstanceRepository.findByInwardItem(item.getId());

            if (item.getQuantity() > currentInstances.size()) {
                int count = item.getQuantity() - currentInstances.size();
                for (int i = 0; i < count; i++) {
                    InwardItemInstance inwardItemInstance = new InwardItemInstance();

                    BomItem bomItem = item.getBomItem();
                    ItemRevision itemRevision = bomItem.getItem();
                    ItemInstance instance = itemRevision.createInstance("0000000000000000");
                    instance.setStorage(onHoldStorages.get(0));
                    instance = itemInstanceRepository.save(instance);
                    BomItemInstance bomItemInstance = new BomItemInstance();
                    bomItemInstance.setItemInstance(instance.getId());
//                    bomItemInstance.setBomInstanceItem();
                    bomItemInstanceRepository.save(bomItemInstance);

                    inwardItemInstance.setInwardItem(item.getId());
                    inwardItemInstance.setItem(instance);

                    currentInstances.add(inwardItemInstance);
                }

                item.setInstancesCreated(true);
            }

            currentInstances = inwardItemInstanceRepository.save(currentInstances);
            item.setInstances(currentInstances);
        }

        return inwardItems;
    }

    @Transactional(readOnly = false)
    public InwardItem createInwardItem(Integer inwardId, InwardItem inwardItem) {

        Inward inward = inwardRepository.findOne(inwardId);

        String system = inward.getBom().getItem().getItemMaster().getItemCode();

        inwardItem = inwardItemRepository.save(inwardItem);

        if (inwardItem.getBomItem().getDefaultSection().getName().equals("Common")) {
            inwardItem.setSection(null);
        } else {
            BomGroup section = bomGroupRepository.findByTypeAndNameAndVersity(BomItemType.SECTION, inwardItem.getBomItem().getDefaultSection().getName(), inwardItem.getBomItem().getDefaultSection().getVersity());
            if (section == null) {
                section = bomGroupRepository.findByTypeAndNameAndVersity(BomItemType.COMMONPART, inwardItem.getBomItem().getDefaultSection().getName(), inwardItem.getBomItem().getDefaultSection().getVersity());
            }
            inwardItem.setSection(section);
        }

        /*List<StorageItem> onHoldStorages = new ArrayList<>();

        if (inwardItem.getBomItem().getDefaultSection().equals("Common")) {
            inwardItem.setSection(null);
            onHoldStorages = storageItemRepository.getItemOnHoldStoragesByBomAndSectionIsNull(inward.getBom().getId(), inwardItem.getBomItem().getUniqueCode());
        } else {
            BomGroup section = bomGroupRepository.findByTypeAndName(BomItemType.SECTION, inwardItem.getBomItem().getDefaultSection());
            inwardItem.setSection(section);
            onHoldStorages = storageItemRepository.getItemOnHoldStoragesByBomAndSection(inward.getBom().getId(), inwardItem.getBomItem().getUniqueCode(), section.getId());
        }

        if (onHoldStorages.size() == 0) {
            throw new CassiniException("No storage space available");
        }

        Storage matchingLocation = null;

        for (StorageItem storageItem : onHoldStorages) {
            if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                if (storageItem.getStorage().getRemainingCapacity() >= inwardItem.getFractionalQuantity()) {
                    matchingLocation = storageItem.getStorage();
                    break;
                }
            } else {
                if (storageItem.getStorage().getRemainingCapacity() >= Double.valueOf(inwardItem.getQuantity())) {
                    matchingLocation = storageItem.getStorage();
                    break;
                }
            }
        }

        if (matchingLocation == null) {
            throw new CassiniException("No storage space available");
        }*/

        if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {

            String section = "0";
            String subSystem = "0";
            String unit = "00";
            String part = inwardItem.getBomItem().getItem().getItemMaster().getItemCode();
            String spec = "0000";
            String specName = "";
            String serialNumber = "";

            if (inwardItem.getBomItem().getItem().getItemMaster().getPartSpec() != null) {
                specName = inwardItem.getBomItem().getItem().getItemMaster().getPartSpec().getSpecName();
            }

            if (specName.length() > 4) {
                serialNumber = specName.substring(specName.length() - 4);
            } else {
                serialNumber = specName;
            }

            spec = spec.substring(serialNumber.length()) + serialNumber;

            BomItem selectedBomItem = inwardItem.getBomItem();

            if (inwardItem.getSection() != null) {
                section = inwardItem.getSection().getCode();
                BomItem parent = bomItemRepository.findOne(selectedBomItem.getParent());
                if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                    unit = parent.getTypeRef().getCode();
                    BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                    if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        subSystem = parent1.getTypeRef().getCode();
                    }
                } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    subSystem = parent.getTypeRef().getCode();
                }
            } else {
                BomItem parent = bomItemRepository.findOne(selectedBomItem.getParent());
                if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                    unit = parent.getTypeRef().getCode();
                    BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                    if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        subSystem = parent1.getTypeRef().getCode();
                    }
                } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    subSystem = parent.getTypeRef().getCode();
                }
            }

            /*ItemType itemType = itemTypeService.get(inwardItem.getBomItem().getItem().getItemMaster().getItemType().getId());
            String sequenceNumber = getNextSeq(itemType.getSequenceNumber());

            if (sequenceNumber == null) {
                throw new CassiniException("Sequence number exceeds");
            }

            itemType.setSequenceNumber(sequenceNumber);

            itemType = itemTypeService.update(itemType);*/

            BomItem bomItem = inwardItem.getBomItem();
            ItemRevision itemRevision = bomItem.getItem();

            /*------------------ OLD ------------*/
//            ItemInstance instance = itemRevision.createInstance(system + "00" + section + "" + subSystem + unit + "" + part + spec + "" + sequenceNumber);

            /*---------------  New -------------*/
            ItemInstance instance = itemRevision.createInstance(system + "00" + section + "" + subSystem + "" + unit + "" + part);
//            instance.setLotNumber(sequenceNumber);/*----------  Sequence Number from ItemType -----------------*/
//            instance.setStorage(matchingLocation);
            instance.setLotSize(inwardItem.getFractionalQuantity());

            /*if (specName.equals("")) {
                instance.setOemNumber(spec);
            } else {
                instance.setOemNumber(specName);
            }*/

            instance.setUniqueCode(inwardItem.getBomItem().getUniqueCode());
            instance.setBom(inward.getBom().getId());
            if (inwardItem.getSection() != null) {
                instance.setSection(inwardItem.getSection().getId());
            }
            instance.setPresentStatus("NEW");
            instance = itemInstanceRepository.save(instance);

//            matchingLocation.setRemainingCapacity(matchingLocation.getRemainingCapacity() - inwardItem.getFractionalQuantity());

//            matchingLocation = storageRepository.save(matchingLocation);

            InwardItemInstance inwardItemInstance = new InwardItemInstance();

            inwardItemInstance.setInwardItem(inwardItem.getId());
            inwardItemInstance.setItem(instance);

            inwardItemInstance = inwardItemInstanceRepository.save(inwardItemInstance);

            ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
            statusHistory.setItemInstance(instance);
            statusHistory.setStatus(ItemInstanceStatus.NEW);
            statusHistory.setPresentStatus(ItemInstanceStatus.NEW.toString());
            statusHistory.setTimestamp(new Date());
            statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            statusHistory = instanceStatusHistoryRepository.save(statusHistory);

            inwardItem.setInstancesCreated(true);
            inwardItem = inwardItemRepository.save(inwardItem);

            inwardItem.setInstances(inwardItemInstanceRepository.findByInwardItem(inwardItem.getId()));

        } else {

            List<InwardItemInstance> currentInstances = inwardItemInstanceRepository.findByInwardItem(inwardItem.getId());

            String section = "0";
            String subSystem = "0";
            String unit = "00";
            String part = inwardItem.getBomItem().getItem().getItemMaster().getItemCode();
            String sequenceNumber = "00";

            if (inwardItem.getSection() != null) {
                BomItem selectedBomItem = inwardItem.getBomItem();
                section = inwardItem.getSection().getCode();
                BomItem parent = bomItemRepository.findOne(selectedBomItem.getParent());
                if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                    unit = parent.getTypeRef().getCode();
                    BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                    if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        subSystem = parent1.getTypeRef().getCode();
                    }
                } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    subSystem = parent.getTypeRef().getCode();
                }
            } else {
                BomItem selectedBomItem = inwardItem.getBomItem();
                BomItem parent = bomItemRepository.findOne(selectedBomItem.getParent());
                if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                    unit = parent.getTypeRef().getCode();
                    BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                    if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        subSystem = parent1.getTypeRef().getCode();
                    }
                } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    subSystem = parent.getTypeRef().getCode();
                }
            }

            if (inwardItem.getQuantity() > currentInstances.size()) {
                int count = inwardItem.getQuantity() - currentInstances.size();
                for (int i = 0; i < count; i++) {
                    InwardItemInstance inwardItemInstance = new InwardItemInstance();

                    BomItem bomItem = inwardItem.getBomItem();
                    ItemRevision itemRevision = bomItem.getItem();
                    /*---------------------  OLD ----------------*/
//                    ItemInstance instance = itemRevision.createInstance(system + "00" + section + "" + subSystem + "" + unit + "" + part + "0000" + "" + sequenceNumber);

                    /*---------------------  NEW ------------------*/
                    ItemInstance instance = itemRevision.createInstance(system + "00" + section + "" + subSystem + "" + unit + "" + part);
                    instance.setBom(inward.getBom().getId());
                    if (inwardItem.getSection() != null) {
                        instance.setSection(inwardItem.getSection().getId());
                    }
//                    instance.setStorage(matchingLocation);
                    instance.setPresentStatus("NEW");
                    instance.setUniqueCode(inwardItem.getBomItem().getUniqueCode());
                    instance = itemInstanceRepository.save(instance);

//                    matchingLocation.setRemainingCapacity(matchingLocation.getRemainingCapacity() - 1);
//                    matchingLocation = storageRepository.save(matchingLocation);

                    inwardItemInstance.setInwardItem(inwardItem.getId());
                    inwardItemInstance.setItem(instance);

                    ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
                    statusHistory.setItemInstance(instance);
                    statusHistory.setStatus(ItemInstanceStatus.NEW);
                    statusHistory.setPresentStatus(ItemInstanceStatus.NEW.toString());
                    statusHistory.setTimestamp(new Date());
                    statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

                    statusHistory = instanceStatusHistoryRepository.save(statusHistory);

                    currentInstances.add(inwardItemInstance);
                }

                inwardItem.setInstancesCreated(true);
            }

            currentInstances = inwardItemInstanceRepository.save(currentInstances);
            inwardItem.setInstances(currentInstances);
        }

        if (inwardItem.getInstancesCreated()) {
            Inventory inventory = null;
            if (inwardItem.getSection() == null) {
                inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode());
            } else {
                inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(inward.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
            }

            if (inventory == null) {
                inventory = new Inventory();
                inventory.setBom(inward.getBom().getId());
                inventory.setItem(inwardItem.getBomItem().getItem());
                inventory.setUniqueCode(inwardItem.getBomItem().getUniqueCode());
                if (inwardItem.getSection() != null) {
                    inventory.setSection(inwardItem.getSection().getId());
                }
                if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                    inventory.setFractionalQtyBuffered(inwardItem.getFractionalQuantity());
                } else {
                    inventory.setQtyBuffered(inwardItem.getQuantity());
                }
            } else {
                if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                    inventory.setFractionalQtyBuffered(inventory.getFractionalQtyBuffered() + inwardItem.getFractionalQuantity());
                } else {
                    inventory.setQtyBuffered(inventory.getQtyBuffered() + inwardItem.getQuantity());
                }
            }

            inventory = inventoryRepository.save(inventory);
        }

        inward.setModifiedDate(new Date());
        inward = inwardRepository.save(inward);

        return inwardItem;
    }

    @Transactional(readOnly = false)
    public InwardItem createInwardItemLot(Integer inwardId, InwardItem inwardItem) {

        Inward inward = inwardRepository.findOne(inwardId);

        List<InwardItemInstance> inwardItemInstances = inwardItem.getInstances();

        inwardItem = inwardItemRepository.save(inwardItem);

        List<Storage> onHoldStorages = storageRepository.findByBomAndOnHoldTrueOrderByCreatedDateAsc(inward.getBom());

        if (onHoldStorages.size() == 0) {
            throw new CassiniException("No On Hold storage available to " + inward.getBom().getItem().getItemMaster().getItemName() + " BOM");
        }
        List<InwardItemInstance> currentInstances = inwardItemInstanceRepository.findByInwardItem(inwardItem.getId());

        String system = inward.getBom().getItem().getItemMaster().getItemCode();
        String section = "0";
        String subSystem = "0";
        String unit = "00";
        String part = inwardItem.getBomItem().getItem().getItemMaster().getItemCode();
        String spec = "0000";
        String specName = "";
        String serialNumber = "";

        if (inwardItem.getBomItem().getItem().getItemMaster().getPartSpec() != null) {
            specName = inwardItem.getBomItem().getItem().getItemMaster().getPartSpec().getSpecName();
        }

        if (specName.length() > 4) {
            serialNumber = specName.substring(specName.length() - 4);
        } else {
            serialNumber = specName;
        }

        spec = spec.substring(serialNumber.length()) + serialNumber;

        String hierarchicalCode = inwardItem.getBomItem().getHierarchicalCode();

        String withOutSystemCode = section + "" + subSystem + "" + unit + "" + "" + part + "" + spec;

        if (inwardItem.getBomItem().getPathCount() == 1) {

            withOutSystemCode = inwardItem.getBomItem().getHierarchicalCode().substring(2) + "" + part + "" + spec;

            /*Bom bom = bomRepository.findOne(inward.getBom().getId());

            List<BomItem> bomChildren = bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId());
            for (BomItem bomChild : bomChildren) {
                inwardItem.getBomItem().setIdPath("");
                if (bomChild.getId().equals(inwardItem.getBomItem().getId())) {
                    inwardItem.getBomItem().setIdPath(bom.getItem().getId().toString());
                } else {
                    bomChild.setIdPath(bom.getItem().getId().toString());
                    if (visitChidrenPath(inwardItem, bomChild)) {
                        break;
                    }
                }
            }

            String idPath = inwardItem.getBomItem().getIdPath();
            String[] splitIds = idPath.split("/");

            for (String id : splitIds) {
                ItemRevision itemRevision = itemRevisionRepository.findOne(Integer.parseInt(id));
                ItemType itemType = itemTypeService.getParentType(itemRevision.getItemMaster().getItemType());
                if (itemType.getName().equals("Section")) {
                    section = itemRevision.getItemMaster().getTypeCode();
                } else if (itemType.getName().equals("Sub System")) {
                    subSystem = itemRevision.getItemMaster().getTypeCode();
                } else if (itemType.getName().equals("Unit")) {
                    unit = itemRevision.getItemMaster().getTypeCode();
                }
            }*/
        }

        for (InwardItemInstance inwardItemInstance : inwardItemInstances) {

            ItemType itemType = itemTypeService.get(inwardItem.getBomItem().getItem().getItemMaster().getItemType().getId());
            String sequenceNumber = getNextSeq(itemType.getSequenceNumber());

            if (sequenceNumber == null) {
                throw new CassiniException("Sequence number exceeds");
            }

            itemType.setSequenceNumber(sequenceNumber);

            itemType = itemTypeService.update(itemType);

            BomItem bomItem = inwardItem.getBomItem();
            ItemRevision itemRevision = bomItem.getItem();
            ItemInstance instance = itemRevision.createInstance(system + "00" + withOutSystemCode + sequenceNumber);
            instance.setStorage(onHoldStorages.get(0));
            instance.setLotSize(inwardItemInstance.getItem().getLotSize());
            instance = itemInstanceRepository.save(instance);


            instance.setOemNumber(spec);
            inwardItemInstance.setInwardItem(inwardItem.getId());
            inwardItemInstance.setItem(instance);

            ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
            statusHistory.setItemInstance(instance);
            statusHistory.setStatus(ItemInstanceStatus.NEW);
            statusHistory.setPresentStatus(ItemInstanceStatus.NEW.toString());
            statusHistory.setTimestamp(new Date());
            statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            statusHistory = instanceStatusHistoryRepository.save(statusHistory);

            currentInstances.add(inwardItemInstance);
        }

        inwardItem.setInstancesCreated(true);
        inwardItem = inwardItemRepository.save(inwardItem);

        currentInstances = inwardItemInstanceRepository.save(currentInstances);
        inwardItem.setInstances(currentInstances);

        Inventory inventory = inventoryRepository.findByBomAndItem(inward.getBom().getId(), inwardItem.getBomItem().getItem());

        if (inventory == null) {
            inventory = new Inventory();
            inventory.setBom(inward.getBom().getId());
            inventory.setItem(inwardItem.getBomItem().getItem());
            inventory.setUniqueCode(inwardItem.getBomItem().getUniqueCode());
            if (inwardItem.getSection() != null) {
                inventory.setSection(inwardItem.getSection().getId());
            }
            if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                inventory.setFractionalQtyBuffered(inwardItem.getFractionalQuantity());
            } else {
                inventory.setQtyBuffered(inwardItem.getQuantity());
            }
        } else {
            if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                inventory.setFractionalQtyBuffered(inventory.getFractionalQtyBuffered() + inwardItem.getFractionalQuantity());
            } else {
                inventory.setQtyBuffered(inventory.getQtyBuffered() + inwardItem.getQuantity());
            }
        }

        inventory = inventoryRepository.save(inventory);

        inwardItem.setInstances(inwardItemInstanceRepository.findByInwardItem(inwardItem.getId()));

        return inwardItem;
    }

    private Boolean visitChidrenPath(InwardItem inwardItem, BomItem bomChild) {
        Bom bom1 = bomRepository.findByItem(bomChild.getItem());
        Boolean itemExist = false;
        if (bom1 != null) {
            List<BomItem> bomItems = bomItemRepository.findByBomOrderByCreatedDateAsc(bom1.getId());
            for (BomItem item : bomItems) {
                if (item.getId().equals(inwardItem.getBomItem().getId())) {
                    itemExist = true;
                    inwardItem.getBomItem().setIdPath(bomChild.getIdPath() + "/" + bomChild.getItem().getId());
                    break;
                } else {
                    item.setIdPath(bomChild.getIdPath() + "/" + bomChild.getItem().getId());
                    itemExist = visitChidrenPath(inwardItem, item);
                    if (itemExist) {
                        break;
                    }
                }
            }

        }

        return itemExist;
    }

    @Transactional(readOnly = false)
    public void deleteInwardItem(Integer inwardId, Integer item, Boolean store) {

        Inward inward = inwardRepository.findOne(inwardId);
        InwardItem inwardItem = inwardItemRepository.findOne(item);

        List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.findByInwardItem(inwardItem.getId());

        if (store) {

            inwardItemInstances.forEach(inwardItemInstance -> {

                ItemInstance itemInstance = itemInstanceRepository.findOne(inwardItemInstance.getItem().getId());

                if (itemInstance.getStorage() != null) {
                    Storage storage = storageRepository.findOne(itemInstance.getStorage().getId());

                    if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                        storage.setRemainingCapacity(storage.getRemainingCapacity() + inwardItemInstance.getItem().getLotSize());
                    } else {
                        storage.setRemainingCapacity(storage.getRemainingCapacity() + 1);
                    }

                    storage = storageRepository.save(storage);
                }
                itemInstanceRepository.delete(inwardItemInstance.getItem().getId());
            });

            Inventory inventory = null;
            if (inwardItem.getSection() == null) {
                inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode());
            } else {
                inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(inward.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
            }

            if (inventory != null) {
                if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                    inventory.setFractionalQtyBuffered(inventory.getFractionalQtyBuffered() - inwardItem.getFractionalQuantity());
                } else {
                    inventory.setQtyBuffered(inventory.getQtyBuffered() - inwardItem.getQuantity());
                }

                inventory = inventoryRepository.save(inventory);
            }

            inwardItemRepository.delete(item);
        } else {

            Boolean issuedInstances = false;

            for (InwardItemInstance inwardItemInstance : inwardItemInstances) {
                ItemInstance itemInstance = itemInstanceRepository.findOne(inwardItemInstance.getItem().getId());

                if (!itemInstance.getStatus().equals(ItemInstanceStatus.STORE_SUBMITTED) && !itemInstance.getStatus().equals(ItemInstanceStatus.INVENTORY)
                        && !itemInstance.getStatus().equals(ItemInstanceStatus.ACCEPT) && !itemInstance.getStatus().equals(ItemInstanceStatus.P_ACCEPT)) {
                    issuedInstances = true;
                }
            }

            if (!issuedInstances) {
                inwardItemInstances.forEach(inwardItemInstance -> {

                    ItemInstance itemInstance = itemInstanceRepository.findOne(inwardItemInstance.getItem().getId());

                    if (itemInstance.getStorage() != null) {
                        Storage storage = storageRepository.findOne(itemInstance.getStorage().getId());

                        if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                            storage.setRemainingCapacity(storage.getRemainingCapacity() + inwardItemInstance.getItem().getLotSize());
                        } else {
                            storage.setRemainingCapacity(storage.getRemainingCapacity() + 1);
                        }

                        storage = storageRepository.save(storage);
                    }
                    itemInstanceRepository.delete(inwardItemInstance.getItem().getId());
                });

                Inventory inventory = null;
                if (inwardItem.getSection() == null) {
                    inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode());
                } else {
                    inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(inward.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
                }

                if (inventory != null) {
                    if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                        inventory.setFractionalQtyBuffered(inventory.getFractionalQtyBuffered() - inwardItem.getFractionalQuantity());
                    } else {
                        inventory.setQtyBuffered(inventory.getQtyBuffered() - inwardItem.getQuantity());
                    }

                    inventory = inventoryRepository.save(inventory);
                }

                inwardItemRepository.delete(item);
            } else {
                throw new CassiniException("Some Items already issued from this Inward Item. We can't delete this Inward Item");
            }
        }

    }

    @Transactional(readOnly = true)
    public List<InwardItem> getInwardItems(Integer inwardId) {
        Inward inward = inwardRepository.findOne(inwardId);
        List<InwardItem> inwardItems = new ArrayList<>();

        if (inward.getStatus().equals(InwardStatus.FINISH)) {
            inwardItems = inwardItemRepository.getInwardItemsOrderByItemName(inwardId);
        } else {
            inwardItems = inwardItemRepository.findByInwardOrderByCreatedDateAsc(inwardId);
        }

        for (InwardItem item : inwardItems) {
            item.setInstances(inwardItemInstanceRepository.findByInwardItem(item.getId()));

            item.getInstances().forEach(itemInstance -> {

                ItemType itemType = itemTypeRepository.findOne(itemInstance.getItem().getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    itemInstance.getItem().getItem().getItemMaster().setParentType(itemType);
                } else {
                    itemInstance.getItem().getItem().getItemMaster().setParentType(itemInstance.getItem().getItem().getItemMaster().getItemType());
                }

                String storageLocation = "";
                if (itemInstance.getItem().getStorage() != null && itemInstance.getItem().getStorage().getParent() != null) {
                    Storage parentStorage = storageRepository.findOne(itemInstance.getItem().getStorage().getParent());

                    if (parentStorage != null) {
                        storageLocation = parentStorage.getName() + "/ " + itemInstance.getItem().getStorage().getName();
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
                    itemInstance.getItem().setStoragePath(storageLocation);

                } else if (itemInstance.getItem().getStorage() != null) {
                    storageLocation = itemInstance.getItem().getStorage().getName();
                    itemInstance.getItem().setStoragePath(storageLocation);
                }
            });
        }

        return inwardItems;
    }

    private List<InwardItemInstance> setInwardItemInstances(InwardItem inwardItem) {
        List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.findByInwardItem(inwardItem.getId());

        inwardItemInstances.forEach(inwardItemInstance -> {
            List<ItemInstanceStatusHistory> statusHistories = instanceStatusHistoryRepository.getItemInstanceHistory(inwardItemInstance.getItem().getId());

            if (!statusHistories.get(0).getStatus().equals(ItemInstanceStatus.ACCEPT)) {
                ItemInstanceStatusHistory inventoryHistory = instanceStatusHistoryRepository.getHistoryByInstanceAndStatus(inwardItemInstance.getItem().getId(), ItemInstanceStatus.INVENTORY);
                if (inventoryHistory != null) {

                    Integer inventoryIndex = statusHistories.indexOf(inventoryHistory);

                    if (!inventoryIndex.equals(0)) {
                        ItemInstanceStatusHistory acceptHistory = instanceStatusHistoryRepository.getHistoryByInstanceAndStatus(inwardItemInstance.getItem().getId(), ItemInstanceStatus.ACCEPT);

                        if (acceptHistory == null) {
                            ItemInstanceStatusHistory previousHistory = statusHistories.get(inventoryIndex + 1);

                            if (previousHistory.getStatus().equals(ItemInstanceStatus.P_ACCEPT)) {
//                                inwardItemInstance.setProvAcceptHistory(previousHistory);
                            } else if (previousHistory.getStatus().equals(ItemInstanceStatus.REVIEWED)) {
                                ItemInstanceStatusHistory provHistory = statusHistories.get(inventoryIndex + 2);
                                if (provHistory != null) {
                                    if (provHistory.getStatus().equals(ItemInstanceStatus.P_ACCEPT)) {
//                                        inwardItemInstance.setProvAcceptHistory(previousHistory);
                                    }
                                }
                            }
                        }

                    } else {
                        ItemInstanceStatusHistory previousHistory = statusHistories.get(inventoryIndex + 1);

                        if (previousHistory.getStatus().equals(ItemInstanceStatus.P_ACCEPT)) {
//                            inwardItemInstance.setProvAcceptHistory(previousHistory);
                        } else if (previousHistory.getStatus().equals(ItemInstanceStatus.REVIEWED)) {
                            ItemInstanceStatusHistory provHistory = statusHistories.get(inventoryIndex + 2);
                            if (provHistory != null) {
                                if (provHistory.getStatus().equals(ItemInstanceStatus.P_ACCEPT)) {
//                                    inwardItemInstance.setProvAcceptHistory(previousHistory);
                                }
                            }
                        }
                    }


                } else {
                    List<ItemInstanceStatusHistory> reviewedHistory = instanceStatusHistoryRepository.getReviewOrPacceptHistoryByInstanceAndStatus(inwardItemInstance.getItem().getId(), ItemInstanceStatus.REVIEWED);
                    if (reviewedHistory.size() > 0) {
                        Integer reviewedIndex = statusHistories.indexOf(reviewedHistory.get(0));

                        ItemInstanceStatusHistory previousHistory = statusHistories.get(reviewedIndex + 1);
                        if (previousHistory.getStatus().equals(ItemInstanceStatus.P_ACCEPT)) {
//                            inwardItemInstance.setProvAcceptHistory(previousHistory);
                        }
                    }
                }
            }
        });

        return inwardItemInstances;
    }

    @Transactional(readOnly = false)
    public InwardItem acceptInwardItem(Integer inward, Integer itemId, InwardItem inwardItem) {

        List<InwardItemInstance> itemInstances = inwardItemInstanceRepository.findByInwardItem(itemId);
        itemInstances.forEach(inwardItemInstance -> {
            if (inwardItemInstance.getItem().getStatus().equals(ItemInstanceStatus.STORE_SUBMITTED)) {

                ItemInstance itemInstance = inwardItemInstance.getItem();
                itemInstance.setStatus(ItemInstanceStatus.ACCEPT);
                itemInstance.setPresentStatus(ItemInstanceStatus.ACCEPT.toString());
                itemInstance.setProvisionalAccept(false);
                itemInstance = itemInstanceRepository.save(itemInstance);

                inwardItemInstance = inwardItemInstanceRepository.save(inwardItemInstance);

                ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
                statusHistory.setItemInstance(itemInstance);
                statusHistory.setStatus(ItemInstanceStatus.ACCEPT);
                statusHistory.setPresentStatus(ItemInstanceStatus.ACCEPT.toString());
                statusHistory.setTimestamp(new Date());
                statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

                statusHistory = instanceStatusHistoryRepository.save(statusHistory);
            }
        });
        inwardItem.setAccepted(true);
        inwardItem = inwardItemRepository.save(inwardItem);
        inwardItem.setInstances(inwardItemInstanceRepository.findByInwardItem(inwardItem.getId()));

        Inward inward1 = updateInwardStatusByInstances(inward);

        return inwardItem;
    }

    private Inward updateInwardStatusByInstances(Integer inward) {

        Inward inward1 = inwardRepository.findOne(inward);

        List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward);
        List<InwardItemInstance> provisionalOrSubmittedInstances = new ArrayList<>();
        List<InwardItemInstance> reviewItems = new ArrayList<>();
        List<InwardItemInstance> returnItems = new ArrayList<>();

        List<InwardItemInstance> acceptItems = new ArrayList<>();
        List<InwardItemInstance> inventoryItems = new ArrayList<>();
        List<InwardItemInstance> provisionallyAcceptedItems = new ArrayList<>();

        Boolean provisionallyInventoryItems = false;
        for (InwardItem inwardItem1 : inwardItems) {
            provisionalOrSubmittedInstances.addAll(inwardItemInstanceRepository.getProvisionalOrSubmittedInstances(inwardItem1.getId(),
                    ItemInstanceStatus.P_ACCEPT, ItemInstanceStatus.STORE_SUBMITTED));

            reviewItems.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem1.getId(), ItemInstanceStatus.REVIEW));
            reviewItems.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem1.getId(), ItemInstanceStatus.REVIEWED));
            returnItems.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem1.getId(), ItemInstanceStatus.REJECTED));

            acceptItems.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem1.getId(), ItemInstanceStatus.ACCEPT));
            inventoryItems.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem1.getId(), ItemInstanceStatus.INVENTORY));
            provisionallyAcceptedItems.addAll(inwardItemInstanceRepository.getProvisionallyAcceptedInwardItemInstances(inwardItem1.getId()));
        }

        InwardStatus presentStatus = inward1.getStatus();

        if (presentStatus.equals(InwardStatus.SSQAG)) {
            if (provisionalOrSubmittedInstances.size() == 0 && reviewItems.size() == 0 && provisionallyAcceptedItems.size() == 0) {
                inward1.setStatus(InwardStatus.INVENTORY);
                inward1.setUnderReview(false);

                inward1 = inwardRepository.save(inward1);

                InwardStatusHistory inwardStatusHistory = new InwardStatusHistory();
                inwardStatusHistory.setInward(inward);
                inwardStatusHistory.setOldStatus(InwardStatus.STORE);
                inwardStatusHistory.setNewStatus(presentStatus);
                inwardStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());
                inwardStatusHistory.setTimestamp(new Date());

                inwardStatusHistoryRepository.save(inwardStatusHistory);
            } else if (reviewItems.size() == 0) {
                inward1.setUnderReview(false);

                inward1 = inwardRepository.save(inward1);
            }

            presentStatus = inward1.getStatus();
        }

        if (presentStatus.equals(InwardStatus.INVENTORY)) {
            if (acceptItems.size() == 0 && /*inventoryItems.size() == 0 &&*/ provisionallyAcceptedItems.size() == 0) {
                inward1.setStatus(InwardStatus.FINISH);
                inward1.setUnderReview(false);

                inward1 = inwardRepository.save(inward1);

                InwardStatusHistory inwardStatusHistory = new InwardStatusHistory();
                inwardStatusHistory.setInward(inward);
                inwardStatusHistory.setOldStatus(InwardStatus.SSQAG);
                inwardStatusHistory.setNewStatus(presentStatus);
                inwardStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());
                inwardStatusHistory.setTimestamp(new Date());

                inwardStatusHistoryRepository.save(inwardStatusHistory);
                Person person = personRepository.findOne(inward1.getModifiedBy());

                String items = null;
                List<InwardItem> inwardItems1 = inwardItemRepository.findByInward(inward1.getId());
                for (InwardItem inwardItem : inwardItems1) {
                    if (items == null) {
                        if (inwardItem.getSection() != null) {
                            items = inwardItem.getBomItem().getItem().getItemMaster().getItemName() + " with " + inwardItem.getSection().getName();
                        } else {
                            items = inwardItem.getBomItem().getItem().getItemMaster().getItemName() + " as common part";
                        }
                    } else {
                        if (inwardItem.getSection() != null) {
                            items = items + ", " + inwardItem.getBomItem().getItem().getItemMaster().getItemName() + " with " + inwardItem.getSection().getName();
                        } else {
                            items = items + ", " + inwardItem.getBomItem().getItem().getItemMaster().getItemName() + " as common part";
                        }
                    }
                }

                String message = person.getFullName() + " finished " + inward1.getNumber() + " Inward with " + items;

                drdoUpdatesService.updateMessage(message, DRDOObjectType.INWARD);
            }
        }

        return inward1;
    }

    @Transactional(readOnly = false)
    public InwardItem provisionalAcceptItem(Integer inward, Integer itemId, String provReason, InwardItem inwardItem) {

        List<InwardItemInstance> itemInstances = inwardItemInstanceRepository.findByInwardItem(itemId);
        itemInstances.forEach(inwardItemInstance -> {
            if (inwardItemInstance.getItem().getStatus().equals(ItemInstanceStatus.STORE_SUBMITTED)) {
                ItemInstance itemInstance = inwardItemInstance.getItem();
                itemInstance.setStatus(ItemInstanceStatus.P_ACCEPT);
                itemInstance.setPresentStatus("P ACCEPT");
                itemInstance.setProvisionalAccept(true);

                itemInstance = itemInstanceRepository.save(itemInstance);

                inwardItemInstance = inwardItemInstanceRepository.save(inwardItemInstance);

                ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
                statusHistory.setItemInstance(itemInstance);
                statusHistory.setStatus(ItemInstanceStatus.P_ACCEPT);
                statusHistory.setPresentStatus("P ACCEPT");
                statusHistory.setTimestamp(new Date());
                statusHistory.setComment(provReason);
                statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

                statusHistory = instanceStatusHistoryRepository.save(statusHistory);
            }
        });

        inwardItem.setpAccepted(true);
        inwardItem = inwardItemRepository.save(inwardItem);

        inwardItem.setInstances(inwardItemInstanceRepository.findByInwardItem(inwardItem.getId()));

        Inward inward1 = updateInwardStatusByInstances(inward);
        inward1 = inwardRepository.save(inward1);

        return inwardItem;
    }

    @Transactional(readOnly = false)
    public InwardItem allocateStorageToInwardItem(Integer inwardId, InwardItem inwardItem) {

        Inward inward = inwardRepository.findOne(inwardItem.getInward());

        Integer typeId = inwardItem.getBomItem().getItem().getItemMaster().getItemType().getId();

        if (!inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
            List<StorageItem> binStores = new ArrayList<>();
            if (inwardItem.getSection() == null) {
                binStores = storageItemRepository.
                        getItemStorageByBomAndTypeAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), StorageType.BIN, inwardItem.getBomItem().getUniqueCode());
            } else {
                binStores = storageItemRepository.
                        getItemStorageByBomAndTypeAndUniqueCodeAndSection(inward.getBom().getId(), StorageType.BIN, inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
            }

            if (binStores.size() > 0) {
                inwardItem = allocateStorageByItemAndStores(inwardItem, binStores);
            } else {
                List<StorageItem> shelfStores = new ArrayList<>();
                if (inwardItem.getSection() == null) {
                    shelfStores = storageItemRepository.
                            getItemStorageByBomAndTypeAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), StorageType.SHELF, inwardItem.getBomItem().getUniqueCode());
                } else {
                    shelfStores = storageItemRepository.
                            getItemStorageByBomAndTypeAndUniqueCodeAndSection(inward.getBom().getId(), StorageType.SHELF, inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
                }
                if (shelfStores.size() > 0) {
                    inwardItem = allocateStorageByItemAndStores(inwardItem, shelfStores);
                } else {
                    List<StorageItem> rackStores = new ArrayList<>();
                    if (inwardItem.getSection() == null) {
                        rackStores = storageItemRepository.
                                getItemStorageByBomAndTypeAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), StorageType.RACK, inwardItem.getBomItem().getUniqueCode());
                    } else {
                        rackStores = storageItemRepository.
                                getItemStorageByBomAndTypeAndUniqueCodeAndSection(inward.getBom().getId(), StorageType.RACK, inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
                    }
                    if (rackStores.size() > 0) {
                        inwardItem = allocateStorageByItemAndStores(inwardItem, rackStores);
                    } else {
                        List<StorageItem> areaStores = new ArrayList<>();
                        if (inwardItem.getSection() == null) {
                            areaStores = storageItemRepository.
                                    getItemStorageByBomAndTypeAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), StorageType.AREA, inwardItem.getBomItem().getUniqueCode());
                        } else {
                            areaStores = storageItemRepository.
                                    getItemStorageByBomAndTypeAndUniqueCodeAndSection(inward.getBom().getId(), StorageType.AREA, inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
                        }
                        if (areaStores.size() > 0) {
                            inwardItem = allocateStorageByItemAndStores(inwardItem, areaStores);
                        } else {
                            throw new CassiniException("No storage space available");
                        }
                    }
                }
            }
        } else {
            List<StorageItem> binStores = new ArrayList<>();
            if (inwardItem.getSection() == null) {
                binStores = storageItemRepository.
                        getItemStorageByBomAndTypeAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), StorageType.BIN, inwardItem.getBomItem().getUniqueCode());
            } else {
                binStores = storageItemRepository.
                        getItemStorageByBomAndTypeAndUniqueCodeAndSection(inward.getBom().getId(), StorageType.BIN, inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
            }

            if (binStores.size() > 0) {
                inwardItem = allocateStorageByItemAndStores(inwardItem, binStores);
            } else {
                List<StorageItem> shelfStores = new ArrayList<>();
                if (inwardItem.getSection() == null) {
                    shelfStores = storageItemRepository.
                            getItemStorageByBomAndTypeAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), StorageType.SHELF, inwardItem.getBomItem().getUniqueCode());
                } else {
                    shelfStores = storageItemRepository.
                            getItemStorageByBomAndTypeAndUniqueCodeAndSection(inward.getBom().getId(), StorageType.SHELF, inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
                }

                if (shelfStores.size() > 0) {
                    inwardItem = allocateStorageByItemAndStores(inwardItem, shelfStores);
                } else {
                    throw new CassiniException("No storage space available");
                }
            }
        }

        inwardItem.setInstances(inwardItemInstanceRepository.findByInwardItem(inwardItem.getId()));

        inwardItem.getInstances().forEach(itemInstance -> {
            String storageLocation = "";
            if (itemInstance.getItem().getStorage() != null && itemInstance.getItem().getStorage().getParent() != null) {
                Storage parentStorage = storageRepository.findOne(itemInstance.getItem().getStorage().getParent());

                if (parentStorage != null) {
                    storageLocation = parentStorage.getName() + "/ " + itemInstance.getItem().getStorage().getName();
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
                itemInstance.getItem().setStoragePath(storageLocation);

            } else if (itemInstance.getItem().getStorage() != null) {
                storageLocation = itemInstance.getItem().getStorage().getName();
                itemInstance.getItem().setStoragePath(storageLocation);
            }
        });

        Inward inward1 = updateInwardStatusByInstances(inwardId);

        inward1.setModifiedDate(new Date());

        inward1 = inwardRepository.save(inward1);

        return inwardItem;
    }

    /*-----------------------------------  Inward Item Instance Methods ----------------------------------------------*/

    private InwardItem allocateStorageByItemAndStores(InwardItem inwardItem, List<StorageItem> storageItems) {

        Storage matchingLocation = null;
        Inward inward = inwardRepository.findOne(inwardItem.getInward());

        List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.getAcceptedItemInstances(inwardItem.getId());
        Double totalInwardLotQuantity = 0.0;

        if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
            for (InwardItemInstance inwardItemInstance : inwardItemInstances) {
                totalInwardLotQuantity = totalInwardLotQuantity + inwardItemInstance.getItem().getLotSize();
            }

            for (StorageItem storageItem : storageItems) {
                if (storageItem.getStorage().getRemainingCapacity() >= totalInwardLotQuantity) {
                    matchingLocation = storageItem.getStorage();
                    break;
                }
            }

        } else {
            for (StorageItem storageItemType : storageItems) {
                if (storageItemType.getStorage().getRemainingCapacity() >= inwardItemInstances.size()) {
                    matchingLocation = storageItemType.getStorage();
                    break;
                }
            }
        }


        if (matchingLocation == null) {
            throw new CassiniException("No storage space available");
        }

        for (InwardItemInstance inwardItemInstance : inwardItemInstances) {

            ItemInstance itemInstance = inwardItemInstance.getItem();

            Storage presentStorage = null;

            if (itemInstance.getStorage() != null) {
                presentStorage = storageRepository.findOne(itemInstance.getStorage().getId());
            }

            itemInstance.setStorage(matchingLocation);
            itemInstance.setStatus(ItemInstanceStatus.INVENTORY);
            itemInstance.setPresentStatus(ItemInstanceStatus.INVENTORY.toString());
            itemInstance = itemInstanceRepository.save(itemInstance);

            if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {

                Storage allocatedStorage = itemInstance.getStorage();
                allocatedStorage.setRemainingCapacity(allocatedStorage.getRemainingCapacity() - inwardItemInstance.getItem().getLotSize());
                allocatedStorage = storageRepository.save(allocatedStorage);

                if (presentStorage != null) {
                    presentStorage.setRemainingCapacity(presentStorage.getRemainingCapacity() + inwardItemInstance.getItem().getLotSize());
                    presentStorage = storageRepository.save(presentStorage);
                }
            } else {

                Storage allocatedStorage = itemInstance.getStorage();
                allocatedStorage.setRemainingCapacity(allocatedStorage.getRemainingCapacity() - 1);
                allocatedStorage = storageRepository.save(allocatedStorage);
                if (presentStorage != null) {
                    presentStorage.setRemainingCapacity(presentStorage.getRemainingCapacity() + 1);
                    presentStorage = storageRepository.save(presentStorage);
                }
            }

            Inventory inventory = null;
            if (inwardItem.getSection() == null) {
                inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode());
            } else {
                inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(inward.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
            }
            if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                if (inventory.getFractionalQtyBuffered() >= inwardItemInstance.getItem().getLotSize()) {
                    inventory.setFractionalQtyBuffered(inventory.getFractionalQtyBuffered() - inwardItemInstance.getItem().getLotSize());
                    inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() + inwardItemInstance.getItem().getLotSize());
                }
                inventory = inventoryRepository.save(inventory);
            } else {
                if (inventory.getQtyBuffered() > 0) {
                    inventory.setQtyBuffered(inventory.getQtyBuffered() - 1);
                    inventory.setQuantity(inventory.getQuantity() + 1);
                }
                inventory = inventoryRepository.save(inventory);
            }

            ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
            statusHistory.setItemInstance(itemInstance);
            statusHistory.setStatus(ItemInstanceStatus.INVENTORY);
            statusHistory.setPresentStatus(ItemInstanceStatus.INVENTORY.toString());
            statusHistory.setTimestamp(new Date());
            statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            statusHistory = instanceStatusHistoryRepository.save(statusHistory);
        }

        inwardItem.setInstances(inwardItemInstanceRepository.findByInwardItem(inwardItem.getId()));

        return inwardItem;
    }

    @Transactional(readOnly = false)
    public InwardItemInstance updateInwardItemInstance(Integer inwardId, InwardItemInstance inwardItemInstance) {
       /* InwardItemInstance existOemNumberInstance = null;
        if (!inwardItemInstance.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
            existOemNumberInstance = inwardItemInstanceRepository.findOemNumberInstanceByMfrCode(inwardItemInstance.getItem().getOemNumber(), inwardItemInstance.getItem().getManufacturer().getMfrCode());
        }
        InwardItemInstance existUpnNumberInstance = inwardItemInstanceRepository.findUpnNumberInstance(inwardItemInstance.getItem().getInitialUpn());

        if (existOemNumberInstance != null && !existOemNumberInstance.getId().equals(inwardItemInstance.getId())) {
            throw new CassiniException(inwardItemInstance.getItem().getOemNumber() + " Serial Number already exist. Please check the serial number once");
        }

        if (existUpnNumberInstance != null && !existUpnNumberInstance.getId().equals(inwardItemInstance.getId())) {
            throw new CassiniException(inwardItemInstance.getItem().getInitialUpn() + " : UPN already exist. Please check the serial number once.");
        }*/


        ItemInstance itemInstance = inwardItemInstance.getItem();

        ItemInstance existInstance = itemInstanceRepository.getInstanceByItemAndMfrAndOemNumber(itemInstance.getItem().getId(), itemInstance.getManufacturer().getId(), itemInstance.getOemNumber());

        if (existInstance != null && !existInstance.getId().equals(itemInstance.getId())) {
            throw new CassiniException(itemInstance.getOemNumber() + " Serial Number already exist. Please check the serial number once");
        }

        ItemInstance returningItemInstance = itemInstanceRepository.findOne(inwardItemInstance.getItem().getId());
        if (returningItemInstance != null && returningItemInstance.getReturningPart().equals(true)) {
            List<InwardItemInstance> existInwardItemInstances = inwardItemInstanceRepository.getInwardItemInstancesByInstanceAndLatestFalse(inwardItemInstance.getItem().getId());

            if (existInwardItemInstances.size() > 0) {
                if (!inwardItemInstance.getItem().getInitialUpn().equals(existInwardItemInstances.get(0).getItem().getInitialUpn())) {
                    List<ItemInstanceStatusHistory> statusHistories = instanceStatusHistoryRepository.getItemInstanceHistory(returningItemInstance.getId());

                    ItemInstance copyInstance = (ItemInstance) Utils.cloneObject(inwardItemInstance.getItem(), ItemInstance.class);

                    instanceStatusHistoryRepository.delete(statusHistories.get(0).getId());
                    instanceStatusHistoryRepository.delete(statusHistories.get(1).getId());

                    itemInstance = new ItemInstance();

                    itemInstance.setItem(copyInstance.getItem());
                    itemInstance.setStatus(ItemInstanceStatus.STORE_SUBMITTED);
                    itemInstance.setPresentStatus("STORE SUBMITTED");
                    itemInstance.setReturningPart(false);
                    itemInstance.setBom(copyInstance.getBom());
                    itemInstance.setSection(copyInstance.getSection());
                    itemInstance.setInstanceName(copyInstance.getInitialUpn());
                    itemInstance.setUpnNumber(copyInstance.getInitialUpn());
                    itemInstance.setOemNumber(copyInstance.getOemNumber());
                    itemInstance.setInitialUpn(copyInstance.getInitialUpn());
                    itemInstance.setManufacturer(copyInstance.getManufacturer());
                    itemInstance.setStorage(copyInstance.getStorage());

                    itemInstance = itemInstanceRepository.save(itemInstance);

                    ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
                    statusHistory.setItemInstance(itemInstance);
                    statusHistory.setStatus(ItemInstanceStatus.NEW);
                    statusHistory.setPresentStatus(ItemInstanceStatus.NEW.toString());
                    statusHistory.setTimestamp(returningItemInstance.getModifiedDate());
                    statusHistory.setComment(itemInstance.getReason());
                    statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

                    statusHistory = instanceStatusHistoryRepository.save(statusHistory);

                    inwardItemInstance.setItem(itemInstance);

                    returningItemInstance.setReturningPart(false);
                    returningItemInstance = itemInstanceRepository.save(returningItemInstance);
                }
            }

        }

        ItemInstanceStatusHistory presentStatus = instanceStatusHistoryRepository.getItemInstanceHistory(inwardItemInstance.getItem().getId()).get(0);

        ItemInstanceStatus presentItemInstanceStatus = itemInstance.getStatus();

        itemInstance.setInstanceName(itemInstance.getInitialUpn());
        itemInstance.setInitialUpn(itemInstance.getInitialUpn());
        if (!itemInstance.getReturningPart() && !presentItemInstanceStatus.equals(ItemInstanceStatus.APPROVED) && !presentItemInstanceStatus.equals(ItemInstanceStatus.P_APPROVED)
                && !presentItemInstanceStatus.equals(ItemInstanceStatus.ISSUE) && !presentItemInstanceStatus.equals(ItemInstanceStatus.FAILURE)) {
            itemInstance.setUpnNumber(itemInstance.getInitialUpn());
        }

        if (presentItemInstanceStatus != null && presentItemInstanceStatus.equals(ItemInstanceStatus.NEW)) {
            itemInstance.setStatus(ItemInstanceStatus.STORE_SUBMITTED);
            itemInstance.setPresentStatus("STORE SUBMITTED");
        } else if (presentItemInstanceStatus != null && itemInstance.getProvisionalAccept()) {

        } else if (presentItemInstanceStatus != null && !presentItemInstanceStatus.equals(ItemInstanceStatus.NEW)
                && !presentItemInstanceStatus.equals(ItemInstanceStatus.STORE_SUBMITTED)) {
            itemInstance.setStatus(ItemInstanceStatus.REVIEWED);
            itemInstance.setPresentStatus(ItemInstanceStatus.REVIEWED.toString());
        }

        itemInstance = itemInstanceRepository.save(itemInstance);

        if (presentStatus != null && presentStatus.getStatus().equals(ItemInstanceStatus.NEW)) {
            ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
            statusHistory.setItemInstance(itemInstance);
            statusHistory.setStatus(ItemInstanceStatus.STORE_SUBMITTED);
            statusHistory.setPresentStatus("STORE SUBMITTED");
            statusHistory.setTimestamp(new Date());
            statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            statusHistory = instanceStatusHistoryRepository.save(statusHistory);
        } else if (presentStatus != null && itemInstance.getProvisionalAccept()) {
            ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
            statusHistory.setItemInstance(itemInstance);
            statusHistory.setStatus(ItemInstanceStatus.REVIEWED);
            statusHistory.setPresentStatus(ItemInstanceStatus.REVIEWED.toString());
            statusHistory.setTimestamp(new Date());
            statusHistory.setComment(itemInstance.getReason());
            statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            statusHistory = instanceStatusHistoryRepository.save(statusHistory);
        } else if (presentStatus != null && !presentStatus.getStatus().equals(ItemInstanceStatus.NEW) && !presentStatus.getStatus().equals(ItemInstanceStatus.STORE_SUBMITTED)
                && !presentStatus.getStatus().equals(ItemInstanceStatus.REVIEWED)) {
            ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
            statusHistory.setItemInstance(itemInstance);
            statusHistory.setStatus(ItemInstanceStatus.REVIEWED);
            statusHistory.setPresentStatus(ItemInstanceStatus.REVIEWED.toString());
            statusHistory.setTimestamp(new Date());
            statusHistory.setComment(itemInstance.getReason());
            statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            statusHistory = instanceStatusHistoryRepository.save(statusHistory);
        }


        inwardItemInstance.setItem(itemInstance);

        Inward inward = inwardRepository.findOne(inwardId);

        inward.setModifiedDate(new Date());
        inward = inwardRepository.save(inward);

        return inwardItemInstance;
    }


    @Transactional(readOnly = false)
    public InwardItemInstance saveInwardItemInstance(Integer inwardId, InwardItemInstance inwardItemInstance) {

        ItemInstance itemInstance = inwardItemInstance.getItem();

        ItemInstance existInstance = itemInstanceRepository.getInstanceByItemAndMfrAndOemNumber(itemInstance.getItem().getId(), itemInstance.getManufacturer().getId(), itemInstance.getOemNumber());

        if (existInstance != null && !existInstance.getId().equals(itemInstance.getId())) {
            throw new CassiniException(itemInstance.getOemNumber() + " Serial Number already exist. Please check the serial number once");
        }

        itemInstance.setInstanceName(itemInstance.getInitialUpn());
        itemInstance.setInitialUpn(itemInstance.getInitialUpn());

        itemInstance = itemInstanceRepository.save(itemInstance);

        inwardItemInstance.setItem(itemInstance);

        Inward inward = inwardRepository.findOne(inwardId);

        inward.setModifiedDate(new Date());
        inward = inwardRepository.save(inward);

        return inwardItemInstance;
    }

    @Transactional(readOnly = false)
    public InwardItemInstance updateReturnedInwardItemInstance(Integer inwardId, InwardItemInstance inwardItemInstance, Integer updateId) {
        InwardItemInstance existOemNumberInstance = null;

        ItemInstance returnedInstance = itemInstanceRepository.findOne(updateId);
        ItemInstanceStatusHistory presentStatus = instanceStatusHistoryRepository.getItemInstanceHistory(updateId).get(0);

        returnedInstance.setInstanceName(returnedInstance.getInitialUpn());
        returnedInstance.setInitialUpn(returnedInstance.getInitialUpn());

        if (presentStatus != null && presentStatus.getStatus().equals(ItemInstanceStatus.DISPATCH)) {
            returnedInstance.setStatus(ItemInstanceStatus.STORE_SUBMITTED);
            returnedInstance.setPresentStatus("STORE SUBMITTED");
        }
        returnedInstance.setStorage(inwardItemInstance.getItem().getStorage());
        returnedInstance.setReturningPart(true);
        returnedInstance = itemInstanceRepository.save(returnedInstance);

        itemInstanceRepository.delete(inwardItemInstance.getItem().getId());

        if (presentStatus != null && presentStatus.getStatus().equals(ItemInstanceStatus.DISPATCH)) {
            ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
            statusHistory.setItemInstance(returnedInstance);
            statusHistory.setStatus(ItemInstanceStatus.NEW);
            statusHistory.setPresentStatus("NEW");
            statusHistory.setTimestamp(new Date());
            statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            statusHistory = instanceStatusHistoryRepository.save(statusHistory);

            statusHistory = new ItemInstanceStatusHistory();
            statusHistory.setItemInstance(returnedInstance);
            statusHistory.setStatus(ItemInstanceStatus.STORE_SUBMITTED);
            statusHistory.setPresentStatus("STORE SUBMITTED");
            statusHistory.setTimestamp(new Date());
            statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            statusHistory = instanceStatusHistoryRepository.save(statusHistory);
        }

        inwardItemInstance.setItem(returnedInstance);

        inwardItemInstance.setLatest(true);
        inwardItemInstance = inwardItemInstanceRepository.save(inwardItemInstance);

        List<InwardItemInstance> existItemInstances = inwardItemInstanceRepository.getInwardItemInstancesByInstance(returnedInstance.getId());

        Integer inwardItemId = inwardItemInstance.getInwardItem();
        existItemInstances.forEach(existItemInstance -> {
            if (!existItemInstance.getInwardItem().equals(inwardItemId)) {
                existItemInstance.setLatest(false);

                existItemInstance = inwardItemInstanceRepository.save(existItemInstance);
            }
        });

        Inward inward = inwardRepository.findOne(inwardId);

        inward.setModifiedDate(new Date());
        inward = inwardRepository.save(inward);

        return inwardItemInstance;
    }

    @Transactional(readOnly = false)
    public InwardItem acceptItemInstance(Integer inwardId, Integer itemId, InwardItemInstance inwardItemInstance) {

        ItemInstance itemInstance = inwardItemInstance.getItem();
        itemInstance.setStatus(ItemInstanceStatus.ACCEPT);
        itemInstance.setPresentStatus(ItemInstanceStatus.ACCEPT.toString());
        itemInstance.setReview(false);
        itemInstance.setProvisionalAccept(false);
        itemInstance = itemInstanceRepository.save(itemInstance);

        ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
        statusHistory.setItemInstance(itemInstance);
        statusHistory.setStatus(ItemInstanceStatus.ACCEPT);
        statusHistory.setPresentStatus(ItemInstanceStatus.ACCEPT.toString());
        statusHistory.setTimestamp(new Date());
        statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        statusHistory = instanceStatusHistoryRepository.save(statusHistory);

        InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());

        List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.findByInwardItem(inwardItemInstance.getInwardItem());
        Boolean setAccepted = true;
        for (InwardItemInstance inwardItemInstance1 : inwardItemInstances) {
            if (inwardItemInstance1.getItem().getStatus().equals(ItemInstanceStatus.STORE_SUBMITTED)) {
                setAccepted = false;
            }
        }

        if (setAccepted) {
            inwardItem.setAccepted(true);
            inwardItem = inwardItemRepository.save(inwardItem);
        }

        Inward inward = inwardRepository.findOne(inwardId);
        List<InwardItem> inwardItems = inwardItemRepository.findByInward(inwardId);
        List<InwardItemInstance> reviewItems = new ArrayList<>();

        inwardItems.forEach(inwardItem1 -> {
            reviewItems.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem1.getId(), ItemInstanceStatus.REVIEW));
        });

        if (reviewItems.size() == 0) {
            inward.setUnderReview(false);
            inward = inwardRepository.save(inward);
        }

        inwardItem.setInstances(inwardItemInstanceRepository.findByInwardItem(inwardItem.getId()));

        Inward inward1 = updateInwardStatusByInstances(inwardId);
        inward1.setModifiedDate(new Date());

        inward1 = inwardRepository.save(inward1);

        return inwardItem;
    }

    @Transactional(readOnly = false)
    public InwardItem acceptItemInstanceLater(Integer inwardId, Integer itemId, InwardItemInstance inwardItemInstance) {

        ItemInstance itemInstance = inwardItemInstance.getItem();
        itemInstance.setProvisionalAccept(false);

        itemInstance = itemInstanceRepository.save(itemInstance);

        ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
        statusHistory.setItemInstance(itemInstance);
        statusHistory.setStatus(ItemInstanceStatus.ACCEPT);
        statusHistory.setPresentStatus(ItemInstanceStatus.ACCEPT.toString());
        statusHistory.setTimestamp(new Date());
        statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        statusHistory = instanceStatusHistoryRepository.save(statusHistory);

        InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());

        List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.findByInwardItem(inwardItemInstance.getInwardItem());
        Boolean setAccepted = true;
        for (InwardItemInstance inwardItemInstance1 : inwardItemInstances) {
            if (inwardItemInstance1.getItem().getStatus().equals(ItemInstanceStatus.STORE_SUBMITTED)) {
                setAccepted = false;
            }
        }

        if (setAccepted) {
            inwardItem.setAccepted(true);
            inwardItem = inwardItemRepository.save(inwardItem);
        }

        Inward inward = inwardRepository.findOne(inwardId);
        List<InwardItem> inwardItems = inwardItemRepository.findByInward(inwardId);
        List<InwardItemInstance> reviewItems = new ArrayList<>();

        inwardItems.forEach(inwardItem1 -> {
            reviewItems.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem1.getId(), ItemInstanceStatus.REVIEW));
        });

        if (reviewItems.size() == 0) {
            inward.setUnderReview(false);
            inward = inwardRepository.save(inward);
        }

        inwardItem.setInstances(inwardItemInstanceRepository.findByInwardItem(inwardItem.getId()));

        Inward inward1 = updateInwardStatusByInstances(inwardId);
        inward1.setModifiedDate(new Date());

        inward1 = inwardRepository.save(inward1);

        return inwardItem;
    }

    @Transactional(readOnly = false)
    public InwardItem provisionalAcceptItemInstance(Integer inwardId, Integer itemId, InwardItemInstance inwardItemInstance) {

        ItemInstance itemInstance = inwardItemInstance.getItem();

        itemInstance.setStatus(ItemInstanceStatus.P_ACCEPT);
        itemInstance.setPresentStatus("P ACCEPT");
        itemInstance.setReview(false);
        itemInstance.setProvisionalAccept(true);
        itemInstance = itemInstanceRepository.save(itemInstance);

        ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
        statusHistory.setItemInstance(itemInstance);
        statusHistory.setStatus(ItemInstanceStatus.P_ACCEPT);
        statusHistory.setPresentStatus("P ACCEPT");
        statusHistory.setComment(itemInstance.getReason());
        statusHistory.setTimestamp(new Date());
        statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        statusHistory = instanceStatusHistoryRepository.save(statusHistory);

        InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());

        List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.findByInwardItem(inwardItem.getId());
        Boolean setPAccepted = true;
        for (InwardItemInstance inwardItemInstance1 : inwardItemInstances) {
            if (inwardItemInstance1.getItem().getStatus().equals(ItemInstanceStatus.STORE_SUBMITTED)) {
                setPAccepted = false;
            }
        }

        if (setPAccepted) {
            inwardItem.setpAccepted(true);
        }
        inwardItem.setInstances(inwardItemInstanceRepository.findByInwardItem(inwardItem.getId()));

        Inward inward = inwardRepository.findOne(inwardId);
        List<InwardItem> inwardItems = inwardItemRepository.findByInward(inwardId);
        List<InwardItemInstance> reviewItems = new ArrayList<>();

        inwardItems.forEach(inwardItem1 -> {
            reviewItems.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem1.getId(), ItemInstanceStatus.REVIEW));
        });

        if (reviewItems.size() == 0) {
            inward.setUnderReview(false);
            inward = inwardRepository.save(inward);
        }

        inward.setModifiedDate(new Date());

        inward = inwardRepository.save(inward);
        inward = updateInwardStatusByInstances(inward.getId());

        return inwardItem;
    }

    @Transactional(readOnly = false)
    public InwardItem returnItemInstance(Integer inward, InwardItemInstance inwardItemInstance) {

        Inward inward1 = inwardRepository.findOne(inward);
        InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());

        ItemInstance itemInstance = inwardItemInstance.getItem();

        itemInstance.setStatus(ItemInstanceStatus.REJECTED);
        itemInstance.setPresentStatus("REJECTED");
        itemInstance.setReview(false);
        itemInstance.setProvisionalAccept(false);
        itemInstance.setReturnBy(sessionWrapper.getSession().getLogin().getPerson());
        itemInstance = itemInstanceRepository.save(itemInstance);

        ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
        statusHistory.setItemInstance(itemInstance);
        statusHistory.setStatus(ItemInstanceStatus.REJECTED);
        statusHistory.setPresentStatus("REJECTED");
        statusHistory.setTimestamp(new Date());
        statusHistory.setComment(itemInstance.getReason());
        statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        statusHistory = instanceStatusHistoryRepository.save(statusHistory);
        Inventory inventory = null;
        if (inwardItem.getSection() != null) {
            inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(inward1.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode(), itemInstance.getSection());

            if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                inventory.setFractionalQtyBuffered(inventory.getFractionalQtyBuffered() - inwardItemInstance.getItem().getLotSize());

                inventory = inventoryRepository.save(inventory);
            } else {
                inventory.setQtyBuffered(inventory.getQtyBuffered() - 1);

                inventory = inventoryRepository.save(inventory);
            }

        } else {
            inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(inward1.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode());

            if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                inventory.setFractionalQtyBuffered(inventory.getFractionalQtyBuffered() - inwardItemInstance.getItem().getLotSize());

                inventory = inventoryRepository.save(inventory);
            } else {
                inventory.setQtyBuffered(inventory.getQtyBuffered() - 1);

                inventory = inventoryRepository.save(inventory);
            }
        }

        inwardItem.setInstances(inwardItemInstanceRepository.findByInwardItem(inwardItem.getId()));

        List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward);
        List<InwardItemInstance> reviewItems = new ArrayList<>();

        inwardItems.forEach(inwardItem1 -> {
            reviewItems.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem1.getId(), ItemInstanceStatus.REVIEW));
        });

        if (reviewItems.size() == 0) {
            inward1.setUnderReview(false);
            inward1 = inwardRepository.save(inward1);
        }

        Inward inward2 = updateInwardStatusByInstances(inward);

        inward2.setModifiedDate(new Date());

        inward2 = inwardRepository.save(inward2);

        return inwardItem;
    }

    @Transactional(readOnly = false)
    public InwardItem addStorageToReturnItemInstance(Integer inward, InwardItemInstance inwardItemInstance) {

        Inward inward1 = inwardRepository.findOne(inward);
        InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());

        List<StorageItem> returnStorages = new ArrayList<>();
        if (inwardItemInstance.getItem().getSection() == null) {
            returnStorages = storageItemRepository.getItemReturnStoragesByBomAndSectionIsNull(inward1.getBom().getId(), inwardItem.getBomItem().getUniqueCode());
        } else {
            returnStorages = storageItemRepository.getItemReturnStoragesByBomAndSection(inward1.getBom().getId(), inwardItem.getBomItem().getUniqueCode(), inwardItemInstance.getItem().getSection());
        }

        if (returnStorages.size() == 0) {
            throw new CassiniException("No Return Storage available for this Item : " + inwardItem.getBomItem().getItem().getItemMaster().getItemName());
        }

        Storage matchingLocation = null;

        for (StorageItem storageItem : returnStorages) {
            if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                if (storageItem.getStorage().getRemainingCapacity() >= inwardItemInstance.getItem().getLotSize()) {
                    matchingLocation = storageItem.getStorage();
                    break;
                }
            } else {
                if (storageItem.getStorage().getRemainingCapacity() >= 1.0) {
                    matchingLocation = storageItem.getStorage();
                    break;
                }
            }
        }

        ItemInstance itemInstance = inwardItemInstance.getItem();

        itemInstance.setStorage(matchingLocation);

        itemInstance = itemInstanceRepository.save(itemInstance);

        if (itemInstance.getStorage() != null) {
            Storage presentStorage = storageRepository.findOne(itemInstance.getStorage().getId());
            if (presentStorage != null) {
                if (!inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                    presentStorage.setRemainingCapacity(presentStorage.getRemainingCapacity() + 1);
                } else {
                    presentStorage.setRemainingCapacity(presentStorage.getRemainingCapacity() + inwardItemInstance.getItem().getLotSize());
                }

                presentStorage = storageRepository.save(presentStorage);
            }
        }

        inwardItemInstance.setHasReturnStorage(true);

        inwardItemInstance = inwardItemInstanceRepository.save(inwardItemInstance);

        inwardItem.setInstances(inwardItemInstanceRepository.findByInwardItem(inwardItem.getId()));

        List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward);
        List<InwardItemInstance> reviewItems = new ArrayList<>();

        inwardItems.forEach(inwardItem1 -> {
            reviewItems.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem1.getId(), ItemInstanceStatus.REVIEW));
        });

        if (reviewItems.size() == 0) {
            inward1.setUnderReview(false);
            inward1 = inwardRepository.save(inward1);
        }

        Inward inward2 = updateInwardStatusByInstances(inward);

        inward2.setModifiedDate(new Date());

        inward2 = inwardRepository.save(inward2);

        return inwardItem;
    }

    @Transactional(readOnly = true)
    public ItemInstance getReturnedInstanceByUpnNumber(String upnNumber, String serialNumber) {

        ItemInstance itemInstance = itemInstanceRepository.findByInitialUpn(upnNumber);

        if (itemInstance != null) {
            List<ItemInstanceStatusHistory> statusHistories = instanceStatusHistoryRepository.getItemInstanceHistory(itemInstance.getId());

            if (statusHistories.size() > 0) {
                if (statusHistories.get(0).getStatus().equals(ItemInstanceStatus.DISPATCH)) {
                    return itemInstance;
                } else {
                    itemInstance = null;
                }
            }
        }

        return itemInstance;
    }

    @Transactional(readOnly = true)
    public ItemInstance getReturnedInstanceBySerialNumberAndManufacturerAndItem(Integer itemId, String serialNumber, Integer mfr) {

        ItemInstance itemInstance = itemInstanceRepository.getInstanceByItemAndMfrAndOemNumber(itemId, mfr, serialNumber);

        if (itemInstance != null) {
            List<ItemInstanceStatusHistory> statusHistories = instanceStatusHistoryRepository.getItemInstanceHistory(itemInstance.getId());

            if (statusHistories.size() > 0) {
                if (statusHistories.get(0).getStatus().equals(ItemInstanceStatus.DISPATCH)) {
                    return itemInstance;
                } else {
                    itemInstance = null;
                }
            }
        }

        return itemInstance;
    }

    @Transactional(readOnly = false)
    public InwardItem reviewItemInstance(Integer inward, InwardItemInstance inwardItemInstance) {

        Inward inward1 = inwardRepository.findOne(inward);

        InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());

        ItemInstance itemInstance = inwardItemInstance.getItem();
        itemInstance.setStatus(ItemInstanceStatus.REVIEW);
        itemInstance.setPresentStatus(ItemInstanceStatus.REVIEW.toString());
        itemInstance.setReview(true);
        itemInstance.setProvisionalAccept(false);
        itemInstance = itemInstanceRepository.save(itemInstance);

        ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
        statusHistory.setItemInstance(itemInstance);
        statusHistory.setStatus(ItemInstanceStatus.REVIEW);
        statusHistory.setPresentStatus(ItemInstanceStatus.REVIEW.toString());
        statusHistory.setComment(itemInstance.getReason());
        statusHistory.setTimestamp(new Date());
        statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        statusHistory = instanceStatusHistoryRepository.save(statusHistory);

        inward1.setUnderReview(true);

        inward1 = inwardRepository.save(inward1);
        inwardItem.setInstances(inwardItemInstanceRepository.findByInwardItem(inwardItem.getId()));

        inward1.setModifiedDate(new Date());

        inward1 = inwardRepository.save(inward1);

        return inwardItem;
    }

    @Transactional(readOnly = false)
    public InwardItem allocateStorage(Integer inwardId, InwardItemInstance inwardItemInstance) {

        InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());

        Inward inward = inwardRepository.findOne(inwardItem.getInward());

        Integer typeId = inwardItem.getBomItem().getItem().getItemMaster().getItemType().getId();

        if (!inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
            List<StorageItem> binStores = new ArrayList<>();
            if (inwardItem.getSection() == null) {
                binStores = storageItemRepository.getItemStorageByBomAndTypeAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), StorageType.BIN, inwardItem.getBomItem().getUniqueCode());
            } else {
                binStores = storageItemRepository.getItemStorageByBomAndTypeAndUniqueCodeAndSection(inward.getBom().getId(), StorageType.BIN, inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
            }

            if (binStores.size() > 0) {
                inwardItemInstance = allocateStorageByItemInstanceAndStores(inwardItemInstance, binStores);
            } else {
                List<StorageItem> shelfStores = new ArrayList<>();
                if (inwardItem.getSection() == null) {
                    shelfStores = storageItemRepository.getItemStorageByBomAndTypeAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), StorageType.SHELF, inwardItem.getBomItem().getUniqueCode());
                } else {
                    shelfStores = storageItemRepository.getItemStorageByBomAndTypeAndUniqueCodeAndSection(inward.getBom().getId(), StorageType.SHELF, inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
                }

                if (shelfStores.size() > 0) {
                    inwardItemInstance = allocateStorageByItemInstanceAndStores(inwardItemInstance, shelfStores);
                } else {
                    List<StorageItem> rackStores = new ArrayList<>();
                    if (inwardItem.getSection() == null) {
                        rackStores = storageItemRepository.getItemStorageByBomAndTypeAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), StorageType.RACK, inwardItem.getBomItem().getUniqueCode());
                    } else {
                        rackStores = storageItemRepository.getItemStorageByBomAndTypeAndUniqueCodeAndSection(inward.getBom().getId(), StorageType.RACK, inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
                    }

                    if (rackStores.size() > 0) {
                        inwardItemInstance = allocateStorageByItemInstanceAndStores(inwardItemInstance, rackStores);
                    } else {
                        List<StorageItem> areaStores = new ArrayList<>();
                        if (inwardItem.getSection() == null) {
                            areaStores = storageItemRepository.getItemStorageByBomAndTypeAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), StorageType.AREA, inwardItem.getBomItem().getUniqueCode());
                        } else {
                            areaStores = storageItemRepository.getItemStorageByBomAndTypeAndUniqueCodeAndSection(inward.getBom().getId(), StorageType.AREA, inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
                        }

                        if (areaStores.size() > 0) {
                            inwardItemInstance = allocateStorageByItemInstanceAndStores(inwardItemInstance, areaStores);
                        } else {
                            throw new CassiniException("No storage space available");
                        }
                    }
                }
            }
        } else {
            List<StorageItem> binStores = new ArrayList<>();
            if (inwardItem.getSection() == null) {
                binStores = storageItemRepository.getItemStorageByBomAndTypeAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), StorageType.BIN, inwardItem.getBomItem().getUniqueCode());
            } else {
                binStores = storageItemRepository.getItemStorageByBomAndTypeAndUniqueCodeAndSection(inward.getBom().getId(), StorageType.BIN, inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
            }

            if (binStores.size() > 0) {
                inwardItemInstance = allocateStorageByItemInstanceAndStores(inwardItemInstance, binStores);
            } else {
                List<StorageItem> shelfStores = new ArrayList<>();
                if (inwardItem.getSection() == null) {
                    shelfStores = storageItemRepository.getItemStorageByBomAndTypeAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), StorageType.SHELF, inwardItem.getBomItem().getUniqueCode());
                } else {
                    shelfStores = storageItemRepository.getItemStorageByBomAndTypeAndUniqueCodeAndSection(inward.getBom().getId(), StorageType.SHELF, inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
                }

                if (shelfStores.size() > 0) {
                    inwardItemInstance = allocateStorageByItemInstanceAndStores(inwardItemInstance, shelfStores);
                } else {
                    throw new CassiniException("No storage space available");
                }
            }
        }

        inwardItem.setInstances(inwardItemInstanceRepository.findByInwardItem(inwardItem.getId()));

        inwardItem.getInstances().forEach(itemInstance -> {
            String storageLocation = "";
            if (itemInstance.getItem().getStorage() != null && itemInstance.getItem().getStorage().getParent() != null) {
                Storage parentStorage = storageRepository.findOne(itemInstance.getItem().getStorage().getParent());

                if (parentStorage != null) {
                    storageLocation = parentStorage.getName() + "/ " + itemInstance.getItem().getStorage().getName();
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
                itemInstance.getItem().setStoragePath(storageLocation);

            } else if (itemInstance.getItem().getStorage() != null) {
                storageLocation = itemInstance.getItem().getStorage().getName();
                itemInstance.getItem().setStoragePath(storageLocation);
            }
        });

        Inward inward1 = updateInwardStatusByInstances(inwardId);

        inward1.setModifiedDate(new Date());

        inward1 = inwardRepository.save(inward1);

        return inwardItem;

    }

    private InwardItemInstance allocateStorageByItemInstanceAndStores(InwardItemInstance inwardItemInstance, List<StorageItem> storageItems) {

        Storage matchingLocation = null;

        InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());

        Inward inward = inwardRepository.findOne(inwardItem.getInward());

        Double totalInwardLotQuantity = 0.0;

        if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {

            for (StorageItem storageItem : storageItems) {
                if (storageItem.getStorage().getRemainingCapacity() >= inwardItemInstance.getItem().getLotSize()) {
                    matchingLocation = storageItem.getStorage();
                    break;
                }
            }

        } else {
            for (StorageItem storageItem : storageItems) {
                if (storageItem.getStorage().getRemainingCapacity() >= 1) {
                    matchingLocation = storageItem.getStorage();
                    break;
                }
            }
        }


        if (matchingLocation == null) {
            throw new CassiniException("No storage space available");
        }


        ItemInstance itemInstance = inwardItemInstance.getItem();

        Storage presentStorage = null;

        if (itemInstance.getStorage() != null) {
            presentStorage = storageRepository.findOne(itemInstance.getStorage().getId());
        }

        Inventory inventory = null;
        if (inwardItem.getSection() == null) {
            inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode());
        } else {
            inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(inward.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
        }
        if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
            if (inventory.getFractionalQtyBuffered() >= inwardItemInstance.getItem().getLotSize()) {
                inventory.setFractionalQtyBuffered(inventory.getFractionalQtyBuffered() - inwardItemInstance.getItem().getLotSize());
                inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() + inwardItemInstance.getItem().getLotSize());
            }
            inventory = inventoryRepository.save(inventory);
        } else {
            if (inventory.getQtyBuffered() > 0) {
                inventory.setQtyBuffered(inventory.getQtyBuffered() - 1);
                inventory.setQuantity(inventory.getQuantity() + 1);
            }
            inventory = inventoryRepository.save(inventory);
        }

        itemInstance.setStorage(matchingLocation);
        itemInstance.setStatus(ItemInstanceStatus.INVENTORY);
        itemInstance.setPresentStatus(ItemInstanceStatus.INVENTORY.toString());
        itemInstance = itemInstanceRepository.save(itemInstance);

        ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
        statusHistory.setItemInstance(itemInstance);
        statusHistory.setStatus(ItemInstanceStatus.INVENTORY);
        statusHistory.setPresentStatus(ItemInstanceStatus.INVENTORY.toString());
        statusHistory.setTimestamp(new Date());
        statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

        statusHistory = instanceStatusHistoryRepository.save(statusHistory);

        if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {

            Storage allocatedStorage = itemInstance.getStorage();
            allocatedStorage.setRemainingCapacity(allocatedStorage.getRemainingCapacity() - inwardItemInstance.getItem().getLotSize());
            allocatedStorage = storageRepository.save(allocatedStorage);
            if (presentStorage != null) {
                presentStorage.setRemainingCapacity(presentStorage.getRemainingCapacity() + inwardItemInstance.getItem().getLotSize());
                presentStorage = storageRepository.save(presentStorage);
            }
        } else {

            Storage allocatedStorage = itemInstance.getStorage();
            allocatedStorage.setRemainingCapacity(allocatedStorage.getRemainingCapacity() - 1);
            allocatedStorage = storageRepository.save(allocatedStorage);
            if (presentStorage != null) {
                presentStorage.setRemainingCapacity(presentStorage.getRemainingCapacity() + 1);
                presentStorage = storageRepository.save(presentStorage);
            }
        }

        return inwardItemInstance;
    }

    @Transactional(readOnly = false)
    public InwardItem verifyStoragePart(Integer inwardId, Integer inwardInstanceId, String storage, String upnNumber) {

        InwardItemInstance inwardItemInstance = inwardItemInstanceRepository.findOne(inwardInstanceId);
        InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());
        Inward inward = inwardRepository.findOne(inwardId);
        ItemInstance verifyInstance = itemInstanceRepository.findByUpnNumber(upnNumber);

        List<Storage> partStorages = storageRepository.findByName(storage);
        Boolean partVerified = false;

        for (Storage storage1 : partStorages) {
            if (storage1.getId().equals(inwardItemInstance.getItem().getStorage().getId())) {
                partVerified = true;
            }
        }

        if (verifyInstance == null) {
            throw new CassiniException("Part not verified.");
        } else if (!partVerified || !verifyInstance.getId().equals(inwardItemInstance.getItem().getId())) {
            throw new CassiniException("Part not verified.");
        } else {
            Inventory inventory = null;
            if (inwardItem.getSection() == null) {
                inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(inward.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode());
            } else {
                inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(inward.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
            }
            if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                if (inventory.getFractionalQtyBuffered() >= inwardItemInstance.getItem().getLotSize()) {
                    inventory.setFractionalQtyBuffered(inventory.getFractionalQtyBuffered() - inwardItemInstance.getItem().getLotSize());
                    inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() + inwardItemInstance.getItem().getLotSize());
                }
                inventory = inventoryRepository.save(inventory);
            } else {
                if (inventory.getQtyBuffered() > 0) {
                    inventory.setQtyBuffered(inventory.getQtyBuffered() - 1);
                    inventory.setQuantity(inventory.getQuantity() + 1);
                }
                inventory = inventoryRepository.save(inventory);
            }

            ItemInstance itemInstance1 = inwardItemInstance.getItem();
            itemInstance1.setStatus(ItemInstanceStatus.VERIFIED);
            itemInstance1.setPresentStatus(ItemInstanceStatus.VERIFIED.toString());
            itemInstance1 = itemInstanceRepository.save(itemInstance1);

            ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
            statusHistory.setItemInstance(itemInstance1);
            statusHistory.setStatus(ItemInstanceStatus.VERIFIED);
            statusHistory.setPresentStatus(ItemInstanceStatus.VERIFIED.toString());
            statusHistory.setTimestamp(new Date());
            statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

            statusHistory = instanceStatusHistoryRepository.save(statusHistory);

            Inward inward1 = updateInwardStatusByInstances(inwardId);

            inwardItem.setInstances(inwardItemInstanceRepository.findByInwardItem(inwardItem.getId()));
        }

        inward.setModifiedDate(new Date());
        Inward inward1 = updateInwardStatusByInstances(inwardId);
        inward1 = inwardRepository.save(inward1);

        return inwardItem;
    }

    @Transactional(readOnly = true)
    public Page<ItemInstance> getAllReturnItems(Pageable pageable) {

        Page<ItemInstance> itemInstances = itemInstanceRepository.getAllReturnInstances(pageable);
        itemInstances.forEach(itemInstance -> {
            ItemType itemType = itemTypeRepository.findOne(itemInstance.getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                itemInstance.getItem().getItemMaster().setParentType(itemType);
            } else {
                itemInstance.getItem().getItemMaster().setParentType(itemInstance.getItem().getItemMaster().getItemType());
            }

            String storageLocation = "";
            if (itemInstance.getStorage() != null && itemInstance.getStorage().getParent() != null) {
                Storage parentStorage = storageRepository.findOne(itemInstance.getStorage().getParent());

                if (parentStorage != null) {
                    storageLocation = parentStorage.getName() + "/ " + itemInstance.getStorage().getName();
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
                itemInstance.setStoragePath(storageLocation);

            } else if (itemInstance.getStorage() != null) {
                storageLocation = itemInstance.getStorage().getName();
                itemInstance.setStoragePath(storageLocation);
            }
        });
        return itemInstances;
    }

    @Transactional(readOnly = true)
    public Page<ItemInstance> getAllFailureItems(Pageable pageable) {

        Page<ItemInstance> itemInstances = itemInstanceRepository.getAllFailureInstances(pageable);

        itemInstances.forEach(itemInstance -> {
            ItemType itemType = itemTypeRepository.findOne(itemInstance.getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                itemInstance.getItem().getItemMaster().setParentType(itemType);
            } else {
                itemInstance.getItem().getItemMaster().setParentType(itemInstance.getItem().getItemMaster().getItemType());
            }

            String storageLocation = "";
            if (itemInstance.getStorage() != null && itemInstance.getStorage().getParent() != null) {
                Storage parentStorage = storageRepository.findOne(itemInstance.getStorage().getParent());

                if (parentStorage != null) {
                    storageLocation = parentStorage.getName() + "/ " + itemInstance.getStorage().getName();
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
                itemInstance.setStoragePath(storageLocation);

            } else if (itemInstance.getStorage() != null) {
                storageLocation = itemInstance.getStorage().getName();
                itemInstance.setStoragePath(storageLocation);
            }
        });

        return itemInstances;
    }

    @Transactional(readOnly = true)
    public Page<ItemInstance> getAllExpiredItems(Pageable pageable) {

        Page<ItemInstance> itemInstances = itemInstanceRepository.getAllExpiredInstances(new Date(), ItemInstanceStatus.NEW, ItemInstanceStatus.STORE_SUBMITTED, ItemInstanceStatus.ACCEPT,
                ItemInstanceStatus.P_ACCEPT, ItemInstanceStatus.REVIEW, ItemInstanceStatus.REVIEWED, ItemInstanceStatus.VERIFIED, ItemInstanceStatus.INVENTORY, pageable);

        itemInstances.forEach(itemInstance -> {
            ItemType itemType = itemTypeRepository.findOne(itemInstance.getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                itemInstance.getItem().getItemMaster().setParentType(itemType);
            } else {
                itemInstance.getItem().getItemMaster().setParentType(itemInstance.getItem().getItemMaster().getItemType());
            }

            String storageLocation = "";
            if (itemInstance.getStorage() != null && itemInstance.getStorage().getParent() != null) {
                Storage parentStorage = storageRepository.findOne(itemInstance.getStorage().getParent());

                if (parentStorage != null) {
                    storageLocation = parentStorage.getName() + "/ " + itemInstance.getStorage().getName();
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
                itemInstance.setStoragePath(storageLocation);

            } else if (itemInstance.getStorage() != null) {
                storageLocation = itemInstance.getStorage().getName();
                itemInstance.setStoragePath(storageLocation);
            }
        });

        return itemInstances;
    }

    @Transactional(readOnly = true)
    public Page<ItemInstance> getToExpireItems(Pageable pageable) {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date futureDate = cal.getTime();
        Page<ItemInstance> itemInstances = itemInstanceRepository.getAllToExpiryItems(today, futureDate, ItemInstanceStatus.NEW, ItemInstanceStatus.STORE_SUBMITTED, ItemInstanceStatus.ACCEPT,
                ItemInstanceStatus.P_ACCEPT, ItemInstanceStatus.REVIEW, ItemInstanceStatus.REVIEWED, ItemInstanceStatus.VERIFIED, ItemInstanceStatus.INVENTORY, pageable);

        itemInstances.forEach(itemInstance -> {
            ItemType itemType = itemTypeRepository.findOne(itemInstance.getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                itemInstance.getItem().getItemMaster().setParentType(itemType);
            } else {
                itemInstance.getItem().getItemMaster().setParentType(itemInstance.getItem().getItemMaster().getItemType());
            }

            String storageLocation = "";
            if (itemInstance.getStorage() != null && itemInstance.getStorage().getParent() != null) {
                Storage parentStorage = storageRepository.findOne(itemInstance.getStorage().getParent());

                if (parentStorage != null) {
                    storageLocation = parentStorage.getName() + "/ " + itemInstance.getStorage().getName();
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
                itemInstance.setStoragePath(storageLocation);

            } else if (itemInstance.getStorage() != null) {
                storageLocation = itemInstance.getStorage().getName();
                itemInstance.setStoragePath(storageLocation);
            }
        });

        return itemInstances;
    }


    /*------------------------------------  Attributes Update Methods   -----------------------------------------------*/

    @Transactional(readOnly = true)
    public Page<ItemInstance> searchItemInstances(Pageable pageable, ItemInstanceCriteria criteria) {
        Predicate predicate = itemInstancePredicateBuilder.build(criteria, QItemInstance.itemInstance);
        Page<ItemInstance> itemInstances = itemInstanceRepository.findAll(predicate, pageable);

        itemInstances.forEach(itemInstance -> {
            ItemType itemType = itemTypeRepository.findOne(itemInstance.getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                itemInstance.getItem().getItemMaster().setParentType(itemType);
            } else {
                itemInstance.getItem().getItemMaster().setParentType(itemInstance.getItem().getItemMaster().getItemType());
            }

            InwardItemInstance inwardItemInstance = inwardItemInstanceRepository.findByItem(itemInstance);

            if (inwardItemInstance != null) {
                InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());
                Inward inward = inwardRepository.findOne(inwardItem.getInward());
                itemInstance.setInward(inward);

                if (itemInstance.getSection() != null) {
                    itemInstance.setSectionName(bomGroupRepository.findOne(itemInstance.getSection()).getName());
                } else {

                    BomItem bomItem = bomItemRepository.findOne(inwardItem.getBomItem().getId());

                    if (bomItem.getParent() != null) {
                        BomItem parent = bomItemRepository.findOne(bomItem.getParent());
                        if (parent.getBomItemType().equals(BomItemType.SECTION) || parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                            itemInstance.setSectionName(parent.getTypeRef().getName());
                        } else {
                            if (parent.getParent() != null) {
                                BomItem parent1 = bomItemRepository.findOne(parent.getParent());

                                if (parent1.getBomItemType().equals(BomItemType.SECTION) || parent1.getBomItemType().equals(BomItemType.COMMONPART)) {
                                    itemInstance.setSectionName(parent1.getTypeRef().getName());
                                } else {
                                    if (parent1.getParent() != null) {
                                        BomItem parent2 = bomItemRepository.findOne(parent1.getParent());

                                        if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                                            itemInstance.setSectionName(parent2.getTypeRef().getName());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        return itemInstances;
    }

    @Transactional(readOnly = false)
    public List<ObjectAttribute> createMultipleLotAttributes(List<Integer> instanceIds, List<ObjectAttribute> attributes) {
        List<ObjectAttribute> objectAttributes = new ArrayList<>();

        for (Integer instanceId : instanceIds) {
            for (ObjectAttribute attribute : attributes) {
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null ||
                        attribute.getStringValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(instanceId, attribute.getId().getAttributeDef()));
                    attribute.setStringValue(attribute.getStringValue());
                    attribute.setIntegerValue(attribute.getIntegerValue());
                    attribute.setBooleanValue(attribute.getBooleanValue());
                    attribute.setDoubleValue((attribute.getDoubleValue()));
                    attribute.setDateValue(attribute.getDateValue());
                    attribute.setTimeValue(attribute.getTimeValue());
                    attribute.setAttachmentValues(attribute.getAttachmentValues());
                    attribute.setRefValue(attribute.getRefValue());
                    attribute.setCurrencyType(attribute.getCurrencyType());
                    attribute.setCurrencyValue(attribute.getCurrencyValue());
                    attribute.setTimestampValue(attribute.getTimestampValue());
                    attribute.setListValue(attribute.getListValue());
                    attribute = objectAttributeRepository.save(attribute);
                }

            }
        }

        return attributes;
    }

    @Transactional(readOnly = false)
    public List<ObjectAttribute> saveMultipleLotImage(List<Integer> instanceIds, Integer attributeId, Map<String, MultipartFile> fileMap) {
        List<ObjectAttribute> objectAttributes = new ArrayList<>();

        for (Integer instanceId : instanceIds) {
            ItemInstance itemInstance = itemInstanceRepository.findOne(instanceId);

            if (itemInstance != null) {
                ObjectAttribute objectAttribute = new ObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(instanceId, attributeId));
                List<MultipartFile> files = new ArrayList<>(fileMap.values());
                if (files.size() > 0) {
                    MultipartFile file = files.get(0);
                    try {
                        objectAttribute.setImageValue(file.getBytes());
                        objectAttribute = objectAttributeRepository.save(objectAttribute);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }


        return objectAttributes;
    }

    @Transactional(readOnly = false)
    public List<AttributeAttachment> uploadMultiLotAttachment(List<Integer> instanceIds, Integer attributeId, Map<String, MultipartFile> fileMap) {
        List<AttributeAttachment> uploaded = new ArrayList<>();

        Login login = sessionWrapper.getSession().getLogin();

        try {
            AttributeAttachment attributeAttachment = new AttributeAttachment();
            List<Integer> attachmentIds = new ArrayList<>();
            for (MultipartFile file : fileMap.values()) {
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");

                attributeAttachment.setAttributeDef(attributeId);
                attributeAttachment.setAddedBy(login.getPerson().getId());
                attributeAttachment.setAddedOn(new Date());
                attributeAttachment.setName(name);

                int index = name.lastIndexOf('.');
                if (index != -1) {
                    String ext = name.substring(index);
                    ext = ext.toLowerCase();
                    attributeAttachment.setExtension(ext);
                }

                attributeAttachment.setSize(new Long(file.getSize()).intValue());
                attributeAttachment.setObjectId(instanceIds.get(0));
                attributeAttachment.setObjectType(ObjectType.valueOf("ITEMINSTANCE"));
                attributeAttachment = attributeAttachmentRepository.save(attributeAttachment);

                attachmentIds.add(attributeAttachment.getId());
                uploaded.add(attributeAttachment);

                saveAttachmentToDisk(attributeAttachment, file);

            }
            for (Integer instanceId : instanceIds) {

                ObjectAttribute objectAttribute = new ObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(instanceId, attributeId));

                objectAttribute.setAttachmentValues(attachmentIds.stream().filter(Objects::nonNull).toArray((Integer[]::new)));

                objectAttribute = objectAttributeRepository.save(objectAttribute);


            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return uploaded;
    }

    private void saveAttachmentToDisk(Attachment attachment,
                                      MultipartFile multipartFile) {
        try {
            java.io.File file = getAttachmentFile(attachment.getObjectId(), attachment);
            FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(
                    file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }

    private java.io.File getAttachmentFile(Integer objectId, Attachment attachment) {
        String path = fileSystemService.getCurrentTenantRoot() + java.io.File.separator +
                "filesystem" + java.io.File.separator + "attachments" + java.io.File.separator + objectId;
        java.io.File attachmentsFolder = new java.io.File(path);
        if (!attachmentsFolder.exists()) {
            attachmentsFolder.mkdirs();
        }
        return new java.io.File(attachmentsFolder, Integer.toString(attachment.getId()));
    }

    @Transactional(readOnly = true)
    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByItemIdsAndAttributeIds(Integer[] objectIds, Integer[] objectAttributeIds) {

        Map<Integer, List<ObjectAttribute>> objectAttributesMap = new HashMap();

        List<ObjectAttribute> attributes = objectAttributeRepository.findByObjectIdsInAndAttributeDefIdsIn(objectIds, objectAttributeIds);

        for (ObjectAttribute attribute : attributes) {
            Integer id = attribute.getId().getObjectId();
            List<ObjectAttribute> objectAttributes = objectAttributesMap.get(id);
            if (objectAttributes == null) {
                objectAttributes = new ArrayList<>();
                objectAttributesMap.put(id, objectAttributes);
            }

            objectAttributes.add(attribute);
        }

        return objectAttributesMap;

    }

    @Transactional(readOnly = false)
    public ObjectAttribute saveInstanceAttribute(Integer instanceId, ObjectAttribute attribute) {
        if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null ||
                attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null ||
                attribute.getStringValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

            attribute.setId(new ObjectAttributeId(instanceId, attribute.getId().getAttributeDef()));
            attribute.setStringValue(attribute.getStringValue());
            attribute.setIntegerValue(attribute.getIntegerValue());
            attribute.setBooleanValue(attribute.getBooleanValue());
            attribute.setDoubleValue((attribute.getDoubleValue()));
            attribute.setDateValue(attribute.getDateValue());
            attribute.setTimeValue(attribute.getTimeValue());
            attribute.setAttachmentValues(attribute.getAttachmentValues());
            attribute.setRefValue(attribute.getRefValue());
            attribute.setCurrencyType(attribute.getCurrencyType());
            attribute.setCurrencyValue(attribute.getCurrencyValue());
            attribute.setTimestampValue(attribute.getTimestampValue());
            attribute.setListValue(attribute.getListValue());
            attribute = objectAttributeRepository.save(attribute);
        }

        return attribute;
    }

    @Transactional(readOnly = false)
    public List<ObjectAttribute> saveInstanceAttributes(Integer instanceId, List<ObjectAttribute> attributes) {
        for (ObjectAttribute attribute : attributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null ||
                    attribute.getStringValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(instanceId, attribute.getId().getAttributeDef()));
                attribute.setStringValue(attribute.getStringValue());
                attribute.setIntegerValue(attribute.getIntegerValue());
                attribute.setBooleanValue(attribute.getBooleanValue());
                attribute.setDoubleValue((attribute.getDoubleValue()));
                attribute.setDateValue(attribute.getDateValue());
                attribute.setTimeValue(attribute.getTimeValue());
                attribute.setAttachmentValues(attribute.getAttachmentValues());
                attribute.setRefValue(attribute.getRefValue());
                attribute.setCurrencyType(attribute.getCurrencyType());
                attribute.setCurrencyValue(attribute.getCurrencyValue());
                attribute.setTimestampValue(attribute.getTimestampValue());
                attribute.setListValue(attribute.getListValue());
                attribute = objectAttributeRepository.save(attribute);
            }
        }

        return attributes;
    }

    @Transactional(readOnly = false)
    public ObjectAttribute saveInstanceImage(Integer instanceId, Integer attributeId, Map<String, MultipartFile> fileMap) {

        ItemInstance itemInstance = itemInstanceRepository.findOne(instanceId);
        ObjectAttribute objectAttribute = new ObjectAttribute();
        if (itemInstance != null) {
            objectAttribute.setId(new ObjectAttributeId(instanceId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    objectAttribute.setImageValue(file.getBytes());
                    objectAttribute = objectAttributeRepository.save(objectAttribute);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return objectAttribute;
    }

    @Transactional(readOnly = false)
    public ObjectAttribute uploadInstanceAttachment(Integer instanceId, Integer attributeId, Map<String, MultipartFile> fileMap) {

        Login login = sessionWrapper.getSession().getLogin();
        ObjectAttribute objectAttribute = new ObjectAttribute();
        ObjectAttribute instanceAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(instanceId, attributeId);
        try {

            objectAttribute.setId(new ObjectAttributeId(instanceId, attributeId));

            List<Integer> attachmentIds = new ArrayList<>();
            if (instanceAttribute != null && instanceAttribute.getAttachmentValues().length > 0) {
                for (int i = 0; i < instanceAttribute.getAttachmentValues().length; i++) {
                    attachmentIds.add(instanceAttribute.getAttachmentValues()[i]);
                }
            }

            /*List<AttributeAttachment> attributeAttachments = attributeAttachmentRepository.findByObjectId(instanceId);

            for (AttributeAttachment attributeAttachment : attributeAttachments) {
                String path = fileSystemService.getCurrentTenantRoot() + java.io.File.separator +
                        "filesystem" + java.io.File.separator + "attachments" + java.io.File.separator + instanceId + java.io.File.separator + attributeAttachment.getId();

                java.io.File file = new java.io.File(path);
                if (file.exists()) {
                    file.delete();
                }
            }*/

            for (MultipartFile file : fileMap.values()) {
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                AttributeAttachment attributeAttachment = new AttributeAttachment();
                attributeAttachment.setAttributeDef(attributeId);
                attributeAttachment.setAddedBy(login.getPerson().getId());
                attributeAttachment.setAddedOn(new Date());
                attributeAttachment.setName(name);

                int index = name.lastIndexOf('.');
                if (index != -1) {
                    String ext = name.substring(index);
                    ext = ext.toLowerCase();
                    attributeAttachment.setExtension(ext);
                }

                attributeAttachment.setSize(new Long(file.getSize()).intValue());
                attributeAttachment.setObjectId(instanceId);
                attributeAttachment.setObjectType(ObjectType.valueOf("ITEMINSTANCE"));
                attributeAttachment = attributeAttachmentRepository.save(attributeAttachment);

                attachmentIds.add(attributeAttachment.getId());

                saveAttachmentToDisk(attributeAttachment, file);

            }

            if (attachmentIds.size() > 0) {
                objectAttribute.setAttachmentValues(attachmentIds.stream().filter(Objects::nonNull).toArray((Integer[]::new)));
                objectAttribute = objectAttributeRepository.save(objectAttribute);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return objectAttribute;
    }

    private String getNextSeq(String currentSeq) {
        String nextSeq = null;

        currentSeq = currentSeq.toUpperCase();
        if (currentSeq.equalsIgnoreCase("99")) {
            return "A1";
        } else if (currentSeq.equalsIgnoreCase("Z9")) {
            return "AA";
        } else if (currentSeq.equalsIgnoreCase("ZZ")) {
            return "1A";
        } else if (currentSeq.equalsIgnoreCase("9Z")) {
            return null;
        } else if (currentSeq.length() == 2) {
            String first = currentSeq.split("")[0];
            String second = currentSeq.split("")[1];

            if (StringUtils.isNumeric(first) && StringUtils.isNumeric(second)) {
                Integer number = Integer.parseInt(currentSeq);
                number++;

                String s = "" + number;
                if (s.length() == 1) {
                    s = "0" + s;
                }

                return s;
            } else if (StringUtils.isAlpha(first) && StringUtils.isNumeric(second)) {
                Integer number = Integer.parseInt(second);
                number++;
                if (number < 10) {
                    return first + number;
                } else {
                    int a = (int) first.charAt(0);
                    a++;
                    if (a < 90) {
                        return ((char) a) + "0";
                    }
                }
            } else if (StringUtils.isAlpha(first) && StringUtils.isAlpha(second)) {
                int a = (int) second.charAt(0);
                a++;
                if (a <= 90) {
                    return first + ((char) a);
                } else {
                    a = (int) first.charAt(0);
                    a++;
                    if (a <= 90) {
                        return ((char) a) + "A";
                    }
                }
            } else if (StringUtils.isNumeric(first) && StringUtils.isAlpha(second)) {
                int a = (int) second.charAt(0);
                a++;
                if (a < 90) {
                    return first + ((char) a);
                } else {
                    Integer number = Integer.parseInt(first);
                    number++;
                    return number + "A";
                }
            }
        }

        return nextSeq;
    }

    @Transactional(readOnly = false)
    public List<ObjectAttribute> applyDetails(Integer instanceId, List<InwardItemInstance> inwardItemInstances) {

        List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectId(instanceId);

        for (InwardItemInstance instance : inwardItemInstances) {

            ItemInstance selectedInstance = instance.getItem();

            ItemInstance exitInstance = itemInstanceRepository.getInstanceByItemAndMfrAndOemNumber(selectedInstance.getItem().getId(), selectedInstance.getManufacturer().getId(), selectedInstance.getOemNumber());

            if (exitInstance != null && !exitInstance.getId().equals(selectedInstance.getId())) {
                throw new CassiniException(instance.getItem().getOemNumber() + " Serial Number already exist. Please check the serial number once");
            }

            /*InwardItemInstance existOemNumberInstance = inwardItemInstanceRepository.findOemNumberInstanceByMfrCode(instance.getItem().getOemNumber(), instance.getItem().getManufacturer().getMfrCode());
            InwardItemInstance existUpnNumberInstance = inwardItemInstanceRepository.findUpnNumberInstance(instance.getItem().getInitialUpn());

            if (existOemNumberInstance != null && !existOemNumberInstance.getId().equals(instance.getId())) {
                throw new CassiniException(instance.getItem().getOemNumber() + " Serial Number already exist. Please check the serial number once");
            }

            if (existUpnNumberInstance != null && !existUpnNumberInstance.getId().equals(instance.getId())) {
                throw new CassiniException(instance.getItem().getInitialUpn() + " : UPN already exist. Please check the serial number once.");
            }*/

            ItemInstance itemInstance = instance.getItem();
            itemInstance.setInstanceName(itemInstance.getInitialUpn());
            itemInstance.setUpnNumber(itemInstance.getInitialUpn());
            itemInstance.setStatus(ItemInstanceStatus.STORE_SUBMITTED);
            itemInstance.setPresentStatus("STORE SUBMITTED");
            itemInstance = itemInstanceRepository.save(itemInstance);

            ItemInstanceStatusHistory presentStatus = instanceStatusHistoryRepository.getItemInstanceHistory(itemInstance.getId()).get(0);

            if (presentStatus != null && !presentStatus.getStatus().equals(ItemInstanceStatus.STORE_SUBMITTED)) {
                ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
                statusHistory.setItemInstance(itemInstance);
                statusHistory.setStatus(ItemInstanceStatus.STORE_SUBMITTED);
                statusHistory.setPresentStatus("STORE SUBMITTED");
                statusHistory.setTimestamp(new Date());
                statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());

                statusHistory = instanceStatusHistoryRepository.save(statusHistory);
            }

            for (ObjectAttribute objectAttribute : objectAttributes) {

                ObjectAttribute attribute = (ObjectAttribute) Utils.cloneObject(objectAttribute, ObjectAttribute.class);
                attribute.setId(new ObjectAttributeId(itemInstance.getId(), objectAttribute.getId().getAttributeDef()));

                ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());

                if (objectTypeAttribute.getDataType().equals(DataType.ATTACHMENT)) {
                    List<Integer> attachmentIds = new ArrayList<>();
                    for (Integer attachment : objectAttribute.getAttachmentValues()) {
                        AttributeAttachment attributeAttachment = attributeAttachmentRepository.findOne(attachment);

                        AttributeAttachment cloneAttachment = new AttributeAttachment();

                        cloneAttachment.setObjectId(instance.getItem().getId());
                        cloneAttachment.setAttributeDef(attributeAttachment.getAttributeDef());
                        cloneAttachment.setExtension(attributeAttachment.getExtension());
                        cloneAttachment.setName(attributeAttachment.getName());
                        cloneAttachment.setObjectType(attributeAttachment.getObjectType());
                        cloneAttachment.setSize(attributeAttachment.getSize());
                        cloneAttachment.setAddedOn(new Date());
                        cloneAttachment.setAddedBy(sessionWrapper.getSession().getLogin().getPerson().getId());

                        cloneAttachment = attributeAttachmentRepository.save(cloneAttachment);

                        File file = getObjectFile(instanceId, attributeAttachment.getId());

                        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + "attachments" + File.separator + instance.getItem().getId();
                        File fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.mkdirs();
                        }

                        String path1 = dir + File.separator + cloneAttachment.getId();
                        saveFileToDisk(file, path1);

                        attachmentIds.add(cloneAttachment.getId());

                    }

                    if (attachmentIds.size() > 0) {
                        attribute.setAttachmentValues(attachmentIds.stream().filter(Objects::nonNull).toArray((Integer[]::new)));
                    }
                }

                attribute = objectAttributeRepository.save(attribute);
            }
        }


        return objectAttributes;
    }

    public File getObjectFile(Integer instanceId, Integer attachmentId) {
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + "attachments" + File.separator + instanceId + File.separator + attachmentId;
        File file = new File(path);

        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }

    }

    @Transactional
    protected void saveFileToDisk(File fileToSave, String path) {
        try {
            File file = new File(path);

            if (!file.exists()) {
                file.createNewFile();
            }
            IOUtils.copy(new FileInputStream(fileToSave), new FileOutputStream(file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }

    @Transactional(readOnly = false)
    public ObjectAttribute updateInstanceAttributeDetails(Integer instanceId, ObjectAttribute objectAttribute) {

        InwardItemInstance inwardItemInstance = inwardItemInstanceRepository.findOne(instanceId);

        List<InwardItemInstance> itemInstances = inwardItemInstanceRepository.findByInwardItem(inwardItemInstance.getInwardItem());

        for (InwardItemInstance instance : itemInstances) {

            ObjectAttribute attribute = (ObjectAttribute) Utils.cloneObject(objectAttribute, ObjectAttribute.class);
            attribute.setId(new ObjectAttributeId(instance.getItem().getId(), objectAttribute.getId().getAttributeDef()));

            attribute = objectAttributeRepository.save(attribute);
        }


        return objectAttribute;
    }

    @Transactional(readOnly = true)
    public List<ItemInstanceStatusHistory> getItemStatusHistory(Integer instanceId) {
        return instanceStatusHistoryRepository.getItemInstanceHistory(instanceId);
    }

    @Transactional(readOnly = false)
    public ObjectAttribute deleteInstanceAttributeAttachment(Integer attributeDef, Integer attachmentId, Integer[] instanceIds) {

        for (Integer instanceId : instanceIds) {
            ObjectAttribute attribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(instanceId, attributeDef);

            List<Integer> attachmentIds = new ArrayList<>();

            for (Integer attachment : attribute.getAttachmentValues()) {
                if (!attachment.equals(attachmentId)) {
                    attachmentIds.add(attachment);
                }
            }

            attribute.setAttachmentValues(attachmentIds.stream().filter(Objects::nonNull).toArray((Integer[]::new)));

            attribute = objectAttributeRepository.save(attribute);
        }

        return null;
    }

    @Transactional(readOnly = true)
    public UpnDetailsDto getUpnDetails(Integer instanceId) {

        UpnDetailsDto upnDetails = new UpnDetailsDto();

        ItemInstance itemInstance = itemInstanceRepository.findOne(instanceId);

        List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.getInwardItemInstanceByInstance(itemInstance.getId());

        HashMap<Integer, Inward> inwardHashMap = new HashMap<>();

        inwardItemInstances.forEach(inwardItemInstance -> {
            InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());

            Inward inward = inwardRepository.findOne(inwardItem.getInward());

            inwardHashMap.put(inward.getId(), inward);
        });

        InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstances.get(0).getInwardItem());

        BomItem parent = bomItemRepository.findOne(inwardItem.getBomItem().getParent());
        if (parent.getBomItemType().equals(BomItemType.UNIT)) {
            upnDetails.setUnit(parent.getTypeRef());
            BomItem parent1 = bomItemRepository.findOne(parent.getParent());
            if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                upnDetails.setSubSystem(parent1.getTypeRef());
                BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                    upnDetails.setSection(parent2.getTypeRef());
                }
            } else {
                upnDetails.setSection(parent1.getTypeRef());
            }
        } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
            upnDetails.setSubSystem(parent.getTypeRef());
            BomItem parent1 = bomItemRepository.findOne(parent.getParent());
            upnDetails.setSection(parent1.getTypeRef());
        } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
            upnDetails.setSection(parent.getTypeRef());
        }

        upnDetails.setItemInstance(itemInstance);
        upnDetails.setBomItem(inwardItem.getBomItem());

        for (Inward inward1 : inwardHashMap.values()) {

            List<InwardItem> inwardItems = inwardItemRepository.findByInwardAndBomItem(inward1.getId(), inwardItem.getBomItem().getId());

            if (inwardItems.size() > 0) {
                inward1.setInwardItem(inwardItems.get(0));
            }

            upnDetails.getInwards().add(inward1);
        }

        upnDetails.setSystem(upnDetails.getInwards().get(0).getBom());
        upnDetails.getStatusHistories().addAll(instanceStatusHistoryRepository.getItemInstanceHistory(instanceId));

        for (ItemInstanceStatusHistory itemInstanceStatusHistory : upnDetails.getStatusHistories()) {
            if (itemInstanceStatusHistory.getStatus().equals(ItemInstanceStatus.ISSUE)) {
                BomItemInstance bomItemInstance = bomItemInstanceRepository.findByItemInstance(instanceId);

                if (bomItemInstance != null) {
                    IssueItem issueItem = issueItemRepository.getByBomItemInstance(bomItemInstance.getId());

                    upnDetails.setIssue(issueItem.getIssue());
                }
            }
        }

        String location = "";

        Storage presentStorage = itemInstance.getStorage();

        if (presentStorage != null) {
            if (presentStorage.getParent() != null) {
                Storage parentStorage = storageRepository.findOne(presentStorage.getParent());

                if (parentStorage != null) {
                    location = parentStorage.getName() + "/" + presentStorage.getName();
                    if (parentStorage.getParent() != null) {
                        Storage parentStorage1 = storageRepository.findOne(parentStorage.getParent());
                        location = parentStorage1.getName() + " /" + location;

                        if (parentStorage1.getParent() != null) {
                            Storage parentStorage2 = storageRepository.findOne(parentStorage1.getParent());
                            location = parentStorage2.getName() + " /" + location;

                            if (parentStorage2.getParent() != null) {

                                Storage parentStorage3 = storageRepository.findOne(parentStorage2.getParent());
                                location = parentStorage3.getName() + " /" + location;

                                if (parentStorage3.getParent() != null) {

                                    Storage parentStorage4 = storageRepository.findOne(parentStorage3.getParent());
                                    location = parentStorage4.getName() + " /" + location;
                                }
                            }
                        }

                    }
                }
            } else {
                location = presentStorage.getName();
            }
        }

        ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

        if (certificateNumberType != null) {
            ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(instanceId, certificateNumberType.getId());

            if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                upnDetails.setCertificateNumber(certificateNumber.getStringValue());
            }
        }

        upnDetails.setStorageLocation(location);

        return upnDetails;
    }

    @Transactional(readOnly = true)
    public UpnDetailsDto getLotUpnDetails(Integer instanceId) {

        UpnDetailsDto upnDetails = new UpnDetailsDto();

        ItemInstance itemInstance = itemInstanceRepository.findOne(instanceId);

        List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.getInwardItemInstanceByInstance(itemInstance.getId());

        HashMap<Integer, Inward> inwardHashMap = new HashMap<>();

        inwardItemInstances.forEach(inwardItemInstance -> {
            InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());

            Inward inward = inwardRepository.findOne(inwardItem.getInward());

            inwardHashMap.put(inward.getId(), inward);
        });

        InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstances.get(0).getInwardItem());

        BomItem parent = bomItemRepository.findOne(inwardItem.getBomItem().getParent());
        if (parent.getBomItemType().equals(BomItemType.UNIT)) {
            upnDetails.setUnit(parent.getTypeRef());
            BomItem parent1 = bomItemRepository.findOne(parent.getParent());
            if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                upnDetails.setSubSystem(parent1.getTypeRef());
                BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                    upnDetails.setSection(parent2.getTypeRef());
                }
            } else {
                upnDetails.setSection(parent1.getTypeRef());
            }
        } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
            upnDetails.setSubSystem(parent.getTypeRef());
            BomItem parent1 = bomItemRepository.findOne(parent.getParent());
            upnDetails.setSection(parent1.getTypeRef());
        } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
            upnDetails.setSection(parent.getTypeRef());
        }

        upnDetails.setItemInstance(itemInstance);
        upnDetails.setBomItem(inwardItem.getBomItem());
        for (Inward inward1 : inwardHashMap.values()) {

            List<InwardItem> inwardItems = inwardItemRepository.findByInwardAndBomItem(inward1.getId(), inwardItem.getBomItem().getId());

            if (inwardItems.size() > 0) {
                inward1.setInwardItem(inwardItems.get(0));
            }

            upnDetails.getInwards().add(inward1);
        }

        upnDetails.setSystem(upnDetails.getInwards().get(0).getBom());
        upnDetails.getStatusHistories().addAll(instanceStatusHistoryRepository.getItemInstanceHistory(instanceId));
        upnDetails.setAvailableLotQuantity(itemInstance.getLotSize());
        if (upnDetails.getItemInstance().getItem().getItemMaster().getItemType().getHasLots()) {
            upnDetails.setLotInstances(lotInstanceRepository.findByInstance(instanceId));

            if (upnDetails.getLotInstances().size() > 0) {
                for (LotInstance lotInstance : upnDetails.getLotInstances()) {

                    upnDetails.setAvailableLotQuantity(upnDetails.getAvailableLotQuantity() - lotInstance.getLotQty());

                    lotInstance.getInstanceHistories().addAll(lotInstanceHistoryRepository.getHistoryByLotInstance(lotInstance.getId()));

                    lotInstance.getInstanceHistories().forEach(lotInstanceHistory -> {
                        if (lotInstanceHistory.getStatus().equals(ItemInstanceStatus.ISSUE)) {
                            IssueItem issueItem = issueItemRepository.findOne(lotInstance.getIssueItem());

                            lotInstance.setIssue(issueItem.getIssue());
                        }
                    });
                }
            }
        }

        String location = "";

        Storage presentStorage = itemInstance.getStorage();

        if (presentStorage != null) {
            if (presentStorage.getParent() != null) {
                Storage parentStorage = storageRepository.findOne(presentStorage.getParent());

                if (parentStorage != null) {
                    location = parentStorage.getName() + "/" + presentStorage.getName();
                    if (parentStorage.getParent() != null) {
                        Storage parentStorage1 = storageRepository.findOne(parentStorage.getParent());
                        location = parentStorage1.getName() + " /" + location;

                        if (parentStorage1.getParent() != null) {
                            Storage parentStorage2 = storageRepository.findOne(parentStorage1.getParent());
                            location = parentStorage2.getName() + " /" + location;

                            if (parentStorage2.getParent() != null) {

                                Storage parentStorage3 = storageRepository.findOne(parentStorage2.getParent());
                                location = parentStorage3.getName() + " /" + location;

                                if (parentStorage3.getParent() != null) {

                                    Storage parentStorage4 = storageRepository.findOne(parentStorage3.getParent());
                                    location = parentStorage4.getName() + " /" + location;
                                }
                            }
                        }

                    }
                }
            } else {
                location = presentStorage.getName();
            }
        }

        ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

        if (certificateNumberType != null) {
            ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(instanceId, certificateNumberType.getId());

            if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                upnDetails.setCertificateNumber(certificateNumber.getStringValue());
            }
        }

        upnDetails.setStorageLocation(location);

        return upnDetails;
    }

    @Transactional(readOnly = true)
    public UpnDetailsDto getIssuedLotUpnDetails(Integer instanceId) {

        UpnDetailsDto upnDetails = new UpnDetailsDto();

        LotInstance lotInstance = lotInstanceRepository.findOne(instanceId);

        IssueItem issueItem = issueItemRepository.findOne(lotInstance.getIssueItem());

        RequestItem requestItem = requestItemRepository.findOne(issueItem.getRequestItem());

        upnDetails.setMissile(requestItem.getRequest().getBomInstance());
        upnDetails.setSystem(bomRepository.findOne(upnDetails.getMissile().getBom()));

        ItemInstance itemInstance = itemInstanceRepository.findOne(lotInstance.getInstance());

        List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.getInwardItemInstanceByInstance(itemInstance.getId());

        HashMap<Integer, Inward> inwardHashMap = new HashMap<>();

        inwardItemInstances.forEach(inwardItemInstance -> {
            InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstance.getInwardItem());

            Inward inward = inwardRepository.findOne(inwardItem.getInward());

            inwardHashMap.put(inward.getId(), inward);
        });

        InwardItem inwardItem = inwardItemRepository.findOne(inwardItemInstances.get(0).getInwardItem());

        BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(issueItem.getBomItemInstance().getBomInstanceItem());

        BomInstanceItem parent = bomInstanceItemRepository.findOne(bomInstanceItem.getParent());
        if (parent.getBomItemType().equals(BomItemType.UNIT)) {
            upnDetails.setUnit(parent.getTypeRef());
            BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
            if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                upnDetails.setSubSystem(parent1.getTypeRef());
                BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                    upnDetails.setSection(parent2.getTypeRef());
                }
            } else {
                upnDetails.setSection(parent1.getTypeRef());
            }
        } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
            upnDetails.setSubSystem(parent.getTypeRef());
            BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
            upnDetails.setSection(parent1.getTypeRef());
        } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
            upnDetails.setSection(parent.getTypeRef());
        }

        upnDetails.setItemInstance(itemInstance);
        upnDetails.setBomItem(inwardItem.getBomItem());
        for (Inward inward1 : inwardHashMap.values()) {
            List<InwardItem> inwardItems = inwardItemRepository.findByInwardAndBomItem(inward1.getId(), inwardItem.getBomItem().getId());

            if (inwardItems.size() > 0) {
                inward1.setInwardItem(inwardItems.get(0));
            }

            upnDetails.getInwards().add(inward1);
        }

        upnDetails.setSystem(upnDetails.getInwards().get(0).getBom());
        upnDetails.getStatusHistories().addAll(instanceStatusHistoryRepository.getItemInstanceHistory(itemInstance.getId()));
        upnDetails.setAvailableLotQuantity(itemInstance.getLotSize());
        upnDetails.getLotHistories().addAll(lotInstanceHistoryRepository.getHistoryByLotInstance(instanceId));

        upnDetails.getLotHistories().forEach(lotInstanceHistory -> {
            if (lotInstanceHistory.getStatus().equals(ItemInstanceStatus.ISSUE)) {
                IssueItem issueItem1 = issueItemRepository.findOne(lotInstance.getIssueItem());

                upnDetails.setIssue(issueItem1.getIssue());
            }
        });

        ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

        if (certificateNumberType != null) {
            ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

            if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                upnDetails.setCertificateNumber(certificateNumber.getStringValue());
            }
        }

        return upnDetails;
    }

    public void generateInstanceBarcode(String upnNumber, HttpServletResponse response) {

        if (upnNumber != null) {
            BitMatrix bitMatrix;

            try {
                response.setContentType("image/png");
                OutputStream out = response.getOutputStream();
                String msg = "{0}";
                msg = MessageFormat.format(msg, upnNumber);
                bitMatrix = new Code128Writer().encode(msg, BarcodeFormat.CODE_128, 150, 80, null);
                MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
                out.flush();
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean checkInwardsWithSupplier(Integer supplier) {
        List<Inward> inwards = inwardRepository.findInwardBySupplier(supplier);
        if (inwards.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional(readOnly = false)
    public ItemInstance nextLotNumber(Integer itemInstanceId) {
        ItemInstance itemInstance = itemInstanceRepository.findOne(itemInstanceId);
        AutoNumber autoNumber = autoNumberRepository.findByName("Default Lot Auto Number");
        if (autoNumber == null) {
            autoNumber = new AutoNumber();
            autoNumber.setName("Default Lot Auto Number");
            autoNumber.setDescription("Auto Lot Number");
            autoNumber.setNumbers(1);
            autoNumber.setStart(1);
            autoNumber.setIncrement(1);
            autoNumber.setNextNumber(1);
            autoNumber.setPadwith("0");
            autoNumber.setPrefix("-");

            autoNumber = autoNumberRepository.save(autoNumber);

        }
        String number = autoNumber.nextNumber();
        autoNumber = autoNumberRepository.save(autoNumber);

        itemInstance.setLotSequence(number);

        return itemInstance;
    }

    @Transactional(readOnly = false)
    public ItemInstance deleteGeneratedLotNumber(Integer itemInstanceId) {
        ItemInstance itemInstance = itemInstanceRepository.findOne(itemInstanceId);

        itemInstance.setOemNumber(null);
        itemInstance.setLotNumber(null);

        itemInstance = itemInstanceRepository.save(itemInstance);

        return itemInstance;
    }

    @Transactional(readOnly = true)
    public ItemInstance generateRootCardNumber(Integer itemInstanceId) {
        ItemInstance itemInstance = itemInstanceRepository.findOne(itemInstanceId);

        JSONParser jsonParser = new JSONParser();
        String defaultAirframeType = null;
        try {

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

            Object obj = null;
            try {
                obj = jsonParser.parse(jsonText);

                JSONObject jsonObject = (JSONObject) obj;
                defaultAirframeType = jsonObject.get("AirframeType").toString();
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        if (defaultAirframeType != null) {
            ItemType aitframeType = itemInstance.getItem().getItemMaster().getItemType();

            ItemType parentType = itemTypeRepository.findOne(aitframeType.getParentType());

            if (parentType.getName().equals(defaultAirframeType)) {
                itemInstance.setRootCardNo(getRootCardNumber());
            }
        }

        return itemInstance;
    }

    private String getRootCardNumber() {
        AutoNumber autoNumber = autoNumberRepository.findByName("Default Root Card Number");
        String next = "";
        if (autoNumber != null) {

            if (autoNumber.getPrefix() != null && !autoNumber.getPrefix().trim().isEmpty()) {
                next += autoNumber.getPrefix();
            }

            int n = autoNumber.getNextNumber();
            String s = "" + n;
            n += autoNumber.getIncrement();
            autoNumber.setNextNumber(n);
            next += s;

            autoNumber = autoNumberRepository.save(autoNumber);
        }

        return next;
    }
}
