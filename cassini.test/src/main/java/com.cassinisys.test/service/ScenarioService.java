package com.cassinisys.test.service;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.test.model.TestCase;
import com.cassinisys.test.model.TestPlan;
import com.cassinisys.test.model.TestScenario;
import com.cassinisys.test.model.TestSuite;
import com.cassinisys.test.repo.CaseRepository;
import com.cassinisys.test.repo.PlanRepository;
import com.cassinisys.test.repo.ScenarioRepository;
import com.cassinisys.test.repo.SuiteRepository;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Rajabrahmachary on 02-07-2018.
 */
@Service
public class ScenarioService implements CrudService<TestScenario, Integer> {
    @Autowired
    private ScenarioRepository scenarioRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private SuiteRepository suiteRepository;
    @Autowired
    private CaseRepository caseRepository;


    /* ---Create testScenario -------*/
    @Override
    @Transactional(readOnly = false)
    public TestScenario create(TestScenario testScenario) {
        TestScenario existScenario = scenarioRepository.findByNameEqualsIgnoreCase(testScenario.getName());
        if (existScenario == null) {
            testScenario = scenarioRepository.save(testScenario);
        } else {
            throw new CassiniException("Scenario name already exist");
        }

        return testScenario;
    }

    /* ------ Update testScenario -----*/
    @Override
    @Transactional(readOnly = false)
    public TestScenario update(TestScenario testScenario) {
        /*------- Check testScenario exist or not ------*/
        TestScenario existScenario = scenarioRepository.findByNameEqualsIgnoreCase(testScenario.getName());
        if (existScenario == null) {
            testScenario = scenarioRepository.save(testScenario);
        } else {
            if (existScenario.getId().equals(testScenario.getId())) {
                testScenario = scenarioRepository.save(testScenario);
            } else {
                throw new CassiniException("Scenario name already exist");
            }
        }
        return testScenario;
    }

