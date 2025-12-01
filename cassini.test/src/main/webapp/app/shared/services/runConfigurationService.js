define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('RunConfigurationService', RunConfigurationService);

        function RunConfigurationService($q, httpFactory) {
            return {
                getRCConfigScenarios: getRCConfigScenarios,
                getRunConfigTree: getRunConfigTree,

                createRunConfiguration: createRunConfiguration,
                updateRunConfiguration: updateRunConfiguration,
                createTestRunSchedule: createTestRunSchedule,
                updateTestRunSchedule: updateTestRunSchedule,
                getRunScheduleByRunConfig: getRunScheduleByRunConfig,
                deleteRunConfig: deleteRunConfig,
                getTestCaseHistory: getTestCaseHistory,
                getConfigurationRuns: getConfigurationRuns
            };

            function getRCConfigScenarios() {
                var url = "api/test/runConfigurations/rcConfigScenarios";
                return httpFactory.get(url);
            }

            function getRunConfigTree() {
                var url = "api/test/runConfigurations/runConfigTree";
                return httpFactory.get(url);
            }


            function createRunConfiguration(runConfiguration) {
                var url = "api/test/runConfigurations";
                return httpFactory.post(url, runConfiguration);
            }

            function updateRunConfiguration(runConfiguration) {
                var url = "api/test/runConfigurations/" + runConfiguration.id;
                return httpFactory.put(url, runConfiguration);
            }

            function createTestRunSchedule(runSchedule) {
                var url = "api/test/runConfigurations/runSchedule";
                return httpFactory.post(url, runSchedule);
            }

            function updateTestRunSchedule(runSchedule) {
                var url = "api/test/runConfigurations/runSchedule/" + runSchedule.runConfig.id;
                return httpFactory.put(url, runSchedule);
            }

            function getRunScheduleByRunConfig(runConfigId) {
                var url = "api/test/runConfigurations/runScheduleByRunConfig/" + runConfigId;
                return httpFactory.get(url, runConfigId);
            }

            function deleteRunConfig(id) {
                var url = "api/test/runConfigurations/" + id;
                return httpFactory.delete(url);
            }

            function getTestCaseHistory(id) {
                var url = "api/test/runConfigurations/testCaseHistory/" + id;
                return httpFactory.get(url);
            }

            function getConfigurationRuns(id) {
                var url = "api/test/runConfigurations/getConfigurationRuns/" + id;
                return httpFactory.get(url);
            }
        }
    }
);