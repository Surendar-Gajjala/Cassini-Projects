define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/maintenancePlanService',
        'app/shared/services/core/assetService'

    ],
    function (module) {

        module.controller('NewMaintenancePlanController', NewMaintenancePlanController);

        function NewMaintenancePlanController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, $application,
                                              MaintenancePlanService, AutonumberService, AssetService, ProjectService) {

            var vm = this;

            var parsed = angular.element("<div></div>");

            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var pleaseSelectAsset = parsed.html($translate.instant("PLEASE_SELECT_ASSET")).html();
            var pleaseSelectAssetType = parsed.html($translate.instant("PLEASE_SELECT_ASSET_TYPE")).html();
            $scope.selectAsset = parsed.html($translate.instant("SELECT_ASSET")).html();

            vm.newMaintenancePlan = {
                id: null,
                number: null,
                name: null,
                description: null,
                asset: null,
                workflowDefinition: null
            };

            vm.planAutoNumber = null;
            vm.autoNumber = autoNumber;
            vm.loadAutoNumberByName = loadAutoNumberByName;
            function loadAutoNumberByName() {
                var preference = $application.defaultValuesPreferences.get("DEFAULT_MAINTENANCE_PLAN_NUMBER_SOURCE");
                if (preference != null && preference.defaultValueName != null) {
                    var name = preference.defaultValueName;
                    AutonumberService.getAutonumberName(name).then(
                        function (data) {
                            vm.planAutoNumber = data;
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function autoNumber() {
                if (vm.planAutoNumber != null && vm.planAutoNumber != "" && vm.planAutoNumber != undefined) {
                    AutonumberService.getNextNumberByName(vm.planAutoNumber.name).then(
                        function (data) {
                            vm.newMaintenancePlan.number = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.assetType == null || vm.assetType == undefined || vm.newMaintenancePlan.asset == "") {
                    $rootScope.showWarningMessage(pleaseSelectAssetType);
                    valid = false;
                } else if (vm.newMaintenancePlan.asset == null || vm.newMaintenancePlan.asset == undefined || vm.newMaintenancePlan.asset == "") {
                    $rootScope.showWarningMessage(pleaseSelectAsset);
                    valid = false;
                }
                else if (vm.newMaintenancePlan.number == null || vm.newMaintenancePlan.number == undefined || vm.newMaintenancePlan.number == "") {
                    $rootScope.showWarningMessage(itemNumberValidation);
                    valid = false;
                }
                else if (vm.newMaintenancePlan.name == null || vm.newMaintenancePlan.name == undefined || vm.newMaintenancePlan.name == "") {
                    $rootScope.showWarningMessage(itemNameValidation);
                    valid = false;
                }


                return valid;
            }


            var insertedSuccefully = parsed.html($translate.instant("M_PLAN_CREATED_SUCCESS")).html();

            function create() {
                var dfd = $q.defer();
                vm.validattributes = [];
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    MaintenancePlanService.createMaintenancePlan(vm.newMaintenancePlan).then(
                        function (data) {
                            vm.newMaintenancePlan = data;
                            $scope.callback();
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel();
                            $rootScope.showSuccessMessage(insertedSuccefully);
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

                return dfd.promise;
            }

            vm.wfs = [];
            function loadWorkflows() {
                vm.wfs = [];
                ProjectService.getWorkflows('MROMAINTENANCEPLAN').then(
                    function (data) {
                        vm.wfs = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.onSelectAssetType = onSelectAssetType;
            function onSelectAssetType(assetType) {
                vm.assetType = assetType;
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                AssetService.getAssetsByType(assetType.id).then(
                    function (data) {
                        vm.assets = data;
                        autoNumber();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                $rootScope.hideBusyIndicator();
                loadWorkflows();
                loadAutoNumberByName();
                $rootScope.$on('app.maintenancePlan.new', create);
            })();
        }
    }
);