package com.cassinisys.test.service;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.test.model.*;
import com.cassinisys.test.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 01-08-2018.
 */
@Service
public class RunConfigurationService implements CrudService<TestRunConfiguration, Integer> {
    @Autowired
    private RunConfigurationRepository runConfigurationRepository;
    @Autowired
    private ScenarioRepository scenarioRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private SuiteRepository suiteRepository;
    @Autowired
    private CaseRepository caseRepository;
    @Autowired
    private InputParamRepository inputParamRepository;
    @Autowired
    private OutputParamRepository outputParamRepository;
    @Autowired
    private TestExecutionRepository testExecutionRepository;
    @Autowired
    private TestProgramExecutionRepository testProgramExecutionRepository;
    @Autowired
    private TestScriptExecutionRepository testScriptExecutionRepository;
    @Autowired
    private RunScheduleRepository runScheduleRepository;
    @Autowired
    private RunScenarioRepository runScenarioRepository;
    @Autowired
    private RunRepository runRepository;

    /* ------ Create runConfiguration and copying scenario ------*/
    @Override
    @Transactional(readOnly = false)
    public TestRunConfiguration create(TestRunConfiguration testRunConfiguration) {
        TestRunConfiguration testRunConfigurationScenarioCheck = runConfigurationRepository.findByScenarioAndNameIgnoreCase(testRunConfiguration.getScenario(), testRunConfiguration.getName());
        if (testRunConfigurationScenarioCheck == null) {
            testRunConfiguration = runConfigurationRepository.save(testRunConfiguration);
            TestScenario testScenario = (TestScenario) Utils.cloneObject(testRunConfiguration.getScenario(), TestScenario.class);
            testRunConfiguration.setScenario(testScenario);
            List<TestPlan> testPlans = planRepository.findByScenarioOrderByCreatedDateAsc(testScenario.getId());
            if (testPlans.size() > 0) {
                for (TestPlan testPlan : testPlans) {
                    testPlan = (TestPlan) Utils.cloneObject(testPlan, TestPlan.class);
                    testScenario.getChildren().add(testPlan);
                    List<TestSuite> testSuites = suiteRepository.findByPlanOrderByCreatedDateAsc(testPlan.getId());
                    if (testSuites.size() > 0) {
                        for (TestSuite testSuite : testSuites) {
                            testSuite = (TestSuite) Utils.cloneObject(testSuite, TestSuite.class);
                            testPlan.getChildren().add(testSuite);
                            List<TestCase> testCases = caseRepository.findBySuiteOrderByCreatedDateAsc(testSuite.getId());
                            if (testCases.size() > 0) {
                                for (TestCase testCase : testCases) {
                                    if (testCase.getExecution() != null) {
                                        TestExecution testExecution = testExecutionRepository.findOne(testCase.getExecution());
                                        testCase.setExecutionType(testExecution.getType().toString());
                                        if (testExecution.getType().equals(ExecutionType.PROGRAM)) {
                                            TestProgramExecution programExecution = testProgramExecutionRepository.findOne(testExecution.getId());
                                            testCase.setProgramExecution(programExecution);
                                        } else if (testExecution.getType().equals(ExecutionType.SCRIPT)) {
                                            TestScriptExecution scriptExecution = testScriptExecutionRepository.findOne(testExecution.getId());
                                            testCase.setScriptExecution(scriptExecution);
                                        }
                                        List<TestInputParam> testInputParams = inputParamRepository.findByTestCase(testCase.getId());
                                        List<TestOutputParam> testOutputParams = outputParamRepository.findByTestCase(testCase.getId());
                                        testCase.setTestInputParams(testInputParams);
                                        testCase.setTestOutputParams(testOutputParams);
                                        testCase.setRunConfiguration(testRunConfiguration.getId());
                                    }
                                    testSuite.getChildren().add((TestCase) Utils.cloneObject(testCase, TestCase.class));
                                }
                            }
                        }
                    }
                }
            }
        } else {
            throw new CassiniException("Same name configuration exist for the scenario");
        }
        return testRunConfiguration;
    }

    /* ----- Update runConfiguration -------*/
    @Override
    @Transactional(readOnly = false)
    public TestRunConfiguration update(TestRunConfiguration testRunConfiguration) {
        return runConfigurationRepository.save(testRunConfiguration);
    }

