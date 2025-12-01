package com.cassinisys.erp.service.crm;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.api.criteria.CustomerReportCriteria;
import com.cassinisys.erp.model.crm.ERPCustomer;
import com.cassinisys.erp.model.crm.ERPSalesRegion;
import com.cassinisys.erp.model.crm.ERPSalesRep;
import com.cassinisys.erp.model.crm.ERPSalesRepFieldReport;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.repo.crm.CustomerRepository;
import com.cassinisys.erp.repo.crm.SalesRepRepository;
import com.cassinisys.erp.repo.hrm.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
public class SalesRepService {

	@Autowired
	SalesRepRepository salesRepRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	private SalesRepFieldReportService fieldReportService;

	@Autowired
	SalesRegionService salesRegionService;

	@Autowired
	EmployeeRepository employeeRepository;



	public ERPSalesRep createSalesRep(ERPSalesRep salesRep) {
		//return salesRepRepository.save(salesRep);

		ERPEmployee emp = employeeRepository.findOne(salesRep.getId());
		if(emp == null) {
			throw new ERPException(HttpStatus.NOT_ACCEPTABLE, ErrorCodes.RESOURCE_NOT_FOUND, "Selected person is not an employee");
		}

		salesRepRepository.createSalesRep(salesRep.getId());

		return salesRep;

	}

	public ERPSalesRep updateSalesRep(ERPSalesRep salesRep) {

		checkNotNull(salesRep);
		checkNotNull(salesRep.getId());
		if (salesRepRepository.findOne(salesRep.getId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return salesRepRepository.save(salesRep);

	}

	public ERPSalesRep getSalesRepById(Integer repId) {
		return salesRepRepository.findOne(repId);

	}

	public void deleteSalesRep(Integer repId) {
		checkNotNull(repId);
		salesRepRepository.delete(repId);
	}

	public List<ERPSalesRep> getAllSalesRep() {
		return salesRepRepository.findAll();
	}

	public Page<ERPSalesRep> getAllSalesRep(Pageable pageable) {
		return salesRepRepository.findAll(pageable);
	}

	public Page<ERPCustomer> getAllCustomerForSalesRep(Integer repId, Pageable pageable) {

		ERPSalesRep salesRep = salesRepRepository.findOne(repId);

		if (salesRep != null) {
			return customerRepository.findBySalesRep(repId, pageable);

		} else {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}

	}

	public List<ERPSalesRegion> getSalesRepRegions(Integer repId) {
		ERPSalesRep salesRep = salesRepRepository.findOne(repId);
		List<ERPSalesRegion> regions;

		if (salesRep != null) {
			regions = customerRepository.getSalesRegionsBySalesRep(salesRep);

		} else {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}

		return regions;
	}

	public Page<ERPSalesRepFieldReport> getCustomerReports(CustomerReportCriteria criteria, Pageable pageable) {
		return fieldReportService.getCustomerReports(criteria, pageable);
	}

	public Page<ERPSalesRepFieldReport> getSalesRepFieldReports(Integer salesRepId, Pageable pageable) {
		return fieldReportService.getSalesRepFieldReports(salesRepId, pageable);
	}

}
