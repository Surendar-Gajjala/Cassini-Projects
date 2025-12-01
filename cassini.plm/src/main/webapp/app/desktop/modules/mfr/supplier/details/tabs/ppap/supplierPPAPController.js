define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/shared/services/core/supplierService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('SupplierPPAPController', SupplierPPAPController);

        function SupplierPPAPController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, DialogService, $translate, SupplierService) {
            var vm = this;

            vm.loading = true;
            vm.supplierId = $stateParams.supplierId;
            vm.supplierPPAPs = [];
            vm.showMfrPartsDetails = showMfrPartsDetails;
            vm.showPpapDetails = showPpapDetails;


            function loadSupplierPPAPs() {
                vm.loading = true;
                SupplierService.getSupplierPPAPs(vm.supplierId).then(
                    function (data) {
                        vm.supplierPPAPs = data;
                        vm.loading = false;
                    });
            }

            function showMfrPartsDetails(mfrpart) {
                $window.localStorage.setItem("shared-permission", $rootScope.sharedPermission);
                $state.go('app.mfr.mfrparts.details', {
                    mfrId: mfrpart.manufacturer,
                    manufacturePartId: mfrpart.id
                });
            }

            function showPpapDetails(ppap) {
                $window.localStorage.setItem("shared-permission", $rootScope.sharedPermission);
                $state.go('app.pqm.ppap.details', {
                    ppapId: ppap.id,
                    tab: 'details.basic'
                });
            }

            (function () {
                $scope.$on('app.supplier.tabActivated', function (event, data) {
                    if (data.tabId == 'details.ppap') {
                        loadSupplierPPAPs();
                    }
                });
            })();
        }
    }
);