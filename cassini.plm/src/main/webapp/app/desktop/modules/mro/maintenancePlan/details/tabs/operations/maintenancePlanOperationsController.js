define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/maintenancePlanService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('MaintenancePlanOperationsController', MaintenancePlanOperationsController);

        function MaintenancePlanOperationsController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, DialogService,
                                                     MaintenancePlanService, LovService) {
            var vm = this;
            vm.maintenancePlanId = $stateParams.maintenancePlanId;

            vm.addOperation = addOperation;
            vm.saveOperation = saveOperation;
            vm.saveOperations = saveOperations;
            vm.removeOperation = removeOperation;
            vm.deleteOperation = deleteOperation;
            vm.editOperation = editOperation;
            vm.cancelChanges = cancelChanges;
            vm.updateOperation = updateOperation;
            vm.removeAllOperations = removeAllOperations;

            var parsed = angular.element("<div></div>");
            var operationDeleted = parsed.html($translate.instant("OPERATION_DELETE_MSG")).html();
            var operationUpdated = parsed.html($translate.instant("OPERATION_UPDATED")).html();
            var deleteOperationTitle = parsed.html($translate.instant("DELETE_OPERATION")).html();
            var deleteOperationDialogMsg = parsed.html($translate.instant("DELETE_OPERATION_DIALOG_MSG")).html();
            var pleaseSelectLov = parsed.html($translate.instant("PLEASE_SELECT_LOV")).html();
            var pleaseSelectParamType = parsed.html($translate.instant("PLEASE_SELECT_PARAM_TYPE")).html();
            var pleaseEnterParamName = parsed.html($translate.instant("PLEASE_ENTER_PARAM_NAME")).html();
            var pleaseEnterName = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var operationCreated = parsed.html($translate.instant("OPERATION_ADDED_SUCCESS")).html();

            vm.selectedOperations = [];


            var emptyOperation = {
                id: null,
                maintenancePlan: vm.maintenancePlanId,
                name: null,
                description: null,
                paramName: null,
                paramType: 'NONE',
                lov: null
            }

            vm.paramTypes = ['NONE', 'TEXT', 'INTEGER', 'DECIMAL', 'BOOLEAN', 'LIST'];
            vm.addedOperations = [];
            function addOperation() {
                vm.newOperation = angular.copy(emptyOperation);
                vm.newOperation.editMode = true;
                vm.newOperation.isNew = true;
                vm.maintenancePlanOperations.unshift(vm.newOperation);
                vm.addedOperations.unshift(vm.newOperation);
            }

            function loadMaintenancePlanOperations() {
                vm.addedOperations = [];
                MaintenancePlanService.getMaintenancePlanOperations(vm.maintenancePlanId).then(
                    function (data) {
                        vm.maintenancePlanOperations = data;
                        angular.forEach(vm.maintenancePlanOperations, function (operation) {
                            operation.editMode = false;
                            operation.isNew = false;
                        })
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function saveOperation(operation) {
                if (validateOperation(operation)) {
                    MaintenancePlanService.createMaintenancePlanOperation(vm.maintenancePlanId, operation).then(
                        function (data) {
                            operation.id = data.id;
                            operation.editMode = false;
                            operation.isNew = false;
                            vm.addedOperations.splice(vm.addedOperations.indexOf(operation), 1);
                            if (vm.addedOperations.length == 0) {
                                loadMaintenancePlanOperations();
                            }
                            $rootScope.loadPlanTabCounts();
                            $rootScope.showSuccessMessage(operationCreated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function saveOperations() {
                if (validateOperations()) {
                    MaintenancePlanService.createMultipleMaintenancePlanOperations(vm.maintenancePlanId, vm.addedOperations).then(
                        function (data) {
                            vm.addedOperations = [];
                            loadMaintenancePlanOperations();
                            $rootScope.loadPlanTabCounts();
                            $rootScope.showSuccessMessage(operationCreated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validateOperation(operation) {
                var valid = true;
                if (operation.name == null || operation.name == "" || operation.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterName);
                } else if (operation.paramName == null || operation.paramName == "" || operation.paramName == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterParamName);
                } else if (operation.paramType == null || operation.paramType == "" || operation.paramType == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseSelectParamType);
                } else if (operation.paramType == 'LIST' && (operation.lov == null || operation.lov == "" || operation.lov == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseSelectLov);
                }
                return valid;
            }

            function validateOperations() {
                var valid = true;
                angular.forEach(vm.addedOperations, function (operation) {
                    if (valid) {
                        if (operation.name == null || operation.name == "" || operation.name == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage(pleaseEnterName);
                        } else if (operation.paramName == null || operation.paramName == "" || operation.paramName == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage(pleaseEnterParamName);
                        } else if (operation.paramType == null || operation.paramType == "" || operation.paramType == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage(pleaseSelectParamType);
                        } else if (operation.paramType == 'LIST' && (operation.lov == null || operation.lov == "" || operation.lov == undefined)) {
                            valid = false;
                            $rootScope.showWarningMessage(pleaseSelectLov);
                        }
                    }
                })
                return valid;
            }

            function updateOperation(operation) {
                if (validateOperation(operation)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    MaintenancePlanService.updateMaintenancePlanOperation(vm.maintenancePlanId, operation).then(
                        function (data) {
                            operation.editMode = false;
                            $rootScope.showSuccessMessage(operationUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function removeOperation(operation) {
                vm.maintenancePlanOperations.splice(vm.maintenancePlanOperations.indexOf(operation), 1);
                vm.addedOperations.splice(vm.addedOperations.indexOf(operation), 1);
            }

            function removeAllOperations() {
                angular.forEach(vm.addedOperations, function (operation) {
                    vm.maintenancePlanOperations.splice(vm.maintenancePlanOperations.indexOf(operation), 1);
                })
                vm.addedOperations = [];
            }

            function deleteOperation(operation) {
                var options = {
                    title: deleteOperationTitle,
                    message: deleteOperationDialogMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            MaintenancePlanService.deleteMaintenancePlanOperation(vm.maintenancePlanId, operation.id).then(
                                function (data) {
                                    if (vm.addedOperations.length == 0) {
                                        loadMaintenancePlanOperations();
                                    } else {
                                        vm.maintenancePlanOperations.splice(vm.maintenancePlanOperations.indexOf(operation), 1);
                                    }
                                    $rootScope.loadPlanTabCounts();
                                    $rootScope.showSuccessMessage(operationDeleted);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                )
            }

            function editOperation(operation) {
                operation.oldName = operation.name;
                operation.oldDescription = operation.description;
                operation.oldParamType = operation.paramType;
                operation.oldParamName = operation.paramName;
                operation.oldLov = operation.lov;
                operation.editMode = true;
            }

            function cancelChanges(operation) {
                operation.name = operation.oldName;
                operation.description = operation.oldDescription;
                operation.paramType = operation.oldParamType;
                operation.paramName = operation.oldParamName;
                operation.lov = operation.oldLov;
                operation.editMode = false;
            }

            vm.lovs = [];
            function loadLovs() {
                LovService.getAllLovs().then(
                    function (data) {
                        vm.lovs = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            (function () {
                $scope.$on('app.maintenancePlan.tabActivated', function (event, data) {
                    if (data.tabId == 'details.operations') {
                        loadMaintenancePlanOperations();
                        loadLovs();
                    }
                })
            })();
        }
    }
);
