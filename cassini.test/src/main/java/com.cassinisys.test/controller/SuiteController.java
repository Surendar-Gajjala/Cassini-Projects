package com.cassinisys.test.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.test.model.TestSuite;
import com.cassinisys.test.service.SuiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
@RestController
@RequestMapping("test/suites")
public class SuiteController extends BaseController {
    @Autowired
    private SuiteService suiteService;

    /* ------ Create testSuite ------*/
    @RequestMapping(method = RequestMethod.POST)
    public TestSuite create(@RequestBody TestSuite testSuite) {
        testSuite.setId(null);
        return suiteService.create(testSuite);
    }

    /*------  Update testSuite -----*/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TestSuite update(@PathVariable("id") Integer id, @RequestBody TestSuite testSuite) {
        testSuite.setId(id);
        return suiteService.update(testSuite);
    }

    /*------ Delete testSuite based on Id ----*/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        suiteService.delete(id);
    }

    /* ------- Get testSuite  based on Id -----*/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TestSuite get(@PathVariable("id") Integer id) {
        return suiteService.get(id);
    }

    /* ------ Get all testSuites -----*/
    @RequestMapping(value = "/allTestSuites", method = RequestMethod.GET)
    public List<TestSuite> getAll() {
        return suiteService.getAll();
    }
}
