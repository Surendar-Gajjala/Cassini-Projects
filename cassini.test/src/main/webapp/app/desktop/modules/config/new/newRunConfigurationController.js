define(
    [
        'app/desktop/modules/config/config.module',
        'app/shared/services/testTreeService',
        'app/shared/services/runConfigurationService'
    ],
    function (module) {
        module.controller('NewRunConfigurationController', NewRunConfigurationController);

        function NewRunConfigurationController($scope, $rootScope, $timeout, $state, $cookies, TestTreeService, RunConfigurationService) {

            var vm = this;
            if ($application.homeLoaded == false) {
                return;
            }

            vm.newRunConfig = {
                id: null,
                name: null,
                description: null,
                scenario: null
            };

            /* ----- Load Scenarios  -----*/
            function loadAllScenarios() {
                TestTreeService.getAllScenarios().then(
                    function (data) {
                        vm.scenarios = data;
                    }
                )
            }

            /*------- Save Run Configuration -------*/
            function createRunConfig() {
                if (validate()) {
                    RunConfigurationService.createRunConfiguration(vm.newRunConfig).then(
                        function (data) {
                            vm.runConfig = data;
                            $rootScope.showSuccessMessage("Run Configuration created successfully");
                            $scope.callback(vm.runConfig);
                            $rootScope.hideSidePanel("left");
                            vm.newRunConfig = {
                                id: null,
                                name: null,
                                description: null,
                                scenario: null
                            };
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            /* ------ Validation For Run Configuration ------*/
            function validate() {
                var valid = true;
                if (vm.newRunConfig.name == null || vm.newRunConfig.name == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter name");
                } else if (vm.newRunConfig.description == null || vm.newRunConfig.description == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter description");
                } else if (vm.newRunConfig.scenario == null || vm.newRunConfig.scenario == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Scenario");
                }

                return valid;
            }


            (function () {
                loadAllScenarios();
                $scope.$on('app.runConfiguration.new', createRunConfig);
            })();

        }
    }
);