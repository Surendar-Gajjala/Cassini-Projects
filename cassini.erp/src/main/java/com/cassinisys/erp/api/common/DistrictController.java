package com.cassinisys.erp.api.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.common.ERPDistrict;
import com.cassinisys.erp.service.common.DistrictService;

@RestController
@RequestMapping("common/districts")
@Api(name="Districts",description="Districts endpoint",group="COMMON")
public class DistrictController extends BaseController {

	@Autowired
	DistrictService districtService; 

	@RequestMapping(method = RequestMethod.POST)
	public ERPDistrict createState(@RequestBody @Valid ERPDistrict district,
			HttpServletRequest request, HttpServletResponse response) {

		if (district != null) {

			return districtService.createDistrict(district);
		}
		return null;

	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPDistrict> getAllDistricts() {
		return districtService.getAllDistricts();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPDistrict getDistrictById(@PathVariable("id") Integer id) {
		return districtService.getDistrictById(id);
	}
	
	
	@RequestMapping(value = "/state/{id}", method = RequestMethod.GET)
	public List<ERPDistrict> getDistrictByState(@PathVariable("id") Integer id) {
		return districtService.getDistrictByState(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPDistrict update(@PathVariable("id") Integer id,
			@RequestBody ERPDistrict district) {
		district.setId(id);
		return districtService.updateDistrict(district);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {
		districtService.deleteDistrict(id);
	}

}
