package com.cassinisys.test.service;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.test.model.*;
import com.cassinisys.test.repo.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
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
public class CaseService implements CrudService<TestCase, Integer> {
    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private TestExecutionRepository testExecutionRepository;

    @Autowired
    private TestProgramExecutionRepository programExecutionRepository;

    @Autowired
    private TestScriptExecutionRepository scriptExecutionRepository;

    @Autowired
    private InputParamRepository inputParamRepository;

    @Autowired
    private OutputParamRepository outputParamRepository;

    @Autowired
    private RcOutputParamExpectedvalueRepository rcOutputParamExpectedvalueRepository;

    @Autowired
    private RcInputParamvalueRepository rcInputParamvalueRepository;

    @Autowired
    private RunConfigurationRepository runConfigurationRepository;

    @Autowired
    private RunRepository runRepository;

    @Autowired
    private ScenarioRepository scenarioRepository;

    /* -------- Create testCase -------*/
    @Override
    @Transactional(readOnly = false)
    public TestCase create(TestCase testCase) {
        TestCase existCase = caseRepository.findBySuiteAndNameEqualsIgnoreCase(testCase.getSuite(), testCase.getName());
        if (existCase == null) {
            if (testCase.getExecutionType().equals("PROGRAM")) {   //If executionType is PROGRAM
                TestProgramExecution programExecution = testCase.getProgramExecution();
                programExecution.setType(ExecutionType.PROGRAM);
                programExecution = programExecutionRepository.save(programExecution);
                testCase.setExecution(programExecution.getId());
                testCase = caseRepository.save(testCase);

            } else if (testCase.getExecutionType().equals("SCRIPT")) {   //If executionType is SCRIPT
                TestScriptExecution scriptExecution = testCase.getScriptExecution();
                scriptExecution.setType(ExecutionType.SCRIPT);
                scriptExecution = scriptExecutionRepository.save(scriptExecution);
                testCase.setExecution(scriptExecution.getId());
                testCase = caseRepository.save(testCase);
            }

        } else {
            throw new CassiniException("Test Case name already exist on Suite");
        }
        return testCase;
    }

    /* ------- Update testCase ------*/
    @Override
    @Transactional(readOnly = false)
    public TestCase update(TestCase testCase) {
        TestCase existCase = caseRepository.findBySuiteAndNameEqualsIgnoreCase(testCase.getSuite(),
                testCase.getName());
        if (existCase == null) {
            if (testCase.getExecution() == null) {
                if (testCase.getExecutionType().equals("PROGRAM")) {     //If executionType is PROGRAM
                    TestProgramExecution programExecution = testCase.getProgramExecution();
                    programExecution.setType(ExecutionType.PROGRAM);
                    programExecution = programExecutionRepository.save(programExecution);
                    testCase.setExecution(programExecution.getId());
                    testCase = caseRepository.save(testCase);

                } else if (testCase.getExecutionType().equals("SCRIPT")) {    //If executionType is SCRIPT
                    TestScriptExecution scriptExecution = testCase.getScriptExecution();
                    scriptExecution.setType(ExecutionType.SCRIPT);
                    scriptExecution = scriptExecutionRepository.save(scriptExecution);
                    testCase.setExecution(scriptExecution.getId());
                    testCase = caseRepository.save(testCase);
                }

            } else {
                testCase = updateTestCaseExecution(testCase);   //Calling updateTestCaseExecution() method
            }
        } else {
            if (existCase.getId().equals(testCase.getId())) {
                if (testCase.getExecution() == null) {
                    if (testCase.getExecutionType().equals("PROGRAM")) {
                        TestProgramExecution programExecution = testCase.getProgramExecution();
                        programExecution.setType(ExecutionType.PROGRAM);
                        programExecution = programExecutionRepository.save(programExecution);
                        testCase.setExecution(programExecution.getId());
                        testCase = caseRepository.save(testCase);

                    } else if (testCase.getExecutionType().equals("SCRIPT")) {
                        TestScriptExecution scriptExecution = testCase.getScriptExecution();
                        scriptExecution.setType(ExecutionType.SCRIPT);
                        scriptExecution = scriptExecutionRepository.save(scriptExecution);
                        testCase.setExecution(scriptExecution.getId());
                        testCase = caseRepository.save(testCase);
                    }

                } else {
                    testCase = updateTestCaseExecution(testCase);
                }
            } else {
                throw new CassiniException("Test Case name already exist on Suite");
            }

        }
        return testCase;
    }

    /* ----- Update testCaseExecution -----*/
    @Transactional(readOnly = false)
    private TestCase updateTestCaseExecution(TestCase testCase) {
        TestCase testCase1 = caseRepository.findOne(testCase.getId());
        Integer executionType = testCase1.getExecution();
        TestExecution testExecution = testExecutionRepository.findOne(executionType);
        if (testExecution.getType().toString().equals(testCase.getExecutionType())) {
            if (testExecution.getType().toString().equals("PROGRAM")) {
                TestProgramExecution updateProgramExecution = programExecutionRepository.findOne(executionType);
                updateProgramExecution.setProgram(testCase.getProgramExecution().getProgram());
                updateProgramExecution.setParams(testCase.getProgramExecution().getParams());
                updateProgramExecution.setWorkingDir(testCase.getProgramExecution().getWorkingDir());
                updateProgramExecution = programExecutionRepository.save(updateProgramExecution);
                testCase = caseRepository.save(testCase);
                testCase.setProgramExecution(updateProgramExecution);
                testCase.setExecutionType(updateProgramExecution.getType().toString());
            } else if (testExecution.getType().toString().equals("SCRIPT")) {
                TestScriptExecution updateScriptExecution = scriptExecutionRepository.findOne(executionType);
                updateScriptExecution.setScriptLanguage(testCase.getScriptExecution().getScriptLanguage());
                updateScriptExecution.setScript(testCase.getScriptExecution().getScript());
                updateScriptExecution = scriptExecutionRepository.save(updateScriptExecution);
                testCase = caseRepository.save(testCase);
                testCase.setScriptExecution(updateScriptExecution);
                testCase.setExecutionType(updateScriptExecution.getType().toString());
            }


        } else {
            if (testCase.getExecutionType().equals("PROGRAM")) {
                TestProgramExecution programExecution = testCase.getProgramExecution();
                programExecution.setId(null);
                programExecution.setType(ExecutionType.PROGRAM);
                programExecution = programExecutionRepository.save(programExecution);
                testCase.setExecution(programExecution.getId());
                testCase = caseRepository.save(testCase);
                testCase.setProgramExecution(programExecution);
                testCase.setExecutionType(programExecution.getType().toString());
            } else if (testCase.getExecutionType().equals("SCRIPT")) {
                TestScriptExecution scriptExecution = testCase.getScriptExecution();
                scriptExecution.setId(null);
                scriptExecution.setType(ExecutionType.SCRIPT);
                scriptExecution = scriptExecutionRepository.save(scriptExecution);
                testCase.setExecution(scriptExecution.getId());
                testCase = caseRepository.save(testCase);
                testCase.setScriptExecution(scriptExecution);
                testCase.setExecutionType(scriptExecution.getType().toString());
            }

            testExecutionRepository.delete(testExecution.getId());
        }

        return testCase;
    }

