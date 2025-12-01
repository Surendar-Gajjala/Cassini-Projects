package com.cassinisys.platform.api.common;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.State;
import com.cassinisys.platform.service.common.StateService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author reddy
 */
@RestController
@RequestMapping("/common/states")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class StateController extends BaseController {

	@Autowired
	private PageRequestConverter pageConverter;

	@Autowired
	private StateService stateService;
	
	@RequestMapping(method = RequestMethod.POST)
	public State create(@RequestBody State state) {
		state.setId(null);
		return stateService.create(state);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public State update(@PathVariable("id") Integer id,
			@RequestBody State state) {
		state.setId(id);
		return stateService.update(state);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {
		stateService.delete(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public State get(@PathVariable("id") Integer id) {
		return stateService.get(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public Page<State> getAll(PageRequest page) {
		Pageable pageable = pageConverter.convert(page);
		return stateService.findAll(pageable);
	}
	
	@RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
	public List<State> getMultiple(@PathVariable Integer[] ids ) {
		return stateService.findMultiple(Arrays.asList(ids));
	}

}
