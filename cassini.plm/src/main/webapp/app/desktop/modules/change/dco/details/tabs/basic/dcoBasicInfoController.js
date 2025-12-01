define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/qualityTypeService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('DCOBasicInfoController', DCOBasicInfoController);

        function DCOBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, $application, ECOService, CommonService,
                                        DCOService, $translate, QualityTypeService) {
            var vm = this;

            vm.loading = true;
            vm.dcoId = $stateParams.dcoId;
            vm.dco = null;
            $scope.title = null;
            $scope.reasonForChange = null;

            var parsed = angular.element("<div></div>");

            /*-------------------  To get ECO Basic details  -----------------------*/

            function loadBasicDco() {
                DCOService.getDCO(vm.dcoId).then(
                    function (data) {
                        vm.dco = data;
                        $rootScope.dco = vm.dco;
                        $scope.title = vm.dco.title;
                        $scope.reasonForChange = vm.dco.reasonForChange;
                        // $rootScope.viewInfo.title = "DCO Details";
                        // $rootScope.viewInfo.description = "Number: {0}, Status: {1}".
                        //     format(vm.dco.dcoNumber, vm.dco.status);
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                        "{0}</div>".
                            format(vm.dco.dcoNumber);
                       $rootScope.viewInfo.description = vm.dco.title;
                        CommonService.getPersonReferences([vm.dco], 'changeAnalyst');
                        CommonService.getPersonReferences([vm.dco], 'createdBy');
                        CommonService.getPersonReferences([vm.dco], 'modifiedBy');
                        ECOService.getChangeTypeReferences([vm.dco], 'dcoType');
                        vm.loading = false;
                        vm.ecoStatus = false;
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 1000);
                        $scope.$evalAsync();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            var dcoUpdateMsg = parsed.html($translate.instant("DCO_UPDATE_MSG")).html();
            vm.updateDco = updateDco;
            function updateDco() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('.view-content'));
                    vm.dco.changeAnalyst = vm.dco.changeAnalystObject.id;
                    DCOService.updateDCO(vm.dco).then(
                        function (data) {
                            vm.dco.description = data.description;
                            vm.dco.title = data.title;
                            $scope.title = data.title;
                            $scope.reasonForChange = data.reasonForChange;
                            $rootScope.showSuccessMessage(dcoUpdateMsg);
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
                if (vm.dco.title == "" || vm.dco.title == null || vm.dco.title == undefined) {
                    valid = false;
                    vm.dco.title = $scope.title;
                    $rootScope.showWarningMessage(titleValidation);
                }
                else if (vm.dco.reasonForChange == "" || vm.dco.reasonForChange == null || vm.dco.reasonForChange == undefined) {
                    valid = false;
                    vm.dco.reasonForChange = $scope.reasonForChange;
                    $rootScope.showWarningMessage(reasonForChange);
                }

                return valid;
            }


            vm.changeAnalysts = [];
            function loadPersons() {
                var preference = $application.defaultValuesPreferences.get("DEFAULT_CHANGE_ANALYST_ROLE");
                if (preference != null && preference.defaultValueName != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.change.dco.all";
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
                $scope.$on('app.dco.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadBasicDco();
                    }
                });
                //if ($application.homeLoaded == true) {
                loadBasicDco();
                loadPersons();
                //}
            })();
        }
    }
);