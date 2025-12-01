define(
    [
        'app/desktop/modules/item/item.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/inspectionPlanService'
    ],
    function (module) {

        module.controller('PlanChecklistParamsController', PlanChecklistParamsController);

        function PlanChecklistParamsController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce,
                                               InspectionPlanService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            var paramsDeletedMessage = parsed.html($translate.instant("PARAMETER_DELETED_MESSAGE")).html();
            var parameterSaved = parsed.html($translate.instant("PARAMETER_SAVED")).html();
            var nameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var unitsValidation = parsed.html($translate.instant("P_ENTER_UNITS")).html();
            var selectExpectedValueType = parsed.html($translate.instant("SELECT_EXPECTED_TYPE")).html();
            var enterExpectedValue = parsed.html($translate.instant("ENTER_EXPECTED_VALUE")).html();
            var enterPassCriteria = parsed.html($translate.instant("ENTER_PASS_CRITERIA")).html();

            vm.checklist = $scope.data.checklistDetails;
            vm.planId = $stateParams.planId;

            vm.dataTypes = [
                'TEXT',
                'INTEGER',
                'DOUBLE',
                'DATE',
                'BOOLEAN'
            ];

            vm.criteria1 = ['=', '!='];
            vm.criteria2 = ['=', '!=', '<', '>'];
            vm.booleanValues = [true, false];

            var emptyParam = {
                id: null,
                inspectionPlan: vm.planId,
                checklist: vm.checklist.id,
                name: null,
                description: null,
                expectedValueType: null,
                expectedValue: {
                    id: null,
                    textValue: null,
                    integerValue: null,
                    doubleValue: null,
                    dateValue: null,
                    timeValue: null,
                    timestampValue: null,
                    refValue: null,
                    booleanValue: null,
                    listValue: null,
                    mlistValue: [],
                    imageValue: null,
                    currencyValue: null
                },
                passCriteria: null,
            }

            vm.checklistParameters = [];
            function loadChecklistParams() {
                vm.loading = true;
                InspectionPlanService.getChecklistParams(vm.planId, vm.checklist.id).then(
                    function (data) {
                        vm.checklistParameters = data;
                        $timeout(function () {
                            adjustHeight();
                        }, 500)
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.newParameters = [];
            vm.addParameter = addParameter;
            function addParameter() {
                var newRow = angular.copy(emptyParam);
                newRow.editMode = true;
                newRow.isNew = true;
                vm.newParameters.push(newRow);
                vm.checklistParameters.push(newRow);
            }

            vm.onCancel = onCancel;
            function onCancel(item) {
                if (item.isNew) {
                    vm.checklistParameters.splice(vm.checklistParameters.indexOf(item), 1);
                    vm.newParameters.splice(vm.newParameters.indexOf(item), 1);
                } else {
                    item.editMode = false;
                    item.isNew = false;
                    item.name = item.newName;
                    item.description = item.newDescription;
                    item.expectedValueType = item.newExpectedValueType;
                    item.expectedValue = item.newExpectedValue;
                    item.passCriteria = item.newPassCriteria;
                }
            }

            vm.onOk = onOk;
            function onOk(params) {
                if (validate(params)) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    if (params.id == null || params.id == undefined || params.id == "") {
                        InspectionPlanService.createPlanChecklistParams(vm.planId, vm.checklist.id, params).then(
                            function (data) {
                                params.id = data.id;
                                params.seq = data.seq;
                                params.editMode = false;
                                params.isNew = false;
                                vm.checklist.paramsCount++;
                                vm.newParameters.splice(vm.newParameters.indexOf(params), 1);
                                $rootScope.showSuccessMessage(parameterSaved);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        InspectionPlanService.updatePlanChecklistParams(vm.planId, vm.checklist.id, params).then(
                            function (data) {
                                params.id = data.id;
                                params.seq = data.seq;
                                params.editMode = false;
                                params.isNew = false;
                                $rootScope.showSuccessMessage(parameterSaved);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function validate(param) {
                var valid = true;
                if (param.name == null || param.name == "" || param.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                } else if (param.expectedValueType == null || param.expectedValueType == "" || param.expectedValueType == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectExpectedValueType);
                } else if (param.units == null || param.units == "" || param.units == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(unitsValidation);
                } else if (param.expectedValueType != null && !validateValues(param)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterExpectedValue);
                } else if (param.passCriteria == null || param.passCriteria == "" || param.passCriteria == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(enterPassCriteria);
                }
                return valid;
            }

            function validateValues(param) {
                var valid = true;
                if (param.expectedValueType == "TEXT" && (param.expectedValue.textValue == "" || param.expectedValue.textValue == null || param.expectedValue.textValue == undefined)) {
                    valid = false;
                } else if (param.expectedValueType == "INTEGER" && (param.expectedValue.integerValue == "" || param.expectedValue.integerValue == null || param.expectedValue.integerValue == undefined)) {
                    valid = false;
                } else if (param.expectedValueType == "DOUBLE" && (param.expectedValue.doubleValue == "" || param.expectedValue.doubleValue == null || param.expectedValue.doubleValue == undefined)) {
                    valid = false;
                } else if (param.expectedValueType == "DATE" && (param.expectedValue.dateValue == "" || param.expectedValue.dateValue == null || param.expectedValue.dateValue == undefined)) {
                    valid = false;
                } else if (param.expectedValueType == "BOOLEAN" && (param.expectedValue.booleanValue === "" || param.expectedValue.booleanValue === null || param.expectedValue.booleanValue === undefined)) {
                    valid = false;
                }

                return valid;
            }

            vm.editParams = editParams;
            function editParams(checklist) {
                checklist.editMode = true;
                checklist.newName = checklist.name;
                checklist.newDescription = checklist.description;
                checklist.newExpectedValueType = checklist.expectedValueType;
                checklist.newExpectedValue = checklist.expectedValue;
                checklist.newPassCriteria = checklist.passCriteria;
            }

            vm.deleteParams = deleteParams;
            function deleteParams(params) {
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                InspectionPlanService.deletePlanChecklistParams(vm.planId, vm.checklist.id, params.id).then(
                    function (data) {
                        vm.checklist.paramsCount--;
                        vm.checklistParameters.splice(vm.checklistParameters.indexOf(params
                        ), 1);
                        $rootScope.showSuccessMessage(paramsDeletedMessage);
                        if (vm.newParameters.length == 0) {
                            loadChecklistParams();
                        } else {
                            $rootScope.hideBusyIndicator();
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function adjustHeight() {
                $('#params-view').height($('#rightSidePanelContent').outerHeight());
            }

            function close() {
                $scope.callback();
                $rootScope.hideSidePanel();
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadChecklistParams();
                $rootScope.$on('app.checklists.params', close);
                //}
            })();
        }
    }
)
;