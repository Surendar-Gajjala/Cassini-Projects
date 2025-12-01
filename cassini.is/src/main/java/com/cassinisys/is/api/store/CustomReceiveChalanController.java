package com.cassinisys.is.api.store;

import com.cassinisys.is.model.store.CustomReceiveChalan;
import com.cassinisys.is.model.store.CustomReceiveItem;
import com.cassinisys.is.service.store.CustomReceiveChalanService;
import com.cassinisys.platform.api.core.BaseController;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@RestController
@RequestMapping("is/stores/receiveChalans")
@Api(name = " CustomReceiveChalan", description = " CustomReceiveChalan endpoint", group = "IS")
public class CustomReceiveChalanController extends BaseController {
    @Autowired
    private CustomReceiveChalanService customReceiveChalanService;


   /*  methods for CustomReceiveChalan */

    @RequestMapping(method = RequestMethod.POST)
    public CustomReceiveChalan create(@RequestBody CustomReceiveChalan receiveChalan) {
        return customReceiveChalanService.create(receiveChalan);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CustomReceiveChalan update(@PathVariable("id") Integer id, @RequestBody CustomReceiveChalan receiveChalan) {
        receiveChalan.setId(id);
        return customReceiveChalanService.update(receiveChalan);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        customReceiveChalanService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CustomReceiveChalan get(@PathVariable("id") Integer id) {
        return customReceiveChalanService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CustomReceiveChalan> get() {
        return customReceiveChalanService.getAll();
    }


     /*  methods for CustomReceiveChalanItem */

    @RequestMapping(value = "/{id}/receiveChalanItems", method = RequestMethod.POST)
    public List<CustomReceiveItem> createReceiveChalanItem(@PathVariable("id") Integer id, @RequestBody List<CustomReceiveItem> receiveChalanItems) {
        return customReceiveChalanService.createReceiveChalanItems(receiveChalanItems);
    }

    @RequestMapping(value = "/{id}/receiveChalanItem/{receiveChalanItemId}", method = RequestMethod.PUT)
    public CustomReceiveItem updateReceiveChalanItem(
            @PathVariable("id") Integer id,
            @PathVariable("receiveChalanItemId") Integer receiveChalanItemId,
            @RequestBody CustomReceiveItem receiveChalanItem) {
        receiveChalanItem.setId(receiveChalanItemId);
        return customReceiveChalanService.updateReceiveChalanItem(receiveChalanItem);
    }

    @RequestMapping(value = "/{id}/receiveChalanItem/{receiveChalanItemId}", method = RequestMethod.DELETE)
    public void deleteCustomReceiveItem(@PathVariable("id") Integer id, @PathVariable("receiveChalanItemId") Integer receiveChalanItemId) {
        customReceiveChalanService.deleteCustomReceiveItem(receiveChalanItemId);
    }

    @RequestMapping(value = "/{id}/receiveChalanItem/{receiveChalanItemId}", method = RequestMethod.GET)
    public CustomReceiveItem getCustomReceiveItem(@PathVariable("id") Integer id,
                                                  @PathVariable("receiveChalanItemId") Integer receiveChalanItemId) {
        return customReceiveChalanService.getCustomReceiveItem(receiveChalanItemId);
    }

    @RequestMapping(value = "/{id}/customReceiveItems", method = RequestMethod.GET)
    public List<CustomReceiveItem> getAllCustomReceiveItems() {
        return customReceiveChalanService.getAllCustomReceiveItems();
    }
}
