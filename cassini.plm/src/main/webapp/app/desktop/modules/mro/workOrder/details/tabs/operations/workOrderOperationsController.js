define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/workOrderService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'
    ],
    function (module) {
        module.controller('WorkOrderOperationsController', WorkOrderOperationsController);

        function WorkOrderOperationsController($scope, $rootScope, $sce, $timeout, $state, $stateParams, DialogService, $translate,
                                               WorkOrderService) {
            var vm = this;
            vm.workOrderId = $stateParams.workOrderId;
            var parsed = angular.element("<div></div>");
            vm.editOperation = editOperation;
            vm.cancelChanges = cancelChanges;
            vm.updateOperation = updateOperation;

            vm.operationResults = ['NONE', 'PASS', 'FAIL'];
            vm.booleanValues = [{label: 'TRUE', value: true}, {label: "FALSE", value: false}];

            var valueValidation = parsed.html($translate.instant("VALUE_VALIDATION")).html();
            var pleaseSelectValue = parsed.html($translate.instant("P_SELECT_VALUE")).html();
            var operationUpdated = parsed.html($translate.instant("OPERATION_UPDATED")).html();
            $scope.select = parsed.html($translate.instant("SELECT")).html();

            function loadWorkOrderOperations() {
                WorkOrderService.getWorkOrderOperations(vm.workOrderId).then(
                    function (data) {
                        vm.workOrderOperations = data;
                        angular.forEach(vm.workOrderOperations, function (operation) {
                            operation.editMode = false;
                        })
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function validateOperation(operation) {
                var valid = true;
                if (operation.paramType == "TEXT" && (operation.result == "PASS" || operation.result == "FAIL") &&
                    (operation.textValue == null || operation.textValue == "" || operation.textValue == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(valueValidation);
                } else if (operation.paramType == "INTEGER" && (operation.result == "PASS" || operation.result == "FAIL") &&
                    (operation.integerValue == null || operation.integerValue == "" || operation.integerValue == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(valueValidation);
                } else if (operation.paramType == "DECIMAL" && (operation.result == "PASS" || operation.result == "FAIL") &&
                    (operation.decimalValue == null || operation.decimalValue == "" || operation.decimalValue == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(valueValidation);
                } else if (operation.paramType == "LIST" && (operation.result == "PASS" || operation.result == "FAIL") &&
                    (operation.listValue == null || operation.listValue == "" || operation.listValue == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseSelectValue);
                }
                return valid;
            }

            function updateOperation(operation) {
                if (validateOperation(operation)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    WorkOrderService.updateWorkOrderOperation(vm.workOrderId, operation).then(
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

            function editOperation(operation) {
                operation.oldTextValue = operation.textValue;
                operation.oldIntegerValue = operation.integerValue;
                operation.oldDecimalValue = operation.decimalValue;
                operation.oldListValue = operation.listValue;
                operation.oldBooleanValue = operation.booleanValue;
                operation.oldResult = operation.result;
                operation.oldNotes = operation.notes;
                operation.editMode = true;
            }

            function cancelChanges(operation) {
                operation.textValue = operation.oldTextValue;
                operation.integerValue = operation.oldIntegerValue;
                operation.decimalValue = operation.oldDecimalValue;
                operation.listValue = operation.oldListValue;
                operation.notes = operation.oldNotes;
                operation.result = operation.oldResult;
                operation.booleanValue = operation.oldBooleanValue;
                operation.editMode = false;
            }

            (function () {
                $scope.$on('app.workOrder.tabActivated', function (event, data) {
                    if (data.tabId == 'details.operations') {
                        loadWorkOrderOperations();
                    }
                })
            })();
        }
    }
);
