package com.cassinisys.test.service;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.test.model.*;
import com.cassinisys.test.repo.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
@Service
public class RunService implements CrudService<TestRun, Integer> {

    @Autowired
    private ScenarioRepository scenarioRepository;

    @Autowired
    private RunConfigurationRepository runConfigurationRepository;

    @Autowired
    private RunRepository runRepository;

    @Autowired
    private RunScenarioRepository runScenarioRepository;

    @Autowired
    private RunPlanRepository runPlanRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private RunSuiteRepository runSuiteRepository;

    @Autowired
    private SuiteRepository suiteRepository;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private TestExecutionRepository testExecutionRepository;

    @Autowired
    private RunExectionRepository runExceRunExectionRepository;

    @Autowired
    private TestProgramExecutionRepository testProgramExecutionRepository;

    @Autowired
    private TestScriptExecutionRepository testScriptExecutionRepository;

    @Autowired
    private RunProgramExecutionRepository runProgramExecutionRepository;

    @Autowired
    private RunScriptExecutionRepository runScriptExecutionRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private RunCaseRepository runCaseRepository;
    @Autowired
    private RunOutputLogRepository runOutputLogRepository;
    @Autowired
    private RunInputparamRepository runInputparamRepository;
    @Autowired
    private RunOutputParamRepository runOutputParamRepository;
    @Autowired
    private RunInputParamValueRepository runInputParamValueRepository;
    @Autowired
    private RunOutputParamExpectedValueRepository runOutputParamExpectedValueRepository;
    @Autowired
    private RunOutputParamActualValueRepository runOutputParamActualValueRepository;

    @Override
    @Transactional(readOnly = false)
    public TestRun create(TestRun testRun) {
        return runRepository.save(testRun);
    }

