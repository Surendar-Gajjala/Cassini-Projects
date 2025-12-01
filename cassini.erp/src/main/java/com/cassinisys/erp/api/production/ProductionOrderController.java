package com.cassinisys.erp.api.production;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.cassinisys.erp.model.production.ERPProductionOrderItem;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.production.ERPProductionOrder;
import com.cassinisys.erp.service.production.ProductionOrderService;

@RestController
@RequestMapping("production/orders")
@Api(name="ProductionOrders",description="ProductionOrders endpoint",group="PRODUCTION")
public class ProductionOrderController extends BaseController {

	private ProductionOrderService productionOrderService;

	private PageRequestConverter pageRequestConverter;

	@Autowired
	public ProductionOrderController(ProductionOrderService productionOrderService,
			PageRequestConverter pageRequestConverter) {
		this.pageRequestConverter = pageRequestConverter;
		this.productionOrderService = productionOrderService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ERPProductionOrder createProductionOrder(
			@RequestBody @Valid ERPProductionOrder productionOrder,
			HttpServletRequest request, HttpServletResponse response) {

		return productionOrderService.create(productionOrder);

	}

	@RequestMapping(value = "/{orderId}/approvedby/{empId}", method = RequestMethod.GET)
	public Boolean approveOrder(@PathVariable("orderId") Integer orderId,
			@PathVariable("empId") Integer empId) {

		return productionOrderService.approveOrder(empId, orderId);

	}

	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
	public ERPProductionOrder getProductionOrderById(
			@PathVariable("orderId") Integer orderId) {

		return productionOrderService.get(orderId);

	}

	@RequestMapping(value = "/{orderId}", method = RequestMethod.PUT)
	public ERPProductionOrder update(@PathVariable("orderId") Integer orderId,
			@RequestBody ERPProductionOrder productionOrder) {

		productionOrder.setId(orderId);
		return productionOrderService.update(productionOrder);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPProductionOrder> getAllProductionOrders() {

		return productionOrderService.getAll();

	}
	@RequestMapping(value = "/pagable", method = RequestMethod.GET)
	public Page<ERPProductionOrder> getAllProductionOrders(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return productionOrderService.findAll(pageable);


	}

	@RequestMapping(value = "/{orderId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("orderId") Integer orderId) {

		productionOrderService.delete(orderId);
	}

	@RequestMapping(value = "/{orderId}/details", method = RequestMethod.GET)
	public List<ERPProductionOrderItem> getAllOrderDetailsForOrder(
			@PathVariable("orderId") Integer orderId) {

		return productionOrderService.getAllOrderDetailsByOrderId(orderId);
	}

}
