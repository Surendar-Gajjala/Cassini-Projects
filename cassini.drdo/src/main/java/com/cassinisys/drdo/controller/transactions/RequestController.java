
package com.cassinisys.drdo.controller.transactions;

import com.cassinisys.drdo.filtering.RequestCriteria;
import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.dto.NewIssueDto;
import com.cassinisys.drdo.model.dto.RequestReportDto;
import com.cassinisys.drdo.model.dto.RequestSummary;
import com.cassinisys.drdo.model.dto.RequestedItemDto;
import com.cassinisys.drdo.model.transactions.Request;
import com.cassinisys.drdo.model.transactions.RequestItem;
import com.cassinisys.drdo.service.transactions.RequestService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subra on 17-10-2018.
 */
@RestController
@RequestMapping("drdo/requests")
public class RequestController extends BaseController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public Request create(@RequestBody Request request) {
        return requestService.create(request);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Request update(@PathVariable("id") Integer id,
                          @RequestBody Request request) {
        return requestService.update(request);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        requestService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Request get(@PathVariable("id") Integer id) {
        return requestService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Request> getAll() {
        return requestService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<Request> getAllRequests(PageRequest pageRequest, RequestCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return requestService.getAllRequests(pageable, criteria);
    }

    @RequestMapping(value = "/finished", method = RequestMethod.GET)
    public List<Request> getAllFinishedRequests() {
        return requestService.getAllFinishedRequests();
    }

    @RequestMapping(value = "/approve/{id}", method = RequestMethod.PUT)
    public Request approveRequest(@PathVariable("id") Integer id, @RequestBody Request request) {
        return requestService.approveRequest(request);
    }

    @RequestMapping(value = "/accept/{id}", method = RequestMethod.PUT)
    public Request acceptRequest(@PathVariable("id") Integer id, @RequestBody Request request) {
        return requestService.acceptRequest(request);
    }

    @RequestMapping(value = "/accept/item/{id}", method = RequestMethod.PUT)
    public RequestItem acceptRequestItem(@PathVariable("id") Integer id, @RequestBody RequestItem requestItem) {
        return requestService.acceptRequestItem(requestItem);
    }

    @RequestMapping(value = "/approve/item/{id}", method = RequestMethod.PUT)
    public RequestItem approveRequestItem(@PathVariable("id") Integer id, @RequestBody RequestItem requestItem) {
        return requestService.approveRequestItem(requestItem);
    }

    @RequestMapping(value = "/reject/{id}", method = RequestMethod.PUT)
    public Request rejectRequest(@PathVariable("id") Integer id, @RequestBody Request request) {
        return requestService.rejectRequest(request);
    }

    @RequestMapping(value = "/{requestId}/items", method = RequestMethod.POST)
    public List<RequestItem> createRequestItems(@PathVariable("requestId") Integer requestId, @RequestBody List<RequestItem> requestItems) {
        return requestService.createRequestItems(requestId, requestItems);
    }

    @RequestMapping(value = "/{requestId}/items", method = RequestMethod.GET)
    public List<RequestedItemDto> getRequestItems(@PathVariable("requestId") Integer requestId) {
        return requestService.getRequestItems(requestId);
    }

    @RequestMapping(value = "/{requestId}/items/{itemId}", method = RequestMethod.PUT)
    public void rejectRequestItem(@PathVariable("requestId") Integer requestId, @PathVariable("itemId") Integer itemId, @RequestBody RequestItem requestItem) {
        requestService.rejectRequestItem(requestId, requestItem);
    }

    @RequestMapping(value = "/{requestId}/requestItems", method = RequestMethod.GET)
    List<NewIssueDto> getRequestedItems(@PathVariable("requestId") Integer requestId) {
        return requestService.getRequestedItems(requestId);
    }

    @RequestMapping(value = "/instance/{instanceId}", method = RequestMethod.GET)
    public List<Request> getRequestsByInstance(@PathVariable("instanceId") Integer instanceId) {
        return requestService.getRequestsByInstance(instanceId);
    }

    @RequestMapping(value = "/{requestId}/report", method = RequestMethod.GET)
    public RequestReportDto getRequestReport(@PathVariable("requestId") Integer requestId) {
        return requestService.getRequestReport(requestId);
    }

    @RequestMapping(value = "/{requestId}/item/{itemId}/validate/upn", method = RequestMethod.GET)
    public ItemInstance validateUpnNumber(@PathVariable("requestId") Integer requestId, @PathVariable("itemId") Integer itemId, @RequestParam("upnNumber") String upnNumber) {
        return requestService.validateUpnNumber(requestId, itemId, upnNumber);
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    private List<RequestSummary> getRequestSummary() {
        return requestService.getRequestSummary();
    }

    @RequestMapping(value = "/summary/{instanceId}", method = RequestMethod.GET)
    private List<RequestSummary> getRequestSummaryByInstance(@PathVariable("instanceId") Integer instanceId) {
        return requestService.getRequestSummaryByInstance(instanceId);
    }

    @RequestMapping(value = "/{requestId}/summary", method = RequestMethod.GET)
    private List<RequestSummary> getSummaryByRequest(@PathVariable("requestId") Integer requestId) {
        return requestService.getSummaryByRequest(requestId);
    }

    @RequestMapping(value = "/update/{requestNumber}", method = RequestMethod.GET)
    public Request updateRequestByNumber(@PathVariable("requestNumber") Integer requestNumber, @RequestParam("partName") String partName) {
        return requestService.updateRequestByNumber(requestNumber, partName);
    }
}