    /* ----- Delete testCase ------*/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        caseRepository.delete(id);
    }

    /* ----- Get testCase based on testCaseId -----*/
    @Override
    @Transactional(readOnly = true)
    public TestCase get(Integer id) {
        TestCase testCase = caseRepository.findOne(id);
        return testCase;
    }

    /*------ Get inputParam and outputParams based on testCaseId ------ */
    @Transactional(readOnly = true)
    public TestCase getInputParamAndOutputParamsByTestCase(Integer testCaseId) {
        TestCase testCase = caseRepository.findOne(testCaseId);
        if (testCase.getExecution() != null) {
            TestExecution testExecution = testExecutionRepository.findOne(testCase.getExecution());
            if (testExecution.getType().toString().equals("PROGRAM")) {
                TestProgramExecution programExecution = programExecutionRepository.findOne(testExecution.getId());
                testCase.setProgramExecution(programExecution);
            } else if (testExecution.getType().toString().equals("SCRIPT")) {
                TestScriptExecution scriptExecution = scriptExecutionRepository.findOne(testExecution.getId());
                testCase.setScriptExecution(scriptExecution);
            }
            testCase.setExecutionType(testExecution.getType().toString());
            List<TestInputParam> testInputParams = inputParamRepository.findByTestCase(testCase.getId());
            List<TestOutputParam> testOutputParams = outputParamRepository.findByTestCase(testCase.getId());
            testCase.setTestInputParams(testInputParams);
            testCase.setTestOutputParams(testOutputParams);
        } else {
            List<TestInputParam> testInputParams = inputParamRepository.findByTestCase(testCase.getId());
            List<TestOutputParam> testOutputParams = outputParamRepository.findByTestCase(testCase.getId());
            testCase.setTestInputParams(testInputParams);
            testCase.setTestOutputParams(testOutputParams);
        }
        return testCase;
    }

    /*Get inputParamValues based on configId and testCaseId*/
    @Transactional(readOnly = true)
    public TestCase getInputParamValuesByConfigAndTestCase(Integer runConfigurationId, Integer testCaseId) {
        TestCase testCase = caseRepository.findOne(testCaseId);
        List<TestInputParam> testInputParams = inputParamRepository.findByTestCase(testCase.getId());
        if (testInputParams.size() > 0) {
            for (TestInputParam testInputParam : testInputParams) {
                RCInputParamValue rcInputParamValue = rcInputParamvalueRepository.findByConfigAndTestCaseAndInputParam(runConfigurationId,
                        testCaseId, testInputParam.getId());
                if (rcInputParamValue != null) {
                    testInputParam.setRcInputParamValue(rcInputParamValue);
                }
            }
        }

        List<TestOutputParam> testOutputParams = outputParamRepository.findByTestCase((testCase.getId()));
        for (TestOutputParam testOutputParam : testOutputParams) {
            RCOutPutParamExpectedValue outPutParamExpectedValue = rcOutputParamExpectedvalueRepository.
                    findByConfigAndTestCaseAndOutputParam(runConfigurationId, testCaseId, testOutputParam.getId());
            if (outPutParamExpectedValue != null) {
                testOutputParam.setOutPutParamExpectedValue(outPutParamExpectedValue);
            }
        }
        testCase.setTestInputParams(testInputParams);
        testCase.setTestOutputParams(testOutputParams);
        return testCase;
    }

    /* ------- Get all testCase ------*/
    @Override
    @Transactional(readOnly = true)
    public List<TestCase> getAll() {
        return caseRepository.findAll();
    }


    /*------------------- Input and Output Params Methods --------------------*/

    @Transactional(readOnly = false)
    public TestInputParam createInputParam(TestInputParam testInputParam) {
        return inputParamRepository.save(testInputParam);
    }

    @Transactional(readOnly = false)
    public TestInputParam updateInputParam(TestInputParam testInputParam) {
        return inputParamRepository.save(testInputParam);
    }

    @Transactional(readOnly = false)
    public void deleteInputParam(Integer paramId) {
        inputParamRepository.delete(paramId);
    }


    @Transactional(readOnly = false)
    public TestOutputParam createOutputParam(TestOutputParam testOutputParam) {
        return outputParamRepository.save(testOutputParam);
    }

    @Transactional(readOnly = false)
    public TestOutputParam updateOutputParam(TestOutputParam testOutputParam) {
        return outputParamRepository.save(testOutputParam);
    }

    @Transactional(readOnly = false)
    public void deleteOutputParam(Integer paramId) {
        outputParamRepository.delete(paramId);
    }

    @Transactional(readOnly = false)
    public RCInputParamValue createInputParamValue(RCInputParamValue testInputParam) {
        return rcInputParamvalueRepository.save(testInputParam);
    }

    @Transactional(readOnly = false)
    public RCInputParamValue updateInputParamValue(RCInputParamValue testInputParam) {
        return rcInputParamvalueRepository.save(testInputParam);
    }

    @Transactional(readOnly = false)
    public void deleteInputParamValue(Integer paramId) {
        rcInputParamvalueRepository.delete(paramId);
    }

    @Transactional(readOnly = false)
    public RCOutPutParamExpectedValue createExpectedOutputValue(RCOutPutParamExpectedValue outPutParamExpectedValue) {
        return rcOutputParamExpectedvalueRepository.save(outPutParamExpectedValue);
    }

    @Transactional(readOnly = false)
    public RCOutPutParamExpectedValue updateExpectedOutputValue(RCOutPutParamExpectedValue outPutParamExpectedValue) {
        return rcOutputParamExpectedvalueRepository.save(outPutParamExpectedValue);
    }

    @Transactional(readOnly = false)
    public void deleteExpectedOutputValue(Integer paramId) {
        rcOutputParamExpectedvalueRepository.delete(paramId);
    }

    /*------Import inputParams At TestDefinition --------*/

    @Transactional(readOnly = false)
    public void importInputParams(Integer testCaseId, MultipartHttpServletRequest request) throws Exception {

        for (MultipartFile file : request.getFileMap().values()) {
            if (file.getOriginalFilename().trim().endsWith(".xlsx")) {
                importInputParamsXLSXFormat(testCaseId, request, file);
            } else if (file.getOriginalFilename().trim().endsWith(".xls")) {
                importInputParamsXLSFormat(testCaseId, request, file);
            } else {
                throw new IllegalArgumentException("Please upload excel sheet");
            }

        }
    }

    private void importInputParamsXLSXFormat(Integer testCaseId, MultipartHttpServletRequest request, MultipartFile file) throws Exception {
        if (file.getOriginalFilename().trim().endsWith(".xlsx")) {
            List<TestInputParam> testInputParamValues = new ArrayList<>();
            int i = 0;
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);
            // Reads the data in excel file until last row is encountered
            while (i <= worksheet.getLastRowNum()) {
                // Creates an object for the IMItemType Model
                TestInputParam inputParam = new TestInputParam();
                // Creates an object representing a single row in excel
                XSSFRow row = worksheet.getRow(i++);
                // Sets the Read data to the model class
                if (row.getRowNum() == 0) continue;
                XSSFCell cell1 = row.getCell(0);
                XSSFCell cell2 = row.getCell(1);
                XSSFCell cell3 = row.getCell(2);
                if (cell1 != null && cell2 != null && cell3 != null) {
                    FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
                    DataFormatter objDefaultFormat = new DataFormatter();
                    objFormulaEvaluator.evaluate(cell1);
                    String celToString = objDefaultFormat.formatCellValue(cell1, objFormulaEvaluator);
                    FormulaEvaluator objFormulaEvaluator1 = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
                    DataFormatter objDefaultFormat1 = new DataFormatter();
                    objFormulaEvaluator1.evaluate(cell2);
                    String celToString2 = objDefaultFormat1.formatCellValue(cell2, objFormulaEvaluator1);
                    FormulaEvaluator objFormulaEvaluator3 = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
                    DataFormatter objDefaultFormat3 = new DataFormatter();
                    objFormulaEvaluator3.evaluate(cell3);
                    String celToString3 = objDefaultFormat1.formatCellValue(cell3, objFormulaEvaluator3);
                    inputParam.setTestCase(testCaseId);
                    inputParam.setName(celToString);
                    inputParam.setDescription(celToString2);
                    inputParam.setDataType(DataType.valueOf(row.getCell(2).getStringCellValue()));
                    testInputParamValues.add(inputParam);
                } else {
                    throw new CassiniException("Please enter proper data");

                }

            }
            workbook.close();
            //Save all added testInputParamValues
            for (TestInputParam inputParamType : testInputParamValues) {
                inputParamRepository.save(inputParamType);
            }

        } else {
            throw new IllegalArgumentException("Please upload excel sheet");
        }


    }


    private void importInputParamsXLSFormat(Integer testCaseId, MultipartHttpServletRequest request, MultipartFile file) throws Exception {
        if (file.getOriginalFilename().trim().endsWith(".xls")) {
            List<TestInputParam> testInputParamValues = new ArrayList<>();
            int i = 0;
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            HSSFSheet worksheet = workbook.getSheetAt(0);
            // Reads the data in excel file until last row is encountered
            while (i <= worksheet.getLastRowNum()) {
                // Creates an object for the IMItemType Model
                TestInputParam inputParam = new TestInputParam();
                // Creates an object representing a single row in excel
                HSSFRow row = worksheet.getRow(i++);
                // Sets the Read data to the model class
                if (row.getRowNum() == 0) continue;
                HSSFCell cell1 = row.getCell(0);
                HSSFCell cell2 = row.getCell(1);
                HSSFCell cell3 = row.getCell(2);
                if (cell1 != null && cell2 != null && cell3 != null) {
                    FormulaEvaluator objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
                    DataFormatter objDefaultFormat = new DataFormatter();
                    objFormulaEvaluator.evaluate(cell1);
                    String celToString = objDefaultFormat.formatCellValue(cell1, objFormulaEvaluator);
                    FormulaEvaluator objFormulaEvaluator1 = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
                    DataFormatter objDefaultFormat1 = new DataFormatter();
                    objFormulaEvaluator1.evaluate(cell2);
                    String celToString2 = objDefaultFormat1.formatCellValue(cell2, objFormulaEvaluator1);
                    FormulaEvaluator objFormulaEvaluator3 = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
                    DataFormatter objDefaultFormat3 = new DataFormatter();
                    objFormulaEvaluator3.evaluate(cell3);
                    String celToString3 = objDefaultFormat1.formatCellValue(cell3, objFormulaEvaluator3);
                    inputParam.setTestCase(testCaseId);
                    inputParam.setName(celToString);
                    inputParam.setDescription(celToString2);
                    inputParam.setDataType(DataType.valueOf(row.getCell(2).getStringCellValue()));
                    testInputParamValues.add(inputParam);
                } else {
                    throw new CassiniException("Please enter proper data");

                }

            }
            workbook.close();
            //Save all added testInputParamValues
            for (TestInputParam inputParamType : testInputParamValues) {
                inputParamRepository.save(inputParamType);
            }

        } else {
            throw new IllegalArgumentException("Please upload excel sheet");
        }


    }


    /*-------Import OutPutParams At TestDefinition ------*/

    @Transactional(readOnly = false)
    public void importOutPutParams(Integer testCaseId, MultipartHttpServletRequest request) throws Exception {

        for (MultipartFile file : request.getFileMap().values()) {
            if (file.getOriginalFilename().trim().endsWith(".xlsx")) {
                importOutPutParamsXLSXFormat(testCaseId, request, file);
            } else if (file.getOriginalFilename().trim().endsWith(".xls")) {
                importOutputParamsXLSFormat(testCaseId, request, file);
            } else {
                throw new IllegalArgumentException("Please upload excel sheet");
            }

        }
    }


    private void importOutPutParamsXLSXFormat(Integer testCaseId, MultipartHttpServletRequest request, MultipartFile file) throws Exception {
        if (file.getOriginalFilename().trim().endsWith(".xlsx")) {
            List<TestOutputParam> testOutputParams = new ArrayList<>();
            int i = 0;
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);
            // Reads the data in excel file until last row is encountered
            while (i <= worksheet.getLastRowNum()) {
                // Creates an object for the IMItemType Model
                TestOutputParam outputParam = new TestOutputParam();
                // Creates an object representing a single row in excel
                XSSFRow row = worksheet.getRow(i++);
                // Sets the Read data to the model class
                if (row.getRowNum() == 0) continue;
                XSSFCell cell1 = row.getCell(0);
                XSSFCell cell2 = row.getCell(1);
                XSSFCell cell3 = row.getCell(2);
                if (cell1 != null && cell2 != null && cell3 != null) {
                    FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
                    DataFormatter objDefaultFormat = new DataFormatter();
                    objFormulaEvaluator.evaluate(cell1);
                    String celToString = objDefaultFormat.formatCellValue(cell1, objFormulaEvaluator);
                    FormulaEvaluator objFormulaEvaluator1 = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
                    DataFormatter objDefaultFormat1 = new DataFormatter();
                    objFormulaEvaluator1.evaluate(cell2);
                    String celToString2 = objDefaultFormat1.formatCellValue(cell2, objFormulaEvaluator1);
                    FormulaEvaluator objFormulaEvaluator3 = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
                    DataFormatter objDefaultFormat3 = new DataFormatter();
                    objFormulaEvaluator3.evaluate(cell3);
                    String celToString3 = objDefaultFormat1.formatCellValue(cell3, objFormulaEvaluator3);
                    outputParam.setTestCase(testCaseId);
                    outputParam.setName(celToString);
                    outputParam.setDescription(celToString2);
                    outputParam.setDataType(DataType.valueOf(row.getCell(2).getStringCellValue()));
                    testOutputParams.add(outputParam);

                } else {
                    throw new CassiniException("Please enter proper data");

                }
            }
            workbook.close();
            //Save all added testOutputParams
            for (TestOutputParam testOutputParam : testOutputParams) {
                outputParamRepository.save(testOutputParam);
            }

        } else {
            throw new IllegalArgumentException("Please upload excel sheet");
        }


    }


    private void importOutputParamsXLSFormat(Integer testCaseId, MultipartHttpServletRequest request, MultipartFile file) throws Exception {
        if (file.getOriginalFilename().trim().endsWith(".xls")) {
            List<TestOutputParam> testOutputParams = new ArrayList<>();
            int i = 0;
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            HSSFSheet worksheet = workbook.getSheetAt(0);
            // Reads the data in excel file until last row is encountered
            while (i <= worksheet.getLastRowNum()) {
                // Creates an object for the IMItemType Model
                TestOutputParam outputParam = new TestOutputParam();
                // Creates an object representing a single row in excel
                HSSFRow row = worksheet.getRow(i++);
                // Sets the Read data to the model class
                if (row.getRowNum() == 0) continue;
                HSSFCell cell1 = row.getCell(0);
                HSSFCell cell2 = row.getCell(1);
                HSSFCell cell3 = row.getCell(2);
                if (cell1 != null && cell2 != null && cell3 != null) {
                    FormulaEvaluator objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
                    DataFormatter objDefaultFormat = new DataFormatter();
                    objFormulaEvaluator.evaluate(cell1);
                    String celToString = objDefaultFormat.formatCellValue(cell1, objFormulaEvaluator);
                    FormulaEvaluator objFormulaEvaluator1 = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
                    DataFormatter objDefaultFormat1 = new DataFormatter();
                    objFormulaEvaluator1.evaluate(cell2);
                    String celToString2 = objDefaultFormat1.formatCellValue(cell2, objFormulaEvaluator1);
                    FormulaEvaluator objFormulaEvaluator3 = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
                    DataFormatter objDefaultFormat3 = new DataFormatter();
                    objFormulaEvaluator3.evaluate(cell3);
                    String celToString3 = objDefaultFormat1.formatCellValue(cell3, objFormulaEvaluator3);
                    outputParam.setTestCase(testCaseId);
                    outputParam.setName(celToString);
                    outputParam.setDescription(celToString2);
                    outputParam.setDataType(DataType.valueOf(row.getCell(2).getStringCellValue()));
                    testOutputParams.add(outputParam);

                } else {
                    throw new CassiniException("Please enter proper data");

                }
            }
            workbook.close();
            //Save all added testOutputParams
            for (TestOutputParam testOutputParam : testOutputParams) {
                outputParamRepository.save(testOutputParam);
            }

        } else {
            throw new IllegalArgumentException("Please upload excel sheet");
        }


    }


    /*-------method to ImportInputParamValuesXLSXFormat------*/
    private void importInputParamValuesXLSXFormat(Integer testCaseId, Integer runConfig, MultipartHttpServletRequest request,
                                                  MultipartFile file) throws IOException, ParseException, Exception {
        List<RCInputParamValue> rcInputParamValues = new ArrayList<>();
        int i = 0;
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);
        FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
        FormulaEvaluator objFormulaEvaluator1 = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
        DataFormatter objDefaultFormat = new DataFormatter();
        DataFormatter objDefaultFormat1 = new DataFormatter();
        // Reads the data in excel file until last row is encountered
        while (i <= worksheet.getLastRowNum()) {

            // Creates an object for the IMItemType Model
            RCInputParamValue rcInputParamValue = new RCInputParamValue();
            // Creates an object representing a single row in excel
            XSSFRow row = worksheet.getRow(i++);
            // Sets the Read data to the model class
            if (row.getRowNum() == 0) continue;
            XSSFCell cell1 = row.getCell(0);
            objFormulaEvaluator.evaluate(cell1);
            String cellValueStr = objDefaultFormat.formatCellValue(cell1, objFormulaEvaluator);
            XSSFCell cell2 = row.getCell(1);
            objFormulaEvaluator1.evaluate(cell2);
            String celToString = objDefaultFormat1.formatCellValue(cell2, objFormulaEvaluator1);
            if (cell1 != null && cell2 != null) {
                TestInputParam testInputParam = getName(cellValueStr, testCaseId);
                if (testInputParam != null) {
                    if (testInputParam.getId() != null) {
                        if (testInputParam.getDataType().equals(DataType.valueOf("STRING"))) {
                            rcInputParamValue.setStringValue(celToString);
                            rcInputParamValue.setTestCase(testCaseId);
                            rcInputParamValue.setConfig(runConfig);
                            rcInputParamValue.setInputParam(testInputParam.getId());
                            rcInputParamValues.add(rcInputParamValue);
                        }
                        if (testInputParam.getDataType().equals(DataType.valueOf("INTEGER"))) {
                            int intVal = Integer.parseInt(celToString);
                            rcInputParamValue.setIntegerValue(intVal);
                            rcInputParamValue.setTestCase(testCaseId);
                            rcInputParamValue.setConfig(runConfig);
                            rcInputParamValue.setInputParam(testInputParam.getId());
                            rcInputParamValues.add(rcInputParamValue);
                        }
                        if (testInputParam.getDataType().equals(DataType.valueOf("DOUBLE"))) {
                            double doubleVal = Double.parseDouble(celToString);
                            rcInputParamValue.setDoubleValue(doubleVal);
                            rcInputParamValue.setTestCase(testCaseId);
                            rcInputParamValue.setConfig(runConfig);
                            rcInputParamValue.setInputParam(testInputParam.getId());
                            rcInputParamValues.add(rcInputParamValue);
                        }
                        if (testInputParam.getDataType().equals(DataType.valueOf("DATE"))) {
                            String dateFormat = "dd/MM/yyyy";
                            Date date = simpleDateFormat(celToString, dateFormat);
                            rcInputParamValue.setDateValue(date);
                            rcInputParamValue.setTestCase(testCaseId);
                            rcInputParamValue.setConfig(runConfig);
                            rcInputParamValue.setInputParam(testInputParam.getId());
                            rcInputParamValues.add(rcInputParamValue);
                        }


                    }
                } else {
                    TestInputParam inputParam = new TestInputParam();
                    inputParam.setName(cellValueStr);
                    inputParam.setTestCase(testCaseId);
                    inputParam.setDataType(DataType.valueOf("STRING"));
                    TestInputParam inputParam1 = inputParamRepository.save(inputParam);

                    if (inputParam1 != null) {
                        if (inputParam1.getId() != null) {
                            if (inputParam1.getDataType().equals(DataType.valueOf("STRING"))) {
                                rcInputParamValue.setStringValue(celToString);
                                rcInputParamValue.setTestCase(testCaseId);
                                rcInputParamValue.setConfig(runConfig);
                                rcInputParamValue.setInputParam(inputParam1.getId());
                                rcInputParamValues.add(rcInputParamValue);
                            }


                        }
                    }


                }

            } else if (cell1 == null) {
                throw new CassiniException("Name column can not be empty");
            } else if (cell2 == null) {
                throw new CassiniException("Value column can not be empty");
            } else {

                throw new CassiniException("Please enter proper data");
            }

            workbook.close();
            //Save all added rcInputParamValues
            for (RCInputParamValue inputParamType : rcInputParamValues) {
                RCInputParamValue rcInputParamVal = rcInputParamvalueRepository.findByConfigAndTestCaseAndInputParam(inputParamType.getConfig(),
                        inputParamType.getTestCase(), inputParamType.getInputParam());
                if (rcInputParamVal != null) {
                    rcInputParamVal.setIntegerValue(inputParamType.getIntegerValue());
                    rcInputParamVal.setStringValue(inputParamType.getStringValue());
                    rcInputParamVal.setDateValue(inputParamType.getDateValue());
                    rcInputParamVal.setDoubleValue(inputParamType.getDoubleValue());
                    rcInputParamVal.setTimeValue(inputParamType.getTimeValue());
                    rcInputParamvalueRepository.save(rcInputParamVal);
                } else {
                    rcInputParamvalueRepository.save(inputParamType);
                }
            }
        }

    }

    /*-------method to ImportInputParamValuesXLSFormat------*/

    private void importInputParamValuesXLSFormat(Integer testCaseId, Integer runConfig, MultipartHttpServletRequest request,
                                                 MultipartFile file) throws IOException, ParseException, Exception {

        List<RCInputParamValue> rcInputParamValues = new ArrayList<>();
        List<TestInputParam> inputParamArrayList = new ArrayList<>();
        int i = 0;
        HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
        HSSFSheet worksheet = workbook.getSheetAt(0);
        FormulaEvaluator objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
        FormulaEvaluator objFormulaEvaluator1 = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
        DataFormatter objDefaultFormat = new DataFormatter();
        DataFormatter objDefaultFormat1 = new DataFormatter();
        // Reads the data in excel file until last row is encountered
        while (i <= worksheet.getLastRowNum()) {
            // Creates an object for the IMItemType Model
            RCInputParamValue rcInputParamValue = new RCInputParamValue();
            // Creates an object representing a single row in excel
            HSSFRow row = worksheet.getRow(i++);
            // Sets the Read data to the model class
            if (row.getRowNum() == 0) continue;
            HSSFCell cell1 = row.getCell(0);
            objFormulaEvaluator.evaluate(cell1);
            String cellValueStr = objDefaultFormat.formatCellValue(cell1, objFormulaEvaluator);
            HSSFCell cell2 = row.getCell(1);
            objFormulaEvaluator1.evaluate(cell2);
            String celToString = objDefaultFormat1.formatCellValue(cell2, objFormulaEvaluator1);
            if (cell1 != null && cell2 != null) {
                TestInputParam testInputParam = getName(cellValueStr, testCaseId);
                if (testInputParam != null) {
                    if (testInputParam.getId() != null) {
                        if (testInputParam.getDataType().equals(DataType.valueOf("STRING"))) {
                            rcInputParamValue.setStringValue(celToString);
                            rcInputParamValue.setTestCase(testCaseId);
                            rcInputParamValue.setConfig(runConfig);
                            rcInputParamValue.setInputParam(testInputParam.getId());
                            rcInputParamValues.add(rcInputParamValue);
                        }
                        if (testInputParam.getDataType().equals(DataType.valueOf("INTEGER"))) {
                            int intVal = Integer.parseInt(celToString);
                            rcInputParamValue.setIntegerValue(intVal);
                            rcInputParamValue.setTestCase(testCaseId);
                            rcInputParamValue.setConfig(runConfig);
                            rcInputParamValue.setInputParam(testInputParam.getId());
                            rcInputParamValues.add(rcInputParamValue);
                        }
                        if (testInputParam.getDataType().equals(DataType.valueOf("DOUBLE"))) {
                            double doubleVal = Double.parseDouble(celToString);
                            rcInputParamValue.setDoubleValue(doubleVal);
                            rcInputParamValue.setTestCase(testCaseId);
                            rcInputParamValue.setConfig(runConfig);
                            rcInputParamValue.setInputParam(testInputParam.getId());
                            rcInputParamValues.add(rcInputParamValue);
                        }
                        if (testInputParam.getDataType().equals(DataType.valueOf("DATE"))) {
                            String dateFormat = "dd/MM/yyyy";
                            Date date = simpleDateFormat(celToString, dateFormat);
                            rcInputParamValue.setDateValue(date);
                            rcInputParamValue.setTestCase(testCaseId);
                            rcInputParamValue.setConfig(runConfig);
                            rcInputParamValue.setInputParam(testInputParam.getId());
                            rcInputParamValues.add(rcInputParamValue);
                        }
                    }
                } else {
                    TestInputParam inputParam = new TestInputParam();
                    inputParam.setName(cellValueStr);
                    inputParam.setTestCase(testCaseId);
                    inputParam.setDataType(DataType.valueOf("STRING"));
                    TestInputParam inputParam1 = inputParamRepository.save(inputParam);
                    if (inputParam1 != null) {
                        if (inputParam1.getId() != null) {
                            if (inputParam1.getDataType().equals(DataType.valueOf("STRING"))) {
                                rcInputParamValue.setStringValue(celToString);
                                rcInputParamValue.setTestCase(testCaseId);
                                rcInputParamValue.setConfig(runConfig);
                                rcInputParamValue.setInputParam(inputParam1.getId());
                                rcInputParamValues.add(rcInputParamValue);
                            }
                        }
                    }
                }

            } else if (cell1 == null) {
                throw new CassiniException("Name column can not be empty");
            } else if (cell2 == null) {
                throw new CassiniException("Value column can not be empty");
            } else {

                throw new CassiniException("Please enter proper data");
            }
        }
        workbook.close();
        //Save all added rcInputParamValues
        for (RCInputParamValue inputParamType : rcInputParamValues) {
            RCInputParamValue rcInputParamValue = rcInputParamvalueRepository.findByConfigAndTestCaseAndInputParam(inputParamType.getConfig(),
                    inputParamType.getTestCase(), inputParamType.getInputParam());
            if (rcInputParamValue != null) { //update
                rcInputParamValue.setIntegerValue(inputParamType.getIntegerValue());
                rcInputParamValue.setStringValue(inputParamType.getStringValue());
                rcInputParamValue.setDateValue(inputParamType.getDateValue());
                rcInputParamValue.setDoubleValue(inputParamType.getDoubleValue());
                rcInputParamValue.setTimeValue(inputParamType.getTimeValue());
                rcInputParamvalueRepository.save(rcInputParamValue);
            } else { //Create
                rcInputParamvalueRepository.save(inputParamType);
            }


        }
    }

    /*------- Import InputParamValues At RunConfiguration ------*/
    @Transactional(readOnly = false)
    public void importInputParamValues(Integer testCaseId, Integer runConfig,
                                       MultipartHttpServletRequest request) throws Exception {
        for (MultipartFile file : request.getFileMap().values()) {
            if (file.getOriginalFilename().trim().endsWith(".xlsx")) {
                importInputParamValuesXLSXFormat(testCaseId, runConfig, request, file);
            } else if (file.getOriginalFilename().trim().endsWith(".xls")) {
                importInputParamValuesXLSFormat(testCaseId, runConfig, request, file);
            } else {
                throw new IllegalArgumentException("Please upload excel sheet");
            }

        }
    }

    /*------ importOutPutParamExpectedValueXLSXFormat -----*/
    private void importOutPutParamExpectedValueXLSXFormat(Integer testCaseId, Integer runConfig, MultipartHttpServletRequest request,
                                                          MultipartFile file) throws IOException, ParseException, Exception {

        List<RCOutPutParamExpectedValue> rcOutPutParamExpectedValues = new ArrayList<>();
        int i = 0;
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);
        FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
        FormulaEvaluator objFormulaEvaluator1 = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
        DataFormatter objDefaultFormat = new DataFormatter();
        DataFormatter objDefaultFormat1 = new DataFormatter();
        // Reads the data in excel file until last row is encountered
        while (i <= worksheet.getLastRowNum()) {

            // Creates an object for the IMItemType Model
            RCOutPutParamExpectedValue rcOutPutParamExpectedValue = new RCOutPutParamExpectedValue();
            // Creates an object representing a single row in excel
            XSSFRow row = worksheet.getRow(i++);
            // Sets the Read data to the model class
            if (row.getRowNum() == 0) continue;
            XSSFCell cell1 = row.getCell(0);
            objFormulaEvaluator.evaluate(cell1);
            String cellValueStr = objDefaultFormat.formatCellValue(cell1, objFormulaEvaluator);
            XSSFCell cell2 = row.getCell(1);
            objFormulaEvaluator1.evaluate(cell2);
            String celToString = objDefaultFormat1.formatCellValue(cell2, objFormulaEvaluator1);
            if ((cellValueStr != null || cellValueStr != "") && (celToString != null || celToString != "")) {
                TestOutputParam testOutputParam = getNameOutPutParam(cellValueStr, testCaseId);
                if (testOutputParam != null) {
                    if (testOutputParam.getId() != null) {
                        if (testOutputParam.getDataType().equals(DataType.valueOf("STRING"))) {
                            rcOutPutParamExpectedValue.setStringValue(celToString);
                            rcOutPutParamExpectedValue.setTestCase(testCaseId);
                            rcOutPutParamExpectedValue.setConfig(runConfig);
                            rcOutPutParamExpectedValue.setOutputParam(testOutputParam.getId());
                            rcOutPutParamExpectedValues.add(rcOutPutParamExpectedValue);
                        }
                        if (testOutputParam.getDataType().equals(DataType.valueOf("INTEGER"))) {
                            int intVal = Integer.parseInt(celToString);
                            rcOutPutParamExpectedValue.setIntegerValue(intVal);
                            rcOutPutParamExpectedValue.setTestCase(testCaseId);
                            rcOutPutParamExpectedValue.setConfig(runConfig);
                            rcOutPutParamExpectedValue.setOutputParam(testOutputParam.getId());
                            rcOutPutParamExpectedValues.add(rcOutPutParamExpectedValue);
                        }
                        if (testOutputParam.getDataType().equals(DataType.valueOf("DOUBLE"))) {
                            double doubleVal = Double.parseDouble(celToString);
                            rcOutPutParamExpectedValue.setDoubleValue(doubleVal);
                            rcOutPutParamExpectedValue.setTestCase(testCaseId);
                            rcOutPutParamExpectedValue.setConfig(runConfig);
                            rcOutPutParamExpectedValue.setOutputParam(testOutputParam.getId());
                            rcOutPutParamExpectedValues.add(rcOutPutParamExpectedValue);
                        }
                        if (testOutputParam.getDataType().equals(DataType.valueOf("DATE"))) {
                            String dateFormat = "dd/MM/yyyy";
                            Date date = simpleDateFormat(celToString, dateFormat);
                            rcOutPutParamExpectedValue.setDateValue(date);
                            rcOutPutParamExpectedValue.setTestCase(testCaseId);
                            rcOutPutParamExpectedValue.setConfig(runConfig);
                            rcOutPutParamExpectedValue.setOutputParam(testOutputParam.getId());
                            rcOutPutParamExpectedValues.add(rcOutPutParamExpectedValue);
                        }
                    }
                } else {
                    TestOutputParam outputParam = new TestOutputParam();
                    outputParam.setName(cellValueStr);
                    outputParam.setTestCase(testCaseId);
                    outputParam.setDataType(DataType.valueOf("STRING"));
                    TestOutputParam outputParam1 = outputParamRepository.save(outputParam);

                    if (outputParam1 != null) {
                        if (outputParam1.getId() != null) {
                            if (outputParam1.getDataType().equals(DataType.valueOf("STRING"))) {
                                rcOutPutParamExpectedValue.setStringValue(celToString);
                                rcOutPutParamExpectedValue.setTestCase(testCaseId);
                                rcOutPutParamExpectedValue.setConfig(runConfig);
                                rcOutPutParamExpectedValue.setOutputParam(outputParam1.getId());
                                rcOutPutParamExpectedValues.add(rcOutPutParamExpectedValue);
                            }


                        }
                    }


                }

            } else {

                throw new CassiniException("Please enter proper data");
            }
        }
        workbook.close();
        //Save all added rcOutputParamValues
        for (RCOutPutParamExpectedValue rcout : rcOutPutParamExpectedValues) {
            RCOutPutParamExpectedValue rcOutPutParam = rcOutputParamExpectedvalueRepository.findByConfigAndTestCaseAndOutputParam(rcout.getConfig(),
                    rcout.getTestCase(), rcout.getOutputParam());
            if (rcOutPutParam != null) { //update
                rcOutPutParam.setIntegerValue(rcout.getIntegerValue());
                rcOutPutParam.setStringValue(rcout.getStringValue());
                rcOutPutParam.setDateValue(rcout.getDateValue());
                rcOutPutParam.setDoubleValue(rcout.getDoubleValue());
                rcOutPutParam.setTimeValue(rcout.getTimeValue());
                rcOutputParamExpectedvalueRepository.save(rcOutPutParam);
            } else { //save
                rcOutputParamExpectedvalueRepository.save(rcout);
            }
        }

    }

    /* ----- Throw exception date format is incorrect -----*/
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

    /*------ importOutPutParamExpectedValueXLSFormat -----*/
    private void importOutPutParamExpectedValueXLSFormat(Integer testCaseId, Integer runConfig,
                                                         MultipartHttpServletRequest request, MultipartFile file) throws IOException, ParseException, Exception {
        List<RCOutPutParamExpectedValue> rcOutPutParamExpectedValues = new ArrayList<>();
        int i = 0;
        HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
        HSSFSheet worksheet = workbook.getSheetAt(0);
        FormulaEvaluator objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
        FormulaEvaluator objFormulaEvaluator1 = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
        DataFormatter objDefaultFormat = new DataFormatter();
        DataFormatter objDefaultFormat1 = new DataFormatter();
        // Reads the data in excel file until last row is encountered
        while (i <= worksheet.getLastRowNum()) {

            // Creates an object for the IMItemType Model
            RCOutPutParamExpectedValue rcOutPutParamExpectedValue = new RCOutPutParamExpectedValue();
            // Creates an object representing a single row in excel
            HSSFRow row = worksheet.getRow(i++);
            // Sets the Read data to the model class
            if (row.getRowNum() == 0) continue;
            HSSFCell cell1 = row.getCell(0);
            objFormulaEvaluator.evaluate(cell1);
            String cellValueStr = objDefaultFormat.formatCellValue(cell1, objFormulaEvaluator);
            HSSFCell cell2 = row.getCell(1);
            objFormulaEvaluator1.evaluate(cell2);
            String celToString = objDefaultFormat1.formatCellValue(cell2, objFormulaEvaluator1);
            if ((cellValueStr != null || cellValueStr != "") && (celToString != null || celToString != "")) {
                TestOutputParam testOutputParam = getNameOutPutParam(cellValueStr, testCaseId);
                if (testOutputParam != null) {
                    if (testOutputParam.getId() != null) {
                        if (testOutputParam.getDataType().equals(DataType.valueOf("STRING"))) {
                            rcOutPutParamExpectedValue.setStringValue(celToString);
                            rcOutPutParamExpectedValue.setTestCase(testCaseId);
                            rcOutPutParamExpectedValue.setConfig(runConfig);
                            rcOutPutParamExpectedValue.setOutputParam(testOutputParam.getId());
                            rcOutPutParamExpectedValues.add(rcOutPutParamExpectedValue);
                        }
                        if (testOutputParam.getDataType().equals(DataType.valueOf("INTEGER"))) {
                            int intVal = Integer.parseInt(celToString);
                            rcOutPutParamExpectedValue.setIntegerValue(intVal);
                            rcOutPutParamExpectedValue.setTestCase(testCaseId);
                            rcOutPutParamExpectedValue.setConfig(runConfig);
                            rcOutPutParamExpectedValue.setOutputParam(testOutputParam.getId());
                            rcOutPutParamExpectedValues.add(rcOutPutParamExpectedValue);
                        }
                        if (testOutputParam.getDataType().equals(DataType.valueOf("DOUBLE"))) {
                            double doubleVal = Double.parseDouble(celToString);
                            rcOutPutParamExpectedValue.setDoubleValue(doubleVal);
                            rcOutPutParamExpectedValue.setTestCase(testCaseId);
                            rcOutPutParamExpectedValue.setConfig(runConfig);
                            rcOutPutParamExpectedValue.setOutputParam(testOutputParam.getId());
                            rcOutPutParamExpectedValues.add(rcOutPutParamExpectedValue);
                        }
                        if (testOutputParam.getDataType().equals(DataType.valueOf("DATE"))) {
                            String dateFormat = "dd/MM/yyyy";
                            Date date = simpleDateFormat(celToString, dateFormat);
                            rcOutPutParamExpectedValue.setDateValue(date);
                            rcOutPutParamExpectedValue.setTestCase(testCaseId);
                            rcOutPutParamExpectedValue.setConfig(runConfig);
                            rcOutPutParamExpectedValue.setOutputParam(testOutputParam.getId());
                            rcOutPutParamExpectedValues.add(rcOutPutParamExpectedValue);
                        }


                    }
                } else {
                    TestOutputParam outputParam = new TestOutputParam();
                    outputParam.setName(cellValueStr);
                    outputParam.setTestCase(testCaseId);
                    outputParam.setDataType(DataType.valueOf("STRING"));
                    TestOutputParam outputParam1 = outputParamRepository.save(outputParam);

                    if (outputParam1 != null) {
                        if (outputParam1.getId() != null) {
                            if (outputParam1.getDataType().equals(DataType.valueOf("STRING"))) {
                                rcOutPutParamExpectedValue.setStringValue(celToString);
                                rcOutPutParamExpectedValue.setTestCase(testCaseId);
                                rcOutPutParamExpectedValue.setConfig(runConfig);
                                rcOutPutParamExpectedValue.setOutputParam(outputParam1.getId());
                                rcOutPutParamExpectedValues.add(rcOutPutParamExpectedValue);
                            }


                        }
                    }


                }

            } else {

                throw new CassiniException("Please enter proper data");
            }

            workbook.close();
            // Save all added rcOutPutParamValues
            for (RCOutPutParamExpectedValue rcout : rcOutPutParamExpectedValues) {
                RCOutPutParamExpectedValue rcOutPutParam = rcOutputParamExpectedvalueRepository.findByConfigAndTestCaseAndOutputParam(rcout.getConfig(),
                        rcout.getTestCase(), rcout.getOutputParam());
                if (rcOutPutParam != null) { //update
                    rcOutPutParam.setIntegerValue(rcout.getIntegerValue());
                    rcOutPutParam.setStringValue(rcout.getStringValue());
                    rcOutPutParam.setDateValue(rcout.getDateValue());
                    rcOutPutParam.setDoubleValue(rcout.getDoubleValue());
                    rcOutPutParam.setTimeValue(rcout.getTimeValue());
                    rcOutputParamExpectedvalueRepository.save(rcOutPutParam);
                } else {  //create
                    rcOutputParamExpectedvalueRepository.save(rcout);
                }
            }
        }
    }

    /*------Import OutPutParamExpectedValue At RunConfiguration------*/
    @Transactional(readOnly = false)
    public void importOutPutParamExpectedValue(Integer testCaseId, Integer runConfig, MultipartHttpServletRequest request) throws Exception {
        for (MultipartFile file : request.getFileMap().values()) {
            if (file.getOriginalFilename().trim().endsWith(".xlsx")) {
                importOutPutParamExpectedValueXLSXFormat(testCaseId, runConfig, request, file);
            } else if (file.getOriginalFilename().trim().endsWith(".xls")) {
                importOutPutParamExpectedValueXLSFormat(testCaseId, runConfig, request, file);
            } else {
                throw new IllegalArgumentException("Please upload excel sheet ");
            }
        }
    }

    /* ------ Get testInputParam based on name and testCaseId -------*/
    public TestInputParam getName(String name, Integer testCaseId) {
        TestInputParam testInputParam = inputParamRepository.findByNameAndTestCase(name, testCaseId);
        return testInputParam;
    }

    /* ------ Get testOutPutParam based on name and testCaseId -------*/
    public TestOutputParam getNameOutPutParam(String name, Integer testCaseId) {
        TestOutputParam testOutputParam = outputParamRepository.findByNameAndTestCase(name, testCaseId);
        return testOutputParam;
    }

    /*------ Get TestRun History At RunConfiguration Module -----*/
    @Transactional(readOnly = false)
    public List<TestRun> getRunHistory(Integer scenarioId) {
        List<TestRun> run = new ArrayList<>();
        TestScenario scenario = scenarioRepository.findOne(scenarioId);
        if (scenario != null) {
            List<TestRunConfiguration> testRunConfiguration = runConfigurationRepository.findByScenario(scenario);
            if (testRunConfiguration != null) {
                for (TestRunConfiguration runConfiguration : testRunConfiguration) {
                    List<TestRun> run1 = runRepository.findByTestRunConfiguration(runConfiguration);
                    run.addAll(run1);
                }
            }
        }

        return run;
    }


}