    @Override
    @Transactional(readOnly = false)
    public TestRun update(TestRun testRun) {
        return runRepository.save(testRun);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {

    }

    @Override
    @Transactional(readOnly = true)
    public TestRun get(Integer id) {
        return runRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public TestCase getTestCaseDetails(Integer testCaseId) {
        return caseRepository.findOne(testCaseId);
    }

    /*------ Get RunCaseDetails -----*/
    @Transactional(readOnly = true)
    public RunCaseDetailsDTO getRunCase(Integer runCaseId) {
        RunCaseDetailsDTO runCaseDetailsDTO = new RunCaseDetailsDTO();
        List<RunInputParam> emptyRunInputParamList = new ArrayList<>();
        List<RunOutPutParam> emptyOutPutParamList = new ArrayList<>();
        RunCase runCase = runCaseRepository.findOne(runCaseId);
        if (runCase != null) {
            runCaseDetailsDTO.setRunCase(runCase);
            List<RunInputParam> runInputParamList = runInputparamRepository.findByTCase(runCase);
            List<RunOutPutParam> runOutPutParamList = runOutputParamRepository.findByTCase(runCase);
            if (runInputParamList.size() > 0 || runOutPutParamList.size() > 0) {
                for (RunInputParam runInputParam : runInputParamList) {
                    RunInputParamValue runInputParamValue = runInputParamValueRepository.findByTCaseAndInputParam(runCase.getId(),
                            runInputParam.getId());
                    if (runInputParamValue != null) {
                        runInputParam.setRunInputParamValue(runInputParamValue);
                        emptyRunInputParamList.add(runInputParam);
                    }
                }
                for (RunOutPutParam runOutPutParam : runOutPutParamList) {
                    RunOutPutParamExpectedValue runOutPutParamExpectedValue = runOutputParamExpectedValueRepository.findByTCaseAndRunOutPutParam(runCase.getId(),
                            runOutPutParam.getId());
                    RunOutputParamActualValue runOutputParamActualValue = runOutputParamActualValueRepository.findByTCaseAndOutputparam(runCase.getId(),
                            runOutPutParam.getId());
                    if (runOutPutParamExpectedValue != null || runOutputParamActualValue != null) {
                        runOutPutParam.setRunOutPutParamExpectedValue(runOutPutParamExpectedValue);
                        runOutPutParam.setRunOutputParamActualValue(runOutputParamActualValue);
                        emptyOutPutParamList.add(runOutPutParam);
                    }
                }
            }

            runCaseDetailsDTO.setRunInputParams(emptyRunInputParamList);
            runCaseDetailsDTO.setRunOutPutParams(emptyOutPutParamList);

        }
        return runCaseDetailsDTO;
    }

    /*-----Create TestRunTree For TestRun Module -----*/
    @Transactional(readOnly = true)
    public TestRun getTestRunTree(Integer runConfigId) {
        TestRun testRunTree = runRepository.findOne(runConfigId);
        if (testRunTree != null) {
            RunOutputLog runOutputLog = runOutputLogRepository.findByTestRun(testRunTree.getId());
            if (runOutputLog != null) {
                testRunTree.setRunOutputLog(runOutputLog);
                RunScenario testScenario = runScenarioRepository.findByTestRun(testRunTree);
                testRunTree.setScenarioName(testScenario.getName());
                testRunTree.setRunScenario(testScenario);
                List<RunPlan> testPlans = runPlanRepository.findByRunScenarioOrderByCreatedDateAsc(testScenario);
                if (testPlans.size() > 0) {
                    testScenario.setChildren(testPlans);
                    for (RunPlan testPlan : testPlans) {
                        List<RunSuite> testSuites = runSuiteRepository.findByRunPlanOrderByCreatedDateAsc(testPlan);
                        if (testSuites.size() > 0) {
                            testPlan.setChildren(testSuites);
                            for (RunSuite testSuite : testSuites) {
                                List<RunCase> testCases = runCaseRepository.findBySuiteOrderByCreatedDateAsc(testSuite);
                                if (testCases.size() > 0) {
                                    testSuite.setChildren(testCases);
                                }
                            }
                        }
                    }
                }
            } else {

                RunScenario testScenario = runScenarioRepository.findByTestRun(testRunTree);
                testRunTree.setScenarioName(testScenario.getName());
                testRunTree.setRunScenario(testScenario);
                List<RunPlan> testPlans = runPlanRepository.findByRunScenarioOrderByCreatedDateAsc(testScenario);
                if (testPlans.size() > 0) {
                    testScenario.setChildren(testPlans);
                    for (RunPlan testPlan : testPlans) {
                        List<RunSuite> testSuites = runSuiteRepository.findByRunPlanOrderByCreatedDateAsc(testPlan);
                        if (testSuites.size() > 0) {
                            testPlan.setChildren(testSuites);
                            for (RunSuite testSuite : testSuites) {
                                List<RunCase> testCases = runCaseRepository.findBySuiteOrderByCreatedDateAsc(testSuite);
                                if (testCases.size() > 0) {
                                    testSuite.setChildren(testCases);
                                }
                            }
                        }
                    }
                }
            }


        }

        return testRunTree;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestRun> getAll() {
        return runRepository.findAll();
    }


    @Transactional(readOnly = true)
    public Page<TestRun> getAllTestRuns(Pageable pageable) {
        return runRepository.findAll(pageable);
    }


    /*-----TestRun Deletion -----*/
    public void deleteTestRun(Integer testRunId) {
        runRepository.delete(testRunId);
    }

    @Transactional(readOnly = false)
    public void importTestRun(MultipartHttpServletRequest request) throws Exception {
        int total = 0, pass = 0, fail = 0;
        for (MultipartFile file : request.getFileMap().values()) {
            if (file.getOriginalFilename().trim().endsWith(".xls")) {
                HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
                HSSFSheet worksheet = workbook.getSheetAt(0);
                HSSFRow headerRow = worksheet.getRow(0);
                for (Integer i = 0; i <= headerRow.getLastCellNum(); i++) {
                    HSSFCell cell = headerRow.getCell(i);
                    if (i > 5) {
                        if (cell != null) {
                            ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findByName(cell.toString());
                            if (objectTypeAttribute == null) {
                                ObjectTypeAttribute objectTypeAttribute1 = new ObjectTypeAttribute();
                                objectTypeAttribute1.setName(cell.toString());
                                objectTypeAttribute1.setDescription(cell.toString());
                                objectTypeAttribute1.setDataType(DataType.STRING);
                                objectTypeAttribute1.setObjectType(TestObjectType.RUNCASE);
                                objectTypeAttribute1 = objectTypeAttributeRepository.save(objectTypeAttribute1);
                            }
                        }
                    }
                }

                String dateFormat = "dd/MM/yyyy";
                HSSFRow scenarioRow = worksheet.getRow(1);
                HSSFCell scenarioCell = scenarioRow.getCell(0);
                TestScenario testScenario = scenarioRepository.findByNameEqualsIgnoreCase(scenarioCell.toString());
                if (testScenario == null) {
                    throw new CassiniException("Scenario does not exist");
                }
                TestRunConfiguration testRunConfiguration = null;
                HSSFCell runConfigCell = scenarioRow.getCell(3);
                if (runConfigCell != null) {
                    testRunConfiguration = runConfigurationRepository.findByScenarioAndName(testScenario, runConfigCell.toString().trim());
                    if (testRunConfiguration == null) {
                        throw new CassiniException("There is no run configuration for " + testScenario.getName() + " scenario");
                    }
                } else {
                    throw new CassiniException("Please provide run configuration name in excel sheet");
                }

                TestRun testRun = new TestRun();

                if (scenarioRow.getCell(5) != null) {
                    HSSFCell dateCell = scenarioRow.getCell(5);
                    if (dateCell != null) {
                        Date date = simpleDateFormat(dateCell.getStringCellValue(), dateFormat);
                        testRun.setStartTime(date);
                        testRun.setFinishTime(date);
                    }
                }

                testRun.setTestRunConfiguration(testRunConfiguration);
                testRun.setStatus(RunStatus.PENDING);
                testRun = runRepository.save(testRun);

                /*---------- Creating TestScenario Structure to RunScenario up to Suites --------*/

                RunScenario runScenario = createRunScenarioStructure(testRun, testScenario);

                for (int row = 0; row <= worksheet.getLastRowNum(); row++) {
                    if (row == 0) continue;
                    HSSFRow testCaseRow = worksheet.getRow(row);
                    RunCase runCase = new RunCase();
                    for (int column = 0; column <= testCaseRow.getLastCellNum(); column++) {

                        if (column == 1) {
                            HSSFCell cell = testCaseRow.getCell(column);
                            if (cell != null) {
                                DataFormatter fmt = new DataFormatter();
                                String valueAsSeenInExcel = fmt.formatCellValue(cell);
                                int result = Integer.parseInt(valueAsSeenInExcel);
                                TestCase testCase = caseRepository.findOne(result);
                                if (testCase == null) {
                                    throw new CassiniException("Given test case does not exist");
                                }
                                TestSuite testSuite = suiteRepository.findOne(testCase.getSuite());
                                TestPlan testPlan = planRepository.findOne(testSuite.getPlan());
                                RunPlan runPlan = runPlanRepository.findByRunScenarioAndName(runScenario, testPlan.getName());
                                RunSuite runSuite = runSuiteRepository.findByRunPlanAndName(runPlan, testSuite.getName());
                                runCase.setName(testCase.getName());
                                runCase.setDescription(testCase.getDescription());
                                runCase.setSuite(runSuite);
                                runCase = runCaseRepository.save(runCase);
                            }

                        }
                        if (column == 4) {
                            HSSFCell resultCell = testCaseRow.getCell(column);
                            if (resultCell != null) {
                                if (resultCell.toString().equals("1.0")) {
                                    pass++;
                                    runCase.setResult(true);
                                } else {
                                    fail++;
                                    runCase.setResult(false);
                                }
                                runCase = runCaseRepository.save(runCase);
                            }

                            total++;
                        }

                        if (column > 5) {
                            HSSFCell valueCell = testCaseRow.getCell(column);
                            HSSFCell headerValue = headerRow.getCell(column);
                            if (headerValue != null && valueCell != null) {
                                /*----------  Updating given values in Excel sheet to ObjectAttribute using RunCase Id ---------------*/
                                ObjectAttribute objectAttribute = updateAttributeValues(valueCell.toString(), headerValue.toString(), runCase);
                            }

                        }
                    }
                }
 /*------Save TestRun Results in Excel XLSX format------*/
                testRun.setFailed(fail);
                testRun.setPassed(pass);
                testRun.setTotal(total);
                runRepository.save(testRun);
            } else if (file.getOriginalFilename().trim().endsWith(".xlsx")) {
                XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
                XSSFSheet worksheet = workbook.getSheetAt(0);
                XSSFRow headerRow = worksheet.getRow(0);
                for (Integer i = 0; i <= headerRow.getLastCellNum(); i++) {
                    XSSFCell cell = headerRow.getCell(i);
                    if (i > 5) {
                        if (cell != null) {
                            ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findByName(cell.toString());
                            if (objectTypeAttribute == null) {
                                ObjectTypeAttribute objectTypeAttribute1 = new ObjectTypeAttribute();
                                objectTypeAttribute1.setName(cell.toString());
                                objectTypeAttribute1.setDescription(cell.toString());
                                objectTypeAttribute1.setDataType(DataType.STRING);
                                objectTypeAttribute1.setObjectType(TestObjectType.RUNCASE);
                                objectTypeAttribute1 = objectTypeAttributeRepository.save(objectTypeAttribute1);
                            }
                        }
                    }
                }

                String dateFormat = "dd/MM/yyyy";
                XSSFRow scenarioRow = worksheet.getRow(1);
                XSSFCell scenarioCell = scenarioRow.getCell(0);
                TestScenario testScenario = scenarioRepository.findByNameEqualsIgnoreCase(scenarioCell.toString());
                if (testScenario == null) {
                    throw new CassiniException("Scenario does not exist");
                }
                TestRunConfiguration testRunConfiguration = null;
                XSSFCell runConfigCell = scenarioRow.getCell(3);
                if (runConfigCell != null) {
                    testRunConfiguration = runConfigurationRepository.findByScenarioAndName(testScenario, runConfigCell.toString().trim());
                    if (testRunConfiguration == null) {
                        throw new CassiniException("There is no run configuration for " + testScenario.getName() + " scenario");
                    }
                } else {
                    throw new CassiniException("Please provide run configuration name in excel sheet");
                }

                TestRun testRun = new TestRun();
                if (scenarioRow.getCell(5) != null) {
                    XSSFCell dateCell = scenarioRow.getCell(5);
                    if (dateCell != null) {
                        Date date = simpleDateFormat(dateCell.getStringCellValue(), dateFormat);
                        testRun.setStartTime(date);
                        testRun.setFinishTime(date);
                    }
                }
                testRun.setTestRunConfiguration(testRunConfiguration);
                testRun.setStatus(RunStatus.PENDING);
                testRun = runRepository.save(testRun);
                /*---------- Creating TestScenario Structure to RunScenario up to Suites --------*/
                RunScenario runScenario = createRunScenarioStructure(testRun, testScenario);
                for (int row = 0; row <= worksheet.getLastRowNum(); row++) {
                    if (row == 0) continue;
                    XSSFRow testCaseRow = worksheet.getRow(row);
                    RunCase runCase = new RunCase();
                    for (int column = 0; column <= testCaseRow.getLastCellNum(); column++) {

                        if (column == 1) {
                            XSSFCell cell = testCaseRow.getCell(column);
                            if (cell != null) {
                                DataFormatter fmt = new DataFormatter();
                                String valueAsSeenInExcel = fmt.formatCellValue(cell);
                                int result = Integer.parseInt(valueAsSeenInExcel);
                                TestCase testCase = caseRepository.findOne(result);
                                if (testCase == null) {
                                    throw new CassiniException("Given test case does not exist");
                                }
                                TestSuite testSuite = suiteRepository.findOne(testCase.getSuite());
                                TestPlan testPlan = planRepository.findOne(testSuite.getPlan());
                                RunPlan runPlan = runPlanRepository.findByRunScenarioAndName(runScenario, testPlan.getName());
                                RunSuite runSuite = runSuiteRepository.findByRunPlanAndName(runPlan, testSuite.getName());
                                runCase.setName(testCase.getName());
                                runCase.setDescription(testCase.getDescription());
                                runCase.setSuite(runSuite);
                                runCase = runCaseRepository.save(runCase);
                            }

                        }

                        if (column == 4) {
                            XSSFCell resultCell = testCaseRow.getCell(column);
                            if (resultCell != null) {
                                if (resultCell.toString().equals("1.0")) {
                                    pass++;
                                    runCase.setResult(true);
                                } else {
                                    fail++;
                                    runCase.setResult(false);
                                }
                                runCase = runCaseRepository.save(runCase);
                            }
                            total++;
                        }

                        if (column > 5) {
                            XSSFCell valueCell = testCaseRow.getCell(column);
                            XSSFCell headerValue = headerRow.getCell(column);
                            if (headerValue != null && valueCell != null) {

                                /*----------  Updating given values in Excel sheet to ObjectAttribute using RunCase Id ---------------*/

                                ObjectAttribute objectAttribute = updateAttributeValues(valueCell.toString(), headerValue.toString(), runCase);
                            }

                        }
                    }
                }
                /*------Save TestRun Results in Excel XLSX format------*/
                testRun.setFailed(fail);
                testRun.setPassed(pass);
                testRun.setTotal(total);
                runRepository.save(testRun);
            } else {
                throw new CassiniException("Please upload Excel sheet");
            }
        }

    }

    /* ------- Create runScenario structure ------*/
    private RunScenario createRunScenarioStructure(TestRun testRun, TestScenario testScenario) {
        RunScenario runScenario = new RunScenario();
        if (testScenario != null) {
            runScenario.setName(testScenario.getName());
            runScenario.setDescription(testScenario.getDescription());
            runScenario.setTestRun(testRun);
            runScenario = runScenarioRepository.save(runScenario);
            List<TestPlan> testPlans = planRepository.findByScenario(testScenario.getId());
            if (testPlans.size() > 0) {
                for (TestPlan testPlan : testPlans) {
                    RunPlan runPlan = new RunPlan();
                    runPlan.setName(testPlan.getName());
                    runPlan.setDescription(testPlan.getDescription());
                    runPlan.setRunScenario(runScenario);
                    runPlan = runPlanRepository.save(runPlan);
                    List<TestSuite> testSuites = suiteRepository.findByPlanOrderByCreatedDateAsc(testPlan.getId());
                    if (testSuites.size() > 0) {
                        for (TestSuite testSuite : testSuites) {
                            RunSuite runSuite = new RunSuite();
                            runSuite.setName(testSuite.getName());
                            runSuite.setDescription(testSuite.getDescription());
                            runSuite.setRunPlan(runPlan);
                            runSuite = runSuiteRepository.save(runSuite);
                        }
                    }
                }
            }
        }
        return runScenario;
    }

    /* -------Update objectattribute values  -----*/
    private ObjectAttribute updateAttributeValues(String value, String header, RunCase runCase) throws Exception {
        ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findByName(header);
        String dateFormat = "dd/MM/yyyy";
        ObjectAttribute objectAttribute = new ObjectAttribute();
        if (objectTypeAttribute != null) {
            ObjectAttributeId objectAttributeId = new ObjectAttributeId();
            objectAttributeId.setObjectId(runCase.getId());
            objectAttributeId.setAttributeDef(objectTypeAttribute.getId());
            objectAttribute.setId(objectAttributeId);
            if (value != null) {
                if (objectTypeAttribute.getDataType().equals(DataType.STRING)) {
                    objectAttribute.setStringValue(value);
                } else if (objectTypeAttribute.getDataType().equals(DataType.INTEGER)) {
                    objectAttribute.setIntegerValue(Integer.parseInt(value));
                } else if (objectTypeAttribute.getDataType().equals(DataType.DOUBLE)) {
                    objectAttribute.setDoubleValue(Double.parseDouble(value));
                } else if (objectTypeAttribute.getDataType().equals(DataType.DATE)) {
                    Date date = simpleDateFormat(value, dateFormat);
                    objectAttribute.setDateValue(date);
                }
            }

            objectAttribute = objectAttributeRepository.save(objectAttribute);

        }

        return objectAttribute;
    }

    /* ------ Throw exception when user enter wrong formated date ------*/
    private Date simpleDateFormat(String date, String format) throws Exception {
        try {
            SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(format);
            Date parseDate = datetimeFormatter1.parse(date);
            Timestamp timestamp1 = new Timestamp(parseDate.getTime());
            return parseDate;
        } catch (ParseException e) {
            throw new CassiniException("Incorrect date format given in Excel sheet. Ex: dd/mm/yyyy");
        }
    }


}
