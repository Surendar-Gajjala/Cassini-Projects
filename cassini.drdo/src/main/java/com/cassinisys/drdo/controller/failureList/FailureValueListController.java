package com.cassinisys.drdo.controller.failureList;

import com.cassinisys.drdo.model.failureList.DRDOFailureValueList;
import com.cassinisys.drdo.service.failureList.FailureValueListService;
import com.cassinisys.platform.api.core.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Nageshreddy on 02-01-2019.
 */

@RestController
@RequestMapping(value = "drdo/failure/values")
public class FailureValueListController extends BaseController {

    @Autowired
    private FailureValueListService failureValueListService;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public DRDOFailureValueList get(@PathVariable("id") Integer id) {
        checkNotNull(id);
        return failureValueListService.get(id);
    }

    @RequestMapping(value = "/{instanceId}", method = RequestMethod.POST)
    public DRDOFailureValueList save(@PathVariable("instanceId") Integer instanceId, @RequestBody DRDOFailureValueList failValueList) {
        checkNotNull(failValueList);
        return failureValueListService.create(instanceId, failValueList);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void delete(@PathVariable("id") Integer id) {
        failureValueListService.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<DRDOFailureValueList> getAll() {
        return failureValueListService.getAll();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public DRDOFailureValueList update(@RequestBody DRDOFailureValueList failValueList, @PathVariable("id") Integer id) {
        checkNotNull(failValueList);
        checkNotNull(id);
        failValueList.setId(id);
        return failureValueListService.update(failValueList);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/multiple")
    public List<DRDOFailureValueList> saveAll(@RequestBody List<DRDOFailureValueList> failValueLists) {
        return failureValueListService.saveAll(failValueLists);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/multiple")
    public List<DRDOFailureValueList> updateAll(@RequestBody List<DRDOFailureValueList> failValueLists) {
        return failureValueListService.saveAll(failValueLists);
    }

    @RequestMapping(value = "/byItem/{item}", method = RequestMethod.GET)
    public List<DRDOFailureValueList> getByItem(@PathVariable("item") Integer item) {
        checkNotNull(item);
        return failureValueListService.getByItem(item);
    }

    @RequestMapping(value = "/byItemAndInstance/{item}/{upn}", method = RequestMethod.GET)
    public List<DRDOFailureValueList> getByItemAndInstance(@PathVariable("item") Integer item, @PathVariable("upn") Integer upn) {
        checkNotNull(item);
        checkNotNull(upn);
        return failureValueListService.findByItemAndInstanceOrderByIdAsc(item, upn);
    }


    @RequestMapping(value = "/byItemAndLotInstance/{item}/{upn}", method = RequestMethod.GET)
    public List<DRDOFailureValueList> getByItemAndLotInstance(@PathVariable("item") Integer item, @PathVariable("upn") Integer upn) {
        checkNotNull(item);
        checkNotNull(upn);
        return failureValueListService.findByItemAndLotInstanceOrderByIdAsc(item, upn);
    }

}
