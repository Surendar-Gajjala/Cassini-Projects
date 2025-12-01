package com.cassinisys.is.api.pm;
/**
 * The Class is for BidWbsController
 **/

import com.cassinisys.is.model.pm.ISBidWbs;
import com.cassinisys.is.service.pm.BidWbsService;
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

/**
 * Created by Home on 4/15/2016.
 */
@Api(name = "Bid Wbs",
        description = "Bid Wbs endpoint", group = "Bid Wbs")
@RestController
@RequestMapping("/bids/{bidId}/bidWbs")
public class BidWbsController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private BidWbsService bidWbsService;

    /**
     * The method used for creating the ISBidWbs
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISBidWbs create(@PathVariable("bidId") Integer bidId,
                           @RequestBody ISBidWbs bidWbs) {
        return bidWbsService.create(bidWbs);
    }

    /**
     * The method used for updating the ISBidWbs
     **/
    @RequestMapping(value = "/{bidWbsId}", method = RequestMethod.PUT)
    public ISBidWbs update(@PathVariable("bidWbsId") Integer bidId,
                           @PathVariable("bidWbsId") Integer bidWbsId,
                           @RequestBody ISBidWbs bidWbs) {
        bidWbs.setId(bidWbsId);
        return bidWbsService.update(bidWbs);
    }

    /**
     * The method used for deleting the ISBidWbs
     **/
    @RequestMapping(value = "/{bidWbsId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("bidId") Integer bidId,
                       @PathVariable("bidWbsId") Integer bidWbsId) {
        bidWbsService.delete(bidWbsId);
    }

    /**
     * The method used to get the value of ISBidWbs
     **/
    @RequestMapping(value = "/{bidWbsId}", method = RequestMethod.GET)
    public ISBidWbs get(@PathVariable("bidId") Integer bidId,
                        @PathVariable("bidWbsId") Integer bidWbsId) {
        return bidWbsService.get(bidWbsId);
    }

    /**
     * The method used to getall the values of ISBidWbs
     **/
    @RequestMapping(method = RequestMethod.GET)
    public Page<ISBidWbs> getAll(PageRequest page) {
        Pageable pageable = pageRequestConverter.convert(page);
        return bidWbsService.findAll(pageable);
    }

    /**
     * The method used to getmultiples  of ISBidWbs
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISBidWbs> getMultiple(@PathVariable Integer[] ids) {
        return bidWbsService.findMultiple(Arrays.asList(ids));
    }

}
