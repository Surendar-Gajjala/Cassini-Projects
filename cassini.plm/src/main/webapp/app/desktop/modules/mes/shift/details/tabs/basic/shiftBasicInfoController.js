define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/shiftService',
    ],
    function (module) {
        module.controller('ShiftBasicInfoController', ShiftBasicInfoController);

        function ShiftBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $translate, $cookies, ShiftService, CommonService) {
            var vm = this;
            vm.updateShift = updateShift;
            vm.shiftId = $stateParams.shiftId;
            vm.shift = null;
            var parsed = angular.element("<div></div>");
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var updatedSuccessMsg = parsed.html($translate.instant("SHIFT_UPDATED_SUCCESS_MESSAGE")).html();
            var startAndTimeValidation = parsed.html($translate.instant("START_TIME_SMALLER_THAN_END_TIME_VALIDATION")).html();
            var startAndTimeSameValidation = parsed.html($translate.instant("START_TIME_END_TIME_SAME_VALIDATION")).html();
            var startTimeValidation = parsed.html($translate.instant("START_TIME_VALIDATION")).html();
            var endTimeValidation = parsed.html($translate.instant("END_TIME_VALIDATION")).html();
            $scope.number = null;
            $scope.name = null;
            $scope.startTime = null;
            $scope.endTime = null;
            $scope.localStartTime = null;
            $scope.localEndTime = null;
            vm.loading = true;
            function loadShiftBasicDetails() {
                vm.loading = true;
                if (vm.shiftId != null && vm.shiftId != undefined) {
                    ShiftService.getShift(vm.shiftId).then(
                        function (data) {
                            vm.shift = data;
                            $rootScope.shift = vm.shift;
                            vm.shift.editMode = false;
                            vm.shift.endEditMode = false;
                            $scope.number = vm.shift.number;
                            $scope.name = vm.shift.name;
                            $scope.startTime = vm.shift.startTime;
                            $scope.endTime = vm.shift.endTime;
                            $scope.localStartTime = vm.shift.localStartTime;
                            $scope.localEndTime = vm.shift.localEndTime;

                            CommonService.getPersonReferences([vm.shift], 'createdBy');
                            CommonService.getPersonReferences([vm.shift], 'modifiedBy');
                            if (vm.shift.createdDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.shift.createDateDe = moment(vm.shift.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.shift.createDateDe = vm.shift.createdDate;
                                }

                            }
                            if (vm.shift.modifiedDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.shift.modifiedDateDe = moment(vm.shift.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.shift.modifiedDateDe = vm.shift.modifiedDate;
                                }
                            }
                            if (vm.shift.description != null && vm.shift.description != undefined) {
                                vm.shift.descriptionHtml = $sce.trustAsHtml(vm.shift.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            $rootScope.viewInfo.title = $translate.instant("SHIFT_DETAILS");
                            $rootScope.viewInfo.description = vm.shift.number + " , " + vm.shift.name;
                            vm.loading = false;
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

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
                    /*vm.shift.startTime = $scope.startTime;
                     vm.shift.endTime = $scope.endTime;*/
                    loadShiftBasicDetails();
                    $rootScope.showWarningMessage(startAndTimeValidation);

                }
                return valid;
            }


            function validate() {
                var valid = true;
                if (vm.shift.number == null || vm.shift.number == undefined ||
                    vm.shift.number == "") {
                    vm.shift.number = $scope.number;

                    $rootScope.showWarningMessage(itemNumberValidation);
                    valid = false;
                }
                else if (vm.shift.name == null || vm.shift.name == undefined ||
                    vm.shift.name == "") {
                    vm.shift.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);
                    valid = false;
                }

                else if (vm.shift.localStartTime == null || vm.shift.localStartTime == undefined ||
                    vm.shift.localStartTime == "") {
                    vm.shift.startTime = $scope.startTime;
                    vm.shift.endTime = $scope.endTime;
                    $rootScope.showWarningMessage(startTimeValidation);
                    valid = false;
                }

                else if (vm.shift.localEndTime == null || vm.shift.localEndTime == undefined ||
                    vm.shift.localEndTime == "") {
                    vm.shift.startTime = $scope.startTime;
                    vm.shift.endTime = $scope.endTime;
                    $rootScope.showWarningMessage(endTimeValidation);
                    valid = false;
                }
                else if ((vm.shift.localStartTime != null && vm.shift.localStartTime != "") && (vm.shift.localEndTime != null && vm.shift.localEndTime != "")) {
                    if (vm.shift.localStartTime == vm.shift.localEndTime) {
                        valid = false;
                        vm.shift.startTime = $scope.startTime;
                        vm.shift.endTime = $scope.endTime;
                        $rootScope.showWarningMessage(startAndTimeSameValidation);
                    }
                   /* else {
                        if (!validateStartAndEndTime(vm.shift.localStartTime, vm.shift.localEndTime)) {
                            vm.shift.startTime = $scope.startTime;
                            vm.shift.endTime = $scope.endTime;
                            vm.shift.editMode = false;
                            vm.shift.endEditMode = false;
                            valid = false;
                        }

                    }*/
                }


                return valid;
            }

            function updateShift() {
                if (validate()) {
                    ShiftService.updateShift(vm.shift).then(
                        function (data) {
                            vm.shift = data;
                            /*$scope.number = vm.shift.number;
                             $scope.name = vm.shift.name;
                             $scope.startTime = vm.shift.startTime;
                             $scope.endTime = vm.shift.endTime;
                             $scope.localStartTime = vm.shift.localStartTime;
                             $scope.localEndTime = vm.shift.localEndTime;*/
                            loadShiftBasicDetails();
                            vm.shift.editMode = false;
                            vm.shift.endEditMode = false;
                            if (vm.shift.description != null && vm.shift.description != undefined) {
                                vm.shift.descriptionHtml = $sce.trustAsHtml(vm.shift.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }

                            CommonService.getPersonReferences([vm.shift], 'createdBy');
                            CommonService.getPersonReferences([vm.shift], 'modifiedBy');
                            if (vm.shift.createdDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.shift.createDateDe = moment(vm.shift.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.shift.createDateDe = vm.shift.createdDate;
                                }

                            }
                            if (vm.shift.modifiedDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.shift.modifiedDateDe = moment(vm.shift.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.shift.modifiedDateDe = vm.shift.modifiedDate;
                                }
                            }

                            $rootScope.viewInfo.description = vm.shift.number + " , " + vm.shift.name;
                            $rootScope.showSuccessMessage(updatedSuccessMsg);

                        }
                        , function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.changeStartTime = changeStartTime;
            vm.cancelStartTime = cancelStartTime;
            vm.changeEndTime = changeEndTime;
            vm.cancelEndTime = cancelEndTime;
            function changeStartTime() {
                vm.shift.editMode = true;
            }

            function cancelStartTime() {
                vm.shift.editMode = false;
                loadShiftBasicDetails();
            }

            function changeEndTime() {
                vm.shift.endEditMode = true;
            }

            function cancelEndTime() {
                vm.shift.endEditMode = false;
                loadShiftBasicDetails();
            }

            (function () {
                $scope.$on('app.shift.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadShiftBasicDetails();
                    }
                });

            })();

        }
    }
);