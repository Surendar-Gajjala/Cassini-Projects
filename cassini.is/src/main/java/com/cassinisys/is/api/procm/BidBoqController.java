package com.cassinisys.is.api.procm;
/**
 * The Class is for BidBoqController
 **/

import com.cassinisys.is.model.procm.ISBidBoq;
import com.cassinisys.is.model.procm.ISBoqItem;
import com.cassinisys.is.service.procm.BidBoqService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(name = "Bid Boq", description = "Bid Boq endpoint", group = "Bid")
@RestController
@RequestMapping("/bids/{bidId}/boq")
public class BidBoqController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private BidBoqService bidBoqService;

    /**
     * The method used for creating the ISBidBoq
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISBidBoq create(@PathVariable("bidId") Integer bidId,
                           @RequestBody ISBidBoq bidBoq) {
        return bidBoqService.create(bidBoq);
    }

    /**
     * The method used for updating the ISBidBoq
     **/
    @RequestMapping(value = "/{boqId}", method = RequestMethod.PUT)
    public ISBidBoq update(@PathVariable("bidId") Integer bidId,
                           @PathVariable("boqId") Integer boqId, @RequestBody ISBidBoq bidBoq) {
        bidBoq.setId(boqId);
        return bidBoqService.update(bidBoq);
    }

    /**
     * The method used for deleting the ISBidBoq
     **/
    @RequestMapping(value = "/{boqId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("bidId") Integer bidId,
                       @PathVariable("boqId") Integer boqId) {
        bidBoqService.delete(boqId);
    }

    /**
     * The method used to get the value for ISBidBoq
     **/
    @RequestMapping(value = "/{boqId}", method = RequestMethod.GET)
    public ISBidBoq get(@PathVariable("bidId") Integer bidId,
                        @PathVariable("boqId") Integer boqId) {
        return bidBoqService.get(boqId);
    }

    /**
     * The method used to get list of items for ISBoqItem by boqId
     **/
    @RequestMapping(value = "/{boqId}/items", method = RequestMethod.GET)
    public List<ISBoqItem> getItems(@PathVariable("bidId") Integer bidId,
                                    @PathVariable("boqId") Integer boqId) {
        return bidBoqService.getItems(boqId);
    }

    /**
     * The method used to addItem for  the ISBoqItem
     **/
    @RequestMapping(value = "/{boqId}/items", method = RequestMethod.POST)
    public ISBoqItem addItem(@PathVariable("bidId") Integer bidId,
                             @PathVariable("boqId") Integer boqId, @RequestBody ISBoqItem item) {
        item.setBoq(boqId);
        return bidBoqService.addItem(item);
    }

    /**
     * The method used to updateItem for  the ISBoqItem
     **/
    @RequestMapping(value = "/{boqId}/items/{itemId}",
            method = RequestMethod.PUT)
    public ISBoqItem updateItem(@PathVariable("bidId") Integer bidId,
                                @PathVariable("boqId") Integer boqId,
                                @PathVariable("itemId") Integer itemId, @RequestBody ISBoqItem item) {
        item.setId(itemId);
        item.setBoq(boqId);
        return bidBoqService.updateItem(item);
    }

    /**
     * The method used to deleteItem
     **/
    @RequestMapping(value = "/{boqId}/items/{itemId}",
            method = RequestMethod.DELETE)
    public void deleteItem(@PathVariable("bidId") Integer bidId,
                           @PathVariable("boqId") Integer boqId,
                           @PathVariable("itemId") Integer itemId) {
        bidBoqService.deleteItem(itemId);
    }

    /**
     * The method used to getMultiple for  the ISBidBoq
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISBidBoq> getMultiple(@PathVariable Integer[] ids) {
        return bidBoqService.findMultiple(Arrays.asList(ids));
    }

}
