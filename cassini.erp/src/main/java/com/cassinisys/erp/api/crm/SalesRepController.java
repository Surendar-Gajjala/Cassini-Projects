package com.cassinisys.erp.api.crm;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.api.filtering.CustomerOrderPredicateBuilder;
import com.cassinisys.erp.api.filtering.CustomerPredicateBuilder;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.CustomerCriteria;
import com.cassinisys.erp.model.api.criteria.CustomerOrderCriteria;
import com.cassinisys.erp.model.api.criteria.CustomerReportCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.crm.*;
import com.cassinisys.erp.repo.crm.CustomerOrderRepository;
import com.cassinisys.erp.repo.crm.CustomerRepository;
import com.cassinisys.erp.service.crm.SalesRepService;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("crm/salesreps")
@Api(name="SalesReps",description="SalesReps endpoint",group="CRM")
public class SalesRepController extends BaseController {
	@Autowired
	private PageRequestConverter pageRequestConverter;

	@Autowired
	SalesRepService salesRepService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerPredicateBuilder customerPredicateBuilder;

    @Autowired
    private CustomerOrderPredicateBuilder customerOrderPredicateBuilder;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;


	@RequestMapping(method = RequestMethod.GET)
	public Page<ERPSalesRep> getAllSalesReps(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return salesRepService.getAllSalesRep(pageable);
	}


	@RequestMapping(method = RequestMethod.POST)
	public ERPSalesRep createSalesRep(@RequestBody @Valid ERPSalesRep salesRep,
			HttpServletRequest request, HttpServletResponse response) {
		return salesRepService.createSalesRep(salesRep);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPSalesRep getSalesRepById(@PathVariable("id") Integer id) {
		return salesRepService.getSalesRepById(id);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPSalesRep update(@PathVariable("id") Integer id,
			@RequestBody ERPSalesRep salesRep) {
		salesRep.setId(id);
		return salesRepService.updateSalesRep(salesRep);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {
		salesRepService.deleteSalesRep(id);
	}

	@RequestMapping(value = "/{id}/customers", method = RequestMethod.GET)
	public Page<ERPCustomer> getAllCustomersOfSalesRep(@PathVariable("id") Integer id,
						CustomerCriteria criteria, ERPPageRequest pageRequest) {
        Predicate predicate = customerPredicateBuilder.build(criteria, QERPCustomer.eRPCustomer);
        Predicate salesRepPredicate = QERPCustomer.eRPCustomer.salesRep.id.eq(id);
        predicate = ExpressionUtils.allOf(salesRepPredicate, predicate);
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return customerRepository.findAll(predicate, pageable);
	}

    @RequestMapping(value = "/{id}/orders", method = RequestMethod.GET)
    public Page<ERPCustomerOrder> getAllOrdersOfSalesRep(@PathVariable("id") Integer id,
                        CustomerOrderCriteria criteria, ERPPageRequest pageRequest) {
        Predicate predicate = customerOrderPredicateBuilder.build(criteria, QERPCustomerOrder.eRPCustomerOrder);
        Predicate salesRepPredicate = QERPCustomerOrder.eRPCustomerOrder.customer.salesRep.id.eq(id);
        predicate = ExpressionUtils.allOf(salesRepPredicate, predicate);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customerOrderRepository.findAll(predicate, pageable);
    }

	@RequestMapping(value = "/{id}/regions", method = RequestMethod.GET)
	public List<ERPSalesRegion> getRegionsOfSalesRep(
			@PathVariable("id") Integer id) {
		return salesRepService.getSalesRepRegions(id);
	}

	@RequestMapping(value = "/{id}/fieldreports", method = RequestMethod.GET)
	public Page<ERPSalesRepFieldReport> getSalesRepFieldReports(@PathVariable("id") Integer id,
					ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return salesRepService.getSalesRepFieldReports(id, pageable);
	}

	@RequestMapping(value = "/fieldreports", method = RequestMethod.GET)
	public Page<ERPSalesRepFieldReport> getSalesRepFieldReports(CustomerReportCriteria criteria,
																ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return salesRepService.getCustomerReports(criteria, pageable);
	}
}
