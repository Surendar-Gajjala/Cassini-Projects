package com.cassinisys.is.api.issue;
/**
 * The Class is for IssueController
 **/

import com.cassinisys.is.filtering.IssueCriteria;
import com.cassinisys.is.model.im.ISIssue;
import com.cassinisys.is.model.im.ISIssueStatusHistory;
import com.cassinisys.is.model.im.ProblemPriorityCount;
import com.cassinisys.is.model.pm.ProjectProblemDto;
import com.cassinisys.is.model.tm.DetailsCountDto;
import com.cassinisys.is.service.issue.IssueService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(name = "Issues", description = "Issues endpoint")
@RestController
@RequestMapping("issues")
public class IssueController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private IssueService issueService;

    /**
     * The method used for creating the IsIssue
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISIssue create(@RequestBody ISIssue issue) {
        return issueService.create(issue);
    }

    /**
     * The method used for updating the IsIssue
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISIssue update(@PathVariable("id") Integer id,
                          @RequestBody ISIssue issue) {
        issue.setId(id);
        return issueService.update(issue);
    }

    /**
     * The method used for deleting the IsIssue
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        issueService.delete(id);
    }

    /**
     * The method used to get the IsIssue
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISIssue get(@PathVariable("id") Integer id) {
        return issueService.get(id);
    }

    /**
     * The method used to getall of the ISIssues
     **/
    @RequestMapping(value = "/targetObjectType/{targetObjectType}/targetObjectId/{targetObjectId}", method = RequestMethod.GET)
    public List<ISIssue> getAll(@PathVariable("targetObjectType") ObjectType objectType,
                                @PathVariable("targetObjectId") Integer objectId) {
        if (objectType == null || objectId == null) {
            return issueService.findAll();
        } else {
            return issueService.findByObjectType(objectId, objectType);
        }
    }

    /**
     * The method used to getall of the ISIssues
     **/
    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ISIssue> getPageableIssues(@RequestParam("targetObjectType") ObjectType objectType,
                                           @RequestParam("targetObjectId") Integer objectId,
                                           PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        if (objectType == null || objectId == null) {
            return issueService.getPageableIssues(pageable);
        } else {
            return issueService.getPageableIssues(objectId, objectType, pageable);
        }
    }

    /**
     * The method used to getStatusHistory of the ISIssueStatusHistory
     **/
    @RequestMapping(value = "/{id}/statushistory", method = RequestMethod.GET)
    public Page<ISIssueStatusHistory> getStatusHistory(
            @PathVariable("id") Integer id, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return issueService.getStatusHistory(id, pageable);
    }

    /**
     * The method used to getMultiples of the ISIssue
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISIssue> getMultiple(@PathVariable Integer[] ids) {
        return issueService.findMultiple(Arrays.asList(ids));
    }

    /**
     * The method used for freetextsearch of ISIssue
     **/
    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<ISIssue> freeTextSearch(PageRequest pageRequest, IssueCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISIssue> issues = issueService.freeTextSearch(pageable, criteria);
        return issues;
    }

    @RequestMapping(value = "/count/targetObjectId/{targetObjectId}", method = RequestMethod.GET)
    public ProblemPriorityCount getIssuesCountByPriority(@PathVariable("targetObjectId") Integer objectId) {
        return issueService.getIssuesCountByPriority(objectId);
    }

    @RequestMapping(value = "/{id}/count", method = RequestMethod.GET)
    public DetailsCountDto getIssueDetailsCount(@PathVariable("id") Integer id) {
        return issueService.getIssueDetailsCount(id);
    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public ProjectProblemDto getIssueDetails(@PathVariable("id") Integer id) {
        return issueService.getIssueDetails(id);
    }
}
