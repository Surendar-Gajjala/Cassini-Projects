package com.cassinisys.erp.api.crm;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.CustomerCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.common.ERPObjectGeoLocation;
import com.cassinisys.erp.model.core.ObjectType;
import com.cassinisys.erp.model.crm.*;
import com.cassinisys.erp.service.common.ObjectGeoLocationService;
import com.cassinisys.erp.service.crm.CustomerOrderService;
import com.cassinisys.erp.service.crm.CustomerReturnService;
import com.cassinisys.erp.service.crm.CustomerService;
import com.cassinisys.erp.service.crm.SalesRepFieldReportService;
import com.cassinisys.erp.util.GeoLocation;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by reddy on 7/18/15.
 */
@RestController
@RequestMapping("crm/customers")
@Api(name = "Customers", description = "Customers endpoint", group = "CRM")
public class CustomerController extends BaseController {

	private CustomerService customerService;
	private CustomerOrderService customerOrderService;
	private CustomerReturnService customerReturnService;
	private PageRequestConverter pageRequestConverter;
    private SalesRepFieldReportService customerReportsService;
	private ObjectGeoLocationService objectGeoLocationService;

	@Autowired
	public CustomerController(CustomerService customerService,
							  CustomerReturnService customerReturnService,
							  CustomerOrderService customerOrderService,
                              SalesRepFieldReportService customerReportsService,
							  ObjectGeoLocationService objectGeoLocationService,
							  PageRequestConverter pageRequestConverter) {
		this.customerService = customerService;
		this.customerOrderService = customerOrderService;
        this.customerReportsService = customerReportsService;
		this.objectGeoLocationService = objectGeoLocationService;
		this.pageRequestConverter = pageRequestConverter;
		this.customerReturnService= customerReturnService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ERPCustomer createCustomer(@RequestBody @Valid ERPCustomer customer,
			HttpServletRequest request, HttpServletResponse response) {
		return customerService.create(customer);
	}

	@RequestMapping(value = "/types", method = RequestMethod.GET)
	public List<ERPCustomerType> getCustomerTypes() {
		return customerService.getCustomerTypes();
	}

	@RequestMapping(value = "/types", method = RequestMethod.POST)
	public ERPCustomerType saveCustomerType(
			@RequestBody @Valid ERPCustomerType customerType) {
		return customerService.saveCustomerType(customerType);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPCustomer getCustomerById(@PathVariable("id") Integer id) {
		return customerService.get(id);
	}

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ERPCustomer updateCustomer(@PathVariable("id") Integer id,
                                       @RequestBody ERPCustomer customer) {
        return customerService.update(customer);
    }

	@RequestMapping(value = "/name/region", method = RequestMethod.GET)
	public Boolean getCustomerbyNameAndRegion(@RequestParam("name") String name,
												  @RequestParam("salesRegion") String salesRegion) {
		Boolean aBoolean =  customerService.getCustomerByNameAndRegion(name, salesRegion);
		return aBoolean;
	}

	@RequestMapping(value = "/{id}/orders", method = RequestMethod.GET)
	public List<ERPCustomerOrder> getCustomerOrders(@PathVariable("id") Integer id) {
		ERPCustomer customer = customerService.get(id);
		return customerOrderService.getCustomerOrders(customer);
	}
	@RequestMapping(value = "/{id}/returns", method = RequestMethod.GET)
	public List<ERPCustomerReturn> getCustomerReturns(@PathVariable("id") Integer customerId) {
		return customerReturnService.getCustomerReturnsByCustomer(customerId);
	}

    @RequestMapping(value = "/{id}/reports", method = RequestMethod.GET)
    public Page<ERPSalesRepFieldReport> getCustomerReports(@PathVariable("id") Integer customerId,
                                                           ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customerReportsService.getCustomerReportsByCustomer(customerId, pageable);
    }

	@RequestMapping(method = RequestMethod.GET)
	public Page<ERPCustomer> getAllCustomers(CustomerCriteria criteria,
			ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return customerService.find(criteria, pageable);
	}

	@RequestMapping(value="/search", method = RequestMethod.GET)
	public Page<ERPCustomer> searchCustomers(CustomerCriteria criteria,
											 ERPPageRequest pageRequest) {
		criteria.setFreeTextSearch(true);
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return customerService.find(criteria, pageable);
	}

	@RequestMapping(value = "/pagable", method = RequestMethod.GET)
	public Page<ERPCustomer> getAllCustomers(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return customerService.findAll(pageable);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {
		customerService.delete(id);
	}

	@RequestMapping(value = "/salesregions", method = RequestMethod.GET)
	public Page<ERPSalesRegion> getSalesRegions(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return customerService.getSalesRegions(pageable);
	}

	@RequestMapping(value = "/geosearch/radius", method = RequestMethod.GET)
	public List<ERPObjectGeoLocation> radiusSearch(@RequestParam("latitude") Double latitude,
                                                     @RequestParam("longitude") Double longitude,
                                                     @RequestParam("radius") float radius) {
		return objectGeoLocationService.radiusSearch(latitude, longitude, radius, ObjectType.CUSTOMER);
	}

    @RequestMapping(value = "/geosearch/box", method = RequestMethod.GET)
    public List<ERPObjectGeoLocation> boxSearch(@RequestParam("neLatitude") Double neLatitude,
                                                  @RequestParam("neLongitude") Double neLongitude,
                                                  @RequestParam("swLatitude") Double swLatitude,
                                                  @RequestParam("swLongitude") Double swLongitude) {
        return objectGeoLocationService.boxSearch(new GeoLocation(neLatitude, neLongitude),
                new GeoLocation(swLatitude, swLongitude), ObjectType.CUSTOMER);
    }

	@RequestMapping(value = "/salesregions", method = RequestMethod.POST)
	public ERPSalesRegion saveSalesRegion(
			@RequestBody @Valid ERPSalesRegion region) {
		return customerService.saveSalesRegion(region);
	}

}
