define(
    [
        'app/desktop/modules/item/item.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/inspectionPlanService'
    ],
    function (module) {

        module.controller('InspectionChecklistParamsController', InspectionChecklistParamsController);

        function InspectionChecklistParamsController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce,
                                                     InspectionService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var parameterSaved = parsed.html($translate.instant("PARAMETER_SAVED")).html();
            var enterActualValue = parsed.html($translate.instant("ENTER_ACTUAL_VALUE")).html();
            var enterValidActualValue = parsed.html($translate.instant("ENTER_VALID_ACTUAL_VALUE")).html();

            vm.checklist = $scope.data.checklistDetails;
            vm.inspectionId = $stateParams.inspectionId;
            vm.booleanValues = [true, false];
            vm.paramActualValues = [];
            function loadChecklistParams() {
                vm.loading = true;
                InspectionService.getInspectionChecklistParams(vm.inspectionId, vm.checklist.id).then(
                    function (data) {
                        vm.paramActualValues = data;
                        $timeout(function () {
                            adjustHeight();
                        }, 500)
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.onCancel = onCancel;
            function onCancel(param) {
                param.editMode = false;
                param.textValue = param.oldTextValue;
                param.integerValue = param.oldIntegerValue;
                param.doubleValue = param.oldDoubleValue;
                param.dateValue = param.oldDateValue;
                param.booleanValue = param.oldBooleanValue;
            }


            vm.editParams = editParams;
            function editParams(param) {
                param.editMode = true;
                param.oldTextValue = param.textValue;
                param.oldIntegerValue = param.integerValue;
                param.oldDoubleValue = param.doubleValue;
                param.oldDateValue = param.dateValue;
                param.oldBooleanValue = param.booleanValue;
            }

            vm.onOk = onOk;
            function onOk(params) {
                if (validateValues(params)) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    InspectionService.updateInspectionChecklistParams(vm.inspectionId, vm.checklist.id, params).then(
                        function (data) {
                            params.id = data.id;
                            params.seq = data.seq;
                            params.result = data.result;
                            params.editMode = false;
                            params.isNew = false;
                            $rootScope.loadInspectionChecklists();
                            $rootScope.loadInspectionDetails();
                            $rootScope.showSuccessMessage(parameterSaved);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validateValues(param) {
                var valid = true;
                if (param.param.expectedValueType == "TEXT" && (param.textValue == "" || param.textValue == null || param.textValue == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterActualValue);
                } else if (param.param.expectedValueType == "INTEGER" && (param.integerValue === "" || param.integerValue === null)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterActualValue);
                } else if (param.param.expectedValueType == "INTEGER" && (param.integerValue === undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterValidActualValue);
                } else if (param.param.expectedValueType == "DOUBLE" && (param.doubleValue == "" || param.doubleValue == null || param.doubleValue == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterActualValue);
                } else if (param.param.expectedValueType == "DATE" && (param.dateValue == "" || param.dateValue == null || param.dateValue == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterActualValue);
                } else if (param.param.expectedValueType == "BOOLEAN" && (param.booleanValue === "" || param.booleanValue === null || param.booleanValue === undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterActualValue);
                }

                return valid;
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
                $rootScope.$on('app.checklist.params', close);
                //}
            })();
        }
    }
)
;