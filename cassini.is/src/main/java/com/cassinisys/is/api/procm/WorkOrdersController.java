package com.cassinisys.is.api.procm;

import com.cassinisys.is.filtering.WorkOrderCriteria;
import com.cassinisys.is.model.procm.ISWorkOrder;
import com.cassinisys.is.model.procm.ISWorkOrderItem;
import com.cassinisys.is.service.procm.WorkOrdersService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by swapna on 22/01/19.
 */
@RestController
@RequestMapping("is/contracts/workOrders")
public class WorkOrdersController extends BaseController {

    @Autowired
    private WorkOrdersService workOrdersService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public ISWorkOrder create(@RequestBody ISWorkOrder workOrder) {
        return workOrdersService.create(workOrder);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ISWorkOrder> getAll() {
        return workOrdersService.getAll();
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ISWorkOrder> getPageableWorkOrder(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workOrdersService.getPageableWorkOrders(pageable);
    }

    @RequestMapping(value = "/{workOrderId}", method = RequestMethod.GET)
    public ISWorkOrder get(@PathVariable("workOrderId") Integer workOrderId) {
        return workOrdersService.get(workOrderId);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ISWorkOrder update(@RequestBody ISWorkOrder workOrder) {
        return workOrdersService.update(workOrder);
    }

    @RequestMapping(value = "/{workOrderId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("workOrderId") Integer workOrderId) {
        workOrdersService.delete(workOrderId);
    }

    @RequestMapping(value = "/contractor/{contractorId}", method = RequestMethod.GET)
    public List<ISWorkOrder> findByContractor(@PathVariable("contractorId") Integer contractorId) {
        return workOrdersService.findByContractor(contractorId);
    }

    @RequestMapping(value = "/{workOrderId}/items", method = RequestMethod.POST)
    public ISWorkOrderItem createWorkOrderItem(@RequestBody ISWorkOrderItem workOrderItem) {
        return workOrdersService.createWorkOrderItem(workOrderItem);
    }

    @RequestMapping(value = "/{workOrderId}/items/multiple", method = RequestMethod.POST)
    public List<ISWorkOrderItem> createWorkOrderItems(@RequestBody List<ISWorkOrderItem> workOrderItems) {
        return workOrdersService.createWorkOrderItems(workOrderItems);
    }

    @RequestMapping(value = "/{workOrderId}/items", method = RequestMethod.GET)
    public List<ISWorkOrderItem> getWorkOrderItems(@PathVariable("workOrderId") Integer workOrderId) {
        return workOrdersService.getWorkOrderItems(workOrderId);
    }

    @RequestMapping(value = "/{workOrderId}/items/pageable", method = RequestMethod.GET)
    public Page<ISWorkOrderItem> getPageableWorkOrderItems(@PathVariable("workOrderId") Integer workOrderId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workOrdersService.getPageableWorkOrderItems(workOrderId, pageable);
    }

    @RequestMapping(value = "/{workOrderId}/items/{workOrderItemId}", method = RequestMethod.GET)
    public ISWorkOrderItem getWorkOrderItem(@PathVariable("workOrderItemId") Integer workOrderItemId) {
        return workOrdersService.getWorkOrderItem(workOrderItemId);
    }

    @RequestMapping(value = "/{workOrderId}/items", method = RequestMethod.PUT)
    public ISWorkOrderItem updateWorkOrderItem(@RequestBody ISWorkOrderItem workOrderItem) {
        return workOrdersService.updateWorkOrderItem(workOrderItem);
    }

    @RequestMapping(value = "/{workOrderId}/items/{workOrderItemId}", method = RequestMethod.DELETE)
    public void deleteWorkOrderItem(@PathVariable("workOrderItemId") Integer workOrderItemId) {
        workOrdersService.deleteWorkOrderItem(workOrderItemId);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<ISWorkOrder> workOrdersFreeTextSearch(PageRequest pageRequest, WorkOrderCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workOrdersService.contractorsFreeTextSearch(pageable, criteria);
    }
}
