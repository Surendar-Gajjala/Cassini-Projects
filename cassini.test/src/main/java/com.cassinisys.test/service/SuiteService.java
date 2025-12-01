package com.cassinisys.test.service;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.test.model.TestSuite;
import com.cassinisys.test.repo.CaseRepository;
import com.cassinisys.test.repo.SuiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
@Service
public class SuiteService implements CrudService<TestSuite, Integer> {

    @Autowired
    private SuiteRepository suiteRepository;
    @Autowired
    private CaseRepository caseRepository;

    /* ------ Create testSuite ------*/
    @Override
    @Transactional(readOnly = false)
    public TestSuite create(TestSuite testSuite) {
        //check testsuite exist or not
        TestSuite existSuite = suiteRepository.findByPlanAndNameEqualsIgnoreCase(testSuite.getPlan(), testSuite.getName());
        if (existSuite == null) {
            testSuite = suiteRepository.save(testSuite);
        } else {
            throw new CassiniException("Suite name already exist on plan");
        }
        return testSuite;
    }

    /* ------ Update test suite ------*/
    @Override
    @Transactional(readOnly = false)
    public TestSuite update(TestSuite testSuite) {
        //check testsuite exist or not
        TestSuite existSuite = suiteRepository.findByPlanAndNameEqualsIgnoreCase(testSuite.getPlan(), testSuite.getName());
        if (existSuite == null) {
            testSuite = suiteRepository.save(testSuite);
        } else {
            if (testSuite.getId().equals(existSuite.getId())) {
                testSuite = suiteRepository.save(testSuite);
            } else {
                throw new CassiniException("Suite Name already exist on Plan");
            }

        }
        return testSuite;
    }

    /*------- Delete testSuite ------*/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        suiteRepository.delete(id);

    }

    /* ------ Get testSuite based on Id -------*/
    @Override
    @Transactional(readOnly = true)
    public TestSuite get(Integer id) {
        TestSuite testSuite = suiteRepository.findOne(id);
        testSuite.getChildren().addAll(caseRepository.findBySuiteOrderByCreatedDateAsc(testSuite.getId()));
        return testSuite;
    }

    /* ------ Get all testSuites -----*/
    @Override
    @Transactional(readOnly = true)
    public List<TestSuite> getAll() {
        return suiteRepository.findAll();
    }
}
