package com.cassinisys.is.api.tm;
/**
 * The Class is for BidTaskController
 **/

import com.cassinisys.is.filtering.BidTaskCriteria;
import com.cassinisys.is.model.im.ISIssue;
import com.cassinisys.is.model.tm.ISBidTask;
import com.cassinisys.is.model.tm.ISTaskAssignedTo;
import com.cassinisys.is.model.tm.ISTaskObserver;
import com.cassinisys.is.model.tm.ISTaskStatusHistory;
import com.cassinisys.is.service.tm.BidTaskService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(name = "Bid tasks", description = "Bid tasks endpoint", group = "Bid")
@RestController
@RequestMapping("/bids/{bidId}/tasks")
public class BidTaskController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private BidTaskService bidTaskService;

    /**
     * The method used for creating the ISBidTask
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISBidTask create(@PathVariable("bidId") Integer bidId,
                            @RequestBody ISBidTask bidTask) {
        return bidTaskService.create(bidTask);
    }

    /**
     * The method used for updating the ISBidTask
     **/
    @RequestMapping(value = "/{taskId}", method = RequestMethod.PUT)
    public ISBidTask update(@PathVariable("bidId") Integer bidId,
                            @PathVariable("taskId") Integer taskId,
                            @RequestBody ISBidTask bidTask) {
        bidTask.setId(taskId);
        return bidTaskService.update(bidTask);
    }

    /**
     * The method used for deleting the ISBidTask
     **/
    @RequestMapping(value = "/{taskId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("bidId") Integer bidId,
                       @PathVariable("taskId") Integer taskId) {
        bidTaskService.delete(taskId);
    }

    /**
     * The method used to get the value of ISBidTask
     **/
    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public ISBidTask get(@PathVariable("bidId") Integer bidId,
                         @PathVariable("taskId") Integer taskId) {
        return bidTaskService.get(taskId);
    }

    /**
     * The method used to getAssignedTo from the list of person
     **/
    @RequestMapping(value = "/{taskId}/assignedTo", method = RequestMethod.GET)
    public List<Person> getAssignedTo(@PathVariable("bidId") Integer bidId,
                                      @PathVariable("taskId") Integer taskId) {
        return bidTaskService.getAssignedTo(taskId);
    }

    /**
     * The method used to addAssignedTo to ISTaskAssignedTo
     **/
    @RequestMapping(value = "/{taskId}/assignedTo/{personId}",
            method = RequestMethod.PUT)
    public ISTaskAssignedTo addAssignedTo(@PathVariable("bidId") Integer bidId,
                                          @PathVariable("taskId") Integer taskId,
                                          @PathVariable("personId") Integer personId) {
        return bidTaskService.addAssignedTo(taskId, personId);
    }

    /**
     * The method used to deleteAssignedTo from ISTaskAssignedTo
     **/
    @RequestMapping(value = "/{taskId}/assignedTo/{personId}",
            method = RequestMethod.DELETE)
    public void deleteAssignedTo(@PathVariable(value = "bidId") Integer bidId,
                                 @PathVariable("taskId") Integer taskId,
                                 @PathVariable("personId") Integer personId) {
        bidTaskService.deleteAssignedTo(taskId, personId);
    }

    /**
     * The method used to getObservers from Person
     **/
    @RequestMapping(value = "/{taskId}/observers", method = RequestMethod.GET)
    public Page<Person> getObservers(@PathVariable("bidId") Integer bidId,
                                     @PathVariable("taskId") Integer taskId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bidTaskService.getObservers(taskId, pageable);
    }

    /**
     * The method used to addObservers to ISTaskObserver
     **/
    @RequestMapping(value = "/{taskId}/observers/{personId}",
            method = RequestMethod.PUT)
    public ISTaskObserver addObservers(@PathVariable("bidId") Integer bidId,
                                       @PathVariable("taskId") Integer taskId,
                                       @PathVariable("personId") Integer personId) {
        return bidTaskService.addObservers(taskId, personId);
    }

    /**
     * The method used to deleteObservers
     **/
    @RequestMapping(value = "/{taskId}/observers/{personId}",
            method = RequestMethod.DELETE)
    public void deleteObservers(@PathVariable(value = "bidId") Integer bidId,
                                @PathVariable("taskId") Integer taskId,
                                @PathVariable("personId") Integer personId) {
        bidTaskService.deleteObservers(taskId, personId);
    }

    /**
     * The method used to getStatusHistory of  ISTaskStatusHistory
     **/
    @RequestMapping(value = "/{taskId}/statushistory",
            method = RequestMethod.GET)
    public Page<ISTaskStatusHistory> getStatusHistory(
            @PathVariable("bidId") Integer bidId,
            @PathVariable("taskId") Integer taskId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bidTaskService.getStatusHistory(taskId, pageable);
    }

    /**
     * The method used to getIssues of  ISStockIssue
     **/
    @RequestMapping(value = "/{taskId}/issues", method = RequestMethod.GET)
    public Page<ISIssue> getIssues(@PathVariable("bidId") Integer bidId,
                                   @PathVariable("taskId") Integer taskId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bidTaskService.getIssues(taskId, pageable);
    }

    /**
     * The method used to getAll values from ISBidTask
     **/
    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ISBidTask> getAll(PageRequest pageRequest, BidTaskCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bidTaskService.findAll(criteria, pageable);

    }

    /**
     * The method used to getMultiple values from the list of ISBidTask
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISBidTask> getMultiple(@PathVariable Integer[] ids) {
        return bidTaskService.findMultiple(Arrays.asList(ids));
    }

}
