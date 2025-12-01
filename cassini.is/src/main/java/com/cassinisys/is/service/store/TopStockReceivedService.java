package com.cassinisys.is.service.store;

import com.cassinisys.is.filtering.ISStockReceiveCriteria;
import com.cassinisys.is.filtering.ISStockReceivePredicateBuilder;
import com.cassinisys.is.filtering.StockMovementCriteria;
import com.cassinisys.is.filtering.StockMovementPredicateBuilder;
import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.model.store.*;
import com.cassinisys.is.repo.procm.*;
import com.cassinisys.is.repo.store.*;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.platform.model.common.Export;
import com.cassinisys.platform.model.common.ExportRow;
import com.cassinisys.platform.model.common.ExportRowDetail;
import com.cassinisys.platform.model.core.*;
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
public class TopStockReceivedService {

    public static Map fileMap = new HashMap();

    @Autowired
    ISTopStoreRepository isTopStoreRepository;
    @Autowired
    private ISStockReceiveItemRepository topStockReceivedRepository;
    @Autowired
    private BoqItemRepository boqItemRepository;
    @Autowired
    private ISTopStockMovementRepository topStockMovementRepository;
    @Autowired
    private ISStockReceiveRepository stockReceiveRepository;
    @Autowired
    private MachineItemRepository machineItemRepository;
    @Autowired
    private MaterialItemRepository materialItemRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ISTopInventoryRepository topInventoryRepository;
    @Autowired
    private ISStockReceivePredicateBuilder stockReceivePredicateBuilder;
    @Autowired
    private ReceiveTypeItemAttributeRepository receiveTypeItemAttributeRepository;
    @Autowired
    private MaterialReceiveTypeRepository materialReceiveTypeRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private MaterialReceiveTypeAttributeRepository materialReceiveTypeAttributeRepository;
    @Autowired
    private StockMovementPredicateBuilder stockMovementPredicateBuilder;

    @Transactional(readOnly = false)
    public ISStockReceive create(ISStockReceive stockReceive) {
        if (stockReceive.getReceiveNumberSource() == null) {
            AutoNumber autoNumber = autoNumberService.getByName("Default Stock Receive Number Source");
            String number = autoNumberService.getNextNumber(autoNumber.getId());
            stockReceive.setReceiveNumberSource(number);
        }
        stockReceive = stockReceiveRepository.save(stockReceive);
        Iterator<ISStockReceiveItem> isStockReceiveItemIterator = stockReceive.getStockReceiveItems().iterator();
        List<ISTopInventory> topInventories = new ArrayList<>();
        while (isStockReceiveItemIterator.hasNext()) {
            ISStockReceiveItem stockReceived = isStockReceiveItemIterator.next();
            stockReceived.setObjectType(ISObjectType.RECEIVEITEM);
            Double openingBal = 0.0;
            List<ISTopInventory> topInventories1 = topInventoryRepository.findByItemAndStore(stockReceived.getItem(), isTopStoreRepository.findOne(stockReceive.getStore()));
            if (topInventories1.size() > 0) {
                for (ISTopInventory topInventory : topInventories1) {
                    openingBal += topInventory.getStoreOnHand();
                }
                stockReceived.setOpeningBalance(openingBal);
                stockReceived.setClosingBalance(stockReceived.getOpeningBalance() + stockReceived.getQuantity());
            } else {
                stockReceived.setOpeningBalance(0.0);
                stockReceived.setClosingBalance(stockReceived.getQuantity());
            }
            if (stockReceived != null) {
                stockReceived.setReceive(stockReceive.getId());
                stockReceived.setStore(isTopStoreRepository.findOne(stockReceive.getStore()));
                if (stockReceive.getProject() != null) {
                    stockReceived.setProject(stockReceive.getProject());
                }
                ISTopInventory isItemInventory = topInventoryRepository.findByStoreAndItemAndProject(stockReceived.getStore(), stockReceived.getItem(), stockReceived.getProject());
                if (isItemInventory != null) {
                    isItemInventory.setStoreOnHand(isItemInventory.getStoreOnHand() + stockReceived.getQuantity());
                    topInventories.add(isItemInventory);
                } else {
                    isItemInventory = new ISTopInventory();
                    isItemInventory.setStoreOnHand(stockReceived.getQuantity());
                    isItemInventory.setStore(stockReceived.getStore());
                    isItemInventory.setItem(stockReceived.getItem());
                    isItemInventory.setProject(stockReceived.getProject());
                    topInventories.add(isItemInventory);
                }
                stockReceived.setTimeStamp(new Date());
                topStockReceivedRepository.save(stockReceived);
            }
        }
        topInventoryRepository.save(topInventories);
        return stockReceive;
    }

