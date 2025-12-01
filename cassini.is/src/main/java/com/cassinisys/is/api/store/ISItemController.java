package com.cassinisys.is.api.store;

import com.cassinisys.is.model.store.ISItem;
import com.cassinisys.is.service.store.ISItemService;
import com.cassinisys.platform.api.core.BaseController;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@RestController
@RequestMapping("is/stores/items")
@Api(name = "ISItem", description = "ISItem endpoint", group = "IS")
public class ISItemController extends BaseController {

    @Autowired
    private ISItemService isItemService;

    @RequestMapping(method = RequestMethod.POST)
    public ISItem create(@RequestBody ISItem item) {
        return isItemService.create(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISItem update(@PathVariable("id") Integer id, @RequestBody ISItem item) {
        item.setId(id);
        return isItemService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        isItemService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISItem get(@PathVariable("id") Integer id) {
        return isItemService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ISItem> get() {
        return isItemService.getAll();
    }

    @RequestMapping(value = "/getByItemNumber/{itemNumber}", method = RequestMethod.GET)
    public ISItem getItemByItemNumber(@PathVariable("itemNumber") String itemNumber) {
        return isItemService.getItemByItemNumber(itemNumber);
    }
}
