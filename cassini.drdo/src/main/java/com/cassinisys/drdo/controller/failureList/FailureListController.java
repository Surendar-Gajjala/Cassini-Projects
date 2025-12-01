package com.cassinisys.drdo.controller.failureList;

import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.failureList.FailureList;
import com.cassinisys.drdo.model.failureList.FailureSteps;
import com.cassinisys.drdo.service.failureList.FailureListService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Nageshreddy on 02-01-2019.
 */

@RestController
@RequestMapping("drdo/failures")
public class FailureListController extends BaseController {

    @Autowired
    private FailureListService failureListService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.GET)
    public List<FailureList> getAll() {
        return failureListService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public FailureList get(@PathVariable("id") Integer id) {
        return failureListService.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        failureListService.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public FailureList save(@RequestBody FailureList falurelist) throws Exception {
        return failureListService.create(falurelist);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public FailureList update(@PathVariable("id") Integer id, @RequestBody FailureList falurelist) {
        return failureListService.update(falurelist);
    }

    @RequestMapping(value = "/steps/multiple/[{ids}]", method = RequestMethod.GET)
    public List<FailureSteps> getMultipleFalurelists(@PathVariable("ids") Integer[] ids) {
        return failureListService.getStepsMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/failureProcess", method = RequestMethod.GET)
    public Page<ItemInstance> getFailureProcessItems(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return failureListService.getFailureProcessItems(pageable);
    }
}
