define(
    [
        'app/desktop/modules/procurement/procurement.module',
        'app/shared/services/core/procurementService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {

        module.controller('EditManufacturerController', EditManufacturerController);

        function EditManufacturerController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies,
                                            ProcurementService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.editManufacturer = angular.copy($scope.data.manufacturerDetails);


            function create() {
                $rootScope.closeNotification();
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    ProcurementService.updateManufacturer(vm.editManufacturer).then(
                        function (data) {
                            vm.editManufacturer = data;
                            $scope.callback();
                            $rootScope.hideSidePanel();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage("Manufacturer updated successfully.")
                        }
                    );
                }
            }

            function validate() {
                var valid = true;

                if (vm.editManufacturer.name == null || vm.editManufacturer.name == "" || vm.editManufacturer.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Manufacturer Name");
                } else if (vm.editManufacturer.mfrCode == null || vm.editManufacturer.mfrCode == "" || vm.editManufacturer.mfrCode == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Manufacturer Code");
                }
                return valid;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.procurement.manufacturer.edit', create);
                }
            })();
        }
    }
)
;