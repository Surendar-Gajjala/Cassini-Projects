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
import com.cassinisys.erp.model.common.ERPState;
import com.cassinisys.erp.service.common.StateService;

@RestController
@RequestMapping("common/states")
@Api(name="States",description="States endpoint",group="COMMON")
public class StateController extends BaseController {

	@Autowired
	StateService stateService;

	@RequestMapping(method = RequestMethod.POST)
	public ERPState createState(@RequestBody @Valid ERPState state,
			HttpServletRequest request, HttpServletResponse response) {

		if (state != null) {
			return stateService.createState(state);
		}
		return null;

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPState getStateById(@PathVariable("id") Integer id) {
		return stateService.getStateById(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPState update(@PathVariable("id") Integer id,
			@RequestBody ERPState state) {
		state.setId(id);
		return stateService.updateState(state);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {
		stateService.deleteState(id);
	}

	
	@RequestMapping(method = RequestMethod.GET)
	public List<ERPState> getAllStates() {
		return stateService.getAllStates();
	}

}
