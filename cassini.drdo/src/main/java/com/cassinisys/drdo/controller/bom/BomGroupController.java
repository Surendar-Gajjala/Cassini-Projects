package com.cassinisys.drdo.controller.bom;

import com.cassinisys.drdo.filtering.BomGroupCriteria;
import com.cassinisys.drdo.model.bom.BomGroup;
import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.service.bom.BomGroupService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanam reddy on 08-11-2018.
 */
@RestController
@RequestMapping("/drdo/bomGroups")
public class BomGroupController extends BaseController {

    @Autowired
    private BomGroupService bomGroupService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public BomGroup create(@RequestBody BomGroup item) {
        return bomGroupService.create(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public BomGroup update(@PathVariable("id") Integer id,
                           @RequestBody BomGroup item) {
        return bomGroupService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        bomGroupService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BomGroup get(@PathVariable("id") Integer id) {
        return bomGroupService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<BomGroup> getAll() {
        return bomGroupService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<BomGroup> getAllItems(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bomGroupService.getAllBomGroups(pageable);
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public Page<BomGroup> getFilteredBomGroups(PageRequest pageRequest, BomGroupCriteria bomGroupCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bomGroupService.getFilteredBomGroups(pageable, bomGroupCriteria);
    }

    @RequestMapping(value = "/types", method = RequestMethod.GET)
    public Page<BomGroup> getBomGroupTypesByBom(PageRequest pageRequest, BomGroupCriteria bomGroupCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bomGroupService.getBomGroupTypesByBom(pageable, bomGroupCriteria);
    }

    @RequestMapping(value = "/{bomGroupTypeId}/items", method = RequestMethod.GET)
    public List<BomItem> getBomItemsByGroupType(@PathVariable("bomGroupTypeId") Integer bomGroupTypeId) {
        return bomGroupService.getBomItemsByGroupType(bomGroupTypeId);
    }
}
