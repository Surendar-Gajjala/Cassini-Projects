package com.cassinisys.is.service.store;

import com.cassinisys.is.filtering.ReportCriteria;
import com.cassinisys.is.filtering.StockMovementCriteria;
import com.cassinisys.is.filtering.StockMovementPredicateBuilder;
import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.model.procm.dto.MaterialTypeStoresReportDTO;
import com.cassinisys.is.model.procm.dto.StoresReportDTO;
import com.cassinisys.is.model.store.*;
import com.cassinisys.is.repo.procm.MachineItemRepository;
import com.cassinisys.is.repo.procm.MaterialItemRepository;
import com.cassinisys.is.repo.procm.MaterialTypeRepository;
import com.cassinisys.is.repo.procm.SupplierRepository;
import com.cassinisys.is.repo.store.*;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.platform.model.common.Export;
import com.cassinisys.platform.model.common.ExportRow;
import com.cassinisys.platform.model.common.ExportRowDetail;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.common.ExportService;
import com.cassinisys.platform.service.core.CrudService;
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
public class TopStockMovementService implements CrudService<ISTopStockMovement, Integer> {

    public static Map fileMap = new HashMap();
    @Autowired
    public ISTopStoreRepository topStoreRepository;
    List<StoreReportDTO> reportDTOs = new ArrayList();
    MaterialTypeStoresReportDTO materialTypeStoresReportDTO = null;
    List<String> materialNames = null;
    @Autowired
    private ISTopStockMovementRepository topStockMovementRepository;
    @Autowired
    private MaterialItemRepository materialItemRepository;
    @Autowired
    private MaterialTypeRepository materialTypeRepository;
    @Autowired
    private MachineItemRepository machineItemRepository;
    @Autowired
    private ISStockReceiveItemRepository topStockReceivedRepository;
    @Autowired
    private ISTStockIssueItemRepository topStockIssuedRepository;
    @Autowired
    private ISStockReceiveRepository stockReceiveRepository;
    @Autowired
    private ISStockIssueRepository stockIssueRepository;
    @Autowired
    private ISLoanRepository loanRepository;
    @Autowired
    private StockMovementPredicateBuilder stockMovementPredicateBuilder;
    @Autowired
    private ISLoanIssueItemRepository loanIssueItemRepository;
    @Autowired
    private ISLoanReceiveItemRepository loanReceiveItemRepository;
    @Autowired
    private ISLoanReturnItemIssuedRepository loanReturnItemIssuedRepository;
    @Autowired
    private ISLoanReturnItemReceivedRepository loanReturnItemReceivedRepository;
    @Autowired
    private ExportService exportService;
    @Autowired
    private ISTopInventoryRepository isTopInventoryRepository;
    @Autowired
    private ISStockReturnRepository stockReturnRepository;
    @Autowired
    private ISStockReturnItemRepository stockReturnItemRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private PersonRepository personRepository;

    @Override
    @Transactional(readOnly = false)
    public ISTopStockMovement create(ISTopStockMovement stockMovement) {
        return topStockMovementRepository.save(stockMovement);
    }