    public ISStockReceive update(ISStockReceive stockReceive) {
        return stockReceiveRepository.save(stockReceive);
    }

    public void delete(Integer id) {
        topStockReceivedRepository.delete(id);
    }

    public ISStockReceive get(Integer id) {
        return stockReceiveRepository.findOne(id);
    }

    public Page<ISStockReceive> getAll(Pageable pageable) {
        return stockReceiveRepository.findAll(pageable);
    }

    public List<ISTopStockMovement> getStockReceiveItems(Integer receiveId) {
        List<ISStockReceiveItem> receiveItems = topStockReceivedRepository.findByReceive(receiveId);
        List<Integer> rowIds = new ArrayList<>();
        for (ISStockReceiveItem receiveItem : receiveItems) {
            rowIds.add(receiveItem.getId());
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
                    itemDTO.setResourceType("MACHINE");
                    itemDTO.setTimeStamp(stockMovement.getTimeStamp());
                } else {
                    itemDTO.setItemNumber(materialItem.getItemNumber());
                    itemDTO.setId(materialItem.getId());
                    itemDTO.setDescription(materialItem.getDescription());
                    itemDTO.setItemName(materialItem.getItemName());
                    itemDTO.setItemType(materialItem.getItemType().getName());
                    itemDTO.setUnits(materialItem.getUnits());
                    itemDTO.setResourceType("MATERIAL");
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

    public List<ISStockReceive> getStockReceivedByStoreId(Integer storeId) {
        return stockReceiveRepository.findByStore(storeId);
    }

    public Page<ISStockReceive> getPagedStockReceives(Integer storeId, Pageable pageable) {
        List<ISStockReceive> stockReceives = getStockReceivedByStoreId(storeId);
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > stockReceives.size() ? stockReceives.size() : (start + pageable.getPageSize());
        return new PageImpl<ISStockReceive>(stockReceives.subList(start, end), pageable, stockReceives.size());
    }

    @Transactional(readOnly = true)
    public Double getStockReceivedByItemNumber(String itemNumber, Integer boqId) {
        Double receivedQty = 0.0;
        List<ISBoqItem> boqItems = boqItemRepository.findByItemNumber(itemNumber);
        for (ISBoqItem boqItem : boqItems) {
            if (boqItem.getId() == boqId) {
                List<ISTopStockMovement> stockMovements = topStockMovementRepository.findByItem(boqItem.getId());
                for (ISTopStockMovement stockMovement : stockMovements) {
                    if (stockMovement.getMovementType() == MovementType.RECEIVED) {
                        receivedQty += stockMovement.getQuantity();
                    }
                }
            }
        }
        return receivedQty;
    }

    public Page<ISTopStockMovement> getPagedStockReceiveItems(Integer receiveId, Pageable pageable) {
        List<ISTopStockMovement> topStockMovements = getStockReceiveItems(receiveId);
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > topStockMovements.size() ? topStockMovements.size() : (start + pageable.getPageSize());
        return new PageImpl<ISTopStockMovement>(topStockMovements.subList(start, end), pageable, topStockMovements.size());
    }

    public Page<ISStockReceive> freeTextSearch(Pageable pageable, ISStockReceiveCriteria criteria) {
        Predicate predicate = stockReceivePredicateBuilder.build(criteria, QISStockReceive.iSStockReceive);
        return stockReceiveRepository.findAll(predicate, pageable);
    }

    public List<ObjectAttribute> saveReceiveTypeAttributes(List<ISReceiveTypeItemAttribute> attributes) {
        List<ObjectAttribute> objectAttributes = new ArrayList<>();
        for (ISReceiveTypeItemAttribute attribute : attributes) {
            ISReceiveTypeItemAttribute receiveTypeItemAttribute = new ISReceiveTypeItemAttribute();
            ISStockReceive at = stockReceiveRepository.findOne(attribute.getId().getObjectId());
            receiveTypeItemAttribute.setId(new ObjectAttributeId(at.getId(), attribute.getId().getAttributeDef()));
            receiveTypeItemAttribute.setStringValue(attribute.getStringValue());
            receiveTypeItemAttribute.setIntegerValue(attribute.getIntegerValue());
            receiveTypeItemAttribute.setBooleanValue(attribute.getBooleanValue());
            receiveTypeItemAttribute.setDoubleValue(attribute.getDoubleValue());
            receiveTypeItemAttribute.setTimeValue(attribute.getTimeValue());
            receiveTypeItemAttribute.setTimestampValue(attribute.getTimestampValue());
            receiveTypeItemAttribute.setAttachmentValues(attribute.getAttachmentValues());
            receiveTypeItemAttribute.setDateValue(attribute.getDateValue());
            receiveTypeItemAttribute.setCurrencyType(attribute.getCurrencyType());
            receiveTypeItemAttribute.setCurrencyValue(attribute.getCurrencyValue());
            receiveTypeItemAttribute.setRefValue(attribute.getRefValue());
            receiveTypeItemAttribute.setListValue(attribute.getListValue());
            ISReceiveTypeItemAttribute itemAttribute1 = receiveTypeItemAttributeRepository.save(receiveTypeItemAttribute);
            objectAttributes.add(itemAttribute1);
        }
        return objectAttributes;
    }

    public Page<ISStockReceive> getStockReceivesByType(Integer receiveTypeId, Integer storeId, Pageable pageable) {
        ISMaterialReceiveType materialReceiveType = materialReceiveTypeRepository.findOne(receiveTypeId);
        return stockReceiveRepository.findAllByMaterialReceiveTypeAndStore(materialReceiveType, storeId, pageable);
    }

    private Date stringToDate(String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Transactional(
            readOnly = false
    )
    public String exportStockReceiveItems(Integer storeId, StockMovementCriteria criteria, String fileType, HttpServletResponse response) {
        ByteArrayInputStream is = null;
        String fileId = null;
        Export export = new Export();
        export.setFileName("ReceiveItems-Report");
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
            startDate2 = topStockReceivedRepository.getMinimumDate(storeId);
            if (startDate2 == null) {
                startDate2 = new Date();
            }
        } else {
            startDate2 = stringToDate(criteria.getFromDate());
        }
        fromDate = formatter.format(startDate2);
        List<String> headers = new ArrayList<>(Arrays.asList("Date", "Item Number", "Item Name", "Units", "Received Qty", "Supplier Name", "Opening Bal as on " + fromDate, "Closing Bal as on " + criteria.getToDate()));
        List<ExportRow> exportRows = new ArrayList();
        Sheet sheet = workbook.createSheet("ReceiveItems Report");
        sheet.setDefaultColumnWidth(30);
        List<StockReceiveItemsDTO> stockReceiveItemsDTOs = getStockReceivedItemsByAttributes(storeId, criteria);
        for (StockReceiveItemsDTO stockReceiveItemsDTO : stockReceiveItemsDTOs) {
            List<ExportRowDetail> rowDetails = new ArrayList();
            ExportRow exportRow = new ExportRow();
            ExportRowDetail rowDetail1 = new ExportRowDetail();
            rowDetail1.setColumnName("Date");
            rowDetail1.setColumnType("String");
            rowDetail1.setColumnValue(formatter.format(stockReceiveItemsDTO.getDate()));
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
            rowDetail6.setColumnName("Supplier Name");
            rowDetail6.setColumnType("String");
            rowDetail6.setColumnValue(stockReceiveItemsDTO.getSupplierName());
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
                    if (objectAtrributeDTO.getObjectAttribute() != null && objectAtrributeDTO.getObjectAttribute().getStringValue() != null) {
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

    public Page<ISStockReceive> getStockReceivesByAttributes(Integer storeId, Pageable pageable, ISStockReceiveCriteria criteria) {
        criteria.setStoreId(storeId);
        Predicate predicate = stockReceivePredicateBuilder.build(criteria, QISStockReceive.iSStockReceive);
        return stockReceiveRepository.findAll(predicate, pageable);
    }

    public List<StockReceiveItemsDTO> getStockReceivedItemsByAttributes(Integer storeId, StockMovementCriteria stockMovementCriteria) {
        Boolean itemSearch = false;
        List<StockReceiveItemsDTO> stockReceiveItemsDTOs = new ArrayList<>();
        List<StockReceiveItemsDTO> stockReceiveItemsDTOs1 = new ArrayList<>();
        List<StockReceiveItemsDTO> stockReceiveItemsDTOs2 = new ArrayList<>();
        ISStockReceiveCriteria criteria = new ISStockReceiveCriteria();
        criteria.setStoreId(stockMovementCriteria.getStoreId());
        criteria.setSearchAttributes(stockMovementCriteria.getSearchAttributes());
        criteria.setStoreId(storeId);
        criteria.setFromDate(stockMovementCriteria.getFromDate());
        criteria.setToDate(stockMovementCriteria.getToDate());
        if ((stockMovementCriteria.getItemName() != null && stockMovementCriteria.getItemName() != "") || (stockMovementCriteria.getItemNumber() != null && stockMovementCriteria.getItemNumber() != "") || (stockMovementCriteria.getDescription() != null && stockMovementCriteria.getDescription() != "") || (stockMovementCriteria.getUnits() != null && stockMovementCriteria.getUnits() != "")) {
            stockReceiveItemsDTOs1 = getStockMovementReceiveItems(stockMovementCriteria);
            itemSearch = true;
        }
        Predicate predicate = stockReceivePredicateBuilder.build(criteria, QISStockReceive.iSStockReceive);
        Iterable<ISStockReceive> stockReceives = stockReceiveRepository.findAll(predicate);
        Date startDate2 = null;
        if (criteria.getFromDate() == null || criteria.getFromDate().equals("undefined") || criteria.getFromDate().equals("null") || criteria.getFromDate().trim().equals("")) {
            startDate2 = topStockReceivedRepository.getMinimumDate(storeId);
            if (startDate2 == null) {
                startDate2 = new Date();
            }
        } else {
            startDate2 = stringToDate(criteria.getFromDate());
        }
        Date startDate = new Date(startDate2.getTime() - (1000 * 60));
        Date endDate = new Date(stringToDate(criteria.getToDate()).getTime() + (1000 * 60 * 60 * 24));
        for (ISStockReceive stockReceive : stockReceives) {
            List<ISStockReceiveItem> stockReceiveItems = topStockReceivedRepository.findByReceiveAndTimeStampAfterAndTimeStampBefore(stockReceive.getId(), startDate, endDate);
            for (ISStockReceiveItem stockReceiveItem : stockReceiveItems) {
                StockReceiveItemsDTO stockReceiveItemsDTO = new StockReceiveItemsDTO();
                stockReceiveItemsDTO.setItemAttributesList(new ArrayList<>());
                stockReceiveItemsDTO.setReceiveId(stockReceive.getId());
                ISMaterialItem materialItem = materialItemRepository.findOne(stockReceiveItem.getItem());
                if (materialItem != null) {
                    stockReceiveItemsDTO.setDate(stockReceiveItem.getCreatedDate());
                    stockReceiveItemsDTO.setItemNumber(materialItem.getItemNumber());
                    stockReceiveItemsDTO.setItemName(materialItem.getItemName());
                    stockReceiveItemsDTO.setUnits(materialItem.getUnits());
                    stockReceiveItemsDTO.setQuantity(stockReceiveItem.getQuantity());
                    stockReceiveItemsDTO.setOpeningBalance(stockReceiveItem.getOpeningBalance());
                    stockReceiveItemsDTO.setClosingBalance(stockReceiveItem.getClosingBalance());
                    if (stockReceive.getSupplier() != null) {
                        ISSupplier supplier = supplierRepository.findOne(stockReceive.getSupplier());
                        if (supplier != null) {
                            stockReceiveItemsDTO.setSupplierName(supplier.getName());
                        }
                    }
                    for (AttributeSearchDto attributeSearchDto : criteria.getSearchAttributes()) {
                        ObjectAtrributeDTO objectAtrributeDTO = setAttributesForItems(attributeSearchDto, stockReceive);
                        stockReceiveItemsDTO.getItemAttributesList().add(objectAtrributeDTO);
                    }
                    stockReceiveItemsDTOs2.add(stockReceiveItemsDTO);

                }
            }
        }
        if (stockReceiveItemsDTOs1.size() > 0 && stockReceiveItemsDTOs2.size() > 0) {
            for (StockReceiveItemsDTO stockReceiveItemsDTO1 : stockReceiveItemsDTOs1) {
                for (StockReceiveItemsDTO stockReceiveItemsDTO2 : stockReceiveItemsDTOs2) {
                    if (stockReceiveItemsDTO1.getItemNumber().equals(stockReceiveItemsDTO2.getItemNumber()) && stockReceiveItemsDTO1.getReceiveId().equals(stockReceiveItemsDTO2.getReceiveId())) {
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

    public List<StockReceiveItemsDTO> getStockMovementReceiveItems(StockMovementCriteria stockMovementCriteria) {
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
                ISStockReceiveItem stockReceiveItem = topStockReceivedRepository.findOne(stockMovement.getId());
                if (stockReceiveItem != null) {
                    ISStockReceive stockReceive = stockReceiveRepository.findOne(stockReceiveItem.getReceive());
                    if (stockReceive != null) {
                        StockReceiveItemsDTO stockReceiveItemsDTO = new StockReceiveItemsDTO();
                        stockReceiveItemsDTO.setItemAttributesList(new ArrayList<>());
                        stockReceiveItemsDTO.setReceiveId(stockReceive.getId());
                        ISMaterialItem materialItem = materialItemRepository.findOne(stockReceiveItem.getItem());
                        if (materialItem != null) {
                            stockReceiveItemsDTO.setDate(stockReceiveItem.getCreatedDate());
                            stockReceiveItemsDTO.setItemNumber(materialItem.getItemNumber());
                            stockReceiveItemsDTO.setItemName(materialItem.getItemName());
                            stockReceiveItemsDTO.setUnits(materialItem.getUnits());
                            stockReceiveItemsDTO.setQuantity(stockReceiveItem.getQuantity());
                            stockReceiveItemsDTO.setOpeningBalance(stockReceiveItem.getOpeningBalance());
                            stockReceiveItemsDTO.setClosingBalance(stockReceiveItem.getClosingBalance());
                            if (stockReceive.getName() != null) {
                                ISSupplier supplier = supplierRepository.findOne(stockReceive.getSupplier());
                                if (supplier != null) {
                                    stockReceiveItemsDTO.setSupplierName(supplier.getName());
                                }
                            }
                            for (AttributeSearchDto attributeSearchDto : stockMovementCriteria.getSearchAttributes()) {
                                ObjectAtrributeDTO objectAtrributeDTO = setAttributesForItems(attributeSearchDto, stockReceive);
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

    public ObjectAtrributeDTO setAttributesForItems(AttributeSearchDto attributeSearchDto, ISStockReceive stockReceive) {
        Boolean hasSearchField = false;
        ObjectAtrributeDTO objectAtrributeDTO = new ObjectAtrributeDTO();
        objectAtrributeDTO.setObjectTypeAttribute(attributeSearchDto.getObjectTypeAttribute());
        ObjectAttribute objectAttribute = null;
        if (attributeSearchDto.getText() != null) {
            hasSearchField = true;
            objectAttribute = objectAttributeRepository.findByObjectIdAndAttValue(stockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getText());
        }
        if (attributeSearchDto.getLongText() != null) {
            hasSearchField = true;
            objectAttribute = objectAttributeRepository.findByObjectIdAndAttValue(stockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getLongText());
        }
        if (attributeSearchDto.getInteger() != null) {
            hasSearchField = true;
            objectAttribute = objectAttributeRepository.findByObjectIdAndAttValue(stockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getInteger());
        }
        if (attributeSearchDto.getList() != null && !attributeSearchDto.getObjectTypeAttribute().isListMultiple()) {
            hasSearchField = true;
            objectAttribute = objectAttributeRepository.findByObjectIdAndAttValue(stockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getList());
        }
        if (attributeSearchDto.getaBoolean() != null) {
            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndBooleanValue(stockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), Boolean.parseBoolean(attributeSearchDto.getaBoolean()));
        }
        if (attributeSearchDto.getaDouble() != null && attributeSearchDto.getaDouble() != 0.0) {
            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndDoubleValue(stockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(),
                    attributeSearchDto.getaDouble());
        }
        if (attributeSearchDto.getCurrency() != null) {
            hasSearchField = true;
            objectAttribute = objectAttributeRepository.findByObjectIdAndAttValue(stockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getCurrency());
        }
        if (attributeSearchDto.getTime() != null) {
            hasSearchField = true;
            objectAttribute = objectAttributeRepository.findByObjectIdAndAttValue(stockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getTime());
        }
        if (!hasSearchField) {
            objectAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(stockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId());
        }
        objectAtrributeDTO.setObjectAttribute(objectAttribute);
        return objectAtrributeDTO;
    }

    public Date getMinimumDate(Integer storeId) {
        return topStockReceivedRepository.getMinimumDate(storeId);
    }

    public AttributesDetailsDTO getReceiveTypeAttributes(Integer receiveId, Integer receiveTypeId) {
        List<ISMaterialReceiveTypeAttribute> materialReceiveTypeAttributes = getAttributesFromHierarchy(receiveTypeId);
        List<Integer> attributeIds = materialReceiveTypeAttributes.stream().map(ISMaterialReceiveTypeAttribute::getId).collect(Collectors.toList());
        AttributesDetailsDTO attributesDetailsDTO = new AttributesDetailsDTO();
        Map<Integer, ObjectAttribute> attributeMap = new HashMap<>();
        List<ObjectTypeAttribute> objectTypeAttributes = objectTypeAttributeRepository.findByIdIn(attributeIds);
        ObjectAttribute attribute = new ObjectAttribute();
        if (objectTypeAttributes.size() > 0) {
            attributesDetailsDTO.setObjectTypeAttributes(objectTypeAttributes);
            for (ObjectTypeAttribute objectTypeAttribute : objectTypeAttributes) {
                attribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(receiveId, objectTypeAttribute.getId());
                if (attribute == null) {
                    attribute = new ObjectAttribute();
                }
                attributeMap.put(objectTypeAttribute.getId(), attribute);
            }
            attributesDetailsDTO.setAttributeMap(attributeMap);
        }
        return attributesDetailsDTO;
    }

    private List<ISMaterialReceiveTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<ISMaterialReceiveTypeAttribute> collector = new ArrayList<>();
        List<ISMaterialReceiveTypeAttribute> atts = materialReceiveTypeAttributeRepository.findByItemTypeOrderByIdAsc(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarch(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarch(List<ISMaterialReceiveTypeAttribute> collector, Integer typeId) {
        ISMaterialReceiveType materialReceiveType = materialReceiveTypeRepository.findOne(typeId);
        if (materialReceiveType != null) {
            Integer parentType = materialReceiveType.getParentType();
            if (parentType != null) {
                List<ISMaterialReceiveTypeAttribute> atts = materialReceiveTypeAttributeRepository.findByItemTypeOrderByIdAsc(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarch(collector, parentType);
            }
        }
    }

}
