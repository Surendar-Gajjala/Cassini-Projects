package com.cassinisys.is.api.procm;
/**
 * The Class is for RequisitionController
 **/

import com.cassinisys.is.model.procm.ISRequisitionApproval;
import com.cassinisys.is.model.procm.ISRequisitionItem;
import com.cassinisys.is.model.procm.ISRequisitionRequest;
import com.cassinisys.is.service.procm.RequisitionService;
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

@Api(name = "Requisition", description = "Requisitions endpoint")
@RestController
@RequestMapping("/requisitions")
public class RequisitionController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private RequisitionService requisitionService;

    /**
     * The method used for creating the ISRequisitionRequest
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISRequisitionRequest create(
            @RequestBody ISRequisitionRequest requisition) {
        return requisitionService.create(requisition);
    }

    /**
     * The method used for updating the ISRequisitionRequest
     **/
    @RequestMapping(value = "/{reqId}", method = RequestMethod.PUT)
    public ISRequisitionRequest update(@PathVariable("reqId") Integer reqId,
                                       @RequestBody ISRequisitionRequest requisition) {
        requisition.setId(reqId);
        return requisitionService.update(requisition);
    }

    /**
     * The method used for deleting the ISRequisitionRequest
     **/
    @RequestMapping(value = "/{reqId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("reqId") Integer reqId) {
        requisitionService.delete(reqId);
    }

    /**
     * The method used to get the value of  ISRequisitionRequest
     **/
    @RequestMapping(value = "/{reqId}", method = RequestMethod.GET)
    public ISRequisitionRequest get(@PathVariable("reqId") Integer reqId) {
        return requisitionService.get(reqId);
    }

    /**
     * The method used to getall the values of  ISRequisitionRequest
     **/
    @RequestMapping(method = RequestMethod.GET)
    public Page<ISRequisitionRequest> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return requisitionService.findAll(pageable);
    }

    /**
     * The method used to approve the ISRequisitionApproval
     **/
    @RequestMapping(value = "/{reqId}/approvals", method = RequestMethod.POST)
    public void approve(@PathVariable("reqId") Integer reqId,
                        @RequestBody ISRequisitionApproval approval) {
        requisitionService.approve(reqId, approval);
    }

    /**
     * The method used to getApprovals for the list of ISRequisitionApproval
     **/
    @RequestMapping(value = "/{reqId}/approvals", method = RequestMethod.GET)
    public Page<ISRequisitionApproval> getApprovals(
            @PathVariable("reqId") Integer reqId,
            @RequestBody PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return requisitionService.getApprovals(reqId, pageable);
    }

    /**
     * The method used to addItem for  ISRequisitionItem
     **/
    @RequestMapping(value = "/{reqId}/items", method = RequestMethod.POST)
    public ISRequisitionItem addItem(@PathVariable("reqId") Integer reqId,
                                     @RequestBody ISRequisitionItem item) {
        item.setRequisition(reqId);
        return requisitionService.addItem(item);
    }

    /**
     * The method used to updateItem for  ISRequisitionItem
     **/
    @RequestMapping(value = "/{reqId}/items/{itemId}",
            method = RequestMethod.PUT)
    public ISRequisitionItem updateItem(@PathVariable("reqId") Integer reqId,
                                        @PathVariable("itemId") Integer itemId,
                                        @RequestBody ISRequisitionItem item) {
        item.setId(itemId);
        item.setRequisition(reqId);
        return requisitionService.updateItem(item);
    }

    /**
     * The method used to deleteItem for  ISRequisitionItem
     **/
    @RequestMapping(value = "/{reqId}/items/{itemId}",
            method = RequestMethod.DELETE)
    public void deleteItem(@PathVariable("reqId") Integer reqId,
                           @PathVariable("itemId") Integer itemId) {
        requisitionService.deleteItem(itemId);
    }

    /**
     * The method used to getItems for  ISRequisitionItem
     **/
    @RequestMapping(value = "/{reqId}/items", method = RequestMethod.GET)
    public Page<ISRequisitionItem> getItems(
            @PathVariable("reqId") Integer reqId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return requisitionService.getItems(reqId, pageable);
    }

    /**
     * The method used to getMultiple values of  ISRequisitionRequest
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISRequisitionRequest> getMultiple(@PathVariable Integer[] ids) {
        return requisitionService.findMultiple(Arrays.asList(ids));
    }

}
