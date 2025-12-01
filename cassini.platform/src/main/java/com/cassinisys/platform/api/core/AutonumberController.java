package com.cassinisys.platform.api.core;

import com.cassinisys.platform.filtering.AutoNumberCriteria;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by reddy on 7/2/15.
 */
@RestController
@RequestMapping("/core/autonumbers")
@Api(tags = "PLATFORM.CORE",description = "Core endpoints")
public class AutonumberController extends BaseController {

	@Autowired
	private AutoNumberService autoNumberService;

	@Autowired
	private PageRequestConverter pageRequestConverter;

	@RequestMapping(method = RequestMethod.GET)
	public List<AutoNumber> getAutonumbers() {
		return autoNumberService.getAllAutonumbers();
	}

	@RequestMapping(value = "/multiple", method = RequestMethod.POST)
	public List<AutoNumber> saveAutonumbers(
			@RequestBody @Valid List<AutoNumber> autonumbers) {
		return autoNumberService.save(autonumbers);
	}

	@RequestMapping(method = RequestMethod.POST)
	public AutoNumber createAutonumber(@RequestBody @Valid AutoNumber autonumber) {
		return autoNumberService.createAutoNumber(autonumber);
	}

	@RequestMapping(value = "/{autonumberId}", method = RequestMethod.PUT)
	public AutoNumber updateAutonumber(@PathVariable Integer autonumberId,
	                                   @RequestBody @Valid AutoNumber autonumber) {
		return autoNumberService.updateAutoNumber(autonumber);
	}

	@RequestMapping(value = "/{autonumberId}", method = RequestMethod.GET)
	public AutoNumber getAutonumber(@PathVariable Integer autonumberId) {
		return autoNumberService.getAutoNumberById(autonumberId);
	}

	@RequestMapping(value = "/{autonumberId}/next", method = RequestMethod.GET, produces = "text/plain")
	public String getNextNumber(@PathVariable Integer autonumberId) {
		return autoNumberService.getNextNumber(autonumberId);
	}

	@RequestMapping(value = "/{autonumberId}/next/{count}", method = RequestMethod.GET)
	public List<String> getNextNumbers(@PathVariable Integer autonumberId, @PathVariable Integer count) {
		return autoNumberService.getNextNumbers(autonumberId, count);
	}

	@RequestMapping(value = "/{autonumberId}", method = RequestMethod.DELETE)
	public void deleteAutonumber(@PathVariable Integer autonumberId) {
		autoNumberService.deleteAutonumber(autonumberId);
	}

	@RequestMapping(value = "/ByName/{name}", method = RequestMethod.GET)
	public AutoNumber getAutonumberByName(@PathVariable("name") String name) {
		return autoNumberService.getByName(name);
	}

	@RequestMapping(value = "/ByPrefix/{prefix}", method = RequestMethod.GET)
	public AutoNumber getAutonumberByPrefix(@PathVariable("prefix") String prefix) {
		return autoNumberService.getByPrefix(prefix);
	}

	@RequestMapping(value = "/{autoNumber}/name/next", method = RequestMethod.GET, produces = "text/plain")
	public String getNextNumberByName(@PathVariable("autoNumber") String autoNumberName) {
		return autoNumberService.getNextNumberWithoutUpdate(autoNumberName);
	}

	@RequestMapping(value = "/pageable", method = RequestMethod.GET)
	public Page<AutoNumber> getAll(PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return autoNumberService.findAll(pageable);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public Page<AutoNumber> getAllAutoNumbers(PageRequest pageRequest, AutoNumberCriteria autoNumberCriteria) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return autoNumberService.getAllAutoNumbers(pageable, autoNumberCriteria);
	}

}
