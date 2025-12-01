define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('TestRunService', TestRunService);

        function TestRunService(httpFactory) {
            return {
                getTestRuns: getTestRuns,
                getTestTreeBasedOnTestRunId: getTestTreeBasedOnTestRunId,
                getTestCaseDetailsBasedOnTestCaseId: getTestCaseDetailsBasedOnTestCaseId,
                importTestRuns: importTestRuns,
                getTestCaseResultsBasedOnRunCaseId: getTestCaseResultsBasedOnRunCaseId,
                getRunCaseFiles: getRunCaseFiles,
                deleteFile: deleteFile,
                getRunCaseDetails: getRunCaseDetails,
                getRunScenario: getRunScenario,
                getRunPlan: getRunPlan,
                getRunSuite: getRunSuite,
                deleteTestRun: deleteTestRun,
                getTestRunCaseFiles: getTestRunCaseFiles
            };


            function importTestRuns(files) {
                var url = "api/test/run/importInputParams";
                return httpFactory.upload(url, files)
            }

            function getTestRuns(pageable) {
                var url = "api/test/run/allTestRuns/pageable?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getTestTreeBasedOnTestRunId(testRunId) {

                var url = "api/test/run/testRunTree/" + testRunId;
                return httpFactory.get(url);
            }

            function getTestCaseResultsBasedOnRunCaseId(testCaseId) {
                var url = "api/test/case/" + testCaseId;
                return httpFactory.get(url);
            }

            function getTestCaseDetailsBasedOnTestCaseId(testCaseId) {
                var url = "api/test/run/testCase/" + testCaseId;
                return httpFactory.get(url);
            }

            function getRunCaseDetails(runCaseId) {
                var url = "api/test/run/runCase/" + runCaseId;
                return httpFactory.get(url);
            }

            function getRunScenario(runScenarioId) {
                var url = "api/test/run/runScenario/" + runScenarioId;
                return httpFactory.get(url);
            }

            function getRunPlan(runPlanId) {
                var url = "api/test/run/runPlan/" + runPlanId;
                return httpFactory.get(url);
            }

            function getRunSuite(runSuiteId) {
                var url = "api/test/run/runSuite/" + runSuiteId;
                return httpFactory.get(url);
            }

            function getRunCaseFiles(testRunId) {
                var url = "api/test/run/" + testRunId + "/files/runFiles";
                return httpFactory.get(url);
            }

            function deleteTestRun(testRunId) {
                var url = "api/test/run/deleteTestRun/" + testRunId;
                return httpFactory.delete(url);
            }


            function deleteFile(testRunId, fileId) {
                var url = "api/test/run/case/" + testRunId + "/files/deleteFile/" + fileId;
                return httpFactory.delete(url);
            }

            function getTestRunCaseFiles(id) {
                var url = "api/test/run/case/" + id + "/files/testRunCaseFiles";
                return httpFactory.get(url);
            }
        }
    }
);