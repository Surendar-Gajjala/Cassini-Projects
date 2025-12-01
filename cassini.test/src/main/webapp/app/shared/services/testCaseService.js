define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('TestCaseService', TestCaseService);

        function TestCaseService($q, httpFactory) {
            return {
                createTestInputParam: createTestInputParam,
                updateTestInputParam: updateTestInputParam,
                deleteTestInputParam: deleteTestInputParam,
                createTestOutputParam: createTestOutputParam,
                updateTestOutputParam: updateTestOutputParam,
                deleteTestOutputParam: deleteTestOutputParam,
                createRCInputParamValue: createRCInputParamValue,
                updateRCInputParamValue: updateRCInputParamValue,
                deleteRCInputParamValue: deleteRCInputParamValue,
                createExpectedOutputValue: createExpectedOutputValue,
                updateExpectedOutputValue: updateExpectedOutputValue,
                deleteExpectedOutputValue: deleteExpectedOutputValue,
                getInputOutputValuesByConfigAndTestCase: getInputOutputValuesByConfigAndTestCase,
                getInputAndOutputParamsByTestCase: getInputAndOutputParamsByTestCase,
                importInputParams: importInputParams,
                importOutputParams: importOutputParams,
                importInputParamsValues: importInputParamsValues,
                importOutPutParamsValues: importOutPutParamsValues,
                getTestRunCaseHistory: getTestRunCaseHistory,
                getTestRunCaseFiles: getTestRunCaseFiles,
                deleteRunCaseFile: deleteRunCaseFile

            };


            function importInputParams(files, testCaseId) {
                var url = "api/test/case/importInputParams/" + testCaseId;
                return httpFactory.upload(url, files)
            }

            function importOutputParams(files, testCaseId) {
                var url = "api/test/case/importOutputParams/" + testCaseId;
                return httpFactory.upload(url, files)
            }


            function importInputParamsValues(files, testCaseId, runConfig) {
                var url = "api/test/case/importInputParamValues/" + testCaseId + "/" + runConfig;
                return httpFactory.upload(url, files)
            }


            function importOutPutParamsValues(files, testCaseId, runConfig) {
                var url = "api/test/case/importOutPutParamValues/" + testCaseId + "/" + runConfig;
                return httpFactory.upload(url, files)
            }


            function createTestInputParam(inputParam) {
                var url = "api/test/case/inputParam";
                return httpFactory.post(url, inputParam);
            }

            function updateTestInputParam(inputParam) {
                var url = "api/test/case/inputParam/" + inputParam.id;
                return httpFactory.put(url, inputParam);
            }

            function deleteTestInputParam(inputParam) {
                var url = "api/test/case/inputParam/" + inputParam.id;
                return httpFactory.delete(url);
            }

            function createTestOutputParam(outputParam) {
                var url = "api/test/case/outputParam";
                return httpFactory.post(url, outputParam);
            }

            function updateTestOutputParam(outputParam) {
                var url = "api/test/case/outputParam/" + outputParam.id;
                return httpFactory.put(url, outputParam);
            }

            function deleteTestOutputParam(outputParam) {
                var url = "api/test/case/outputParam/" + outputParam.id;
                return httpFactory.delete(url);
            }

            function createRCInputParamValue(inputParam) {
                var url = "api/test/case/inputParamValue";
                return httpFactory.post(url, inputParam);
            }

            function updateRCInputParamValue(inputParam) {
                var url = "api/test/case/inputParamValue/" + inputParam.id;
                return httpFactory.put(url, inputParam);
            }

            function deleteRCInputParamValue(inputParam) {
                var url = "api/test/case/inputParamValue/" + inputParam.id;
                return httpFactory.delete(url);
            }

            function createExpectedOutputValue(expectedOutputValue) {
                var url = "api/test/case/expectedOutputValue";
                return httpFactory.post(url, expectedOutputValue);
            }

            function updateExpectedOutputValue(expectedOutputValue) {
                var url = "api/test/case/expectedOutputValue/" + expectedOutputValue.id;
                return httpFactory.put(url, expectedOutputValue);
            }

            function deleteExpectedOutputValue(expectedOutputValue) {
                var url = "api/test/case/expectedOutputValue/" + expectedOutputValue.id;
                return httpFactory.delete(url);
            }

            function getInputOutputValuesByConfigAndTestCase(testCase) {
                var url = "api/test/case/config/" + testCase.runConfiguration + "/testCase/" + testCase.id;
                return httpFactory.get(url);
            }

            function getInputAndOutputParamsByTestCase(testCase) {
                var url = "api/test/case/testCase/" + testCase.id;
                return httpFactory.get(url);
            }

            function getTestRunCaseHistory(id) {
                var url = "api/test/case/testRunCaseHistory/" + id;
                return httpFactory.get(url);
            }

            function getTestRunCaseFiles(id) {
                var url = "api/test/case/testRunCaseFiles/" + id;
                return httpFactory.get(url);
            }

            function deleteRunCaseFile(runCaseId, fileId) {
                var url = "api/test/run/" + runCaseId + "/files/deleteFile/" + fileId;
                return httpFactory.delete(url);
            }
        }
    }
);