    /*------- Delete runConfiguration -----*/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer runConfigId) {
        runConfigurationRepository.delete(runConfigId);
    }

    /* ------ Get runConfiguration ------*/
    @Override
    @Transactional(readOnly = true)
    public TestRunConfiguration get(Integer runConfigId) {
        return runConfigurationRepository.findOne(runConfigId);
    }

    /*------ Get all runConfiguarations ------*/
    @Override
    @Transactional(readOnly = true)
    public List<TestRunConfiguration> getAll() {
        return runConfigurationRepository.findAll();
    }

    /* ------ Get all RcConfigScenarios ------*/
    @Transactional(readOnly = true)
    public List<TestScenario> getRCConfigScenarios() {
        List<TestScenario> testScenarios = scenarioRepository.findAllByOrderByCreatedDateAsc();
        return testScenarios;
    }

    /* ------ Get runConfiguration tree -------*/
    @Transactional(readOnly = true)
    public List<TestRunConfiguration> getRunConfigTree() {
        List<TestRunConfiguration> testRunConfigurations = runConfigurationRepository.findAllByOrderByCreatedDateAsc();
        if (testRunConfigurations.size() > 0) {
            for (TestRunConfiguration runConfiguration : testRunConfigurations) {
                TestScenario testScenario = (TestScenario) Utils.cloneObject(runConfiguration.getScenario(), TestScenario.class);
                runConfiguration.setScenario(testScenario);
                List<TestPlan> testPlans = planRepository.findByScenarioOrderByCreatedDateAsc(testScenario.getId());
                if (testPlans.size() > 0) {
                    for (TestPlan testPlan : testPlans) {
                        testPlan = (TestPlan) Utils.cloneObject(testPlan, TestPlan.class);
                        testScenario.getChildren().add(testPlan);
                        List<TestSuite> testSuites = suiteRepository.findByPlanOrderByCreatedDateAsc(testPlan.getId());
                        if (testSuites.size() > 0) {
                            for (TestSuite testSuite : testSuites) {
                                testSuite = (TestSuite) Utils.cloneObject(testSuite, TestSuite.class);
                                testPlan.getChildren().add(testSuite);
                                List<TestCase> testCases = caseRepository.findBySuiteOrderByCreatedDateAsc(testSuite.getId());
                                if (testCases.size() > 0) {
                                    for (TestCase testCase : testCases) {
                                        if (testCase.getExecution() != null) {
                                            TestExecution testExecution = testExecutionRepository.findOne(testCase.getExecution());
                                            testCase.setExecutionType(testExecution.getType().toString());
                                            if (testExecution.getType().equals(ExecutionType.PROGRAM)) {
                                                TestProgramExecution programExecution = testProgramExecutionRepository.findOne(testExecution.getId());
                                                testCase.setProgramExecution(programExecution);
                                            } else if (testExecution.getType().equals(ExecutionType.SCRIPT)) {
                                                TestScriptExecution scriptExecution = testScriptExecutionRepository.findOne(testExecution.getId());
                                                testCase.setScriptExecution(scriptExecution);
                                            }

                                            List<TestInputParam> testInputParams = inputParamRepository.findByTestCase(testCase.getId());
                                            List<TestOutputParam> testOutputParams = outputParamRepository.findByTestCase(testCase.getId());

                                            testCase.setTestInputParams(testInputParams);
                                            testCase.setTestOutputParams(testOutputParams);
                                            testCase.setRunConfiguration(runConfiguration.getId());
                                        } else {
                                            List<TestInputParam> testInputParams = inputParamRepository.findByTestCase(testCase.getId());
                                            List<TestOutputParam> testOutputParams = outputParamRepository.findByTestCase(testCase.getId());

                                            testCase.setTestInputParams(testInputParams);
                                            testCase.setTestOutputParams(testOutputParams);
                                            testCase.setRunConfiguration(runConfiguration.getId());
                                        }

                                        testSuite.getChildren().add((TestCase) Utils.cloneObject(testCase, TestCase.class));

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return testRunConfigurations;
    }

    /* ------ Create runSchedule ------*/

    @Transactional(readOnly = false)
    public RunSchedule createRunSchedule(RunSchedule runSchedule) {
        runSchedule.setId(null);
        return runScheduleRepository.save(runSchedule);
    }

    /* ------- Update runSchedule ------*/
    @Transactional(readOnly = false)
    public RunSchedule updateRunSchedule(RunSchedule runSchedule) {
        return runScheduleRepository.save(runSchedule);
    }

    /* ----- Get runSchedule based on runConfigId -----*/
    @Transactional(readOnly = false)
    public RunSchedule getRunScheduleByRunConfig(Integer runConfigId) {
        RunSchedule runSchedule = runScheduleRepository.findByRunConfig(runConfigId);
        return runSchedule;
    }

    /*------- Get TestRun History At RunConfiguration -------*/
    @Transactional(readOnly = false)
    public List<TestRun> getRunHistory(Integer runConfigId) {
        List<TestRun> run = new ArrayList<>();
        TestRunConfiguration testRunConfiguration = runConfigurationRepository.findOne(runConfigId);
        if (testRunConfiguration != null) {
            run = runRepository.findByTestRunConfiguration(testRunConfiguration);
        }
        return run;
    }

    /* ------- Get all testRunconfigurations based on id */
    @Transactional(readOnly = false)
    public List<TestRun> getConfigurationRuns(Integer id) {
        TestRunConfiguration testRunConfiguration = runConfigurationRepository.findOne(id);
        List<TestRun> testRun = runRepository.findByTestRunConfiguration(testRunConfiguration);
        return testRun;
    }

}
