define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/ecrService',
        'app/shared/services/core/qualityTypeService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/customerSupplierService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'
    ],
    function (module) {
        module.controller('ECRBasicInfoController', ECRBasicInfoController);

        function ECRBasicInfoController($scope, $rootScope, $sce, $timeout, $application, $state, $stateParams, $cookies, ECOService,
                                        CommonService, ECRService, $translate, QualityTypeService, CustomerSupplierService,
                                        LoginService, LovService) {
            var vm = this;

            vm.loading = true;
            vm.ecrId = $stateParams.ecrId;
            vm.ecr = null;

            var parsed = angular.element("<div></div>");
            var titleValidation = parsed.html($translate.instant("TITLE_VALIDATION")).html();
            var ecrUpdated = parsed.html($translate.instant("ECR_UPDATED")).html();

            /*-------------------  To get ECO Basic details  -----------------------*/

            $rootScope.loadBasicECR = loadBasicECR;
            function loadBasicECR() {
                ECRService.getECR(vm.ecrId).then(
                    function (data) {
                        vm.ecr = data;
                        $rootScope.ecr = data;
                        vm.copiedECR = angular.copy(vm.ecr);
                     //   $rootScope.viewInfo.title = "ECR Details";
                       // $rootScope.viewInfo.description = "Number: {0}, Status: {1}".
                            //format(vm.ecr.crNumber, vm.ecr.status);
                            $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div>".
                                format(vm.ecr.crNumber);
                        $rootScope.viewInfo.description = vm.ecr.title;
                        CommonService.getMultiplePersonReferences([vm.ecr], ['changeAnalyst', 'originator', 'createdBy', 'modifiedBy']);
                        CommonService.getPersonReferences([vm.ecr], 'requestedBy');
                        ECOService.getChangeTypeReferences([vm.ecr], 'crType');
                        if (vm.ecr.requesterType == "CUSTOMER" && vm.ecr.requestedBy != null) {
                            CustomerSupplierService.getCustomer(vm.ecr.requestedBy).then(
                                function (data) {
                                    vm.ecr.requestedByObject = data;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                        vm.loading = false;
                        loadPersons();
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

            vm.showQcr = showQcr;
            function showQcr(qcr) {
                $state.go('app.pqm.qcr.details', {qcrId: qcr.id})
            }

            vm.updateECR = updateECR;
            function updateECR() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('.view-content'));
                    vm.ecr.changeAnalyst = vm.ecr.changeAnalystObject.id;
                    vm.ecr.originator = vm.ecr.originatorObject.id;
                    ECRService.updateECR(vm.ecr).then(
                        function (data) {
                            loadBasicECR();
                            $rootScope.showSuccessMessage(ecrUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.ecr.title == "" || vm.ecr.title == null || vm.ecr.title == undefined) {
                    valid = false;
                    vm.ecr.title = vm.copiedECR.title;
                    $rootScope.showWarningMessage(titleValidation);
                }

                return valid;
            }

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
                if (preference != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.change.ecr.all";
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
                if (preference != null) {
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
                //if ($application.homeLoaded == true) {
                loadChangeRequestUrgency();
                loadBasicECR();
                //}
            })();
        }
    }
);