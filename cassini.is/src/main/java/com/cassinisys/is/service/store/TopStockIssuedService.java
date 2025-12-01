package com.cassinisys.is.service.store;

import com.cassinisys.is.filtering.ISStockIssueCriteria;
import com.cassinisys.is.filtering.ISStockIssuePredicateBuilder;
import com.cassinisys.is.filtering.StockMovementCriteria;
import com.cassinisys.is.filtering.StockMovementPredicateBuilder;
import com.cassinisys.is.model.pm.ISProjectResource;
import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.model.store.*;
import com.cassinisys.is.repo.pm.ResourceRepository;
import com.cassinisys.is.repo.procm.*;
import com.cassinisys.is.repo.store.*;
import com.cassinisys.is.service.pm.ProjectService;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.is.service.procm.ItemService;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Export;
import com.cassinisys.platform.model.common.ExportRow;
import com.cassinisys.platform.model.common.ExportRowDetail;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Service
public class TopStockIssuedService {

    public static Map fileMap = new HashMap();

    @Autowired
    ISTopStoreRepository isTopStoreRepository;
    @Autowired
    private ISTStockIssueItemRepository topStockIssuedRepository;
    @Autowired
    private ISTopStockMovementRepository topStockMovementRepository;
    @Autowired
    private ISStockIssueRepository stockIssueRepository;
    @Autowired
    private MachineItemRepository machineItemRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private MaterialItemRepository materialItemRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ISTopInventoryRepository topInventoryRepository;
    @Autowired
    private ISTStockIssueItemRepository stockIssueItemRepository;
    @Autowired
    private BoqItemRepository boqItemRepository;
    @Autowired
    private ResourceRepository projectResourceRepository;
    @Autowired
    private IssueTypeItemAttributeRepository issueTypeItemAttributeRepository;
    @Autowired
    private MaterialIssueTypeRepository materialIssueTypeRepository;
    @Autowired
    private ISStockReturnItemRepository stockReturnItemRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private IssueTypeAttributeRepository issueTypeAttributeRepository;
    @Autowired
    private ISStockIssuePredicateBuilder stockIssuePredicateBuilder;
    @Autowired
    private StockMovementPredicateBuilder stockMovementPredicateBuilder;

    @Transactional(readOnly = false)
    public ISStockIssue create(ISStockIssue stockIssue) {
        ISTopStore topStore = isTopStoreRepository.findOne(stockIssue.getStore());
        if (stockIssue.getIssueNumberSource() == null) {
            AutoNumber autoNumber = autoNumberService.getByName("Default Stock Issue Number Source");
            String number = autoNumberService.getNextNumber(autoNumber.getId());
            stockIssue.setIssueNumberSource(number);
        }
        if (stockIssue.getIssuedToName() != null) {
            Person person = null;
            String firstName = null;
            String lastName = null;
            String[] arr = stockIssue.getIssuedToName().trim().split(" ", 2);
            if (arr.length > 1) {
                firstName = arr[0];
                lastName = arr[1];
                person = personRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(arr[0], arr[1]);
            } else {
                firstName = arr[0];
                List<Person> persons = personRepository.findByFirstNameIgnoreCaseAndLastNameIsNull(arr[0]);
                if (persons.size() > 0) {
                    person = persons.get(0);
                }
            }
            if (person != null) {
                stockIssue.setIssuedTo(person.getId());
            } else {
                person = new Person();
                person.setFirstName(firstName);
                person.setLastName(lastName);
                person.setPersonType(1);
                person = personRepository.save(person);
                stockIssue.setIssuedTo(person.getId());
            }

        }
        stockIssue = stockIssueRepository.save(stockIssue);
        if (stockIssue.getStockIssueItems().size() > 0) {
            for (ISTStockIssueItem stockIssueItem : stockIssue.getStockIssueItems()) {
                stockIssueItem.setIssue(stockIssue.getId());
                stockIssueItem.setStore(topStore);
                Double openingBal = 0.0;
                Double qty = 0.0;
                ISTopInventory projectInventory = null;
                ISTopInventory projectIsNullInventory = null;
                List<ISTopInventory> topInventories1 = topInventoryRepository.findByItemAndStore(stockIssueItem.getItem(), topStore);
                if (topInventories1.size() > 0) {
                    for (ISTopInventory topInventory : topInventories1) {
                        openingBal += topInventory.getStoreOnHand();
                    }
                    stockIssueItem.setOpeningBalance(openingBal);
                    stockIssueItem.setClosingBalance(stockIssueItem.getOpeningBalance() - stockIssueItem.getQuantity());
                } else {
                    stockIssueItem.setOpeningBalance(0.0);
                    stockIssueItem.setClosingBalance(stockIssueItem.getQuantity());
                }
                //check with this store and if item has inventory, update or save
                if (stockIssueItem != null) {
                    projectInventory = topInventoryRepository.findByStoreAndItemAndProject(topStore, stockIssueItem.getItem(), stockIssue.getProject());
                    if (projectInventory == null) {
                        projectIsNullInventory = topInventoryRepository.findByStoreAndItemAndProjectIsNull(topStore, stockIssueItem.getItem());
                        if (projectIsNullInventory == null) {
                            throw new RuntimeException("This store doesn't have inventory for this item.");
                        }
                    }
                    if (projectInventory.getStoreOnHand() >= stockIssueItem.getQuantity()) {
                        qty = projectInventory.getStoreOnHand() - stockIssueItem.getQuantity();
                        projectInventory.setStoreOnHand(qty);
                    } else {
                        projectIsNullInventory = topInventoryRepository.findByStoreAndItemAndProjectIsNull(topStore, stockIssueItem.getItem());
                        if (projectIsNullInventory != null) {
                            Double storeQuantity = (stockIssueItem.getQuantity() - projectInventory.getStoreOnHand());
                            storeQuantity = projectIsNullInventory.getStoreOnHand() - storeQuantity;
                            projectIsNullInventory.setStoreOnHand(storeQuantity);
                            projectInventory.setStoreOnHand(0.0);
                            topInventoryRepository.save(projectIsNullInventory);
                        }
                    }
                    topInventoryRepository.save(projectInventory);
                    if (!(stockIssue.getTask() == null)) {
                        ISProjectResource projectResource = projectResourceRepository.findByProjectAndReferenceIdAndTaskAndResourceType(stockIssue.getProject(), stockIssueItem.getBoqReference(), stockIssue.getTask(), ResourceType.MATERIALTYPE);
                        projectResource.setIssuedQuantity(projectResource.getIssuedQuantity() + stockIssueItem.getQuantity());
                        projectResourceRepository.save(projectResource);
                    }
                    stockIssueItem.setTimeStamp(new Date());
                    topStockIssuedRepository.save(stockIssueItem);
                }
            }
        }
        return stockIssue;
    }

