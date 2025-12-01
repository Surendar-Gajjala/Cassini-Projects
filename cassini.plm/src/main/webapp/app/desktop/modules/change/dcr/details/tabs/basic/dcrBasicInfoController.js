define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/dcrService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'
    ],
    function (module) {
        module.controller('DCRBasicInfoController', DCRBasicInfoController);

        function DCRBasicInfoController($scope, $rootScope, $sce, $timeout, $application, $state, $stateParams, $cookies, $translate,
                                        ECOService, CommonService, DCRService, QualityTypeService, LovService, LoginService) {
            var vm = this;

            vm.loading = true;
            vm.dcrId = $stateParams.dcrId;
            vm.dcr = null;
            $scope.title = null;
            $scope.reasonForChange = null;

            var parsed = angular.element("<div></div>");
            var dcrUpdated = parsed.html($translate.instant("DCR_UPDATED")).html();

            /*-------------------  To get ECO Basic details  -----------------------*/

            function loadBasicDCR() {
                DCRService.getDCR(vm.dcrId).then(
                    function (data) {
                        vm.dcr = data;
                        $rootScope.dcr = data;
                        $scope.title = vm.dcr.title;
                        $scope.reasonForChange = vm.dcr.reasonForChange;
                        vm.dcrStatus = vm.dcr.isApproved;
                        if (vm.dcr.statusType == 'REJECTED') {
                            vm.dcrStatus = true;
                        }
                        // $rootScope.viewInfo.title = "DCR Details";
                        // $rootScope.viewInfo.description = "Number: {0}, Status: {1}".
                        //     format(vm.dcr.crNumber, vm.dcr.status);
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                        "{0}</div>".
                            format(vm.dcr.crNumber);
                       $rootScope.viewInfo.description = vm.dcr.title;
                        CommonService.getPersonReferences([vm.dcr], 'changeAnalyst');
                        CommonService.getPersonReferences([vm.dcr], 'originator');
                        CommonService.getPersonReferences([vm.dcr], 'requestedBy');
                        CommonService.getPersonReferences([vm.dcr], 'createdBy');
                        CommonService.getPersonReferences([vm.dcr], 'modifiedBy');
                        ECOService.getChangeTypeReferences([vm.dcr], 'crType');
                        vm.loading = false;
                        vm.ecoStatus = false;
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 1000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.updateDcr = updateDcr;
            function updateDcr() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('.view-content'));
                    vm.dcr.changeAnalyst = vm.dcr.changeAnalystObject.id;
                    vm.dcr.originator = vm.dcr.originatorObject.id;
                    DCRService.updateDCR(vm.dcr).then(
                        function (data) {
                            vm.dcr.title = data.title;
                            vm.dcr.reasonForChange = data.reasonForChange;
                            vm.dcr.descriptionofChange = data.descriptionofChange;
                            $scope.title = data.title;
                            $scope.reasonForChange = data.reasonForChange;
                            $rootScope.showSuccessMessage(dcrUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            var titleValidation = parsed.html($translate.instant("TITLE_VALIDATION")).html();
            var reasonForChange = parsed.html($translate.instant("REASON_FOR_CHANGE_VALIDATION")).html();

            function validate() {
                var valid = true;
                if (vm.dcr.title == "" || vm.dcr.title == null || vm.dcr.title == undefined) {
                    valid = false;
                    vm.dcr.title = $scope.title;
                    $rootScope.showWarningMessage(titleValidation);
                }
                else if (vm.dcr.reasonForChange == "" || vm.dcr.reasonForChange == null || vm.dcr.reasonForChange == undefined) {
                    valid = false;
                    vm.dcr.reasonForChange = $scope.reasonForChange;
                    $rootScope.showWarningMessage(reasonForChange);
                }

                return valid;
            }


            vm.changeAnalysts = [];
            function loadPersons() {
                vm.persons = [];
                vm.originators = [];
                vm.changeAnalysts = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                                vm.originators.push(login.person);
                            }
                        });
                        vm.persons.push({id: 0, fullName: "Other"});
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
                var preference = $application.defaultValuesPreferences.get("DEFAULT_CHANGE_ANALYST_ROLE");
                if (preference != null && preference.defaultValueName != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.change.dcr.all";
                    QualityTypeService.getPersonByGroupNameAndPermission(groupName, permission).then(
                        function (data) {
                            vm.changeAnalysts = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadChangeRequestUrgency() {
                vm.urgencys = [];
                var preference = $application.defaultValuesPreferences.get("DEFAULT_CHANGE_REQUEST_URGENCY");
                if (preference != null && preference.defaultValueName != null) {
                    var name = preference.defaultValueName;
                    LovService.getLovByName(name).then(
                        function (data) {
                            if (data != null && data != "") {
                                vm.urgencys = data.values;
                            }
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }


            (function () {
                $scope.$on('app.dcr.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadBasicDCR();
                        loadPersons();
                    }
                });
                //if ($application.homeLoaded == true) {
                loadChangeRequestUrgency();
                loadBasicDCR();
                loadPersons();
                //}
            })();
        }
    }
);