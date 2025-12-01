package com.cassinisys.is.api.procm;
/**
 * The Class is for ProjectRfqController
 **/

import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.model.procm.dto.RfqRecipientsTO;
import com.cassinisys.is.model.procm.dto.RfqResponseTO;
import com.cassinisys.is.service.procm.ProjectRfqService;
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

@Api(name = "Project Rfq",
        description = "Project Rfq endpoint",
        group = "Project")
@RestController
@RequestMapping("/projects/{projectId}/rfqs")
public class ProjectRfqController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private ProjectRfqService rfqService;

    /**
     * The method used for creating the ISProjectRfq
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISProjectRfq create(@PathVariable("projectId") Integer projectId,
                               @RequestBody ISProjectRfq rfq) {
        return rfqService.create(rfq);
    }

    /**
     * The method used for updating the ISProjectRfq
     **/
    @RequestMapping(value = "/{rfqId}", method = RequestMethod.PUT)
    public ISProjectRfq update(@PathVariable("projectId") Integer projectId,
                               @PathVariable("rfqId") Integer rfqId, @RequestBody ISProjectRfq rfq) {
        rfq.setId(rfqId);
        return rfqService.update(rfq);
    }

    /**
     * The method used for deleting the ISProjectRfq
     **/
    @RequestMapping(value = "/{rfqId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("projectId") Integer projectId,
                       @PathVariable("rfqId") Integer rfqId) {
        rfqService.delete(rfqId);
    }

    /**
     * The method used to get the value of ISProjectRfq
     **/
    @RequestMapping(value = "/{rfqId}", method = RequestMethod.GET)
    public ISProjectRfq get(@PathVariable("projectId") Integer projectId,
                            @PathVariable("rfqId") Integer rfqId) {
        return rfqService.get(rfqId);
    }

    /**
     * The method used to getItems of ISRfqItem
     **/
    @RequestMapping(value = "/{rfqId}/items", method = RequestMethod.GET)
    public Page<ISRfqItem> getItems(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("rfqId") Integer rfqId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return rfqService.getItems(rfqId, pageable);
    }

    /**
     * The method used to addItem for ISRfqItem
     **/
    @RequestMapping(value = "/{rfqId}/items", method = RequestMethod.POST)
    public ISRfqItem addItem(@PathVariable("projectId") Integer projectId,
                             @PathVariable("rfqId") Integer rfqId, @RequestBody ISRfqItem item) {
        item.setRfq(rfqId);
        return rfqService.addItem(item);
    }

    /**
     * The method used to updateItem for ISRfqItem
     **/
    @RequestMapping(value = "/{rfqId}/items/{itemId}",
            method = RequestMethod.PUT)
    public ISRfqItem updateItem(@PathVariable("projectId") Integer projectId,
                                @PathVariable("rfqId") Integer rfqId,
                                @PathVariable("itemId") Integer itemId, @RequestBody ISRfqItem item) {
        item.setId(itemId);
        item.setRfq(rfqId);
        return rfqService.updateItem(item);
    }

    /**
     * The method used to deleteItem
     **/
    @RequestMapping(value = "/{rfqId}/items/{itemId}",
            method = RequestMethod.DELETE)
    public void deleteItem(@PathVariable("projectId") Integer projectId,
                           @PathVariable("rfqId") Integer rfqId,
                           @PathVariable("itemId") Integer itemId) {
        rfqService.deleteItem(itemId);
    }

    /**
     * The method used to sendRfq to specific recipients
     **/
    @RequestMapping(value = "/{rfqId}/recipients", method = RequestMethod.PUT)
    public void sendRfq(@PathVariable("projectId") Integer projectId,
                        @PathVariable("rfqId") Integer rfqId,
                        @RequestBody RfqRecipientsTO recipients) {
        rfqService.sendRfq(rfqId, recipients);
    }

    /**
     * The method used to getRecipients of ISRfqSentTo
     **/
    @RequestMapping(value = "/{rfqId}/recipients", method = RequestMethod.GET)
    public Page<ISRfqSentTo> getRecipients(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("rfqId") Integer rfqId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return rfqService.getRecipients(rfqId, pageable);
    }

    /**
     * The method used for the response  of ISRfqResponse
     **/
    @RequestMapping(value = "/{rfqId}/responses", method = RequestMethod.PUT)
    public ISRfqResponse response(@PathVariable("projectId") Integer projectId,
                                  @PathVariable("rfqId") Integer rfqId,
                                  @RequestBody RfqResponseTO response) {
        return rfqService.responseToRfq(rfqId, response);
    }

    /**
     * The method used to getResponses for ISRfqResponse
     **/
    @RequestMapping(value = "/{rfqId}/responses", method = RequestMethod.GET)
    public Page<ISRfqResponse> getResponses(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("rfqId") Integer rfqId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return rfqService.getResponses(rfqId, pageable);
    }

    /**
     * The method used to getResponseItems for ISRfqResponseItem
     **/
    @RequestMapping(value = "/{rfqId}/responses/{responseId}/items",
            method = RequestMethod.GET)
    public Page<ISRfqResponseItem> getResponseItems(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("rfqId") Integer rfqId,
            @PathVariable("responseId") Integer responseId,
            PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return rfqService.getResponseItems(responseId, pageable);
    }

    /**
     * The method used to getMultiple values for ISProjectRfq
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISProjectRfq> getMultiple(@PathVariable Integer[] ids) {
        return rfqService.findMultiple(Arrays.asList(ids));
    }

}
