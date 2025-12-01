package com.cassinisys.test.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.test.model.TestCase;
import com.cassinisys.test.model.TestPlan;
import com.cassinisys.test.model.TestScenario;
import com.cassinisys.test.model.TestSuite;
import com.cassinisys.test.service.ScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * Created by Rajabrahmachary on 02-07-2018.
 */
@RestController
@RequestMapping("test/scenario")
public class ScenarioController extends BaseController {
	@Autowired
	private ScenarioService scenarioService;

	/* ----- Create testScenario -----*/
	@RequestMapping(method = RequestMethod.POST)
	public TestScenario create(@RequestBody TestScenario testScenario) {
		testScenario.setId(null);
		return scenarioService.create(testScenario);
	}

	/*------Update testScenario -----*/
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public TestScenario update(@PathVariable("id") Integer id, @RequestBody TestScenario testScenario) {
		testScenario.setId(id);
		return scenarioService.update(testScenario);
	}

	/* ---- Get testScenario based onId -----*/
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public TestScenario get(@PathVariable("id") Integer id) {
		return scenarioService.get(id);
	}

	/*------  Delete testScenario based on Id -----*/
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {
		scenarioService.delete(id);
	}

	/*------- Get all testScenarios ------*/
	@RequestMapping(value = "/allScenarios", method = RequestMethod.GET)
	public List<TestScenario> getAll() {
		return scenarioService.getAll();
	}

	/* ----- Get testScenario tree ----*/
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public List<TestScenario> getTestTree() {
		return scenarioService.getTestTree();
	}

	/* ------ Import testScenario ------*/
	@RequestMapping(value = "/importScenario", method = RequestMethod.POST)
	public List<TestScenario> importScenario(MultipartHttpServletRequest request) throws Exception {
		return scenarioService.importScenario(request.getFileMap());
	}

	/*------ Get TestPlan based on scenarioId ------*/
	@RequestMapping(value = "/planByScenario/{scenarioId}", method = RequestMethod.GET)
	public List<TestPlan> getPlanByScenario(@PathVariable("scenarioId") Integer scenarioId) {
		return scenarioService.getPlanByScenario(scenarioId);
	}

	/*------ Get TestSuite based on planId ------*/
	@RequestMapping(value = "/suiteByPlan/{planId}", method = RequestMethod.GET)
	public List<TestSuite> getSuiteByPlan(@PathVariable("planId") Integer planId) {
		return scenarioService.getSuiteByPlan(planId);
	}

	/*------ Get TestCase based on suiteId ------*/
	@RequestMapping(value = "/caseBySuite/{suiteId}", method = RequestMethod.GET)
	public List<TestCase> getCaseBySuite(@PathVariable("suiteId") Integer suiteId) {
		return scenarioService.getCaseBySuite(suiteId);
	}
}
