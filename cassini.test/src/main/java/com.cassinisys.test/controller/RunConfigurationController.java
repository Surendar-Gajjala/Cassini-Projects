package com.cassinisys.test.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.test.model.RunSchedule;
import com.cassinisys.test.model.TestRun;
import com.cassinisys.test.model.TestRunConfiguration;
import com.cassinisys.test.model.TestScenario;
import com.cassinisys.test.service.RunConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
@RestController
@RequestMapping("test/runConfigurations")
public class RunConfigurationController extends BaseController {
    @Autowired
    RunConfigurationService runConfigurationService;

    /*------ Create runConfiguration -----*/
    @RequestMapping(method = RequestMethod.POST)
    public TestRunConfiguration create(@RequestBody TestRunConfiguration runConfiguration) {
        runConfiguration.setId(null);
        return runConfigurationService.create(runConfiguration);
    }

    /*------ Update runConfiguration -----*/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TestRunConfiguration update(@PathVariable("id") Integer id,
                                       @RequestBody TestRunConfiguration runConfiguration) {
        runConfiguration.setId(id);
        return runConfigurationService.update(runConfiguration);
    }

    /* ------ Delete runConfiguration ------*/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        runConfigurationService.delete(id);
    }

    /*------ Get runConfiguration based on runConfigId -------*/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TestRunConfiguration get(@PathVariable("id") Integer id) {
        return runConfigurationService.get(id);
    }

    /* ------ Get all runConfigurations -----*/
    @RequestMapping(value = "/allConfigurations", method = RequestMethod.GET)
    public List<TestRunConfiguration> getAll() {
        return runConfigurationService.getAll();
    }

    /* ----- Get all runConfigurations scenarios -----*/
    @RequestMapping(value = "/rcConfigScenarios", method = RequestMethod.GET)
    public List<TestScenario> getRCConfigScenarios() {
        return runConfigurationService.getRCConfigScenarios();
    }

    /* ----- Get runConfiguration tree -----*/
    @RequestMapping(value = "/runConfigTree", method = RequestMethod.GET)
    public List<TestRunConfiguration> getRunConfigTree() {
        return runConfigurationService.getRunConfigTree();
    }

    /* ------ Create runSchedule------*/
    @RequestMapping(value = "/runSchedule", method = RequestMethod.POST)
    public RunSchedule createRunSchedule(@RequestBody RunSchedule runSchedule) {
        return runConfigurationService.createRunSchedule(runSchedule);
    }

    /*------ Update runSchedule based on runConfigId ------*/
    @RequestMapping(value = "/runSchedule/{runConfigId}", method = RequestMethod.PUT)
    public RunSchedule updateRunSchedule(@PathVariable("runConfigId") Integer runConfigId, @RequestBody RunSchedule runSchedule) {
        return runConfigurationService.updateRunSchedule(runSchedule);
    }

    /*------ Get runSchedule based on runConfigId -----*/
    @RequestMapping(value = "/runScheduleByRunConfig/{runConfigId}", method = RequestMethod.GET)
    public RunSchedule getRunScheduleByRunConfig(@PathVariable("runConfigId") Integer runConfigId) {
        return runConfigurationService.getRunScheduleByRunConfig(runConfigId);
    }

    /* ------ Get testRun history -------*/
    @RequestMapping(value = "/testCaseHistory/{runConfigId}", method = RequestMethod.GET)
    public List<TestRun> getRunHistory(@PathVariable("runConfigId") Integer runConfigId) {
        return runConfigurationService.getRunHistory(runConfigId);
    }

    /*------Get testRunConfigurations based on testRunId ------*/
    @RequestMapping(value = "/getConfigurationRuns/{id}", method = RequestMethod.GET)
    public List<TestRun> getConfigurationRuns(@PathVariable("id") Integer id) {
        return runConfigurationService.getConfigurationRuns(id);
    }
}
