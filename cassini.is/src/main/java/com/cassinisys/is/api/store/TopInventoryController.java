package com.cassinisys.is.api.store;

import com.cassinisys.is.filtering.MaterialCriteria;
import com.cassinisys.is.filtering.TopInventoryCriteria;
import com.cassinisys.is.model.procm.dto.InventoryListDTO;
import com.cassinisys.is.model.store.ISTopInventory;
import com.cassinisys.is.model.store.ISTopStore;
import com.cassinisys.is.repo.store.ISTopStoreRepository;
import com.cassinisys.is.service.pm.MaterialShortageDTO;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.is.service.store.TopInventoryService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@RestController
@RequestMapping("is/stores/inventory")
@Api(name = "ISTopInventory", description = "ISTopInventory endpoint", group = "IS")
public class TopInventoryController extends BaseController {

    @Autowired
    private TopInventoryService topInventoryService;

    @Autowired
    private ISTopStoreRepository topStoreRepository;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public ISTopInventory create(@RequestBody ISTopInventory item) {
        return topInventoryService.create(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISTopInventory update(@PathVariable("id") Integer id, @RequestBody ISTopInventory item) {
        item.setId(id);
        return topInventoryService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        topInventoryService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISTopInventory get(@PathVariable("id") Integer id) {
        return topInventoryService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ISTopInventory> get() {
        return topInventoryService.getAll();
    }

    @RequestMapping(value = "/getInventoryByItemNumber/{itemNumber}", method = RequestMethod.GET)
    public List<ISTopInventory> getInventoryByItemNumber(@PathVariable("itemNumber") String itemNumber) {
        return topInventoryService.getInventoryByItem(itemNumber);
    }

    @RequestMapping(value = "/project/{projectId}/item/{itemNumber}", method = RequestMethod.GET)
    public List<ISTopInventory> getInventoryByItemNumberAndProject(@PathVariable("projectId") Integer projectId, @PathVariable("itemNumber") String itemNumber) {
        return topInventoryService.getInventoryByItemNumberAndProject(projectId, itemNumber);
    }

    @RequestMapping(value = "/getInventoryByStore/{storeId}", method = RequestMethod.GET)
    public List<ISTopInventory> getItemByItemNumber(@PathVariable("storeId") Integer storeId) {
        ISTopStore store = topStoreRepository.findOne(storeId);
        return topInventoryService.getInventoryByStore(store);
    }

    @RequestMapping(value = "/project/{projectId}/itemNumber/{itemNumber}/boq/{boqId}", method = RequestMethod.GET)
    public List<ISTopInventory> getInventoryByBoqItemNumber(@PathVariable("projectId") Integer projectId, @PathVariable("itemNumber") String itemNumber, @PathVariable("boqId") Integer boqId) {
        return topInventoryService.getInventoryByBoqItemNumber(itemNumber, boqId, projectId);
    }

    @RequestMapping(value = "/allInvStoreDetails/pageable", method = RequestMethod.GET)
    public List<InventoryListDTO> getAllInvStoreDetails(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topInventoryService.getCustomInventories(pageable);
    }

    @RequestMapping(value = "/item/{itemId}/store/{storeId}/project/{projectId}", method = RequestMethod.GET)
    public ISTopInventory getInventoryByItemAndStore(@PathVariable("projectId") Integer projectId, @PathVariable("itemId") Integer itemId, @PathVariable("storeId") Integer storeId) {
        return topInventoryService.getInventoryByItemAndStore(projectId, itemId, storeId);
    }

    @RequestMapping(value = "/project/{projectId}", method = RequestMethod.GET)
    public List<MaterialShortageDTO> getProjectMaterialShortageDTO(@PathVariable("projectId") Integer projectId) {
        return topInventoryService.getProjectMaterialShortageDTO(projectId);
    }

    @RequestMapping(value = "/store/{storeId}/filters", method = RequestMethod.POST)
    public Page<ISTopInventory> getInventoryByFilters(@PathVariable("storeId") Integer storeId, PageRequest pageRequest, @RequestBody TopInventoryCriteria topInventoryCriteria) {
        topInventoryCriteria.setStoreId(storeId);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topInventoryService.getInventoryByFilters(topInventoryCriteria, pageable);
    }

    @RequestMapping(value = "/project/{projectId}/store/{storeId}/pageable", method = RequestMethod.GET)
    public Page<ISTopInventory> getStoreInventoryforLoan(@PathVariable("projectId") Integer projectId, @PathVariable("storeId") Integer storeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topInventoryService.getStoreInventoryforLoan(projectId, storeId, pageable);
    }

    @RequestMapping(value = "/project/{projectId}/store/{storeId}/inventoryBoq", method = RequestMethod.GET)
    public List<ItemDTO> getUnallocatedProjectInventoryBoq(@PathVariable("projectId") Integer projectId, @PathVariable("storeId") Integer storeId) {
        return topInventoryService.getUnallocatedProjectInventoryBoq(projectId, storeId);
    }

    @RequestMapping(value = "/project/{projectId}/store/{storeId}", method = RequestMethod.PUT)
    public void allocateItemsToProject(@PathVariable("projectId") Integer projectId, @PathVariable("storeId") Integer storeId, @RequestBody List<ItemDTO> itemDTOs) {
        topInventoryService.allocateItemsToProject(projectId, storeId, itemDTOs);
    }

    @RequestMapping(value = "/allInvStoreDetails/freesearch", method = RequestMethod.GET)
    public List<InventoryListDTO> searchCustomInventories(PageRequest pageRequest, MaterialCriteria materialCriteria) {
        materialCriteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topInventoryService.searchCustomInventories(pageable, materialCriteria);
    }
}
