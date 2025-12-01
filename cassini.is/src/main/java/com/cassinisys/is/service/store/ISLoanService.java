package com.cassinisys.is.service.store;

import com.cassinisys.is.filtering.ISLoanCriteria;
import com.cassinisys.is.filtering.ISLoanPredicateBuilder;
import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.procm.ISMachineItem;
import com.cassinisys.is.model.procm.ISMaterialItem;
import com.cassinisys.is.model.procm.MovementType;
import com.cassinisys.is.model.store.*;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.is.repo.procm.MachineItemRepository;
import com.cassinisys.is.repo.procm.MaterialItemRepository;
import com.cassinisys.is.repo.store.*;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.mysema.query.types.Predicate;
import freemarker.template.Configuration;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
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

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by swapna on 10/08/18.
 */
@Service
public class ISLoanService {

    public static Map fileMap = new HashMap();
    @Autowired
    Configuration fmConfiguration;
    @Autowired
    private ISLoanRepository loanRepository;
    @Autowired
    private ISLoanIssueItemRepository loanIssueItemRepository;
    @Autowired
    private ISLoanReceiveItemRepository loanReceiveItemRepository;
    @Autowired
    private ISTopInventoryRepository topInventoryRepository;
    @Autowired
    private ISTopStoreRepository storeRepository;
    @Autowired
    private MaterialItemRepository materialItemRepository;
    @Autowired
    private MachineItemRepository machineItemRepository;
    @Autowired
    private ISLoanReturnItemIssuedRepository loanReturnItemIssuedRepository;
    @Autowired
    private ISLoanReturnItemReceivedRepository loanReturnItemReceivedRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ISTopStoreRepository isTopStoreRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ISLoanPredicateBuilder loanPredicateBuilder;

