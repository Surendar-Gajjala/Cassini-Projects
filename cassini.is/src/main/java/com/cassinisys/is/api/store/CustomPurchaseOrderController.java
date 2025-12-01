package com.cassinisys.is.api.store;

import com.cassinisys.is.filtering.CustomPurchaseOrderCriteria;
import com.cassinisys.is.model.procm.ISSupplier;
import com.cassinisys.is.model.store.CustomPurchaseOrder;
import com.cassinisys.is.model.store.CustomPurchaseOrderItem;
import com.cassinisys.is.model.store.PurchaseOrderStatus;
import com.cassinisys.is.service.store.CustomPurchaseOrderService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@RestController
@RequestMapping("is/stores/purchaseOrders")
@Api(name = "CustomPurchaseOrder", description = "CustomPurchaseOrder endpoint", group = "IS")
public class CustomPurchaseOrderController extends BaseController {
    @Autowired
    private CustomPurchaseOrderService customPurchaseOrderService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

   /*  methods for CustomPurchaseOrder */

    @RequestMapping(method = RequestMethod.POST)
    public CustomPurchaseOrder create(@RequestBody CustomPurchaseOrder purchaseOrder) {
        purchaseOrder.setStatus(PurchaseOrderStatus.NEW);
        return customPurchaseOrderService.create(purchaseOrder);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CustomPurchaseOrder update(@PathVariable("id") Integer id, @RequestBody CustomPurchaseOrder purchaseOrder) {
        purchaseOrder.setId(id);
        return customPurchaseOrderService.update(purchaseOrder);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        customPurchaseOrderService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CustomPurchaseOrder get(@PathVariable("id") Integer id) {
        return customPurchaseOrderService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CustomPurchaseOrder> get() {
        return customPurchaseOrderService.getAll();
    }

    @RequestMapping(value = "/byStore/{storeId}/pageable", method = RequestMethod.GET)
    public Page<CustomPurchaseOrder> getPageableIndents(@PathVariable("storeId") Integer storeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customPurchaseOrderService.getPageablePurchaseOrders(storeId, pageable);
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<CustomPurchaseOrder> getAllPurchaseOrders(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customPurchaseOrderService.getAllPurchaseOrders(pageable);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<CustomPurchaseOrder> customIndentFreeTextSearch(PageRequest pageRequest, CustomPurchaseOrderCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<CustomPurchaseOrder> customPurchaseOrders = customPurchaseOrderService.customIndentFreeTextSearch(pageable, criteria);
        return customPurchaseOrders;
    }

     /*  methods for CustomPurchaseOrderItem */

    @RequestMapping(value = "/{id}/purchaseOrderItem", method = RequestMethod.POST)
    public List<CustomPurchaseOrderItem> createPurchaseOrderItem(@PathVariable("id") Integer id, @RequestBody List<CustomPurchaseOrderItem> purchaseOrderItems) {
        return customPurchaseOrderService.createPurchaseOrderItems(purchaseOrderItems);
    }

    @RequestMapping(value = "/{id}/purchaseOrderItem/{purchaseOrderItemId}", method = RequestMethod.PUT)
    public CustomPurchaseOrderItem updatePurchaseOrderItem(
            @PathVariable("id") Integer id,
            @PathVariable("purchaseOrderItemId") Integer purchaseOrderItemId,
            @RequestBody CustomPurchaseOrderItem purchaseOrderItem) {
        purchaseOrderItem.setId(purchaseOrderItemId);
        return customPurchaseOrderService.updatePurchaseOrderItem(id, purchaseOrderItem);
    }

    @RequestMapping(value = "/{id}/purchaseOrderItem/{purchaseOrderItemId}", method = RequestMethod.DELETE)
    public void deletePurchaseOrderItem(@PathVariable("id") Integer id, @PathVariable("purchaseOrderItemId") Integer purchaseOrderItemId) {
        customPurchaseOrderService.deletePurchaseOrderItem(purchaseOrderItemId);
    }

    @RequestMapping(value = "/{id}/purchaseOrderItem/{purchaseOrderItem}", method = RequestMethod.GET)
    public CustomPurchaseOrderItem getPurchaseOrderItem(@PathVariable("id") Integer id,
                                                        @PathVariable("purchaseOrderItem") Integer purchaseOrderItemId) {
        return customPurchaseOrderService.getPurchaseOrderItem(purchaseOrderItemId);
    }

    @RequestMapping(value = "/{id}/customPurchaseOrderItems", method = RequestMethod.GET)
    public List<CustomPurchaseOrderItem> getAllPurchaseOrderItems() {
        return customPurchaseOrderService.getAllPurchaseOrderItems();
    }

    @RequestMapping(value = "/{poId}/supplier", method = RequestMethod.GET)
    public ISSupplier getSupplierByPurchaseOrderId(@PathVariable("poId") Integer poId) {
        return customPurchaseOrderService.getSupplierByPurchaseOrderId(poId);
    }

    @RequestMapping(value = "/multiple/{poIds}", method = RequestMethod.GET)
    public List<CustomPurchaseOrder> getPurchaseOrdersByIds(@PathVariable("poIds") List<Integer> poIds) {
        return customPurchaseOrderService.getPurchaseOrdersByIds(poIds);
    }
}
