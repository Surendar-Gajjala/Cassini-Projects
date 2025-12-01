package com.cassinisys.is.api.col;
/**
 * The Class is for BidMessage Controller
 **/

import com.cassinisys.is.model.col.ISBidMessage;
import com.cassinisys.is.service.col.BidMessageService;
import com.cassinisys.platform.api.core.BaseController;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(name = "Bid messages",
        description = "Bid messages endpoint",
        group = "Bid")
@RestController
@RequestMapping("/bids/{bidId}/messages")
public class BidMessageController extends BaseController {

    @Autowired
    private BidMessageService bidMessageService;

    /**
     * The method used for creating the ISBIDMessage
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISBidMessage send(@PathVariable("bidId") Integer bidId,
                             @RequestBody ISBidMessage message) {
        return bidMessageService.create(message);
    }

    /**
     * The method used for deleting the ISBIDMessage
     **/
    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("bidId") Integer bidId,
                       @PathVariable("messageId") Integer messageId) {
        bidMessageService.delete(messageId);
    }

    /**
     * The method used to obtain(or get) the ISBidMessage
     **/
    @RequestMapping(value = "/{messageId}", method = RequestMethod.GET)
    public ISBidMessage get(@PathVariable("bidId") Integer bidId,
                            @PathVariable("messageId") Integer messageId) {
        return bidMessageService.get(messageId);
    }

    /**
     * The method used to getMultiples of ISBidMessage
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISBidMessage> getMultiple(@PathVariable Integer[] ids) {
        return bidMessageService.findMultiple(Arrays.asList(ids));
    }

}
