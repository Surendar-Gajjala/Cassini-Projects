package com.cassinisys.drdo.controller.transactions;

import com.cassinisys.drdo.filtering.IssueCriteria;
import com.cassinisys.drdo.model.dto.IssueDetailsDto;
import com.cassinisys.drdo.model.dto.IssueDto;
import com.cassinisys.drdo.model.dto.IssueReportDto;
import com.cassinisys.drdo.model.transactions.Issue;
import com.cassinisys.drdo.model.transactions.IssueItem;
import com.cassinisys.drdo.service.transactions.IssueService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.security.GroupPermission;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyam reddy on 17-10-2018.
 */
@RestController
@RequestMapping("/drdo/issues")
public class IssueController extends BaseController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public Issue create(@RequestBody Issue issue) {
        return issueService.create(issue);
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public Issue createNewIssue(@RequestBody Issue issue) {
        return issueService.newIssue(issue);
    }

    @RequestMapping(value = "/items", method = RequestMethod.POST)
    public Issue createIssue(@RequestBody Issue issue) {
        return issueService.createIssue(issue);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Issue update(@PathVariable("id") Integer id,
                        @RequestBody Issue issue) {
        return issueService.update(issue);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        issueService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Issue get(@PathVariable("id") Integer id) {
        return issueService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Issue> getAll() {
        return issueService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<Issue> getAllIssues(PageRequest pageRequest, IssueCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return issueService.getAllIssues(pageable, criteria);
    }

    @RequestMapping(value = "/{instanceId}/all", method = RequestMethod.GET)
    public List<Issue> getIssuesByInstance(@PathVariable("instanceId") Integer instanceId) {
        return issueService.getIssuesByInstance(instanceId);
    }

    @RequestMapping(value = "/{issueId}/details", method = RequestMethod.GET)
    public IssueDto getIssueDetails(@PathVariable("issueId") Integer issueId) {
        return issueService.getIssueDetails(issueId);
    }


    @RequestMapping(value = "/{issueId}/approve", method = RequestMethod.PUT)
    public Issue approveIssue(@PathVariable("issueId") Integer issueId, @RequestBody Issue issue) {
        return issueService.approveIssue(issueId, issue);
    }


    @RequestMapping(value = "/{issueId}/item/{itemId}/approve", method = RequestMethod.PUT)
    public IssueItem approveIssueItem(@PathVariable("issueId") Integer issueId, @PathVariable("itemId") Integer itemId, @RequestBody IssueItem issueItem) {
        return issueService.approveIssueItem(issueId, itemId, issueItem);
    }

    @RequestMapping(value = "/{issueId}/item/{itemId}/approveItem", method = RequestMethod.PUT)
    public IssueItem approveItem(@PathVariable("issueId") Integer issueId, @PathVariable("itemId") Integer itemId, @RequestBody IssueItem issueItem) {
        return issueService.approveItem(issueId, itemId, issueItem);
    }

    @RequestMapping(value = "/{issueId}/item/{itemId}/partially/approve", method = RequestMethod.PUT)
    public IssueDetailsDto partiallyApproveIssueItem(@PathVariable("issueId") Integer issueId, @PathVariable("itemId") Integer itemId, @RequestBody IssueDetailsDto issueItem) {
        return issueService.partiallyApproveIssueItem(issueId, itemId, issueItem);
    }

    @RequestMapping(value = "/{issueId}/item/{issueItemId}/reject", method = RequestMethod.PUT)
    public IssueDetailsDto rejectIssueItem(@PathVariable("issueId") Integer issueId, @PathVariable("issueItemId") Integer issueItemId, @RequestBody IssueDetailsDto issueItem) {
        return issueService.rejectIssueItem(issueId, issueItemId, issueItem);
    }

    @RequestMapping(value = "/{issueId}/item/{issueItemId}/check", method = RequestMethod.PUT)
    public IssueItem checkRejectedItemAlreadyIssued(@PathVariable("issueId") Integer issueId, @PathVariable("issueItemId") Integer issueItemId, @RequestBody IssueDetailsDto issueItem) {
        return issueService.checkRejectedItemAlreadyIssued(issueId, issueItemId, issueItem);
    }

    @RequestMapping(value = "/{issueId}/item/{issueItemId}/reset", method = RequestMethod.PUT)
    public IssueDetailsDto resetIssueItem(@PathVariable("issueId") Integer issueId, @PathVariable("issueItemId") Integer issueItemId, @RequestBody IssueDetailsDto issueItem) {
        return issueService.resetIssueItem(issueId, issueItemId, issueItem);
    }

    @RequestMapping(value = "/{issueId}/item/{issueItemId}/inventory", method = RequestMethod.PUT)
    public void addRejectedItemToInventory(@PathVariable("issueId") Integer issueId, @PathVariable("issueItemId") Integer issueItemId, @RequestBody IssueDetailsDto issueItem) {
        issueService.addRejectedItemToInventory(issueId, issueItemId, issueItem);
    }

    @RequestMapping(value = "/{issueId}/issue/items", method = RequestMethod.PUT)
    public Issue issueItems(@PathVariable("issueId") Integer issueId, @RequestBody Issue issue) {
        return issueService.issueItems(issueId);
    }

    @RequestMapping(value = "/{issueId}/issue/receive", method = RequestMethod.PUT)
    public Issue receiveItems(@PathVariable("issueId") Integer issueId, @RequestBody List<IssueItem> issueItems) {
        return issueService.receiveItems(issueId, issueItems);
    }

    @RequestMapping(value = "/missile/{missileId}/section/{sectionId}", method = RequestMethod.GET)
    public List<IssueReportDto> getIssueReportByMissile(@PathVariable("missileId") Integer missileId, @PathVariable("sectionId") Integer sectionId, @RequestParam("searchText") String searchText) {
        return issueService.getIssueReportByMissile(missileId, sectionId, searchText);
    }

    @RequestMapping(value = "/{personId}/permission", method = RequestMethod.GET)
    public GroupPermission getPermissionByPersonId(@PathVariable("personId") Integer personId) {
        return issueService.getPermissionByPersonId(personId);
    }
}
