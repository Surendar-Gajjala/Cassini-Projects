define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/shiftService'


    ],
    function (module) {

        module.controller('NewShiftController', NewShiftController);

        function NewShiftController($scope, $q, $rootScope, $translate, $application, $timeout, $state, $stateParams, $cookies, $sce, CommonService,
                                    ShiftService, AutonumberService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            var numberValidation = parsed.html($translate.instant("NUMBER_VALIDATIONS")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var successMsg = parsed.html($translate.instant("SHIFT_CREATED_SUCCESS_MESSAGE")).html();
            var startAndTimeValidation = parsed.html($translate.instant("START_TIME_SMALLER_THAN_END_TIME_VALIDATION")).html();
            var startAndTimeSameValidation = parsed.html($translate.instant("START_TIME_END_TIME_SAME_VALIDATION")).html();
            var startTimeValidation = parsed.html($translate.instant("START_TIME_VALIDATION")).html();
            var endTimeValidation = parsed.html($translate.instant("END_TIME_VALIDATION")).html();
            vm.newShift = {
                number: null,
                name: null,
                description: null,
                localStartTime: null,
                localEndTime: null
            };

            function validateStartAndEndTime(startTime, endTime) {
                var valid = true;
                var timefrom = new Date();
                var fromTemp = startTime.split(":");
                timefrom.setHours((parseInt(fromTemp[0]) + 24) % 24);
                timefrom.setMinutes(parseInt(fromTemp[1]));

                var timeto = new Date();
                var toTemp = endTime.split(":");
                timeto.setHours((parseInt(toTemp[0]) + 24) % 24);
                timeto.setMinutes(parseInt(toTemp[1]));

                if (timeto < timefrom) {
                    valid = false;
                    $rootScope.showWarningMessage(startAndTimeValidation);

                }
                return valid;
            }


            function validate() {
                var valid = true;
                if (vm.newShift.number == null || vm.newShift.number == undefined ||
                    vm.newShift.number == "") {
                    $rootScope.showWarningMessage(numberValidation);
                    valid = false;
                }
                else if (vm.newShift.name == null || vm.newShift.name == undefined ||
                    vm.newShift.name == "") {
                    $rootScope.showWarningMessage(itemNameValidation);
                    valid = false;
                }

                else if (vm.newShift.localStartTime == null || vm.newShift.localStartTime == undefined ||
                    vm.newShift.localStartTime == "") {
                    $rootScope.showWarningMessage(startTimeValidation);
                    valid = false;
                }

                else if (vm.newShift.localEndTime == null || vm.newShift.localEndTime == undefined ||
                    vm.newShift.localEndTime == "") {
                    $rootScope.showWarningMessage(endTimeValidation);
                    valid = false;
                }
                else if ((vm.newShift.localStartTime != null && vm.newShift.localStartTime != "") && (vm.newShift.localEndTime != null && vm.newShift.localEndTime != "")) {
                    if (vm.newShift.localStartTime == vm.newShift.localEndTime) {
                        valid = false;
                        $rootScope.showWarningMessage(startAndTimeSameValidation);
                    }
                    /*else {
                     if (!validateStartAndEndTime(vm.newShift.localStartTime, vm.newShift.localEndTime)) {
                     valid = false;
                     }

                     }*/
                }


                return valid;
            }

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    ShiftService.createShift(vm.newShift).then(
                        function (data) {
                            vm.newShift = {
                                number: null,
                                name: null,
                                description: null,
                                localStartTime: null,
                                localEndTime: null
                            };
                            $scope.callback();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage(successMsg);
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.autoNumber = autoNumber;
            function autoNumber() {
                var preference = $application.defaultValuesPreferences.get("DEFAULT_SHIFT_NUMBER_SOURCE");
                if (preference != null && preference.defaultValueName != null) {
                    var name = preference.defaultValueName;
                    AutonumberService.getAutonumberName(name).then(
                        function (data) {
                            if (data.id != null && data.id != "") {
                                AutonumberService.getNextNumberByName(data.name).then(
                                    function (data) {
                                        vm.newShift.number = data;
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }


                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                autoNumber();
                $rootScope.$on('app.shift.new', create);
            })();
        }
    }
);