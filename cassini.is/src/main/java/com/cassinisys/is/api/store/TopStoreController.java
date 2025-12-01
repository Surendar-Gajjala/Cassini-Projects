package com.cassinisys.is.api.store;

import com.cassinisys.is.filtering.BoqItemCriteria;
import com.cassinisys.is.filtering.ReportCriteria;
import com.cassinisys.is.filtering.TopInventoryCriteria;
import com.cassinisys.is.filtering.TopStoreCriteria;
import com.cassinisys.is.model.procm.ISBoqItem;
import com.cassinisys.is.model.procm.ISMaterialItem;
import com.cassinisys.is.model.procm.MovementType;
import com.cassinisys.is.model.store.*;
import com.cassinisys.is.repo.procm.MaterialItemRepository;
import com.cassinisys.is.repo.store.ISTopStockMovementRepository;
import com.cassinisys.is.repo.store.ISTopStoreRepository;
import com.cassinisys.is.service.store.TopStoreService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Export;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@RestController
@RequestMapping("is/stores")
@Api(name = "ISTopStore", description = "ISTopStore endpoint", group = "IS")
public class TopStoreController extends BaseController {

    @Autowired
    private TopStoreService topStoreService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private ISTopStockMovementRepository topStockMovementRepository;

    @Autowired
    private ISTopStoreRepository topStoreRepository;

