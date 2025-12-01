package com.cassinisys.test.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.test.model.TestPlan;
import com.cassinisys.test.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
@RestController
@RequestMapping("test/plan")
public class PlanController extends BaseController {
    @Autowired
    private PlanService planService;

    /* ----- Create plan ------*/
    @RequestMapping(method = RequestMethod.POST)
    public TestPlan create(@RequestBody TestPlan testPlan) {
        testPlan.setId(null);
        return planService.create(testPlan);
    }

    /*------Update plan ------*/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TestPlan update(@PathVariable("id") Integer id, @RequestBody TestPlan testPlan) {
        testPlan.setId(id);
        return planService.update(testPlan);
    }

    /*------ Delete plan ------*/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        planService.delete(id);
    }

    /*------ Get plan based on planId -----*/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TestPlan get(@PathVariable("id") Integer id) {
        return planService.get(id);
    }

    /* ----- Get all test plans -----*/
    @RequestMapping(value = "/allTestPlans", method = RequestMethod.GET)
    public List<TestPlan> getAll() {
        return planService.getAll();
    }


}
