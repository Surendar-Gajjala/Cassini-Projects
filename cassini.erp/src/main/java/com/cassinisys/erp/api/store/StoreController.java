package com.cassinisys.erp.api.store;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.store.ERPStore;
import com.cassinisys.erp.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Nageshreddy on 19-08-2018.
 */

@RestController
@RequestMapping("/stores")
public class StoreController extends BaseController{

    @Autowired
    private StoreService storeService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public ERPStore create(@RequestBody ERPStore item) {
        return storeService.create(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ERPStore update(@PathVariable("id") Integer id, @RequestBody ERPStore item) {
        item.setId(id);
        return storeService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        storeService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ERPStore get(@PathVariable("id") Integer id) {
        return storeService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ERPStore> getAllStores() {
        return storeService.getAll();
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ERPStore> getAllStores(ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return storeService.findAll(pageable);
    }


}
