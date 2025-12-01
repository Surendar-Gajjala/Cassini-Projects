define(
    [
        'app/desktop/modules/procurement/procurement.module',
        'app/shared/services/core/procurementService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {

        module.controller('NewSupplierController', NewSupplierController);

        function NewSupplierController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies,
                                       ProcurementService, CommonService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.newSupplier = {
                id: null,
                supplierName: null,
                supplierCode: null,
                contactPerson: null,
                phoneNumber: null,
                address: {
                    id: null,
                    addressType: CommonService.getAddressTypeByName("Office").id,
                    addressText: null,
                    district: null,
                    pincode: null,
                    city: null,
                    state: null,
                    country: CommonService.getCountryAndStatesMapByCountry("India").country.id
                },
                email: null
            };


            vm.states = CommonService.getCountryAndStatesMapByCountry("India").states;
            vm.checkSuppCode = checkSuppCode;
            function checkSuppCode() {
                ProcurementService.getSupplierByCode(vm.newSupplier.supplierCode).then(
                    function (data) {
                        if (data != undefined && data != null && data != "") {
                            $rootScope.showWarningMessage("Manufacturer code is already used, Please enter another code");
                        }
                    }
                )
            }

            function create() {
                $rootScope.closeNotification();
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    ProcurementService.createSupplier(vm.newSupplier).then(
                        function (data) {
                            vm.newSupplier = data;
                            vm.newSupplier = {
                                id: null,
                                supplierName: null,
                                supplierCode: null,
                                contactPerson: null,
                                phoneNumber: null,
                                address: {
                                    addressType: null,
                                    addressText: null,
                                    district: null,
                                    pincode: null,
                                    city: null,
                                    state: null,
                                    country: null
                                },
                                email: null
                            };
                            $scope.callback();
                            $rootScope.hideSidePanel();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage("Supplier created successfully.")
                        }
                    );
                }
            }

            function validate() {
                var valid = true;

                if (vm.newSupplier.supplierName == null || vm.newSupplier.supplierName == "" || vm.newSupplier.supplierName == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Supplier Name");
                } else if (vm.newSupplier.supplierCode == null || vm.newSupplier.supplierCode == "" || vm.newSupplier.supplierCode == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Supplier Code");
                } else if (vm.newSupplier.address.addressText == null || vm.newSupplier.address.addressText == "" || vm.newSupplier.address.addressText == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Address");
                } else if (vm.newSupplier.address.city == null || vm.newSupplier.address.city == "" || vm.newSupplier.address.city == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter City");
                } else if (vm.newSupplier.address.state == null || vm.newSupplier.address.state == "" || vm.newSupplier.address.state == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select State");
                } else if (vm.newSupplier.contactPerson == null || vm.newSupplier.contactPerson == "" || vm.newSupplier.contactPerson == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Name");
                }
                return valid;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.procurement.supplier.new', create);
                }
            })();
        }
    }
)
;