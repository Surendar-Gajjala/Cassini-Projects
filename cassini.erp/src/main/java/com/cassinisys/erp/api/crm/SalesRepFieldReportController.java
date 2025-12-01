package com.cassinisys.erp.api.crm;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.crm.ERPSalesRepFieldReport;
import com.cassinisys.erp.service.crm.SalesRepFieldReportService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("crm/salesrepfieldreports")
@Api(name="SalesRepFieldReports",description="SalesRepFieldReports endpoint",group="CRM")
public class SalesRepFieldReportController extends BaseController {

	private SalesRepFieldReportService fieldReportService;

	private PageRequestConverter pageRequestConverter;

	@Autowired
	public SalesRepFieldReportController(
			SalesRepFieldReportService fieldReportService,
			PageRequestConverter pageRequestConverter) {
		this.pageRequestConverter = pageRequestConverter;
		this.fieldReportService = fieldReportService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ERPSalesRepFieldReport createFieldReport(
			@RequestBody @Valid ERPSalesRepFieldReport fieldReport,
			HttpServletRequest request, HttpServletResponse response) {
		fieldReport.setTimestamp(new Date());
		return fieldReportService.create(fieldReport);
	}

	@RequestMapping(value = "/{fieldReportId}", method = RequestMethod.GET)
	public ERPSalesRepFieldReport getFieldReportById(
			@PathVariable("fieldReportId") Integer fieldReportId) {
		return fieldReportService.get(fieldReportId);
	}

	@RequestMapping(value = "/{fieldReportId}", method = RequestMethod.PUT)
	public ERPSalesRepFieldReport update(
			@PathVariable("fieldReportId") Integer fieldReportId,
			@RequestBody ERPSalesRepFieldReport fieldReport) {

		fieldReport.setId(fieldReportId);
		return fieldReportService.update(fieldReport);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPSalesRepFieldReport> getAllFieldReports() {
		return fieldReportService.getAll();
	}

	@RequestMapping(value = "/pagable", method = RequestMethod.GET)
	public Page<ERPSalesRepFieldReport> getAllFieldReports(
			ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return fieldReportService.findAll(pageable);
	}


	@RequestMapping(value = "/{fieldReportId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("fieldReportId") Integer fieldReportId) {
		fieldReportService.delete(fieldReportId);
	}

}