    @Autowired
    private MaterialItemRepository materialItemRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ISTopStore create(@RequestBody ISTopStore item) {
        return topStoreService.create(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISTopStore update(@PathVariable("id") Integer id, @RequestBody ISTopStore item) {
        item.setId(id);
        return topStoreService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        topStoreService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISTopStore get(@PathVariable("id") Integer id) {
        return topStoreService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ISTopStore> get() {
        return topStoreService.getAll();
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ISTopStore> getAllStores(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topStoreService.getAllStores(pageable);
    }

    @RequestMapping(value = "/filters", method = RequestMethod.GET)
    public Page<ISTopStore> getAllStoresByFilters(TopStoreCriteria criteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topStoreService.find(criteria, pageable);
    }

    @RequestMapping(value = "/{storeId}/stocksReceived", method = RequestMethod.POST)
    public List<ISStockReceiveItem> createStockReceived(@PathVariable("storeId") Integer storeId,
                                                        @RequestBody List<ISStockReceiveItem> stockReceived) {
        return topStoreService.createStockReceived(storeId, stockReceived);
    }

    @RequestMapping(value = "/{storeId}/stocksIssued", method = RequestMethod.POST)
    public List<ISTStockIssueItem> createStockIssued(@PathVariable("storeId") Integer storeId,
                                                     @RequestBody List<ISTStockIssueItem> stockIssued) {
        return topStoreService.createStockIssued(storeId, stockIssued);
    }

    @RequestMapping(value = "/{id}/stockMovement/{movementType}", method = RequestMethod.GET)
    public List<ISTopStockMovement> getStockReceivedByStore(@PathVariable("id") Integer id, @PathVariable("movementType") MovementType movementType) {
        return topStoreService.getStockReceivedByStore(id, movementType);
    }

    @RequestMapping(value = "/{id}/storeInventory", method = RequestMethod.GET)
    public List<ISTopInventory> getInventoryByStore(@PathVariable("id") Integer id) {
        return topStoreService.getInventoryByStore(id);
    }

    @RequestMapping(value = "/{id}/pageable", method = RequestMethod.GET)
    public Page<ISTopInventory> getStoreInventory(@PathVariable("id") Integer id, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topStoreService.getStoreInventory(id, pageable);
    }

    @RequestMapping(value = "/{id}/storeInventory/{itemId}", method = RequestMethod.GET)
    public List<ISTopInventory> getInventoryByStoreAndItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        return topStoreService.getInventoryByStoreAndItem(id, itemId);
    }

    @RequestMapping(value = "/{id}/stockReceived/{itemId}", method = RequestMethod.GET)
    public List<ISStockReceiveItem> getStockReceivedByStoreAndItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        return topStoreService.getStockReceivedByStoreAndItem(id, itemId);
    }

    @RequestMapping(value = "/{id}/stockIssued/{itemId}", method = RequestMethod.GET)
    public List<ISTStockIssueItem> getStockIssuedByStoreAndItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        return topStoreService.getStockIssuedByStoreAndItem(id, itemId);
    }

    @RequestMapping(value = "/storeName/{storeName}", method = RequestMethod.GET)
    public ISTopStore getStoreByName(@PathVariable("storeName") String storeName) {
        return topStoreService.getStoreByName(storeName);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<ISTopStore> freeTextSearch(PageRequest pageRequest,
                                           TopStoreCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISTopStore> stores = topStoreService.freeTextSearch(pageable, criteria);
        return stores;
    }

    @RequestMapping(value = "/searchAll", method = RequestMethod.GET)
    public List<ISTopStore> searchStores(TopStoreCriteria criteria) {
        criteria.setFreeTextSearch(true);
        List<ISTopStore> stores = topStoreService.searchStores(criteria);
        return stores;
    }

    @RequestMapping(value = "/stockMovement/{storeId}/freeTextSearch", method = RequestMethod.GET)
    public List<ISTopStockMovement> getStockMovementByStoreAndFreeTextSearch(@PathVariable("storeId") Integer storeId, PageRequest pageRequest, BoqItemCriteria criteria) {
        List<ISTopStockMovement> stockReceivedByStors = topStockMovementRepository.findByStore(topStoreRepository.findOne(storeId));
        List<ISTopStockMovement> topStockMovements = new ArrayList<>();
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISBoqItem> isBoqItems = topStoreService.freeTextSearchForBoqItem(pageable, criteria);
        for (ISTopStockMovement topStockMovement : stockReceivedByStors) {
            Boolean materialExist = false;
            for (ISBoqItem isBoqItem : isBoqItems.getContent()) {
                if (topStockMovement.getItem().equals(isBoqItem.getId())) {
                    materialExist = true;
                }
            }
            if (materialExist) {
                topStockMovements.add(topStockMovement);
            }
        }
        return topStockMovements;
    }

    @RequestMapping(value = "/{storeId}/freeTextSearch", method = RequestMethod.GET)
    public List<ISTopInventory> freeTextSearchWithoutProject(@PathVariable("storeId") Integer storeId, PageRequest pageRequest, BoqItemCriteria criteria) {
        List<ISTopInventory> inventories = getInventoryByStore(storeId);
        Map<Integer, ISTopInventory> inventoryMap = new HashMap<>();
        List<ISTopInventory> inventories1 = new ArrayList<>();
        for (ISTopInventory inventory : inventories) {
            inventoryMap.put(inventory.getItem(), inventory);
        }
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISBoqItem> isBoqItems = topStoreService.freeTextSearchForBoqItem(pageable, criteria);
        for (ISBoqItem isBoqItem : isBoqItems) {
            ISMaterialItem materialItem = materialItemRepository.findByItemNumber(isBoqItem.getItemNumber());
            ISTopInventory topInventory = inventoryMap.get(materialItem.getId());
            if (topInventory != null) {
                inventories1.add(topInventory);
            }
        }
        return inventories1;
    }

    @RequestMapping(value = "/{storeId}/project/{projectId}/freeTextSearch", method = RequestMethod.GET)
    public Page<ISTopInventory> freeTextSearchWithProject(@PathVariable("storeId") Integer storeId, @PathVariable("projectId") Integer projectId, PageRequest pageRequest, TopInventoryCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        criteria.setStoreId(storeId);
        criteria.setProjectId(projectId);
        criteria.setItemType(0);
        criteria.setFreeTextSearch(Boolean.TRUE);
//        return topStoreService.findByStoreAndProject(pageable, criteria);
        return null;
    }

    @RequestMapping(value = "/{storeId}/filters", method = RequestMethod.GET)
    public List<ISTopInventory> getTopInventoryByFilters(@PathVariable("storeId") Integer storeId, BoqItemCriteria criteria, PageRequest pageRequest) {
        List<ISTopInventory> inventories = getInventoryByStore(storeId);
        Map<Integer, ISTopInventory> inventoryMap = new HashMap<>();
        List<ISTopInventory> inventories1 = new ArrayList<>();
        for (ISTopInventory inventory : inventories) {
            inventoryMap.put(inventory.getItem(), inventory);
        }
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISBoqItem> isBoqItems = topStoreService.getTopInventoryByFilters(criteria, pageable);
        for (ISBoqItem isBoqItem : isBoqItems) {
            ISTopInventory topInventory = inventoryMap.get(isBoqItem.getId());
            inventories1.add(topInventory);
        }
        return inventories1;
    }

    @RequestMapping(value = "/itemQuantity/{itemId}", method = RequestMethod.GET)
    public List<ISStockReceiveItem> getAllItemQuantityByItem(@PathVariable("itemId") Integer itemId) {
        return topStoreService.getAllItemQuantityByItem(itemId);
    }

    @RequestMapping(value = "items/[{itemIds}]/stockReceived", method = RequestMethod.GET)
    public Map<Integer, Double> getStockReceivedQuantities(@PathVariable("itemIds") Integer[] itemIds) {
        return topStoreService.getStockReceivedQuantities(Arrays.asList(itemIds));
    }

    @RequestMapping(value = "/[{storeIds}]/inventory/[{itemIds}]", method = RequestMethod.GET)
    public List<ISTopInventory> getInventoryByMultipleStores(@PathVariable("projectId") Integer projectId,
                                                             @PathVariable("storeIds") Integer[] storeIds,
                                                             @PathVariable("itemIds") Integer[] itemIds) {
        return topStoreService.getInventoryByMultipleStoresAndItems(Arrays.asList(storeIds), Arrays.asList(itemIds));
    }

    @RequestMapping(value = "/receivedAttributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getReceivedAttributes(@PathVariable("objectType") String objectType) {
        return topStoreService.getReceivedAttributes(objectType);
    }

    @RequestMapping(value = "/multiple/[{storeIds}]")
    public List<ISTopStore> getStoresByIds(@PathVariable("storeIds") Integer[] storeIds) {
        return topStoreService.getStoresByIds(Arrays.asList(storeIds));
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public List<StoreReportDTO> getTaskReportRecords(ReportCriteria criteria) {
        return topStoreService.getReportByDates(criteria.getId(), criteria.getFromDate(), criteria.getToDate());
    }

    @RequestMapping(value = "/{fileType}/report", method = RequestMethod.GET,
            produces = "text/html")
    public String getTaskReport(@PathVariable("fileType") String fileType, HttpServletResponse response) {
        return topStoreService.exportTaskReport(fileType, response);
    }

    @RequestMapping(value = "/printReceiveChallan/{id}", method = RequestMethod.GET, produces = "text/html")
    public String printReceiveChallan(@PathVariable("id") Integer id,
                                      HttpServletRequest request, HttpServletResponse response) {
        String fileName = topStoreService.printReceiveChallan(id, request, response);
        return fileName;
    }

    @RequestMapping(value = "/printScrapChallan/{id}", method = RequestMethod.GET, produces = "text/html")
    public String printScrapChallan(@PathVariable("id") Integer id,
                                    HttpServletRequest request, HttpServletResponse response) {
        String fileName = topStoreService.printScrapChallan(id, request, response);
        return fileName;
    }

    @RequestMapping(value = "/print/RoadChallan/{id}", method = RequestMethod.GET, produces = "text/html")
    public String printRoadChallan(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
        String fileName = topStoreService.printRoadChallan(id, request, response);
        return fileName;
    }

    @RequestMapping(value = "/print/IssueChallan/{id}", method = RequestMethod.GET, produces = "text/html")
    public String printIssueChallan(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
        String fileName = topStoreService.printIssueChallan(id, request, response);
        return fileName;
    }

    @RequestMapping(value = "/print/PurchaseChallan/{purchaseOrderId}", method = RequestMethod.GET, produces = "text/html")
    public String printPurchaseChallan(@PathVariable("purchaseOrderId") Integer purchaseOrderId,
                                       HttpServletRequest request, HttpServletResponse response) {
        String fileName = topStoreService.printPurchaseChallan(purchaseOrderId, request, response);
        return fileName;
    }

    @RequestMapping(value = "/print/IndentChallan/{id}", method = RequestMethod.GET, produces = "text/html")
    public String printIndentChallan(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
        String fileName = topStoreService.printIndentChallan(id, request, response);
        return fileName;
    }

    @RequestMapping(value = "/file/{fileName}/download", method = RequestMethod.GET)
    public void downloadExportFile(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        topStoreService.downloadExportFile(fileName, response);

    }

    @RequestMapping(value = "/{storeId}/import", method = RequestMethod.POST)
    public ISTopStore importStoreItems(@PathVariable("storeId") Integer storeId, MultipartHttpServletRequest request) throws Exception {
        return topStoreService.importStoreItems(storeId, request);
    }

    @RequestMapping(value = "/storeImportFileFormat/download", method = {RequestMethod.POST}, produces = {"text/html"})
    public String storeImportFileFormat(@RequestParam("fileType") String fileType, @RequestBody Export export, HttpServletResponse response) {
        String fileName = topStoreService.storeImportFileFormat(fileType, export, response);
        return fileName;
    }

}
