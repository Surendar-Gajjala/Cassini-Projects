package com.cassinisys.is.api.issue;
/**
 * The Class is for IssueTypeController
 **/

import com.cassinisys.is.model.im.ISIssueType;
import com.cassinisys.is.service.issue.IssueTypeService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(name = "Issue types", description = "Issue types management")
@RestController
@RequestMapping("issuetypes")
public class IssueTypeController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private IssueTypeService issueTypeService;

    /**
     * The method is used to create ISIssueType
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISIssueType create(@RequestBody ISIssueType issueType) {
        issueType.setId(null);
        return issueTypeService.create(issueType);
    }

    /**
     * The method is used to update ISIssueType
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISIssueType update(@PathVariable("id") Integer id,
                              @RequestBody ISIssueType issueType) {
        issueType.setId(id);
        return issueTypeService.update(issueType);
    }

    /**
     * The method is used to delete ISIssueType
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        issueTypeService.delete(id);
    }

    /**
     * The method is used to get ISIssueType
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISIssueType get(@PathVariable("id") Integer id) {
        return issueTypeService.get(id);
    }

    /**
     * The method is used to findall of ISIssueType
     **/
    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ISIssueType> findAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return issueTypeService.findAll(pageable);
    }

    /**
     * The method is used to getall of ISIssueType
     **/
    @RequestMapping(method = RequestMethod.GET)
    public List<ISIssueType> getAll() {
        return issueTypeService.getAll();
    }

}
