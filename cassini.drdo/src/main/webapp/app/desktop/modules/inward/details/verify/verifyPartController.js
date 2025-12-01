define(
    [
        'app/desktop/modules/inward/inward.module',
        'app/shared/services/core/inwardService'
    ],
    function (module) {
        module.controller('VerifyPartController', VerifyPartController);

        function VerifyPartController($scope, $stateParams, $rootScope, $timeout, $interval, $state, $cookies, InwardService) {

            var vm = this;

            var inwardId = $stateParams.inwardId;
            vm.inwardItemInstance = $scope.data.verifyInwardItemInstance;

            vm.upnNumber = null;
            vm.storageName = null;

            function verifyItem() {
                if (validateItem()) {
                    InwardService.verifyStoragePart(inwardId, vm.inwardItemInstance.id, vm.storageName, vm.upnNumber).then(
                        function (data) {
                            $scope.callback(data);
                            vm.inwardItemInstance = null;
                            vm.upnNumber = null;
                            $rootScope.hideSidePanel();
                            $rootScope.showSuccessMessage("Part verified successfully");
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function validateItem() {
                var valid = true;
                if (vm.upnNumber == "" || vm.upnNumber == null || vm.upnNumber == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please scan part UPN Barcode");
                } else if (vm.storageName == "" || vm.storageName == null || vm.storageName == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please scan Storage Barcode");
                }
                return valid;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.inward.itemInstance.verify', verifyItem);
                }
            })();
        }
    }
);