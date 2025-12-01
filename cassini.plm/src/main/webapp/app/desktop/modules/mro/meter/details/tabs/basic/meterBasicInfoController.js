define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/meterService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MeterBasicInfoController', MeterBasicInfoController);

        function MeterBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                          MeterService, $translate,$i18n, LoginService) {
            var vm = this;
            vm.loading = true;
            vm.meterId = $stateParams.meterId;
            vm.meter = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateMeter = updateMeter;

            function loadMeterBasicDetails() {
                vm.loading = true;
                if (vm.meterId != null && vm.meterId != undefined) {
                    MeterService.getMeter(vm.meterId).then(
                        function (data) {
                            vm.meter = data;
                            $rootScope.meter = vm.meter;
                            $scope.name = vm.meter.name;
                            CommonService.getPersonReferences([vm.meter], 'modifiedBy');
                            CommonService.getPersonReferences([vm.meter], 'createdBy');
                            if (vm.meter.createdDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.meter.createDateDe = moment(vm.meter.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.meter.createDateDe = vm.meter.createdDate;
                                }

                            }
                            if (vm.meter.modifiedDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.meter.modifiedDateDe = moment(vm.meter.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.meter.modifiedDateDe = vm.meter.modifiedDate;
                                }
                            }
                            if (vm.meter.description != null && vm.meter.description != undefined) {
                                vm.meter.descriptionHtml = $sce.trustAsHtml(vm.meter.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            vm.loading = false;
                            vm.editMeterReading = false;
                            vm.editType = false;
                            $rootScope.viewInfo.title = $translate.instant("METER_DETAILS");
                            $rootScope.viewInfo.description = vm.meter.number + " , " + vm.meter.name;
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.selectTitle = $i18n.getValue("SELECT");
            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var meterManagerValidation = parsed.html($translate.instant("METER_MANAGER_VALIDATION")).html();
            var enterValidEmail = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var meterUpdated = parsed.html($translate.instant("METER_UPDATED")).html();


            function validateMeter() {
                var valid = true;
                if (vm.meter.name == null || vm.meter.name == ""
                    || vm.meter.name == undefined) {
                    valid = false;
                    vm.meter.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }


                return valid;
            }

            function updateMeter() {
                if (validateMeter()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    MeterService.updateMeter(vm.meter).then(
                        function (data) {
                            loadMeterBasicDetails();
                            vm.editMaintenance = false;
                            vm.editMeterReading = false;
                            vm.editType = false;
                            $rootScope.showSuccessMessage(meterUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            vm.editMeterReading = false;
            vm.meterReadings = [
                {label: "ABSOLUTE", value: "ABSOLUTE"},

                {label: "CHANGE", value: "CHANGE"}
            ]

            vm.changeMeterReading = changeMeterReading;
            function changeMeterReading() {
                vm.editMeterReading = true;
                vm.oldStatus = vm.meter.meterReadingType;
            }

            vm.cancelMeterReading = cancelMeterReading;
            function cancelMeterReading() {
                vm.editMeterReading = false;
                vm.meter.meterReadingType = vm.oldStatus;
            }

            vm.editType = false;
            vm.types = ["CONTINUOUS", "GUAGE"];

            vm.changeType = changeType;
            function changeType() {
                vm.editType = true;
                vm.oldType = vm.meter.meterType;
            }

            vm.cancelType = cancelType;
            function cancelType() {
                vm.editType = false;
                vm.meter.meterType = vm.oldType;
            }


            (function () {
                $scope.$on('app.meter.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadPersons();
                        loadMeterBasicDetails();
                    }
                });

            })();

        }
    }
);