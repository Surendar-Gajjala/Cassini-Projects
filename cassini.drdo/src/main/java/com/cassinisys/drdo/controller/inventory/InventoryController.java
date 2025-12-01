package com.cassinisys.drdo.controller.inventory;

import com.cassinisys.drdo.model.dto.BomInstanceInventoryDto;
import com.cassinisys.drdo.model.dto.BomInventoryDto;
import com.cassinisys.drdo.model.inventory.Inventory;
import com.cassinisys.drdo.service.inventory.InventoryService;
import com.cassinisys.platform.api.core.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subra on 19-11-2018.
 */
@RestController
@RequestMapping("drdo/inventory")
public class InventoryController extends BaseController {

    @Autowired
    private InventoryService inventoryService;

    @RequestMapping(method = RequestMethod.POST)
    public Inventory create(@RequestBody Inventory itemType) {
        return inventoryService.create(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Inventory update(@PathVariable("id") Integer id,
                            @RequestBody Inventory itemType) {
        return inventoryService.update(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        inventoryService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Inventory get(@PathVariable("id") Integer id) {
        return inventoryService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Inventory> getAll() {
        return inventoryService.getAll();
    }

    @RequestMapping(value = "/{bomId}/bom", method = RequestMethod.GET)
    public List<BomInventoryDto> getBomInventory(@PathVariable("bomId") Integer bomId) {
        return inventoryService.getBomInventory(bomId);
    }

    @RequestMapping(value = "/{bomId}/bom/search", method = RequestMethod.GET)
    public List<BomInventoryDto> searchBomInventory(@PathVariable("bomId") Integer bomId, @RequestParam("searchText") String searchText) {
        return inventoryService.searchBomInventory(bomId, searchText);
    }

    @RequestMapping(value = "/{bomId}/bomInstance/search", method = RequestMethod.GET)
    public List<BomInstanceInventoryDto> searchBomInstanceInventory(@PathVariable("bomId") Integer bomId, @RequestParam("searchText") String searchText) {
        return inventoryService.searchBomInstanceInventory(bomId, searchText);
    }

    @RequestMapping(value = "/{bomId}/bom/instance", method = RequestMethod.GET)
    public List<BomInstanceInventoryDto> getBomInstanceInventory(@PathVariable("bomId") Integer bomId) {
        return inventoryService.getBomInstanceInventory(bomId);
    }

    @RequestMapping(value = "/{bomId}/bom/{bomItemId}/children", method = RequestMethod.GET)
    public List<BomInventoryDto> getBomChildrenInventory(@PathVariable("bomId") Integer bomId, @PathVariable("bomItemId") Integer bomItemId) {
        return inventoryService.getBomChildrenInventory(bomId, bomItemId);
    }

    @RequestMapping(value = "/{bomId}/bom/instance/{bomItemId}/children", method = RequestMethod.GET)
    public List<BomInstanceInventoryDto> getBomInstanceChildrenInventory(@PathVariable("bomId") Integer bomId, @PathVariable("bomItemId") Integer bomItemId) {
        return inventoryService.getBomInstanceChildrenInventory(bomId, bomItemId);
    }

    @RequestMapping(value = "/{bomId}/bom/report", method = RequestMethod.GET)
    public List<BomInventoryDto> getInventoryReportByBom(@PathVariable("bomId") Integer bomId) {
        return inventoryService.getInventoryReportByBom(bomId);
    }

    @RequestMapping(value = "/{bomId}/bom/{sectionId}/section/report", method = RequestMethod.GET)
    public List<BomInventoryDto> getInventoryReportBySection(@PathVariable("bomId") Integer bomId, @PathVariable("sectionId") Integer sectionId) {
        return inventoryService.getInventoryReportBySection(bomId, sectionId);
    }

    @RequestMapping(value = "/{bomId}/instance/report", method = RequestMethod.GET)
    public List<BomInstanceInventoryDto> getInventoryReportByInstance(@PathVariable("bomId") Integer bomId) {
        return inventoryService.getInventoryReportByInstance(bomId);
    }

    @RequestMapping(value = "/{bomId}/instance/{sectionId}/section/report", method = RequestMethod.GET)
    public List<BomInstanceInventoryDto> getInventoryReportByInstanceSection(@PathVariable("bomId") Integer bomId, @PathVariable("sectionId") Integer sectionId) {
        return inventoryService.getInventoryReportByInstanceSection(bomId, sectionId);
    }
}
