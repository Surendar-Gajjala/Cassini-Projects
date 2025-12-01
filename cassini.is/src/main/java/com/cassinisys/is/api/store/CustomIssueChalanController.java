package com.cassinisys.is.api.store;

import com.cassinisys.is.model.store.CustomIssueChalan;
import com.cassinisys.is.model.store.CustomIssueItem;
import com.cassinisys.is.service.store.CustomIssueChalanService;
import com.cassinisys.platform.api.core.BaseController;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@RestController
@RequestMapping("is/stores/issueChalans")
@Api(name = "CustomIssueChalan", description = "CustomIssueChalan endpoint", group = "IS")
public class CustomIssueChalanController extends BaseController {
    @Autowired
    private CustomIssueChalanService customIssueChalanService;


   /*  methods for CustomIssueChalan */

    @RequestMapping(method = RequestMethod.POST)
    public CustomIssueChalan create(@RequestBody CustomIssueChalan issueChalan) {
        return customIssueChalanService.create(issueChalan);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CustomIssueChalan update(@PathVariable("id") Integer id, @RequestBody CustomIssueChalan issueChalan) {
        issueChalan.setId(id);
        return customIssueChalanService.update(issueChalan);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        customIssueChalanService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CustomIssueChalan get(@PathVariable("id") Integer id) {
        return customIssueChalanService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CustomIssueChalan> get() {
        return customIssueChalanService.getAll();
    }


     /*  methods for CustomIssueChalanItem */

    @RequestMapping(value = "/{id}/issueChalanItems", method = RequestMethod.POST)
    public List<CustomIssueItem> createIssueChalanItem(@PathVariable("id") Integer id, @RequestBody List<CustomIssueItem> issueChalanItems) {
        return customIssueChalanService.createIssueChalanItems(issueChalanItems);
    }

    @RequestMapping(value = "/{id}/issueChalanItem/{issueChalanItemId}", method = RequestMethod.PUT)
    public CustomIssueItem updateIssueChalanItem(
            @PathVariable("id") Integer id,
            @PathVariable("issueChalanItemId") Integer issueChalanItemId,
            @RequestBody CustomIssueItem issueChalanItem) {
        issueChalanItem.setId(issueChalanItemId);
        return customIssueChalanService.updateIssueChalanItem(issueChalanItem);
    }

    @RequestMapping(value = "/{id}/issueChalanItem/{issueChalanItemId}", method = RequestMethod.DELETE)
    public void deleteIssueChalanItem(@PathVariable("id") Integer id, @PathVariable("issueChalanItemId") Integer issueChalanItemId) {
        customIssueChalanService.deleteIssueChalanItem(issueChalanItemId);
    }

    @RequestMapping(value = "/{id}/issueChalanItem/{issueChalanItemId}", method = RequestMethod.GET)
    public CustomIssueItem getIssueChalanItem(@PathVariable("id") Integer id,
                                              @PathVariable("issueChalanItemId") Integer issueChalanItemId) {
        return customIssueChalanService.getIssueChalanItem(issueChalanItemId);
    }

    @RequestMapping(value = "/{id}/customIssueItems", method = RequestMethod.GET)
    public List<CustomIssueItem> getAllIssueChalanItems() {
        return customIssueChalanService.getAllIssueChalanItems();
    }
}
