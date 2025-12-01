package com.cassinisys.drdo.controller.inventory;

import com.cassinisys.drdo.model.dto.BomAllocationDto;
import com.cassinisys.drdo.model.dto.MissileAllocationDto;
import com.cassinisys.drdo.model.inventory.ItemAllocation;
import com.cassinisys.drdo.service.inventory.ItemAllocationService;
import com.cassinisys.platform.api.core.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Nageshreddy on 03-12-2018.
 */
@RestController
@RequestMapping("/drdo/allocation")
public class ItemAllocationController extends BaseController {

    @Autowired
    private ItemAllocationService allocationService;

    @RequestMapping(method = RequestMethod.POST)
    public ItemAllocation create(@RequestBody ItemAllocation itemAllocation) {
        return allocationService.create(itemAllocation);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ItemAllocation update(@PathVariable("id") Integer id,
                                 @RequestBody ItemAllocation itemAllocation) {
        return allocationService.update(itemAllocation);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        allocationService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ItemAllocation get(@PathVariable("id") Integer id) {
        return allocationService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ItemAllocation> getAll() {
        return allocationService.getAll();
    }

    @RequestMapping(value = "/{bomId}/bom", method = RequestMethod.GET)
    public List<BomAllocationDto> getBomAllocation(@PathVariable("bomId") Integer bomId) {
        return allocationService.getBomAllocation(bomId);
    }

    @RequestMapping(value = "/{bomId}/bom/instance", method = RequestMethod.GET)
    public List<MissileAllocationDto> getBomInstanceAllocation(@PathVariable("bomId") Integer bomId) {
        return allocationService.getBomInstanceAllocation(bomId);
    }

    @RequestMapping(value = "/{bomId}/bom/{bomItemId}/children/[{missileIds}]", method = RequestMethod.POST)
    public List<BomAllocationDto> getBomChildrenInventory(@PathVariable("bomId") Integer bomId, @PathVariable("bomItemId") Integer bomItemId,
                                                          @PathVariable("missileIds") Integer[] missileIds, @RequestBody Integer[] selectedMissileIds,
                                                          @RequestParam("workCenter") String workCenter, @RequestParam("searchBomText") String searchBomText) {
        return allocationService.getBomChildrenAllocation(bomId, bomItemId, missileIds, selectedMissileIds, workCenter, searchBomText);
    }

    @RequestMapping(value = "/load/{bomId}/bom/{bomItemId}/children/[{missileIds}]", method = RequestMethod.POST)
    public void loadBomChildrenAllocation(@PathVariable("bomId") Integer bomId, @PathVariable("bomItemId") Integer bomItemId,
                                          @PathVariable("missileIds") Integer[] missileIds, @RequestBody Integer[] selectedMissileIds) {
        allocationService.loadBomChildrenAllocation(bomId, bomItemId, missileIds, selectedMissileIds);
    }

    @RequestMapping(value = "/load/{bomId}/bom/children/[{missileIds}]", method = RequestMethod.POST)
    public void loadBomChildrenWithoutSecAllocation(@PathVariable("bomId") Integer bomId,
                                                    @PathVariable("missileIds") Integer[] missileIds, @RequestBody Integer[] selectedMissileIds) {
        allocationService.loadBomChildrenWithoutSecAllocation(bomId, missileIds, selectedMissileIds);
    }

    @RequestMapping(value = "/{bomId}/bom/instance/{bomItemId}/children", method = RequestMethod.GET)
    public List<MissileAllocationDto> getBomInstanceChildrenInventory(@PathVariable("bomId") Integer bomId, @PathVariable("bomItemId") Integer bomItemId) {
        return allocationService.getBomInstanceChildrenInventory(bomId, bomItemId);
    }

    @RequestMapping(value = "/plan", method = RequestMethod.POST)
    public void planInventoryToCurrentMissiles(@RequestBody List<ItemAllocation> itemAllocations) {
        allocationService.planInventoryToCurrentMissiles(itemAllocations);
    }

    @RequestMapping(value = "/resetPlan", method = RequestMethod.POST)
    public void resetInventoryToCurrentMissiles(@RequestBody List<ItemAllocation> itemAllocations) {
        allocationService.resetInventoryToCurrentMissiles(itemAllocations);
    }

    @RequestMapping(value = "/missiles/plan//[{missileIds}]", method = RequestMethod.GET)
    public void planSelectedMissiles(@PathVariable("missileIds") Integer[] missileIds) {
        allocationService.planForSelectedMissiles(missileIds);
    }

    @RequestMapping(value = "/missiles/reset//[{missileIds}]", method = RequestMethod.GET)
    public void resetPlanForSelectedMissiles(@PathVariable("missileIds") Integer[] missileIds) {
        allocationService.reSetPlanForSelectedMissiles(missileIds);
    }
}
