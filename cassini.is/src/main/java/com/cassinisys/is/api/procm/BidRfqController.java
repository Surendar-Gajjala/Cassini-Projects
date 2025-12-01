package com.cassinisys.is.api.procm;
/**
 * The Class is for BidRfqController
 **/

import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.model.procm.dto.RfqRecipientsTO;
import com.cassinisys.is.model.procm.dto.RfqResponseTO;
import com.cassinisys.is.service.procm.BidRfqService;
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

@Api(name = "Bid Rfq", description = "Bid Rfq endpoint", group = "Bid")
@RestController
@RequestMapping("/bids/{bidId}/rfqs")
public class BidRfqController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private BidRfqService rfqService;

    /**
     * The method used for creating the ISBidRfq
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISBidRfq create(@PathVariable("bidId") Integer bidId,
                           @RequestBody ISBidRfq rfq) {
        return rfqService.create(rfq);
    }

    /**
     * The method used for updating the ISBidRfq
     **/
    @RequestMapping(value = "/{rfqId}", method = RequestMethod.PUT)
    public ISBidRfq update(@PathVariable("bidId") Integer bidId,
                           @PathVariable("rfqId") Integer rfqId, @RequestBody ISBidRfq rfq) {
        rfq.setId(rfqId);
        return rfqService.update(rfq);
    }

    /**
     * The method used for deleting the ISBidRfq
     **/
    @RequestMapping(value = "/{rfqId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("bidId") Integer bidId,
                       @PathVariable("rfqId") Integer rfqId) {
        rfqService.delete(rfqId);
    }

    /**
     * The method used get the value of ISBidRfq
     **/
    @RequestMapping(value = "/{rfqId}", method = RequestMethod.GET)
    public ISBidRfq get(@PathVariable("bidId") Integer bidId,
                        @PathVariable("rfqId") Integer rfqId) {
        return rfqService.get(rfqId);
    }

    /**
     * The method used getItems of ISRfqItem
     **/
    @RequestMapping(value = "/{rfqId}/items", method = RequestMethod.GET)
    public Page<ISRfqItem> getItems(@PathVariable("bidId") Integer bidId,
                                    @PathVariable("rfqId") Integer rfqId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return rfqService.getItems(rfqId, pageable);
    }

    /**
     * The method used to addItem in ISRfqItem
     **/
    @RequestMapping(value = "/{rfqId}/items", method = RequestMethod.POST)
    public ISRfqItem addItem(@PathVariable("bidId") Integer bidId,
                             @PathVariable("rfqId") Integer rfqId, @RequestBody ISRfqItem item) {
        item.setRfq(rfqId);
        return rfqService.addItem(item);
    }

    /**
     * The method used to updateItem in ISRfqItem
     **/
    @RequestMapping(value = "/{rfqId}/items/{itemId}",
            method = RequestMethod.PUT)
    public ISRfqItem updateItem(@PathVariable("bidId") Integer bidId,
                                @PathVariable("rfqId") Integer rfqId,
                                @PathVariable("itemId") Integer itemId, @RequestBody ISRfqItem item) {
        item.setId(itemId);
        item.setRfq(rfqId);
        return rfqService.updateItem(item);
    }

    /**
     * The method used to deleteItem in ISRfqItem
     **/
    @RequestMapping(value = "/{rfqId}/items/{itemId}",
            method = RequestMethod.DELETE)
    public void deleteItem(@PathVariable("bidId") Integer bidId,
                           @PathVariable("rfqId") Integer rfqId,
                           @PathVariable("itemId") Integer itemId) {
        rfqService.deleteItem(itemId);
    }

    /**
     * The method used to sendRfq
     **/
    @RequestMapping(value = "/{rfqId}/recipients", method = RequestMethod.PUT)
    public void sendRfq(@PathVariable("bidId") Integer bidId,
                        @PathVariable("rfqId") Integer rfqId,
                        @RequestBody RfqRecipientsTO recipients) {
        rfqService.sendRfq(rfqId, recipients);
    }

    /**
     * The method used to getRecipients in ISRfqSentTo
     **/
    @RequestMapping(value = "/{rfqId}/recipients", method = RequestMethod.GET)
    public Page<ISRfqSentTo> getRecipients(
            @PathVariable("bidId") Integer bidId,
            @PathVariable("rfqId") Integer rfqId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return rfqService.getRecipients(rfqId, pageable);
    }

    /**
     * The method used for responseToRfq in ISRfqResponse
     **/
    @RequestMapping(value = "/{rfqId}/responses", method = RequestMethod.PUT)
    public ISRfqResponse response(@PathVariable("bid") Integer bidId,
                                  @PathVariable("rfqId") Integer rfqId,
                                  @RequestBody RfqResponseTO response) {
        return rfqService.responseToRfq(rfqId, response);
    }

    /**
     * The method used to getResponses in ISRfqResponse
     **/
    @RequestMapping(value = "/{rfqId}/responses", method = RequestMethod.GET)
    public Page<ISRfqResponse> getResponses(
            @PathVariable("bidId") Integer bidId,
            @PathVariable("rfqId") Integer rfqId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return rfqService.getResponses(rfqId, pageable);
    }

    /**
     * The method used to getResponseItems in ISRfqResponseItem
     **/
    @RequestMapping(value = "/{rfqId}/responses/{responseId}/items",
            method = RequestMethod.GET)
    public Page<ISRfqResponseItem> getResponseItems(
            @PathVariable("bidId") Integer bidId,
            @PathVariable("rfqId") Integer rfqId,
            @PathVariable("responseId") Integer responseId,
            PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return rfqService.getResponseItems(responseId, pageable);
    }

    /**
     * The method used to getMultiple values of ISBidRfq
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISBidRfq> getMultiple(@PathVariable Integer[] ids) {
        return rfqService.findMultiple(Arrays.asList(ids));
    }
}
