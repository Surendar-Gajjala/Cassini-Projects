package com.cassinisys.is.service.store;

import com.cassinisys.is.filtering.StockReturnCriteria;
import com.cassinisys.is.filtering.StockReturnPredicateBuilder;
import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.procm.ISMaterialItem;
import com.cassinisys.is.model.procm.MovementType;
import com.cassinisys.is.model.store.*;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.is.repo.procm.MaterialItemRepository;
import com.cassinisys.is.repo.store.ISStockReturnItemRepository;
import com.cassinisys.is.repo.store.ISStockReturnRepository;
import com.cassinisys.is.repo.store.ISTopInventoryRepository;
import com.cassinisys.is.repo.store.ISTopStoreRepository;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.mysema.query.types.Predicate;
import freemarker.template.Configuration;
import org.apache.commons.io.IOUtils;
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

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by swapna on 05/12/18.
 */
@Service
public class ISStockReturnService {

    public static Map fileMap = new HashMap();
    @Autowired
    Configuration fmConfiguration;
    @Autowired
    private ISStockReturnRepository stockReturnRepository;
    @Autowired
    private ISStockReturnItemRepository stockReturnItemRepository;
    @Autowired
    private ISTopInventoryRepository topInventoryRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ISTopStoreRepository storeRepository;
    @Autowired
    private MaterialItemRepository materialItemRepository;
    @Autowired
    private StockReturnPredicateBuilder stockReturnPredicateBuilder;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Transactional(readOnly = false)
    public ISStockReturn createStockReturn(Integer storeId, ISStockReturn stockReturn) {
        stockReturn.setReturnedDate(new Date());
        stockReturn.setStore(storeId);
        ISStockReturn newStockReturn = stockReturnRepository.save(stockReturn);
        for (ISStockReturnItem stockReturnItem : stockReturn.getStockReturnItemList()) {
            stockReturnItem.setStockReturn(newStockReturn.getId());
            stockReturnItem.setTimeStamp(new Date());
            stockReturnItem.setMovementType(MovementType.RETURNED);
            stockReturnItem.setProject(stockReturn.getProject());
            stockReturnItem.setStore(storeRepository.findOne(storeId));
        }
        List<ISStockReturnItem> stockReturnItemList = stockReturnItemRepository.save(stockReturn.getStockReturnItemList());
        return newStockReturn;
    }

    public ISStockReturn getStockReturn(Integer stockreturnId) {
        return stockReturnRepository.findOne(stockreturnId);
    }

    public Page<ISStockReturn> getStoreStockReturns(Integer storeId, Pageable pageable) {
        return stockReturnRepository.findByStore(storeId, pageable);
    }

    public Page<ISStockReturn> getStockReturns(Pageable pageable) {
        return stockReturnRepository.findAll(pageable);
    }

    @Transactional(readOnly = false)
    public ISStockReturn updateStockReturn(ISStockReturn newStockReturn) {
        ISStockReturn previousStockReturn = stockReturnRepository.findOne(newStockReturn.getId());
        if (!previousStockReturn.getStatus().equals(StockReturnStatus.APPROVED) && newStockReturn.getStatus().equals(StockReturnStatus.APPROVED)) {
            List<ISStockReturnItem> stockReturnItemList = stockReturnItemRepository.findByStockReturn(newStockReturn.getId());
            for (ISStockReturnItem stockReturnItem : stockReturnItemList) {
                stockReturnItem.setStockReturn(newStockReturn.getId());
                stockReturnItem.setTimeStamp(new Date());
                stockReturnItem.setMovementType(MovementType.RETURNED);
                stockReturnItem.setProject(newStockReturn.getProject());
                stockReturnItem.setStore(storeRepository.findOne(newStockReturn.getStore()));
                ISTopInventory inventory = topInventoryRepository.findByStoreAndItemAndProject(storeRepository.findOne(newStockReturn.getStore()), stockReturnItem.getItem(), newStockReturn.getProject());
                if (inventory != null) {
                    stockReturnItem.setOpeningBalance(inventory.getStoreOnHand());
                    stockReturnItem.setClosingBalance(stockReturnItem.getOpeningBalance() + stockReturnItem.getQuantity());
                    inventory.setStoreOnHand(inventory.getStoreOnHand() + stockReturnItem.getQuantity());
                } else {
                    inventory = new ISTopInventory();
                    inventory.setItem(stockReturnItem.getItem());
                    if (newStockReturn.getProject() != null) {
                        inventory.setProject(newStockReturn.getProject());
                    }
                    inventory.setStore(storeRepository.findOne(newStockReturn.getStore()));
                    inventory.setStoreOnHand(stockReturnItem.getQuantity());
                }
                topInventoryRepository.save(inventory);
            }
        }
        return stockReturnRepository.save(newStockReturn);
    }

    public void deleteStockReturn(Integer stockreturnId) {
        ISStockReturn stockReturn = stockReturnRepository.findOne(stockreturnId);
        if (stockReturn != null) {
            stockReturnRepository.delete(stockReturn);
        }
    }

    @Transactional(readOnly = false)
    public List<ISStockReturnItem> creteStockReturnItems(Integer storeId, Integer stockreturnId, List<ISStockReturnItem> stockReturnItems) {
        List<ISStockReturnItem> stockReturnItemList = stockReturnItemRepository.save(stockReturnItems);
        ISStockReturn stockReturn = stockReturnRepository.findOne(stockreturnId);
        for (ISStockReturnItem stockReturnItem : stockReturnItemList) {
            ISTopInventory inventory = topInventoryRepository.findByStoreAndItemAndProject(storeRepository.findOne(storeId), stockReturnItem.getItem(), stockReturn.getProject());
            inventory.setStoreOnHand(inventory.getStoreOnHand() - stockReturnItem.getQuantity());
            topInventoryRepository.save(inventory);
        }
        return stockReturnItemList;
    }