    @Transactional(readOnly = false)
    public ISStockIssue update(ISStockIssue stockIssue) {
        return stockIssueRepository.save(stockIssue);
    }

    @Transactional(readOnly = false)
    public void delete(Integer id) {
        topStockIssuedRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public ISStockIssue get(Integer id) {
        return stockIssueRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<ISStockIssue> getStockIssuedByStore(Integer storeId) {
        return stockIssueRepository.findByStore(storeId);
    }

    public Page<ISStockIssue> stockIssuedFreeTextSearch(Pageable pageable, ISStockIssueCriteria criteria) {
        Predicate predicate = stockIssuePredicateBuilder.build(criteria, QISStockIssue.iSStockIssue);
        return stockIssueRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ISStockIssue> getPagedStoreStockIssues(Integer storeId, Pageable pageable) {
        List<ISStockIssue> stockIssues = getStockIssuedByStore(storeId);
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > stockIssues.size() ? stockIssues.size() : (start + pageable.getPageSize());
        return new PageImpl<ISStockIssue>(stockIssues.subList(start, end), pageable, stockIssues.size());
    }

    public Page<ISStockIssue> getPagedStockIssues(Pageable pageable) {
        return stockIssueRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Double getStockIssuedQtyByItemNumber(Integer projectId, String itemNumber) {
        Double issuedQty = 0.0;
        List<ISTopStockMovement> stockMovements = new ArrayList<>();
        ISMaterialItem materialItem = materialItemRepository.findByItemNumber(itemNumber);
        if (materialItem != null) {
            stockMovements = topStockMovementRepository.findByItemAndProject(materialItem.getId(), projectId);
        }
        ISMachineItem machineItem = machineItemRepository.findByItemNumber(itemNumber);
        if (machineItem != null) {
            stockMovements = topStockMovementRepository.findByItemAndProject(machineItem.getId(), projectId);
        }
        for (ISTopStockMovement stockMovement : stockMovements) {
            if (stockMovement.getMovementType() == MovementType.ISSUED) {
                issuedQty += stockMovement.getQuantity();
            }
        }
        return issuedQty;
    }

    @Transactional(readOnly = true)
    public List<ISTopStockMovement> getStockIssueItems(Integer issueId) {
        List<ISTStockIssueItem> issueItems = topStockIssuedRepository.findByIssue(issueId);
        List<Integer> rowIds = new ArrayList<>();
        for (ISTStockIssueItem issueItem : issueItems) {
            rowIds.add(issueItem.getId());
        }
        List<ISTopStockMovement> topStockMovements1 = topStockMovementRepository.findAll(rowIds);
        List<ISTopStockMovement> topStockMovements = new ArrayList<>();
        Map<Integer, ISTopStockMovement> hashMap = new HashMap<>();
        for (ISTopStockMovement stockMovement : topStockMovements1) {
            ISTopStockMovement topStockMovement = hashMap.get(stockMovement.getItem());
            if (topStockMovement == null) {
                ItemDTO itemDTO = new ItemDTO();
                ISMachineItem machineItem = machineItemRepository.findOne(stockMovement.getItem());
                ISMaterialItem materialItem = materialItemRepository.findOne(stockMovement.getItem());
                List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectId(stockMovement.getId());
                if (objectAttributes != null) {
                    if (objectAttributes.size() > 0) {
                        itemDTO.setEditAttribute(true);
                    }
                }
                if (machineItem != null) {
                    itemDTO.setItemNumber(machineItem.getItemNumber());
                    itemDTO.setId(machineItem.getId());
                    itemDTO.setDescription(machineItem.getDescription());
                    itemDTO.setItemName(machineItem.getItemName());
                    itemDTO.setItemType(machineItem.getItemType().getName());
                    itemDTO.setUnits(machineItem.getUnits());
                    itemDTO.setTimeStamp(stockMovement.getTimeStamp());
                } else {
                    itemDTO.setItemNumber(materialItem.getItemNumber());
                    itemDTO.setId(materialItem.getId());
                    itemDTO.setDescription(materialItem.getDescription());
                    itemDTO.setItemName(materialItem.getItemName());
                    itemDTO.setItemType(materialItem.getItemType().getName());
                    itemDTO.setUnits(materialItem.getUnits());
                    itemDTO.setTimeStamp(stockMovement.getTimeStamp());
                }
                stockMovement.setItemDTO(itemDTO);
                hashMap.put(stockMovement.getItem(), stockMovement);
                topStockMovements.add(stockMovement);
            } else {
                topStockMovement.setQuantity(topStockMovement.getQuantity() + stockMovement.getQuantity());
            }
        }
        return topStockMovements;
    }

    @Transactional(readOnly = true)
    public Page<ISTopStockMovement> getPagedStockIssueItems(Integer issueId, Pageable pageable) {
        List<ISTopStockMovement> topStockMovements = getStockIssueItems(issueId);
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > topStockMovements.size() ? topStockMovements.size() : (start + pageable.getPageSize());
        return new PageImpl<ISTopStockMovement>(topStockMovements.subList(start, end), pageable, topStockMovements.size());
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> getProjectResourcesInventory(Integer storeId, Integer projectId, Integer taskId) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        List<ItemDTO> itemDTOs = new ArrayList<>();
        List<Integer> boqIds = new ArrayList<>();
        List<ISProjectResource> projectResourceList = projectService.getResources(projectId, taskId);
        if (projectResourceList == null || projectResourceList.size() == 0) {
            throw new CassiniException("No resources available for the selected Task");
        } else {
            for (ISProjectResource projectResource : projectResourceList) {
                boqIds.add(projectResource.getReferenceId());
            }
            itemDTOList = itemService.getItemsByBoqIds(taskId, boqIds);
            List<ISTopInventory> topInventoryList = topInventoryRepository.findByStoreAndProject(isTopStoreRepository.findOne(storeId), projectId);
            for (ItemDTO itemDTO : itemDTOList) {
                for (ISTopInventory inventory : topInventoryList) {
                    if (itemDTO.getId().equals(inventory.getItem())) {
                        itemDTO.setStoreInventory(inventory.getStoreOnHand());
                        if (itemDTO.getProjectResource().getQuantity() > (itemDTO.getStoreInventory() + itemDTO.getItemIssueQuantity())) {
                            itemDTO.setShortage(itemDTO.getProjectResource().getQuantity() - itemDTO.getStoreInventory() - itemDTO.getItemIssueQuantity());
                        } else {
                            itemDTO.setShortage(0.0);
                        }
                        itemDTOs.add(itemDTO);
                    }
                }
            }
        }
        return itemDTOs;
    }

    public Page<ItemDTO> getProjectItems(Integer storeId, Integer projectId, Pageable pageable) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        Long actualSize = 0l;
        Hashtable<Integer, ISTStockIssueItem> hashtable = new Hashtable<>();
        Page<ISTStockIssueItem> issueItems = topStockIssuedRepository.findByStore(isTopStoreRepository.findOne(storeId), pageable);
        for (ISTStockIssueItem issueItem : issueItems.getContent()) {
            if (hashtable.get(issueItem.getItem()) == null) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setItemIssueQuantity(topStockIssuedRepository.getTotalIssuedQuantityByProjectAndItemAndStore(projectId, issueItem.getItem(), isTopStoreRepository.findOne(storeId)));
                ISMaterialItem materialItem = materialItemRepository.findOne(issueItem.getItem());
                itemDTO = setItemDTO(materialItem, itemDTO);
                if (projectId == null) {
                    itemDTO.setItemReturnQuantity(stockReturnItemRepository.getTotalReturnQuantityByItemAndStoreAndProjectIsNull(issueItem.getItem(), isTopStoreRepository.findOne(storeId)));
                } else {
                    itemDTO.setItemReturnQuantity(stockReturnItemRepository.getTotalReturnQuantityByProjectAndItemAndStore(projectId, issueItem.getItem(), isTopStoreRepository.findOne(storeId)));
                }
                itemDTOList.add(itemDTO);
                hashtable.put(issueItem.getItem(), issueItem);

            }
        }
        actualSize = issueItems.getTotalElements();
        return new PageImpl<ItemDTO>(itemDTOList, pageable, actualSize.intValue());
    }

    public ItemDTO setItemDTO(ISMaterialItem materialItem, ItemDTO itemDTO) {
        itemDTO.setItemReturnQuantity(0.0);
        itemDTO.setItemName(materialItem.getItemName());
        itemDTO.setItemNumber(materialItem.getItemNumber());
        itemDTO.setUnits(materialItem.getUnits());
        itemDTO.setId(materialItem.getId());
        itemDTO.setDescription(materialItem.getDescription());
        itemDTO.setItemType(materialItem.getItemType().getName());
        return itemDTO;

    }

    public List<ObjectAttribute> saveIssueTypeAttributes(List<ISIssueTypeItemAttribute> attributes) {
        List<ObjectAttribute> objectAttributes = new ArrayList<>();
        for (ISIssueTypeItemAttribute attribute : attributes) {
            ISIssueTypeItemAttribute isIssueTypeItemAttribute = new ISIssueTypeItemAttribute();
            ISStockIssue at = stockIssueRepository.findOne(attribute.getId().getObjectId());
            isIssueTypeItemAttribute.setId(new ObjectAttributeId(at.getId(), attribute.getId().getAttributeDef()));
            isIssueTypeItemAttribute.setStringValue(attribute.getStringValue());
            isIssueTypeItemAttribute.setIntegerValue(attribute.getIntegerValue());
            isIssueTypeItemAttribute.setBooleanValue(attribute.getBooleanValue());
            isIssueTypeItemAttribute.setDoubleValue(attribute.getDoubleValue());
            isIssueTypeItemAttribute.setTimeValue(attribute.getTimeValue());
            isIssueTypeItemAttribute.setTimestampValue(attribute.getTimestampValue());
            isIssueTypeItemAttribute.setAttachmentValues(attribute.getAttachmentValues());
            isIssueTypeItemAttribute.setDateValue(attribute.getDateValue());
            isIssueTypeItemAttribute.setCurrencyType(attribute.getCurrencyType());
            isIssueTypeItemAttribute.setCurrencyValue(attribute.getCurrencyValue());
            isIssueTypeItemAttribute.setRefValue(attribute.getRefValue());
            isIssueTypeItemAttribute.setListValue(attribute.getListValue());
            ISIssueTypeItemAttribute itemAttribute1 = issueTypeItemAttributeRepository.save(isIssueTypeItemAttribute);
            objectAttributes.add(itemAttribute1);
        }
        return objectAttributes;
    }

    public Page<ISStockIssue> getStockIssuesByType(Integer storeId, Integer issueTypeId, Pageable pageable) {
        ISMaterialIssueType materialIssueType = materialIssueTypeRepository.findOne(issueTypeId);
        return stockIssueRepository.findByMaterialIssueTypeAndStore(materialIssueType, storeId, pageable);
    }

    private Date stringToDate(String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<StockReceiveItemsDTO> getStockIssuedItemsReport(Integer storeId, String startDate1, String endDate1) {
        Date startDate2 = null;
        if (startDate1 == null || startDate1.equals("undefined") || startDate1.equals("null") || startDate1.trim().equals("")) {
            startDate2 = stockIssueItemRepository.getMinimumDate(storeId);
            if (startDate2 == null) {
                startDate2 = new Date();
            }
        } else {
            startDate2 = stringToDate(startDate1);
        }
        Date startDate = new Date(startDate2.getTime() - (1000 * 60));
        Date endDate = new Date(stringToDate(endDate1).getTime() + (1000 * 60 * 60 * 24));
        List<StockReceiveItemsDTO> stockReceiveItemsDTOs = new ArrayList<>();
        List<ISTStockIssueItem> stockIssueItems = stockIssueItemRepository.findByStoreAndTimeStampAfterAndTimeStampBefore(isTopStoreRepository.findOne(storeId), startDate, endDate);
        for (ISTStockIssueItem stockIssueItem : stockIssueItems) {
            ISMaterialItem materialItem = materialItemRepository.findOne(stockIssueItem.getItem());
            if (materialItem != null) {
                StockReceiveItemsDTO receiveItemsDTO = new StockReceiveItemsDTO();
                receiveItemsDTO.setItemAttributesList(new ArrayList<>());
                receiveItemsDTO.setDate(stockIssueItem.getCreatedDate());
                receiveItemsDTO.setItemId(materialItem.getId());
                receiveItemsDTO.setItemName(materialItem.getItemName());
                receiveItemsDTO.setItemNumber(materialItem.getItemNumber());
                receiveItemsDTO.setUnits(materialItem.getUnits());
                receiveItemsDTO.setOpeningBalance(stockIssueItem.getOpeningBalance());
                receiveItemsDTO.setClosingBalance(stockIssueItem.getClosingBalance());
                receiveItemsDTO.setQuantity(stockIssueItem.getQuantity());
                ISStockIssue stockIssue = stockIssueRepository.findOne(stockIssueItem.getIssue());
                if (stockIssue != null && stockIssue.getIssuedTo() != null) {
                    Person person = personRepository.findOne(stockIssue.getIssuedTo());
                    if (person != null) {
                        receiveItemsDTO.setIssuedTo(person.getFullName());
                    }
                }
                stockReceiveItemsDTOs.add(receiveItemsDTO);
            }
        }
        return stockReceiveItemsDTOs;
    }

    @Transactional(
            readOnly = false
    )
    public String exportIssuedItemsReport(Integer storeId, StockMovementCriteria criteria, String fileType, HttpServletResponse response) {
        ByteArrayInputStream is = null;
        String fileId = null;
        Export export = new Export();
        export.setFileName("IssueItems-Report");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dt = new Date();
        String fName = export.getFileName() + "_" + dateFormat.format(dt);
        if (fileType != null && fileType.equalsIgnoreCase("excel")) {
            fileId = export.getFileName() + ".xls";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + export.getFileName() + "\"");
            response.setContentType("application/vnd.ms-excel");
            HSSFWorkbook e3 = new HSSFWorkbook();
            try {
                this.buildExcelDocument(storeId, criteria, export, e3, response);
                ByteArrayOutputStream e2 = new ByteArrayOutputStream();
                e3.write(e2);
                is = new ByteArrayInputStream(e2.toByteArray());
            } catch (Exception var15) {
                var15.printStackTrace();
            }
        }
        fileMap.put(fileId, is);
        return fileId;
    }

    @Transactional(
            readOnly = false
    )
    public void buildExcelDocument(Integer storeId, StockMovementCriteria criteria, Export export, Workbook workbook, HttpServletResponse response) throws Exception {
        Date startDate2 = null;
        String fromDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        if (criteria.getFromDate() == null || criteria.getFromDate().equals("undefined") || criteria.getFromDate().equals("null") || criteria.getFromDate().trim().equals("")) {
            startDate2 = topStockIssuedRepository.getMinimumDate(storeId);
            if (startDate2 == null) {
                startDate2 = new Date();
            }
        } else {
            startDate2 = stringToDate(criteria.getFromDate());
        }
        fromDate = formatter.format(startDate2);
        List<String> headers = new ArrayList<>(Arrays.asList("Date", "Item Number", "Item Name", "Units", "Issued Qty", "Issued To", "Opening Bal as on " + fromDate, "Closing Bal as on " + criteria.getToDate()));
        List<ExportRow> exportRows = new ArrayList();
        Sheet sheet = workbook.createSheet("IssueItems Report");
        sheet.setDefaultColumnWidth(30);
        List<StockReceiveItemsDTO> stockReceiveItemsDTOs = getStockIssuedItemsByAttributes(storeId, criteria);
        for (StockReceiveItemsDTO stockReceiveItemsDTO : stockReceiveItemsDTOs) {
            List<ExportRowDetail> rowDetails = new ArrayList();
            ExportRow exportRow = new ExportRow();
            ExportRowDetail rowDetail1 = new ExportRowDetail();
            rowDetail1.setColumnName("Date");
            rowDetail1.setColumnType("String");
            rowDetail1.setColumnValue(stockReceiveItemsDTO.getDate().toString());
            ExportRowDetail rowDetail2 = new ExportRowDetail();
            rowDetail2.setColumnName("Item Number");
            rowDetail2.setColumnType("String");
            rowDetail2.setColumnValue(stockReceiveItemsDTO.getItemNumber());
            ExportRowDetail rowDetail3 = new ExportRowDetail();
            rowDetail3.setColumnName("Item Name");
            rowDetail3.setColumnType("String");
            rowDetail3.setColumnValue(stockReceiveItemsDTO.getItemName());
            ExportRowDetail rowDetail4 = new ExportRowDetail();
            rowDetail4.setColumnName("Units");
            rowDetail4.setColumnType("String");
            rowDetail4.setColumnValue(stockReceiveItemsDTO.getUnits());
            ExportRowDetail rowDetail5 = new ExportRowDetail();
            rowDetail5.setColumnName("Qty");
            rowDetail5.setColumnType("Integer");
            rowDetail5.setColumnValue(stockReceiveItemsDTO.getQuantity().toString());
            ExportRowDetail rowDetail6 = new ExportRowDetail();
            rowDetail6.setColumnName("Issued To");
            rowDetail6.setColumnType("String");
            rowDetail6.setColumnValue(stockReceiveItemsDTO.getIssuedTo());
            ExportRowDetail rowDetail7 = new ExportRowDetail();
            rowDetail7.setColumnName("Opening Qty");
            rowDetail7.setColumnType("Integer");
            rowDetail7.setColumnValue(stockReceiveItemsDTO.getOpeningBalance().toString());
            ExportRowDetail rowDetail8 = new ExportRowDetail();
            rowDetail8.setColumnName("Closing Qty");
            rowDetail8.setColumnType("Integer");
            rowDetail8.setColumnValue(stockReceiveItemsDTO.getClosingBalance().toString());
            rowDetails.add(rowDetail1);
            rowDetails.add(rowDetail2);
            rowDetails.add(rowDetail3);
            rowDetails.add(rowDetail4);
            rowDetails.add(rowDetail5);
            rowDetails.add(rowDetail6);
            rowDetails.add(rowDetail7);
            rowDetails.add(rowDetail8);
            for (ObjectAtrributeDTO objectAtrributeDTO : stockReceiveItemsDTO.getItemAttributesList()) {
                ExportRowDetail rowDetail = new ExportRowDetail();
                rowDetail7.setColumnName(objectAtrributeDTO.getObjectTypeAttribute().getName());
                rowDetail7.setColumnType(objectAtrributeDTO.getObjectTypeAttribute().getDataType().toString());
                rowDetail.setColumnValue("");
                if (objectAtrributeDTO.getObjectTypeAttribute().getDataType().equals(DataType.TEXT)) {
                    if (objectAtrributeDTO.getObjectAttribute() != null && !objectAtrributeDTO.equals("null") && objectAtrributeDTO.getObjectAttribute().getStringValue() != null) {
                        rowDetail.setColumnValue(objectAtrributeDTO.getObjectAttribute().getStringValue());
                    }
                } else if (objectAtrributeDTO.getObjectTypeAttribute().getDataType().equals(DataType.DOUBLE)) {
                    if (objectAtrributeDTO.getObjectAttribute() != null && objectAtrributeDTO.getObjectAttribute().getDoubleValue() != null) {
                        rowDetail.setColumnValue(objectAtrributeDTO.getObjectAttribute().getDoubleValue().toString());
                    }
                } else if (objectAtrributeDTO.getObjectTypeAttribute().getDataType().equals(DataType.DATE)) {
                    if (objectAtrributeDTO.getObjectAttribute() != null && objectAtrributeDTO.getObjectAttribute().getDateValue() != null) {
                        rowDetail.setColumnValue(objectAtrributeDTO.getObjectAttribute().getDateValue().toString());
                    }
                } else if (objectAtrributeDTO.getObjectTypeAttribute().getDataType().equals(DataType.INTEGER)) {
                    if (objectAtrributeDTO.getObjectAttribute() != null && objectAtrributeDTO.getObjectAttribute().getIntegerValue() != null) {
                        rowDetail.setColumnValue(objectAtrributeDTO.getObjectAttribute().getIntegerValue().toString());
                    }
                }
                rowDetails.add(rowDetail);
            }
            exportRow.setExportRowDetails(rowDetails);
            exportRows.add(exportRow);
        }
        for (ObjectAtrributeDTO objectAttribute : stockReceiveItemsDTOs.get(0).getItemAttributesList()) {
            headers.add(objectAttribute.getObjectTypeAttribute().getName());
        }
        export.setExportRows(exportRows);
        export.setHeaders(headers);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor((short) 12);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor((short) 9);
        style.setFont(font);
        int i = 0;
        Row header = sheet.createRow(0);
        for (Iterator rowCount = export.getHeaders().iterator(); rowCount.hasNext(); ++i) {
            String headerTxt = (String) rowCount.next();
            header.createCell(i).setCellValue(headerTxt);
            header.getCell(i).setCellStyle(style);
        }
        int var16 = 1;
        Iterator var17 = export.getExportRows().iterator();
        while (var17.hasNext()) {
            ExportRow exportRow1 = (ExportRow) var17.next();
            Row userRow = sheet.createRow(var16++);
            int j = 0;
            for (Iterator var14 = exportRow1.getExportRowDetails().iterator(); var14.hasNext(); ++j) {
                ExportRowDetail exportRowDetail = (ExportRowDetail) var14.next();
                userRow.createCell(j).setCellValue(exportRowDetail.getColumnValue());
            }
        }
    }

    public void downloadExportFile(String fileId, HttpServletResponse response) {
        try {
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileId, "UTF-8"));
        } catch (UnsupportedEncodingException var51) {
            response.setHeader("Content-disposition", "attachment; filename=" + fileId);
        }
        try {
            ServletOutputStream var5 = response.getOutputStream();
            IOUtils.copy((InputStream) fileMap.get(fileId), var5);
            var5.flush();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public List<StockReceiveItemsDTO> getStockIssuedItemsByAttributes(Integer storeId, StockMovementCriteria stockMovementCriteria) {
        Boolean itemSearch = false;
        List<StockReceiveItemsDTO> stockReceiveItemsDTOs = new ArrayList<>();
        List<StockReceiveItemsDTO> stockReceiveItemsDTOs1 = new ArrayList<>();
        List<StockReceiveItemsDTO> stockReceiveItemsDTOs2 = new ArrayList<>();
        ISStockIssueCriteria criteria = new ISStockIssueCriteria();
        criteria.setStoreId(stockMovementCriteria.getStoreId());
        criteria.setSearchAttributes(stockMovementCriteria.getSearchAttributes());
        criteria.setStoreId(storeId);
        criteria.setFromDate(stockMovementCriteria.getFromDate());
        criteria.setToDate(stockMovementCriteria.getToDate());
        if ((stockMovementCriteria.getItemName() != null && stockMovementCriteria.getItemName() != "") || (stockMovementCriteria.getItemNumber() != null && stockMovementCriteria.getItemNumber() != "") || (stockMovementCriteria.getDescription() != null && stockMovementCriteria.getDescription() != "") || (stockMovementCriteria.getUnits() != null && stockMovementCriteria.getUnits() != "")) {
            stockReceiveItemsDTOs1 = getStockMovementIssueItems(stockMovementCriteria);
            itemSearch = true;
        }
        Predicate predicate = stockIssuePredicateBuilder.build(criteria, QISStockIssue.iSStockIssue);
        Iterable<ISStockIssue> stockIssues = stockIssueRepository.findAll(predicate);
        Date startDate2 = null;
        if (criteria.getFromDate() == null || criteria.getFromDate().equals("undefined") || criteria.getFromDate().equals("null") || criteria.getFromDate().trim().equals("")) {
            startDate2 = topStockIssuedRepository.getMinimumDate(storeId);
            if (startDate2 == null) {
                startDate2 = new Date();
            }
        } else {
            startDate2 = stringToDate(criteria.getFromDate());
        }
        Date startDate = new Date(startDate2.getTime() - (1000 * 60));
        Date endDate = new Date(stringToDate(criteria.getToDate()).getTime() + (1000 * 60 * 60 * 24));
        for (ISStockIssue stockIssue : stockIssues) {
            List<ISTStockIssueItem> stockIssueItems = topStockIssuedRepository.findByIssueAndTimeStampAfterAndTimeStampBefore(stockIssue.getId(), startDate, endDate);
            for (ISTStockIssueItem stockIssueItem : stockIssueItems) {
                StockReceiveItemsDTO stockReceiveItemsDTO = new StockReceiveItemsDTO();
                stockReceiveItemsDTO.setItemAttributesList(new ArrayList<>());
                ;
                ISMaterialItem materialItem = materialItemRepository.findOne(stockIssueItem.getItem());
                if (materialItem != null) {
                    stockReceiveItemsDTO.setDate(stockIssueItem.getCreatedDate());
                    stockReceiveItemsDTO.setItemNumber(materialItem.getItemNumber());
                    stockReceiveItemsDTO.setItemName(materialItem.getItemName());
                    stockReceiveItemsDTO.setUnits(materialItem.getUnits());
                    stockReceiveItemsDTO.setQuantity(stockIssueItem.getQuantity());
                    stockReceiveItemsDTO.setOpeningBalance(stockIssueItem.getOpeningBalance());
                    stockReceiveItemsDTO.setClosingBalance(stockIssueItem.getClosingBalance());
                    stockReceiveItemsDTO.setIssueId(stockIssue.getId());
                    Person person = personRepository.findOne(stockIssue.getIssuedTo());
                    if (person != null) {
                        stockReceiveItemsDTO.setIssuedTo(person.getFullName());
                    }
                    for (AttributeSearchDto attributeSearchDto : criteria.getSearchAttributes()) {
                        ObjectAtrributeDTO objectAtrributeDTO = setAttributesForItems(attributeSearchDto, stockIssue);
                        stockReceiveItemsDTO.getItemAttributesList().add(objectAtrributeDTO);
                    }
                    stockReceiveItemsDTOs2.add(stockReceiveItemsDTO);
                }
            }
        }
        if (stockReceiveItemsDTOs1.size() > 0 && stockReceiveItemsDTOs2.size() > 0) {
            for (StockReceiveItemsDTO stockReceiveItemsDTO1 : stockReceiveItemsDTOs1) {
                for (StockReceiveItemsDTO stockReceiveItemsDTO2 : stockReceiveItemsDTOs2) {
                    if (stockReceiveItemsDTO1.getItemNumber().equals(stockReceiveItemsDTO2.getItemNumber()) && stockReceiveItemsDTO1.getIssueId().equals(stockReceiveItemsDTO2.getIssueId())) {
                        stockReceiveItemsDTOs.add(stockReceiveItemsDTO1);
                    }
                }
            }
        } else if (stockReceiveItemsDTOs1.size() > 0) {
            stockReceiveItemsDTOs.addAll(stockReceiveItemsDTOs1);
        } else if (!itemSearch) {
            stockReceiveItemsDTOs.addAll(stockReceiveItemsDTOs2);
        }
        return stockReceiveItemsDTOs;
    }

    public List<StockReceiveItemsDTO> getStockMovementIssueItems(StockMovementCriteria stockMovementCriteria) {
        List<StockReceiveItemsDTO> stockReceiveItemsDTOs = new ArrayList<>();
        Date startDate2 = null;
        Date endDate2 = null;
        if (stockMovementCriteria.getFromDate() == null || stockMovementCriteria.getFromDate().equals("undefined") || stockMovementCriteria.getFromDate().equals("null") || stockMovementCriteria.getFromDate().trim().equals("")) {
            startDate2 = topStockMovementRepository.getMinimumDate(stockMovementCriteria.getStoreId());
            if (startDate2 == null) {
                startDate2 = new Date();
            }
        } else {
            startDate2 = stringToDate(stockMovementCriteria.getFromDate());
        }
        if (stockMovementCriteria.getToDate() == null || stockMovementCriteria.getToDate().equals("undefined") || stockMovementCriteria.getToDate().equals("null") || stockMovementCriteria.getToDate().trim().equals("")) {
            endDate2 = topStockMovementRepository.getMaximumDate(stockMovementCriteria.getStoreId());
            if (endDate2 == null) {
                endDate2 = new Date();
            }
        } else {
            endDate2 = stringToDate(stockMovementCriteria.getToDate());
        }
        Date startDate = new Date(startDate2.getTime() - (1000 * 60));
        Date endDate = new Date(endDate2.getTime() + (1000 * 60 * 60 * 24));
        TypedQuery<ISTopStockMovement> typedQuery = stockMovementPredicateBuilder.getItemTypeQuery(stockMovementCriteria);
        List<ISTopStockMovement> topStockMovements = typedQuery.getResultList();
        List<Integer> stockIds = topStockMovements.stream().map(ISTopStockMovement::getId).collect(Collectors.toList());
        if (stockIds.size() > 0) {
            topStockMovements = topStockMovementRepository.findByIdInAndTimeStampAfterAndTimeStampBefore(stockIds, startDate, endDate);
            for (ISTopStockMovement stockMovement : topStockMovements) {
                ISTStockIssueItem stockIssueItem = stockIssueItemRepository.findOne(stockMovement.getId());
                if (stockIssueItem != null) {
                    ISStockIssue stockIssue = stockIssueRepository.findOne(stockIssueItem.getIssue());
                    if (stockIssue != null) {
                        StockReceiveItemsDTO stockReceiveItemsDTO = new StockReceiveItemsDTO();
                        stockReceiveItemsDTO.setItemAttributesList(new ArrayList<>());
                        stockReceiveItemsDTO.setIssueId(stockIssue.getId());
                        ISMaterialItem materialItem = materialItemRepository.findOne(stockIssueItem.getItem());
                        if (materialItem != null) {
                            stockReceiveItemsDTO.setDate(stockIssueItem.getCreatedDate());
                            stockReceiveItemsDTO.setItemNumber(materialItem.getItemNumber());
                            stockReceiveItemsDTO.setItemName(materialItem.getItemName());
                            stockReceiveItemsDTO.setUnits(materialItem.getUnits());
                            stockReceiveItemsDTO.setQuantity(stockIssueItem.getQuantity());
                            stockReceiveItemsDTO.setOpeningBalance(stockIssueItem.getOpeningBalance());
                            stockReceiveItemsDTO.setClosingBalance(stockIssueItem.getClosingBalance());
                            Person person = personRepository.findOne(stockIssue.getIssuedTo());
                            if (person != null) {
                                stockReceiveItemsDTO.setIssuedTo(person.getFullName());
                            }
                            for (AttributeSearchDto attributeSearchDto : stockMovementCriteria.getSearchAttributes()) {
                                ObjectAtrributeDTO objectAtrributeDTO = setAttributesForItems(attributeSearchDto, stockIssue);
                                stockReceiveItemsDTO.getItemAttributesList().add(objectAtrributeDTO);
                            }
                            stockReceiveItemsDTOs.add(stockReceiveItemsDTO);

                        }
                    }
                }
            }
        }
        return stockReceiveItemsDTOs;
    }

    public ObjectAtrributeDTO setAttributesForItems(AttributeSearchDto attributeSearchDto, ISStockIssue stockIssue) {
        Boolean hasSearchField = false;
        ObjectAtrributeDTO objectAtrributeDTO = new ObjectAtrributeDTO();
        objectAtrributeDTO.setObjectTypeAttribute(attributeSearchDto.getObjectTypeAttribute());
        ObjectAttribute objectAttribute = null;
        if (attributeSearchDto.getText() != null) {
            hasSearchField = true;
            objectAttribute = objectAttributeRepository.findByObjectIdAndAttValue(stockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getText());
        }
        if (attributeSearchDto.getLongText() != null) {
            hasSearchField = true;
            objectAttribute = objectAttributeRepository.findByObjectIdAndAttValue(stockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getLongText());
        }
        if (attributeSearchDto.getInteger() != null) {
            hasSearchField = true;
            objectAttribute = objectAttributeRepository.findByObjectIdAndAttValue(stockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getInteger());
        }
        if (attributeSearchDto.getList() != null && !attributeSearchDto.getObjectTypeAttribute().isListMultiple()) {
            hasSearchField = true;
            objectAttribute = objectAttributeRepository.findByObjectIdAndAttValue(stockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getList());
        }
        if (attributeSearchDto.getaDouble() != null && attributeSearchDto.getaDouble() != 0.0) {
            objectAttribute = objectAttributeRepository.findByObjectIdAndAttValue(stockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getaDouble().toString());
        }
        if (attributeSearchDto.getCurrency() != null) {
            hasSearchField = true;
            objectAttribute = objectAttributeRepository.findByObjectIdAndAttValue(stockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getCurrency());
        }
        if (attributeSearchDto.getTime() != null) {
            hasSearchField = true;
            objectAttribute = objectAttributeRepository.findByObjectIdAndAttValue(stockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getTime());
        }
        if (!hasSearchField) {
        }
        objectAtrributeDTO.setObjectAttribute(objectAttribute);
        return objectAtrributeDTO;
    }

    public Date getMinimumDate(Integer storeId) {
        return topStockIssuedRepository.getMinimumDate(storeId);
    }

    public Page<ISStockIssue> getStockReceivesByAttributes(Integer storeId, Pageable pageable, ISStockIssueCriteria criteria) {
        criteria.setStoreId(storeId);
        Predicate predicate = stockIssuePredicateBuilder.build(criteria, QISStockIssue.iSStockIssue);
        return stockIssueRepository.findAll(predicate, pageable);
    }

    public AttributesDetailsDTO getIssueTypeAttributes(Integer issueId, Integer issueTypeId) {
        List<ISMaterialIssueTypeAttribute> materialIssueTypeAttributes = getAttributesFromHierarchy(issueTypeId);
        List<Integer> attributeIds = materialIssueTypeAttributes.stream().map(ISMaterialIssueTypeAttribute::getId).collect(Collectors.toList());
        AttributesDetailsDTO attributesDetailsDTO = new AttributesDetailsDTO();
        Map<Integer, ObjectAttribute> attributeMap = new HashMap<>();
        List<ObjectTypeAttribute> objectTypeAttributes = objectTypeAttributeRepository.findByIdIn(attributeIds);
        ObjectAttribute attribute = new ObjectAttribute();
        if (objectTypeAttributes.size() > 0) {
            attributesDetailsDTO.setObjectTypeAttributes(objectTypeAttributes);
            for (ObjectTypeAttribute objectTypeAttribute : objectTypeAttributes) {
                attribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(issueId, objectTypeAttribute.getId());
                if (attribute == null) {
                    attribute = new ObjectAttribute();
                }
                attributeMap.put(objectTypeAttribute.getId(), attribute);
            }
            attributesDetailsDTO.setAttributeMap(attributeMap);
        }
        return attributesDetailsDTO;
    }

    private List<ISMaterialIssueTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<ISMaterialIssueTypeAttribute> collector = new ArrayList<>();
        List<ISMaterialIssueTypeAttribute> atts = issueTypeAttributeRepository.findByItemTypeOrderByIdAsc(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarch(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarch(List<ISMaterialIssueTypeAttribute> collector, Integer typeId) {
        ISMaterialIssueType materialIssueType = materialIssueTypeRepository.findOne(typeId);
        if (materialIssueType != null) {
            Integer parentType = materialIssueType.getParentType();
            if (parentType != null) {
                List<ISMaterialIssueTypeAttribute> atts = issueTypeAttributeRepository.findByItemTypeOrderByIdAsc(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarch(collector, parentType);
            }
        }
    }

}
