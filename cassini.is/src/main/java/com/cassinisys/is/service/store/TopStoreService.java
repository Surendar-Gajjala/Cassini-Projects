// 
// Decompiled by Procyon v0.5.36
// 

package com.cassinisys.is.service.store;

import com.cassinisys.is.filtering.BoqItemCriteria;
import com.cassinisys.is.filtering.BoqItemPredicateBuilder;
import com.cassinisys.is.filtering.TopStoreCriteria;
import com.cassinisys.is.filtering.TopStorePredicateBuilder;
import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.pm.ISProjectResource;
import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.model.store.*;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.is.repo.pm.ResourceRepository;
import com.cassinisys.is.repo.procm.*;
import com.cassinisys.is.repo.store.*;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Export;
import com.cassinisys.platform.model.common.ExportRow;
import com.cassinisys.platform.model.common.ExportRowDetail;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.common.ExportService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.converter.ImportConverter;
import com.google.common.collect.Lists;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mysema.query.types.Predicate;
import freemarker.template.Configuration;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopStoreService implements CrudService<ISTopStore, Integer>
{
    public static Map fileMap;
    @Autowired
    Configuration fmConfiguration;
    List<StoreReportDTO> reportDTOs;
    @Autowired
    ISScrapRequestItemRepository isScrapRequestItemRepository;
    @Autowired
    private ISTopStoreRepository topStoreRepository;
    @Autowired
    private ExportService exportService;
    @Autowired
    private BoqItemRepository boqItemRepository;
    @Autowired
    private ISTopInventoryRepository topInventoryRepository;
    @Autowired
    private ISStockReceiveItemRepository topStockReceivedRepository;
    @Autowired
    private ISTStockIssueItemRepository stockIssueItemRepository;
    @Autowired
    private CustomIndentItemRepository customIndentItemRepository;
    @Autowired
    private CustomRoadChalanItemRepository customRoadChalanItemRepository;
    @Autowired
    private ISTStockIssueItemRepository topStockIssuedRepository;
    @Autowired
    private ISTopStockMovementRepository topStockMovementRepository;
    @Autowired
    private TopStorePredicateBuilder topStorePredicateBuilder;
    @Autowired
    private BoqItemPredicateBuilder boqItemPredicateBuilder;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private MachineItemRepository machineItemRepository;
    @Autowired
    private MaterialItemRepository materialItemRepository;
    @Autowired
    private MachineTypeRepository machineTypeRepository;
    @Autowired
    private MaterialTypeRepository materialTypeRepository;
    @Autowired
    private CustomPurchaseOrderRepository customPurchaseOrderRepository;
    @Autowired
    private CustomPurchaseOrderItemRepository customPurchaseOrderItemRepository;
    @Autowired
    private ISStockReceiveRepository stockReceiveRepository;
    @Autowired
    private CustomIndentRepository customIndentRepository;
    @Autowired
    private ISScrapRequestRepository isScrapRequestRepository;
    @Autowired
    private ISStockIssueRepository stockIssueRepository;
    @Autowired
    private CustomRoadChalanRepository customRoadChalanRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ResourceRepository projectResourceRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private AutoNumberService autoNumberService;

    public TopStoreService() {
        this.reportDTOs = new ArrayList<StoreReportDTO>();
    }

    @Transactional(readOnly = false)
    public ISTopStore create(final ISTopStore topStore) {
        final ISTopStore isTopStore = this.topStoreRepository.findByStoreNameEqualsIgnoreCase(topStore.getStoreName());
        if (isTopStore == null) {
            return (ISTopStore)this.topStoreRepository.save( topStore);
        }
        throw new RuntimeException("Store " + topStore.getStoreName() + " already exists");
    }

    @Transactional(readOnly = false)
    public ISTopStore update(final ISTopStore topStore) {
        return (ISTopStore)this.topStoreRepository.save( topStore);
    }

    @Transactional(readOnly = false)
    public void delete(final Integer id) {
        final List<ISTopInventory> topInventoryList = (List<ISTopInventory>)this.topInventoryRepository.findByStore((ISTopStore)this.topStoreRepository.findOne(id));
        for (final ISTopInventory topInventory : topInventoryList) {
            if (topInventory.getStoreOnHand() > 0.0) {
                throw new CassiniException("Selected Store has inventory, cannot delete this store");
            }
        }
        this.topStoreRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public ISTopStore get(final Integer id) {
        return (ISTopStore)this.topStoreRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<ISTopStore> getAll() {
        return (List<ISTopStore>)this.topStoreRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ISTopStore> getStoresByIds(final List<Integer> ids) {
        return (List<ISTopStore>)this.topStoreRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public Page<ISTopStore> getAllStores(final Pageable pageable) {
        return (Page<ISTopStore>)this.topStoreRepository.findAll(pageable);
    }

    @Transactional(readOnly = false)
    public List<ISStockReceiveItem> createStockReceived(final Integer storeId, final List<ISStockReceiveItem> stocksReceived) {
        final List<ISStockReceiveItem> stockReceiveItems = new ArrayList<ISStockReceiveItem>();
        final Iterator<ISStockReceiveItem> isStockReceiveItemIterator = stocksReceived.iterator();
        final List<ISTopInventory> topInventories = new ArrayList<ISTopInventory>();
        while (isStockReceiveItemIterator.hasNext()) {
            final ISStockReceiveItem stockReceived = isStockReceiveItemIterator.next();
            stockReceived.setObjectType((Enum)ISObjectType.RECEIVEITEM);
            Double openingBal = 0.0;
            final List<ISTopInventory> topInventories2 = (List<ISTopInventory>)this.topInventoryRepository.findByItemAndStore(stockReceived.getItem(), stockReceived.getStore());
            if (topInventories2.size() > 0) {
                for (final ISTopInventory topInventory : topInventories2) {
                    openingBal += topInventory.getStoreOnHand();
                }
                stockReceived.setOpeningBalance(openingBal);
                stockReceived.setClosingBalance(Double.valueOf(stockReceived.getOpeningBalance() + stockReceived.getQuantity()));
            }
            else {
                stockReceived.setOpeningBalance(Double.valueOf(0.0));
                stockReceived.setClosingBalance(stockReceived.getQuantity());
            }
            if (stockReceived != null) {
                ISTopInventory isItemInventory = this.topInventoryRepository.findByStoreAndItemAndProject(stockReceived.getStore(), stockReceived.getItem(), stockReceived.getProject());
                if (isItemInventory != null) {
                    isItemInventory.setStoreOnHand(Double.valueOf(isItemInventory.getStoreOnHand() + stockReceived.getQuantity()));
                    topInventories.add(isItemInventory);
                }
                else {
                    isItemInventory = new ISTopInventory();
                    isItemInventory.setStoreOnHand(stockReceived.getQuantity());
                    isItemInventory.setStore(stockReceived.getStore());
                    isItemInventory.setItem(stockReceived.getItem());
                    isItemInventory.setProject(stockReceived.getProject());
                    topInventories.add(isItemInventory);
                }
                stockReceived.setTimeStamp(new Date());
                this.topStockReceivedRepository.save( stockReceived);
            }
        }
        this.topInventoryRepository.save((Iterable)topInventories);
        return stocksReceived;
    }

    @Transactional(readOnly = false)
    public List<ISTStockIssueItem> createStockIssued(final Integer storeId, final List<ISTStockIssueItem> stocksIssued) {
        final ISStockIssue stockIssue = (ISStockIssue)this.stockIssueRepository.findOne(stocksIssued.get(0).getIssue());
        if (stocksIssued != null) {
            for (ISTStockIssueItem stockIssued : stocksIssued) {
                Double openingBal = 0.0;
                final List<ISTopInventory> topInventories1 = (List<ISTopInventory>)this.topInventoryRepository.findByItemAndStore(stockIssued.getItem(), stockIssued.getStore());
                if (topInventories1.size() > 0) {
                    for (final ISTopInventory topInventory : topInventories1) {
                        openingBal += topInventory.getStoreOnHand();
                    }
                    stockIssued.setOpeningBalance(openingBal);
                    stockIssued.setClosingBalance(Double.valueOf(stockIssued.getOpeningBalance() - stockIssued.getQuantity()));
                }
                else {
                    stockIssued.setOpeningBalance(Double.valueOf(0.0));
                    stockIssued.setClosingBalance(stockIssued.getQuantity());
                }
                if (stockIssued != null) {
                    stockIssued.setItem(stockIssued.getItem());
                    final ISTopInventory isItemInventory = this.topInventoryRepository.findByStoreAndItemAndProject(stockIssued.getStore(), stockIssued.getItem(), stockIssued.getProject());
                    if (isItemInventory == null) {
                        throw new RuntimeException("This store doesn't have inventory for this item.");
                    }
                    Double qty = isItemInventory.getStoreOnHand();
                    if (qty >= stockIssued.getQuantity()) {
                        qty -= stockIssued.getQuantity();
                        isItemInventory.setStoreOnHand(qty);
                    }
                    this.topInventoryRepository.save( isItemInventory);
                    final ISProjectResource projectResource = this.projectResourceRepository.findByProjectAndReferenceIdAndTaskAndResourceType(stockIssued.getProject(), stockIssued.getBoqReference(), stockIssue.getTask(), ResourceType.MATERIALTYPE);
                    projectResource.setIssuedQuantity(Double.valueOf(projectResource.getIssuedQuantity() + stockIssued.getQuantity()));
                    this.projectResourceRepository.save( projectResource);
                    stockIssued.setTimeStamp(new Date());
                    stockIssued = (ISTStockIssueItem)this.topStockIssuedRepository.save( stockIssued);
                }
            }
        }
        return stocksIssued;
    }

    @Transactional(readOnly = true)
    public List<ISTopStockMovement> getStockReceivedByStore(final Integer id, final MovementType movementType) {
        final ISTopStore store = (ISTopStore)this.topStoreRepository.findOne(id);
        final List<ISTopStockMovement> topStockMovements1 = (List<ISTopStockMovement>)this.topStockMovementRepository.findByStoreAndMovementType(store, movementType);
        final List<ISTopStockMovement> topStockMovements2 = new ArrayList<ISTopStockMovement>();
        final Map<Integer, ISTopStockMovement> hashMap = new HashMap<Integer, ISTopStockMovement>();
        for (final ISTopStockMovement stockMovement : topStockMovements1) {
            final ISTopStockMovement topStockMovement = hashMap.get(stockMovement.getItem());
            if (topStockMovement != null) {
                topStockMovement.setQuantity(Double.valueOf(topStockMovement.getQuantity() + stockMovement.getQuantity()));
            }
            else {
                if (topStockMovement != null) {
                    continue;
                }
                hashMap.put(stockMovement.getItem(), stockMovement);
                topStockMovements2.add(stockMovement);
            }
        }
        return topStockMovements2;
    }

    @Transactional(readOnly = true)
    public List<ISTopInventory> getInventoryByStore(final Integer id) {
        final ISTopStore store = (ISTopStore)this.topStoreRepository.findOne(id);
        final List<Integer> itemIds = (List<Integer>)this.topInventoryRepository.findDisItemsBystores(store.getId());
        final List<ISTopInventory> topInventories1 = new ArrayList<ISTopInventory>();
        final Map<String, ISTopInventory> hashMap = new HashMap<String, ISTopInventory>();
        for (final Integer itemId : itemIds) {
            final List<ISTopInventory> isTopInventories = (List<ISTopInventory>)this.topInventoryRepository.findByItemAndStore(itemId, store);
            for (final ISTopInventory inventory : isTopInventories) {
                ISMaterialItem materialItem = new ISMaterialItem();
                ISMachineItem machineItem = new ISMachineItem();
                materialItem = (ISMaterialItem)this.materialItemRepository.findOne(inventory.getItem());
                machineItem = (ISMachineItem)this.machineItemRepository.findOne(inventory.getItem());
                if (materialItem != null) {
                    final ISTopInventory topInventory1 = hashMap.get(materialItem.getItemNumber());
                    if (topInventory1 != null) {
                        topInventory1.setStoreOnHand(Double.valueOf(topInventory1.getStoreOnHand() + inventory.getStoreOnHand()));
                    }
                    else {
                        hashMap.put(materialItem.getItemNumber(), inventory);
                        topInventories1.add(inventory);
                    }
                }
                else {
                    if (machineItem == null) {
                        continue;
                    }
                    final ISTopInventory topInventory1 = hashMap.get(machineItem.getItemNumber());
                    if (topInventory1 != null) {
                        topInventory1.setStoreOnHand(Double.valueOf(topInventory1.getStoreOnHand() + inventory.getStoreOnHand()));
                    }
                    else {
                        hashMap.put(machineItem.getItemNumber(), inventory);
                        topInventories1.add(inventory);
                    }
                }
            }
        }
        for (final ISTopInventory topInventory2 : topInventories1) {
            topInventory2.setStockMovementDTO(this.getStockIssuedByItem(topInventory2.getItem(), topInventory2.getStore()));
        }
        return topInventories1;
    }

    @Transactional
    public List<ISTopInventory> findByStoreAndProject(final Integer storeId, final Integer projectId) {
        final List<ISTopInventory> topInventoryList = (List<ISTopInventory>)this.topInventoryRepository.findByStoreAndProject((ISTopStore)this.topStoreRepository.findOne(storeId), projectId);
        for (final ISTopInventory inventory : topInventoryList) {
            final ISMaterialItem materialItem = (ISMaterialItem)this.materialItemRepository.findOne(inventory.getItem());
            final ItemDTO itemDTO = new ItemDTO();
            itemDTO.setId(materialItem.getId());
            itemDTO.setDescription(materialItem.getDescription());
            itemDTO.setItemName(materialItem.getItemName());
            itemDTO.setItemType(materialItem.getItemType().getName());
            itemDTO.setItemNumber(materialItem.getItemNumber());
            itemDTO.setStoreInventory(inventory.getStoreOnHand());
            itemDTO.setResourceType(materialItem.getItemType().getName());
            inventory.setItemDTO(itemDTO);
        }
        return topInventoryList;
    }

    @Transactional(readOnly = true)
    public Page<ISTopInventory> getStoreInventory(final Integer storeId, final Pageable pageable) {
        final List<ISTopInventory> topInventories = this.getInventoryByStore(storeId);
        final int start = pageable.getOffset();
        final int end = (start + pageable.getPageSize() > topInventories.size()) ? topInventories.size() : (start + pageable.getPageSize());
        return (Page<ISTopInventory>)new PageImpl((List)topInventories.subList(start, end), pageable, (long)topInventories.size());
    }

    @Transactional(readOnly = true)
    public List<ISTopInventory> getInventoryByStoreAndItem(final Integer id, final Integer boqItem) {
        final ISTopStore store = (ISTopStore)this.topStoreRepository.findOne(id);
        return (List<ISTopInventory>)this.topInventoryRepository.findByItemAndStore(boqItem, store);
    }

    @Transactional(readOnly = true)
    public List<ISStockReceiveItem> getStockReceivedByStoreAndItem(final Integer storeId, final Integer boqItem) {
        final ISTopStore store = (ISTopStore)this.topStoreRepository.findOne(storeId);
        return (List<ISStockReceiveItem>)this.topStockReceivedRepository.findByStoreAndItem(store, boqItem);
    }

    @Transactional(readOnly = true)
    public List<ISTStockIssueItem> getStockIssuedByStoreAndItem(final Integer storeId, final Integer boqItem) {
        final ISTopStore store = (ISTopStore)this.topStoreRepository.findOne(storeId);
        return (List<ISTStockIssueItem>)this.topStockIssuedRepository.findByStoreAndItem(store, boqItem);
    }

    @Transactional(readOnly = true)
    public StockMovementDTO getStockIssuedByItem(final Integer itemId, final ISTopStore topStore) {
        final StockMovementDTO stockMovementDTO = new StockMovementDTO();
        Double recevQty = 0.0;
        Double issueQty = 0.0;
        Double returnedQty = 0.0;
        final ItemDTO itemDTO = new ItemDTO();
        final List<ISTopStockMovement> topStockMovements1 = (List<ISTopStockMovement>)this.topStockMovementRepository.findByItemAndStore(itemId, topStore);
        for (final ISTopStockMovement stockMovement : topStockMovements1) {
            if (stockMovement.getMovementType().equals( MovementType.ISSUED)) {
                issueQty += stockMovement.getQuantity();
            }
            else if (stockMovement.getMovementType().equals( MovementType.RECEIVED) || stockMovement.getMovementType().equals( MovementType.OPENINGBALANCE)) {
                recevQty += stockMovement.getQuantity();
            }
            else if (stockMovement.getMovementType().equals( MovementType.LOANISSUED) || stockMovement.getMovementType().equals( MovementType.LOANRETURNITEMISSUED)) {
                issueQty += stockMovement.getQuantity();
            }
            else if (stockMovement.getMovementType() == MovementType.LOANRECEIVED || stockMovement.getMovementType().equals( MovementType.LOANRETURNITEMRECEIVED)) {
                recevQty += stockMovement.getQuantity();
            }
            else {
                if (stockMovement.getMovementType() != MovementType.RETURNED) {
                    continue;
                }
                returnedQty += stockMovement.getQuantity();
            }
        }
        final ISMaterialItem materialItem = (ISMaterialItem)this.materialItemRepository.findOne(itemId);
        final ISMachineItem machineItem = (ISMachineItem)this.machineItemRepository.findOne(itemId);
        if (machineItem != null) {
            itemDTO.setItemNumber(machineItem.getItemNumber());
            itemDTO.setId(machineItem.getId());
            itemDTO.setDescription(machineItem.getDescription());
            itemDTO.setItemName(machineItem.getItemName());
            itemDTO.setItemType(machineItem.getItemType().getName());
            itemDTO.setResourceType("MACHINETYPE");
        }
        else {
            itemDTO.setItemNumber(materialItem.getItemNumber());
            itemDTO.setId(materialItem.getId());
            itemDTO.setDescription(materialItem.getDescription());
            itemDTO.setItemName(materialItem.getItemName());
            itemDTO.setItemType(materialItem.getItemType().getName());
            itemDTO.setUnits(materialItem.getUnits());
            itemDTO.setResourceType("MATERIALTYPE");
            itemDTO.setMaterialType(materialItem.getItemType());
        }
        stockMovementDTO.setItemDTO(itemDTO);
        stockMovementDTO.setIssuedQty(issueQty);
        stockMovementDTO.setReceivedQty(recevQty);
        stockMovementDTO.setReturnedQty(returnedQty);
        return stockMovementDTO;
    }

    @Transactional(readOnly = true)
    public ISTopStore getStoreByName(final String storeName) {
        return this.topStoreRepository.findByStoreNameEqualsIgnoreCase(storeName);
    }

    public Page<ISTopStore> freeTextSearch(final Pageable pageable, final TopStoreCriteria storeCriteria) {
        final Predicate predicate = this.topStorePredicateBuilder.build(storeCriteria, QISTopStore.iSTopStore);
        return (Page<ISTopStore>)this.topStoreRepository.findAll(predicate, pageable);
    }

    public List<ISTopStore> searchStores(final TopStoreCriteria storeCriteria) {
        final Predicate predicate = this.topStorePredicateBuilder.build(storeCriteria, QISTopStore.iSTopStore);
        final List<ISTopStore> list = (List<ISTopStore>)Lists.newArrayList(this.topStoreRepository.findAll(predicate));
        return list;
    }

    public Page<ISBoqItem> freeTextSearchForBoqItem(final Pageable pageable, final BoqItemCriteria boqItemCriteria) {
        final Predicate predicate = this.boqItemPredicateBuilder.build(boqItemCriteria, QISBoqItem.iSBoqItem);
        return (Page<ISBoqItem>)this.boqItemRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ISBoqItem> getTopInventoryByFilters(final BoqItemCriteria criteria, final Pageable pageable) {
        final Predicate predicate = this.boqItemPredicateBuilder.build(criteria, QISBoqItem.iSBoqItem);
        return (Page<ISBoqItem>)this.boqItemRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public List<ISStockReceiveItem> getAllItemQuantityByItem(final Integer boqItem) {
        return (List<ISStockReceiveItem>)this.topStockReceivedRepository.findByItem(boqItem);
    }

    @Transactional(readOnly = true)
    public Map<Integer, Double> getStockReceivedQuantities(final List<Integer> itemIds) {
        final Map<Integer, Double> map = new HashMap<Integer, Double>();
        final List<ISStockReceiveItem> stockReceiveds = (List<ISStockReceiveItem>)this.topStockReceivedRepository.findByItemIn((Iterable)itemIds);
        for (final ISStockReceiveItem stockReceived : stockReceiveds) {
            final Integer item = stockReceived.getItem();
            final Double qty = map.get(item);
            if (qty == null) {
                map.put(item, stockReceived.getQuantity());
            }
            else {
                map.put(item, qty + stockReceived.getQuantity());
            }
        }
        return map;
    }

    @Transactional(readOnly = true)
    public List<ISTopInventory> getInventoryByMultipleStoresAndItems(final List<Integer> storeIds, final List<Integer> itemIds) {
        final List<ISTopStore> topStores = new ArrayList<ISTopStore>();
        for (final Integer storeId : storeIds) {
            final ISTopStore topStore = (ISTopStore)this.topStoreRepository.findOne(storeId);
            topStores.add(topStore);
        }
        return (List<ISTopInventory>)this.topInventoryRepository.findByStoreInAndItemIn((Iterable)topStores, (Iterable)itemIds);
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getReceivedAttributes(final String objectType) {
        final List<ObjectTypeAttribute> objectTypeAttributes = (List<ObjectTypeAttribute>)this.objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType));
        return objectTypeAttributes;
    }

    @Transactional(readOnly = true)
    public Page<ISTopStore> find(final TopStoreCriteria criteria, final Pageable pageable) {
        final Predicate predicate = this.topStorePredicateBuilder.build(criteria, QISTopStore.iSTopStore);
        return (Page<ISTopStore>)this.topStoreRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public List<StoreReportDTO> getReportByDates(final Integer store, final String startDate1, final String endDate1) {
        this.reportDTOs.clear();
        final List<ISTopInventory> topInventories = (List<ISTopInventory>)this.topInventoryRepository.findByStore((ISTopStore)this.topStoreRepository.getOne(store));
        final List<Integer> itemIds = topInventories.stream().map(ISTopInventory::getItem).collect(Collectors.toList());
        final List<ISMaterialItem> materialItems = (List<ISMaterialItem>)this.materialItemRepository.findByIdIn((Iterable)itemIds);
        if (itemIds.size() > 0) {
            Date startDate2 = null;
            if (startDate1.equals("undefined") || startDate1.equals("null") || startDate1.trim().equals("")) {
                startDate2 = this.topStockMovementRepository.getMinimumDate((List)itemIds, store);
                if (startDate2 == null) {
                    startDate2 = new Date();
                }
            }
            else {
                startDate2 = this.stringToDate(startDate1);
            }
            final Date startDate3 = new Date(startDate2.getTime() - 60000L);
            final Date endDate2 = new Date(this.stringToDate(endDate1).getTime() + 86400000L);
            final List<ISTopStockMovement> topStockMovements = (List<ISTopStockMovement>)this.topStockMovementRepository.findByItemInAndStoreAndTimeStampAfterAndTimeStampBeforeAndMovementTypeNot((List)itemIds, (ISTopStore)this.topStoreRepository.getOne(store), startDate3, endDate2, MovementType.ALLOCATED);
            if (topStockMovements.size() > 0) {
                final Map<Integer, List<ISTopStockMovement>> map = topStockMovements.stream().collect(Collectors.groupingBy(ISTopStockMovement::getItem));
                for (int i = 0; i < materialItems.size(); ++i) {
                    final ISMaterialItem materialItem = materialItems.get(i);
                    Double receivedQty = 0.0;
                    Double issuedQty = 0.0;
                    Double openingBal = 0.0;
                    Double closingBal = 0.0;
                    Double returnedQty = 0.0;
                    if (map.containsKey(materialItem.getId())) {
                        final List<ISTopStockMovement> materialItems2 = map.get(materialItem.getId());
                        materialItems2.sort(Comparator.comparing(o -> o.getTimeStamp()));
                        receivedQty = 0.0;
                        issuedQty = 0.0;
                        openingBal = materialItems2.get(0).getOpeningBalance();
                        closingBal = materialItems2.get(materialItems2.size() - 1).getClosingBalance();
                        for (final ISTopStockMovement topStockMovement : materialItems2) {
                            if (topStockMovement.getMovementType() == MovementType.valueOf("RECEIVED") || topStockMovement.getMovementType() == MovementType.valueOf("LOANRECEIVED") || topStockMovement.getMovementType() == MovementType.valueOf("LOANRETURNITEMRECEIVED") || topStockMovement.getMovementType() == MovementType.valueOf("OPENINGBALANCE")) {
                                receivedQty += topStockMovement.getQuantity();
                            }
                            else if (topStockMovement.getMovementType() == MovementType.valueOf("ISSUED") || topStockMovement.getMovementType() == MovementType.valueOf("LOANISSUED") || topStockMovement.getMovementType() == MovementType.valueOf("LOANRETURNITEMISSUED")) {
                                issuedQty += topStockMovement.getQuantity();
                            }
                            else {
                                if (topStockMovement.getMovementType() != MovementType.RETURNED) {
                                    continue;
                                }
                                returnedQty += topStockMovement.getQuantity();
                            }
                        }
                    }
                    this.reportDTOs.add(new StoreReportDTO(materialItem.getItemNumber(), materialItem.getItemName(), materialItem.getUnits(), openingBal, receivedQty, issuedQty, returnedQty, closingBal));
                }
            }
        }
        return this.reportDTOs;
    }

    private Date stringToDate(final String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public String exportTaskReport(final String fileType, final HttpServletResponse response) {
        final List<String> columns = new ArrayList<String>(Arrays.asList("Item No", "Item", "Units", "Opening Qty", "Received Qty", "Issued Qty", "Returned Qty", "Closing Qty"));
        final Export export = new Export();
        export.setFileName("Store-Report");
        export.setHeaders((List)columns);
        this.exportService.createExportObject((List)this.reportDTOs, (List)columns, export);
        return this.exportService.exportFile(fileType, export, response);
    }

    public String printReceiveChallan(final Integer receiveId, final HttpServletRequest request, final HttpServletResponse response) {
        InputStream is = null;
        String fileId = null;
        final ISStockReceive stockReceive = (ISStockReceive)this.stockReceiveRepository.findOne(receiveId);
        fileId = stockReceive.getReceiveNumberSource() + ".html";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileId + "\"");
            response.setContentType("text/html");
            final List<ISStockReceiveItem> stockReceiveItems = (List<ISStockReceiveItem>)this.topStockReceivedRepository.findByReceive(receiveId);
            final String htmlResponse = this.getExportHtml(stockReceiveItems, stockReceive, request, response);
            response.setContentLength(htmlResponse.getBytes().length);
            is = new ByteArrayInputStream(htmlResponse.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        fileId = fileId.replace("/", ".");
        TopStoreService.fileMap.put(fileId, is);
        return fileId;
    }

    @Transactional(readOnly = true)
    public String getExportHtml(final List<ISStockReceiveItem> stockReceiveItems, final ISStockReceive stockReceive, final HttpServletRequest request, final HttpServletResponse response) {
        ISProject project = new ISProject();
        if (stockReceive.getProject() != null) {
            project = (ISProject)this.projectRepository.findOne(stockReceive.getProject());
        }
        final ISTopStore topStore = (ISTopStore)this.topStoreRepository.findOne(stockReceive.getStore());
        final List<ObjectAtrributeDTO> objectAtrributeDTOList1 = new ArrayList<ObjectAtrributeDTO>();
        final List<ObjectAtrributeDTO> objectAtrributeDTOList2 = new ArrayList<ObjectAtrributeDTO>();
        int i = 0;
        final List<ObjectTypeAttribute> objectTypeAttributes = (List<ObjectTypeAttribute>)this.objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.RECEIVE);
        for (final ObjectTypeAttribute objectTypeAttribute : objectTypeAttributes) {
            final ObjectAtrributeDTO objectAtrributeDTO = new ObjectAtrributeDTO();
            objectAtrributeDTO.setObjectAttribute(this.objectAttributeRepository.findByObjectIdAndAttributeDefId(stockReceive.getId(), objectTypeAttribute.getId()));
            objectAtrributeDTO.setObjectTypeAttribute(objectTypeAttribute);
            if (++i % 2 == 0) {
                objectAtrributeDTOList1.add(objectAtrributeDTO);
            }
            else {
                objectAtrributeDTOList2.add(objectAtrributeDTO);
            }
        }
        for (final ISStockReceiveItem stockReceiveItem : stockReceiveItems) {
            stockReceiveItem.setItemAttributesList((List)new ArrayList());
            final ISMachineItem machineItem = (ISMachineItem)this.machineItemRepository.findOne(stockReceiveItem.getItem());
            final ISMaterialItem materialItem = (ISMaterialItem)this.materialItemRepository.findOne(stockReceiveItem.getItem());
            final List<ObjectTypeAttribute> itemAttributesList = (List<ObjectTypeAttribute>)this.objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.RECEIVEITEM);
            for (final ObjectTypeAttribute objectTypeAttribute2 : itemAttributesList) {
                if (!objectTypeAttribute2.getDataType().equals( DataType.ATTACHMENT) && !objectTypeAttribute2.getDataType().equals( DataType.IMAGE)) {
                    final ObjectAtrributeDTO objectAtrributeDTO2 = new ObjectAtrributeDTO();
                    objectAtrributeDTO2.setObjectAttribute(this.objectAttributeRepository.findByObjectIdAndAttributeDefId(stockReceiveItem.getId(), objectTypeAttribute2.getId()));
                    objectAtrributeDTO2.setObjectTypeAttribute(objectTypeAttribute2);
                    stockReceiveItem.getItemAttributesList().add(objectAtrributeDTO2);
                }
            }
            if (machineItem != null) {
                stockReceiveItem.getItemDTO().setItemNumber(machineItem.getItemNumber());
                stockReceiveItem.getItemDTO().setDescription(machineItem.getDescription());
                stockReceiveItem.getItemDTO().setItemName(machineItem.getItemName());
                stockReceiveItem.getItemDTO().setItemType(machineItem.getItemType().getName());
                stockReceiveItem.getItemDTO().setUnits(machineItem.getUnits());
            }
            else {
                stockReceiveItem.getItemDTO().setItemNumber(materialItem.getItemNumber());
                stockReceiveItem.getItemDTO().setDescription(materialItem.getDescription());
                stockReceiveItem.getItemDTO().setItemName(materialItem.getItemName());
                stockReceiveItem.getItemDTO().setItemType(materialItem.getItemType().getName());
                stockReceiveItem.getItemDTO().setUnits(materialItem.getUnits());
            }
            final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            final String createdDate = df.format(stockReceiveItem.getTimeStamp());
            stockReceiveItem.setReceiveDate(createdDate);
        }
        String imgUrl = null;
        String title = null;
        String supplier = "";
        if (stockReceive.getSupplier() != null) {
            supplier = ((ISSupplier)this.supplierRepository.findOne(stockReceive.getSupplier())).getName();
        }
        final ServletContext context = request.getServletContext();
        final String path = context.getRealPath("/") + "application.json";
        final JSONParser parser = new JSONParser();
        try {
            final Object obj = parser.parse((Reader)new FileReader(path));
            final JSONObject jsonObject = (JSONObject)obj;
            title = (String)jsonObject.get( "printTemplateTitle");
            final String localImg = (String)jsonObject.get( "templateLogo");
            imgUrl = this.getLocalHostUrl() + "/" + localImg;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        catch (ParseException e3) {
            e3.printStackTrace();
        }
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectAtrributes1", objectAtrributeDTOList1);
        model.put("objectAtrributes2", objectAtrributeDTOList2);
        model.put("itemAttributesList", stockReceiveItems.get(0).getItemAttributesList());
        model.put("receiveItems", stockReceiveItems);
        model.put("receive", stockReceive);
        if (!supplier.equals("")) {
            model.put("supplier", supplier);
        }
        if (project != null) {
            model.put("projectName", project.getName());
        }
        model.put("storeName", topStore.getStoreName());
        model.put("logo", imgUrl);
        model.put("title", title);
        final String exportHtmlData = this.getContentFromTemplate(model);
        return exportHtmlData;
    }

    @Transactional(readOnly = true)
    public String getContentFromTemplate(final Map<String, Object> model) {
        final StringBuffer content = new StringBuffer();
        try {
            this.fmConfiguration.setClassForTemplateLoading((Class)this.getClass(), "/nfr/");
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(this.fmConfiguration.getTemplate("receiveChallan.html"),  model));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public String printRoadChallan(final Integer roadChalanId, final HttpServletRequest request, final HttpServletResponse response) {
        InputStream is = null;
        String fileId = null;
        final CustomRoadChalan customRoadChalan = (CustomRoadChalan)this.customRoadChalanRepository.findOne(roadChalanId);
        fileId = customRoadChalan.getChalanNumber() + ".html";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileId + "\"");
            response.setContentType("text/html");
            final List<CustomRoadChalanItem> customRoadChalanItems = (List<CustomRoadChalanItem>)this.customRoadChalanItemRepository.findByRoadChalan(customRoadChalan);
            final String htmlResponse = this.getRoadChallanExportHtml(customRoadChalanItems, customRoadChalan, request, response);
            response.setContentLength(htmlResponse.getBytes().length);
            is = new ByteArrayInputStream(htmlResponse.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        fileId = fileId.replace("/", ".");
        TopStoreService.fileMap.put(fileId, is);
        return fileId;
    }

    @Transactional(readOnly = true)
    public String getRoadChallanExportHtml(final List<CustomRoadChalanItem> customRoadChalanItems, final CustomRoadChalan roadChalan, final HttpServletRequest request, final HttpServletResponse response) {
        final ISTopStore topStore = roadChalan.getStore();
        final List<ObjectAtrributeDTO> objectAtrributeDTOList1 = new ArrayList<ObjectAtrributeDTO>();
        final List<ObjectAtrributeDTO> objectAtrributeDTOList2 = new ArrayList<ObjectAtrributeDTO>();
        int i = 0;
        final List<ObjectTypeAttribute> objectTypeAttributes = (List<ObjectTypeAttribute>)this.objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.ROADCHALLAN);
        for (final ObjectTypeAttribute objectTypeAttribute : objectTypeAttributes) {
            final ObjectAtrributeDTO objectAtrributeDTO = new ObjectAtrributeDTO();
            objectAtrributeDTO.setObjectAttribute(this.objectAttributeRepository.findByObjectIdAndAttributeDefId(roadChalan.getId(), objectTypeAttribute.getId()));
            objectAtrributeDTO.setObjectTypeAttribute(objectTypeAttribute);
            if (++i % 2 == 0) {
                objectAtrributeDTOList1.add(objectAtrributeDTO);
            }
            else {
                objectAtrributeDTOList2.add(objectAtrributeDTO);
            }
        }
        for (final CustomRoadChalanItem customRoadChalanItem : customRoadChalanItems) {
            customRoadChalanItem.setItemAttributesList((List)new ArrayList());
            final List<ObjectTypeAttribute> itemAttributesList = (List<ObjectTypeAttribute>)this.objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.ROADCHALLANITEM);
            for (final ObjectTypeAttribute objectTypeAttribute2 : itemAttributesList) {
                if (!objectTypeAttribute2.getDataType().equals( DataType.ATTACHMENT) && !objectTypeAttribute2.getDataType().equals( DataType.IMAGE)) {
                    final ObjectAtrributeDTO objectAtrributeDTO2 = new ObjectAtrributeDTO();
                    objectAtrributeDTO2.setObjectAttribute(this.objectAttributeRepository.findByObjectIdAndAttributeDefId(customRoadChalanItem.getId(), objectTypeAttribute2.getId()));
                    objectAtrributeDTO2.setObjectTypeAttribute(objectTypeAttribute2);
                    customRoadChalanItem.getItemAttributesList().add(objectAtrributeDTO2);
                }
            }
            customRoadChalanItem.getItemDTO().setItemNumber(customRoadChalanItem.getMaterialItem().getItemNumber());
            customRoadChalanItem.getItemDTO().setDescription(customRoadChalanItem.getMaterialItem().getDescription());
            customRoadChalanItem.getItemDTO().setItemName(customRoadChalanItem.getMaterialItem().getItemName());
            customRoadChalanItem.getItemDTO().setItemType(customRoadChalanItem.getMaterialItem().getItemType().getName());
            customRoadChalanItem.getItemDTO().setUnits(customRoadChalanItem.getMaterialItem().getUnits());
            final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            final String createdDate = df.format(roadChalan.getChalanDate());
            customRoadChalanItem.setReceiveDate(createdDate);
        }
        String imgUrl = null;
        String title = null;
        final ServletContext context = request.getServletContext();
        final String path = context.getRealPath("/") + "application.json";
        final JSONParser parser = new JSONParser();
        try {
            final Object obj = parser.parse((Reader)new FileReader(path));
            final JSONObject jsonObject = (JSONObject)obj;
            title = (String)jsonObject.get( "printTemplateTitle");
            final String localImg = (String)jsonObject.get( "templateLogo");
            imgUrl = this.getLocalHostUrl() + "/" + localImg;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        catch (ParseException e3) {
            e3.printStackTrace();
        }
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectAtrributes1", objectAtrributeDTOList1);
        model.put("objectAtrributes2", objectAtrributeDTOList2);
        model.put("itemAttributesList", customRoadChalanItems.get(0).getItemAttributesList());
        model.put("roadChalanItems", customRoadChalanItems);
        model.put("roadChalan", roadChalan);
        model.put("logo", imgUrl);
        model.put("title", title);
        final String exportHtmlData = this.getRoadChallanFromTemplate(model);
        return exportHtmlData;
    }

    @Transactional(readOnly = true)
    public String getRoadChallanFromTemplate(final Map<String, Object> model) {
        final StringBuffer content = new StringBuffer();
        try {
            this.fmConfiguration.setClassForTemplateLoading((Class)this.getClass(), "/nfr/");
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(this.fmConfiguration.getTemplate("roadChallan.html"),  model));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public void downloadExportFile(final String fileId, final HttpServletResponse response) {
        try {
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileId, "UTF-8"));
        }
        catch (UnsupportedEncodingException var7) {
            response.setHeader("Content-disposition", "attachment; filename=" + fileId);
        }
        try {
            final ServletOutputStream var5 = response.getOutputStream();
            IOUtils.copy((InputStream)TopStoreService.fileMap.get(fileId), (OutputStream)var5);
            var5.flush();
        }
        catch (IOException var6) {
            var6.printStackTrace();
        }
    }

    public String printIndentChallan(final Integer indentId, final HttpServletRequest request, final HttpServletResponse response) {
        InputStream is = null;
        String fileId = null;
        final CustomIndent customIndent = (CustomIndent)this.customIndentRepository.findOne(indentId);
        fileId = customIndent.getIndentNumber() + ".html";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileId + "\"");
            response.setContentType("text/html");
            final List<CustomIndentItem> indentItems = (List<CustomIndentItem>)this.customIndentItemRepository.findByIndentId(indentId);
            final String htmlResponse = this.getIndentChallanExportHtml(indentItems, customIndent, request, response);
            response.setContentLength(htmlResponse.getBytes().length);
            is = new ByteArrayInputStream(htmlResponse.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        fileId = fileId.replace("/", ".");
        TopStoreService.fileMap.put(fileId, is);
        return fileId;
    }

    @Transactional(readOnly = true)
    public String getIndentChallanExportHtml(final List<CustomIndentItem> indentItems, final CustomIndent customIndent, final HttpServletRequest request, final HttpServletResponse response) {
        final ISProject project = customIndent.getProject();
        final ISTopStore topStore = (ISTopStore)this.topStoreRepository.findOne(customIndent.getStore());
        String imgUrl = null;
        String title = null;
        final ServletContext context = request.getServletContext();
        final String path = context.getRealPath("/") + "application.json";
        final JSONParser parser = new JSONParser();
        try {
            final Object obj = parser.parse((Reader)new FileReader(path));
            final JSONObject jsonObject = (JSONObject)obj;
            title = (String)jsonObject.get( "printTemplateTitle");
            final String localImg = (String)jsonObject.get( "templateLogo");
            imgUrl = this.getLocalHostUrl() + "/" + localImg;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        catch (ParseException e3) {
            e3.printStackTrace();
        }
        final List<ObjectAtrributeDTO> objectAtrributeDTOList1 = new ArrayList<ObjectAtrributeDTO>();
        final List<ObjectAtrributeDTO> objectAtrributeDTOList2 = new ArrayList<ObjectAtrributeDTO>();
        int i = 0;
        final List<ObjectTypeAttribute> objectTypeAttributes = (List<ObjectTypeAttribute>)this.objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.valueOf("CUSTOM_INDENT"));
        for (final ObjectTypeAttribute objectTypeAttribute : objectTypeAttributes) {
            final ObjectAtrributeDTO objectAtrributeDTO = new ObjectAtrributeDTO();
            objectAtrributeDTO.setObjectAttribute(this.objectAttributeRepository.findByObjectIdAndAttributeDefId(customIndent.getId(), objectTypeAttribute.getId()));
            objectAtrributeDTO.setObjectTypeAttribute(objectTypeAttribute);
            if (++i % 2 == 0) {
                objectAtrributeDTOList1.add(objectAtrributeDTO);
            }
            else {
                objectAtrributeDTOList2.add(objectAtrributeDTO);
            }
        }
        final Map<String, Integer> map = new HashMap<String, Integer>();
        for (final CustomIndentItem indentItem : indentItems) {
            final Integer integer = map.get(indentItem.getRequisition().getRequisitionNumber());
            if (integer == null) {
                map.put(indentItem.getRequisition().getRequisitionNumber(), indentItem.getId());
            }
        }
        final Set<String> requisitionNumbers = map.keySet();
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectAtrributes1", objectAtrributeDTOList1);
        model.put("objectAtrributes2", objectAtrributeDTOList2);
        model.put("reqNumbers", requisitionNumbers);
        model.put("indentItems", indentItems);
        model.put("indent", customIndent);
        model.put("projectName", project.getName());
        model.put("storeName", topStore.getStoreName());
        model.put("logo", imgUrl);
        model.put("title", title);
        final String exportHtmlData = this.getIndentChallanFromTemplate(model);
        return exportHtmlData;
    }

    public String printScrapChallan(final Integer scrapDetailsId, final HttpServletRequest request, final HttpServletResponse response) {
        InputStream is = null;
        String fileId = null;
        final ISScrapRequest scrapRequest = (ISScrapRequest)this.isScrapRequestRepository.findOne(scrapDetailsId);
        fileId = scrapRequest.getScrapNumber() + ".html";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileId + "\"");
            response.setContentType("text/html");
            final List<ISScrapRequestItem> scrapRequestItems = (List<ISScrapRequestItem>)this.isScrapRequestItemRepository.findByScrapRequest(scrapDetailsId);
            final String htmlResponse = this.getScrapChallanExportHtml(scrapRequestItems, scrapRequest, request, response);
            response.setContentLength(htmlResponse.getBytes().length);
            is = new ByteArrayInputStream(htmlResponse.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        fileId = fileId.replace("/", ".");
        TopStoreService.fileMap.put(fileId, is);
        return fileId;
    }

    @Transactional(readOnly = true)
    public String getScrapChallanExportHtml(final List<ISScrapRequestItem> scrapRequestItems, final ISScrapRequest scrapRequest, final HttpServletRequest request, final HttpServletResponse response) {
        final ISTopStore topStore = (ISTopStore)this.topStoreRepository.findOne(scrapRequest.getStore());
        final List<ObjectAtrributeDTO> objectAtrributeDTOList = new ArrayList<ObjectAtrributeDTO>();
        final List<ObjectTypeAttribute> objectTypeAttributes = (List<ObjectTypeAttribute>)this.objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.SCRAPREQUEST);
        for (final ObjectTypeAttribute objectTypeAttribute : objectTypeAttributes) {
            final ObjectAtrributeDTO objectAtrributeDTO = new ObjectAtrributeDTO();
            objectAtrributeDTO.setObjectAttribute(this.objectAttributeRepository.findByObjectIdAndAttributeDefId(scrapRequest.getId(), objectTypeAttribute.getId()));
            objectAtrributeDTO.setObjectTypeAttribute(objectTypeAttribute);
            objectAtrributeDTOList.add(objectAtrributeDTO);
        }
        for (final ISScrapRequestItem scrapRequestItem : scrapRequestItems) {
            final ISMaterialItem materialItem = (ISMaterialItem)this.materialItemRepository.findOne(scrapRequestItem.getItem());
            scrapRequestItem.getItemDTO().setItemNumber(materialItem.getItemNumber());
            scrapRequestItem.getItemDTO().setDescription(materialItem.getDescription());
            scrapRequestItem.getItemDTO().setItemName(materialItem.getItemName());
            scrapRequestItem.getItemDTO().setItemType(materialItem.getItemType().getName());
            scrapRequestItem.getItemDTO().setUnits(materialItem.getUnits());
        }
        String imgUrl = null;
        String title = null;
        final ServletContext context = request.getServletContext();
        final String path = context.getRealPath("/") + "application.json";
        final JSONParser parser = new JSONParser();
        try {
            final Object obj = parser.parse((Reader)new FileReader(path));
            final JSONObject jsonObject = (JSONObject)obj;
            title = (String)jsonObject.get( "printTemplateTitle");
            final String localImg = (String)jsonObject.get( "templateLogo");
            imgUrl = this.getLocalHostUrl() + "/" + localImg;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        catch (ParseException e3) {
            e3.printStackTrace();
        }
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectAtrributes", objectAtrributeDTOList);
        model.put("itemAttributesList", scrapRequestItems.get(0).getItemAttributesList());
        model.put("scrapRequestItems", scrapRequestItems);
        model.put("scrapRequest", scrapRequest);
        model.put("storeName", topStore.getStoreName());
        model.put("logo", imgUrl);
        model.put("title", title);
        final String exportHtmlData = this.getScrapChallanFromTemplate(model);
        return exportHtmlData;
    }

    @Transactional(readOnly = true)
    public String getScrapChallanFromTemplate(final Map<String, Object> model) {
        final StringBuffer content = new StringBuffer();
        try {
            this.fmConfiguration.setClassForTemplateLoading((Class)this.getClass(), "/nfr/");
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(this.fmConfiguration.getTemplate("scrapChallan.html"),  model));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public String printIssueChallan(final Integer issueId, final HttpServletRequest request, final HttpServletResponse response) {
        InputStream is = null;
        String fileId = null;
        final ISStockIssue isStockIssue = (ISStockIssue)this.stockIssueRepository.findOne(issueId);
        fileId = isStockIssue.getIssueNumberSource() + ".html";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileId + "\"");
            response.setContentType("text/html");
            final List<ISTStockIssueItem> istStockIssueItems = (List<ISTStockIssueItem>)this.stockIssueItemRepository.findByIssue(issueId);
            final String htmlResponse = this.getIssueChallanExportHtml(istStockIssueItems, isStockIssue, request, response);
            response.setContentLength(htmlResponse.getBytes().length);
            is = new ByteArrayInputStream(htmlResponse.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        fileId = fileId.replace("/", ".");
        TopStoreService.fileMap.put(fileId, is);
        return fileId;
    }

    public String printPurchaseChallan(final Integer purchaseOrderId, final HttpServletRequest request, final HttpServletResponse response) {
        InputStream is = null;
        String fileId = null;
        final CustomPurchaseOrder customPurchaseOrder = (CustomPurchaseOrder)this.customPurchaseOrderRepository.findOne(purchaseOrderId);
        fileId = customPurchaseOrder.getPoNumber() + ".html";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileId + "\"");
            response.setContentType("text/html");
            final List<CustomPurchaseOrderItem> customPurchaseOrderItems = (List<CustomPurchaseOrderItem>)this.customPurchaseOrderItemRepository.findByCustomPurchaseOrderId(purchaseOrderId);
            final String htmlResponse = this.getPurchaseChallanExportHtml(customPurchaseOrderItems, customPurchaseOrder, request, response);
            response.setContentLength(htmlResponse.getBytes().length);
            is = new ByteArrayInputStream(htmlResponse.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        fileId = fileId.replace("/", ".");
        TopStoreService.fileMap.put(fileId, is);
        return fileId;
    }

    @Transactional(readOnly = true)
    public String getIssueChallanExportHtml(final List<ISTStockIssueItem> istStockIssueItems, final ISStockIssue isStockIssue, final HttpServletRequest request, final HttpServletResponse response) {
        final ISProject project = (ISProject)this.projectRepository.findOne(isStockIssue.getProject());
        final ISTopStore topStore = (ISTopStore)this.topStoreRepository.findOne(isStockIssue.getStore());
        final List<ObjectAtrributeDTO> objectAtrributeDTOList1 = new ArrayList<ObjectAtrributeDTO>();
        final List<ObjectAtrributeDTO> objectAtrributeDTOList2 = new ArrayList<ObjectAtrributeDTO>();
        final List<ObjectAtrributeDTO> objectAtrributeDTOList3 = new ArrayList<ObjectAtrributeDTO>();
        int i = 0;
        final List<ObjectTypeAttribute> objectTypeAttributes = (List<ObjectTypeAttribute>)this.objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.ISSUE);
        for (final ObjectTypeAttribute objectTypeAttribute : objectTypeAttributes) {
            final ObjectAtrributeDTO objectAtrributeDTO = new ObjectAtrributeDTO();
            objectAtrributeDTO.setObjectAttribute(this.objectAttributeRepository.findByObjectIdAndAttributeDefId(isStockIssue.getId(), objectTypeAttribute.getId()));
            objectAtrributeDTO.setObjectTypeAttribute(objectTypeAttribute);
            if (++i % 2 == 0) {
                objectAtrributeDTOList1.add(objectAtrributeDTO);
            }
            else {
                objectAtrributeDTOList2.add(objectAtrributeDTO);
            }
        }
        String caNo = "";
        String releasedOrderNo = "";
        final List<ObjectTypeAttribute> issueAttributes = (List<ObjectTypeAttribute>)this.objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.valueOf("MATERIALISSUETYPE"));
        for (final ObjectTypeAttribute objectTypeAttribute2 : issueAttributes) {
            if (objectTypeAttribute2.getName().equals("CA No")) {
                final ObjectAttribute ObjectAttribute = this.objectAttributeRepository.findByObjectIdAndAttributeDefId(isStockIssue.getId(), objectTypeAttribute2.getId());
                if (ObjectAttribute != null) {
                    if (ObjectAttribute.getStringValue() != null) {
                        caNo = ObjectAttribute.getStringValue();
                    }
                    if (ObjectAttribute.getIntegerValue() != null) {
                        caNo = ObjectAttribute.getIntegerValue().toString();
                    }
                    if (ObjectAttribute.getDoubleValue() != null) {
                        caNo = ObjectAttribute.getDoubleValue().toString();
                    }
                }
            }
            if (objectTypeAttribute2.getName().equals("Release Order No")) {
                final ObjectAttribute ObjectAttribute = this.objectAttributeRepository.findByObjectIdAndAttributeDefId(isStockIssue.getId(), objectTypeAttribute2.getId());
                if (ObjectAttribute == null) {
                    continue;
                }
                if (ObjectAttribute.getStringValue() != null) {
                    releasedOrderNo = ObjectAttribute.getStringValue();
                }
                if (ObjectAttribute.getIntegerValue() != null) {
                    releasedOrderNo = ObjectAttribute.getIntegerValue().toString();
                }
                if (ObjectAttribute.getDoubleValue() == null) {
                    continue;
                }
                caNo = ObjectAttribute.getDoubleValue().toString();
            }
        }
        for (final ISTStockIssueItem istStockIssueItem : istStockIssueItems) {
            istStockIssueItem.setItemAttributesList((List)new ArrayList());
            final ISMaterialItem materialItem = (ISMaterialItem)this.materialItemRepository.findOne(istStockIssueItem.getItem());
            final List<ObjectTypeAttribute> itemAttributesList = (List<ObjectTypeAttribute>)this.objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.ISSUEITEM);
            for (final ObjectTypeAttribute objectTypeAttribute3 : itemAttributesList) {
                if (!objectTypeAttribute3.getDataType().equals( DataType.ATTACHMENT) && !objectTypeAttribute3.getDataType().equals( DataType.IMAGE)) {
                    final ObjectAtrributeDTO objectAtrributeDTO2 = new ObjectAtrributeDTO();
                    objectAtrributeDTO2.setObjectAttribute(this.objectAttributeRepository.findByObjectIdAndAttributeDefId(istStockIssueItem.getId(), objectTypeAttribute3.getId()));
                    objectAtrributeDTO2.setObjectTypeAttribute(objectTypeAttribute3);
                    istStockIssueItem.getItemAttributesList().add(objectAtrributeDTO2);
                }
            }
            if (materialItem != null) {
                istStockIssueItem.getItemDTO().setItemNumber(materialItem.getItemNumber());
                istStockIssueItem.getItemDTO().setDescription(materialItem.getDescription());
                istStockIssueItem.getItemDTO().setItemName(materialItem.getItemName());
                istStockIssueItem.getItemDTO().setItemType(materialItem.getItemType().getName());
                istStockIssueItem.getItemDTO().setUnits(materialItem.getUnits());
            }
            final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            final String createdDate = df.format(istStockIssueItem.getTimeStamp());
            istStockIssueItem.setReceiveDate(createdDate);
        }
        String imgUrl = null;
        String title = null;
        final Person issuedTo = (Person)this.personRepository.findOne(isStockIssue.getIssuedTo());
        String issuedToPerson = "";
        if (issuedTo.getLastName() != null) {
            issuedToPerson = issuedTo.getFirstName() + " " + issuedTo.getLastName();
        }
        else {
            issuedToPerson = issuedTo.getFirstName();
        }
        final ServletContext context = request.getServletContext();
        final String path = context.getRealPath("/") + "application.json";
        final JSONParser parser = new JSONParser();
        try {
            final Object obj = parser.parse((Reader)new FileReader(path));
            final JSONObject jsonObject = (JSONObject)obj;
            title = (String)jsonObject.get( "printTemplateTitle");
            final String localImg = (String)jsonObject.get( "templateLogo");
            imgUrl = this.getLocalHostUrl() + "/" + localImg;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        catch (ParseException e3) {
            e3.printStackTrace();
        }
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectAtrributes1", objectAtrributeDTOList1);
        model.put("objectAtrributes2", objectAtrributeDTOList2);
        model.put("objectAtrributes3", objectAtrributeDTOList3);
        model.put("itemAttributesList", istStockIssueItems.get(0).getItemAttributesList());
        model.put("issueItems", istStockIssueItems);
        model.put("caNo", caNo);
        model.put("releasedOrderNo", releasedOrderNo);
        model.put("issue", isStockIssue);
        model.put("issuedTo", issuedToPerson);
        model.put("projectName", project.getName());
        model.put("storeName", topStore.getStoreName());
        model.put("logo", imgUrl);
        model.put("title", title);
        final String exportHtmlData = this.getIssueChallanFromTemplate(model);
        return exportHtmlData;
    }

    @Transactional(readOnly = true)
    public String getPurchaseChallanExportHtml(final List<CustomPurchaseOrderItem> customPurchaseOrderItems, final CustomPurchaseOrder customPurchaseOrder, final HttpServletRequest request, final HttpServletResponse response) {
        final ISTopStore topStore = (ISTopStore)this.topStoreRepository.findOne(customPurchaseOrder.getStore());
        final List<ObjectAtrributeDTO> objectAtrributeDTOList1 = new ArrayList<ObjectAtrributeDTO>();
        final List<ObjectAtrributeDTO> objectAtrributeDTOList2 = new ArrayList<ObjectAtrributeDTO>();
        int i = 0;
        final List<ObjectTypeAttribute> objectTypeAttributes = (List<ObjectTypeAttribute>)this.objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.valueOf("CUSTOM_PURCHASEORDER"));
        for (final ObjectTypeAttribute objectTypeAttribute : objectTypeAttributes) {
            final ObjectAtrributeDTO objectAtrributeDTO = new ObjectAtrributeDTO();
            objectAtrributeDTO.setObjectAttribute(this.objectAttributeRepository.findByObjectIdAndAttributeDefId(customPurchaseOrder.getId(), objectTypeAttribute.getId()));
            objectAtrributeDTO.setObjectTypeAttribute(objectTypeAttribute);
            if (++i % 2 == 0) {
                objectAtrributeDTOList1.add(objectAtrributeDTO);
            }
            else {
                objectAtrributeDTOList2.add(objectAtrributeDTO);
            }
        }
        final Map<String, Integer> map = new HashMap<String, Integer>();
        for (final CustomPurchaseOrderItem customPurchaseOrderItem : customPurchaseOrderItems) {
            final Integer integer = map.get(customPurchaseOrderItem.getRequisition().getRequisitionNumber());
            if (integer == null) {
                map.put(customPurchaseOrderItem.getRequisition().getRequisitionNumber(), customPurchaseOrderItem.getId());
            }
        }
        final Set<String> requisitionNumbers = map.keySet();
        String imgUrl = null;
        String title = null;
        final ServletContext context = request.getServletContext();
        final String path = context.getRealPath("/") + "application.json";
        final JSONParser parser = new JSONParser();
        try {
            final Object obj = parser.parse((Reader)new FileReader(path));
            final JSONObject jsonObject = (JSONObject)obj;
            title = (String)jsonObject.get( "printTemplateTitle");
            final String localImg = (String)jsonObject.get( "templateLogo");
            imgUrl = this.getLocalHostUrl() + "/" + localImg;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        catch (ParseException e3) {
            e3.printStackTrace();
        }
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectAtrributes1", objectAtrributeDTOList1);
        model.put("objectAtrributes2", objectAtrributeDTOList2);
        model.put("reqNumbers", requisitionNumbers);
        model.put("poItems", customPurchaseOrderItems);
        model.put("purchaseOrder", customPurchaseOrder);
        model.put("storeName", topStore.getStoreName());
        model.put("logo", imgUrl);
        model.put("title", title);
        final String exportHtmlData = this.getPurchaseChallanFromTemplate(model);
        return exportHtmlData;
    }

    @Transactional(readOnly = true)
    public String getPurchaseChallanFromTemplate(final Map<String, Object> model) {
        final StringBuffer content = new StringBuffer();
        try {
            this.fmConfiguration.setClassForTemplateLoading((Class)this.getClass(), "/nfr/");
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(this.fmConfiguration.getTemplate("purchaseOrderChallan.html"),  model));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Transactional(readOnly = true)
    public String getIssueChallanFromTemplate(final Map<String, Object> model) {
        final StringBuffer content = new StringBuffer();
        try {
            this.fmConfiguration.setClassForTemplateLoading((Class)this.getClass(), "/nfr/");
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(this.fmConfiguration.getTemplate("issueChallan.html"),  model));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Transactional(readOnly = true)
    public String getIndentChallanFromTemplate(final Map<String, Object> model) {
        final StringBuffer content = new StringBuffer();
        try {
            this.fmConfiguration.setClassForTemplateLoading((Class)this.getClass(), "/nfr/");
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(this.fmConfiguration.getTemplate("indentChallan.html"),  model));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Transactional(readOnly = true)
    public String getLocalHostUrl() {
        final HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        final String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return url;
    }

    @Transactional(readOnly = false)
    public ISTopStore importStoreItems(final Integer storeId, final MultipartHttpServletRequest request) throws Exception {
        final ISTopStore topStore = (ISTopStore)this.topStoreRepository.findOne(storeId);
        final DataFormatter df = new DataFormatter();
        for (final MultipartFile file1 : request.getFileMap().values()) {
            final File file2 = ImportConverter.trimAndConvertMultipartFileToFile(file1);
            if ((file2 == null || !file2.getName().trim().endsWith(".xls")) && !file2.getName().trim().endsWith(".xlsx")) {
                throw new CassiniException("Please upload excel sheet with proper Name & Code data");
            }
            final List<ISTopStockMovement> stockMovementList = new ArrayList<ISTopStockMovement>();
            final List<ISTopInventory> topInventoryList = new ArrayList<ISTopInventory>();
            final Workbook workbook = WorkbookFactory.create(file2);
            for (int totalSheets = workbook.getNumberOfSheets(), j = 0; j < totalSheets; ++j) {
                int i = 0;
                final Map<String, ObjectTypeAttribute> attributesMap = new HashMap<String, ObjectTypeAttribute>();
                final Sheet worksheet = workbook.getSheetAt(j);
                if (!worksheet.getSheetName().equalsIgnoreCase("Import Settings")) {
                    final int lastRow = worksheet.getLastRowNum();
                    while (i <= lastRow) {
                        final ISTopStockMovement newStockMovement = new ISTopStockMovement();
                        ISTopStockMovement stockMovement = new ISTopStockMovement();
                        final ISTopInventory topInventory = new ISTopInventory();
                        Integer itemId = 0;
                        if (stockMovementList.size() > 0) {
                            stockMovementList.sort((s1, s2) -> s2.getTimeStamp().compareTo(s1.getTimeStamp()));
                        }
                        final Row row = worksheet.getRow(i++);
                        if (row.getRowNum() == 0) {
                            for (int columnCount = row.getLastCellNum(), column = 7; column < columnCount; ++column) {
                                if (row.getCell(column).getStringCellValue() != null && row.getCell(column).getStringCellValue() != "" && row.getCell(column).getStringCellValue() != " ") {
                                    final ObjectTypeAttribute materialAttribute = this.objectTypeAttributeRepository.findByNameAndObjectType(row.getCell(column).getStringCellValue(), ObjectType.MATERIAL);
                                    if (materialAttribute == null) {
                                        final ObjectTypeAttribute machineAttribute = this.objectTypeAttributeRepository.findByNameAndObjectType(row.getCell(column).getStringCellValue(), ObjectType.MACHINE);
                                        if (machineAttribute == null) {
                                            throw new RuntimeException("Please save " + row.getCell(column).getStringCellValue() + " in custom properties");
                                        }
                                        attributesMap.put(row.getCell(column).getStringCellValue(), machineAttribute);
                                    }
                                    else {
                                        attributesMap.put(row.getCell(column).getStringCellValue(), materialAttribute);
                                    }
                                }
                            }
                        }
                        else {
                            System.out.println(row.getRowNum());
                            final Cell cell1 = row.getCell(0);
                            final Cell cell2 = row.getCell(1);
                            final Cell cell3 = row.getCell(2);
                            final Cell cell4 = row.getCell(3);
                            final Cell cell5 = row.getCell(4);
                            final Cell cell6 = row.getCell(5);
                            final Cell cell7 = row.getCell(6);
                            if (df.formatCellValue(cell1) == null || cell1.getStringCellValue().equals("")) {
                                newStockMovement.setProject((Integer)null);
                            }
                            else {
                                final ISProject project = this.projectRepository.findByName(cell1.getStringCellValue());
                                if (project == null) {
                                    throw new CassiniException(cell1.toString().trim() + " does not exist");
                                }
                                newStockMovement.setProject(project.getId());
                            }
                            if (cell3.getStringCellValue() == null || cell3.getStringCellValue() == "") {
                                throw new CassiniException("Item Name cannot be empty");
                            }
                            if (cell2.getStringCellValue() == null || cell2.getStringCellValue() == "") {
                                if (cell3.getStringCellValue() != null || cell3.getStringCellValue() != "") {
                                    if (cell4.getStringCellValue() != null && cell4.getStringCellValue() != "") {
                                        itemId = this.findItemsByType(cell2.getStringCellValue(), cell3.getStringCellValue(), cell4.getStringCellValue(), cell5.getStringCellValue(), cell6.getStringCellValue());
                                    }
                                }
                                else {
                                    if (cell4.getStringCellValue() != null && cell4.getStringCellValue() != "") {
                                        throw new RuntimeException("Item Number, Item Name and Item Description cannot be empty");
                                    }
                                    throw new RuntimeException("Item Name cannot be empty");
                                }
                            }
                            else {
                                if ((cell3.getStringCellValue() != null || cell3.getStringCellValue() != "") && cell4.getStringCellValue() != null && cell4.getStringCellValue() != "") {
                                    final ISMaterialType materialType = this.materialTypeRepository.findByName(cell4.getStringCellValue());
                                    if (materialType != null) {
                                        final ISMaterialItem materialItem = this.materialItemRepository.findByItemNameAndItemType(cell3.getStringCellValue(), materialType);
                                        if (materialItem != null && (!materialItem.getItemNumber().equalsIgnoreCase(cell2.getStringCellValue()) || !materialItem.getItemName().equals(cell3.getStringCellValue()))) {
                                            throw new RuntimeException("row : " + (row.getRowNum() + 1) + "  Different Item Number already exists with same Item Name and Type");
                                        }
                                    }
                                    else {
                                        final ISMachineType machineType = this.machineTypeRepository.findByName(cell4.getStringCellValue());
                                        if (machineType != null) {
                                            final ISMachineItem machineItem = this.machineItemRepository.findByItemNameAndItemType(cell3.getStringCellValue(), machineType);
                                            if (machineItem != null && (!machineItem.getItemNumber().equalsIgnoreCase(cell2.getStringCellValue()) || !machineItem.getUnits().equals(cell6.getStringCellValue()))) {
                                                throw new RuntimeException("row : " + (row.getRowNum() + 1) + "  Different Item Number already exists with same Item Name and Type");
                                            }
                                        }
                                    }
                                }
                                final ISMachineItem machineItem2 = this.machineItemRepository.findByItemNumber(cell2.getStringCellValue());
                                final ISMaterialItem materialItem = this.materialItemRepository.findByItemNumber(cell2.getStringCellValue());
                                if (machineItem2 != null) {
                                    itemId = machineItem2.getId();
                                }
                                else if (materialItem != null) {
                                    itemId = materialItem.getId();
                                }
                                else if (cell4.getStringCellValue() == null || cell4.getStringCellValue().equals("")) {
                                    ISMaterialType isMaterialType = this.materialTypeRepository.findByName("Imported Parts");
                                    if (isMaterialType == null) {
                                        isMaterialType = new ISMaterialType();
                                        final AutoNumber autoNumber = this.autoNumberRepository.findByName("Default Material Item Number Source");
                                        isMaterialType.setName("Imported Parts");
                                        isMaterialType.setMaterialNumberSource(autoNumber);
                                        isMaterialType = (ISMaterialType)this.materialTypeRepository.save( isMaterialType);
                                    }
                                    itemId = this.findItemsByType(cell2.getStringCellValue(), cell3.getStringCellValue(), isMaterialType.getName(), cell5.getStringCellValue(), cell6.getStringCellValue());
                                }
                                else {
                                    itemId = this.findItemsByType(cell2.getStringCellValue(), cell3.getStringCellValue(), cell4.getStringCellValue(), cell5.getStringCellValue(), cell6.getStringCellValue());
                                }
                            }
                            int cellNum = 7;
                            for (int attr = 0; attr < attributesMap.size(); ++attr) {
                                final String cellValue = worksheet.getRow(0).getCell(cellNum).getStringCellValue();
                                final ObjectTypeAttribute objectTypeAttribute = attributesMap.get(worksheet.getRow(0).getCell(cellNum).getStringCellValue());
                                if (objectTypeAttribute.isRequired() && (row.getCell(cellNum).getStringCellValue() == null || row.getCell(cellNum).getStringCellValue().equals(" "))) {
                                    throw new RuntimeException(cellValue + " at row : " + row.getRowNum() + 1 + " cannot be empty");
                                }
                                if (row.getCell(cellNum) != null) {
                                    final ObjectAttribute objectAttribute = this.objectAttributeRepository.findByObjectIdAndAttributeDefId(itemId, objectTypeAttribute.getId());
                                    if (objectAttribute == null) {
                                        final ObjectAttribute objectAttribute2 = new ObjectAttribute();
                                        objectAttribute2.setObjectTypeAttribute(objectTypeAttribute);
                                        final ObjectAttributeId attributeId = new ObjectAttributeId();
                                        attributeId.setObjectId(itemId);
                                        attributeId.setAttributeDef(objectTypeAttribute.getId());
                                        objectAttribute2.setId(attributeId);
                                        if (objectTypeAttribute.getDataType().equals( DataType.TEXT)) {
                                            objectAttribute2.setStringValue(row.getCell(cellNum).getStringCellValue());
                                            this.objectAttributeRepository.save( objectAttribute2);
                                        }
                                        else if (objectTypeAttribute.getDataType().equals( DataType.INTEGER)) {
                                            objectAttribute2.setIntegerValue(Integer.valueOf(Integer.parseInt(row.getCell(cellNum).getStringCellValue())));
                                            this.objectAttributeRepository.save( objectAttribute2);
                                        }
                                        else if (objectTypeAttribute.getDataType().equals( DataType.DOUBLE)) {
                                            objectAttribute2.setDoubleValue(Double.valueOf(Double.parseDouble(row.getCell(cellNum).getStringCellValue())));
                                            this.objectAttributeRepository.save( objectAttribute2);
                                        }
                                    }
                                    ++cellNum;
                                }
                            }
                            final Integer id = itemId;
                            stockMovement = stockMovementList.stream().filter(x -> x.getItem() == id).findFirst().orElse(null);
                            if (stockMovement != null && stockMovement.getItem() != null && (df.formatCellValue(cell7) != null || df.formatCellValue(cell7) != "")) {
                                stockMovement.setClosingBalance(Double.valueOf(Double.parseDouble(cell7.getStringCellValue().trim()) + stockMovement.getClosingBalance()));
                            }
                            if ((stockMovement == null || stockMovement.getItem() == null) && (df.formatCellValue(cell7) == null || df.formatCellValue(cell7) == "")) {
                                newStockMovement.setQuantity(Double.valueOf(0.0));
                                newStockMovement.setOpeningBalance(Double.valueOf(0.0));
                                newStockMovement.setClosingBalance(Double.valueOf(0.0));
                            }
                            else if (stockMovement == null || stockMovement.getItem() == null) {
                                newStockMovement.setOpeningBalance(Double.valueOf(0.0));
                                newStockMovement.setQuantity(Double.valueOf(Double.parseDouble(cell7.getStringCellValue().trim())));
                                newStockMovement.setClosingBalance(Double.valueOf(Double.parseDouble(cell7.getStringCellValue().trim())));
                            }
                            if (stockMovement != null) {
                                continue;
                            }
                            newStockMovement.setStore(topStore);
                            newStockMovement.setMovementType(MovementType.OPENINGBALANCE);
                            topInventory.setStore(topStore);
                            newStockMovement.setItem(itemId);
                            newStockMovement.setTimeStamp(new Date());
                            newStockMovement.setRecordedBy(topStore.getCreatedBy());
                            topInventory.setProject(newStockMovement.getProject());
                            topInventory.setStoreOnHand(newStockMovement.getQuantity());
                            topInventory.setItem(newStockMovement.getItem());
                            topInventoryList.add(topInventory);
                            stockMovementList.add(newStockMovement);
                        }
                    }
                }
            }
            workbook.close();
            this.topStockMovementRepository.save((Iterable)stockMovementList);
            this.topInventoryRepository.save((Iterable)topInventoryList);
        }
        return topStore;
    }

    @Transactional(readOnly = false)
    public Integer findItemsByType(final String itemNumber, final String itemName, final String itemType, final String description, final String units) {
        Integer itemId = 0;
        final ISMaterialType materialType = this.materialTypeRepository.findByName(itemType);
        final ISMachineType machineType = this.machineTypeRepository.findByName(itemType);
        if (materialType == null && machineType == null) {
            ISMaterialType isMaterialType = new ISMaterialType();
            isMaterialType.setName(itemType);
            final AutoNumber autoNumber = this.autoNumberRepository.findByName("Default Material Item Number Source");
            isMaterialType.setMaterialNumberSource(autoNumber);
            isMaterialType = (ISMaterialType)this.materialTypeRepository.save( isMaterialType);
            ISMaterialItem materialItem1 = new ISMaterialItem();
            materialItem1.setItemType(isMaterialType);
            if (units != null) {
                materialItem1.setUnits(units);
            }
            else {
                materialItem1.setUnits("");
            }
            if (description == null) {
                materialItem1.setDescription("Store Imported Part");
            }
            else {
                materialItem1.setDescription(description);
            }
            materialItem1.setActive(false);
            if (itemName != null) {
                materialItem1.setItemName(itemName);
            }
            else {
                materialItem1.setItemName("");
            }
            materialItem1.setUnitCost(0.0);
            materialItem1.setUnitPrice(0.0);
            materialItem1.setItemType(isMaterialType);
            if (itemNumber != null && itemNumber != "") {
                materialItem1.setItemNumber(itemNumber);
            }
            else {
                final String number = this.getNextMaterialNumber(itemName, isMaterialType);
                if (number != null) {
                    materialItem1.setItemNumber(number);
                    itemId = materialItem1.getId();
                }
                else {
                    itemId = this.materialItemRepository.findByItemNameAndItemType(itemName, isMaterialType).getId();
                }
            }
            materialItem1.setUnitCost(0.0);
            materialItem1.setUnitPrice(0.0);
            materialItem1.setItemType(isMaterialType);
            materialItem1 = (ISMaterialItem)this.materialItemRepository.save( materialItem1);
            itemId = materialItem1.getId();
        }
        else if (materialType != null) {
            ISMaterialItem materialItem2 = new ISMaterialItem();
            materialItem2.setItemType(materialType);
            final ISMaterialItem materialItem3 = this.materialItemRepository.findByItemNameAndItemType(itemName, materialType);
            if (materialItem3 != null) {
                materialItem2 = materialItem3;
                itemId = materialItem3.getId();
            }
            else if (itemNumber != null && itemNumber != "") {
                materialItem2.setItemNumber(itemNumber);
                itemId = materialItem2.getId();
            }
            else {
                final String number2 = this.getNextMaterialNumber(itemName, materialType);
                if (number2 != null) {
                    materialItem2.setItemNumber(number2);
                    itemId = materialItem2.getId();
                }
                else {
                    materialItem2 = this.materialItemRepository.findByItemNameAndItemType(itemName, materialType);
                }
            }
            if (units != null) {
                materialItem2.setUnits(units);
            }
            else {
                materialItem2.setUnits("");
            }
            if (description == null) {
                materialItem2.setDescription("Store Imported Part");
            }
            else {
                materialItem2.setDescription(description);
            }
            materialItem2.setActive(false);
            if (itemName != null) {
                materialItem2.setItemName(itemName);
            }
            else {
                materialItem2.setItemName("");
            }
            materialItem2.setUnitCost(0.0);
            materialItem2.setUnitPrice(0.0);
            materialItem2.setItemType(materialType);
            materialItem2 = (ISMaterialItem)this.materialItemRepository.save( materialItem2);
            itemId = materialItem2.getId();
        }
        else if (machineType != null) {
            ISMachineItem machineItem1 = new ISMachineItem();
            machineItem1.setItemType(machineType);
            final ISMachineItem machineItem2 = this.machineItemRepository.findByItemNameAndItemType(itemName, machineType);
            if (machineItem2 != null) {
                machineItem1 = machineItem2;
                itemId = machineItem2.getId();
            }
            else if (itemNumber != null && itemNumber != "") {
                machineItem1.setItemNumber(itemNumber);
                this.machineItemRepository.save( machineItem1);
                itemId = machineItem1.getId();
            }
            else {
                final String number2 = this.getNextMachineNumber(itemName, machineType);
                if (number2 != null) {
                    machineItem1.setItemNumber(number2);
                    this.machineItemRepository.save( machineItem1);
                    itemId = machineItem1.getId();
                }
                else {
                    itemId = this.machineItemRepository.findByItemNameAndItemType(itemName, machineType).getId();
                }
            }
            if (units != null) {
                machineItem1.setUnits(units);
            }
            else {
                machineItem1.setUnits("");
            }
            if (description == null) {
                machineItem1.setDescription("Store Imported Part");
            }
            else {
                machineItem1.setDescription(description);
            }
            if (itemName != null) {
                machineItem1.setItemName(itemName);
            }
            else {
                machineItem1.setItemName("");
            }
            machineItem1.setUnitCost(0.0);
            machineItem1.setUnitPrice(0.0);
            machineItem1.setItemType(machineType);
            this.machineItemRepository.save( machineItem1);
        }
        return itemId;
    }

    @Transactional(readOnly = false)
    public String getNextMaterialNumber(final String itemName, final ISMaterialType materialType) {
        String next = null;
        ISMaterialItem materialItem = this.materialItemRepository.findByItemNameAndItemType(itemName, materialType);
        if (materialItem == null) {
            final AutoNumber auto = (AutoNumber)this.autoNumberRepository.findOne(materialType.getMaterialNumberSource().getId());
            next = auto.next();
            materialItem = this.materialItemRepository.findByItemNumber(next);
            if (materialItem != null && materialItem.getItemNumber() != null) {
                if (materialItem.getItemName() != itemName) {
                    auto.setNextNumber(auto.getNextNumber());
                    return this.getNextMaterialNumber(itemName, materialType);
                }
                return null;
            }
        }
        return next;
    }

    @Transactional(readOnly = false)
    public String getNextMachineNumber(final String itemName, final ISMachineType machineType) {
        String next = null;
        ISMachineItem machineItem = this.machineItemRepository.findByItemNameAndItemType(itemName, machineType);
        if (machineItem == null) {
            final AutoNumber auto = (AutoNumber)this.autoNumberRepository.findOne(machineType.getMachineNumberSource().getId());
            next = auto.next();
            machineItem = this.machineItemRepository.findByItemNumber(next);
            if (machineItem != null && machineItem.getItemNumber() != null) {
                if (machineItem.getItemName() != itemName) {
                    auto.setNextNumber(auto.getNextNumber());
                    return this.getNextMachineNumber(itemName, machineType);
                }
                return null;
            }
        }
        return next;
    }

    @Transactional(readOnly = false)
    public String storeImportFileFormat(final String fileType, final Export export, final HttpServletResponse response) {
        ByteArrayInputStream is = null;
        String fileId = null;
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date dt = new Date();
        final String fName = export.getFileName() + "_" + dateFormat.format(dt);
        if (fileType != null && fileType.equalsIgnoreCase("excel")) {
            fileId = export.getFileName() + ".xls";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + export.getFileName() + "\"");
            response.setContentType("application/vnd.ms-excel");
            final HSSFWorkbook e3 = new HSSFWorkbook();
            try {
                this.buildExcelDocument(export, (Workbook)e3, response);
                final ByteArrayOutputStream e4 = new ByteArrayOutputStream();
                e3.write((OutputStream)e4);
                is = new ByteArrayInputStream(e4.toByteArray());
            }
            catch (Exception var15) {
                var15.printStackTrace();
            }
        }
        else if (fileType != null && fileType.equalsIgnoreCase("pdf")) {
            fileId = export.getFileName() + ".pdf";
            final ByteArrayOutputStream e5 = new ByteArrayOutputStream();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + export.getFileName() + "\"");
            response.setContentType("application/pdf");
            try {
                final Document e6 = new Document(PageSize.A4.rotate(), 36.0f, 36.0f, 54.0f, 36.0f);
                final PdfWriter writer = PdfWriter.getInstance(e6, (OutputStream)e5);
                writer.setViewerPreferences(this.getViewerPreferences());
                e6.open();
                e6.add((Element)new Paragraph(export.getFileName() + " :" + LocalDate.now()));
                this.buildPdfDocument(export, e6, response);
                e6.close();
                response.setContentLength(e5.toByteArray().length);
                is = new ByteArrayInputStream(e5.toByteArray());
            }
            catch (Exception var16) {
                var16.printStackTrace();
            }
        }
        else if (fileType != null && fileType.equalsIgnoreCase("csv")) {
            fileId = export.getFileName() + ".csv";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + export.getFileName() + "\"");
            response.setContentType("text/csv");
            ByteArrayOutputStream e5 = null;
            try {
                e5 = this.writeCsvStream(export);
                response.setContentLength(e5.toByteArray().length);
            }
            catch (IOException var17) {
                var17.printStackTrace();
            }
            is = new ByteArrayInputStream(e5.toByteArray());
        }
        else if (fileType != null && fileType.equalsIgnoreCase("html")) {
            fileId = export.getFileName() + ".html";
            try {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + export.getFileName() + "\"");
                response.setContentType("text/html");
                final String e7 = this.getExportHtml(export);
                response.setContentLength(e7.getBytes().length);
                is = new ByteArrayInputStream(e7.getBytes());
            }
            catch (Exception var18) {
                var18.printStackTrace();
            }
        }
        TopStoreService.fileMap.put(fileId, is);
        return fileId;
    }

    @Transactional(readOnly = false)
    public void buildExcelDocument(final Export export, final Workbook workbook, final HttpServletResponse response) throws Exception {
        final Sheet sheet = workbook.createSheet("Store Import file");
        final Sheet sheet2 = workbook.createSheet("Import Settings");
        sheet.setDefaultColumnWidth(30);
        final CellStyle style = workbook.createCellStyle();
        final Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor((short)12);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor((short)9);
        style.setFont(font);
        int i = 0;
        final Row header = sheet.createRow(0);
        for (final String headerTxt : export.getHeaders()) {
            header.createCell(i).setCellValue(headerTxt);
            header.getCell(i).setCellStyle(style);
            ++i;
        }
        final Row info1 = sheet2.createRow(0);
        info1.createCell(0).setCellValue("Item name and Item type are mandatory fields");
        final Row info2 = sheet2.createRow(1);
        info2.createCell(0).setCellValue("If same item is found more than once, quantity will be updated");
        final Row info3 = sheet2.createRow(2);
        info3.createCell(0).setCellValue("Check white spaces before the values i.e ( 'MAT-0001' is different from '    MAT-0001' )");
        int var16 = 1;
        for (final ExportRow exportRow1 : export.getExportRows()) {
            final Row userRow = sheet.createRow(var16++);
            int j = 0;
            for (final ExportRowDetail exportRowDetail : exportRow1.getExportRowDetails()) {
                userRow.createCell(j).setCellValue(exportRowDetail.getColumnValue());
                ++j;
            }
        }
    }

    @Transactional(readOnly = false)
    protected int getViewerPreferences() {
        return 2053;
    }

    @Transactional(readOnly = false)
    public String getExportHtml(final Export export) {
        final HashMap model = new HashMap();
        final List<ExportRow> exportRows = export.getExportRows();
        for (final ExportRow exportHtmlData : exportRows) {
            final List<ExportRowDetail> exportRowDetails = exportHtmlData.getExportRowDetails();
            for (final ExportRowDetail exportRowDetail : exportRowDetails) {
                final String value = exportRowDetail.getColumnValue();
                if (value == null) {
                    exportRowDetail.setColumnValue(" ");
                }
            }
        }
        model.put("exportHeaders", export.getHeaders());
        model.put("exportData", export.getExportRows());
        final String templatePath2 = "tablesDataExport/tablesDataExport.html";
        final String exportHtmlData2 = this.getContentFromTemplate(templatePath2, model);
        return exportHtmlData2;
    }

    @Transactional(readOnly = false)
    public String getContentFromTemplate(final String templatePath, final Map<String, Object> model) {
        final StringBuffer content = new StringBuffer();
        try {
            this.fmConfiguration.setClassForTemplateLoading((Class)this.getClass(), "/templates/");
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(this.fmConfiguration.getTemplate(templatePath),  model));
        }
        catch (Exception var5) {
            var5.printStackTrace();
        }
        return content.toString();
    }

    @Transactional(readOnly = false)
    public void buildPdfDocument(final Export export, final Document document, final HttpServletResponse response) throws Exception {
        final PdfPTable table = new PdfPTable(export.getHeaders().size());
        table.setWidthPercentage(100.0f);
        table.setSpacingBefore(10.0f);
        final com.itextpdf.text.Font font = FontFactory.getFont("Times");
        font.setColor(BaseColor.WHITE);
        final PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setPadding(5.0f);
        for (final String exportRow : export.getHeaders()) {
            cell.setPhrase(new Phrase(exportRow, font));
            table.addCell(cell);
        }
        for (final ExportRow exportRow2 : export.getExportRows()) {
            for (final ExportRowDetail exportRowDetail : exportRow2.getExportRowDetails()) {
                table.addCell(exportRowDetail.getColumnValue());
            }
        }
        document.add((Element)table);
    }

    @Transactional(readOnly = false)
    public ByteArrayOutputStream writeCsvStream(final Export export) throws IOException {
        final ArrayList listList = new ArrayList();
        ArrayList lst = null;
        listList.add(export.getHeaders());
        for (final ExportRow out : export.getExportRows()) {
            lst = new ArrayList();
            for (final ExportRowDetail exportRowDetail : out.getExportRowDetails()) {
                lst.add(exportRowDetail.getColumnValue());
            }
            listList.add(lst);
        }
        final ByteArrayOutputStream riskCategoryCsvStream2 = new ByteArrayOutputStream();
        final BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(riskCategoryCsvStream2));
        final CSVPrinter csvPrinter2 = new CSVPrinter((Appendable)out2, CSVFormat.EXCEL);
        csvPrinter2.printRecords((Iterable)listList);
        csvPrinter2.flush();
        csvPrinter2.close();
        return riskCategoryCsvStream2;
    }

    static {
        TopStoreService.fileMap = new HashMap();
    }
}