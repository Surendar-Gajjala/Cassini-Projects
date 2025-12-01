define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/qualityTypeService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'

    ],
    function (module) {
        module.controller('ECOBasicInfoController', ECOBasicInfoController);

        function ECOBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $application, $stateParams, $cookies, ECOService,
                                        CommonService, $translate, QualityTypeService) {
            var vm = this;

            vm.loading = true;
            vm.ecoId = $stateParams.ecoId;
            vm.eco = null;
            var ecoTitle = null;
            $scope.title = null;
            $scope.reasonForChange = null;
            $rootScope.loadBasicEco = loadBasicEco;
            var parsed = angular.element("<div></div>");

            /*-------------------  To get ECO Basic details  -----------------------*/

            function loadBasicEco() {
                ECOService.getECO(vm.ecoId).then(
                    function (data) {
                        vm.eco = data;
                        $scope.title = vm.eco.title;
                        $scope.reasonForChange = vm.eco.reasonForChange;
                        $rootScope.eco = data;
                        if (vm.eco.createdDate) {
                            vm.eco.createdDatede = moment(vm.eco.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                        }
                        if (vm.eco.modifiedDate) {
                            vm.eco.modifiedDatede = moment(vm.eco.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                        }
                        $rootScope.ecoStatus = vm.eco.released;
                        if (vm.eco.statusType == 'REJECTED') {
                            $rootScope.ecoStatus = true;
                        }
                        // $rootScope.viewInfo.title = "ECO Details";
                        /* $rootScope.viewInfo.description = "Number: {0}, Status: <span class='label {1}'>{2}</span>".
                         format(vm.eco.ecoNumber, getStatusLabelClass(), vm.eco.status);*/
                        // $rootScope.viewInfo.description = "Number: {0}, Status: {1}".
                        //     format(vm.eco.ecoNumber, vm.eco.status)
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                         "{0}</div>".
                             format(vm.eco.ecoNumber);
                        $rootScope.viewInfo.description = vm.eco.title;
                        CommonService.getPersonReferences([vm.eco], 'ecoOwner');
                        CommonService.getPersonReferences([vm.eco], 'createdBy');
                        CommonService.getPersonReferences([vm.eco], 'modifiedBy');
                        ECOService.getChangeTypeReferences([vm.eco], 'ecoType');
                        vm.loading = false;
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 1000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            var itemNameValidation = parsed.html($translate.instant("TITLE_VALIDATION")).html();
            var plantManagerValidation = parsed.html($translate.instant("PLANT_MANAGER_VALIDATION")).html();
            var reasonForChangeValidation = parsed.html($translate.instant("REASON_FOR_CHANGE_VALIDATION")).html();

            function validateEco() {
                var valid = true;
                if (vm.eco.title == null || vm.eco.title == ""
                    || vm.eco.title == undefined) {
                    valid = false;
                    vm.eco.title = $scope.title;
                    $rootScope.showWarningMessage(itemNameValidation);

                }
                else if (vm.eco.reasonForChange == null || vm.eco.reasonForChange == ""
                    || vm.eco.reasonForChange == undefined) {
                    valid = false;
                    vm.eco.reasonForChange = $scope.reasonForChange;
                    $rootScope.showWarningMessage(reasonForChangeValidation);

                }

                return valid;
            }


            var ecoUpdateMessage = parsed.html($translate.instant("ECO_UPDATE_MESSAGE")).html();

            vm.updateEco = updateEco;
            function updateEco() {
                if (validateEco()) {
                    $rootScope.showBusyIndicator($('.view-content'));
                    vm.eco.ecoOwner = vm.eco.ecoOwnerObject.id;
                    ECOService.updateECO(vm.eco).then(
                        function (data) {
                            $scope.title = data.title;
                            $scope.reasonForChange = data.reasonForChange;
                            vm.eco.description = data.description;
                            $rootScope.showSuccessMessage(ecoUpdateMessage);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

            }

            function loadPersons() {
                vm.changeAnalysts = [];
                var preference = $application.defaultValuesPreferences.get("DEFAULT_CHANGE_ANALYST_ROLE");
                if (preference != null && preference.defaultValueName != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.change.eco.all";
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
                loadBasicEco();
                loadPersons();
                /*$scope.$on('app.eco.tabactivated', function (event, data) {
                 if (data.tabId == 'details.basic') {
                 loadBasicEco();
                 loadPersons();
                 }
                 });*/
            })();
        }
    }
);