    public Page<ItemDTO> getStockReturnItems(Integer stockreturnId, Pageable pageable) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        List<ISStockReturnItem> stockReturnItemList = stockReturnItemRepository.findByStockReturn(stockreturnId);
        for (ISStockReturnItem stockReturnItem : stockReturnItemList) {
            ISMaterialItem materialItem = materialItemRepository.findOne(stockReturnItem.getItem());
            if (materialItem != null) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setDescription(materialItem.getDescription());
                itemDTO.setItemNumber(materialItem.getItemNumber());
                itemDTO.setItemType(materialItem.getItemType().getName());
                itemDTO.setId(materialItem.getId());
                itemDTO.setItemName(materialItem.getItemName());
                itemDTO.setItemReturnQuantity(stockReturnItem.getQuantity());
                itemDTOList.add(itemDTO);
            }
        }
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > itemDTOList.size() ? itemDTOList.size() : (start + pageable.getPageSize());
        return new PageImpl<ItemDTO>(itemDTOList.subList(start, end), pageable, itemDTOList.size());
    }

    public String printStockReturnChallan(Integer requisitionId, HttpServletRequest request, HttpServletResponse response) {
        InputStream is = null;
        String fileId = null;
        ISStockReturn stockReturn = stockReturnRepository.findOne(requisitionId);
        fileId = stockReturn.getReturnNumberSource() + ".html";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileId + "\"");
            response.setContentType("text/html");
            List<ISStockReturnItem> stockReturnItemList = stockReturnItemRepository.findByStockReturn(requisitionId);
            String htmlResponse = getRequisitionChallanExportHtml(stockReturnItemList, stockReturn, request, response);
            response.setContentLength(htmlResponse.getBytes().length);
            is = new ByteArrayInputStream(htmlResponse.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
        fileId = fileId.replace("/", ".");
        fileMap.put(fileId, is);
        return fileId;
    }

    @Transactional(readOnly = true)
    public String getRequisitionChallanExportHtml(List<ISStockReturnItem> stockReturnItemList, ISStockReturn stockReturn, HttpServletRequest request, HttpServletResponse response) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        List<ObjectAtrributeDTO> objectAtrributeDTOList = new ArrayList<>();
        List<ObjectTypeAttribute> objectTypeAttributes = objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.valueOf("STOCKRETURN"));
        for (ObjectTypeAttribute objectTypeAttribute : objectTypeAttributes) {
            ObjectAtrributeDTO objectAtrributeDTO = new ObjectAtrributeDTO();
            objectAtrributeDTO.setObjectAttribute(objectAttributeRepository.findByObjectIdAndAttributeDefId(stockReturn.getId(), objectTypeAttribute.getId()));
            objectAtrributeDTO.setObjectTypeAttribute(objectTypeAttribute);
            objectAtrributeDTOList.add(objectAtrributeDTO);
        }
        for (ISStockReturnItem stockReturnItem : stockReturnItemList) {
            ItemDTO itemDTO = new ItemDTO();
            ISMaterialItem materialItem = materialItemRepository.findOne(stockReturnItem.getItem());
            itemDTO.setItemNumber(materialItem.getItemNumber());
            itemDTO.setDescription(materialItem.getDescription());
            itemDTO.setItemName(materialItem.getItemName());
            itemDTO.setItemType(materialItem.getItemType().getName());
            itemDTO.setUnits(materialItem.getUnits());
            itemDTO.setItemReturnQuantity(stockReturnItem.getQuantity());
            itemDTOList.add(itemDTO);
        }
        String imgUrl = null;
        String title = null;
        ISProject project = projectRepository.findOne(stockReturn.getProject());
        ISTopStore store = storeRepository.findOne(stockReturn.getStore());
        ServletContext context = request.getServletContext();
        String path = context.getRealPath("/") + "application.json";
        JSONParser parser = new JSONParser();
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
        model.put("objectAtrributes", objectAtrributeDTOList);
        model.put("stockReturnItemList", itemDTOList);
        model.put("stockReturn", stockReturn);
        model.put("projectName", project.getName());
        model.put("storeName", store.getStoreName());
        model.put("logo", imgUrl);
        model.put("title", title);
        String exportHtmlData = getRequisitionChallanFromTemplate(model);
        return exportHtmlData;

    }

    @Transactional(readOnly = true)
    public String getRequisitionChallanFromTemplate(Map<String, Object> model) {
        StringBuffer content = new StringBuffer();
        try {
            fmConfiguration.setClassForTemplateLoading(this.getClass(), "/nfr");
            content.append(FreeMarkerTemplateUtils
                    .processTemplateIntoString(fmConfiguration.getTemplate("stockReturnChallan.html"), model));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
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

    @Transactional(readOnly = true)
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

    public Page<ISStockReturn> freeTextSearch(Pageable pageable, StockReturnCriteria criteria) {
        Predicate predicate = stockReturnPredicateBuilder.build(criteria, QISStockReturn.iSStockReturn);
        return stockReturnRepository.findAll(predicate, pageable);
    }
}
