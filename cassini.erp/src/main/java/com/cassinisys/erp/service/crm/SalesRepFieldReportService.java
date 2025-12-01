package com.cassinisys.erp.service.crm;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.api.filtering.CustomerReportPredicateBuilder;
import com.cassinisys.erp.model.api.criteria.CustomerReportCriteria;
import com.cassinisys.erp.model.crm.ERPSalesRepFieldReport;
import com.cassinisys.erp.model.crm.QERPSalesRepFieldReport;
import com.cassinisys.erp.repo.crm.SalesRepFieldReportRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
public class SalesRepFieldReportService implements
		CrudService<ERPSalesRepFieldReport, Integer>,
		PageableService<ERPSalesRepFieldReport, Integer> {

	@Autowired
	SalesRepFieldReportRepository fieldReportRepository;


	@Autowired
	CustomerReportPredicateBuilder customerReportPredicateBuilder;



	@Override
	public ERPSalesRepFieldReport create(ERPSalesRepFieldReport fieldReport) {
		fieldReport.setTimestamp(new Date());
		return fieldReportRepository.save(fieldReport);

	}
	@Override
	public Page<ERPSalesRepFieldReport> findAll(Pageable pageable) {
		return fieldReportRepository.findAll(pageable);
	}
	@Override
	public ERPSalesRepFieldReport update(ERPSalesRepFieldReport fieldReport) {
		checkNotNull(fieldReport);
		checkNotNull(fieldReport.getId());
		if (fieldReportRepository.findOne(fieldReport.getId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		fieldReport.setTimestamp(new Date());
		return fieldReportRepository.save(fieldReport);
	}

	@Override
	public void delete(Integer fieldReportId) {
		checkNotNull(fieldReportId);
		fieldReportRepository.delete(fieldReportId);
	}

	@Override
	public ERPSalesRepFieldReport get(Integer fieldReportId) {
		return fieldReportRepository.findOne(fieldReportId);
	}

	@Override
	public List<ERPSalesRepFieldReport> getAll() {
		return fieldReportRepository.findAll();
	}

    public Page<ERPSalesRepFieldReport> getSalesRepFieldReports(Integer salesRepId, Pageable pageable) {
        return fieldReportRepository.findBySalesRepId(salesRepId, pageable);
    }


	public Page<ERPSalesRepFieldReport> getCustomerReports(CustomerReportCriteria criteria, Pageable pageable) {
		Predicate predicate = customerReportPredicateBuilder.build(criteria, QERPSalesRepFieldReport.eRPSalesRepFieldReport);
		return fieldReportRepository.findAll(predicate, pageable);
	}

    public Page<ERPSalesRepFieldReport> getCustomerReportsByCustomer(Integer customerId, Pageable pageable) {
        return fieldReportRepository.findByCustomerId(customerId, pageable);
    }
}
