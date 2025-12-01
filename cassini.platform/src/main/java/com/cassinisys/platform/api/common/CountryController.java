package com.cassinisys.platform.api.common;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Country;
import com.cassinisys.platform.model.common.State;
import com.cassinisys.platform.service.common.CountryService;
import com.cassinisys.platform.service.common.StateService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author reddy
 */
@RestController
@RequestMapping("/common/countries")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class CountryController extends BaseController {

	@Autowired
	private PageRequestConverter pageConverter;

	@Autowired
	private CountryService countryService;

	@Autowired
	private StateService stateService;

	@RequestMapping(method = RequestMethod.POST)
	public Country create(@RequestBody Country country) {
		country.setId(null);
		return countryService.create(country);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Country update(@PathVariable("id") Integer id,
			@RequestBody Country country) {
		country.setId(id);
		return countryService.update(country);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {
		countryService.delete(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Country get(@PathVariable("id") Integer id) {
		return countryService.get(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public Page<Country> getAll(PageRequest page) {
		Pageable pageable = pageConverter.convert(page);
		return countryService.findAll(pageable);
	}

	@RequestMapping(value = "/{id}/states", method = RequestMethod.GET)
	public Page<State> getStates(@PathVariable("id") Integer id,
			PageRequest page) {
		Pageable pageable = pageConverter.convert(page);
		return countryService.getStates(id, pageable);
	}

	@RequestMapping(value = "/states", method = RequestMethod.GET)
	public List<ModelMap> getCountriesAndStates() {
		List<ModelMap> maps = new ArrayList<>();

		List<Country> countries = countryService.getAll();
		for(Country country : countries) {
			ModelMap map = new ModelMap();
			map.addAttribute("country", country);
			List<State> states = stateService.findByCountry(country.getId());
			map.addAttribute("states", states);

			maps.add(map);
		}

		return maps;
	}
	
	@RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
	public List<Country> getMultiple(@PathVariable Integer[] ids ) {
		return countryService.findMultiple(Arrays.asList(ids));
	}
}
