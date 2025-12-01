package com.cassinisys.test.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.test.model.*;
import com.cassinisys.test.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
@RestController
@RequestMapping("test/case")
public class CaseController extends BaseController {
    @Autowired
    private CaseService caseService;

    /* ------ Create testCase ------*/
    @RequestMapping(method = RequestMethod.POST)
    public TestCase create(@RequestBody TestCase testCase) {
        testCase.setId(null);
        return caseService.create(testCase);
    }

    /* ------ Update testCase ------*/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TestCase update(@PathVariable("id") Integer id,
                           @RequestBody TestCase testCase) {
        testCase.setId(id);
        return caseService.update(testCase);
    }

    /* ------ Delete testCase ------*/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        caseService.delete(id);
    }

    /* ------ Get testCase based on Id  ------*/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TestCase get(@PathVariable("id") Integer id) {
        return caseService.get(id);
    }

    /* ------ Get input and outputParams based on testCaseId ------*/
    @RequestMapping(value = "/testCase/{testCaseId}", method = RequestMethod.GET)
    public TestCase getInputParamAndOutputParamsByTestCase(@PathVariable("testCaseId") Integer testCaseId) {
        return caseService.getInputParamAndOutputParamsByTestCase(testCaseId);
    }

    /* ------ Get input param values baed on configurationId and testCaseId ------*/
    @RequestMapping(value = "/config/{configId}/testCase/{testCaseId}", method = RequestMethod.GET)
    public TestCase getInputParamValuesByConfigAndTestCase(@PathVariable("configId") Integer configId,
                                                           @PathVariable("testCaseId") Integer testCaseId) {
        return caseService.getInputParamValuesByConfigAndTestCase(configId, testCaseId);
    }

    /* ------ Get all testCases ------*/
    @RequestMapping(value = "/allTestCases", method = RequestMethod.GET)
    public List<TestCase> getAll() {
        return caseService.getAll();
    }

    /*-----------  Test Case Input & Output Params Methods  -----------------------------*/

    @RequestMapping(value = "inputParam", method = RequestMethod.POST)
    public TestInputParam createInputParam(@RequestBody TestInputParam testInputParam) {
        testInputParam.setId(null);
        return caseService.createInputParam(testInputParam);
    }

    @RequestMapping(value = "inputParam/{paramId}", method = RequestMethod.PUT)
    public TestInputParam updateInputParam(@PathVariable("paramId") Integer paramId, @RequestBody TestInputParam testInputParam) {
        testInputParam.setId(paramId);
        return caseService.updateInputParam(testInputParam);
    }

    @RequestMapping(value = "inputParam/{paramId}", method = RequestMethod.DELETE)
    public void deleteInputParam(@PathVariable("paramId") Integer paramId) {
        caseService.deleteInputParam(paramId);
    }


    @RequestMapping(value = "outputParam", method = RequestMethod.POST)
    public TestOutputParam createOutputParam(@RequestBody TestOutputParam testOutputParam) {
        testOutputParam.setId(null);
        return caseService.createOutputParam(testOutputParam);
    }

    @RequestMapping(value = "outputParam/{paramId}", method = RequestMethod.PUT)
    public TestOutputParam updateOutputParam(@PathVariable("paramId") Integer paramId, @RequestBody TestOutputParam testOutputParam) {
        testOutputParam.setId(paramId);
        return caseService.updateOutputParam(testOutputParam);
    }

    @RequestMapping(value = "outputParam/{paramId}", method = RequestMethod.DELETE)
    public void deleteOutputParam(@PathVariable("paramId") Integer paramId) {
        caseService.deleteOutputParam(paramId);
    }


    /*-----------  Test Case Input & Output Param Values Methods  -----------------------------*/
    @RequestMapping(value = "/inputParamValue", method = RequestMethod.POST)
    public RCInputParamValue createInputParamValue(@RequestBody RCInputParamValue testInputParam) {
        testInputParam.setId(null);
        return caseService.createInputParamValue(testInputParam);
    }

    @RequestMapping(value = "/inputParamValue/{paramId}", method = RequestMethod.PUT)
    public RCInputParamValue updateInputParamValue(@PathVariable("paramId") Integer paramId, @RequestBody RCInputParamValue testInputParam) {
        testInputParam.setId(paramId);
        return caseService.updateInputParamValue(testInputParam);
    }

    @RequestMapping(value = "/inputParamValue/{paramId}", method = RequestMethod.DELETE)
    public void deleteInputParamValue(@PathVariable("paramId") Integer paramId) {
        caseService.deleteInputParamValue(paramId);
    }

    @RequestMapping(value = "/expectedOutputValue", method = RequestMethod.POST)
    public RCOutPutParamExpectedValue createExpectedOutputValue(@RequestBody RCOutPutParamExpectedValue outPutParamExpectedValue) {
        outPutParamExpectedValue.setId(null);
        return caseService.createExpectedOutputValue(outPutParamExpectedValue);
    }

    @RequestMapping(value = "/expectedOutputValue/{paramId}", method = RequestMethod.PUT)
    public RCOutPutParamExpectedValue updateExpectedOutputValue(@PathVariable("paramId") Integer paramId, @RequestBody RCOutPutParamExpectedValue outPutParamExpectedValue) {
        outPutParamExpectedValue.setId(paramId);
        return caseService.updateExpectedOutputValue(outPutParamExpectedValue);
    }

    @RequestMapping(value = "/expectedOutputValue/{paramId}", method = RequestMethod.DELETE)
    public void deleteExpectedOutputValue(@PathVariable("paramId") Integer paramId) {
        caseService.deleteExpectedOutputValue(paramId);
    }


    @RequestMapping(value = "/importInputParams/{testCaseId}", method = RequestMethod.POST)
    public void importInputParams(@PathVariable("testCaseId") Integer testCaseId, MultipartHttpServletRequest request) throws Exception {
        caseService.importInputParams(testCaseId, request);

    }


    @RequestMapping(value = "/importOutputParams/{testCaseId}", method = RequestMethod.POST)
    public void importOutPutParams(@PathVariable("testCaseId") Integer testCaseId, MultipartHttpServletRequest request) throws Exception {
        caseService.importOutPutParams(testCaseId, request);
    }


    @RequestMapping(value = "/importInputParamValues/{testCaseId}/{runConfig}", method = RequestMethod.POST)
    public void importInputParamValues(@PathVariable("testCaseId") Integer testCaseId, @PathVariable("runConfig") Integer runConfig, MultipartHttpServletRequest request) throws Exception {
        caseService.importInputParamValues(testCaseId, runConfig, request);
    }


    @RequestMapping(value = "/importOutPutParamValues/{testCaseId}/{runConfig}", method = RequestMethod.POST)
    public void importOutPutParamValues(@PathVariable("testCaseId") Integer testCaseId, @PathVariable("runConfig") Integer runConfig, MultipartHttpServletRequest request) throws Exception {

        caseService.importOutPutParamExpectedValue(testCaseId, runConfig, request);
    }

    @RequestMapping(value = "/testRunCaseHistory/{scenarioId}", method = RequestMethod.GET)
    public List<TestRun> getTestRunCaseHistory(@PathVariable("scenarioId") Integer scenarioId) {
        return caseService.getRunHistory(scenarioId);
    }


}
