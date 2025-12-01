package com.cassinisys.erp.api.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.common.ERPCountry;
import com.cassinisys.erp.model.common.ERPState;
import com.cassinisys.erp.service.common.CountryService;
import com.cassinisys.erp.service.common.StateService;

@RestController
@RequestMapping("common/countries")
@Api(name="Countries",description="Countries endpoint",group="COMMON")
public class CountryController extends BaseController {

	@Autowired
	CountryService countryService;

	@Autowired
	StateService stateService;

	@RequestMapping(method = RequestMethod.POST)
	public ERPCountry createCountry(@RequestBody @Valid ERPCountry country,
			HttpServletRequest request, HttpServletResponse response) {

		if (country != null) {
			return countryService.createCountry(country);
		}
		return null;

	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPCountry> getAllCountries() {
		return countryService.getAllCountries();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPCountry getCountryById(@PathVariable("id") Integer id) {
		return countryService.getCountryById(id);
	}

	@RequestMapping(value = "/{id}/states", method = RequestMethod.GET)
	public List<ERPState> getStates(@PathVariable("id") Integer id) {
		return stateService.getStatesByCountry(id);
	}

	@RequestMapping(value = "/states", method = RequestMethod.GET)
	public List<ModelMap> getCountriesAndStates() {
		List<ModelMap> maps = new ArrayList<>();

		List<ERPCountry> countries = countryService.getAllCountries();
		for(ERPCountry country : countries) {
			ModelMap map = new ModelMap();
			map.addAttribute("country", country);
			List<ERPState> states = stateService.getStatesByCountry(country.getId());
			map.addAttribute("states", states);

			maps.add(map);
		}

		return maps;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPCountry update(@PathVariable("id") Integer id,
			@RequestBody ERPCountry country) {
		country.setId(id);
		return countryService.updateCountry(country);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {
		countryService.deleteCountry(id);
	}

}
