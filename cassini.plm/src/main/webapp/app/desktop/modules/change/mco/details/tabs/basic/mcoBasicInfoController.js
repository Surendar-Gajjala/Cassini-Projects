define(
    [
        'app/desktop/modules/change/change.module',
        'moment',
        'moment-timezone-with-data',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/shared/services/core/qcrService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/workflowService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MCOBasicInfoController', MCOBasicInfoController);

        function MCOBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, $application, ECOService, CommonService,
                                        MCOService, $translate, QcrService, WorkflowService, QualityTypeService) {
            var vm = this;

            vm.loading = true;
            vm.mcoId = $stateParams.mcoId;
            vm.mco = null;
            var parsed = angular.element("<div></div>");
            var titleValidation = parsed.html($translate.instant("TITLE_VALIDATION")).html();
            var mcoUpdated = parsed.html($translate.instant("MCO_UPDATED")).html();


            /*-------------------  To get ECO Basic details  -----------------------*/
            $rootScope.loadBasicMCO = loadBasicMCO;
            function loadBasicMCO() {
                vm.loading = true;
                MCOService.getMCO(vm.mcoId).then(
                    function (data) {
                        var promise = null;
                        if (data.objectType == "ITEMMCO") {
                            promise = MCOService.getItemMco(vm.mcoId);
                        } else {
                            promise = MCOService.getMaterialMco(vm.mcoId);
                        }
                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    vm.mco = data;
                                    $rootScope.mco = data;
                                    vm.copiedMco = angular.copy(vm.mco);
                                    // $rootScope.viewInfo.title = "MCO Details";
                                    // $rootScope.viewInfo.description = "Number: {0}, Status: {1}".
                                    //     format(vm.mco.mcoNumber, vm.mco.status);
                                    $rootScope.viewInfo.title = "<div class='item-number'>" +
                                    "{0}</div>".
                                        format(vm.mco.mcoNumber);
                                   $rootScope.viewInfo.description = vm.mco.title;
                                    CommonService.getMultiplePersonReferences([vm.mco], ['changeAnalyst', 'createdBy', 'modifiedBy']);
                                    vm.loading = false;
                                    loadQcr();
                                    loadWorkflow();
                                    $timeout(function () {
                                        $scope.$broadcast('app.attributes.tabActivated', {});
                                    }, 1000);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.mcoWorkflowStarted = false;
            function loadWorkflow() {
                WorkflowService.getWorkflow($rootScope.mco.workflow).then(
                    function (data) {
                        vm.workflow = data;
                        if (vm.workflow.started) {
                            $rootScope.mcoWorkflowStarted = true;
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadQcr() {
                if (vm.mco.qcr != null && vm.mco.qcr != "") {
                    QcrService.getQcr(vm.mco.qcr).then(
                        function (data) {
                            vm.qcr = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.updateMco = updateMco;
            function updateMco() {
                if (validate()) {
                    $rootScope.showBusyIndicator($(".view-container"));
                    var promise = null;
                    vm.mco.changeAnalyst = vm.mco.changeAnalystObject.id;
                    if (vm.mco.objectType == "ITEMMCO") {
                        promise = MCOService.updateItemMco(vm.mcoId, vm.mco);
                    } else {
                        promise = MCOService.updateMaterialMco(vm.mcoId, vm.mco);
                    }
                    if (promise != null) {
                        promise.then(
                            function (data) {
                                loadBasicMCO();
                                $rootScope.showSuccessMessage(mcoUpdated);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function validate() {
                var valid = true;
                if (vm.mco.title == "" || vm.mco.title == null || vm.mco.title == undefined) {
                    valid = false;
                    vm.mco.title = vm.copiedMco.title;
                    $rootScope.showWarningMessage(titleValidation);
                }

                return valid;
            }

            function loadPersons() {
                vm.changeAnalysts = [];
                var preference = $application.defaultValuesPreferences.get("DEFAULT_CHANGE_ANALYST_ROLE");
                if (preference != null && preference.defaultValueName != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.change.mco.all";
                    QualityTypeService.getPersonByGroupNameAndPermission(groupName, permission).then(
                        function (data) {
                            vm.changeAnalysts = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadBasicMCO();
                loadPersons();
                //}
            })();
        }
    }
);