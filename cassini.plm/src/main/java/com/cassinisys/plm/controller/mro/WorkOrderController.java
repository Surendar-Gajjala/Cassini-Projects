package com.cassinisys.plm.controller.mro;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.WorkOrderCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.mro.dto.WorkOrderDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.service.mro.WorkOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * Created by Suresh Cassini on 18-11-2020.
 */
@RestController
@RequestMapping("/mro/workorders")
@Api(tags = "PLM.MRO", description = "MRO Related")
public class WorkOrderController extends BaseController {
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MROWorkOrder create(@RequestBody MROWorkOrder workOrder) {
        return workOrderService.create(workOrder);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MROWorkOrder update(@PathVariable("id") Integer id,
                               @RequestBody MROWorkOrder workOrder) {
        return workOrderService.update(workOrder);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        workOrderService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MROWorkOrder get(@PathVariable("id") Integer id) {
        return workOrderService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MROWorkOrder> getAll() {
        return workOrderService.getAll();
    }

    @RequestMapping(value = "/{id}/count", method = RequestMethod.GET)
    public ItemDetailsDto getTabCounts(@PathVariable("id") Integer id) {
        return workOrderService.getTabCounts(id);
    }

    @RequestMapping(value = "/{id}/promoteworkorder", method = RequestMethod.GET)
    public MROWorkOrder promoteWorkOrderStatus(@PathVariable("id") Integer id) {
        return workOrderService.promoteWorkOrderStatus(id);
    }

    @RequestMapping(value = "/{id}/holdworkorder", method = RequestMethod.GET)
    public MROWorkOrder holdWorkOrder(@PathVariable("id") Integer id) {
        return workOrderService.holdWorkOrder(id);
    }

    @RequestMapping(value = "/{id}/removeonhold", method = RequestMethod.GET)
    public MROWorkOrder removeOnHoldWorkOrder(@PathVariable("id") Integer id) {
        return workOrderService.removeOnHoldWorkOrder(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<WorkOrderDto> getAllWorkOrders(PageRequest pageRequest, WorkOrderCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workOrderService.getAllWorkOrders(pageable, criteria);
    }

    @RequestMapping(value = "/create/attributes/multiple", method = RequestMethod.POST)
    public void saveObjectAttributes(@RequestBody List<MROObjectAttribute> attributes) {
        workOrderService.savemroObjectAttributes(attributes);
    }

    @RequestMapping(value = "/update/attributes", method = RequestMethod.PUT)
    public MROObjectAttribute updateObjectAttribute(@PathVariable("id") Integer id,
                                                    @RequestBody MROObjectAttribute attribute) {
        return workOrderService.updateObjectAttribute(attribute);
    }

    @RequestMapping(value = "/uploadimageattribute/{objectid}/{attributeid}", method = RequestMethod.POST)
    public MROWorkOrder saveImageAttributeValue(@PathVariable("objectid") Integer objectId,
                                                @PathVariable("attributeid") Integer attributeId, MultipartHttpServletRequest request) {
        return workOrderService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MROWorkOrder> getPartsByType(@PathVariable("typeId") Integer id,
                                             PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workOrderService.getWorkOrdersByType(id, pageable);
    }

    @RequestMapping(value = "/{workOrderId}/operations", method = RequestMethod.GET)
    public List<MROWorkOrderOperation> getWorkOrderOperations(@PathVariable("workOrderId") Integer workOrderId) {
        return workOrderService.getWorkOrderOperations(workOrderId);
    }

    @RequestMapping(value = "/{workOrderId}/operations/{operationId}", method = RequestMethod.PUT)
    public MROWorkOrderOperation updateWorkOrderOperation(@PathVariable("workOrderId") Integer workOrderId, @PathVariable("operationId") Integer operationId,
                                                          @RequestBody MROWorkOrderOperation workOrderOperation) {
        return workOrderService.updateWorkOrderOperation(workOrderOperation);
    }

    @RequestMapping(value = "/{workOrderId}/parts/multiple", method = RequestMethod.POST)
    public List<MROWorkOrderPart> createWorkOrderSpareParts(@PathVariable("workOrderId") Integer workOrderId, @RequestBody List<MROWorkOrderPart> workOrderParts) {
        return workOrderService.createWorkOrderSpareParts(workOrderId, workOrderParts);
    }

    @RequestMapping(value = "/{workOrderId}/parts", method = RequestMethod.GET)
    public List<MROWorkOrderPart> getWorkOrderSpareParts(@PathVariable("workOrderId") Integer workOrderId) {
        return workOrderService.getWorkOrderSpareParts(workOrderId);
    }

    @RequestMapping(value = "/{workOrderId}/parts", method = RequestMethod.POST)
    public MROWorkOrderPart createWorkOrderSparePart(@PathVariable("workOrderId") Integer workOrderId, @RequestBody MROWorkOrderPart workOrderPart) {
        return workOrderService.createWorkOrderSparePart(workOrderPart);
    }

    @RequestMapping(value = "/{workOrderId}/parts/{partId}", method = RequestMethod.PUT)
    public MROWorkOrderPart updateWorkOrderSparePart(@PathVariable("workOrderId") Integer workOrderId, @PathVariable("partId") Integer partId,
                                                     @RequestBody MROWorkOrderPart workOrderPart) {
        return workOrderService.updateWorkOrderSparePart(workOrderPart);
    }

    @RequestMapping(value = "/{workOrderId}/parts/{partId}", method = RequestMethod.DELETE)
    public void deleteWorkOrderSparePart(@PathVariable("workOrderId") Integer workOrderId, @PathVariable("partId") Integer partId) {
        workOrderService.deleteWorkOrderSparePart(partId);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return workOrderService.attachWOWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/workflow/{typeId}/mroType/{type}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getWorkflows(@PathVariable("typeId") Integer typeId,
                                                    @PathVariable("type") String type) {
        return workOrderService.getHierarchyWorkflows(typeId, type);
    }

    /*
    *
    * Work Order Resources
    * */

    @RequestMapping(value = "/{workorderid}/resources/multiple", method = RequestMethod.POST)
    public List<MROWorkOrderResource> createWorkOrderResources(@PathVariable("workorderid") Integer workOrderId, @RequestBody List<MROWorkOrderResource> workOrderResources) {
        return workOrderService.createWorkOrderResources(workOrderId, workOrderResources);
    }

    @RequestMapping(value = "/{workorderid}/resources", method = RequestMethod.GET)
    public List<MROWorkOrderResource> getWorkOrderResourcess(@PathVariable("workorderid") Integer workOrderId) {
        return workOrderService.getAllWorkOrderResources(workOrderId);
    }

    @RequestMapping(value = "/{workorderid}/resources/{resourceid}", method = RequestMethod.DELETE)
    public void deleteWorkOrderResource(@PathVariable("workorderid") Integer workOrderId, @PathVariable("resourceid") Integer resourceId) {
        workOrderService.deleteWorkOrderResource(resourceId);
    }

    @RequestMapping(value = "/{workorderId}/instructions", method = RequestMethod.POST)
    public MROWorkOrderInstructions createWorkOrderInstructions(@PathVariable("workorderId") Integer workorderId,
                                                                      @RequestBody MROWorkOrderInstructions instructions) {
        return  workOrderService.createWorkOrderInstructions(workorderId, instructions);
    }

    @RequestMapping(value = "/{workorderId}/instructions", method = RequestMethod.GET)
    public MROWorkOrderInstructions getWorkOrderInstructions(@PathVariable("workorderId") Integer workorderId) {
        return workOrderService.getWorkOrderInstructions(workorderId);
    }
}