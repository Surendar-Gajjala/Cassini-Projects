define(
    [
        'app/desktop/modules/procurement/procurement.module',
        'app/shared/services/core/procurementService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {

        module.controller('NewManufacturerController', NewManufacturerController);

        function NewManufacturerController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies,
                                           ProcurementService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            vm.checkMfrCode = checkMfrCode;
            vm.newManufacturer = {
                id: null,
                name: null,
                description: null,
                mfrCode: null,
                phoneNumber: null,
                email: null
            };

            function checkMfrCode() {
                ProcurementService.getManufacturerByCode(vm.newManufacturer.mfrCode).then(
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
                    ProcurementService.createManufacturer(vm.newManufacturer).then(
                        function (data) {
                            vm.newManufacturer = data;
                            vm.newManufacturer = {
                                id: null,
                                name: null,
                                description: null,
                                mfrCode: null,
                                phoneNumber: null,
                                email: null
                            };
                            $scope.callback();
                            $rootScope.hideSidePanel();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage("Manufacturer created successfully.")
                        }
                    );
                }
            }

            function validate() {
                var valid = true;

                if (vm.newManufacturer.name == null || vm.newManufacturer.name == "" || vm.newManufacturer.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Manufacturer Name");
                }
                /*else if (vm.newManufacturer.mfrCode == null || vm.newManufacturer.mfrCode == "" || vm.newManufacturer.mfrCode == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Manufacturer Code");
                 }*/
                return valid;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.procurement.manufacturer.new', create);
                }
            })();
        }
    }
)
;