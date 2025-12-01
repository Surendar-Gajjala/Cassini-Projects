define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/shared/services/core/mfrService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MfrBasicController', MfrBasicController);

        function MfrBasicController($scope, $rootScope, $timeout, $state, $sce, $stateParams, MfrService, $translate, CommonService) {

            $rootScope.viewInfo.icon = "fa fa-industry";
            var vm = this;
            vm.loading = true;
            vm.mfrId = $stateParams.manufacturerId;
            vm.manufacturer = null;
            vm.updateManufacture = updateManufacture;

            var parsed = angular.element("<div></div>");
            $rootScope.loadManufacturer = loadManufacturer;
            function loadManufacturer() {
                MfrService.getManufacturer(vm.mfrId).then(
                    function (data) {
                        vm.manufacturer = data;
                        vm.loading = false;
                        CommonService.getMultiplePersonReferences([vm.manufacturer], ['createdBy', 'modifiedBy']);
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 1000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var mfrUpdateItem = $translate.instant("MANUFACTURER_UPDATE_MFR_MESSAGE");
            var nameValidation = $translate.instant("NAME_CANNOT_BE_EMPTY");

            function updateManufacture() {
                if (validate()) {
                    MfrService.updateManufacture(vm.manufacturer).then(
                        function (data) {
                            $rootScope.viewInfo.description = "Name : " + data.name;
                            $rootScope.showSuccessMessage(mfrUpdateItem);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.manufacturer.name == null || vm.manufacturer.name == ""
                    || vm.manufacturer.name == undefined) {
                    valid = false;
                    vm.manufacturer.name = $rootScope.mfr.name;
                    $rootScope.showWarningMessage(nameValidation);

                }
                return valid;
            }

            (function () {
                loadManufacturer();
                $scope.$on('app.Manufacturer.update', function () {
                    updateManufacture();
                })
            })();
        }
    }
);