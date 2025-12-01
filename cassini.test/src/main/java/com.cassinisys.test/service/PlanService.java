package com.cassinisys.test.service;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.test.model.TestPlan;
import com.cassinisys.test.repo.PlanRepository;
import com.cassinisys.test.repo.SuiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
@Service
public class PlanService implements CrudService<TestPlan, Integer> {
    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private SuiteRepository suiteRepository;

    /* ------ Create testPlan -----*/
    @Override
    @Transactional(readOnly = false)
    public TestPlan create(TestPlan testPlan) {
        //check testPlan exist or Not
        TestPlan existPlan = planRepository.findByScenarioAndNameEqualsIgnoreCase(testPlan.getScenario(), testPlan.getName());
        if (existPlan == null) {
            testPlan = planRepository.save(testPlan);
        } else {
            throw new CassiniException("Plan name already exist on Scenario");
        }
        return testPlan;
    }

    /* ----- Update testPlan -----*/
    @Override
    @Transactional(readOnly = false)
    public TestPlan update(TestPlan testPlan) {
        //check testPlan exist or Not
        TestPlan existPlan = planRepository.findByScenarioAndNameEqualsIgnoreCase(testPlan.getScenario(), testPlan.getName());
        if (existPlan == null) {
            testPlan = planRepository.save(testPlan);
        } else {
            if (existPlan.getId().equals(testPlan.getId())) {
                testPlan = planRepository.save(testPlan);
            } else {
                throw new CassiniException("Plan name already exist on Scenario");
            }
        }

        return testPlan;
    }

    /* ----- Delete testPlan -----*/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        planRepository.delete(id);

    }

    /* ------- Get testPlan based on testPlanId  -----*/
    @Override
    @Transactional(readOnly = true)
    public TestPlan get(Integer id) {
        TestPlan testPlan = planRepository.findOne(id);
        testPlan.getChildren().addAll(suiteRepository.findByPlanOrderByCreatedDateAsc(testPlan.getId()));
        return testPlan;
    }

    /* ------  Get all testPlans ------*/
    @Override
    @Transactional(readOnly = true)
    public List<TestPlan> getAll() {
        return planRepository.findAll();
    }
}