    @Override
    @Transactional(readOnly = false)
    public ISTopStockMovement update(ISTopStockMovement stockMovement) {
        return topStockMovementRepository.save(stockMovement);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        topStockMovementRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ISTopStockMovement get(Integer id) {
        return topStockMovementRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ISTopStockMovement> getAll() {
        return topStockMovementRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ISTopStockMovement> getStockMovementByItemAndStore(Integer boqItem, Integer storeId) {
        ISTopStore topStore = topStoreRepository.findOne(storeId);
        return topStockMovementRepository.findByItemAndStore(boqItem, topStore);
    }

    @Transactional(readOnly = true)
    public List<ISTopStockMovement> getStockMovementByItem(Integer itemId) {
        List<ISTopStockMovement> stockMovements = topStockMovementRepository.findByItem(itemId);
        for (ISTopStockMovement stockMovement : stockMovements) {
            if (stockMovement.getMovementType() == MovementType.RECEIVED) {
                ISStockReceiveItem receiveItem = topStockReceivedRepository.findOne(stockMovement.getId());
                if (receiveItem != null) {
                    ISStockReceive receive = stockReceiveRepository.findOne(receiveItem.getReceive());
                    if (receive != null) {
                        stockMovement.setReference(receive.getReceiveNumberSource());
                    }
                }
            } else if (stockMovement.getMovementType() == MovementType.ISSUED) {
                ISTStockIssueItem stockIssueItem = topStockIssuedRepository.findOne(stockMovement.getId());
                if (stockIssueItem != null) {
                    ISStockIssue issue = stockIssueRepository.findOne(stockIssueItem.getIssue());
                    if (issue != null) {
                        stockMovement.setReference(issue.getIssueNumberSource());
                    }
                }
            } else if (stockMovement.getMovementType() == MovementType.LOANISSUED) {
                ISLoanIssueItem isLoanIssueItem = loanIssueItemRepository.findOne(stockMovement.getId());
                if (isLoanIssueItem != null) {
                    ISLoan loan = loanRepository.findOne(isLoanIssueItem.getLoan());
                    if (loan != null) {
                        stockMovement.setReference(loan.getLoanNumber());
                    }
                }
            } else if (stockMovement.getMovementType() == MovementType.LOANRECEIVED) {
                ISLoanReceiveItem loanReceiveItem = loanReceiveItemRepository.findOne(stockMovement.getId());
                if (loanReceiveItem != null) {
                    ISLoan loan = loanRepository.findOne(loanReceiveItem.getLoan());
                    if (loan != null) {
                        stockMovement.setReference(loan.getLoanNumber());
                    }
                }

            } else if (stockMovement.getMovementType() == MovementType.LOANRETURNITEMISSUED) {
                ISLoanReturnItemIssued loanReturnItemIssued = loanReturnItemIssuedRepository.findOne(stockMovement.getId());
                if (loanReturnItemIssued != null) {
                    ISLoan loan = loanRepository.findOne(loanReturnItemIssued.getLoan());
                    if (loan != null) {
                        stockMovement.setReference(loan.getLoanNumber());
                    }
                }
            } else if (stockMovement.getMovementType() == MovementType.LOANRETURNITEMRECEIVED) {
                ISLoanReturnItemReceived loanReturnItemReceived = loanReturnItemReceivedRepository.findOne(stockMovement.getId());
                if (loanReturnItemReceived != null) {
                    ISLoan loan = loanRepository.findOne(loanReturnItemReceived.getLoan());
                    if (loan != null) {
                        stockMovement.setReference(loan.getLoanNumber());
                    }
                }
            } else if (stockMovement.getMovementType() == MovementType.RETURNED) {
                ISStockReturnItem stockReturnItem = stockReturnItemRepository.findOne(stockMovement.getId());
                if (stockReturnItem != null) {
                    ISStockReturn stockReturn = stockReturnRepository.findOne(stockReturnItem.getStockReturn());
                    if (stockReturn != null) {
                        stockMovement.setReference(stockReturn.getReturnNumberSource());
                    }
                }
            }

        }
        return stockMovements;
    }

    public List<ISTopStockMovement> getStockMoveByStore(ISTopStore store) {
        return topStockMovementRepository.findByStore(store);
    }

    @Transactional(readOnly = true)
    public List<ISTopStockMovement> getStockMovementByProjectItemNum(String itemNumber, Integer projectId) {
        ISMachineItem machineItem = machineItemRepository.findByItemNumber(itemNumber);
        ISMaterialItem materialItem = materialItemRepository.findByItemNumber(itemNumber);
        List<ISTopStockMovement> topStockMovements = new ArrayList<>();
        if (machineItem != null) {
            topStockMovements = topStockMovementRepository.findByItemAndProject(machineItem.getId(), projectId);
        } else {
            topStockMovements = topStockMovementRepository.findByItemAndProject(materialItem.getId(), projectId);
        }
        return topStockMovements;
    }

    @Transactional(readOnly = true)
    public List<ISTopStockMovement> getStockMovementByBomItemAndItemNum(String itemNumber, Integer bomItem) {
        List<ISTopStockMovement> topStockMovements = topStockMovementRepository.findByItem(bomItem);
        return topStockMovements;
    }

    @Transactional(readOnly = true)
    public Page<ISTopStockMovement> getStockMovementByStore(Integer storeId, Pageable pageable) {
        Page<ISTopStockMovement> stockMovements = topStockMovementRepository.findByStoreOrderByTimeStampDesc(topStoreRepository.findOne(storeId), pageable);
        for (ISTopStockMovement stockMovement : stockMovements.getContent()) {
            stockMovement.setReference("");
            if (stockMovement.getMovementType() == MovementType.RECEIVED) {
                ISStockReceiveItem receiveItem = topStockReceivedRepository.findOne(stockMovement.getId());
                if (receiveItem != null) {
                    ISStockReceive receive = stockReceiveRepository.findOne(receiveItem.getReceive());
                    if (receive != null) {
                        stockMovement.setReference(receive.getReceiveNumberSource());
                    }
                }
            } else if (stockMovement.getMovementType() == MovementType.ISSUED) {
                ISTStockIssueItem stockIssueItem = topStockIssuedRepository.findOne(stockMovement.getId());
                if (stockIssueItem != null) {
                    ISStockIssue issue = stockIssueRepository.findOne(stockIssueItem.getIssue());
                    if (issue != null) {
                        stockMovement.setReference(issue.getIssueNumberSource());
                    }
                }
            } else if (stockMovement.getMovementType() == MovementType.LOANISSUED) {
                ISLoanIssueItem isLoanIssueItem = loanIssueItemRepository.findOne(stockMovement.getId());
                if (isLoanIssueItem != null) {
                    ISLoan loan = loanRepository.findOne(isLoanIssueItem.getLoan());
                    if (loan != null) {
                        stockMovement.setReference(loan.getLoanNumber());
                    }
                }
            } else if (stockMovement.getMovementType() == MovementType.LOANRECEIVED) {
                ISLoanReceiveItem loanReceiveItem = loanReceiveItemRepository.findOne(stockMovement.getId());
                if (loanReceiveItem != null) {
                    ISLoan loan = loanRepository.findOne(loanReceiveItem.getLoan());
                    if (loan != null) {
                        stockMovement.setReference(loan.getLoanNumber());
                    }
                }

            } else if (stockMovement.getMovementType() == MovementType.LOANRETURNITEMISSUED) {
                ISLoanReturnItemIssued loanReturnItemIssued = loanReturnItemIssuedRepository.findOne(stockMovement.getId());
                if (loanReturnItemIssued != null) {
                    ISLoan loan = loanRepository.findOne(loanReturnItemIssued.getLoan());
                    if (loan != null) {
                        stockMovement.setReference(loan.getLoanNumber());
                    }
                }
            } else if (stockMovement.getMovementType() == MovementType.LOANRETURNITEMRECEIVED) {
                ISLoanReturnItemReceived loanReturnItemReceived = loanReturnItemReceivedRepository.findOne(stockMovement.getId());
                if (loanReturnItemReceived != null) {
                    ISLoan loan = loanRepository.findOne(loanReturnItemReceived.getLoan());
                    if (loan != null) {
                        stockMovement.setReference(loan.getLoanNumber());
                    }
                }
            } else if (stockMovement.getMovementType() == MovementType.RETURNED) {
                ISStockReturnItem stockReturnItem = stockReturnItemRepository.findOne(stockMovement.getId());
                if (stockReturnItem != null) {
                    ISStockReturn stockReturn = stockReturnRepository.findOne(stockReturnItem.getStockReturn());
                    if (stockReturn != null) {
                        stockMovement.setReference(stockReturn.getReturnNumberSource());
                    }
                }
            }
            ISMachineItem machineItem = machineItemRepository.findOne(stockMovement.getItem());
            ISMaterialItem materialItem = materialItemRepository.findOne(stockMovement.getItem());
            ItemDTO itemDTO = new ItemDTO();
            if (machineItem != null) {
                itemDTO.setItemNumber(machineItem.getItemNumber());
                itemDTO.setId(machineItem.getId());
                itemDTO.setDescription(machineItem.getDescription());
                itemDTO.setItemName(machineItem.getItemName());
                itemDTO.setItemType(machineItem.getItemType().getName());
                itemDTO.setResourceType("MACHINETYPE");
            } else {
                itemDTO.setItemNumber(materialItem.getItemNumber());
                itemDTO.setId(materialItem.getId());
                itemDTO.setDescription(materialItem.getDescription());
                itemDTO.setItemName(materialItem.getItemName());
                itemDTO.setUnits(materialItem.getUnits());
                itemDTO.setItemType(materialItem.getItemType().getName());
                itemDTO.setResourceType("MATERIALTYPE");
            }
            stockMovement.setItemDTO(itemDTO);
        }
        return stockMovements;
    }

    @Transactional(readOnly = true)
    public Page<ISTopStockMovement> getPageableStockMovementByFilter(StockMovementCriteria stockMovementCriteria, Pageable pageable) {
        List<ISTopStockMovement> topStockMovements = getStockMovementByFilter(stockMovementCriteria);
        int start = pageable.getOffset();
        int end = 0;
        if (topStockMovements != null) {
            end = (start + pageable.getPageSize()) > topStockMovements.size() ? topStockMovements.size() : (start + pageable.getPageSize());
            return new PageImpl<ISTopStockMovement>(topStockMovements.subList(start, end), pageable, topStockMovements.size());
        } else {
            return null;
        }
    }

    public List<ISTopStockMovement> getStockMovementByFilter(StockMovementCriteria stockMovementCriteria) {
        Date startDate2 = null;
        Date endDate2 = null;
        List<ObjectTypeAttribute> issueObjectTypeAttributes = objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.ISSUE);
        List<ObjectTypeAttribute> issueTypeAttributes = objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.valueOf("MATERIALISSUETYPE"));
        issueObjectTypeAttributes.addAll(issueTypeAttributes);
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
                ISMachineItem machineItem = machineItemRepository.findOne(stockMovement.getItem());
                ISMaterialItem materialItem = materialItemRepository.findOne(stockMovement.getItem());
                ItemDTO itemDTO = new ItemDTO();
                stockMovement.setItemAttributesList(new ArrayList<>());
                if (stockMovement.getMovementType() == MovementType.RECEIVED) {
                    ISStockReceiveItem receiveItem = topStockReceivedRepository.findOne(stockMovement.getId());
                    if (receiveItem != null) {
                        ISStockReceive receive = stockReceiveRepository.findOne(receiveItem.getReceive());
                        if (receive != null) {
                            stockMovement.setReference(receive.getReceiveNumberSource());
                            if (receive.getSupplier() != null) {
                                ISSupplier supplier = supplierRepository.findOne(receive.getSupplier());
                                if (supplier != null) {
                                    itemDTO.setSupplierName(supplier.getName());
                                }
                            }
                        }
                    }
                } else if (stockMovement.getMovementType() == MovementType.ISSUED) {
                    ISTStockIssueItem stockIssueItem = topStockIssuedRepository.findOne(stockMovement.getId());
                    if (stockIssueItem != null) {
                        ISStockIssue issue = stockIssueRepository.findOne(stockIssueItem.getIssue());
                        if (issue != null) {
                            stockMovement.setReference(issue.getIssueNumberSource());
                            Person person = personRepository.findOne(issue.getIssuedTo());
                            if (person != null) {
                                itemDTO.setIssuedToName(person.getFullName());
                            }
                        }
                        for (ObjectTypeAttribute objectTypeAttribute : issueObjectTypeAttributes) {
                            ObjectAtrributeDTO objectAtrributeDTO = new ObjectAtrributeDTO();
                            if (!((objectTypeAttribute.getDataType().equals(DataType.ATTACHMENT)) || (objectTypeAttribute.getDataType().equals(DataType.IMAGE)))) {
                                objectAtrributeDTO.setObjectAttribute(objectAttributeRepository.findByObjectIdAndAttributeDefId(issue.getId(), objectTypeAttribute.getId()));
                                objectAtrributeDTO.setObjectTypeAttribute(objectTypeAttribute);
                                stockMovement.getItemAttributesList().add(objectAtrributeDTO);
                            }

                        }
                    }
                } else if (stockMovement.getMovementType() == MovementType.LOANISSUED) {
                    ISLoanIssueItem isLoanIssueItem = loanIssueItemRepository.findOne(stockMovement.getId());
                    if (isLoanIssueItem != null) {
                        ISLoan loan = loanRepository.findOne(isLoanIssueItem.getLoan());
                        if (loan != null) {
                            stockMovement.setReference(loan.getLoanNumber());
                        }
                    }
                } else if (stockMovement.getMovementType() == MovementType.LOANRECEIVED) {
                    ISLoanReceiveItem loanReceiveItem = loanReceiveItemRepository.findOne(stockMovement.getId());
                    if (loanReceiveItem != null) {
                        ISLoan loan = loanRepository.findOne(loanReceiveItem.getLoan());
                        if (loan != null) {
                            stockMovement.setReference(loan.getLoanNumber());
                        }
                    }

                } else if (stockMovement.getMovementType() == MovementType.LOANRETURNITEMISSUED) {
                    ISLoanReturnItemIssued loanReturnItemIssued = loanReturnItemIssuedRepository.findOne(stockMovement.getId());
                    if (loanReturnItemIssued != null) {
                        ISLoan loan = loanRepository.findOne(loanReturnItemIssued.getLoan());
                        if (loan != null) {
                            stockMovement.setReference(loan.getLoanNumber());
                        }
                    }
                } else if (stockMovement.getMovementType() == MovementType.LOANRETURNITEMRECEIVED) {
                    ISLoanReturnItemReceived loanReturnItemReceived = loanReturnItemReceivedRepository.findOne(stockMovement.getId());
                    if (loanReturnItemReceived != null) {
                        ISLoan loan = loanRepository.findOne(loanReturnItemReceived.getLoan());
                        if (loan != null) {
                            stockMovement.setReference(loan.getLoanNumber());
                        }
                    }
                } else if (stockMovement.getMovementType() == MovementType.RETURNED) {
                    ISStockReturnItem stockReturnItem = stockReturnItemRepository.findOne(stockMovement.getId());
                    if (stockReturnItem != null) {
                        ISStockReturn stockReturn = stockReturnRepository.findOne(stockReturnItem.getStockReturn());
                        if (stockReturn != null) {
                            stockMovement.setReference(stockReturn.getReturnNumberSource());
                        }
                    }
                } else {
                    stockMovement.setReference("");
                }
                if (machineItem != null) {
                    itemDTO.setItemNumber(machineItem.getItemNumber());
                    itemDTO.setId(machineItem.getId());
                    itemDTO.setDescription(machineItem.getDescription());
                    itemDTO.setItemName(machineItem.getItemName());
                    itemDTO.setItemType(machineItem.getItemType().getName());
                    itemDTO.setResourceType("MACHINETYPE");
                } else {
                    itemDTO.setItemNumber(materialItem.getItemNumber());
                    itemDTO.setId(materialItem.getId());
                    itemDTO.setDescription(materialItem.getDescription());
                    itemDTO.setItemName(materialItem.getItemName());
                    itemDTO.setItemType(materialItem.getItemType().getName());
                    itemDTO.setUnits(materialItem.getUnits());
                    itemDTO.setResourceType("MATERIALTYPE");
                    itemDTO.setTimeStamp(stockMovement.getTimeStamp());
                }
                stockMovement.setItemDTO(itemDTO);
            }
        } else {
            return null;
        }
        return topStockMovements;

    }

    @Transactional(readOnly = true)
    public List<ISTopStockMovement> getStockMovementsByReceiveId(Integer receiveId) {
        List<ISStockReceiveItem> receiveItems = topStockReceivedRepository.findByReceive(receiveId);
        List<ISTopStockMovement> stockMovements = new ArrayList<>();
        List<Integer> rowIds = new ArrayList<>();
        for (ISStockReceiveItem receiveItem : receiveItems) {
            rowIds.add(receiveItem.getId());
        }
        Map<Integer, ISTopStockMovement> hashmap = new HashMap<>();
        List<ISTopStockMovement> topStockMovements = topStockMovementRepository.findAll(rowIds);
        for (ISTopStockMovement topStockMovement : topStockMovements) {
            ISTopStockMovement topStockMovement1 = hashmap.get(topStockMovement.getItem());
            if (topStockMovement1 != null) {
                topStockMovement1.setQuantity(topStockMovement.getQuantity() + topStockMovement1.getQuantity());
            } else {
                hashmap.put(topStockMovement.getItem(), topStockMovement);
                stockMovements.add(topStockMovement);
            }
        }
        return stockMovements;
    }

    @Transactional(readOnly = true)
    public MaterialTypeStoresReportDTO getReportByDates(String startDate1, String endDate1) {
        reportDTOs.clear();
        materialTypeStoresReportDTO = new MaterialTypeStoresReportDTO();
        List<ISMaterialType> materialTypes = materialTypeRepository.findAll();
        materialNames = materialTypes.stream().map(ISMaterialType::getName).collect(Collectors.toList());
        Date startDate2 = null;
        if (startDate1.equals("undefined") || startDate1.equals("null") || startDate1.trim().equals("")) {
            startDate2 = new Date();
        } else {
            startDate2 = stringToDate(startDate1);
        }
        Date startDate = new Date(startDate2.getTime() - (1000 * 60));
        Date endDate = new Date(stringToDate(endDate1).getTime() + (1000 * 60 * 60 * 24));
        List<ISTopStockMovement> topStockMovements =
                topStockMovementRepository.findByTimeStampAfterAndTimeStampBefore(startDate, endDate);
        Set<ISTopStore> stores = topStockMovements.stream().map(ISTopStockMovement::getStore).collect(Collectors.toSet());
        List<String> storeNames = stores.stream().map(ISTopStore::getStoreName).collect(Collectors.toList());
        Map<String, List<StoresReportDTO>> materialItemMap = new HashMap();
        for (ISMaterialType materialType : materialTypes) {
            List<StoresReportDTO> storesReportDTOs = new ArrayList();
            List<ISMaterialItem> materialItems = materialItemRepository.findByItemType(materialType);
            for (int j = 0; j < materialItems.size(); j++) {
                List<Double> storesQty = new ArrayList();
                ISMaterialItem item = materialItems.get(j);
                for (ISTopStore store : stores) {
                    Double onHand = 0.0;
                    List<ISTopInventory> topInventories = isTopInventoryRepository.findByItemAndStore(item.getId(), store);
                    if (topInventories.size() > 0) {
                        for (ISTopInventory inventory : topInventories) {
                            onHand += inventory.getStoreOnHand();
                        }
                    }
                    storesQty.add(onHand);
                }
                storesReportDTOs.add(new StoresReportDTO(item.getItemNumber(), item.getItemName(), item.getUnits(), storesQty,
                        storesQty.stream().mapToDouble(i -> i.intValue()).sum()));
            }
            materialItemMap.put(materialType.getName(), storesReportDTOs);
        }
        materialTypeStoresReportDTO.setHeaders(storeNames);
        materialTypeStoresReportDTO.setIsMaterialTypeListMap(materialItemMap);
        return materialTypeStoresReportDTO;
    }

    private Date stringToDate(String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public String exportTaskReport(String fileType, HttpServletResponse response, ReportCriteria criteria) {
        List<String> columns = new ArrayList<String>(Arrays.asList("Item No", "Item", "Units", "Total Qty"));
        MaterialTypeStoresReportDTO materialTypeStoresReportDTO = getReportByDates(criteria.getFromDate(), criteria.getToDate());
        columns.addAll(3, materialTypeStoresReportDTO.getHeaders());
        Export export = new Export();
        export.setFileName("Stock-Report");
        export.setHeaders(columns);
        createExportObject(columns, export);
        return exportService.exportFile(fileType, export, response);
    }

    @Transactional(readOnly = false)
    private void createExportObject(List<String> columns, Export export) {
        List<ExportRow> exportRows = new ArrayList();
        List<String> stores = materialTypeStoresReportDTO.getHeaders();
        Map<String, List<StoresReportDTO>> map = materialTypeStoresReportDTO.getIsMaterialTypeListMap();
        for (int k = 0; k < materialNames.size(); k++) {
            String name = materialNames.get(k);
            List<ExportRowDetail> rowDetails = new ArrayList();
            ExportRow exportRowM = new ExportRow();
            ExportRowDetail rowDetailM = new ExportRowDetail();
            rowDetailM.setColumnName("");
            rowDetailM.setColumnType("String");
            rowDetailM.setColumnValue(String.valueOf(k + 1));
            rowDetails.add(rowDetailM);
            ExportRowDetail rowDetail2 = new ExportRowDetail();
            rowDetail2.setColumnName("");
            rowDetail2.setColumnType("String");
            rowDetail2.setColumnValue(name.toUpperCase());
            rowDetails.add(rowDetail2);
            exportRowM.setExportRowDetails(rowDetails);
            exportRows.add(exportRowM);
            for (StoresReportDTO row : map.get(name)) {
                ExportRow exportRow1 = new ExportRow();
                List<ExportRowDetail> rowDetailsD = new ArrayList();
                for (int i = 0; i < columns.size(); i++) {
                    ExportRowDetail rowDetail = new ExportRowDetail();
                    String column = columns.get(i);
                    rowDetail.setColumnName(column);
                    if (column.equals("Item No")) {
                        rowDetail.setColumnType("Integer");
                        rowDetail.setColumnValue(row.getItemNumber());
                    } else if (column.equals("Item")) {
                        rowDetail.setColumnType("String");
                        rowDetail.setColumnValue("    " + row.getItem());
                    } else if (column.equals("Units")) {
                        rowDetail.setColumnType("String");
                        rowDetail.setColumnValue(row.getUnit());
                    } else if (column.equals("Total Qty")) {
                        rowDetail.setColumnType("Integer");
                        rowDetail.setColumnValue(row.getTotalQty().toString());
                    }
                    for (int j = 0; j < stores.size(); j++) {
                        if (column.equals(stores.get(j))) {
                            rowDetail.setColumnType("Integer");
                            rowDetail.setColumnValue(row.getStoresQty().get(j).toString());
                        }
                    }
                    rowDetailsD.add(rowDetail);
                }
                exportRow1.setExportRowDetails(rowDetailsD);
                exportRows.add(exportRow1);
            }
        }
        export.setExportRows(exportRows);
    }

    public List<StockReceiveItemsDTO> getOpeningAndClosingBalReport(StockMovementCriteria criteria) {
        Date startDate2 = null;
        Date endDate = null;
        TypedQuery<ISTopStockMovement> typedQuery = stockMovementPredicateBuilder.getItemTypeQuery(criteria);
        List<ISTopStockMovement> topStockMovements = typedQuery.getResultList();
        List<Integer> stockIds = topStockMovements.stream().map(ISTopStockMovement::getId).collect(Collectors.toList());
        List<Integer> itemIds = topStockMovements.stream().map(ISTopStockMovement::getItem).collect(Collectors.toList());
        itemIds = itemIds.stream().distinct().collect(Collectors.toList());
        if (criteria.getFromDate().equals("undefined") || criteria.getFromDate().equals("null") || criteria.getFromDate().trim().equals("")) {
            startDate2 = topStockMovementRepository.getMinimumDate(criteria.getStoreId());
        } else {
            startDate2 = stringToDate(criteria.getFromDate());
        }
        Date startDate = new Date(startDate2.getTime() + (1000 * 60 * 60 * 24));
        if (criteria.getToDate() == null) {
            endDate = topStockMovementRepository.getMaximumDate(criteria.getStoreId());
        }
        endDate = new Date(stringToDate(criteria.getToDate()).getTime() + (1000 * 60 * 60 * 24));
        //List<Integer> itemIds = isTopInventoryRepository.findDistinctStoreItems(criteria.getStoreId());
        List<StockReceiveItemsDTO> stockReceiveItemsDTOs = new ArrayList<>();
        for (Integer itemId : itemIds) {
            ISMaterialItem materialItem = materialItemRepository.findOne(itemId);
            if (materialItem != null) {
                StockReceiveItemsDTO stockReceiveItemsDTO = new StockReceiveItemsDTO();
                stockReceiveItemsDTO.setItemNumber(materialItem.getItemNumber());
                stockReceiveItemsDTO.setItemName(materialItem.getItemName());
                stockReceiveItemsDTO.setUnits(materialItem.getUnits());
                List<ISTopStockMovement> stockMovements = topStockMovementRepository.findByIdInAndItemAndStoreAndTimeStampBeforeOrderByTimeStampDesc(stockIds, itemId, topStoreRepository.findOne(criteria.getStoreId()), startDate);
                if (stockMovements.size() > 0) {
                    if (stockMovements.size() == 1) {
                        stockReceiveItemsDTO.setOpeningBalance(stockMovements.get(0).getOpeningBalance());
                    } else {
                        stockReceiveItemsDTO.setOpeningBalance(stockMovements.get(0).getClosingBalance());
                    }
                } else {
                    stockReceiveItemsDTO.setOpeningBalance(0.0);
                }
                List<ISTopStockMovement> stockMovementList = topStockMovementRepository.findByIdInAndItemAndStoreAndTimeStampBeforeOrderByTimeStampDesc(stockIds, itemId, topStoreRepository.findOne(criteria.getStoreId()), endDate);
                if (stockMovementList.size() > 0) {
                    if (stockMovementList.size() == 1) {
                        stockReceiveItemsDTO.setClosingBalance(stockMovementList.get(0).getClosingBalance());
                    } else {
                        stockReceiveItemsDTO.setClosingBalance(stockMovementList.get(0).getOpeningBalance());
                    }
                } else {
                    stockReceiveItemsDTO.setClosingBalance(0.0);
                }
                stockReceiveItemsDTOs.add(stockReceiveItemsDTO);
            }
        }
        return stockReceiveItemsDTOs;
    }

    @Transactional(
            readOnly = false
    )
    public String exportStockMovements(StockMovementCriteria criteria, String fileType, HttpServletResponse response) {
        ByteArrayInputStream is = null;
        String fileId = null;
        Export export = new Export();
        if (criteria.getMovementType().equals(MovementType.RECEIVED)) {
            export.setFileName("ReceiveItems-Report");
        } else if (criteria.getMovementType().equals(MovementType.ISSUED)) {
            export.setFileName("IssueItems-Report");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dt = new Date();
        if (fileType != null && fileType.equalsIgnoreCase("excel")) {
            fileId = export.getFileName() + ".xls";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + export.getFileName() + "\"");
            response.setContentType("application/vnd.ms-excel");
            HSSFWorkbook e3 = new HSSFWorkbook();
            try {
                this.buildExcelDocument(criteria, export, e3, response);
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
    public void buildExcelDocument(StockMovementCriteria criteria, Export export, Workbook workbook, HttpServletResponse response) throws Exception {
        Date startDate2 = null;
        String fromDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        if (criteria.getFromDate() == null || criteria.getFromDate().equals("undefined") || criteria.getFromDate().equals("null") || criteria.getFromDate().trim().equals("")) {
            if (criteria.getMovementType().equals(MovementType.ISSUED)) {
                startDate2 = topStockIssuedRepository.getMinimumDate(criteria.getStoreId());
            } else if (criteria.getMovementType().equals(MovementType.RECEIVED)) {
                startDate2 = topStockReceivedRepository.getMinimumDate(criteria.getStoreId());
            }
            if (startDate2 == null) {
                startDate2 = new Date();
            }
        } else {
            startDate2 = stringToDate(criteria.getFromDate());
        }
        fromDate = formatter.format(startDate2);
        List<String> headers = new ArrayList();
        if (criteria.getMovementType().equals(MovementType.ISSUED)) {
            headers = new ArrayList<>(Arrays.asList("Date", "Item Number", "Item Name", "Units", "Issued Qty", "Issued To", "Opening Bal as on " + fromDate, "Closing Bal as on " + criteria.getToDate()));
        } else if (criteria.getMovementType().equals(MovementType.RECEIVED)) {
            headers = new ArrayList<>(Arrays.asList("Date", "Item Number", "Item Name", "Units", "Received Qty", "Supplier Name", "Opening Bal as on " + fromDate, "Closing Bal as on " + criteria.getToDate()));
        }
        List<ExportRow> exportRows = new ArrayList();
        Sheet sheet = null;
        if (criteria.getMovementType().equals(MovementType.RECEIVED)) {
            sheet = workbook.createSheet("ReceiveItems-Report");
        } else if (criteria.getMovementType().equals(MovementType.ISSUED)) {
            sheet = workbook.createSheet("IssueItems-Report");
        }
        sheet.setDefaultColumnWidth(30);
        List<ISTopStockMovement> stockMovementList = getStockMovementByFilter(criteria);
        for (ISTopStockMovement stockMovement : stockMovementList) {
            List<ExportRowDetail> rowDetails = new ArrayList();
            ExportRow exportRow = new ExportRow();
            ExportRowDetail rowDetail1 = new ExportRowDetail();
            rowDetail1.setColumnName("Date");
            rowDetail1.setColumnType("String");
            rowDetail1.setColumnValue(stockMovement.getTimeStamp().toString());
            ExportRowDetail rowDetail2 = new ExportRowDetail();
            rowDetail2.setColumnName("Item Number");
            rowDetail2.setColumnType("String");
            rowDetail2.setColumnValue(stockMovement.getItemDTO().getItemNumber());
            ExportRowDetail rowDetail3 = new ExportRowDetail();
            rowDetail3.setColumnName("Item Name");
            rowDetail3.setColumnType("String");
            rowDetail3.setColumnValue(stockMovement.getItemDTO().getItemName());
            ExportRowDetail rowDetail4 = new ExportRowDetail();
            rowDetail4.setColumnName("Units");
            rowDetail4.setColumnType("String");
            rowDetail4.setColumnValue(stockMovement.getItemDTO().getUnits());
            ExportRowDetail rowDetail5 = new ExportRowDetail();
            rowDetail5.setColumnName("Qty");
            rowDetail5.setColumnType("Integer");
            rowDetail5.setColumnValue(stockMovement.getQuantity().toString());
            ExportRowDetail rowDetail6 = new ExportRowDetail();
            if (stockMovement.getMovementType().equals(MovementType.ISSUED)) {
                rowDetail6.setColumnName("Issued To");
                rowDetail6.setColumnValue(stockMovement.getItemDTO().getIssuedToName());
                rowDetail6.setColumnType("String");
            } else if (stockMovement.getMovementType().equals(MovementType.RECEIVED)) {
                rowDetail6.setColumnName("Supplier Name");
                rowDetail6.setColumnValue(stockMovement.getItemDTO().getSupplierName());
                rowDetail6.setColumnType("String");
            }
            ExportRowDetail rowDetail7 = new ExportRowDetail();
            rowDetail7.setColumnName("Opening Qty");
            rowDetail7.setColumnType("Integer");
            rowDetail7.setColumnValue(stockMovement.getOpeningBalance().toString());
            ExportRowDetail rowDetail8 = new ExportRowDetail();
            rowDetail8.setColumnName("Closing Qty");
            rowDetail8.setColumnType("Integer");
            rowDetail8.setColumnValue(stockMovement.getClosingBalance().toString());
            rowDetails.add(rowDetail1);
            rowDetails.add(rowDetail2);
            rowDetails.add(rowDetail3);
            rowDetails.add(rowDetail4);
            rowDetails.add(rowDetail5);
            rowDetails.add(rowDetail6);
            rowDetails.add(rowDetail7);
            rowDetails.add(rowDetail8);
            for (ObjectAtrributeDTO objectAtrributeDTO : stockMovement.getItemAttributesList()) {
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
        for (ObjectAtrributeDTO objectAttribute : stockMovementList.get(0).getItemAttributesList()) {
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

}
