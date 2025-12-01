package com.cassinisys.drdo.controller.transactions;

import com.cassinisys.drdo.filtering.DispatchCriteria;
import com.cassinisys.drdo.filtering.NotificationCriteria;
import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.dto.NotificationDto;
import com.cassinisys.drdo.model.transactions.Dispatch;
import com.cassinisys.drdo.service.transactions.DispatchService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyam reddy on 26-11-2018.
 */
@RestController
@RequestMapping("drdo/dispatch")
public class DispatchController extends BaseController {

    @Autowired
    private DispatchService dispatchService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public Dispatch create(@RequestBody Dispatch item) {
        return dispatchService.create(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Dispatch update(@PathVariable("id") Integer id,
                           @RequestBody Dispatch item) {
        return dispatchService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        dispatchService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Dispatch get(@PathVariable("id") Integer id) {
        return dispatchService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Dispatch> getAll() {
        return dispatchService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<Dispatch> getAllDispatches(PageRequest pageRequest, DispatchCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return dispatchService.getAllDispatches(pageable, criteria);
    }

    @RequestMapping(value = "/{bomId}/itemsToDispatch/{type}", method = RequestMethod.GET)
    public List<ItemInstance> getItemsToDispatchByBomId(@PathVariable("bomId") Integer bomId, @PathVariable("type") String type) {
        return dispatchService.getItemsToDispatchByBomId(bomId, type);
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    public NotificationDto getAllNotifications(NotificationCriteria notificationCriteria) {
        return dispatchService.getAllNotifications(notificationCriteria);
    }
}
