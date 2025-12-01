package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.MESObjectCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mes.MESMBOMInstance;
import com.cassinisys.plm.model.mes.MESProductionOrder;
import com.cassinisys.plm.model.mes.MESProductionOrderItem;
import com.cassinisys.plm.model.mes.dto.ProductionOrderCalenderDto;
import com.cassinisys.plm.model.mes.dto.ProductionOrderDateDto;
import com.cassinisys.plm.model.mes.dto.ProductionOrderDto;
import com.cassinisys.plm.model.mes.dto.ProductionOrderItemDto;
import com.cassinisys.plm.service.mes.ProductionOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mes/productionorders")
@Api(tags = "PLM.MES", description = "MES Related")
public class ProductionOrderController extends BaseController {

    @Autowired
    private ProductionOrderService productionOrderService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESProductionOrder create(@RequestBody MESProductionOrder productionOrder) {
        return productionOrderService.create(productionOrder);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESProductionOrder update(@PathVariable("id") Integer id,
                                     @RequestBody MESProductionOrder productionOrder) {
        return productionOrderService.update(productionOrder);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        productionOrderService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESProductionOrder get(@PathVariable("id") Integer id) {
        return productionOrderService.get(id);
    }

    @RequestMapping(value = "/{id}/counts", method = RequestMethod.GET)
    public ItemDetailsDto getProductionOrderCounts(@PathVariable("id") Integer id) {
        return productionOrderService.getProductionOrderCounts(id);
    }

    @RequestMapping(value = "/{id}/promote", method = RequestMethod.PUT)
    public MESProductionOrder promoteProductionOrder(@PathVariable("id") Integer id, @RequestBody MESProductionOrder productionOrder) {
        return productionOrderService.promoteProductionOrder(id, productionOrder);
    }

    @RequestMapping(value = "/{id}/demote", method = RequestMethod.PUT)
    public MESProductionOrder demoteProductionOrder(@PathVariable("id") Integer id, @RequestBody MESProductionOrder productionOrder) {
        return productionOrderService.demoteProductionOrder(id, productionOrder);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESProductionOrder> getAll() {
        return productionOrderService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<ProductionOrderDto> getAllProductionOrders(PageRequest pageRequest, MESObjectCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return productionOrderService.getAllProductionOrdersByPageable(pageable, criteria);
    }

    @RequestMapping(value = "/calender/all", method = RequestMethod.GET)
    public List<ProductionOrderCalenderDto> getAllCalenderProductionOrders() {
        return productionOrderService.getAllCalenderProductionOrders();
    }

    @RequestMapping(value = "/minmax/dates", method = RequestMethod.GET)
    public ProductionOrderDateDto getProductionOrderMinAndMaxDate() {
        return productionOrderService.getProductionOrderMinAndMaxDate();
    }

    @RequestMapping(value = "/objects/multiple", method = RequestMethod.POST)
    public List<ProductionOrderDto> saveMultipleProductionOrders(@RequestBody List<ProductionOrderDto> productionOrderDtos) {
        return productionOrderService.saveMultipleProductionOrders(productionOrderDtos);
    }

    @RequestMapping(value = "/{id}/items", method = RequestMethod.GET)
    public List<ProductionOrderItemDto> getProductionOrderItems(@PathVariable Integer id) {
        return productionOrderService.getProductionOrderItems(id);
    }

    @RequestMapping(value = "/{id}/items/multiple", method = RequestMethod.POST)
    public List<MESProductionOrderItem> createProductionOrderItems(@PathVariable Integer id, @RequestBody List<MESProductionOrderItem> productionOrderItem) {
        return productionOrderService.createProductionOrderItems(productionOrderItem);
    }

    @RequestMapping(value = "/{id}/items", method = RequestMethod.POST)
    public MESProductionOrderItem createProductionOrderItem(@PathVariable Integer id, @RequestBody MESProductionOrderItem productionOrderItem) {
        return productionOrderService.createProductionOrderItem(productionOrderItem);
    }

    @RequestMapping(value = "/{id}/items/{itemId}", method = RequestMethod.PUT)
    public ProductionOrderItemDto updateProductionOrderItem(@PathVariable Integer id, @PathVariable Integer itemId,
                                                            @RequestBody ProductionOrderItemDto productionOrderItem) {
        return productionOrderService.updateProductionOrderItem(productionOrderItem);
    }

    @RequestMapping(value = "/{id}/items/{itemId}/instance", method = RequestMethod.PUT)
    public MESMBOMInstance updateProductionOrderItemInstance(@PathVariable Integer id, @PathVariable Integer itemId,
                                                             @RequestBody MESMBOMInstance mesmbomInstance) {
        return productionOrderService.updateProductionOrderItemInstance(mesmbomInstance);
    }

    @RequestMapping(value = "/{id}/items/{itemId}", method = RequestMethod.DELETE)
    public void deleteProductionOrderItem(@PathVariable Integer id, @PathVariable Integer itemId) {
        productionOrderService.deleteProductionOrderItem(itemId);
    }

    @RequestMapping(value = "/{id}/items/{itemId}/instance", method = RequestMethod.DELETE)
    public void deleteProductionOrderItemInstance(@PathVariable Integer id, @PathVariable Integer itemId) {
        productionOrderService.deleteProductionOrderItemInstance(itemId);
    }

    @RequestMapping(value = "/{id}/items/{itemId}", method = RequestMethod.GET)
    public MESProductionOrderItem getProductionOrderItem(@PathVariable Integer id, @PathVariable Integer itemId) {
        return productionOrderService.getProductionOrderItem(itemId);
    }
}

