package com.cassinisys.is.api.pm;
/**
 * The Class is for BidController
 **/

import com.cassinisys.is.model.col.ISBidMeeting;
import com.cassinisys.is.model.col.ISBidMessage;
import com.cassinisys.is.model.dm.ISBidFolder;
import com.cassinisys.is.model.im.ISIssue;
import com.cassinisys.is.model.pm.ISBid;
import com.cassinisys.is.model.procm.ISBidBoq;
import com.cassinisys.is.model.procm.ISBidRfq;
import com.cassinisys.is.model.tm.ISBidTask;
import com.cassinisys.is.service.pm.BidService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(name = "Bids", description = "Bids endpoint", group = "Bid")
@RestController
@RequestMapping("/bids")
public class BidController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private BidService bidService;

    /**
     * The method used for creating the ISBid
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISBid create(@RequestBody ISBid bid) {
        return bidService.create(bid);
    }

    /**
     * The method used for updating the ISBid
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISBid update(@PathVariable("id") Integer id, @RequestBody ISBid bid) {
        bid.setId(id);
        return bidService.update(bid);
    }

    /**
     * The method used delete the ISBid
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        bidService.delete(id);
    }

    /**
     * The method used to get the value of  ISBid
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISBid get(@PathVariable("id") Integer id) {
        return bidService.get(id);
    }

    /**
     * The method used to getall the values of  ISBid
     **/
    @RequestMapping(method = RequestMethod.GET)
    public Page<ISBid> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bidService.findAll(pageable);
    }

    /**
     * The method used to getTasks of  ISBidTask
     **/
    @RequestMapping(value = "/{id}/tasks", method = RequestMethod.GET)
    public Page<ISBidTask> getTasks(@PathVariable("id") Integer id,
                                    PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bidService.getTasks(id, pageable);
    }

    /**
     * The method used to getFolders of ISBidFolder
     **/
    @RequestMapping(value = "/{id}/folders", method = RequestMethod.GET)
    public Page<ISBidFolder> getFolders(@PathVariable("id") Integer id,
                                        PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bidService.getRootFolders(id, pageable);
    }

    /**
     * The method used to getIssues  of  ISStockIssue
     **/
    @RequestMapping(value = "/{id}/issues", method = RequestMethod.GET)
    public Page<ISIssue> getIssues(@PathVariable("id") Integer id,
                                   PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bidService.getIssues(id, pageable);
    }

    /**
     * The method used to getMessages  of  ISBidMessage
     **/
    @RequestMapping(value = "/{id}/messages", method = RequestMethod.GET)
    public Page<ISBidMessage> getMessages(@PathVariable("id") Integer id,
                                          PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bidService.getMessages(id, pageable);
    }

    /**
     * The method used to getMeetings  of  ISBidMeeting
     **/
    @RequestMapping(value = "/{id}/meetings", method = RequestMethod.GET)
    public Page<ISBidMeeting> getMeetings(@PathVariable("id") Integer id,
                                          PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bidService.getMeetings(id, pageable);
    }

    /**
     * The method used to getBoqs  of  ISBidBoq
     **/
    @RequestMapping(value = "/{id}/boqs", method = RequestMethod.GET)
    public Page<ISBidBoq> getBoqs(@PathVariable("id") Integer id,
                                  PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bidService.getBoqs(id, pageable);
    }

    /**
     * The method used to getRfqs  of  ISBidRfq
     **/
    @RequestMapping(value = "/{id}/rfqs", method = RequestMethod.GET)
    public Page<ISBidRfq> getRfqs(@PathVariable("id") Integer id,
                                  PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bidService.getRfqs(id, pageable);
    }

    /**
     * The method used to getMultiples  of  ISBid
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISBid> getMultiple(@PathVariable Integer[] ids) {
        return bidService.findMultiple(Arrays.asList(ids));
    }

}