    /* ------- Delete testScenario ------*/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        scenarioRepository.delete(id);
    }

    /* ------- Get testScenario based on testScenario id ------*/
    @Override
    @Transactional(readOnly = true)
    public TestScenario get(Integer id) {
        TestScenario testScenario = scenarioRepository.findOne(id);
        /*Code For No.of  TestCases to this Scenario*/
        List<TestCase> testCaseList = new ArrayList<TestCase>();
        List<TestPlan> testPlans = planRepository.findByScenario(testScenario.getId());
        if (testPlans.size() > 0) {
            testScenario.getChildren().addAll(testPlans);
            for (TestPlan testPlan : testPlans) {
                List<TestSuite> testSuites = suiteRepository.findByPlanOrderByCreatedDateAsc(testPlan.getId());
                if (testSuites != null) {
                    if (testSuites.size() > 0) {
                        for (TestSuite suite : testSuites) {
                            List<TestCase> testCases = caseRepository.findBySuiteOrderByCreatedDateAsc(suite.getId());
                            if (testCases != null) {
                                if (testCases.size() > 0) {
                                    testCaseList.addAll(testCases);
                                }
                            }
                        }
                    }
                }
            }
        }
        testScenario.setTestCases(testCaseList);
        return testScenario;
    }

    /* ------- Get all testScenarios -------*/
    @Override
    @Transactional(readOnly = true)
    public List<TestScenario> getAll() {
        return scenarioRepository.findAll();
    }

    /* -------  Get testplans based on scenario id -------*/

    public List<TestPlan> getPlanByScenario(Integer id) {
        List<TestPlan> testPlans = new ArrayList<>();
        testPlans = planRepository.findByScenario(id);
        return testPlans;
    }

    public List<TestSuite> getSuiteByPlan(Integer id) {
        List<TestSuite> suites = new ArrayList<>();
        suites = suiteRepository.findByPlan(id);
        return suites;
    }

    public List<TestCase> getCaseBySuite(Integer id) {
        List<TestCase> cases = new ArrayList<>();
        cases = caseRepository.findBySuite(id);
        return cases;
    }


    /* ------- Get testScenario tree ------*/
    @Transactional(readOnly = true)
    public List<TestScenario> getTestTree() {
        List<TestScenario> scenarios = scenarioRepository.findAllByOrderByCreatedDateAsc();
        if (scenarios.size() > 0) {
            for (TestScenario scenario : scenarios) {
                List<TestPlan> testPlans = planRepository.findByScenarioOrderByCreatedDateAsc(scenario.getId());
                if (testPlans.size() > 0) {
                    scenario.setChildren(testPlans);
                    for (TestPlan testPlan : testPlans) {
                        List<TestSuite> testSuites = suiteRepository.findByPlanOrderByCreatedDateAsc(testPlan.getId());
                        if (testSuites.size() > 0) {
                            testPlan.setChildren(testSuites);
                            for (TestSuite testSuite : testSuites) {
                                List<TestCase> testCases = caseRepository.findBySuiteOrderByCreatedDateAsc(testSuite.getId());
                                if (testCases.size() > 0) {
                                    testSuite.setChildren(testCases);
                                }
                            }
                        }
                    }
                }
            }
        }

        return scenarios;
    }

    /*-------- method to importscenarioxlsxFormat ------*/
    private void importScenarioXLSXFormat(MultipartFile file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);
        for (int row = 0; row <= worksheet.getLastRowNum(); row++) {
            if (row == 0) continue;
            XSSFRow scenarioRow = worksheet.getRow(row);
            XSSFCell scenarioNameCell = scenarioRow.getCell(0);
            XSSFCell scenarioDescriptionCell = scenarioRow.getCell(1);
            XSSFCell planNameCell = scenarioRow.getCell(2);
            XSSFCell planDescriptionCell = scenarioRow.getCell(3);
            XSSFCell suiteNameCell = scenarioRow.getCell(4);
            XSSFCell suiteDescriptionCell = scenarioRow.getCell(5);
            XSSFCell testCaseNameCell = scenarioRow.getCell(6);
            XSSFCell testCaseDescriptionCell = scenarioRow.getCell(7);
            if (scenarioNameCell != null && planNameCell != null && suiteNameCell != null && testCaseNameCell != null) {
                TestScenario testScenario = scenarioRepository.findByNameEqualsIgnoreCase(scenarioNameCell.toString());
                if (testScenario == null) {
                    TestScenario scenario = new TestScenario();
                    scenario.setName(scenarioNameCell.toString());
                    if (scenarioDescriptionCell != null) {
                        scenario.setDescription(scenarioDescriptionCell.toString());
                    }
                    scenario = scenarioRepository.save(scenario);
                    if (scenario != null) {
                        TestPlan plan = planRepository.findByScenarioAndNameEqualsIgnoreCase(scenario.getId(), planNameCell.toString());
                        if (plan == null) {
                            TestPlan testPlan = new TestPlan();
                            testPlan.setName(planNameCell.toString());
                            if (planDescriptionCell != null) {
                                testPlan.setDescription(planDescriptionCell.toString());
                            }
                            testPlan.setScenario(scenario.getId());
                            testPlan = planRepository.save(testPlan);
                            if (testPlan != null) {
                                TestSuite testSuite = new TestSuite();
                                testSuite.setName(suiteNameCell.toString());
                                if (suiteDescriptionCell != null) {
                                    testSuite.setDescription(suiteDescriptionCell.toString());
                                }
                                testSuite.setPlan(testPlan.getId());
                                testSuite = suiteRepository.save(testSuite);
                                if (testSuite != null) {
                                    TestCase testCase = new TestCase();
                                    testCase.setName(testCaseNameCell.toString());
                                    if (testCaseDescriptionCell != null) {
                                        testCase.setDescription(testCaseDescriptionCell.toString());
                                    }
                                    testCase.setSuite(testSuite.getId());
                                    testCase = caseRepository.save(testCase);
                                }
                            }
                        } else {
                            TestSuite suite = suiteRepository.findByPlanAndNameEqualsIgnoreCase(plan.getId(),
                                    suiteNameCell.toString());
                            if (suite == null) {
                                TestSuite testSuite = new TestSuite();
                                testSuite.setName(suiteNameCell.toString());
                                if (suiteDescriptionCell != null) {
                                    testSuite.setDescription(suiteDescriptionCell.toString());
                                }
                                testSuite.setPlan(plan.getId());
                                suite = suiteRepository.save(testSuite);

                                if (suite != null) {
                                    TestCase testCase = new TestCase();
                                    testCase.setName(testCaseNameCell.toString());
                                    if (testCaseDescriptionCell != null) {
                                        testCase.setDescription(testCaseDescriptionCell.toString());
                                    }
                                    testCase.setSuite(suite.getId());
                                    testCase = caseRepository.save(testCase);
                                }
                            }
                        }
                    }

                } else {
                    testScenario.setDescription(scenarioDescriptionCell.toString());
                    scenarioRepository.save(testScenario);
                    TestPlan plan = planRepository.findByScenarioAndNameEqualsIgnoreCase(testScenario.getId(),
                            planNameCell.toString());

                    if (plan == null) {
                        TestPlan testPlan = new TestPlan();
                        testPlan.setName(planNameCell.toString());
                        if (planDescriptionCell != null) {
                            testPlan.setDescription(planDescriptionCell.toString());
                        }
                        testPlan.setScenario(testScenario.getId());
                        testPlan = planRepository.save(testPlan);
                        TestSuite suite = suiteRepository.findByPlanAndNameEqualsIgnoreCase(testPlan.getId(),
                                suiteNameCell.toString());
                        if (suite == null) {
                            TestSuite testSuite = new TestSuite();
                            testSuite.setName(suiteNameCell.toString());
                            testSuite.setPlan(testPlan.getId());
                            if (suiteDescriptionCell != null) {
                                testSuite.setDescription(suiteDescriptionCell.toString());
                            }
                            testSuite = suiteRepository.save(testSuite);
                            TestCase testCase = new TestCase();
                            testCase.setName(testCaseNameCell.toString());
                            testCase.setSuite(testSuite.getId());
                            if (testCaseDescriptionCell != null) {
                                testCase.setDescription(testCaseDescriptionCell.toString());
                            }
                            testCase = caseRepository.save(testCase);
                        } else {
                            TestCase testCase = caseRepository.findBySuiteAndNameEqualsIgnoreCase(suite.getId(),
                                    testCaseNameCell.toString());
                            if (testCase == null) {
                                TestCase testCase1 = new TestCase();
                                testCase1.setName(testCaseNameCell.toString());
                                testCase1.setSuite(suite.getId());
                                if (testCaseDescriptionCell != null) {
                                    testCase1.setDescription(testCaseDescriptionCell.toString());
                                }
                                testCase1 = caseRepository.save(testCase1);
                            }

                        }
                    } else {
                        plan.setDescription(planDescriptionCell.toString());
                        plan = planRepository.save(plan);
                        TestSuite suite = suiteRepository.findByPlanAndNameEqualsIgnoreCase(plan.getId(),
                                suiteNameCell.toString());
                        if (suite == null) {
                            TestSuite testSuite = new TestSuite();
                            testSuite.setName(suiteNameCell.toString());
                            if (suiteDescriptionCell.toString() != null) {
                                testSuite.setDescription(suiteDescriptionCell.toString());
                            }
                            testSuite.setPlan(plan.getId());
                            testSuite = suiteRepository.save(testSuite);
                            TestCase testCase = caseRepository.findBySuiteAndNameEqualsIgnoreCase(testSuite.getId(),
                                    testCaseNameCell.toString());
                            if (testCase == null) {
                                TestCase testCase2 = new TestCase();
                                testCase2.setName(testCaseNameCell.toString());
                                if (testCaseDescriptionCell != null) {
                                    testCase2.setDescription(testCaseDescriptionCell.toString());
                                }
                                testCase2.setSuite(testSuite.getId());
                                testCase2 = caseRepository.save(testCase2);
                            }
                        } else {
                            suite.setDescription(suiteDescriptionCell.toString());
                            suiteRepository.save(suite);
                            TestCase testCase = caseRepository.findBySuiteAndNameEqualsIgnoreCase(suite.getId(),
                                    testCaseNameCell.toString());
                            if (testCase == null) {
                                TestCase testCase2 = new TestCase();
                                testCase2.setName(testCaseNameCell.toString());
                                if (testCaseDescriptionCell != null) {
                                    testCase2.setDescription(testCaseDescriptionCell.toString());
                                }
                                testCase2.setSuite(suite.getId());
                                testCase2 = caseRepository.save(testCase2);
                            } else {
                                testCase.setDescription(testCaseDescriptionCell.toString());
                                caseRepository.save(testCase);
                            }
                        }
                    }
                }
            }
        }

        workbook.close();


    }

    /*-------- method to importscenarioxlsFormat ------*/
    private void importScenarioXLSFormat(MultipartFile file) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
        HSSFSheet worksheet = workbook.getSheetAt(0);
        for (int row = 0; row <= worksheet.getLastRowNum(); row++) {
            if (row == 0) continue;
            HSSFRow scenarioRow = worksheet.getRow(row);
            HSSFCell scenarioNameCell = scenarioRow.getCell(0);
            HSSFCell scenarioDescriptionCell = scenarioRow.getCell(1);
            HSSFCell planNameCell = scenarioRow.getCell(2);
            HSSFCell planDescriptionCell = scenarioRow.getCell(3);
            HSSFCell suiteNameCell = scenarioRow.getCell(4);
            HSSFCell suiteDescriptionCell = scenarioRow.getCell(5);
            HSSFCell testCaseNameCell = scenarioRow.getCell(6);
            HSSFCell testCaseDescriptionCell = scenarioRow.getCell(7);
            TestScenario testScenario1 = scenarioRepository.findByNameEqualsIgnoreCase(scenarioNameCell.toString());
            if (scenarioNameCell != null && planNameCell != null && suiteNameCell != null && testCaseNameCell != null) {
                TestScenario testScenario = scenarioRepository.findByNameEqualsIgnoreCase(scenarioNameCell.toString());
                if (testScenario == null) {
                    TestScenario scenario = new TestScenario();
                    scenario.setName(scenarioNameCell.toString());
                    if (scenarioDescriptionCell != null) {
                        scenario.setDescription(scenarioDescriptionCell.toString());
                    }
                    scenario = scenarioRepository.save(scenario);
                    if (scenario != null) {
                        TestPlan plan = planRepository.findByScenarioAndNameEqualsIgnoreCase(scenario.getId(), planNameCell.toString());
                        if (plan == null) {
                            TestPlan testPlan = new TestPlan();
                            testPlan.setName(planNameCell.toString());
                            if (planDescriptionCell != null) {
                                testPlan.setDescription(planDescriptionCell.toString());
                            }
                            testPlan.setScenario(scenario.getId());
                            testPlan = planRepository.save(testPlan);
                            if (testPlan != null) {
                                TestSuite testSuite = new TestSuite();
                                testSuite.setName(suiteNameCell.toString());
                                if (suiteDescriptionCell != null) {
                                    testSuite.setDescription(suiteDescriptionCell.toString());
                                }
                                testSuite.setPlan(testPlan.getId());
                                testSuite = suiteRepository.save(testSuite);
                                if (testSuite != null) {
                                    TestCase testCase = new TestCase();
                                    testCase.setName(testCaseNameCell.toString());
                                    if (testCaseDescriptionCell != null) {
                                        testCase.setDescription(testCaseDescriptionCell.toString());
                                    }
                                    testCase.setSuite(testSuite.getId());
                                    testCase = caseRepository.save(testCase);
                                }
                            }
                        } else {
                            TestSuite suite = suiteRepository.findByPlanAndNameEqualsIgnoreCase(plan.getId(),
                                    suiteNameCell.toString());
                            if (suite == null) {
                                TestSuite testSuite = new TestSuite();
                                testSuite.setName(suiteNameCell.toString());
                                if (suiteDescriptionCell != null) {
                                    testSuite.setDescription(suiteDescriptionCell.toString());
                                }
                                testSuite.setPlan(plan.getId());
                                suite = suiteRepository.save(testSuite);

                                if (suite != null) {
                                    TestCase testCase = new TestCase();
                                    testCase.setName(testCaseNameCell.toString());
                                    if (testCaseDescriptionCell != null) {
                                        testCase.setDescription(testCaseDescriptionCell.toString());
                                    }
                                    testCase.setSuite(suite.getId());
                                    testCase = caseRepository.save(testCase);
                                }
                            }
                        }
                    }

                } else {
                    testScenario.setDescription(scenarioDescriptionCell.toString());
                    scenarioRepository.save(testScenario);
                    TestPlan plan = planRepository.findByScenarioAndNameEqualsIgnoreCase(testScenario.getId(),
                            planNameCell.toString());

                    if (plan == null) {
                        TestPlan testPlan = new TestPlan();
                        testPlan.setName(planNameCell.toString());
                        if (planDescriptionCell != null) {
                            testPlan.setDescription(planDescriptionCell.toString());
                        }
                        testPlan.setScenario(testScenario.getId());
                        testPlan = planRepository.save(testPlan);
                        TestSuite suite = suiteRepository.findByPlanAndNameEqualsIgnoreCase(testPlan.getId(),
                                suiteNameCell.toString());
                        if (suite == null) {
                            TestSuite testSuite = new TestSuite();
                            testSuite.setName(suiteNameCell.toString());
                            testSuite.setPlan(testPlan.getId());
                            if (suiteDescriptionCell != null) {
                                testSuite.setDescription(suiteDescriptionCell.toString());
                            }
                            testSuite = suiteRepository.save(testSuite);
                            TestCase testCase = new TestCase();
                            testCase.setName(testCaseNameCell.toString());
                            testCase.setSuite(testSuite.getId());
                            if (testCaseDescriptionCell != null) {
                                testCase.setDescription(testCaseDescriptionCell.toString());
                            }
                            testCase = caseRepository.save(testCase);
                        } else {
                            TestCase testCase = caseRepository.findBySuiteAndNameEqualsIgnoreCase(suite.getId(),
                                    testCaseNameCell.toString());
                            if (testCase == null) {
                                TestCase testCase1 = new TestCase();
                                testCase1.setName(testCaseNameCell.toString());
                                testCase1.setSuite(suite.getId());
                                if (testCaseDescriptionCell != null) {
                                    testCase1.setDescription(testCaseDescriptionCell.toString());
                                }
                                testCase1 = caseRepository.save(testCase1);
                            }

                        }
                    } else {
                        plan.setDescription(planDescriptionCell.toString());
                        plan = planRepository.save(plan);
                        TestSuite suite = suiteRepository.findByPlanAndNameEqualsIgnoreCase(plan.getId(),
                                suiteNameCell.toString());
                        if (suite == null) {
                            TestSuite testSuite = new TestSuite();
                            testSuite.setName(suiteNameCell.toString());
                            if (suiteDescriptionCell.toString() != null) {
                                testSuite.setDescription(suiteDescriptionCell.toString());
                            }
                            testSuite.setPlan(plan.getId());
                            testSuite = suiteRepository.save(testSuite);
                            TestCase testCase = caseRepository.findBySuiteAndNameEqualsIgnoreCase(testSuite.getId(),
                                    testCaseNameCell.toString());
                            if (testCase == null) {
                                TestCase testCase2 = new TestCase();
                                testCase2.setName(testCaseNameCell.toString());
                                if (testCaseDescriptionCell != null) {
                                    testCase2.setDescription(testCaseDescriptionCell.toString());
                                }
                                testCase2.setSuite(testSuite.getId());
                                testCase2 = caseRepository.save(testCase2);
                            }
                        } else {
                            suite.setDescription(suiteDescriptionCell.toString());
                            suiteRepository.save(suite);
                            TestCase testCase = caseRepository.findBySuiteAndNameEqualsIgnoreCase(suite.getId(),
                                    testCaseNameCell.toString());
                            if (testCase == null) {
                                TestCase testCase2 = new TestCase();
                                testCase2.setName(testCaseNameCell.toString());
                                if (testCaseDescriptionCell != null) {
                                    testCase2.setDescription(testCaseDescriptionCell.toString());
                                }
                                testCase2.setSuite(suite.getId());
                                testCase2 = caseRepository.save(testCase2);
                            } else {
                                testCase.setDescription(testCaseDescriptionCell.toString());
                                caseRepository.save(testCase);
                            }
                        }
                    }
                }
            }


        }

        workbook.close();
    }

    /*-------- Import testScenario ------*/
    @Transactional
    public List<TestScenario> importScenario(Map<String, MultipartFile> fileMap) throws Exception {
        List<TestScenario> scenarios = new ArrayList<>();
        for (MultipartFile file : fileMap.values()) {
            if (file.getOriginalFilename().trim().endsWith(".xlsx")) {
                importScenarioXLSXFormat(file);

            } else if (file.getOriginalFilename().trim().endsWith(".xls")) {
                importScenarioXLSFormat(file);

            } else {
                throw new CassiniException("Please upload correct excel sheet file");
            }

        }
        return scenarios;
    }


}
