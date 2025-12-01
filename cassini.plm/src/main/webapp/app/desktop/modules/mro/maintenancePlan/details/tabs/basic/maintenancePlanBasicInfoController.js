define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/maintenancePlanService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/shared/services/core/assetService'
    ],
    function (module) {
        module.controller('MaintenancePlanBasicInfoController', MaintenancePlanBasicInfoController);

        function MaintenancePlanBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                                    MaintenancePlanService, $translate, LoginService, AssetService) {
            var vm = this;
            vm.loading = true;
            vm.maintenancePlanId = $stateParams.maintenancePlanId;
            vm.maintenancePlan = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.showAsset = showAsset;
            vm.updateMaintenancePlan = updateMaintenancePlan;
            $rootScope.loadMaintenancePlanBasicDetails = loadMaintenancePlanBasicDetails;
            function loadMaintenancePlanBasicDetails() {
                vm.loading = true;
                if (vm.maintenancePlanId != null && vm.maintenancePlanId != undefined) {
                    MaintenancePlanService.getMaintenancePlan(vm.maintenancePlanId).then(
                        function (data) {
                            vm.maintenancePlan = data;
                            $rootScope.maintenancePlan = vm.maintenancePlan;
                            $scope.name = vm.maintenancePlan.name;
                            loadAsset();
                            CommonService.getPersonReferences([vm.maintenancePlan], 'modifiedBy');
                            CommonService.getPersonReferences([vm.maintenancePlan], 'createdBy');
                            if (vm.maintenancePlan.description != null && vm.maintenancePlan.description != undefined) {
                                vm.maintenancePlan.descriptionHtml = $sce.trustAsHtml(vm.maintenancePlan.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            vm.editStatus = false;
                            vm.loading = false;
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadAsset() {
                AssetService.getAsset(vm.maintenancePlan.asset).then(
                    function (data) {
                        vm.maintenancePlan.assetObject = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var maintenancePlanUpdated = parsed.html($translate.instant("M_PLAN_UPDATED_SUCCESS")).html();


            function validateMaintenancePlan() {
                var valid = true;
                if (vm.maintenancePlan.name == null || vm.maintenancePlan.name == "" || vm.maintenancePlan.name == undefined) {
                    valid = false;
                    vm.maintenancePlan.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }


                return valid;
            }

            function updateMaintenancePlan() {
                if (validateMaintenancePlan()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    MaintenancePlanService.updateMaintenancePlan(vm.maintenancePlan).then(
                        function (data) {
                            loadMaintenancePlanBasicDetails();
                            vm.editMaintenance = false;
                            vm.editStatus = false;
                            $rootScope.showSuccessMessage(maintenancePlanUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function showAsset(maintenancePlan) {
                $state.go('app.mro.asset.details', {assetId: maintenancePlan.asset, tab: 'details.basic'});
            }


            (function () {
                $scope.$on('app.maintenancePlan.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        $rootScope.loadMaintenancePlanBasicDetails();
                    }
                });

            })();

        }
    }
);