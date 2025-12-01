define(
    [
        'app/desktop/modules/procurement/procurement.module',
        'app/shared/services/core/procurementService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {

        module.controller('EditSupplierController', EditSupplierController);

        function EditSupplierController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies,
                                        ProcurementService, CommonService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.editSupplier = angular.copy($scope.data.supplierDetails);

            vm.states = CommonService.getCountryAndStatesMapByCountry("India").states;

            function update() {
                $rootScope.closeNotification();
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    ProcurementService.updateSupplier(vm.editSupplier).then(
                        function (data) {
                            vm.editSupplier = data;
                            $scope.callback();
                            $rootScope.hideSidePanel();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage("Supplier updated successfully.")
                        }
                    );
                }
            }

            function validate() {
                var valid = true;

                if (vm.editSupplier.supplierName == null || vm.editSupplier.supplierName == "" || vm.editSupplier.supplierName == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Supplier Name");
                } else if (vm.editSupplier.supplierCode == null || vm.editSupplier.supplierCode == "" || vm.editSupplier.supplierCode == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Supplier Code");
                } else if (vm.editSupplier.address.addressText == null || vm.editSupplier.address.addressText == "" || vm.editSupplier.address.addressText == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Address");
                } else if (vm.editSupplier.address.city == null || vm.editSupplier.address.city == "" || vm.editSupplier.address.city == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter City");
                } else if (vm.editSupplier.address.state == null || vm.editSupplier.address.state == "" || vm.editSupplier.address.state == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select State");
                } else if (vm.editSupplier.contactPerson == null || vm.editSupplier.contactPerson == "" || vm.editSupplier.contactPerson == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Name");
                }
                return valid;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.procurement.supplier.edit', update);
                }
            })();
        }
    }
)
;