    @Transactional(readOnly = false)
    public ISLoan createLoan(ISLoan isLoan) {
        try {
            if (isLoan.getLoanNumber() == null) {
                AutoNumber autoNumber = autoNumberService.getByName("Default Loan Number Source");
                String number = autoNumberService.getNextNumber(autoNumber.getId());
                isLoan.setLoanNumber(number);
            }
            isLoan = loanRepository.save(isLoan);
            List<ISLoanIssueItem> loanIssueItems = isLoan.getLoanIssueItems();
            for (ISLoanIssueItem loanIssueItem : loanIssueItems) {
                loanIssueItem.setProject(isLoan.getFromProject());
                loanIssueItem.setLoan(isLoan.getId());
                loanIssueItem.setStore(storeRepository.findOne(isLoan.getFromStore()));
                loanIssueItem.setTimeStamp(new Date());
            }
            createLoanIssueItems(isLoan.getFromStore(), isLoan.getToStore(), isLoan.getToProject(), isLoan.getLoanIssueItems());

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return isLoan;
    }

    @Transactional(readOnly = true)
    public Page<ISLoan> getAllLoans(Pageable pageable) {
        return loanRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<ISLoan> getLoansIssuedByStore(Integer storeId) {
        return loanRepository.findByFromStore(storeId);
    }

    @Transactional(readOnly = true)
    public List<ISLoan> getLoansReceivedByStore(Integer storeId) {
        return loanRepository.findByToStore(storeId);
    }

    @Transactional(readOnly = true)
    public ISLoan getLoanById(Integer loanId) {
        return loanRepository.findOne(loanId);
    }

    @Transactional(readOnly = false)
    public ISLoan update(ISLoan loan) {
        return loanRepository.save(loan);
    }

    @Transactional(readOnly = false)
    public void createLoanIssueItems(Integer fromStore, Integer toStore, Integer toProject, List<ISLoanIssueItem> loanIssueItems) {
        List<ISLoanReceiveItem> loanReceiveItems = new ArrayList<>();
        for (ISLoanIssueItem loanIssueItem : loanIssueItems) {
            Double openingBal = 0.0;
            ISLoanReceiveItem loanReceiveItem = new ISLoanReceiveItem();
            loanReceiveItem.setStore(storeRepository.findOne(toStore));
            loanReceiveItem.setQuantity(loanIssueItem.getQuantity());
            loanReceiveItem.setMovementType(MovementType.LOANRECEIVED);
            loanReceiveItem.setItem(loanIssueItem.getItem());
            loanReceiveItem.setLoan(loanIssueItem.getLoan());
            loanReceiveItem.setRecordedBy(loanIssueItem.getRecordedBy());
            loanReceiveItem.setTimeStamp(loanIssueItem.getTimeStamp());
            loanReceiveItem.setProject(toProject);
            loanReceiveItems.add(loanReceiveItem);
            ISTopInventory isItemInventory = topInventoryRepository.findByStoreAndItemAndProject(loanIssueItem.getStore(), loanIssueItem.getItem(), loanIssueItem.getProject());
            List<ISTopInventory> topInventories = topInventoryRepository.findByItemAndStore(loanIssueItem.getItem(), loanIssueItem.getStore());
            if (topInventories.size() > 0) {
                for (ISTopInventory topInventory : topInventories) {
                    openingBal += topInventory.getStoreOnHand();
                }
                loanIssueItem.setOpeningBalance(openingBal);
                loanIssueItem.setClosingBalance(loanIssueItem.getOpeningBalance() - loanIssueItem.getQuantity());
            } else {
                loanIssueItem.setOpeningBalance(0.0);
                loanIssueItem.setClosingBalance(loanIssueItem.getQuantity());
            }
            if (isItemInventory != null) {
                loanIssueItem.setOpeningBalance(isItemInventory.getStoreOnHand());
                Double qty = isItemInventory.getStoreOnHand();
                qty -= loanIssueItem.getQuantity();
                isItemInventory.setStoreOnHand(qty);
                loanIssueItem.setClosingBalance(qty);
            } else {
                isItemInventory = new ISTopInventory();
                isItemInventory.setStoreOnHand(loanIssueItem.getQuantity());
                isItemInventory.setStore(loanIssueItem.getStore());
                isItemInventory.setItem(loanIssueItem.getItem());
                isItemInventory.setProject(loanIssueItem.getProject());
                loanIssueItem.setOpeningBalance(0.0);
                loanIssueItem.setClosingBalance(loanIssueItem.getQuantity());

            }
            topInventoryRepository.save(isItemInventory);
            ISTopInventory isItemInventory1 = topInventoryRepository.findByStoreAndItemAndProject(loanReceiveItem.getStore(), loanReceiveItem.getItem(), loanReceiveItem.getProject());
            openingBal = 0.0;
            List<ISTopInventory> topInventories1 = topInventoryRepository.findByItemAndStore(loanReceiveItem.getItem(), loanReceiveItem.getStore());
            if (topInventories1.size() > 0) {
                for (ISTopInventory topInventory : topInventories1) {
                    openingBal += topInventory.getStoreOnHand();
                }
                loanReceiveItem.setOpeningBalance(openingBal);
                loanReceiveItem.setClosingBalance(loanReceiveItem.getOpeningBalance() + loanReceiveItem.getQuantity());
            } else {
                loanReceiveItem.setOpeningBalance(0.0);
                loanReceiveItem.setClosingBalance(loanReceiveItem.getQuantity());
            }
            if (isItemInventory1 != null) {
                Double qty = isItemInventory1.getStoreOnHand();
                qty += loanReceiveItem.getQuantity();
                isItemInventory1.setStoreOnHand(qty);
            } else {
                isItemInventory1 = new ISTopInventory();
                isItemInventory1.setStoreOnHand(loanReceiveItem.getQuantity());
                isItemInventory1.setStore(loanReceiveItem.getStore());
                isItemInventory1.setItem(loanReceiveItem.getItem());
                isItemInventory1.setProject(loanReceiveItem.getProject());

            }
            topInventoryRepository.save(isItemInventory1);
        }
        loanIssueItemRepository.save(loanIssueItems);
        loanReceiveItemRepository.save(loanReceiveItems);
    }

    @Transactional(readOnly = true)
    public List<ISLoanIssueItem> getLoanItems(Integer loanId) {
        List<ISLoanIssueItem> loanIssueItems = loanIssueItemRepository.findByLoan(loanId);
        List<ISLoanIssueItem> loanIssueItemList = new ArrayList<>();
        Hashtable<Integer, ISLoanIssueItem> hashtable = new Hashtable<>();
        for (ISLoanIssueItem loanIssueItem : loanIssueItems) {
            ISLoanIssueItem loanIssueItem1 = hashtable.get(loanIssueItem.getItem());
            ISMachineItem machineItem = machineItemRepository.findOne(loanIssueItem.getItem());
            ISMaterialItem materialItem = materialItemRepository.findOne(loanIssueItem.getItem());
            ItemDTO itemDTO = new ItemDTO();
            if (loanIssueItem1 == null) {
                hashtable.put(loanIssueItem.getItem(), loanIssueItem);
                if (machineItem != null) {
                    itemDTO.setItemNumber(machineItem.getItemNumber());
                    itemDTO.setId(machineItem.getId());
                    itemDTO.setDescription(machineItem.getDescription());
                    itemDTO.setItemName(machineItem.getItemName());
                    itemDTO.setItemType(machineItem.getItemType().getName());
                    itemDTO.setUnits(machineItem.getUnits());
                    itemDTO.setResourceType("MACHINE");
                    itemDTO.setTimeStamp(loanIssueItem.getTimeStamp());
                } else if (materialItem != null) {
                    itemDTO.setItemNumber(materialItem.getItemNumber());
                    itemDTO.setId(materialItem.getId());
                    itemDTO.setDescription(materialItem.getDescription());
                    itemDTO.setItemName(materialItem.getItemName());
                    itemDTO.setItemType(materialItem.getItemType().getName());
                    itemDTO.setUnits(materialItem.getUnits());
                    itemDTO.setResourceType("MATERIAL");
                    itemDTO.setTimeStamp(loanIssueItem.getTimeStamp());
                }
                loanIssueItem.setItemDTO(itemDTO);
                loanIssueItemList.add(loanIssueItem);
            } else {
                loanIssueItem1.setQuantity(loanIssueItem.getQuantity() + loanIssueItem1.getQuantity());
            }
        }
        return loanIssueItemList;
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> getLoanReturnItemsDetails(Integer storeId, Integer loanId) {
        List<ItemDTO> itemDTOs = new ArrayList<>();
        Hashtable<Integer, ItemDTO> hashtable = new Hashtable<>();
        ISLoan loan = loanRepository.findOne(loanId);
        List<ISLoanIssueItem> loanIssueItems = loanIssueItemRepository.findByLoanAndStoreAndMovementType(loanId, storeRepository.findOne(loan.getFromStore()), MovementType.LOANISSUED);
        for (ISLoanIssueItem loanIssueItem : loanIssueItems) {
            ItemDTO itemDTO1 = hashtable.get(loanIssueItem.getItem());
            List<ISLoanReturnItemIssued> loanReturnItemIssueds = loanReturnItemIssuedRepository.findByLoanAndStoreAndItem(loanId, storeRepository.findOne(loan.getToStore()), loanIssueItem.getItem());
            ISMachineItem machineItem = machineItemRepository.findOne(loanIssueItem.getItem());
            ISMaterialItem materialItem = materialItemRepository.findOne(loanIssueItem.getItem());
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setStoreInventory(0.0);
            itemDTO.setItemReturnQuantity(0.0);
            if (itemDTO1 == null) {
                if (machineItem != null) {
                    itemDTO.setItemNumber(machineItem.getItemNumber());
                    itemDTO.setId(machineItem.getId());
                    itemDTO.setDescription(machineItem.getDescription());
                    itemDTO.setItemName(machineItem.getItemName());
                    itemDTO.setItemType(machineItem.getItemType().getName());
                    itemDTO.setUnits(machineItem.getUnits());
                    itemDTO.setResourceType("MACHINE");
                    itemDTO.setItemIssueQuantity(loanIssueItem.getQuantity());
                    itemDTO.setTimeStamp(loanIssueItem.getTimeStamp());
                } else {
                    itemDTO.setItemNumber(materialItem.getItemNumber());
                    itemDTO.setId(materialItem.getId());
                    itemDTO.setDescription(materialItem.getDescription());
                    itemDTO.setItemName(materialItem.getItemName());
                    itemDTO.setItemType(materialItem.getItemType().getName());
                    itemDTO.setUnits(materialItem.getUnits());
                    itemDTO.setResourceType("MATERIAL");
                    itemDTO.setItemIssueQuantity(loanIssueItem.getQuantity());
                    itemDTO.setTimeStamp(loanIssueItem.getTimeStamp());
                }
                if (loanReturnItemIssueds.size() > 0) {
                    for (ISLoanReturnItemIssued loanReturnItemIssued : loanReturnItemIssueds) {
                        itemDTO.setItemReturnQuantity(itemDTO.getItemReturnQuantity() + loanReturnItemIssued.getQuantity());
                    }
                } else {
                    itemDTO.setItemReturnQuantity(0.0);
                }
                ISTopInventory inventory = topInventoryRepository.findByStoreAndItemAndProject(storeRepository.findOne(storeId), loanIssueItem.getItem(), loan.getToProject());
                if (inventory != null) {
                    itemDTO.setStoreInventory(inventory.getStoreOnHand());
                }
                itemDTOs.add(itemDTO);
                hashtable.put(loanIssueItem.getItem(), itemDTO);
            } else {
                itemDTO1.setItemIssueQuantity(loanIssueItem.getQuantity() + itemDTO1.getItemIssueQuantity());
            }

        }
        return itemDTOs;
    }

    @Transactional(readOnly = false)
    public List<ISLoanReturnItemIssued> createLoanReturnItems(Integer loanId, List<ISLoanReturnItemIssued> loanReturnItemIssueds) {
        ISLoan loan = loanRepository.findOne(loanId);
        List<ISLoanReturnItemReceived> isLoanReturnItemReceivedList = new ArrayList<>();
        Integer count = 0;
        for (ISLoanReturnItemIssued loanReturnItemIssued : loanReturnItemIssueds) {
            Double openingBal = 0.0;
            ISTopInventory isItemInventory = topInventoryRepository.findByStoreAndItemAndProject(storeRepository.findOne(loan.getToStore()), loanReturnItemIssued.getItem(), loan.getToProject());
            List<ISTopInventory> topInventories = topInventoryRepository.findByItemAndStore(loanReturnItemIssued.getItem(), loanReturnItemIssued.getStore());
            if (topInventories.size() > 0) {
                for (ISTopInventory topInventory : topInventories) {
                    openingBal += topInventory.getStoreOnHand();
                }
                loanReturnItemIssued.setOpeningBalance(openingBal);
                loanReturnItemIssued.setClosingBalance(loanReturnItemIssued.getOpeningBalance() - loanReturnItemIssued.getQuantity());
            } else {
                loanReturnItemIssued.setOpeningBalance(0.0);
                loanReturnItemIssued.setClosingBalance(loanReturnItemIssued.getQuantity());
            }
            if (isItemInventory != null) {
                Double qty = isItemInventory.getStoreOnHand();
                qty -= loanReturnItemIssued.getQuantity();
                isItemInventory.setStoreOnHand(qty);
            } else {
                isItemInventory = new ISTopInventory();
                isItemInventory.setStoreOnHand(loanReturnItemIssued.getQuantity());
                isItemInventory.setStore(loanReturnItemIssued.getStore());
                isItemInventory.setItem(loanReturnItemIssued.getItem());
                isItemInventory.setProject(loanReturnItemIssued.getProject());
            }
            topInventoryRepository.save(isItemInventory);
        }
        loanReturnItemIssueds = loanReturnItemIssuedRepository.save(loanReturnItemIssueds);
        for (ISLoanReturnItemIssued loanReturnItemIssued : loanReturnItemIssueds) {
            ISLoanReturnItemReceived loanReturnItemReceived = new ISLoanReturnItemReceived();
            loanReturnItemReceived.setItem(loanReturnItemIssued.getItem());
            loanReturnItemReceived.setStore(storeRepository.findOne(loan.getFromStore()));
            loanReturnItemReceived.setProject(loan.getFromProject());
            loanReturnItemReceived.setMovementType(MovementType.LOANRETURNITEMRECEIVED);
            loanReturnItemReceived.setTimeStamp(loanReturnItemIssued.getTimeStamp());
            loanReturnItemReceived.setLoan(loanId);
            loanReturnItemReceived.setQuantity(loanReturnItemIssued.getQuantity());
            loanReturnItemReceived.setRecordedBy(loanReturnItemIssued.getRecordedBy());
            loanReturnItemReceived.setDate(loanReturnItemIssued.getDate());
            isLoanReturnItemReceivedList.add(loanReturnItemReceived);
            ISTopInventory isItemInventory1 = topInventoryRepository.findByStoreAndItemAndProject(storeRepository.findOne(loan.getFromStore()), loanReturnItemIssued.getItem(), loan.getFromProject());
            Double openingBal = 0.0;
            List<ISTopInventory> topInventories1 = topInventoryRepository.findByItemAndStore(loanReturnItemReceived.getItem(), loanReturnItemReceived.getStore());
            if (topInventories1.size() > 0) {
                for (ISTopInventory topInventory : topInventories1) {
                    openingBal += topInventory.getStoreOnHand();
                }
                loanReturnItemReceived.setOpeningBalance(openingBal);
                loanReturnItemReceived.setClosingBalance(loanReturnItemReceived.getOpeningBalance() + loanReturnItemReceived.getQuantity());
            } else {
                loanReturnItemReceived.setOpeningBalance(0.0);
                loanReturnItemReceived.setClosingBalance(loanReturnItemReceived.getQuantity());
            }
            if (isItemInventory1 != null) {
                Double qty = isItemInventory1.getStoreOnHand();
                qty += loanReturnItemIssued.getQuantity();
                isItemInventory1.setStoreOnHand(qty);
            } else {
                isItemInventory1 = new ISTopInventory();
                isItemInventory1.setStoreOnHand(loanReturnItemIssued.getQuantity());
                isItemInventory1.setStore(loanReturnItemIssued.getStore());
                isItemInventory1.setItem(loanReturnItemIssued.getItem());
                isItemInventory1.setProject(loanReturnItemIssued.getProject());

            }
            topInventoryRepository.save(isItemInventory1);
        }
        loanReturnItemReceivedRepository.save(isLoanReturnItemReceivedList);
        // to set status of the loan
        List<ItemDTO> itemDTOs = getLoanReturnItemsDetails(loan.getToStore(), loanId);
        for (ItemDTO itemDTO : itemDTOs) {
            if (!itemDTO.getItemIssueQuantity().equals(itemDTO.getItemReturnQuantity())) {
                count++;
            }
        }
        if (count == 0) {
            loan.setStatus(LoanStatus.RETURNED);
            loanRepository.save(loan);
        }
        return loanReturnItemIssueds;
    }

    @Transactional(readOnly = true)
    public Page<ISLoan> getPagedLoansIssued(Integer storeId, Pageable pageable) {
        List<ISLoan> loanList = loanRepository.findByFromStore(storeId);
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > loanList.size() ? loanList.size() : (start + pageable.getPageSize());
        return new PageImpl<ISLoan>(loanList.subList(start, end), pageable, loanList.size());
    }

    @Transactional(readOnly = true)
    public Page<ISLoan> getPagedLoansReceived(Integer storeId, Pageable pageable) {
        List<ISLoan> loanList = loanRepository.findByToStore(storeId);
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > loanList.size() ? loanList.size() : (start + pageable.getPageSize());
        return new PageImpl<ISLoan>(loanList.subList(start, end), pageable, loanList.size());
    }

    @Transactional(readOnly = true)
    public List<ISLoanReturnItemIssued> getLoanReturnItemHistory(Integer loanId, Integer itemId) {
        List<ISLoanReturnItemIssued> returnItemIssueds = new ArrayList<>();
        returnItemIssueds = loanReturnItemIssuedRepository.findByLoanAndItem(loanId, itemId);
        return returnItemIssueds;
    }

    public String printLoanChallan(Integer loanId, HttpServletRequest request, HttpServletResponse response) {
        InputStream is = null;
        String fileId = null;
        ISLoan loan = loanRepository.findOne(loanId);
        fileId = loan.getLoanNumber() + ".html";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileId + "\"");
            response.setContentType("text/html");
            List<ISLoanIssueItem> loanItems = loanIssueItemRepository.findByLoan(loanId);
            String htmlResponse = getLoanChallanExportHtml(loanItems, loan, request, response);
            response.setContentLength(htmlResponse.getBytes().length);
            is = new ByteArrayInputStream(htmlResponse.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
        fileId = fileId.replace("/", ".");
        fileMap.put(fileId, is);
        return fileId;
    }

    public String getLocalHostUrl() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String url = request.getScheme()
                + "://"
                + request.getServerName()
                + ":"
                + request.getServerPort();
        return url;
    }

    @Transactional(readOnly = true)
    public String getLoanChallanExportHtml(List<ISLoanIssueItem> loanItems, ISLoan loan, HttpServletRequest request, HttpServletResponse response) {
        List<ObjectAtrributeDTO> objectAtrributeDTOList = new ArrayList<>();
        List<ItemDTO> itemDTOList = new ArrayList<>();
        int i = 0;
        List<ObjectTypeAttribute> objectTypeAttributes = objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.valueOf("LOAN"));
        for (ObjectTypeAttribute objectTypeAttribute : objectTypeAttributes) {
            ObjectAtrributeDTO objectAtrributeDTO = new ObjectAtrributeDTO();
            objectAtrributeDTO.setObjectAttribute(objectAttributeRepository.findByObjectIdAndAttributeDefId(loan.getId(), objectTypeAttribute.getId()));
            objectAtrributeDTO.setObjectTypeAttribute(objectTypeAttribute);
            objectAtrributeDTOList.add(objectAtrributeDTO);
        }
        for (ISLoanIssueItem loanIssueItem : loanItems) {
            ItemDTO itemDTO = new ItemDTO();
            ISMaterialItem materialItem = materialItemRepository.findOne(loanIssueItem.getItem());
            itemDTO.setDescription(materialItem.getDescription());
            itemDTO.setItemName(materialItem.getItemName());
            itemDTO.setItemNumber(materialItem.getItemNumber());
            itemDTO.setItemIssueQuantity(loanIssueItem.getQuantity());
            itemDTO.setUnits(materialItem.getUnits());
            itemDTOList.add(itemDTO);
        }
        ISProject fromProject = projectRepository.findOne(loan.getFromProject());
        ISProject toProject = projectRepository.findOne(loan.getToProject());
        ISTopStore fromStore = isTopStoreRepository.findOne(loan.getFromStore());
        ISTopStore toStore = isTopStoreRepository.findOne(loan.getToStore());
        String imgUrl = null;
        String title = null;
        ServletContext context = request.getServletContext();
        String path = context.getRealPath("/") + "application.json";
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        try {
            Object obj = parser.parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) obj;
            title = (String) jsonObject.get("printTemplateTitle");
            String localImg = (String) jsonObject.get("templateLogo");
            imgUrl = getLocalHostUrl() + "/" + localImg;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Map<String, Object> model = new HashMap<>();
        model.put("fromProject", fromProject);
        model.put("toProject", toProject);
        model.put("fromStore", fromStore);
        model.put("toStore", toStore);
        model.put("logo", imgUrl);
        model.put("objectAtrributes", objectAtrributeDTOList);
        model.put("loanItems", itemDTOList);
        model.put("loan", loan);
        model.put("title", title);
        String exportHtmlData = getLoanChallanFromTemplate(model);
        return exportHtmlData;

    }

    @Transactional(readOnly = true)
    public String getLoanChallanFromTemplate(Map<String, Object> model) {
        StringBuffer content = new StringBuffer();
        try {
            fmConfiguration.setClassForTemplateLoading(this.getClass(), "/nfr");
            content.append(FreeMarkerTemplateUtils
                    .processTemplateIntoString(fmConfiguration.getTemplate("loanChallan.html"), model));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public String getNfrRailwaysLogoImageUrl() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String url = request.getScheme()
                + "://"
                + request.getServerName()
                + ":"
                + request.getServerPort()
                + "/app/assets/images/railwaysLogo.png";
        return url;
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

    public Page<ISLoan> freeTextSearch(Pageable pageable, ISLoanCriteria criteria) {
        Predicate predicate = loanPredicateBuilder.build(criteria, QISLoan.iSLoan);
        return loanRepository.findAll(predicate, pageable);
    }

}
