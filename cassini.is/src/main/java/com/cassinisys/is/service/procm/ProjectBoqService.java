package com.cassinisys.is.service.procm;

import com.cassinisys.is.filtering.BoqItemCriteria;
import com.cassinisys.is.filtering.BoqItemPredicateBuilder;
import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.pm.ISProjectResource;
import com.cassinisys.is.model.pm.ProjectBoqAttachment;
import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.model.store.ISTopInventory;
import com.cassinisys.is.model.store.ISTopStockMovement;
import com.cassinisys.is.repo.pm.ProjectBoqAttachmentsRepository;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.is.repo.pm.ResourceRepository;
import com.cassinisys.is.repo.procm.*;
import com.cassinisys.is.repo.store.ISTopInventoryRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Export;
import com.cassinisys.platform.model.common.ExportRow;
import com.cassinisys.platform.model.common.ExportRowDetail;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mysema.query.types.Predicate;
import freemarker.template.Configuration;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for ProjectBoqService
 */
@Service
@Transactional
public class ProjectBoqService extends BoqService implements
        CrudService<ISProjectBoq, Integer>,
        PageableService<ISProjectBoq, Integer> {

    public static Map fileMap = new HashMap();

    @Autowired
    Configuration fmConfiguration;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BoqActualCostRepository boqActualCostRepository;
    @Autowired
    private ProjectBoqRepository projectBoqRepository;

    @Autowired
    private BoqItemRepository boqItemRepository;

    @Autowired
    private BoqItemPredicateBuilder predicateBuilder;

    @Autowired
    private MachineItemRepository machineItemRepository;

    @Autowired
    private MaterialItemRepository materialItemRepository;

    @Autowired
    private MachineTypeRepository machineTypeRepository;

    @Autowired
    private MaterialTypeRepository materialTypeRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private BoqItemPredicateBuilder boqItemPredicateBuilder;

    @Autowired
    private ISTopInventoryRepository topInventoryRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ProjectBoqAttachmentsRepository projectBoqAttachmentsRepository;

    @Autowired
    private FileSystemService fileSystemService;

    /**
     * The method used to getBomItemsByItemType for the list of  ISBoqItem
     **/
    @Transactional(readOnly = true)
    public Page<ISBoqItem> getBomItemsByItemType(Integer projectId, ResourceType itemType, Pageable pageable) {
        List<ISBoqItem> isBoqItems = boqItemRepository.findByProjectAndItemType(projectId, itemType);
        Page<ISBoqItem> pages = new PageImpl<ISBoqItem>(isBoqItems, pageable, isBoqItems.size());
        return pages;
    }

    @Transactional(readOnly = true)
    public Page<ISBoqItem> getItemsForTask(Integer projectId, Integer taskId, ResourceType itemType, Pageable pageable) {
        List<ISBoqItem> isBoqItems = boqItemRepository.findByProjectAndItemType(projectId, itemType);
        List<ISBoqItem> boqItemList = new ArrayList<>();
        for (ISBoqItem boqItem : isBoqItems) {
            ISProjectResource resource = resourceRepository.findByProjectAndReferenceIdAndTaskAndResourceType(projectId, boqItem.getId(), taskId, itemType);
            if (resource == null) {
                boqItemList.add(boqItem);
            }
        }
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > boqItemList.size() ? boqItemList.size() : (start + pageable.getPageSize());
        return new PageImpl<ISBoqItem>(boqItemList.subList(start, end), pageable, boqItemList.size());
    }

    /**
     * The method used to getBomItemsByItemNumber for the list of  ISBoqItem
     **/
    @Transactional(readOnly = true)
    public List<ISBoqItem> getBomItemsByItemNumber(String itemNumber) {
        return boqItemRepository.findByItemNumber(itemNumber);
    }

    /**
     * The method used to getBomItemsByItemNumberAndboqName for the ISBoqItem
     **/
    @Transactional(readOnly = true)
    public ISBoqItem getBomItemsByItemNumberAndboqName(Integer projectId, String itemNumber, String boqName) {
        return boqItemRepository.findByItemNumberAndBoqNameAndProject(itemNumber, boqName, projectId);
    }

    /**
     * The method used to getCostToProject for the list of  ISBoqActualCost
     **/
    @Transactional(readOnly = true)
    public List<ISBoqActualCost> getCostToProject(Integer itemId) {
        List<ISBoqActualCost> actualCosts = boqActualCostRepository.findByBoqItem(itemId);
        return actualCosts;
    }

    /**
     * The method used to deleteCostToProject
     **/
    @Transactional(readOnly = false)
    public void deleteCostToProject(Integer id) {
        checkNotNull(id);
        ISBoqActualCost boqActualCost = boqActualCostRepository.findOne(id);
        ISBoqItem boqItem = boqItemRepository.findOne(boqActualCost.getBoqItem());
        boqItem.setUnitCost(boqItem.getUnitCost() - boqActualCost.getCost());
        if (boqItem.getQuantity() > 0) {
            boqItem.setActualCost(boqItem.getActualCost() - (boqItem.getQuantity() * boqItem.getUnitCost()));
        }
        boqActualCostRepository.delete(id);
    }

    /**
     * The method used to createCostToProject for the list of  ISBoqActualCost
     **/
    @Transactional(readOnly = false)
    public List<ISBoqActualCost> createCostToProject(List<ISBoqActualCost> boqActualCosts) {
        List<ISBoqActualCost> actualCosts = new ArrayList<ISBoqActualCost>();
        for (ISBoqActualCost boqActualCost : boqActualCosts) {
            ISBoqActualCost actualCost = boqActualCostRepository.save(boqActualCost);
            actualCosts.add(actualCost);
        }
        return actualCosts;
    }

    /**
     * The method used to create ISProjectBoq
     **/
    @Override
    @Transactional(readOnly = false)
    public ISProjectBoq create(ISProjectBoq projectBoq) {
        checkNotNull(projectBoq);
        projectBoq.setId(null);
        ISProject project = projectRepository.findOne(projectBoq.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        Login login = sessionWrapper.getSession().getLogin();
        projectBoq.setCreatedBy(login.getPerson().getId());
        projectBoq.setModifiedBy(login.getPerson().getId());
        projectBoq = projectBoqRepository.save(projectBoq);
        return projectBoq;
    }

    /**
     * The method used to update ISProjectBoq
     **/
    @Override
    @Transactional(readOnly = false)
    public ISProjectBoq update(ISProjectBoq projectBoq) {
        checkNotNull(projectBoq);
        checkNotNull(projectBoq.getId());
        ISProject project = projectRepository.findOne(projectBoq.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        ISProjectBoq boq = projectBoqRepository.findOne(projectBoq.getId());
        boq.setName(projectBoq.getName());
        projectBoq = projectBoqRepository.save(projectBoq);
        List<ISBoqItem> boqItems = boqItemRepository.findByBoq(projectBoq.getId());
        for (ISBoqItem boqItem : boqItems) {
            boqItem.setBoqName(projectBoq.getName());
        }
        return projectBoq;
    }

    /**
     * The method used to delete
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISProjectBoq projectBoq = projectBoqRepository.findOne(id);
        if (projectBoq == null) {
            throw new ResourceNotFoundException();
        }
        ISProject project = projectRepository.findOne(projectBoq.getProject());
        projectBoqRepository.delete(projectBoq);

    }

    /**
     * The method used to get ISProjectBoq
     **/
    @Transactional(readOnly = true)
    @Override
    public ISProjectBoq get(Integer id) {
        checkNotNull(id);
        ISProjectBoq projectBoq = projectBoqRepository.findOne(id);
        if (projectBoq == null) {
            throw new ResourceNotFoundException();
        }
        return projectBoq;
    }

    /**
     * The method used to getAll for the list of ISProjectBoq
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISProjectBoq> getAll() {
        return projectBoqRepository.findAll();
    }

    /**
     * The method used to findAll for the page of ISProjectBoq
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISProjectBoq> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "createdDate")));
        }
        return projectBoqRepository.findAll(pageable);
    }

    @Transactional
    public List<ISBoqItem> searchBomItems(Integer prjectId, BoqItemCriteria boqItemCriteria) {
        Predicate predicate = predicateBuilder.build(boqItemCriteria, QISBoqItem.iSBoqItem);
        Iterable<ISBoqItem> isBoqItems = boqItemRepository.findAll(predicate);
        Map<String, ISBoqItem> map = new HashMap<>();
        Map<Integer, ISMaterialItem> materialMap = new HashMap<>();
        ISMaterialItem materialItem = new ISMaterialItem();
        for (ISBoqItem boqItem : isBoqItems) {
            boqItem.setIssuedQty(0.0);
            boqItem.setReceivedQty(0.0);
            boqItem.setInventory(0.0);
            boqItem.setTotalBoqQuantity(0.0);
            boqItem.setReturnedQty(0.0);
            if (boqItem.getItemType().equals(ResourceType.valueOf("MACHINETYPE"))) {
                ISMachineItem machineItem = machineItemRepository.findByItemNumber(boqItem.getItemNumber());
                boqItem.setResourceTypeName(machineItem.getItemType().getName());
                map.put(materialItem.getItemNumber(), boqItem);

            } else if (boqItem.getItemType().equals(ResourceType.valueOf("MATERIALTYPE"))) {
                materialItem = materialItemRepository.findByItemNumber(boqItem.getItemNumber());
                boqItem.setResourceTypeName(materialItem.getItemType().getName());
                map.put(materialItem.getItemNumber(), boqItem);
                materialMap.put(materialItem.getId(), materialItem);
            }
            List<ISBoqItem> boqItemList1 = boqItemRepository.findByProjectAndItemNumber(prjectId, boqItem.getItemNumber());
            for (ISBoqItem boqItem1 : boqItemList1) {
                boqItem.setTotalBoqQuantity(boqItem.getTotalBoqQuantity() + boqItem1.getQuantity());
            }
        }
        List<ISTopInventory> topInventoryList = topInventoryRepository.findByProject(prjectId);
        for (ISTopInventory topInventory : topInventoryList) {
            materialItem = materialMap.get(topInventory.getItem());
            if (materialItem != null) {
                ISBoqItem boqItem = map.get(materialItem.getItemNumber());
                if (boqItem != null) {
                    boqItem.setInventory(boqItem.getInventory() + topInventory.getStoreOnHand());
                }
            }
        }
        List<ISTopStockMovement> issueStockMovementList = topStockMovementRepository.findByProject(prjectId);
        for (ISTopStockMovement stockMovement : issueStockMovementList) {
            materialItem = materialMap.get(stockMovement.getItem());
            if (materialItem != null) {
                ISBoqItem boqItem = map.get(materialItem.getItemNumber());
                if (boqItem != null) {
                    if (stockMovement.getMovementType().equals(MovementType.ISSUED)) {
                        boqItem.setIssuedQty(boqItem.getIssuedQty() + stockMovement.getQuantity());
                    } else if (stockMovement.getMovementType().equals(MovementType.RECEIVED) || stockMovement.getMovementType().equals(MovementType.ALLOCATED) || stockMovement.getMovementType().equals(MovementType.OPENINGBALANCE)) {
                        boqItem.setReceivedQty(boqItem.getReceivedQty() + stockMovement.getQuantity());
                    } else if (stockMovement.getMovementType().equals(MovementType.RETURNED)) {
                        boqItem.setReturnedQty(boqItem.getReturnedQty() + stockMovement.getQuantity());
                    }
                }
            }
        }
        List<ISBoqItem> boqItemList1 = new ArrayList<>();
        boqItemList1 = map.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        return boqItemList1;
    }

    /**
     * The method used to findMultiple for the list of ISProjectBoq
     **/
    @Transactional(readOnly = true)
    public List<ISProjectBoq> findMultiple(List<Integer> ids) {
        return projectBoqRepository.findByIdIn(ids);
    }

    /**
     * The method used to findMultipleItems for the list of ISBoqItem
     **/
    @Transactional(readOnly = true)
    public List<ISBoqItem> findMultipleItems(List<Integer> ids) {
        List<ISBoqItem> boqItems = boqItemRepository.findByIdIn(ids);
        for (ISBoqItem boqItem : boqItems) {
            if (boqItem != null) {
                if (boqItem.getItemType() == ResourceType.MACHINETYPE) {
                    ISMachineItem machineItem = machineItemRepository.findByItemNumber(boqItem.getItemNumber());
                    if (machineItem != null) {
                        boqItem.setResourceTypeName(machineItem.getItemType().getName());
                    }
                } else {
                    ISMaterialItem materialItem = materialItemRepository.findByItemNumber(boqItem.getItemNumber());
                    if (materialItem != null) {
                        boqItem.setResourceTypeName(materialItem.getItemType().getName());
                    }
                }
            }
        }
        return boqItems;
    }

    /**
     * The method used to getItemByItemNumber for the list of ISBoqItem
     **/
    @Transactional(readOnly = true)
    public List<ISBoqItem> getItemByItemNumber(List<String> itemNumbers) {
        return boqItemRepository.findByItemNumberIn(itemNumbers);
    }

    /**
     * The method used to getAllBoqItems for the list of ISBoqItem
     **/
    @Transactional(readOnly = true)
    public List<ISBoqItem> getAllBoqItems(Integer projectId) {
        return boqItemRepository.findByProject(projectId);
    }

    /**
     * The method used to getPagedBoqItems for the page of ISBoqItem
     **/
    @Transactional(readOnly = true)
    public Page<ISBoqItem> getPagedBoqItems(Integer projectId, Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        return boqItemRepository.findAllByProject(projectId, pageable);
    }

    @Transactional(readOnly = true)
    public List<ISBoqItem> getBoqItem(Integer projectId, String itemNumber) {
        return boqItemRepository.findByProjectAndItemNumber(projectId, itemNumber);
    }

    public List<ISProjectBoq> getProjectBoqs(Integer projectId) {
        return projectBoqRepository.findByProject(projectId);
    }

    @Transactional(readOnly = false)
    public void importBoq(Integer projectId, MultipartHttpServletRequest request) throws Exception {
        DataFormatter df = new DataFormatter();
        Map<String, MultipartFile> map = request.getFileMap();
        List<MultipartFile> files = new ArrayList<>(map.values());
        for (MultipartFile file1 : files) {
            if (file1 != null && (file1.getOriginalFilename().trim().endsWith(".xls") || file1.getOriginalFilename().trim().endsWith(".xlsx"))) {
                List<ISBoqItem> boqItemList = new ArrayList<>();
                Workbook workbook = WorkbookFactory.create(file1.getInputStream());
                int totalSheets = workbook.getNumberOfSheets();
                for (int j = 0; j < totalSheets; j++) {
                    int i = 0;
                    Map<String, ObjectTypeAttribute> attributesMap = new HashMap<>();
                    Sheet worksheet = workbook.getSheetAt(j);
                    // Reads the data in excel file until last row is encountered
                    int lastRow = worksheet.getLastRowNum();
                    while (i <= lastRow) {
                        String itemNum = null;
                        // Creates an object for the UserInfo Model
                        ISBoqItem boqItem = new ISBoqItem();
                        // Creates an object representing a single row in excel
                        Row row = worksheet.getRow(i++);
                        // Sets the Read data to the model class
                        //user.setId((int) row.getCell(0).getNumericCellValue());
                        if (row.getRowNum() == 0) {
                            int columnCount = row.getLastCellNum();
                            for (int column = 6; column < columnCount; column++) {
                                if (row.getCell(column).getStringCellValue() != null && row.getCell(column).getStringCellValue() != "" && row.getCell(column).getStringCellValue() != " ") {
                                    ObjectTypeAttribute materialAttribute = objectTypeAttributeRepository.findByNameAndObjectType(row.getCell(column).getStringCellValue(), ObjectType.MATERIAL);
                                    if (materialAttribute == null) {
                                        ObjectTypeAttribute machineAttribute = objectTypeAttributeRepository.findByNameAndObjectType(row.getCell(column).getStringCellValue(), ObjectType.MACHINE);
                                        if (machineAttribute == null) {
                                            throw new RuntimeException("Please save " + row.getCell(column).getStringCellValue() + " in custom properties");
                                        } else {
                                            attributesMap.put(row.getCell(column).getStringCellValue(), machineAttribute);
                                        }
                                    } else {
                                        attributesMap.put(row.getCell(column).getStringCellValue(), materialAttribute);
                                    }
                                }
                            }
                            continue;
                        }
                        //item number
                        Cell cell1 = row.getCell(0);
                        //item name
                        Cell cell2 = row.getCell(1);
                        //item type
                        Cell cell3 = row.getCell(2);
                        //item description
                        Cell cell4 = row.getCell(3);
                        //units
                        Cell cell5 = row.getCell(4);
                        //quantity
                        Cell cell6 = row.getCell(5);
                        //notes
//                        Cell cell7 = row.getCell(6);
                        if (cell2.getStringCellValue() == null || cell2.getStringCellValue() == "") {
                            throw new CassiniException("Item Name cannot be empty");
                        }
                        if (cell1.getStringCellValue() == null || cell1.getStringCellValue() == "") {
                            if (cell2.getStringCellValue() != null || cell2.getStringCellValue() != "") {
                                if (cell3.getStringCellValue() != null && cell3.getStringCellValue() != "") {
                                    itemNum = findItemsByType(cell1.getStringCellValue(), cell2.getStringCellValue(), cell3.getStringCellValue(), cell4.getStringCellValue(), cell5.getStringCellValue());
                                }
                            } else if (cell3.getStringCellValue() != null && cell3.getStringCellValue() != "") {
                                throw new CassiniException("Item Number, Item Name and Item Description cannot be empty");
                            } else {
                                throw new CassiniException("Item Name cannot be empty");
                            }
                        } else {
                            /*if ((cell2.getStringCellValue() != null || cell2.getStringCellValue() != "") && (cell3.getStringCellValue() != null && cell3.getStringCellValue() != "")) {
						        ISMaterialType materialType = materialTypeRepository.findByName(cell3.getStringCellValue());
                                if (materialType != null) {
                                    ISMaterialItem materialItem = materialItemRepository.findByItemNameAndItemType(cell2.getStringCellValue(), materialType);
                                    if (materialItem != null) {
                                        if (!(materialItem.getItemNumber().equalsIgnoreCase(cell1.getStringCellValue()) && materialItem.getUnits().equals(cell5.getStringCellValue()))) {
                                            throw new CassiniException("row : " + (row.getRowNum() + 1) + "  Different Item Number already exists with same Item Name and Type");
                                        }
                                    }
                                } else {
                                    ISMachineType machineType = machineTypeRepository.findByName(cell3.getStringCellValue());
                                    if (machineType != null) {
                                        ISMachineItem machineItem = machineItemRepository.findByItemNameAndItemType(cell2.getStringCellValue(), machineType);
                                        if (machineItem != null) {
                                            if (!(machineItem.getItemNumber().equalsIgnoreCase(cell1.getStringCellValue()) && machineItem.getUnits().equals(cell5.getStringCellValue()))) {
                                                throw new CassiniException("row : " + (row.getRowNum() + 1) + "  Different Item Number already exists with same Item Name and Type");
                                            }
                                        }
                                    }
                                }
                            }*/
                            ISMachineItem machineItem = null;
                            ISMaterialItem materialItem = null;
                            if ((cell1.getStringCellValue() != null || cell1.getStringCellValue() != "")) {
                                materialItem = materialItemRepository.findByItemNumber(cell1.getStringCellValue());
                                if (materialItem != null) {
                                    if ((materialItem.getItemNumber().equalsIgnoreCase(cell1.getStringCellValue()))) {
                                        throw new CassiniException("row : " + (row.getRowNum() + 1) + "  Item Number already exists");
                                    }
                                } else {
                                    machineItem = machineItemRepository.findByItemNumber(cell1.getStringCellValue());
                                    if (machineItem != null) {
                                        if ((machineItem.getItemNumber().equalsIgnoreCase(cell1.getStringCellValue()))) {
                                            throw new CassiniException("row : " + (row.getRowNum() + 1) + "  Item Number already exists");
                                        }
                                    }
                                }
                            }
                            if (machineItem != null) {
                                itemNum = machineItem.getItemNumber();
                            } else if (materialItem != null) {
                                itemNum = materialItem.getItemNumber();
                            } else {
                                if (cell3.getStringCellValue() == null || cell3.getStringCellValue().equals("")) {
                                    ISMaterialType isMaterialType = materialTypeRepository.findByName("Imported Parts");
                                    if (isMaterialType == null) {
                                        isMaterialType = new ISMaterialType();
                                        AutoNumber autoNumber = autoNumberRepository.findByName("Default Material Item Number Source");
                                        isMaterialType.setName("Imported Parts");
                                        isMaterialType.setMaterialNumberSource(autoNumber);
                                        isMaterialType = materialTypeRepository.save(isMaterialType);
                                    }
                                    itemNum = findItemsByType(cell1.getStringCellValue(), cell2.getStringCellValue(), isMaterialType.getName(), cell4.getStringCellValue(), df.formatCellValue(cell5));
                                } else {
                                    itemNum = findItemsByType(cell1.getStringCellValue(), cell2.getStringCellValue(), cell3.getStringCellValue(), cell4.getStringCellValue(), df.formatCellValue(cell5));
                                }
                            }

                        }
                        //saving attributes
                        int cellNum = 6;
                        for (int attr = 0; attr < attributesMap.size(); attr++) {
                            Integer itemId = null;
                            String cellValue = worksheet.getRow(0).getCell(cellNum).getStringCellValue();
                            ISMachineItem machineItem = machineItemRepository.findByItemNumber(cell1.getStringCellValue());
                            ISMaterialItem materialItem = materialItemRepository.findByItemNumber(cell1.getStringCellValue());
                            if (machineItem != null) {
                                itemId = machineItem.getId();
                            } else if (materialItem != null) {
                                itemId = materialItem.getId();
                            }
                            ObjectTypeAttribute objectTypeAttribute = attributesMap.get(worksheet.getRow(0).getCell(cellNum).getStringCellValue());
                            if (objectTypeAttribute.isRequired() && (getCellValue(row.getCell(cellNum)) == null || getCellValue(row.getCell(cellNum)).equals(" "))) {
                                throw new RuntimeException(cellValue + " at row : " + row.getRowNum() + 1 + " cannot be empty");
                            }
                            if (row.getCell(cellNum) != null) {
                                ObjectAttribute objectAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemId, objectTypeAttribute.getId());
                                if (objectAttribute == null) {
                                    ObjectAttribute objectAttribute1 = new ObjectAttribute();
                                    objectAttribute1.setObjectTypeAttribute(objectTypeAttribute);
                                    ObjectAttributeId attributeId = new ObjectAttributeId();
                                    attributeId.setObjectId(itemId);
                                    attributeId.setAttributeDef(objectTypeAttribute.getId());
                                    objectAttribute1.setId(attributeId);
                                    if (objectTypeAttribute.getDataType().equals(DataType.TEXT)) {
                                        objectAttribute1.setStringValue(getCellValue(row.getCell(cellNum)));
                                        objectAttributeRepository.save(objectAttribute1);
                                    } else if (objectTypeAttribute.getDataType().equals(DataType.INTEGER)) {
                                        objectAttribute1.setIntegerValue(Integer.parseInt(getCellValue(row.getCell(cellNum))));
                                        objectAttributeRepository.save(objectAttribute1);
                                    } else if (objectTypeAttribute.getDataType().equals(DataType.DOUBLE)) {
                                        objectAttribute1.setDoubleValue(Double.parseDouble(getCellValue(row.getCell(cellNum))));
                                        objectAttributeRepository.save(objectAttribute1);
                                    }
                                }
                                cellNum++;
                            }
                        }
                        ISBoqItem isBoqItem = boqItemRepository.findByItemNumberAndBoqNameAndProject(itemNum, worksheet.getSheetName(), projectId);
                        if (isBoqItem == null) {
                            ISMachineItem machineItem = machineItemRepository.findByItemNumber(itemNum);
                            ISMaterialItem materialItem = materialItemRepository.findByItemNumber(itemNum);
                            if (machineItem != null) {
                                boqItem.setItemType(ResourceType.MACHINETYPE);
                                boqItem.setItemNumber(itemNum);
                                boqItem.setItemName(row.getCell(1).toString());
                            } else if (materialItem != null) {
                                boqItem.setItemType(ResourceType.MATERIALTYPE);
                                boqItem.setItemNumber(itemNum);
                                boqItem.setItemName(row.getCell(1).getStringCellValue());
                            }
                            ISProjectBoq boq = projectBoqRepository.findByNameAndProject(worksheet.getSheetName(), projectId);
                            if (boq == null) {
                                ISProjectBoq projectBoq = new ISProjectBoq();
                                projectBoq.setProject(projectId);
                                projectBoq.setName(worksheet.getSheetName());
                                projectBoqRepository.save(projectBoq);
                                boqItem.setBoqName(projectBoq.getName());
                                boqItem.setBoq(projectBoq.getId());
                            } else {
                                boqItem.setBoqName(boq.getName());
                                boqItem.setBoq(boq.getId());
                            }
                            boqItem.setProject(projectId);
                            boqItem.setUnitCost(0.0);
                            boqItem.setUnitPrice(0.0);
                            boqItem.setActualCost(0.0);
                            boqItem.setIsSavedItem(false);
                            if (cell2 == null) {
                                boqItem.setItemName("");
                            } else {
                                boqItem.setItemName(getCellValue(row.getCell(1)));
                            }
                            if (cell4 == null) {
                                boqItem.setDescription("");
                            } else {
                                boqItem.setDescription(getCellValue(row.getCell(3)));
                            }
                            if (cell5 != null) {
                                boqItem.setUnits(getCellValue(row.getCell(4)));
                            } else {
                                boqItem.setUnits("");
                            }
                            if (cell6 != null) {
                                Double qty = (cell6.getNumericCellValue());
                                boqItem.setQuantity(qty);
                            } else {
                                boqItem.setQuantity(0.0);
                            }
                            boqItemRepository.save(boqItem);
                        } else {
                            if (cell6 != null) {
                                String qty = (getCellValue(cell6));
                                isBoqItem.setQuantity(isBoqItem.getQuantity() + Double.parseDouble(qty));
                                boqItemRepository.save(isBoqItem);
                            }
                        }

                    }
                }
                workbook.close();
            } else {
                throw new IllegalArgumentException("Please upload excel sheet with proper Name & Code data");
            }
        }
    }

    private String getCellValue(Cell cell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }

    @Transactional(readOnly = false)
    public String findItemsByType(String itemNumber, String itemName, String itemType, String description, String units) {
        String itemNum = null;
        ISMaterialType materialType = materialTypeRepository.findByName(itemType);
        ISMachineType machineType = machineTypeRepository.findByName(itemType);
        if (materialType == null && machineType == null) {
            ISMaterialType isMaterialType = new ISMaterialType();
            isMaterialType.setName(itemType);
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Material Item Number Source");
            isMaterialType.setMaterialNumberSource(autoNumber);
            isMaterialType = materialTypeRepository.save(isMaterialType);
            ISMaterialItem materialItem1 = new ISMaterialItem();
            materialItem1.setItemType(isMaterialType);
            if (units != null) {
                materialItem1.setUnits(units);
            } else {
                materialItem1.setUnits("");
            }
            if (description == null) {
                materialItem1.setDescription("Store Imported Part");
            } else {
                materialItem1.setDescription(description);
            }
            materialItem1.setActive(false);
            if (itemName != null) {
                materialItem1.setItemName(itemName);
            } else {
                materialItem1.setItemName("");
            }
            materialItem1.setUnitCost(0.0);
            materialItem1.setUnitPrice(0.0);
            materialItem1.setItemType(isMaterialType);
            if (itemNumber != null && itemNumber != "") {
                materialItem1.setItemNumber(itemNumber);
            } else {
                String number = getNextMaterialNumber(itemName, isMaterialType);
                if (number != null) {
                    materialItem1.setItemNumber(number);
                    itemNum = materialItem1.getItemNumber();
                } else {
                    itemNum = materialItemRepository.findByItemNameAndItemType(itemName, isMaterialType).getItemNumber();
                }
            }
            materialItem1.setUnitCost(0.0);
            materialItem1.setUnitPrice(0.0);
            materialItem1.setItemType(isMaterialType);
            materialItem1 = materialItemRepository.save(materialItem1);
            itemNum = materialItem1.getItemNumber();

        } else if (materialType != null) {
            ISMaterialItem materialItem1 = new ISMaterialItem();
            materialItem1.setItemType(materialType);
            ISMaterialItem materialItem = materialItemRepository.findByItemNumberAndItemType(itemNumber, materialType);
            if (materialItem != null) {
                materialItem1 = materialItem;
                itemNum = materialItem.getItemNumber();
            } else {
                if (itemNumber != null && itemNumber != "") {
                    materialItem1.setItemNumber(itemNumber);
                    itemNum = materialItem1.getItemNumber();
                } else {
                    String number = getNextMaterialNumber(itemName, materialType);
                    if (number != null) {
                        materialItem1.setItemNumber(number);
                        itemNum = materialItem1.getItemNumber();
                    } else {
                        materialItem1 = materialItemRepository.findByItemNameAndItemType(itemName, materialType);
                    }
                }
            }
            if (units != null) {
                materialItem1.setUnits(units);
            } else {
                materialItem1.setUnits("");
            }
            if (description == null) {
                materialItem1.setDescription("Store Imported Part");
            } else {
                materialItem1.setDescription(description);
            }
            materialItem1.setActive(false);
            if (itemName != null) {
                materialItem1.setItemName(itemName);
            } else {
                materialItem1.setItemName("");
            }
            materialItem1.setUnitCost(0.0);
            materialItem1.setUnitPrice(0.0);
            materialItem1.setItemType(materialType);
            materialItem1 = materialItemRepository.save(materialItem1);
            itemNum = materialItem1.getItemNumber();

        } else if (machineType != null) {
            ISMachineItem machineItem1 = new ISMachineItem();
            machineItem1.setItemType(machineType);
            ISMachineItem machineItem = machineItemRepository.findByItemNumberAndItemType(itemName, machineType);
            if (machineItem != null) {
                machineItem1 = machineItem;
                itemNum = machineItem.getItemNumber();
            } else {
                if (itemNumber != null && itemNumber != "") {
                    machineItem1.setItemNumber(itemNumber);
                    machineItemRepository.save(machineItem1);
                    itemNum = machineItem1.getItemNumber();
                } else {
                    String number = getNextMachineNumber(itemName, machineType);
                    if (number != null) {
                        machineItem1.setItemNumber(number);
                        machineItemRepository.save(machineItem1);
                        itemNum = machineItem1.getItemNumber();
                    } else {
                        itemNum = machineItemRepository.findByItemNameAndItemType(itemName, machineType).getItemNumber();
                    }
                }
            }
            if (units != null) {
                machineItem1.setUnits(units);
            } else {
                machineItem1.setUnits("");
            }
            if (description == null) {
                machineItem1.setDescription("Store Imported Part");
            } else {
                machineItem1.setDescription(description);
            }
            if (itemName != null) {
                machineItem1.setItemName(itemName);
            } else {
                machineItem1.setItemName("");
            }
            machineItem1.setUnitCost(0.0);
            machineItem1.setUnitPrice(0.0);
            machineItem1.setItemType(machineType);
            machineItemRepository.save(machineItem1);

        }
        return itemNum;
    }

    @Transactional(readOnly = false)
    public String getNextMaterialNumber(String itemName, ISMaterialType materialType) {
        String next = null;
        ISMaterialItem materialItem = materialItemRepository.findByItemNameAndItemType(itemName, materialType);
        if (materialItem == null) {
            AutoNumber auto = autoNumberRepository.findOne(materialType.getMaterialNumberSource().getId());
            next = auto.next();
            materialItem = materialItemRepository.findByItemNumber(next);
            if (materialItem != null && materialItem.getItemNumber() != null) {
                if (materialItem.getItemName() != itemName) {
                    auto.setNextNumber(auto.getNextNumber());
                    return getNextMaterialNumber(itemName, materialType);
                } else {
                    return null;
                }
            }
        }
        return next;
    }

    @Transactional(readOnly = false)
    public String getNextMachineNumber(String itemName, ISMachineType machineType) {
        String next = null;
        ISMachineItem machineItem = machineItemRepository.findByItemNameAndItemType(itemName, machineType);
        if (machineItem == null) {
            AutoNumber auto = autoNumberRepository.findOne(machineType.getMachineNumberSource().getId());
            next = auto.next();
            machineItem = machineItemRepository.findByItemNumber(next);
            if (machineItem != null && machineItem.getItemNumber() != null) {
                if (machineItem.getItemName() != itemName) {
                    auto.setNextNumber(auto.getNextNumber());
                    return getNextMachineNumber(itemName, machineType);
                } else {
                    return null;
                }
            }
        }
        return next;
    }

    @Transactional(
            readOnly = false
    )
    public String exportBoq(Integer projectId, String fileType, Export export, HttpServletResponse response) {
        ByteArrayInputStream is = null;
        String fileId = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dt = new Date();
        String fName = export.getFileName() + "_" + dateFormat.format(dt);
        if (fileType != null && fileType.equalsIgnoreCase("excel")) {
            fileId = export.getFileName() + ".xls";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + export.getFileName() + "\"");
            response.setContentType("application/vnd.ms-excel");
            HSSFWorkbook e3 = new HSSFWorkbook();
            try {
                this.buildExcelDocument(projectId, export, e3, response);
                ByteArrayOutputStream e2 = new ByteArrayOutputStream();
                e3.write(e2);
                is = new ByteArrayInputStream(e2.toByteArray());
            } catch (Exception var15) {
                var15.printStackTrace();
            }
        } else {
            ByteArrayOutputStream e4;
            if (fileType != null && fileType.equalsIgnoreCase("pdf")) {
                fileId = export.getFileName() + ".pdf";
                e4 = new ByteArrayOutputStream();
                response.setHeader("Content-Disposition", "attachment; filename=\"" + export.getFileName() + "\"");
                response.setContentType("application/pdf");
                try {
                    Document e1 = new Document(PageSize.A4.rotate(), 36.0F, 36.0F, 54.0F, 36.0F);
                    PdfWriter writer = PdfWriter.getInstance(e1, e4);
                    writer.setViewerPreferences(this.getViewerPreferences());
                    e1.open();
                    e1.add(new Paragraph(export.getFileName() + " :" + LocalDate.now()));
                    this.buildPdfDocument(export, e1, response);
                    e1.close();
                    response.setContentLength(e4.toByteArray().length);
                    is = new ByteArrayInputStream(e4.toByteArray());
                } catch (Exception var14) {
                    var14.printStackTrace();
                }
            } else if (fileType != null && fileType.equalsIgnoreCase("csv")) {
                fileId = export.getFileName() + ".csv";
                response.setHeader("Content-Disposition", "attachment; filename=\"" + export.getFileName() + "\"");
                response.setContentType("text/csv");
                e4 = null;
                try {
                    e4 = this.writeCsvStream(export);
                    response.setContentLength(e4.toByteArray().length);
                } catch (IOException var13) {
                    var13.printStackTrace();
                }
                is = new ByteArrayInputStream(e4.toByteArray());
            } else if (fileType != null && fileType.equalsIgnoreCase("html")) {
                fileId = export.getFileName() + ".html";
                try {
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + export.getFileName() + "\"");
                    response.setContentType("text/html");
                    String e = this.getExportHtml(export);
                    response.setContentLength(e.getBytes().length);
                    is = new ByteArrayInputStream(e.getBytes());
                } catch (Exception var12) {
                    var12.printStackTrace();
                }
            }
        }
        fileMap.put(fileId, is);
        return fileId;
    }

    @Transactional(
            readOnly = false
    )
    public void buildExcelDocument(Integer projectId, Export export, Workbook workbook, HttpServletResponse response) throws Exception {
        List<ISProjectBoq> projectBoqs = projectBoqRepository.findByProject(projectId);
        for (ISProjectBoq projectBoq : projectBoqs) {
            List<ExportRow> exportRows = new ArrayList();
            Sheet sheet = workbook.createSheet(projectBoq.getName());
            sheet.setDefaultColumnWidth(30);
            List<ISBoqItem> boqItems = boqItemRepository.findByBoq(projectBoq.getId());
            for (ISBoqItem boqItem : boqItems) {
                if (boqItem.getItemType() == ResourceType.MACHINETYPE) {
                    ISMachineItem machineItem = machineItemRepository.findByItemNumber(boqItem.getItemNumber());
                    boqItem.setResourceTypeName(machineItem.getItemType().getName());
                } else {
                    ISMaterialItem materialItem = materialItemRepository.findByItemNumber(boqItem.getItemNumber());
                    boqItem.setResourceTypeName(materialItem.getItemType().getName());
                }
            }
            for (ISBoqItem boqItem : boqItems) {
                List<ExportRowDetail> rowDetails = new ArrayList();
                ExportRow exportRow = new ExportRow();
                ExportRowDetail rowDetail1 = new ExportRowDetail();
                rowDetail1.setColumnName("Item Number");
                rowDetail1.setColumnType("String");
                rowDetail1.setColumnValue(boqItem.getItemNumber());
                ExportRowDetail rowDetail2 = new ExportRowDetail();
                rowDetail2.setColumnName("Item Name");
                rowDetail2.setColumnType("String");
                rowDetail2.setColumnValue(boqItem.getItemName());
                ExportRowDetail rowDetail3 = new ExportRowDetail();
                rowDetail3.setColumnName("Item Description");
                rowDetail3.setColumnType("String");
                rowDetail3.setColumnValue(boqItem.getDescription());
                ExportRowDetail rowDetail4 = new ExportRowDetail();
                rowDetail4.setColumnName("Units");
                rowDetail4.setColumnType("String");
                rowDetail4.setColumnValue(boqItem.getUnits());
                ExportRowDetail rowDetail5 = new ExportRowDetail();
                rowDetail5.setColumnName("Quantity");
                rowDetail5.setColumnType("Integer");
                rowDetail5.setColumnValue(boqItem.getQuantity().toString());
                ExportRowDetail rowDetail6 = new ExportRowDetail();
                rowDetail6.setColumnName("Notes");
                rowDetail6.setColumnType("String");
                rowDetail6.setColumnValue(boqItem.getNotes());
                ExportRowDetail rowDetail7 = new ExportRowDetail();
                rowDetail7.setColumnName("Item Type");
                rowDetail7.setColumnType("String");
                rowDetail7.setColumnValue(boqItem.getResourceTypeName());
                rowDetails.add(rowDetail1);
                rowDetails.add(rowDetail2);
                rowDetails.add(rowDetail3);
                rowDetails.add(rowDetail7);
                rowDetails.add(rowDetail4);
                rowDetails.add(rowDetail5);
                rowDetails.add(rowDetail6);
                exportRow.setExportRowDetails(rowDetails);
                exportRows.add(exportRow);
            }
            export.setExportRows(exportRows);
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
    }

    @Transactional(
            readOnly = false
    )
    protected int getViewerPreferences() {
        return 2053;
    }

    @Transactional(
            readOnly = false
    )
    public String getExportHtml(Export export) {
        HashMap model = new HashMap();
        List exportRows = export.getExportRows();
        Iterator templatePath = exportRows.iterator();
        while (templatePath.hasNext()) {
            ExportRow exportHtmlData = (ExportRow) templatePath.next();
            List exportRowDetails = exportHtmlData.getExportRowDetails();
            Iterator var7 = exportRowDetails.iterator();
            while (var7.hasNext()) {
                ExportRowDetail exportRowDetail = (ExportRowDetail) var7.next();
                String value = exportRowDetail.getColumnValue();
                if (value == null) {
                    exportRowDetail.setColumnValue(" ");
                }
            }
        }
        model.put("exportHeaders", export.getHeaders());
        model.put("exportData", export.getExportRows());
        String templatePath1 = "tablesDataExport/tablesDataExport.html";
        String exportHtmlData1 = this.getContentFromTemplate(templatePath1, model);
        return exportHtmlData1;
    }

    @Transactional(
            readOnly = false
    )
    public String getContentFromTemplate(String templatePath, Map<String, Object> model) {
        StringBuffer content = new StringBuffer();
        try {
            this.fmConfiguration.setClassForTemplateLoading(this.getClass(), "/templates/");
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(this.fmConfiguration.getTemplate(templatePath), model));
        } catch (Exception var5) {
            var5.printStackTrace();
        }
        return content.toString();
    }

    @Transactional(
            readOnly = false
    )
    public void buildPdfDocument(Export export, Document document, HttpServletResponse response) throws Exception {
        PdfPTable table = new PdfPTable(export.getHeaders().size());
        table.setWidthPercentage(100.0F);
        table.setSpacingBefore(10.0F);
        com.itextpdf.text.Font font = FontFactory.getFont("Times");
        font.setColor(BaseColor.WHITE);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setPadding(5.0F);
        Iterator var7 = export.getHeaders().iterator();
        while (var7.hasNext()) {
            String exportRow = (String) var7.next();
            cell.setPhrase(new Phrase(exportRow, font));
            table.addCell(cell);
        }
        var7 = export.getExportRows().iterator();
        while (var7.hasNext()) {
            ExportRow exportRow1 = (ExportRow) var7.next();
            Iterator var9 = exportRow1.getExportRowDetails().iterator();
            while (var9.hasNext()) {
                ExportRowDetail exportRowDetail = (ExportRowDetail) var9.next();
                table.addCell(exportRowDetail.getColumnValue());
            }
        }
        document.add(table);
    }

    @Transactional(
            readOnly = false
    )
    public ByteArrayOutputStream writeCsvStream(Export export) throws IOException {
        ArrayList listList = new ArrayList();
        ArrayList lst = null;
        listList.add(export.getHeaders());
        Iterator riskCategoryCsvStream = export.getExportRows().iterator();
        while (riskCategoryCsvStream.hasNext()) {
            ExportRow out = (ExportRow) riskCategoryCsvStream.next();
            lst = new ArrayList();
            Iterator csvPrinter = out.getExportRowDetails().iterator();
            while (csvPrinter.hasNext()) {
                ExportRowDetail exportRowDetail = (ExportRowDetail) csvPrinter.next();
                lst.add(exportRowDetail.getColumnValue());
            }
            listList.add(lst);
        }
        ByteArrayOutputStream riskCategoryCsvStream1 = new ByteArrayOutputStream();
        BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(riskCategoryCsvStream1));
        CSVPrinter csvPrinter1 = new CSVPrinter(out1, CSVFormat.EXCEL);
        csvPrinter1.printRecords(listList);
        csvPrinter1.flush();
        csvPrinter1.close();
        return riskCategoryCsvStream1;
    }

    @Transactional(
            readOnly = false
    )
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

    @Transactional(readOnly = true)
    public Page<ISBoqItem> getBoqItemsByFilters(Integer projectId, Integer taskId, Pageable pageable, BoqItemCriteria boqItemCriteria) {
        boqItemCriteria.setProject(projectId);
        Predicate predicate = boqItemPredicateBuilder.build(boqItemCriteria, QISBoqItem.iSBoqItem);
        Iterable<ISBoqItem> items = boqItemRepository.findAll(predicate);
        List<ISBoqItem> boqItemList = new ArrayList<>();
        ResourceType resourceType = null;
        for (ISBoqItem boqItem : items) {
            if (boqItemCriteria.getItemType().equals("MACHINETYPE")) {
                resourceType = ResourceType.MACHINETYPE;
            } else if (boqItemCriteria.getItemType().equals("MATERIALTYPE")) {
                resourceType = ResourceType.MATERIALTYPE;
            } else {
                resourceType = ResourceType.MANPOWERTYPE;
            }
            ISProjectResource resource = resourceRepository.findByProjectAndReferenceIdAndTaskAndResourceType(projectId, boqItem.getId(), taskId, resourceType);
            if (resource == null) {
                boqItemList.add(boqItem);
            }
        }
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > boqItemList.size() ? boqItemList.size() : (start + pageable.getPageSize());
        return new PageImpl<ISBoqItem>(boqItemList.subList(start, end), pageable, boqItemList.size());
    }

    public void importBoqPdf(Integer projectId, MultipartHttpServletRequest request) throws CassiniException {
        Login login = sessionWrapper.getSession().getLogin();
        List<MultipartFile> multipartFiles = new ArrayList<>(request.getFileMap().values());
        MultipartFile multipartFile = multipartFiles.get(0);
        String fileName = multipartFile.getOriginalFilename();
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = multipartFile.getName();
        }
        ProjectBoqAttachment boqAttachment = projectBoqAttachmentsRepository.findByProject(projectId);
        if (boqAttachment != null) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + projectId + File.separator + boqAttachment.getId();
            fileSystemService.deleteDocumentFromDisk(boqAttachment.getId(), dir);
            projectBoqAttachmentsRepository.delete(boqAttachment.getId());
        }
        ProjectBoqAttachment projectBoqAttachment = new ProjectBoqAttachment();
        projectBoqAttachment.setName(fileName);
        projectBoqAttachment.setProject(projectId);
        projectBoqAttachment.setSize(multipartFile.getSize());
        projectBoqAttachment.setCreatedBy(login.getPerson().getId());
        projectBoqAttachment.setModifiedBy(login.getPerson().getId());
        projectBoqAttachment = projectBoqAttachmentsRepository.save(projectBoqAttachment);
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + projectId;
        File file1 = new File(path);
        try {
            if (!file1.exists()) {
                FileUtils.forceMkdir(file1);
            }
        } catch (IOException e) {
            throw new CassiniException("Failed to create file " + path + " REASON: " + e.getMessage());
        }
        path = path + File.separator + projectBoqAttachment.getId();
        fileSystemService.saveDocumentToDisk(multipartFile, path);

    }

    @Transactional(readOnly = true)
    public ProjectBoqAttachment getPdfFile(Integer projectId) {
        ProjectBoqAttachment boqAttachments = projectBoqAttachmentsRepository.findByProject(projectId);
        return boqAttachments;
    }

}


