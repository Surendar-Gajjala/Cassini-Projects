package com.cassinisys.test.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.test.model.*;
import com.cassinisys.test.repo.RunPlanRepository;
import com.cassinisys.test.repo.RunScenarioRepository;
import com.cassinisys.test.repo.RunSuiteRepository;
import com.cassinisys.test.service.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
@RestController
@RequestMapping("test/run")
public class RunController extends BaseController {
    @Autowired
    private RunService runService;
    @Autowired
    private RunScenarioRepository runScenarioRepository;
    @Autowired
    private RunPlanRepository runPlanRepository;
    @Autowired
    private RunSuiteRepository runSuiteRepository;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    /* ----- Create testrun -----*/
    @RequestMapping(method = RequestMethod.POST)
    public TestRun create(@RequestBody TestRun testRun) {
        testRun.setId(null);
        return runService.create(testRun);
    }

    /*------ Update testRun -----*/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TestRun update(@PathVariable("id") Integer id, @RequestBody TestRun testRun) {
        testRun.setId(id);
        return runService.update(testRun);
    }

    /*----- Delete testRun -----*/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        runService.delete(id);
    }

    /* ------ Get testRun based Id ------*/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TestRun get(@PathVariable("id") Integer id) {
        return runService.get(id);
    }

    /*------ Get all testruns by pagination -----*/
    @RequestMapping(value = "/allTestRuns/pageable", method = RequestMethod.GET)
    public Page<TestRun> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return runService.getAllTestRuns(pageable);
    }

    /*------ Import inputParams ------*/
    @RequestMapping(value = "/importInputParams", method = RequestMethod.POST)
    public void importTestRunCases(MultipartHttpServletRequest request) throws Exception {
        runService.importTestRun(request);
    }

    /*-----Get testRunTree based on testRunId ----*/
    @RequestMapping(value = "/testRunTree/{id}", method = RequestMethod.GET)
    public TestRun getTestRunTree(@PathVariable("id") Integer id) {
        return runService.getTestRunTree(id);
    }

    /* ----- Get testCase details based on testRunId -----*/
    @RequestMapping(value = "/testCase/{id}", method = RequestMethod.GET)
    public TestCase getTestCaseDetails(@PathVariable("id") Integer id) {
        return runService.getTestCaseDetails(id);
    }

    /*------- Get all related runCase details based onrunCaseId -----*/
    @RequestMapping(value = "/runCase/{id}", method = RequestMethod.GET)
    public RunCaseDetailsDTO getRunCase(@PathVariable("id") Integer id) {
        return runService.getRunCase(id);
    }

    /* ----- Get runScenario details based on runScenarioId -----*/
    @RequestMapping(value = "/runScenario/{id}", method = RequestMethod.GET)
    public RunScenario getRunScenario(@PathVariable("id") Integer id) {
        return runScenarioRepository.findOne(id);
    }

    /* ----- Get runPlan details based on runPlanId -----*/
    @RequestMapping(value = "/runPlan/{id}", method = RequestMethod.GET)
    public RunPlan getRunPlan(@PathVariable("id") Integer id) {
        return runPlanRepository.findOne(id);
    }

    /* ----- Get runSuite details based on runSuiteId -----*/
    @RequestMapping(value = "/runSuite/{id}", method = RequestMethod.GET)
    public RunSuite getRunSuite(@PathVariable("id") Integer id) {
        return runSuiteRepository.findOne(id);
    }

    /*-------Delete testRun based on testRunId ------*/
    @RequestMapping(value = "/deleteTestRun/{testRunId}", method = RequestMethod.DELETE)
    public void deleteTestRun(@PathVariable("testRunId") Integer testRunId) {
        runService.deleteTestRun(testRunId);
    }

}
