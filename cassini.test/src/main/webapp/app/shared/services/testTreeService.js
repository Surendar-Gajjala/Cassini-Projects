define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('TestTreeService', TestTreeService);

        function TestTreeService($q, httpFactory) {
            return {
                getTestTree: getTestTree,
                getAllScenarios: getAllScenarios,

                createScenario: createScenario,
                updateScenario: updateScenario,
                getScenario: getScenario,

                updateTestPlan: updateTestPlan,
                createTestPlan: createTestPlan,
                getTestPlan: getTestPlan,

                createTestSuite: createTestSuite,
                updateTestSuite: updateTestSuite,
                getTestSuite: getTestSuite,

                createTestCase: createTestCase,
                updateTestCase: updateTestCase,
                getTestCase: getTestCase,

                deleteScenario: deleteScenario,
                deletePlan: deletePlan,
                deleteSuite: deleteSuite,
                deleteTestCase: deleteTestCase,
                getTestCasesByScenarios: getTestCasesByScenarios,
                importScenario: importScenario,
                getPlanByScenario: getPlanByScenario,
                getSuiteByPlan: getSuiteByPlan,
                getCaseBySuite: getCaseBySuite
            };

            function getTestCasesByScenarios(scenarioId) {
                var url = "api/test/scenario/testCasesByScenarioId" + scenarioId;
                return httpFactory.get(url);
            }

            function getTestTree() {
                var url = "api/test/scenario/tree";
                return httpFactory.get(url);
            }

            function getAllScenarios() {
                var url = "api/test/scenario/allScenarios";
                return httpFactory.get(url);
            }

            function createScenario(scenario) {
                var url = "api/test/scenario";
                return httpFactory.post(url, scenario);
            }

            function updateScenario(scenario) {
                var url = "api/test/scenario/" + scenario.id;
                return httpFactory.put(url, scenario);
            }

            function getScenario(scenario) {
                var url = "api/test/scenario/" + scenario.id;
                return httpFactory.get(url);
            }

            function createTestPlan(plan) {
                var url = "api/test/plan";
                return httpFactory.post(url, plan);
            }

            function updateTestPlan(plan) {
                var url = "api/test/plan/" + plan.id;
                return httpFactory.put(url, plan);
            }

            function getTestPlan(plan) {
                var url = "api/test/plan/" + plan.id;
                return httpFactory.get(url);
            }

            function createTestSuite(suite) {
                var url = "api/test/suites";
                return httpFactory.post(url, suite);
            }

            function updateTestSuite(suite) {
                var url = "api/test/suites/" + suite.id;
                return httpFactory.put(url, suite);
            }

            function getTestSuite(suite) {
                var url = "api/test/suites/" + suite.id;
                return httpFactory.get(url);
            }

            function createTestCase(testCase) {
                var url = "api/test/case";
                return httpFactory.post(url, testCase);
            }

            function updateTestCase(testCase) {
                var url = "api/test/case/" + testCase.id;
                return httpFactory.put(url, testCase);
            }

            function getTestCase(testCase) {
                var url = "api/test/case/" + testCase.id;
                return httpFactory.get(url);
            }

            function deleteScenario(id) {
                var url = "api/test/scenario/" + id;
                return httpFactory.delete(url);
            }

            function deletePlan(id) {
                var url = "api/test/plan/" + id;
                return httpFactory.delete(url);
            }

            function deleteSuite(id) {
                var url = "api/test/suites/" + id;
                return httpFactory.delete(url);
            }

            function deleteTestCase(id) {
                var url = "api/test/case/" + id;
                return httpFactory.delete(url);
            }

            function importScenario(files) {
                var url = "api/test/scenario/importScenario";
                return httpFactory.upload(url, files);
            }

            function getPlanByScenario(id) {
                var url = "api/test/scenario/planByScenario/" + id;
                return httpFactory.get(url);
            }

            function getSuiteByPlan(id) {
                var url = "api/test/scenario/suiteByPlan/" + id;
                return httpFactory.get(url);
            }

            function getCaseBySuite(id) {
                var url = "api/test/scenario/caseBySuite/" + id;
                return httpFactory.get(url);
            }
        }
    }
);