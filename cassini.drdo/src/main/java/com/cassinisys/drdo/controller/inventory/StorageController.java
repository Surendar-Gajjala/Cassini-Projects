package com.cassinisys.drdo.controller.inventory;

import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.bom.ItemType;
import com.cassinisys.drdo.model.dto.StorageDetailsDto;
import com.cassinisys.drdo.model.inventory.Storage;
import com.cassinisys.drdo.model.inventory.StorageItem;
import com.cassinisys.drdo.model.inventory.StorageItemType;
import com.cassinisys.drdo.service.inventory.StorageService;
import com.cassinisys.platform.api.core.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by subra on 07-10-2018.
 */
@RestController
@RequestMapping("drdo/storage")
public class StorageController extends BaseController {

    @Autowired
    private StorageService storageService;

    @RequestMapping(method = RequestMethod.POST)
    public Storage create(@RequestBody Storage storage) {
        return storageService.create(storage);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Storage update(@PathVariable("id") Integer id,
                          @RequestBody Storage storage) {
        return storageService.update(storage);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        storageService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Storage get(@PathVariable("id") Integer id) {
        return storageService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Storage> getAll() {
        return storageService.getAll();
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<Storage> getBomItemsTree() {
        return storageService.getStorageTree();
    }

    @RequestMapping(value = "/warehouseByName/{name}", method = RequestMethod.GET)
    public Storage getWarehouseByName(@PathVariable("name") String name) {
        return storageService.getWarehouseByName(name);
    }

    @RequestMapping(value = "/stockroomByName/{name}", method = RequestMethod.GET)
    public Storage getStockroomByName(@PathVariable("name") String name) {
        return storageService.getStockroomByName(name);
    }

    @RequestMapping(value = "/stockroomByParentAndName/{parent}/{name}", method = RequestMethod.GET)
    public Storage getStockroomByParentAndName(@PathVariable("parent") Integer parent, @PathVariable("name") String name) {
        return storageService.getStockroomByParentAndName(parent, name);
    }

    @RequestMapping(value = "/areaByParentAndName/{parent}/{name}", method = RequestMethod.GET)
    public Storage getAreaByParentAndName(@PathVariable("parent") Integer parent, @PathVariable("name") String name) {
        return storageService.getAreaByParentAndName(parent, name);
    }

    @RequestMapping(value = "/rackByParentAndName/{parent}/{name}", method = RequestMethod.GET)
    public Storage getRackByParentAndName(@PathVariable("parent") Integer parent, @PathVariable("name") String name) {
        return storageService.getRackByParentAndName(parent, name);
    }

    @RequestMapping(value = "/shelfByParentAndName/{parent}/{name}", method = RequestMethod.GET)
    public Storage getShelfByParentAndName(@PathVariable("parent") Integer parent, @PathVariable("name") String name) {
        return storageService.getShelfByParentAndName(parent, name);
    }

    @RequestMapping(value = "/binByParentAndName/{parent}/{name}", method = RequestMethod.GET)
    public Storage getBinByParentAndName(@PathVariable("parent") Integer parent, @PathVariable("name") String name) {
        return storageService.getBinByParentAndName(parent, name);
    }

    @RequestMapping(value = "/barcode/{id}", method = RequestMethod.GET)
    public void generateStorageBarcode(@PathVariable("id") Integer id,
                                       HttpServletResponse response) {
        storageService.generateStorageBarcode(id, response);

    }

    @RequestMapping(value = "/saveStorageItemTypes/{storageId}", method = RequestMethod.POST)
    public List<StorageItemType> saveStorageItemTypes(@PathVariable("storageId") Integer storageId, @RequestBody List<ItemType> itemTypes) {
        return storageService.saveStorageItemTypes(storageId, itemTypes);
    }

    @RequestMapping(value = "/classificationTree/{storageId}", method = RequestMethod.GET)
    public List<ItemType> getStorageClassificationTree(@PathVariable("storageId") Integer storageId) {
        return storageService.getStorageClassificationTree(storageId);
    }

    @RequestMapping(value = "/{storageId}/deleteType/{typeId}", method = RequestMethod.DELETE)
    public void deleteStorageItemType(@PathVariable("storageId") Integer storageId, @PathVariable("typeId") Integer typeId) {
        storageService.deleteStorageItemType(storageId, typeId);
    }

    @RequestMapping(value = "/{storageId}/storageTypes", method = RequestMethod.GET)
    public List<StorageItemType> getStorageTypes(@PathVariable("storageId") Integer storageId) {
        return storageService.getStorageTypes(storageId);
    }

    @RequestMapping(value = "/{storageId}/items", method = RequestMethod.GET)
    public List<ItemInstance> getStorageItems(@PathVariable("storageId") Integer storageId) {
        return storageService.getStorageItems(storageId);
    }

    @RequestMapping(value = "/{storageId}/storageDetails", method = RequestMethod.GET)
    public StorageDetailsDto getStorageDetails(@PathVariable("storageId") Integer storageId) {
        return storageService.getStorageDetails(storageId);
    }

    @RequestMapping(value = "/{storageId}/storageParts", method = RequestMethod.POST)
    public List<StorageItem> saveStorageItems(@PathVariable("storageId") Integer storageId, @RequestBody List<BomItem> bomItems) {
        return storageService.saveStorageItems(storageId, bomItems);
    }

    @RequestMapping(value = "/storageItem/{storageItemId}", method = RequestMethod.DELETE)
    public void deleteStorageItem(@PathVariable("storageItemId") Integer storageItemId) {
        storageService.deleteStorageItem(storageItemId);
    }
}
